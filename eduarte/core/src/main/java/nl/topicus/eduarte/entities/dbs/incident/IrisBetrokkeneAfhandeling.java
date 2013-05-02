/*
 * Copyright (c) 2010, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.incident;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.onderwijs.incidentkoppeling.model.IrisAfhandeling;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen IrisBetrokkene en IrisAfhandeling
 * 
 * @author niesink
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class IrisBetrokkeneAfhandeling extends Entiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "betrokkene")
	@Index(name = "idx_IrisBA_betrokkene")
	private IrisBetrokkene betrokkene;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private IrisAfhandeling afhandeling;

	public void setBetrokkene(IrisBetrokkene betrokkene)
	{
		this.betrokkene = betrokkene;
	}

	public IrisBetrokkene getBetrokkene()
	{
		return betrokkene;
	}

	public void setAfhandeling(IrisAfhandeling afhandeling)
	{
		this.afhandeling = afhandeling;
	}

	public IrisAfhandeling getAfhandeling()
	{
		return afhandeling;
	}

	@Override
	public boolean isLandelijk()
	{
		return false;
	}
}
