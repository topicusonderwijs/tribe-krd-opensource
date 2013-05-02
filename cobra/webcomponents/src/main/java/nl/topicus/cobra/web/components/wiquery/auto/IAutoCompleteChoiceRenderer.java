package nl.topicus.cobra.web.components.wiquery.auto;

import org.apache.wicket.model.IDetachable;

public interface IAutoCompleteChoiceRenderer<T> extends IDetachable
{
	public String getIdValue(T value);

	public String getDisplayValue(T value);

	public String getFieldValue(T value);

	public T getObject(String id);
}
