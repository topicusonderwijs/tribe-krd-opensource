package nl.topicus.eduarte.entities.inschrijving;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;

@Exportable
public class OnderwijsproductWrappedList
{
	private Verbintenis verbintenis;

	public OnderwijsproductWrappedList(Verbintenis verbintenis,
			@SuppressWarnings("unused") String arg0)
	{
		this.verbintenis = verbintenis;
	}

	@Exportable
	public List<OnderwijsproductWrappedEntitity> getLijst()
	{
		Map<OnderwijsproductAfname, Resultaat> resultaten =
			new HashMap<OnderwijsproductAfname, Resultaat>();

		List<OnderwijsproductAfname> onderwijsproductAfnames =
			new ArrayList<OnderwijsproductAfname>();
		List<OnderwijsproductWrappedEntitity> list =
			new ArrayList<OnderwijsproductWrappedEntitity>();
		for (OnderwijsproductAfnameContext context : verbintenis.getAfnameContexten())
		{
			onderwijsproductAfnames.add(context.getOnderwijsproductAfname());
		}

		resultaten =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).getEindresultaten(
				verbintenis.getDeelnemer(), onderwijsproductAfnames);

		for (OnderwijsproductAfname afname : resultaten.keySet())
		{
			list.add(new OnderwijsproductWrappedEntitity(afname.getOnderwijsproduct(), resultaten
				.get(afname)));
		}

		return list;
	}
}