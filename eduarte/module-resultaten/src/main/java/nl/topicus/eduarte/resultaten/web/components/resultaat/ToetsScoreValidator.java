package nl.topicus.eduarte.resultaten.web.components.resultaat;

import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;

public class ToetsScoreValidator extends AbstractToetsValidator
{
	private static final long serialVersionUID = 1L;

	private int poging;

	public ToetsScoreValidator(IModel<Toets> toetsModel, FormComponent<Object> inputField,
			int poging)
	{
		super(toetsModel, inputField);
		this.poging = poging;
	}

	@Override
	protected void onValidate(IValidatable<Object> validatable)
	{
		Toets toets = getToets();
		Integer value = (Integer) validatable.getValue();
		if (value != null && !toets.isGeldigeScore(value, poging))
		{
			error(validatable);
		}
	}
}
