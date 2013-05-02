package nl.topicus.cobra.modelsv2;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.entities.TransientIdObject;

/**
 * IChangeRecordingModel biedt de mogelijkheid om een compleet object, inclusief
 * onderliggende objecten, op te slaan in of te verwijderen uit de database. De 'grens'
 * van dit model wordt aangegeven door de {@link ModelManager}.
 * 
 * @author papegaaij
 * @param <T>
 */
public interface IChangeRecordingModel<T extends TransientIdObject> extends ExtendedModel<T>
{
	public void doNotDelete(IdObject object);

	/**
	 * Verwijdert het complete object uit de database, inclusief alle onderliggende
	 * objecten.
	 * 
	 * @param callback
	 *            callback interface voor database wijzigingen
	 */
	public void deleteObject(IModificationCallback... callback);

	/**
	 * Slaat het complete object op in de database, inclusief alle onderliggende objecten.
	 * 
	 * @param callback
	 *            callback interface voor database wijzigingen
	 */
	public void saveObject(IModificationCallback... callback);

	/**
	 * Herberekent de object graaf.
	 */
	public void recalculate();
}
