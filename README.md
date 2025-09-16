# Daraja Java SDK

[![Build Status](https://travis-ci.org/your-username/daraja-java-sdk.svg?branch=main)](https://travis-ci.org/your-username/daraja-java-sdk)

A lightweight, intuitive, and robust Java SDK for integrating with the Safaricom Daraja API. This SDK simplifies the complexities of HTTP requests, authentication, and JSON data mapping, allowing you to focus on your application's core logic.

## Features

-   **Authentication**: Automatic and transparent handling of the OAuth2 authentication lifecycle.
-   **STK Push**: Initiate M-Pesa Express (STK Push) payment requests.
-   **C2B**: Register Customer to Business (C2B) callback URLs.
-   **B2C**: Initiate Business to Customer (B2C) payments.
-   **B2B**: Initiate Business to Business (B2B) payments.
-   **Transaction Status**: Query the status of a transaction.
-   **Account Balance**: Query the balance of an M-Pesa account.
-   **Reversal**: Reverse a transaction.
-   **Error Handling**: Meaningful Java exceptions for API errors.
-   **Framework Agnostic**: Works with Spring Boot, Quarkus, or plain Java applications.
-   **Flexible Configuration**: Support for properties files, environment variables, or database configuration.

## Table of Contents

- [Installation](#installation)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
  - [Properties-Based Configuration](#properties-based-configuration)
  - [Database-Based Configuration](#database-based-configuration)
  - [Environment Variables](#environment-variables)
- [API Operations](#api-operations)
- [Error Handling](#error-handling)
- [Best Practices](#best-practices)

## Installation

### Gradle

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.wmnjuguna:daraja-java-sdk:1.0.0'
}
```

### Maven

```xml
<dependency>
    <groupId>io.github.wmnjuguna</groupId>
    <artifactId>daraja-java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick Start

### Basic Usage (Plain Java)

```java
import io.github.wmnjuguna.DarajaClientFactory;
import io.github.wmnjuguna.DarajaApiClient;
import io.github.wmnjuguna.DarajaEnvironment;

public class PaymentExample {
    public static void main(String[] args) {
        // Create client factory
        DarajaClientFactory factory = new DarajaClientFactory(
            DarajaEnvironment.SANDBOX,
            System.getenv("DARAJA_CONSUMER_KEY"),
            System.getenv("DARAJA_CONSUMER_SECRET")
        );

        // Create API client
        DarajaApiClient client = factory.createApiClient();

        // Ready to make API calls
    }
}
```

## Configuration

The SDK is designed to be framework-agnostic and supports multiple configuration approaches. Choose the one that best fits your project setup.

### Properties-Based Configuration

#### Spring Boot with @ConfigurationProperties (Recommended)

**1. Create Configuration Properties Class:**

```java
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "daraja")
public class DarajaProperties {

    private String environment = "SANDBOX";
    private Consumer consumer = new Consumer();
    private Timeouts timeouts = new Timeouts();

    // Getters and setters
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public Consumer getConsumer() { return consumer; }
    public void setConsumer(Consumer consumer) { this.consumer = consumer; }

    public Timeouts getTimeouts() { return timeouts; }
    public void setTimeouts(Timeouts timeouts) { this.timeouts = timeouts; }

    public static class Consumer {
        private String key;
        private String secret;

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }

        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
    }

    public static class Timeouts {
        private int connect = 30000;
        private int read = 60000;

        public int getConnect() { return connect; }
        public void setConnect(int connect) { this.connect = connect; }

        public int getRead() { return read; }
        public void setRead(int read) { this.read = read; }
    }
}
```

**2. Configuration Class:**

```java
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.wmnjuguna.DarajaClientFactory;
import io.github.wmnjuguna.DarajaApiClient;
import io.github.wmnjuguna.DarajaEnvironment;

@Configuration
@EnableConfigurationProperties(DarajaProperties.class)
public class DarajaConfiguration {

    @Bean
    public DarajaApiClient darajaApiClient(DarajaProperties properties) {
        DarajaClientFactory factory = new DarajaClientFactory(
            DarajaEnvironment.valueOf(properties.getEnvironment()),
            properties.getConsumer().getKey(),
            properties.getConsumer().getSecret()
        );

        return factory.createApiClient();
    }
}
```

**3. Application Properties (application.yml):**

```yaml
daraja:
  environment: SANDBOX  # or PRODUCTION
  consumer:
    key: ${DARAJA_CONSUMER_KEY:your-consumer-key}
    secret: ${DARAJA_CONSUMER_SECRET:your-consumer-secret}
  timeouts:
    connect: 30000
    read: 60000
```

#### Spring Boot with @Value Annotations

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DarajaConfiguration {

    @Value("${daraja.environment:SANDBOX}")
    private String environment;

    @Value("${daraja.consumer.key}")
    private String consumerKey;

    @Value("${daraja.consumer.secret}")
    private String consumerSecret;

    @Bean
    public DarajaApiClient darajaApiClient() {
        DarajaClientFactory factory = new DarajaClientFactory(
            DarajaEnvironment.valueOf(environment),
            consumerKey,
            consumerSecret
        );

        return factory.createApiClient();
    }
}
```

### Database-Based Configuration

For applications that need to store configuration in the database:

**1. Database Entity:**

```java
import jakarta.persistence.*;

@Entity
@Table(name = "daraja_configuration")
public class DarajaConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "environment", nullable = false)
    private String environment;

    @Column(name = "consumer_key", nullable = false)
    private String consumerKey;

    @Column(name = "consumer_secret", nullable = false)
    private String consumerSecret;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors, getters, and setters
    public DarajaConfig() {}

    public DarajaConfig(String environment, String consumerKey, String consumerSecret) {
        this.environment = environment;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and setters
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public String getConsumerKey() { return consumerKey; }
    public void setConsumerKey(String consumerKey) { this.consumerKey = consumerKey; }

    public String getConsumerSecret() { return consumerSecret; }
    public void setConsumerSecret(String consumerSecret) { this.consumerSecret = consumerSecret; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
```

**2. Repository Interface:**

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DarajaConfigRepository extends JpaRepository<DarajaConfig, Long> {

    @Query("SELECT c FROM DarajaConfig c WHERE c.active = true")
    DarajaConfig findActiveConfiguration();

    DarajaConfig findByEnvironmentAndActiveTrue(String environment);
}
```

**3. Configuration Service:**

```java
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DarajaConfigurationService {

    @Autowired
    private DarajaConfigRepository configRepository;

    public DarajaApiClient createDarajaClient() {
        DarajaConfig config = loadConfiguration();

        DarajaClientFactory factory = new DarajaClientFactory(
            DarajaEnvironment.valueOf(config.getEnvironment()),
            config.getConsumerKey(),
            config.getConsumerSecret()
        );

        return factory.createApiClient();
    }

    private DarajaConfig loadConfiguration() {
        DarajaConfig config = configRepository.findActiveConfiguration();
        if (config == null) {
            throw new IllegalStateException("No active Daraja configuration found in database");
        }
        return config;
    }
}
```

**4. Database Schema:**

```sql
CREATE TABLE daraja_configuration (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    environment VARCHAR(20) NOT NULL,
    consumer_key VARCHAR(255) NOT NULL,
    consumer_secret VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample configuration
INSERT INTO daraja_configuration (environment, consumer_key, consumer_secret)
VALUES ('SANDBOX', 'your-sandbox-key', 'your-sandbox-secret');
```

### Environment Variables

Set these environment variables for any configuration approach:

```bash
export DARAJA_ENVIRONMENT=SANDBOX
export DARAJA_CONSUMER_KEY=your-consumer-key
export DARAJA_CONSUMER_SECRET=your-consumer-secret
```

### Fallback Configuration Strategy

Combine database and properties for robust configuration:

```java
@Configuration
public class DarajaConfiguration {

    @Autowired
    private DarajaConfigRepository configRepository;

    @Value("${daraja.environment:SANDBOX}")
    private String fallbackEnvironment;

    @Value("${daraja.consumer.key:}")
    private String fallbackConsumerKey;

    @Value("${daraja.consumer.secret:}")
    private String fallbackConsumerSecret;

    @Bean
    public DarajaApiClient darajaApiClient() {
        // Try database first, fall back to properties
        DarajaConfig config = configRepository.findActiveConfiguration();

        String environment = (config != null) ? config.getEnvironment() : fallbackEnvironment;
        String consumerKey = (config != null) ? config.getConsumerKey() : fallbackConsumerKey;
        String consumerSecret = (config != null) ? config.getConsumerSecret() : fallbackConsumerSecret;

        if (consumerKey.isEmpty() || consumerSecret.isEmpty()) {
            throw new IllegalStateException("Daraja configuration not found in database or properties");
        }

        DarajaClientFactory factory = new DarajaClientFactory(
            DarajaEnvironment.valueOf(environment),
            consumerKey,
            consumerSecret
        );

        return factory.createApiClient();
    }
}
```

## API Operations

The SDK provides methods for all major Daraja API operations. Here are detailed examples for each:

### STK Push (M-Pesa Express)

Initiate an M-Pesa Express payment request that sends a payment prompt to the customer's phone.

```java
import io.github.wmnjuguna.stkpush.StkPushRequest;
import io.github.wmnjuguna.stkpush.StkPushResponse;
import io.github.wmnjuguna.util.StkPushPasswordUtil;

// Generate password and timestamp
StkPushPasswordUtil.PasswordResult passwordResult =
    StkPushPasswordUtil.generatePassword("174379", "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919");

StkPushRequest request = new StkPushRequest(
    "174379",                           // Business short code
    passwordResult.getPassword(),       // Generated password
    passwordResult.getTimestamp(),      // Generated timestamp
    "CustomerPayBillOnline",           // Transaction type
    "1",                               // Amount
    "254708374149",                    // Phone number
    "174379",                          // Party A (same as business short code)
    "174379",                          // Party B (same as business short code)
    "https://mydomain.com/callback",   // Callback URL
    "CompanyXLTD",                     // Account reference
    "Payment of X"                     // Transaction description
);

StkPushResponse response = client.initiateStkPush(request);
System.out.println("Checkout Request ID: " + response.checkoutRequestID());
```

**Handling STK Push Callbacks:**

```java
import io.github.wmnjuguna.stkpush.StkPushCallback;
import io.github.wmnjuguna.webhook.StkPushCallbackHandler;

@RestController
@RequestMapping("/api/callbacks")
public class CallbackController {

    @PostMapping("/stkpush")
    public ResponseEntity<String> handleStkPushCallback(@RequestBody StkPushCallback callback) {
        // Process the callback
        if (callback.getBody().getStkCallback().getResultCode() == 0) {
            // Payment successful
            String mpesaReceiptNumber = StkPushCallbackHandler.extractMpesaReceiptNumber(callback);
            String amount = StkPushCallbackHandler.extractAmount(callback);
            String phoneNumber = StkPushCallbackHandler.extractPhoneNumber(callback);

            // Update your database, send confirmation, etc.
            System.out.println("Payment successful: " + mpesaReceiptNumber);
        } else {
            // Payment failed
            String errorMessage = callback.getBody().getStkCallback().getResultDesc();
            System.out.println("Payment failed: " + errorMessage);
        }

        return ResponseEntity.ok("OK");
    }
}
```

### C2B - Customer to Business

Register URLs for C2B transactions and handle customer payments.

**Register C2B URLs:**

```java
import io.github.wmnjuguna.c2b.C2BRegisterUrlRequest;
import io.github.wmnjuguna.c2b.C2BRegisterUrlResponse;

C2BRegisterUrlRequest request = new C2BRegisterUrlRequest(
    "600988",                                    // Short code
    "Completed",                                 // Response type
    "https://mydomain.com/c2b/confirmation",    // Confirmation URL
    "https://mydomain.com/c2b/validation"       // Validation URL
);

C2BRegisterUrlResponse response = client.registerC2BUrls(request);
System.out.println("Registration status: " + response.responseDescription());
```

**Handle C2B Callbacks:**

```java
@PostMapping("/c2b/validation")
public ResponseEntity<Map<String, String>> validateC2BTransaction(@RequestBody Map<String, Object> request) {
    // Validate the transaction
    String transactionType = (String) request.get("TransType");
    String amount = (String) request.get("TransAmount");
    String phoneNumber = (String) request.get("MSISDN");

    Map<String, String> response = new HashMap<>();

    // Your validation logic here
    if (isValidTransaction(amount, phoneNumber)) {
        response.put("ResultCode", "0");
        response.put("ResultDesc", "Accepted");
    } else {
        response.put("ResultCode", "C2B00011");
        response.put("ResultDesc", "Rejected");
    }

    return ResponseEntity.ok(response);
}

@PostMapping("/c2b/confirmation")
public ResponseEntity<Map<String, String>> confirmC2BTransaction(@RequestBody Map<String, Object> request) {
    // Process the confirmed transaction
    String transactionId = (String) request.get("TransID");
    String amount = (String) request.get("TransAmount");
    String phoneNumber = (String) request.get("MSISDN");

    // Update your database, send notifications, etc.
    processPayment(transactionId, amount, phoneNumber);

    Map<String, String> response = new HashMap<>();
    response.put("ResultCode", "0");
    response.put("ResultDesc", "Success");

    return ResponseEntity.ok(response);
}
```

### B2C - Business to Customer

Send money from business to customer (salary payments, refunds, etc.).

```java
import io.github.wmnjuguna.b2c.B2CRequest;
import io.github.wmnjuguna.b2c.B2CResponse;

B2CRequest request = new B2CRequest(
    "testapi",                          // Initiator name
    "Safaricom999!*!",                 // Security credential
    "BusinessPayment",                  // Command ID
    "100",                             // Amount
    "600992",                          // Party A (business short code)
    "254708374149",                    // Party B (customer phone number)
    "Salary payment for March",        // Remarks
    "https://mydomain.com/b2c/timeout", // Queue timeout URL
    "https://mydomain.com/b2c/result",  // Result URL
    "March 2024 Salary"                // Occasion
);

B2CResponse response = client.b2cPayment(request);
System.out.println("Conversation ID: " + response.conversationID());
```

### B2B - Business to Business

Transfer money between business accounts.

```java
import io.github.wmnjuguna.b2b.B2BRequest;
import io.github.wmnjuguna.b2b.B2BResponse;

B2BRequest request = new B2BRequest(
    "testapi",                          // Initiator
    "Safaricom999!*!",                 // Security credential
    "BusinessToBusinessTransfer",       // Command ID
    "4",                               // Sender identifier type
    "4",                               // Receiver identifier type
    "1000",                            // Amount
    "600992",                          // Party A (sender short code)
    "600000",                          // Party B (receiver short code)
    "Payment for services",            // Remarks
    "https://mydomain.com/b2b/timeout", // Queue timeout URL
    "https://mydomain.com/b2b/result",  // Result URL
    "INV-2024-001"                     // Account reference
);

B2BResponse response = client.b2bPayment(request);
System.out.println("Conversation ID: " + response.conversationID());
```

### Transaction Status

Query the status of any transaction using its transaction ID.

```java
import io.github.wmnjuguna.transactionstatus.TransactionStatusRequest;
import io.github.wmnjuguna.transactionstatus.TransactionStatusResponse;

TransactionStatusRequest request = new TransactionStatusRequest(
    "testapi",                                    // Initiator
    "Safaricom999!*!",                           // Security credential
    "TransactionStatusQuery",                     // Command ID
    "AG_20240301_0000123456789",                 // Transaction ID
    "600992",                                    // Party A (short code)
    "4",                                         // Identifier type
    "https://mydomain.com/status/result",        // Result URL
    "https://mydomain.com/status/timeout",       // Queue timeout URL
    "Transaction status inquiry",                 // Remarks
    "Daily reconciliation"                        // Occasion
);

TransactionStatusResponse response = client.transactionStatus(request);
System.out.println("Conversation ID: " + response.conversationID());
```

### Account Balance

Check the balance of your M-Pesa business account.

```java
import io.github.wmnjuguna.accountbalance.AccountBalanceRequest;
import io.github.wmnjuguna.accountbalance.AccountBalanceResponse;

AccountBalanceRequest request = new AccountBalanceRequest(
    "testapi",                                     // Initiator
    "Safaricom999!*!",                            // Security credential
    "AccountBalance",                              // Command ID
    "600992",                                     // Party A (short code)
    "4",                                          // Identifier type
    "Balance inquiry",                             // Remarks
    "https://mydomain.com/balance/timeout",       // Queue timeout URL
    "https://mydomain.com/balance/result"         // Result URL
);

AccountBalanceResponse response = client.accountBalance(request);
System.out.println("Conversation ID: " + response.conversationID());
```

### Transaction Reversal

Reverse a completed transaction (within specific time limits).

```java
import io.github.wmnjuguna.reversal.ReversalRequest;
import io.github.wmnjuguna.reversal.ReversalResponse;

ReversalRequest request = new ReversalRequest(
    "testapi",                                  // Initiator
    "Safaricom999!*!",                         // Security credential
    "TransactionReversal",                      // Command ID
    "AG_20240301_0000123456789",               // Transaction ID to reverse
    "100",                                     // Amount to reverse
    "600992",                                  // Receiver party (short code)
    "4",                                       // Receiver identifier type
    "https://mydomain.com/reversal/result",    // Result URL
    "https://mydomain.com/reversal/timeout",   // Queue timeout URL
    "Refund for cancelled order",              // Remarks
    "Customer refund request"                   // Occasion
);

ReversalResponse response = client.reversal(request);
System.out.println("Conversation ID: " + response.conversationID());
```

### Complete Example: Payment Service

Here's a complete example showing how to use the SDK in a Spring Boot service:

```java
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.wmnjuguna.DarajaApiClient;
import io.github.wmnjuguna.stkpush.StkPushRequest;
import io.github.wmnjuguna.stkpush.StkPushResponse;
import io.github.wmnjuguna.b2c.B2CRequest;
import io.github.wmnjuguna.b2c.B2CResponse;
import io.github.wmnjuguna.util.StkPushPasswordUtil;

@Service
public class PaymentService {

    private final DarajaApiClient darajaClient;

    @Autowired
    public PaymentService(DarajaApiClient darajaClient) {
        this.darajaClient = darajaClient;
    }

    public String initiatePayment(String phoneNumber, String amount, String accountReference) {
        try {
            // Generate password and timestamp
            StkPushPasswordUtil.PasswordResult passwordResult =
                StkPushPasswordUtil.generatePassword("174379", "your-passkey");

            // Create payment request
            StkPushRequest request = new StkPushRequest(
                "174379",
                passwordResult.getPassword(),
                passwordResult.getTimestamp(),
                "CustomerPayBillOnline",
                amount,
                phoneNumber,
                "174379",
                "174379",
                "https://yourdomain.com/api/callbacks/stkpush",
                accountReference,
                "Payment for services"
            );

            // Initiate payment
            StkPushResponse response = darajaClient.initiateStkPush(request);

            // Return checkout request ID for tracking
            return response.checkoutRequestID();

        } catch (Exception e) {
            throw new RuntimeException("Failed to initiate payment: " + e.getMessage(), e);
        }
    }

    public void processB2CPayment(String phoneNumber, String amount, String remarks) {
        try {
            B2CRequest request = new B2CRequest(
                "testapi",
                "your-security-credential",
                "BusinessPayment",
                amount,
                "600992",
                phoneNumber,
                remarks,
                "https://yourdomain.com/api/callbacks/b2c/timeout",
                "https://yourdomain.com/api/callbacks/b2c/result",
                "B2C Payment"
            );

            B2CResponse response = darajaClient.b2cPayment(request);

            // Log or store conversation ID for tracking
            System.out.println("B2C initiated with conversation ID: " + response.conversationID());

        } catch (Exception e) {
            throw new RuntimeException("Failed to process B2C payment: " + e.getMessage(), e);
        }
    }
}

## Error Handling

The SDK provides comprehensive error handling through custom exceptions that map to different types of API errors:

### Exception Hierarchy

```java
DarajaException                    // Base exception
├── DarajaAuthenticationException  // Authentication failures
├── InvalidDarajaRequestException  // Invalid request parameters
└── DarajaApiException            // General API errors
```

### Exception Types

- **`DarajaException`**: Base exception for all Daraja SDK errors
- **`DarajaAuthenticationException`**: Thrown when authentication fails (invalid credentials, expired tokens)
- **`InvalidDarajaRequestException`**: Thrown for malformed requests, missing required parameters
- **`DarajaApiException`**: Thrown for API-level errors (server errors, service unavailable)

### Error Handling Examples

**Basic Error Handling:**

```java
import io.github.wmnjuguna.exception.*;

try {
    StkPushResponse response = client.initiateStkPush(request);
    // Process successful response
} catch (DarajaAuthenticationException e) {
    // Handle authentication errors
    logger.error("Authentication failed: {}", e.getMessage());
    // Possibly refresh credentials or alert admin
} catch (InvalidDarajaRequestException e) {
    // Handle invalid request parameters
    logger.error("Invalid request: {}", e.getMessage());
    // Return validation error to user
} catch (DarajaApiException e) {
    // Handle API errors
    logger.error("API error: {}", e.getMessage());
    // Possibly retry or alert operations team
} catch (DarajaException e) {
    // Handle any other Daraja-related errors
    logger.error("Daraja SDK error: {}", e.getMessage());
}
```

**Service-Level Error Handling:**

```java
@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentResult initiatePayment(PaymentRequest paymentRequest) {
        try {
            StkPushResponse response = client.initiateStkPush(createStkRequest(paymentRequest));

            return PaymentResult.success(response.checkoutRequestID());

        } catch (DarajaAuthenticationException e) {
            logger.error("Payment authentication failed for request {}: {}",
                        paymentRequest.getReference(), e.getMessage());
            return PaymentResult.failure("AUTHENTICATION_ERROR", "Service temporarily unavailable");

        } catch (InvalidDarajaRequestException e) {
            logger.error("Invalid payment request {}: {}",
                        paymentRequest.getReference(), e.getMessage());
            return PaymentResult.failure("VALIDATION_ERROR", "Invalid payment details");

        } catch (DarajaApiException e) {
            logger.error("Daraja API error for payment {}: {}",
                        paymentRequest.getReference(), e.getMessage());
            return PaymentResult.failure("API_ERROR", "Payment service temporarily unavailable");

        } catch (Exception e) {
            logger.error("Unexpected error processing payment {}: {}",
                        paymentRequest.getReference(), e.getMessage(), e);
            return PaymentResult.failure("UNKNOWN_ERROR", "An unexpected error occurred");
        }
    }
}
```

## Best Practices

### 1. Configuration Management

**✅ Do:**
```java
// Use environment variables for sensitive data
DarajaClientFactory factory = new DarajaClientFactory(
    DarajaEnvironment.valueOf(System.getenv("DARAJA_ENVIRONMENT")),
    System.getenv("DARAJA_CONSUMER_KEY"),
    System.getenv("DARAJA_CONSUMER_SECRET")
);
```

**❌ Don't:**
```java
// Never hardcode credentials
DarajaClientFactory factory = new DarajaClientFactory(
    DarajaEnvironment.SANDBOX,
    "hardcoded-key",
    "hardcoded-secret"
);
```

### 2. Client Instance Management

**✅ Do:** Create one client instance and reuse it (thread-safe):
```java
@Configuration
public class DarajaConfiguration {
    @Bean
    @Singleton
    public DarajaApiClient darajaApiClient(DarajaProperties properties) {
        // Single instance shared across application
        DarajaClientFactory factory = new DarajaClientFactory(/*...*/);
        return factory.createApiClient();
    }
}
```

**❌ Don't:** Create new clients for every request:
```java
// Inefficient - creates new client each time
public void processPayment() {
    DarajaApiClient client = new DarajaClientFactory(/*...*/).createApiClient();
    // ...
}
```

### 3. Callback URL Security

**✅ Do:** Validate callback authenticity:
```java
@PostMapping("/callbacks/stkpush")
public ResponseEntity<String> handleCallback(@RequestBody StkPushCallback callback,
                                           HttpServletRequest request) {
    // Validate request comes from Safaricom
    if (!isValidCallback(request, callback)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Process callback
    processCallback(callback);
    return ResponseEntity.ok("OK");
}

private boolean isValidCallback(HttpServletRequest request, StkPushCallback callback) {
    // Implement IP whitelisting, signature validation, etc.
    String sourceIp = request.getRemoteAddr();
    return safaricomIpRanges.contains(sourceIp);
}
```

### 4. Transaction Tracking

**✅ Do:** Store transaction references for reconciliation:
```java
@Service
public class PaymentService {

    public String initiatePayment(PaymentRequest request) {
        // Generate unique reference
        String transactionRef = generateTransactionReference();

        try {
            StkPushResponse response = client.initiateStkPush(createRequest(request));

            // Store mapping for callback processing
            transactionRepository.save(new TransactionRecord(
                transactionRef,
                response.checkoutRequestID(),
                request.getPhoneNumber(),
                request.getAmount(),
                TransactionStatus.PENDING
            ));

            return transactionRef;

        } catch (Exception e) {
            // Update status to failed
            transactionRepository.updateStatus(transactionRef, TransactionStatus.FAILED);
            throw e;
        }
    }
}
```

### 5. Retry Logic

**✅ Do:** Implement retry for transient failures:
```java
@Service
public class PaymentService {

    @Retryable(value = {DarajaApiException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public StkPushResponse initiatePaymentWithRetry(StkPushRequest request) {
        return client.initiateStkPush(request);
    }

    @Recover
    public StkPushResponse recover(DarajaApiException ex, StkPushRequest request) {
        logger.error("Failed to initiate payment after retries: {}", ex.getMessage());
        throw new PaymentException("Payment service unavailable", ex);
    }
}
```

### 6. Logging and Monitoring

**✅ Do:** Log important events without sensitive data:
```java
@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public String initiatePayment(String phoneNumber, String amount) {
        // Mask sensitive data in logs
        String maskedPhone = maskPhoneNumber(phoneNumber);

        logger.info("Initiating STK push for phone: {}, amount: {}", maskedPhone, amount);

        try {
            StkPushResponse response = client.initiateStkPush(request);
            logger.info("STK push initiated successfully. CheckoutRequestID: {}",
                       response.checkoutRequestID());
            return response.checkoutRequestID();

        } catch (Exception e) {
            logger.error("STK push failed for phone: {}, amount: {}, error: {}",
                        maskedPhone, amount, e.getMessage());
            throw e;
        }
    }

    private String maskPhoneNumber(String phone) {
        return phone.replaceAll("(\\d{3})(\\d{6})(\\d{3})", "$1******$3");
    }
}
```

### 7. Environment-Specific Configuration

**✅ Do:** Use different configurations for different environments:
```yaml
# application-dev.yml
daraja:
  environment: SANDBOX
  consumer:
    key: ${DARAJA_DEV_CONSUMER_KEY}
    secret: ${DARAJA_DEV_CONSUMER_SECRET}

---
# application-prod.yml
daraja:
  environment: PRODUCTION
  consumer:
    key: ${DARAJA_PROD_CONSUMER_KEY}
    secret: ${DARAJA_PROD_CONSUMER_SECRET}
```

### 8. Testing

**✅ Do:** Use dependency injection for testability:
```java
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private DarajaApiClient darajaClient;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldInitiatePaymentSuccessfully() {
        // Given
        StkPushResponse mockResponse = new StkPushResponse(
            "00000000",
            "ws_CO_123456789",
            "Success",
            "Request accepted for processing"
        );
        when(darajaClient.initiateStkPush(any())).thenReturn(mockResponse);

        // When
        String result = paymentService.initiatePayment("254700000000", "100", "REF123");

        // Then
        assertEquals("ws_CO_123456789", result);
        verify(darajaClient).initiateStkPush(any(StkPushRequest.class));
    }
}
```

### 9. Callback URL Requirements

- **HTTPS Only**: Safaricom only sends callbacks to HTTPS URLs
- **Public Accessibility**: URLs must be publicly accessible (not localhost)
- **Unique URLs**: Use different callback URLs for different transaction types
- **Response Format**: Always respond with HTTP 200 and appropriate body

### 10. Security Considerations

- **Never log sensitive data**: Credentials, full phone numbers, transaction details
- **Use HTTPS**: All communication must be over HTTPS
- **Validate inputs**: Always validate and sanitize user inputs
- **Store credentials securely**: Use vault services or encrypted configuration
- **Monitor transactions**: Implement real-time monitoring and alerting

## Troubleshooting

### Common Issues

1. **Authentication Failures**
   - Verify consumer key and secret are correct
   - Check if credentials are for the right environment (SANDBOX vs PRODUCTION)
   - Ensure credentials haven't expired

2. **Callback Not Received**
   - Verify callback URL is publicly accessible via HTTPS
   - Check firewall and security group settings
   - Ensure endpoint returns HTTP 200 status

3. **Invalid Request Parameters**
   - Verify phone number format (254XXXXXXXXX)
   - Check amount is a valid positive number
   - Ensure all required fields are provided

## Contributing

Contributions are welcome! Please feel free to submit a pull request.

## License

This SDK is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
