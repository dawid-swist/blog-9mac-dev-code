package dev.nmac.blog.examples.java17.part3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MultiLevelSealedExample demonstrating multi-level sealed hierarchies.
 */
class MultiLevelSealedExampleTest {

    @Test
    @DisplayName("Should create Car with correct properties")
    void shouldCreateCarWithCorrectProperties() {
        var car = new Car("Tesla", 0, 4, true);

        assertEquals("Tesla", car.brand());
        assertEquals(0, car.engineCC());
        assertEquals(4, car.doors());
        assertTrue(car.isElectric());
    }

    @Test
    @DisplayName("Should create Motorcycle with correct properties")
    void shouldCreateMotorcycleWithCorrectProperties() {
        var motorcycle = new Motorcycle("Harley-Davidson", 1200, false);

        assertEquals("Harley-Davidson", motorcycle.brand());
        assertEquals(1200, motorcycle.engineCC());
        assertFalse(motorcycle.hasSidecar());
    }

    @Test
    @DisplayName("Should create Bicycle with correct properties")
    void shouldCreateBicycleWithCorrectProperties() {
        var bicycle = new Bicycle("Trek", 21, "mountain");

        assertEquals("Trek", bicycle.brand());
        assertEquals(21, bicycle.gears());
        assertEquals("mountain", bicycle.type());
    }

    @Test
    @DisplayName("Should verify multi-level sealed hierarchy")
    void shouldVerifyMultiLevelSealedHierarchy() {
        // Top level is sealed
        assertTrue(Vehicle.class.isSealed());
        var vehiclePermitted = Vehicle.class.getPermittedSubclasses();
        assertEquals(2, vehiclePermitted.length);

        // Second level is sealed
        assertTrue(MotorVehicle.class.isSealed());
        var motorPermitted = MotorVehicle.class.getPermittedSubclasses();
        assertEquals(2, motorPermitted.length);

        // Leaf classes are final
        assertTrue(java.lang.reflect.Modifier.isFinal(Car.class.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isFinal(Motorcycle.class.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isFinal(Bicycle.class.getModifiers()));
    }

    @Test
    @DisplayName("Should correctly identify MotorVehicle instances")
    void shouldCorrectlyIdentifyMotorVehicleInstances() {
        var car = new Car("BMW", 3000, 4, false);
        var motorcycle = new Motorcycle("Yamaha", 600, false);

        assertTrue(car instanceof MotorVehicle);
        assertTrue(motorcycle instanceof MotorVehicle);
        // Note: Bicycle cannot be MotorVehicle (sealed hierarchy prevents it)
    }

    @Test
    @DisplayName("Should correctly identify Vehicle instances")
    void shouldCorrectlyIdentifyVehicleInstances() {
        var car = new Car("BMW", 3000, 4, false);
        var motorcycle = new Motorcycle("Yamaha", 600, false);
        var bicycle = new Bicycle("Giant", 18, "road");

        assertTrue(car instanceof Vehicle);
        assertTrue(motorcycle instanceof Vehicle);
        assertTrue(bicycle instanceof Vehicle);
    }

    @Test
    @DisplayName("Should generate correct description for Car")
    void shouldGenerateCorrectDescriptionForCar() {
        var car = new Car("Tesla", 0, 4, true);
        var description = car.describe();

        assertTrue(description.contains("Tesla"));
        assertTrue(description.contains("0"));
        assertTrue(description.contains("4"));
        assertTrue(description.contains("true"));
    }

    @Test
    @DisplayName("Should generate correct description for Motorcycle")
    void shouldGenerateCorrectDescriptionForMotorcycle() {
        var motorcycle = new Motorcycle("Harley-Davidson", 1200, false);
        var description = motorcycle.describe();

        assertTrue(description.contains("Harley-Davidson"));
        assertTrue(description.contains("1200"));
        assertTrue(description.contains("false"));
    }

    @Test
    @DisplayName("Should generate correct description for Bicycle")
    void shouldGenerateCorrectDescriptionForBicycle() {
        var bicycle = new Bicycle("Trek", 21, "mountain");
        var description = bicycle.describe();

        assertTrue(description.contains("Trek"));
        assertTrue(description.contains("21"));
        assertTrue(description.contains("mountain"));
    }
}
