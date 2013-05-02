/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.personen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.StringUtil.StringConverter;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.groep.GroepDocent;
import nl.topicus.eduarte.entities.kenmerk.MedewerkerKenmerk;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.entities.vrijevelden.MedewerkerVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.providers.MedewerkerProvider;
import nl.topicus.eduarte.providers.PersoonProvider;
import nl.topicus.eduarte.util.OrganisatieEenheidLocatieUtil;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author loite
 */
@Exportable
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@IsViewWhenOnNoise
public class Medewerker extends BeginEinddatumInstellingEntiteit implements PersoonProvider,
		IContextInfoObject, IOrganisatieEenheidLocatieKoppelbaarEntiteit<OrganisatieMedewerker>,
		VrijVeldable<MedewerkerVrijVeld>, MedewerkerProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "persoon", nullable = false)
	@Index(name = "idx_Medewerker_persoon")
	@AutoFormEmbedded
	private Persoon persoon;

	/**
	 * Dit staat ook bekend als de roosterCode
	 */
	@Column(length = 10, nullable = false)
	@AutoForm(htmlClasses = "unit_80")
	private String afkorting;

	/**
	 * Collectie met alle organisatie-eenheden waarbij de medewerker hoort.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "medewerker")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<OrganisatieMedewerker> organisatieMedewerkers;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "functie", nullable = true)
	@Index(name = "idx_Medewerker_functie")
	private Functie functie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "redenUitDienst", nullable = true)
	@Index(name = "idx_Medewerker_redenUitDienst")
	private RedenUitDienst redenUitDienst;

	@Column(nullable = false, name = "uitgeslotenVanCorres")
	@AutoForm(label = "Uitgesloten van correspondentie")
	private boolean uitgeslotenVanCorrespondentie = false;

	@Column(length = 200, nullable = true, name = "redenUitgeslotenVanCorres")
	@AutoForm(label = "Reden uitgesloten van correspondentie")
	private String redenUitgeslotenVanCorrespondentie;

	/**
	 * Collectie van groepen waarvan deze medewerker de docent is.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "medewerker")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@AutoForm(include = false)
	private Collection<GroepDocent> groepenDocent;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "medewerker")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<MedewerkerVrijVeld> vrijVelden = new ArrayList<MedewerkerVrijVeld>();

	@Transient
	transient Locatie transLocatie = null;

	// niet persistent, maar wel cachen wanneer het opgevraagd wordt.
	@Transient
	@AutoFormEmbedded
	private Account account;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "medewerker")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<MedewerkerKenmerk> kenmerken = new ArrayList<MedewerkerKenmerk>();

	public Medewerker()
	{
	}

	@Override
	@AutoForm(label = "Datum in dienst")
	public Date getBegindatum()
	{
		return super.getBegindatum();
	}

	@Override
	@AutoForm(label = "Datum uit dienst")
	public Date getEinddatum()
	{
		return super.getEinddatum();
	}

	/**
	 * @return Het account van deze medewerker, of null indien deze medewerker geen
	 *         account heeft.
	 */
	@Exportable
	public Account getAccount()
	{
		if (account == null)
			account = DataAccessRegistry.getHelper(AccountDataAccessHelper.class).get(this);

		return account;
	}

	@Exportable
	public Persoon getPersoon()
	{
		return persoon;
	}

	public void setPersoon(Persoon persoon)
	{
		this.persoon = persoon;
	}

	@Exportable
	public String getAfkorting()
	{
		return afkorting;
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

	public List<OrganisatieMedewerker> getOrganisatieMedewerkers()
	{
		if (organisatieMedewerkers == null)
			organisatieMedewerkers = new ArrayList<OrganisatieMedewerker>();
		return organisatieMedewerkers;
	}

	public void setOrganisatieMedewerkers(List<OrganisatieMedewerker> organisatieMedewerkers)
	{
		this.organisatieMedewerkers = organisatieMedewerkers;
	}

	@Override
	public String toString()
	{
		if (getAfkorting() != null)
			return getAfkorting() + " - " + getPersoon().getVolledigeNaam();
		return getPersoon().getVolledigeNaam();
	}

	/**
	 * @return De default organisatie-eenheid van deze medewerker.
	 */
	@Exportable
	public OrganisatieEenheid getDefaultOrganisatieEenheid()
	{
		List<OrganisatieEenheid> list =
			OrganisatieEenheidLocatieUtil.getActieveOrganisatieEenheden(this);
		if (!list.isEmpty())
		{
			return list.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @return Geeft een default Locatie van deze medewerker.
	 */
	@Exportable
	public Locatie getDefaultLocatie()
	{
		if (transLocatie == null)
		{
			List<OrganisatieMedewerker> list = getOrganisatieMedewerkers();
			for (OrganisatieMedewerker om : list)
			{
				if (om.isActief() && om.getLocatie() != null)
				{
					transLocatie = om.getLocatie();
					break;
				}
			}
		}
		return transLocatie;
	}

	/**
	 * @return Ja indien de medewerker actief is, en anders Nee
	 */
	@Exportable
	public String getActiefOmschrijving()
	{
		return isActief() ? "Ja" : "Nee";
	}

	@Override
	public String getContextInfoOmschrijving()
	{
		return toString();
	}

	public String getActieveOrganisatieEenhedenNamen()
	{
		StringBuilder res = new StringBuilder();
		int count = 0;
		for (OrganisatieEenheid oe : OrganisatieEenheidLocatieUtil
			.getActieveOrganisatieEenheden(this))
		{
			if (count > 0)
			{
				res.append(", ");
			}
			res.append(oe.getNaam());
			count++;
		}
		return res.toString();
	}

	/**
	 * @return een comma-separated string met de name van de actieve locaties waaraan deze
	 *         medewerker is gekoppeld.
	 */
	public String getActieveLocatiesNamen()
	{
		return getActieveLocatiesNamen(null);
	}

	public String getActieveLocatiesNamen(OrganisatieEenheid organisatieEenheid)
	{
		StringBuilder res = new StringBuilder();
		int count = 0;
		for (Locatie locatie : OrganisatieEenheidLocatieUtil.getActieveLocaties(this
			.getOrganisatieEenheidLocatieKoppelingen(), organisatieEenheid))
		{
			if (count > 0)
			{
				res.append(", ");
			}
			res.append(locatie);
			count++;
		}
		return res.toString();

	}

	@Exportable
	public Functie getFunctie()
	{
		return functie;
	}

	public void setFunctie(Functie functie)
	{
		this.functie = functie;
	}

	@Exportable
	public RedenUitDienst getRedenUitDienst()
	{
		return redenUitDienst;
	}

	public void setRedenUitDienst(RedenUitDienst redenUitDienst)
	{
		this.redenUitDienst = redenUitDienst;
	}

	public boolean isUitgeslotenVanCorrespondentie()
	{
		return uitgeslotenVanCorrespondentie;
	}

	public void setUitgeslotenVanCorrespondentie(boolean uitgeslotenVanCorrespondentie)
	{
		this.uitgeslotenVanCorrespondentie = uitgeslotenVanCorrespondentie;
	}

	public String getRedenUitgeslotenVanCorrespondentie()
	{
		return redenUitgeslotenVanCorrespondentie;
	}

	public void setRedenUitgeslotenVanCorrespondentie(String redenUitgeslotenVanCorrespondentie)
	{
		this.redenUitgeslotenVanCorrespondentie = redenUitgeslotenVanCorrespondentie;
	}

	public String getOrganisatieNaam()
	{
		return getOrganisatie().getNaam();
	}

	public void setGroepenDocent(Collection<GroepDocent> groepenDocent)
	{
		this.groepenDocent = groepenDocent;
	}

	public Collection<GroepDocent> getGroepenDocent()
	{
		return groepenDocent;
	}

	@Override
	public List<OrganisatieMedewerker> getOrganisatieEenheidLocatieKoppelingen()
	{
		return getOrganisatieMedewerkers();
	}

	@Override
	public List<MedewerkerVrijVeld> getVrijVelden()
	{
		return vrijVelden;
	}

	@Override
	public List<MedewerkerVrijVeld> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<MedewerkerVrijVeld> res = new ArrayList<MedewerkerVrijVeld>();
		for (MedewerkerVrijVeld mvv : getVrijVelden())
		{
			if (mvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(mvv);
			}
		}
		return res;
	}

	@Override
	public MedewerkerVrijVeld newVrijVeld()
	{
		MedewerkerVrijVeld mvv = new MedewerkerVrijVeld();
		mvv.setMedewerker(this);

		return mvv;
	}

	@Override
	public void setVrijVelden(List<MedewerkerVrijVeld> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}

	@Exportable
	@Override
	public String getVrijVeldWaarde(String naam)
	{
		for (MedewerkerVrijVeld vrijVeld : vrijVelden)
			if (vrijVeld.getVrijVeld().getNaam().equals(naam))
				return vrijVeld.getOmschrijving();
		return null;
	}

	public List<MedewerkerKenmerk> getKenmerken()
	{
		return kenmerken;
	}

	public void setKenmerken(List<MedewerkerKenmerk> kenmerken)
	{
		this.kenmerken = kenmerken;
	}

	public String getKenmerkNamen()
	{
		return StringUtil.toString(getKenmerken(), "", new StringConverter<MedewerkerKenmerk>()
		{

			@Override
			public String getSeparator(int listIndex)
			{
				return ",";
			}

			@Override
			public String toString(MedewerkerKenmerk object, int listIndex)
			{
				return object.getKenmerk().getNaam();
			}

		});
	}

	public String getKenmerkCategorien()
	{
		return StringUtil.toString(getKenmerken(), "", new StringConverter<MedewerkerKenmerk>()
		{

			@Override
			public String getSeparator(int listIndex)
			{
				return ",";
			}

			@Override
			public String toString(MedewerkerKenmerk object, int listIndex)
			{
				return object.getKenmerk().getCategorie().getNaam();
			}

		});
	}

	public String getKenmerkToelichtingen()
	{
		return StringUtil.toString(getKenmerken(), "", new StringConverter<MedewerkerKenmerk>()
		{

			@Override
			public String getSeparator(int listIndex)
			{
				return ",";
			}

			@Override
			public String toString(MedewerkerKenmerk object, int listIndex)
			{
				return object.getToelichting();
			}

		});
	}

	public String getBegindatumKenmerken()
	{
		return StringUtil.toString(getKenmerken(), "", new StringConverter<MedewerkerKenmerk>()
		{

			@Override
			public String getSeparator(int listIndex)
			{
				return ",";
			}

			@Override
			public String toString(MedewerkerKenmerk object, int listIndex)
			{
				return object.getBegindatumFormatted();
			}

		});
	}

	public String getEinddatumKenmerken()
	{
		return StringUtil.toString(getKenmerken(), "", new StringConverter<MedewerkerKenmerk>()
		{

			@Override
			public String getSeparator(int listIndex)
			{
				return ",";
			}

			@Override
			public String toString(MedewerkerKenmerk object, int listIndex)
			{
				String res = object.getEinddatumFormatted();
				return res == null ? "geen" : res;
			}

		});
	}

	@Override
	public Medewerker getMedewerker()
	{
		return this;
	}

	/**
	 * Deze funtie haalt het totaal aantal uren op dat deze medewerker gekoppeld is aan
	 * stage/bpv overeenkomsten.
	 */
	public int getBPVBegeleidingsUrenSom()
	{
		return 0;
	}
}
