package nl.topicus.cobra.web.components.datapanel.columns;

import java.util.Iterator;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class CollectionPropertyColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	private int maxSize;

	public CollectionPropertyColumn(String id, String header, String propertyExpression, int maxSize)
	{
		super(id, header, propertyExpression);
		this.maxSize = maxSize;
	}

	public CollectionPropertyColumn(String id, String header, String propertyExpression,
			boolean repeatWhenEqualToPrevRow, int maxSize)
	{
		super(id, header, propertyExpression, repeatWhenEqualToPrevRow);
		this.maxSize = maxSize;
	}

	@Override
	protected IModel<String> createLabelModel(IModel<T> embeddedModel)
	{
		final IModel< ? > superModel = super.createLabelModel(embeddedModel);
		return new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				StringBuilder ret = new StringBuilder();
				if (superModel.getObject() == null)
					return "";
				Iterator< ? > it = ((Iterable< ? >) superModel.getObject()).iterator();
				int count = 0;
				while (count < maxSize && it.hasNext())
				{
					if (count > 0)
						ret.append(", ");
					ret.append(it.next());
					count++;
				}
				if (it.hasNext())
					ret.append(", ...");
				return ret.toString();
			}
		};
	}
}
