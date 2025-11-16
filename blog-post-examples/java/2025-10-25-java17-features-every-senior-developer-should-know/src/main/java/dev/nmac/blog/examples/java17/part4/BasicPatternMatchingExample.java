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
     * Simple scope rules demonstration:
     * Pattern variables are only in scope where compiler proves they exist.
     */
    public static String demonstrateScope(Object obj) {
        // RULE 1: Pattern variable in scope after instanceof
        if (obj instanceof String s) {
            return "String: " + s;
        }

        // RULE 2: Pattern variable in scope with &&
        if (obj instanceof Integer i && i > 0) {
            return "Positive integer: " + i;
        }

        // RULE 3: In else block with negation
        return "Something else";
    }

    /**
     * Real-world case: Validate input if it's EITHER non-empty String OR valid default.
     *
     * BUSINESS LOGIC:
     * "Accept the input if it's a non-empty String OR it's a valid default value"
     *
     * You CANNOT write:
     *   if (input instanceof String s && !s.isEmpty() || isValidDefault(input)) {
     *       return process(s);  // ✗ COMPILER ERROR!
     *   }
     *
     * WHY IT FAILS:
     * - Left side (input instanceof String s && !s.isEmpty()) is TRUE → 's' is assigned
     * - Right side (isValidDefault(input)) is TRUE → 's' is NOT assigned
     * - The if-block executes when EITHER is true
     * - When right side is true, 's' doesn't exist!
     *
     * SOLUTION: Handle each case separately
     */
    public static String demonstrateOrProblem(Object input) {
        // Pattern 1: Non-empty String
        if (input instanceof String s && !s.isEmpty()) {
            return "Valid string: " + s;
        }

        // Pattern 2: Valid default
        if (isValidDefault(input)) {
            return "Valid default";
        }

        return "Invalid";
    }

    /**
     * Real case for instanceof with ||:
     * "Process if it's a String AND not empty, OR if it's from a trusted source"
     *
     * BUSINESS LOGIC:
     * - Accept non-empty Strings
     * - Also accept ANY non-String if it comes from a trusted source
     *
     * WRONG - does not compile:
     *   if (input instanceof String s && !s.isEmpty() || isTrustedSource()) {
     *       return process(s);  // ✗ COMPILER ERROR: 's' might not exist!
     *   }
     *
     * CORRECT - you CAN use || with boolean if you DON'T use the pattern variable:
     *   if (input instanceof String s && !s.isEmpty() || isTrustedSource()) {
     *       // Don't use 's' here - it might not be assigned
     *       return "Accepted from trusted source";
     *   }
     *
     * OR - separate the String logic from the boolean check:
     */
    public static String handleStringOrTrusted(Object input) {
        // Check 1: If it's a non-empty String, use it directly
        if (input instanceof String s && !s.isEmpty()) {
            return "Processed string: " + s;
        }

        // Check 2: If NOT a String but from trusted source, accept anyway
        if (isTrustedSource(input)) {
            return "Accepted from trusted source";
        }

        return "Rejected";
    }

    private static boolean isTrustedSource(Object obj) {
        // Example: only allow Integer from trusted sources
        return obj instanceof Integer;
    }

    private static boolean isSpecialValue(Object obj) {
        return obj instanceof String s && s.startsWith("S");
    }

    private static boolean isValidDefault(Object obj) {
        return obj != null && (obj instanceof Integer || obj instanceof Boolean);
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

        System.out.println("=== Scope Rules ===");
        System.out.println(demonstrateScope("Hello"));           // String: Hello
        System.out.println(demonstrateScope(42));                // Positive integer: 42
        System.out.println(demonstrateScope(-5));                // Something else

        System.out.println("\n=== OR Operator with Pattern Variables ===");
        System.out.println("Case 1: String AND valid OR default type:");
        System.out.println(demonstrateOrProblem("Hello"));           // Valid string: Hello
        System.out.println(demonstrateOrProblem(""));                // Invalid (empty string)
        System.out.println(demonstrateOrProblem(42));                // Valid default (Integer)
        System.out.println(demonstrateOrProblem(true));              // Valid default (Boolean)
        System.out.println(demonstrateOrProblem(3.14));              // Invalid (Double)

        System.out.println("\nCase 2: String AND non-empty OR trusted source:");
        System.out.println(handleStringOrTrusted("Hello"));          // Processed string: Hello
        System.out.println(handleStringOrTrusted(""));               // Rejected (empty string)
        System.out.println(handleStringOrTrusted(42));               // Accepted from trusted source (Integer)
        System.out.println(handleStringOrTrusted(3.14));             // Rejected (not trusted source)

        System.out.println("\n=== Null Handling ===");
        System.out.println(handleNull("test"));    // String: test
        System.out.println(handleNull(null));      // null or not a String
        System.out.println(handleNull(42));        // null or not a String
    }
}
