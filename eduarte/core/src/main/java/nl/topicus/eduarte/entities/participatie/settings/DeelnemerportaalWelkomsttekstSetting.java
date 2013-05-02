/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.participatie.settings;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.settings.OrganisatieSetting;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Houd per organisatie bij wat de termijn is waarop deelnemers nog absentiemeldingen
 * kunnen aanmaken, nadat de afsrpaak geweest is
 * 
 * @author marrink, ambrosius
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DeelnemerportaalWelkomsttekstSetting extends OrganisatieSetting<String>
{
	private static final long serialVersionUID = 1L;

	@Column(name = "stringValue", nullable = true)
	private String value;

	/**
	 * Hibernate constructor
	 */
	public DeelnemerportaalWelkomsttekstSetting()
	{
		setValue("");
	}

	@Override
	public String getOmschrijving()
	{
		return "Welkomsttekstsetting";
	}

	@Override
	public String getValue()
	{
		return value;
	}

	@Override
	public void setValue(String value)
	{
		this.value = value;
	}

}
