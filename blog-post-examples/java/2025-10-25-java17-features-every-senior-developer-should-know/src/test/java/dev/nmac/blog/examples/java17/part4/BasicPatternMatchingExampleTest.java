package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BasicPatternMatchingExample demonstrating pattern matching for instanceof.
 *
 * These tests verify:
 * - Pattern matching correctly identifies and casts different types
 * - Null handling works as expected (instanceof returns false for null)
 * - Pattern variable scope rules are respected
 * - Traditional and modern approaches produce identical results
 */
class BasicPatternMatchingExampleTest {

    @Test
    @DisplayName("Should describe String type using pattern matching")
    void shouldDescribeStringType() {
        String result = BasicPatternMatchingExample.describeModern("Hello");

        assertEquals("String of length 5", result);
    }

    @Test
    @DisplayName("Should describe Integer type using pattern matching")
    void shouldDescribeIntegerType() {
        String result = BasicPatternMatchingExample.describeModern(42);

        assertEquals("Integer: 42", result);
    }

    @Test
    @DisplayName("Should describe Double type using pattern matching")
    void shouldDescribeDoubleType() {
        String result = BasicPatternMatchingExample.describeModern(3.14);

        assertEquals("Double: 3.14", result);
    }

    @Test
    @DisplayName("Should handle null input gracefully")
    void shouldHandleNullInput() {
        String result = BasicPatternMatchingExample.describeModern(null);

        assertEquals("Unknown type: null", result);
    }

    @Test
    @DisplayName("Should produce identical results for traditional and modern approaches")
    void shouldProduceIdenticalResultsForBothApproaches() {
        Object[] testValues = {"Hello", 42, 3.14, null, new Object()};

        for (Object val : testValues) {
            String traditional = BasicPatternMatchingExample.describeTraditional(val);
            String modern = BasicPatternMatchingExample.describeModern(val);

            assertEquals(traditional, modern,
                "Results should be identical for value: " + val);
        }
    }

    @Test
    @DisplayName("Should respect pattern variable scope with logical AND")
    void shouldRespectPatternVariableScopeWithAnd() {
        String result = BasicPatternMatchingExample.demonstrateScope("HelloWorld");

        assertEquals("Long string: HELLOWORLD", result);
    }

    @Test
    @DisplayName("Should handle short strings correctly in scope demonstration")
    void shouldHandleShortStringsInScopeDemo() {
        String result = BasicPatternMatchingExample.demonstrateScope("Hi");

        // Short string (length <= 5) fails the length check, goes to else block
        assertEquals("String in else block: Hi", result);
    }

    @Test
    @DisplayName("Should handle non-String types in scope demonstration")
    void shouldHandleNonStringInScopeDemo() {
        String result = BasicPatternMatchingExample.demonstrateScope(123);

        assertEquals("Not a string", result);
    }

    @Test
    @DisplayName("Should return false for null in instanceof check")
    void shouldReturnFalseForNullInInstanceof() {
        String result = BasicPatternMatchingExample.handleNull(null);

        assertEquals("null or not a String", result);
    }

    @Test
    @DisplayName("Should handle String in null handling method")
    void shouldHandleStringInNullHandlingMethod() {
        String result = BasicPatternMatchingExample.handleNull("test");

        assertEquals("String: test", result);
    }

    @Test
    @DisplayName("Should handle non-String in null handling method")
    void shouldHandleNonStringInNullHandlingMethod() {
        String result = BasicPatternMatchingExample.handleNull(42);

        assertEquals("null or not a String", result);
    }
}
