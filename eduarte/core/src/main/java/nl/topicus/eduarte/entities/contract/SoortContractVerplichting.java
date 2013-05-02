/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.contract;

import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
public class SoortContractVerplichting extends CodeNaamActiefInstellingEntiteit implements
		Comparable<SoortContractVerplichting>
{
	private static final long serialVersionUID = 1L;

	public SoortContractVerplichting()
	{
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	@Override
	public int compareTo(SoortContractVerplichting o)
	{
		return getCode().compareTo(o.getCode());
	}
}
