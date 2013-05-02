package nl.topicus.cobra.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HibernateUtil
{
	private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);

	private HibernateUtil()
	{
	}

	/**
	 * Doet een rollback op de transactie, logt eventuele foutmeldingen, maar laat een
	 * hibernate exception niet optreden. Gebruik deze methode alleen in exception
	 * afhandeling blokken, aangezien je dan niet wilt dat een bestaande exceptie wordt
	 * overschreven.
	 * 
	 * @param transaction
	 *            de transactie die gerollbackt moet worden.
	 */
	public static void rollbackQuietly(Transaction transaction)
	{
		if (transaction == null)
		{
			log.debug("Poging tot rollbacken van een null transaction");
			return;
		}
		try
		{
			if (transaction.isActive())
			{
				log.debug("Rolling back transaction " + transaction);
				transaction.rollback();
			}
			else
				log
					.warn("Transaction not active, nothing to rollback. Transaction: "
						+ transaction);
		}
		catch (HibernateException e)
		{
			log.error("Exception bij het rollbacken van " + transaction, e);
		}
	}

	/**
	 * rolls back the current transaction and opens another one. This is helpfull for
	 * systems that automaticly open and commit/rollback a transaction.
	 * 
	 * @param session
	 *            session containing the transaction
	 * @param transaction
	 *            the transaction
	 * @throws IllegalArgumentException
	 *             if session or transaction is null.
	 * @throws HibernateException
	 *             if there is a problem with the transaction
	 */
	public static final void rollbackAndOpenAnotherTransaction(Transaction transaction,
			Session session)
	{
		Asserts.assertNotNull("session", session);
		Asserts.assertNotNull("transaction", transaction);
		// this is not likely to happen because the default jdbc implementation always
		// gives back a new transaction rather then null.
		if (!transaction.isActive())
			log.warn("Transaction not active!, nothing to rollback. Transaction: " + transaction);
		else
			transaction.rollback();

		// Clear the current cache to ensure that no roll backed data will be returned
		// from the cache,
		// or written to the database. KOL
		session.clear();
		session.beginTransaction();
	}

	/**
	 * Commits the current transaction and opens another one. This is helpfull for systems
	 * that automaticly open and commit/rollback a transaction.
	 * 
	 * @param transaction
	 * @param session
	 * @throws IllegalArgumentException
	 *             if transction is null.
	 * @throws HibernateException
	 *             if there is a problem with the transaction
	 */
	public static final void commitAndOpenAnotherTransaction(Transaction transaction,
			Session session)
	{
		Asserts.assertNotNull("transaction", transaction);
		Asserts.assertNotNull("session", session);
		if (!transaction.isActive())
			log.warn("Transaction not active!, nothing to commit. Transaction: " + transaction);
		else
			transaction.commit();
		session.beginTransaction();
	}

}
