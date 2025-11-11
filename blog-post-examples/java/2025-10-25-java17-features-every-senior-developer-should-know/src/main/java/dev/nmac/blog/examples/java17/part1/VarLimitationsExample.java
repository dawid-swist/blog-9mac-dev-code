package dev.nmac.blog.examples.java17.part1;

import java.util.*;
import java.util.function.Predicate;

/**
 * Demonstrates limitations and common pitfalls when using the var keyword.
 *
 * Understanding these limitations is crucial for effective use of var in
 * production code. This example shows what doesn't work with var and
 * explains the best practices.
 *
 * Blog: blog.9mac.dev
 * Series: Java 17 Features Every Senior Developer Should Know
 * Part: 1 - Introduction and var keyword
 */
public class VarLimitationsExample {

    // ❌ LIMITATION 1: Cannot use var for fields
    // var classField = "This won't compile"; // Compilation error!

    // ✅ Must use explicit types for fields
    private String classField = "This works";
    private static final String CONSTANT = "Constants need explicit types too";

    /**
     * Demonstrates where var CANNOT be used.
     */
    public static void demonstrateForbiddenUsages() {
        System.out.println("=== Where var CANNOT be used ===\n");

        // ❌ Cannot use without initialization
        // var x; // Compilation error: cannot infer type

        // ❌ Cannot initialize with null
        // var y = null; // Compilation error: variable initializer is 'null'

        // ❌ Cannot use with lambda expressions
        // var predicate = (String s) -> s.isEmpty(); // Compilation error!

        // ✅ Must use explicit type with lambdas
        Predicate<String> predicate = (String s) -> s.isEmpty();
        System.out.println("Lambda needs explicit type: " + predicate.test(""));

        // ❌ Cannot use with method references
        //var comparator = String::compareToIgnoreCase; // Compilation error!

        // ✅ Must use explicit type with method references
        Comparator<String> comparator = String::compareToIgnoreCase;
        System.out.println("Method reference needs explicit type: " +
            comparator.compare("A", "a"));

        // ❌ Cannot use with array initializer
        // var array = {1, 2, 3}; // Compilation error!

        // ✅ Must use array creation expression
        var validArray = new int[]{1, 2, 3};
        System.out.println("Array with var: " + Arrays.toString(validArray));
    }

    /**
     * PITFALL 1: Type becomes too specific, limiting flexibility.
     */
    public static void demonstrateTooSpecificType() {
        System.out.println("\n=== PITFALL 1: Too Specific Type ===\n");

        // ❌ PROBLEM: Type is ArrayList<String>, not List<String>
        var specificList = new ArrayList<String>();
        specificList.add("Java");

        // This won't work - cannot assign LinkedList to ArrayList variable
        // specificList = new LinkedList<>(); // Compilation error!

        System.out.println("Type is ArrayList, not List - limits flexibility");

        // ✅ SOLUTION: Use explicit interface type when you need flexibility
        List<String> flexibleList = new ArrayList<>();
        flexibleList.add("Java");
        flexibleList = new LinkedList<>(); // ✅ This works!

        System.out.println("Explicit List type allows changing implementations");
    }

    /**
     * PITFALL 2: Diamond operator without type arguments.
     */
    public static void demonstrateDiamondOperatorIssue() {
        System.out.println("\n=== PITFALL 2: Diamond Operator ===\n");

        // ❌ Cannot use diamond operator without type info
        // var list = new ArrayList<>(); // Compilation error: cannot infer type

        // ✅ Must specify type arguments
        var list1 = new ArrayList<String>();
        System.out.println("Must specify type with var: ArrayList<String>");

        // ✅ Or use factory methods
        var list2 = List.of("Java", "Python", "Scala");
        System.out.println("Or use factory methods: List.of(...)");

        // ✅ Or initialize with explicit type and then use var
        List<String> temp = new ArrayList<>();
        temp.add("Item");
        var list3 = temp;
        System.out.println("Or initialize with explicit type first");
    }

    /**
     * PITFALL 3: Primitive type widening.
     */
    public static void demonstratePrimitiveTypeWidening() {
        System.out.println("\n=== PITFALL 3: Primitive Type Widening ===\n");

        // ⚠️ CAUTION: Literals default to wider types
        var byteValue = 127; // Type is int, NOT byte!
        var shortValue = 32000; // Type is int, NOT short!
        var longValue = 100; // Type is int, NOT long!
        var floatValue = 3.14; // Type is double, NOT float!

        System.out.println("byteValue type: " + ((Object)byteValue).getClass().getSimpleName());
        System.out.println("floatValue type: " + ((Object)floatValue).getClass().getSimpleName());

        // ❌ This causes compilation error
        // byte b = byteValue; // Error: incompatible types: int cannot be converted to byte

        // ✅ SOLUTION: Use explicit types for specific primitive sizes
        byte actualByte = 127;
        short actualShort = 32000;
        long actualLong = 100L; // Note the L suffix
        float actualFloat = 3.14f; // Note the f suffix

        System.out.println("Use explicit types when size matters");
    }

    /**
     * PITFALL 4: Ternary operator type resolution.
     */
    public static void demonstrateTernaryOperatorIssues() {
        System.out.println("\n=== PITFALL 4: Ternary Operator ===\n");

        // Type becomes the wider type (double, not int)
        var result1 = true ? 42 : 3.14; // Type is double
        System.out.println("Ternary result type: " + ((Object)result1).getClass().getSimpleName());

        // Type becomes the common supertype
        var result2 = true ? "String" : new StringBuilder("Builder");
        // Type is Serializable & Comparable<String> & CharSequence (intersection type)
        System.out.println("Result2 is CharSequence: " + (result2 instanceof CharSequence));
        System.out.println("Result2 is Serializable: " + (result2 instanceof java.io.Serializable));

        // ✅ BEST PRACTICE: Use explicit type when ternary types differ
        String result3 = true ? "String" : new StringBuilder("Builder").toString();
        System.out.println("Explicit type avoids ambiguity: " + result3);
    }

    /**
     * PITFALL 5: Reduced readability with unclear initializers.
     */
    public static void demonstrateReadabilityIssues() {
        System.out.println("\n=== PITFALL 5: Readability ===\n");

        // ❌ BAD: Return type is not obvious
        var data1 = fetchData(); // What type is this?
        System.out.println("Unclear return type reduces readability");

        // ❌ BAD: Complex expression with unclear result type
        var result = process(transform(fetchData()));
        System.out.println("Nested calls make type unclear");

        // ✅ GOOD: Clear from context
        var user = new User("Alice", 30);
        var count = user.getAge();
        System.out.println("Type is obvious from constructor/method name");

        // ✅ GOOD: Well-named variables help
        var userList = getActiveUsers();
        var userCount = userList.size();
        System.out.println("Good naming makes var acceptable");
    }

    /**
     * Best practices summary.
     */
    public static void bestPractices() {
        System.out.println("\n=== Best Practices ===\n");

        // ✅ DO: Use var when type is obvious
        var builder = new StringBuilder();
        var users = new ArrayList<User>();
        var message = "Hello";

        // ✅ DO: Use var with long generic types
        var employeesByDepartment = new HashMap<String, List<Employee>>();

        // ✅ DO: Use var in enhanced for-loops
        for (var user : users) {
            // process user
        }

        // ❌ DON'T: Use var when type is unclear
        // var data = getData(); // What type?

        // ❌ DON'T: Use var with numeric literals if size matters
        // var age = 25; // int, not byte/short

        // ❌ DON'T: Use var when you need interface flexibility
        // var list = new ArrayList<String>(); // Too specific

        System.out.println("✅ Use var when it improves readability");
        System.out.println("✅ Use var with obvious types");
        System.out.println("✅ Use var with long generic types");
        System.out.println("❌ Avoid var when type is unclear");
        System.out.println("❌ Avoid var when you need flexibility");
        System.out.println("❌ Avoid var for numeric literals when size matters");
    }

    // Helper classes and methods for examples

    static class User {
        private String name;
        private int age;

        User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        int getAge() { return age; }
        String getName() { return name; }
    }

    static class Employee {
        private String name;
        Employee(String name) { this.name = name; }
    }

    static String fetchData() {
        return "some data";
    }

    static String transform(String data) {
        return data.toUpperCase();
    }

    static String process(String data) {
        return data + " processed";
    }

    static List<User> getActiveUsers() {
        return List.of(new User("Alice", 30), new User("Bob", 25));
    }

    /**
     * Main method demonstrating all limitations and pitfalls.
     */
    public static void main(String[] args) {
        demonstrateForbiddenUsages();
        demonstrateTooSpecificType();
        demonstrateDiamondOperatorIssue();
        demonstratePrimitiveTypeWidening();
        demonstrateTernaryOperatorIssues();
        demonstrateReadabilityIssues();
        bestPractices();

        System.out.println("\n=== Key Takeaway ===");
        System.out.println("var has limitations - understand them!");
        System.out.println("Use var to reduce boilerplate, not to hide types");
        System.out.println("When in doubt, prefer explicit types for clarity");
    }
}
