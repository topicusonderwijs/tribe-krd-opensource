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
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class IntakegesprekVrijVeld extends VrijVeldEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "intakegesprek", nullable = true)
	@Index(name = "idx_VVV_intakegesprek")
	private Intakegesprek intakegesprek;

	public IntakegesprekVrijVeld()
	{
	}

	public Intakegesprek getIntakegesprek()
	{
		return intakegesprek;
	}

	public void setIntakegesprek(Intakegesprek intakegesprek)
	{
		this.intakegesprek = intakegesprek;
	}

	@Override
	public VrijVeldable< ? extends VrijVeldEntiteit> getEntiteit()
	{
		return getIntakegesprek();
	}

	@Override
	public void setEntiteit(VrijVeldable< ? extends VrijVeldEntiteit> entiteit)
	{
		setIntakegesprek((Intakegesprek) entiteit);
	}
}
