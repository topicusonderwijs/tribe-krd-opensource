package nl.topicus.cobra.web.components.datapanel.columns;

public class TreeColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	public TreeColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	public TreeColumn(String id, String header, String sortProperty, String propertyExpression)
	{
		super(id, header, sortProperty, propertyExpression);
	}

}
