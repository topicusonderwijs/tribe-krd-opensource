package nl.topicus.eduarte.resultaten.web.components.resultaat;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

public class ToetsSchaallengteValidator extends AbstractValidator<Integer>
{
	private static final long serialVersionUID = 1L;

	private IModel<Toets> toetsModel;

	private int tijdvak;

	private Integer vorigeLengte;

	public ToetsSchaallengteValidator(IModel<Toets> toetsModel, int tijdvak)
	{
		this.toetsModel = toetsModel;
		this.tijdvak = tijdvak;
	}

	@Override
	protected void onValidate(IValidatable<Integer> validatable)
	{
		Integer lengte = validatable.getValue();
		if (lengte == null)
			return;

		if (lengte.equals(vorigeLengte))
			return;
		vorigeLengte = lengte;

		List<Integer> deelnemers =
			DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).getDeelnemersMetHogereScore(
				toetsModel.getObject(), tijdvak, lengte);
		if (!deelnemers.isEmpty())
		{
			ValidationError error = new ValidationError();
			error.setMessage("Er zijn deelnemers met een score boven de " + lengte + ": "
				+ StringUtil.maakCommaSeparatedString(deelnemers)
				+ (deelnemers.size() == 10 ? " (mogelijk meer)" : "")
				+ ". Druk nogmaals op opslaan om toch op te slaan. "
				+ "Deze deelnemers zullen geen resultaat krijgen.");
			validatable.error(error);
		}
	}
}
