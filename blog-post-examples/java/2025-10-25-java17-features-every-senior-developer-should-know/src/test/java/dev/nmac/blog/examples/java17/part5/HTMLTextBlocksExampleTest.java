package dev.nmac.blog.examples.java17.part5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HTMLTextBlocksExampleTest {

    @Test
    @DisplayName("Should generate valid HTML structure")
    void shouldGenerateValidHTML() {
        String html = HTMLTextBlocksExample.getDashboardHTML();

        assertTrue(html.contains("<html>"));
        assertTrue(html.contains("</html>"));
        assertTrue(html.contains("<h1>Welcome, User!</h1>"));
        assertTrue(html.contains("<p>This is your dashboard.</p>"));
    }

    @Test
    @DisplayName("Should generate valid SVG markup")
    void shouldGenerateValidSVG() {
        String svg = HTMLTextBlocksExample.getSVGCircle();

        assertTrue(svg.contains("<svg"));
        assertTrue(svg.contains("</svg>"));
        assertTrue(svg.contains("<circle"));
        assertTrue(svg.contains("<text"));
        assertTrue(svg.contains("SVG"));
    }
}
