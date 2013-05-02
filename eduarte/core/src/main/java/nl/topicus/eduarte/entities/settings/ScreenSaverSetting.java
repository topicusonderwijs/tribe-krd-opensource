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
 * Setting om per organisatie de screensaver in te stellen
 * 
 * @author papegaaij
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ScreenSaverSetting extends OrganisatieSetting<ScreenSaverConfiguration>
{
	private static final long serialVersionUID = 1L;

	@Embedded
	@AutoFormEmbedded
	private ScreenSaverConfiguration value;

	public ScreenSaverSetting()
	{
		value = new ScreenSaverConfiguration();
	}

	@Override
	public String getOmschrijving()
	{
		return "Schermbeveiliging";
	}

	@Override
	public ScreenSaverConfiguration getValue()
	{
		return value;
	}

	@Override
	public void setValue(ScreenSaverConfiguration value)
	{
		this.value = value;
	}

}
