package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dev.nmac.blog.examples.java17.part4.PatternSwitchExample.*;

/**
 * Unit tests for PatternSwitchExample demonstrating pattern matching in switch expressions.
 *
 * These tests verify:
 * - Type patterns: switch can match different types directly
 * - Guarded patterns: when clause adds conditions to patterns
 * - Null handling: null can be explicitly matched in switch
 * - Record patterns: records can be destructured in switch patterns
 * - Sealed types: exhaustiveness is enforced by compiler
 */
class PatternSwitchExampleTest {

    @Test
    @DisplayName("Type patterns: String type matched in switch")
    void shouldMatchStringType() {
        String result = PatternSwitchExample.processObject("Hello");

        assertTrue(result.contains("String"));
        assertTrue(result.contains("Hello"));
    }

    @Test
    @DisplayName("Type patterns: Integer type matched in switch")
    void shouldMatchIntegerType() {
        String result = PatternSwitchExample.processObject(42);

        assertTrue(result.contains("Integer"));
        assertTrue(result.contains("42"));
    }

    @Test
    @DisplayName("Type patterns: Null explicitly matched")
    void shouldMatchNullValue() {
        String result = PatternSwitchExample.processObject(null);

        assertEquals("Null value", result);
    }

    @Test
    @DisplayName("Guarded patterns: when clause filters empty strings")
    void shouldUseGuardedPatternForEmptyString() {
        String result = PatternSwitchExample.classifyString("");

        assertEquals("Empty string", result);
    }

    @Test
    @DisplayName("Guarded patterns: when clause filters short strings")
    void shouldUseGuardedPatternForShortString() {
        String result = PatternSwitchExample.classifyString("Hi");

        assertTrue(result.contains("Short string"));
        assertTrue(result.contains("Hi"));
    }

    @Test
    @DisplayName("Guarded patterns: when clause filters long strings")
    void shouldUseGuardedPatternForLongString() {
        String result = PatternSwitchExample.classifyString("This is a very long string");

        assertTrue(result.contains("Long string"));
    }

    @Test
    @DisplayName("Sealed types: exhaustive pattern matching for Shape")
    void shouldMatchAllShapeTypes() {
        int circleArea = PatternSwitchExample.getShapeArea(new Circle(5));
        int rectArea = PatternSwitchExample.getShapeArea(new Rectangle(10, 20));
        int triangleArea = PatternSwitchExample.getShapeArea(new Triangle(10, 15));

        assertTrue(circleArea > 0);
        assertEquals(200, rectArea);
        assertEquals(75, triangleArea);
    }

    @Test
    @DisplayName("Record patterns: destructure Point record in switch")
    void shouldDestructurePointRecord() {
        String quad1 = PatternSwitchExample.describePoint(new Point(3, 4));
        String quad2 = PatternSwitchExample.describePoint(new Point(-2, 5));
        String origin = PatternSwitchExample.describePoint(new Point(0, 0));

        assertTrue(quad1.contains("quadrant 1"));
        assertTrue(quad2.contains("quadrant 2"));
        assertTrue(origin.contains("origin"));
    }

    @Test
    @DisplayName("Record patterns with guards: on-axis detection")
    void shouldDetectPointsOnAxis() {
        String onXAxis = PatternSwitchExample.describePoint(new Point(5, 0));
        String onYAxis = PatternSwitchExample.describePoint(new Point(0, 3));

        assertTrue(onXAxis.contains("X-axis"));
        assertTrue(onYAxis.contains("Y-axis"));
    }
}
