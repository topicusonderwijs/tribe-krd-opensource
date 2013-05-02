package nl.topicus.eduarte.krd.web.validators;

import java.util.List;

import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.validation.IValidatable;

public class GeenOverlapVoVerbintenisValidator<T> extends AbstractVerbintenisValidator<T>
{
	private static final long serialVersionUID = 1L;

	public GeenOverlapVoVerbintenisValidator(VerbintenisProvider provider)
	{
		super(provider);
	}

	@Override
	protected void onValidate(IValidatable<T> validatable, Verbintenis verbintenis)
	{
		if (verbintenis.isVOVerbintenis())
		{
			List<Verbintenis> verbintenissen = verbintenis.getDeelnemer().getVerbintenissen();
			for (Verbintenis verb : verbintenissen)
			{
				if (!verb.equals(verbintenis) && verb.isVOVerbintenis()
					&& !verb.getStatus().isVerwijderd()
					&& BeginEinddatumUtil.isActief(verb, verbintenis.getBegindatum()))
					error(validatable);
			}
		}
	}
}
