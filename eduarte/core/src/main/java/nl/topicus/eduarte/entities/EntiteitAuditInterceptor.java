package nl.topicus.eduarte.entities;

import java.io.Serializable;
import java.util.Date;

import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.security.authentication.Account;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate interceptor that updates audit information on who created or modified
 * {@link Entiteit}en.
 */
public class EntiteitAuditInterceptor extends EmptyInterceptor
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(EntiteitAuditInterceptor.class);

	public EntiteitAuditInterceptor()
	{
		log.debug("Created EntiteitAuditInterceptor");
	}

	/**
	 * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object,
	 *      java.io.Serializable, java.lang.Object[], java.lang.Object[],
	 *      java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
			Object[] previousState, String[] propertyNames, Type[] types)
	{
		boolean modified = false;
		Date now = new Date();
		Account currentUser = getCurrentUser();
		if (entity instanceof Entiteit)
		{
			for (int i = 0; i < propertyNames.length; i++)
			{
				// conversion of old data: when createdAt and createdBy are null,
				// fill it with the current user
				if ("createdAt".contains(propertyNames[i]) && currentState[i] == null)
				{
					currentState[i] = now;
					modified = true;
				}
				if ("createdBy".contains(propertyNames[i]) && currentState[i] == null)
				{
					currentState[i] = currentUser;
					modified = true;
				}
				if ("lastModifiedAt".equals(propertyNames[i]))
				{
					currentState[i] = now;
					modified = true;
				}
				if ("lastModifiedBy".equals(propertyNames[i]))
				{
					currentState[i] = currentUser;
					modified = true;
				}
			}
		}

		return modified;
	}

	/**
	 * @see org.hibernate.EmptyInterceptor#onSave(java.lang.Object, java.io.Serializable,
	 *      java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames,
			Type[] types)
	{
		boolean modified = false;
		Date now = new Date();
		if (entity instanceof Entiteit)
		{
			for (int i = 0; i < propertyNames.length; i++)
			{
				if ("createdAt|lastModifiedAt".contains(propertyNames[i]))
				{
					state[i] = now;
					modified = true;
				}
				if ("createdBy|lastModifiedBy".contains(propertyNames[i]))
				{
					state[i] = getCurrentUser();
					modified = true;
				}
			}
		}

		return modified;
	}

	/**
	 * Bepaalt de gebruiker die de entiteit aangemaakt of gewijzigd heeft.
	 * 
	 * @return de gebruiker of <code>null</code> als deze niet bepaald kon worden.
	 */
	private Account getCurrentUser()
	{
		return EduArteContext.get().getAccount();
	}
}
