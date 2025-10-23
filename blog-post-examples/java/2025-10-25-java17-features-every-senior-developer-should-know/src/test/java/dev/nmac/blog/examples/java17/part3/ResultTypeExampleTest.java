package dev.nmac.blog.examples.java17.part3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ResultTypeExample demonstrating sealed Result type for error handling.
 */
class ResultTypeExampleTest {

    @Test
    @DisplayName("Should create Success with valid value")
    void shouldCreateSuccessWithValidValue() {
        var result = Result.success("test");

        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());
        assertEquals("test", result.getValue());
    }

    @Test
    @DisplayName("Should create Failure with error message")
    void shouldCreateFailureWithErrorMessage() {
        var result = Result.<String>failure("Something went wrong");

        assertTrue(result.isFailure());
        assertFalse(result.isSuccess());
        assertEquals("Something went wrong", result.getError());
    }

    @Test
    @DisplayName("Should throw exception when accessing value on Failure")
    void shouldThrowExceptionWhenAccessingValueOnFailure() {
        var result = Result.<String>failure("Error");

        assertThrows(UnsupportedOperationException.class, result::getValue);
    }

    @Test
    @DisplayName("Should throw exception when accessing error on Success")
    void shouldThrowExceptionWhenAccessingErrorOnSuccess() {
        var result = Result.success("test");

        assertThrows(UnsupportedOperationException.class, result::getError);
    }

    @Test
    @DisplayName("Should map Success value")
    void shouldMapSuccessValue() {
        var result = Result.success(5)
            .map(x -> x * 2)
            .map(x -> "Value: " + x);

        assertTrue(result.isSuccess());
        assertEquals("Value: 10", result.getValue());
    }

    @Test
    @DisplayName("Should propagate Failure through map")
    void shouldPropagateFailureThroughMap() {
        var result = Result.<Integer>failure("Initial error")
            .map(x -> x * 2)
            .map(x -> "Value: " + x);

        assertTrue(result.isFailure());
        assertEquals("Initial error", result.getError());
    }

    @Test
    @DisplayName("Should flatMap Success results")
    void shouldFlatMapSuccessResults() {
        var result = Result.success(5)
            .flatMap(x -> Result.success(x * 2))
            .flatMap(x -> Result.success("Result: " + x));

        assertTrue(result.isSuccess());
        assertEquals("Result: 10", result.getValue());
    }

    @Test
    @DisplayName("Should propagate Failure through flatMap")
    void shouldPropagateFailureThroughFlatMap() {
        var result = Result.<Integer>failure("Initial error")
            .flatMap(x -> Result.success(x * 2));

        assertTrue(result.isFailure());
        assertEquals("Initial error", result.getError());
    }

    @Test
    @DisplayName("Should use orElse for Failure recovery")
    void shouldUseOrElseForFailureRecovery() {
        var result = Result.<String>failure("Error")
            .orElse(() -> Result.success("fallback"));

        assertTrue(result.isSuccess());
        assertEquals("fallback", result.getValue());
    }

    @Test
    @DisplayName("Should not use orElse for Success")
    void shouldNotUseOrElseForSuccess() {
        var result = Result.success("original")
            .orElse(() -> Result.success("fallback"));

        assertEquals("original", result.getValue());
    }

    @Test
    @DisplayName("Should find user by valid ID")
    void shouldFindUserByValidID() {
        var result = ResultTypeExample.findUserById(42);

        assertTrue(result.isSuccess());
        assertEquals("User42", result.getValue());
    }

    @Test
    @DisplayName("Should fail for invalid user ID")
    void shouldFailForInvalidUserID() {
        var result = ResultTypeExample.findUserById(-1);

        assertTrue(result.isFailure());
        assertTrue(result.getError().contains("Invalid user ID"));
    }

    @Test
    @DisplayName("Should fail for user not found")
    void shouldFailForUserNotFound() {
        var result = ResultTypeExample.findUserById(2000);

        assertTrue(result.isFailure());
        assertTrue(result.getError().contains("User not found"));
    }

    @Test
    @DisplayName("Should calculate discount for Premium user")
    void shouldCalculateDiscountForPremiumUser() {
        var result = ResultTypeExample.calculateDiscount("PremiumUser123");

        assertTrue(result.isSuccess());
        assertEquals(20, result.getValue());
    }

    @Test
    @DisplayName("Should calculate discount for Regular user")
    void shouldCalculateDiscountForRegularUser() {
        var result = ResultTypeExample.calculateDiscount("RegularUser456");

        assertTrue(result.isSuccess());
        assertEquals(10, result.getValue());
    }

    @Test
    @DisplayName("Should fail for unknown user category")
    void shouldFailForUnknownUserCategory() {
        var result = ResultTypeExample.calculateDiscount("GuestUser");

        assertTrue(result.isFailure());
        assertTrue(result.getError().contains("Unknown user category"));
    }

    @Test
    @DisplayName("Should chain operations successfully")
    void shouldChainOperationsSuccessfully() {
        var result = ResultTypeExample.findUserById(42)
            .map(String::toUpperCase)
            .map(name -> "Hello, " + name);

        assertTrue(result.isSuccess());
        assertEquals("Hello, USER42", result.getValue());
    }

    @Test
    @DisplayName("Should propagate error through chain")
    void shouldPropagateErrorThroughChain() {
        var result = ResultTypeExample.findUserById(2000)
            .flatMap(ResultTypeExample::calculateDiscount)
            .map(discount -> "Discount: " + discount + "%");

        assertTrue(result.isFailure());
        assertTrue(result.getError().contains("User not found"));
    }

    @Test
    @DisplayName("Should handle railway-oriented programming")
    void shouldHandleRailwayOrientedProgramming() {
        // findUserById(500) returns "User500", which doesn't match Premium/Regular
        // so calculateDiscount will fail - demonstrating error propagation
        var result = ResultTypeExample.findUserById(500)
            .flatMap(ResultTypeExample::calculateDiscount)
            .map(discount -> "Discount: " + discount + "%");

        assertTrue(result.isFailure());
        assertTrue(result.getError().contains("Unknown user category"));
    }

    @Test
    @DisplayName("Should throw exception for null Success value")
    void shouldThrowExceptionForNullSuccessValue() {
        assertThrows(IllegalArgumentException.class, () -> new Success<>(null));
    }
}
