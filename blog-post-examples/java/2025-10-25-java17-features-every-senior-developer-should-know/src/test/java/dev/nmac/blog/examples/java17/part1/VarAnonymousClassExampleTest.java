package dev.nmac.blog.examples.java17.part1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for VarAnonymousClassExample demonstrating var with anonymous classes.
 *
 * Blog: blog.9mac.dev
 * Series: Java 17 Features Every Senior Developer Should Know
 * Part: 1 - Introduction and var keyword
 */
@DisplayName("var with Anonymous Classes")
class VarAnonymousClassExampleTest {

    @Test
    @DisplayName("Should enable access to custom methods in anonymous class through var")
    void shouldAccessCustomMethodInAnonymousClass() {
        var comparator = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s2.length() - s1.length();
            }

            public String getDescription() {
                return "Length descending comparator";
            }
        };

        assertEquals("Length descending comparator", comparator.getDescription());

        assertTrue(comparator.compare("short", "verylongword") > 0);
        assertTrue(comparator.compare("verylongword", "short") < 0);
    }

    @Test
    @DisplayName("Should sort strings using anonymous comparator with var")
    void shouldSortUsingAnonymousComparator() {
        var comparator = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s2.length() - s1.length();
            }

            public String getDescription() {
                return "Sorts by length descending";
            }
        };

        var words = List.of("Java", "is", "awesome");
        var sorted = words.stream().sorted(comparator).toList();

        assertEquals("awesome", sorted.get(0));
        assertEquals("Java", sorted.get(1));
        assertEquals("is", sorted.get(2));
    }

    @Test
    @DisplayName("Should track state in stateful anonymous class using var")
    void shouldTrackStateInAnonymousClass() {
        var statefulComparator = new Comparator<String>() {
            private int comparisonCount = 0;

            @Override
            public int compare(String s1, String s2) {
                comparisonCount++;
                return s1.compareTo(s2);
            }

            public int getComparisonCount() {
                return comparisonCount;
            }
        };

        var words = List.of("charlie", "alice", "bob");
        var sorted = words.stream().sorted(statefulComparator).toList();

        assertEquals(List.of("alice", "bob", "charlie"), sorted);
        assertTrue(statefulComparator.getComparisonCount() > 0);
    }

    @Test
    @DisplayName("Should support custom methods in Runnable anonymous class")
    void shouldWorkWithRunnableAnonymousClass() {
        var customRunnable = new Runnable() {
            private int executionCount = 0;

            @Override
            public void run() {
                executionCount++;
            }

            public int getExecutionCount() {
                return executionCount;
            }

            public void reset() {
                executionCount = 0;
            }
        };

        customRunnable.run();
        customRunnable.run();
        customRunnable.run();

        assertEquals(3, customRunnable.getExecutionCount());

        customRunnable.reset();
        assertEquals(0, customRunnable.getExecutionCount());
    }

    @Test
    @DisplayName("Should provide access to multiple custom methods and state tracking")
    void shouldDemonstrateMultipleCustomMethods() {
        var enhancedComparator = new Comparator<Integer>() {
            private int comparisonCount = 0;
            private int maxValue = Integer.MIN_VALUE;

            @Override
            public int compare(Integer i1, Integer i2) {
                comparisonCount++;
                maxValue = Math.max(maxValue, Math.max(i1, i2));
                return i1.compareTo(i2);
            }

            public int getComparisonCount() {
                return comparisonCount;
            }

            public int getMaxValue() {
                return maxValue;
            }

            public String getStatistics() {
                return String.format("Comparisons: %d, Max value seen: %d",
                    comparisonCount, maxValue);
            }
        };

        var numbers = List.of(5, 2, 8, 1, 9, 3);
        var sorted = numbers.stream().sorted(enhancedComparator).toList();

        assertEquals(List.of(1, 2, 3, 5, 8, 9), sorted);
        assertEquals(9, enhancedComparator.getMaxValue());
        assertTrue(enhancedComparator.getComparisonCount() > 0);
        assertNotNull(enhancedComparator.getStatistics());
    }
}
