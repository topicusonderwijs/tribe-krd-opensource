package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameZoekFilter;

public interface OnderwijsproductAfnameDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<OnderwijsproductAfname, OnderwijsproductAfnameZoekFilter>
{

	/**
	 * 
	 * @param deelnemer
	 * @param verbintenis
	 * @return Alle onderwijsproductafnames van de gegeven deelnemer die niet gekoppeld
	 *         zijn middels een context aan de gegeven verbintenis. Dit komt overeen met
	 *         alle afgenomen onderwijsproducten van de deelnemer die niet getoond worden
	 *         op de productregelspagina van de gegeven verbintenis.
	 */
	public List<OnderwijsproductAfname> getAfgenomenOnderwijsproductenNietGekoppeldAanVerbintenis(
			Deelnemer deelnemer, Verbintenis verbintenis);
}
