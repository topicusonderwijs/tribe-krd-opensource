/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.vrijevelden;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.personen.Persoon;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class PersoonVrijVeld extends VrijVeldEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * Lazy omdat lijst met telefoonnummers vanuit de persoon wordt opgevraagd, oftewel de
	 * persoon zal in het algemeen al aanwezig zijn in de 1st of 2nd level cache.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "persoon", nullable = true)
	@Index(name = "idx_PVV_persoon")
	private Persoon persoon;

	public PersoonVrijVeld()
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

	@Override
	public VrijVeldable< ? extends VrijVeldEntiteit> getEntiteit()
	{
		return getPersoon();
	}

	@Override
	public void setEntiteit(VrijVeldable< ? extends VrijVeldEntiteit> entiteit)
	{
		setPersoon((Persoon) entiteit);
	}
}
