package dev.nmac.blog.examples.java17.part5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EscapeSequencesExampleTest {

    @Test
    @DisplayName("Should handle standard escape sequences")
    void shouldHandleStandardEscapes() {
        String result = EscapeSequencesExample.getStandardEscapes();

        assertTrue(result.contains("\\n"));
        assertTrue(result.contains("\\t"));
        assertTrue(result.contains("\\\\"));
    }

    @Test
    @DisplayName("Should preserve essential space with backslash-s")
    void shouldPreserveEssentialSpace() {
        String result = EscapeSequencesExample.getEssentialSpace();

        assertTrue(result.contains("space:\\s"));
    }

    @Test
    @DisplayName("Should join lines with backslash continuation")
    void shouldJoinLinesWithBackslashContinuation() {
        String result = EscapeSequencesExample.getLineContinuation();

        assertTrue(result.contains("single line in the string"));
        assertFalse(result.contains("to \nsplit"));
    }

    @Test
    @DisplayName("Should escape triple quotes")
    void shouldEscapeTripleQuotes() {
        String result = EscapeSequencesExample.getTripleQuoteEscape();

        assertTrue(result.contains("He said:"));
        assertTrue(result.contains("Hello!"));
        assertTrue(result.contains("Hi there!"));
    }

    @Test
    @DisplayName("Should handle SQL with special characters")
    void shouldHandleSQLWithSpecialChars() {
        String result = EscapeSequencesExample.getSQLWithSpecialChars();

        assertTrue(result.contains("John\\'s Data"));
        assertTrue(result.contains("LIKE"));
    }
}
