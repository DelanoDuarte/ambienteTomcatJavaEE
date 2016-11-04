/**
 * 
 */
package br.com.app.bean;

import java.io.Serializable;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import br.com.app.domain.Person;
import br.com.app.repository.IPersonRepository;

/**
 * @author delano.junior
 *
 */
@Model
public class PersonBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private IPersonRepository personRepository;

	private Person person = new Person();

	public void savePerson() {
		personRepository.savePerson(person);
	}

	public IPersonRepository getPersonRepository() {
		return personRepository;
	}

	public void setPersonRepository(IPersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
