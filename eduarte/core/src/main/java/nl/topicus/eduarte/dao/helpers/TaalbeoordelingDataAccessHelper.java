package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.ModerneTaal;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalbeoordeling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalscore;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.TaalscoreNiveauVerzameling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalvaardigheid;
import nl.topicus.eduarte.zoekfilters.TaalbeoordelingZoekFilter;

/**
 * @author vandenbrink
 */
public interface TaalbeoordelingDataAccessHelper extends
		ZoekFilterDataAccessHelper<Taalbeoordeling, TaalbeoordelingZoekFilter>
{
	/**
	 * Convenience functie om te zien of een cohort in gebruik is. Dit is zo als er een
	 * Beoordeling is bij een uitstroom van een kwalificatiedossier voor het opgegeven
	 * cohort.
	 * 
	 * @param cohort
	 * @return true als cohort in gebruik
	 */
	public boolean isCohortInGebruik(Cohort cohort);

	/**
	 * Convenience functie om te zien of een taal in gebruik is. In dit geval wil dat
	 * zeggen dat er een beoordeling is voor die taal en deelnemer.
	 * 
	 * @param taal
	 * @param deelnemer
	 * @return true als taal in gebruik
	 */
	public boolean isTaalInGebruik(ModerneTaal taal, Deelnemer deelnemer);

	/**
	 * De taalbeoordelingen van deze deelnemer voor de gegeven taal
	 * 
	 * @param deelnemer
	 * @param taal
	 * @return lijst met beoordelingen
	 */
	public List<Taalbeoordeling> getTaalbeoordelingen(Deelnemer deelnemer, ModerneTaal taal);

	/**
	 * Taalscores of alfabetische volgorde
	 * 
	 * @param beoordeling
	 * @return taalscores
	 */
	public List<Taalscore> getTaalscoresOrdered(TaalscoreNiveauVerzameling beoordeling);

	/**
	 * De nieuwste taalbeoordeling voor deze deelnemer en taal, dit wordt gezien als de
	 * huidige stand.
	 * 
	 * @param deelnemer
	 * @param taal
	 * @return de taalbeoordeling
	 */
	public Taalbeoordeling getNieuwsteTaalbeoordeling(Deelnemer deelnemer, ModerneTaal taal);

	/**
	 * @param vaardigheid
	 * @param verzameling
	 * @return taalscore
	 */
	public Taalscore getTaalscore(Taalvaardigheid vaardigheid,
			TaalscoreNiveauVerzameling verzameling);

	/**
	 * check of er een beoordeling is waarvan de bijbehorende deelnemer in deelnemerIDs
	 * zit
	 */
	public boolean bestaatBeoordeling(List<Long> deelnemerIDs);
}
