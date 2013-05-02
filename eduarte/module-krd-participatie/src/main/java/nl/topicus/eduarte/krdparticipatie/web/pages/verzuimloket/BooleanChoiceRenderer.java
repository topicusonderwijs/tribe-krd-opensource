package nl.topicus.eduarte.krdparticipatie.web.pages.verzuimloket;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class BooleanChoiceRenderer implements IChoiceRenderer<Boolean>
{
	private static final long serialVersionUID = 1L;

	private String trueString = "Ja";

	private String falseString = "Nee";

	public BooleanChoiceRenderer(String trueString, String falseString)
	{
		super();
		this.trueString = trueString;
		this.falseString = falseString;
	}

	@Override
	public Object getDisplayValue(Boolean object)
	{
		Boolean val = object;
		if (val.booleanValue())
			return trueString;
		return falseString;
	}

	@Override
	public String getIdValue(Boolean object, int index)
	{
		Boolean val = object;
		return val.toString();
	}
}