/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.inschrijving;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author idserda
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
@Exportable
public class UitkomstIntakegesprek extends CodeNaamActiefInstellingEntiteit implements
		Comparable<UitkomstIntakegesprek>
{
	@Column(nullable = false)
	@AutoForm(include = true)
	private boolean succesvol = false;

	private static final long serialVersionUID = 1L;

	public UitkomstIntakegesprek()
	{
	}

	public void setSuccesvol(boolean succesvol)
	{
		this.succesvol = succesvol;
	}

	@Exportable
	public boolean isSuccesvol()
	{
		return succesvol;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	@Override
	public int compareTo(UitkomstIntakegesprek o)
	{
		return getCode().compareTo(o.getCode());
	}
}