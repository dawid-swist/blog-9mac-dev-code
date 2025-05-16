package dev.nmac.blog.examples.springframework6.impl;

import dev.nmac.blog.examples.springframework6.api.CoffeeBean;

public class RobustaBeans implements CoffeeBean {
    @Override
    public String getVariety() {
        return "Robusta";
    }

    @Override
    public String getRoastLevel() {
        return "Dark";
    }
}
