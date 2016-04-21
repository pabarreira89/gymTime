package gymTime.test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import gymTime.entities.User;
import gymTime.entities.User.user_type;
import gymTime.util.Transaction;
import gymTime.util.TransactionUtil;

public class UserTest {
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
	public void testCreateUser() {
		EntityManager em = emf.createEntityManager();
		
		final User user = new User();
		user.setFull_name("Pablo");
		user.setType(user_type.ADM);
		TransactionUtil.doTransaction(new Transaction() {
			public void run(EntityManager em) {
				em.persist(user);
			}
		}, em);
		
		User userRecuperado = em.createQuery("SELECT u FROM User u WHERE u.fullName = 'Pablo'", User.class)
				.getSingleResult();
		assertEquals(user_type.ADM, userRecuperado.getType());
		
	}
	
	@Test
	public void testUpdateUser() {
		EntityManager em = emf.createEntityManager();
		
		final User user = new User();
		user.setFull_name("Ruben");
		user.setType(user_type.EMP);
		
		TransactionUtil.doTransaction(new Transaction() {
			public void run(EntityManager em) {
				em.persist(user);
			}
		}, em);

		TransactionUtil.doTransaction(new Transaction() {
			public void run(EntityManager em) {
				User userRecuperado = em
						.createQuery("SELECT u FROM User u WHERE u.fullName = 'Ruben'", User.class)
						.getSingleResult();
				assertEquals(user_type.EMP, userRecuperado.getType());
				userRecuperado.setFull_name("Pedro");
			}
		}, em);

		User userRecuperadoModificado = em
				.createQuery("SELECT u FROM User u WHERE u.fullName = 'Pedro'", User.class)
				.getSingleResult();
		assertFalse(userRecuperadoModificado == null);
	}
}