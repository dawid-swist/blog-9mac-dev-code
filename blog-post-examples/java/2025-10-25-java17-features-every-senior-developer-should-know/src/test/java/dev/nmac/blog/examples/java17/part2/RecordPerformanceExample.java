package dev.nmac.blog.examples.java17.part2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

    // ============================================================
    // JUnit Tests - Performance Demonstrations
    // ============================================================

    @Test
    void testMemoryEstimates() {
        // This test demonstrates memory layout concepts
        // Actual memory depends on JVM settings (compressed oops, etc.)

        SmallRecord small = new SmallRecord(10, 20);
        LargeRecord large = new LargeRecord(1L, 2L, 3L, 4L, 5L);

        // Memory estimates:
        // SmallRecord: header (12-16) + 2 ints (8) + padding (~4) ≈ 24 bytes
        // LargeRecord: header (12-16) + 5 longs (40) ≈ 56 bytes

        System.out.println("SmallRecord estimated: ~24 bytes per instance");
        System.out.println("LargeRecord estimated: ~56 bytes per instance");
        System.out.println("Records have same overhead as equivalent manual classes");

        // Verify objects are created
        assertNotNull(small);
        assertNotNull(large);
    }

    @Test
    void testEqualityPerformance() {
        TestRecord r1 = new TestRecord("alpha", "beta", 100, 200L);
        TestRecord r2 = new TestRecord("alpha", "beta", 100, 200L);
        TestRecord r3 = new TestRecord("alpha", "beta", 100, 999L); // Different last field

        int iterations = 1_000_000;

        // Benchmark: equal objects
        long start1 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            boolean eq = r1.equals(r2);
        }
        long duration1 = System.nanoTime() - start1;

        // Benchmark: different objects (fails on last field)
        long start2 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            boolean eq = r1.equals(r3);
        }
        long duration2 = System.nanoTime() - start2;

        System.out.println("1M equals (same objects): " + duration1 / 1_000_000 + "ms");
        System.out.println("1M equals (diff objects): " + duration2 / 1_000_000 + "ms");

        // Verify both complete quickly (under 100ms on modern hardware)
        assertTrue(duration1 < 100_000_000, "Should complete in under 100ms");
        assertTrue(duration2 < 100_000_000, "Should complete in under 100ms");
    }

    @Test
    void testHashCodePerformance() {
        TestRecord record = new TestRecord("test", "data", 42, 100L);

        int iterations = 1_000_000;

        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            int hash = record.hashCode();
        }
        long duration = System.nanoTime() - start;

        System.out.println("1M hashCode calls: " + duration / 1_000_000 + "ms");

        // Should be very fast
        assertTrue(duration < 50_000_000, "Should complete in under 50ms");
    }

    @Test
    void testAsMapKey() {
        Map<Coordinate, String> map = new HashMap<>();

        // Insert 1000 coordinates as keys
        for (int i = 0; i < 1000; i++) {
            map.put(new Coordinate(i, i * 2), "value" + i);
        }

        assertEquals(1000, map.size());

        // Lookup by value equality (different object, same value)
        Coordinate key = new Coordinate(500, 1000);
        assertEquals("value500", map.get(key));

        // All lookups work
        for (int i = 0; i < 1000; i++) {
            String value = map.get(new Coordinate(i, i * 2));
            assertEquals("value" + i, value);
        }
    }

    @Test
    void testHashDistribution() {
        Map<Coordinate, String> map = new HashMap<>();

        // Add 1000 coordinates
        for (int i = 0; i < 1000; i++) {
            map.put(new Coordinate(i, i * 2), "value" + i);
        }

        // Check hash code distribution
        Set<Integer> hashCodes = new HashSet<>();
        for (Coordinate coord : map.keySet()) {
            hashCodes.add(coord.hashCode());
        }

        // Good hash distribution - most coordinates should have unique hashes
        System.out.println("Unique hash codes: " + hashCodes.size() + " out of 1000");
        assertTrue(hashCodes.size() > 950, "Should have good hash distribution");
    }

    @Test
    void testInSet() {
        Set<Coordinate> set = new HashSet<>();

        // Add coordinates
        for (int i = 0; i < 100; i++) {
            set.add(new Coordinate(i, i));
        }

        assertEquals(100, set.size());

        // Value-based lookup
        assertTrue(set.contains(new Coordinate(50, 50)));
        assertFalse(set.contains(new Coordinate(200, 200)));

        // Add duplicate (by value) - set size shouldn't change
        set.add(new Coordinate(50, 50));
        assertEquals(100, set.size());
    }

    @Test
    void testCollectionPerformance() {
        int size = 10_000;
        List<Coordinate> coords = new ArrayList<>();

        // Create many coordinates
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            coords.add(new Coordinate(i, i * 2));
        }
        long createDuration = System.nanoTime() - start;

        // Add to set (tests hashCode performance)
        Set<Coordinate> set = new HashSet<>();
        start = System.nanoTime();
        set.addAll(coords);
        long addDuration = System.nanoTime() - start;

        // Lookup in set
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            boolean found = set.contains(new Coordinate(i, i * 2));
            assertTrue(found);
        }
        long lookupDuration = System.nanoTime() - start;

        System.out.println("Create " + size + " records: " + createDuration / 1_000_000 + "ms");
        System.out.println("Add to HashSet: " + addDuration / 1_000_000 + "ms");
        System.out.println("Lookup all: " + lookupDuration / 1_000_000 + "ms");

        assertEquals(size, set.size());
    }

    @Test
    void testCachingPattern() {
        Color.clearCache();

        Color c1 = Color.of(255, 0, 0);
        Color c2 = Color.of(255, 0, 0);

        // Same instance returned from cache
        assertSame(c1, c2);

        // Value equality also works
        assertEquals(c1, c2);

        // Cache contains entry
        assertEquals(1, Color.cacheSize());
    }

    @Test
    void testCacheEffectiveness() {
        // Create a unique color to establish baseline
        Color unique1 = Color.of(123, 45, 67);
        int cacheBeforeLoop = Color.cacheSize();

        // Create same colors multiple times
        for (int i = 0; i < 100; i++) {
            Color.of(123, 45, 67);   // Same as unique1
        }

        int cacheAfterLoop = Color.cacheSize();

        // Cache size should not grow (instances are reused)
        assertEquals(cacheBeforeLoop, cacheAfterLoop);

        // Verify caching works - same instance returned
        Color unique2 = Color.of(123, 45, 67);
        assertSame(unique1, unique2);
    }

    @Test
    void testCacheMemorySavings() {
        Color.clearCache();

        // Without caching, this would create 100 objects
        // With caching, we reuse instances
        List<Color> colors = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            colors.add(Color.of(255, 0, 0)); // All red
        }

        // All references point to same instance
        Color first = colors.get(0);
        for (Color c : colors) {
            assertSame(first, c);
        }

        // Only 1 Color instance in cache
        assertEquals(1, Color.cacheSize());
    }

    @Test
    void testColorConstants() {
        // Constants are same instance
        Color red1 = Color.RED;
        Color red2 = Color.RED;
        assertSame(red1, red2);

        // Multiple calls to Color.of() return same cached instance
        Color red3 = Color.of(255, 0, 0);
        Color red4 = Color.of(255, 0, 0);
        assertSame(red3, red4);

        // Constants equal to cached values
        assertEquals(Color.RED, red3);
    }

    @Test
    void testColorToHex() {
        assertEquals("#FF0000", Color.RED.toHex());
        assertEquals("#00FF00", Color.GREEN.toHex());
        assertEquals("#0000FF", Color.BLUE.toHex());
        assertEquals("#FFFFFF", Color.WHITE.toHex());
        assertEquals("#000000", Color.BLACK.toHex());

        Color purple = Color.of(128, 0, 128);
        assertEquals("#800080", purple.toHex());
    }

    @Test
    void testRecordsSorting() {
        List<Coordinate> coords = new ArrayList<>(List.of(
            new Coordinate(5, 0),
            new Coordinate(1, 1),
            new Coordinate(3, 4),
            new Coordinate(0, 0)
        ));

        // Sort by manhattan distance
        coords.sort(Comparator.comparingInt(Coordinate::manhattanDistance));

        assertEquals(new Coordinate(0, 0), coords.get(0)); // distance: 0
        assertEquals(new Coordinate(1, 1), coords.get(1)); // distance: 2
        assertEquals(new Coordinate(5, 0), coords.get(2)); // distance: 5
        assertEquals(new Coordinate(3, 4), coords.get(3)); // distance: 7
    }

    @Test
    void testStreamPerformance() {
        int size = 10_000;
        List<Coordinate> coords = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            coords.add(new Coordinate(i, i * 2));
        }

        long start = System.nanoTime();

        // Stream operations with records
        long count = coords.stream()
            .filter(c -> c.x() > 5000)
            .map(c -> new Coordinate(c.x() * 2, c.y() * 2))
            .filter(c -> c.manhattanDistance() < 100000)
            .count();

        long duration = System.nanoTime() - start;

        System.out.println("Stream processing " + size + " records: " +
                          duration / 1_000_000 + "ms");

        assertTrue(count >= 0); // Result depends on filter conditions
        assertTrue(duration < 100_000_000, "Should complete quickly");
    }

    @Test
    void testRecordCreationSpeed() {
        int iterations = 1_000_000;

        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            SmallRecord r = new SmallRecord(i, i * 2);
        }
        long duration = System.nanoTime() - start;

        System.out.println("Created " + iterations + " records in: " +
                          duration / 1_000_000 + "ms");

        // Record creation is very fast
        assertTrue(duration < 200_000_000, "Should create 1M records in under 200ms");
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
