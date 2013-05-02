package nl.topicus.eduarte.krd.web.pages.medewerker;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.transformers.HoofdletterMode;
import nl.topicus.cobra.transformers.HoofdletterTransformer;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
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
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.EmptyMenu;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.quicksearch.SearchEditorSelectBehavior;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.BsnValidator;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.DatumInVerledenValidator;
import nl.topicus.cobra.web.validators.DatumKleinerOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.OrganisatieMedewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.krd.principals.medewerker.MedewerkerPersonaliaWrite;
import nl.topicus.eduarte.krd.web.pages.intake.stap1.VoornamenNaarVoorlettersAjaxHandler;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenEditPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitEditPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieLocatieTable;
import nl.topicus.eduarte.web.components.panels.organisatielocatie.OrganisatieEenheidLocatieEntiteitEditPanel;
import nl.topicus.eduarte.web.components.panels.organisatielocatie.OrganisatieEenheidLocatieEntiteitModalWindowPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerZoekenPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidator;

/**
 * 
 */
@PageInfo(title = "Nieuwe medewerker toevoegen", menu = {"Medewerker > nieuw"})
@InPrincipal(MedewerkerPersonaliaWrite.class)
public class NieuweMedewerkerPage extends SecurePage implements IModuleEditPage<Medewerker>
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private MedewerkerModel medewerkerModel;

	private AutoFieldSet<Medewerker> personalia;

	private VoornamenNaarVoorlettersAjaxHandler voornamenNaarVoorletters;

	private boolean aanspreeknaamWijktAf = false;

	private AutoFieldSet<Medewerker> fieldset;

	private DatumField geboorteDatumField;

	private DatumField begindatumField;

	private DatumField einddatumField;

	/**
	 * Nodig voor nieuw buttons
	 * 
	 * @param model
	 * @param returnPage
	 */
	public NieuweMedewerkerPage(IModel< ? > model, SecurePage returnPage)
	{
		this();
	}

	public NieuweMedewerkerPage()
	{
		super(CoreMainMenuItem.Medewerker);

		add(form = new Form<Void>("personaliaForm"));
		medewerkerModel = new MedewerkerModel(new Date());

		form.add(createFieldsetPersonalia());
		form.add(createFieldsetAanstelling());

		form.add(createFieldsetContactGegevens());
		form.add(new OrganisatieEenheidLocatieEntiteitEditPanel<OrganisatieMedewerker>(
			"panelOrganisatieMedewerker", new PropertyModel<List<OrganisatieMedewerker>>(
				medewerkerModel.getEntiteitModel(), "organisatieMedewerkers"), medewerkerModel
				.getEntiteitManager(), new OrganisatieLocatieTable<OrganisatieMedewerker>(true),
			false)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public OrganisatieMedewerker createNewT()
			{
				return new OrganisatieMedewerker(medewerkerModel.getObject());
			}

			@Override
			protected void processPanelBeforeSubmit(
					OrganisatieEenheidLocatieEntiteitModalWindowPanel<OrganisatieMedewerker> panel)
			{
				panel.getForm().add(
					new BegindatumVoorEinddatumValidator(panel.getBegindatumField(), panel
						.getEinddatumField())
					{

						private static final long serialVersionUID = 1L;

						@Override
						public void validate(Form< ? > _form)
						{
							if (getBeginField() != null)
							{
								if (begindatumField.getDatum() != null)
								{
									if (begindatumField.getDatum().after(
										((DatumField) getBeginField()).getDatum()))
									{
										Map<String, Object> params = new HashMap<String, Object>();
										params.put("date", TimeUtil.getInstance().formatDate(
											begindatumField.getDatum()));
										error(getBeginField(), "OrgMedLocNaDatumInDienst.error",
											params);
									}
								}
								if (geboorteDatumField.getDatum() != null)
								{
									if (geboorteDatumField.getDatum().after(
										((DatumField) getBeginField()).getDatum()))
									{
										Map<String, Object> params = new HashMap<String, Object>();
										params.put("date", TimeUtil.getInstance().formatDate(
											geboorteDatumField.getDatum()));
										error(getBeginField(), "OrgMedLocNaGeboortedatum.error",
											params);
									}

								}
							}

						}

					});

				super.processPanelBeforeSubmit(panel);
			}
		});

		AdressenEditPanel<PersoonAdres, Persoon> adressenPanel =
			new AdressenEditPanel<PersoonAdres, Persoon>("inputFieldsAdres", medewerkerModel
				.getPersoonModel(), medewerkerModel.getEntiteitManager(), false);
		form.add(adressenPanel);

		UniqueConstraintFormValidator afkortingValidator =
			new UniqueConstraintFormValidator(personalia, "medewerker", "afkorting");
		afkortingValidator.setProperties("organisatie", "begindatum");
		form.add(afkortingValidator);

		createComponents();
	}

	private AutoFieldSet<Medewerker> createFieldsetPersonalia()
	{
		personalia =
			new AutoFieldSet<Medewerker>("inputFieldsPersonalia", medewerkerModel
				.getEntiteitModel(), "Personalia");
		personalia.setRenderMode(RenderMode.EDIT);
		personalia.setSortAccordingToPropertyNames(true);

		personalia.setPropertyNames("afkorting", "persoon.bsn", "persoon.officieleAchternaam",
			"persoon.officieleVoorvoegsel", "aanspreeknaamWijktAf", "persoon.achternaam",
			"persoon.voorvoegsel", "persoon.voornamen", "persoon.voorletters", "persoon.roepnaam",
			"persoon.geslacht", "persoon.geboortedatum");

		addModifier(personalia, "persoon.voorletters", new HoofdletterAjaxHandler(
			HoofdletterMode.PuntSeperated));

		personalia.addFieldModifier(new PseudoFieldModifier<Medewerker, Boolean>(Medewerker.class,
			"aanspreeknaamWijktAf", Boolean.TYPE, new PropertyModel<Boolean>(this,
				"aanspreeknaamWijktAf")));
		personalia.addFieldModifier(new EnableModifier(new PropertyModel<Boolean>(this,
			"aanspreeknaamWijktAf"), "persoon.achternaam", "persoon.voorvoegsel"));

		personalia.addModifier("aanspreeknaamWijktAf", new AjaxFormComponentUpdatingBehavior(
			"onclick")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				Component achternaamVeld = personalia.findFieldComponent("persoon.achternaam");
				Component voorvoegselVeld = personalia.findFieldComponent("persoon.voorvoegsel");

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
		personalia.addModifier("persoon.achternaam", new HoofdletterAjaxHandler(
			HoofdletterMode.EersteLetterEenWoord));

		personalia.addFieldModifier(new EditorClassModifier(DatumField.class,
			"persoon.geboortedatum"));
		personalia.addFieldModifier(new HtmlClassModifier("unit_80", "persoon.geboortedatum"));

		voornamenNaarVoorletters = new VoornamenNaarVoorlettersAjaxHandler();
		personalia.addModifier("persoon.voornamen", voornamenNaarVoorletters);

		addModifier(personalia, "persoon.achternaam", new HoofdletterAjaxHandler(
			HoofdletterMode.ElkWoord));
		addModifier(personalia, "persoon.roepnaam", new HoofdletterAjaxHandler(
			HoofdletterMode.ElkWoord));

		addModifier(personalia, "afkorting", new HoofdletterAjaxHandler(HoofdletterMode.Alles));

		addModifier(personalia, "persoon.geboortedatum", new DatumInVerledenValidator());

		addModifier(personalia, "persoon.achternaam", new HoofdletterAjaxHandler(
			HoofdletterMode.ElkWoord));

		addModifier(personalia, "persoon.bsn", new AjaxFormComponentValidatingBehavior("onchange"));
		addModifier(personalia, "persoon.bsn", new BsnValidator());
		addModifier(personalia, "persoon.bsn", new BsnInGebruikValidator<Long>(
			new PropertyModel<Persoon>(medewerkerModel.getEntiteitModel(), "persoon")));

		return personalia;
	}

	private ContactgegevenEntiteitEditPanel<PersoonContactgegeven, Persoon> createFieldsetContactGegevens()
	{
		return new ContactgegevenEntiteitEditPanel<PersoonContactgegeven, Persoon>(
			"inputFieldsContactgegevens", medewerkerModel.getPersoonModel());
	}

	private AutoFieldSet<Medewerker> createFieldsetAanstelling()
	{
		fieldset =
			new AutoFieldSet<Medewerker>("inputFieldsAanstelling", medewerkerModel
				.getEntiteitModel(), "Aanstelling");
		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setSortAccordingToPropertyNames(true);

		fieldset.setPropertyNames("functie", "begindatum", "einddatum", "redenUitDienst",
			"uitgeslotenVanCorrespondentie", "redenUitgeslotenVanCorrespondentie");

		return fieldset;
	}

	private void addModifier(AutoFieldSet<Medewerker> fields, String property,
			IValidator< ? > validator)
	{
		fields.addFieldModifier(new ValidateModifier(validator, property));
	}

	private void addModifier(AutoFieldSet<Medewerker> fields, String property, IBehavior behavior)
	{
		fields.addFieldModifier(new BehaviorModifier(behavior, property));
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{

				NieuweMedewerkerPage.this.medewerkerModel.save();

				EduArteRequestCycle.get().setResponsePage(MedewerkerZoekenPage.class);
			}
		});

		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				NieuweMedewerkerPage.this.medewerkerModel.save();
				medewerkerModel.createDefaultT();
				EduArteRequestCycle.get().setResponsePage(NieuweMedewerkerPage.class);
			}

			@Override
			public String getLabel()
			{
				return "Opslaan en nieuwe toevoegen";
			}

			@Override
			public ActionKey getAction()
			{
				ActionKey action = CobraKeyAction.VOLGENDE;
				return action;
			}
		});

		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new MedewerkerZoekenPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return MedewerkerZoekenPage.class;
			}

		}));
	}

	@Override
	public boolean supportsBookmarks()
	{
		// deze specifieke pagina kan wel gebookmarked worden (lege edit page)
		return true;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new EmptyMenu(id);
	}

	@Override
	public void detachModels()
	{
		super.detachModels();

		ComponentUtil.detachQuietly(medewerkerModel);
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

		geboorteDatumField = (DatumField) personalia.findFieldComponent("persoon.geboortedatum");
		begindatumField = (DatumField) fieldset.findFieldComponent("begindatum");
		einddatumField = (DatumField) fieldset.findFieldComponent("einddatum");

		form.add(new DatumKleinerOfGelijkDatumValidator(geboorteDatumField, begindatumField
			.getDatum()));

		form.add(new BegindatumVoorEinddatumValidator(begindatumField, einddatumField));

		form.add(new DatumGroterOfGelijkDatumValidator(begindatumField, getMedewerker()
			.getPersoon().getGeboortedatum()));
	}

	public Medewerker getMedewerker()
	{
		return medewerkerModel.getObject();
	}
}
