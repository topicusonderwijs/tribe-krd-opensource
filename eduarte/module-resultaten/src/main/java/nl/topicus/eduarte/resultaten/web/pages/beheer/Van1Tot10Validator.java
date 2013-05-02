package nl.topicus.eduarte.resultaten.web.pages.beheer;

import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

public class Van1Tot10Validator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private String prevErrorKey = null;

	public Van1Tot10Validator()
	{
	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		return null;
	}

	@Override
	public void validate(Form< ? > form)
	{
		Schaal schaal = (Schaal) form.getModelObject();
		String errorKey = schaal.getMinimum() + ":" + schaal.getMaximum();
		if (!schaal.isVan1Tot10() && !errorKey.equals(prevErrorKey))
		{
			form.error("De schaal loopt niet van 1 tot 10. Dit kan problemen geven als deze schaal"
				+ " gebruikt wordt bij de berekening van gemiddelden. Klik nogmaals op opslaan om "
				+ "toch op te slaan.");
			prevErrorKey = errorKey;
		}
	}
}
