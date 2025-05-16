package dev.nmac.blog.examples.springframework6;

import dev.nmac.blog.examples.springframework6.api.CoffeeBean;
import dev.nmac.blog.examples.springframework6.api.Water;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class CoffeeSpiApp {
    public static void main(String[] args) {
        // Load implementations using SPI
        Water water = ServiceLoader.load(Water.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Water implementation found"));

        List<CoffeeBean> beans = ServiceLoader.load(CoffeeBean.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());

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