package dev.nmac.blog.examples.java17.part1;

import java.util.Comparator;
import java.util.List;

/**
 * Demonstrates how var enables access to extended API in anonymous classes.
 *
 * When using traditional type declarations with anonymous classes, you're
 * limited to the interface/class methods. With var, the compiler infers the
 * actual anonymous class type, giving you access to custom methods.
 *
 * Blog: blog.9mac.dev
 * Series: Java 17 Features Every Senior Developer Should Know
 * Part: 1 - Introduction and var keyword
 */
public class VarAnonymousClassExample {

    /**
     * Demonstrates the limitation of traditional anonymous class usage.
     */
    public static void traditionalAnonymousClass() {
        System.out.println("=== Traditional Anonymous Class (Limited Access) ===\n");

        // Traditional approach: type is Comparator<String>
        Comparator<String> traditional = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s2.length() - s1.length(); // Sort by length descending
            }

            // This method is part of the anonymous class but NOT accessible
            // through the Comparator<String> reference!
            public String getDescription() {
                return "Sorts strings by length in descending order";
            }
        };

        // We can use it as a Comparator
        var words = List.of("Java", "is", "awesome");
        var sorted = words.stream().sorted(traditional).toList();
        System.out.println("Sorted: " + sorted);

        // But we CANNOT call getDescription()
        // traditional.getDescription(); // ❌ Compilation error!
        System.out.println("❌ Cannot access getDescription() with traditional approach");
    }

    /**
     * Demonstrates how var enables access to custom methods in anonymous classes.
     */
    public static void varAnonymousClass() {
        System.out.println("\n=== var with Anonymous Class (Full Access) ===\n");

        // Using var: type is the actual anonymous class
        var enhanced = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s2.length() - s1.length(); // Sort by length descending
            }

            // Custom method accessible through var!
            public String getDescription() {
                return "Sorts strings by length in descending order";
            }

            // We can add multiple custom methods
            public String getAlgorithm() {
                return "Length-based comparison";
            }
        };

        // We can use it as a Comparator
        var words = List.of("Java", "is", "awesome", "and", "powerful");
        var sorted = words.stream().sorted(enhanced).toList();
        System.out.println("Sorted: " + sorted);

        // AND we can call custom methods! ✅
        System.out.println("Description: " + enhanced.getDescription());
        System.out.println("Algorithm: " + enhanced.getAlgorithm());
    }

    /**
     * Practical example: Stateful anonymous class with custom API.
     */
    public static void practicalExample() {
        System.out.println("\n=== Practical Example: Stateful Comparator ===\n");

        // Using var with a stateful anonymous class
        var statefulComparator = new Comparator<String>() {
            private int comparisonCount = 0;

            @Override
            public int compare(String s1, String s2) {
                comparisonCount++;
                return s1.compareToIgnoreCase(s2);
            }

            public int getComparisonCount() {
                return comparisonCount;
            }

            public void reset() {
                comparisonCount = 0;
            }

            public String getStatistics() {
                return String.format("Performed %d comparisons", comparisonCount);
            }
        };

        var words = List.of("Zebra", "apple", "Banana", "cherry");

        System.out.println("Before sorting: " + words);
        var sorted = words.stream().sorted(statefulComparator).toList();
        System.out.println("After sorting: " + sorted);

        // Access custom methods to get statistics
        System.out.println(statefulComparator.getStatistics());
        System.out.println("Total comparisons: " + statefulComparator.getComparisonCount());
    }

    /**
     * Main method demonstrating all examples.
     */
    public static void main(String[] args) {
        traditionalAnonymousClass();
        varAnonymousClass();
        practicalExample();

        System.out.println("\n=== Key Takeaway ===");
        System.out.println("var enables access to extended API in anonymous classes");
        System.out.println("This is useful for adding custom methods, state, and statistics");
        System.out.println("Without var, you're limited to the interface/base class methods");
    }
}
