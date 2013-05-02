/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.personen;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.providers.MedewerkerProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Koppeltabel tussen organisatie-eenheid, locatie en medewerker. Wordt gebruikt om te
 * bepalen bij welke organisatie-eenheden en locaties een medewerker (en dus account)
 * hoort.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(appliesTo = "OrganisatieMedewerker", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@javax.persistence.Table(name = "OrganisatieMedewerker", uniqueConstraints = @UniqueConstraint(columnNames = {
	"organisatieEenheid", "locatie", "medewerker"}))
@IsViewWhenOnNoise
public class OrganisatieMedewerker extends BeginEinddatumInstellingEntiteit implements
		IOrganisatieEenheidLocatieKoppelEntiteit<OrganisatieMedewerker>, MedewerkerProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_OrgMedewerker_orgEhd")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_OrgMedewerker_locatie")
	private Locatie locatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker")
	@Index(name = "idx_OrgMedewerker_medewerker")
	private Medewerker medewerker;

	public OrganisatieMedewerker()
	{
	}

	public OrganisatieMedewerker(Medewerker medewerker)
	{
		setMedewerker(medewerker);
	}

	public OrganisatieMedewerker(OrganisatieEenheid organisatieEenheid, Locatie locatie)
	{
		setOrganisatieEenheid(organisatieEenheid);
		setLocatie(locatie);
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;

	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public Locatie getLocatie()
	{
		return locatie;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	@Override
	public Medewerker getEntiteit()
	{
		return getMedewerker();
	}
}
