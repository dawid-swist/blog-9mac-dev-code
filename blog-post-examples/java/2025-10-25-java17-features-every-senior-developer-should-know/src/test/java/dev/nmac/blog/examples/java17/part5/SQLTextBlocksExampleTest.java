package dev.nmac.blog.examples.java17.part5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SQLTextBlocksExampleTest {

    @Test
    @DisplayName("Should produce identical SQL for traditional and text block")
    void shouldProduceIdenticalSQL() {
        String traditional = SQLTextBlocksExample.getTraditionalQuery();
        String textBlock = SQLTextBlocksExample.getTextBlockQuery();

        assertEquals(traditional, textBlock);
    }

    @Test
    @DisplayName("Should contain SQL keywords")
    void shouldContainSQLKeywords() {
        String query = SQLTextBlocksExample.getTextBlockQuery();

        assertTrue(query.contains("SELECT"));
        assertTrue(query.contains("FROM"));
        assertTrue(query.contains("JOIN"));
        assertTrue(query.contains("WHERE"));
        assertTrue(query.contains("ORDER BY"));
    }

    @Test
    @DisplayName("Should format query with parameters")
    void shouldFormatQueryWithParameters() {
        String query = SQLTextBlocksExample.getParameterizedQuery("SHIPPED", 75.5);

        assertTrue(query.contains("'SHIPPED'"));
        assertTrue(query.contains("75.50"));
    }

    @Test
    @DisplayName("Should support complex queries with CTEs")
    void shouldSupportComplexQueriesWithCTEs() {
        String query = SQLTextBlocksExample.getComplexQuery();

        assertTrue(query.contains("WITH user_stats AS"));
        assertTrue(query.contains("top_users AS"));
    }

    @Test
    @DisplayName("Should preserve query structure and indentation")
    void shouldPreserveQueryStructureAndIndentation() {
        String query = SQLTextBlocksExample.getTextBlockQuery();

        // Check for proper line structure
        assertTrue(query.contains("FROM users u"));
        assertTrue(query.contains("JOIN orders o"));
    }
}
