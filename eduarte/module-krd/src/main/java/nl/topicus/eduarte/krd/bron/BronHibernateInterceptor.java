package nl.topicus.eduarte.krd.bron;

import java.io.Serializable;
import java.util.Iterator;

import nl.topicus.cobra.hibernate.SessionAware;
import nl.topicus.cobra.util.ExceptionUtil;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;
import nl.topicus.onderwijs.duo.bron.BronException;

import org.hibernate.EmptyInterceptor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate interceptor die veranderingen bijhoudt op entiteiten die gemarkeerd zijn met
 * <tt>@Bron</tt> annotaties. Deze veranderingen worden in meldingen opgeslagen in de
 * zogeheten wachtrij. Bij het aanmaken van de BRON batch worden de overige gegevens
 * aangevuld.
 */
public class BronHibernateInterceptor extends EmptyInterceptor implements SessionAware
{
	private static final Logger log = LoggerFactory.getLogger(BronHibernateInterceptor.class);

	private static final long serialVersionUID = 1L;

	private Session currentSession;

	private BronController controller = null;

	private boolean inPostFlush = false;

	public BronHibernateInterceptor()
	{
		log.debug("Created BronHibernateInterceptor");
	}

	@Override
	public void setSession(Session session)
	{
		this.currentSession = session;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
			Object[] previousState, String[] propertyNames, Type[] types)
	{
		if (entity instanceof InstellingEntiteit || entity instanceof LandelijkOfInstellingEntiteit)
		{
			try
			{
				getController().controleerOpWijzigingenOpBronVelden((Entiteit) entity,
					currentState, previousState, propertyNames);
			}
			catch (BronException e)
			{
				log.error("Wijzigingen op entiteit {} zijn niet geaccordeerd: {}", entity
					.getClass().getSimpleName(), e.toString());
				throw new HibernateException(e);
			}
			catch (HibernateException e)
			{
				throw e;
			}
		}
		return false;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames,
			Type[] types)
	{
		Object[] oldState = new Object[state.length];
		return onFlushDirty(entity, id, state, oldState, propertyNames, types);
	}

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames,
			Type[] types)
	{
		Object[] newState = new Object[state.length];
		onFlushDirty(entity, id, newState, state, propertyNames, types);
	}

	@SuppressWarnings( {"unchecked"})
	@Override
	public void postFlush(Iterator entities)
	{
		if (getController().hasChanges() && !inPostFlush)
		{
			try
			{
				inPostFlush = true;
				controller.save();
				controller = null;

				// omdat we wijzigingen aangebracht hebben (nieuwe entiteiten toegevoegd),
				// moet er opnieuw geflusht worden.
				currentSession.flush();
			}
			catch (BronException e)
			{
				// wrap de bron exception in een hibernate exception, hiermee laten we
				// hibernate weten dat de transactie teruggedraaid moet worden.
				throw new HibernateException(e);
			}
			catch (HibernateException e)
			{
				String message = ExceptionUtil.getRootCause(e).getMessage();
				throw new HibernateException(new BronException(
					"Kon geen BRON melding aanmaken, en daardoor ook de wijzigingen niet doorvoeren: "
						+ message, e));
			}
			finally
			{
				// reset de controller, zodat de wijzigingen/nieuwe meldingen niet *nog*
				// een keer aangemaakt worden.
				controller = null;
				inPostFlush = false;
			}
		}
	}

	@Override
	public void beforeTransactionCompletion(Transaction tx)
	{
		// Hier flushen is te laat, aangezien Hibernate dan elke exceptie die optreedt
		// afvangt en inslikt, zonder de buitenwacht (of zichzelf) er van te notificeren.

		// als de transactie wordt teruggedraaid, de controller leegmaken, aangezien de
		// wijzigingen niet doorgevoerd worden.
		if (tx != null && tx.wasRolledBack())
		{
			controller = null;
		}
	}

	@Override
	public void afterTransactionCompletion(Transaction tx)
	{
		// als de transactie wordt teruggedraaid, de controller leegmaken, aangezien de
		// wijzigingen niet doorgevoerd worden.
		if (tx != null && tx.wasRolledBack())
		{
			controller = null;
		}
	}

	private BronController getController()
	{
		if (controller == null)
		{
			controller = new BronController();
		}
		return controller;
	}
}
