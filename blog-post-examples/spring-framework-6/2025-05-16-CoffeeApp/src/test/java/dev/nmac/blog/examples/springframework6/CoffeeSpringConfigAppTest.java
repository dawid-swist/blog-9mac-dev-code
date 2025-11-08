package dev.nmac.blog.examples.springframework6;

import dev.nmac.blog.examples.springframework6.api.Water;
import dev.nmac.blog.examples.springframework6.config.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CoffeeSpringConfigApp - Spring Java-based Configuration")
class CoffeeSpringConfigAppTest {

    private AnnotationConfigApplicationContext context;
    private CoffeeMaker coffeeMaker;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        coffeeMaker = context.getBean("coffeeMaker", CoffeeMaker.class);
    }

    void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    @DisplayName("Should initialize Spring context with Java configuration class")
    void shouldInitializeSpringContextWithJavaConfigurationClass() {
        // When: Spring loads AppConfig class
        assertNotNull(context);

        // Then: ApplicationContext is properly initialized with @Configuration class
        assertTrue(context.containsBean("coffeeMaker"));
    }

    @Test
    @DisplayName("Should inject dependencies defined in @Configuration class")
    void shouldInjectDependenciesDefinedInConfigurationClass() {
        // When: CoffeeMaker is retrieved from context
        assertNotNull(coffeeMaker);

        // Then: All dependencies are properly injected
        assertNotNull(coffeeMaker.getWaterType());
        assertFalse(coffeeMaker.getAvailableCoffees().isEmpty());
    }

    @Test
    @DisplayName("Should inject Water bean from @Bean method")
    void shouldInjectWaterBeanFromBeanMethod() {
        // When: Get water type from coffeeMaker
        String waterType = coffeeMaker.getWaterType();

        // Then: Water is properly injected from @Bean method
        assertEquals("Filtered water", waterType);
    }

    @Test
    @DisplayName("Should inject List of CoffeeBean implementations from @Bean method")
    void shouldInjectListOfCoffeeBeanImplementations() {
        // When: Get available coffees
        List<String> availableCoffees = coffeeMaker.getAvailableCoffees();

        // Then: All coffee implementations are injected as a list
        assertEquals(3, availableCoffees.size());
        assertTrue(availableCoffees.contains("Arabica (Medium)"));
        assertTrue(availableCoffees.contains("Robusta (Dark)"));
        assertTrue(availableCoffees.contains("Liberica (Light)"));
    }

    @Test
    @DisplayName("Should support type-safe configuration with Java classes")
    void shouldSupportTypeSafeConfiguration() {
        // Java configuration provides:
        // 1. Type safety - caught at compile time
        // 2. IDE support - auto-completion, refactoring
        // 3. Code reusability - using Java methods

        // When
        var coffees = coffeeMaker.getAvailableCoffees();

        // Then: Configuration was type-safe and proper
        assertEquals(3, coffees.size());
    }

    @Test
    @DisplayName("Should allow programmatic bean configuration")
    void shouldAllowProgrammaticBeanConfiguration() {
        // Java configuration allows:
        // 1. Conditional bean creation
        // 2. Programmatic logic in @Bean methods
        // 3. Type safety with generics

        // When: Get coffeeMaker configured programmatically
        assertNotNull(context.getBean("coffeeMaker", CoffeeMaker.class));
        assertNotNull(context.getBean("water", Water.class));
        assertNotNull(context.getBean("arabicaBeans"));
    }

    @Test
    @DisplayName("Should brew coffee using Java-configured dependencies")
    void shouldBrewCoffeeUsingJavaConfiguredDependencies() {
        // When: Brew Arabica
        String arabicaResult = coffeeMaker.brewCoffee(0);

        // Then: Dependencies are properly wired
        assertTrue(arabicaResult.contains("Filtered water"));
        assertTrue(arabicaResult.contains("Arabica"));
        assertTrue(arabicaResult.contains("Medium"));
    }

    @Test
    @DisplayName("Should support advanced features like conditional beans")
    void shouldSupportConditionalBeans() {
        // Java @Configuration allows:
        // @ConditionalOnProperty
        // @ConditionalOnClass
        // @ConditionalOnMissingBean

        // When: Check if bean exists
        assertTrue(context.containsBean("coffeeMaker"));
        assertTrue(context.containsBean("water"));

        // Then: Beans are present in context
        assertNotNull(context.getBean("coffeeMaker"));
    }

    @Test
    @DisplayName("Should provide type safety through generic List injection")
    void shouldProvideTypeSafetyThroughGenericListInjection() {
        // Java configuration with List<CoffeeBean> provides:
        // 1. Type checking at compile time
        // 2. IDE auto-completion for collection operations
        // 3. Clear intent about expected type

        // When
        List<String> coffees = coffeeMaker.getAvailableCoffees();

        // Then: Proper types were injected
        assertEquals(3, coffees.size());
        coffees.forEach(coffee -> assertNotNull(coffee));
    }

    @Test
    @DisplayName("Should maintain singleton scope by default")
    void shouldMaintainSingletonScopeByDefault() {
        // When: Get the same bean twice
        CoffeeMaker maker1 = context.getBean("coffeeMaker", CoffeeMaker.class);
        CoffeeMaker maker2 = context.getBean("coffeeMaker", CoffeeMaker.class);

        // Then: Both are the same instance (singleton scope)
        assertSame(maker1, maker2);
    }

    @Test
    @DisplayName("Should be the modern preferred approach for Spring configuration")
    void shouldBeModernPreferredApproach() {
        // Spring best practices recommend:
        // 1. Java @Configuration over XML
        // 2. @Bean methods for explicit dependency declaration
        // 3. Type-safe programmatic configuration

        // When: All beans are retrieved
        assertNotNull(context.getBean("coffeeMaker"));
        assertNotNull(context.getBean("water"));

        // Then: Modern approach is fully functional
        assertTrue(context.containsBean("coffeeMaker"));
    }
}
