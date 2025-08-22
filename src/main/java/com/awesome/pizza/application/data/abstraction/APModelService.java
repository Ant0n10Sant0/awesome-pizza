package com.awesome.pizza.application.data.abstraction;

import org.springframework.transaction.annotation.Transactional;

import com.awesome.pizza.application.data.core.CoreMessage;
import com.awesome.pizza.application.data.exception.APApplicationException;
import com.awesome.pizza.application.data.exception.APException;
import com.awesome.pizza.application.data.exception.APNotFoundException;

/**
 * Abstract class for the service classes that deal with {@link APModel}.
 * 
 * @hidden Since I implemented logical deletion, I decided to build my own
 *         entity life-cycle instead of relying on {@code EntityCallback}
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

	/* ***************************************************************************/
	/* * MAIN METHODS ************************************************************/
	/* ***************************************************************************/

	/**
	 * Gets the entity from the database using its id.
	 * 
	 * @throws APException General error
	 */
	@SuppressWarnings("unchecked")
	public T getById(long id) throws APException {
		// If the entity implements logical deletion, it uses the method with the
		// additional filter
		if (dao instanceof LogicalDeleteDao<?> logicDao) {
			return (T) logicDao.findByLogDelIsFalseAndId(id).orElseThrow(APNotFoundException::new);
		}
		return dao().findById(id).orElseThrow(APNotFoundException::new);
	} // getById

	/**
	 * Inserts the input entity in the database.
	 * 
	 * @throws APApplicationException If the entity already exists in the database
	 * @throws APException            General error
	 */
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

	/**
	 * Updates the input entity in the database.
	 * 
	 * @throws APException General error
	 */
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

	/**
	 * Deletes the input entity from the database.
	 * 
	 * @throws APException General error
	 */
	public void delete(T model) throws APException {
		beforeAll(model);
		beforeDelete(model);
		doDelete(model);
		afterDelete(model);
		afterAll(model);
	} // delete

	/* ***************************************************************************/
	/* * LIFE CYCLE **************************************************************/
	/* ***************************************************************************/

	/** The actual insert operation. */
	protected T doInsert(T model) throws APException {
		// If the entity already exits it throws an exception
		long id = model.getId();
		if (id != 0L && dao().existsById(id)) {
			throw new APApplicationException(CoreMessage.ENTITY_ALREADY_EXISTS);
		}
		return dao().save(model);
	} // doInsert

	/** The actual update operation. */
	protected T doUpdate(T model) throws APException {
		return dao().save(model);
	} // doUpdate

	/** The actual delete operation. */
	@SuppressWarnings("unchecked")
	protected void doDelete(T model) throws APException {
		if (model instanceof LogicalDelete logicalModel) {
			dao().save((T) logicalModel.setLogDel(true));
			return;
		}
		dao().deleteById(model.getId());
	} // doDelete

	/** Method called before every persistence operations. */
	protected void beforeAll(T model) throws APException {
	} // beforeAll

	/** Method called before every insert and update operations. */
	protected void beforeUpsert(T model) throws APException {
	} // beforeUpsert

	/** Method called before every update operations. */
	protected void beforeUpdate(T model) throws APException {
	} // beforeUpdate

	/** Method called before every insert operations. */
	protected void beforeInsert(T model) throws APException {
	} // beforeInsert

	/** Method called before every delete operations. */
	protected void beforeDelete(T model) throws APException {
	} // beforeDelete

	/** Method called after every persistence operations. */
	protected void afterAll(T model) throws APException {
	} // afterAll

	/** Method called after every insert and update operations. */
	protected void afterUpsert(T model) throws APException {
	} // afterUpsert

	/** Method called after every update operations. */
	protected void afterUpdate(T model) throws APException {
	} // afterUpdate

	/** Method called after every insert operations. */
	protected void afterInsert(T model) throws APException {
	} // afterInsert

	/** Method called after every delete operations. */
	protected void afterDelete(T model) throws APException {
	} // afterDelete
} // APModelService
