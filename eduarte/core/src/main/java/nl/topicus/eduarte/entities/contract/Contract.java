package nl.topicus.eduarte.entities.contract;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.*;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.FieldContainerType;
import nl.topicus.cobra.web.components.wiquery.DropDownCheckList;
import nl.topicus.cobra.web.components.wiquery.MoneyInputField;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.entities.vrijevelden.ContractVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.web.components.choice.SoortContractCombobox;
import nl.topicus.eduarte.web.components.choice.TypeFinancieringCombobox;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Contract
 * 
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
@Table(appliesTo = "Contract", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@Exportable
public class Contract extends BeginEinddatumInstellingEntiteit implements
		VrijVeldable<ContractVrijVeld>, IContextInfoObject
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 30)
	@Index(name = "idx_contract_code")
	@AutoForm(label = "Interne code")
	private String code;

	@Column(nullable = false, length = 100)
	@Index(name = "idx_contract_naam")
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = true)
	@Index(name = "idx_Contract_orgEhd")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@Lob
	private String toelichting;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externeOrganisatie", nullable = true)
	@Index(name = "idx_contract_extOrg")
	@AutoForm(htmlClasses = "unit_max")
	private ExterneOrganisatie externeOrganisatie;

	@Column(nullable = true, length = 20)
	private String externNummer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactPersoon", nullable = true)
	@Index(name = "idx_contract_contPers")
	@AutoForm(htmlClasses = "unit_max", label = "Externe contactpersoon")
	private ExterneOrganisatieContactPersoon contactPersoon;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "soortContract", nullable = false)
	@Index(name = "idx_contract_soortContract")
	@AutoForm(editorClass = SoortContractCombobox.class, editorContainer = FieldContainerType.SELECT, htmlClasses = "unit_max")
	private SoortContract soortContract;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "beheerder", nullable = true)
	@Index(name = "idx_contract_beheerder")
	@AutoForm(htmlClasses = "unit_max")
	private Medewerker beheerder;

	@Column(nullable = true, length = 30)
	@AutoForm(htmlClasses = "unit_max")
	private String aanwezigBij;

	@Column(nullable = true)
	private Integer minimumAantalDeelnemers;

	@Column(nullable = true)
	private Integer maximumAantalDeelnemers;

	@Column(nullable = true, scale = 2, precision = 19)
	@AutoForm(editorClass = MoneyInputField.class)
	private BigDecimal kostprijs;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeFinanciering", nullable = true)
	@Index(name = "idx_contract_typeFinanciering")
	@AutoForm(editorClass = TypeFinancieringCombobox.class, editorContainer = FieldContainerType.SELECT, htmlClasses = "unit_max")
	private TypeFinanciering typeFinanciering;

	@Column(nullable = false)
	private Date eindeInstroom;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinTable(name = "ContractLocatieKoppeling", joinColumns = {@JoinColumn(name = "contract_Id")}, inverseJoinColumns = {@JoinColumn(name = "locatie_Id")})
	@AutoForm(editorClass = DropDownCheckList.class, htmlClasses = "unit_max")
	private List<Locatie> locaties;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
	private List<ContractOnderdeel> contractOnderdelen;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
	private List<ContractVerplichting> contractVerplichtingen;

	/**
	 * Voor keurmerk inburgering moet onderscheid gemaakt worden tussen
	 * inburgeringscontracten in hoofd- en onderaanneming.
	 */
	public static enum Onderaanneming
	{
		/**
		 * Geheel onder eigen verantwoordelijkheid uitgevoerd
		 */
		Geen,
		/**
		 * Instelling is hoofdaannemer, andere partij voert de gehele inburgeringscursus
		 * uit
		 */
		GeheelUitbesteed,
		/**
		 * Instelling is hoofdaannemer, andere partij voert een deel van de
		 * inburgeringscursus uit
		 */
		GedeeltelijkUitbesteed,
		/**
		 * Andere partij is hoofdaannemer, instelling voert een deel van de
		 * inburgeringscursus uit
		 */
		InOnderaanneming;

		@Override
		public String toString()
		{
			return StringUtil.convertCamelCase(name());
		}
	}

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@AutoForm(editorClass = EnumCombobox.class, htmlClasses = "unit_max")
	private Onderaanneming onderaanneming = Onderaanneming.Geen;

	@Column(nullable = true, length = 50)
	@AutoForm(htmlClasses = "unit_max")
	private String onderaannemingBij;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<ContractVrijVeld> vrijVelden;

	public Contract()
	{
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public ExterneOrganisatie getExterneOrganisatie()
	{
		return externeOrganisatie;
	}

	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	public String getExternNummer()
	{
		return externNummer;
	}

	public void setExternNummer(String externNummer)
	{
		this.externNummer = externNummer;
	}

	public ExterneOrganisatieContactPersoon getContactPersoon()
	{
		return contactPersoon;
	}

	public void setContactPersoon(ExterneOrganisatieContactPersoon contactPersoon)
	{
		this.contactPersoon = contactPersoon;
	}

	public SoortContract getSoortContract()
	{
		return soortContract;
	}

	public void setSoortContract(SoortContract soortContract)
	{
		this.soortContract = soortContract;
	}

	public Medewerker getBeheerder()
	{
		return beheerder;
	}

	public void setBeheerder(Medewerker beheerder)
	{
		this.beheerder = beheerder;
	}

	public String getAanwezigBij()
	{
		return aanwezigBij;
	}

	public void setAanwezigBij(String aanwezigBij)
	{
		this.aanwezigBij = aanwezigBij;
	}

	@Exportable
	public Integer getMinimumAantalDeelnemers()
	{
		return minimumAantalDeelnemers;
	}

	public void setMinimumAantalDeelnemers(Integer minimumAantalDeelnemers)
	{
		this.minimumAantalDeelnemers = minimumAantalDeelnemers;
	}

	@Exportable
	public Integer getMaximumAantalDeelnemers()
	{
		return maximumAantalDeelnemers;
	}

	public void setMaximumAantalDeelnemers(Integer maximumAantalDeelnemers)
	{
		this.maximumAantalDeelnemers = maximumAantalDeelnemers;
	}

	@Exportable
	@AutoForm(label = "Kostprijs")
	public String getKostprijsEuro()
	{
		String currency = "";
		if (getKostprijs() != null)
		{
			NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("nl", "NL"));
			currency = format.format(getKostprijs());
		}
		return currency;
	}

	public BigDecimal getKostprijs()
	{
		return kostprijs;
	}

	public void setKostprijs(BigDecimal kostprijs)
	{
		this.kostprijs = kostprijs;
	}

	public TypeFinanciering getTypeFinanciering()
	{
		return typeFinanciering;
	}

	public void setTypeFinanciering(TypeFinanciering typeFinanciering)
	{
		this.typeFinanciering = typeFinanciering;
	}

	public Date getEindeInstroom()
	{
		return eindeInstroom;
	}

	public void setEindeInstroom(Date eindeInstroom)
	{
		this.eindeInstroom = eindeInstroom;
	}

	@Exportable
	public List<ContractOnderdeel> getContractOnderdelen()
	{
		if (contractOnderdelen == null)
			contractOnderdelen = new ArrayList<ContractOnderdeel>();

		return contractOnderdelen;
	}

	public void setContractOnderdelen(List<ContractOnderdeel> contractOnderdelen)
	{
		this.contractOnderdelen = contractOnderdelen;
	}

	public List<ContractVerplichting> getContractVerplichtingen()
	{
		if (contractVerplichtingen == null)
			contractVerplichtingen = new ArrayList<ContractVerplichting>();

		return contractVerplichtingen;
	}

	public void setContractVerplichtingen(List<ContractVerplichting> contractverplichtingen)
	{
		this.contractVerplichtingen = contractverplichtingen;
	}

	public void setOnderaanneming(Onderaanneming onderaanneming)
	{
		this.onderaanneming = onderaanneming;
	}

	public Onderaanneming getOnderaanneming()
	{
		return onderaanneming;
	}

	public String getOnderaannemingBij()
	{
		return onderaannemingBij;
	}

	public void setOnderaannemingBij(String onderaannemingBij)
	{
		this.onderaannemingBij = onderaannemingBij;
	}

	public String getToelichting()
	{
		return toelichting;
	}

	public void setToelichting(String toelichting)
	{
		this.toelichting = toelichting;
	}

	@Override
	public String toString()
	{
		String res = getCode() + " - " + getNaam();
		if (getEinddatum() != null
			|| (getBegindatum() != null && getBegindatum().after(
				EduArteApp.MIN_BEGINDATUM_VOOR_TOSTRING)))
		{
			res = res + " (";
			if (getBegindatum().after(EduArteApp.MIN_BEGINDATUM_VOOR_TOSTRING))
			{
				res = res + "vanaf " + TimeUtil.getInstance().formatDate(getBegindatum());
				if (getEinddatum() != null)
				{
					res = res + " ";
				}
			}
			if (getEinddatum() != null)
			{
				res = res + "tot " + TimeUtil.getInstance().formatDate(getEinddatum());
			}
			res = res + ")";
		}
		return res;
	}

	public List<Locatie> getLocaties()
	{
		if (locaties == null)
			locaties = new ArrayList<Locatie>();
		return locaties;
	}

	public void setLocaties(List<Locatie> locaties)
	{
		this.locaties = locaties;
	}

	public boolean isInburgering()
	{
		if (soortContract != null)
			return Boolean.TRUE.equals(soortContract.getInburgering());
		return false;
	}

	@Override
	public List<ContractVrijVeld> getVrijVelden()
	{
		if (vrijVelden == null)
			vrijVelden = new ArrayList<ContractVrijVeld>();

		return vrijVelden;
	}

	@Override
	public List<ContractVrijVeld> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<ContractVrijVeld> res = new ArrayList<ContractVrijVeld>();
		for (ContractVrijVeld cvv : getVrijVelden())
		{
			if (cvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(cvv);
			}
		}
		return res;
	}

	@Override
	public ContractVrijVeld newVrijVeld()
	{
		ContractVrijVeld cvv = new ContractVrijVeld();
		cvv.setContract(this);

		return cvv;
	}

	@Override
	public void setVrijVelden(List<ContractVrijVeld> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}

	@Exportable
	@Override
	public String getVrijVeldWaarde(String vrijVeldNaam)
	{
		for (ContractVrijVeld vrijVeld : vrijVelden)
			if (vrijVeld.getVrijVeld().getNaam().equals(vrijVeldNaam))
				return vrijVeld.getOmschrijving();
		return null;
	}

	@Override
	public String getContextInfoOmschrijving()
	{
		return getNaam();
	}

	@Exportable
	@Override
	public Date getEinddatum()
	{
		return super.getEinddatum();
	}

	@Exportable
	@Override
	public Date getBegindatum()
	{
		return super.getBegindatum();
	}
}
