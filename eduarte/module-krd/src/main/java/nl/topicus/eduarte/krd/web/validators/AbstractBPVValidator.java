package nl.topicus.eduarte.krd.web.validators;

import java.util.Map;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.providers.BPVInschrijvingProvider;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

public abstract class AbstractBPVValidator<T> extends AbstractValidator<T>
{
	private static final long serialVersionUID = 1L;

	private final BPVInschrijvingProvider provider;

	public AbstractBPVValidator(BPVInschrijvingProvider provider)
	{
		Asserts.assertNotNull("provider", provider);
		this.provider = provider;
	}

	@Override
	protected final void onValidate(IValidatable<T> validatable)
	{
		BPVInschrijving bpv = provider.getBPV();
		if (bpv != null && bpv.getStatus().isBronCommuniceerbaar())
		{
			onValidate(validatable, bpv);
		}
	}

	protected abstract void onValidate(IValidatable<T> validatable, BPVInschrijving bpvInschrijving);

	@Override
	protected Map<String, Object> variablesMap(IValidatable<T> validatable)
	{
		Map<String, Object> map = super.variablesMap(validatable);
		BPVInschrijving bpv = provider.getBPV();
		if (bpv != null)
		{
			map.put("status", bpv.getStatus().toString().toLowerCase());
		}
		return map;
	}
}
