package com.awesome.pizza.application.configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.awesome.pizza.application.data.collection.SetBlockingQueue;

@Configuration
public class CacheConfig {
    public static final String PIZZA_ORDER_CACHE = "pizzaOrderCache";

    @Bean(CacheConfig.PIZZA_ORDER_CACHE)
	SetBlockingQueue<Long> pizzaOrderCache(@Value("${awesome.pizza.pizza-order-cache.size:1000}") int size) {
		BlockingQueue<Long> queue = new ArrayBlockingQueue<>(size);
		return new SetBlockingQueue<>(queue);
	} // pizzaOrderCache
} // CacheConfig
