/*
 * Copyright (c) 2010, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.settings;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import nl.topicus.cobra.web.components.form.AutoFormEmbedded;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Setting om per organisatie financieel-gerelateerde zaken in te stellen.
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DebiteurNummerSetting extends OrganisatieSetting<DebiteurNummerConfiguration>
{
	private static final long serialVersionUID = 1L;

	@Embedded
	@AutoFormEmbedded
	private DebiteurNummerConfiguration value;

	public DebiteurNummerSetting()
	{
		value = new DebiteurNummerConfiguration();
	}

	@Override
	public String getOmschrijving()
	{
		return "Nummeringsinstellingen";
	}

	@Override
	public DebiteurNummerConfiguration getValue()
	{
		return value;
	}

	@Override
	public void setValue(DebiteurNummerConfiguration value)
	{
		this.value = value;
	}

}
