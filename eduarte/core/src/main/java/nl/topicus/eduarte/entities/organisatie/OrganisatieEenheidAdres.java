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

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen organisatieeenheid en adres.
 * 
 * @author loite
 */
@Entity()
@Exportable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class OrganisatieEenheidAdres extends AdresEntiteit<OrganisatieEenheidAdres>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = true)
	@Basic(optional = false)
	@Index(name = "idx_OEAdres_orgEhd")
	private OrganisatieEenheid organisatieEenheid;

	/**
	 * Gebruik newAdres() van bijbehorende Adresseerbaar entiteit.
	 */
	public OrganisatieEenheidAdres()
	{
	}

	/**
	 * @return Returns the persoon.
	 */
	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	/**
	 * @param organisatieEenheid
	 *            The organisatieEenheid to set.
	 */
	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	@Override
	public OrganisatieEenheid getEntiteit()
	{
		return getOrganisatieEenheid();
	}

	@Override
	public void setEntiteit(Adresseerbaar<OrganisatieEenheidAdres> entiteit)
	{
		setOrganisatieEenheid((OrganisatieEenheid) entiteit);
	}
}
