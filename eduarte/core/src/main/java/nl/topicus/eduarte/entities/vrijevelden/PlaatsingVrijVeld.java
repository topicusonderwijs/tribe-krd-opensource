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
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class PlaatsingVrijVeld extends VrijVeldEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plaatsing", nullable = true)
	@Index(name = "idx_PLVV_plaatsing")
	private Plaatsing plaatsing;

	public PlaatsingVrijVeld()
	{
	}

	/**
	 * @return Returns the plaatsing.
	 */
	public Plaatsing getPlaatsing()
	{
		return plaatsing;
	}

	/**
	 * @param plaatsing
	 *            The plaatsing to set.
	 */
	public void setPlaatsing(Plaatsing plaatsing)
	{
		this.plaatsing = plaatsing;
	}

	@Override
	public VrijVeldable< ? extends VrijVeldEntiteit> getEntiteit()
	{
		return getPlaatsing();
	}

	@Override
	public void setEntiteit(VrijVeldable< ? extends VrijVeldEntiteit> entiteit)
	{
		setPlaatsing((Plaatsing) entiteit);
	}
}
