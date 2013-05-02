/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.providers.ExterneOrganisatieProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ExterneOrganisatieOpmerking extends InstellingEntiteit implements
		ExterneOrganisatieProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externeOrganisatie", nullable = false)
	@Index(name = "idx_ExtOrgOpm_externeOrgan")
	private ExterneOrganisatie externeOrganisatie;

	/**
	 * Datum. Het is aan de gebruikers om af te spreken welke datum men hier bijhoudt:
	 * bijvoorbeeld de aanmaakdatum of de datum waarop de opmerking voor het laatst
	 * bewerkt is.
	 */
	@Column(nullable = true)
	private Date datum;

	/**
	 * De opmerking zelf (vrij tekstveld)
	 */
	@Column(nullable = false)
	@Lob
	private String opmerking;

	/**
	 * Auteur. Hier wordt automatisch de momenteel ingelogde gebruiker ingevuld, maar dit
	 * kan gewijzigd worden.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auteur", nullable = true)
	@Index(name = "idx_ExtOrgOpm_auteur")
	private Medewerker auteur;

	/**
	 * Tonen bij matching (checkbox). Wanneer deze checkbox aangevinkt wordt, wordt de
	 * betreffende opmerking getoond bij het matchen van deelnemers aan leerplaatsen van
	 * dit bedrijf. Er komt bij de leerplaats een icoontje te voorschijn, waar men met de
	 * muis boven kan hangen om de opmerking te zien.
	 */
	@Column(nullable = false)
	private boolean tonenBijMatching;

	public ExterneOrganisatieOpmerking()
	{
	}

	public ExterneOrganisatieOpmerking(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	public ExterneOrganisatie getExterneOrganisatie()
	{
		return externeOrganisatie;
	}

	public Date getDatum()
	{
		return datum;
	}

	public void setDatum(Date datum)
	{
		this.datum = datum;
	}

	public String getOpmerking()
	{
		return opmerking;
	}

	public void setOpmerking(String opmerking)
	{
		this.opmerking = opmerking;
	}

	public Medewerker getAuteur()
	{
		return auteur;
	}

	public void setAuteur(Medewerker auteur)
	{
		this.auteur = auteur;
	}

	public boolean isTonenBijMatching()
	{
		return tonenBijMatching;
	}

	public void setTonenBijMatching(boolean tonenBijMatching)
	{
		this.tonenBijMatching = tonenBijMatching;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		if (getDatum() != null)
		{
			builder.append(TimeUtil.getInstance().formatDate(getDatum()));
			builder.append(" - ");
		}
		if (getAuteur() != null)
		{
			builder.append(getAuteur().getAfkorting());
			builder.append(" - ");
		}
		builder.append(getOpmerking());
		return builder.toString();
	}
}
