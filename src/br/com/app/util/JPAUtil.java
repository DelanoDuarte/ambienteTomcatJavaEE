/**
 * 
 */
package br.com.app.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author delano.junior
 *
 */
public class JPAUtil {

	private static volatile boolean initialized = false;
	private static Boolean lock = new Boolean(true);
	private static EntityManagerFactory emf = null;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private EntityManager em;

	/**
	 * open Dba and also start a transaction
	 */
	public JPAUtil() {
		this(false);
	}

	/**
	 * open Dba; if readonly no JPA transaction is actually started, meaning you
	 * will have no persistence store. You can still persist stuff, but the
	 * entities won't become managed.
	 */
	public JPAUtil(boolean readOnly) {

		initialize();
		openEm(readOnly);
	}

	public void openEm(boolean readOnly) {
		if (em != null) {
			return;
		}

		em = emf.createEntityManager();

		if (readOnly == false) {
			em.getTransaction().begin();
		}
	}

	/**
	 * Get the em transaction; an active transaction must already exist for this
	 * to succeed.
	 */
	public EntityManager getActiveEm() {
		if (em == null) {
			throw new IllegalStateException("No transaction was active!");
		}

		return em;
	}

	/**
	 * Close the entity manager, properly committing or rolling back a
	 * transaction if one is still active.
	 */
	public void closeEm() {
		if (em == null) {
			return;
		}

		try {
			if (em.getTransaction().isActive()) {

				if (em.getTransaction().getRollbackOnly()) {
					em.getTransaction().rollback();
				} else {
					em.getTransaction().commit();
				}
			}

		} finally {
			em.close();
			em = null;
		}
	}

	/**
	 * Mark the transaction as rollback only, if there is an active transaction
	 * to begin with.
	 */
	public void markRollback() {

		if (em != null) {
			em.getTransaction().setRollbackOnly();
		}
	}

	public boolean isRollbackOnly() {
		return em != null && em.getTransaction().getRollbackOnly();
	}

	// thread safe way to initialize the entity manager factory.
	private void initialize() {

		if (initialized) {
			return;
		}

		synchronized (lock) {

			if (initialized) {
				return;
			}

			initialized = true;

			try {
				emf = Persistence.createEntityManagerFactory("TomcatExample");

			} catch (Throwable t) {
				logger.error("Failed to setup persistence unit!", t);
				return;
			}
		}
	}
}
