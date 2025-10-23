package dev.nmac.blog.examples.java17.part1;

/**
 * Demonstrates var with intersection types.
 *
 * Intersection types are a unique capability of var - you can declare a variable
 * that implements multiple interfaces simultaneously using lambda expressions.
 * This is impossible with explicit type declarations.
 *
 * Example: var x = (InterfaceA & InterfaceB) () -> "value"
 *
 * This allows the variable to satisfy multiple interface contracts at once,
 * enabling access to default methods from all interfaces.
 *
 * Blog: blog.9mac.dev
 * Series: Java 17 Features Every Senior Developer Should Know
 * Part: 1 - Introduction and var keyword
 */
public class VarIntersectionTypesExample {

    /**
     * Interface for objects that can greet.
     */
    interface Welcome {
        String greet();

        default String getWelcomeMessage() {
            return "Welcome, " + greet() + "!";
        }
    }

    /**
     * Interface for objects that can say goodbye.
     */
    interface Goodbye {
        String greet();

        default String getFarewellMessage() {
            return "Goodbye, " + greet() + "!";
        }
    }

    /**
     * Interface for logging.
     */
    interface Loggable {
        String getMessage();

        default void log() {
            System.out.println("[LOG] " + getMessage());
        }
    }

    /**
     * Interface for validation.
     */
    interface Validatable {
        String getMessage();

        default boolean isValid() {
            return getMessage() != null && !getMessage().isEmpty();
        }
    }

    /**
     * Demonstrates basic intersection type with var.
     * This shows var's unique capability - impossible with explicit types!
     */
    public static void demonstrateBasicIntersectionType() {
        System.out.println("=== Basic Intersection Type ===\n");

        // var enables declaring a variable with intersection type
        // This lambda implements BOTH Welcome AND Goodbye interfaces!
        var greeter = (Welcome & Goodbye) () -> "World";

        // We can call greet() (common to both interfaces)
        System.out.println("Greeting: " + greeter.greet());

        // We can access default methods from Welcome interface
        System.out.println(greeter.getWelcomeMessage());

        // We can access default methods from Goodbye interface
        System.out.println(greeter.getFarewellMessage());

        // Verify instance checks
        System.out.println("\nInstance checks:");
        System.out.println("greeter instanceof Welcome: " + (greeter instanceof Welcome));
        System.out.println("greeter instanceof Goodbye: " + (greeter instanceof Goodbye));

        System.out.println("\n⚠️ NOTE: This is IMPOSSIBLE with explicit type declaration!");
        System.out.println("You cannot write: Welcome & Goodbye greeter = ...");
    }

    /**
     * Demonstrates why intersection types are useful - accessing multiple default methods.
     */
    public static void demonstrateMultipleDefaultMethods() {
        System.out.println("\n=== Multiple Default Methods ===\n");

        // Intersection type: implements both Loggable and Validatable
        var message = (Loggable & Validatable) () -> "Important system message";

        // Access methods from both interfaces
        System.out.println("Is valid: " + message.isValid());
        message.log();

        // Another example with empty message
        var emptyMessage = (Loggable & Validatable) () -> "";
        System.out.println("\nEmpty message is valid: " + emptyMessage.isValid());
        emptyMessage.log();
    }

    /**
     * Practical example: Creating a flexible message handler.
     */
    public static void practicalExample() {
        System.out.println("\n=== Practical Example: Message Handler ===\n");

        // Create a message handler with multiple capabilities
        var handler = (Loggable & Validatable) () -> "Processing user request";

        // Validate before logging
        if (handler.isValid()) {
            handler.log();
            System.out.println("✅ Message processed successfully");
        } else {
            System.out.println("❌ Invalid message");
        }

        System.out.println();

        // Another handler with invalid message
        var invalidHandler = (Loggable & Validatable) () -> null;
        if (!invalidHandler.isValid()) {
            System.out.println("❌ Cannot process invalid message");
        }
    }

    /**
     * Demonstrates the limitation - you cannot store intersection types in fields.
     */
    public static void demonstrateLimitations() {
        System.out.println("\n=== Limitations ===\n");

        System.out.println("✅ var works with intersection types in local variables");
        var local = (Welcome & Goodbye) () -> "Local";
        System.out.println("Local greeter: " + local.greet());

        System.out.println("\n❌ CANNOT use intersection types in:");
        System.out.println("  - Class fields");
        System.out.println("  - Method parameters");
        System.out.println("  - Method return types");
        System.out.println("\n👉 Intersection types with var work ONLY for local variables");
    }

    /**
     * Main method demonstrating all intersection type examples.
     */
    public static void main(String[] args) {
        demonstrateBasicIntersectionType();
        demonstrateMultipleDefaultMethods();
        practicalExample();
        demonstrateLimitations();

        System.out.println("\n=== Key Takeaway ===");
        System.out.println("var enables intersection types - a unique capability!");
        System.out.println("Syntax: var x = (Interface1 & Interface2) () -> value");
        System.out.println("This allows accessing default methods from multiple interfaces");
        System.out.println("Impossible to achieve with explicit type declarations");
    }
}
