package dev.nmac.blog.examples.java17.part5;

/**
 * Demonstrates basic text block syntax and usage (JEP 378 - Java 15).
 *
 * Text blocks eliminate the need for string concatenation and escape sequences
 * when working with multi-line strings. They use triple-double-quote delimiters
 * and automatically handle indentation and line terminator normalization.
 */
public class BasicTextBlocksExample {

    // Traditional approach - verbose and error-prone
    public static String getTraditionalMultiline() {
        return "Line 1\n" +
               "Line 2\n" +
               "Line 3\n";
    }

    // Text block approach - clean and readable
    public static String getTextBlockMultiline() {
        return """
            Line 1
            Line 2
            Line 3
            """;
    }

    // Traditional poem with quotes
    public static String getTraditionalPoem() {
        return "Roses are \"red\",\n" +
               "Violets are \"blue\",\n" +
               "Java 15 is awesome,\n" +
               "And so are you!";
    }

    // Text block poem - no escape hell
    public static String getTextBlockPoem() {
        return """
            Roses are "red",
            Violets are "blue",
            Java 15 is awesome,
            And so are you!""";
    }

    // Demonstrating indentation control
    public static String getIndentedBlock() {
        return """
                This line has 4 spaces of indent
                    This line has 8 spaces
                Back to 4 spaces
                """;
    }

    public static void main(String[] args) {
        System.out.println("=== Traditional vs Text Block ===");
        System.out.println("Traditional:");
        System.out.println(getTraditionalMultiline());
        System.out.println("Text Block:");
        System.out.println(getTextBlockMultiline());

        System.out.println("\n=== Poem with Quotes ===");
        System.out.println("Traditional:");
        System.out.println(getTraditionalPoem());
        System.out.println("\nText Block:");
        System.out.println(getTextBlockPoem());

        System.out.println("\n=== Indented Block ===");
        System.out.println(getIndentedBlock());
    }
}
