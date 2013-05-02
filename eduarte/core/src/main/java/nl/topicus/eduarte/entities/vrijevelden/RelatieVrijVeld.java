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
import nl.topicus.eduarte.entities.personen.AbstractRelatie;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class RelatieVrijVeld extends VrijVeldEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "relatie", nullable = true)
	@Index(name = "idx_RVV_relatie")
	private AbstractRelatie relatie;

	public RelatieVrijVeld()
	{
	}

	@Override
	public VrijVeldable< ? extends VrijVeldEntiteit> getEntiteit()
	{
		return getRelatie();
	}

	@Override
	public void setEntiteit(VrijVeldable< ? extends VrijVeldEntiteit> entiteit)
	{
		setRelatie((AbstractRelatie) entiteit);
	}

	public void setRelatie(AbstractRelatie relatie)
	{
		this.relatie = relatie;
	}

	public AbstractRelatie getRelatie()
	{
		return relatie;
	}
}
