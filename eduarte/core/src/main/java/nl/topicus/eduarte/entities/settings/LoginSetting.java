/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.settings;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import nl.topicus.cobra.web.components.form.AutoFormEmbedded;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Houd per organisatie bij of en hoevaak een gebruiker foutief kan inloggen voordat zijn
 * account geblokkerd word.
 * 
 * @author marrink
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class LoginSetting extends OrganisatieSetting<LoginConfiguration>
{
	private static final long serialVersionUID = 1L;

	@Embedded
	@AutoFormEmbedded
	private LoginConfiguration value;

	public LoginSetting()
	{
		value = new LoginConfiguration();
	}

	@Override
	public String getOmschrijving()
	{
		return "Maximum aantal keren foutief inloggen";
	}

	@Override
	public LoginConfiguration getValue()
	{
		return value;
	}

	@Override
	public void setValue(LoginConfiguration value)
	{
		this.value = value;
	}
}
