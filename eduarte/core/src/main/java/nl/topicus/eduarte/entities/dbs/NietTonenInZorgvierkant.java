/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Setting die per medewerker aangeeft dat een begeleidingsobject
 * (notitie/incident/bijzonderheid/test) niet getoond moet worden in het zorgvierkant.
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorColumn(length = 255)
public abstract class NietTonenInZorgvierkant extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = false)
	@Index(name = "idx_TonenInZorgv_medewerker")
	private Medewerker medewerker;

	public NietTonenInZorgvierkant()
	{
	}

	public NietTonenInZorgvierkant(Medewerker medewerker)
	{
		setMedewerker(medewerker);
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}
}
