package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;
import nl.topicus.eduarte.web.components.choice.TaxonomieCombobox;
import nl.topicus.eduarte.web.components.choice.TaxonomieElementTypeCombobox;

import org.apache.wicket.model.IModel;

public class TaxonomieElementZoekFilter extends
		AbstractLandelijkOfInstellingZoekFilter<TaxonomieElement>
{
	private static final long serialVersionUID = 1L;

	private Class< ? extends TaxonomieElement> zoekenNaarClass;

	private String taxonomiecode;

	private String afkorting;

	private String naam;

	private String externeCode;

	@AutoForm(editorClass = TaxonomieElementTypeCombobox.class)
	private IModel<TaxonomieElementType> taxonomieElementType;

	@AutoForm(editorClass = TaxonomieCombobox.class)
	private IModel<Taxonomie> taxonomie;

	private IModel<TaxonomieElement> parent;

	private IModel<Onderwijsproduct> gekoppeldAanOnderwijsProduct;

	private Boolean inschrijfbaar;

	private Boolean uitzonderlijk;

	public TaxonomieElementZoekFilter()
	{
	}

	public TaxonomieElementZoekFilter(Class< ? extends TaxonomieElement> zoekenNaarClass)
	{
		addOrderByProperty("sorteercode");
		setUitzonderlijk(false);
		this.zoekenNaarClass = zoekenNaarClass;
	}

	public String getTaxonomiecode()
	{
		return taxonomiecode;
	}

	public void setTaxonomiecode(String taxonomiecode)
	{
		this.taxonomiecode = taxonomiecode;
	}

	public String getAfkorting()
	{
		return afkorting;
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getExterneCode()
	{
		return externeCode;
	}

	public void setExterneCode(String externeCode)
	{
		this.externeCode = externeCode;
	}

	public TaxonomieElementType getTaxonomieElementType()
	{
		return getModelObject(taxonomieElementType);
	}

	public void setTaxonomieElementType(TaxonomieElementType taxonomieElementType)
	{
		this.taxonomieElementType = makeModelFor(taxonomieElementType);
	}

	public Class< ? extends TaxonomieElement> getZoekenNaarClass()
	{
		return zoekenNaarClass;
	}

	public void setZoekenNaarClass(Class< ? extends TaxonomieElement> zoekenNaarClass)
	{
		this.zoekenNaarClass = zoekenNaarClass;
	}

	public Taxonomie getTaxonomie()
	{
		return getModelObject(taxonomie);
	}

	public void setTaxonomie(Taxonomie taxonomie)
	{
		this.taxonomie = makeModelFor(taxonomie);
	}

	public TaxonomieElement getParent()
	{
		return getModelObject(parent);
	}

	public void setParent(TaxonomieElement parent)
	{
		this.parent = makeModelFor(parent);
	}

	public Onderwijsproduct getGekoppeldAanOnderwijsProduct()
	{
		return getModelObject(gekoppeldAanOnderwijsProduct);
	}

	public void setGekoppeldAanOnderwijsProduct(Onderwijsproduct gekoppeldAanOnderwijsProduct)
	{
		this.gekoppeldAanOnderwijsProduct = makeModelFor(gekoppeldAanOnderwijsProduct);
	}

	public Boolean getInschrijfbaar()
	{
		return inschrijfbaar;
	}

	public void setInschrijfbaar(Boolean inschrijfbaar)
	{
		this.inschrijfbaar = inschrijfbaar;
	}

	public Boolean getUitzonderlijk()
	{
		return uitzonderlijk;
	}

	public void setUitzonderlijk(Boolean uitzonderlijk)
	{
		this.uitzonderlijk = uitzonderlijk;
	}
}
