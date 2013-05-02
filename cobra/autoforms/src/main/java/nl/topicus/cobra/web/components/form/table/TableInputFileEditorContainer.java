package nl.topicus.cobra.web.components.form.table;

import nl.topicus.cobra.web.components.form.FieldProperties;

/**
 * De editor container voor een {@code <input type="file">} veld.
 * 
 * @author papegaaij
 */
public class TableInputFileEditorContainer<X, Y, Z> extends AbstractTableFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Maakt een nieuw TableInputFileEditorContainer.
	 * 
	 * @param id
	 *            Het id.
	 * @param properties
	 *            De properties.
	 */
	public TableInputFileEditorContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);
	}
}
