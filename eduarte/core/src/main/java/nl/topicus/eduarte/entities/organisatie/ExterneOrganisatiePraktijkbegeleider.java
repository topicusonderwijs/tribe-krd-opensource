/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.providers.ExterneOrganisatieProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author vandekamp
 * 
 *         Vaste praktijkbegeleiders: een lijst van medewerkers van de onderwijsinstelling
 *         die standaard de BPV-deelnemers bij dit BPV-bedrijf begeleiden. Het is mogelijk
 *         om op deze plek een medewerker uit de lijst te verwijderen, maar het is niet
 *         mogelijk er een toe te voegen. Dat wordt namelijk gedaan bij het aanmaken van
 *         een BPV-verbintenis tussen een deelnemer en het bedrijf in kwestie. Onder het
 *         veld voor de praktijkbegeleider bij een (nieuwe) BPV-verbintenis wordt een
 *         vinkje ‘opslaan bij bedrijf’ toegevoegd. Deze staat standaard aan, maar kan
 *         uitgezet worden. Wanneer het vinkje aanstaat, wordt de gekozen medewerker
 *         opgeslagen in de lijst met vaste praktijkbegeleiders, tenzij de medewerker daar
 *         al in staat. Het veld ‘praktijkbegeleider’ bij een nieuwe BPV-verbintenis wordt
 *         standaard gevuld met de laatst gebruikte medewerker voor dat bedrijf. Het is
 *         mogelijk dit te wijzigen. Wanneer men op het ‘zoeken’ icoontje achter het veld
 *         klikt, wordt een zoeklijst voor medewerkers getoond in een popup. In eerste
 *         instantie worden alle medewerkers getoond. Deze zijn zo gegroepeerd dat
 *         bovenaan de lijst de medewerkers staan die in de lijst met vaste
 *         praktijkbegeleiders voorkomen en daaronder alle andere medewerkers.
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(name = "ExtOrgPraktijkBegeleider")
public class ExterneOrganisatiePraktijkbegeleider extends InstellingEntiteit implements
		ExterneOrganisatieProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externeOrganisatie", nullable = false)
	@Index(name = "idx_ExtOrgPrak_externeOrgan")
	private ExterneOrganisatie externeOrganisatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = false)
	@Index(name = "idx_ExtOrgPrak_medewerker")
	private Medewerker medewerker;

	@Column(nullable = false)
	private boolean laatstGebruikteMedewerker;

	public ExterneOrganisatiePraktijkbegeleider()
	{
	}

	public ExterneOrganisatiePraktijkbegeleider(ExterneOrganisatie externeOrganisatie)
	{
		setExterneOrganisatie(externeOrganisatie);
	}

	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	public ExterneOrganisatie getExterneOrganisatie()
	{
		return externeOrganisatie;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setLaatstGebruikteMedewerker(boolean laatstGebruikteMedewerker)
	{
		this.laatstGebruikteMedewerker = laatstGebruikteMedewerker;
	}

	public boolean isLaatstGebruikteMedewerker()
	{
		return laatstGebruikteMedewerker;
	}
}
