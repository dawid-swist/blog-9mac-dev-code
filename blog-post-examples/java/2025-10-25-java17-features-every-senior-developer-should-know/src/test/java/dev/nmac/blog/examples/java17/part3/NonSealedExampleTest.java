package dev.nmac.blog.examples.java17.part3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NonSealedExample demonstrating the non-sealed modifier.
 */
class NonSealedExampleTest {

    @Test
    @DisplayName("Should create Dog with breed information")
    void shouldCreateDogWithBreedInformation() {
        var dog = new Dog("Buddy", "Golden Retriever");

        assertEquals("Buddy", dog.name());
        assertEquals("Golden Retriever", dog.breed());
        assertEquals("woof", dog.sound());
    }

    @Test
    @DisplayName("Should create Cat with indoor status")
    void shouldCreateCatWithIndoorStatus() {
        var cat = new Cat("Whiskers", true);

        assertEquals("Whiskers", cat.name());
        assertTrue(cat.isIndoor());
        assertEquals("meow", cat.sound());
    }

    @Test
    @DisplayName("Should create Hamster as Pet subclass")
    void shouldCreateHamsterAsPetSubclass() {
        var hamster = new Hamster("Fluffy", "Alice", "brown");

        assertEquals("Fluffy", hamster.name());
        assertEquals("Alice", hamster.owner());
        assertEquals("brown", hamster.color());
        assertEquals("squeak squeak", hamster.sound());
    }

    @Test
    @DisplayName("Should create GuineaPig as Pet subclass")
    void shouldCreateGuineaPigAsPetSubclass() {
        var guineaPig = new GuineaPig("Patches", "Bob", true);

        assertEquals("Patches", guineaPig.name());
        assertEquals("Bob", guineaPig.owner());
        assertTrue(guineaPig.isLongHaired());
    }

    @Test
    @DisplayName("Should verify Animal and Mammal are sealed but Pet is not")
    void shouldVerifyAnimalAndMammalAreSealedButPetIsNot() {
        assertTrue(Animal.class.isSealed());
        assertTrue(Mammal.class.isSealed());
        assertFalse(Pet.class.isSealed()); // non-sealed!
    }

    @Test
    @DisplayName("Should allow unlimited extensions of non-sealed Pet class")
    void shouldAllowUnlimitedExtensionsOfNonSealedPetClass() {
        // Hamster and GuineaPig can extend Pet without being in permits clause
        assertTrue(Hamster.class.getSuperclass().equals(Pet.class));
        assertTrue(GuineaPig.class.getSuperclass().equals(Pet.class));
    }

    @Test
    @DisplayName("Should update Pet owner")
    void shouldUpdatePetOwner() {
        var hamster = new Hamster("Fluffy", "Alice", "brown");
        assertEquals("Alice", hamster.owner());

        hamster.setOwner("Bob");
        assertEquals("Bob", hamster.owner());
    }

    @Test
    @DisplayName("Should verify Mammal permits Dog, Cat, and Pet")
    void shouldVerifyMammalPermitsDogCatAndPet() {
        assertTrue(Mammal.class.isSealed());

        var permitted = Mammal.class.getPermittedSubclasses();
        assertEquals(3, permitted.length);
    }

    @Test
    @DisplayName("Should correctly identify Pet instances")
    void shouldCorrectlyIdentifyPetInstances() {
        var hamster = new Hamster("Fluffy", "Alice", "brown");
        var guineaPig = new GuineaPig("Patches", "Bob", true);

        assertTrue(hamster instanceof Pet);
        assertTrue(guineaPig instanceof Pet);
        // Note: Dog cannot be Pet (sealed hierarchy - Dog is final, separate branch)
    }

    @Test
    @DisplayName("Should verify all animals are Animal instances")
    void shouldVerifyAllAnimalsAreAnimalInstances() {
        var dog = new Dog("Buddy", "Golden Retriever");
        var cat = new Cat("Whiskers", true);
        var bird = new Bird("Tweety", 15.5);
        var fish = new Fish("Nemo", "saltwater");
        var hamster = new Hamster("Fluffy", "Alice", "brown");

        assertTrue(dog instanceof Animal);
        assertTrue(cat instanceof Animal);
        assertTrue(bird instanceof Animal);
        assertTrue(fish instanceof Animal);
        assertTrue(hamster instanceof Animal);
    }

    @Test
    @DisplayName("Should create Bird with wingspan")
    void shouldCreateBirdWithWingspan() {
        var bird = new Bird("Tweety", 15.5);

        assertEquals("Tweety", bird.name());
        assertEquals(15.5, bird.wingspan());
        assertEquals("chirp", bird.sound());
    }

    @Test
    @DisplayName("Should create Fish with water type")
    void shouldCreateFishWithWaterType() {
        var fish = new Fish("Nemo", "saltwater");

        assertEquals("Nemo", fish.name());
        assertEquals("saltwater", fish.waterType());
        assertEquals("blub", fish.sound());
    }
}
