package nl.topicus.eduarte.web.components.panels.filter.renderer;

import nl.topicus.cobra.web.components.form.AbstractFieldContainer;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.markup.html.basic.Label;

public abstract class AbstractZoekFilterFieldContainer<X, Y, Z> extends
		AbstractFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	public AbstractZoekFilterFieldContainer(String id, final FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);

		add(new Label("label", properties.getLabelModel()));
	}
}
