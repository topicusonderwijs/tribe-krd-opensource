package nl.topicus.eduarte.krd.web.validators;

import java.util.HashMap;
import java.util.Map;

import nl.topicus.eduarte.entities.inschrijving.IVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.validation.IValidatable;

public class VooropleidingVerplichtValidator<T> extends AbstractVerbintenisValidator<T>
{
	private static final long serialVersionUID = 1L;

	public VooropleidingVerplichtValidator(VerbintenisProvider provider)
	{
		super(provider);
	}

	@Override
	protected void onValidate(IValidatable<T> validatable, Verbintenis verbintenis)
	{
		if (verbintenis.isBVEVerbintenis())
		{
			IVooropleiding vooropleiding = verbintenis.getRelevanteVooropleiding();
			if (vooropleiding == null && (validatable.getValue() instanceof Vooropleiding))
				vooropleiding = (Vooropleiding) validatable.getValue();

			if (vooropleiding == null)
			{
				Map<String, Object> desc = new HashMap<String, Object>();
				desc.put("verbintenis", verbintenis.getContextInfoOmschrijving());
				if (!(validatable.getValue() instanceof VerbintenisStatus && VerbintenisStatus.Voorlopig
					.equals(validatable.getValue())))
				{
					error(validatable, "VooropleidingVerplichtValidator.vooropleiding", desc);
				}

			}
			else if (vooropleiding.getSoortOnderwijs() == null)
			{
				Map<String, Object> desc = new HashMap<String, Object>();
				desc.put("verbintenis", verbintenis.getContextInfoOmschrijving());

				error(validatable, "VooropleidingVerplichtValidator.soortonderwijs", desc);
			}
		}

	}
}
