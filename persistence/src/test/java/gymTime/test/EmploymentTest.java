package gymTime.test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import gymTime.entities.Employee;
import gymTime.entities.Employment;
import gymTime.entities.Employment.ratingJob;
import gymTime.util.Transaction;
import gymTime.util.TransactionUtil;

public class EmploymentTest {
	private static EntityManagerFactory emf;

	@BeforeClass
	public static void createEntityManagerFactory() {
		emf = Persistence.createEntityManagerFactory("gymTimeTest");
	}

	@AfterClass
	public static void closeEntityManagerFactory() {
		emf.close();
	}
	
	@Test
	public void testCreateEmployment() {

		EntityManager em = emf.createEntityManager();

		final Employment employment = new Employment();
		employment.setRating(ratingJob.Administrator);
		TransactionUtil.doTransaction(new Transaction() {
			//@Override
			public void run(EntityManager em) {
				em.persist(employment);
			}
		}, em);

		Employment employmentRecuperado = em.createQuery("SELECT t FROM Employment t WHERE t.rating = 0", Employment.class)
				.getSingleResult();
				
		assertEquals(ratingJob.Administrator, employmentRecuperado.getRating());
	}
	
	@Test
	public void testCreateEmploymentWithEmployees()
	{
		EntityManager em = emf.createEntityManager();
		
		final Employment employment = new Employment();
		employment.setRating(ratingJob.Employeed);
		TransactionUtil.doTransaction(new Transaction() {
			//@Override
			public void run(EntityManager em) {
				em.persist(employment);
			}
		}, em);

		Employment employmentRecuperado = em.createQuery("SELECT t FROM Employment t WHERE t.rating = 1", Employment.class)
				.getSingleResult();
		assertEquals(ratingJob.Employeed, employmentRecuperado.getRating());
		
		final Employee employee = new Employee();
		employee.setFullName("Diego");
		TransactionUtil.doTransaction(new Transaction() {
			//@Override
			public void run(EntityManager em) {
				em.persist(employee);
			}
		}, em);
		
		Employee employeeRecuperado = em.createQuery("SELECT p FROM Employee p WHERE p.fullName = 'Diego'", Employee.class)
				.getSingleResult();
		assertEquals("Diego", employeeRecuperado.getFullName());
	}
	
	@Test
	public void testGetEmployees(){
		EntityManager em = emf.createEntityManager();
		
		final Employment employment = new Employment();
		employment.setRating(ratingJob.Fired);
		TransactionUtil.doTransaction(new Transaction() {
		//	@Override
			public void run(EntityManager em) {
				em.persist(employment);
			}
		}, em);

		Employment employmentRecuperado = em.createQuery("SELECT t FROM Employment t WHERE t.rating = 2", Employment.class)
				.getSingleResult();
		assertEquals(ratingJob.Fired, employmentRecuperado.getRating());
		
		final Employee employee = new Employee();
		employee.setFullName("Alex");
		employee.setEmployment(employmentRecuperado);
		TransactionUtil.doTransaction(new Transaction() {
		//	@Override
			public void run(EntityManager em) {
				em.persist(employee);
			}
		}, em);
		
		Employment employmentToni = em.createQuery("SELECT t FROM Employment t WHERE t.rating = 2", Employment.class)
				.getSingleResult();
		assertEquals(ratingJob.Fired, employmentToni.getRating());
		
		System.out.println(employmentToni + " " + employmentRecuperado);
		
		assertEquals(1,employmentToni.getEmployees().size());
	}
}
