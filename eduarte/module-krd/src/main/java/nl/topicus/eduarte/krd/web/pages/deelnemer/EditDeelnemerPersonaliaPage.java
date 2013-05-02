/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.deelnemer;

import static nl.topicus.cobra.transformers.HoofdletterMode.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentValidatingBehavior;
import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.behaviors.HoofdletterAjaxHandler;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.BehaviorModifier;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.MultiFieldValueTransformModifier;
import nl.topicus.cobra.web.components.form.modifier.PseudoFieldModifier;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.quicksearch.SearchEditorSelectBehavior;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.BankrekeningElfProefValidator;
import nl.topicus.cobra.web.validators.BsnValidator;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.DatumInVerledenValidator;
import nl.topicus.cobra.web.validators.DatumKleinerOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.OnderwijsnummerValidator;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.personen.AbstractRelatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.personen.Persoon.ToepassingGeboortedatum;
import nl.topicus.eduarte.entities.vrijevelden.PersoonVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.principals.deelnemer.DeelnemerPersonaliaWrite;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.krd.web.validators.BronValidator;
import nl.topicus.eduarte.krd.web.validators.GeboortedatumMinMaxWarningValidator;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.util.OverlijdenUtil;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenEditPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitEditPanel;
import nl.topicus.eduarte.web.components.quicksearch.plaats.PlaatsSearchEditor;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

/**
 * @author idserda
 */
@PageInfo(title = "Personalia Bewerken", menu = {"Deelnemer > [deelnemer]",
	"Groep > [groep] > [deelnemer]"})
@InPrincipal(DeelnemerPersonaliaWrite.class)
public class EditDeelnemerPersonaliaPage extends AbstractDeelnemerPage implements
		VerbintenisProvider, IModuleEditPage<Deelnemer>
{
	private boolean aanspreeknaamWijktAf = false;

	private WebMarkupContainer overledenDatumPanel;

	private DatumField datumVanOverlijden;

	private AutoFieldSet<Deelnemer> personalia;

	private OverledenAjaxCheckBox overledenCheckbox;

	private boolean initieelOverleden;

	private boolean negeerSleutelWijzigingen = false;

	private Form<Void> form;

	private SecurePage returnToPage;

	private PageLinkButton verhuizen;

	public EditDeelnemerPersonaliaPage(Deelnemer deelnemer, SecurePage returnToPage)
	{
		super(DeelnemerMenuItem.Personalia, deelnemer,
			returnToPage instanceof AbstractDeelnemerPage ? ((AbstractDeelnemerPage) returnToPage)
				.getContextVerbintenis() : null);
		deelnemer.getPersoon().checkAndCreateDebiteurNummer();
		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(deelnemer,
			new DefaultModelManager(AbstractRelatie.class, Relatie.class,
				PersoonExterneOrganisatie.class, PersoonAdres.class, Adres.class,
				VrijVeldOptieKeuze.class, PersoonVrijVeld.class, PersoonContactgegeven.class,
				Deelnemer.class, Persoon.class)));

		this.returnToPage = returnToPage;

		form = new Form<Void>("personaliaForm");

		aanspreeknaamWijktAf = deelnemer.getPersoon().isGeboortenaamWijktAf();
		initieelOverleden = (deelnemer.getPersoon().getDatumOverlijden() != null);

		form.add(createPersonaliaAutoFieldset());
		createOverledenDatumPanel();

		AdressenEditPanel<PersoonAdres, Persoon> adressenEditPanel =
			new AdressenEditPanel<PersoonAdres, Persoon>("adresEditTabs",
				new PropertyModel<Persoon>(getDefaultModel(), "persoon"), getManager(),
				!getDeelnemer().getPersoon().isOverleden())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSaveCurrent(AjaxRequestTarget target, PersoonAdres object)
				{
					target.addComponent(verhuizen);
				}

				@Override
				protected void onSaveNew(AjaxRequestTarget target, PersoonAdres object)
				{
					target.addComponent(verhuizen);
				}
			};

		form.add(adressenEditPanel);
		form.add(new ContactgegevenEntiteitEditPanel<PersoonContactgegeven, Persoon>(
			"contactgegevens", new PropertyModel<Persoon>(getDefaultModel(), "persoon")));

		VrijVeldEntiteitEditPanel<Persoon> VVEEPanel =
			new VrijVeldEntiteitEditPanel<Persoon>("vrijVelden", new PropertyModel<Persoon>(
				getDefaultModel(), "persoon"));
		VVEEPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.DEELNEMERPERSONALIA);
		form.add(VVEEPanel);

		add(form);

		createComponents();
	}

	@SuppressWarnings("unchecked")
	private IChangeRecordingModel<Deelnemer> getChangeRecordingModel()
	{
		return (IChangeRecordingModel<Deelnemer>) getDefaultModel();
	}

	private ModelManager getManager()
	{
		return getChangeRecordingModel().getManager();
	}

	private AutoFieldSet<Deelnemer> createPersonaliaAutoFieldset()
	{
		personalia =
			new AutoFieldSet<Deelnemer>("inputFieldsPersonalia", getChangeRecordingModel(),
				"Personalia");
		personalia.setPropertyNames(Arrays.asList("persoon.bsn", "persoon.officieleAchternaam",
			"persoon.officieleVoorvoegsel", "aanspreeknaamWijktAf", "persoon.achternaam",
			"persoon.voorvoegsel", "persoon.voornamen", "persoon.voorletters", "persoon.roepnaam",
			"persoon.geslacht", "persoon.geboortedatum", "persoon.geboorteland",
			"persoon.geboorteplaats", "persoon.burgerlijkeStaat", "onderwijsnummer",
			"persoon.datumInNederland", "persoon.cumiCategorie", "persoon.cumiRatio",
			"persoon.nationaliteit1", "persoon.nationaliteit2", "allochtoon", "persoon.nieuwkomer",
			"startkwalificatieplichtigTot", "lgf", "persoon.debiteurennummer",
			"persoon.persoonBetalingsplichtige", "persoon.bankrekeningnummer",
			"persoon.nietVerstrekkenAanDerden"));
		personalia.setSortAccordingToPropertyNames(true);
		personalia.setRenderMode(RenderMode.EDIT);

		addModifier(personalia, "persoon.bankrekeningnummer", new BankrekeningElfProefValidator());

		personalia.addFieldModifier(new ConstructorArgModifier("persoon.geboortedatum",
			new PropertyModel<ToepassingGeboortedatum>(getDefaultModel(),
				"persoon.toepassingGeboortedatum")));

		personalia.addFieldModifier(new PseudoFieldModifier<Persoon, Boolean>(Persoon.class,
			"aanspreeknaamWijktAf", Boolean.TYPE, new PropertyModel<Boolean>(this,
				"aanspreeknaamWijktAf")));
		personalia
			.addFieldModifier(new EnableModifier(new PropertyModel<Boolean>(this,
				"aanspreeknaamWijktAf"), "persoon.achternaam", "persoon.voorvoegsel",
				"onderwijsnummer"));
		personalia.addFieldModifier(new EduArteAjaxRefreshModifier("aanspreeknaamWijktAf",
			"persoon.achternaam", "persoon.voorvoegsel"));

		personalia.addFieldModifier(new EnableModifier(!getDeelnemer().getPersoon()
			.isPersoonBetalingsplichtige(), "persoon.persoonBetalingsplichtige"));

		// sleutelgegevens alleen enabled indien nog niet aan BRON gemeld
		AbstractReadOnlyModel<Boolean> sleutelgegevensEnabledModel =
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return !getDeelnemer().getBronStatus().isGemeldAanBron();
				}
			};
		personalia.addFieldModifier(new EnableModifier(sleutelgegevensEnabledModel, "persoon.bsn",
			"persoon.geslacht", "persoon.geboortedatum"));

		personalia.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getDeelnemer().getPersoon().getCumiCategorie() != null
					|| getDeelnemer().getPersoon().getCumiRatio() != null;
			}
		}, "persoon.cumiCategorie", "persoon.cumiRatio"));

		addModifier(personalia, "persoon.voorletters", new HoofdletterAjaxHandler(
			HoofdletterMode.PuntSeperated)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				String value = ((TextField< ? >) getComponent()).getInput();
				if (!value.contains(".") && !value.contains(" "))
				{
					getComponent().setDefaultModelObject(processString());
					target.addComponent(getComponent());
				}
			}

		});
		addModifier(personalia, "persoon.voornamen", new HoofdletterAjaxHandler(
			HoofdletterMode.ElkWoord));

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
		personalia.addFieldModifier(new ConstructorArgModifier("persoon.geboorteplaats",
			new PropertyModel<Land>(getDefaultModel(), "persoon.geboorteland")));
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

		personalia.addFieldModifier(new BehaviorModifier(new SearchEditorSelectBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				if (!aanspreeknaamWijktAf)
				{
					Component offVoorvoegsel =
						personalia.findFieldComponent("persoon.officieleVoorvoegsel");
					Component voorvoegsel = personalia.findFieldComponent("persoon.voorvoegsel");
					voorvoegsel.setDefaultModelObject(offVoorvoegsel.getDefaultModelObject());
					target.addComponent(voorvoegsel);
				}
			}
		}, "persoon.officieleVoorvoegsel"));

		personalia.addFieldModifier(new MultiFieldValueTransformModifier(
			"persoon.officieleAchternaam", "persoon.achternaam", new HoofdletterTransformer(
				HoofdletterMode.EersteLetterEenWoord), createDuplicateFieldsModel()));

		personalia.addModifier("persoon.achternaam", new HoofdletterAjaxHandler(
			EersteLetterEenWoord));

		addModifier(personalia, "persoon.roepnaam", new HoofdletterAjaxHandler(
			HoofdletterMode.ElkWoord));
		addModifier(personalia, "persoon.geboortedatum", new DatumInVerledenValidator());
		addModifier(personalia, "persoon.geboortedatum", new GeboortedatumMinMaxWarningValidator(
			this));

		personalia.addModifier("persoon.geboorteplaats", new HoofdletterAjaxHandler(ElkWoord));

		addModifier(personalia, "persoon.bsn", new UniqueConstraintValidator<Long>(personalia,
			"deelnemer", "persoon.bsn", "organisatie"));

		addModifier(personalia, "persoon.bsn", new AjaxFormComponentValidatingBehavior("onchange"));
		addModifier(personalia, "persoon.bsn", new BsnValidator());
		addModifier(personalia, "onderwijsnummer", new OnderwijsnummerValidator());

		return personalia;
	}

	private IModel<Boolean> createDuplicateFieldsModel()
	{
		return new AbstractReadOnlyModel<Boolean>()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return !aanspreeknaamWijktAf;
			}

		};
	}

	private void createOverledenDatumPanel()
	{
		overledenCheckbox =
			new OverledenAjaxCheckBox("overledenCheckBox",
				new Model<Boolean>(EditDeelnemerPersonaliaPage.this.getDeelnemer().getPersoon()
					.getDatumOverlijden() != null));
		overledenDatumPanel = new WebMarkupContainer("overledenDatumPanel");
		overledenDatumPanel.setOutputMarkupPlaceholderTag(true);
		overledenDatumPanel.setVisible(EditDeelnemerPersonaliaPage.this.getDeelnemer().getPersoon()
			.getDatumOverlijden() != null);
		form.add(overledenDatumPanel);

		overledenDatumPanel.add(datumVanOverlijden = new DatumField("persoon.datumOverlijden"));
		datumVanOverlijden.setRequired(true);
		datumVanOverlijden.setLabel(new Model<String>("Datum van overlijden"));
		datumVanOverlijden.add(new BronValidator<Date>(this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onValidate(IValidatable<Date> validatable, Verbintenis verbintenis)
			{
				if (EditDeelnemerPersonaliaPage.this.getDeelnemer().getPersoon()
					.getDatumOverlijden() != null)
				{
					for (Verbintenis verbintenisDeelnemer : EditDeelnemerPersonaliaPage.this
						.getDeelnemer().getVerbintenissen())
					{
						super.onValidate(validatable, verbintenisDeelnemer);
					}
				}
			}
		}

		);

		form.add(overledenCheckbox);
	}

	protected void processOnSubmit()
	{
		boolean succes = true;

		getDeelnemer().setNegeerSleutelWijzigingenBron(isNegeerSleutelWijzigingen());

		// Toch niet overleden
		if (initieelOverleden && !EditDeelnemerPersonaliaPage.this.overledenDatumPanel.isVisible())
		{
			OverlijdenUtil overlijdenUtil = new OverlijdenUtil(getContextDeelnemerModel());

			succes =
				overlijdenUtil.updateDeelnemer(getDeelnemer().getPersoon().getDatumOverlijden(),
					true);
		}
		// Deelnemer als overleden registreren
		else if (!initieelOverleden && getDeelnemer().getPersoon().getDatumOverlijden() != null)
		{
			OverlijdenUtil overlijdenUtil = new OverlijdenUtil(getContextDeelnemerModel());
			succes =
				overlijdenUtil.updateDeelnemer(getDeelnemer().getPersoon().getDatumOverlijden(),
					false);
		}
		// Met het vinkje gespeeld, conclusie: toch niet overleden
		else if (!EditDeelnemerPersonaliaPage.this.overledenDatumPanel.isVisible())
		{
			getDeelnemer().getPersoon().setDatumOverlijden(null);
		}

		if (!aanspreeknaamWijktAf)
		{
			getDeelnemer().getPersoon().setVoorvoegsel(
				getDeelnemer().getPersoon().getOfficieleVoorvoegsel());
		}

		if (succes)
		{
			getChangeRecordingModel().saveObject();
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
			setResponsePage(returnToPage);
		}
		else
		{
			error("Er is geen reden uitschrijving geregistreerd die overlijden kenmerkt.");
		}
	}

	private void addModifier(AutoFieldSet<Deelnemer> fields, String property,
			IValidator< ? > validator)
	{
		fields.addFieldModifier(new ValidateModifier(validator, property));
	}

	private void addModifier(AutoFieldSet<Deelnemer> fields, String property, IBehavior behavior)
	{
		fields.addFieldModifier(new BehaviorModifier(behavior, property));
	}

	private class OverledenAjaxCheckBox extends AjaxCheckBox
	{
		public OverledenAjaxCheckBox(String id, IModel<Boolean> model)
		{
			super(id, model);
			setOutputMarkupId(true);

			if (!getDeelnemer().getPersoon().isOverleden())
				add(onCheckTrueBehaviour);
			else
				add(onCheckFalseBehaviour);
		}

		private static final long serialVersionUID = 1L;

		public final AttributePrependingModifier onCheckTrueBehaviour =
			new AttributePrependingModifier("onclick",
				"if(!confirm('Weet u zeker dat u deze deelnemer als overleden wilt registreren?')) return false; ");

		public final AttributePrependingModifier onCheckFalseBehaviour =
			new AttributePrependingModifier(
				"onclick",
				"if(!confirm('Weet u zeker dat u deze deelnemer niet meer als overleden wilt registreren?')) return false; ");

		@Override
		protected void onUpdate(AjaxRequestTarget target)
		{
			EditDeelnemerPersonaliaPage.this.overledenDatumPanel.setVisible(getConvertedInput());

			if (!(Boolean) getConvertedInput())
			{
				if (!initieelOverleden)
					add(onCheckTrueBehaviour);
				else
					remove(onCheckFalseBehaviour);
			}
			else
			{
				if (initieelOverleden)
					add(onCheckFalseBehaviour);
				else
					remove(onCheckTrueBehaviour);
			}

			target.addComponent(EditDeelnemerPersonaliaPage.this.overledenDatumPanel);
			target.addComponent(this);
		}
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		Date geboorteDatum = getDeelnemer().getPersoon().getGeboortedatum();

		DatumField datumInNederland =
			(DatumField) personalia.findFieldComponent("persoon.datumInNederland");

		form.add(new DatumGroterOfGelijkDatumValidator("Datum van overlijden", datumVanOverlijden,
			geboorteDatum));
		form.add(new DatumKleinerOfGelijkDatumValidator("Datum van overlijden", datumVanOverlijden,
			TimeUtil.getInstance().currentDate()));
		form.add(new DatumGroterOfGelijkDatumValidator("Datum in Nederland", datumInNederland,
			geboorteDatum));

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
				processOnSubmit();
			}

		});
		panel.addButton(new AnnulerenButton(panel, returnToPage));

		panel.addButton(new DeelnemerVerwijderButton(panel, ModelFactory.getModel(getDeelnemer())));
		panel.addButton(new PageLinkButton(panel, "Sleutelgegevens wijzigen", ButtonAlignment.LEFT,
			new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					return new EditDeelnemerSleutelgegevensPage(getChangeRecordingModel(),
						EditDeelnemerPersonaliaPage.this);
				}

				@Override
				public Class<EditDeelnemerSleutelgegevensPage> getPageIdentity()
				{
					return EditDeelnemerSleutelgegevensPage.class;
				}
			})
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				// alleen zichtbaar als de deelnemer aan BRON is gemeld; anders kun je de
				// sleutelgegevens gewoon aanpassen
				return getDeelnemer().getBronStatus() != null
					&& getDeelnemer().getBronStatus().isGemeldAanBron();
			}
		});
		verhuizen = new PageLinkButton(panel, "Verhuizen", ButtonAlignment.LEFT, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new DeelnemerVerhuizenPage(EditDeelnemerPersonaliaPage.this,
					getChangeRecordingModel());
			}

			@Override
			public Class<DeelnemerVerhuizenPage> getPageIdentity()
			{
				return DeelnemerVerhuizenPage.class;
			}

		})
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				// alleen zichtbaar als de deelnemer een onbeeindigd adres heeft
				if (getDeelnemer().getPersoon() != null)
				{
					List<PersoonAdres> adressen = getDeelnemer().getPersoon().getAdressen();
					if (adressen != null)
					{
						for (PersoonAdres adres : adressen)
						{
							if (adres.isActief())
							{
								return true;
							}
						}
					}
				}
				return false;

			}
		};
		verhuizen.setOutputMarkupId(true);
		verhuizen.setOutputMarkupPlaceholderTag(true);
		panel.addButton(verhuizen);
	}

	private class AttributePrependingModifier extends AppendingAttributeModifier
	{
		private static final long serialVersionUID = 1L;

		public AttributePrependingModifier(String attribute, CharSequence value)
		{
			super(attribute, value);
		}

		@Override
		public void onComponentTag(Component component, ComponentTag tag)
		{
			if (isEnabled(component))
			{
				CharSequence old = tag.getString(getAttribute());
				if (old != null)
					tag.put(getAttribute(), getValue() + " " + old.toString());
				else
					tag.put(getAttribute(), getValue());
			}
		}
	}

	private Deelnemer getDeelnemer()
	{
		return (Deelnemer) getDefaultModelObject();
	}

	@Override
	public Verbintenis getVerbintenis()
	{
		if (!getDeelnemer().getVerbintenissen().isEmpty())
			return getDeelnemer().getVerbintenissen().get(0);
		else
			return null;
	}

	public boolean isNegeerSleutelWijzigingen()
	{
		return negeerSleutelWijzigingen;
	}

	public void setNegeerSleutelWijzigingen(boolean negeerSleutelWijzigingen)
	{
		this.negeerSleutelWijzigingen = negeerSleutelWijzigingen;
	}

}