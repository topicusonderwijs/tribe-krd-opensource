/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.participatie.settings;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.settings.OrganisatieEenheidSetting;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Houd per organisatie eenheid bij of absentie dan wel presentie wordt bijgehouden in
 * noise. Dit is puur een flag om de beste strategie te bepalen bij het weergeven hiervan.
 * 
 * @author marrink
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class AbsentiePresentieSetting extends OrganisatieEenheidSetting<AbsentiePresentie>
{
	private static final long serialVersionUID = 1L;

	@Column(name = "absentiePresentie", nullable = true)
	@Basic(optional = false)
	private AbsentiePresentie value;

	/**
	 * Hibernate constructor.
	 */
	public AbsentiePresentieSetting()
	{
	}

	public AbsentiePresentieSetting(OrganisatieEenheid eenheid)
	{
		super(eenheid);
		setValue(AbsentiePresentie.Geen);
	}

	@Override
	public AbsentiePresentie getValue()
	{
		return value;
	}

	@Override
	public void setValue(AbsentiePresentie value)
	{
		this.value = value;

	}

	@Override
	public final String getOmschrijving()
	{
		return "Bijhouden van absentie / presentie";
	}

}
