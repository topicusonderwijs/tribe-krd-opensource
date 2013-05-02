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
 * Mutatie Blokkadedatum
 * 
 * Met de blokkadedatum is het mogelijk om mutatie's aan verbintenissen van voor een
 * bepaalde datum te blokkkeren. Hierbij wordt gekeken naar de begindatum van de
 * verbintenis. Als deze voor de blokkadedatum ligt is er geen enkele wijziging aan de
 * verbintenis mogelijk.
 * 
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class MutatieBlokkedatumSetting extends OrganisatieSetting<MutatieBlokkedatumConfiguration>
{
	private static final long serialVersionUID = 1L;

	@Embedded
	@AutoFormEmbedded
	private MutatieBlokkedatumConfiguration value;

	public MutatieBlokkedatumSetting()
	{
		value = new MutatieBlokkedatumConfiguration();
	}

	@Override
	public String getOmschrijving()
	{
		return "Mutatie blokkedatum";
	}

	@Override
	public MutatieBlokkedatumConfiguration getValue()
	{
		return value;
	}

	@Override
	public void setValue(MutatieBlokkedatumConfiguration value)
	{
		this.value = value;
	}
}
