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
public class TypeFinanciering extends CodeNaamActiefInstellingEntiteit implements
		Comparable<TypeFinanciering>
{
	private static final long serialVersionUID = 1L;

	public TypeFinanciering()
	{
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	@Override
	public int compareTo(TypeFinanciering o)
	{
		return getCode().compareTo(o.getCode());
	}
}
