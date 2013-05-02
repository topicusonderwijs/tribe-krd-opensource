package nl.topicus.eduarte.web.components.choice.renderer;

import nl.topicus.eduarte.entities.adres.SoortContactgegeven;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Renderer voor entiteiten waarbij de toString() gebruikt wordt voor de toonwaarde, en
 * het id voor de id-waarde.
 * 
 * @author loite
 */
public class SoortContactgegevenRenderer implements IChoiceRenderer<SoortContactgegeven>
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(SoortContactgegeven gegeven)
	{
		return gegeven.getNaam() + " (" + gegeven.getCode() + ")";
	}

	@Override
	public String getIdValue(SoortContactgegeven gegeven, int index)
	{
		return gegeven.getId().toString();
	}

}
