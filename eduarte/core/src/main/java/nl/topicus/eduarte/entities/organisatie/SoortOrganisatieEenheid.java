/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(name = "SoortOrgEhd", uniqueConstraints = {@UniqueConstraint(columnNames = {
	"code", "organisatie"})})
@IsViewWhenOnNoise
public class SoortOrganisatieEenheid extends CodeNaamActiefInstellingEntiteit implements
		Comparable<SoortOrganisatieEenheid>
{
	private static final long serialVersionUID = 1L;

	public SoortOrganisatieEenheid()
	{
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	@Override
	public int compareTo(SoortOrganisatieEenheid o)
	{
		return getCode().compareTo(o.getCode());
	}
}
