package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.ParticipatieMaandOverzicht;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieMeldingZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.ParticipatieAanwezigheidMaandZoekFilter;

import org.hibernate.Criteria;

public interface AbsentieMeldingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<AbsentieMelding, AbsentieMeldingZoekFilter>
{
	public void addCriteria(Criteria criteria, AbsentieMeldingZoekFilter filter);

	String getDagenEnUren(AbsentieMeldingZoekFilter meldingFilter, boolean metAantal);

	/**
	 * @param filter
	 * @return true als er nog meldingen zonder einddatum zijn voor het gegeven
	 *         filter(deelnemer)
	 */
	AbsentieMelding zonderEindDatum(AbsentieMeldingZoekFilter filter);

	/**
	 * @param filter
	 * @return Absentiemelding die de einddatumtijd, en de begindatumtijd dekt voor de
	 *         betreffende deelnemer, als er geen absentiemeldin is return null
	 */
	AbsentieMelding meldingVoorDatum(AbsentieMeldingZoekFilter filter);

	/**
	 * @param absentieMeldingZoekFilter
	 * @return het aantal meldingen
	 */
	int getAantalMeldingen(AbsentieMeldingZoekFilter absentieMeldingZoekFilter);

	/**
	 * @param absentieMeldingZoekFilter
	 * @return De laatst toegevoegde absentieMelding
	 */
	AbsentieMelding getLaatstToegevoegdeMelding(AbsentieMeldingZoekFilter absentieMeldingZoekFilter);

	/**
	 * @param absentieMeldingZoekFilter
	 * @return List<AbsentieMelding>
	 */
	List<AbsentieMelding> getOverlappendeMeldingen(
			AbsentieMeldingZoekFilter absentieMeldingZoekFilter);

	/**
	 * @param filter
	 * @param overzicht
	 */
	public void getMaandOverzicht(ParticipatieAanwezigheidMaandZoekFilter filter,
			ParticipatieMaandOverzicht overzicht);

	/**
	 * Maakt een map van de lijst met absentiemeldingen waarbij de deelnemer die gekoppeld
	 * is aan de melding gebruikt wordt als key in de map.
	 * 
	 * @param meldingen
	 *            Een lijst met absentiemeldingen die omgezet moet worden in een map.
	 * @return Een map met de gegeven meldingen waarbij de deelnemer de key is.
	 */
	public Map<Deelnemer, AbsentieMelding> makeMap(List<AbsentieMelding> meldingen);

	/**
	 * Methode die true teruggeeft als er een absentiemelding voor de gegeven deelnemer
	 * bestaat binnen de begin/eindtijd van de gegeven afspraak.
	 * 
	 * @param deelnemer
	 * @param afspraak
	 */
	public boolean heeftAbsentieMelding(Deelnemer deelnemer, Afspraak afspraak);

}
