package nl.topicus.eduarte.krd.web.validators;

import java.util.Map;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

public abstract class AbstractVerbintenisValidator<T> extends AbstractValidator<T>
{
	private static final long serialVersionUID = 1L;

	private final VerbintenisProvider provider;

	public AbstractVerbintenisValidator(VerbintenisProvider provider)
	{
		Asserts.assertNotNull("provider", provider);
		this.provider = provider;
	}

	@Override
	protected final void onValidate(IValidatable<T> validatable)
	{
		Verbintenis verbintenis = provider.getVerbintenis();
		if (verbintenis != null && isBronCommuniceerbaar(verbintenis))
		{
			onValidate(validatable, verbintenis);
		}
	}

	protected abstract void onValidate(IValidatable<T> validatable, Verbintenis verbintenis);

	@Override
	protected Map<String, Object> variablesMap(IValidatable<T> validatable)
	{
		Map<String, Object> map = super.variablesMap(validatable);
		Verbintenis verbintenis = provider.getVerbintenis();
		if (verbintenis != null)
		{
			map.put("status", verbintenis.getStatus().toString().toLowerCase());
		}
		return map;
	}

	protected boolean isBronCommuniceerbaar(Verbintenis verbintenis)
	{
		return verbintenis.getStatus().isBronCommuniceerbaar();
	}
}
