/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.mail;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.mail.EmailBuilder;
import nl.topicus.cobra.mail.JavaMailDao;
import nl.topicus.cobra.mail.SimpleEmailBuilder;
import nl.topicus.cobra.mail.VelocityEmailBuilder;
import nl.topicus.eduarte.dao.helpers.InstellingDataAccessHelper;

/**
 * @author marrink
 */
public class EduArteMailDao extends JavaMailDao
{
	private final boolean simpleMode;

	/**
	 * 
	 */
	public EduArteMailDao()
	{
		this(false);
	}

	/**
	 * @param simpleMode
	 */
	public EduArteMailDao(boolean simpleMode)
	{
		super("mail/EduArte");
		this.simpleMode = simpleMode;
	}

	/**
	 * @see nl.topicus.cobra.mail.EmailDao#createEmailBuilder()
	 */
	@Override
	public EmailBuilder createEmailBuilder()
	{

		EmailBuilder builder;
		if (simpleMode)
			builder = new SimpleEmailBuilder();
		else
			builder = new VelocityEmailBuilder();
		String from =
			DataAccessRegistry.getHelper(InstellingDataAccessHelper.class).getApplicationTitle();
		builder.setFrom(from, "noreply@topicus.nl");
		builder.setReplyTo("Geen antwoord", "noreply@topicus.nl");
		return builder;
	}

}
