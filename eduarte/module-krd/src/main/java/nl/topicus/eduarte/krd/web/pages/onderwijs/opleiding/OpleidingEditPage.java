package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.Enclosure;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.VersionedForm;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.text.RequiredDatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingFase;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.entities.opleiding.Opleidingsvariant;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.entities.vrijevelden.OpleidingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingWrite;
import nl.topicus.eduarte.krd.web.components.choice.LeerwegCombobox;
import nl.topicus.eduarte.krd.web.components.modalwindow.opleiding.OpleidingFaseModalWindowPanel;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.providers.OpleidingProvider;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OpleidingFaseTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieLocatieTable;
import nl.topicus.eduarte.web.components.panels.organisatielocatie.OrganisatieEenheidLocatieEntiteitEditPanel;
import nl.topicus.eduarte.web.components.quicksearch.taxonomie.TaxonomieElementSearchEditor;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.AbstractOpleidingPage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.OpleidingZoekenPage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.OpleidingkaartPage;
import nl.topicus.eduarte.web.validators.TableMinimumValidator;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Pagina voor toevoegen/wijzigen van een opleiding
 * 
 * @author vandekamp
 */
@PageInfo(title = "Opleiding toevoegen", menu = {
	"Onderwijs > Opleidingen > [Opleiding] > Bewerken", "Onderwijs > Opleidingen > toevoegen"})
@InPrincipal(OpleidingWrite.class)
public class OpleidingEditPage extends AbstractOpleidingPage implements IModuleEditPage<Opleiding>,
		OpleidingProvider
{
	private static final long serialVersionUID = 1L;

	protected final VersionedForm<Opleiding> form;

	private final SecurePage returnToPage;

	private ModelManager modelManager;

	private final IChangeRecordingModel<Opleiding> opleidingModel;

	private boolean variant;

	private AbstractEduArteToevoegenBewerkenPanel<OpleidingFase> fasesPanel;

	public OpleidingEditPage(Opleiding opleiding, SecurePage returnToPage)
	{
		super(OpleidingMenuItem.Opleidingkaart, opleiding);
		modelManager =
			new DefaultModelManager(OpleidingAanbod.class, VrijVeldOptieKeuze.class,
				OpleidingVrijVeld.class, Opleidingsvariant.class, OpleidingFase.class,
				Opleiding.class);
		opleidingModel = ModelFactory.getCompoundChangeRecordingModel(opleiding, modelManager);
		setDefaultModel(opleidingModel);
		this.returnToPage = returnToPage;
		form = new VersionedForm<Opleiding>("form", opleidingModel);
		form.setOutputMarkupId(true);
		add(form);
		addVerbintenisgebied(opleiding);
		variant = opleiding instanceof Opleidingsvariant;
		addOpleidingvariantVelden();

		add(new Label("caption", StringUtil.convertCamelCaseFirstCharUpperCase(opleiding.getClass()
			.getSimpleName())));

		form.add(ComponentUtil.fixLength(new RequiredTextField<String>("code")
			.add(new UniqueConstraintValidator<String>(form, opleiding.getClass().getName(),
				"code", "organisatie")), Opleiding.class));

		form.add(ComponentUtil.fixLength(new RequiredTextField<String>("naam"), Opleiding.class));
		WebMarkupContainer container = new WebMarkupContainer("wervingsnaamContainer");
		container.setVisible(EduArteApp.get().isModuleActive(EduArteModuleKey.DIGITAALAANMELDEN));
		form.add(container);
		container.add(ComponentUtil.fixLength(new RequiredTextField<String>("wervingsnaam"),
			Opleiding.class));

		addLeerweg();
		addDefaultIntensiteit();

		RequiredDatumField begindatumField = new RequiredDatumField("begindatum");
		DatumField einddatumField = new DatumField("einddatum");
		DatumField datumLaatsteInschrijving = new DatumField("datumLaatsteInschrijving");
		form.add(begindatumField);
		form.add(datumLaatsteInschrijving);
		form.add(einddatumField);
		form.add(new TextField<Integer>("duurInMaanden", Integer.class).setOutputMarkupId(true));
		form.add(new TextField<Integer>("beginLeerjaar", Integer.class));
		form.add(new TextField<Integer>("eindLeerjaar", Integer.class));
		form.add(new BegindatumVoorEinddatumValidator(begindatumField, einddatumField));
		form.add(new BegindatumVoorEinddatumValidator(begindatumField, datumLaatsteInschrijving,
			"datumLaatsteInschrijvingVoorBegindatum"));
		form.add(new BegindatumVoorEinddatumValidator(datumLaatsteInschrijving, einddatumField,
			"datumLaatsteInschrijvingNaEinddatum"));
		form.add(new TextArea<String>("diplomatekst1"));
		form.add(new TextArea<String>("diplomatekst2"));
		form.add(new TextArea<String>("diplomatekst3"));
		form.add(new JaNeeCombobox("negeerLandelijkeProductregels").setRequired(true));
		form.add(new JaNeeCombobox("negeerLandelijkeCriteria").setRequired(true));
		form.add(new JaNeeCombobox("communicerenMetBRON").setRequired(true));
		form.add(new JaNeeCombobox("kiesKenniscentrum").setRequired(true));

		OrganisatieEenheidLocatieEntiteitEditPanel<OpleidingAanbod> aanbodPanel =
			new OrganisatieEenheidLocatieEntiteitEditPanel<OpleidingAanbod>("aanbod",
				new PropertyModel<List<OpleidingAanbod>>(opleidingModel, "aanbod"), modelManager,
				new OrganisatieLocatieTable<OpleidingAanbod>(false, true), true)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public OpleidingAanbod createNewT()
				{
					return new OpleidingAanbod(opleidingModel.getObject());
				}
			};

		addFases();

		form.add(new TableMinimumValidator(aanbodPanel.getCustomDataPanel(), 1));
		form.add(aanbodPanel);
		VrijVeldEntiteitEditPanel<Opleiding> vrijVeldenPanel =
			new VrijVeldEntiteitEditPanel<Opleiding>("vrijvelden", opleidingModel);
		vrijVeldenPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		vrijVeldenPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.OPLEIDING);
		form.add(vrijVeldenPanel);

		createComponents();
	}

	private void addFases()
	{
		PropertyModel<List<OpleidingFase>> listModel =
			new PropertyModel<List<OpleidingFase>>(opleidingModel, "fases");

		fasesPanel =
			new AbstractEduArteToevoegenBewerkenPanel<OpleidingFase>("fases", listModel,
				modelManager, new OpleidingFaseTable())
			{

				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return getOpleiding().getVerbintenisgebied() != null
						&& getOpleiding().getVerbintenisgebied().getTaxonomie().isHO();
				}

				@Override
				public AbstractToevoegenBewerkenModalWindowPanel<OpleidingFase> createModalWindowPanel(
						String id, AbstractToevoegenBewerkenModalWindow<OpleidingFase> modalWindow)
				{
					return new OpleidingFaseModalWindowPanel(id, modalWindow, this);
				}

				@Override
				public OpleidingFase createNewT()
				{
					OpleidingFase opleidingFase = new OpleidingFase();
					opleidingFase.setOpleiding(getOpleiding());
					return opleidingFase;
				}

				@Override
				public String getModalWindowTitle()
				{
					return "Fase";
				}

				@Override
				public String getToevoegenLabel()
				{
					return "Toevoegen";
				}
			};
		fasesPanel.setOutputMarkupPlaceholderTag(true);
		form.add(fasesPanel);
	}

	private void addVerbintenisgebied(Opleiding opleiding)
	{
		TaxonomieElementZoekFilter taxFilter =
			new TaxonomieElementZoekFilter(Verbintenisgebied.class);
		taxFilter.setInschrijfbaar(Boolean.TRUE);

		TaxonomieElementSearchEditor editor =
			new TaxonomieElementSearchEditor("verbintenisgebied", taxFilter);
		editor.setRequired(true);
		editor.setLabel(new Model<String>("verbintenisgebied"));
		boolean viaHerstellen =
			(returnToPage instanceof OpleidingkaartPage)
				&& ((OpleidingkaartPage) returnToPage).viaOpleidingHerstellenPage();

		editor
			.setEnabled(!variant
				&& (viaHerstellen || !opleiding.isSaved() || (opleiding.getVerbintenisgebied() != null && opleiding
					.getVerbintenisgebied().getTaxonomiecode().equals("6.ONB"))));

		editor.addListener(new ISelectListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(form.get("leerweg"));
				target.addComponent(form.get("duurInMaanden"));
				target.addComponent(fasesPanel);
				target.addComponent(form.get("defaultIntensiteit"));
				getOpleiding().setLeerweg(null);
				getOpleiding().setDefaultIntensiteit(null);
			}
		});
		form.add(editor);
	}

	private void addOpleidingvariantVelden()
	{
		Label parent = new Label("parent");
		WebMarkupContainer parentContainer = new WebMarkupContainer("parentContainer");
		parentContainer.setVisible(variant);
		parentContainer.add(parent);
		form.add(parentContainer);

		WebMarkupContainer ivContainer = new WebMarkupContainer("instroomvariantContainer");
		ivContainer.setVisible(variant);
		ivContainer.add(new CheckBox("instroomvariant"));
		form.add(ivContainer);

		WebMarkupContainer uvContainer = new WebMarkupContainer("uitstroomvariantContainer");
		uvContainer.setVisible(variant);
		uvContainer.add(new CheckBox("uitstroomvariant"));
		form.add(uvContainer);
	}

	protected void addLeerweg()
	{
		Enclosure enclosure = new Enclosure("leerweg");
		form.add(enclosure);

		LeerwegCombobox comboBox = new LeerwegCombobox("leerweg", this);
		enclosure.add(comboBox);
	}

	private final class IntensiteitListModel extends LoadableDetachableModel<List<Intensiteit>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Intensiteit> load()
		{
			List<Intensiteit> res = new ArrayList<Intensiteit>(Arrays.asList(Intensiteit.values()));
			if (getOpleiding().getLeerweg() == MBOLeerweg.BBL)
			{
				res.remove(Intensiteit.Voltijd);
			}
			if (getOpleiding().getVerbintenisgebied() != null
				&& getOpleiding().getVerbintenisgebied().getTaxonomie().isVO())
			{
				res.remove(Intensiteit.Deeltijd);
			}
			return res;
		}

	}

	protected void addDefaultIntensiteit()
	{
		Enclosure enclosure = new Enclosure("defaultIntensiteit");
		form.add(enclosure);

		EnumCombobox<Intensiteit> comboBox =
			new EnumCombobox<Intensiteit>("defaultIntensiteit", null, new IntensiteitListModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return getOpleiding().getVerbintenisgebied() == null
						|| getOpleiding().getVerbintenisgebied().getTaxonomie().isBO()
						|| getOpleiding().getVerbintenisgebied().getTaxonomie().isCGO()
						|| (getOpleiding().getVerbintenisgebied().getTaxonomie().isVO() && !getOpleiding()
							.isVavo());
				}
			};
		comboBox.setNullValid(true);
		enclosure.add(comboBox);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				if (!EduArteApp.get().isModuleActive(EduArteModuleKey.DIGITAALAANMELDEN))
					getOpleiding().setWervingsnaam(getOpleiding().getNaam());
				IChangeRecordingModel<Opleiding> model = opleidingModel;
				model.saveObject();
				getContextOpleiding().commit();
				setResponsePage(getResponsePageNaOpslaan());
			}
		});
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public ActionKey getAction()
			{
				return CobraKeyAction.VOLGENDE;
			}

			@Override
			public String getLabel()
			{
				return "Opslaan en kopie maken";
			}

			@Override
			protected void onSubmit()
			{
				if (!EduArteApp.get().isModuleActive(EduArteModuleKey.DIGITAALAANMELDEN))
					getOpleiding().setWervingsnaam(getOpleiding().getNaam());
				IChangeRecordingModel<Opleiding> model = opleidingModel;
				model.saveObject();
				getContextOpleiding().commit();

				HibernateObjectCopyManager copyManager =
					new HibernateObjectCopyManager(OpleidingAanbod.class, Opleiding.class);
				Opleiding opleiding = copyManager.copyObject(getContextOpleiding());
				opleiding.setCode(null);
				opleiding.setNaam(null);

				setResponsePage(new OpleidingEditPage(opleiding, new OpleidingZoekenPage()));
			}

		});
		panel.addButton(new AnnulerenButton(panel, returnToPage));
		panel.addButton(new VerwijderButton(panel, "Verwijderen", "Weet u zeker dat u "
			+ getContextOpleiding().toString() + " wilt verwijderen?")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				Opleiding opl = getContextOpleiding();
				for (OpleidingAanbod aanbod : opl.getAanbod())
					aanbod.delete();
				opl.delete();
				opl.commit();
				setResponsePage(new OpleidingZoekenPage());
			}

			@Override
			public boolean isVisible()
			{
				return getContextOpleiding().isDeletable();
			}
		});
	}

	protected SecurePage getResponsePageNaOpslaan()
	{
		if (returnToPage instanceof OpleidingkaartPage)
		{
			return new OpleidingkaartPage(getContextOpleiding(),
				((OpleidingkaartPage) returnToPage).getReturnToPage());
		}
		else
		{
			return new OpleidingkaartPage(getContextOpleiding());
		}

	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		returnToPage.detach();
	}

	@Override
	public Opleiding getOpleiding()
	{
		return Opleiding.class.cast(opleidingModel.getObject());
	}
}
