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
import nl.topicus.eduarte.entities.opleiding.Opleiding;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class OpleidingVrijVeld extends VrijVeldEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opleiding", nullable = true)
	@Index(name = "idx_VVV_opleiding")
	private Opleiding opleiding;

	public OpleidingVrijVeld()
	{
	}

	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
	}

	@Override
	public VrijVeldable< ? extends VrijVeldEntiteit> getEntiteit()
	{
		return getOpleiding();
	}

	@Override
	public void setEntiteit(VrijVeldable< ? extends VrijVeldEntiteit> entiteit)
	{
		setOpleiding((Opleiding) entiteit);
	}
}
