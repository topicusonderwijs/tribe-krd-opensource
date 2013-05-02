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
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class MedewerkerVrijVeld extends VrijVeldEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = true)
	@Index(name = "idx_MVV_medewerker")
	private Medewerker medewerker;

	public MedewerkerVrijVeld()
	{
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	@Override
	public Medewerker getEntiteit()
	{
		return getMedewerker();
	}

	@Override
	public void setEntiteit(VrijVeldable< ? extends VrijVeldEntiteit> entiteit)
	{
		setMedewerker((Medewerker) entiteit);
	}
}
