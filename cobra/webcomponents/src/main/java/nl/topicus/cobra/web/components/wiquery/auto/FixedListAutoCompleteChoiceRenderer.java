package nl.topicus.cobra.web.components.wiquery.auto;

import java.util.List;

import org.apache.wicket.model.IModel;

public class FixedListAutoCompleteChoiceRenderer<T> implements IAutoCompleteChoiceRenderer<T>
{
	private static final long serialVersionUID = 1L;

	private IModel<List<T>> choices;

	public FixedListAutoCompleteChoiceRenderer(IModel<List<T>> choices)
	{
		this.choices = choices;
	}

	private List<T> getChoices()
	{
		return choices.getObject();
	}

	@Override
	public String getDisplayValue(T value)
	{
		return value.toString();
	}

	@Override
	public String getFieldValue(T value)
	{
		return getDisplayValue(value);
	}

	@Override
	public String getIdValue(T value)
	{
		return Integer.toString(getChoices().indexOf(value));
	}

	@Override
	public T getObject(String id)
	{
		return getChoices().get(Integer.parseInt(id));
	}

	@Override
	public void detach()
	{
		choices.detach();
	}
}
