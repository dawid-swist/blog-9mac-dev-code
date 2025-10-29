package dev.nmac.blog.examples.java17.part3;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Example 4: Exhaustive Switch with Sealed Types
 *
 * Demonstrates:
 * - Sealed interface with 6 permitted implementations
 * - Exhaustive switch expressions (no default case needed)
 * - Compiler verification of completeness
 * - Records and enum as sealed type implementations
 * - Type-safe JSON value hierarchy
 *
 * Key concept: Exhaustive switch with sealed types is incredibly powerful.
 * The compiler knows all possible implementations, so it can verify your
 * switch covers every case. Adding a new permitted subclass makes every
 * switch a compilation error until you handle the new type—preventing bugs
 * at compile-time, not runtime.
 */

// ============================================================================
// Sealed interface - JSON value hierarchy
// ============================================================================

/**
 * Sealed JSONValue interface representing all possible JSON types.
 * This creates a closed set of JSON value implementations.
 */
sealed interface JSONValue
    permits JSONObject, JSONArray, JSONString, JSONNumber, JSONBoolean, JSONNull {

    /**
     * Converts this JSON value to its string representation.
     *
     * @return JSON string representation
     */
    String toJson();
}

// ============================================================================
// Record implementations - complex JSON types
// ============================================================================

/**
 * JSON object type containing key-value pairs.
 */
record JSONObject(Map<String, JSONValue> values) implements JSONValue {

    @Override
    public String toJson() {
        return values.entrySet().stream()
            .map(e -> "\"" + e.getKey() + "\":" + e.getValue().toJson())
            .collect(Collectors.joining(",", "{", "}"));
    }
}

/**
 * JSON array type containing ordered values.
 */
record JSONArray(List<JSONValue> values) implements JSONValue {

    @Override
    public String toJson() {
        return values.stream()
            .map(JSONValue::toJson)
            .collect(Collectors.joining(",", "[", "]"));
    }
}

// ============================================================================
// Record implementations - primitive JSON types
// ============================================================================

/**
 * JSON string type.
 */
record JSONString(String value) implements JSONValue {

    @Override
    public String toJson() {
        return "\"" + value.replace("\"", "\\\"") + "\"";
    }
}

/**
 * JSON number type.
 */
record JSONNumber(double value) implements JSONValue {

    @Override
    public String toJson() {
        return String.valueOf(value);
    }
}

/**
 * JSON boolean type.
 */
record JSONBoolean(boolean value) implements JSONValue {

    @Override
    public String toJson() {
        return String.valueOf(value);
    }
}

// ============================================================================
// Enum implementation - JSON null
// ============================================================================

/**
 * JSON null type using enum singleton pattern.
 * Enums are implicitly final, making them perfect sealed type implementations.
 */
enum JSONNull implements JSONValue {
    INSTANCE;

    @Override
    public String toJson() {
        return "null";
    }
}

// ============================================================================
// Main example class
// ============================================================================

/**
 * Demonstrates exhaustive switch expressions with sealed types.
 *
 * Shows:
 * - Exhaustive switch without default case
 * - Compiler-verified completeness checking
 * - Type descriptions using switch
 * - Recursive processing with switch
 */
public class ExhaustiveSwitchExample {

    /**
     * Describes the type of a JSON value using exhaustive switch.
     * Note: No default case needed - compiler verifies all cases are covered!
     *
     * @param value the JSON value to describe
     * @return human-readable type description
     */
    public static String describeType(JSONValue value) {
        return switch (value) {
            case JSONObject obj -> "object with " + obj.values().size() + " properties";
            case JSONArray arr -> "array with " + arr.values().size() + " elements";
            case JSONString str -> "string: \"" + str.value() + "\"";
            case JSONNumber num -> "number: " + num.value();
            case JSONBoolean bool -> "boolean: " + bool.value();
            case JSONNull ignored -> "null value";
            // No default case needed - compiler knows all cases are covered!
        };
    }

    /**
     * Estimates the string size of a JSON value using exhaustive switch.
     * Demonstrates recursive processing with pattern matching.
     *
     * @param value the JSON value to measure
     * @return estimated character count
     */
    public static int estimateSize(JSONValue value) {
        return switch (value) {
            case JSONObject obj -> obj.values().values().stream()
                .mapToInt(ExhaustiveSwitchExample::estimateSize)
                .sum() + 2; // {} brackets
            case JSONArray arr -> arr.values().stream()
                .mapToInt(ExhaustiveSwitchExample::estimateSize)
                .sum() + 2; // [] brackets
            case JSONString str -> str.value().length() + 2; // quotes
            case JSONNumber num -> String.valueOf(num.value()).length();
            case JSONBoolean bool -> String.valueOf(bool.value()).length();
            case JSONNull ignored -> 4; // "null"
        };
    }

    /**
     * Main method demonstrating exhaustive switch with sealed types.
     */
    public static void main(String[] args) {
        // Build sample JSON structure
        var jsonData = new JSONObject(Map.of(
            "name", new JSONString("John Doe"),
            "age", new JSONNumber(30),
            "active", new JSONBoolean(true),
            "address", JSONNull.INSTANCE,
            "hobbies", new JSONArray(List.of(
                new JSONString("reading"),
                new JSONString("coding"),
                new JSONString("gaming")
            ))
        ));

        // Display JSON structure
        System.out.println("=== JSON Structure ===");
        System.out.println(jsonData.toJson());

        // Describe types using exhaustive switch
        System.out.println("\n=== Type Descriptions (Exhaustive Switch) ===");
        jsonData.values().forEach((key, value) -> {
            System.out.println(key + " -> " + describeType(value));
        });

        // Estimate size
        System.out.println("\n=== Size Estimation ===");
        System.out.println("Estimated size: " + estimateSize(jsonData) + " characters");
        System.out.println("Actual size: " + jsonData.toJson().length() + " characters");

        // Demonstrate exhaustiveness with all types
        System.out.println("\n=== Exhaustive Pattern Matching ===");
        var values = List.of(
            new JSONString("test"),
            new JSONNumber(42),
            new JSONBoolean(false),
            JSONNull.INSTANCE
        );

        for (var value : values) {
            System.out.println(describeType(value));
        }

        // Show that adding a new type would break compilation
        System.out.println("\n=== Compiler Verification ===");
        System.out.println("If we add a new JSONValue type (e.g., JSONDate),");
        System.out.println("all switch statements become compilation errors");
        System.out.println("until we handle the new type!");
    }
}
