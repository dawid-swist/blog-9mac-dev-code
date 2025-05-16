package dev.nmac.blog.examples.springframework6.impl;


import dev.nmac.blog.examples.springframework6.api.CoffeeBean;

public class ArabicaBeans implements CoffeeBean {
    @Override
    public String getVariety() {
        return "Arabica";
    }

    @Override
    public String getRoastLevel() {
        return "Medium";
    }
}
