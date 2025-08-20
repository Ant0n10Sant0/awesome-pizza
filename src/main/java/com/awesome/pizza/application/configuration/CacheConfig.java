package com.awesome.pizza.application.configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {
    public static final String PIZZA_ORDER_CACHE = "pizzaOrderCache";

    @Bean(CacheConfig.PIZZA_ORDER_CACHE)
    BlockingQueue<Long> pizzaOrderCache() {
		return new ArrayBlockingQueue<>(10);
	} // pizzaOrderCache
} // CacheConfig
