package dev.nmac.blog.examples.java17.part5;

/**
 * Demonstrates text blocks with JSON - the killer use case.
 *
 * Text blocks eliminate escape sequence hell when embedding JSON in Java code.
 * Combined with formatted(), they provide a clean alternative to JSON libraries
 * for simple cases like test fixtures and configuration templates.
 */
public class JSONTextBlocksExample {

    // Traditional JSON - escape sequence nightmare
    public static String getTraditionalJSON() {
        return "{\n" +
               "  \"name\": \"Alice Smith\",\n" +
               "  \"age\": 30,\n" +
               "  \"email\": \"alice@example.com\",\n" +
               "  \"address\": {\n" +
               "    \"street\": \"123 Main St\",\n" +
               "    \"city\": \"Springfield\"\n" +
               "  }\n" +
               "}";
    }

    // Text block JSON - clean and readable
    public static String getTextBlockJSON() {
        return """
            {
              "name": "Alice Smith",
              "age": 30,
              "email": "alice@example.com",
              "address": {
                "street": "123 Main St",
                "city": "Springfield"
              }
            }""";
    }

    // Using formatted() for dynamic JSON
    public static String getFormattedJSON(String name, int age, String email) {
        return """
            {
              "name": "%s",
              "age": %d,
              "email": "%s"
            }""".formatted(name, age, email);
    }

    // Complex nested JSON
    public static String getComplexJSON() {
        return """
            {
              "users": [
                {
                  "id": 1,
                  "name": "Alice",
                  "roles": ["admin", "user"]
                },
                {
                  "id": 2,
                  "name": "Bob",
                  "roles": ["user"]
                }
              ],
              "metadata": {
                "version": "1.0",
                "timestamp": "2024-01-15T10:30:00Z"
              }
            }""";
    }

    public static void main(String[] args) {
        System.out.println("=== Traditional JSON ===");
        System.out.println(getTraditionalJSON());

        System.out.println("\n=== Text Block JSON ===");
        System.out.println(getTextBlockJSON());

        System.out.println("\n=== Formatted JSON ===");
        System.out.println(getFormattedJSON("Charlie", 25, "charlie@example.com"));

        System.out.println("\n=== Complex JSON ===");
        System.out.println(getComplexJSON());
    }
}
