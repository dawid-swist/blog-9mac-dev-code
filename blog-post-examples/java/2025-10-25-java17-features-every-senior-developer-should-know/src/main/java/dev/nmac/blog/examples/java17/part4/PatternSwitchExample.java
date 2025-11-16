package dev.nmac.blog.examples.java17.part4;

/**
 * Demonstrates pattern matching in switch expressions (Java 17+, JEP 406).
 *
 * Pattern matching in switch enables:
 * - Type patterns: match and cast in one operation
 * - Guarded patterns: add conditions with when clause
 * - Null handling: explicitly match null
 *
 * This is the most powerful feature because it combines:
 * 1. Pattern matching (from instanceof)
 * 2. Switch expressions (from Java 14)
 * 3. Sealed types (for exhaustiveness)
 */
public class PatternSwitchExample {

    /**
     * Type patterns in switch - Java 17+
     * Match different types and extract values in one expression.
     */
    public static String processObject(Object obj) {
        return switch (obj) {
            case String s -> "String: " + s;
            case Integer i -> "Integer: " + i;
            case Double d -> "Double: " + d;
            case Boolean b -> "Boolean: " + b;
            case null -> "Null value";
            default -> "Unknown type: " + obj.getClass().getSimpleName();
        };
    }

    /**
     * Guarded patterns - Java 17+
     * Use 'when' clause to add conditions to patterns.
     */
    public static String classifyString(Object obj) {
        return switch (obj) {
            case String s when s.isEmpty() -> "Empty string";
            case String s when s.length() < 5 -> "Short string: " + s;
            case String s when s.length() < 20 -> "Medium string: " + s;
            case String s -> "Long string: " + s;
            default -> "Not a string";
        };
    }

    /**
     * Complex type matching with sealed types
     * When used with sealed types, exhaustiveness is guaranteed.
     */
    public static int getShapeArea(Shape shape) {
        return switch (shape) {
            case Circle c -> (int) (Math.PI * c.radius() * c.radius());
            case Rectangle r -> r.width() * r.height();
            case Triangle t -> (t.base() * t.height()) / 2;
            // No default needed - all sealed types covered!
        };
    }

    /**
     * Record pattern matching - Java 17+
     * Destructure records and access fields in patterns.
     * Use guarded patterns (when clause) for complex conditions.
     */
    public static String describePoint(Object obj) {
        return switch (obj) {
            case Point(int x, int y) when x > 0 && y > 0 ->
                "Point in quadrant 1: (" + x + ", " + y + ")";
            case Point(int x, int y) when x < 0 && y > 0 ->
                "Point in quadrant 2: (" + x + ", " + y + ")";
            case Point(int x, int y) when x < 0 && y < 0 ->
                "Point in quadrant 3: (" + x + ", " + y + ")";
            case Point(int x, int y) when x > 0 && y < 0 ->
                "Point in quadrant 4: (" + x + ", " + y + ")";
            case Point(int x, int y) when x == 0 && y == 0 ->
                "Point at origin";
            case Point(int x, int y) when y == 0 ->
                "Point on X-axis: (" + x + ", 0)";
            case Point(int x, int y) when x == 0 ->
                "Point on Y-axis: (0, " + y + ")";
            default -> "Unknown";
        };
    }

    /**
     * Traditional approach - before Java 17
     * Required multiple instanceof checks and manual casting
     */
    public static String processObjectTraditional(Object obj) {
        if (obj instanceof String) {
            String s = (String) obj;
            return "String: " + s;
        } else if (obj instanceof Integer) {
            Integer i = (Integer) obj;
            return "Integer: " + i;
        } else if (obj instanceof Double) {
            Double d = (Double) obj;
            return "Double: " + d;
        } else {
            return "Unknown type";
        }
    }

    // Sealed interface for type-safe pattern matching
    public sealed interface Shape permits Circle, Rectangle, Triangle {}

    public record Circle(double radius) implements Shape {}

    public record Rectangle(int width, int height) implements Shape {}

    public record Triangle(int base, int height) implements Shape {}

    // Simple record for pattern demonstration
    public record Point(int x, int y) {}

    public static void main(String[] args) {
        System.out.println("=== Type Patterns in Switch ===");
        System.out.println(processObject("Hello"));
        System.out.println(processObject(42));
        System.out.println(processObject(3.14));
        System.out.println(processObject(true));
        System.out.println(processObject(null));

        System.out.println("\n=== Guarded Patterns ===");
        System.out.println(classifyString(""));
        System.out.println(classifyString("Hi"));
        System.out.println(classifyString("Hello World"));
        System.out.println(classifyString("This is a very long string with many words"));

        System.out.println("\n=== Shape Pattern Matching ===");
        System.out.println("Circle area: " + getShapeArea(new Circle(5)));
        System.out.println("Rectangle area: " + getShapeArea(new Rectangle(10, 20)));
        System.out.println("Triangle area: " + getShapeArea(new Triangle(10, 15)));

        System.out.println("\n=== Record Pattern Matching ===");
        System.out.println(describePoint(new Point(3, 4)));
        System.out.println(describePoint(new Point(-2, 5)));
        System.out.println(describePoint(new Point(-3, -4)));
        System.out.println(describePoint(new Point(2, -3)));
        System.out.println(describePoint(new Point(0, 0)));
        System.out.println(describePoint(new Point(5, 0)));

        System.out.println("\n=== Traditional vs Modern ===");
        System.out.println("Traditional: " + processObjectTraditional("Test"));
        System.out.println("Modern:      " + processObject("Test"));
    }
}
