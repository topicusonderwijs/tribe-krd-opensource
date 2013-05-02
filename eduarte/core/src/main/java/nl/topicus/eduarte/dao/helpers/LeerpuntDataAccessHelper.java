package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Groepsbeoordeling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.GroepsbeoordelingOverschrijving;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Kerntaak;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Leerpunt;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Werkproces;

public interface LeerpuntDataAccessHelper extends DataAccessHelper<Leerpunt>
{
	/**
	 * Geeft een lijst van alle leerpunten voor de opgeven kerntaak.
	 * 
	 * @param kerntaak
	 * @return De leerpunten.
	 */
	public List<Leerpunt> getLeerpunten(Kerntaak kerntaak);

	/**
	 * Geeft een lijst van alle leerpunten voor het opgeven werkproces.
	 * 
	 * @param werkproces
	 * @return De leerpunten.
	 */
	public List<Leerpunt> getLeerpunten(Werkproces werkproces);

	/**
	 * Geeft een lijst van alle leerpunten voor de opgeven matrix.
	 * 
	 * @param matrix
	 * @return De leerpunten.
	 */
	public List<Leerpunt> getUsedLeerpunten(CompetentieMatrix matrix);

	/**
	 * Geeft een lijst met leerpunten van de groepsbeoordeling die door in een individuele
	 * {@link GroepsbeoordelingOverschrijving} overschreven zijn.
	 * 
	 * @param groepsBeoordeling
	 * @return De leerpunten.
	 */
	public List<Leerpunt> getGroepsbeoordelingOverschrevenLeerpunten(
			Groepsbeoordeling groepsBeoordeling);
}
