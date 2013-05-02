package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.choice.ActiefCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.productregel.SoortProductregel;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.web.components.choice.TaxonomieCombobox;

import org.apache.wicket.model.IModel;

/**
 * Zoekfilter voor bijlagen.
 * 
 * @author vandekamp
 */
public class SoortProductregelZoekFilter extends
		AbstractLandelijkOfInstellingZoekFilter<SoortProductregel>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	private Integer volgnummer;

	@AutoForm(editorClass = ActiefCombobox.class)
	private Boolean actief;

	@AutoForm(editorClass = TaxonomieCombobox.class)
	private IModel<Taxonomie> taxonomie;

	public SoortProductregelZoekFilter()
	{
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public Integer getVolgnummer()
	{
		return volgnummer;
	}

	public void setVolgnummer(Integer volgnummer)
	{
		this.volgnummer = volgnummer;
	}

	public Boolean isActief()
	{
		return actief;
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public Taxonomie getTaxonomie()
	{
		return getModelObject(taxonomie);
	}

	public void setTaxonomie(Taxonomie taxonomie)
	{
		this.taxonomie = makeModelFor(taxonomie);
	}
}
