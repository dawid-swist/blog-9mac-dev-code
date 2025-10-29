package dev.nmac.blog.examples.java17.part1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for VarLimitationsExample demonstrating limitations and pitfalls.
 *
 * Blog: blog.9mac.dev
 * Series: Java 17 Features Every Senior Developer Should Know
 * Part: 1 - Introduction and var keyword
 */
@DisplayName("var Limitations and Pitfalls")
class VarLimitationsExampleTest {

    @Test
    @DisplayName("Should infer specific type ArrayList instead of interface List")
    void shouldInferSpecificType() {
        var specificList = new ArrayList<String>();
        specificList.add("Java");

        assertTrue(specificList instanceof ArrayList);
        assertEquals(1, specificList.size());
    }

    @Test
    @DisplayName("Should allow explicit List type for implementation flexibility")
    void shouldAllowExplicitTypeForFlexibility() {
        List<String> flexibleList = new ArrayList<>();
        flexibleList.add("Java");
        flexibleList = new LinkedList<>(flexibleList);

        assertTrue(flexibleList instanceof LinkedList);
        assertEquals(1, flexibleList.size());
    }

    @Test
    @DisplayName("Should require type arguments when using var with diamond operator")
    void shouldRequireTypeArgumentsWithDiamond() {
        var list = new ArrayList<String>();
        list.add("Java");

        assertEquals(1, list.size());
        assertTrue(list.get(0) instanceof String);
    }

    @Test
    @DisplayName("Should work with factory methods instead of diamond operator")
    void shouldWorkWithFactoryMethods() {
        var list = List.of("Java", "Python", "Scala");

        assertEquals(3, list.size());
        assertTrue(list.contains("Java"));
    }

    @Test
    @DisplayName("Should infer int type for numeric literals instead of byte or short")
    void shouldInferIntTypeForNumericLiterals() {
        var byteValue = 127;
        var floatValue = 3.14;

        assertEquals(Integer.class, ((Object)byteValue).getClass());
        assertEquals(Double.class, ((Object)floatValue).getClass());
    }

    @Test
    @DisplayName("Should require explicit types for specific primitive sizes")
    void shouldRequireExplicitTypesForPrimitiveSizes() {
        byte actualByte = 127;
        short actualShort = 32000;
        long actualLong = 100L;
        float actualFloat = 3.14f;

        assertEquals(127, actualByte);
        assertEquals(32000, actualShort);
        assertEquals(100L, actualLong);
        assertEquals(3.14f, actualFloat, 0.001);
    }

    @Test
    @DisplayName("Should infer double type from ternary operator with int and double")
    void shouldInferDoubleFromTernaryOperator() {
        var result = true ? 42 : 3.14;

        assertEquals(Double.class, ((Object)result).getClass());
        assertEquals(42.0, result, 0.001);
    }

    @Test
    @DisplayName("Should infer intersection type from ternary with String and StringBuilder")
    void shouldInferIntersectionTypeFromTernary() {
        var result = true ? "String" : new StringBuilder("Builder");

        assertTrue(result instanceof CharSequence);
        assertTrue(result instanceof java.io.Serializable);
    }

    @Test
    @DisplayName("Should work with explicit type to avoid ternary ambiguity")
    void shouldWorkWithExplicitTypeForTernary() {
        String result = true ? "String" : new StringBuilder("Builder").toString();

        assertEquals("String", result);
        assertTrue(result instanceof String);
    }

    @Test
    @DisplayName("Should work with var in enhanced for-loop")
    void shouldWorkWithVarInEnhancedForLoop() {
        var numbers = List.of(1, 2, 3, 4, 5);
        var sum = 0;

        for (var num : numbers) {
            sum += num;
        }

        assertEquals(15, sum);
    }

    @Test
    @DisplayName("Should work with var in traditional for-loop")
    void shouldWorkWithVarInTraditionalForLoop() {
        var sum = 0;

        for (var i = 0; i < 5; i++) {
            sum += i;
        }

        assertEquals(10, sum);
    }

    @Test
    @DisplayName("Should infer Map.Entry type in foreach loop over Map")
    void shouldInferMapEntryType() {
        var map = Map.of("key1", "value1", "key2", "value2");
        var count = 0;

        for (var entry : map.entrySet()) {
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should work with array creation expression using var")
    void shouldWorkWithArrayCreationExpression() {
        var array = new int[]{1, 2, 3, 4, 5};

        assertEquals(5, array.length);
        assertEquals(1, array[0]);
        assertEquals(5, array[4]);
    }
}
