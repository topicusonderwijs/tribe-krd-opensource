package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis;

import java.util.Date;

import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;

/*
 * Component zonder verdere markup die de BlokkadedatumVerbintenisValidator bevat.   
 */
public class BlokkadedatumVerbintenisValidatingFormComponent extends FormComponent<Void>
{
	private static final long serialVersionUID = 1L;

	public BlokkadedatumVerbintenisValidatingFormComponent(String id, VerbintenisProvider provider,
			Date initieleBegindatum, BlokkadedatumValidatorMode mode)
	{
		this(id, provider, initieleBegindatum, null, mode);
	}

	public BlokkadedatumVerbintenisValidatingFormComponent(String id, VerbintenisProvider provider,
			BlokkadedatumValidatorMode mode)
	{
		this(id, provider, null, null, mode);
	}

	public BlokkadedatumVerbintenisValidatingFormComponent(String id, VerbintenisProvider provider,
			Date initieleBegindatum, Form< ? > form, BlokkadedatumValidatorMode mode)
	{
		super(id);

		add(new BlokkadedatumVerbintenisValidator<Void>(provider, form, initieleBegindatum, mode)
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
