package dev.nmac.blog.examples.java17.part3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Design Philosophy: Animal Hierarchy (sealed → sealed → final)")
class DesignPhilosophyAnimalHierarchyExampleTest {

    @Test
    @DisplayName("Should create Dog with breed information")
    void shouldCreateDogWithBreedInformation() {
        var dog = new DesignPhilosophyAnimalHierarchyExample.Dog("Buddy", "Golden Retriever");

        assertEquals("Buddy", dog.name());
        assertEquals("Golden Retriever", dog.breed());
        assertEquals("woof", dog.sound());
        assertEquals("brown", dog.furColor());
    }

    @Test
    @DisplayName("Should create Cat with indoor status")
    void shouldCreateCatWithIndoorStatus() {
        var cat = new DesignPhilosophyAnimalHierarchyExample.Cat("Whiskers", true);

        assertEquals("Whiskers", cat.name());
        assertTrue(cat.isIndoor());
        assertEquals("meow", cat.sound());
        assertEquals("gray", cat.furColor());
    }

    @Test
    @DisplayName("Should create Bird with wingspan")
    void shouldCreateBirdWithWingspan() {
        var bird = new DesignPhilosophyAnimalHierarchyExample.Bird("Tweety", 20.5);

        assertEquals("Tweety", bird.name());
        assertEquals(20.5, bird.wingspan(), 0.001);
        assertEquals("chirp", bird.sound());
    }

    @Test
    @DisplayName("Should verify full control chain: Animal and Mammal are sealed")
    void shouldVerifyFullControlChain() {
        assertTrue(DesignPhilosophyAnimalHierarchyExample.Animal.class.isSealed());
        assertTrue(DesignPhilosophyAnimalHierarchyExample.Mammal.class.isSealed());
    }

    @Test
    @DisplayName("Should verify leaf nodes are final")
    void shouldVerifyLeafNodesAreFinal() {
        assertTrue(java.lang.reflect.Modifier.isFinal(
            DesignPhilosophyAnimalHierarchyExample.Dog.class.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isFinal(
            DesignPhilosophyAnimalHierarchyExample.Cat.class.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isFinal(
            DesignPhilosophyAnimalHierarchyExample.Bird.class.getModifiers()));
    }

    @Test
    @DisplayName("Should verify Animal permits only Mammal and Bird")
    void shouldVerifyAnimalPermitsCorrectClasses() {
        var permitted = DesignPhilosophyAnimalHierarchyExample.Animal.class.getPermittedSubclasses();
        assertEquals(2, permitted.length);

        var names = java.util.Arrays.stream(permitted)
            .map(Class::getSimpleName)
            .collect(java.util.stream.Collectors.toSet());

        assertTrue(names.contains("Mammal"));
        assertTrue(names.contains("Bird"));
    }

    @Test
    @DisplayName("Should verify Mammal permits only Dog and Cat")
    void shouldVerifyMammalPermitsCorrectClasses() {
        var permitted = DesignPhilosophyAnimalHierarchyExample.Mammal.class.getPermittedSubclasses();
        assertEquals(2, permitted.length);

        var names = java.util.Arrays.stream(permitted)
            .map(Class::getSimpleName)
            .collect(java.util.stream.Collectors.toSet());

        assertTrue(names.contains("Dog"));
        assertTrue(names.contains("Cat"));
    }

    @Test
    @DisplayName("Should create correct descriptions")
    void shouldCreateCorrectDescriptions() {
        var dog = new DesignPhilosophyAnimalHierarchyExample.Dog("Buddy", "Golden Retriever");
        var description = dog.describe();

        assertTrue(description.contains("Buddy"));
        assertTrue(description.contains("Golden Retriever"));
        assertTrue(description.contains("woof"));
    }

    @Test
    @DisplayName("Should demonstrate why each level is a checkpoint")
    void shouldDemonstrateHierarchyCheckpoints() {
        // Each level independently controls what it permits
        var animalPermitted = DesignPhilosophyAnimalHierarchyExample.Animal.class.getPermittedSubclasses();
        var mammalPermitted = DesignPhilosophyAnimalHierarchyExample.Mammal.class.getPermittedSubclasses();

        // They are independent - each decides its own hierarchy
        assertTrue(DesignPhilosophyAnimalHierarchyExample.Animal.class.isSealed());
        assertTrue(DesignPhilosophyAnimalHierarchyExample.Mammal.class.isSealed());

        // Animal permits Mammal and Bird (2 items)
        assertEquals(2, animalPermitted.length);

        // Mammal independently permits Dog and Cat (2 items)
        assertEquals(2, mammalPermitted.length);

        // These are different sets with different purposes
        assertFalse(java.util.Arrays.equals(animalPermitted, mammalPermitted));
    }
}
