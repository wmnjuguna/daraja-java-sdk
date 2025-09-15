package app.daraja.sdk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DarajaEnvironmentTest {

    @Test
    void sandbox_ShouldHaveCorrectBaseUrl() {
        assertEquals("https://sandbox.safaricom.co.ke", DarajaEnvironment.SANDBOX.getBaseUrl());
    }

    @Test
    void production_ShouldHaveCorrectBaseUrl() {
        assertEquals("https://api.safaricom.co.ke", DarajaEnvironment.PRODUCTION.getBaseUrl());
    }

    @Test
    void sandbox_ShouldHaveCorrectAuthUrl() {
        String expected = "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
        assertEquals(expected, DarajaEnvironment.SANDBOX.getAuthUrl());
    }

    @Test
    void production_ShouldHaveCorrectAuthUrl() {
        String expected = "https://api.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
        assertEquals(expected, DarajaEnvironment.PRODUCTION.getAuthUrl());
    }

    @Test
    void sandbox_ShouldHaveCorrectStkPushUrl() {
        String expected = "https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest";
        assertEquals(expected, DarajaEnvironment.SANDBOX.getStkPushUrl());
    }

    @Test
    void production_ShouldHaveCorrectStkPushUrl() {
        String expected = "https://api.safaricom.co.ke/mpesa/stkpush/v1/processrequest";
        assertEquals(expected, DarajaEnvironment.PRODUCTION.getStkPushUrl());
    }

    @Test
    void sandbox_ShouldHaveCorrectStkPushQueryUrl() {
        String expected = "https://sandbox.safaricom.co.ke/mpesa/stkpushquery/v1/query";
        assertEquals(expected, DarajaEnvironment.SANDBOX.getStkPushQueryUrl());
    }

    @Test
    void production_ShouldHaveCorrectStkPushQueryUrl() {
        String expected = "https://api.safaricom.co.ke/mpesa/stkpushquery/v1/query";
        assertEquals(expected, DarajaEnvironment.PRODUCTION.getStkPushQueryUrl());
    }

    @Test
    void sandbox_ShouldHaveCorrectC2BRegisterUrl() {
        String expected = "https://sandbox.safaricom.co.ke/mpesa/c2b/v1/registerurl";
        assertEquals(expected, DarajaEnvironment.SANDBOX.getC2BRegisterUrl());
    }

    @Test
    void production_ShouldHaveCorrectC2BRegisterUrl() {
        String expected = "https://api.safaricom.co.ke/mpesa/c2b/v1/registerurl";
        assertEquals(expected, DarajaEnvironment.PRODUCTION.getC2BRegisterUrl());
    }

    @Test
    void sandbox_ShouldHaveCorrectC2BSimulateUrl() {
        String expected = "https://sandbox.safaricom.co.ke/mpesa/c2b/v1/simulate";
        assertEquals(expected, DarajaEnvironment.SANDBOX.getC2BSimulateUrl());
    }

    @Test
    void production_ShouldHaveCorrectC2BSimulateUrl() {
        String expected = "https://api.safaricom.co.ke/mpesa/c2b/v1/simulate";
        assertEquals(expected, DarajaEnvironment.PRODUCTION.getC2BSimulateUrl());
    }

    @Test
    void sandbox_ShouldHaveCorrectB2CUrl() {
        String expected = "https://sandbox.safaricom.co.ke/mpesa/b2c/v1/paymentrequest";
        assertEquals(expected, DarajaEnvironment.SANDBOX.getB2CUrl());
    }

    @Test
    void production_ShouldHaveCorrectB2CUrl() {
        String expected = "https://api.safaricom.co.ke/mpesa/b2c/v1/paymentrequest";
        assertEquals(expected, DarajaEnvironment.PRODUCTION.getB2CUrl());
    }

    @Test
    void sandbox_ShouldHaveCorrectTransactionStatusUrl() {
        String expected = "https://sandbox.safaricom.co.ke/mpesa/transactionstatus/v1/query";
        assertEquals(expected, DarajaEnvironment.SANDBOX.getTransactionStatusUrl());
    }

    @Test
    void production_ShouldHaveCorrectTransactionStatusUrl() {
        String expected = "https://api.safaricom.co.ke/mpesa/transactionstatus/v1/query";
        assertEquals(expected, DarajaEnvironment.PRODUCTION.getTransactionStatusUrl());
    }

    @Test
    void sandbox_ShouldHaveCorrectAccountBalanceUrl() {
        String expected = "https://sandbox.safaricom.co.ke/mpesa/accountbalance/v1/query";
        assertEquals(expected, DarajaEnvironment.SANDBOX.getAccountBalanceUrl());
    }

    @Test
    void production_ShouldHaveCorrectAccountBalanceUrl() {
        String expected = "https://api.safaricom.co.ke/mpesa/accountbalance/v1/query";
        assertEquals(expected, DarajaEnvironment.PRODUCTION.getAccountBalanceUrl());
    }

    @Test
    void sandbox_ShouldHaveCorrectReversalUrl() {
        String expected = "https://sandbox.safaricom.co.ke/mpesa/reversal/v1/request";
        assertEquals(expected, DarajaEnvironment.SANDBOX.getReversalUrl());
    }

    @Test
    void production_ShouldHaveCorrectReversalUrl() {
        String expected = "https://api.safaricom.co.ke/mpesa/reversal/v1/request";
        assertEquals(expected, DarajaEnvironment.PRODUCTION.getReversalUrl());
    }

    @Test
    void enumValues_ShouldContainBothEnvironments() {
        DarajaEnvironment[] values = DarajaEnvironment.values();
        assertEquals(2, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(DarajaEnvironment.SANDBOX));
        assertTrue(java.util.Arrays.asList(values).contains(DarajaEnvironment.PRODUCTION));
    }

    @Test
    void valueOf_ShouldReturnCorrectEnum() {
        assertEquals(DarajaEnvironment.SANDBOX, DarajaEnvironment.valueOf("SANDBOX"));
        assertEquals(DarajaEnvironment.PRODUCTION, DarajaEnvironment.valueOf("PRODUCTION"));
    }

    @Test
    void toString_ShouldReturnEnumName() {
        assertEquals("SANDBOX", DarajaEnvironment.SANDBOX.toString());
        assertEquals("PRODUCTION", DarajaEnvironment.PRODUCTION.toString());
    }
}