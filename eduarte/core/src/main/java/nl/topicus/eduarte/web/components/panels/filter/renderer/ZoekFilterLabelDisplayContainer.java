package nl.topicus.eduarte.web.components.panels.filter.renderer;

import nl.topicus.cobra.web.components.form.FieldProperties;

/**
 * De display container voor een {@code <span>}.
 * 
 * @author papegaaij
 */
public class ZoekFilterLabelDisplayContainer<X, Y, Z> extends
		AbstractZoekFilterFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	public ZoekFilterLabelDisplayContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);
	}
}
