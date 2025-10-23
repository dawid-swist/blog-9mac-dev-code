package dev.nmac.blog.examples.java17.part3;

import java.util.List;

/**
 * Example 3: Multi-level Sealed Hierarchies
 *
 * Demonstrates:
 * - Sealed classes at multiple hierarchy levels
 * - Second-level sealed class extending first-level sealed class
 * - Final classes at different hierarchy depths
 * - Tree-like type structures with controlled inheritance
 * - instanceof checks and type categorization
 *
 * Key concept: Multi-level sealed hierarchies let you model complex domain
 * structures with controlled inheritance at each level. Each sealed node in
 * the tree decides its children's extension policy.
 */

// ============================================================================
// Top-level sealed class - first level of hierarchy
// ============================================================================

/**
 * Top-level sealed Vehicle class permitting MotorVehicle and Bicycle.
 * This creates two main branches in the vehicle hierarchy.
 */
sealed abstract class Vehicle
    permits MotorVehicle, Bicycle {

    private final String brand;

    /**
     * Constructor for all vehicles.
     *
     * @param brand the vehicle brand
     */
    protected Vehicle(String brand) {
        this.brand = brand;
    }

    /**
     * Gets the vehicle brand.
     *
     * @return the brand name
     */
    public String brand() {
        return brand;
    }

    /**
     * Provides a human-readable description of the vehicle.
     *
     * @return formatted description string
     */
    public abstract String describe();
}

// ============================================================================
// Second-level sealed class - intermediate hierarchy level
// ============================================================================

/**
 * Second-level sealed MotorVehicle class permitting Car and Motorcycle.
 * This creates a sub-hierarchy under Vehicle with its own controlled inheritance.
 */
sealed abstract class MotorVehicle extends Vehicle
    permits Car, Motorcycle {

    private final int engineCC;

    /**
     * Constructor for all motor vehicles.
     *
     * @param brand    the vehicle brand
     * @param engineCC the engine displacement in cubic centimeters
     */
    protected MotorVehicle(String brand, int engineCC) {
        super(brand);
        this.engineCC = engineCC;
    }

    /**
     * Gets the engine displacement.
     *
     * @return engine size in cubic centimeters
     */
    public int engineCC() {
        return engineCC;
    }
}

// ============================================================================
// Final implementations at third level - leaf classes under MotorVehicle
// ============================================================================

/**
 * Car implementation with door count and electric/combustion distinction.
 * Final modifier prevents any further subclassing.
 */
final class Car extends MotorVehicle {
    private final int doors;
    private final boolean isElectric;

    /**
     * Creates a Car instance.
     *
     * @param brand      the car brand
     * @param engineCC   engine size (0 for electric)
     * @param doors      number of doors
     * @param isElectric whether the car is electric
     */
    public Car(String brand, int engineCC, int doors, boolean isElectric) {
        super(brand, engineCC);
        this.doors = doors;
        this.isElectric = isElectric;
    }

    /**
     * Gets the number of doors.
     *
     * @return door count
     */
    public int doors() {
        return doors;
    }

    /**
     * Checks if the car is electric.
     *
     * @return true if electric, false if combustion
     */
    public boolean isElectric() {
        return isElectric;
    }

    @Override
    public String describe() {
        return String.format("Car[brand=%s, engineCC=%d, doors=%d, electric=%b]",
            brand(), engineCC(), doors, isElectric);
    }
}

/**
 * Motorcycle implementation with sidecar option.
 * Final modifier prevents any further subclassing.
 */
final class Motorcycle extends MotorVehicle {
    private final boolean hasSidecar;

    /**
     * Creates a Motorcycle instance.
     *
     * @param brand      the motorcycle brand
     * @param engineCC   engine size
     * @param hasSidecar whether the motorcycle has a sidecar
     */
    public Motorcycle(String brand, int engineCC, boolean hasSidecar) {
        super(brand, engineCC);
        this.hasSidecar = hasSidecar;
    }

    /**
     * Checks if the motorcycle has a sidecar.
     *
     * @return true if sidecar present
     */
    public boolean hasSidecar() {
        return hasSidecar;
    }

    @Override
    public String describe() {
        return String.format("Motorcycle[brand=%s, engineCC=%d, hasSidecar=%b]",
            brand(), engineCC(), hasSidecar);
    }
}

// ============================================================================
// Final implementation at second level - leaf class directly under Vehicle
// ============================================================================

/**
 * Bicycle implementation with gears and type classification.
 * Final modifier prevents any further subclassing.
 * This is a leaf node at the second level, showing that sealed hierarchies
 * can have final classes at different depths.
 */
final class Bicycle extends Vehicle {
    private final int gears;
    private final String type; // "mountain", "road", "hybrid"

    /**
     * Creates a Bicycle instance.
     *
     * @param brand the bicycle brand
     * @param gears number of gears
     * @param type  bicycle type (mountain, road, hybrid)
     */
    public Bicycle(String brand, int gears, String type) {
        super(brand);
        this.gears = gears;
        this.type = type;
    }

    /**
     * Gets the number of gears.
     *
     * @return gear count
     */
    public int gears() {
        return gears;
    }

    /**
     * Gets the bicycle type.
     *
     * @return type string (mountain, road, hybrid)
     */
    public String type() {
        return type;
    }

    @Override
    public String describe() {
        return String.format("Bicycle[brand=%s, gears=%d, type=%s]",
            brand(), gears, type);
    }
}

// ============================================================================
// Main example class
// ============================================================================

/**
 * Demonstrates multi-level sealed hierarchies with vehicle types.
 *
 * Shows:
 * - Creating instances across multiple hierarchy levels
 * - Polymorphic behavior through different sealed levels
 * - Type categorization using instanceof
 * - Reflection on multi-level sealed structure
 */
public class MultiLevelSealedExample {

    /**
     * Main method demonstrating multi-level sealed hierarchy usage.
     */
    public static void main(String[] args) {
        // Create instances from different hierarchy levels
        var vehicles = List.of(
            new Car("Tesla", 0, 4, true),
            new Car("BMW", 3000, 2, false),
            new Motorcycle("Harley-Davidson", 1200, false),
            new Motorcycle("Ural", 750, true),
            new Bicycle("Trek", 21, "mountain"),
            new Bicycle("Specialized", 18, "road")
        );

        // Display all vehicles
        System.out.println("=== Vehicle Inventory ===");
        for (var vehicle : vehicles) {
            System.out.println(vehicle.describe());
        }

        // Categorize by type using instanceof
        System.out.println("\n=== Categorization ===");
        long motorVehicles = vehicles.stream()
            .filter(v -> v instanceof MotorVehicle)
            .count();
        long bicycles = vehicles.stream()
            .filter(v -> v instanceof Bicycle)
            .count();

        System.out.println("Motor Vehicles: " + motorVehicles);
        System.out.println("Bicycles: " + bicycles);

        // Type-specific operations
        System.out.println("\n=== Motor Vehicle Details ===");
        var car = new Car("Audi", 2000, 4, false);
        System.out.println("Car doors: " + car.doors());
        System.out.println("Engine CC: " + car.engineCC());

        // Hierarchy reflection
        System.out.println("\n=== Sealed Hierarchy ===");
        System.out.println("Vehicle is sealed: " + Vehicle.class.isSealed());
        System.out.println("MotorVehicle is sealed: " + MotorVehicle.class.isSealed());
        System.out.println("Car is final: " +
            java.lang.reflect.Modifier.isFinal(Car.class.getModifiers()));
    }
}
