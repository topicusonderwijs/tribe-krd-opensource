package nl.topicus.eduarte.krd.web.validators;

import java.util.HashMap;
import java.util.Map;

import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.validation.IValidatable;

public class PlaatsingVOVerplichtValidator<T> extends AbstractVerbintenisValidator<T>
{
	private static final long serialVersionUID = 1L;

	public PlaatsingVOVerplichtValidator(VerbintenisProvider provider)
	{
		super(provider);
	}

	@Override
	protected void onValidate(IValidatable<T> validatable, Verbintenis verbintenis)
	{
		VerbintenisStatus status = null;
		if (validatable.getValue() instanceof VerbintenisStatus)
		{
			status = (VerbintenisStatus) validatable.getValue();
		}
		else
		{
			status = verbintenis.getStatus();
		}
		if (verbintenis.isVOVerbintenis())
		{
			if (verbintenis.isActief() && verbintenis.getEinddatum() == null
				&& status.isBronCommuniceerbaar())
			{
				boolean error = true;
				for (Plaatsing plaatsing : verbintenis.getPlaatsingen())
				{
					if (plaatsing.getLeerjaar() != null
						|| plaatsing.getJarenPraktijkonderwijs() != null)
						error = false;
				}
				if (error)
				{
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("verbintenis", verbintenis.getContextInfoOmschrijving());
					error(validatable, params);
				}
			}
		}
	}
}
