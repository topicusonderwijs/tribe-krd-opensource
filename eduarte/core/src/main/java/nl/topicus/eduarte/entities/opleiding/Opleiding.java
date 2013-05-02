package nl.topicus.eduarte.entities.opleiding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.dao.helpers.CriteriumDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.hogeronderwijs.Hoofdfase;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingFase;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingsVorm;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.MBONiveau;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieEnum;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleiding;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleidingAanbod;
import nl.topicus.eduarte.entities.taxonomie.mbo.AbstractMBOVerbintenisgebied;
import nl.topicus.eduarte.entities.taxonomie.vo.Elementcode;
import nl.topicus.eduarte.entities.vrijevelden.OpleidingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.providers.OpleidingProvider;
import nl.topicus.eduarte.zoekfilters.CriteriumZoekFilter;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

import org.apache.wicket.RequestCycle;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Een opleiding die door een instelling aangeboden kan worden op 1 of meer
 * organisatie-eenheden.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@BatchSize(size = 20)
@Exportable
@Table(appliesTo = "Opleiding", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@IsViewWhenOnNoise
public class Opleiding extends BeginEinddatumInstellingEntiteit implements IContextInfoObject,
		IOrganisatieEenheidLocatieKoppelbaarEntiteit<OpleidingAanbod>,
		VrijVeldable<OpleidingVrijVeld>, OpleidingProvider,
		IBijlageKoppelEntiteit<OpleidingBijlage>
{
	private static final long serialVersionUID = 1L;

	private static final int CREDITS_PER_JAAR = 60;

	@Column(nullable = false, length = 20)
	@Index(name = "idx_Opleiding_code")
	private String code;

	@Column(nullable = false, length = 100)
	@Index(name = "idx_Opleiding_naam")
	@AutoForm(label = "Opleiding")
	private String naam;

	@Column(nullable = true, length = 100)
	private String internationaleNaam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "verbintenisgebied")
	@Index(name = "idx_Opleiding_verbgeb")
	private Verbintenisgebied verbintenisgebied;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "opleiding")
	@OrderBy(value = "locatie, organisatieEenheid")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<OpleidingAanbod> aanbod;

	@Column(nullable = true)
	private Integer beginLeerjaar;

	@Column(nullable = true)
	private Integer eindLeerjaar;

	@Column(nullable = true)
	private Date datumLaatsteInschrijving;

	@Column(nullable = true)
	@Enumerated(value = EnumType.STRING)
	private MBOLeerweg leerweg;

	@Column(nullable = true)
	private Integer duurInMaanden;

	/**
	 * Vrije tekst die opgenomen kan worden op diploma's.
	 */
	@Column(nullable = true, length = 1000)
	private String diplomatekst1;

	@Column(nullable = true, length = 1000)
	private String diplomatekst2;

	@Column(nullable = true, length = 1000)
	private String diplomatekst3;

	/**
	 * Vlaggetje om aan te geven of eventuele landelijke productregels genegeerd moeten
	 * worden.
	 */
	@Column(nullable = false)
	private boolean negeerLandelijkeProductregels;

	/**
	 * Of een kenniscentrum gekozen moet worden voor verbintenissen van deze opleiding
	 * (mantis 63058)
	 */
	@Column(nullable = false)
	private boolean kiesKenniscentrum = false;

	/**
	 * Vlaggetje om aan te geven of eventuele landelijke criteria genegeerd moeten worden.
	 */
	@Column(nullable = false)
	private boolean negeerLandelijkeCriteria;

	/**
	 * Vlaggetje om aan te geven of deze opleiding met BRON gecommuniceerd moet worden.
	 * Heeft alleen invloed op opleidingen die binnen een taxonomie vallen die
	 * daadwerkelijk met BRON uitgewisseld kunnen worden.
	 */
	@Column(nullable = false, name = "communicerenMetBRON")
	private boolean communicerenMetBRON = true;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "opleiding")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<OpleidingVrijVeld> vrijVelden;

	/**
	 * De bijlages van deze opleiding
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "opleiding")
	private List<OpleidingBijlage> bijlagen;

	@Column(nullable = false, length = 100)
	@Index(name = "idx_Opleiding_wervingsnaam")
	@AutoForm(description = "Deze naam kan gebruikt worden bij bijvoorbeeld een digitaal aanmeld portaal")
	private String wervingsnaam;

	@Column(nullable = true, length = 100)
	@Enumerated(value = EnumType.STRING)
	@AutoForm(description = "De default intensiteit voor deelnemers die ingeschreven worden op deze opleiding")
	private Intensiteit defaultIntensiteit;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "opleiding")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("cohort ASC")
	private List<Curriculum> curriculums;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "opleiding")
	@OrderBy(value = "opleidingsvorm desc, hoofdfase desc")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<OpleidingFase> fases;

	public Opleiding()
	{
	}

	@Exportable
	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Exportable
	public String getNaamZonderUitstroom()
	{
		if (naam.contains("("))
			return naam.substring(0, naam.indexOf("("));
		return naam;
	}

	@Exportable
	public String getNaamAlleenUitstroom()
	{
		if (naam.contains("(") && naam.contains(")"))
		{
			int indexOpen = naam.indexOf("(");
			int indexSluiten = naam.indexOf(")");
			if (indexOpen < indexSluiten)
			{
				return naam.substring(indexOpen + 1, indexSluiten);
			}
		}
		return "";
	}

	@Exportable
	@AutoFormEmbedded
	public Verbintenisgebied getVerbintenisgebied()
	{
		return verbintenisgebied;
	}

	public void setVerbintenisgebied(Verbintenisgebied verbintenisgebied)
	{
		this.verbintenisgebied = verbintenisgebied;
		generateFases();
	}

	@Override
	public String getContextInfoOmschrijving()
	{
		return getNaam();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(getCode());
		builder.append(" - ");
		if (EduArteContext.get().getAccount() != null
			&& RechtenSoort.DIGITAALAANMELDER == EduArteContext.get().getAccount()
				.getRechtenSoort())
			builder.append(getWervingsnaam());
		else
			builder.append(getNaam());
		String crebo =
			getVerbintenisgebied() != null ? getVerbintenisgebied().getExterneCode() : null;
		if (crebo != null)
		{
			builder.append(" - ");
			builder.append(crebo);
		}
		return builder.toString();
	}

	@Exportable
	public List<OpleidingAanbod> getAanbod()
	{
		if (aanbod == null)
			aanbod = new ArrayList<OpleidingAanbod>();
		return aanbod;
	}

	public void setAanbod(List<OpleidingAanbod> aanbod)
	{
		this.aanbod = aanbod;
	}

	public Date getDatumLaatsteInschrijving()
	{
		return datumLaatsteInschrijving;
	}

	public String getDatumLaatsteInschrijvingFormatted()
	{
		return TimeUtil.getInstance().formatDate(getDatumLaatsteInschrijving());
	}

	public void setDatumLaatsteInschrijving(Date datumLaatsteInschrijving)
	{
		this.datumLaatsteInschrijving = datumLaatsteInschrijving;
	}

	/**
	 * @param cohort
	 * @return Alle landelijke en lokale productregels die van toepassing zijn voor deze
	 *         opleiding voor het gegeven cohort.
	 */
	public List<Productregel> getLandelijkeEnLokaleProductregels(Cohort cohort)
	{
		ProductregelZoekFilter filter = new ProductregelZoekFilter(this, cohort);
		return DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class).list(filter);
	}

	/**
	 * @return Geeft de productregels welke actief zijn op het cohort dat is geselecteerd
	 *         en onthouden is in {@link EduArteSession}. Wanneer dit nog niet gebeurd is
	 *         gebruiken we het cohort van vandaag.
	 */
	@Exportable
	public List<Productregel> getLandelijkeEnLokaleProductregels()
	{
		Cohort cohort = null;

		if (RequestCycle.get() != null && EduArteSession.get() != null
			&& EduArteSession.get().getSelectedCohortModel() != null)
			cohort = EduArteSession.get().getSelectedCohortModel().getObject();

		if (cohort == null)
			cohort = Cohort.getHuidigCohort();

		if (RequestCycle.get() != null && EduArteSession.get() != null)
			EduArteSession.get().setSelectedOpleiding(this);

		return getLandelijkeEnLokaleProductregels(cohort);
	}

	public List<Productregel> getLokaleProductregels(Cohort cohort)
	{
		List<Productregel> res = getLandelijkeEnLokaleProductregels(cohort);
		int index = 0;
		while (index < res.size())
		{
			if (res.get(index).isLandelijk())
			{
				res.remove(index);
			}
			else
			{
				index++;
			}
		}
		return res;
	}

	public Productregel getProductregel(String afkorting, Cohort cohort)
	{
		List<Productregel> regels = getLandelijkeEnLokaleProductregels(cohort);
		for (Productregel regel : regels)
		{
			if (regel.getAfkorting().equals(afkorting))
			{
				return regel;
			}
		}
		return null;
	}

	public List<Productregel> getLandelijkeEnLokaleProductregelsBehalveAfgeleideProductregels(
			Cohort cohort)
	{
		List<Productregel> res = getLandelijkeEnLokaleProductregels(cohort);
		int index = 0;
		while (index < res.size())
		{
			Productregel regel = res.get(index);
			if (regel.getTypeProductregel() == TypeProductregel.AfgeleideProductregel)
			{
				res.remove(index);
			}
			else
			{
				index++;
			}
		}
		return res;
	}

	/**
	 * @param cohort
	 * @return Het hoogste productregelvolgnummer voor deze opleiding voor het gegeven
	 *         cohort.
	 */
	public int getMaxProductregelVolgnummer(Cohort cohort)
	{
		int max = 0;
		for (Productregel regel : getLandelijkeEnLokaleProductregels(cohort))
		{
			max = Math.max(regel.getVolgnummer(), max);
		}
		return max;
	}

	@Exportable
	public List<Criterium> getLandelijkeEnLokaleCriteria()
	{
		Cohort cohort = null;

		if (RequestCycle.get() != null && EduArteSession.get() != null
			&& EduArteSession.get().getSelectedCohortModel() != null)
			cohort = EduArteSession.get().getSelectedCohortModel().getObject();

		if (cohort == null)
			cohort = Cohort.getHuidigCohort();

		return getLandelijkeEnLokaleCriteria(cohort);
	}

	/**
	 * @param cohort
	 * @return Alle landelijke en lokale criteria die van toepassing zijn voor deze
	 *         opleiding voor het gegeven cohort.
	 */
	public List<Criterium> getLandelijkeEnLokaleCriteria(Cohort cohort)
	{
		CriteriumZoekFilter filter = new CriteriumZoekFilter(this, cohort);
		return DataAccessRegistry.getHelper(CriteriumDataAccessHelper.class).list(filter);
	}

	/**
	 * @param cohort
	 * @return Het hoogste criteriumvolgnummer voor deze opleiding voor het gegeven
	 *         cohort.
	 */
	public int getMaxCriteriumVolgnummer(Cohort cohort)
	{
		int max = 0;
		for (Criterium criterium : getLandelijkeEnLokaleCriteria(cohort))
		{
			max = Math.max(criterium.getVolgnummer(), max);
		}
		return max;
	}

	public List<Criterium> getLokaleCriteria(Cohort cohort)
	{
		List<Criterium> res = getLandelijkeEnLokaleCriteria(cohort);
		int index = 0;
		while (index < res.size())
		{
			if (res.get(index).isLandelijk())
			{
				res.remove(index);
			}
			else
			{
				index++;
			}
		}
		return res;
	}

	public Criterium getCriterium(String criteriumNaam, Cohort cohort)
	{
		List<Criterium> criteria = getLandelijkeEnLokaleCriteria(cohort);
		for (Criterium criterium : criteria)
		{
			if (criterium.getNaam().equals(criteriumNaam))
			{
				return criterium;
			}
		}
		return null;
	}

	@Exportable
	public Integer getBeginLeerjaar()
	{
		return beginLeerjaar;
	}

	public void setBeginLeerjaar(Integer beginLeerjaar)
	{
		this.beginLeerjaar = beginLeerjaar;
	}

	@Exportable
	public Integer getEindLeerjaar()
	{
		return eindLeerjaar;
	}

	public void setEindLeerjaar(Integer eindLeerjaar)
	{
		this.eindLeerjaar = eindLeerjaar;
	}

	@Exportable
	public String getLeerwegOmschrijvingLC()
	{
		return leerweg != null ? leerweg.getOmschrijvingLC() : "";
	}

	@Exportable
	public MBOLeerweg getLeerweg()
	{
		return leerweg;
	}

	public void setLeerweg(MBOLeerweg leerweg)
	{
		this.leerweg = leerweg;
	}

	@Exportable
	public Integer getDuurInMaanden()
	{
		return duurInMaanden;
	}

	public void setDuurInMaanden(Integer duurInMaanden)
	{
		this.duurInMaanden = duurInMaanden;
	}

	public boolean isVavo()
	{
		String externeCode = verbintenisgebied.getExterneCode();
		return verbintenisgebied.getTaxonomie().getTaxonomieEnum() == TaxonomieEnum.VO
			&& externeCode != null && externeCode.startsWith("5");
	}

	public boolean heeftLwoo()
	{
		Verbintenisgebied gebied = getVerbintenisgebied();
		if (gebied.getTaxonomie() != null && gebied.getTaxonomie().getTaxonomieEnum().heeftLWOO())
		{
			if (Elementcode.class.isAssignableFrom(Hibernate.getClass(gebied)))
			{
				Elementcode elementcode = (Elementcode) verbintenisgebied.doUnproxy();
				if (Boolean.TRUE.equals(elementcode.getLwoo())
					|| elementcode.getLwooTaxonomieElement() != null)
					return true;
			}
		}
		return false;
	}

	@Exportable
	public String getDiplomatekst1()
	{
		return diplomatekst1;
	}

	public void setDiplomatekst1(String diplomatekst1)
	{
		this.diplomatekst1 = diplomatekst1;
	}

	@Exportable
	public String getDiplomatekst2()
	{
		return diplomatekst2;
	}

	public void setDiplomatekst2(String diplomatekst2)
	{
		this.diplomatekst2 = diplomatekst2;
	}

	@Exportable
	public String getDiplomatekst3()
	{
		return diplomatekst3;
	}

	public void setDiplomatekst3(String diplomatekst3)
	{
		this.diplomatekst3 = diplomatekst3;
	}

	public List<Curriculum> getCurriculums()
	{
		return curriculums;
	}

	public void setCurriculums(List<Curriculum> curriculums)
	{
		this.curriculums = curriculums;
	}

	@Override
	public List<OpleidingAanbod> getOrganisatieEenheidLocatieKoppelingen()
	{
		return getAanbod();
	}

	public boolean isDeletable()
	{
		return !isInGebruik();
	}

	@Override
	public List<OpleidingVrijVeld> getVrijVelden()
	{
		if (vrijVelden == null)
			vrijVelden = new ArrayList<OpleidingVrijVeld>();

		return vrijVelden;
	}

	@Override
	public List<OpleidingVrijVeld> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<OpleidingVrijVeld> res = new ArrayList<OpleidingVrijVeld>();
		for (OpleidingVrijVeld pvv : getVrijVelden())
		{
			if (pvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(pvv);
			}
		}
		return res;
	}

	@Override
	public OpleidingVrijVeld newVrijVeld()
	{
		OpleidingVrijVeld pvv = new OpleidingVrijVeld();
		pvv.setOpleiding(this);

		return pvv;
	}

	@Override
	public void setVrijVelden(List<OpleidingVrijVeld> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}

	@Override
	public Opleiding getOpleiding()
	{
		return this;
	}

	public boolean isAangebodenOp(OrganisatieEenheid organisatieEenheid)
	{
		for (OpleidingAanbod curAanbod : getAanbod())
			if (curAanbod.getOrganisatieEenheid().equals(organisatieEenheid))
				return true;
		return false;
	}

	public boolean isAangebodenOp(Locatie locatie)
	{
		for (OpleidingAanbod curAanbod : getAanbod())
			if (curAanbod.getLocatie().equals(locatie))
				return true;
		return false;
	}

	public boolean isInburgering()
	{
		return getVerbintenisgebied().getTaxonomie().getTaxonomieEnum().equals(
			TaxonomieEnum.Inburgering);
	}

	public MBONiveau getNiveau()
	{
		Verbintenisgebied gebied = (Verbintenisgebied) getVerbintenisgebied().doUnproxy();
		if (gebied instanceof AbstractMBOVerbintenisgebied)
			return ((AbstractMBOVerbintenisgebied) gebied).getNiveau();
		return null;
	}

	@Exportable
	@Override
	public String getVrijVeldWaarde(String vrijVeldNaam)
	{
		for (OpleidingVrijVeld vrijVeld : vrijVelden)
			if (vrijVeld.getVrijVeld().getNaam().equals(vrijVeldNaam))
				return vrijVeld.getOmschrijving();
		return null;
	}

	public boolean isNegeerLandelijkeProductregels()
	{
		return negeerLandelijkeProductregels;
	}

	public void setNegeerLandelijkeProductregels(boolean negeerLandelijkeProductregels)
	{
		this.negeerLandelijkeProductregels = negeerLandelijkeProductregels;
	}

	public String getNegeerLandelijkeProductregelsOmschrijving()
	{
		return isNegeerLandelijkeProductregels() ? "Ja" : "Nee";
	}

	public String getKiesKennisCenterumOmschrijving()
	{
		return isKiesKenniscentrum() ? "Ja" : "Nee";
	}

	public boolean isNegeerLandelijkeCriteria()
	{
		return negeerLandelijkeCriteria;
	}

	public void setNegeerLandelijkeCriteria(boolean negeerLandelijkeCriteria)
	{
		this.negeerLandelijkeCriteria = negeerLandelijkeCriteria;
	}

	public String getNegeerLandelijkeCriteriaOmschrijving()
	{
		return isNegeerLandelijkeCriteria() ? "Ja" : "Nee";
	}

	@Override
	public OpleidingBijlage addBijlage(Bijlage bijlage)
	{
		OpleidingBijlage newBijlage = new OpleidingBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setOpleiding(this);

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (OpleidingBijlage oplBijlage : getBijlagen())
		{
			if (oplBijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	public List<OpleidingBijlage> getBijlagen()
	{
		if (bijlagen == null)
			bijlagen = new ArrayList<OpleidingBijlage>();
		return bijlagen;
	}

	public int getAantalBijlagenVanType(DocumentType documentType)
	{
		int aantal = 0;
		for (OpleidingBijlage bijlage : getBijlagen())
		{
			if (documentType.equals(bijlage.getBijlage().getDocumentType()))
			{
				aantal++;
			}
		}
		return aantal;
	}

	@Override
	public void setBijlagen(List<OpleidingBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;

	}

	public String getTeams()
	{
		StringBuilder builder = new StringBuilder();
		List<Team> teams = new ArrayList<Team>();
		for (OpleidingAanbod aanb : getAanbod())
		{
			if (aanb.getTeam() != null && !teams.contains(aanb.getTeam()))
			{
				teams.add(aanb.getTeam());
				builder.append(aanb.getTeam() + ", ");
			}
		}
		if (builder.length() > 0)
			builder.deleteCharAt(builder.lastIndexOf(","));
		return builder.toString();
	}

	/**
	 * @deprecated use {@link Opleiding#isCommunicerenMetDUO}
	 */
	@Deprecated
	public boolean isCommunicerenMetBRON()
	{
		return communicerenMetBRON;
	}

	/**
	 * @deprecated use {@link Opleiding#setCommunicerenMetDUO(boolean)}
	 */
	@Deprecated
	public void setCommunicerenMetBRON(boolean communicerenMetBRON)
	{
		this.communicerenMetBRON = communicerenMetBRON;
	}

	/**
	 * @deprecated use {@link Opleiding#getCommunicerenMetDUOOmschrijving}
	 */
	@Deprecated
	public String getCommunicerenMetBRONOmschrijving()
	{
		return isCommunicerenMetBRON() ? "Ja" : "Nee";
	}

	/**
	 * Geeft terug of gegevens van studenten/deelnemers/leerlingen die deze opleiding
	 * volgen naar DUO gestuurd moeten worden volgens de geldende protocollen (CRI-HO of
	 * BRON). Dit is alleen van invloed voor opleidingen die in een DUO-communiceerbare
	 * taxonomie vallen.
	 */
	public boolean isCommunicerenMetDUO()
	{
		return communicerenMetBRON;
	}

	/**
	 * Bepaalt of gegevens van studenten/deelnemers/leerlingen die deze opleiding volgen
	 * naar DUO gestuurd moeten worden volgens de geldende protocollen (CRI-HO of BRON).
	 * Dit is alleen van invloed voor opleidingen die in een DUO-communiceerbare taxonomie
	 * vallen.
	 */
	public void setCommunicerenMetDUO(boolean communicerenMetDUO)
	{
		this.communicerenMetBRON = communicerenMetDUO;
	}

	public String getCommunicerenMetDUOOmschrijving()
	{
		return isCommunicerenMetDUO() ? "Ja" : "Nee";
	}

	public void setWervingsnaam(String wervingsnaam)
	{
		this.wervingsnaam = wervingsnaam;
	}

	public String getWervingsnaam()
	{
		return wervingsnaam;
	}

	public boolean heeftAanbod()
	{
		return !getAanbod().isEmpty();
	}

	public void setInternationaleNaam(String internationaleNaam)
	{
		this.internationaleNaam = internationaleNaam;
	}

	public String getInternationaleNaam()
	{
		if (StringUtil.isNotEmpty(internationaleNaam))
			return internationaleNaam;

		return naam;
	}

	public void setKiesKenniscentrum(boolean kiesKenniscentrum)
	{
		this.kiesKenniscentrum = kiesKenniscentrum;
	}

	public boolean isKiesKenniscentrum()
	{
		return kiesKenniscentrum;
	}

	public void setFases(List<OpleidingFase> fases)
	{
		this.fases = fases;
	}

	public List<OpleidingFase> getFases()
	{
		if (fases == null)
			return Collections.emptyList();
		return fases;
	}

	public void generateFases()
	{
		if (verbintenisgebied instanceof CrohoOpleiding)
		{
			fases = new ArrayList<OpleidingFase>();
			CrohoOpleiding croho = (CrohoOpleiding) verbintenisgebied;
			List<CrohoOpleidingAanbod> crohoAanbod =
				croho.getAanbod(EduArteContext.get().getInstelling().getBrincode());
			int maxcredits = 0;
			for (CrohoOpleidingAanbod coa : crohoAanbod)
			{
				int credits = coa.getStudielast();
				if (credits > maxcredits)
					maxcredits = credits;
				OpleidingFase oplfase;
				Hoofdfase hoofdfase;
				if (croho.getNaam().startsWith("B "))
				{
					hoofdfase = Hoofdfase.Bachelor;
					if (credits > CREDITS_PER_JAAR)
					{
						oplfase =
							new OpleidingFase(this, coa.getOpleidingsvorm(), Hoofdfase.PropBach);
						oplfase.setCredits(CREDITS_PER_JAAR);
						fases.add(oplfase);
						credits -= CREDITS_PER_JAAR;
					}
				}
				else
					hoofdfase = Hoofdfase.Master;
				oplfase = new OpleidingFase(this, coa.getOpleidingsvorm(), hoofdfase);
				oplfase.setCredits(credits);
				fases.add(oplfase);
			}

			if (duurInMaanden == null && maxcredits > 0)
				setDuurInMaanden(maxcredits * 12 / CREDITS_PER_JAAR);
		}
	}

	public Set<Hoofdfase> getHoofdfases(OpleidingsVorm vorm)
	{
		Set<Hoofdfase> result = EnumSet.noneOf(Hoofdfase.class);
		for (OpleidingFase fase : fases)
			if (vorm == null || fase.getOpleidingsvorm() == vorm)
				result.add(fase.getHoofdfase());

		return result;
	}

	/**
	 * Return het team dat gekoppeld is aan de gegeven organisatie-eenheid en locatie
	 */
	public Team selecteerTeam(OrganisatieEenheid orgeenh, Locatie loc)
	{
		if ((orgeenh != null || loc != null) && getOpleiding() != null)
		{
			for (OpleidingAanbod opleidingAanbod : aanbod)
			{
				if (opleidingAanbod.getOrganisatieEenheid() == orgeenh
					&& opleidingAanbod.getLocatie() == loc)
				{
					return opleidingAanbod.getTeam();
				}
			}
		}
		return null;
	}

	public boolean isHogerOnderwijs()
	{
		Taxonomie taxonomie = getVerbintenisgebied().getTaxonomie();
		return taxonomie != null && taxonomie.isHO();
	}

	public Intensiteit getDefaultIntensiteit()
	{
		return defaultIntensiteit;
	}

	public void setDefaultIntensiteit(Intensiteit defaultIntensiteit)
	{
		this.defaultIntensiteit = defaultIntensiteit;
	}
}
