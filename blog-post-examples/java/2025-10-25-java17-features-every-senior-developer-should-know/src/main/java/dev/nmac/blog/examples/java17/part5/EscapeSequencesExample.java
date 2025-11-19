package dev.nmac.blog.examples.java17.part5;

/**
 * Demonstrates escape sequences in text blocks.
 *
 * Text blocks support all standard Java escape sequences plus two new ones:
 * \s for essential space and \ for line continuation without newlines.
 */
public class EscapeSequencesExample {

    // Standard escapes - all traditional escapes work in text blocks
    public static String getStandardEscapes() {
        return """
            Newline escape: \\n works here
            Tab escape: \\t indents like this
            Backslash: \\\\ is a single backslash
            Quote: \\" doesn't need escaping in text blocks
            """;
    }

    // Essential space (\s) - preserves trailing whitespace
    public static String getEssentialSpace() {
        return """
            Line ending with essential space:\\s
            Line without space
            Another line with space:\\s
            """;
    }

    // Line continuation (\) - joins lines without newline
    public static String getLineContinuation() {
        return """
            This is a long sentence that spans \
            multiple lines in source code but \
            appears as a single line in the string.""";
    }

    // Escaping triple quotes
    public static String getTripleQuoteEscape() {
        return """
            He said: \\"\\"\\"Hello!\\"\\"\\"
            And I replied: \\"\\"\\"Hi there!\\"\\"\\"
            """;
    }

    // Practical: SQL with special characters
    public static String getSQLWithSpecialChars() {
        return """
            SELECT * FROM table
            WHERE name = 'John\\'s Data'
            AND description LIKE '%\\\\%'
            """;
    }
}
