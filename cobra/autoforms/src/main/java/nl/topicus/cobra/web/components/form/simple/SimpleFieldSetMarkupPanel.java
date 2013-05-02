package nl.topicus.cobra.web.components.form.simple;

import nl.topicus.cobra.web.components.form.AutoFieldSet;

import org.apache.wicket.markup.html.panel.Panel;

public class SimpleFieldSetMarkupPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	public SimpleFieldSetMarkupPanel(String id, AutoFieldSet< ? > fieldSet)
	{
		super(id);
	}
}
