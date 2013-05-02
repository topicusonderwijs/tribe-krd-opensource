package nl.topicus.eduarte.dao.helpers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameContextZoekFilter;

public interface OnderwijsproductAfnameContextDataAccessHelper
		extends
		BatchZoekFilterDataAccessHelper<OnderwijsproductAfnameContext, OnderwijsproductAfnameContextZoekFilter>
{

	/**
	 * @param verbintenis
	 * @param productregel
	 * @return De onderwijsproductafname bij de gegeven productregel bij de gegeven
	 *         verbintenis.
	 */
	public OnderwijsproductAfnameContext getOnderwijsproductAfnameContext(Verbintenis verbintenis,
			Productregel productregel);

	/**
	 * @param verbintenis
	 * @return Een map met alle onderwijsproductafnames in de context van de gegeven
	 *         verbintenis.
	 */
	public Map<Productregel, OnderwijsproductAfnameContext> list(Verbintenis verbintenis);

	/**
	 * 
	 * @param verbintenis
	 * @return Alle onderwijsproductafnamecontexten voor de gegeven verbintenis.
	 */
	public List<OnderwijsproductAfnameContext> listContexten(Verbintenis verbintenis);

	/**
	 * 
	 * @param verbintenis
	 * @return de onderwijsproducten die binnen de gegeven verbintenis meerdere keren
	 *         gekozen zijn.
	 */
	public Set<Onderwijsproduct> getDuplicaten(Verbintenis verbintenis);
}
