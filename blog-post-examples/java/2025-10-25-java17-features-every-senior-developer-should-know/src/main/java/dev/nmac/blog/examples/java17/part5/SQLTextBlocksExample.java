package dev.nmac.blog.examples.java17.part5;

/**
 * Demonstrates text blocks with SQL queries.
 *
 * Text blocks make SQL queries dramatically more readable by preserving
 * the natural structure of the query without concatenation or escapes.
 */
public class SQLTextBlocksExample {

    // Traditional SQL - concatenation hell
    public static String getTraditionalQuery() {
        return "SELECT u.id, u.name, u.email,\n" +
               "       o.order_id, o.total, o.status\n" +
               "FROM users u\n" +
               "JOIN orders o ON u.id = o.user_id\n" +
               "WHERE o.status = 'COMPLETED'\n" +
               "  AND o.total > 100\n" +
               "ORDER BY o.created_at DESC\n" +
               "LIMIT 10";
    }

    // Text block SQL - clean and maintainable
    public static String getTextBlockQuery() {
        return """
            SELECT u.id, u.name, u.email,
                   o.order_id, o.total, o.status
            FROM users u
            JOIN orders o ON u.id = o.user_id
            WHERE o.status = 'COMPLETED'
              AND o.total > 100
            ORDER BY o.created_at DESC
            LIMIT 10""";
    }

    // Parameterized query with formatted()
    public static String getParameterizedQuery(String status, double minTotal) {
        return """
            SELECT u.id, u.name, u.email,
                   o.order_id, o.total, o.status
            FROM users u
            JOIN orders o ON u.id = o.user_id
            WHERE o.status = '%s'
              AND o.total > %.2f
            ORDER BY o.created_at DESC""".formatted(status, minTotal);
    }

    // Complex query with CTEs
    public static String getComplexQuery() {
        return """
            WITH user_stats AS (
                SELECT user_id,
                       COUNT(*) as order_count,
                       SUM(total) as total_spent
                FROM orders
                WHERE status = 'COMPLETED'
                GROUP BY user_id
            ),
            top_users AS (
                SELECT user_id
                FROM user_stats
                WHERE total_spent > 1000
                ORDER BY total_spent DESC
                LIMIT 100
            )
            SELECT u.name, u.email,
                   us.order_count, us.total_spent
            FROM users u
            JOIN top_users tu ON u.id = tu.user_id
            JOIN user_stats us ON u.id = us.user_id
            ORDER BY us.total_spent DESC""";
    }

    public static void main(String[] args) {
        System.out.println("=== Traditional Query ===");
        System.out.println(getTraditionalQuery());

        System.out.println("\n=== Text Block Query ===");
        System.out.println(getTextBlockQuery());

        System.out.println("\n=== Parameterized Query ===");
        System.out.println(getParameterizedQuery("PENDING", 50.0));

        System.out.println("\n=== Complex Query with CTEs ===");
        System.out.println(getComplexQuery());
    }
}
