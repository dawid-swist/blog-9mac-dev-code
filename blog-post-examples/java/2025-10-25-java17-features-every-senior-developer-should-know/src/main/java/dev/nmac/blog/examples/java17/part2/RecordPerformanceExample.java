package dev.nmac.blog.examples.java17.part2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Record Performance Examples - Understanding Performance Characteristics
 *
 * This example demonstrates:
 * - Memory layout and overhead estimation
 * - Equality and hashCode performance
 * - Collection performance with records
 * - Caching patterns for frequently used records
 * - Performance comparisons with traditional classes
 *
 * Note: These are simplified benchmarks for educational purposes.
 * Use JMH (Java Microbenchmarking Harness) for production benchmarks.
 */
public class RecordPerformanceExample {

    /**
     * Small record for memory layout demonstration.
     * Estimated memory: ~24 bytes
     * - Object header: 12-16 bytes
     * - int x: 4 bytes
     * - int y: 4 bytes
     * - Padding: ~4 bytes (8-byte alignment)
     */
    public record SmallRecord(int x, int y) {
    }

    /**
     * Larger record for comparison.
     * Estimated memory: ~56 bytes
     * - Object header: 12-16 bytes
     * - 5 longs: 40 bytes (5 × 8)
     * - Padding: minimal
     */
    public record LargeRecord(long a, long b, long c, long d, long e) {
    }

    /**
     * Record with multiple fields for equality testing.
     */
    public record TestRecord(String field1, String field2, int field3, long field4) {
    }

    /**
     * Coordinate record for collection performance tests.
     */
    public record Coordinate(int x, int y) {

        /**
         * Manhattan distance from origin.
         */
        public int manhattanDistance() {
            return Math.abs(x) + Math.abs(y);
        }
    }

    /**
     * Color record demonstrating caching pattern.
     */
    public record Color(int r, int g, int b) {

        private static final Map<String, Color> CACHE = new ConcurrentHashMap<>();

        /**
         * Compact constructor with validation.
         */
        public Color {
            if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                throw new IllegalArgumentException(
                    "RGB values must be 0-255: r=" + r + ", g=" + g + ", b=" + b
                );
            }
        }

        /**
         * Factory method with caching.
         * Returns cached instance if available.
         *
         * @param r red component (0-255)
         * @param g green component (0-255)
         * @param b blue component (0-255)
         * @return Color instance (possibly cached)
         */
        public static Color of(int r, int g, int b) {
            String key = r + "," + g + "," + b;
            return CACHE.computeIfAbsent(key, k -> new Color(r, g, b));
        }

        /**
         * Clears the cache (for testing).
         */
        public static void clearCache() {
            CACHE.clear();
        }

        /**
         * Returns cache size (for testing).
         */
        public static int cacheSize() {
            return CACHE.size();
        }

        // Common colors as constants
        public static final Color BLACK = Color.of(0, 0, 0);
        public static final Color WHITE = Color.of(255, 255, 255);
        public static final Color RED = Color.of(255, 0, 0);
        public static final Color GREEN = Color.of(0, 255, 0);
        public static final Color BLUE = Color.of(0, 0, 255);

        /**
         * Converts to hex string.
         *
         * @return hex representation (e.g., "#FF0000")
         */
        public String toHex() {
            return String.format("#%02X%02X%02X", r, g, b);
        }
    }

    // Main method for manual testing
    public static void main(String[] args) {
        System.out.println("=== Record Performance Examples ===\n");

        // Memory estimates
        System.out.println("--- Memory Layout ---");
        SmallRecord small = new SmallRecord(10, 20);
        LargeRecord large = new LargeRecord(1L, 2L, 3L, 4L, 5L);
        System.out.println("SmallRecord: ~24 bytes");
        System.out.println("LargeRecord: ~56 bytes");

        System.out.println("\n--- Equality Performance ---");
        TestRecord r1 = new TestRecord("alpha", "beta", 100, 200L);
        TestRecord r2 = new TestRecord("alpha", "beta", 100, 200L);

        int iterations = 1_000_000;
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            r1.equals(r2);
        }
        long duration = System.nanoTime() - start;
        System.out.println("1M equality checks: " + duration / 1_000_000 + "ms");

        System.out.println("\n--- HashMap Performance ---");
        Map<Coordinate, String> map = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            map.put(new Coordinate(i, i * 2), "value" + i);
        }
        System.out.println("Inserted 1000 records as keys");
        System.out.println("Lookup test: " + map.get(new Coordinate(500, 1000)));

        System.out.println("\n--- Caching Pattern ---");
        Color.clearCache();
        Color red1 = Color.of(255, 0, 0);
        Color red2 = Color.of(255, 0, 0);
        System.out.println("red1 == red2 (same instance): " + (red1 == red2));
        System.out.println("Cache size: " + Color.cacheSize());
        System.out.println("RED constant: " + Color.RED.toHex());

        System.out.println("\n--- Creation Speed ---");
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            new SmallRecord(i, i * 2);
        }
        duration = System.nanoTime() - start;
        System.out.println("Created " + iterations + " records in: " +
                          duration / 1_000_000 + "ms");
    }
}
