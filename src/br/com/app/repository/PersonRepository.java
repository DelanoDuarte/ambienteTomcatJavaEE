/**
 * 
 */
package br.com.app.repository;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;

import br.com.app.domain.Person;
import br.com.app.util.JPAUtil;

/**
 * @author delano.junior
 *
 */
@RequestScoped
public class PersonRepository implements IPersonRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.app.repository.IPersonRepository#savePerson(br.com.app.domain.
	 * Person)
	 */
	private JPAUtil jpaUtil = new JPAUtil();

	private EntityManager em = jpaUtil.getActiveEm();

	@Override
	public void savePerson(Person person) {
		try {

			if (person.getId() == null) {
				em.persist(person);
			} else {
				em.merge(person);
			}

		} catch (Exception e) {

		} finally {
			jpaUtil.closeEm();
		}

	}

}
