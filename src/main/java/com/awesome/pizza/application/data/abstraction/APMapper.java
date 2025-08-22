package com.awesome.pizza.application.data.abstraction;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.DestinationSetter;
import org.modelmapper.spi.TypeSafeSourceGetter;

/**
 * Abstract class for handling the mapping between a {@link APModel} and DTO
 * class.
 */
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

	/**
	 * Registers a conversion between a {@link APModel}'s property and a DTO's one
	 * in the corresponding {@link TypeMap}.
	 * 
	 * @param <V>    The type of the property to set in the DTO object
	 * @param getter The callback to retrieve from the {@link APModel} the property
	 *               to set in the DTO
	 * @param setter The callback to set the object returned from the getter
	 * 
	 * @apiNote It should be called in the implementation class's constructor, in
	 *          order to register the mapping at initialization.
	 */
	protected <V> void addEntityToDtoConversion(TypeSafeSourceGetter<T, V> getter, DestinationSetter<D, V> setter) {
		addSimpleConversion(entityClass, dtoClass, getter, setter);
	} // addEntityToDtoConversion

	/**
	 * Registers a conversion between a DTO's property and a {@link APModel}'s one
	 * in the corresponding {@link TypeMap}.
	 * 
	 * @param <V>    The type of the property to set in the entity object
	 * @param getter The callback to retrieve from the DTO the property to set in
	 *               the {@link APModel}
	 * @param setter The callback to set the object returned from the getter
	 * 
	 * @apiNote It should be called in the implementation class's constructor, in
	 *          order to register the mapping at initialization.
	 */
	protected <V> void addDtoToEntityConversion(TypeSafeSourceGetter<D, V> getter, DestinationSetter<T, V> setter) {
		addSimpleConversion(dtoClass, entityClass, getter, setter);
	} // addDtoToEntityConversion

	/**
	 * Registers the property conversion between the two input classes in the
	 * corresponding {@link TypeMap}.
	 * 
	 * @param <A>    Source type
	 * @param <B>    Target type
	 * @param <V>    The type of the property to set in the target class object
	 * @param src    The source class
	 * @param trg    The target class
	 * @param getter The callback to retrieve, from the source class, the property
	 *               to set in the target class
	 * @param setter The callback to set the object returned from the getter
	 */
	private <A, B, V> void addSimpleConversion(Class<A> src, Class<B> trg, TypeSafeSourceGetter<A, V> getter,
			DestinationSetter<B, V> setter) {
		// Retrieves the `TypeMap`
		TypeMap<A, B> typeMap = mapper.getTypeMap(src, trg);
		// If it's not already been defined, it creates it
		if (typeMap == null) {
			typeMap = mapper.createTypeMap(src, trg);
		}
		// Adds the mapping to the map
		typeMap.addMappings(expMap -> expMap.using(ctx -> {
			@SuppressWarnings("unchecked")
			A obj = (A) ctx.getSource();
			return getter.get(obj);
		}).map(s -> s, setter));
	} // addSimpleConversion
} // APMapper
