package nl.topicus.eduarte.entities.inschrijving;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.cobra.web.components.form.AutoFormValidator;
import nl.topicus.cobra.web.components.form.FieldContainerType;
import nl.topicus.cobra.web.components.labels.DatumTijdLabel;
import nl.topicus.cobra.web.components.text.DatumTijdField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.LocatieAdres;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.vrijevelden.IntakegesprekVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.web.components.choice.UitkomstIntakegesprekCombobox;
import nl.topicus.eduarte.web.components.panels.ExterneOrganisatiePanel;

import org.apache.wicket.markup.html.basic.Label;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author hop
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Exportable
public class Intakegesprek extends InstellingEntiteit implements DeelnemerProvider,
		VerbintenisProvider, VrijVeldable<IntakegesprekVrijVeld>, OrganisatieEenheidLocatieProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "verbintenis")
	@Index(name = "idx_Intakegesprek_verbintenis")
	@AutoFormEmbedded
	private Verbintenis verbintenis;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	@AutoForm(displayClass = DatumTijdLabel.class, editorClass = DatumTijdField.class, label = "Datum/Tijd", editorContainer = FieldContainerType.DIV)
	private Date datumTijd;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "intaker", nullable = true)
	@Index(name = "idx_Intakegesprek_intaker")
	@AutoForm(htmlClasses = "unit_max")
	private Medewerker intaker;

	@Column(nullable = true)
	@AutoForm(htmlClasses = "unit_max", label = "Intaker-omschrijving")
	private String intakerOverig;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_Intakegesprek_orgehd")
	@AutoForm(htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_Intakegesprek_locatie")
	@AutoForm(htmlClasses = "unit_max")
	private Locatie locatie;

	@Column(nullable = true)
	private String kamer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gewensteOpleiding", nullable = true)
	@Index(name = "idx_Intakegesprek_g_opleiding")
	@AutoForm(displayClass = Label.class, htmlClasses = "unit_max")
	private Opleiding gewensteOpleiding;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gewensteGroep", nullable = true)
	@Index(name = "idx_Intakegesprek_g_groep")
	@AutoForm(displayClass = Label.class, htmlClasses = "unit_max")
	private Groep gewensteGroep;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gewensteLocatie", nullable = true)
	@Index(name = "idx_Intakegesprek_g_locatie")
	@AutoForm(htmlClasses = "unit_max")
	private Locatie gewensteLocatie;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	@AutoForm(validators = @AutoFormValidator(formValidator = BegindatumVoorEinddatumValidator.class, otherProperties = "gewensteEinddatum"))
	private Date gewensteBegindatum;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date gewensteEinddatum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gewensteBPV", nullable = true)
	@Index(name = "idx_intake_bpvBedrijf")
	@AutoForm(label = "Gewenste BPV", displayClass = ExterneOrganisatiePanel.class, htmlClasses = "unit_max")
	private ExterneOrganisatie gewensteBPV;

	/**
	 * Veld is toegevoegd voor digitaal aanmelden als deelnemer een BBL opleiding kiest
	 * moet dit ingevuld kunnen worden, dit moet door medewerker worden omgezet in BPV.
	 */
	@Column(nullable = true, length = 100)
	private String plaatsWerkgever;

	/**
	 * Veld is toegevoegd voor digitaal aanmelden als deelnemer een BBL opleiding kiest
	 * moet dit ingevuld kunnen worden, dit moet door medewerker worden omgezet in BPV.
	 */
	@Column(nullable = true, length = 100)
	private String naamWerkgever;

	/**
	 * Veld is toegevoegd voor digitaal aanmelden als deelnemer een BBL opleiding kiest
	 * moet dit ingevuld kunnen worden, dit moet door medewerker worden omgezet in BPV.
	 */
	@Column(nullable = true, length = 100)
	private String contactpersoonWerkgever;

	public static enum IntakegesprekStatus
	{
		/**
		 * Gesprek moet nog worden gepland
		 */
		NogNietGepland,
		/**
		 * Gesprek moet nog worden uitgevoerd
		 */
		Uitvoeren,

		/**
		 * Gesprek is uitgevoerd
		 */
		Uitgevoerd,

		/**
		 * Gesprek vindt niet meer plaats
		 */
		Geannuleerd;

		@Override
		public String toString()
		{
			return StringUtil.convertCamelCase(name());
		}
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private IntakegesprekStatus status = IntakegesprekStatus.NogNietGepland;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uitkomstIntakegesprek", nullable = true)
	@AutoForm(editorClass = UitkomstIntakegesprekCombobox.class, editorContainer = FieldContainerType.SELECT, htmlClasses = "unit_max")
	@Index(name = "idx_intakegesprek_uitkomst")
	private UitkomstIntakegesprek uitkomst;

	@Lob
	@Column(nullable = true)
	@AutoForm(htmlClasses = "unit_250")
	private String opmerking;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "intakegesprek")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<IntakegesprekVrijVeld> vrijVelden;

	public Intakegesprek()
	{
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	@Exportable
	public Date getDatumTijd()
	{
		return datumTijd;
	}

	public void setDatumTijd(Date datumTijd)
	{
		this.datumTijd = datumTijd;
	}

	public String getDatumTijdFormatted()
	{
		return TimeUtil.getInstance().formatDateTime(getDatumTijd());
	}

	@Exportable
	public Medewerker getIntaker()
	{
		return intaker;
	}

	public void setIntaker(Medewerker intaker)
	{
		this.intaker = intaker;
	}

	@Exportable
	public Locatie getLocatie()
	{
		return locatie;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	@Exportable
	public String getKamer()
	{
		return kamer;
	}

	public void setKamer(String kamer)
	{
		this.kamer = kamer;
	}

	public IntakegesprekStatus getStatus()
	{
		return status;
	}

	public void setStatus(IntakegesprekStatus status)
	{
		this.status = status;
	}

	public void setUitkomst(UitkomstIntakegesprek uitkomst)
	{
		this.uitkomst = uitkomst;
	}

	@Exportable
	public UitkomstIntakegesprek getUitkomst()
	{
		return uitkomst;
	}

	public void setOpmerking(String opmerking)
	{
		this.opmerking = opmerking;
	}

	@Exportable
	public String getOpmerking()
	{
		return opmerking;
	}

	public Opleiding getGewensteOpleiding()
	{
		return gewensteOpleiding;
	}

	public void setGewensteOpleiding(Opleiding gewensteOpleiding)
	{
		this.gewensteOpleiding = gewensteOpleiding;
	}

	@Exportable
	public Locatie getGewensteLocatie()
	{
		return gewensteLocatie;
	}

	public void setGewensteLocatie(Locatie gewensteLocatie)
	{
		this.gewensteLocatie = gewensteLocatie;
	}

	@Exportable
	public Date getGewensteBegindatum()
	{
		return gewensteBegindatum;
	}

	public String getGewensteBegindatumNL()
	{
		if (getGewensteBegindatum() != null)
		{
			return TimeUtil.getInstance().formatDate(getGewensteBegindatum());
		}
		return "";
	}

	public void setGewensteBegindatum(Date gewensteBegindatum)
	{
		this.gewensteBegindatum = gewensteBegindatum;
	}

	@Exportable
	public Date getGewensteEinddatum()
	{
		return gewensteEinddatum;
	}

	public void setGewensteEinddatum(Date gewensteEinddatum)
	{
		this.gewensteEinddatum = gewensteEinddatum;
	}

	@Override
	public Deelnemer getDeelnemer()
	{
		return verbintenis.getDeelnemer();
	}

	@Override
	public List<IntakegesprekVrijVeld> getVrijVelden()
	{
		if (vrijVelden == null)
			vrijVelden = new ArrayList<IntakegesprekVrijVeld>();

		return vrijVelden;
	}

	@Override
	public List<IntakegesprekVrijVeld> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<IntakegesprekVrijVeld> res = new ArrayList<IntakegesprekVrijVeld>();
		for (IntakegesprekVrijVeld pvv : getVrijVelden())
		{
			if (pvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(pvv);
			}
		}
		return res;
	}

	@Override
	public IntakegesprekVrijVeld newVrijVeld()
	{
		IntakegesprekVrijVeld pvv = new IntakegesprekVrijVeld();
		pvv.setIntakegesprek(this);

		return pvv;
	}

	@Override
	public void setVrijVelden(List<IntakegesprekVrijVeld> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}

	@Override
	public String toString()
	{
		return verbintenis.toString();
	}

	@Exportable
	@Override
	public String getVrijVeldWaarde(String naam)
	{
		for (IntakegesprekVrijVeld vrijVeld : vrijVelden)
			if (vrijVeld.getVrijVeld().getNaam().equals(naam))
				return vrijVeld.getOmschrijving();
		return null;
	}

	public void setIntakerOverig(String intakerOverig)
	{
		this.intakerOverig = intakerOverig;
	}

	@Exportable
	public String getIntakerOverig()
	{
		return intakerOverig;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	@Exportable
	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	@Exportable
	public ExterneOrganisatie getGewensteBPV()
	{
		return gewensteBPV;
	}

	public void setGewensteBPV(ExterneOrganisatie gewensteBPV)
	{
		this.gewensteBPV = gewensteBPV;
	}

	public String getPlaatsWerkgever()
	{
		return plaatsWerkgever;
	}

	public void setPlaatsWerkgever(String plaatsWerkgever)
	{
		this.plaatsWerkgever = plaatsWerkgever;
	}

	public String getNaamWerkgever()
	{
		return naamWerkgever;
	}

	public void setNaamWerkgever(String naamWerkgever)
	{
		this.naamWerkgever = naamWerkgever;
	}

	public String getContactpersoonWerkgever()
	{
		return contactpersoonWerkgever;
	}

	public void setContactpersoonWerkgever(String contactpersoonWerkgever)
	{
		this.contactpersoonWerkgever = contactpersoonWerkgever;
	}

	public void setGewensteGroep(Groep gewensteGroep)
	{
		this.gewensteGroep = gewensteGroep;
	}

	public Groep getGewensteGroep()
	{
		return gewensteGroep;
	}

	@Exportable
	public Adres getLocatieAdres()
	{
		List<LocatieAdres> list = getLocatie().getFactuurAdressenOpPeildatum();
		return list.get(0).getAdres();
	}
}