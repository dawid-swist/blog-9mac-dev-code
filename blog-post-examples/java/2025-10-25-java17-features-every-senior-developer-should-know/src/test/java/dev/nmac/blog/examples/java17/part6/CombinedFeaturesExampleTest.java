package dev.nmac.blog.examples.java17.part6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dev.nmac.blog.examples.java17.part6.CombinedFeaturesExample.*;

class CombinedFeaturesExampleTest {

    @Test
    @DisplayName("Should create User record with validation")
    void shouldCreateUserRecordWithValidation() {
        var user = new User("Alice", "alice@test.com", 30, UserType.PREMIUM);

        assertEquals("Alice", user.name());
        assertEquals(30, user.age());
        assertTrue(user.isAdult());
    }

    @Test
    @DisplayName("Should throw exception for negative age")
    void shouldThrowExceptionForNegativeAge() {
        assertThrows(IllegalArgumentException.class,
            () -> new User("Bob", "bob@test.com", -1, UserType.REGULAR));
    }

    @Test
    @DisplayName("Should describe user types with switch expression")
    void shouldDescribeUserTypesWithSwitchExpression() {
        assertEquals("Standard user with basic features",
            CombinedFeaturesExample.getUserTypeDescription(UserType.REGULAR));
        assertEquals("Premium user with enhanced features",
            CombinedFeaturesExample.getUserTypeDescription(UserType.PREMIUM));
        assertEquals("Administrator with full access",
            CombinedFeaturesExample.getUserTypeDescription(UserType.ADMIN));
    }

    @Test
    @DisplayName("Should process Success payment result with pattern matching")
    void shouldProcessSuccessPaymentResult() {
        var result = new Success("TX-12345", 99.99);

        String message = CombinedFeaturesExample.processPaymentResult(result);

        assertTrue(message.contains("successful"));
        assertTrue(message.contains("TX-12345"));
        assertTrue(message.contains("99.99"));
    }

    @Test
    @DisplayName("Should process Failure payment result with pattern matching")
    void shouldProcessFailurePaymentResult() {
        var result = new Failure("ERR-001", "Insufficient funds");

        String message = CombinedFeaturesExample.processPaymentResult(result);

        assertTrue(message.contains("failed"));
        assertTrue(message.contains("ERR-001"));
        assertTrue(message.contains("Insufficient funds"));
    }

    @Test
    @DisplayName("Should process Pending payment result with pattern matching")
    void shouldProcessPendingPaymentResult() {
        var result = new Pending("Awaiting confirmation");

        String message = CombinedFeaturesExample.processPaymentResult(result);

        assertTrue(message.contains("pending"));
        assertTrue(message.contains("Awaiting confirmation"));
    }

    @Test
    @DisplayName("Should calculate discount based on user type")
    void shouldCalculateDiscountBasedOnUserType() {
        var regularUser = new User("Bob", "bob@test.com", 25, UserType.REGULAR);
        var premiumUser = new User("Alice", "alice@test.com", 30, UserType.PREMIUM);
        var adminUser = new User("Charlie", "charlie@test.com", 35, UserType.ADMIN);
        var order = new Order("ORD-001", 50.0, OrderStatus.PENDING);

        assertEquals(0.0, CombinedFeaturesExample.calculateDiscount(regularUser, order));
        assertEquals(0.10, CombinedFeaturesExample.calculateDiscount(premiumUser, order));
        assertEquals(0.15, CombinedFeaturesExample.calculateDiscount(adminUser, order));
    }

    @Test
    @DisplayName("Should add extra discount for large orders")
    void shouldAddExtraDiscountForLargeOrders() {
        var premiumUser = new User("Alice", "alice@test.com", 30, UserType.PREMIUM);
        var largeOrder = new Order("ORD-001", 150.0, OrderStatus.PENDING);

        double discount = CombinedFeaturesExample.calculateDiscount(premiumUser, largeOrder);

        assertEquals(0.15, discount, 0.001); // 10% base + 5% large order
    }

    @Test
    @DisplayName("Should generate SQL query with text blocks")
    void shouldGenerateSQLQueryWithTextBlocks() {
        String query = CombinedFeaturesExample.getUserOrdersQuery(
            "USER-123", OrderStatus.COMPLETED);

        assertTrue(query.contains("SELECT"));
        assertTrue(query.contains("FROM users u"));
        assertTrue(query.contains("USER-123"));
        assertTrue(query.contains("COMPLETED"));
    }

    @Test
    @DisplayName("Should check if user is adult")
    void shouldCheckIfUserIsAdult() {
        var adult = new User("Alice", "alice@test.com", 18, UserType.REGULAR);
        var minor = new User("Bob", "bob@test.com", 17, UserType.REGULAR);

        assertTrue(adult.isAdult());
        assertFalse(minor.isAdult());
    }

    @Test
    @DisplayName("Should format payment result with deconstruction")
    void shouldFormatPaymentResultWithDeconstruction() {
        var success = new Success("TX-999", 150.50);

        String formatted = CombinedFeaturesExample.formatPaymentResult(success);

        assertTrue(formatted.contains("Success"));
        assertTrue(formatted.contains("TX-999"));
        assertTrue(formatted.contains("150.50"));
    }
}
