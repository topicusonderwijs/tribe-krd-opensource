package nl.topicus.eduarte.web.components.panels.organisatielocatie;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenPanel;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.DatumKleinerOfGelijkDatumValidator;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.opleiding.ITeamEntiteit;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.web.components.choice.LocatieCombobox;
import nl.topicus.eduarte.web.components.choice.SecureOrganisatieEenheidLocatieFormChoicePanel;
import nl.topicus.eduarte.web.components.choice.TeamCombobox;
import nl.topicus.eduarte.web.components.choice.SecureOrganisatieEenheidLocatieChoicePanel.OrganisatieEenheidLocatieRequired;
import nl.topicus.eduarte.web.validators.OrganisatieEenheidLocatieKoppelEntiteitValidator;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.IModel;

/**
 * Panel voor het toevoegen van een {@link IOrganisatieEenheidLocatieKoppelEntiteit}.
 * 
 * @author hoeve
 */
public class OrganisatieEenheidLocatieEntiteitModalWindowPanel<T extends IOrganisatieEenheidLocatieKoppelEntiteit<T>>
		extends AbstractToevoegenBewerkenModalWindowPanel<T>
{
	private static final long serialVersionUID = 1L;

	private DatumField begindatumField;

	private DatumField einddatumField;

	private WebMarkupContainer teamContainer;

	private WebMarkupContainer beginEindDatumContainer;

	private final IModel< ? extends List<T>> model;

	public OrganisatieEenheidLocatieEntiteitModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<T> modalWindow,
			AbstractToevoegenBewerkenPanel<T> editPanel, IModel< ? extends List<T>> orgEhdmodel,
			boolean locatieRequired)
	{
		super(id, modalWindow, editPanel);
		this.model = orgEhdmodel;

		SecureOrganisatieEenheidLocatieFormChoicePanel<T> choicepanel =
			new SecureOrganisatieEenheidLocatieFormChoicePanel<T>("organisatieEenheidLocatie",
				modalWindow.getModel(), locatieRequired ? OrganisatieEenheidLocatieRequired.Beide
					: OrganisatieEenheidLocatieRequired.OrganisatieEenheid);

		choicepanel.setRenderBodyOnly(true);
		LocatieCombobox cBox = choicepanel.getLocatieCombo();
		cBox.add(new OrganisatieEenheidLocatieKoppelEntiteitValidator<Locatie, T>(model));
		getFormContainer().add(choicepanel);

		beginEindDatumContainer = new WebMarkupContainer("beginEindDatum");
		getFormContainer().add(beginEindDatumContainer);
		begindatumField = new DatumField("begindatum");
		begindatumField.setRequired(true);
		einddatumField = new DatumField("einddatum");
		beginEindDatumContainer.add(begindatumField);
		beginEindDatumContainer.add(einddatumField);

		teamContainer = new WebMarkupContainer("teamContainer");
		TeamCombobox teamCombo = new TeamCombobox("team");
		teamCombo.setNullValid(true).setRequired(false);
		teamContainer.add(teamCombo);
		getFormContainer().add(teamContainer);

		createComponents();
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		if (!(getDefaultModelObject() instanceof BeginEinddatumInstellingEntiteit))
			beginEindDatumContainer.setVisible(false);
		getForm().add(
			new BegindatumVoorEinddatumValidator(begindatumField, einddatumField,
				"BegindatumVoorEinddatumGeldigValidator.error"));
		if (!(getDefaultModelObject() instanceof ITeamEntiteit))
			teamContainer.setVisible(false);

		/**
		 * validatie voor meerdere locaties op dezelfde organisatieeenheid validatie op
		 * begin en einddatum van het form en de organisatie-eenheid, deze kunnen allen
		 * null zijn, hier wordt op gecontroleerd.
		 */
		getForm().add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(Form< ? > _form)
			{
				T entiteit = getModalWindow().getModelObject();
				if (entiteit.getOrganisatieEenheid() != null)
				{

					Date orgBeginDatum = entiteit.getOrganisatieEenheid().getBegindatum();
					Date orgEindDatum = entiteit.getOrganisatieEenheid().getEinddatum();
					if (entiteit instanceof BeginEinddatumInstellingEntiteit)
					{
						if (orgEindDatum != null)
						{
							DatumKleinerOfGelijkDatumValidator beginEindVal =
								new DatumKleinerOfGelijkDatumValidator("Geldig vanaf",
									getBegindatumField(), orgEindDatum);
							beginEindVal.validate(_form);
							DatumKleinerOfGelijkDatumValidator eindEindVal =
								new DatumKleinerOfGelijkDatumValidator("Geldig tot",
									getEinddatumField(), orgEindDatum);
							eindEindVal.validate(_form);
						}

						if (orgBeginDatum != null)
						{
							DatumGroterOfGelijkDatumValidator eindBeginVal =
								new DatumGroterOfGelijkDatumValidator("Geldig vanaf",
									getBegindatumField(), orgBeginDatum);
							eindBeginVal.validate(_form);
						}
					}
				}

			}

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				return null;
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
