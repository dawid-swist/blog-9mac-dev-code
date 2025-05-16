package dev.nmac.blog.examples.springframework6.impl;

import dev.nmac.blog.examples.springframework6.api.CoffeeBean;

public class LibericaBeans implements CoffeeBean {
    @Override
    public String getVariety() {
        return "Liberica";
    }

    @Override
    public String getRoastLevel() {
        return "Light";
    }
}