package nl.topicus.eduarte.krd.web.validators;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;

import org.apache.wicket.validation.IValidatable;

/**
 * Validator die controleert of de geboortedatum van de deelnemer ingevuld is als de
 * verbintenis een status krijgt die BRON communiceerbaar is.
 */
public class GeboortedatumVerplichtValidator<T> extends AbstractVerbintenisValidator<T>
{
	private static final long serialVersionUID = 1L;

	public GeboortedatumVerplichtValidator(VerbintenisProvider provider)
	{
		super(provider);
	}

	@Override
	protected void onValidate(IValidatable<T> validatable, Verbintenis verbintenis)
	{
		BronEduArteModel model = new BronEduArteModel();

		VerbintenisStatus status = null;

		if (validatable.getValue() instanceof VerbintenisStatus)
		{
			status = (VerbintenisStatus) validatable.getValue();
		}
		else
		{
			status = verbintenis.getStatus();
		}

		if (!status.isMuteerbaar())
		{
			Datum geboortedatum = model.getGeboortedatum(verbintenis.getDeelnemer());
			if (geboortedatum == null)
			{
				error(validatable);
			}
		}
	}
}
