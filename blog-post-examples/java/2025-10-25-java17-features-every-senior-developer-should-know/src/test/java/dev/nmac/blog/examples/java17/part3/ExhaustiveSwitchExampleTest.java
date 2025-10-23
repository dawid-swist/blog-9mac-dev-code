package dev.nmac.blog.examples.java17.part3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ExhaustiveSwitchExample demonstrating exhaustive switch with sealed types.
 */
class ExhaustiveSwitchExampleTest {

    @Test
    @DisplayName("Should create JSONString and convert to JSON")
    void shouldCreateJSONStringAndConvertToJSON() {
        var json = new JSONString("hello");

        assertEquals("\"hello\"", json.toJson());
    }

    @Test
    @DisplayName("Should create JSONNumber and convert to JSON")
    void shouldCreateJSONNumberAndConvertToJSON() {
        var json = new JSONNumber(42.5);

        assertEquals("42.5", json.toJson());
    }

    @Test
    @DisplayName("Should create JSONBoolean and convert to JSON")
    void shouldCreateJSONBooleanAndConvertToJSON() {
        var jsonTrue = new JSONBoolean(true);
        var jsonFalse = new JSONBoolean(false);

        assertEquals("true", jsonTrue.toJson());
        assertEquals("false", jsonFalse.toJson());
    }

    @Test
    @DisplayName("Should create JSONNull and convert to JSON")
    void shouldCreateJSONNullAndConvertToJSON() {
        var json = JSONNull.INSTANCE;

        assertEquals("null", json.toJson());
    }

    @Test
    @DisplayName("Should create JSONArray and convert to JSON")
    void shouldCreateJSONArrayAndConvertToJSON() {
        var json = new JSONArray(List.of(
            new JSONNumber(1),
            new JSONNumber(2),
            new JSONNumber(3)
        ));

        assertEquals("[1.0,2.0,3.0]", json.toJson());
    }

    @Test
    @DisplayName("Should create JSONObject and convert to JSON")
    void shouldCreateJSONObjectAndConvertToJSON() {
        var json = new JSONObject(Map.of(
            "name", new JSONString("John"),
            "age", new JSONNumber(30)
        ));

        var jsonString = json.toJson();
        assertTrue(jsonString.contains("\"name\":\"John\""));
        assertTrue(jsonString.contains("\"age\":30.0"));
    }

    @Test
    @DisplayName("Should describe JSON types using exhaustive switch")
    void shouldDescribeJSONTypesUsingExhaustiveSwitch() {
        assertEquals("string: \"test\"",
            ExhaustiveSwitchExample.describeType(new JSONString("test")));
        assertEquals("number: 42.0",
            ExhaustiveSwitchExample.describeType(new JSONNumber(42)));
        assertEquals("boolean: true",
            ExhaustiveSwitchExample.describeType(new JSONBoolean(true)));
        assertEquals("null value",
            ExhaustiveSwitchExample.describeType(JSONNull.INSTANCE));
    }

    @Test
    @DisplayName("Should describe JSONArray with element count")
    void shouldDescribeJSONArrayWithElementCount() {
        var array = new JSONArray(List.of(
            new JSONNumber(1),
            new JSONNumber(2),
            new JSONNumber(3)
        ));

        var description = ExhaustiveSwitchExample.describeType(array);
        assertTrue(description.contains("array with 3 elements"));
    }

    @Test
    @DisplayName("Should describe JSONObject with property count")
    void shouldDescribeJSONObjectWithPropertyCount() {
        var obj = new JSONObject(Map.of(
            "name", new JSONString("John"),
            "age", new JSONNumber(30)
        ));

        var description = ExhaustiveSwitchExample.describeType(obj);
        assertTrue(description.contains("object with 2 properties"));
    }

    @Test
    @DisplayName("Should estimate JSON size correctly")
    void shouldEstimateJSONSizeCorrectly() {
        assertEquals(6, ExhaustiveSwitchExample.estimateSize(new JSONString("test"))); // "test"
        assertEquals(4, ExhaustiveSwitchExample.estimateSize(JSONNull.INSTANCE)); // null
        assertEquals(4, ExhaustiveSwitchExample.estimateSize(new JSONBoolean(true))); // true
    }

    @Test
    @DisplayName("Should estimate JSONArray size correctly")
    void shouldEstimateJSONArraySizeCorrectly() {
        var array = new JSONArray(List.of(
            new JSONString("a"),
            new JSONString("b")
        ));

        // ["a","b"] = 3 + 3 + 2 (brackets) = 8
        assertEquals(8, ExhaustiveSwitchExample.estimateSize(array));
    }

    @Test
    @DisplayName("Should verify JSONValue interface is sealed")
    void shouldVerifyJSONValueInterfaceIsSealed() {
        assertTrue(JSONValue.class.isSealed());
        assertEquals(6, JSONValue.class.getPermittedSubclasses().length);
    }

    @Test
    @DisplayName("Should escape quotes in JSON strings")
    void shouldEscapeQuotesInJSONStrings() {
        var json = new JSONString("say \"hello\"");
        var jsonString = json.toJson();

        assertTrue(jsonString.contains("\\\""));
    }
}
