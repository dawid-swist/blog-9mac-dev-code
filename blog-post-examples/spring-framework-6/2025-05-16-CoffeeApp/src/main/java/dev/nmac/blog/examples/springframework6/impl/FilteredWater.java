package dev.nmac.blog.examples.springframework6.impl;

import dev.nmac.blog.examples.springframework6.api.Water;

// Concrete ingredients
public class FilteredWater implements Water {
    @Override
    public String getType() {
        return "Filtered water";
    }
}
