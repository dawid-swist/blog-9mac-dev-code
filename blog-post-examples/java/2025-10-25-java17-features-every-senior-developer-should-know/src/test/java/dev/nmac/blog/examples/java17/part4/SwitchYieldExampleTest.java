package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dev.nmac.blog.examples.java17.part4.SwitchYieldExample.Operation;

/**
 * Unit tests for SwitchYieldExample demonstrating yield keyword in switch expressions.
 *
 * These tests verify:
 * - Arithmetic operations with yield return correct results
 * - Division by zero is handled appropriately
 * - Grade calculation works correctly for all ranges
 * - Nested switch expressions categorize data correctly
 * - Invalid inputs throw appropriate exceptions
 */
class SwitchYieldExampleTest {

    @Test
    @DisplayName("Should perform addition using yield")
    void shouldPerformAddition() {
        double result = SwitchYieldExample.calculate(Operation.ADD, 10, 5);

        assertEquals(15.0, result, 0.001);
    }

    @Test
    @DisplayName("Should perform subtraction using yield")
    void shouldPerformSubtraction() {
        double result = SwitchYieldExample.calculate(Operation.SUBTRACT, 10, 5);

        assertEquals(5.0, result, 0.001);
    }

    @Test
    @DisplayName("Should perform division and handle division by zero")
    void shouldPerformDivisionAndHandleDivisionByZero() {
        assertEquals(2.0, SwitchYieldExample.calculate(Operation.DIVIDE, 10, 5), 0.001);
        assertTrue(Double.isNaN(SwitchYieldExample.calculate(Operation.DIVIDE, 10, 0)));
    }

    @Test
    @DisplayName("Should calculate power using yield")
    void shouldCalculatePower() {
        double result = SwitchYieldExample.calculate(Operation.POWER, 2, 8);

        assertEquals(256.0, result, 0.001);
    }

    @Test
    @DisplayName("Should throw exception for modulo by zero")
    void shouldThrowExceptionForModuloByZero() {
        assertThrows(ArithmeticException.class,
            () -> SwitchYieldExample.calculate(Operation.MODULO, 10, 0));
    }

    @Test
    @DisplayName("Should assign grade A for scores 90-100")
    void shouldAssignGradeAForHighScores() {
        assertEquals("A", SwitchYieldExample.getGrade(95));
        assertEquals("A", SwitchYieldExample.getGrade(100));
        assertEquals("A", SwitchYieldExample.getGrade(90));
    }

    @Test
    @DisplayName("Should assign appropriate grades for various scores")
    void shouldAssignAppropriateGrades() {
        assertEquals("B", SwitchYieldExample.getGrade(85));
        assertEquals("C", SwitchYieldExample.getGrade(75));
        assertEquals("D", SwitchYieldExample.getGrade(65));
        assertEquals("F", SwitchYieldExample.getGrade(55));
    }

    @Test
    @DisplayName("Should throw exception for invalid scores")
    void shouldThrowExceptionForInvalidScores() {
        assertThrows(IllegalArgumentException.class, () -> SwitchYieldExample.getGrade(-1));
        assertThrows(IllegalArgumentException.class, () -> SwitchYieldExample.getGrade(101));
    }

    @Test
    @DisplayName("Should categorize numbers correctly in nested switch")
    void shouldCategorizeNumbersCorrectly() {
        assertEquals("Number category: single digit", SwitchYieldExample.categorize("number", 5));
        assertEquals("Number category: multiple digits", SwitchYieldExample.categorize("number", 42));
        assertEquals("Number category: negative", SwitchYieldExample.categorize("number", -10));
        assertEquals("Number category: zero", SwitchYieldExample.categorize("number", 0));
    }

    @Test
    @DisplayName("Should categorize priority levels correctly in nested switch")
    void shouldCategorizePriorityLevelsCorrectly() {
        assertEquals("Priority level: critical", SwitchYieldExample.categorize("priority", 1));
        assertEquals("Priority level: high", SwitchYieldExample.categorize("priority", 2));
        assertEquals("Priority level: medium", SwitchYieldExample.categorize("priority", 3));
        assertEquals("Priority level: low", SwitchYieldExample.categorize("priority", 5));
    }
}
