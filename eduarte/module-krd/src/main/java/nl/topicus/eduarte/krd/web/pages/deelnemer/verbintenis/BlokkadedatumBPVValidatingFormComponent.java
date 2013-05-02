package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis;

import java.util.Date;

import nl.topicus.eduarte.entities.bpv.BPVInschrijving;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

/*
 * Component zonder verdere markup die de BlokkadedatumBPVValidator bevat.   
 */
public class BlokkadedatumBPVValidatingFormComponent extends FormComponent<Void>
{
	private static final long serialVersionUID = 1L;

	public BlokkadedatumBPVValidatingFormComponent(String id,
			IModel<BPVInschrijving> bpvInschrijvingModel, BlokkadedatumValidatorMode mode)
	{
		this(id, bpvInschrijvingModel, null, mode);
	}

	public BlokkadedatumBPVValidatingFormComponent(String id,
			IModel<BPVInschrijving> bpvInschrijvingModel, Date initieleBegindatum,
			BlokkadedatumValidatorMode mode)
	{
		super(id);

		add(new BlokkadedatumBPVValidator<Void>(bpvInschrijvingModel, initieleBegindatum, mode)
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
