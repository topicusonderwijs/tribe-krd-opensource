/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.entities.IOrganisatie;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.entities.NaamEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author loite
 */
@Exportable
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public abstract class Organisatie extends NaamEntiteit implements IOrganisatie, IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private boolean actief;

	public Organisatie()
	{
	}

	public Organisatie(String naam)
	{
		super(naam);
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	/**
	 * De rechtensoort waarmee deze organisatie overeenkomt.
	 * 
	 * @return de rechtensoort
	 */
	public abstract RechtenSoort getRechtenSoort();
}
