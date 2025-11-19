package dev.nmac.blog.examples.java17.part5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SecureSQLExampleTest {

    @Test
    @DisplayName("Should use PreparedStatement placeholders instead of formatted strings")
    void shouldUsePreparedStatementPlaceholders() {
        String query = SecureSQLExample.getOrdersByStatusQuerySafe();

        // Should contain ? placeholders for PreparedStatement
        assertTrue(query.contains("?"), "Query should use ? placeholders for parameters");

        // Count the number of placeholders (should be 3: status, minAmount, limit)
        long placeholderCount = query.chars().filter(ch -> ch == '?').count();
        assertEquals(3, placeholderCount, "Query should have exactly 3 parameter placeholders");

        // Should NOT contain %s or other format specifiers (unsafe pattern)
        assertFalse(query.contains("%s"), "Query should not use %s format specifiers");
        assertFalse(query.contains("%d"), "Query should not use %d format specifiers");
        assertFalse(query.contains("%f"), "Query should not use %f format specifiers");
    }

    @Test
    @DisplayName("Should contain essential SQL structure for orders query")
    void shouldContainEssentialSQLStructure() {
        String query = SecureSQLExample.getOrdersByStatusQuerySafe();

        assertTrue(query.contains("SELECT"), "Query should have SELECT clause");
        assertTrue(query.contains("FROM orders o"), "Query should select from orders table");
        assertTrue(query.contains("JOIN users u"), "Query should join with users table");
        assertTrue(query.contains("WHERE"), "Query should have WHERE clause");
        assertTrue(query.contains("ORDER BY"), "Query should have ORDER BY clause");
        assertTrue(query.contains("LIMIT"), "Query should have LIMIT clause");
    }

    @Test
    @DisplayName("Should support complex CTE queries with safe parameterization")
    void shouldSupportComplexCTEQueriesWithSafeParameterization() {
        String query = SecureSQLExample.getTopCustomersQuerySafe();

        // Verify CTE structure
        assertTrue(query.contains("WITH customer_totals AS"), "Query should use CTE");
        assertTrue(query.contains("SELECT id, name, email, total_spent, order_count"),
            "Query should select aggregated data");

        // Count placeholders (should be 4: status, created_at, total_spent, limit)
        long placeholderCount = query.chars().filter(ch -> ch == '?').count();
        assertEquals(4, placeholderCount, "CTE query should have exactly 4 parameter placeholders");

        // Verify no unsafe formatting
        assertFalse(query.contains("%"), "Query should not use format specifiers");
    }

    @Test
    @DisplayName("Should preserve readability with proper indentation")
    void shouldPreserveReadabilityWithProperIndentation() {
        String query = SecureSQLExample.getTopCustomersQuerySafe();

        // Check for proper structure (CTE should be indented)
        assertTrue(query.contains("    SELECT u.id"), "CTE content should be indented");
        assertTrue(query.contains("GROUP BY u.id"), "GROUP BY should be present");
    }

    @Test
    @DisplayName("Should demonstrate safe SQL pattern compared to unsafe formatted strings")
    void shouldDemonstrateSafeSQLPattern() {
        String safeQuery = SecureSQLExample.getOrdersByStatusQuerySafe();

        // Safe pattern uses ? placeholders
        assertTrue(safeQuery.contains("WHERE o.status = ?"),
            "Safe query should use ? for status parameter");
        assertTrue(safeQuery.contains("AND o.total >= ?"),
            "Safe query should use ? for amount parameter");

        // This test validates that the example follows security best practices
        // In production, these would be set using PreparedStatement.setString(), etc.
    }
}
