/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Bijlage bij een afspraak. Bevat een document in de vorm van een Lob.
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class AfspraakBijlage extends BijlageEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "afspraak", nullable = true)
	@Index(name = "idx_ABijlage_afspraak")
	private Afspraak afspraak;

	public AfspraakBijlage()
	{
	}

	public Afspraak getAfspraak()
	{
		return afspraak;
	}

	public void setAfspraak(Afspraak afspraak)
	{
		this.afspraak = afspraak;
	}

	@Override
	public IBijlageKoppelEntiteit<AfspraakBijlage> getEntiteit()
	{
		return getAfspraak();
	}
}
