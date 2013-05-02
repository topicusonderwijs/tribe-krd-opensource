package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Beoordeling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.IJkpunt;
import nl.topicus.eduarte.zoekfilters.IJkpuntZoekFilter;

public interface IJkpuntDataAccessHelper extends
		ZoekFilterDataAccessHelper<IJkpunt, IJkpuntZoekFilter>
{
	/**
	 * Verwijderd alle deelnemer specifieke ijkpunten voor de gegeven matrix en deelnemer
	 * combinatie.
	 * 
	 * @param matrix
	 * @param deelnemer
	 */
	public void batchDeleteIJkpunten(CompetentieMatrix matrix, Deelnemer deelnemer);

	/**
	 * Vindt de ijkpunten die matchen aan de gegeven parameters. Als deelnemer null is,
	 * moet deze in de database ook null zijn. Als onderwijsAanbod null is, matcht alle
	 * onderwijsAanbod.
	 * 
	 * @param opleiding
	 * @param deelnemer
	 * @param matrix
	 * @return IJkpunten
	 */
	public List<IJkpunt> getIJkpunten(Opleiding opleiding, Deelnemer deelnemer,
			CompetentieMatrix matrix);

	/**
	 * Vindt de individuele ijkpunten die matchen aan de gegeven parameters. Als
	 * onderwijsAanbod null is, matcht alle onderwijsAanbod.
	 * 
	 * @param opleiding
	 * @param deelnemer
	 * @param matrix
	 * @return IJkpunten
	 */
	public List<IJkpunt> getIndividueleIJkpunten(Opleiding opleiding, Deelnemer deelnemer,
			CompetentieMatrix matrix);

	public List<IJkpunt> getBereikteIJkpunten(Deelnemer deelnemer, Beoordeling beoordeling);

	public List<Deelnemer> getDeelnemers(IJkpunt ijkpunt);

	public boolean isSignaalVerstuurd(IJkpunt ijkpunt, Deelnemer deelnemer, IdObject ontvanger);
}
