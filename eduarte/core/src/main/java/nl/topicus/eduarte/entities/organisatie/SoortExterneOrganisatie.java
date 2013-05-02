/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefLandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
@IsViewWhenOnNoise
public class SoortExterneOrganisatie extends CodeNaamActiefLandelijkOfInstellingEntiteit implements
		Comparable<SoortExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private boolean brin;

	@Column(nullable = false)
	private boolean tonenBijVooropleiding;

	public SoortExterneOrganisatie()
	{
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	@Override
	public int compareTo(SoortExterneOrganisatie o)
	{
		return getCode().compareTo(o.getCode());
	}

	public boolean isBrin()
	{
		return brin;
	}

	public void setBrin(boolean brin)
	{
		this.brin = brin;
	}

	public boolean isTonenBijVooropleiding()
	{
		return tonenBijVooropleiding;
	}

	public void setTonenBijVooropleiding(boolean tonenBijVooropleiding)
	{
		this.tonenBijVooropleiding = tonenBijVooropleiding;
	}
}
