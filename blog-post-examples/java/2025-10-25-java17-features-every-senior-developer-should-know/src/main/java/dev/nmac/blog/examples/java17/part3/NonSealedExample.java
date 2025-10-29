package dev.nmac.blog.examples.java17.part3;

import java.util.List;

/**
 * Example 5: The non-sealed Modifier
 *
 * Demonstrates:
 * - non-sealed modifier to break the seal
 * - Mixed sealed and open hierarchies
 * - Strategic use of non-sealed for extensibility
 * - When and why to break the seal
 * - Reflection on sealed vs non-sealed classes
 *
 * Key concept: The non-sealed modifier strategically breaks the seal at a
 * specific point in the hierarchy. Core abstractions remain sealed with
 * controlled inheritance, but specific branches allow unlimited extensions.
 * This is useful for tight control over core abstractions while providing
 * flexibility in specific areas—like allowing plugin developers to extend
 * certain types while keeping the core hierarchy closed.
 */

// ============================================================================
// Sealed base class - top of hierarchy
// ============================================================================

/**
 * Sealed base Animal class with controlled inheritance.
 * Only Mammal, Bird, and Fish can extend Animal.
 */
sealed abstract class Animal
    permits Mammal, Bird, Fish {

    private final String name;

    /**
     * Constructor for all animals.
     *
     * @param name the animal's name
     */
    protected Animal(String name) {
        this.name = name;
    }

    /**
     * Gets the animal's name.
     *
     * @return the name
     */
    public String name() {
        return name;
    }

    /**
     * Gets the sound the animal makes.
     *
     * @return sound string
     */
    public abstract String sound();
}

// ============================================================================
// Sealed intermediate class - continues controlled hierarchy
// ============================================================================

/**
 * Sealed Mammal class permitting Dog, Cat, and Pet.
 * The hierarchy remains controlled at this level.
 */
sealed abstract class Mammal extends Animal
    permits Dog, Cat, Pet {

    protected Mammal(String name) {
        super(name);
    }
}

// ============================================================================
// Non-sealed class - breaks the seal!
// ============================================================================

/**
 * Non-sealed Pet class - breaks the seal at this point!
 * Anyone can now extend Pet without restriction.
 * This allows unlimited pet types (Hamster, GuineaPig, Rabbit, etc.)
 * while keeping the core Animal hierarchy sealed.
 */
non-sealed abstract class Pet extends Mammal {
    private String owner;

    /**
     * Constructor for pets.
     *
     * @param name  the pet's name
     * @param owner the pet's owner
     */
    protected Pet(String name, String owner) {
        super(name);
        this.owner = owner;
    }

    /**
     * Gets the pet's owner.
     *
     * @return owner name
     */
    public String owner() {
        return owner;
    }

    /**
     * Sets a new owner for the pet.
     *
     * @param owner new owner name
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }
}

// ============================================================================
// Unlimited extensions of non-sealed Pet class
// ============================================================================

/**
 * Hamster extends Pet - allowed because Pet is non-sealed.
 * No need to be listed in a permits clause.
 */
class Hamster extends Pet {
    private final String color;

    /**
     * Creates a Hamster instance.
     *
     * @param name  hamster name
     * @param owner owner name
     * @param color hamster color
     */
    public Hamster(String name, String owner, String color) {
        super(name, owner);
        this.color = color;
    }

    /**
     * Gets the hamster's color.
     *
     * @return color string
     */
    public String color() {
        return color;
    }

    @Override
    public String sound() {
        return "squeak squeak";
    }
}

/**
 * GuineaPig extends Pet - also allowed because Pet is non-sealed.
 */
class GuineaPig extends Pet {
    private final boolean isLongHaired;

    /**
     * Creates a GuineaPig instance.
     *
     * @param name         guinea pig name
     * @param owner        owner name
     * @param isLongHaired whether long-haired
     */
    public GuineaPig(String name, String owner, boolean isLongHaired) {
        super(name, owner);
        this.isLongHaired = isLongHaired;
    }

    /**
     * Checks if long-haired.
     *
     * @return true if long-haired
     */
    public boolean isLongHaired() {
        return isLongHaired;
    }

    @Override
    public String sound() {
        return "wheek wheek";
    }
}

// ============================================================================
// Final implementations in sealed hierarchy
// ============================================================================

/**
 * Final Dog class - part of the sealed Mammal hierarchy.
 */
final class Dog extends Mammal {
    private final String breed;

    /**
     * Creates a Dog instance.
     *
     * @param name  dog name
     * @param breed dog breed
     */
    public Dog(String name, String breed) {
        super(name);
        this.breed = breed;
    }

    /**
     * Gets the dog's breed.
     *
     * @return breed string
     */
    public String breed() {
        return breed;
    }

    @Override
    public String sound() {
        return "woof";
    }
}

/**
 * Final Cat class - part of the sealed Mammal hierarchy.
 */
final class Cat extends Mammal {
    private final boolean isIndoor;

    /**
     * Creates a Cat instance.
     *
     * @param name     cat name
     * @param isIndoor whether indoor cat
     */
    public Cat(String name, boolean isIndoor) {
        super(name);
        this.isIndoor = isIndoor;
    }

    /**
     * Checks if indoor cat.
     *
     * @return true if indoor
     */
    public boolean isIndoor() {
        return isIndoor;
    }

    @Override
    public String sound() {
        return "meow";
    }
}

// ============================================================================
// Other sealed branches
// ============================================================================

/**
 * Final Bird class extending Animal directly.
 */
final class Bird extends Animal {
    private final double wingspan;

    /**
     * Creates a Bird instance.
     *
     * @param name     bird name
     * @param wingspan wingspan in centimeters
     */
    public Bird(String name, double wingspan) {
        super(name);
        this.wingspan = wingspan;
    }

    /**
     * Gets the wingspan.
     *
     * @return wingspan in cm
     */
    public double wingspan() {
        return wingspan;
    }

    @Override
    public String sound() {
        return "chirp";
    }
}

/**
 * Final Fish class extending Animal directly.
 */
final class Fish extends Animal {
    private final String waterType;

    /**
     * Creates a Fish instance.
     *
     * @param name      fish name
     * @param waterType water type (freshwater/saltwater)
     */
    public Fish(String name, String waterType) {
        super(name);
        this.waterType = waterType;
    }

    /**
     * Gets the water type.
     *
     * @return water type string
     */
    public String waterType() {
        return waterType;
    }

    @Override
    public String sound() {
        return "blub";
    }
}

// ============================================================================
// Main example class
// ============================================================================

/**
 * Demonstrates the non-sealed modifier and its strategic use.
 *
 * Shows:
 * - Creating instances from sealed and non-sealed branches
 * - Polymorphic behavior across sealed/non-sealed boundaries
 * - Filtering by sealed vs non-sealed types
 * - Reflection revealing sealed status at each level
 */
public class NonSealedExample {

    /**
     * Main method demonstrating non-sealed modifier usage.
     */
    public static void main(String[] args) {
        // Create instances from both sealed and non-sealed branches
        var animals = List.of(
            new Dog("Buddy", "Golden Retriever"),
            new Cat("Whiskers", true),
            new Bird("Tweety", 15.5),
            new Fish("Nemo", "saltwater"),
            new Hamster("Fluffy", "Alice", "brown"),
            new GuineaPig("Patches", "Bob", true)
        );

        // Display all animals and their sounds
        System.out.println("=== Animal Sounds ===");
        for (var animal : animals) {
            System.out.println(animal.name() + " says: " + animal.sound());
        }

        // Demonstrate non-sealed hierarchy - filter for pets
        System.out.println("\n=== Pet Hierarchy (non-sealed) ===");
        var pets = animals.stream()
            .filter(a -> a instanceof Pet)
            .map(a -> (Pet) a)
            .toList();

        for (var pet : pets) {
            System.out.println(pet.name() + " belongs to " + pet.owner());
        }

        // Reflection on sealed hierarchy
        System.out.println("\n=== Sealed Hierarchy Analysis ===");
        System.out.println("Animal is sealed: " + Animal.class.isSealed());
        System.out.println("Mammal is sealed: " + Mammal.class.isSealed());
        System.out.println("Pet is sealed: " + Pet.class.isSealed()); // false!

        System.out.println("\nMammal permits: ");
        for (var permitted : Mammal.class.getPermittedSubclasses()) {
            System.out.println("  - " + permitted.getSimpleName());
        }

        // Demonstrate owner change (Pet is mutable)
        System.out.println("\n=== Pet Ownership Transfer ===");
        var hamster = new Hamster("Fluffy", "Alice", "brown");
        System.out.println("Original owner: " + hamster.owner());
        hamster.setOwner("Charlie");
        System.out.println("New owner: " + hamster.owner());
    }
}
