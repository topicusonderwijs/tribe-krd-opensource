/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.gedrag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.ZorgvierkantObject;
import nl.topicus.eduarte.entities.dbs.bijlagen.IncidentBijlage;
import nl.topicus.eduarte.entities.dbs.incident.IrisBetrokkene;
import nl.topicus.eduarte.entities.dbs.incident.IrisBetrokkeneAfhandeling;
import nl.topicus.eduarte.entities.dbs.incident.IrisIncident;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Incident extends InstellingEntiteit implements
		IBijlageKoppelEntiteit<IncidentBijlage>, ZorgvierkantObject
{
	private static final long serialVersionUID = 1L;

	public static final String INCIDENT = "INCIDENT";

	public static final String VERTROUWELIJK_INCIDENT = "VERTROUWELIJK_INCIDENT";

	@Column(nullable = true)
	@Lob
	@AutoForm(htmlClasses = "unit_max")
	private String consequenties;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "incident")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<IncidentBijlage> bijlagen = new ArrayList<IncidentBijlage>();

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "incident")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BetrokkenMedewerker> betrokkenMedewerkers = new ArrayList<BetrokkenMedewerker>();

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "incident")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private IrisBetrokkene betrokkene;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_newIncident_deelnemer")
	private Deelnemer deelnemer;

	public Incident()
	{
	}

	public Incident(Deelnemer deelnemer)
	{
		setDeelnemer(deelnemer);
	}

	public String getConsequenties()
	{
		if (consequenties != null && !"".equals(consequenties))
			return consequenties;

		String afhandelingen = "";

		if (getBetrokkene() != null && getBetrokkene().getBetrokkeneAfhandelingen() != null)
			for (IrisBetrokkeneAfhandeling afhandeling : getBetrokkene()
				.getBetrokkeneAfhandelingen())
				afhandelingen += afhandeling.getAfhandeling().getNaam();

		return afhandelingen;
	}

	public void setConsequenties(String consequenties)
	{
		this.consequenties = consequenties;
	}

	public List<IncidentBijlage> getBijlagen()
	{
		return bijlagen;
	}

	@Override
	public boolean isVertrouwelijk()
	{
		return getBetrokkene().getIrisIncident().isVertrouwelijk();
	}

	@Override
	public boolean isTonenInZorgvierkant()
	{
		return getBetrokkene().getIrisIncident().isTonenInZorgvierkant();
	}

	public void setTonenInZorgvierkant(boolean tonenInZorgvierkant, boolean save)
	{
		getBetrokkene().getIrisIncident().setTonenInZorgvierkant(tonenInZorgvierkant, save);
	}

	public void setBijlagen(List<IncidentBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
	}

	public List<BetrokkenMedewerker> getBetrokkenMedewerkers()
	{
		return betrokkenMedewerkers;
	}

	public void setBetrokkenMedewerkers(List<BetrokkenMedewerker> betrokkenMedewerkers)
	{
		this.betrokkenMedewerkers = betrokkenMedewerkers;
	}

	public List<Medewerker> getBetrokkenMedewerkersAsMedewerker()
	{
		List<Medewerker> ret = new ArrayList<Medewerker>();
		for (BetrokkenMedewerker curMedewerker : getBetrokkenMedewerkers())
		{
			ret.add(curMedewerker.getMedewerker());
		}
		return ret;
	}

	public String getBetrokkenMedewersAsString()
	{
		StringBuilder builder = new StringBuilder();

		for (Medewerker medewerker : getBetrokkenMedewerkersAsMedewerker())
		{
			builder.append(medewerker.getPersoon().getVolledigeNaam());
			builder.append(", ");
		}
		return builder.substring(0, (builder.length() - 2));
	}

	@Override
	public IncidentBijlage addBijlage(Bijlage bijlage)
	{
		IncidentBijlage newBijlage = new IncidentBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setDeelnemer(getDeelnemer());
		newBijlage.setIncident(this);

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	public void removeBijlage(Bijlage bijlage)
	{
		List<IncidentBijlage> toRemove = new ArrayList<IncidentBijlage>();
		for (IncidentBijlage deelbijlage : getBijlagen())
		{
			if (deelbijlage.getBijlage().equals(bijlage))
				toRemove.add(deelbijlage);
		}
		getBijlagen().removeAll(toRemove);
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (IncidentBijlage deelbijlage : getBijlagen())
		{
			if (deelbijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	public String getSecurityId()
	{
		return INCIDENT;
	}

	@Override
	public String getVertrouwelijkSecurityId()
	{
		return VERTROUWELIJK_INCIDENT;
	}

	public void setBetrokkene(IrisBetrokkene betrokkene)
	{
		this.betrokkene = betrokkene;
	}

	public IrisBetrokkene getBetrokkene()
	{
		return betrokkene;
	}

	public IncidentCategorie getCategorie()
	{
		return getBetrokkene().getIrisIncident().getCategorie();
	}

	public Date getDatumInvoer()
	{
		return getBetrokkene().getIrisIncident().getBegindatum();
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	@Override
	public Integer getZorglijn()
	{
		return getBetrokkene().getIrisIncident().getZorglijn();
	}

	public String getCssClass()
	{
		if (getBetrokkene().getIrisIncident().getKleur() == null)
			return "";
		return getBetrokkene().getIrisIncident().getKleur().getCssClass();
	}

	public String getTitel()
	{
		IrisBetrokkene bet = getBetrokkene();
		IrisIncident ii = bet.getIrisIncident();
		return ii.getTitel();
	}

	// Om de rapportage te laten werken
	public Medewerker getAuteur()
	{
		return getBetrokkene().getIrisIncident().getAuteur();
	}

	public Kleur getKleur()
	{
		return getBetrokkene().getIrisIncident().getKleur();
	}

	public String getDatumInvoerString()
	{
		return TimeUtil.getInstance().formatDate(getDatumInvoer());
	}

	public String getOmschrijving()
	{
		return getBetrokkene().getIrisIncident().getToelichting();
	}
}
