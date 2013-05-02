package nl.topicus.cobra.web.components.text;

import nl.topicus.cobra.converters.PostcodeConverter;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class PostcodeTextField extends TextField<String>
{
	private static final long serialVersionUID = 1L;

	public PostcodeTextField(String id)
	{
		super(id, String.class);
		// @mantis 44677: zet het type van het veld zodat we de converter gebruiken bij
		// het submitten van waarden, anders wordt elke postcode met waarde '1234AA'
		// gerenderd als '1234 AA' en daarna als '1234 AA' opgeslagen (bij edit pages)
	}

	public PostcodeTextField(String id, IModel<String> model)
	{
		super(id, model);
		setType(String.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IConverter getConverter(Class type)
	{
		return new PostcodeConverter();
	}

	@Override
	protected void convertInput()
	{
		super.convertInput();
	}
}
