package dev.nmac.blog.examples.springframework6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CoffeeSpringXmlApp - Spring Framework XML Configuration")
class CoffeeSpringXmlAppTest {

    private ClassPathXmlApplicationContext context;
    private CoffeeMaker coffeeMaker;

    @BeforeEach
    void setUp() {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        coffeeMaker = context.getBean("coffeeMaker", CoffeeMaker.class);
    }

    void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    @DisplayName("Should load bean definitions from XML configuration file")
    void shouldLoadBeanDefinitionsFromXmlConfiguration() {
        // When: Spring loads applicationContext.xml
        assertNotNull(context);

        // Then: CoffeeMaker bean is created and injected
        assertNotNull(coffeeMaker);
    }

    @Test
    @DisplayName("Should inject Water dependency via XML configuration")
    void shouldInjectWaterDependencyViaXmlConfiguration() {
        // When: CoffeeMaker is obtained from Spring context
        String waterType = coffeeMaker.getWaterType();

        // Then: Water dependency is properly injected
        assertEquals("Filtered water", waterType);
    }

    @Test
    @DisplayName("Should inject list of CoffeeBean implementations via XML")
    void shouldInjectListOfCoffeeBeanImplementationsViaXml() {
        // When: Get available coffees from the injected list
        List<String> availableCoffees = coffeeMaker.getAvailableCoffees();

        // Then: All beans are available through XML configuration
        assertEquals(3, availableCoffees.size());
        assertTrue(availableCoffees.contains("Arabica (Medium)"));
        assertTrue(availableCoffees.contains("Robusta (Dark)"));
        assertTrue(availableCoffees.contains("Liberica (Light)"));
    }

    @Test
    @DisplayName("Should brew coffee using XML-configured dependencies")
    void shouldBrewCoffeeUsingXmlConfiguredDependencies() {
        // When: Brew coffee using dependencies from XML
        String result = coffeeMaker.brewCoffee(0);

        // Then: Coffee is brewed with XML-configured beans
        assertNotNull(result);
        assertTrue(result.contains("Making coffee"));
        assertTrue(result.contains("Arabica"));
    }

    @Test
    @DisplayName("Should support declarative configuration through XML")
    void shouldSupportDeclarativeConfigurationThroughXml() {
        // XML configuration allows:
        // 1. Separation of configuration from code
        // 2. External management of bean wiring
        // 3. Changes to configuration without recompilation

        // When: CoffeeMaker is retrieved from context
        assertNotNull(coffeeMaker);

        // Then: Bean was created according to XML declaration
        List<String> coffees = coffeeMaker.getAvailableCoffees();
        assertEquals(3, coffees.size());
    }

    @Test
    @DisplayName("Should enable externalization of configuration")
    void shouldEnableExternalizationOfConfiguration() {
        // Unlike Pure Java DI or SPI, XML configuration can be:
        // - Stored externally
        // - Changed without recompiling
        // - Managed by non-developers

        // When: Get beans from context
        assertNotNull(context.getBean("water"));
        assertNotNull(context.getBean("arabicaBeans"));
        assertNotNull(context.getBean("robustaBeans"));
        assertNotNull(context.getBean("libericaBeans"));
    }

    @Test
    @DisplayName("Should support bean instantiation and wiring through XML")
    void shouldSupportBeanInstantiationAndWiringThroughXml() {
        // XML Bean definition elements:
        // <bean id="coffeeMaker" class="..." >
        //   <constructor-arg ref="filteredWater"/>
        //   <constructor-arg ref="coffeeList"/>
        // </bean>

        // When
        var brewResult = coffeeMaker.brewCoffee(1); // Robusta

        // Then: Dependencies were correctly wired by Spring
        assertTrue(brewResult.contains("Robusta"));
    }

    @Test
    @DisplayName("Should maintain singleton scope for beans by default in XML config")
    void shouldMaintainSingletonScopeForBeansByDefault() {
        // When: Get the same bean twice
        CoffeeMaker maker1 = context.getBean("coffeeMaker", CoffeeMaker.class);
        CoffeeMaker maker2 = context.getBean("coffeeMaker", CoffeeMaker.class);

        // Then: Both references point to the same instance (singleton)
        assertSame(maker1, maker2);
    }
}
