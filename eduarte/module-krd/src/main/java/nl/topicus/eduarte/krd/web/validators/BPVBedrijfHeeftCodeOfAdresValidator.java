package nl.topicus.eduarte.krd.web.validators;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.providers.BPVInschrijvingProvider;

import org.apache.wicket.validation.IValidatable;

public class BPVBedrijfHeeftCodeOfAdresValidator<T> extends AbstractBPVValidator<T>
{
	public BPVBedrijfHeeftCodeOfAdresValidator(BPVInschrijvingProvider provider)
	{
		super(provider);
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void onValidate(IValidatable<T> validatable, BPVInschrijving bpvInschrijving)
	{
		if (!bpvInschrijving.isOpnemenInBron()
			|| !bpvInschrijving.getStatus().isBronCommuniceerbaar()
			|| bpvInschrijving.getBpvBedrijf() == null)
			return;

		if (bpvInschrijving.getBedrijfsgegeven() != null
			&& StringUtil.isEmpty(bpvInschrijving.getBedrijfsgegeven().getCodeLeerbedrijf()))
		{
			if (bpvInschrijving.getBpvBedrijf().getFysiekAdres() == null)
				error(validatable);
		}
	}
}
