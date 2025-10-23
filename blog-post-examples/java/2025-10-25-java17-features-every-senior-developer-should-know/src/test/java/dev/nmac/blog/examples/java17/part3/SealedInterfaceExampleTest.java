package dev.nmac.blog.examples.java17.part3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SealedInterfaceExample demonstrating sealed interfaces with records.
 */
class SealedInterfaceExampleTest {

    @Test
    @DisplayName("Should create CreditCard payment with valid data")
    void shouldCreateCreditCardPaymentWithValidData() {
        var payment = new CreditCard("1234567890123456", "John Doe", 150.00, "MERCHANT_001");

        assertEquals(150.00, payment.amount());
        assertEquals("John Doe", payment.cardHolder());
        assertEquals("MERCHANT_001", payment.merchantId());
        assertTrue(payment.description().contains("$150.00"));
    }

    @Test
    @DisplayName("Should create DebitCard payment with valid data")
    void shouldCreateDebitCardPaymentWithValidData() {
        var payment = new DebitCard("9876543210987654", "Jane Smith", 75.50, "1234");

        assertEquals(75.50, payment.amount());
        assertEquals("Jane Smith", payment.cardHolder());
    }

    @Test
    @DisplayName("Should create Cash payment with currency")
    void shouldCreateCashPaymentWithCurrency() {
        var payment = new Cash(50.00, "USD");

        assertEquals(50.00, payment.amount());
        assertEquals("USD", payment.currency());
        assertTrue(payment.description().contains("USD"));
    }

    @Test
    @DisplayName("Should create BankTransfer with reference number")
    void shouldCreateBankTransferWithReferenceNumber() {
        var payment = new BankTransfer("ACC001", "ACC002", 1000.00, "INV-2024-001");

        assertEquals(1000.00, payment.amount());
        assertEquals("INV-2024-001", payment.reference());
        assertTrue(payment.description().contains("INV-2024-001"));
    }

    @Test
    @DisplayName("Should verify Payment interface is sealed")
    void shouldVerifyPaymentInterfaceIsSealed() {
        assertTrue(Payment.class.isSealed());

        var permitted = Payment.class.getPermittedSubclasses();
        assertEquals(4, permitted.length);
    }

    @Test
    @DisplayName("Should throw exception for invalid payment amounts")
    void shouldThrowExceptionForInvalidPaymentAmounts() {
        assertThrows(IllegalArgumentException.class,
            () -> new CreditCard("1234567890123456", "John", 0, "M001"));
        assertThrows(IllegalArgumentException.class,
            () -> new Cash(-10, "USD"));
    }

    @Test
    @DisplayName("Should throw exception for invalid card number")
    void shouldThrowExceptionForInvalidCardNumber() {
        assertThrows(IllegalArgumentException.class,
            () -> new CreditCard("123", "John", 100, "M001"));
    }

    @Test
    @DisplayName("Should mask card number in description")
    void shouldMaskCardNumberInDescription() {
        var payment = new CreditCard("1234567890123456", "John Doe", 150.00, "M001");
        var description = payment.description();

        assertTrue(description.contains("3456"));
        assertFalse(description.contains("123456789012"));
    }

    @Test
    @DisplayName("Should format bank transfer description correctly")
    void shouldFormatBankTransferDescriptionCorrectly() {
        var payment = new BankTransfer("ACC001", "ACC002", 1000.00, "INV-2024-001");
        var description = payment.description();

        assertTrue(description.contains("Bank Transfer"));
        assertTrue(description.contains("$1000.00"));
        assertTrue(description.contains("INV-2024-001"));
    }
}
