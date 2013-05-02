package nl.topicus.eduarte.zoekfilters;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Leerstijl;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductStatus;
import nl.topicus.eduarte.entities.onderwijsproduct.SoortOnderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.SoortPraktijklokaal;
import nl.topicus.eduarte.entities.onderwijsproduct.TypeLocatie;
import nl.topicus.eduarte.entities.onderwijsproduct.TypeToets;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;
import nl.topicus.eduarte.entities.vrijevelden.OnderwijsproductVrijVeld;
import nl.topicus.eduarte.web.components.choice.SoortOnderwijsproductCombobox;
import nl.topicus.eduarte.web.components.choice.TaxonomieCombobox;
import nl.topicus.eduarte.web.components.choice.TaxonomieElementTypeCombobox;
import nl.topicus.eduarte.web.components.choice.TypeToetsCombobox;

import org.apache.wicket.model.IModel;

/**
 * Zoekfilter voor onderwijsproducten
 * 
 * @author loite
 */
public class OnderwijsproductZoekFilter
		extends
		AbstractOrganisatieEenheidLocatieVrijVeldableZoekFilter<OnderwijsproductVrijVeld, Onderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_60")
	private String code;

	private String titel;

	private IModel<Onderwijsproduct> voorwaardeVoor;

	private IModel<Onderwijsproduct> opvolgerVan;

	private IModel<Onderwijsproduct> nietOpvolgerVan;

	private IModel<Onderwijsproduct> parent;

	private IModel<List<OpleidingAanbod>> aanbod;

	private IModel<Leerstijl> leerstijl;

	@AutoForm(htmlClasses = "unit_60")
	private String taxonomiecode;

	@AutoForm(editorClass = TaxonomieCombobox.class, htmlClasses = "unit_100")
	private IModel<Taxonomie> taxonomie;

	@AutoForm(editorClass = TaxonomieElementTypeCombobox.class, htmlClasses = "unit_100")
	private IModel<TaxonomieElementType> taxonomieElementType;

	private IModel<SoortPraktijklokaal> soortPraktijklokaal;

	@AutoForm(editorClass = TypeToetsCombobox.class)
	private IModel<TypeToets> typeToets;

	private IModel<TypeLocatie> typeLocatie;

	private IModel<Cohort> cohort;

	private IModel<List<Deelnemer>> deelnemers;

	@AutoForm(editorClass = SoortOnderwijsproductCombobox.class)
	private IModel<SoortOnderwijsproduct> soortOnderwijsproduct;

	private OnderwijsproductStatus status;

	private IModel<List<Onderwijsproduct>> excludedProductenModel;

	private String zoekterm;

	private Boolean bijIntake;

	private Integer credits;

	private Integer mincredits;

	private Integer maxcredits;

	private Boolean stage;

	// indien false, wordt de organisatieEenheid in het zoekfilterpanel readonly gemaakt
	private boolean staOrganisatieEenheidAanpassingToe = true;

	public OnderwijsproductZoekFilter()
	{
		super(OnderwijsproductVrijVeld.class);
	}

	public OnderwijsproductZoekFilter(OrganisatieEenheid organisatieEenheid)
	{
		super(OnderwijsproductVrijVeld.class);
		setOrganisatieEenheid(organisatieEenheid);
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getTitel()
	{
		return titel;
	}

	public void setTitel(String titel)
	{
		this.titel = titel;
	}

	public Onderwijsproduct getVoorwaardeVoor()
	{
		return getModelObject(voorwaardeVoor);
	}

	public void setVoorwaardeVoor(Onderwijsproduct voorwaardeVoor)
	{
		this.voorwaardeVoor = makeModelFor(voorwaardeVoor);
	}

	public Onderwijsproduct getParent()
	{
		return getModelObject(parent);
	}

	public void setParent(Onderwijsproduct parent)
	{
		this.parent = makeModelFor(parent);
	}

	public List<OpleidingAanbod> getAanbod()
	{
		return getModelObject(aanbod);
	}

	public void setAanbod(List<OpleidingAanbod> aanbod)
	{
		this.aanbod = makeModelFor(aanbod);
	}

	public Leerstijl getLeerstijl()
	{
		return getModelObject(leerstijl);
	}

	public void setLeerstijl(Leerstijl leerstijl)
	{
		this.leerstijl = makeModelFor(leerstijl);
	}

	public SoortPraktijklokaal getSoortPraktijklokaal()
	{
		return getModelObject(soortPraktijklokaal);
	}

	public void setSoortPraktijklokaal(SoortPraktijklokaal soortPraktijklokaal)
	{
		this.soortPraktijklokaal = makeModelFor(soortPraktijklokaal);
	}

	public TypeToets getTypeToets()
	{
		return getModelObject(typeToets);
	}

	public void setTypeToets(TypeToets typeToets)
	{
		this.typeToets = makeModelFor(typeToets);
	}

	public TypeLocatie getTypeLocatie()
	{
		return getModelObject(typeLocatie);
	}

	public void setTypeLocatie(TypeLocatie typeLocatie)
	{
		this.typeLocatie = makeModelFor(typeLocatie);
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = makeModelFor(cohort);
	}

	public Cohort getCohort()
	{
		return getModelObject(cohort);
	}

	public void setCohortModel(IModel<Cohort> cohortModel)
	{
		this.cohort = cohortModel;
	}

	public List<Deelnemer> getDeelnemers()
	{
		return getModelObject(deelnemers);
	}

	public void setDeelnemers(List<Deelnemer> deelnemers)
	{
		this.deelnemers = makeModelFor(deelnemers);
	}

	public SoortOnderwijsproduct getSoortOnderwijsproduct()
	{
		return getModelObject(soortOnderwijsproduct);
	}

	public void setSoortOnderwijsproduct(SoortOnderwijsproduct soortOnderwijsproduct)
	{
		this.soortOnderwijsproduct = makeModelFor(soortOnderwijsproduct);
	}

	public void setStatus(OnderwijsproductStatus status)
	{
		this.status = status;
	}

	public OnderwijsproductStatus getStatus()
	{
		return status;
	}

	public static OnderwijsproductZoekFilter createDefaultFilter()
	{
		OnderwijsproductZoekFilter ret = new OnderwijsproductZoekFilter();
		ret.addOrderByProperty("code");
		return ret;
	}

	public String getTaxonomiecode()
	{
		return taxonomiecode;
	}

	public void setTaxonomiecode(String taxonomiecode)
	{
		this.taxonomiecode = taxonomiecode;
	}

	public Taxonomie getTaxonomie()
	{
		return getModelObject(taxonomie);
	}

	public void setTaxonomie(Taxonomie taxonomie)
	{
		this.taxonomie = makeModelFor(taxonomie);
	}

	public TaxonomieElementType getTaxonomieElementType()
	{
		return getModelObject(taxonomieElementType);
	}

	public void setTaxonomieElementType(TaxonomieElementType taxonomieElementType)
	{
		this.taxonomieElementType = makeModelFor(taxonomieElementType);
	}

	public boolean heeftTaxonomieCriteria()
	{
		return getTaxonomie() != null || getTaxonomiecode() != null
			|| getTaxonomieElementType() != null;
	}

	public IModel<List<Onderwijsproduct>> getExcludedProductenModel()
	{
		return excludedProductenModel;
	}

	public void setExcludedProductenModel(IModel<List<Onderwijsproduct>> excludedProductenModel)
	{
		this.excludedProductenModel = excludedProductenModel;
	}

	public List<Onderwijsproduct> getExcludedProducten()
	{
		return getModelObject(excludedProductenModel);
	}

	public List<Long> getExcludedProductenIds()
	{
		if (getExcludedProducten() == null)
			return null;
		List<Long> excluded = new ArrayList<Long>(getExcludedProducten().size());
		for (Onderwijsproduct prod : getExcludedProducten())
		{
			excluded.add(prod.getId());
		}
		return excluded;
	}

	public String getZoekterm()
	{
		return zoekterm;
	}

	public void setZoekterm(String zoekterm)
	{
		this.zoekterm = zoekterm;
	}

	public Onderwijsproduct getOpvolgerVan()
	{
		return getModelObject(opvolgerVan);
	}

	public void setOpvolgerVan(Onderwijsproduct opvolgerVan)
	{
		this.opvolgerVan = makeModelFor(opvolgerVan);
	}

	public Onderwijsproduct getNietOpvolgerVan()
	{
		return getModelObject(nietOpvolgerVan);
	}

	public void setNietOpvolgerVan(Onderwijsproduct nietOpvolgerVan)
	{
		this.nietOpvolgerVan = makeModelFor(nietOpvolgerVan);
	}

	public void setAanbodModel(IModel<List<OpleidingAanbod>> aanbodModel)
	{
		this.aanbod = aanbodModel;
	}

	public Boolean getBijIntake()
	{
		return bijIntake;
	}

	public void setBijIntake(Boolean bijIntake)
	{
		this.bijIntake = bijIntake;
	}

	public void setCredits(Integer credits)
	{
		this.credits = credits;
	}

	public Integer getCredits()
	{
		return credits;
	}

	public void setMincredits(Integer mincredits)
	{
		this.mincredits = mincredits;
	}

	public Integer getMincredits()
	{
		return mincredits;
	}

	public void setMax(Integer maxcredits)
	{
		this.maxcredits = maxcredits;
	}

	public Integer getMaxcredits()
	{
		return maxcredits;
	}

	public void setStage(Boolean stage)
	{
		this.stage = stage;
	}

	public Boolean getStage()
	{
		return stage;
	}

	public void setStaOrganisatieEenheidAanpassingToe(boolean staOrganisatieEenheidAanpassingToe)
	{
		this.staOrganisatieEenheidAanpassingToe = staOrganisatieEenheidAanpassingToe;
	}

	public boolean isStaOrganisatieEenheidAanpassingToe()
	{
		return staOrganisatieEenheidAanpassingToe;
	}
}
