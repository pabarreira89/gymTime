package gymTime.test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import gymTime.entities.Employee;
import gymTime.util.Transaction;
import gymTime.util.TransactionUtil;

public class EmployeeTest {
	private static EntityManagerFactory emf;
	
	@BeforeClass
	public static void createEntityManagerFactory() {
		emf = Persistence.createEntityManagerFactory("gymTime");
	}

	@AfterClass
	public static void closeEntityManagerFactory() {
		emf.close();
	}
	
	@Test
	public void testCreateemployee() {
		EntityManager em = emf.createEntityManager();
		
		final Employee employee = new Employee();
		employee.setFullName("Pablo");
//		employee.setEmployment("Eagles");
		TransactionUtil.doTransaction(new Transaction() {
			//@Override
			public void run(EntityManager em) {
				em.persist(employee);
			}
		}, em);
		
		Employee employeeRecuperado = em.createQuery("SELECT u FROM employee u WHERE u.fullName = 'Pablo'", Employee.class)
				.getSingleResult();
		assertEquals("Pablo", employeeRecuperado.getFullName());
		
	}
	
	@Test
	public void testUpdateemployee() {
		EntityManager em = emf.createEntityManager();
		
		final Employee employee = new Employee();
		employee.setFullName("Pablo");
//		employee.setEmployment("Eagles");		
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
						.createQuery("SELECT u FROM employee u WHERE u.fullName = 'Pablo'", Employee.class)
						.getSingleResult();
				assertEquals("Pablo", employeeRecuperado.getFullName());
				employeeRecuperado.setFullName("Pedro");
			}
		}, em);

		Employee employeeRecuperadoModificado = em
				.createQuery("SELECT u FROM employee u WHERE u.fullName = 'Pedro'", Employee.class)
				.getSingleResult();
		assertFalse(employeeRecuperadoModificado == null);
	}
}