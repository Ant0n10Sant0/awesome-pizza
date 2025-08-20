package com.awesome.pizza.application.data.abstraction;

import java.util.List;

import org.modelmapper.ModelMapper;

public abstract class APMapper<T extends APModel, D> {

	private final Class<T> entityClass;
	private final Class<D> dtoClass;
	protected final ModelMapper mapper;

	protected APMapper(Class<T> entityClass, Class<D> dtoClass, ModelMapper mapper) {
		this.entityClass = entityClass;
		this.dtoClass = dtoClass;
		this.mapper = mapper;
	} // APMapper

	public D toDto(T entity) {
		return mapper.map(entity, dtoClass);
	} // toDto

	public T toEntity(D dto) {
		return mapper.map(dto, entityClass);
	} // toEntity

	public List<D> toDtoList(List<T> entities) {
		return entities.stream().map(this::toDto).toList();
	} // toDtoList

	public List<T> toEntityList(List<D> dtos) {
		return dtos.stream().map(this::toEntity).toList();
	} // toEntityList
} // APMapper
