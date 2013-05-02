package nl.topicus.cobra.web.components.wiquery.auto;

import java.util.List;

public interface IAutoCompleteBase<T>
{
	public List<T> getChoices(String query);

	public IAutoCompleteChoiceRenderer< ? super T> getRenderer();
}
