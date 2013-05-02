package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieNiveauVerzameling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Kerntaak;

/**
 * Data access methoden voor het opvragen van kerntaken.
 * 
 * @author vanharen
 */
public interface KerntaakDataAccessHelper extends DataAccessHelper<Kerntaak>
{
	/**
	 * Geeft een lijst van alle kerntaken die een competentieniveau bevatten binnen de
	 * opgegeven beoordeling.
	 * 
	 * @param beoordeling
	 * @return De gevonden kerntaken.
	 */
	public List<Kerntaak> getKerntaken(CompetentieNiveauVerzameling beoordeling);
}
