package nl.topicus.eduarte.web.pages.beheer.organisatie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.transformers.HoofdletterMode;
import nl.topicus.cobra.web.behaviors.HoofdletterAjaxHandler;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.PostProcessModifier;
import nl.topicus.cobra.web.components.form.modifier.SingleFieldAdapter;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.validators.BankrekeningElfProefValidator;
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.core.principals.beheer.organisatie.OrganisatiemodelPrincipal;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidAdres;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidContactgegeven;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidLocatie;
import nl.topicus.eduarte.entities.personen.OrganisatieEenheidContactPersoon;
import nl.topicus.eduarte.web.components.choice.OrganisatieEenheidCombobox;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenEditPanel;
import nl.topicus.eduarte.web.components.modalwindow.organisatie.OrganisatieEenheidLocatieEditPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitEditPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ProbeerTeVerwijderenButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieEenheidContactPersoonTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieEenheidLocatieTable;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Organisatie-eenheid", menu = "Beheer > Organisatie-eenheid > [OrganisatieEenheid]")
@InPrincipal(OrganisatiemodelPrincipal.class)
public class OrganisatieEenheidEditPage extends AbstractBeheerPage<OrganisatieEenheid> implements
		IModuleEditPage<OrganisatieEenheid>
{
	private Form<Void> form;

	private OrganisatieEenheidModel organisatieEenheidModel;

	public OrganisatieEenheidEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getModel(new OrganisatieEenheid(new Date())), returnPage);
	}

	public OrganisatieEenheidEditPage(IModel<OrganisatieEenheid> organisatieEenheidModel,
			SecurePage returnPage)
	{
		this(new OrganisatieEenheidModel(organisatieEenheidModel.getObject()), returnPage);
	}

	private OrganisatieEenheidEditPage(OrganisatieEenheidModel organisatieEenheid,
			SecurePage returnPage)
	{
		super(organisatieEenheid.getEntiteitModel(), BeheerMenuItem.Organisatie_eenheden);
		setReturnPage(returnPage);
		this.organisatieEenheidModel = organisatieEenheid;
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		addAlgemeenFieldSet();
		addAdresTable();
		addFieldsetContactGegevens();

		form.add(new OrganisatieEenheidLocatieEditPanel("panelLocaties", organisatieEenheidModel
			.getOrganisatieEenheidLocatieListModel(), organisatieEenheidModel.getEntiteitManager(),
			new OrganisatieEenheidLocatieTable())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public OrganisatieEenheidLocatie createNewT()
			{
				return new OrganisatieEenheidLocatie(organisatieEenheidModel
					.getOrganisatieEenheid());
			}

			@Override
			public OrganisatieEenheidModel getOrganisatieDeelnemerModel()
			{
				return organisatieEenheidModel;
			}
		});
		form.add(new OrganisatieEenheidContactPersoonEditPanel("contactpersonen",
			new PropertyModel<List<OrganisatieEenheidContactPersoon>>(organisatieEenheidModel
				.getEntiteitModel(), "contactpersonen"), organisatieEenheidModel
				.getEntiteitManager(), new OrganisatieEenheidContactPersoonTable())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public OrganisatieEenheidContactPersoon createNewT()
			{
				return new OrganisatieEenheidContactPersoon(organisatieEenheidModel
					.getOrganisatieEenheid());
			}
		});

		createComponents();
	}

	private void addAlgemeenFieldSet()
	{
		AutoFieldSet<OrganisatieEenheid> algemeenFieldSet =
			new AutoFieldSet<OrganisatieEenheid>("organisatieEenheid", getContextModel(),
				"Organisatie-eenheid");

		List<String> fields = new ArrayList<String>();
		fields.addAll(Arrays.asList("afkorting", "naam", "soortOrganisatieEenheid",
			"officieleNaam", "parent", "instellingBrincode", "bankrekeningnummer", "begindatum",
			"einddatum", "intakeWizardStap3Overslaan"));
		if (EduArteApp.get().isModuleActive(EduArteModuleKey.DIGITAALAANMELDEN))
		{
			fields.addAll(Arrays.asList("tonenBijDigitaalAanmelden"));
		}
		algemeenFieldSet.setPropertyNames(fields);
		algemeenFieldSet.setRenderMode(RenderMode.EDIT);
		algemeenFieldSet.setSortAccordingToPropertyNames(true);
		algemeenFieldSet.addFieldModifier(new ValidateModifier(new BankrekeningElfProefValidator(),
			"bankrekeningnummer"));

		algemeenFieldSet.addFieldModifier(new PostProcessModifier("parent")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
					FieldProperties<T, ? , ? > fieldProperties)
			{
				OrganisatieEenheidCombobox combobox = (OrganisatieEenheidCombobox) field;
				combobox.setChoices(new LoadableDetachableModel<List<OrganisatieEenheid>>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected List<OrganisatieEenheid> load()
					{
						List<OrganisatieEenheid> organisatieEenheden =
							DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class)
								.list();

						if (organisatieEenheden == null)
							organisatieEenheden = new ArrayList<OrganisatieEenheid>();

						if (getDefaultModelObject() != null)
							organisatieEenheden.remove(getDefaultModelObject());

						return organisatieEenheden;
					}
				});
			}
		})

		;
		String afkorting = getOrganisatieEenheidModel().getOrganisatieEenheid().getAfkorting();
		if (afkorting == null || afkorting.toUpperCase().equals(afkorting))
			algemeenFieldSet.addModifier("afkorting", new HoofdletterAjaxHandler(
				HoofdletterMode.Alles));

		form.add(new UniqueConstraintFormValidator(algemeenFieldSet, "Organisatie-eenheid",
			"afkorting"));

		algemeenFieldSet.addFieldModifier(new SingleFieldAdapter("parent",
			nl.topicus.cobra.web.components.form.modifier.FieldModifier.Action.REQUIRED)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public <T> boolean isRequired(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
					FieldProperties<T, ? , ? > fieldProperties)
			{
				OrganisatieEenheid rootEenheid =
					DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class)
						.getRootOrganisatieEenheid();
				return rootEenheid != null
					&& rootEenheid != OrganisatieEenheidEditPage.this.getDefaultModelObject();
			}

		});

		form.add(algemeenFieldSet);
	}

	private void addAdresTable()
	{
		AdressenEditPanel<OrganisatieEenheidAdres, OrganisatieEenheid> adressenPanel =
			new AdressenEditPanel<OrganisatieEenheidAdres, OrganisatieEenheid>("inputFieldsAdres",
				organisatieEenheidModel.getEntiteitModel(), organisatieEenheidModel
					.getEntiteitManager(), false);
		form.add(adressenPanel);
	}

	private void addFieldsetContactGegevens()
	{
		form
			.add(new ContactgegevenEntiteitEditPanel<OrganisatieEenheidContactgegeven, OrganisatieEenheid>(
				"inputFieldsContactgegevens", organisatieEenheidModel.getEntiteitModel()));
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
				OrganisatieEenheidEditPage.this.organisatieEenheidModel.save();

				EduArteRequestCycle.get().setResponsePage(
					OrganisatieEenheidEditPage.this.getReturnPage());
			}
		});

		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				OrganisatieEenheidEditPage.this.organisatieEenheidModel.save();

				OrganisatieEenheidEditPage page =
					new OrganisatieEenheidEditPage(new OrganisatieEenheidZoekenPage());
				EduArteRequestCycle.get().setResponsePage(page);
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
				return OrganisatieEenheidEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return OrganisatieEenheidEditPage.this.getReturnPageClass();
			}
		}));
		panel.addButton(new ProbeerTeVerwijderenButton(panel, getOrganisatieEenheidModel()
			.getEntiteitModel(), "deze organisatie-eenheid", OrganisatieEenhedenPage.class));
	}

	public OrganisatieEenheidModel getOrganisatieEenheidModel()
	{
		return organisatieEenheidModel;
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		if (organisatieEenheidModel != null)
			organisatieEenheidModel.detach();
	}
}
