package dev.nmac.blog.examples.java17.part4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dev.nmac.blog.examples.java17.part4.EqualsPatternMatchingExample.*;

class EqualsPatternMatchingExampleTest {

    @Test
    @DisplayName("Should compare PointTraditional instances by value")
    void shouldComparePointTraditionalByValue() {
        PointTraditional p1 = new PointTraditional(10, 20);
        PointTraditional p2 = new PointTraditional(10, 20);
        PointTraditional p3 = new PointTraditional(10, 30);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotSame(p1, p2);
    }

    @Test
    @DisplayName("Should compare PointModern instances by value using pattern matching")
    void shouldComparePointModernByValue() {
        PointModern p1 = new PointModern(10, 20);
        PointModern p2 = new PointModern(10, 20);
        PointModern p3 = new PointModern(10, 30);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotSame(p1, p2);
    }

    @Test
    @DisplayName("Should have consistent hashCode for equal PointModern instances")
    void shouldHaveConsistentHashCodeForPointModern() {
        PointModern p1 = new PointModern(10, 20);
        PointModern p2 = new PointModern(10, 20);

        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("Should compare Person instances by all fields")
    void shouldComparePersonByAllFields() {
        Person p1 = new Person("Alice", "Smith", 30);
        Person p2 = new Person("Alice", "Smith", 30);
        Person p3 = new Person("Alice", "Smith", 31);
        Person p4 = new Person("Bob", "Smith", 30);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p1, p4);
    }

    @Test
    @DisplayName("Should handle null safely in pattern matching equals")
    void shouldHandleNullSafelyInPatternMatchingEquals() {
        SafeEquals s1 = new SafeEquals("test");

        assertFalse(s1.equals(null));
    }

    @Test
    @DisplayName("Should return false when comparing with different type")
    void shouldReturnFalseWhenComparingWithDifferentType() {
        PointModern point = new PointModern(10, 20);
        String notAPoint = "not a point";

        assertFalse(point.equals(notAPoint));
    }

    @Test
    @DisplayName("Should handle reflexivity in equals")
    void shouldHandleReflexivityInEquals() {
        PointModern p = new PointModern(10, 20);

        assertEquals(p, p);
    }

    @Test
    @DisplayName("Should handle symmetry in equals")
    void shouldHandleSymmetryInEquals() {
        PointModern p1 = new PointModern(10, 20);
        PointModern p2 = new PointModern(10, 20);

        assertEquals(p1, p2);
        assertEquals(p2, p1);
    }

    @Test
    @DisplayName("Should compare SafeEquals with null values correctly")
    void shouldCompareSafeEqualsWithNullValues() {
        SafeEquals s1 = new SafeEquals(null);
        SafeEquals s2 = new SafeEquals(null);
        SafeEquals s3 = new SafeEquals("test");

        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
    }
}
