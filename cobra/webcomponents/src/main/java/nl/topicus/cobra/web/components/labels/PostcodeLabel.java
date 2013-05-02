package nl.topicus.cobra.web.components.labels;

import nl.topicus.cobra.converters.PostcodeConverter;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.convert.IConverter;

public class PostcodeLabel extends Label
{
	private static final long serialVersionUID = 1L;

	public PostcodeLabel(String id)
	{
		super(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IConverter getConverter(Class type)
	{
		return new PostcodeConverter();
	}
}
