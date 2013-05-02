package nl.topicus.eduarte.krd.web.validators;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

/**
 * 
 * 
 * @author vanharen
 */
public class IntensiteitLeerwegKoppelValidator<T> extends AbstractVerbintenisValidator<T>
{

	private static final long serialVersionUID = 1L;

	public IntensiteitLeerwegKoppelValidator(VerbintenisProvider verbintenisProvider)
	{
		super(verbintenisProvider);
	}

	@Override
	protected void onValidate(IValidatable<T> validatable, Verbintenis verbintenis)
	{
		if (verbintenis.getOpleiding() == null || !verbintenis.isBOVerbintenis())
			return;
		MBOLeerweg leerweg = verbintenis.getOpleiding().getLeerweg();
		if (leerweg != null && leerweg == MBOLeerweg.BBL)
		{
			Intensiteit intensiteit = (Intensiteit) validatable.getValue();
			if (intensiteit != Intensiteit.Deeltijd && intensiteit != Intensiteit.Examendeelnemer)
			{
				ValidationError error = new ValidationError();
				error
					.setMessage("BBL kan alleen Deeltijd of Examendeelnemer als Intensiteit hebben.");
				validatable.error(error);
			}
		}
	}

	@Override
	protected boolean isBronCommuniceerbaar(Verbintenis verbintenis)
	{
		return true;
	}
}
