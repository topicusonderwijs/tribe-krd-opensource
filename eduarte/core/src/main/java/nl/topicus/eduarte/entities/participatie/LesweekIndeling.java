/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.participatie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Indelingen lesweken. Verschillende lesweekindelingen kunnen verschillende weekdagen
 * bevatten en verschillende lesuren. Een lesweekindeling bevat een lijst met
 * lesdagindelingen. Een lesdagindeling bevat weer een verwijzing naar de indeling van
 * lesuren op die dag.
 * 
 * @author loite, henzen
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class LesweekIndeling extends InstellingEntiteit implements
		IOrganisatieEenheidLocatieKoppelbaarEntiteit<LesweekIndelingOrganisatieEenheidLocatie>,
		IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(name = "naam", length = 25, nullable = false)
	@Index(name = "idx_Lesweek_naam")
	@AutoForm(label = "Naam")
	private String naam;

	@Column(name = "actief", length = 25)
	@Index(name = "idx_Lesweek_actief")
	@AutoForm(label = "Actief")
	private boolean actief;

	@Column(name = "omschrijving", length = 60)
	@Index(name = "idx_Lesweek_Omschr")
	@AutoForm(label = "Omschrijving")
	private String omschrijving;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lesweekIndeling")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@AutoForm(label = "Dag")
	private List<LesdagIndeling> lesdagIndelingen = new ArrayList<LesdagIndeling>();

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lesweekIndeling")
	private List<LesweekIndelingOrganisatieEenheidLocatie> LesweekIndelingOrganisatieEenheidLocatie;

	public LesweekIndeling()
	{
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public List<LesdagIndeling> getLesdagIndelingen()
	{
		return lesdagIndelingen;
	}

	public List<LesdagIndeling> getLesdagIndelingenOrderedByDay()
	{
		List<LesdagIndeling> lesDagen = new ArrayList<LesdagIndeling>();
		lesDagen.addAll(lesdagIndelingen);
		Collections.sort(lesDagen, new Comparator<LesdagIndeling>()
		{
			@Override
			public int compare(LesdagIndeling lesdag1, LesdagIndeling lesdag2)
			{
				if (lesdag1.getDagNummer() > lesdag2.getDagNummer())
				{
					return 1;
				}
				return 0;
			}
		});
		return lesDagen;
	}

	public void addLesdagIndeling(LesdagIndeling lesdag)
	{
		lesdagIndelingen.add(lesdag);
	}

	/**
	 * @return de lesdagindeling met de meeste lestijden, als er geen lesdagindelingen
	 *         zijn null
	 */
	public LesdagIndeling getIndelingMetMeesteLestijden()
	{
		int meesteLestijden = -1;
		LesdagIndeling indelingMetMeesteLestijden = null;
		for (LesdagIndeling lesdag : getLesdagIndelingen())
		{
			if (lesdag.getLesuurIndeling().size() > meesteLestijden)
			{
				meesteLestijden = lesdag.getLesuurIndeling().size();
				indelingMetMeesteLestijden = lesdag;
			}
		}
		if (indelingMetMeesteLestijden != null)
			return indelingMetMeesteLestijden;
		return null;
	}

	public void setLesdagIndelingen(List<LesdagIndeling> lesdagIndelingen)
	{
		this.lesdagIndelingen = lesdagIndelingen;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public boolean isActief()
	{
		return actief;
	}

	public String getActiefOmschrijving()
	{
		return isActief() ? "Ja" : "Nee";
	}

	@Override
	public List<LesweekIndelingOrganisatieEenheidLocatie> getOrganisatieEenheidLocatieKoppelingen()
	{
		return getLesweekIndelingOrganisatieEenheidLocatie();
	}

	public void setLesweekIndelingOrganisatieEenheidLocatie(
			List<LesweekIndelingOrganisatieEenheidLocatie> lesweekIndelingOrganisatieEenheidLocatie)
	{
		LesweekIndelingOrganisatieEenheidLocatie = lesweekIndelingOrganisatieEenheidLocatie;
	}

	public List<LesweekIndelingOrganisatieEenheidLocatie> getLesweekIndelingOrganisatieEenheidLocatie()
	{
		return LesweekIndelingOrganisatieEenheidLocatie;
	}

	@Override
	public String toString()
	{
		return naam;
	}
}
