package nl.topicus.cobra.web.components.form.simple;

import nl.topicus.cobra.web.components.form.FieldProperties;

/**
 * De display container voor een {@code <span>}.
 * 
 * @author papegaaij
 */
public class SimpleLabelDisplayContainer<X, Y, Z> extends AbstractSimpleFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	public SimpleLabelDisplayContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);
	}
}
