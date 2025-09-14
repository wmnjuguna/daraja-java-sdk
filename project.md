# Daraja-Feign SDK

**A Java Integration Library for Safaricom Daraja APIs using OpenFeign**

- **Version**: 1.0
- **Author**: Wilfred Njuguna
- **Date**: September 14, 2025

## 1. Overview

### 1.1. Goal

The primary goal of this project is to create a lightweight, intuitive, and robust Java SDK that simplifies integration with the Safaricom Daraja API. By leveraging the OpenFeign library, we will provide a declarative, interface-based client that abstracts away the complexities of HTTP requests, authentication, and JSON data mapping.

### 1.2. Core Principles

- **Simplicity**: Developers should be able to integrate with Daraja APIs using simple Java method calls without writing boilerplate HTTP client code.
- **Reliability**: The library must be robust, handling API errors, authentication lifecycle, and network issues gracefully.
- **Type-Safety**: All API requests and responses will be mapped to strongly-typed Java objects (DTOs) to reduce runtime errors.
- **Maintainability**: The codebase will be modular and well-documented, making it easy to update as the Daraja API evolves.

### 1.3. Core Technologies & Libraries

- **Java Version**: Java 17
- **HTTP Client**: OpenFeign (for creating declarative REST clients)
- **JSON Processing**: Jackson (for serializing and deserializing API request/response bodies)
- **Logging**: SLF4J (as a logging facade to allow the host application to use its preferred logging implementation)
- **Build Tool**: Gradle

### 1.4. Disclaimer

This SDK is a wrapper designed to simplify the use of the Safaricom Daraja API. While we strive to keep it up-to-date, the official Safaricom Daraja API documentation remains the single source of truth. Developers should always consult the official portal for the most current API specifications, parameter names, and endpoint behaviors.

## 2. Architectural Design

The SDK will be built around a central `DarajaClientFactory` which will be responsible for constructing and configuring the necessary Feign clients. This design decouples the client's construction from its usage.

### Key Components:

- **DarajaEnvironment**: An Enum to easily switch between SANDBOX and PRODUCTION base URLs
- **DarajaAuthClient**: A private, internal Feign client dedicated solely to fetching the OAuth2 access token
- **DarajaAuthInterceptor**: A Feign RequestInterceptor that automatically and transparently handles the entire authentication lifecycle
- **DarajaApiClient**: The main public Feign client interface that developers will use
- **Data Transfer Objects (DTOs)**: A comprehensive set of Java Records representing every JSON object used in the Daraja API. All field names will use Jackson's `@JsonProperty` annotation to map directly to the API's naming convention (e.g., BusinessShortCode)

## 3. Development Milestones

### Milestone 1: Foundation & Authentication

**Objective**: Establish the core project structure and implement a robust, automatic authentication mechanism.

**Tasks**:

#### Project Setup (build.gradle):

```gradle
plugins {
    id 'java-library'
}

group = 'com.yourdomain' // Change to your group id
version = '1.0.0'

repositories {
    mavenCentral()
}

dependencies {
    // OpenFeign for declarative REST clients
    api 'io.github.openfeign:feign-core:13.1'
    api 'io.github.openfeign:feign-jackson:13.1'

    // Jackson for JSON processing
    api 'com.fasterxml.jackson.core:jackson-databind:2.15.2'

    // Logging Facade
    api 'org.slf4j:slf4j-api:2.0.7'
}
```

#### DarajaEnvironment Enum:

```javapublic enum DarajaEnvironment {
    SANDBOX("https://sandbox.safaricom.co.ke"),
    PRODUCTION("https://api.safaricom.co.ke");

    private final String baseUrl;

    DarajaEnvironment(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
```

#### Authentication DTO:

```javaimport com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponse(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("expires_in") String expiresIn
) {}
```

#### DarajaAuthClient Interface:

```javaimport feign.Headers;
import feign.Param;
import feign.RequestLine;

interface DarajaAuthClient {
    @RequestLine("GET /oauth/v1/generate?grant_type=client_credentials")
    @Headers("Authorization: {authHeader}")
    AuthResponse getAccessToken(@Param("authHeader") String authHeader);
}
```

#### DarajaAuthInterceptor Implementation:

```javaimport feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Base64;
import java.util.Objects;

public class DarajaAuthInterceptor implements RequestInterceptor {
    // Implementation details will include logic to cache and refresh token
    @Override
    public void apply(RequestTemplate template) {
        // Logic goes here
    }
}
```

### Milestone 2: M-Pesa Express (STK Push)**Objective**: Implement the full flow for initiating an STK Push payment request.

**Tasks**:

#### DTOs for STK Push (as Java Records):
- `StkPushRequest.java`
- `StkPushResponse.java`
- `StkPushCallback.java` (with all nested records)

#### Update DarajaApiClient Interface:

```javapublic interface DarajaApiClient {
    @RequestLine("POST /mpesa/stkpush/v1/processrequest")
    @Headers("Content-Type: application/json")
    StkPushResponse initiateStkPush(StkPushRequest request);
}
```

#### Password Generation Utility:Create a static helper method to generate the Timestamp and Base64 encoded Password (BusinessShortCode + Passkey + Timestamp).

#### Documentation & Developer Responsibility:
The README must explain that the developer is responsible for creating a public CallBackURL endpoint in their application. The SDK provides the StkPushCallback record to simplify parsing the incoming request from Safaricom.

### Milestone 3: Customer to Business (C2B)**Objective**: Enable registration of C2B callback URLs and provide models for handling C2B payments.

**Tasks**:

#### DTOs for C2B (as Java Records):
- `C2BRegisterUrlRequest.java`
- `C2BRegisterUrlResponse.java`
- `C2BValidationCallback.java`
- `C2BConfirmationCallback.java`

#### Update DarajaApiClient Interface:

```javapublic interface DarajaApiClient {
    // ... other methods
    @RequestLine("POST /mpesa/c2b/v1/registerurl")
    @Headers("Content-Type: application/json")
    C2BRegisterUrlResponse registerC2BUrls(C2BRegisterUrlRequest request);
}
```

#### Documentation & Developer Responsibility:Clearly state that the host application is responsible for implementing the two required endpoints (ValidationURL and ConfirmationURL). The SDK provides the necessary DTOs to handle these inbound calls.

### Milestone 4: Business to Customer (B2C)**Objective**: Implement the full flow for initiating B2C payments.

**Tasks**:

#### DTOs for B2C (as Java Records):
- `B2CRequest.java`
- `B2CResponse.java`
- `B2CCallback.java`

#### Update DarajaApiClient Interface:
Add the method for initiating a B2C transaction.

#### Documentation:
Clarify the roles of the QueueTimeOutURL and ResultURL.

### Milestone 5: Transaction Status**Objective**: Implement the API for querying the status of a transaction.

**Tasks**:

#### DTOs for Transaction Status (as Java Records):
- `TransactionStatusRequest.java`
- `TransactionStatusResponse.java`
- `TransactionStatusCallback.java`

#### Update DarajaApiClient Interface:
Add the method for querying transaction status.

### Milestone 6: Account Balance**Objective**: Implement the API for querying a shortcode's account balance.

**Tasks**:

#### DTOs for Account Balance (as Java Records):
- `AccountBalanceRequest.java`
- `AccountBalanceResponse.java`
- `AccountBalanceCallback.java`

#### Update DarajaApiClient Interface:
Add the method for checking account balance.

## 4. Final Packaging and Distribution- **Error Handling**: Implement a custom Feign ErrorDecoder to translate HTTP errors (like 400, 401, 500) into specific, meaningful Java exceptions (e.g., InvalidDarajaRequestException, DarajaAuthenticationException)
- **Logging**: Ensure all critical steps (token refresh, API calls, errors) are logged appropriately using SLF4J at different levels (INFO, DEBUG, ERROR)
- **Build & Deploy**: Configure the build.gradle file to build the JAR and publish it to a repository like Maven Central
- **Final Documentation**: Create a detailed README.md file with installation instructions and clear usage examples for every feature