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

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.onderwijs.incidentkoppeling.model.IrisLocatie;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen IrisIncident en IrisLocatie
 * 
 * @author niesink
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class IrisIncidentLocatie extends Entiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "incident")
	@Index(name = "idx_IrisIL_incident")
	@IgnoreInGebruik
	private IrisIncident incident;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private IrisLocatie locatie;

	public void setLocatie(IrisLocatie locatie)
	{
		this.locatie = locatie;
	}

	public IrisLocatie getLocatie()
	{
		return locatie;
	}

	public void setIncident(IrisIncident incident)
	{
		this.incident = incident;
	}

	public IrisIncident getIncident()
	{
		return incident;
	}

	@Override
	public boolean isLandelijk()
	{
		return false;
	}

}
