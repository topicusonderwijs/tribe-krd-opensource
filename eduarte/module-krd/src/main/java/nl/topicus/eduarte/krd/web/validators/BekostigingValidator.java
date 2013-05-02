package nl.topicus.eduarte.krd.web.validators;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.krd.web.components.choice.IntensiteitCombobox;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

public class BekostigingValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private IntensiteitCombobox intensiteitCombobox;

	private EnumCombobox<Bekostigd> bekostigdCombobox;

	public BekostigingValidator(IntensiteitCombobox intensiteitCombobox,
			EnumCombobox<Bekostigd> bekostigdCombobox)
	{
		this.intensiteitCombobox = intensiteitCombobox;
		this.bekostigdCombobox = bekostigdCombobox;
	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		return new FormComponent[] {intensiteitCombobox, bekostigdCombobox};
	}

	@Override
	public void validate(Form< ? > form)
	{
		if (intensiteitCombobox.getConvertedInput() != null
			&& intensiteitCombobox.getConvertedInput().equals(Intensiteit.Examendeelnemer))
		{
			if (bekostigdCombobox.getConvertedInput() != null
				&& !bekostigdCombobox.getConvertedInput().equals(Bekostigd.Nee))
			{
				error(bekostigdCombobox);
			}
		}

	}
}