/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;

/**
 * Entiteit met een naam. B.V. een organisatie of een groep. Het is niet de bedoeling dat
 * deze class misbruikt wordt voor b.v. leerlingen die een vornaam dan wel achternaam op
 * dit veld mappen.
 * 
 * @author loite
 */
@MappedSuperclass()
public abstract class NaamEntiteit extends LandelijkEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 100)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	public NaamEntiteit()
	{
	}

	public NaamEntiteit(String naam)
	{
		setNaam(naam);
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}
}
