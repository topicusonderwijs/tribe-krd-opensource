package nl.topicus.eduarte.krd.web.validators;

import java.util.Date;

import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.components.quicksearch.opleiding.OpleidingSearchEditor;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

public class IngangsdatumOpleidingNaBegindatumValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private OpleidingSearchEditor opleidingSearchEditor;

	private DatumField ingangsdatum;

	public IngangsdatumOpleidingNaBegindatumValidator(OpleidingSearchEditor opleidingSearchEditor,
			DatumField ingangsdatum)
	{
		this.opleidingSearchEditor = opleidingSearchEditor;
		this.ingangsdatum = ingangsdatum;
	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		return new FormComponent[] {opleidingSearchEditor, ingangsdatum};
	}

	@Override
	public void validate(Form< ? > form)
	{
		if (opleidingSearchEditor.getConvertedInput() != null && ingangsdatum.getDatum() != null)
		{
			Opleiding opleiding = opleidingSearchEditor.getConvertedInput();
			Date begindatum = ingangsdatum.getDatum();
			if (!opleiding.isActief(begindatum))
			{
				error(opleidingSearchEditor);
			}
		}

	}
}