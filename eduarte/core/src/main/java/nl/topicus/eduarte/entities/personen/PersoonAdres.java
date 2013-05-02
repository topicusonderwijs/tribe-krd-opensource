/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.personen;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen persoon en adres.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class PersoonAdres extends AdresEntiteit<PersoonAdres>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Lazy omdat lijst met adressen vanuit de persoon wordt opgevraagd, oftewel de
	 * persoon zal in het algemeen al aanwezig zijn in de 1st of 2nd level cache.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "persoon", nullable = true)
	@Basic(optional = false)
	@Index(name = "idx_PAdres_persoon")
	private Persoon persoon;

	/**
	 * Gebruik newAdres() van bijbehorende Adresseerbaar entiteit.
	 */
	public PersoonAdres()
	{
	}

	/**
	 * @return Returns the persoon.
	 */
	public Persoon getPersoon()
	{
		return persoon;
	}

	/**
	 * @param persoon
	 *            The persoon to set.
	 */
	public void setPersoon(Persoon persoon)
	{
		this.persoon = persoon;
	}

	/**
	 * @param adres
	 *            The adres to set.
	 */
	@Override
	public void setAdres(Adres adres)
	{
		if (getAdres() != null && getAdres() != adres)
		{
			getAdres().getPersoonAdressen().remove(this);
		}
		super.setAdres(adres);
		if (adres != null && !adres.getPersoonAdressen().contains(this))
		{
			adres.getPersoonAdressen().add(this);
		}
	}

	@Override
	public Persoon getEntiteit()
	{
		return getPersoon();
	}

	@Override
	public void setEntiteit(Adresseerbaar<PersoonAdres> entiteit)
	{
		setPersoon((Persoon) entiteit);
	}
}
