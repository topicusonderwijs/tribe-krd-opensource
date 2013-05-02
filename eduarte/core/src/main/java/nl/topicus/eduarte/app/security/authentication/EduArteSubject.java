/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.authentication;

import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authorization.Recht;
import nl.topicus.eduarte.entities.security.authorization.Rol;

import org.apache.wicket.security.hive.authentication.DefaultSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Subject, copies the principals from the accounts so changes to the account can happen
 * without causing problems in the current session.
 * 
 * @author marrink
 */
public class EduArteSubject extends DefaultSubject
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(EduArteSubject.class);

	/**
	 * Maakt nieuw subject aan met dezelfde rechten als de account.
	 * 
	 * @param account
	 */
	public EduArteSubject(Account account)
	{
		super();
		for (Rol rol : account.getRollenAsRol())
		{
			for (Recht recht : rol.getRechten())
			{
				try
				{
					addPrincipal(recht.getPrincipal());
				}
				catch (ClassNotFoundException e)
				{
					log.info("Ignoring deleted principal: " + e.getMessage());
				}
			}
		}
		setReadOnly();
	}

}
