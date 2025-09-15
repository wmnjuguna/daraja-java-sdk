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

### Milestone 1: Foundation & Authentication (Completed)

**Objective**: Establish the core project structure and implement a robust, automatic authentication mechanism.

### Milestone 2: M-Pesa Express (STK Push) (Completed)

**Objective**: Implement the full flow for initiating an STK Push payment request.

### Milestone 3: Customer to Business (C2B) (Completed)

**Objective**: Enable registration of C2B callback URLs and provide models for handling C2B payments.

### Milestone 4: Business to Customer (B2C) (Completed)

**Objective**: Implement the full flow for initiating B2C payments.

### Milestone 5: Business to Business (B2B) (Completed)

**Objective**: Implement the full flow for initiating B2B payments.

### Milestone 6: Transaction Status (Completed)

**Objective**: Implement the API for querying the status of a transaction.

### Milestone 7: Account Balance (Completed)

**Objective**: Implement the API for querying a shortcode's account balance.

### Milestone 8: Reversal (Completed)

**Objective**: Implement the API for reversing a transaction.

## 4. Final Packaging and Distribution
- **Error Handling**: Implement a custom Feign ErrorDecoder to translate HTTP errors (like 400, 401, 500) into specific, meaningful Java exceptions (e.g., InvalidDarajaRequestException, DarajaAuthenticationException)
- **Logging**: Ensure all critical steps (token refresh, API calls, errors) are logged appropriately using SLF4J at different levels (INFO, DEBUG, ERROR)
- **Build & Deploy**: Configure the build.gradle file to build the JAR and publish it to a repository like Maven Central
- **Final Documentation**: Create a detailed README.md file with installation instructions and clear usage examples for every feature