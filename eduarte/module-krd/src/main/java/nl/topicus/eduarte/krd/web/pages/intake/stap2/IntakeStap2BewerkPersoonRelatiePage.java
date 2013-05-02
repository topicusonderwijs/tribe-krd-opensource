package nl.topicus.eduarte.krd.web.pages.intake.stap2;

import java.util.ArrayList;
import java.util.Arrays;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.transformers.HoofdletterMode;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentValidatingBehavior;
import nl.topicus.cobra.web.behaviors.HoofdletterAjaxHandler;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.validators.BsnValidator;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.entities.personen.AbstractRelatie;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardModel;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardPage;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenEditPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitEditPanel;
import nl.topicus.eduarte.zoekfilters.RelatieSoortZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Intake stap 2 - Persoon als relatie toevoegen", menu = {"Deelnemer > intake"})
public class IntakeStap2BewerkPersoonRelatiePage extends IntakeWizardPage
{
	private Form<Void> personaliaForm;

	private AdressenEditPanel<PersoonAdres, Persoon> adressenpanel;

	private AutoFieldSet<AbstractRelatie> personalia;

	private IModel<AbstractRelatie> relatieModel;

	private boolean adressenGelijkMakenAanDeelnemer = true;

	private boolean relatieIsNieuw;

	public IntakeStap2BewerkPersoonRelatiePage(IntakeWizardModel wizard, Relatie relatie,
			boolean relatieIsNieuw)
	{
		setWizard(wizard);

		this.relatieIsNieuw = relatieIsNieuw;
		personaliaForm = new Form<Void>("personaliaForm");
		add(personaliaForm);
		this.relatieModel = wizard.getModel(relatie);

		adressenpanel =
			new AdressenEditPanel<PersoonAdres, Persoon>("adressen", new PropertyModel<Persoon>(
				relatieModel, "relatie"), getManager())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return !adressenGelijkMakenAanDeelnemer;
				}
			};
		adressenpanel.setOutputMarkupPlaceholderTag(true);

		createPersonaliaFields(personaliaForm);
		createAdressenFields(personaliaForm);
		createContactGegevenFields(personaliaForm);
		createVrijVeldenFields(personaliaForm);

		createComponents();
	}

	private void createPersonaliaFields(Form<Void> form)
	{
		ArrayList<String> fields = new ArrayList<String>();
		fields.addAll(Arrays.asList("relatie.achternaam", "relatie.voorvoegsel",
			"relatie.voorletters", "relatie.geslacht", "relatie.bsn", "relatie.geboorteland",
			"relatie.nationaliteit1", "relatieSoort"));

		if (!getWizard().getDeelnemer().getPersoon().isMeerderjarig())
		{
			fields.addAll(Arrays.asList("wettelijkeVertegenwoordiger", "betalingsplichtige"));
		}

		fields.add("relatie.debiteurennummer");
		fields.add("relatie.bankrekeningnummer");

		personalia = new AutoFieldSet<AbstractRelatie>("personalia", relatieModel, "Personalia");
		form.add(personalia);
		personalia.setPropertyNames(fields);
		personalia.setSortAccordingToPropertyNames(true);
		personalia.setRenderMode(RenderMode.EDIT);

		RelatieSoortZoekFilter filter = new RelatieSoortZoekFilter();
		filter.setPersoonOpname(true);
		personalia.addFieldModifier(new ConstructorArgModifier("relatieSoort", filter));

		personalia.addFieldModifier(new RequiredModifier(true, "soort"));

		personalia.addModifier("relatie.voorletters", new HoofdletterAjaxHandler(
			HoofdletterMode.PuntSeperated));
		personalia.addModifier("relatie.achternaam", new HoofdletterAjaxHandler(
			HoofdletterMode.EersteLetterEenWoord));

		personalia.addModifier("relatie.bsn", new AjaxFormComponentValidatingBehavior("onchange"));
		personalia.addModifier("relatie.bsn", new BsnValidator());
		personalia.addModifier("persoon.bsn", new UniqueConstraintValidator<Long>(personalia,
			"relatie", "persoon.bsn", "organisatie"));
	}

	private void createAdressenFields(Form<Void> form)
	{
		form.add(adressenpanel);
		form.add(new CheckBox("adressenGelijkDeelnemer", new PropertyModel<Boolean>(
			IntakeStap2BewerkPersoonRelatiePage.this, "adressenGelijkMakenAanDeelnemer"))
			.add(new AjaxFormComponentUpdatingBehavior("onclick")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					Persoon updateVerzorger = getRelatie().getRelatie();
					updateVerzorger.getAdressen().clear();
					if (adressenGelijkMakenAanDeelnemer)
					{
						getWizard().kopieerAdressenVanDeelnemerNaarRelatie(
							getRelatie().getRelatie());

					}
					target.addComponent(getComponent());
					target.addComponent(adressenpanel);
				}
			}));
	}

	private void createContactGegevenFields(Form<Void> form)
	{
		form.add(new ContactgegevenEntiteitEditPanel<PersoonContactgegeven, Persoon>(
			"contactgegevens", new PropertyModel<Persoon>(relatieModel, "relatie")));
	}

	private void createVrijVeldenFields(Form<Void> form)
	{
		VrijVeldEntiteitEditPanel<Persoon> VVEEPanel =
			new VrijVeldEntiteitEditPanel<Persoon>("vrijVelden", new PropertyModel<Persoon>(
				relatieModel, "relatie"));
		VVEEPanel.getVrijVeldZoekFilter().setIntakeScherm(true);
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.RELATIE);
		form.add(VVEEPanel);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);

		voegToevoegenKnopToe(panel);
		voegRelatieAnnulerenKnopToe(panel);
		voegVerwijderenKnopToe(panel);
	}

	private void voegVerwijderenKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new VerwijderButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return !relatieIsNieuw;
			}

			@Override
			protected void onClick()
			{
				IntakeWizardModel wizard = getWizard();
				Relatie relatie = getRelatie();
				wizard.removeRelatie(relatie);
				setResponsePage(new IntakeStap2Achtergrond(wizard));
			}
		});
	}

	private void voegToevoegenKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new AbstractBottomRowButton(panel, "Opslaan", null, ButtonAlignment.RIGHT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected WebMarkupContainer getLink(String linkId)
			{
				SubmitLink submitLink = new SubmitLink(linkId, personaliaForm)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onSubmit()
					{
						IntakeWizardModel wizard = getWizard();
						if (getRelatie().isBetalingsplichtige())
							wizard.setBetalingsplichtigeRelatie(getRelatie());
						setResponsePage(new IntakeStap2Achtergrond(wizard));
					}
				};
				personaliaForm.setDefaultButton(submitLink);
				return submitLink;
			}
		});
	}

	private void voegRelatieAnnulerenKnopToe(BottomRowPanel panel)
	{
		panel
			.addButton(new AbstractBottomRowButton(panel, "Annuleren", null, ButtonAlignment.RIGHT)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected WebMarkupContainer getLink(String linkId)
				{
					return new Link<Void>(linkId)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick()
						{
							IntakeWizardModel wizard = getWizard();
							if (relatieIsNieuw)
							{
								Relatie relatie = getRelatie();
								// model op null zetten voorkomt probleem bij dubbelklik
								// (mantis 43834)
								relatieModel = new Model<AbstractRelatie>(null);
								wizard.removeRelatie(relatie);
							}
							setResponsePage(new IntakeStap2Achtergrond(wizard));
						}
					};
				}
			});
	}

	private Relatie getRelatie()
	{
		return ((Relatie) relatieModel.getObject());
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachFields(this);
		super.onDetach();
	}

	@Override
	protected int getStapNummer()
	{
		return 2;
	}

	@Override
	protected String getStapTitel()
	{
		return "Relatie toevoegen (persoon)";
	}
}
