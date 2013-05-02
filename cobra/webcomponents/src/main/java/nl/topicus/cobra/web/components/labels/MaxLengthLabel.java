package nl.topicus.cobra.web.components.labels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Label dat de tekst afkapt als deze langer wordt dan de maximale lengte die gezet is. De
 * maximale lengte moet minimaal 3 zijn.
 * 
 * @author loite
 */
public class MaxLengthLabel extends Label
{
	private static final long serialVersionUID = 1L;

	private final int maxLength;

	public MaxLengthLabel(String id, int maxLength)
	{
		super(id);
		if (maxLength < 3)
			throw new IllegalArgumentException("Maximale lengte moet minimaal 3 zijn.");
		this.maxLength = maxLength;
	}

	public MaxLengthLabel(String id, String text, int maxLength)
	{
		super(id, text);
		if (maxLength < 3)
			throw new IllegalArgumentException("Maximale lengte moet minimaal 3 zijn.");
		this.maxLength = maxLength;
	}

	public MaxLengthLabel(String id, IModel< ? > model, int maxLength)
	{
		super(id, model);
		if (maxLength < 3)
			throw new IllegalArgumentException("Maximale lengte moet minimaal 3 zijn.");
		this.maxLength = maxLength;
	}

	@Override
	protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
	{
		String value = getDefaultModelObjectAsString();
		if (value != null && value.length() > maxLength)
		{
			value = value.substring(0, maxLength - 3) + "...";
		}
		replaceComponentTagBody(markupStream, openTag, value);
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		String value = getDefaultModelObjectAsString();
		if (value != null && value.length() > maxLength)
		{
			add(new AttributeModifier("title", true, new Model<String>(value)));
		}
	}

}
