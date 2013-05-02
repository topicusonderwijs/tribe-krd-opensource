/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.incident;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.iris.services.DagDeel;
import nl.iris.services.TijdSpecificatie;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.text.HtmlLabel;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.bijlagen.IrisIncidentBijlage;
import nl.topicus.eduarte.entities.dbs.gedrag.Incident;
import nl.topicus.eduarte.entities.dbs.gedrag.IncidentCategorie;
import nl.topicus.eduarte.entities.dbs.gedrag.IrisIncidentNietTonenInZorgvierkant;
import nl.topicus.eduarte.entities.dbs.gedrag.Kleur;
import nl.topicus.eduarte.entities.dbs.incident.IrisBetrokkene.EduArteRolOpSchool;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.choice.ZorglijnCombobox;
import nl.topicus.eduarte.web.components.text.samenvatting.SamenvattingTextEditorPanel;
import nl.topicus.onderwijs.incidentkoppeling.model.IrisLocatie;
import nl.topicus.onderwijs.incidentkoppeling.model.IrisRolBijIncident;
import nl.topicus.onderwijs.incidentkoppeling.model.IrisVoorval;
import nl.topicus.onderwijs.incidentkoppeling.model.IrisVoorwerp;
import nl.topicus.onderwijs.incidentkoppeling.model.interfaces.IIrisIncident;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class IrisIncident extends BeginEinddatumInstellingEntiteit implements IIrisIncident,
		IBijlageKoppelEntiteit<IrisIncidentBijlage>
{
	private static final long serialVersionUID = 1L;

	@Column(length = 50, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String titel;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@AutoForm(label = "Zwaarte", htmlClasses = "unit_max")
	private Kleur kleur;

	@Column(nullable = true, length = 2)
	@AutoForm(editorClass = ZorglijnCombobox.class)
	private Integer zorglijn;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "irisIncident")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<IrisIncidentNietTonenInZorgvierkant> nietTonenInZorgvierkants =
		new ArrayList<IrisIncidentNietTonenInZorgvierkant>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "irisIncident")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<IrisBetrokkene> betrokkene = new ArrayList<IrisBetrokkene>();

	@ManyToOne(fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@JoinColumn(name = "auteur", nullable = false)
	@Index(name = "idx_IrisInc_auteur")
	private Medewerker auteur;

	@ManyToOne(fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@JoinColumn(name = "categorie", nullable = false)
	@Index(name = "idx_IrisInc_categorie")
	private IncidentCategorie categorie;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "incident")
	private List<IrisIncidentLocatie> incidentLocatie = new ArrayList<IrisIncidentLocatie>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "incident")
	private List<IrisIncidentVoorwerp> incidentVoorwerp = new ArrayList<IrisIncidentVoorwerp>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "irisIncident")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<IrisIncidentBijlage> bijlagen = new ArrayList<IrisIncidentBijlage>();

	@Column(nullable = true)
	@AutoForm(htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@Column(nullable = false)
	private boolean afgerond;

	@Column(nullable = false)
	private boolean onzeker;

	@Column(nullable = false)
	private boolean vertrouwelijk;

	@Lob
	@AutoForm(editorClass = SamenvattingTextEditorPanel.class, displayClass = HtmlLabel.class)
	private String toelichting;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TijdSpecificatie tijdType;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private DagDeel dagDeel;

	@Column(nullable = true)
	private String irisIncidentNummer;

	@Column(nullable = true)
	private String tijdstip;

	public IrisIncident()
	{
	}

	public IrisIncident(Medewerker auteur, Deelnemer betrokkenDeelnemer)
	{
		Incident i = new Incident();
		i.setDeelnemer(betrokkenDeelnemer);
		IrisBetrokkene bet = new IrisBetrokkene(this);
		bet.setIncident(i);
		i.setBetrokkene(bet);
		bet.setRolOpSchool(EduArteRolOpSchool.Deelnemer);
		addBetrokkene(bet);
		setAuteur(auteur);
		setTijdType(TijdSpecificatie.TIJDSTIP);
	}

	public IrisIncident(Medewerker auteur, Medewerker betrokkenMedewerker)
	{
		IrisBetrokkene bet = new IrisBetrokkene();
		bet.setMedewerker(betrokkenMedewerker);
		addBetrokkene(bet);
		setAuteur(auteur);
		setTijdType(TijdSpecificatie.TIJDSTIP);
	}

	@Override
	public boolean getAfgerond()
	{
		return afgerond;
	}

	@Override
	public Date getBeginDatum()
	{
		return super.getBegindatum();
	}

	@Override
	public List<IrisBetrokkene> getBetrokkene()
	{
		return betrokkene;
	}

	public void setBetrokkene(List<IrisBetrokkene> betrokkene)
	{
		this.betrokkene = betrokkene;
	}

	@Override
	public DagDeel getDagDeel()
	{
		return dagDeel;
	}

	@Override
	public Date getEindDatum()
	{
		return getTijdType().equals(TijdSpecificatie.TIJDSTIP) ? getBegindatum() : super
			.getEinddatum();
	}

	/**
	 * Wicket's ListMultipleChoice wil perse iedere keer hetzelfde object terug van de
	 * getter, vandaar dit transient object
	 */
	@Transient
	private transient List<IrisLocatie> locatiesCache = new ArrayList<IrisLocatie>();

	@Override
	public List<IrisLocatie> getLocatie()
	{
		List<IrisLocatie> locaties = locatiesCache;
		locaties.clear();
		if (getIncidentLocatie() != null)
		{
			for (IrisIncidentLocatie loc : getIncidentLocatie())
				locaties.add(loc.getLocatie());
			return locaties;
		}
		else
		{
			return locaties;
		}
	}

	public void setLocatie(List<IrisLocatie> loc)
	{
		if (loc != null)
		{
			List<IrisLocatie> nieuw = new ArrayList<IrisLocatie>(loc);
			List<IrisLocatie> huidig = getLocatie();
			for (IrisLocatie il : nieuw)
				if (!huidig.contains(il))
					addLocatie(il);

			for (IrisLocatie il : huidig)
				if (!nieuw.contains(il))
					removeLocatie(il);
		}
	}

	public void addLocatie(IrisLocatie il)
	{
		IrisIncidentLocatie iil = new IrisIncidentLocatie();
		iil.setIncident(this);
		iil.setLocatie(il);
		getIncidentLocatie().add(iil);
	}

	public void removeLocatie(IrisLocatie il)
	{
		for (IrisIncidentLocatie iil : getIncidentLocatie())
		{
			if (iil.getLocatie().equals(il))
			{
				getIncidentLocatie().remove(iil);
				return;
			}
		}
	}

	public List<IrisIncidentLocatie> getIncidentLocatie()
	{
		return incidentLocatie;
	}

	public void setIncidentLocatie(List<IrisIncidentLocatie> incidentLocatie)
	{
		this.incidentLocatie = incidentLocatie;
	}

	@Override
	public boolean getOnzeker()
	{
		return onzeker;
	}

	@Override
	public TijdSpecificatie getTijdType()
	{
		return tijdType;
	}

	@Override
	public String getTijdstip()
	{
		return tijdstip;
	}

	public void setTijdstip(String tijdstip)
	{
		this.tijdstip = tijdstip;
	}

	@Override
	public String getToelichting()
	{
		return toelichting;
	}

	@Override
	public String getVestigingBRIN()
	{
		// Returnt BRIN van de organisatie, BRIN op organisatie-eenheid niveau wordt nog
		// niet ondersteund in EduArte
		return EduArteContext.get().getInstelling().getBrincode().getCode();
	}

	@Override
	public List<IrisVoorval> getVoorval()
	{
		List<IrisVoorval> voorvallen = new ArrayList<IrisVoorval>();
		voorvallen.add(getCategorie().getIrisVoorval());
		return voorvallen;
	}

	/**
	 * Wicket's ListMultipleChoice wil perse iedere keer hetzelfde object terug van de
	 * getter, vandaar dit transient object
	 */
	@Transient
	private transient List<IrisVoorwerp> voorwerpCache = new ArrayList<IrisVoorwerp>();

	@Override
	public List<IrisVoorwerp> getVoorwerp()
	{
		List<IrisVoorwerp> voorwerpen = voorwerpCache;
		voorwerpCache.clear();
		if (getIncidentVoorwerp() != null)
		{
			for (IrisIncidentVoorwerp vw : getIncidentVoorwerp())
				voorwerpen.add(vw.getVoorwerp());
			return voorwerpen;
		}
		else
		{
			return null;
		}
	}

	public void setVoorwerp(List<IrisVoorwerp> voorwerpen)
	{
		if (voorwerpen != null)
		{
			List<IrisVoorwerp> nieuw = new ArrayList<IrisVoorwerp>(voorwerpen);
			List<IrisVoorwerp> huidig = getVoorwerp();
			for (IrisVoorwerp iv : nieuw)
				if (!huidig.contains(iv))
					addVoorwerp(iv);

			for (IrisVoorwerp iv : huidig)
				if (!nieuw.contains(iv))
					removeVoorwerp(iv);
		}
	}

	public void addVoorwerp(IrisVoorwerp iv)
	{
		IrisIncidentVoorwerp iiv = new IrisIncidentVoorwerp();
		iiv.setIncident(this);
		iiv.setVoorwerp(iv);
		getIncidentVoorwerp().add(iiv);
	}

	public void removeVoorwerp(IrisVoorwerp iv)
	{
		for (IrisIncidentVoorwerp iiv : getIncidentVoorwerp())
		{
			if (iiv.getVoorwerp().equals(iv))
			{
				getIncidentVoorwerp().remove(iiv);
				return;
			}
		}
	}

	public void setIncidentVoorwerp(List<IrisIncidentVoorwerp> iivs)
	{
		this.incidentVoorwerp = iivs;
	}

	public List<IrisIncidentVoorwerp> getIncidentVoorwerp()
	{
		return incidentVoorwerp;
	}

	public void setAuteur(Medewerker auteur)
	{
		this.auteur = auteur;
	}

	public Medewerker getAuteur()
	{
		return auteur;
	}

	public void setCategorie(IncidentCategorie categorie)
	{
		this.categorie = categorie;
	}

	public IncidentCategorie getCategorie()
	{
		return categorie;
	}

	@Override
	public IrisIncidentBijlage addBijlage(Bijlage bijlage)
	{
		for (IrisBetrokkene bet : getBetrokkene())
		{
			if (bet.getIncident() != null)
			{
				bet.getIncident().addBijlage(bijlage);
			}
		}

		IrisIncidentBijlage newBijlage = new IrisIncidentBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setIrisIncident(this);
		getBijlagen().add(newBijlage);
		return newBijlage;
	}

	/**
	 * Verwijder de bijlage uit de incidenten van de betrokkenen
	 */
	public void removeIncidentBijlages(Bijlage bijlage)
	{
		for (IrisBetrokkene bet : getBetrokkene())
		{
			if (bet.getIncident() != null)
			{
				bet.getIncident().removeBijlage(bijlage);
			}
		}
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (IrisIncidentBijlage deelbijlage : getBijlagen())
		{
			if (deelbijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	public List<IrisIncidentBijlage> getBijlagen()
	{
		return bijlagen;
	}

	@Override
	public void setBijlagen(List<IrisIncidentBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;

	}

	public void setTitel(String titel)
	{
		this.titel = titel;
	}

	public void setTijdType(TijdSpecificatie ts)
	{
		this.tijdType = ts;
	}

	public String getTitel()
	{
		return titel;
	}

	public void setKleur(Kleur kleur)
	{
		this.kleur = kleur;
	}

	public Kleur getKleur()
	{
		return kleur;
	}

	public void setZorglijn(Integer zorglijn)
	{
		this.zorglijn = zorglijn;
	}

	public Integer getZorglijn()
	{
		return zorglijn;
	}

	public void setVertrouwelijk(boolean vertrouwelijk)
	{
		this.vertrouwelijk = vertrouwelijk;
	}

	public boolean isVertrouwelijk()
	{
		return vertrouwelijk;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void addBetrokkene(IrisBetrokkene bet)
	{
		if (betrokkene == null)
			betrokkene = new ArrayList<IrisBetrokkene>();
		betrokkene.add(bet);
	}

	public void setToelichting(String toelichting)
	{
		this.toelichting = toelichting;
	}

	public List<Medewerker> betrokkenMedewerkersAsMedewerker()
	{
		List<Medewerker> medewerkers = new ArrayList<Medewerker>();
		for (IrisBetrokkene ib : betrokkene)
			if (ib.getMedewerker() != null)
				medewerkers.add(ib.getMedewerker());
		return medewerkers;
	}

	private IrisIncidentNietTonenInZorgvierkant findNietTonen()
	{
		Medewerker medewerker = EduArteContext.get().getMedewerker();
		for (IrisIncidentNietTonenInZorgvierkant curTonen : getNietTonenInZorgvierkants())
		{
			if (curTonen.getMedewerker().equals(medewerker))
				return curTonen;
		}
		return null;
	}

	public boolean isTonenInZorgvierkant()
	{
		return findNietTonen() == null;
	}

	public void setTonenInZorgvierkant(boolean tonenInZorgvierkant)
	{
		setTonenInZorgvierkant(tonenInZorgvierkant, false);
	}

	public void setTonenInZorgvierkant(boolean tonenInZorgvierkant, boolean save)
	{
		IrisIncidentNietTonenInZorgvierkant nietTonen = findNietTonen();
		if (nietTonen == null && !tonenInZorgvierkant)
		{
			IrisIncidentNietTonenInZorgvierkant newNietTonen =
				new IrisIncidentNietTonenInZorgvierkant(this, EduArteContext.get().getMedewerker());
			getNietTonenInZorgvierkants().add(newNietTonen);

			if (save)
				newNietTonen.save();
		}
		else if (nietTonen != null && tonenInZorgvierkant)
		{
			getNietTonenInZorgvierkants().remove(nietTonen);

			if (save)
				nietTonen.delete();
		}
	}

	public List<IrisIncidentNietTonenInZorgvierkant> getNietTonenInZorgvierkants()
	{
		return nietTonenInZorgvierkants;
	}

	public void setNietTonenInZorgvierkants(
			List<IrisIncidentNietTonenInZorgvierkant> nietTonenInZorgvierkants)
	{
		this.nietTonenInZorgvierkants = nietTonenInZorgvierkants;
	}

	public void setIrisIncidentNummer(String irisIncidentNummer)
	{
		this.irisIncidentNummer = irisIncidentNummer;
	}

	public String getFormattedIrisIncidentNummer()
	{
		if (!StringUtil.isEmpty(getIrisIncidentNummer()))
			return getIrisIncidentNummer();

		String nietIngevuld = getNietIngevuldeIrisVelden();
		return !StringUtil.isEmpty(nietIngevuld) ? nietIngevuld : "Incident nog niet verstuurd";
	}

	/**
	 * Return comma-seperated lijst van de namen van de velden die niet ingevuld zijn,
	 * maar wel verplicht zijn voor IRIS+ communicatie. Return een lege string als alle
	 * benodigde velden ingevuld zijn.
	 */
	public String getNietIngevuldeIrisVelden()
	{
		List<String> velden = new ArrayList<String>();

		if (getTitel() == null)
			velden.add("Titel");
		if (getBegindatum() == null)
			velden.add("Datum");
		if (getTijdType() == null)
			velden.add("Tijdtype");
		if (getTijdType().equals(TijdSpecificatie.PERIODE) && getEinddatum() == null)
			velden.add("Einddatum");
		if (getTijdType().equals(TijdSpecificatie.TIJDSTIP) && getDagDeel() == null)
			velden.add("Dagdeel");
		if (getLocatie().isEmpty())
			velden.add("Locatie");
		if (getVoorwerp().isEmpty())
			velden.add("Voorwerp");

		for (IrisBetrokkene bet : getBetrokkene())
		{
			if (bet.getRolBijIncident().equals(IrisRolBijIncident.Dader)
				&& bet.getMotief().isEmpty())
				velden.add("Motief van " + bet.getNaam());
		}

		if (getCategorie() == null || getCategorie().getIrisVoorval() == null)
		{
			if (!velden.isEmpty())
				return "Het incident is niet verzonden naar IRIS+ omdat de categorie niet aan IRIS+ gekoppeld is en de volgende velden niet ingevuld zijn: "
					+ StringUtil.maakCommaSeparatedString(velden);
			else
				return "Het incident is niet verzonden naar IRIS+ omdat de categorie niet aan IRIS+ gekoppeld is";
		}
		else
		{
			if (!velden.isEmpty())
				return "Het incident is niet verzonden naar IRIS+ omdat de volgende velden niet ingevuld zijn: "
					+ StringUtil.maakCommaSeparatedString(velden);
			else
				return null;
		}
	}

	public String getIrisIncidentNummer()
	{
		return irisIncidentNummer;
	}

	@Override
	public String getIncidentNummer()
	{
		return getIrisIncidentNummer();
	}

	public boolean staatInIris()
	{
		return getIrisIncidentNummer() != null;
	}

	public String getDeelnemers()
	{
		List<Deelnemer> deelnemers = new ArrayList<Deelnemer>();
		for (IrisBetrokkene bet : getBetrokkene())
			if (bet.isDeelnemer())
				deelnemers.add(bet.getDeelnemer());
		return StringUtil.maakCommaSeparatedString(deelnemers);
	}

	public String getMedewerkers()
	{
		List<Medewerker> medewerkers = new ArrayList<Medewerker>();
		for (IrisBetrokkene bet : getBetrokkene())
			if (bet.isMedewerker())
				medewerkers.add(bet.getMedewerker());
		return StringUtil.maakCommaSeparatedString(medewerkers);
	}

	public void setAfgerond(boolean afgerond)
	{
		this.afgerond = afgerond;
	}

	public void setOnzeker(boolean onzeker)
	{
		this.onzeker = onzeker;
	}

	public void setDagDeel(DagDeel dagdeel)
	{
		this.dagDeel = dagdeel;
	}
}
