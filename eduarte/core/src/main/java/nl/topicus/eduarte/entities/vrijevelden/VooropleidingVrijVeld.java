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
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class VooropleidingVrijVeld extends VrijVeldEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vooropleiding", nullable = true)
	@Index(name = "idx_VVV_vooropleiding")
	private Vooropleiding vooropleiding;

	public VooropleidingVrijVeld()
	{
	}

	public Vooropleiding getVooropleiding()
	{
		return vooropleiding;
	}

	public void setVooropleiding(Vooropleiding vooropleiding)
	{
		this.vooropleiding = vooropleiding;
	}

	@Override
	public VrijVeldable< ? extends VrijVeldEntiteit> getEntiteit()
	{
		return getVooropleiding();
	}

	@Override
	public void setEntiteit(VrijVeldable< ? extends VrijVeldEntiteit> entiteit)
	{
		setVooropleiding((Vooropleiding) entiteit);
	}
}
