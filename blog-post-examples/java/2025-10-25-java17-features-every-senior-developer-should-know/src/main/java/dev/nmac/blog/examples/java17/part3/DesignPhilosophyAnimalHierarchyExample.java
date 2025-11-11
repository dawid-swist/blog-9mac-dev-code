package dev.nmac.blog.examples.java17.part3;

/**
 * Demonstrates the design philosophy of sealed classes:
 * Pattern 1 - Full Control Chain (sealed → sealed → final)
 *
 * Shows how to maintain maximal control over the entire hierarchy
 * using sealed at each level with final leaf nodes.
 */
public class DesignPhilosophyAnimalHierarchyExample {

    // Top level: sealed, permits intermediate levels
    public sealed abstract static class Animal
        permits Mammal, Bird {
        protected String name;

        protected Animal(String name) {
            this.name = name;
        }

        public String name() { return name; }
        public abstract String sound();
        public abstract String describe();
    }

    // Middle level: sealed, permits leaf implementations
    public sealed abstract static class Mammal extends Animal
        permits Dog, Cat {

        protected Mammal(String name) {
            super(name);
        }

        public abstract String furColor();
    }

    // Leaf level: final, no extensions
    public static final class Dog extends Mammal {
        private final String breed;

        public Dog(String name, String breed) {
            super(name);
            this.breed = breed;
        }

        public String breed() { return breed; }

        @Override
        public String sound() {
            return "woof";
        }

        @Override
        public String furColor() {
            return "brown";
        }

        @Override
        public String describe() {
            return String.format("Dog[name=%s, breed=%s, sound=%s]", name, breed, sound());
        }
    }

    // Leaf level: final, no extensions
    public static final class Cat extends Mammal {
        private final boolean isIndoor;

        public Cat(String name, boolean isIndoor) {
            super(name);
            this.isIndoor = isIndoor;
        }

        public boolean isIndoor() { return isIndoor; }

        @Override
        public String sound() {
            return "meow";
        }

        @Override
        public String furColor() {
            return "gray";
        }

        @Override
        public String describe() {
            return String.format("Cat[name=%s, indoor=%b, sound=%s]", name, isIndoor, sound());
        }
    }

    // Final implementation at second level
    public static final class Bird extends Animal {
        private final double wingspan;

        public Bird(String name, double wingspan) {
            super(name);
            this.wingspan = wingspan;
        }

        public double wingspan() { return wingspan; }

        @Override
        public String sound() {
            return "chirp";
        }

        @Override
        public String describe() {
            return String.format("Bird[name=%s, wingspan=%.1f, sound=%s]", name, wingspan, sound());
        }
    }

    // This would NOT compile - Dog is final!
    // public class ServiceDog extends Dog { }
    // ❌ Compile error: Dog is final

    public static void main(String[] args) {
        System.out.println("=== Animal Hierarchy Demonstrations ===");

        var dog = new Dog("Buddy", "Golden Retriever");
        var cat = new Cat("Whiskers", true);
        var bird = new Bird("Tweety", 20.5);

        System.out.println(dog.describe());
        System.out.println("  Sound: " + dog.sound());
        System.out.println("  Fur color: " + dog.furColor());

        System.out.println("\n" + cat.describe());
        System.out.println("  Sound: " + cat.sound());
        System.out.println("  Fur color: " + cat.furColor());

        System.out.println("\n" + bird.describe());
        System.out.println("  Sound: " + bird.sound());
        System.out.println("  Wingspan: " + bird.wingspan());

        System.out.println("\n=== Sealed Hierarchy Control ===");
        System.out.println("Animal is sealed: " + Animal.class.isSealed());
        System.out.println("Mammal is sealed: " + Mammal.class.isSealed());

        System.out.println("\nDog is final: " + java.lang.reflect.Modifier.isFinal(Dog.class.getModifiers()));
        System.out.println("Cat is final: " + java.lang.reflect.Modifier.isFinal(Cat.class.getModifiers()));
        System.out.println("Bird is final: " + java.lang.reflect.Modifier.isFinal(Bird.class.getModifiers()));

        System.out.println("\nAnimal permitted subclasses:");
        for (var permitted : Animal.class.getPermittedSubclasses()) {
            System.out.println("  - " + permitted.getSimpleName());
        }

        System.out.println("\nMammal permitted subclasses:");
        for (var permitted : Mammal.class.getPermittedSubclasses()) {
            System.out.println("  - " + permitted.getSimpleName());
        }

        System.out.println("\n=== Why Each Level is a Checkpoint ===");
        System.out.println("✓ Animal controls who extends it (only Mammal, Bird)");
        System.out.println("✓ Mammal independently controls its children (only Dog, Cat)");
        System.out.println("✓ Dog is final - the chain ends (no ServiceDog possible)");
        System.out.println("✓ Bird is final - the chain ends");
        System.out.println("✓ No level can be bypassed - full compiler verification");
    }
}
