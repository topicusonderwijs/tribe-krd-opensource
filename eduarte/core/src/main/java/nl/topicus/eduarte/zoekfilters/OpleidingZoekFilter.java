package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.MBONiveau;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.entities.vrijevelden.OpleidingVrijVeld;
import nl.topicus.eduarte.web.components.choice.MBONiveauCombobox;
import nl.topicus.eduarte.web.components.quicksearch.taxonomie.TaxonomieElementSearchEditor;

import org.apache.wicket.model.IModel;

/**
 * Zoekfilter voor opleidingen.
 * 
 * @author loite
 */
public class OpleidingZoekFilter extends
		AbstractOrganisatieEenheidLocatieVrijVeldableZoekFilter<OpleidingVrijVeld, Opleiding>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_60")
	private String code;

	private Boolean gekoppeldAanMeeteenheid;

	private String naam;

	@AutoForm(htmlClasses = "unit_80")
	private MBOLeerweg leerweg;

	private String taxonomiecode;

	@AutoForm(editorClass = TaxonomieElementSearchEditor.class, htmlClasses = "unit_160")
	private IModel<Verbintenisgebied> verbintenisgebied;

	private IModel<Groep> groep;

	@AutoForm(htmlClasses = "unit_80")
	private IModel<Cohort> cohort;

	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean maximumAanwezig;

	private Boolean isAangeboden = true;

	@AutoForm(editorClass = MBONiveauCombobox.class)
	private IModel<MBONiveau> verbintenisgebiedniveau;

	private IModel<Opleiding> variantVan;

	public OpleidingZoekFilter()
	{
		super(OpleidingVrijVeld.class);
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

	public MBOLeerweg getLeerweg()
	{
		return leerweg;
	}

	public void setLeerweg(MBOLeerweg leerweg)
	{
		this.leerweg = leerweg;
	}

	public String getTaxonomiecode()
	{
		return taxonomiecode;
	}

	public void setTaxonomiecode(String taxonomiecode)
	{
		this.taxonomiecode = taxonomiecode;
	}

	public Verbintenisgebied getVerbintenisgebied()
	{
		return getModelObject(verbintenisgebied);
	}

	public MBONiveau getVerbintenisgebiedniveau()
	{
		return getModelObject(verbintenisgebiedniveau);
	}

	public void setVerbintenisgebiedniveau(MBONiveau niveau)
	{
		verbintenisgebiedniveau = makeModelFor(niveau);
	}

	public void setVerbintenisgebied(Verbintenisgebied verbintenisgebied)
	{
		this.verbintenisgebied = makeModelFor(verbintenisgebied);
	}

	public static OpleidingZoekFilter createDefaultFilter()
	{
		OpleidingZoekFilter ret = new OpleidingZoekFilter();
		ret.addOrderByProperty("naam");
		return ret;
	}

	public Cohort getCohort()
	{
		return getModelObject(cohort);
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = makeModelFor(cohort);
	}

	public void setCohortModel(IModel<Cohort> cohortModel)
	{
		this.cohort = cohortModel;
	}

	public Groep getGroep()
	{
		return getModelObject(groep);
	}

	public void setGroep(Groep groep)
	{
		this.groep = makeModelFor(groep);
	}

	public Boolean IsgekoppeldAanMeeteenheid()
	{
		return gekoppeldAanMeeteenheid;
	}

	public void setgekoppeldAanMeeteenheid(Boolean isgekoppeldAanMeeteenheid)
	{
		this.gekoppeldAanMeeteenheid = isgekoppeldAanMeeteenheid;
	}

	public void setIsAangeboden(Boolean isAangeboden)
	{
		this.isAangeboden = isAangeboden;
	}

	public Boolean getIsAangeboden()
	{
		return isAangeboden;
	}

	public Boolean getMaximumAanwezig()
	{
		return maximumAanwezig;
	}

	public void setMaximumAanwezig(Boolean maximumAanwezig)
	{
		this.maximumAanwezig = maximumAanwezig;
	}

	public void setVariantVan(Opleiding variantVan)
	{
		this.variantVan = makeModelFor(variantVan);
	}

	public Opleiding getVariantVan()
	{
		return getModelObject(variantVan);
	}
}
