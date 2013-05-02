package nl.topicus.eduarte.krd.web.validators;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.validation.IValidatable;

public class GeslachtVerplichtValidator<T> extends AbstractVerbintenisValidator<T>
{
	private static final long serialVersionUID = 1L;

	public GeslachtVerplichtValidator(VerbintenisProvider provider)
	{
		super(provider);
	}

	@Override
	protected void onValidate(IValidatable<T> validatable, Verbintenis verbintenis)
	{
		BronEduArteModel model = new BronEduArteModel();
		Geslacht geslacht = model.getGeslacht(verbintenis.getDeelnemer());
		if (geslacht == null)
		{
			error(validatable);
		}
	}
}
