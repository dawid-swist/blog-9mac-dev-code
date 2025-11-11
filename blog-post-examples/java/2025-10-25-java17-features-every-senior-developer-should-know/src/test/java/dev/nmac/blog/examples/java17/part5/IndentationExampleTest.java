package dev.nmac.blog.examples.java17.part5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IndentationExampleTest {

    @Test
    @DisplayName("Should strip common leading whitespace")
    void shouldStripCommonLeadingWhitespace() {
        String result = IndentationExample.getStripExample();

        assertTrue(result.startsWith("Line 1"));
        assertFalse(result.startsWith(" "));
    }

    @Test
    @DisplayName("Should preserve essential space with backslash-s")
    void shouldPreserveEssentialSpace() {
        String result = IndentationExample.getEssentialSpace();

        assertTrue(result.contains("space: \n"));
    }

    @Test
    @DisplayName("Should join lines with backslash continuation")
    void shouldJoinLinesWithBackslashContinuation() {
        String result = IndentationExample.getLineContinuation();

        // Should not contain multiple newlines where backslash was used
        assertFalse(result.contains("to \nsplit"));
        assertTrue(result.contains("to split"));
    }

    @Test
    @DisplayName("Should preserve relative indentation")
    void shouldPreserveRelativeIndentation() {
        String result = IndentationExample.getRelativeIndent();
        String[] lines = result.split("\n");

        // Check that level 2 lines have indentation relative to level 1
        assertTrue(lines[1].startsWith("    Level 2"));
        assertTrue(lines[2].startsWith("        Level 3"));
    }

    @Test
    @DisplayName("Should preserve empty lines")
    void shouldPreserveEmptyLines() {
        String result = IndentationExample.getEmptyLines();

        // Count newlines - should have blank lines between paragraphs
        long newlineCount = result.chars().filter(ch -> ch == '\n').count();
        assertTrue(newlineCount >= 5); // At least 5 newlines for 3 paragraphs with blank lines
    }

    @Test
    @DisplayName("Should handle mixed features correctly")
    void shouldHandleMixedFeaturesCorrectly() {
        String result = IndentationExample.getMixedExample();

        assertTrue(result.contains("Regular line"));
        assertTrue(result.contains("    Indented line"));
        assertTrue(result.contains("space: \n"));
        assertTrue(result.contains("Split across multiple source lines"));
    }
}
