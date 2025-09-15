# Daraja-Java-SDK

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

## Getting Started

### Installation

To use the SDK, add the following dependency to your `build.gradle` file:

```gradle
repositories {
    mavenCentral() // or jcenter()
}

dependencies {
    implementation 'app.daraja.sdk:daraja-java-sdk:1.0.0' // Replace with the latest version
}
```

### Configuration

There is no properties file to configure. All configuration is done in code.

### Authentication

To get started, you need to create a `DarajaClientFactory` and then an `DarajaApiClient` instance. The factory requires your consumer key, consumer secret, and the desired environment (`SANDBOX` or `PRODUCTION`).

```java
import app.daraja.sdk.DarajaClientFactory;
import app.daraja.sdk.DarajaApiClient;
import app.daraja.sdk.DarajaEnvironment;

public class Main {
    public static void main(String[] args) {
        DarajaClientFactory factory = new DarajaClientFactory(
            "YOUR_CONSUMER_KEY",
            "YOUR_CONSUMER_SECRET",
            DarajaEnvironment.SANDBOX
        );

        DarajaApiClient client = factory.createApiClient();

        // You can now use the client to make API calls
    }
}
```

## API Operations

### STK Push

Initiate an M-Pesa Express (STK Push) payment request.

To generate the password for the STK Push request, you can use the `StkPushPasswordUtil` class.

```java
import app.daraja.sdk.stkpush.StkPushRequest;
import app.daraja.sdk.stkpush.StkPushResponse;
import app.daraja.sdk.util.StkPushPasswordUtil;

// ...

// Generate password and timestamp
StkPushPasswordUtil.PasswordResult passwordResult = StkPushPasswordUtil.generatePassword("174379", "YOUR_PASSKEY");

StkPushRequest request = StkPushRequest.builder()
    .businessShortCode("174379")
    .password(passwordResult.getPassword())
    .timestamp(passwordResult.getTimestamp())
    .amount(1)
    .phoneNumber("254708374149")
    .callBackURL("https://example.com/callback")
    .accountReference("account")
    .transactionDesc("description")
    .build();

StkPushResponse response = client.initiateStkPush(request);
```

### C2B - Register URL

Registers the C2B validation and confirmation URLs.

```java
import app.daraja.sdk.c2b.C2BRegisterUrlRequest;
import app.daraja.sdk.c2b.C2BRegisterUrlResponse;

// ...

C2BRegisterUrlRequest request = new C2BRegisterUrlRequest(
    "600988",
    "Completed",
    "https://example.com/confirmation",
    "https://example.com/validation"
);

C2BRegisterUrlResponse response = client.registerC2BUrls(request);
```

### B2C - Business to Customer

Initiates a B2C (Business to Customer) payment.

```java
import app.daraja.sdk.b2c.B2CRequest;
import app.daraja.sdk.b2c.B2CResponse;

// ...

B2CRequest request = new B2CRequest(
    "testuser",
    "credential",
    "BusinessPayment",
    "100",
    "600988",
    "254708374149",
    "remarks",
    "https://example.com/timeout",
    "https://example.com/result",
    "occasion"
);

B2CResponse response = client.b2cPayment(request);
```

### B2B - Business to Business

Initiates a B2B (Business to Business) payment.

```java
import app.daraja.sdk.b2b.B2BRequest;
import app.daraja.sdk.b2b.B2BResponse;

// ...

B2BRequest request = new B2BRequest(
    "testuser",
    "credential",
    "BusinessToBusinessTransfer",
    "4",
    "4",
    "100",
    "600988",
    "600988",
    "remarks",
    "https://example.com/timeout",
    "https://example.com/result",
    "reference"
);

B2BResponse response = client.b2bPayment(request);
```

### Transaction Status

Queries the status of a transaction.

```java
import app.daraja.sdk.transactionstatus.TransactionStatusRequest;
import app.daraja.sdk.transactionstatus.TransactionStatusResponse;

// ...

TransactionStatusRequest request = new TransactionStatusRequest(
    "testuser",
    "credential",
    "TransactionStatusQuery",
    "ABCDEFGHI",
    "600988",
    "1",
    "https://example.com/result",
    "https://example.com/timeout",
    "remarks",
    "occasion"
);

TransactionStatusResponse response = client.transactionStatus(request);
```

### Account Balance

Queries the balance of an M-Pesa account.

```java
import app.daraja.sdk.accountbalance.AccountBalanceRequest;
import app.daraja.sdk.accountbalance.AccountBalanceResponse;

// ...

AccountBalanceRequest request = new AccountBalanceRequest(
    "testuser",
    "credential",
    "AccountBalance",
    "600988",
    "4",
    "remarks",
    "https://example.com/timeout",
    "https://example.com/result"
);

AccountBalanceResponse response = client.accountBalance(request);
```

### Reversal

Reverses a transaction.

```java
import app.daraja.sdk.reversal.ReversalRequest;
import app.daraja.sdk.reversal.ReversalResponse;

// ...

ReversalRequest request = new ReversalRequest(
    "testuser",
    "credential",
    "TransactionReversal",
    "ABCDEFGHI",
    "100",
    "600988",
    "4",
    "https://example.com/result",
    "https://example.com/timeout",
    "remarks",
    "occasion"
);

ReversalResponse response = client.reversal(request);
```

## Error Handling

The SDK throws the following custom exceptions:

-   `DarajaAuthenticationException`: For authentication errors (e.g., invalid credentials).
-   `InvalidDarajaRequestException`: For invalid requests (e.g., missing required parameters).
-   `DarajaApiException`: For general API errors (e.g., server errors).
-   `DarajaException`: The base exception for all other exceptions.

## Contributing

Contributions are welcome! Please feel free to submit a pull request.

## License

This SDK is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
