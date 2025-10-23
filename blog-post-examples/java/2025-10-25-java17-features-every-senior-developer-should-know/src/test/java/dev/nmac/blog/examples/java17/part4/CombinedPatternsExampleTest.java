package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import dev.nmac.blog.examples.java17.part4.CombinedPatternsExample.*;

class CombinedPatternsExampleTest {

    @Test
    @DisplayName("Should process credit card payment when under limit")
    void shouldProcessCreditCardPaymentWhenUnderLimit() {
        PaymentMethod cc = new CreditCard("1234-5678-9012-3456", "123", 1000.0);
        String result = CombinedPatternsExample.processPayment(cc, 500.0);

        assertTrue(result.contains("Charging"));
        assertTrue(result.contains("3456"));
    }

    @Test
    @DisplayName("Should decline credit card payment when over limit")
    void shouldDeclineCreditCardPaymentWhenOverLimit() {
        PaymentMethod cc = new CreditCard("1234-5678-9012-3456", "123", 100.0);
        String result = CombinedPatternsExample.processPayment(cc, 500.0);

        assertTrue(result.contains("declined"));
        assertTrue(result.contains("limit"));
    }

    @Test
    @DisplayName("Should process cash payment with correct change")
    void shouldProcessCashPaymentWithCorrectChange() {
        PaymentMethod cash = new Cash(100.0);
        String result = CombinedPatternsExample.processPayment(cash, 75.0);

        assertTrue(result.contains("change: $25.00"));
    }

    @Test
    @DisplayName("Should reject insufficient cash")
    void shouldRejectInsufficientCash() {
        PaymentMethod cash = new Cash(50.0);
        String result = CombinedPatternsExample.processPayment(cash, 75.0);

        assertTrue(result.contains("Insufficient cash"));
    }

    @Test
    @DisplayName("Should process digital wallet payment when sufficient balance")
    void shouldProcessDigitalWalletPaymentWhenSufficientBalance() {
        PaymentMethod wallet = new DigitalWallet("PayPal", "user@example.com", 200.0);
        String result = CombinedPatternsExample.processPayment(wallet, 75.0);

        assertTrue(result.contains("Paid"));
        assertTrue(result.contains("PayPal"));
    }

    @Test
    @DisplayName("Should analyze long strings correctly")
    void shouldAnalyzeLongStringsCorrectly() {
        String longText = "A".repeat(150);
        String result = CombinedPatternsExample.analyzeData(longText);

        assertTrue(result.startsWith("Long text:"));
    }

    @Test
    @DisplayName("Should detect numeric strings")
    void shouldDetectNumericStrings() {
        String result = CombinedPatternsExample.analyzeData("12345");

        assertEquals("Numeric string: 12345", result);
    }

    @Test
    @DisplayName("Should classify integers by size")
    void shouldClassifyIntegersBySize() {
        assertEquals("Large number: 2000", CombinedPatternsExample.analyzeData(2000));
        assertEquals("Negative number: -10", CombinedPatternsExample.analyzeData(-10));
        assertEquals("Number: 42", CombinedPatternsExample.analyzeData(42));
    }

    @Test
    @DisplayName("Should analyze empty and non-empty lists")
    void shouldAnalyzeEmptyAndNonEmptyLists() {
        assertEquals("Empty list", CombinedPatternsExample.analyzeData(List.of()));
        assertEquals("List with 3 elements", CombinedPatternsExample.analyzeData(List.of("a", "b", "c")));
    }

    @Test
    @DisplayName("Should provide free express shipping for VIP large orders")
    void shouldProvideFreeExpressShippingForVipLargeOrders() {
        Order order = new Order("O1", 1500.0, "VIP", 10);
        String result = CombinedPatternsExample.determineShipping(order);

        assertEquals("FREE express shipping (VIP + large order)", result);
    }

    @Test
    @DisplayName("Should provide free standard shipping for VIP customers")
    void shouldProvideFreeStandardShippingForVipCustomers() {
        Order order = new Order("O2", 200.0, "VIP", 3);
        String result = CombinedPatternsExample.determineShipping(order);

        assertEquals("FREE standard shipping (VIP)", result);
    }

    @Test
    @DisplayName("Should provide free shipping for bulk orders")
    void shouldProvideFreeShippingForBulkOrders() {
        Order order = new Order("O3", 600.0, "REGULAR", 8);
        String result = CombinedPatternsExample.determineShipping(order);

        assertEquals("FREE standard shipping (bulk order)", result);
    }

    @Test
    @DisplayName("Should charge standard shipping for medium orders")
    void shouldChargeStandardShippingForMediumOrders() {
        Order order = new Order("O4", 150.0, "REGULAR", 2);
        String result = CombinedPatternsExample.determineShipping(order);

        assertEquals("Standard shipping: $10", result);
    }

    @Test
    @DisplayName("Should charge higher shipping for small orders")
    void shouldChargeHigherShippingForSmallOrders() {
        Order order = new Order("O5", 50.0, "REGULAR", 1);
        String result = CombinedPatternsExample.determineShipping(order);

        assertEquals("Standard shipping: $15", result);
    }
}
