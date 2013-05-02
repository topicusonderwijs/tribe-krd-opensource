package nl.topicus.cobra.modelsv2;

import nl.topicus.cobra.entities.IdObject;

/**
 * Callback interface voor wijzigingen aan de database door het
 * {@link IChangeRecordingModel}.
 * 
 * @author papegaaij
 */
public interface IModificationCallback
{
	/**
	 * Wordt aangeroepen als een object uit de database verwijderd gaat worden.
	 * 
	 * @param object
	 *            Het object dat verwijderd moet worden. Zeer waarschijnlijk een proxy.
	 * @param clazz
	 *            De unproxied class van het object.
	 */
	public void delete(IdObject object, Class< ? extends IdObject> clazz);

	/**
	 * Wordt aangeroepen als een object in de database opgeslagen gaat worden.
	 * 
	 * @param object
	 *            Het object dat opgeslagen moet worden. Zeer waarschijnlijk een proxy.
	 * @param clazz
	 *            De unproxied class van het object.
	 */
	public void saveOrUpdate(IdObject object, Class< ? extends IdObject> clazz);
}
