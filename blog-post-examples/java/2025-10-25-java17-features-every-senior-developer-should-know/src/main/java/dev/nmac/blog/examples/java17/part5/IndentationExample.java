package dev.nmac.blog.examples.java17.part5;

/**
 * Demonstrates text block indentation rules and special escape sequences.
 *
 * Text blocks automatically strip common leading whitespace and handle
 * trailing whitespace. Special escapes \s and \ provide fine-grained control.
 */
public class IndentationExample {

    // Common indent stripping - closing delimiter determines indent level
    public static String getStripExample() {
        return """
            Line 1
            Line 2
            Line 3
            """;
    }

    // Closing delimiter aligned left - minimal stripping
    public static String getLeftAlignedClosing() {
        return """
    Indented line 1
    Indented line 2
""";
    }

    // Essential whitespace with \s
    public static String getEssentialSpace() {
        return """
            Line with trailing space:\s
            Another line
            """;
    }

    // Line continuation with backslash
    public static String getLineContinuation() {
        return """
            This is a very long line that we want to \
            split in source code but keep as \
            one line in the string.
            """;
    }

    // Relative indentation preserved
    public static String getRelativeIndent() {
        return """
            Level 1
                Level 2
                    Level 3
                Level 2
            Level 1
            """;
    }

    // Empty lines handling
    public static String getEmptyLines() {
        return """
            First paragraph.

            Second paragraph after blank line.

            Third paragraph.
            """;
    }

    // Mixed example with all features
    public static String getMixedExample() {
        return """
            Regular line
                Indented line
            Line with essential space:\s
            Split across \
            multiple source lines
            """;
    }

    public static void main(String[] args) {
        System.out.println("=== Strip Example ===");
        System.out.println("[" + getStripExample() + "]");

        System.out.println("\n=== Left Aligned Closing ===");
        System.out.println("[" + getLeftAlignedClosing() + "]");

        System.out.println("\n=== Essential Space ===");
        System.out.println("[" + getEssentialSpace() + "]");

        System.out.println("\n=== Line Continuation ===");
        System.out.println("[" + getLineContinuation() + "]");

        System.out.println("\n=== Relative Indent ===");
        System.out.println(getRelativeIndent());

        System.out.println("\n=== Empty Lines ===");
        System.out.println(getEmptyLines());

        System.out.println("\n=== Mixed Example ===");
        System.out.println("[" + getMixedExample() + "]");
    }
}
