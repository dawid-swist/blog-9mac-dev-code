package dev.nmac.blog.examples.springframework6;

import dev.nmac.blog.examples.springframework6.impl.ArabicaBeans;
import dev.nmac.blog.examples.springframework6.impl.FilteredWater;
import dev.nmac.blog.examples.springframework6.impl.LibericaBeans;
import dev.nmac.blog.examples.springframework6.impl.RobustaBeans;

import java.util.List;


// Main application
public class CoffeePureApp {
    public static void main(String[] args) {
        // Manual dependency injection
        var water = new FilteredWater();
        var beans = List.of(
                new ArabicaBeans(),
                new RobustaBeans(),
                new LibericaBeans()
        );
        var coffeeMaker = new CoffeeMaker(water, beans);

        System.out.println("Using: " + coffeeMaker.getWaterType());
        System.out.println("\nAvailable coffees:");
        var coffees = coffeeMaker.getAvailableCoffees();
        for (int i = 0; i < coffees.size(); i++) {
            System.out.println(i + ": " + coffees.get(i));
        }

        System.out.println("\nBrewing each type:");
        for (int i = 0; i < coffees.size(); i++) {
            System.out.println(coffeeMaker.brewCoffee(i));
        }
    }
}