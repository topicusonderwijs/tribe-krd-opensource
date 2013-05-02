/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.personen;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * @author idserda
 */
@Exportable
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(name = "ExtOrgContactPersoon")
@Table(appliesTo = "ExtOrgContactPersoon", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@IsViewWhenOnNoise
public class ExterneOrganisatieContactPersoon extends BeginEinddatumInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externeOrganisatie", nullable = false)
	@Index(name = "idx_ExtOrgCP_externeOrgan")
	private ExterneOrganisatie externeOrganisatie;

	@Column(length = 80, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(length = 60, nullable = true)
	@AutoForm(htmlClasses = "unit_max", label = "E-mailadres")
	private String emailadres;

	@Column(length = 60, nullable = true)
	@AutoForm(htmlClasses = "unit_max")
	private String telefoon;

	@Column(length = 60, nullable = true)
	@AutoForm(htmlClasses = "unit_max")
	private String mobiel;

	@Column(nullable = false)
	@Index(name = "idx_ExtOrgCP_geslacht")
	@Enumerated(value = EnumType.STRING)
	@AutoForm(htmlClasses = "unit_100")
	private Geslacht geslacht;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rol", nullable = true)
	@Index(name = "idx_ExtOrgCP_rol")
	private ExterneOrganisatieContactPersoonRol rol;

	public ExterneOrganisatieContactPersoon()
	{
	}

	public ExterneOrganisatieContactPersoon(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
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

	public void setRol(ExterneOrganisatieContactPersoonRol rol)
	{
		this.rol = rol;
	}

	public ExterneOrganisatieContactPersoonRol getRol()
	{
		return rol;
	}

	/**
	 * @param naam
	 *            The naam to set.
	 */
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	/**
	 * @return Returns the naam.
	 */
	@Exportable
	public String getNaam()
	{
		return naam;
	}

	/**
	 * @return Returns de naam met toevoeging 'inactief' als deze niet meer actief is.
	 */
	public String getNaamInactief()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getNaam());
		stringBuilder.append(isActief() ? "" : " (inactief)");
		return stringBuilder.toString();
	}

	/**
	 * @param emailadres
	 *            The emailadres to set.
	 */
	public void setEmailadres(String emailadres)
	{
		this.emailadres = emailadres;
	}

	/**
	 * @return Returns the emailadres.
	 */
	@Exportable
	public String getEmailadres()
	{
		return emailadres;
	}

	/**
	 * @param telefoon
	 *            The telefoon to set.
	 */
	public void setTelefoon(String telefoon)
	{
		this.telefoon = telefoon;
	}

	/**
	 * @return Returns the telefoon.
	 */
	@Exportable
	public String getTelefoon()
	{
		return telefoon;
	}

	@Exportable
	public String getMobiel()
	{
		return mobiel;
	}

	public void setMobiel(String mobiel)
	{
		this.mobiel = mobiel;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	@Exportable
	public Geslacht getGeslacht()
	{
		return geslacht;
	}

	public void setGeslacht(Geslacht geslacht)
	{
		this.geslacht = geslacht;
	}
}
