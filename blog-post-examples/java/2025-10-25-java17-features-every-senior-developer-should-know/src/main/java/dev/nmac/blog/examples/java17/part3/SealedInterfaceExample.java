package dev.nmac.blog.examples.java17.part3;

import java.util.List;

/**
 * Example 2: Sealed Interfaces with Records
 *
 * Demonstrates:
 * - Sealed interface with permits clause
 * - Records as implementations (implicitly final)
 * - Validation in compact constructors
 * - Type-safe payment processing domain model
 * - Reflection on sealed interfaces
 *
 * Key concept: Sealed interfaces work perfectly with records because records
 * are implicitly final. This combination creates immutable, type-safe domain
 * models where the compiler knows every possible implementation.
 */

// ============================================================================
// Sealed interface - explicitly lists all permitted implementations
// ============================================================================

/**
 * Sealed Payment interface that permits only CreditCard, DebitCard, Cash,
 * and BankTransfer implementations. This creates a closed set of payment types.
 */
sealed interface Payment
    permits CreditCard, DebitCard, Cash, BankTransfer {

    /**
     * Gets the payment amount.
     *
     * @return the payment amount
     */
    double amount();

    /**
     * Gets a human-readable description of the payment.
     *
     * @return formatted description string
     */
    String description();
}

// ============================================================================
// Record implementations - implicitly final, perfect for sealed types
// ============================================================================

/**
 * Credit card payment record with validation.
 * Records are implicitly final, making them ideal sealed type implementations.
 */
record CreditCard(
    String cardNumber,
    String cardHolder,
    double amount,
    String merchantId
) implements Payment {

    /**
     * Compact constructor with validation.
     * Validates amount and card number format.
     */
    public CreditCard {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (cardNumber == null || cardNumber.length() != 16) {
            throw new IllegalArgumentException("Invalid card number");
        }
    }

    /**
     * Provides a formatted description masking most card digits.
     *
     * @return description showing amount and last 4 digits
     */
    @Override
    public String description() {
        return String.format("Credit Card payment: $%.2f (Card ending %s)",
            amount, cardNumber.substring(12));
    }
}

/**
 * Debit card payment record with validation.
 */
record DebitCard(
    String cardNumber,
    String cardHolder,
    double amount,
    String pin
) implements Payment {

    /**
     * Compact constructor with validation.
     */
    public DebitCard {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    /**
     * Provides a formatted description masking most card digits.
     *
     * @return description showing amount and last 4 digits
     */
    @Override
    public String description() {
        return String.format("Debit Card payment: $%.2f (Card ending %s)",
            amount, cardNumber.substring(12));
    }
}

/**
 * Cash payment record with currency specification.
 */
record Cash(
    double amount,
    String currency
) implements Payment {

    /**
     * Compact constructor with validation.
     */
    public Cash {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    /**
     * Provides a formatted description with currency.
     *
     * @return description showing amount and currency
     */
    @Override
    public String description() {
        return String.format("Cash payment: %.2f %s", amount, currency);
    }
}

/**
 * Bank transfer payment record with account details.
 */
record BankTransfer(
    String fromAccount,
    String toAccount,
    double amount,
    String reference
) implements Payment {

    /**
     * Compact constructor with validation.
     */
    public BankTransfer {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    /**
     * Provides a formatted description with reference number.
     *
     * @return description showing amount and reference
     */
    @Override
    public String description() {
        return String.format("Bank Transfer: $%.2f (Ref: %s)", amount, reference);
    }
}

// ============================================================================
// Main example class
// ============================================================================

/**
 * Demonstrates sealed interfaces with record implementations.
 *
 * Shows:
 * - Creating record instances that implement sealed interface
 * - Polymorphic payment processing
 * - Type-specific record component access
 * - Reflection on sealed interfaces
 */
public class SealedInterfaceExample {

    /**
     * Main method demonstrating sealed interface usage with records.
     */
    public static void main(String[] args) {
        // Create instances of all payment types
        var payments = List.of(
            new CreditCard("1234567890123456", "John Doe", 150.00, "MERCHANT_001"),
            new DebitCard("9876543210987654", "Jane Smith", 75.50, "1234"),
            new Cash(50.00, "USD"),
            new BankTransfer("ACC001", "ACC002", 1000.00, "INV-2024-001")
        );

        // Process payments polymorphically
        System.out.println("=== Payment Processing ===");
        double total = 0.0;
        for (var payment : payments) {
            System.out.println(payment.description());
            total += payment.amount();
        }

        System.out.printf("\nTotal processed: $%.2f\n", total);

        // Type-specific access using record components
        System.out.println("\n=== Type-Specific Operations ===");
        var creditCard = new CreditCard("1111222233334444", "Alice Brown", 299.99, "SHOP_123");
        System.out.println("Card holder: " + creditCard.cardHolder());
        System.out.println("Merchant ID: " + creditCard.merchantId());

        // Reflection on sealed interface
        System.out.println("\n=== Interface Reflection ===");
        System.out.println("Payment is sealed: " + Payment.class.isSealed());
        System.out.print("Permitted implementations: ");
        for (var permitted : Payment.class.getPermittedSubclasses()) {
            System.out.print(permitted.getSimpleName() + " ");
        }
        System.out.println();
    }
}
