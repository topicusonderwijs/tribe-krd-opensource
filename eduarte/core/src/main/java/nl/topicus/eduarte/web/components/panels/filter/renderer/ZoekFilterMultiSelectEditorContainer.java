package nl.topicus.eduarte.web.components.panels.filter.renderer;

import nl.topicus.cobra.web.components.form.FieldProperties;

/**
 * De editor container voor een multi {@code <select>} veld.
 * 
 * @author papegaaij
 */
public class ZoekFilterMultiSelectEditorContainer<X, Y, Z> extends
		AbstractZoekFilterFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	public static final String TYPE = "MULTI_SELECT";

	public ZoekFilterMultiSelectEditorContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);
	}
}
