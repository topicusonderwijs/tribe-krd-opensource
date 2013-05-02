package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Meeteenheid;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.MeeteenheidKoppel;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.MeeteenheidKoppelType;

import org.apache.wicket.model.IModel;

/**
 * @author vanharen
 */
public class MeeteenheidKoppelZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<MeeteenheidKoppel>
{

	private static final long serialVersionUID = 1L;

	private IModel<Cohort> cohort;

	private String omschrijvingOpleiding;

	private String naamMeeteenheid;

	private OpleidingZoekFilter opleidingZoekFilter;

	@AutoFormEmbedded
	private IModel<Opleiding> opleiding;

	private IModel<Meeteenheid> meeteenheid;

	private IModel<MeeteenheidKoppelType> type;

	private Boolean vastgezet;

	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean automatischAangemaakt;

	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean maximumAanwezig;

	public MeeteenheidKoppelZoekFilter()
	{
		setOpleidingZoekFilter(new OpleidingZoekFilter());
	}

	public Cohort getCohort()
	{
		return getModelObject(cohort);
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = makeModelFor(cohort);
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public Meeteenheid getMeeteenheid()
	{
		return getModelObject(meeteenheid);
	}

	public void setMeeteenheid(Meeteenheid meeteenheid)
	{
		this.meeteenheid = makeModelFor(meeteenheid);
	}

	public String getOmschrijvingOpleiding()
	{
		return omschrijvingOpleiding;
	}

	public void setOmschrijvingOpleiding(String omschrijvingOpleiding)
	{
		this.omschrijvingOpleiding = omschrijvingOpleiding;
	}

	public String getNaamMeeteenheid()
	{
		return naamMeeteenheid;
	}

	public void setNaamMeeteenheid(String naamMeeteenheid)
	{
		this.naamMeeteenheid = naamMeeteenheid;
	}

	public MeeteenheidKoppelType getType()
	{
		return getModelObject(type);
	}

	public void setType(MeeteenheidKoppelType type)
	{
		this.type = makeModelFor(type);
	}

	public Boolean getVastgezet()
	{
		return vastgezet;
	}

	public void setVastgezet(Boolean vastgezet)
	{
		this.vastgezet = vastgezet;
	}

	public Boolean getAutomatischAangemaakt()
	{
		return automatischAangemaakt;
	}

	public void setAutomatischAangemaakt(Boolean automatischAangemaakt)
	{
		this.automatischAangemaakt = automatischAangemaakt;
	}

	public OpleidingZoekFilter getOpleidingZoekFilter()
	{
		return opleidingZoekFilter;
	}

	public void setOpleidingZoekFilter(OpleidingZoekFilter opleidingZoekFilter)
	{
		this.opleidingZoekFilter = opleidingZoekFilter;
	}

	public String getOpleidingnaam()
	{
		return this.opleidingZoekFilter.getNaam();
	}

	public void setOpleidingnaam(String naam)
	{
		this.opleidingZoekFilter.setNaam(naam);
	}

	public String getOpleidingcode()
	{
		return this.opleidingZoekFilter.getCode();
	}

	public void setOpleidingcode(String code)
	{
		this.opleidingZoekFilter.setCode(code);
	}

	public MBOLeerweg getOpleidingleerweg()
	{
		return this.opleidingZoekFilter.getLeerweg();
	}

	public void setOpleidingleerweg(MBOLeerweg leerweg)
	{
		this.opleidingZoekFilter.setLeerweg(leerweg);
	}

	public Verbintenisgebied getVerbintenisgebied()
	{
		return this.opleidingZoekFilter.getVerbintenisgebied();
	}

	public void setVerbintenisgebied(Verbintenisgebied vg)
	{
		this.opleidingZoekFilter.setVerbintenisgebied(vg);
	}

	public Boolean getMaximumAanwezig()
	{
		return maximumAanwezig;
	}

	public void setMaximumAanwezig(Boolean maximumAanwezig)
	{
		this.maximumAanwezig = maximumAanwezig;
	}
}
