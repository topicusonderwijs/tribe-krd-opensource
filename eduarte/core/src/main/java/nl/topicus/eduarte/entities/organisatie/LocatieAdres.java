/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen locatie en adres.
 * 
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class LocatieAdres extends AdresEntiteit<LocatieAdres>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Basic(optional = false)
	@Index(name = "idx_LocatieAdres_locatie")
	private Locatie locatie;

	/**
	 * Gebruik newAdres() van bijbehorende Adresseerbaar entiteit.
	 */
	public LocatieAdres()
	{
	}

	/**
	 * @return Returns the persoon.
	 */
	public Locatie getLocatie()
	{
		return locatie;
	}

	/**
	 * @param locatie
	 *            The locatie to set.
	 */
	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	@Override
	public Locatie getEntiteit()
	{
		return getLocatie();
	}

	@Override
	public void setEntiteit(Adresseerbaar<LocatieAdres> entiteit)
	{
		setLocatie((Locatie) entiteit);
	}
}
