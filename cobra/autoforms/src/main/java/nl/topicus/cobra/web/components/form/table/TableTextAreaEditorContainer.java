package nl.topicus.cobra.web.components.form.table;

import nl.topicus.cobra.web.components.form.FieldProperties;

/**
 * De editor container voor een {@code <textarea>} veld.
 * 
 * @author papegaaij
 */
public class TableTextAreaEditorContainer<X, Y, Z> extends AbstractTableFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Maakt een nieuw TableTextAreaEditorContainer.
	 * 
	 * @param id
	 *            Het id.
	 * @param properties
	 *            De properties.
	 */
	public TableTextAreaEditorContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);
	}
}
