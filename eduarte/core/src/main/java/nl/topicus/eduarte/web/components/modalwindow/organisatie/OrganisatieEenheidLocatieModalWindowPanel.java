package nl.topicus.eduarte.web.components.modalwindow.organisatie;

import java.util.Date;

import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.DatumKleinerOfGelijkDatumValidator;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidLocatie;
import nl.topicus.eduarte.web.components.choice.LocatieCombobox;
import nl.topicus.eduarte.web.pages.beheer.organisatie.OrganisatieEenheidModel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Panel voor het toevoegen & bewerken van een {@link OrganisatieEenheidLocatie}. Niet te
 * verwarren met een {@link IOrganisatieEenheidLocatieKoppelEntiteit}.
 * 
 * @author hoeve
 */
public class OrganisatieEenheidLocatieModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<OrganisatieEenheidLocatie>
{
	private static final long serialVersionUID = 1L;

	private DatumField begindatumField;

	private DatumField einddatumField;

	private IModel<OrganisatieEenheid> organisatieEenheidModel;

	private IModel<Locatie> locatieModel;

	public OrganisatieEenheidLocatieModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<OrganisatieEenheidLocatie> modalWindow,
			OrganisatieEenheidLocatieEditPanel organisatieEenheidLocatieEditPanel,
			OrganisatieEenheidModel organisatieEenheidModel)
	{
		super(id, modalWindow, organisatieEenheidLocatieEditPanel);
		this.organisatieEenheidModel = organisatieEenheidModel;
		locatieModel = new PropertyModel<Locatie>(modalWindow.getDefaultModel(), "locatie");
		LocatieListModel listModel = new LocatieListModel(organisatieEenheidModel);
		LocatieCombobox combobox = new LocatieCombobox("locatie", locatieModel);
		combobox.setChoices(listModel);
		combobox.setRequired(true);

		begindatumField =
			new DatumField("begindatum", new PropertyModel<Date>(modalWindow.getDefaultModel(),
				"begindatum"));
		begindatumField.setRequired(true);

		einddatumField =
			new DatumField("einddatum", new PropertyModel<Date>(modalWindow.getDefaultModel(),
				"einddatum"));

		getFormContainer().add(begindatumField);
		getFormContainer().add(einddatumField);
		getFormContainer().add(combobox);

		createComponents();
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		getForm().add(new BegindatumVoorEinddatumValidator(begindatumField, einddatumField));
		getForm().add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(Form< ? > _form)
			{
				Locatie locatie = locatieModel.getObject();
				if (locatie != null)
				{
					Date locBeginDatum = locatie.getBegindatum();
					Date locEindDatum = locatie.getEinddatum();

					if (locEindDatum != null)
					{
						DatumKleinerOfGelijkDatumValidator beginEindVal =
							new DatumKleinerOfGelijkDatumValidator("Begindatum",
								getBegindatumField(), locEindDatum);
						beginEindVal.validate(_form);
						DatumKleinerOfGelijkDatumValidator eindEindVal =
							new DatumKleinerOfGelijkDatumValidator("Einddatum",
								getEinddatumField(), locEindDatum);
						eindEindVal.validate(_form);
					}

					if (locBeginDatum != null)
					{
						DatumGroterOfGelijkDatumValidator eindBeginVal =
							new DatumGroterOfGelijkDatumValidator("Begindatum",
								getBegindatumField(), locBeginDatum);
						eindBeginVal.validate(_form);
					}
				}

			}

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				FormComponent< ? >[] formComponents = {begindatumField, einddatumField};
				return formComponents;
			}
		});

		getForm().add(new AbstractFormValidator()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				FormComponent< ? >[] formComponents = {begindatumField, einddatumField};
				return formComponents;
			}

			@Override
			public void validate(Form< ? > _form)
			{
				OrganisatieEenheid orgEenheid = organisatieEenheidModel.getObject();
				if (orgEenheid != null)
				{
					Date orgBeginDatum = orgEenheid.getBegindatum();
					Date orgEindDatum = orgEenheid.getEinddatum();

					if (orgEindDatum != null)
					{
						DatumKleinerOfGelijkDatumValidator beginEindVal =
							new DatumKleinerOfGelijkDatumValidator("Begindatum",
								getBegindatumField(), orgEindDatum);
						beginEindVal.validate(_form);
						DatumKleinerOfGelijkDatumValidator eindEindVal =
							new DatumKleinerOfGelijkDatumValidator("Einddatum",
								getEinddatumField(), orgEindDatum);
						eindEindVal.validate(_form);
					}

					if (orgBeginDatum != null)
					{
						DatumGroterOfGelijkDatumValidator eindBeginVal =
							new DatumGroterOfGelijkDatumValidator("Begindatum",
								getBegindatumField(), orgBeginDatum);
						eindBeginVal.validate(_form);
					}
				}
			}

		});
	}

	public DatumField getBegindatumField()
	{
		return begindatumField;
	}

	public void setBegindatumField(DatumField begindatumField)
	{
		this.begindatumField = begindatumField;
	}

	public DatumField getEinddatumField()
	{
		return einddatumField;
	}

	public void setEinddatumField(DatumField einddatumField)
	{
		this.einddatumField = einddatumField;
	}

}
