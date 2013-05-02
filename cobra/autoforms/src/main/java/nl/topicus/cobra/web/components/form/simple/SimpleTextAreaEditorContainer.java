package nl.topicus.cobra.web.components.form.simple;

import nl.topicus.cobra.web.components.form.FieldProperties;

/**
 * De editor container voor een {@code <textarea>} veld.
 * 
 * @author papegaaij
 */
public class SimpleTextAreaEditorContainer<X, Y, Z> extends AbstractSimpleFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	public SimpleTextAreaEditorContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);
	}
}
