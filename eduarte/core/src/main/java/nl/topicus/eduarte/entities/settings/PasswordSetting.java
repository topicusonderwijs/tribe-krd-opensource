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
 * Houd per organisatie bij wat de wachtwoordsterkte is. Dus of er een bepaalde lengte
 * verplicht is, leestekens, hoofd en kleineletters etc.
 * 
 * @author marrink
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class PasswordSetting extends OrganisatieSetting<PasswordConfiguration>
{

	private static final long serialVersionUID = 1L;

	@Embedded
	@AutoFormEmbedded
	private PasswordConfiguration value;

	public PasswordSetting()
	{
		value = new PasswordConfiguration();
	}

	@Override
	public String getOmschrijving()
	{
		return "Extra wachtwoord eisen";
	}

	@Override
	public PasswordConfiguration getValue()
	{
		return value;
	}

	@Override
	public void setValue(PasswordConfiguration value)
	{
		this.value = value;
	}
}
