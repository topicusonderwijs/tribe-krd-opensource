package nl.topicus.eduarte.web.components.choice.renderer;

import nl.topicus.eduarte.entities.Entiteit;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Renderer voor entiteiten waarbij de toString() gebruikt wordt voor de toonwaarde, en
 * het id voor de id-waarde.
 * 
 * @author loite
 */
public class EntiteitToStringRenderer implements IChoiceRenderer<Entiteit>
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Entiteit object)
	{
		return object.toString();
	}

	@Override
	public String getIdValue(Entiteit object, int index)
	{
		Entiteit entiteit = object;
		if (entiteit.getTemporaryId() != null)
			return entiteit.getTemporaryId().toString();
		return entiteit.getId().toString();
	}

}
