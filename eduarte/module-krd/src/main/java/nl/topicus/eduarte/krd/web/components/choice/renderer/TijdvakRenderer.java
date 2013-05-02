package nl.topicus.eduarte.krd.web.components.choice.renderer;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class TijdvakRenderer implements IChoiceRenderer<Integer>
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Integer object)
	{
		return "Tijdvak " + object;
	}

	@Override
	public String getIdValue(Integer object, int index)
	{
		if (object == null)
			return null;
		return object.toString();
	}
}