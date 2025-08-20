package com.awesome.pizza.application.data.abstraction;

import org.springframework.transaction.annotation.Transactional;

import com.awesome.pizza.application.data.exception.APException;
import com.awesome.pizza.application.data.exception.APNotFoundException;

/**
 * Abstract class for the service classes. Since I implemented logical delete, i
 * decided to build my own entity lifecycle instead of relying on
 * <code>EntityCallback</code>.
 */
@Transactional(rollbackFor = Throwable.class)
public abstract class APModelService<T extends APModel, R extends APModelDao<T>> {
	private final R dao;

	protected APModelService(R dao) {
		this.dao = dao;
	} // APModelService

	protected R dao() {
		return this.dao;
	} // dao

	@SuppressWarnings("unchecked")
	public T getById(long id) throws APException {
		if (dao instanceof LogicalDeleteDao<?> logicDao) {
			return (T) logicDao.findByLogDelIsFalseAndId(id).orElseThrow(APNotFoundException::new);
		}
		return dao().findById(id).orElseThrow(APNotFoundException::new);
	} // getById

	public T insert(T model) throws APException {
		beforeAll(model);
		beforeUpsert(model);
		beforeInsert(model);
		model = doInsert(model);
		afterInsert(model);
		afterUpsert(model);
		afterAll(model);
		return model;
	} // insert

	public T update(T model) throws APException {
		beforeAll(model);
		beforeUpsert(model);
		beforeUpdate(model);
		model = doUpdate(model);
		afterUpdate(model);
		afterUpsert(model);
		afterAll(model);
		return model;
	} // update

	public void delete(T model) throws APException {
		beforeAll(model);
		beforeDelete(model);
		doDelete(model);
		afterDelete(model);
		afterAll(model);
	} // delete

	protected T doInsert(T model) throws APException {
		return dao().save(model);
	} // doInsert

	protected T doUpdate(T model) throws APException {
		return dao().save(model);
	} // doUpdate

	@SuppressWarnings("unchecked")
	public void doDelete(T model) throws APException {
		if (model instanceof LogicalDelete logicalModel) {
			dao().save((T) logicalModel.setLogDel(true));
			return;
		}
		dao().deleteById(model.getId());
	} // doDelete

	protected void beforeAll(T model) throws APException {
	} // beforeAll

	protected void beforeUpsert(T model) throws APException {
	} // beforeUpsert

	protected void beforeUpdate(T model) throws APException {
	} // beforeUpdate

	protected void beforeInsert(T model) throws APException {
	} // beforeInsert

	protected void beforeDelete(T model) throws APException {
	} // beforeDelete

	protected void afterAll(T model) throws APException {
	} // afterAll

	protected void afterUpsert(T model) throws APException {
	} // afterUpsert

	protected void afterUpdate(T model) throws APException {
	} // afterUpdate

	protected void afterInsert(T model) throws APException {
	} // afterInsert

	protected void afterDelete(T model) throws APException {
	} // afterDelete
} // APModelService
