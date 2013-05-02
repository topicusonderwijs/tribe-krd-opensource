package nl.topicus.cobra.web.components.wiquery.auto;

public class StringRenderer implements IAutoCompleteChoiceRenderer<String>
{
	private static final long serialVersionUID = 1L;

	@Override
	public String getDisplayValue(String value)
	{
		return value;
	}

	@Override
	public String getFieldValue(String value)
	{
		return value;
	}

	@Override
	public String getIdValue(String value)
	{
		return value;
	}

	@Override
	public String getObject(String id)
	{
		return id;
	}

	@Override
	public void detach()
	{
	}
}
