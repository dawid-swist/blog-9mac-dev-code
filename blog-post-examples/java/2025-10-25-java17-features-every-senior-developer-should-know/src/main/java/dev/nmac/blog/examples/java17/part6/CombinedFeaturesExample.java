package dev.nmac.blog.examples.java17.part6;

import java.util.List;

/**
 * Demonstrates all Java 10-17 features working together.
 *
 * This example combines:
 * - var (Java 10)
 * - Records (Java 16)
 * - Sealed classes (Java 17)
 * - Pattern matching (Java 16)
 * - Switch expressions (Java 14)
 * - Text blocks (Java 15)
 */
public class CombinedFeaturesExample {

    // Records for domain objects
    public record User(String name, String email, int age, UserType type) {
        public User {
            if (age < 0) throw new IllegalArgumentException("Age must be positive");
        }

        public boolean isAdult() {
            return age >= 18;
        }
    }

    public record Order(String id, double total, OrderStatus status) {}

    public enum UserType { REGULAR, PREMIUM, ADMIN }
    public enum OrderStatus { PENDING, PROCESSING, COMPLETED, CANCELLED }

    // Sealed type hierarchy for payment results
    public sealed interface PaymentResult permits Success, Failure, Pending {}

    public record Success(String transactionId, double amount) implements PaymentResult {}
    public record Failure(String errorCode, String message) implements PaymentResult {}
    public record Pending(String reason) implements PaymentResult {}

    // Text block for configuration template
    private static final String CONFIG_JSON = """
        {
          "version": "1.0",
          "features": {
            "var": true,
            "records": true,
            "sealed": true,
            "patternMatching": true,
            "switchExpressions": true,
            "textBlocks": true
          }
        }""";

    // Switch expression with enum
    public static String getUserTypeDescription(UserType type) {
        return switch (type) {
            case REGULAR -> "Standard user with basic features";
            case PREMIUM -> "Premium user with enhanced features";
            case ADMIN -> "Administrator with full access";
        };
    }

    // Pattern matching for instanceof
    public static String processPaymentResult(PaymentResult result) {
        if (result instanceof Success s) {
            return "Payment successful: $" + String.format("%.2f", s.amount()) + " (TX: " + s.transactionId() + ")";
        } else if (result instanceof Failure f) {
            return "Payment failed [" + f.errorCode() + "]: " + f.message();
        } else if (result instanceof Pending p) {
            return "Payment pending: " + p.reason();
        }
        return "Unknown result";
    }

    // Switch expression with pattern matching (future Java, simulated here)
    public static String formatPaymentResult(PaymentResult result) {
        // This uses instanceof pattern matching within if-else
        // Future Java versions will support pattern matching directly in switch
        if (result instanceof Success(var txId, var amount)) {
            return "Success: $" + String.format("%.2f", amount) + " (TX: " + txId + ")";
        } else if (result instanceof Failure(var code, var msg)) {
            return "Failed [" + code + "]: " + msg;
        } else if (result instanceof Pending(var reason)) {
            return "Pending: " + reason;
        }
        return "Unknown";
    }

    // Using var with records and collections
    public static void demonstrateVarWithRecords() {
        var users = List.of(
            new User("Alice", "alice@test.com", 30, UserType.PREMIUM),
            new User("Bob", "bob@test.com", 17, UserType.REGULAR),
            new User("Charlie", "charlie@test.com", 25, UserType.ADMIN)
        );

        System.out.println("=== Users ===");
        for (var user : users) {
            var typeDesc = getUserTypeDescription(user.type());
            var adultStatus = user.isAdult() ? "adult" : "minor";
            System.out.printf("%s (%s, %s): %s%n",
                user.name(), adultStatus, user.type(), typeDesc);
        }
    }

    // Text block for SQL query
    public static String getUserOrdersQuery(String userId, OrderStatus status) {
        return """
            SELECT
                u.name,
                u.email,
                o.id,
                o.total,
                o.status
            FROM users u
            JOIN orders o ON u.id = o.user_id
            WHERE u.id = '%s'
              AND o.status = '%s'
            ORDER BY o.created_at DESC""".formatted(userId, status);
    }

    // Combined: var + records + pattern matching + switch
    public static double calculateDiscount(User user, Order order) {
        var baseDiscount = switch (user.type()) {
            case REGULAR -> 0.0;
            case PREMIUM -> 0.10;
            case ADMIN -> 0.15;
        };

        // Pattern matching to check conditions
        if (order instanceof Order o && o.total() > 100 && user.isAdult()) {
            return baseDiscount + 0.05; // Extra 5% for large orders
        }

        return baseDiscount;
    }

    public static void main(String[] args) {
        System.out.println("=== Java 10-17 Features Combined ===\n");

        // Demonstrate var with records
        demonstrateVarWithRecords();

        System.out.println("\n=== Payment Results ===");
        var results = List.of(
            new Success("TX-12345", 99.99),
            new Failure("ERR-001", "Insufficient funds"),
            new Pending("Awaiting bank confirmation")
        );

        for (var result : results) {
            System.out.println(processPaymentResult(result));
        }

        System.out.println("\n=== Discount Calculation ===");
        var alice = new User("Alice", "alice@test.com", 30, UserType.PREMIUM);
        var order = new Order("ORD-001", 150.0, OrderStatus.PENDING);
        var discount = calculateDiscount(alice, order);
        System.out.printf("Discount for %s: %.0f%%%n", alice.name(), discount * 100);

        System.out.println("\n=== SQL Query ===");
        System.out.println(getUserOrdersQuery("USER-123", OrderStatus.COMPLETED));

        System.out.println("\n=== Config JSON ===");
        System.out.println(CONFIG_JSON);
    }
}
