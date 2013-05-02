package nl.topicus.eduarte.web.components.modalwindow.schaalwaarde;

import java.math.BigDecimal;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

public class SchaalwaardeModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<Schaalwaarde>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<Schaalwaarde> schaalwaardeFieldSet;

	public SchaalwaardeModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<Schaalwaarde> modalWindow,
			SchaalwaardeEditPanel schaalwaardeEditPanel)
	{
		super(id, modalWindow, schaalwaardeEditPanel);

		schaalwaardeFieldSet =
			new AutoFieldSet<Schaalwaarde>("schaalwaarde", modalWindow.getModel(), "Schaalwaarde");
		schaalwaardeFieldSet.setPropertyNames("naam", "volgnummer", "interneWaarde",
			"externeWaarde", "behaald", "vanafCijfer", "totCijfer", "nominaleWaarde");
		schaalwaardeFieldSet.setRenderMode(RenderMode.EDIT);
		schaalwaardeFieldSet.setSortAccordingToPropertyNames(true);
		schaalwaardeFieldSet.addFieldModifier(new ValidateModifier(new AbstractValidator<Integer>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<Integer> validatable)
			{
				int inpVolgnummer = validatable.getValue();
				for (Schaalwaarde curWaarde : getSchaalwaarde().getSchaal().getSchaalwaarden())
				{
					if (!curWaarde.equals(getSchaalwaarde())
						&& curWaarde.getVolgnummer() == inpVolgnummer)
					{
						ValidationError error = new ValidationError();
						error.setMessage("Het volgnummer is reeds in gebruik binnen de schaal.");
						validatable.error(error);
						break;
					}
				}
			}
		}, "volgnummer"));
		schaalwaardeFieldSet.addFieldModifier(new ValidateModifier(new AbstractValidator<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<String> validatable)
			{
				String inpInterneWaarde = validatable.getValue();
				for (Schaalwaarde curWaarde : getSchaalwaarde().getSchaal().getSchaalwaarden())
				{
					if (!curWaarde.equals(getSchaalwaarde())
						&& curWaarde.getInterneWaarde().equals(inpInterneWaarde))
					{
						ValidationError error = new ValidationError();
						error.setMessage("De interne waarde is reeds in gebruik binnen de schaal.");
						validatable.error(error);
						break;
					}
				}
			}
		}, "interneWaarde"));
		getFormContainer().add(schaalwaardeFieldSet);

		modalWindow.setInitialHeight(300);
		modalWindow.setInitialWidth(475);

		createComponents();
	}

	@SuppressWarnings("unchecked")
	private TextField<BigDecimal> findVanafCijferField()
	{
		return (TextField<BigDecimal>) schaalwaardeFieldSet.findFieldComponent("vanafCijfer");
	}

	@SuppressWarnings("unchecked")
	private TextField<BigDecimal> findTotCijferField()
	{
		return (TextField<BigDecimal>) schaalwaardeFieldSet.findFieldComponent("totCijfer");
	}

	@SuppressWarnings("unchecked")
	private TextField<BigDecimal> findNominaleWaardeField()
	{
		return (TextField<BigDecimal>) schaalwaardeFieldSet.findFieldComponent("nominaleWaarde");
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		getForm().add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				return new FormComponent[] {findVanafCijferField(), findTotCijferField()};
			}

			@Override
			public void validate(Form< ? > validateForm)
			{
				BigDecimal vanaf = findVanafCijferField().getConvertedInput();
				BigDecimal tot = findTotCijferField().getConvertedInput();
				if ((vanaf == null) != (tot == null))
				{
					findVanafCijferField().error(
						"'Vanaf cijfer' en 'T/m cijfer' dienen "
							+ "beiden of wel of niet ingevuld te zijn.");
				}
				else if (vanaf != null && tot != null && vanaf.compareTo(tot) > 0)
				{
					findVanafCijferField().error(
						"'Vanaf cijfer' mag niet groter zijn dan 'T/m cijfer'.");
				}
			}
		});
		getForm().add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				return new FormComponent[] {findVanafCijferField(), findTotCijferField(),
					findNominaleWaardeField()};
			}

			@Override
			public void validate(Form< ? > validateForm)
			{
				BigDecimal vanaf = findVanafCijferField().getConvertedInput();
				BigDecimal tot = findTotCijferField().getConvertedInput();
				BigDecimal nominaal = findNominaleWaardeField().getConvertedInput();
				if (vanaf != null && tot != null)
				{
					if (vanaf.compareTo(nominaal) > 0 || tot.compareTo(nominaal) < 0)
						findNominaleWaardeField().error(
							"'Nominale waarde' moet tussen 'Vanaf cijfer' en "
								+ "'T/m cijfer' liggen.");
				}
			}
		});
	}

	private Schaalwaarde getSchaalwaarde()
	{
		return schaalwaardeFieldSet.getModelObject();
	}
}
