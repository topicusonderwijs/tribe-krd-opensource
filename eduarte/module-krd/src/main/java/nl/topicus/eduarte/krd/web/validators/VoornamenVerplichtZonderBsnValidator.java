package nl.topicus.eduarte.krd.web.validators;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.validation.IValidatable;

/**
 * Validator die controleert of de voornamen van de deelnemer ingevuld is als de
 * verbintenis een status krijgt die BRON communiceerbaar is en er geen BSN ingevuld is.
 */
public class VoornamenVerplichtZonderBsnValidator<T> extends AbstractVerbintenisValidator<T>
{
	private static final long serialVersionUID = 1L;

	public VoornamenVerplichtZonderBsnValidator(VerbintenisProvider provider)
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
			Deelnemer deelnemer = verbintenis.getDeelnemer();
			String sofinummer = model.getSofinummer(deelnemer);
			String voornamen = model.getAlleVoornamen(deelnemer);
			if (StringUtil.isEmpty(sofinummer) && StringUtil.isEmpty(voornamen))
			{
				error(validatable);
			}
		}
	}
}
