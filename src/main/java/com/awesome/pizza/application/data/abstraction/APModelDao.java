package com.awesome.pizza.application.data.abstraction;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface APModelDao<T extends APModel> extends ListCrudRepository<T, Long> {

}
