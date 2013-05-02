/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.personen;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(appliesTo = "ContactPersoon", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
public class ContactPersoon extends BeginEinddatumInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "voorOrganisatieEenheid", nullable = true)
	@Index(name = "idx_ContactPerso_bijOrganisat")
	private OrganisatieEenheid voorOrganisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "persoon", nullable = false)
	@Index(name = "idx_ContactPersoon_persoon")
	private Persoon persoon;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bijOrganisatieEenheid", nullable = true)
	@Index(name = "idx_ContactPerso_voorOrganisa")
	private OrganisatieEenheid bijOrganisatieEenheid;

	/**
	 * Default constructor voor Hibernate.
	 */
	public ContactPersoon()
	{
	}

	/**
	 * @return Returns the voorOrganisatieEenheid.
	 */
	public OrganisatieEenheid getVoorOrganisatieEenheid()
	{
		return voorOrganisatieEenheid;
	}

	/**
	 * @param voorOrganisatieEenheid
	 *            The voorOrganisatieEenheid to set.
	 */
	public void setVoorOrganisatieEenheid(OrganisatieEenheid voorOrganisatieEenheid)
	{
		this.voorOrganisatieEenheid = voorOrganisatieEenheid;
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

	/**
	 * @return Returns the bijOrganisatieEenheid.
	 */
	public OrganisatieEenheid getBijOrganisatieEenheid()
	{
		return bijOrganisatieEenheid;
	}

	/**
	 * @param bijOrganisatieEenheid
	 *            The bijOrganisatieEenheid to set.
	 */
	public void setBijOrganisatieEenheid(OrganisatieEenheid bijOrganisatieEenheid)
	{
		this.bijOrganisatieEenheid = bijOrganisatieEenheid;
	}

}
