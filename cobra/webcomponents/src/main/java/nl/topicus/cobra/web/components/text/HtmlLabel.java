package nl.topicus.cobra.web.components.text;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class HtmlLabel extends Label
{
	private static final long serialVersionUID = 1L;

	public HtmlLabel(String id)
	{
		super(id);
		setEscapeModelStrings(false);
	}

	public HtmlLabel(String id, String label)
	{
		super(id, label);
		setEscapeModelStrings(false);
	}

	public HtmlLabel(String id, IModel<String> model)
	{
		super(id, model);
		setEscapeModelStrings(false);
	}
}
