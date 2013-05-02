package nl.topicus.cobra.web.components.form.table;

import nl.topicus.cobra.web.components.form.FieldProperties;

/**
 * De editor container voor een {@code <div>}.
 * 
 * @author papegaaij
 */
public class TableDivEditorContainer<X, Y, Z> extends AbstractTableFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Maakt een nieuw TableDivEditorContainer.
	 * 
	 * @param id
	 *            Het id.
	 * @param properties
	 *            De properties.
	 */
	public TableDivEditorContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);
	}
}
