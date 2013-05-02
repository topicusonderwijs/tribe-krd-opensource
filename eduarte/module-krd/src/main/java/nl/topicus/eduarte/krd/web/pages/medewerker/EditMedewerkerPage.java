/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.medewerker;

import static nl.topicus.cobra.transformers.HoofdletterMode.*;
import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.transformers.HoofdletterMode;
import nl.topicus.cobra.transformers.HoofdletterTransformer;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentValidatingBehavior;
import nl.topicus.cobra.web.behaviors.HoofdletterAjaxHandler;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.BehaviorModifier;
import nl.topicus.cobra.web.components.form.modifier.EditorClassModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.HtmlClassModifier;
import nl.topicus.cobra.web.components.form.modifier.MultiFieldValueTransformModifier;
import nl.topicus.cobra.web.components.form.modifier.PseudoFieldModifier;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.quicksearch.SearchEditorSelectBehavior;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.BsnValidator;
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.OrganisatieMedewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.vrijevelden.PersoonVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.principals.medewerker.MedewerkerPersonaliaWrite;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.krd.web.pages.intake.stap1.VoornamenNaarVoorlettersAjaxHandler;
import nl.topicus.eduarte.web.components.menu.MedewerkerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenEditPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitEditPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ProbeerTeVerwijderenButton;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.medewerker.AbstractMedewerkerPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerZoekenPage;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidator;

/**
 * @author idserda
 */
@PageInfo(title = "Personalia Bewerken", menu = {"Deelnemer > [deelnemer]",
	"Groep > [groep] > [deelnemer]"})
@InPrincipal(MedewerkerPersonaliaWrite.class)
public class EditMedewerkerPage extends AbstractMedewerkerPage implements
		IModuleEditPage<Medewerker>
{
	private VoornamenNaarVoorlettersAjaxHandler voornamenNaarVoorletters;

	private AutoFieldSet<Medewerker> personalia;

	private boolean aanspreeknaamWijktAf = false;

	private Form<Void> form;

	private SecurePage returnToPage;

	public EditMedewerkerPage(Medewerker medewerker, SecurePage returnToPage)
	{
		super(MedewerkerMenuItem.Personalia, medewerker);

		this.returnToPage = returnToPage;

		ModelManager manager =
			new DefaultModelManager(OrganisatieMedewerker.class, PersoonAdres.class, Adres.class,
				VrijVeldOptieKeuze.class, PersoonVrijVeld.class, PersoonContactgegeven.class,
				Medewerker.class, Persoon.class);
		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(medewerker, manager));

		form = new Form<Void>("personaliaForm");
		personalia = createPersonaliaAutoFieldset("inputFieldsPersonalia");
		form.add(personalia);

		AdressenEditPanel<PersoonAdres, Persoon> adressenPanel =
			new AdressenEditPanel<PersoonAdres, Persoon>("adresEditTabs",
				new PropertyModel<Persoon>(getDefaultModel(), "persoon"), manager, false);
		form.add(adressenPanel);

		form.add(new ContactgegevenEntiteitEditPanel<PersoonContactgegeven, Persoon>(
			"contactgegevens", new PropertyModel<Persoon>(getDefaultModel(), "persoon")));

		VrijVeldEntiteitEditPanel<Persoon> VVEEPanel =
			new VrijVeldEntiteitEditPanel<Persoon>("vrijVelden", new PropertyModel<Persoon>(
				getDefaultModel(), "persoon"));
		VVEEPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.MEDEWERKERPERSONALIA);
		form.add(VVEEPanel);

		add(form);

		createComponents();
	}

	private AutoFieldSet<Medewerker> createPersonaliaAutoFieldset(String id)
	{
		AutoFieldSet<Medewerker> fieldsetpersonalia =
			new AutoFieldSet<Medewerker>(id, getContextMedewerkerModel(), "Personalia");
		fieldsetpersonalia.setPropertyNames("persoon.bsn", "afkorting",
			"persoon.officieleAchternaam", "persoon.officieleVoorvoegsel", "aanspreeknaamWijktAf",
			"persoon.achternaam", "persoon.voorvoegsel", "persoon.voornamen",
			"persoon.voorletters", "persoon.roepnaam", "persoon.geslacht", "persoon.geboortedatum");

		fieldsetpersonalia.setSortAccordingToPropertyNames(true);
		fieldsetpersonalia.setRenderMode(RenderMode.EDIT);

		fieldsetpersonalia.addFieldModifier(new EditorClassModifier(DatumField.class,
			"persoon.geboortedatum"));
		fieldsetpersonalia.addFieldModifier(new HtmlClassModifier("unit_80",
			"persoon.geboortedatum"));
		addModifier(fieldsetpersonalia, "persoon.voorletters", new HoofdletterAjaxHandler(
			HoofdletterMode.PuntSeperated));

		fieldsetpersonalia.addFieldModifier(new PseudoFieldModifier<Medewerker, Boolean>(
			Medewerker.class, "aanspreeknaamWijktAf", Boolean.TYPE, new PropertyModel<Boolean>(
				this, "aanspreeknaamWijktAf")));
		fieldsetpersonalia.addFieldModifier(new EnableModifier(new PropertyModel<Boolean>(this,
			"aanspreeknaamWijktAf"), "persoon.achternaam", "persoon.voorvoegsel"));

		fieldsetpersonalia.addModifier("aanspreeknaamWijktAf",
			new AjaxFormComponentUpdatingBehavior("onclick")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					Component achternaamVeld = personalia.findFieldComponent("persoon.achternaam");
					Component voorvoegselVeld =
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

					}
					target.addComponent(achternaamVeld);
					target.addComponent(voorvoegselVeld);
				}
			});
		fieldsetpersonalia.addFieldModifier(new BehaviorModifier(new SearchEditorSelectBehavior()
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

		fieldsetpersonalia.addFieldModifier(new MultiFieldValueTransformModifier(
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

		fieldsetpersonalia.addModifier("persoon.achternaam", new HoofdletterAjaxHandler(
			EersteLetterEenWoord));

		addModifier(fieldsetpersonalia, "persoon.roepnaam", new HoofdletterAjaxHandler(
			HoofdletterMode.ElkWoord));
		addModifier(fieldsetpersonalia, "persoon.bsn", new AjaxFormComponentValidatingBehavior(
			"onchange"));
		addModifier(fieldsetpersonalia, "persoon.bsn", new BsnValidator());
		addModifier(fieldsetpersonalia, "persoon.bsn", new BsnInGebruikValidator<Long>(
			new PropertyModel<Persoon>(getDefaultModel(), "persoon")));

		voornamenNaarVoorletters = new VoornamenNaarVoorlettersAjaxHandler();
		addModifier(fieldsetpersonalia, "persoon.voornamen", voornamenNaarVoorletters);
		addModifier(fieldsetpersonalia, "afkorting", new HoofdletterAjaxHandler(
			HoofdletterMode.Alles));

		UniqueConstraintFormValidator afkortingValidator =
			new UniqueConstraintFormValidator(fieldsetpersonalia, "medewerker", "afkorting");
		afkortingValidator.setProperties("organisatie", "begindatum");
		form.add(afkortingValidator);

		return fieldsetpersonalia;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				((IChangeRecordingModel< ? >) EditMedewerkerPage.this.getDefaultModel())
					.saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(returnToPage);
			}

		});
		panel.addButton(new AnnulerenButton(panel, returnToPage));
		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextMedewerkerModel(),
			"deze medewerker", MedewerkerZoekenPage.class));
	}

	private void addModifier(AutoFieldSet<Medewerker> fields, String property, IBehavior behavior)
	{
		fields.addFieldModifier(new BehaviorModifier(behavior, property));
	}

	private void addModifier(AutoFieldSet<Medewerker> fields, String property,
			IValidator< ? > validator)
	{
		fields.addFieldModifier(new ValidateModifier(validator, property));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		if (!form.hasBeenRendered())
		{
			TextField<String> voorletters =
				(TextField<String>) personalia.getFieldProperties("persoon.voorletters")
					.getComponent();

			voornamenNaarVoorletters.setVoorlettersField(voorletters);
		}
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachFields(this);
	}
}
