package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.TypeBijlage;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;

import org.apache.wicket.model.IModel;

/**
 * Zoekfilter voor bijlagen.
 * 
 * @author vandekamp
 */
public class BijlageZoekFilter extends AbstractZoekFilter<Bijlage>
{
	private static final long serialVersionUID = 1L;

	private String omschrijving;

	private TypeBijlage typeBijlage;

	private IModel<Onderwijsproduct> onderwijsproduct;

	public BijlageZoekFilter()
	{
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public TypeBijlage getTypeBijlage()
	{
		return typeBijlage;
	}

	public void setTypeBijlage(TypeBijlage typeBijlage)
	{
		this.typeBijlage = typeBijlage;
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return getModelObject(onderwijsproduct);
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = makeModelFor(onderwijsproduct);
	}
}
