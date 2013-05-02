package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Beoordeling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieNiveauVerzameling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Groepsbeoordeling;
import nl.topicus.eduarte.zoekfilters.BeoordelingZoekFilter;

/**
 * Data access methoden voor beoordelingen.
 */
public interface BeoordelingDataAccessHelper extends
		ZoekFilterDataAccessHelper<Beoordeling, BeoordelingZoekFilter>
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
	 * Zoekt de EVC/EVK voor een deelnemer. Dit kan er hooguit 1 zijn.
	 * 
	 * @param matrix
	 * @param deelnemer
	 * @return het basis niveau of null.
	 */
	public Beoordeling getEvcEvk(CompetentieMatrix matrix, Deelnemer deelnemer);

	/**
	 * Zoekt het basis niveau op voor de gegeven deelnemer en matrix. Het basis niveau is
	 * een EVC/EVK of vastgestelde beoordeling die nog niet opgenomen is in een andere
	 * beoordeling. Dit kan er hooguit 1 zijn.
	 * 
	 * @param matrix
	 * @param deelnemer
	 * @return het basis niveau of null.
	 */
	public Beoordeling getBasisNiveau(CompetentieMatrix matrix, Deelnemer deelnemer);

	/**
	 * Geeft alle beoordelingen die zijn opgenomen in de gegeven beoordeling.
	 * 
	 * @param vastgestelde
	 * @return De beoordelingen
	 */
	public List<Beoordeling> getOpgenomen(Beoordeling vastgestelde);

	/**
	 * Geeft alle competentie niveau verzamelingen die gekoppeld zijn aan de gegeven
	 * matrix. Dit kunnen beoordelingen, ijkpunten of locale maxima zijn.
	 * 
	 * @param matrix
	 * @return De competentie niveau verzamelingen.
	 */
	public List<CompetentieNiveauVerzameling> getVerzamelingen(CompetentieMatrix matrix);

	/**
	 * Geeft true als er een beoordeling bestaad voor de gegeven deelnemer voor de gegeven
	 * matrix.
	 * 
	 * @param matrix
	 * @param deelnemer
	 * @return True als er een beoordeling is, anders false.
	 */
	public boolean isBeoordeeld(CompetentieMatrix matrix, Deelnemer deelnemer);

	/**
	 * Verwijderd de complete beoordeling.
	 * 
	 * @param beoordeling
	 */
	public void batchDeleteBeoordeling(Beoordeling beoordeling);

	public List<Groepsbeoordeling> getBeoordelingen(Groep groep);

	/**
	 * Check of er een beoordeling bestaat met de matrix matrix en waarvan de deelnemer in
	 * deelnemerIDs zit
	 */
	public boolean bestaatBeoordeling(List<Long> deelnemerIDs, CompetentieMatrix matrix);
}
