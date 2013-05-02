package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.ParticipatieMaandOverzicht;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.ParticipatieAanwezigheidMaandZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;

public interface WaarnemingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Waarneming, WaarnemingZoekFilter>
{
	/**
	 * Geeft alle waarnemingen die overlappen met het gegeven filter. Let op: Dit betekent
	 * dat de waarnemingen overlappen met de begin- en eindtijd van het gegeven filter,
	 * maar dat ze *NIET* gelijk zijn.
	 * 
	 * @param waarnemingZoekFilter
	 * @return List<Waarneming>
	 */
	List<Waarneming> getOverlappendeWaarnemingen(WaarnemingZoekFilter waarnemingZoekFilter);

	/**
	 * @param waarnemingZoekFilter
	 * @return List<Waarneming>
	 */
	List<Waarneming> getWaarnemingenVanDag(WaarnemingZoekFilter waarnemingZoekFilter);

	/**
	 * Geeft alle waarnemingen die liggen tussen de beginDatumTijd, en eindDatumTijd van
	 * het zoekfilter, en die gelijk zijn.
	 * 
	 * @param waarnemingZoekFilter
	 * @return List<Waarneming>
	 */
	List<Waarneming> getAlleWaarnemingenVanTot(WaarnemingZoekFilter waarnemingZoekFilter);

	/**
	 * Geeft alle waarnemingen waarvan de begindatumtijd voor de begindatumtijd van het
	 * zoekfilter is, of er gelijk aan is. En waarvan de einddatumtijd na de einddatumtijd
	 * van het zoekfilter is, of er gelijk aan is.
	 * 
	 * @param filter
	 * @return List<Waarneming>
	 */
	List<Waarneming> getAlleWaarnemingenGroterOfGelijk(WaarnemingZoekFilter filter);

	/**
	 * Geeft alle waarnemingen die overlap hebben, of gelijk zijn.
	 * 
	 * @param waarnemingZoekFilter
	 * @return List<Waarneming>
	 */
	List<Waarneming> getWaarnemingenOverlapEnGelijk(WaarnemingZoekFilter waarnemingZoekFilter);

	/**
	 * @param id
	 * @return de waarneming
	 */
	public Waarneming getById(Long id);

	/**
	 * @param afspraak
	 * @param instelling
	 * @return de waarneming
	 */
	public List<Waarneming> getByAfspraak(Afspraak afspraak, Instelling instelling);

	/**
	 * @param deelnemer
	 * @param afspraak
	 * @param instelling
	 * @return de waarnemingen voor deze deelnemer en afspraak
	 */
	public List<Waarneming> getByDeelnemerAfspraak(Deelnemer deelnemer, Afspraak afspraak,
			Instelling instelling);

	/**
	 * @param filter
	 * @param overzicht
	 */
	void getMaandOverzicht(ParticipatieAanwezigheidMaandZoekFilter filter,
			ParticipatieMaandOverzicht overzicht);

	/**
	 * @return het aantal waarnemingen in de DB
	 */
	public long getAantalWaarnemingen();

}
