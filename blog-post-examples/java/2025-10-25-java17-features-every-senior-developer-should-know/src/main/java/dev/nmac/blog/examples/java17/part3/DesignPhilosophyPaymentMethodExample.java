package dev.nmac.blog.examples.java17.part3;

/**
 * Demonstrates the design philosophy of sealed classes:
 * Pattern 3 - Final Leaf Nodes (sealed → final)
 *
 * Shows when and why to prevent further extension,
 * using final to make architectural intentions explicit.
 */
public class DesignPhilosophyPaymentMethodExample {

    public sealed abstract static class PaymentMethod
        permits CardPayment, BankTransfer, CryptoCurrency {
        protected String description;

        protected PaymentMethod(String description) {
            this.description = description;
        }

        public String description() { return description; }
        public abstract void process(double amount);
    }

    // Branch 1: final - impossible to extend
    public static final class CardPayment extends PaymentMethod {
        private final String cardNetwork; // Visa, Mastercard, etc.

        public CardPayment(String cardNetwork) {
            super("Card Payment - " + cardNetwork);
            this.cardNetwork = cardNetwork;
        }

        public String cardNetwork() { return cardNetwork; }

        @Override
        public void process(double amount) {
            // PCI compliance: no extensions allowed for security
            System.out.println("Processing card payment via " + cardNetwork + ": $" + String.format("%.2f", amount));
        }
    }

    // Branch 2: sealed - further subdivision possible
    public sealed abstract static class BankTransfer extends PaymentMethod
        permits DomesticTransfer, InternationalTransfer {

        protected BankTransfer(String description) {
            super(description);
        }
    }

    public static final class DomesticTransfer extends BankTransfer {
        private final String routingNumber;

        public DomesticTransfer(String routingNumber) {
            super("Domestic Bank Transfer");
            this.routingNumber = routingNumber;
        }

        public String routingNumber() { return routingNumber; }

        @Override
        public void process(double amount) {
            System.out.println("Processing domestic transfer via routing " + routingNumber + ": $" + String.format("%.2f", amount));
        }
    }

    public static final class InternationalTransfer extends BankTransfer {
        private final String swiftCode;
        private final String targetCountry;

        public InternationalTransfer(String swiftCode, String targetCountry) {
            super("International Bank Transfer");
            this.swiftCode = swiftCode;
            this.targetCountry = targetCountry;
        }

        public String swiftCode() { return swiftCode; }
        public String targetCountry() { return targetCountry; }

        @Override
        public void process(double amount) {
            System.out.println("Processing international transfer to " + targetCountry
                + " via SWIFT " + swiftCode + ": $" + String.format("%.2f", amount));
        }
    }

    // Branch 3: final - architectural endpoint
    public static final class CryptoCurrency extends PaymentMethod {
        private final String blockchain;
        private final String walletAddress;

        public CryptoCurrency(String blockchain, String walletAddress) {
            super("Cryptocurrency - " + blockchain);
            this.blockchain = blockchain;
            this.walletAddress = walletAddress;
        }

        public String blockchain() { return blockchain; }
        public String walletAddress() { return walletAddress; }

        @Override
        public void process(double amount) {
            System.out.println("Processing " + blockchain + " payment to wallet " + walletAddress.substring(0, 8) + "...: " + amount);
        }
    }

    // This would NOT compile:
    // public class EnhancedCardPayment extends CardPayment { }
    // ❌ Compile error: CardPayment is final

    public static void main(String[] args) {
        System.out.println("=== Payment Method Processing ===");

        // Create various payment methods
        var cardPayment = new CardPayment("Visa");
        var domesticTransfer = new DomesticTransfer("123456789");
        var internationalTransfer = new InternationalTransfer("DEUTDEFF", "Germany");
        var cryptoPayment = new CryptoCurrency("Bitcoin", "1A1z7agoat2wurSrF3S6fDwQoO4XZNXqo");

        // Process payments
        cardPayment.process(150.50);
        domesticTransfer.process(1000.00);
        internationalTransfer.process(5000.00);
        cryptoPayment.process(0.25);

        System.out.println("\n=== Sealed Hierarchy Analysis ===");
        System.out.println("PaymentMethod is sealed: " + PaymentMethod.class.isSealed());
        System.out.println("BankTransfer is sealed: " + BankTransfer.class.isSealed());

        System.out.println("\nCardPayment is final: " + java.lang.reflect.Modifier.isFinal(CardPayment.class.getModifiers()));
        System.out.println("CryptoCurrency is final: " + java.lang.reflect.Modifier.isFinal(CryptoCurrency.class.getModifiers()));
        System.out.println("DomesticTransfer is final: " + java.lang.reflect.Modifier.isFinal(DomesticTransfer.class.getModifiers()));

        System.out.println("\nPaymentMethod permitted subclasses:");
        for (var permitted : PaymentMethod.class.getPermittedSubclasses()) {
            System.out.println("  - " + permitted.getSimpleName());
        }

        System.out.println("\nBankTransfer permitted subclasses:");
        for (var permitted : BankTransfer.class.getPermittedSubclasses()) {
            System.out.println("  - " + permitted.getSimpleName());
        }
    }
}
