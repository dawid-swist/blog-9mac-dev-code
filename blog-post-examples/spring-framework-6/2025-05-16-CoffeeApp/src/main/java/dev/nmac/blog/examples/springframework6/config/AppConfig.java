package dev.nmac.blog.examples.springframework6.config;

import dev.nmac.blog.examples.springframework6.CoffeeMaker;
import dev.nmac.blog.examples.springframework6.api.CoffeeBean;
import dev.nmac.blog.examples.springframework6.api.Water;
import dev.nmac.blog.examples.springframework6.impl.ArabicaBeans;
import dev.nmac.blog.examples.springframework6.impl.FilteredWater;
import dev.nmac.blog.examples.springframework6.impl.LibericaBeans;
import dev.nmac.blog.examples.springframework6.impl.RobustaBeans;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public Water water() {
        return new FilteredWater();
    }

    @Bean
    public CoffeeBean arabicaBeans() {
        return new ArabicaBeans();
    }

    @Bean
    public CoffeeBean robustaBeans() {
        return new RobustaBeans();
    }

    @Bean
    public CoffeeBean libericaBeans() {
        return new LibericaBeans();
    }

    @Bean
    public CoffeeMaker coffeeMaker(Water water, List<CoffeeBean> beans) {
        return new CoffeeMaker(water, beans);
    }
}