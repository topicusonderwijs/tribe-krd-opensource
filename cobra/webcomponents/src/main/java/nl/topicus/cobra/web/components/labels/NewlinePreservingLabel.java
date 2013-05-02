package nl.topicus.cobra.web.components.labels;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Label voor Lob's, dit label zorgt ervoor dat ingevoerde newlines ook zichtbaar zijn in
 * VIEW mode in EDIT mode zorgt een TEXTAREA voor de weergave van newline's
 * 
 * @author elferink
 */
public class NewlinePreservingLabel extends Label
{
	private static final long serialVersionUID = 1L;

	public NewlinePreservingLabel(String id)
	{
		super(id);
	}

	public NewlinePreservingLabel(String id, String text)
	{
		super(id, text);
	}

	public NewlinePreservingLabel(String id, IModel< ? > model)
	{
		super(id, model);
	}

	@Override
	protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
	{
		String contents = getDefaultModelObjectAsString();
		contents = contents.replaceAll("\n", getLineSeparator());
		replaceComponentTagBody(markupStream, openTag, contents);
	}

	protected String getLineSeparator()
	{
		return "<br/>";
	}
}
