package nl.topicus.eduarte.krd.web.validators;

import java.util.List;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

import org.apache.wicket.validation.IValidatable;

public class EnigeVoVerbintenisValidator<T> extends AbstractVerbintenisValidator<T>
{
	private static final long serialVersionUID = 1L;

	public EnigeVoVerbintenisValidator(VerbintenisProvider provider)
	{
		super(provider);
	}

	@Override
	protected void onValidate(IValidatable<T> validatable, Verbintenis verbintenis)
	{
		if (verbintenis.isVOVerbintenis() && verbintenis.isActief()
			&& verbintenis.isBronCommuniceerbaar()
			&& verbintenis.getIntensiteit() != Intensiteit.Examendeelnemer)
		{
			List<Verbintenis> verbintenissen = verbintenis.getDeelnemer().getVerbintenissen();
			for (Verbintenis verb : verbintenissen)
			{
				if (!verb.equals(verbintenis)
					&& verb.isVOVerbintenis()
					&& verb.isActief()
					&& verbintenis.getIntensiteit() != Intensiteit.Examendeelnemer
					&& (verb.isBronCommuniceerbaar() && VerbintenisStatus.Beeindigd != verb
						.getStatus()))
					error(validatable);
			}
		}
	}
}
