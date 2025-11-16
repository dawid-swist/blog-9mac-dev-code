package dev.nmac.blog.examples.java17.part4;

/**
 * Demonstrates sealed types + pattern matching for Result types (Functional Programming style).
 *
 * This example shows how pattern matching shines with sealed interfaces.
 * A Result<T> is either Success or Failure - sealed types ensure exhaustiveness,
 * and pattern matching makes handling both cases elegant and type-safe.
 */
public class ResultPatternExample {

    /**
     * Sealed interface representing the result of an operation.
     * Can only be Success or Failure - compiler knows all cases.
     */
    public sealed interface Result<T> permits Success, Failure {}

    /**
     * Successful result containing a value.
     */
    public record Success<T>(T value) implements Result<T> {}

    /**
     * Failed result containing an error message.
     */
    public record Failure<T>(String error) implements Result<T> {}

    /**
     * Process a Result using pattern matching.
     * Demonstrates exhaustiveness - compiler knows these are the ONLY two cases.
     */
    public static <T> String describe(Result<T> result) {
        // Pattern matching handles both cases with type safety
        if (result instanceof Success<T> s) {
            return "Success: " + s.value();
        } else if (result instanceof Failure<T> f) {
            return "Failure: " + f.error();
        }
        // Compiler would error here if we forget a case
        throw new AssertionError("Unreachable - all Result cases covered");
    }

    /**
     * Transform a Result using pattern matching.
     * Success values are transformed, Failures are propagated.
     */
    public static <T, U> Result<U> map(Result<T> result, java.util.function.Function<T, U> fn) {
        if (result instanceof Success<T> s) {
            return new Success<>(fn.apply(s.value()));
        } else if (result instanceof Failure<T> f) {
            return new Failure<>(f.error());
        }
        throw new AssertionError("Unreachable");
    }

    /**
     * Handle both success and failure with different actions.
     */
    public static <T> void handle(Result<T> result) {
        if (result instanceof Success<T> s) {
            System.out.println("✓ Operation succeeded with value: " + s.value());
        } else if (result instanceof Failure<T> f) {
            System.err.println("✗ Operation failed: " + f.error());
        }
    }

    /**
     * Chain operations with Result type.
     * Shows how pattern matching enables functional composition.
     */
    public static <T> Result<Integer> parseInt(String input) {
        try {
            return new Success<>(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return new Failure<>("Invalid number: " + input);
        }
    }

    /**
     * Demonstrate Result pattern with nested operations.
     */
    public static Result<String> processNumber(String input) {
        Result<Integer> parsed = parseInt(input);

        // Pattern matching shows the result clearly
        if (parsed instanceof Success<Integer> s) {
            int value = s.value();
            if (value < 0) {
                return new Failure<>("Negative numbers not allowed");
            }
            return new Success<>("Number " + value + " is valid");
        } else if (parsed instanceof Failure<Integer> f) {
            return new Failure<>("Parse error: " + f.error());
        }
        throw new AssertionError("Unreachable");
    }

    public static void main(String[] args) {
        System.out.println("=== Basic Result Pattern Matching ===");
        Result<String> success = new Success<>("Hello, Pattern Matching!");
        Result<String> failure = new Failure<>("Something went wrong");

        System.out.println(describe(success));
        System.out.println(describe(failure));

        System.out.println("\n=== Handling Results ===");
        handle(success);
        handle(failure);

        System.out.println("\n=== Transforming Results with map ===");
        Result<Integer> num = new Success<>(42);
        Result<String> transformed = map(num, n -> "Answer is " + n);
        System.out.println(describe(transformed));

        System.out.println("\n=== Processing Input with Results ===");
        System.out.println(describe(processNumber("42")));
        System.out.println(describe(processNumber("-10")));
        System.out.println(describe(processNumber("abc")));
    }
}
