package nl.topicus.eduarte.krd.web.validators;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.providers.BPVInschrijvingProvider;

import org.apache.wicket.validation.IValidatable;

public class BronBPVValidator<T> extends AbstractBPVValidator<T>
{
	private static final long serialVersionUID = 1L;

	private List<AbstractBPVValidator<T>> validators = new ArrayList<AbstractBPVValidator<T>>();

	public BronBPVValidator(BPVInschrijvingProvider provider)
	{
		super(provider);
		validators.add(new BPVBedrijfHeeftCodeOfAdresValidator<T>(provider));
	}

	@Override
	public void onValidate(IValidatable<T> validatable, BPVInschrijving bpvInschrijving)
	{
		for (AbstractBPVValidator<T> validator : validators)
		{
			validator.onValidate(validatable, bpvInschrijving);
		}
	}
}
