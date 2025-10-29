package dev.nmac.blog.examples.java17.part2;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Record Methods Examples - Custom Methods and Wither Pattern
 *
 * This example demonstrates:
 * - Custom instance methods in records
 * - Wither pattern for "modifying" immutable records
 * - Static factory methods
 * - Method overloading in records
 * - Business logic in records
 *
 * Records can contain rich behavior while maintaining immutability.
 */
public class RecordMethodsExample {

    /**
     * Person record with custom methods and validation.
     */
    public record Person(String firstName, String lastName, int age) {

        /**
         * Compact constructor with normalization and validation.
         */
        public Person {
            // Normalize names
            firstName = firstName.trim();
            lastName = lastName.trim();

            // Validate age
            if (age < 0) {
                throw new IllegalArgumentException("Age cannot be negative: " + age);
            }
            if (age > 150) {
                throw new IllegalArgumentException("Age unrealistic: " + age);
            }
        }

        /**
         * Returns the full name.
         *
         * @return first name + space + last name
         */
        public String fullName() {
            return firstName + " " + lastName;
        }

        /**
         * Checks if person is an adult (18+).
         *
         * @return true if age >= 18
         */
        public boolean isAdult() {
            return age >= 18;
        }

        /**
         * Checks if person is a senior (65+).
         *
         * @return true if age >= 65
         */
        public boolean isSenior() {
            return age >= 65;
        }

        /**
         * Returns initials.
         *
         * @return first letter of first name + first letter of last name
         */
        public String initials() {
            return firstName.substring(0, 1).toUpperCase() +
                   lastName.substring(0, 1).toUpperCase();
        }

        // Wither methods - return modified copies

        /**
         * Returns a new Person with updated age (wither pattern).
         *
         * @param newAge the new age
         * @return new Person instance with updated age
         */
        public Person withAge(int newAge) {
            return new Person(firstName, lastName, newAge);
        }

        /**
         * Returns a new Person with updated first name.
         *
         * @param newFirstName the new first name
         * @return new Person instance with updated first name
         */
        public Person withFirstName(String newFirstName) {
            return new Person(newFirstName, lastName, age);
        }

        /**
         * Returns a new Person with updated last name.
         *
         * @param newLastName the new last name
         * @return new Person instance with updated last name
         */
        public Person withLastName(String newLastName) {
            return new Person(firstName, newLastName, age);
        }

        /**
         * Returns a new Person one year older.
         *
         * @return new Person with age incremented by 1
         */
        public Person haveBirthday() {
            return withAge(age + 1);
        }
    }

    /**
     * Money record representing an amount with currency.
     * Demonstrates business logic in records.
     */
    public record Money(BigDecimal amount, String currency) {

        /**
         * Compact constructor with validation and normalization.
         */
        public Money {
            Objects.requireNonNull(amount, "Amount cannot be null");
            Objects.requireNonNull(currency, "Currency cannot be null");

            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Amount cannot be negative: " + amount);
            }

            // Normalize currency to uppercase
            currency = currency.toUpperCase();

            // Scale to 2 decimal places for precision
            amount = amount.setScale(2, java.math.RoundingMode.HALF_UP);
        }

        /**
         * Adds two money values.
         * Currencies must match.
         *
         * @param other money to add
         * @return new Money with combined amount
         * @throws IllegalArgumentException if currencies don't match
         */
        public Money add(Money other) {
            if (!currency.equals(other.currency)) {
                throw new IllegalArgumentException(
                    "Currency mismatch: " + currency + " vs " + other.currency
                );
            }
            return new Money(amount.add(other.amount), currency);
        }

        /**
         * Subtracts money values.
         *
         * @param other money to subtract
         * @return new Money with difference
         * @throws IllegalArgumentException if currencies don't match or result is negative
         */
        public Money subtract(Money other) {
            if (!currency.equals(other.currency)) {
                throw new IllegalArgumentException("Currency mismatch");
            }
            BigDecimal result = amount.subtract(other.amount);
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Result would be negative");
            }
            return new Money(result, currency);
        }

        /**
         * Multiplies by a factor.
         *
         * @param factor multiplication factor
         * @return new Money with multiplied amount
         */
        public Money multiply(double factor) {
            if (factor < 0) {
                throw new IllegalArgumentException("Factor cannot be negative");
            }
            return new Money(amount.multiply(BigDecimal.valueOf(factor)), currency);
        }

        /**
         * Checks if this amount is greater than another.
         *
         * @param other money to compare
         * @return true if this amount > other amount
         * @throws IllegalArgumentException if currencies don't match
         */
        public boolean isGreaterThan(Money other) {
            if (!currency.equals(other.currency)) {
                throw new IllegalArgumentException("Currency mismatch");
            }
            return amount.compareTo(other.amount) > 0;
        }

        /**
         * Checks if this represents zero money.
         *
         * @return true if amount is zero
         */
        public boolean isZero() {
            return amount.compareTo(BigDecimal.ZERO) == 0;
        }

        // Static factory methods

        /**
         * Creates Money in USD.
         *
         * @param amount dollar amount
         * @return Money in USD
         */
        public static Money dollars(double amount) {
            return new Money(BigDecimal.valueOf(amount), "USD");
        }

        /**
         * Creates Money in EUR.
         *
         * @param amount euro amount
         * @return Money in EUR
         */
        public static Money euros(double amount) {
            return new Money(BigDecimal.valueOf(amount), "EUR");
        }

        /**
         * Creates zero money in given currency.
         *
         * @param currency currency code
         * @return Money with zero amount
         */
        public static Money zero(String currency) {
            return new Money(BigDecimal.ZERO, currency);
        }
    }

    // Main method for manual testing
    public static void main(String[] args) {
        System.out.println("=== Record Methods Examples ===\n");

        // Person examples
        Person person = new Person("  Alice  ", "  Smith  ", 30);
        System.out.println("Person: " + person);
        System.out.println("Full name: " + person.fullName());
        System.out.println("Initials: " + person.initials());
        System.out.println("Is adult: " + person.isAdult());
        System.out.println("Is senior: " + person.isSenior());

        System.out.println("\nWither pattern:");
        Person older = person.withAge(31);
        System.out.println("After birthday: " + older);
        System.out.println("Original: " + person); // Unchanged

        System.out.println("\n--- Money Examples ---");

        Money m1 = Money.dollars(10.50);
        Money m2 = Money.dollars(5.25);
        System.out.println("m1: " + m1);
        System.out.println("m2: " + m2);
        System.out.println("Sum: " + m1.add(m2));
        System.out.println("Doubled: " + m1.multiply(2));

        System.out.println("\nCurrency validation:");
        try {
            Money usd = Money.dollars(10);
            Money eur = Money.euros(10);
            usd.add(eur); // Will throw
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
    }
}
