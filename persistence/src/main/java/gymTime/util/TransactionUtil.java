package gymTime.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class TransactionUtil {

	public static void doTransaction(Transaction transaction, EntityManager em) {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			transaction.run(em);
			tx.commit();
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		}
	}

}