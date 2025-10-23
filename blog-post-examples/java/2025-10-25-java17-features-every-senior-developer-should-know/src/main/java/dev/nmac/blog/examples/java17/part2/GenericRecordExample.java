package dev.nmac.blog.examples.java17.part2;

import java.util.List;
import java.util.function.Function;

/**
 * Generic Record Examples - Type Parameters in Records
 *
 * Demonstrates:
 * - Generic records with multiple type parameters
 * - Bounded type parameters
 * - Type inference with records
 * - Generic methods in records
 *
 * Generic records enable reusable, type-safe data structures.
 *
 * Blog: blog.9mac.dev
 * Series: Java 17 Features Every Senior Developer Should Know
 * Part: 2 - Records
 */
public class GenericRecordExample {

    /**
     * Generic pair record - holds two values of potentially different types.
     *
     * @param <T> type of first element
     * @param <U> type of second element
     */
    public record Pair<T, U>(T first, U second) {

        /**
         * Swaps the order of elements.
         *
         * @return new Pair with elements reversed
         */
        public Pair<U, T> swap() {
            return new Pair<>(second, first);
        }

        /**
         * Maps the first element using a function.
         *
         * @param <R> result type
         * @param mapper function to apply to first element
         * @return new Pair with transformed first element
         */
        public <R> Pair<R, U> mapFirst(Function<T, R> mapper) {
            return new Pair<>(mapper.apply(first), second);
        }
    }

    /**
     * Generic box with bounded type parameter.
     * Only accepts Number subtypes (Integer, Double, Long, etc.)
     *
     * @param <T> type parameter that must extend Number
     */
    public record Box<T extends Number>(T value) {

        /**
         * Returns the value as a double.
         * This works because T extends Number.
         *
         * @return double representation of the value
         */
        public double doubleValue() {
            return value.doubleValue();
        }

        /**
         * Returns the value as an int.
         *
         * @return int representation of the value
         */
        public int intValue() {
            return value.intValue();
        }

        /**
         * Checks if the value is zero.
         *
         * @return true if doubleValue() is zero
         */
        public boolean isZero() {
            return doubleValue() == 0.0;
        }

        /**
         * Static factory method for Integer boxes.
         *
         * @param value integer value
         * @return Box containing the integer
         */
        public static Box<Integer> ofInt(int value) {
            return new Box<>(value);
        }

        /**
         * Static factory method for Double boxes.
         *
         * @param value double value
         * @return Box containing the double
         */
        public static Box<Double> ofDouble(double value) {
            return new Box<>(value);
        }
    }

    /**
     * Triple - generic record with three components.
     *
     * @param <T> type of first element
     * @param <U> type of second element
     * @param <V> type of third element
     */
    public record Triple<T, U, V>(T first, U second, V third) {

        /**
         * Converts to a Pair by dropping the third element.
         *
         * @return Pair of first two elements
         */
        public Pair<T, U> toPair() {
            return new Pair<>(first, second);
        }
    }

    /**
     * Main method demonstrating generic records.
     */
    public static void main(String[] args) {
        System.out.println("=== Generic Record Examples ===\n");

        // Example 1: Pair with different types
        System.out.println("--- Pair Examples ---");
        var pair1 = new Pair<>("age", 30);
        System.out.println("String-Integer pair: " + pair1);
        System.out.println("First: " + pair1.first() + ", Second: " + pair1.second());

        var pair2 = new Pair<>(42, "answer");
        System.out.println("Integer-String pair: " + pair2);

        // Swap
        var swapped = pair1.swap();
        System.out.println("Swapped: " + swapped);

        // MapFirst
        var uppercase = pair1.mapFirst(String::toUpperCase);
        System.out.println("Mapped first to uppercase: " + uppercase);

        // Example 2: Box with bounded type
        System.out.println("\n--- Box with Bounded Type ---");
        var intBox = Box.ofInt(42);
        var doubleBox = Box.ofDouble(3.14);

        System.out.println("Integer box: " + intBox);
        System.out.println("  as double: " + intBox.doubleValue());
        System.out.println("  is zero? " + intBox.isZero());

        System.out.println("Double box: " + doubleBox);
        System.out.println("  as int: " + doubleBox.intValue());

        // Example 3: Triple
        System.out.println("\n--- Triple Examples ---");
        var triple = new Triple<>("status", 200, true);
        System.out.println("Triple: " + triple);
        System.out.println("As pair: " + triple.toPair());

        // Example 4: Nested generics
        System.out.println("\n--- Nested Generics ---");
        var nested = new Pair<>(new Pair<>(1, 2), "coordinates");
        System.out.println("Nested pair: " + nested);
        System.out.println("Inner first: " + nested.first().first());

        // Example 5: In collections
        System.out.println("\n--- Generic Records in Collections ---");
        var pairs = List.of(
            new Pair<>("one", 1),
            new Pair<>("two", 2),
            new Pair<>("three", 3)
        );
        System.out.println("List of pairs:");
        pairs.forEach(p -> System.out.println("  " + p));

        // Example 6: Type inference with var
        System.out.println("\n--- Type Inference ---");
        var inferred = new Pair<>("inferred", true);
        System.out.println("Inferred type Pair<String, Boolean>: " + inferred);

        System.out.println("\n=== Key Takeaways ===");
        System.out.println("✓ Records can be generic with type parameters");
        System.out.println("✓ Bounded types (T extends Number) work with records");
        System.out.println("✓ Generic records support methods and transformations");
        System.out.println("✓ Type inference works seamlessly with generic records");
    }
}
