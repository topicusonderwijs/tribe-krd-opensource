package nl.topicus.cobra.web.components.choice.render;

import nl.topicus.cobra.entities.IdObject;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Renderer voor het tonen van items in een combobox. Default is om de toString() methode
 * van het object te gebruiken. Opmerking, null is een uitzondering en wordt als spatie
 * getoond. Opmerking voor de id wordt er vanuit gegaan dat het om {@link IdObject}s gaat.
 */
public class ToStringRenderer implements IChoiceRenderer<IdObject>
{
	private static final long serialVersionUID = 1L;

	public ToStringRenderer()
	{
	}

	public String getDisplayValue(IdObject object)
	{
		String stringrep = " ";
		if (object != null)
			stringrep = object.toString();

		return stringrep;
	}

	/**
	 * Gets the value that is invisble to the end user, and that is used as the selection
	 * id.
	 * 
	 * @throws ClassCastException
	 *             als het geen {@link IdObject} betreft
	 */
	public String getIdValue(IdObject object, int index)
	{
		if (object == null)
			return null;
		return object.getIdAsSerializable().toString();
	}
}
