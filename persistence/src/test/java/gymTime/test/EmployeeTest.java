package gymTime.test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import gymTime.entities.Employee;
//import gymTime.entities.Employment;
//import gymTime.entities.Employment.ratingJob;
import gymTime.util.Transaction;
import gymTime.util.TransactionUtil;

public class EmployeeTest {
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
	public void testCreateEmployee() {
		EntityManager em = emf.createEntityManager();
		
		final Employee employee = new Employee();
		employee.setFullName("Pablo");
		//Employment employment = new Employment();				
		//employment.setRating(ratingJob.Employeed);
		//employee.setEmployment(employment);
		TransactionUtil.doTransaction(new Transaction() {
			//@Override
			public void run(EntityManager em) {
				em.persist(employee);
			}
		}, em);
		
		Employee employeeRecuperado = em.createQuery("SELECT u FROM Employee u WHERE u.fullName = 'Pablo'", Employee.class)
				.getSingleResult();
		assertEquals("Pablo", employeeRecuperado.getFullName());
		
	}
	
	@Test
	public void testRenameEmployee() {
		EntityManager em = emf.createEntityManager();
		
		final Employee employee = new Employee();
		employee.setFullName("María");		
		TransactionUtil.doTransaction(new Transaction() {
			//@Override
			public void run(EntityManager em) {
				em.persist(employee);
			}
		}, em);

		TransactionUtil.doTransaction(new Transaction() {
			//@Override
			public void run(EntityManager em) {
				Employee employeeRecuperado = em
						.createQuery("SELECT u FROM Employee u WHERE u.fullName = 'María'", Employee.class)
						.getSingleResult();
				assertEquals(employee.getFullName(), employeeRecuperado.getFullName());
				employeeRecuperado.setFullName("Mery");
			}
		}, em);

		Employee employeeRecuperadoModificado = em
				.createQuery("SELECT u FROM Employee u WHERE u.fullName = 'Mery'", Employee.class)
				.getSingleResult();
		assertFalse(employeeRecuperadoModificado == null);
	}
}