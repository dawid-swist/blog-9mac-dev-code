package dev.nmac.blog.examples.java17.part2;

/**
 * Basic Record Examples - Introduction to Java Records
 *
 * Demonstrates:
 * - Basic record syntax
 * - Automatically generated methods (equals, hashCode, toString)
 * - Compact constructor with validation
 * - Custom instance methods
 *
 * Records provide immutable data carriers with minimal boilerplate.
 *
 * Blog: blog.9mac.dev
 * Series: Java 17 Features Every Senior Developer Should Know
 * Part: 2 - Records
 */
public class BasicRecordExample {

    /**
     * Basic record representing 2D coordinates.
     * The compiler generates:
     * - Private final fields: x, y
     * - Canonical constructor: Point(int x, int y)
     * - Accessor methods: x(), y()
     * - equals(), hashCode(), toString()
     */
    public record Point(int x, int y) {
    }

    /**
     * Record with validation using compact constructor.
     * The compact constructor runs before field initialization,
     * allowing validation and normalization.
     */
    public record Range(int start, int end) {

        /**
         * Compact constructor - validates range invariants.
         * No need to list parameters - they're implicit.
         * Field assignments happen automatically after this block.
         */
        public Range {
            if (start > end) {
                throw new IllegalArgumentException(
                    "start must not exceed end: start=" + start + ", end=" + end
                );
            }
        }

        /**
         * Custom method - checks if value is within range.
         *
         * @param value the value to check
         * @return true if value is within [start, end] inclusive
         */
        public boolean contains(int value) {
            return value >= start && value <= end;
        }

        /**
         * Returns the length of the range.
         *
         * @return end - start
         */
        public int length() {
            return end - start;
        }
    }

    /**
     * Main method demonstrating basic record usage.
     */
    public static void main(String[] args) {
        System.out.println("=== Basic Record Examples ===\n");

        // Example 1: Point - basic record
        System.out.println("--- Point Examples ---");
        var p1 = new Point(10, 20);
        var p2 = new Point(10, 20);
        var p3 = new Point(30, 40);

        System.out.println("Point p1: " + p1);
        System.out.println("Accessing components: x=" + p1.x() + ", y=" + p1.y());

        // Equality
        System.out.println("\nEquality (value-based):");
        System.out.println("p1.equals(p2): " + p1.equals(p2));  // true
        System.out.println("p1 == p2: " + (p1 == p2));           // false (different objects)
        System.out.println("p1.equals(p3): " + p1.equals(p3));  // false

        // Hash code
        System.out.println("\nHash codes:");
        System.out.println("p1.hashCode(): " + p1.hashCode());
        System.out.println("p2.hashCode(): " + p2.hashCode());  // Same as p1
        System.out.println("p3.hashCode(): " + p3.hashCode());  // Different

        // Example 2: Range - with compact constructor validation
        System.out.println("\n--- Range Examples ---");
        var r1 = new Range(1, 10);
        System.out.println("Range: " + r1);
        System.out.println("Contains 5? " + r1.contains(5));    // true
        System.out.println("Contains 15? " + r1.contains(15));  // false
        System.out.println("Length: " + r1.length());           // 9

        // Validation
        System.out.println("\nValidation in compact constructor:");
        try {
            var invalid = new Range(10, 1);  // start > end
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Expected error: " + e.getMessage());
        }

        // Example 3: Immutability
        System.out.println("\n--- Immutability ---");
        var original = new Point(100, 200);
        System.out.println("Original: " + original);

        // Cannot modify - must create new instance
        var modified = new Point(300, original.y());
        System.out.println("Modified: " + modified);
        System.out.println("Original unchanged: " + original);

        System.out.println("\n=== Key Takeaways ===");
        System.out.println("✓ Records provide immutable data carriers");
        System.out.println("✓ Compiler generates equals(), hashCode(), toString()");
        System.out.println("✓ Compact constructor enables validation");
        System.out.println("✓ Can add custom methods like contains() and length()");
    }
}
