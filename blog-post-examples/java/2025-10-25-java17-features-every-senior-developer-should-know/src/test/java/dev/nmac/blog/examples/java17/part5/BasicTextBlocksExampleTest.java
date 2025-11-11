package dev.nmac.blog.examples.java17.part5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BasicTextBlocksExampleTest {

    @Test
    @DisplayName("Should produce identical output for traditional and text block multiline")
    void shouldProduceIdenticalOutputForMultiline() {
        String traditional = BasicTextBlocksExample.getTraditionalMultiline();
        String textBlock = BasicTextBlocksExample.getTextBlockMultiline();

        assertEquals(traditional, textBlock);
    }

    @Test
    @DisplayName("Should preserve quotes without escaping in text blocks")
    void shouldPreserveQuotesWithoutEscaping() {
        String textBlock = BasicTextBlocksExample.getTextBlockPoem();

        assertTrue(textBlock.contains("\"red\""));
        assertTrue(textBlock.contains("\"blue\""));
    }

    @Test
    @DisplayName("Should contain correct number of lines")
    void shouldContainCorrectNumberOfLines() {
        String multiline = BasicTextBlocksExample.getTextBlockMultiline();

        assertEquals(3, multiline.lines().count());
    }

    @Test
    @DisplayName("Should strip common leading whitespace")
    void shouldStripCommonLeadingWhitespace() {
        String indented = BasicTextBlocksExample.getIndentedBlock();

        assertTrue(indented.startsWith("This line"));
        assertFalse(indented.startsWith("    "));
    }

    @Test
    @DisplayName("Should preserve relative indentation")
    void shouldPreserveRelativeIndentation() {
        String indented = BasicTextBlocksExample.getIndentedBlock();
        String[] lines = indented.split("\n");

        // Second line should have 4 extra spaces
        assertTrue(lines[1].startsWith("    This line has 8"));
    }
}
