package nl.topicus.eduarte.krd.web.validators;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.validation.IValidatable;

public class DeelnemerOuderDan4Validator<T> extends AbstractVerbintenisValidator<T>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerOuderDan4Validator(VerbintenisProvider provider)
	{
		super(provider);
	}

	@Override
	protected void onValidate(IValidatable<T> validatable, Verbintenis verbintenis)
	{
		Deelnemer deelnemer = verbintenis.getDeelnemer();
		if (deelnemer.getPersoon().getGeboortedatum() == null)
		{
			return;
		}
		int leeftijd =
			deelnemer.getPersoon().getLeeftijdOpDatum(TimeUtil.getInstance().currentDate());
		if (leeftijd < 4)
		{
			error(validatable);
		}
	}
}
