package nl.topicus.eduarte.web.components.label;

import nl.topicus.eduarte.entities.adres.Adres;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Speciaal label voor die vervelende gebruikers die 2 fricking spaties tussen postcode en
 * woonplaats willen. Dit label bind je bij voorkeur aan {@link Adres#getPostcodePlaats()}
 * en presto de twee spaties worden gerenderd als non-breaking spaces.
 * 
 * @author dashorst
 */
public class PostcodeWoonplaatsLabel extends Label
{
	private static final long serialVersionUID = 1L;

	public PostcodeWoonplaatsLabel(String id)
	{
		super(id);
	}

	public PostcodeWoonplaatsLabel(String id, String text)
	{
		super(id, text);
	}

	public PostcodeWoonplaatsLabel(String id, IModel< ? > model)
	{
		super(id, model);
	}

	@Override
	protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
	{
		String contents = getDefaultModelObjectAsString();
		contents = contents.replaceAll(" ", "&nbsp;");
		replaceComponentTagBody(markupStream, openTag, contents);
	}
}
