package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import nl.topicus.eduarte.krd.web.validators.BronValidator;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;

/*
 * Component zonder verdere markup die de BRON validator bevat.   
 */
public class BronValidatingFormComponent extends FormComponent<Void>
{
	private static final long serialVersionUID = 1L;

	public BronValidatingFormComponent(String id, VerbintenisProvider provider)
	{
		this(id, provider, null);
	}

	public BronValidatingFormComponent(String id, VerbintenisProvider provider, Form< ? > form)
	{
		super(id);

		add(new BronValidator<Void>(provider, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean validateOnNullValue()
			{
				return true;
			}
		});
	}

	@Override
	public void updateModel()
	{
		// niets doen, deze heeft geen model
	}
}
