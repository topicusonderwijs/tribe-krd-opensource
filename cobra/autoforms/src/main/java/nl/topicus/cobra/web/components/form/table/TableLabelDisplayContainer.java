package nl.topicus.cobra.web.components.form.table;

import nl.topicus.cobra.web.components.form.FieldProperties;

/**
 * De display container voor een {@code <span>}.
 * 
 * @author papegaaij
 */
public class TableLabelDisplayContainer<X, Y, Z> extends AbstractTableFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Maakt een nieuw TableLabelDisplayContainer.
	 * 
	 * @param id
	 *            Het id.
	 * @param properties
	 *            De properties.
	 */
	public TableLabelDisplayContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);
	}
}
