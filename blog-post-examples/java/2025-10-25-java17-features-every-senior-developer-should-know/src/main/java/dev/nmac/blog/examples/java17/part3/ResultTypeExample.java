package dev.nmac.blog.examples.java17.part3;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Example 6: Domain Modeling with Result Type
 *
 * Demonstrates:
 * - Sealed interface for Result type (Success/Failure)
 * - Type-safe error handling without exceptions
 * - Functional composition with map, flatMap, orElse
 * - Exhaustive switch on sealed Result type
 * - Records as sealed type implementations
 * - Railway-oriented programming pattern
 *
 * Key concept: The sealed Result<T> type demonstrates domain modeling at its
 * finest. With only two possible implementations (Success and Failure), the
 * compiler can verify exhaustive handling in switch expressions. This pattern
 * eliminates null checks, provides type-safe error handling, and enables
 * functional composition—all without throwing exceptions.
 */

// ============================================================================
// Sealed Result interface with two permitted implementations
// ============================================================================

/**
 * Sealed Result type representing either Success or Failure.
 * This is Java's answer to Scala's Either or Rust's Result.
 *
 * @param <T> the type of successful result value
 */
sealed interface Result<T>
    permits Success, Failure {

    /**
     * Factory method for creating a Success result.
     *
     * @param value the success value
     * @param <T>   value type
     * @return Success instance
     */
    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    /**
     * Factory method for creating a Failure result.
     *
     * @param error error message
     * @param <T>   value type
     * @return Failure instance
     */
    static <T> Result<T> failure(String error) {
        return new Failure<>(error);
    }

    /**
     * Factory method for creating a Failure result with cause.
     *
     * @param error error message
     * @param cause underlying exception
     * @param <T>   value type
     * @return Failure instance
     */
    static <T> Result<T> failure(String error, Throwable cause) {
        return new Failure<>(error, cause);
    }

    // Query methods
    boolean isSuccess();
    boolean isFailure();

    // Value extraction (safe)
    T getValue();
    String getError();

    // Functional operations
    <U> Result<U> map(Function<T, U> mapper);
    <U> Result<U> flatMap(Function<T, Result<U>> mapper);
    Result<T> orElse(Supplier<Result<T>> alternative);
}

// ============================================================================
// Success implementation - holds successful value
// ============================================================================

/**
 * Success record holding the successful result value.
 * Records are implicitly final, perfect for sealed types.
 *
 * @param value the successful value (never null)
 * @param <T>   value type
 */
record Success<T>(T value) implements Result<T> {

    /**
     * Compact constructor with null check.
     */
    public Success {
        if (value == null) {
            throw new IllegalArgumentException("Success value cannot be null");
        }
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public String getError() {
        throw new UnsupportedOperationException("Success has no error");
    }

    /**
     * Maps the success value to a new type.
     * If the mapper throws, returns Failure.
     *
     * @param mapper transformation function
     * @param <U>    result type
     * @return new Result with transformed value
     */
    @Override
    public <U> Result<U> map(Function<T, U> mapper) {
        try {
            return new Success<>(mapper.apply(value));
        } catch (Exception e) {
            return new Failure<>(e.getMessage(), e);
        }
    }

    /**
     * FlatMaps the success value to a new Result.
     * If the mapper throws, returns Failure.
     *
     * @param mapper transformation function
     * @param <U>    result type
     * @return new Result from mapper
     */
    @Override
    public <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        try {
            return mapper.apply(value);
        } catch (Exception e) {
            return new Failure<>(e.getMessage(), e);
        }
    }

    /**
     * Returns this Success, ignoring the alternative.
     *
     * @param alternative fallback supplier (not used)
     * @return this Success
     */
    @Override
    public Result<T> orElse(Supplier<Result<T>> alternative) {
        return this;
    }
}

// ============================================================================
// Failure implementation - holds error information
// ============================================================================

/**
 * Failure record holding error message and optional cause.
 *
 * @param error error message
 * @param cause optional underlying exception
 * @param <T>   value type (for type compatibility)
 */
record Failure<T>(String error, Throwable cause) implements Result<T> {

    /**
     * Constructor without cause.
     *
     * @param error error message
     */
    public Failure(String error) {
        this(error, null);
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public T getValue() {
        throw new UnsupportedOperationException("Failure has no value: " + error);
    }

    @Override
    public String getError() {
        return error;
    }

    /**
     * Propagates the failure without applying mapper.
     *
     * @param mapper transformation function (not used)
     * @param <U>    result type
     * @return this Failure with updated type
     */
    @Override
    @SuppressWarnings("unchecked")
    public <U> Result<U> map(Function<T, U> mapper) {
        return (Result<U>) this;
    }

    /**
     * Propagates the failure without applying mapper.
     *
     * @param mapper transformation function (not used)
     * @param <U>    result type
     * @return this Failure with updated type
     */
    @Override
    @SuppressWarnings("unchecked")
    public <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        return (Result<U>) this;
    }

    /**
     * Returns the alternative Result instead of this Failure.
     *
     * @param alternative fallback supplier
     * @return result from alternative
     */
    @Override
    public Result<T> orElse(Supplier<Result<T>> alternative) {
        return alternative.get();
    }
}

// ============================================================================
// Main example class
// ============================================================================

/**
 * Demonstrates sealed Result type for domain modeling and error handling.
 *
 * Shows:
 * - Creating Success and Failure results
 * - Functional composition with map and flatMap
 * - Error propagation through chains
 * - Fallback with orElse
 * - Exhaustive switch on sealed Result type
 */
public class ResultTypeExample {

    /**
     * Simulates finding a user by ID.
     *
     * @param id user ID
     * @return Result with user name or error
     */
    public static Result<String> findUserById(int id) {
        if (id <= 0) {
            return Result.failure("Invalid user ID: " + id);
        }
        if (id > 1000) {
            return Result.failure("User not found: " + id);
        }
        return Result.success("User" + id);
    }

    /**
     * Calculates discount based on user name.
     *
     * @param userName the user name
     * @return Result with discount percentage or error
     */
    public static Result<Integer> calculateDiscount(String userName) {
        if (userName.startsWith("Premium")) {
            return Result.success(20);
        } else if (userName.startsWith("Regular")) {
            return Result.success(10);
        } else {
            return Result.failure("Unknown user category: " + userName);
        }
    }

    /**
     * Main method demonstrating Result type usage.
     */
    public static void main(String[] args) {
        System.out.println("=== Basic Result Operations ===");

        // Success case
        var successResult = findUserById(42);
        System.out.println("Is success? " + successResult.isSuccess());
        System.out.println("Value: " + successResult.getValue());

        // Failure case
        var failureResult = findUserById(-1);
        System.out.println("\nIs failure? " + failureResult.isFailure());
        System.out.println("Error: " + failureResult.getError());

        // Chaining with map
        System.out.println("\n=== Chaining with map() ===");
        var mapped = findUserById(100)
            .map(name -> name.toUpperCase())
            .map(name -> "Hello, " + name);

        if (mapped.isSuccess()) {
            System.out.println("Result: " + mapped.getValue());
        }

        // Chaining with flatMap
        System.out.println("\n=== Chaining with flatMap() ===");
        var chained = findUserById(500)
            .flatMap(userName -> calculateDiscount(userName))
            .map(discount -> "Discount: " + discount + "%");

        if (chained.isSuccess()) {
            System.out.println(chained.getValue());
        }

        // Error propagation
        System.out.println("\n=== Error Propagation ===");
        var errorChain = findUserById(2000) // Fails here
            .flatMap(userName -> calculateDiscount(userName))
            .map(discount -> "Discount: " + discount + "%");

        if (errorChain.isFailure()) {
            System.out.println("Error: " + errorChain.getError());
        }

        // Fallback with orElse
        System.out.println("\n=== Fallback with orElse() ===");
        var withFallback = findUserById(2000)
            .orElse(() -> Result.success("GuestUser"));

        System.out.println("Final value: " + withFallback.getValue());

        // Exhaustive switch on Result
        System.out.println("\n=== Exhaustive Switch ===");
        var result = findUserById(123);
        String message = switch (result) {
            case Success<String> s -> "Found user: " + s.value();
            case Failure<String> f -> "Error: " + f.error();
        };
        System.out.println(message);

        // Railway-oriented programming example
        System.out.println("\n=== Railway-Oriented Programming ===");
        var pipeline = findUserById(42)
            .map(name -> name.toUpperCase())
            .flatMap(ResultTypeExample::calculateDiscount)
            .map(discount -> "User discount: " + discount + "%");

        System.out.println(pipeline.isSuccess()
            ? pipeline.getValue()
            : "Error: " + pipeline.getError());
    }
}
