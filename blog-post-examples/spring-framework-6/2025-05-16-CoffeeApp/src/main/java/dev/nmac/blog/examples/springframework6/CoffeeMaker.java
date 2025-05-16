package dev.nmac.blog.examples.springframework6;

import dev.nmac.blog.examples.springframework6.api.CoffeeBean;
import dev.nmac.blog.examples.springframework6.api.Water;
import java.util.List;

public class CoffeeMaker {
    private final List<CoffeeBean> availableBeans;
    private final Water water;

    public CoffeeMaker(Water water, List<CoffeeBean> beans) {
        this.water = water;
        this.availableBeans = beans;
    }

    public String brewCoffee(int selection) {
        if (selection < 0 || selection >= availableBeans.size()) {
            throw new IllegalArgumentException("Invalid coffee selection");
        }
        CoffeeBean selected = availableBeans.get(selection);
        return String.format("Making coffee with %s and %s coffee (%s roast)",
                water.getType(),
                selected.getVariety(),
                selected.getRoastLevel());
    }

    public List<String> getAvailableCoffees() {
        return availableBeans.stream()
                .map(bean -> bean.getVariety() + " (" + bean.getRoastLevel() + ")")
                .toList();
    }

    public String getWaterType() {
        return water.getType();
    }
}