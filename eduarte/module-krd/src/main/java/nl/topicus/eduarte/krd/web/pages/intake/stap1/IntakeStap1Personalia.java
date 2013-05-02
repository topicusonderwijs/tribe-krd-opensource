package nl.topicus.eduarte.krd.web.pages.intake.stap1;

import static nl.topicus.cobra.transformers.HoofdletterMode.*;
import static nl.topicus.cobra.web.components.form.RenderMode.*;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.converters.PostcodeConverter;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.transformers.HoofdletterMode;
import nl.topicus.cobra.transformers.HoofdletterTransformer;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentValidatingBehavior;
import nl.topicus.cobra.web.behaviors.HoofdletterAjaxHandler;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.modifier.BehaviorModifier;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.MultiFieldValueTransformModifier;
import nl.topicus.cobra.web.components.form.modifier.PseudoFieldModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VolgendeButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VorigeButton;
import nl.topicus.cobra.web.components.quicksearch.SearchEditorSelectBehavior;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.cobra.web.validators.BankrekeningElfProefValidator;
import nl.topicus.cobra.web.validators.BsnValidator;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.DatumInVerledenValidator;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.dao.helpers.PersoonDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.personen.Persoon.ToepassingGeboortedatum;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronValidatingFormComponent;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardModel;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardPage;
import nl.topicus.eduarte.krd.web.pages.intake.stap0.IntakeStap0Zoeken;
import nl.topicus.eduarte.krd.web.pages.intake.stap2.IntakeStap2Achtergrond;
import nl.topicus.eduarte.krd.web.validators.GeboortedatumMinMaxWarningValidator;
import nl.topicus.eduarte.providers.PersoonProvider;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenEditPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitEditPanel;
import nl.topicus.eduarte.web.components.quicksearch.plaats.PlaatsSearchEditor;
import nl.topicus.eduarte.web.components.text.GeboortedatumField;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Intake stap 1 voor het invoeren van personalia gegevens van een deelnemer.
 */
@PageInfo(title = "Intake stap 1 van 4", menu = {"Deelnemer > intake"})
@RequiredSecurityCheck(NietOverledenSecurityCheck.class)
public class IntakeStap1Personalia extends IntakeWizardPage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private Form<Deelnemer> form;

	private AutoFieldSet<Deelnemer> personalia;

	private VoornamenNaarVoorlettersAjaxHandler voornamenNaarVoorletters;

	private AutoFieldSet<IntakeWizardModel> algemeen;

	private boolean aanspreeknaamWijktAf = false;

	private boolean volgende;

	public IntakeStap1Personalia()
	{
		this(new IntakeWizardModel(getIngelogdeMedewerker()));
	}

	public IntakeStap1Personalia(IntakeWizardModel wizardmodel)
	{
		setWizard(wizardmodel);
		createComponents();
	}

	/**
	 * Voer herintake van deze deelnemer uit
	 */
	public IntakeStap1Personalia(Deelnemer deelnemer, Page returnPage)
	{
		IntakeWizardModel wizardmodel = new IntakeWizardModel(deelnemer, getIngelogdeMedewerker());
		setWizard(wizardmodel);
		wizardmodel.setReturnPage(returnPage);
		createComponents();
	}

	public IntakeStap1Personalia(Deelnemer deelnemer, VerbintenisZoekFilter stap0Filter)
	{
		this(deelnemer, (Page) null);
		getWizard().setStap0Filter(stap0Filter);
	}

	public IntakeStap1Personalia(VerbintenisZoekFilter stap0Filter)
	{
		this();

		getWizard().setStap0Filter(stap0Filter);
		String achternaam = stap0Filter.getAchternaam();
		Date geboortedatum = stap0Filter.getGeboortedatum();
		Long bsn = stap0Filter.getBsn();
		final String postcode = stap0Filter.getPostcode();

		Persoon persoon = getWizard().getDeelnemer().getPersoon();
		if (!StringUtil.isEmpty(achternaam))
		{
			String hoofdletterAchternaam =
				new HoofdletterTransformer(HoofdletterMode.EersteLetterEenWoord)
					.transform(achternaam);
			persoon.setOfficieleAchternaam(hoofdletterAchternaam);
			persoon.setAchternaam(hoofdletterAchternaam);
		}
		persoon.setBsn(bsn);
		persoon.setGeboortedatum(geboortedatum);
		// de net aangemaakte persoon heeft 1 adres, dit is het huidige post en woonadres
		Date peildatum = TimeUtil.getInstance().currentDate();
		if (StringUtil.isNotEmpty(postcode) && persoon.getFysiekAdres(peildatum) != null
			&& persoon.getFysiekAdres(peildatum).getAdres() != null)
		{
			if (Pattern.matches(PostcodeConverter.POSTCODE_NL_REGEX, postcode))
				persoon.getFysiekAdres(peildatum).getAdres().setPostcode(postcode);
		}
	}

	@Override
	protected void createComponents()
	{
		add(form =
			new Form<Deelnemer>("personaliaForm", new PropertyModel<Deelnemer>(this,
				"wizard.deelnemer"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit()
				{
					if (volgende)
					{
						volgende = false; // voor de volgende keer

						getWizard().startStap2();
						setResponsePage(new IntakeStap2Achtergrond(getWizard()));
					}
				}
			});

		voegAlgemeenVeldenToeAanForm(form);
		voegPersonaliaVeldenToeAanForm(form);
		voegAdresVeldenToeAanForm(form);
		voegContactgegevensVeldenToeAanForm(form);
		voegVrijVeldVeldenToeAanForm();

		form.add(new Label("caption", EduArteApp.get().getDeelnemerTerm() + "personalia"));
		super.createComponents();
	}

	private void voegVrijVeldVeldenToeAanForm()
	{
		VrijVeldEntiteitEditPanel<Persoon> VVEEPanel =
			new VrijVeldEntiteitEditPanel<Persoon>("vrijVelden", new PropertyModel<Persoon>(this,
				"wizard.deelnemer.persoon"));
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.DEELNEMERPERSONALIA);
		VVEEPanel.getVrijVeldZoekFilter().setIntakeScherm(true);
		form.add(VVEEPanel);
	}

	@SuppressWarnings("hiding")
	private void voegAlgemeenVeldenToeAanForm(Form<Deelnemer> form)
	{
		algemeen =
			new AutoFieldSet<IntakeWizardModel>("inputFieldsAlgemeen",
				new PropertyModel<IntakeWizardModel>(this, "wizard"), "Algemeen");

		algemeen.setRenderMode(EDIT);
		algemeen.setPropertyNames("registratieDatum", "defaultOrganisatieEenheid", "begindatum");
		algemeen.addFieldModifier(new EnableModifier(!getWizard().getDeelnemer().isSaved(),
			"registratieDatum"));
		algemeen.setSortAccordingToPropertyNames(true);

		form.add(algemeen);
		form.add(new BronValidatingFormComponent("bronvalidator", this, form)
			.setRenderBodyOnly(true));
	}

	@SuppressWarnings("hiding")
	private void voegPersonaliaVeldenToeAanForm(Form<Deelnemer> form)
	{
		personalia =
			new AutoFieldSet<Deelnemer>("inputFieldsPersonalia", new PropertyModel<Deelnemer>(this,
				"wizard.deelnemer"), "Personalia");

		personalia.setRenderMode(EDIT);
		personalia.setSortAccordingToPropertyNames(true);
		personalia.setPropertyNames("persoon.bsn", "deelnemernummer",
			"persoon.officieleAchternaam", "persoon.officieleVoorvoegsel", "aanspreeknaamWijktAf",
			"persoon.achternaam", "persoon.voorvoegsel", "persoon.voornamen",
			"persoon.voorletters", "persoon.roepnaam", "persoon.geslacht", "persoon.geboortedatum",
			"persoon.geboorteland", "persoon.geboorteplaats", "persoon.burgerlijkeStaat",
			"persoon.datumInNederland", "persoon.nationaliteit1", "persoon.nationaliteit2",
			"persoon.allochtoon", "indicatieGehandicapt", "allochtoon", "persoon.nieuwkomer",
			"lgf", "persoon.debiteurennummer", "persoon.bankrekeningnummer",
			"persoon.nietVerstrekkenAanDerden");

		personalia.addModifier("persoon.bankrekeningnummer", new BankrekeningElfProefValidator());
		personalia.addModifier("persoon.bsn", new AjaxFormComponentValidatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				super.onUpdate(target);
				// Controleer of er al een persoon met het ingegeven sofinummer
				// bestaat.
				Persoon persoon = ((PersoonProvider) personalia.getModelObject()).getPersoon();
				if (persoon.getBsn() != null)
				{
					List<Persoon> personen =
						DataAccessRegistry.getHelper(PersoonDataAccessHelper.class).getByBSN(
							persoon.getBsn());
					for (Persoon pson : personen)
					{
						if (!pson.equals(persoon))
						{
							// Waarschuw dat er andere personen met hetzelfde bsn
							// bestaan.
							warn("Let op: Er bestaat al een andere persoon met het BSN "
								+ persoon.getBsn() + ", namelijk " + pson.getVolledigeNaam());
						}
					}
				}
			}
		});

		personalia.addFieldModifier(new ConstructorArgModifier("persoon.geboortedatum",
			new PropertyModel<ToepassingGeboortedatum>(personalia.getModel(),
				"persoon.toepassingGeboortedatum")));
		personalia.addModifier("persoon.bsn", new BsnValidator());
		personalia.addModifier("persoon.bsn", new UniqueConstraintValidator<Long>(form,
			"deelnemer", "persoon.bsn", "organisatie"));

		personalia.addModifier("persoon.voorletters", new HoofdletterAjaxHandler(PuntSeperated));
		voornamenNaarVoorletters = new VoornamenNaarVoorlettersAjaxHandler();
		personalia.addModifier("persoon.voornamen", voornamenNaarVoorletters);

		personalia.addFieldModifier(new RequiredModifier(true, "persoon.geslacht"));
		personalia.addModifier("persoon.geslacht", new OnChangeAjaxBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				// noop, just submit
			}
		});
		personalia.addFieldModifier(new PseudoFieldModifier<Deelnemer, Boolean>(Deelnemer.class,
			"aanspreeknaamWijktAf", Boolean.TYPE, new PropertyModel<Boolean>(this,
				"aanspreeknaamWijktAf")));
		personalia.addFieldModifier(new EnableModifier(new PropertyModel<Boolean>(this,
			"aanspreeknaamWijktAf"), "persoon.achternaam", "persoon.voorvoegsel"));

		personalia.addModifier("persoon.achternaam", new HoofdletterAjaxHandler(
			EersteLetterEenWoord));

		personalia.addModifier("persoon.roepnaam", new HoofdletterAjaxHandler(ElkWoord));
		personalia.addModifier("persoon.geboortedatum", new DatumInVerledenValidator());
		personalia.addModifier("persoon.geboortedatum", new GeboortedatumMinMaxWarningValidator(
			this));

		personalia.addModifier("persoon.geboorteplaats", new HoofdletterAjaxHandler(ElkWoord));

		personalia.addFieldModifier(new ConstructorArgModifier("persoon.geboorteplaats",
			new PropertyModel<Land>(getWizard(), "deelnemer.persoon.geboorteland")));

		personalia.addFieldModifier(new EduArteAjaxRefreshModifier("persoon.geboorteland")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				PlaatsSearchEditor searchEditor =
					(PlaatsSearchEditor) personalia.findFieldComponent("persoon.geboorteplaats");
				target.addComponent(searchEditor);
			}
		});

		personalia.addModifier("aanspreeknaamWijktAf", new AjaxFormComponentUpdatingBehavior(
			"onclick")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				FormComponent< ? > achternaamVeld =
					personalia.findFieldComponent("persoon.achternaam");
				FormComponent< ? > voorvoegselVeld =
					personalia.findFieldComponent("persoon.voorvoegsel");

				if (!aanspreeknaamWijktAf)
				{
					Object officieleAchternaamValue =
						personalia.findFieldComponent("persoon.officieleAchternaam")
							.getDefaultModelObject();
					Object officieleVoorvoegselValue =
						personalia.findFieldComponent("persoon.officieleVoorvoegsel")
							.getDefaultModelObject();

					achternaamVeld.setDefaultModelObject(officieleAchternaamValue);
					voorvoegselVeld.setDefaultModelObject(officieleVoorvoegselValue);
					achternaamVeld.clearInput();
					voorvoegselVeld.clearInput();
				}
				target.addComponent(achternaamVeld);
				target.addComponent(voorvoegselVeld);
			}
		});

		personalia.addFieldModifier(new MultiFieldValueTransformModifier(
			"persoon.officieleAchternaam", "persoon.achternaam", new HoofdletterTransformer(
				HoofdletterMode.EersteLetterEenWoord), new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return !aanspreeknaamWijktAf;
				}
			}));
		personalia.addFieldModifier(new BehaviorModifier(new SearchEditorSelectBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				Component offVoorvoegsel =
					personalia.findFieldComponent("persoon.officieleVoorvoegsel");
				Component voorvoegsel = personalia.findFieldComponent("persoon.voorvoegsel");
				voorvoegsel.setDefaultModelObject(offVoorvoegsel.getDefaultModelObject());
				target.addComponent(voorvoegsel);
			}
		}, "persoon.officieleVoorvoegsel"));
		form.add(personalia);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		if (!form.hasBeenRendered())
		{
			TextField<String> voorletters =
				(TextField<String>) personalia.getFieldProperties("persoon.voorletters")
					.getComponent();

			voornamenNaarVoorletters.setVoorlettersField(voorletters);

			FormComponent< ? > registratieDatum =
				(FormComponent< ? >) algemeen.getFieldProperties("registratieDatum").getComponent();
			registratieDatum.setRequired(true);

			Component achternaamVeld = personalia.findFieldComponent("persoon.achternaam");
			achternaamVeld.setOutputMarkupPlaceholderTag(true);
			Component voorvoegselVeld = personalia.findFieldComponent("persoon.voorvoegsel");
			voorvoegselVeld.setOutputMarkupPlaceholderTag(true);

			GeboortedatumField geboortedatumVeld =
				(GeboortedatumField) personalia.findFieldComponent("persoon.geboortedatum");

			DatumField datumInNederlandVeld =
				(DatumField) personalia.findFieldComponent("persoon.datumInNederland");
			form.add(new DatumGroterOfGelijkDatumValidator(datumInNederlandVeld, geboortedatumVeld
				.getDatumField()));
		}
	}

	@SuppressWarnings("hiding")
	private void voegAdresVeldenToeAanForm(Form<Deelnemer> form)
	{
		form.add(new AdressenEditPanel<PersoonAdres, Persoon>("adresEditTabs",
			new PropertyModel<Persoon>(this, "wizard.deelnemer.persoon"), getManager()));
	}

	@SuppressWarnings("hiding")
	private void voegContactgegevensVeldenToeAanForm(Form<Deelnemer> form)
	{
		form.add(new ContactgegevenEntiteitEditPanel<PersoonContactgegeven, Persoon>(
			"contactgegevens", new PropertyModel<Persoon>(this, "wizard.deelnemer.persoon")));
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		voegVorigeKnopToe(panel);
		panel.addButton(new VolgendeButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onVolgende()
			{
				volgende = true;
			}
		});

		voegVoltooienKnopToe(panel, form);
		voegAnnulerenKnopToe(panel);
	}

	private void voegVorigeKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new VorigeButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVorige()
			{
				setResponsePage(new IntakeStap0Zoeken(getWizard().getStap0Filter()));
			}

			@Override
			public boolean isVisible()
			{
				return getWizard().getStap0Filter() != null;
			}
		});
	}

	@Override
	public boolean supportsBookmarks()
	{
		// deze specifieke pagina kan wel gebookmarked worden (start van de intake wizard)
		return true;
	}

	public AutoFieldSet<Deelnemer> getPersonalia()
	{
		return personalia;
	}

	@Override
	protected int getStapNummer()
	{
		return 1;
	}

	@Override
	protected String getStapTitel()
	{
		return "Personalia";
	}
}
