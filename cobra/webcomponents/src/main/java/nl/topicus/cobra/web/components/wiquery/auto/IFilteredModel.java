package nl.topicus.cobra.web.components.wiquery.auto;

import org.apache.wicket.model.IModel;

public interface IFilteredModel<T> extends IModel<T>
{
	public Object getObject(String query, int maxResults);
}
