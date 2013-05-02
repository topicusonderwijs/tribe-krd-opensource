package nl.topicus.cobra.web.components.form.simple;

import nl.topicus.cobra.web.components.form.FieldProperties;

/**
 * De editor container voor een {@code <div>}.
 * 
 * @author papegaaij
 */
public class SimpleDivEditorContainer<X, Y, Z> extends AbstractSimpleFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	public SimpleDivEditorContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);
	}
}
