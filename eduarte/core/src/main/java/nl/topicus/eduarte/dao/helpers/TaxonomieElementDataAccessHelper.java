package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Kwalificatiedossier;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

/**
 * @author loite
 */
public interface TaxonomieElementDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<TaxonomieElement, TaxonomieElementZoekFilter>
{

	/**
	 * @return Alle taxonomien bij de gegeven instelling, inclusief de landelijke
	 *         taxonomien.
	 */
	public List<Taxonomie> listTaxonomien();

	/**
	 * @param element
	 * @return true indien er opleidingen of onderwijsproducten naar het gegeven
	 *         taxonomie-element verwijzen.
	 */
	public boolean isInGebruik(TaxonomieElement element);

	/**
	 * @param parent
	 *            De parent van het te zoeken element. Mag niet null zijn.
	 * @param naam
	 * @param type
	 * @return Het taxonomie-element met de gegeven naam van het gegeven type.
	 */
	public TaxonomieElement getTaxonomieElement(TaxonomieElement parent, String naam,
			TaxonomieElementType type);

	/**
	 * Zoekt het taxonomie-element met de gegeven naam. Hierbij wordt een ilike-expressie
	 * gebruikt voor de naam om een beetje fuzzy zoeken toe te staan.
	 * 
	 * @param <T>
	 * @param clazz
	 * @param naam
	 * @param strict
	 * @return De taxonomie-elementen met de gegeven naam. Hierbij wordt eerst specifiek
	 *         gezocht naar elementen met precies de gegeven naam, en indien geen
	 *         gevonden, en de optie 'strict' niet is aangezet, met een ilike. Hierdoor
	 *         kunnen meerdere dossiers teruggegeven worden.
	 */
	public <T extends TaxonomieElement> List<T> getTaxonomieElementen(Class<T> clazz, String naam,
			boolean strict);

	/**
	 * Methode die gebruikt wordt om een volgnummer toe te kennen aan taxonomie-elementen.
	 * 
	 * @param parent
	 * @param includeerOnbekend
	 * @return Het aantal children dat het gegeven taxonomie-element heeft. Afhankelijk
	 *         van de waarde van includeerOnbekend wordt het taxonomie-element met de naam
	 *         'Onbekend' wel/niet meegeteld.
	 */
	public int getAantalChildren(TaxonomieElement parent, boolean includeerOnbekend);

	public String getMaxTaxonomiecode(TaxonomieElement parent, boolean includeerOnbekend);

	/**
	 * Deze methode geeft het hoogste volgnummer terug van de children van het meegegeven
	 * parent element. Kan handig zijn voor een parent-element in de Onbekend taxonomie,
	 * waar sorteren op zoekcode niet het gewenste resultaat geeft.
	 */
	public int getMaxTaxonomiecodeVolgnummer(TaxonomieElement parent, boolean includeerOnbekend);

	/**
	 * @param taxonomiecode
	 * @return Het taxonomie-element met de gegeven taxonomiecode bij de gegeven
	 *         instelling, of van de landeljike taxonomien.
	 */
	public TaxonomieElement get(String taxonomiecode);

	/**
	 * @param id
	 * @return Het taxonomie-element met de gegeven taxonomiecode bij de gegeven
	 *         instelling, of van de landelijke taxonomien.
	 */
	public TaxonomieElement get(Long id);

	/**
	 * @param taxonomiecode
	 * @return Het taxonomie-element met de gegeven taxonomiecode van de landelijke
	 *         taxonomien.
	 */
	public TaxonomieElement getLandelijk(String taxonomiecode);

	/**
	 * @param prefix
	 * @param codes
	 * @return een lijst met alle taxonomie-elementen met de gegeven codes. Bij elke code
	 *         wordt de gegeven prefix eerst toegevoegd. Er wordt gezocht bij de gegeven
	 *         instelling en in de landelijke taxonomie.
	 */
	public List<TaxonomieElement> list(String prefix, List<String> codes);

	/**
	 * @param taxonomieElementType
	 * @param externeCode
	 * @return De taxonomie-elementen van het gegeven type met de gegeven externe code.
	 *         Normaal gesproken zou deze functie maar een element moeten opleveren, maar
	 *         er kunnen binnen sommige taxonomieen meerdere elementen zijn met dezelfde
	 *         externe code die binnen verschillende paden in de boom vallen.
	 */
	public List<TaxonomieElement> list(TaxonomieElementType taxonomieElementType, String externeCode);

	public List<TaxonomieElement> list(String externeCode);

	public List<Kwalificatiedossier> getKwalificatieDossiers();
}