/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.resultaatstructuur;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen organisatie-eenheid, locatie en ToetsCodeFilter. Wordt gebruikt om
 * te bepalen bij welke organisatie-eenheden en locaties een ToetsCodeFilter hoort.
 * 
 * @author papegaaij
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(name = "ToetsCodeFilterOrgEhdLoc")
public class ToetsCodeFilterOrganisatieEenheidLocatie extends InstellingEntiteit implements
		IOrganisatieEenheidLocatieKoppelEntiteit<ToetsCodeFilterOrganisatieEenheidLocatie>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_TCFOEL_orgEhd")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = false)
	@Index(name = "idx_TCFOEL_locatie")
	private Locatie locatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "toetsCodeFilter")
	@Index(name = "idx_TCFOEL_toetsCodeFilter")
	private ToetsCodeFilter toetsCodeFilter;

	public ToetsCodeFilterOrganisatieEenheidLocatie()
	{
	}

	public ToetsCodeFilterOrganisatieEenheidLocatie(OrganisatieEenheid organisatieEenheid,
			Locatie locatie)
	{
		setOrganisatieEenheid(organisatieEenheid);
		setLocatie(locatie);
	}

	public ToetsCodeFilterOrganisatieEenheidLocatie(ToetsCodeFilter filter)
	{
		setToetsCodeFilter(filter);
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

	public void setToetsCodeFilter(ToetsCodeFilter toetsCodeFilter)
	{
		this.toetsCodeFilter = toetsCodeFilter;
	}

	public ToetsCodeFilter getToetsCodeFilter()
	{
		return toetsCodeFilter;
	}

	@Override
	public ToetsCodeFilter getEntiteit()
	{
		return getToetsCodeFilter();
	}

	@Override
	public boolean isActief(Date peildatum)
	{
		return true;
	}
}
