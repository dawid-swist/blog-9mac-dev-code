package dev.nmac.blog.examples.java17.part2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dev.nmac.blog.examples.java17.part2.BasicRecordExample.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BasicRecordExample demonstrating basic record features.
 *
 * Blog: blog.9mac.dev
 * Series: Java 17 Features Every Senior Developer Should Know
 * Part: 2 - Records
 */
@DisplayName("Basic Records - Point and Range")
class BasicRecordExampleTest {

    @Test
    @DisplayName("Should create Point with x and y coordinates")
    void shouldCreatePointWithCoordinates() {
        var point = new Point(10, 20);

        assertEquals(10, point.x());
        assertEquals(20, point.y());
    }

    @Test
    @DisplayName("Should compare Points by value equality")
    void shouldComparePointsByValueEquality() {
        var p1 = new Point(10, 20);
        var p2 = new Point(10, 20);
        var p3 = new Point(10, 30);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);

        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("Should generate toString in record format")
    void shouldGenerateToStringInRecordFormat() {
        var point = new Point(10, 20);

        assertEquals("Point[x=10, y=20]", point.toString());
    }

    @Test
    @DisplayName("Should create different Point objects with same values")
    void shouldCreateDifferentPointObjectsWithSameValues() {
        var p1 = new Point(10, 20);
        var p2 = new Point(10, 20);

        assertNotSame(p1, p2);
        assertEquals(p1, p2);
    }

    @Test
    @DisplayName("Should create valid Range with start less than end")
    void shouldCreateValidRangeWithStartLessThanEnd() {
        var range = new Range(1, 10);

        assertEquals(1, range.start());
        assertEquals(10, range.end());
    }

    @Test
    @DisplayName("Should throw exception when creating Range with start greater than end")
    void shouldThrowExceptionWhenCreatingRangeWithStartGreaterThanEnd() {
        var exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Range(10, 1)
        );

        assertTrue(exception.getMessage().contains("start must not exceed end"));
    }

    @Test
    @DisplayName("Should check if Range contains value within bounds")
    void shouldCheckIfRangeContainsValueWithinBounds() {
        var range = new Range(1, 10);

        assertTrue(range.contains(1));
        assertTrue(range.contains(5));
        assertTrue(range.contains(10));

        assertFalse(range.contains(0));
        assertFalse(range.contains(11));
        assertFalse(range.contains(-5));
    }

    @Test
    @DisplayName("Should calculate Range length correctly")
    void shouldCalculateRangeLengthCorrectly() {
        var r1 = new Range(1, 10);
        assertEquals(9, r1.length());

        var r2 = new Range(0, 100);
        assertEquals(100, r2.length());

        var r3 = new Range(5, 5);
        assertEquals(0, r3.length());
    }

    @Test
    @DisplayName("Should compare Ranges by value equality")
    void shouldCompareRangesByValueEquality() {
        var r1 = new Range(1, 10);
        var r2 = new Range(1, 10);
        var r3 = new Range(1, 11);

        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
    }

    @Test
    @DisplayName("Should generate toString for Range in record format")
    void shouldGenerateToStringForRangeInRecordFormat() {
        var range = new Range(1, 10);

        assertEquals("Range[start=1, end=10]", range.toString());
    }

    @Test
    @DisplayName("Should demonstrate record immutability by creating new instances")
    void shouldDemonstrateRecordImmutabilityByCreatingNewInstances() {
        var p1 = new Point(10, 20);

        var p2 = new Point(30, p1.y());

        assertEquals(30, p2.x());
        assertEquals(20, p2.y());

        assertEquals(10, p1.x());
    }
}
