package dev.nmac.blog.examples.java17.part5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JSONTextBlocksExampleTest {

    @Test
    @DisplayName("Should produce identical JSON for traditional and text block")
    void shouldProduceIdenticalJSON() {
        String traditional = JSONTextBlocksExample.getTraditionalJSON();
        String textBlock = JSONTextBlocksExample.getTextBlockJSON();

        assertEquals(traditional, textBlock);
    }

    @Test
    @DisplayName("Should contain valid JSON structure")
    void shouldContainValidJSONStructure() {
        String json = JSONTextBlocksExample.getTextBlockJSON();

        assertTrue(json.contains("\"name\""));
        assertTrue(json.contains("\"age\""));
        assertTrue(json.contains("\"address\""));
    }

    @Test
    @DisplayName("Should format JSON with dynamic values")
    void shouldFormatJSONWithDynamicValues() {
        String json = JSONTextBlocksExample.getFormattedJSON("Test User", 99, "test@test.com");

        assertTrue(json.contains("\"Test User\""));
        assertTrue(json.contains("99"));
        assertTrue(json.contains("\"test@test.com\""));
    }

    @Test
    @DisplayName("Should handle nested JSON structures")
    void shouldHandleNestedJSONStructures() {
        String json = JSONTextBlocksExample.getComplexJSON();

        assertTrue(json.contains("\"users\""));
        assertTrue(json.contains("\"metadata\""));
        assertTrue(json.contains("\"roles\""));
    }

    @Test
    @DisplayName("Should preserve JSON array syntax")
    void shouldPreserveJSONArraySyntax() {
        String json = JSONTextBlocksExample.getComplexJSON();

        assertTrue(json.contains("[\"admin\", \"user\"]"));
    }
}
