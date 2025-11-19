package dev.nmac.blog.examples.java17.part5;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SecureSQLExample {

    // Safe SQL: Text block with PreparedStatement placeholders (?)
    // Text blocks improve readability while PreparedStatement prevents SQL injection
    public static String getOrdersByStatusQuerySafe() {
        return """
            SELECT o.order_id, o.total, u.name, u.email
            FROM orders o
            JOIN users u ON o.user_id = u.id
            WHERE o.status = ?
              AND o.total >= ?
            ORDER BY o.created_at DESC
            LIMIT ?""";
    }

    // Complex CTE query demonstrating text block power with security
    // Common Table Expressions (CTEs) are highly readable with text blocks
    public static String getTopCustomersQuerySafe() {
        return """
            WITH customer_totals AS (
                SELECT u.id, u.name, u.email,
                       SUM(o.total) as total_spent,
                       COUNT(o.order_id) as order_count
                FROM users u
                JOIN orders o ON u.id = o.user_id
                WHERE o.status = ?
                  AND o.created_at >= ?
                GROUP BY u.id, u.name, u.email
            )
            SELECT id, name, email, total_spent, order_count
            FROM customer_totals
            WHERE total_spent >= ?
            ORDER BY total_spent DESC
            LIMIT ?""";
    }

    // Example execution method showing PreparedStatement usage
    // In production, this would execute against a real database
    public static List<String> executeOrderQuery(Connection conn, String status, double minAmount, int limit)
            throws SQLException {
        List<String> results = new ArrayList<>();
        String query = getOrdersByStatusQuerySafe();

        // PreparedStatement automatically escapes parameters - SAFE from SQL injection
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status);      // Parameter 1: status
            ps.setDouble(2, minAmount);   // Parameter 2: minAmount
            ps.setInt(3, limit);          // Parameter 3: limit

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(String.format("Order %s: $%.2f - %s (%s)",
                        rs.getString("order_id"),
                        rs.getDouble("total"),
                        rs.getString("name"),
                        rs.getString("email")));
                }
            }
        }
        return results;
    }

    public static void main(String[] args) {
        System.out.println("=== Safe SQL with PreparedStatement ===");
        System.out.println(getOrdersByStatusQuerySafe());

        System.out.println("\n=== Complex CTE Query ===");
        System.out.println(getTopCustomersQuerySafe());

        System.out.println("\n=== Key Points ===");
        System.out.println("1. Use ? placeholders instead of %s formatting");
        System.out.println("2. PreparedStatement.setString/setDouble/setInt for parameters");
        System.out.println("3. Text blocks still improve readability");
        System.out.println("4. Completely safe from SQL injection attacks");
    }
}
