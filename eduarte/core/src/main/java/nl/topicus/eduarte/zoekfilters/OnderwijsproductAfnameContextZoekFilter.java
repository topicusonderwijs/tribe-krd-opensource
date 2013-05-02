package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel;

import org.apache.wicket.model.IModel;

/**
 * Zoekfilter voor onderwijsproductafnamecontexten.
 * 
 * @author vandekamp
 */
public class OnderwijsproductAfnameContextZoekFilter extends
		AbstractZoekFilter<OnderwijsproductAfnameContext>
{
	private static final long serialVersionUID = 1L;

	private IModel<Productregel> productregel;

	private IModel<Verbintenis> verbintenis;

	private IModel<OnderwijsproductAfname> onderwijsproductAfname;

	public OnderwijsproductAfnameContextZoekFilter()
	{
	}

	public Productregel getProductregel()
	{
		return getModelObject(productregel);
	}

	public void setProductregel(Productregel productregel)
	{
		this.productregel = makeModelFor(productregel);
	}

	public Verbintenis getVerbintenis()
	{
		return getModelObject(verbintenis);
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = makeModelFor(verbintenis);
	}

	public void setVerbintenis(IModel<Verbintenis> verbintenisModel)
	{
		this.verbintenis = verbintenisModel;
	}

	public OnderwijsproductAfname getOnderwijsproductAfname()
	{
		return getModelObject(onderwijsproductAfname);
	}

	public void setOnderwijsproductAfname(OnderwijsproductAfname onderwijsproductAfname)
	{
		this.onderwijsproductAfname = makeModelFor(onderwijsproductAfname);
	}

}
