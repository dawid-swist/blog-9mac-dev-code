package dev.nmac.blog.examples.java17.part1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dev.nmac.blog.examples.java17.part1.VarIntersectionTypesExample.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for VarIntersectionTypesExample demonstrating var with intersection types.
 *
 * Blog: blog.9mac.dev
 * Series: Java 17 Features Every Senior Developer Should Know
 * Part: 1 - Introduction and var keyword
 */
@DisplayName("var with Intersection Types")
class VarIntersectionTypesExampleTest {

    @Test
    @DisplayName("Should enable declaring variable with multiple interface types using var")
    void shouldEnableIntersectionType() {
        var greeter = (Welcome & Goodbye) () -> "World";

        assertEquals("World", greeter.greet());
        assertEquals("Welcome, World!", greeter.getWelcomeMessage());
        assertEquals("Goodbye, World!", greeter.getFarewellMessage());
    }

    @Test
    @DisplayName("Should verify lambda implements both interfaces with instanceof checks")
    void shouldVerifyMultipleInterfaceImplementation() {
        var greeter = (Welcome & Goodbye) () -> "World";

        assertTrue(greeter instanceof Welcome);
        assertTrue(greeter instanceof Goodbye);
    }

    @Test
    @DisplayName("Should access default methods from all intersection type interfaces")
    void shouldAccessDefaultMethodsFromAllInterfaces() {
        var message = (Loggable & Validatable) () -> "Important system message";

        assertTrue(message.isValid());
        assertDoesNotThrow(message::log);
    }

    @Test
    @DisplayName("Should handle empty message with intersection type validation")
    void shouldHandleEmptyMessageWithValidation() {
        var emptyMessage = (Loggable & Validatable) () -> "";

        assertFalse(emptyMessage.isValid());
    }

    @Test
    @DisplayName("Should handle null message with intersection type validation")
    void shouldHandleNullMessageWithValidation() {
        var nullMessage = (Loggable & Validatable) () -> null;

        assertFalse(nullMessage.isValid());
    }

    @Test
    @DisplayName("Should infer intersection type from ternary operator with different types")
    void shouldInferIntersectionTypeFromTernary() {
        boolean useString = true;
        var result = useString ? "String" : new StringBuilder("Builder");

        assertTrue(result instanceof CharSequence);
        assertTrue(result instanceof java.io.Serializable);
        assertTrue(result instanceof Comparable);
    }

    @Test
    @DisplayName("Should access CharSequence methods from ternary intersection type")
    void shouldAccessCharSequenceMethodsFromTernary() {
        var result = true ? "Hello" : new StringBuilder("World");

        assertEquals(5, result.length());
        assertEquals('H', result.charAt(0));
    }

    @Test
    @DisplayName("Should work with different ternary result types")
    void shouldWorkWithDifferentTernaryResults() {
        var stringResult = true ? "String" : new StringBuilder("Builder");
        assertEquals("String", stringResult.toString());

        var builderResult = false ? "String" : new StringBuilder("Builder");
        assertEquals("Builder", builderResult.toString());
    }

    @Test
    @DisplayName("Should validate message before logging in practical example")
    void shouldValidateBeforeLogging() {
        var handler = (Loggable & Validatable) () -> "Processing user request";

        assertTrue(handler.isValid());
        assertDoesNotThrow(handler::log);
    }

    @Test
    @DisplayName("Should reject invalid message in practical example")
    void shouldRejectInvalidMessage() {
        var invalidHandler = (Loggable & Validatable) () -> null;

        assertFalse(invalidHandler.isValid());
    }

    @Test
    @DisplayName("Should demonstrate intersection types work only for local variables")
    void shouldDemonstrateLocalVariableScope() {
        var local = (Welcome & Goodbye) () -> "Local";

        assertEquals("Local", local.greet());
        assertEquals("Welcome, Local!", local.getWelcomeMessage());
        assertEquals("Goodbye, Local!", local.getFarewellMessage());
    }
}
