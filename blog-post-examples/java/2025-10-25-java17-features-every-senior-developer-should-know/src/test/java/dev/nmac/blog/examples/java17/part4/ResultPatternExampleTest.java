package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dev.nmac.blog.examples.java17.part4.ResultPatternExample.*;

/**
 * Unit tests for ResultPatternExample demonstrating pattern matching with sealed types.
 *
 * These tests verify:
 * - Pattern matching handles both Success and Failure cases exhaustively
 * - Result<T> transformation with map preserves type safety
 * - Sealed types ensure compiler-enforced completeness
 */
class ResultPatternExampleTest {

    @Test
    @DisplayName("Should describe Success and Failure using pattern matching")
    void shouldDescribeResultTypes() {
        Result<String> success = new Success<>("Value");
        Result<String> failure = new Failure<>("Error");

        assertEquals("Success: Value", ResultPatternExample.describe(success));
        assertEquals("Failure: Error", ResultPatternExample.describe(failure));
    }

    @Test
    @DisplayName("Should transform Success and propagate Failure with map")
    void shouldMapSuccessAndPropagateFailure() {
        Result<Integer> success = new Success<>(10);
        Result<Integer> failure = new Failure<>("Parse error");

        Result<String> mappedSuccess = ResultPatternExample.map(success, n -> "Number: " + n);
        Result<String> mappedFailure = ResultPatternExample.map(failure, n -> "Number: " + n);

        assertEquals("Success: Number: 10", ResultPatternExample.describe(mappedSuccess));
        assertEquals("Failure: Parse error", ResultPatternExample.describe(mappedFailure));
    }

    @Test
    @DisplayName("Should handle parseNumber with valid and invalid inputs")
    void shouldParseNumberCorrectly() {
        assertEquals("Success: Number 42 is valid",
            ResultPatternExample.describe(ResultPatternExample.processNumber("42")));
        assertEquals("Failure: Negative numbers not allowed",
            ResultPatternExample.describe(ResultPatternExample.processNumber("-10")));
        assertTrue(ResultPatternExample.describe(ResultPatternExample.processNumber("abc"))
            .startsWith("Failure:"));
    }

    @Test
    @DisplayName("Should enforce exhaustiveness - compiler knows all Result cases")
    void shouldEnforceExhaustiveness() {
        // This test documents the compile-time guarantee:
        // Pattern matching on sealed Result<T> is exhaustive.
        // If we add a new Result type, all pattern matching code fails to compile.

        Result<String> success = new Success<>("test");
        Result<String> failure = new Failure<>("error");

        // Both cases handled - compiler ensures no missing cases
        String result1 = ResultPatternExample.describe(success);
        String result2 = ResultPatternExample.describe(failure);

        assertNotNull(result1);
        assertNotNull(result2);
    }
}
