package dev.nmac.blog.examples.java17.part4;

/**
 * Demonstrates basic pattern matching for instanceof (JEP 394 - Java 16).
 *
 * Pattern matching eliminates the redundant test-and-cast ceremony that was
 * required in Java before version 16. Instead of checking the type with instanceof
 * and then manually casting, pattern matching combines both operations atomically.
 *
 * This example compares traditional instanceof usage with modern pattern matching
 * and demonstrates scope rules for pattern variables.
 */
public class BasicPatternMatchingExample {

    /**
     * Traditional approach - before Java 16.
     * Requires explicit casting after instanceof check.
     * Note the redundancy: we write the type name three times per branch.
     */
    public static String describeTraditional(Object obj) {
        if (obj instanceof String) {
            String s = (String) obj;  // Manual cast required
            return "String of length " + s.length();
        } else if (obj instanceof Integer) {
            Integer i = (Integer) obj;  // Repeated pattern
            return "Integer: " + i;
        } else if (obj instanceof Double) {
            Double d = (Double) obj;  // Same redundancy
            return "Double: " + d;
        } else {
            return "Unknown type: " + (obj != null ? obj.getClass().getSimpleName() : "null");
        }
    }

    /**
     * Modern approach - Java 16+ with pattern matching.
     * The pattern variable (s, i, d) is automatically cast and available in scope.
     * Much more concise and less error-prone.
     */
    public static String describeModern(Object obj) {
        if (obj instanceof String s) {
            // 's' is already cast to String here
            return "String of length " + s.length();
        } else if (obj instanceof Integer i) {
            return "Integer: " + i;
        } else if (obj instanceof Double d) {
            return "Double: " + d;
        } else {
            return "Unknown type: " + (obj != null ? obj.getClass().getSimpleName() : "null");
        }
    }

    /**
     * Demonstrates scope rules for pattern variables.
     *
     * Pattern variables are in scope only where the compiler can prove
     * the instanceof check succeeded. This uses flow analysis.
     */
    public static String demonstrateScope(Object obj) {
        // Pattern variable scope with && (logical AND)
        // 's' is in scope in the second part of the condition and in the if block
        if (obj instanceof String s && s.length() > 5) {
            return "Long string: " + s.toUpperCase();
        }

        // Pattern variable scope with negation
        // If obj is NOT a String, we enter the if block
        // If obj IS a String, we enter the else block where 's' is available
        if (!(obj instanceof String s)) {
            return "Not a string";
        } else {
            // 's' IS in scope here (obj is definitely String)
            return "String in else block: " + s;
        }
    }

    /**
     * Demonstrates null handling with pattern matching.
     *
     * instanceof returns false for null, so pattern variables are never
     * bound to null. This is safer than manual casting which required
     * explicit null checks.
     */
    public static String handleNull(Object obj) {
        if (obj instanceof String s) {
            return "String: " + s;
        }
        // This branch handles both null and non-String types
        return "null or not a String";
    }

    public static void main(String[] args) {
        System.out.println("=== Traditional vs Modern ===");
        Object[] values = {"Hello", 42, 3.14, null};

        for (Object val : values) {
            System.out.println("Traditional: " + describeTraditional(val));
            System.out.println("Modern:      " + describeModern(val));
            System.out.println();
        }

        System.out.println("=== Scope Demonstration ===");
        System.out.println(demonstrateScope("Hello"));           // Not a string (goes to else block)
        System.out.println(demonstrateScope("HelloWorld"));      // Long string: HELLOWORLD
        System.out.println(demonstrateScope(123));               // Not a string

        System.out.println("\n=== Null Handling ===");
        System.out.println(handleNull("test"));    // String: test
        System.out.println(handleNull(null));      // null or not a String
        System.out.println(handleNull(42));        // null or not a String
    }
}
