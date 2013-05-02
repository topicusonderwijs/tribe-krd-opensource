package nl.topicus.eduarte.krd.web.validators;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.validation.IValidatable;

public class AdresVerplichtValidator<T> extends AbstractVerbintenisValidator<T>
{
	private static final long serialVersionUID = 1L;

	public AdresVerplichtValidator(VerbintenisProvider provider)
	{
		super(provider);
	}

	@Override
	protected void onValidate(IValidatable<T> validatable, Verbintenis verbintenis)
	{
		String prefix = getClass().getSimpleName() + ".";

		BronEduArteModel model = new BronEduArteModel();
		Deelnemer deelnemer = verbintenis.getDeelnemer();

		Adres woonadres = model.getWoonadres(deelnemer);
		if (woonadres == null)
		{
			error(validatable, prefix + "woonadres");
			return;
		}
		String postcode = model.getPostcode(deelnemer);
		if (!woonadres.isBuitenlandsAdres() && StringUtil.isEmpty(postcode))
		{
			error(validatable, prefix + "postcode");
		}
	}
}
