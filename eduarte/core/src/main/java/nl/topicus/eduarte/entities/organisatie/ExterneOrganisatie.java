/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.util.StringUtil.StringConverter;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.entities.*;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;
import nl.topicus.eduarte.entities.adres.AdresseerbaarUtil;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumLandelijkOfInstellingEntiteit;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaExterneOrganisatie;
import nl.topicus.eduarte.entities.kenmerk.ExterneOrganisatieKenmerk;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.entities.vrijevelden.ExterneOrganisatieVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.web.components.choice.SoortExterneOrganisatieCombobox;
import nl.topicus.eduarte.web.components.quicksearch.externeorganisatie.ExterneOrganisatieSearchEditor;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.collection.PersistentBag;

/**
 * Externe organisatie.
 * 
 * @author idserda
 */
@Exportable
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@IsViewWhenOnNoise
public class ExterneOrganisatie extends BeginEinddatumLandelijkOfInstellingEntiteit implements
		Adresseerbaar<ExterneOrganisatieAdres>, Contacteerbaar<ExterneOrganisatieContactgegeven>,
		IContextInfoObject, VrijVeldable<ExterneOrganisatieVrijVeld>, Debiteur,
		IBijlageKoppelEntiteit<ExterneOrganisatieBijlage>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Status van vergelijking van interne gegevens met externe service.
	 */
	public enum ControleResultaat
	{
		ONBEKEND,
		GELIJK,
		VERSCHILLEND;
	}

	@Column(length = 100, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(length = 50, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String verkorteNaam;

	@BatchSize(size = 100)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "externeOrganisatie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy(value = "begindatum DESC")
	private List<ExterneOrganisatieAdres> adressen = new ArrayList<ExterneOrganisatieAdres>();

	@BatchSize(size = 100)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "externeOrganisatie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy(value = "volgorde")
	private List<ExterneOrganisatieContactgegeven> contactgegevens;

	@BatchSize(size = 100)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "externeOrganisatie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy(value = "begindatum")
	private List<ExterneOrganisatieContactPersoon> contactPersonen;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "externeOrganisatie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BPVBedrijfsgegeven> bpvBedrijfsgegevens = new ArrayList<BPVBedrijfsgegeven>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "soortExterneOrganisatie", nullable = true)
	@Index(name = "idx_ExtOrg_SoortExtOrg")
	@AutoForm(editorClass = SoortExterneOrganisatieCombobox.class, required = true, htmlClasses = "unit_max")
	private SoortExterneOrganisatie soortExterneOrganisatie;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "externeOrganisatie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<ExterneOrganisatieVrijVeld> vrijVelden;

	@Column(nullable = true)
	@AutoForm(htmlClasses = "unit_max", readOnly = true)
	private Long debiteurennummer;

	@Column(length = 11, nullable = true)
	@AutoForm(label = "Rekeningnummer")
	private String bankrekeningnummer;

	@Column(nullable = true)
	private Date laatsteExportDatum;

	@Column(nullable = false)
	private boolean bpvBedrijf;

	@Column(nullable = true)
	@Lob
	@AutoForm(htmlClasses = "unit_max")
	private String omschrijving;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ondertekeningBPVOdoor", nullable = true)
	@Index(name = "idx_ExtOrg_ondertBPVO")
	@AutoForm(editorClass = ExterneOrganisatieSearchEditor.class, label = "Ondertekening BPVO door", description = "Geeft aan dat een andere organisatie (bijv. holding) verantwoordelijk is voor de ondertekening van BPV-overeenkomsten gesloten met deze organisatie.", htmlClasses = "unit_max")
	private ExterneOrganisatie ondertekeningBPVOdoor;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "externeOrganisatie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<ExterneOrganisatieKenmerk> kenmerken = new ArrayList<ExterneOrganisatieKenmerk>();

	@AutoForm(description = "Geeft aan of dit bedrijf verzamelfacturen wil ontvangen voor meerdere deelnemers.")
	private boolean verzamelfacturen = true;

	@Column(nullable = true)
	@AutoForm(description = "Specifieke betalingstermijn voor deze debiteur (in dagen).", label = "Betalingstermijn")
	private Integer betalingstermijn;

	/**
	 * Geeft aan of dit veld nog gecontroleerd moet worden dmv. webservice.
	 */
	@Column(nullable = true)
	private Boolean nogControleren;

	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private ControleResultaat controleResultaat;

	/**
	 * Niet geschikt voor BPV-deelnemers: het kan voorkomen dat een onderwijsinstelling
	 * naar een bepaald BPV-bedrijf liever geen deelnemers stuurt. Dit kan worden
	 * aangegeven door de checkbox „Niet geschikt voor BPV-deelnemers‟ aan te vinken. In
	 * het tekstveld bij „Toelichting‟ kan een verklaring hiervoor worden ingevoerd.
	 * Wanneer deze vink bij een BPV-bedrijf aanstaat, wordt tijdens het matchen bij
	 * leerplaatsen van dit bedrijf een waarschuwingsicoontje getoond. Wanneer de muis
	 * boven het icoontje wordt gehouden, verschijnt een popup met de toelichting, indien
	 * deze aanwezig is. De gebruiker kan ervoor kiezen om toch een deelnemer aan het
	 * bedrijf te matchen.
	 */
	@Column(nullable = true)
	@AutoForm(label = "Niet geschikt voor BPV deelnemers")
	private Boolean nietGeschiktVoorBPVDeelnemers;

	@Column(nullable = true)
	@Lob
	@AutoForm(htmlClasses = "unit_max", label = "Toelichting niet geschikt")
	private String toelichtingNietGeschiktVoorBPV;

	@BatchSize(size = 100)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "externeOrganisatie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<ExterneOrganisatiePraktijkbegeleider> praktijkbegeleiders =
		new ArrayList<ExterneOrganisatiePraktijkbegeleider>();

	@BatchSize(size = 100)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "externeOrganisatie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("datum")
	private List<ExterneOrganisatieOpmerking> opmerkingen =
		new ArrayList<ExterneOrganisatieOpmerking>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "externeOrganisatie")
	private List<ExterneOrganisatieBijlage> bijlagen = new ArrayList<ExterneOrganisatieBijlage>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "externeOrganisatie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BPVCriteriaExterneOrganisatie> bpvCriteria =
		new ArrayList<BPVCriteriaExterneOrganisatie>();

	/**
	 * Niet geschikt voor BPV-match: instellingen kunnen bij BPV bedrijven ervoor kiezen
	 * dat de stageplaatsen van een bedrijf niet meegenomen worden in de matching
	 * procedures. Op deze manier kunnen de stages van bepaalde bedrijven exclusief
	 * gehouden worden en enkel door de instelling aan een kandidaat vergeven worden.
	 */
	@Column(nullable = true)
	@AutoForm(label = "Niet geschikt voor stage/BPV matching", description = "Stage/BPV-plaatsen van dit bedrijf zijn niet bedoeld om mee te nemen in de matching procedure.")
	private Boolean nietGeschiktVoorBPVMatch;

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private Betaalwijze factuurBetaalwijze;

	public ExterneOrganisatie()
	{
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	public void setVerkorteNaam(String verkorteNaam)
	{
		this.verkorteNaam = verkorteNaam;
	}

	@Exportable
	public String getVerkorteNaam()
	{
		return verkorteNaam;
	}

	/**
	 * @param externeOrganisatieContactPersonen
	 *            The extereneOrganisatieContactPersonen to set.
	 */
	public void setContactPersonen(
			List<ExterneOrganisatieContactPersoon> externeOrganisatieContactPersonen)
	{
		this.contactPersonen = externeOrganisatieContactPersonen;
	}

	/**
	 * <p>
	 * Dit is zo omdat we nooit bij de originele lijst komen en alle wijzigingen die we
	 * willen doen nooit opgeslagen gaan zijn. Men mag niet alle contactpersonen bekijken
	 * omdat dit ook contactpersonen kunnen zijn van andere instellingen.
	 * </p>
	 * 
	 * @return Returns the ExterneOrganisatieContactPersonen. Geeft alleen de instelling
	 *         specifieke en landelijk contactpersonen. Add en Remove op deze lijst
	 *         wijzigt ook de onderliggende {@link PersistentBag}.
	 */
	@Exportable
	public List<ExterneOrganisatieContactPersoon> getContactPersonen()
	{
		if (contactPersonen == null)
			contactPersonen = new ArrayList<ExterneOrganisatieContactPersoon>();

		return new InstellingsSpecifiekeLijst<ExterneOrganisatieContactPersoon>(contactPersonen);
	}

	/**
	 * Lijst die een (gedeeltelijke) kopie is van een andere lijst. Wijzigingen via add,
	 * remove en set worden doorgezet naar de originele lijst. Nodig om een
	 * instellingsspecifieke lijst contactpersonen etc. op te leveren met read-write
	 * access.
	 */
	private final class InstellingsSpecifiekeLijst<T extends Entiteit> extends ArrayList<T>
	{
		private static final long serialVersionUID = 1L;

		private List<T> original;

		public InstellingsSpecifiekeLijst(List<T> original)
		{
			this.original = original;
			List<T> subList = new ArrayList<T>();

			for (T object : original)
			{
				if (object instanceof IInstellingEntiteit)
					if (((IInstellingEntiteit) object).getOrganisatie().getId().equals(
						EduArteContext.get().getInstelling().getId()))
						subList.add(object);

				if (object instanceof LandelijkOfInstellingEntiteit)
				{
					Instelling organisatie =
						((LandelijkOfInstellingEntiteit) object).getOrganisatie();
					if (organisatie == null
						|| organisatie.getId().equals(EduArteContext.get().getInstelling().getId()))
						subList.add(object);
				}
			}
			addAll(subList); // wordt niet doorgezet naar origineel
		}

		@Override
		public boolean add(T e)
		{
			original.add(e);
			return super.add(e);
		}

		@Override
		public void add(int index, T element)
		{
			original.add(index, element);
			super.add(index, element);
		}

		@Override
		public T remove(int index)
		{
			original.remove(index);
			return super.remove(index);
		}

		@Override
		public boolean remove(Object o)
		{
			original.remove(o);
			return super.remove(o);
		}

		@Override
		public T set(int index, T element)
		{
			original.set(index, element);
			return super.set(index, element);
		}
	}

	public void addContactPersoon(ExterneOrganisatieContactPersoon persoon)
	{
		getContactPersonen().add(persoon);
	}

	public void setDebiteurennummer(Long debiteurennummer)
	{
		this.debiteurennummer = debiteurennummer;
	}

	@Exportable
	public Long getDebiteurennummer()
	{
		return debiteurennummer;
	}

	@Override
	public List<ExterneOrganisatieAdres> getAdressen()
	{
		return adressen;
	}

	@Override
	public void setAdressen(List<ExterneOrganisatieAdres> adressen)
	{
		this.adressen = adressen;
	}

	@Override
	public List<ExterneOrganisatieAdres> getAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createPeildatumFilter());
	}

	@Override
	public List<ExterneOrganisatieAdres> getFysiekAdressen()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createFysiekadresFilter());
	}

	@Override
	public List<ExterneOrganisatieAdres> getFysiekAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil
			.createFysiekadresOpPeildatumFilter());
	}

	@Override
	public List<ExterneOrganisatieAdres> getPostAdressen()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createPostadresFilter());
	}

	@Override
	public List<ExterneOrganisatieAdres> getPostAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil
			.createPostadresOpPeildatumFilter());
	}

	@Override
	public List<ExterneOrganisatieAdres> getFactuurAdressen()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createFactuuradresFilter());
	}

	@Override
	public List<ExterneOrganisatieAdres> getFactuurAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil
			.createFactuuradresOpPeildatumFilter());
	}

	@Exportable
	@Override
	public ExterneOrganisatieAdres getFysiekAdres()
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFysiekadresOpPeildatumFilter());
	}

	@Override
	public ExterneOrganisatieAdres getFysiekAdres(Date peildatum)
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFysiekadresOpPeildatumFilter(peildatum));
	}

	@Exportable
	@Override
	public ExterneOrganisatieAdres getPostAdres()
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createPostadresOpPeildatumFilter());
	}

	@Override
	public ExterneOrganisatieAdres getPostAdres(Date peildatum)
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createPostadresOpPeildatumFilter(peildatum));
	}

	@Exportable
	@Override
	public ExterneOrganisatieAdres getFactuurAdres()
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFactuuradresOpPeildatumFilter());
	}

	@Override
	public ExterneOrganisatieAdres getFactuurAdres(Date peildatum)
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFactuuradresOpPeildatumFilter(peildatum));
	}

	@Override
	public ExterneOrganisatieAdres newAdres()
	{
		ExterneOrganisatieAdres nieuwAdres = new ExterneOrganisatieAdres();
		nieuwAdres.setBegindatum(TimeUtil.getInstance().currentDate());
		nieuwAdres.setExterneOrganisatie(this);
		return nieuwAdres;
	}

	@Override
	public String getFysiekAdresOmschrijving()
	{
		return "Bezoekadres";
	}

	@Override
	public String getPostAdresOmschrijving()
	{
		return "Postadres";
	}

	@Override
	public String getFactuurAdresOmschrijving()
	{
		return "Factuuradres";
	}

	@Exportable
	public SoortExterneOrganisatie getSoortExterneOrganisatie()
	{
		return soortExterneOrganisatie;
	}

	public void setSoortExterneOrganisatie(SoortExterneOrganisatie soortExterneOrganisatie)
	{
		this.soortExterneOrganisatie = soortExterneOrganisatie;
	}

	@Override
	public List<ExterneOrganisatieContactgegeven> getContactgegevens()
	{
		if (contactgegevens == null)
			contactgegevens = new ArrayList<ExterneOrganisatieContactgegeven>();

		return new InstellingsSpecifiekeLijst<ExterneOrganisatieContactgegeven>(contactgegevens);
	}

	@Override
	public void setContactgegevens(List<ExterneOrganisatieContactgegeven> contactgegevens)
	{
		this.contactgegevens = contactgegevens;
	}

	@Override
	public ExterneOrganisatieContactgegeven newContactgegeven()
	{
		ExterneOrganisatieContactgegeven gegeven = new ExterneOrganisatieContactgegeven();
		gegeven.setExterneOrganisatie(this);
		gegeven.setVolgorde(getContactgegevens().size());

		return gegeven;
	}

	/**
	 * @see nl.topicus.eduarte.entities.ContacteerbaarUtil#getEersteTelefoon(Contacteerbaar)
	 */
	public List<ExterneOrganisatieContactgegeven> getContactgegevens(SoortContactgegeven soort)
	{
		return ContacteerbaarUtil.getContactgegevens(this, soort);
	}

	/**
	 * @see nl.topicus.eduarte.entities.ContacteerbaarUtil#getEersteEmailAdres(Contacteerbaar)
	 */
	@AutoForm(label = "Eerste e-mailadres")
	@AutoFormEmbedded
	@Exportable
	public ExterneOrganisatieContactgegeven getEersteEmailAdres()
	{
		return ContacteerbaarUtil.getEersteEmailAdres(this);
	}

	/**
	 * @see nl.topicus.eduarte.entities.ContacteerbaarUtil#getEersteHomepage(Contacteerbaar)
	 */
	@AutoForm
	@AutoFormEmbedded
	@Exportable
	public ExterneOrganisatieContactgegeven getEersteHomepage()
	{
		return ContacteerbaarUtil.getEersteHomepage(this);
	}

	/**
	 * @see nl.topicus.eduarte.entities.ContacteerbaarUtil#getEersteMobieltelefoon(Contacteerbaar)
	 */
	@AutoForm
	@AutoFormEmbedded
	@Exportable
	public ExterneOrganisatieContactgegeven getEersteMobieltelefoon()
	{
		return ContacteerbaarUtil.getEersteMobieltelefoon(this);
	}

	/**
	 * @see nl.topicus.eduarte.entities.ContacteerbaarUtil#getEersteOverig(Contacteerbaar)
	 */
	@AutoForm
	@AutoFormEmbedded
	@Exportable
	public ExterneOrganisatieContactgegeven getEersteOverig()
	{
		return ContacteerbaarUtil.getEersteOverig(this);
	}

	/**
	 * @see nl.topicus.eduarte.entities.Contacteerbaar#getEersteTelefoon()
	 */
	@AutoForm
	@AutoFormEmbedded
	@Exportable
	public ExterneOrganisatieContactgegeven getEersteTelefoon()
	{
		return ContacteerbaarUtil.getEersteTelefoon(this);
	}

	@Override
	public String getContextInfoOmschrijving()
	{
		return getNaam();
	}

	public void setBpvBedrijfsgegevens(List<BPVBedrijfsgegeven> bpvBedrijfsgegevens)
	{
		this.bpvBedrijfsgegevens = bpvBedrijfsgegevens;
	}

	public List<BPVBedrijfsgegeven> getBpvBedrijfsgegevens()
	{
		if (bpvBedrijfsgegevens == null)
			bpvBedrijfsgegevens = new ArrayList<BPVBedrijfsgegeven>();

		return new InstellingsSpecifiekeLijst<BPVBedrijfsgegeven>(bpvBedrijfsgegevens);
	}

	@Override
	public String toString()
	{
		return getNaamMetFysiekAdres();
	}

	@Exportable
	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public void setBpvBedrijf(boolean bpvBedrijf)
	{
		this.bpvBedrijf = bpvBedrijf;
	}

	public boolean isBpvBedrijf()
	{
		return bpvBedrijf;
	}

	@Exportable
	@Override
	public List<ExterneOrganisatieVrijVeld> getVrijVelden()
	{
		if (vrijVelden == null)
			vrijVelden = new ArrayList<ExterneOrganisatieVrijVeld>();

		return new InstellingsSpecifiekeLijst<ExterneOrganisatieVrijVeld>(vrijVelden);
	}

	public String getBPVBedrijvenKenniscentrumFormatted()
	{
		return StringUtil.toString(getBpvBedrijfsgegevens(), "",
			new StringUtil.StringConverter<BPVBedrijfsgegeven>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(BPVBedrijfsgegeven object, int listIndex)
				{
					return object.getKenniscentrum().toString();
				}
			});
	}

	public String getBPVBedrijvenCodeLeerbedrijfFormatted()
	{
		return StringUtil.toString(getBpvBedrijfsgegevens(), "",
			new StringUtil.StringConverter<BPVBedrijfsgegeven>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(BPVBedrijfsgegeven object, int listIndex)
				{
					return object.getCodeLeerbedrijf();
				}
			});
	}

	@Exportable
	public String getNaamMetFysiekAdres()
	{
		StringBuilder builder = new StringBuilder(50);
		builder.append(getNaam());

		ExterneOrganisatieAdres adres = getFysiekAdres();
		if (adres != null)
		{
			builder.append(", ").append(adres.getAdres().getVolledigAdresOp1Regel());
		}

		return builder.toString();
	}

	@Override
	public List<ExterneOrganisatieVrijVeld> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<ExterneOrganisatieVrijVeld> res = new ArrayList<ExterneOrganisatieVrijVeld>();
		for (ExterneOrganisatieVrijVeld pvv : getVrijVelden())
		{
			if (pvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(pvv);
			}
		}
		return res;
	}

	@Override
	public ExterneOrganisatieVrijVeld newVrijVeld()
	{
		ExterneOrganisatieVrijVeld pvv = new ExterneOrganisatieVrijVeld();
		pvv.setExterneOrganisatie(this);

		return pvv;
	}

	@Override
	public void setVrijVelden(List<ExterneOrganisatieVrijVeld> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}

	public void setOndertekeningBPVOdoor(ExterneOrganisatie ondertekeningBPVOdoor)
	{
		this.ondertekeningBPVOdoor = ondertekeningBPVOdoor;
	}

	@Exportable
	public ExterneOrganisatie getOndertekeningBPVOdoor()
	{
		return ondertekeningBPVOdoor;
	}

	public void setBankrekeningnummer(String bankrekeningnummer)
	{
		this.bankrekeningnummer = bankrekeningnummer;
	}

	@Exportable
	public String getBankrekeningnummer()
	{
		return bankrekeningnummer;
	}

	@Exportable
	@Override
	public String getVrijVeldWaarde(String vrijVeldNaam)
	{
		for (ExterneOrganisatieVrijVeld vrijVeld : getVrijVelden())
			if (vrijVeld.getVrijVeld().getNaam().equals(vrijVeldNaam))
				return vrijVeld.getOmschrijving();
		return null;
	}

	@Override
	public Date getLaatsteExportDatum()
	{
		return laatsteExportDatum;
	}

	@Override
	public void setLaatsteExportDatum(Date laatsteExportDatum)
	{
		this.laatsteExportDatum = laatsteExportDatum;
	}

	@Override
	@Exportable
	public String getFormeleNaam()
	{
		return naam;
	}

	@Override
	public AutomatischeIncasso getAutomatischeIncasso()
	{
		return null;
	}

	@Override
	public Date getAutomatischeIncassoEinddatum()
	{
		return null;
	}

	@Override
	public char getBetaalwijze()
	{
		return 'A';
	}

	@Override
	public String getBankrekeningTenaamstelling()
	{
		return getOmschrijving();
	}

	public List<ExterneOrganisatieKenmerk> getKenmerken()
	{
		return kenmerken;
	}

	public void setKenmerken(List<ExterneOrganisatieKenmerk> kenmerken)
	{
		this.kenmerken = kenmerken;
	}

	public String getKenmerkNamen()
	{
		return StringUtil.toString(getKenmerken(), "",
			new StringConverter<ExterneOrganisatieKenmerk>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(ExterneOrganisatieKenmerk object, int listIndex)
				{
					return object.getKenmerk().getNaam();
				}

			});
	}

	public String getKenmerkCategorien()
	{
		return StringUtil.toString(getKenmerken(), "",
			new StringConverter<ExterneOrganisatieKenmerk>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(ExterneOrganisatieKenmerk object, int listIndex)
				{
					return object.getKenmerk().getCategorie().getNaam();
				}

			});
	}

	public String getKenmerkToelichtingen()
	{
		return StringUtil.toString(getKenmerken(), "",
			new StringConverter<ExterneOrganisatieKenmerk>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(ExterneOrganisatieKenmerk object, int listIndex)
				{
					return object.getToelichting();
				}

			});
	}

	public String getBegindatumKenmerken()
	{
		return StringUtil.toString(getKenmerken(), "",
			new StringConverter<ExterneOrganisatieKenmerk>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(ExterneOrganisatieKenmerk object, int listIndex)
				{
					return object.getBegindatumFormatted();
				}

			});
	}

	public String getEinddatumKenmerken()
	{
		return StringUtil.toString(getKenmerken(), "",
			new StringConverter<ExterneOrganisatieKenmerk>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(ExterneOrganisatieKenmerk object, int listIndex)
				{
					String res = object.getEinddatumFormatted();
					return res == null ? "geen" : res;
				}

			});
	}

	@Override
	public String getContactpersoon()
	{
		List<ExterneOrganisatieContactPersoon> personen = getContactPersonen();
		if (personen.size() == 1)
			return personen.get(0).getNaam();

		return "Crediteurenadministratie";
	}

	@Exportable
	@Override
	public ExterneOrganisatieContactPersoon getContactpersoonMetRol(String rol)
	{
		for (ExterneOrganisatieContactPersoon persoon : getContactPersonen())
			if (persoon.getRol() != null && persoon.getRol().getNaam().equals(rol))
				return persoon;

		return null;
	}

	public void setVerzamelfacturen(boolean verzamelfacturen)
	{
		this.verzamelfacturen = verzamelfacturen;
	}

	@Override
	public boolean isVerzamelfacturen()
	{
		return verzamelfacturen;
	}

	public String getRelatienummers()
	{
		List<String> relnrs = new ArrayList<String>();
		for (BPVBedrijfsgegeven bpvbg : bpvBedrijfsgegevens)
		{
			if (bpvbg.getRelatienummer() != null)
				relnrs.add(bpvbg.getRelatienummer());
		}

		return StringUtil.maakCommaSeparatedString(relnrs);
	}

	@Override
	@Exportable
	public Integer getBetalingstermijn()
	{
		return betalingstermijn;
	}

	@Override
	public void setBetalingstermijn(Integer betalingstermijn)
	{
		this.betalingstermijn = betalingstermijn;
	}

	/**
	 * Controleer of deze ext.org. een debiteurennummer heeft die null is. Dit kan
	 * bijvoorbeeld gebeuren na een Noise import. Wanneer deze null is dan controleren we
	 * de settings en genereren we alsnog een debiteurnummer volgens protocol
	 */
	public void checkAndCreateDebiteurNummer()
	{
		if (debiteurennummer == null)
		{
			NummerGeneratorDataAccessHelper generator =
				DataAccessRegistry.getHelper(NummerGeneratorDataAccessHelper.class);
			setDebiteurennummer(generator.newOrganisatieDebiteurnummer());
		}
	}

	public Boolean isNogControleren()
	{
		return nogControleren;
	}

	public void setNogControleren(Boolean nogControleren)
	{
		this.nogControleren = nogControleren;
	}

	public ControleResultaat getControleResultaat()
	{
		return controleResultaat;
	}

	public void setControleResultaat(ControleResultaat controleResultaat)
	{
		this.controleResultaat = controleResultaat;
	}

	public Boolean getNietGeschiktVoorBPVDeelnemers()
	{
		return nietGeschiktVoorBPVDeelnemers;
	}

	public void setNietGeschiktVoorBPVDeelnemers(Boolean nietGeschiktVoorBPVDeelnemers)
	{
		this.nietGeschiktVoorBPVDeelnemers = nietGeschiktVoorBPVDeelnemers;
	}

	public String getToelichtingNietGeschiktVoorBPV()
	{
		return toelichtingNietGeschiktVoorBPV;
	}

	public void setToelichtingNietGeschiktVoorBPV(String toelichtingNietGeschiktVoorBPV)
	{
		this.toelichtingNietGeschiktVoorBPV = toelichtingNietGeschiktVoorBPV;
	}

	public List<ExterneOrganisatiePraktijkbegeleider> getPraktijkbegeleiders()
	{
		return praktijkbegeleiders;
	}

	public void setPraktijkbegeleiders(
			List<ExterneOrganisatiePraktijkbegeleider> praktijkbegeleiders)
	{
		this.praktijkbegeleiders = praktijkbegeleiders;
	}

	public void setOpmerkingen(List<ExterneOrganisatieOpmerking> opmerkingen)
	{
		this.opmerkingen = opmerkingen;
	}

	public List<ExterneOrganisatieOpmerking> getOpmerkingen()
	{
		return opmerkingen;
	}

	public List<ExterneOrganisatieBijlage> getBijlagen()
	{
		return bijlagen;
	}

	public void setBijlagen(List<ExterneOrganisatieBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
	}

	/**
	 * Verwijdert de gegeven bijlage van dit onderwijsproduct. Aanroeper is
	 * verantwoordelijk voor het committen.
	 * 
	 * @param bijlage
	 */
	public void deleteBijlage(Bijlage bijlage)
	{
		ExterneOrganisatieBijlage teVerwijderen = null;
		for (ExterneOrganisatieBijlage externeOrganisatieBijlage : getBijlagen())
		{
			if (externeOrganisatieBijlage.getBijlage().equals(bijlage))
			{
				teVerwijderen = externeOrganisatieBijlage;
				break;
			}
		}
		if (teVerwijderen != null)
		{
			getBijlagen().remove(teVerwijderen);
			teVerwijderen.delete();
		}
	}

	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (ExterneOrganisatieBijlage externeOrganisatieBijlage : getBijlagen())
		{
			if (externeOrganisatieBijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	public ExterneOrganisatieBijlage addBijlage(Bijlage bijlage)
	{
		ExterneOrganisatieBijlage newBijlage = new ExterneOrganisatieBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setExterneOrganisatie(this);
		getBijlagen().add(newBijlage);
		return newBijlage;
	}

	public void setBpvCriteria(List<BPVCriteriaExterneOrganisatie> bpvCriteria)
	{
		this.bpvCriteria = bpvCriteria;
	}

	public List<BPVCriteriaExterneOrganisatie> getBpvCriteria()
	{
		return bpvCriteria;
	}

	public void setNietGeschiktVoorBPVMatch(Boolean nietGeschiktVoorBPVMatch)
	{
		this.nietGeschiktVoorBPVMatch = nietGeschiktVoorBPVMatch;
	}

	public Boolean getNietGeschiktVoorBPVMatch()
	{
		return nietGeschiktVoorBPVMatch;
	}

	/**
	 * @return een bankrekeningnummer, niet zijnde een max-7-cijferig
	 *         ING-bankrekeningnummer
	 */
	@Override
	@Exportable
	public String getBankrekeningBank()
	{
		if (bankrekeningnummer == null || bankrekeningnummer.length() <= 7)
			return null;

		return bankrekeningnummer;
	}

	/**
	 * @return een max-7-cijferig ING-bankrekeningnummer, voorheen bekend als Girorekening
	 */
	@Override
	@Exportable
	public String getBankrekeningGiro()
	{
		if (bankrekeningnummer == null || bankrekeningnummer.length() > 7)
			return null;

		return bankrekeningnummer;
	}

	public void setFactuurBetaalwijze(Betaalwijze factuurBetaalwijze)
	{
		this.factuurBetaalwijze = factuurBetaalwijze;
	}

	@Override
	public Betaalwijze getFactuurBetaalwijze()
	{
		return factuurBetaalwijze;
	}

}