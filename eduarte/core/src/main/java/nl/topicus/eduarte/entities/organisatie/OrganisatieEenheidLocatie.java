/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Een organisatie-eenheid-locatie.
 * 
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
@BatchSize(size = 100)
@Table(appliesTo = "OrganisatieEenheidLocatie", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@IsViewWhenOnNoise
public class OrganisatieEenheidLocatie extends BeginEinddatumInstellingEntiteit implements
		Comparable<OrganisatieEenheidLocatie>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_OrgEhdL_organisatieEenheid")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = false)
	@Index(name = "idx_OrgEhdL_locatie")
	@AutoForm(htmlClasses = "unit_max")
	private Locatie locatie;

	public OrganisatieEenheidLocatie()
	{
	}

	public OrganisatieEenheidLocatie(OrganisatieEenheid eenheid)
	{
		setOrganisatieEenheid(eenheid);
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
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
	public int compareTo(OrganisatieEenheidLocatie o)
	{
		int result = getOrganisatieEenheid().getId().compareTo(o.getOrganisatieEenheid().getId());
		if (result == 0)
			result = getLocatie().getId().compareTo(o.getLocatie().getId());

		return result;
	}
}
