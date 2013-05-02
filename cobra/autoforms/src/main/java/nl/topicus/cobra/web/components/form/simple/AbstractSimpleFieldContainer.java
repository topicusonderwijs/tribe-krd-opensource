package nl.topicus.cobra.web.components.form.simple;

import nl.topicus.cobra.web.components.form.AbstractFieldContainer;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;

public abstract class AbstractSimpleFieldContainer<X, Y, Z> extends AbstractFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	public AbstractSimpleFieldContainer(String id, final FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);

		WebMarkupContainer label = new WebMarkupContainer("label")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isTransparentResolver()
			{
				return true;
			}
		};
		label.add(new AttributeAppender("title", properties.getLabelModel(), " "));
		add(label);
	}
}
