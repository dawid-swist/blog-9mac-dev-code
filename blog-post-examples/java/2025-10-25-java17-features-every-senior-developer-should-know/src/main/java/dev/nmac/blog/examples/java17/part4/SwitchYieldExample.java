package dev.nmac.blog.examples.java17.part4;

/**
 * Demonstrates the yield keyword in switch expressions (JEP 361 - Java 14).
 *
 * The `yield` keyword enables multi-statement branches in switch expressions.
 * This combines the conciseness of expressions (returning values) with the
 * flexibility of statements (executing multiple lines of code).
 *
 * Use yield when you need to:
 * - Perform validation or logging before returning a value
 * - Execute multiple statements within a case
 * - Return early from complex conditional logic
 */
public class SwitchYieldExample {

    public enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, POWER, MODULO
    }

    /**
     * Switch expression with yield for multi-statement branches.
     * Each case can log, validate, or perform complex logic before yielding a result.
     */
    public static double calculate(Operation op, double a, double b) {
        return switch (op) {
            case ADD -> {
                System.out.println("Performing addition");
                yield a + b;
            }
            case SUBTRACT -> {
                System.out.println("Performing subtraction");
                yield a - b;
            }
            case MULTIPLY -> {
                System.out.println("Performing multiplication");
                yield a * b;
            }
            case DIVIDE -> {
                if (b == 0) {
                    System.out.println("Division by zero - returning NaN");
                    yield Double.NaN;
                }
                System.out.println("Performing division");
                yield a / b;
            }
            case POWER -> {
                System.out.println("Calculating power");
                yield Math.pow(a, b);
            }
            case MODULO -> {
                if (b == 0) {
                    throw new ArithmeticException("Modulo by zero");
                }
                System.out.println("Calculating modulo");
                yield a % b;
            }
        };
    }

    /**
     * Complex logic with yield - grade calculation with logging.
     * Demonstrates validation, feedback, and returning different types of values.
     */
    public static String getGrade(int score) {
        // Validate score range before switch
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Invalid score: " + score);
        }

        return switch (score / 10) {
            case 10, 9 -> {
                System.out.println("Excellent performance!");
                yield "A";
            }
            case 8 -> {
                System.out.println("Good work!");
                yield "B";
            }
            case 7 -> {
                System.out.println("Satisfactory");
                yield "C";
            }
            case 6 -> {
                System.out.println("Needs improvement");
                yield "D";
            }
            default -> {
                System.out.println("Failed");
                yield "F";
            }
        };
    }

    /**
     * Nested switch with yield.
     * Demonstrates composing switch expressions for complex categorization logic.
     */
    public static String categorize(String type, int value) {
        return switch (type) {
            case "number" -> {
                // Inner switch expression to categorize the number
                String category = switch (value) {
                    case 0 -> "zero";
                    case 1, 2, 3, 4, 5, 6, 7, 8, 9 -> "single digit";
                    default -> value < 0 ? "negative" : "multiple digits";
                };
                yield "Number category: " + category;
            }
            case "priority" -> {
                // Inner switch for priority levels
                String level = switch (value) {
                    case 1 -> "critical";
                    case 2 -> "high";
                    case 3 -> "medium";
                    default -> "low";
                };
                yield "Priority level: " + level;
            }
            default -> "Unknown type: " + type;
        };
    }

    public static void main(String[] args) {
        System.out.println("=== Calculator with yield ===");
        System.out.println("10 + 5 = " + calculate(Operation.ADD, 10, 5));
        System.out.println("10 / 3 = " + calculate(Operation.DIVIDE, 10, 3));
        System.out.println("10 / 0 = " + calculate(Operation.DIVIDE, 10, 0));
        System.out.println("2^8 = " + calculate(Operation.POWER, 2, 8));

        System.out.println("\n=== Grade Calculator ===");
        System.out.println("Score 95: " + getGrade(95));
        System.out.println("Score 78: " + getGrade(78));
        System.out.println("Score 55: " + getGrade(55));

        System.out.println("\n=== Nested Switch ===");
        System.out.println(categorize("number", 5));
        System.out.println(categorize("number", 42));
        System.out.println(categorize("priority", 1));
    }
}
