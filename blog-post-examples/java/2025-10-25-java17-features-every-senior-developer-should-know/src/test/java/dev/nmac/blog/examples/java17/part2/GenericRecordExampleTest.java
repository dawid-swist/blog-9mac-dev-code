package dev.nmac.blog.examples.java17.part2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.nmac.blog.examples.java17.part2.GenericRecordExample.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for GenericRecordExample demonstrating generic records.
 *
 * Blog: blog.9mac.dev
 * Series: Java 17 Features Every Senior Developer Should Know
 * Part: 2 - Records
 */
@DisplayName("Generic Records - Pair, Box, and Triple")
class GenericRecordExampleTest {

    @Test
    @DisplayName("Should create generic Pair with different types")
    void shouldCreateGenericPairWithDifferentTypes() {
        var p1 = new Pair<>("age", 30);
        assertEquals("age", p1.first());
        assertEquals(30, p1.second());

        var p2 = new Pair<>(42, "answer");
        assertEquals(42, p2.first());
        assertEquals("answer", p2.second());
    }

    @Test
    @DisplayName("Should swap Pair elements")
    void shouldSwapPairElements() {
        var original = new Pair<>("key", 123);
        var swapped = original.swap();

        assertEquals(123, swapped.first());
        assertEquals("key", swapped.second());

        assertEquals("key", original.first());
    }

    @Test
    @DisplayName("Should map first element of Pair")
    void shouldMapFirstElementOfPair() {
        var pair = new Pair<>("hello", 5);

        var mapped = pair.mapFirst(String::toUpperCase);

        assertEquals("HELLO", mapped.first());
        assertEquals(5, mapped.second());

        var lengthPair = pair.mapFirst(String::length);
        assertEquals(5, lengthPair.first());
        assertEquals(5, lengthPair.second());
    }

    @Test
    @DisplayName("Should support nested Pairs")
    void shouldSupportNestedPairs() {
        var nested = new Pair<>(new Pair<>(1, 2), "coordinates");

        assertEquals(1, nested.first().first());
        assertEquals(2, nested.first().second());
        assertEquals("coordinates", nested.second());
    }

    @Test
    @DisplayName("Should create Box with bounded generic type")
    void shouldCreateBoxWithBoundedGenericType() {
        var intBox = new Box<>(42);
        var doubleBox = new Box<>(3.14);
        var longBox = new Box<>(1000000L);

        assertEquals(42.0, intBox.doubleValue());
        assertEquals(3.14, doubleBox.doubleValue(), 0.001);
        assertEquals(1000000.0, longBox.doubleValue());
    }

    @Test
    @DisplayName("Should convert Box value to int")
    void shouldConvertBoxValueToInt() {
        var box = new Box<>(42.7);
        assertEquals(42, box.intValue());

        var intBox = new Box<>(100);
        assertEquals(100, intBox.intValue());
    }

    @Test
    @DisplayName("Should check if Box value is zero")
    void shouldCheckIfBoxValueIsZero() {
        var zero = new Box<>(0);
        var nonZero = new Box<>(42);
        var doubleZero = new Box<>(0.0);

        assertTrue(zero.isZero());
        assertFalse(nonZero.isZero());
        assertTrue(doubleZero.isZero());
    }

    @Test
    @DisplayName("Should create Box using factory methods")
    void shouldCreateBoxUsingFactoryMethods() {
        var intBox = Box.ofInt(42);
        assertEquals(42, intBox.value());

        var doubleBox = Box.ofDouble(3.14);
        assertEquals(3.14, doubleBox.value(), 0.001);
    }

    @Test
    @DisplayName("Should infer generic types with var")
    void shouldInferGenericTypesWithVar() {
        var pair = new Pair<>("key", 123);

        assertEquals("key", pair.first());
        assertEquals(123, pair.second());

        var box = Box.ofInt(42);
        assertEquals(42, box.value());
    }

    @Test
    @DisplayName("Should create Triple with three components")
    void shouldCreateTripleWithThreeComponents() {
        var triple = new Triple<>("status", 200, true);

        assertEquals("status", triple.first());
        assertEquals(200, triple.second());
        assertTrue(triple.third());
    }

    @Test
    @DisplayName("Should convert Triple to Pair")
    void shouldConvertTripleToPair() {
        var triple = new Triple<>("key", 42, false);

        var pair = triple.toPair();

        assertEquals("key", pair.first());
        assertEquals(42, pair.second());
    }

    @Test
    @DisplayName("Should compare generic records by value equality")
    void shouldCompareGenericRecordsByValueEquality() {
        var p1 = new Pair<>("test", 1);
        var p2 = new Pair<>("test", 1);
        var p3 = new Pair<>("test", 2);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);

        var b1 = Box.ofInt(42);
        var b2 = Box.ofInt(42);
        assertEquals(b1, b2);
    }

    @Test
    @DisplayName("Should generate toString for generic records")
    void shouldGenerateToStringForGenericRecords() {
        var pair = new Pair<>("age", 30);
        assertEquals("Pair[first=age, second=30]", pair.toString());

        var box = Box.ofDouble(3.14);
        assertEquals("Box[value=3.14]", box.toString());
    }

    @Test
    @DisplayName("Should work in collections")
    void shouldWorkInCollections() {
        var pairs = List.of(
            new Pair<>("one", 1),
            new Pair<>("two", 2),
            new Pair<>("three", 3)
        );

        assertEquals(3, pairs.size());
        assertEquals("two", pairs.get(1).first());
    }
}
