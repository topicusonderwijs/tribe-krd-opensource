package nl.topicus.cobra.modelsv2;

import org.apache.wicket.model.IModel;

/**
 * Interface for managed models.
 * 
 * @author marrink, papegaaij
 * @param <T>
 */
public interface ExtendedModel<T> extends IModel<T>
{
	public ModelManager getManager();

	public void discardChanges();

	/**
	 * Geeft de huidige status van het object terug. Dit kan gebruikt worden om
	 * wijzigingen terug te draaien (zie {@link #setState(ObjectState)}).
	 */
	public ObjectState getState();

	/**
	 * Draait de wijzigingen op het object terug naar een eerder opgeslagen punt. Dit kan
	 * vooral handig zijn voor genest pagina structuren, waarbij een annuleren knop al
	 * gemaakte wijzigingen terug moet draaien.
	 */
	public void setState(ObjectState state);

	public boolean isAttached();
}
