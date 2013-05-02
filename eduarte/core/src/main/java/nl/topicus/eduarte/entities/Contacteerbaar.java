package nl.topicus.eduarte.entities;

import java.util.List;

import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.personen.Persoon;

/**
 * Generieke facade voor het kunnen werken met contacteerbare entiteiten, zoals
 * {@link Persoon}, {@link Instelling} en {@link Locatie}.
 * 
 * @author hoeve
 * @param <T>
 *            de specifieke koppeling entiteit tussen de contacteerbare en het
 *            contactgegeven.
 */
public interface Contacteerbaar<T extends IContactgegevenEntiteit>
{
	/**
	 * @return de lijst van contactgegevens van de contacteerbare.
	 */
	public List<T> getContactgegevens();

	/**
	 * @param contactgegevens
	 *            de lijst van contactgegevens van de contacteerbare.
	 */
	public void setContactgegevens(List<T> contactgegevens);

	/**
	 * @return een nieuwe instantie van het contactgegeven object.
	 */
	public T newContactgegeven();

	public List<T> getContactgegevens(SoortContactgegeven soort);

	public T getEersteEmailAdres();

	public T getEersteHomepage();

	public T getEersteMobieltelefoon();

	public T getEersteOverig();

	public T getEersteTelefoon();
}
