package dev.nmac.blog.examples.springframework6;

import dev.nmac.blog.examples.springframework6.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class CoffeeSpringConfigApp {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        var coffeeMaker = context.getBean("coffeeMaker", CoffeeMaker.class);

        System.out.println("Using: " + coffeeMaker.getWaterType());
        System.out.println("\nAvailable coffees:");
        List<String> coffees = coffeeMaker.getAvailableCoffees();
        for (int i = 0; i < coffees.size(); i++) {
            System.out.println(i + ": " + coffees.get(i));
        }

        System.out.println("\nBrewing each type:");
        for (int i = 0; i < coffees.size(); i++) {
            System.out.println(coffeeMaker.brewCoffee(i));
        }

        context.close();
    }
}