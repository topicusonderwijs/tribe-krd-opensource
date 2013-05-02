package nl.topicus.eduarte.resultaten.web.components.resultaat;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

public abstract class AbstractToetsValidator extends AbstractValidator<Object>
{
	private static final long serialVersionUID = 1L;

	private IModel<Toets> toetsModel;

	private FormComponent<Object> inputField;

	public AbstractToetsValidator(IModel<Toets> toetsModel, FormComponent<Object> inputField)
	{
		this.toetsModel = toetsModel;
		this.inputField = inputField;
	}

	protected Toets getToets()
	{
		return toetsModel.getObject();
	}

	@Override
	protected Map<String, Object> variablesMap(IValidatable<Object> validatable)
	{
		Map<String, Object> ret = super.variablesMap(validatable);
		ret.put("toetscode", getToets().getCode() + " uit "
			+ getToets().getResultaatstructuur().getOnderwijsproduct().getCode());
		if (getToets().hasCijferSchaal())
		{
			IConverter converter = inputField.getConverter(BigDecimal.class);
			Locale locale = Locale.getDefault();
			Object value = converter.convertToObject(inputField.getRawInput(), locale);
			ret.put("converted", converter.convertToString(value, locale));
			ret.put("min", converter.convertToString(getToets().getSchaal().getMinimum(), locale));
			ret.put("max", converter.convertToString(getToets().getSchaal().getMaximum(), locale));
		}
		return ret;
	}
}
