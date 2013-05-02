/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.settings.OrganisatieEenheidSetting;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Setting die aangeeft welke lesweekindeling default gebruikt moet worden per
 * organisatie-eenheid, wanneer geen rooster bekend is.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DefaultLesweekIndeling extends OrganisatieEenheidSetting<LesweekIndeling>
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lesweekIndeling", nullable = true)
	@Index(name = "idx_OrganisatieS_lesweekIndel")
	private LesweekIndeling lesweekIndeling;

	/**
	 * Default constructor voor Hibernate.
	 */
	public DefaultLesweekIndeling()
	{
	}

	public DefaultLesweekIndeling(OrganisatieEenheid eenheid)
	{
		super(eenheid);
	}

	@Override
	public String getOmschrijving()
	{
		return "Default lesweekindeling per organisatie-eenheid";
	}

	@Override
	public LesweekIndeling getValue()
	{
		return getLesweekIndeling();
	}

	@Override
	public void setValue(LesweekIndeling value)
	{
		setLesweekIndeling(value);
	}

	/**
	 * @return Returns the lesweekIndeling.
	 */
	public LesweekIndeling getLesweekIndeling()
	{
		return lesweekIndeling;
	}

	/**
	 * @param lesweekIndeling
	 *            The lesweekIndeling to set.
	 */
	public void setLesweekIndeling(LesweekIndeling lesweekIndeling)
	{
		this.lesweekIndeling = lesweekIndeling;
	}

}
