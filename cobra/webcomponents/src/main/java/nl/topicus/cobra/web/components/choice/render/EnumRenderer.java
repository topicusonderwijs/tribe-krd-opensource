package nl.topicus.cobra.web.components.choice.render;

import java.util.Map;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Renderer voor het tonen van de namen voor de combobox.
 */
public class EnumRenderer<TE extends Enum< ? >> implements IChoiceRenderer<TE>
{
	private static final long serialVersionUID = 1L;

	private final Map<TE, String> names;

	public EnumRenderer(Map<TE, String> names)
	{
		this.names = names;
	}

	public String getDisplayValue(TE object)
	{
		if (names != null)
		{
			String name = names.get(object);
			if (name != null)
				return name;
		}
		return object.toString();
	}

	public String getIdValue(TE object, int index)
	{
		return Integer.toString(object.ordinal());
	}
}