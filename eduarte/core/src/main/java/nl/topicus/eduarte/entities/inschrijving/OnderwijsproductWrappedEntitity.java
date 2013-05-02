package nl.topicus.eduarte.entities.inschrijving;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;

@Exportable
public class OnderwijsproductWrappedEntitity
{
	private Onderwijsproduct product;

	private Resultaat resultaat;

	public OnderwijsproductWrappedEntitity(Onderwijsproduct product, Resultaat resultaat)
	{
		this.product = product;
		this.resultaat = resultaat;
	}

	@Exportable
	public String getResultaat()
	{
		if (resultaat == null)
			return "";
		return resultaat.getFormattedDisplayWaarde(true);
	}

	@Exportable
	public String getResultaatDatum()
	{
		if (resultaat == null)
			return "";
		return TimeUtil.getInstance().formatDateTime(resultaat.getLastModifiedAt(), "dd-MM-yyyy");
	}

	@Exportable
	public Onderwijsproduct getOnderwijsproduct()
	{
		return product;
	}
}