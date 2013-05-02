package nl.topicus.eduarte.entities;

import java.util.List;

import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldEntiteit;

/**
 * Generieke facade voor het kunnen werken met vrijveldable entiteiten, zoals
 * {@link Persoon}, {@link Instelling} en {@link Locatie}.
 * 
 * @author hoeve * @param <T> de specifieke koppeling entiteit tussen de vrijveldable en
 *         het vrijveld.
 */
public interface VrijVeldable<T extends VrijVeldEntiteit>
{
	/**
	 * @return de lijst van vrije velden van de vrijveldable.
	 */
	public List<T> getVrijVelden();

	/**
	 * @param vrijvelden
	 *            de lijst van vrije velden van de vrijveldable.
	 */
	public void setVrijVelden(List<T> vrijvelden);

	/**
	 * @return een nieuwe instantie van het VrijeVeld object.
	 */
	public T newVrijVeld();

	/**
	 * @param categorie
	 * @return alle vrije velden van deze vrijveldable van de gegeven categorie.
	 */
	public List<T> getVrijVelden(VrijVeldCategorie categorie);

	public String getVrijVeldWaarde(String naam);
}
