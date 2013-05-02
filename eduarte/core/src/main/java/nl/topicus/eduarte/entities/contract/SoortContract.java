/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.contract;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.web.components.form.AutoForm;
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
public class SoortContract extends CodeNaamActiefInstellingEntiteit implements
		Comparable<SoortContract>
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@AutoForm(description = "Geeft aan of contracten van deze soort inburgeringscontracten zijn. Inburgeringscontracten hebben extra velden t.b.v. het Kermerk Inburgeren.")
	private Boolean inburgering = false;

	public SoortContract()
	{
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	@Override
	public int compareTo(SoortContract o)
	{
		return getCode().compareTo(o.getCode());
	}

	public void setInburgering(Boolean inburgering)
	{
		this.inburgering = inburgering;
	}

	public Boolean getInburgering()
	{
		return inburgering;
	}
}
