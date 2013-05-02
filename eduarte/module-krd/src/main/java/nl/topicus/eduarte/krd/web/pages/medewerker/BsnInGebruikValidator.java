package nl.topicus.eduarte.krd.web.pages.medewerker;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.eduarte.dao.helpers.PersoonDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Persoon;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

public class BsnInGebruikValidator<T> extends AbstractValidator<T>
{
	private static final long serialVersionUID = 1L;

	private Long lastBSN = null;

	private IModel<Persoon> persoonModel;

	public BsnInGebruikValidator(IModel<Persoon> persoonModel)
	{
		this.persoonModel = persoonModel;
	}

	@Override
	protected void onValidate(IValidatable<T> validatable)
	{
		Persoon persoon = persoonModel.getObject();
		if (JavaUtil.equalsOrBothNull(lastBSN, persoon.getBsn()))
			return;

		lastBSN = persoon.getBsn();
		List<Persoon> personen =
			DataAccessRegistry.getHelper(PersoonDataAccessHelper.class).getByBSN(persoon.getBsn());
		for (Persoon pson : personen)
		{
			if (!pson.equals(persoon))
			{
				// Waarschuw dat er andere personen met hetzelfde bsn
				// bestaan.
				ValidationError error = new ValidationError();
				error.setMessage("Let op: Er bestaat al een andere persoon met het BSN "
					+ persoon.getBsn() + ", namelijk " + pson.getVolledigeNaam()
					+ ". Druk nogmaals op 'Opslaan' om toch op te slaan.");
				validatable.error(error);
			}
		}
	}
}
