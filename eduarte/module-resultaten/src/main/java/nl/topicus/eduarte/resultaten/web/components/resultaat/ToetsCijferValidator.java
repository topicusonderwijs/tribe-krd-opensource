package nl.topicus.eduarte.resultaten.web.components.resultaat;

import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;

public class ToetsCijferValidator extends AbstractToetsValidator
{
	private static final long serialVersionUID = 1L;

	public ToetsCijferValidator(IModel<Toets> toetsModel, FormComponent<Object> inputField)
	{
		super(toetsModel, inputField);
	}

	@Override
	protected String resourceKey()
	{
		return super.resourceKey() + "." + getToets().getSchaal().getSchaaltype();
	}

	@Override
	protected void onValidate(IValidatable<Object> validatable)
	{
		Toets toets = getToets();
		Object value = validatable.getValue();
		if (value != null && !toets.isGeldigCijfer(value))
		{
			error(validatable);
		}
	}
}
