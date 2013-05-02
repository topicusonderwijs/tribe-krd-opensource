/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.gedrag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefLandelijkOfInstellingEntiteit;
import nl.topicus.onderwijs.incidentkoppeling.model.IrisVoorval;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
public class IncidentCategorie extends CodeNaamActiefLandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private IrisVoorval irisVoorval;

	public IncidentCategorie()
	{
	}

	@Override
	public String toString()
	{
		return getCode() + " - " + getNaam();
	}

	public void setIrisVoorval(IrisVoorval irisVoorval)
	{
		this.irisVoorval = irisVoorval;
	}

	public IrisVoorval getIrisVoorval()
	{
		return irisVoorval;
	}
}
