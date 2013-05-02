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

import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen ExterneOrganisatie en Adres.
 * 
 * @author idserda
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ExterneOrganisatieAdres extends AdresEntiteit<ExterneOrganisatieAdres>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externeOrganisatie", nullable = true)
	@Basic(optional = false)
	@Index(name = "idx_ExterneOrgan_externeOrgan")
	private ExterneOrganisatie externeOrganisatie;

	/**
	 * Gebruik newAdres() van bijbehorende Adresseerbaar entiteit.
	 */
	public ExterneOrganisatieAdres()
	{
	}

	/**
	 * @param externeOrganisatie
	 *            The externeOrganisatie to set.
	 */
	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	/**
	 * @return Returns the externeOrganisatie.
	 */
	public ExterneOrganisatie getExterneOrganisatie()
	{
		return externeOrganisatie;
	}

	@Override
	public ExterneOrganisatie getEntiteit()
	{
		return getExterneOrganisatie();
	}

	@Override
	public void setEntiteit(Adresseerbaar<ExterneOrganisatieAdres> entiteit)
	{
		setExterneOrganisatie((ExterneOrganisatie) entiteit);
	}
}