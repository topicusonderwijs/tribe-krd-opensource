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
 * Setting om per organisatie de controle van de resultaatstructuur in te kunnen stellen
 * 
 * @author papegaaij
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ResultaatControleSetting extends OrganisatieSetting<ResultaatControleConfiguration>
{
	private static final long serialVersionUID = 1L;

	@Embedded
	@AutoFormEmbedded
	private ResultaatControleConfiguration value;

	public ResultaatControleSetting()
	{
		value = new ResultaatControleConfiguration();
	}

	@Override
	public String getOmschrijving()
	{
		return "Resultaatstructuur controleren";
	}

	@Override
	public ResultaatControleConfiguration getValue()
	{
		return value;
	}

	@Override
	public void setValue(ResultaatControleConfiguration value)
	{
		this.value = value;
	}

}
