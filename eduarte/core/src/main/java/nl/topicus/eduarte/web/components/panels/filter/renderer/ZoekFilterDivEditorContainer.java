package nl.topicus.eduarte.web.components.panels.filter.renderer;

import nl.topicus.cobra.web.components.form.FieldProperties;

/**
 * De editor container voor een {@code <div>}.
 * 
 * @author papegaaij
 */
public class ZoekFilterDivEditorContainer<X, Y, Z> extends
		AbstractZoekFilterFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	public ZoekFilterDivEditorContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);
	}
}
