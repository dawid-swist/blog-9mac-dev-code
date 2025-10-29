package dev.nmac.blog.examples.java17.part4;

import java.util.List;

/**
 * Demonstrates combining pattern matching with guard conditions.
 *
 * Advanced pattern matching scenarios combine type checks with additional
 * conditions (guards) using the && operator. This enables sophisticated
 * conditional logic that's both type-safe and concise.
 */
public class CombinedPatternsExample {

    public sealed interface PaymentMethod permits CreditCard, DebitCard, Cash, DigitalWallet {}

    public record CreditCard(String number, String cvv, double limit) implements PaymentMethod {}
    public record DebitCard(String number, String pin) implements PaymentMethod {}
    public record Cash(double amount) implements PaymentMethod {}
    public record DigitalWallet(String provider, String accountId, double balance) implements PaymentMethod {}

    // Pattern matching with guard conditions
    public static String processPayment(PaymentMethod method, double amount) {
        if (method instanceof CreditCard cc && cc.limit() >= amount) {
            return String.format("Charging $%.2f to credit card ending in %s",
                amount, cc.number().substring(cc.number().length() - 4));
        } else if (method instanceof CreditCard cc) {
            return String.format("Credit card declined - limit $%.2f exceeded by $%.2f",
                cc.limit(), amount - cc.limit());
        } else if (method instanceof DebitCard dc) {
            return String.format("Processing debit card payment of $%.2f", amount);
        } else if (method instanceof Cash cash && cash.amount() >= amount) {
            double change = cash.amount() - amount;
            return String.format("Cash payment of $%.2f received, change: $%.2f",
                cash.amount(), change);
        } else if (method instanceof Cash cash) {
            return String.format("Insufficient cash - need $%.2f more",
                amount - cash.amount());
        } else if (method instanceof DigitalWallet wallet && wallet.balance() >= amount) {
            return String.format("Paid $%.2f via %s", amount, wallet.provider());
        } else if (method instanceof DigitalWallet wallet) {
            return String.format("%s wallet has insufficient funds", wallet.provider());
        }

        return "Unknown payment method";
    }

    // Complex nested patterns with different object types
    public static String analyzeData(Object data) {
        if (data instanceof String s && s.length() > 100) {
            return "Long text: " + s.substring(0, 50) + "...";
        } else if (data instanceof String s && s.matches("\\d+")) {
            return "Numeric string: " + s;
        } else if (data instanceof String s) {
            return "Short text: " + s;
        } else if (data instanceof Integer i && i > 1000) {
            return "Large number: " + i;
        } else if (data instanceof Integer i && i < 0) {
            return "Negative number: " + i;
        } else if (data instanceof Integer i) {
            return "Number: " + i;
        } else if (data instanceof List<?> list && list.isEmpty()) {
            return "Empty list";
        } else if (data instanceof List<?> list) {
            return "List with " + list.size() + " elements";
        }

        return "Unknown data type";
    }

    // Real-world example: Order processing with multiple validations
    public record Order(String id, double total, String customerType, int itemCount) {}

    public static String determineShipping(Order order) {
        if (order.customerType().equals("VIP") && order.total() > 1000) {
            return "FREE express shipping (VIP + large order)";
        } else if (order.customerType().equals("VIP")) {
            return "FREE standard shipping (VIP)";
        } else if (order.total() > 500 && order.itemCount() > 5) {
            return "FREE standard shipping (bulk order)";
        } else if (order.total() > 100) {
            return "Standard shipping: $10";
        } else {
            return "Standard shipping: $15";
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Payment Processing ===");
        PaymentMethod[] methods = {
            new CreditCard("1234-5678-9012-3456", "123", 1000.0),
            new DebitCard("9876-5432-1098-7654", "1234"),
            new Cash(50.0),
            new DigitalWallet("PayPal", "user@example.com", 200.0)
        };

        for (PaymentMethod method : methods) {
            System.out.println(processPayment(method, 75.0));
        }

        System.out.println("\n=== Over-limit Credit Card ===");
        CreditCard limitedCard = new CreditCard("1111-2222-3333-4444", "999", 50.0);
        System.out.println(processPayment(limitedCard, 75.0));

        System.out.println("\n=== Data Analysis ===");
        Object[] dataPoints = {
            "Hello",
            "12345",
            "A".repeat(150),
            42,
            -10,
            2000,
            List.of(),
            List.of("a", "b", "c")
        };

        for (Object data : dataPoints) {
            System.out.println(analyzeData(data));
        }

        System.out.println("\n=== Shipping Determination ===");
        Order[] orders = {
            new Order("O1", 1500.0, "VIP", 10),
            new Order("O2", 200.0, "VIP", 3),
            new Order("O3", 600.0, "REGULAR", 8),
            new Order("O4", 150.0, "REGULAR", 2),
            new Order("O5", 50.0, "REGULAR", 1)
        };

        for (Order order : orders) {
            System.out.printf("Order %s ($%.2f, %s): %s%n",
                order.id(), order.total(), order.customerType(), determineShipping(order));
        }
    }
}
