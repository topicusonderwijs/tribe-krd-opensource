package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis;

import java.math.BigDecimal;
import java.util.*;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.HibernateModel;
import nl.topicus.cobra.modelsv2.HibernateObjectListModel;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentSaveBehaviour;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.choice.IntegerRangeCombobox;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractConfirmationLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.text.ReadonlyTextField;
import nl.topicus.cobra.web.components.text.RequiredDatumField;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.cobra.web.security.RequiredSecurityChecks;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.app.security.checks.OrganisatieEenheidLocatieSecurityCheck;
import nl.topicus.eduarte.app.sidebar.datastores.RecenteDeelnemersDataStore;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.ExamenstatusOvergang;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.hogeronderwijs.Hoofdfase;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingsVorm;
import nl.topicus.eduarte.entities.inschrijving.*;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Leerprofiel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.ProfielInburgering;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.RedenInburgering;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.SoortPraktijkexamen;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.StaatsExamenType;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.Team;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.VerbintenisBijlage;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.vrijevelden.VerbintenisVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenisStatusovergang;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenVerwijderen;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.krd.web.components.choice.IntensiteitCombobox;
import nl.topicus.eduarte.krd.web.components.modalwindow.verbintenis.bekostiging.BekostigingsperiodeEditPanel;
import nl.topicus.eduarte.krd.web.components.modalwindow.verbintenis.verbinteniscontract.VerbintenisContractEditPanel;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.krd.web.validators.BekostigingValidator;
import nl.topicus.eduarte.krd.web.validators.BronValidator;
import nl.topicus.eduarte.krd.web.validators.BronVerbintenisMutatieToegestaanValidator;
import nl.topicus.eduarte.krd.web.validators.IngangsdatumOpleidingNaBegindatumValidator;
import nl.topicus.eduarte.krd.web.validators.IntensiteitLeerwegKoppelValidator;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.choice.CohortCombobox;
import nl.topicus.eduarte.web.components.choice.KenniscentrumCombobox;
import nl.topicus.eduarte.web.components.choice.RedenUitschrijvingComboBox;
import nl.topicus.eduarte.web.components.choice.SecureOrganisatieEenheidLocatieFormChoicePanel;
import nl.topicus.eduarte.web.components.choice.SecureOrganisatieEenheidLocatieChoicePanel.OrganisatieEenheidLocatieRequired;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.datapanel.table.VerbintenisContractTable;
import nl.topicus.eduarte.web.components.panels.verbintenis.CreditsPerHoofdfaseModel;
import nl.topicus.eduarte.web.components.quicksearch.groep.GroepSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.opleiding.OpleidingSearchEditor;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.DeelnemerVerbintenisPage;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter.SoortRedenUitschrijvingTonen;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;
import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.security.components.SecureComponentHelper;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;
import org.apache.wicket.validation.validator.RangeValidator;

/*
 * @author idserda
 */
@PageInfo(title = "Verbintenis bewerken", menu = {
	"Deelnemer > [deelnemer] > Verbintenis > Nieuwe Verbintenis", "Groep > [groep] > [deelnemer]"})
@InPrincipal(DeelnemerVerbintenissenWrite.class)
@RequiredSecurityChecks( {@RequiredSecurityCheck(NietOverledenSecurityCheck.class),
	@RequiredSecurityCheck(OrganisatieEenheidLocatieSecurityCheck.class)})
public class EditVerbintenisPage extends AbstractDeelnemerPage implements VerbintenisProvider,
		IModuleEditPage<Verbintenis>
{
	private static final boolean CONTROLEER_OP_BEGIN_EN_EINDDATA_BEKOSTIGINGSPERIODES = false;

	private Form<Void> form;

	private final SecurePage returnToPage;

	private Panel bekostigingsperiodeEditPanel;

	private AutoFieldSet<Verbintenis> veldenBeginEinddatum;

	private DatumField begindatumVeld;

	private DatumField geplandeEinddatumVeld;

	private CohortCombobox cohortCombo;

	private WebMarkupContainer veldenLinks;

	private SecureOrganisatieEenheidLocatieFormChoicePanel<Verbintenis> organisatieEenheidLocatieChoice;

	private EnumCombobox<VerbintenisStatus> statusCombobox;

	private OpleidingSearchEditor opleiding;

	private OpleidingZoekFilter opleidingZoekFilter;

	private JaNeeCombobox onderwijsproductAfnamesMakenCombobox;

	private boolean opgeslagenVerbintenisMuteerbaar;

	private boolean nieuw;

	private boolean statusTeruggezet;

	private IModel<Verbintenis> oudeVerbintenisModel;

	private PlaatsingModel plaatsingModel;

	private AbstractAjaxDropDownChoice<IVooropleiding> relevanteVooropleiding;

	private Label categorieVooropleiding;

	private WebMarkupContainer intensiteitRow;

	private boolean initialGeplandeDatum = true;

	private ContextImage waarschuwingVooropleidingen;

	public static final String EXTRA_STATUSOVERGANGEN = "EXTRA_STATUSOVERGANGEN";

	private IModel<Opleiding> oudeOpleiding;

	private Long origineleOpleidingID;

	private Label teamLabel;

	/**
	 * Constructor voor het normale gebruik van deze pagina
	 * 
	 * @param verbintenis
	 *            te bewerken verbintenis
	 * @param returnToPage
	 *            return page na annuleren
	 */
	public EditVerbintenisPage(Verbintenis verbintenis, SecurePage returnToPage)
	{
		this(verbintenis, null, returnToPage, !verbintenis.isSaved());
	}

	public EditVerbintenisPage(Verbintenis verbintenis, SecurePage returnToPage, boolean nieuw)
	{
		this(verbintenis, null, returnToPage, nieuw);
	}

	/**
	 * Constructor voor het beeindigen van een oude verbintenis en het maken van een
	 * nieuwe verbintenis (bijv. na kopieren)
	 * 
	 * @param verbintenis
	 *            te bewerken, nieuwe verbintenis
	 * @param oudeVerbintenis
	 *            te beeindigen oude verbintenis
	 * @param returnToPage
	 *            return page na annuleren
	 * @param nieuw
	 *            verbintenis is nieuw
	 */
	public EditVerbintenisPage(Verbintenis verbintenis, Verbintenis oudeVerbintenis,
			SecurePage returnToPage, boolean nieuw)
	{
		super(DeelnemerMenuItem.Verbintenis, verbintenis.getDeelnemer(), verbintenis);

		this.returnToPage = returnToPage;

		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(verbintenis,
			new DefaultModelManager(VerbintenisBijlage.class, Vervolgonderwijs.class,
				BPVInschrijving.class, Intakegesprek.class, Plaatsing.class,
				VrijVeldOptieKeuze.class, VerbintenisVrijVeld.class, VerbintenisContract.class,
				Bekostigingsperiode.class, Verbintenis.class)));

		form = new Form<Void>("verbintenisForm");

		opgeslagenVerbintenisMuteerbaar = verbintenisIsMuteerbaar();
		this.nieuw = nieuw;

		if (verbintenis.isSaved() && verbintenis.getOpleiding() != null)
			origineleOpleidingID = verbintenis.getOpleiding().getId();

		if (oudeVerbintenis != null)
		{
			oudeVerbintenisModel =
				ModelFactory.getCompoundChangeRecordingModel(oudeVerbintenis, getManager());
		}
		else
			oudeVerbintenisModel = new Model<Verbintenis>(null);

		plaatsingModel = new PlaatsingModel();

		form.add(bekostigingsperiodeEditPanel = createBekostigingsperiodeEditPanel());

		addOudeVerbintenis();

		addVeldenVerbintenis();
		addCreditsVelden();
		addVeldenBeginEinddatum();
		voegVrijVeldVeldenToeAanForm();
		voegVerbintenisContractenToeAanForm();

		voegBlokkadedatumValidatorToe();

		add(form);

		if (verbintenis.getStatus() == VerbintenisStatus.Intake)
			verbintenis.setStatus(VerbintenisStatus.Voorlopig);

		createComponents();
	}

	@SuppressWarnings("unchecked")
	private void addCreditsVelden()
	{
		WebMarkupContainer creditsContainer = new WebMarkupContainer("creditsContainer");
		boolean hoVerbintenis = getVerbintenis().isHOVerbintenis();
		form.add(creditsContainer.setVisible(hoVerbintenis));
		if (hoVerbintenis)
		{
			OpleidingsVorm vorm = null;
			if (getVerbintenis().getPlaatsingOpPeildatum() != null)
				vorm = getVerbintenis().getPlaatsingOpPeildatum().getOpleidingsVorm();
			RepeatingView creditsPerHoofdfase = new RepeatingView("creditsPerHoofdfase");
			for (Hoofdfase fase : getVerbintenis().getOpleiding().getHoofdfases(vorm))
			{
				WebMarkupContainer wmc = new WebMarkupContainer(creditsPerHoofdfase.newChildId());
				wmc.add(new Label("hoofdfase", "Credits " + fase.getValue().toLowerCase()));
				wmc.add(new TextField<Integer>("credits", new CreditsPerHoofdfaseModel(
					(IModel<Verbintenis>) getDefaultModel(), fase), Integer.class));
				creditsPerHoofdfase.add(wmc);
			}
			creditsContainer.add(creditsPerHoofdfase);
		}
	}

	private void voegBlokkadedatumValidatorToe()
	{
		BlokkadedatumValidatorMode mode =
			(nieuw ? BlokkadedatumValidatorMode.Aanmaken : BlokkadedatumValidatorMode.Bewerken);

		form.add(new BlokkadedatumVerbintenisValidatingFormComponent(
			"blokkadedatumVerbintenisValidator", this, getVerbintenis().getBegindatum(), mode));
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(oudeOpleiding);
		super.onDetach();
	}

	@SuppressWarnings("unchecked")
	private IChangeRecordingModel<Verbintenis> getChangeRecordingModel()
	{
		return (IChangeRecordingModel<Verbintenis>) getDefaultModel();
	}

	private ModelManager getManager()
	{
		return getChangeRecordingModel().getManager();
	}

	private void addOudeVerbintenis()
	{
		WebMarkupContainer oudeVerbintenisContainer =
			new WebMarkupContainer("oudeVerbintenis", oudeVerbintenisModel);
		oudeVerbintenisContainer.setVisible(getOudeVerbintenis() != null);
		oudeVerbintenisContainer.add(new RequiredDatumField("einddatum"));
		oudeVerbintenisContainer.add(new RedenUitschrijvingComboBox("redenUitschrijving",
			SoortRedenUitschrijvingTonen.Verbintenis).setRequired(true));
		form.add(oudeVerbintenisContainer);
	}

	private void addVeldenVerbintenis()
	{
		veldenLinks = new WebMarkupContainer("veldenLinks");
		veldenLinks.setOutputMarkupId(true);
		form.add(veldenLinks);

		addIntakeChoice();
		addStatusVeld();
		addRedenBeeindigen();
		addOpleidingVeld();
		addMaakOnderwijsproductAfnames();
		addPlaatsingVelden();
		addKenniscentrumVeld();

		addExterneCodeVeld();
		addLeerwegVeld();
		addIntensiteitVeld();
		addContacturenPerWeek();
		addLeerprofielVeld();
		addOrganisatieEenheidLocatieVeld();
		addReleventeVooropleidingVeld();
		addwaarschuwingVooropleidingenImage();
		addToelichtingVeld();
		addGehandicapt();

		addInburgeringVelden();
	}

	private void addInburgeringVelden()
	{
		WebMarkupContainer veldenInburgering = new WebMarkupContainer("veldenInburgering")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				if (isInburgering())
				{
					((EnumCombobox< ? >) get("profielInburgering")).setRequired(!isStaatsExamen());
					((EnumCombobox< ? >) get("leerprofiel")).setRequired(!isStaatsExamen());
				}
				return isInburgering();
			}
		};

		veldenInburgering.add(new EnumCombobox<RedenInburgering>("redenInburgering",
			RedenInburgering.values()).setRequired(false));

		veldenInburgering.add(new EnumCombobox<ProfielInburgering>("profielInburgering",
			ProfielInburgering.values()).setRequired(!isStaatsExamen()));
		veldenInburgering.add(new EnumCombobox<Leerprofiel>("leerprofiel", Leerprofiel.values())
			.setRequired(!isStaatsExamen()));
		veldenInburgering.add(new JaNeeCombobox("deelcursus").setRequired(true));

		WebMarkupContainer praktijkexamenRow = new WebMarkupContainer("praktijkexamenRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				if (getOpleiding() != null && getOpleiding().isInburgering())
				{
					String taxCode = getOpleiding().getVerbintenisgebied().getTaxonomiecode();
					return super.isVisible() && !taxCode.startsWith("5.SE");
				}
				return false;
			}
		};
		praktijkexamenRow.setOutputMarkupId(true);
		praktijkexamenRow.add(new EnumCombobox<SoortPraktijkexamen>("soortPraktijkexamen",
			SoortPraktijkexamen.values()).setRequired(true));
		veldenInburgering.add(praktijkexamenRow);

		WebMarkupContainer staatsExamenTypeRow = new WebMarkupContainer("staatsExamenTypeRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return isStaatsExamen();
			}
		};
		staatsExamenTypeRow.setOutputMarkupId(true);
		staatsExamenTypeRow.add(new EnumCombobox<StaatsExamenType>("staatsExamenType",
			StaatsExamenType.values()).setRequired(true));
		veldenInburgering.add(staatsExamenTypeRow);

		veldenInburgering.add(new EnumCombobox<NT2Niveau>("beginNiveauSchriftelijkeVaardigheden",
			NT2Niveau.inburgeringsNiveaus()));
		veldenInburgering.add(new EnumCombobox<NT2Niveau>("eindNiveauSchriftelijkeVaardigheden",
			NT2Niveau.inburgeringsNiveaus()));

		form.add(veldenInburgering);
	}

	private void addGehandicapt()
	{
		WebMarkupContainer gehandicaptRow = new WebMarkupContainer("gehandicaptRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return getVerbintenis().isBVEVerbintenis();
			}
		};
		gehandicaptRow.add(new CheckBox("indicatieGehandicapt", new PropertyModel<Boolean>(
			getDefaultModel(), "indicatieGehandicapt")));
		veldenLinks.add(gehandicaptRow);
	}

	private void addKenniscentrumVeld()
	{
		WebMarkupContainer kenniscentrumRow = new WebMarkupContainer("kenniscentrumRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				Opleiding gekozenOpleiding = getOpleiding();
				if (gekozenOpleiding != null)
				{
					return gekozenOpleiding.isKiesKenniscentrum();
				}
				return false;
			}
		};
		kenniscentrumRow.add(new KenniscentrumCombobox("brin").setRequired(true));
		veldenLinks.add(kenniscentrumRow);
	}

	private List<IVooropleiding> getVooropleidingen()
	{
		return getVerbintenis().getMogelijkeVooropleidingen();
	}

	private void addReleventeVooropleidingVeld()
	{
		relevanteVooropleiding =
			new AbstractAjaxDropDownChoice<IVooropleiding>("relevanteVooropleiding",
				new Model<IVooropleiding>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public IVooropleiding getObject()
					{
						return getVerbintenis().getRelevanteVooropleiding();
					}

					@Override
					public void setObject(IVooropleiding obj)
					{
						getVerbintenis().setRelevanteVooropleiding(obj);
					}
				}, new VooropleidingenModel(), new EntiteitPropertyRenderer("omschrijving"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired()
				{
					return verbintenisIsBRONBVE();
				}

				@Override
				public List<IVooropleiding> getChoices()
				{
					return getVooropleidingen();
				}

				@Override
				protected void onUpdate(AjaxRequestTarget target, IVooropleiding newSelection)
				{
					target.addComponent(categorieVooropleiding);
				}
			};
		relevanteVooropleiding.setLabel(Model.of("Relevante vooropleiding"));
		veldenLinks.add(relevanteVooropleiding);
		categorieVooropleiding =
			new Label("categorieVooropleiding", new PropertyModel<SoortOnderwijs>(
				getDefaultModel(), "relevanteVooropleiding.soortOnderwijs"));
		categorieVooropleiding.setOutputMarkupId(true);
		veldenLinks.add(categorieVooropleiding);
	}

	@InPrincipal(DeelnemerVerbintenissenVerwijderen.class)
	private class VerbintenisVerwijderenButton extends VerwijderButton
	{
		private static final long serialVersionUID = 1L;

		private VerbintenisVerwijderenButton(BottomRowPanel bottomRow, String label,
				String confirmMessage)
		{
			super(bottomRow, label, confirmMessage);
			ComponentUtil.setSecurityCheck(this, new DeelnemerSecurityCheck(new ClassSecurityCheck(
				VerbintenisVerwijderenButton.class), getDeelnemer()));
		}

		@Override
		public boolean isVisible()
		{
			return !nieuw && !statusTeruggezet && verbintenisIsMuteerbaar()
				&& !heeftBronMeldingen()
				&& getVerbintenis().getDeelnemer().getVerbintenissen().size() > 1;
		}

		@Override
		protected void onClick()
		{
			Verbintenis verbintenis = getVerbintenis();
			Deelnemer deelnemer = verbintenis.getDeelnemer();

			/**
			 * haal als eerst de data store op om de pas bezochte verbintenis te
			 * verwijderen. Doen we dit niet dan krijgen we een nullpointer omdat de
			 * verbintenis wordt getoond in de sidebar maar deze is reeds verwijderd.
			 * 
			 * @mantis 0040658
			 */
			RecenteDeelnemersDataStore deelnemerStore =
				EduArteSession.get().getSideBarDataStore(RecenteDeelnemersDataStore.class);
			deelnemerStore.getInschrijvingenModel().getObject().remove(getVerbintenis());
			deelnemerStore.getInschrijvingenModel().detach();

			/**
			 * Verwijder vervolgens het object en redirect
			 */
			deelnemer.getVerbintenissen().remove(getVerbintenis());
			// Verwijder onderwijsproductafnamecontexten (kan niet automatisch door
			// het model gebeuren omdat deze objecten ook aparte verwijderd moeten
			// kunnen worden bij het aanpassen van de verbintenis).
			for (OnderwijsproductAfnameContext context : getVerbintenis().getAfnameContexten())
			{
				context.delete();
			}

			// Ook eventuele examendeelnames wissen
			for (Examendeelname deelname : getVerbintenis().getExamendeelnames())
			{
				for (ExamenstatusOvergang overgang : deelname.getStatusovergangen())
				{
					overgang.delete();
				}
				deelname.delete();
			}

			// Verwijder links naar deze verbintenis als de 'Relevante Verbintenis' van
			// andere verbintenissen
			VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
			filter.setRelevanteVerbintenis(getVerbintenis());
			List<Verbintenis> verbintenissen =
				DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class).list(filter);
			for (Verbintenis v : verbintenissen)
			{
				v.setRelevanteVerbintenis(null);
			}

			getVerbintenis().getAfnameContexten().clear();
			getChangeRecordingModel().deleteObject();
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();

			setResponsePage(new DeelnemerkaartPage(deelnemer));
		}
	}

	private final class VooropleidingenModel extends LoadableDetachableModel<List<IVooropleiding>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<IVooropleiding> load()
		{
			return getVooropleidingen();
		}
	}

	private void addPlaatsingVelden()
	{
		PlaatsingRow leerjaarRow = new PlaatsingRow("leerjaarRow");
		AbstractAjaxDropDownChoice<Integer> leerjaar =
			new AbstractAjaxDropDownChoice<Integer>("leerjaar", new PropertyModel<Integer>(
				plaatsingModel, "leerjaar"), new LeerjarenModel(getChangeRecordingModel()))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired()
				{
					VerbintenisStatus status = getVerbintenis().getStatus();
					if (!status.isMuteerbaar() && !status.isAfgesloten())
					{
						Opleiding gekozenOpleiding = getVerbintenis().getOpleiding();
						if (gekozenOpleiding != null)
							return getVerbintenis().isVOVerbintenis()
								&& !"0090".equals(gekozenOpleiding.getVerbintenisgebied()
									.getExterneCode());
					}
					return false;
				}
			};
		leerjaarRow.add(leerjaar);
		veldenLinks.add(leerjaarRow);

		PlaatsingRow plaatsingsgroepRow = new PlaatsingRow("plaatsingsgroepRow");
		GroepZoekFilter filter = GroepZoekFilter.createDefaultFilter();
		filter.setPlaatsingsgroep(Boolean.TRUE);
		filter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(plaatsingModel,
			"verbintenis.organisatieEenheid"));
		plaatsingsgroepRow.add(new GroepSearchEditor("groep", new PropertyModel<Groep>(
			plaatsingModel, "groep"), filter));
		veldenLinks.add(plaatsingsgroepRow);

		PlaatsingRow jarenPraktijkonderwijsRow =
			new PlaatsingRow("jarenPraktijkonderwijsRow", true);
		jarenPraktijkonderwijsRow.add(new IntegerRangeCombobox("jarenPraktijkonderwijs",
			new PropertyModel<Integer>(plaatsingModel, "jarenPraktijkonderwijs"), 1, 9)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired()
			{
				VerbintenisStatus status = getVerbintenis().getStatus();
				if (!status.isMuteerbaar() && !status.isAfgesloten())
				{
					Opleiding gekozenOpleiding = getVerbintenis().getOpleiding();
					if (gekozenOpleiding != null)
						return gekozenOpleiding.getVerbintenisgebied().getTaxonomie().isVO()
							&& "0090".equals(gekozenOpleiding.getVerbintenisgebied()
								.getExterneCode());
				}
				return false;
			}
		});
		veldenLinks.add(jarenPraktijkonderwijsRow);
	}

	private void addIntakeChoice()
	{
		WebMarkupContainer rowIntakeSelect = new WebMarkupContainer("rowIntakeSelect")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return nieuw && getOudeVerbintenis() == null;
			}
		};

		DropDownChoice<Verbintenis> intakeSelect =
			new DropDownChoice<Verbintenis>("intakeSelect", new HibernateModel<Verbintenis>(
				getVerbintenis()), new HibernateObjectListModel<List<Verbintenis>, Verbintenis>(
				getDeelnemer().getIntakes()), new ChoiceRenderer<Verbintenis>(
				"organisatieEenheid.naam"));
		intakeSelect.setNullValid(true);

		intakeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				if (getFormComponent().getModelObject() != null)
				{
					Verbintenis oudeVerbintenis =
						(Verbintenis) EditVerbintenisPage.this.getDefaultModelObject();
					if (oudeVerbintenis.isSaved())
						oudeVerbintenis.setStatus(VerbintenisStatus.Intake);
					EditVerbintenisPage.this.setDefaultModelObject(getFormComponent()
						.getModelObject());
				}
				else
				{
					EditVerbintenisPage.this.setDefaultModelObject(getDeelnemer()
						.nieuweVerbintenis(false));
				}
				getChangeRecordingModel().recalculate();
				target.addComponent(form);
			}

		});
		rowIntakeSelect.add(intakeSelect);

		veldenLinks.add(rowIntakeSelect);
	}

	private BekostigingsperiodeEditPanel createBekostigingsperiodeEditPanel()
	{
		BekostigingsperiodeEditPanel ret =
			new BekostigingsperiodeEditPanel("bekostigingEditPanel",
				new PropertyModel<List<Bekostigingsperiode>>(getDefaultModel(),
					"bekostigingsperiodes"), getManager())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible()
						&& (getVerbintenis() != null && getVerbintenis().isBOVerbintenis())
						&& (getBekostigd() == Bekostigd.Gedeeltelijk);
				}

				@Override
				public Bekostigingsperiode createNewT()
				{
					Bekostigingsperiode periode = new Bekostigingsperiode();
					periode.setVerbintenis(getVerbintenis());

					if (getLaatsteEinddatumBekostigingsperiodes() != null
						&& (getEinddatumVerbintenis() == null || getLaatsteEinddatumBekostigingsperiodes()
							.before(getEinddatumVerbintenis())))
					{
						periode.setBegindatum(TimeUtil.getInstance().addDays(
							getLaatsteEinddatumBekostigingsperiodes(), 1));
					}
					else if (getBekostigingsperiodes().isEmpty())
					{
						periode.setBegindatum(getVerbintenis().getBegindatum());
					}

					periode.setEinddatum(getEinddatumVerbintenis());

					return periode;
				}

				@Override
				protected void onSaveCurrent(AjaxRequestTarget target, Bekostigingsperiode object)
				{
					if (object != null)
						dataBijwerken(object, target);
				}

				@Override
				protected void onSaveNew(AjaxRequestTarget target, Bekostigingsperiode object)
				{
					if (object != null)
						dataBijwerken(object, target);
				}

				/**
				 * Werk de overige bekostigingsperiodes bij qua begin- en einddata.
				 * 
				 */
				private void dataBijwerken(Bekostigingsperiode nieuwePeriode,
						AjaxRequestTarget target)
				{
					List<Bekostigingsperiode> teVerwijderenPeriodes =
						new ArrayList<Bekostigingsperiode>();

					for (Bekostigingsperiode bestaandePeriode : getBekostigingsperiodes())
					{
						if (bestaandePeriode != nieuwePeriode)
						{
							if (bestaandePeriode.getBegindatum().equals(
								nieuwePeriode.getBegindatum()))
							{
								warn("Er bestaat al	 een bekostigingsperiode met deze begindatum.");
								refreshFeedback(target);

								// Niet direct uit lijst verwijderen om
								// ConcurrentModificationException te voorkomen
								teVerwijderenPeriodes.add(nieuwePeriode);
							}
							// ligt de andere periode voor deze?
							else if (bestaandePeriode.getBegindatum().before(
								nieuwePeriode.getBegindatum()))
							{
								// werk de einddatum van de andere periode bij indien deze
								// overlapt
								if (bestaandePeriode.getEinddatum() == null
									|| !bestaandePeriode.getEinddatum().before(
										nieuwePeriode.getBegindatum()))
									bestaandePeriode.setEinddatum(TimeUtil.getInstance().addDays(
										nieuwePeriode.getBegindatum(), -1));
							}
							else
							{
								// deze periode ligt voor de andere periode
								// werk de einddatum van deze periode bij indien deze
								// overlapt
								if (nieuwePeriode.getEinddatum() == null
									|| !nieuwePeriode.getEinddatum().before(
										bestaandePeriode.getBegindatum()))
									nieuwePeriode.setEinddatum(TimeUtil.getInstance().addDays(
										bestaandePeriode.getBegindatum(), -1));
							}
						}
					}

					for (Bekostigingsperiode teVerwijderenPeriode : teVerwijderenPeriodes)
					{
						getBekostigingsperiodes().remove(teVerwijderenPeriode);
					}

					sorteerBekostigingsperiodesOpBegindatum(getBekostigingsperiodes());
					sorteerBekostigingsperiodesOpBegindatum(getModelObject());

				}
			};
		ret.setOutputMarkupPlaceholderTag(true);
		ret.getCustomDataPanel().setButtonsVisible(false);
		ret.getCustomDataPanel().setTitleVisible(false);
		return ret;
	}

	private void voegVrijVeldVeldenToeAanForm()
	{
		VrijVeldEntiteitEditPanel<Verbintenis> VVEEPanel =
			new VrijVeldEntiteitEditPanel<Verbintenis>("vrijVelden", getChangeRecordingModel());
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.VERBINTENIS);
		VVEEPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		VVEEPanel.getVrijVeldZoekFilter().setFilterOpTaxonomie(true);
		VVEEPanel.getVrijVeldZoekFilter().setOpleiding(
			new PropertyModel<Opleiding>(getDefaultModel(), "opleiding"));
		form.add(VVEEPanel);
	}

	private void voegVerbintenisContractenToeAanForm()
	{
		VerbintenisContractEditPanel VCPanel =
			new VerbintenisContractEditPanel("contracten",
				new PropertyModel<List<VerbintenisContract>>(getDefaultModel(), "contracten"),
				getManager(), new VerbintenisContractTable())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public VerbintenisContract createNewT()
				{
					return new VerbintenisContract(getVerbintenis());
				}

				@Override
				protected void onSaveCurrent(AjaxRequestTarget target, VerbintenisContract object)
				{
					target.addComponent(veldenBeginEinddatum);
				}

				@Override
				protected void onSaveNew(AjaxRequestTarget target, VerbintenisContract object)
				{
					target.addComponent(veldenBeginEinddatum);
				}
			};

		form.add(VCPanel);
	}

	private void addVeldenBeginEinddatum()
	{
		veldenBeginEinddatum =
			new AutoFieldSet<Verbintenis>("veldenRechts", getChangeRecordingModel());

		veldenBeginEinddatum.setRenderMode(RenderMode.EDIT);
		veldenBeginEinddatum.setSortAccordingToPropertyNames(true);
		veldenBeginEinddatum.setOutputMarkupId(true);
		veldenBeginEinddatum.setPropertyNames("begindatum", "datumAanmelden", "datumAkkoord",
			"datumEersteActiviteit", "geplandeEinddatum", "examenDatum", "cohort", "bekostigd",
			"datumOvereenkomstOndertekend");
		veldenBeginEinddatum.addFieldModifier(new LabelModifier("datumOvereenkomstOndertekend",
			"Overeenkomst ondertekend"));
		veldenBeginEinddatum.addFieldModifier(new VisibilityModifier("datumEersteActiviteit",
			new IsInburgeringModel()));
		veldenBeginEinddatum.addFieldModifier(new VisibilityModifier("datumAanmelden",
			new IsInburgeringContractModel(true)));
		veldenBeginEinddatum.addFieldModifier(new VisibilityModifier("datumAkkoord",
			new IsInburgeringContractModel(false)));
		veldenBeginEinddatum.addFieldModifier(new VisibilityModifier(
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return isInburgering() || isStaatsExamen();
				}
			}, "examenDatum"));

		veldenBeginEinddatum.addFieldModifier(new EduArteAjaxRefreshModifier("bekostigd",
			bekostigingsperiodeEditPanel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				if (getBekostigd() != Bekostigd.Gedeeltelijk)
				{
					if (getBekostigingsperiodes().size() == 1
						&& !getBekostigingsperiodes().get(0).isSaved())
						getBekostigingsperiodes().clear();
					else
						for (Bekostigingsperiode p : getBekostigingsperiodes())
							p.setBekostigd(getBekostigd() == Bekostigd.Ja);
				}
				else
				{
					if (getBekostigingsperiodes().isEmpty())
						getBekostigingsperiodes().add(createNewBekostigingsperiode());
				}

			}
		});
		veldenBeginEinddatum.addFieldModifier(new VisibilityModifier(
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return getVerbintenis().isBOVerbintenis();
				}
			}, "bekostigd"));

		veldenBeginEinddatum.addFieldModifier(new EnableModifier(
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					boolean opgeslagen = getVerbintenis().isSaved();
					boolean gedeeltelijkBekostigd =
						Bekostigd.Gedeeltelijk.equals(getVerbintenis().getBekostigd());
					boolean bronCommuniceerbaar = getVerbintenis().isBronCommuniceerbaar();

					return !opgeslagen || !gedeeltelijkBekostigd || !bronCommuniceerbaar;
				}
			}, "bekostigd"));

		veldenBeginEinddatum.addFieldModifier(new ConstructorArgModifier("bekostigd",
			new LoadableDetachableModel<List<Bekostigd>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Bekostigd> load()
				{
					if (getVerbintenis().getIntensiteit() != null
						&& getVerbintenis().getIntensiteit().equals(Intensiteit.Examendeelnemer))
					{
						return Arrays.asList(new Bekostigd[] {Bekostigd.Nee});
					}
					else
					{
						return Arrays.asList(Bekostigd.values());
					}
				}
			}));

		veldenBeginEinddatum.addModifier("begindatum", new AjaxFormComponentUpdatingBehavior(
			"onblur")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				Verbintenis verbintenis = getVerbintenis();
				if (isInitialGeplandeDatum() || verbintenis.getGeplandeEinddatum() == null)
				{
					updateGeplandeEinddatum(verbintenis);
					target.addComponent(geplandeEinddatumVeld);
				}
				if (verbintenis.getBegindatum() != null)
				{
					verbintenis.setCohort(Cohort.getCohort(verbintenis.getBegindatum()));
					target.addComponent(cohortCombo);
				}
				if (getPlaatsing() != null)
					getPlaatsing().setBegindatum(verbintenis.getBegindatum());

				target.addComponent(relevanteVooropleiding);
				target.addComponent(categorieVooropleiding);
				target.addComponent(waarschuwingVooropleidingen);
			}
		});
		veldenBeginEinddatum.addModifier("geplandeEinddatum",
			new AjaxFormComponentUpdatingBehavior("onblur")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					initialGeplandeDatum = false;
				}
			});
		veldenBeginEinddatum.addFieldModifier(new EnableModifier(new PropertyModel<Boolean>(this,
			"opgeslagenVerbintenisMuteerbaar"), "begindatum", "geplandeEinddatum"));
		form.add(veldenBeginEinddatum);
	}

	private Date getLaatsteEinddatumBekostigingsperiodes()
	{
		Date laatsteEinddatum = null;

		for (Bekostigingsperiode bekostigingsperiode : getBekostigingsperiodes())
		{
			if (bekostigingsperiode.getEinddatum() != null)
				if (laatsteEinddatum == null)
				{
					laatsteEinddatum = bekostigingsperiode.getEinddatum();
				}
				else if (bekostigingsperiode.getEinddatum().after(laatsteEinddatum))
					laatsteEinddatum = bekostigingsperiode.getEinddatum();
		}

		return laatsteEinddatum;
	}

	/**
	 * Koppel de onderwijsproductafnames los van de productregels indien er een nieuwe
	 * opleiding gekozen is. Mantis: 62481
	 */
	private void removeOnderwijsproductAfnameContextenVanOudeOpleiding()
	{
		if (origineleOpleidingID != null && getVerbintenis().getOpleiding() != null
			&& origineleOpleidingID.compareTo(getVerbintenis().getOpleiding().getId()) != 0)
		{
			List<OnderwijsproductAfnameContext> contexten = getVerbintenis().getAfnameContexten();
			for (OnderwijsproductAfnameContext c : contexten)
			{
				c.delete();
				c.commit();
			}
			getVerbintenis().setAfnameContexten(new ArrayList<OnderwijsproductAfnameContext>());
		}
	}

	private void processOnSubmit()
	{
		boolean opgeslagen = getVerbintenis().isSaved();
		if (opgeslagen)
			removeOnderwijsproductAfnameContextenVanOudeOpleiding();
		boolean success = true;
		if (getBekostigd() == Bekostigd.Gedeeltelijk)
		{
			success = processOnSubmitGedeeltelijkBekostigd();
		}
		if (success)
		{
			Plaatsing plaatsing = getPlaatsing();
			if (plaatsing != null)
			{
				plaatsing.setBegindatum(getVerbintenis().getBegindatum());
				plaatsing.setEinddatum(getVerbintenis().getEinddatum());
			}
			getChangeRecordingModel().saveObject();
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();

			if (getOudeVerbintenis() != null)
			{
				Plaatsing actievePlaatsingOudeVerbintenis =
					getOudeVerbintenis().getPlaatsingOpDatum(getOudeVerbintenis().getEinddatum());

				if (actievePlaatsingOudeVerbintenis != null)
				{
					Plaatsing nieuwePlaatsing =
						new HibernateObjectCopyManager(Plaatsing.class)
							.copyObject(actievePlaatsingOudeVerbintenis);
					nieuwePlaatsing.setVerbintenis(getVerbintenis());
					nieuwePlaatsing.setBegindatum(getVerbintenis().getBegindatum());
					nieuwePlaatsing.setIdInOudPakket(null);
					nieuwePlaatsing.saveOrUpdate();

					getVerbintenis().getPlaatsingen().add(nieuwePlaatsing);

					actievePlaatsingOudeVerbintenis.setEinddatum(getOudeVerbintenis()
						.getEinddatum());
					actievePlaatsingOudeVerbintenis.saveOrUpdate();
				}
				else
				{
					Plaatsing nieuwePlaatsing = getVerbintenis().nieuwePlaatsing();
					nieuwePlaatsing.saveOrUpdate();
				}
			}

			if (onderwijsproductAfnamesMakenCombobox.isVisible()
				&& onderwijsproductAfnamesMakenCombobox.getModelObject().booleanValue())
			{
				getVerbintenis().maakDefaultProductregelKeuzes();
			}
			if (!opgeslagen)
			{
				getDeelnemer().getVerbintenissen().add(0, getVerbintenis());
				getDeelnemer().update();
			}

			DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
			setResponsePage(new DeelnemerVerbintenisPage(getDeelnemer(), getVerbintenis()));
		}
	}

	private Plaatsing getPlaatsing()
	{
		Plaatsing plaatsing = plaatsingModel.getObject();
		return plaatsing;
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

		voegVerwijderenKnopToe(panel);
		voegStatusTerugzettenKnopToe(panel);
	}

	private void voegVerwijderenKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new VerbintenisVerwijderenButton(panel, "Verwijderen",
			"Weet u zeker dat u deze verbintenis wilt verwijderen?"));
	}

	private boolean heeftBronMeldingen()
	{
		BronMeldingZoekFilter filter = new BronMeldingZoekFilter(getVerbintenis());
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		long aantalMeldingen =
			DataAccessRegistry.getHelper(BronDataAccessHelper.class).getAantalMeldingen(filter);
		return aantalMeldingen > 0;
	}

	private void voegStatusTerugzettenKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new StatusTerugzettenKnop(panel, "Status terugzetten", CobraKeyAction.GEEN,
			ButtonAlignment.LEFT, getStatusTerugzettenConfirmationMessage()));
	}

	private String getStatusTerugzettenConfirmationMessage()
	{
		String nieuweStatus = "Voorlopig";
		String melding = "Verwijdering";
		if (getVerbintenis().getStatus() == VerbintenisStatus.Beeindigd)
		{
			nieuweStatus = "Definitief";
			melding = "Aanpassing";
		}
		return "Weet u zeker dat u de status wilt terugzetten naar " + nieuweStatus
			+ "? Dit resulteert in een " + melding + " bij BRON.";
	}

	@InPrincipal(DeelnemerVerbintenisStatusovergang.class)
	private final class StatusTerugzettenKnop extends AbstractConfirmationLinkButton
	{
		private static final long serialVersionUID = 1L;

		private StatusTerugzettenKnop(BottomRowPanel bottomRow, String label, ActionKey action,
				ButtonAlignment alignment, String confirmationMessage)
		{
			super(bottomRow, label, action, alignment, confirmationMessage);
			ComponentUtil.setSecurityCheck(this, new DeelnemerSecurityCheck(new ClassSecurityCheck(
				StatusTerugzettenKnop.class), getDeelnemer()));
		}

		@Override
		protected void onClick()
		{
			if (getVerbintenis().getStatus() == VerbintenisStatus.Beeindigd)
			{
				getVerbintenis().setStatus(VerbintenisStatus.Definitief);
				getVerbintenis().setEinddatum(null);
				getVerbintenis().setRedenUitschrijving(null);
			}
			else
			{
				getVerbintenis().setStatus(VerbintenisStatus.Voorlopig);
				getVerbintenis().setOvereenkomstnummer(
					DataAccessRegistry.getHelper(NummerGeneratorDataAccessHelper.class)
						.newOvereenkomstnummer());
				if (getVerbintenis().getBekostigd() == Bekostigd.Ja)
					getVerbintenis().setBekostigd(Bekostigd.Nee);
				opgeslagenVerbintenisMuteerbaar = true;
				organisatieEenheidLocatieChoice.getLocatieCombo().setRequired(
					!verbintenisIsMuteerbaar());
				relevanteVooropleiding.setRequired(verbintenisIsBRONBVE());

			}
			statusCombobox.setChoices(Arrays.asList(getVerbintenis().getStatus()));

			statusTeruggezet = true;

			setConfirmationMessage(getStatusTerugzettenConfirmationMessage());
		}

		@Override
		public boolean isVisible()
		{
			return getVerbintenis() != null && getVerbintenis().getStatus().isBronCommuniceerbaar();
		}
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		begindatumVeld = (DatumField) veldenBeginEinddatum.findFieldComponent("begindatum");
		geplandeEinddatumVeld =
			(DatumField) veldenBeginEinddatum.findFieldComponent("geplandeEinddatum");
		geplandeEinddatumVeld.setRequired(verbintenisIsBronCommuniceerbaar() && verbintenisIsBVE());
		cohortCombo = (CohortCombobox) veldenBeginEinddatum.findFieldComponent("cohort");

		Date geboorteDatum = getDeelnemer().getPersoon().getGeboortedatum();
		if (geboorteDatum != null)
		{
			form.add(new DatumGroterOfGelijkDatumValidator("Begindatum", begindatumVeld,
				geboorteDatum));
		}

		form.add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				FormComponent< ? >[] formCompontens = {begindatumVeld, cohortCombo};
				return formCompontens;
			}

			@Override
			public void validate(Form< ? > formP)
			{
				Date begindatum = begindatumVeld.getDatum();
				Cohort ch = cohortCombo.getCohort();
				if (begindatum != null && ch != null)
					if (begindatum.before(ch.getBegindatum()))
					{
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("begincohort", TimeUtil.getInstance().formatDate(
							ch.getBegindatum()));
						error(begindatumVeld, "voorcohort", params);
					}
			}
		});

		form.add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				FormComponent< ? >[] formCompontens = {begindatumVeld, opleiding};
				return formCompontens;
			}

			@Override
			public void validate(Form< ? > formP)
			{
				Date begindatum = begindatumVeld.getDatum();
				if (begindatum != null && opleiding.getModelObject() != null)
				{
					Opleiding geselecteerdeOpleiding = opleiding.getModelObject();

					if (geselecteerdeOpleiding.getDatumLaatsteInschrijving() != null
						&& begindatum.after(geselecteerdeOpleiding.getDatumLaatsteInschrijving()))
					{
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("datumlaatsteinschrijving", TimeUtil.getInstance().formatDate(
							geselecteerdeOpleiding.getDatumLaatsteInschrijving()));
						error(begindatumVeld, "laatsteinschrijving", params);
					}
				}
			}
		});

		form.add(new BegindatumVoorEinddatumValidator(begindatumVeld, geplandeEinddatumVeld));
		form.add(new BekostigingValidator((IntensiteitCombobox) intensiteitRow.get("intensiteit"),
			getBekostigdCombobox()));
		Verbintenis verbintenis = getVerbintenis();

		if (!hasBeenRendered())
		{
			oudeOpleiding = ModelFactory.getModel(verbintenis.getOpleiding());
			form.add(new BronVerbintenisMutatieToegestaanValidator(statusCombobox, verbintenis
				.getBegindatum(), verbintenis.getEinddatum(), verbintenis.getStatus(),
				oudeOpleiding, verbintenis.getIntensiteit(), verbintenis.getBekostigd(),
				getChangeRecordingModel()));
		}

		form.add(new IngangsdatumOpleidingNaBegindatumValidator(opleiding, begindatumVeld));

	}

	@SuppressWarnings("unchecked")
	private EnumCombobox<Bekostigd> getBekostigdCombobox()
	{
		return (EnumCombobox<Bekostigd>) veldenBeginEinddatum.findFieldComponent("bekostigd");
	}

	private boolean processOnSubmitGedeeltelijkBekostigd()
	{
		Boolean succes = true;

		List<Bekostigingsperiode> bplist = getBekostigingsperiodes();

		Date begindatumVerbintenis = getVerbintenis().getBegindatum();
		Date einddatumVerbintenis = getVerbintenis().getEinddatum();

		for (int i = 0; i < bplist.size(); i++)
		{
			Date begindatumHuidigeBekostigingsperiode = bplist.get(i).getBegindatum();
			Date einddatumHuidigeBekostigingsperiode = bplist.get(i).getEinddatum();

			// Vergelijk einddatum verbintenis i met begindatum verbintenis i+1
			if (i < bplist.size() - 1)
			{
				Date begindatumVolgendeBekostigingsperiode = bplist.get(i + 1).getBegindatum();

				if (einddatumHuidigeBekostigingsperiode != null
					&& begindatumVolgendeBekostigingsperiode != null)
				{
					Date einddatumHuidigeBekostigingsperiodePlus1Dag =
						TimeUtil.getInstance().addDays(einddatumHuidigeBekostigingsperiode, 1);

					if (!einddatumHuidigeBekostigingsperiodePlus1Dag
						.equals(begindatumVolgendeBekostigingsperiode))
					{
						succes = false;
						error("Bekostigingsperiode " + (i + 2)
							+ " sluit niet aan op de vorige periode.");
					}
				}
				else
				{
					succes = false;
					error("Eindatum niet-laatste periode mag niet leeg zijn.");
				}
			}

			// KOL 2010-03-08: Onderstaande checks zijn niet nodig voor BRON, en gezien de
			// gegevens die uit nOISe komen ook niet wenselijk.

			// Controleer of begindatum bekostigingsperiode niet voor begindatum
			// verbintenis ligt
			if (CONTROLEER_OP_BEGIN_EN_EINDDATA_BEKOSTIGINGSPERIODES)
			{
				if (begindatumHuidigeBekostigingsperiode != null && begindatumVerbintenis != null)
				{
					if (begindatumHuidigeBekostigingsperiode.before(begindatumVerbintenis))
					{
						error("Begindatum bekostigingsperiode " + (i + 1)
							+ " ligt voor begindatum verbintenis");
					}
				}

				// Controleer of einddatum bekostigingsperiode niet na einddatum
				// verbintenis ligt
				if (einddatumHuidigeBekostigingsperiode != null && einddatumVerbintenis != null)
				{
					if (einddatumHuidigeBekostigingsperiode.after(einddatumVerbintenis))
					{
						error("Einddatum bekostigingsperiode " + (i + 1)
							+ " ligt na einddatum verbintenis.");
					}
				}
			}

		}

		if (CONTROLEER_OP_BEGIN_EN_EINDDATA_BEKOSTIGINGSPERIODES)
		{
			Date begindatumEersteBekostigingsperiode = bplist.get(0).getBegindatum();
			if (begindatumEersteBekostigingsperiode != null && begindatumVerbintenis != null)
			{
				if (!begindatumEersteBekostigingsperiode.equals(begindatumVerbintenis))
				{
					succes = false;
					error("Begindatum bekostigingsperiode is niet gelijk aan begindatum verbintenis");
				}
			}
		}

		if (einddatumVerbintenis != null)
		{
			Date einddatumLaatsteBekostigingsperiode = bplist.get(bplist.size() - 1).getEinddatum();
			if (einddatumLaatsteBekostigingsperiode != null)
			{
				if (CONTROLEER_OP_BEGIN_EN_EINDDATA_BEKOSTIGINGSPERIODES)
				{
					if (!einddatumVerbintenis.equals(einddatumLaatsteBekostigingsperiode))
					{
						succes = false;
						error("Einddatum laatste bekostigingsperiode is niet gelijk aan einddatum verbintenis.");
					}
				}
			}
			else
			{
				succes = false;
				error("Einddatum laatste bekostigingsperiode mag niet leeg zijn.");
			}
		}
		return succes;
	}

	private void addStatusVeld()
	{
		// status
		VerbintenisStatus status = VerbintenisStatus.Voorlopig;
		if (getVerbintenis() != null && getVerbintenis().getStatus() != null)
			status = getVerbintenis().getStatus();
		statusCombobox =
			new EnumCombobox<VerbintenisStatus>("statusSelect",
				new PropertyModel<VerbintenisStatus>(getDefaultModel(), "status"), true,
				getToegestaneVervolgstatussen(status))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target, VerbintenisStatus newSelection)
				{
					boolean verplicht = !verbintenisIsMuteerbaar() && !verbintenisIsAfgesloten();
					organisatieEenheidLocatieChoice.getLocatieCombo().setRequired(verplicht);
					geplandeEinddatumVeld.setRequired(verbintenisIsBronCommuniceerbaar()
						&& verbintenisIsBVE());

					if (getVerbintenis().getStatus() == VerbintenisStatus.Beeindigd
						&& getVerbintenis().getStatusZonderBeeindigdCheck() == VerbintenisStatus.Voorlopig)
					{
						getVerbintenis().setEinddatum(null);
					}

					if (newSelection == VerbintenisStatus.Definitief
						&& getVerbintenis().getBekostigd() == Bekostigd.Nee
						&& getVerbintenis().isBOVerbintenis())
					{
						getVerbintenis().setBekostigd(Bekostigd.Ja);

						EnumCombobox<Bekostigd> bekostigd = getBekostigdCombobox();
						target.addComponent(bekostigd);
						target.addComponent(bekostigingsperiodeEditPanel);
					}
					target.addComponent(veldenLinks);
					target.addComponent(geplandeEinddatumVeld);
				}
			};
		statusCombobox.setRequired(true);
		statusCombobox.add(new BronValidator<VerbintenisStatus>(this));
		veldenLinks.add(statusCombobox);
	}

	private VerbintenisStatus[] getToegestaneVervolgstatussen(VerbintenisStatus status)
	{
		return status.getVervolgEnHuidige(heeftExtraStatusovergangAutorisatie(), isVerbintenisVO());
	}

	private boolean isVerbintenisVO()
	{
		if (getVerbintenis().getTaxonomie() != null)
			return getVerbintenis().getTaxonomie().isVO() && getVerbintenis().isVOVerbintenis();
		else
			return false;
	}

	private boolean heeftExtraStatusovergangAutorisatie()
	{
		DataSecurityCheck dataCheck =
			new DataSecurityCheck(SecureComponentHelper.alias(EditVerbintenisPage.class)
				+ EXTRA_STATUSOVERGANGEN);

		DeelnemerSecurityCheck deelnemerCheck =
			new DeelnemerSecurityCheck(dataCheck, getDeelnemer());

		return deelnemerCheck.isActionAuthorized(Enable.class);
	}

	private void addRedenBeeindigen()
	{
		WebMarkupContainer redenBeeindigenRow = new WebMarkupContainer("redenBeeindigenRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return getVerbintenis() != null && getVerbintenis().getStatus().isAfgesloten();
			}
		};
		RedenUitschrijvingComboBox redenBeeindigenComboBox =
			new RedenUitschrijvingComboBox("redenUitschrijving",
				new PropertyModel<RedenUitschrijving>(getDefaultModel(), "redenUitschrijving"),
				SoortRedenUitschrijvingTonen.Verbintenis);
		redenBeeindigenRow.add(redenBeeindigenComboBox);
		veldenLinks.add(redenBeeindigenRow);
	}

	private void addOpleidingVeld()
	{
		opleidingZoekFilter = new OpleidingZoekFilter();
		opleidingZoekFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(
			getDefaultModel(), "organisatieEenheid"));
		opleidingZoekFilter
			.setLocatieModel(new PropertyModel<Locatie>(getDefaultModel(), "locatie"));

		opleiding = new OpleidingSearchEditor("opleiding", null, opleidingZoekFilter)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				// je kunt hem aanpassen als je deze pagina binnenkwam met een
				// muteerbare status (of met status terugzetten)
				return opgeslagenVerbintenisMuteerbaar;
			}

			@Override
			public boolean isRequired()
			{
				// je moet hem invullen als de huidige gekozen status niet muteerbaar
				// is (tenzij hij afgesloten is)
				return !verbintenisIsMuteerbaar() && !verbintenisIsAfgesloten();
			}

			@Override
			protected void onBeforeRender()
			{
				super.onBeforeRender();
				// Zet caching uit zodat de ondertussen aangepaste
				// organisatieeenheid/locatie doorwerkt in het zoekveld
				if (!hasBeenRendered())
					getSearchField().getOptions().put("cacheLength", 0);
			}

		};
		opleiding.setEnabled(verbintenisIsMuteerbaar());
		opleiding.setLabel(Model.of("opleiding"));
		opleiding.addListener(new ISelectListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				updateGeplandeEinddatum(getVerbintenis());
				if (getVerbintenis().getOpleiding() != null)
				{
					getVerbintenis().setIntensiteit(
						getVerbintenis().getOpleiding().getDefaultIntensiteit());
				}
				target.addComponent(EditVerbintenisPage.this);
			}
		});
		veldenLinks.add(opleiding);
	}

	private void addMaakOnderwijsproductAfnames()
	{
		WebMarkupContainer container =
			new WebMarkupContainer("onderwijsproductAfnamesMakenContainer");
		veldenLinks.add(container);
		onderwijsproductAfnamesMakenCombobox =
			new JaNeeCombobox("onderwijsproductAfnamesMaken", Model.of(nieuw));
		onderwijsproductAfnamesMakenCombobox.setNullValid(false).setRequired(true);
		container.setVisible(nieuw);
		container.add(onderwijsproductAfnamesMakenCombobox);
	}

	private void addExterneCodeVeld()
	{
		Label externeCodeLabel = new Label("externeCodeLabel", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return getExterneCodelLabel();
			}

		});
		veldenLinks.add(externeCodeLabel);
		ReadonlyTextField<String> externeCode = new ReadonlyTextField<String>("externeCode");
		veldenLinks.add(externeCode);
	}

	private void addLeerwegVeld()
	{
		ReadonlyTextField<MBOLeerweg> leerweg =
			new ReadonlyTextField<MBOLeerweg>("opleiding.leerweg")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					Verbintenis verbintenis = getVerbintenis();
					return verbintenis != null && verbintenis.isBOVerbintenis();
				}
			};
		veldenLinks.add(leerweg);

	}

	private void addIntensiteitVeld()
	{
		intensiteitRow = new WebMarkupContainer("intensiteitRow")
		{
			private IntensiteitCombobox child;

			private static final long serialVersionUID = 1L;
			{
				child = new IntensiteitCombobox("intensiteit", false, EditVerbintenisPage.this)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public List<Intensiteit> getChoices()
					{
						Verbintenis verbintenis = getVerbintenis();
						List<Intensiteit> choices = new ArrayList<Intensiteit>();

						if (verbintenis.isVOVerbintenis())
							choices.addAll(Arrays.asList(Intensiteit.Voltijd,
								Intensiteit.Examendeelnemer));
						else
						{
							choices.addAll(Arrays.asList(Intensiteit.values()));
							if (verbintenis.getOpleiding() != null
								&& verbintenis.getOpleiding().getLeerweg() == MBOLeerweg.BBL)
							{
								choices.remove(Intensiteit.Voltijd);
							}
						}
						return choices;
					}

					@Override
					public boolean isEnabled()
					{
						// je kunt hem aanpassen als je deze pagina binnenkwam met een
						// muteerbare status (of met status terugzetten)
						return opgeslagenVerbintenisMuteerbaar;
					}
				};

				child.add(new AjaxFormComponentUpdatingBehavior("onchange")
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target)
					{
						EnumCombobox<Bekostigd> bekostigd = getBekostigdCombobox();

						if (!Bekostigd.Gedeeltelijk.equals(bekostigd.getModelObject())
							&& child.getModelObject() != null
							&& getVerbintenis().getStatus() == VerbintenisStatus.Definitief)
						{
							bekostigd.setModelObject(Bekostigd.Ja);
						}

						target.addComponent(bekostigd);
						target.addComponent(bekostigingsperiodeEditPanel);
					}
				});

				child.add(new IntensiteitLeerwegKoppelValidator<Intensiteit>(
					EditVerbintenisPage.this));
				add(child);
			}

			@Override
			public boolean isVisible()
			{
				return getVerbintenis().isBOVerbintenis() || getVerbintenis().isVOVerbintenis();
			}
		};
		intensiteitRow.setOutputMarkupPlaceholderTag(true);
		veldenLinks.add(intensiteitRow);
	}

	private void addContacturenPerWeek()
	{
		WebMarkupContainer contacturenRow = new WebMarkupContainer("contacturenRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				Opleiding gekozenOpleiding = getOpleiding();
				if (gekozenOpleiding != null)
				{
					Taxonomie taxonomie = gekozenOpleiding.getVerbintenisgebied().getTaxonomie();
					return taxonomie.isEducatie() || gekozenOpleiding.isVavo() || isInburgering()
						|| isStaatsExamen();
				}
				return false;
			}
		};
		TextField<BigDecimal> contacturenPerWeek =
			new TextField<BigDecimal>("contacturenPerWeek", BigDecimal.class);
		contacturenPerWeek.add(new AjaxFormComponentSaveBehaviour());
		contacturenPerWeek.add(new RangeValidator<BigDecimal>(BigDecimal.ZERO, BigDecimal
			.valueOf(40)));
		contacturenPerWeek.setLabel(Model.of("Contacturen per week (klokuren)"));
		contacturenRow.add(contacturenPerWeek);
		veldenLinks.add(contacturenRow);
	}

	private void addLeerprofielVeld()
	{
		WebMarkupContainer leerprofielRow = new WebMarkupContainer("leerprofielRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				Opleiding gekozenOpleiding = getOpleiding();
				if (gekozenOpleiding != null)
				{
					Taxonomie taxonomie = gekozenOpleiding.getVerbintenisgebied().getTaxonomie();
					return taxonomie.isEducatie();
				}
				return false;
			}
		};
		leerprofielRow.add(new EnumCombobox<Leerprofiel>("leerprofiel", Leerprofiel.values())
			.setRequired(false));
		veldenLinks.add(leerprofielRow);
	}

	private void addOrganisatieEenheidLocatieVeld()
	{
		OrganisatieEenheidLocatieRequired required =
			verbintenisIsMuteerbaar() ? OrganisatieEenheidLocatieRequired.OrganisatieEenheid
				: OrganisatieEenheidLocatieRequired.Beide;

		organisatieEenheidLocatieChoice =
			new SecureOrganisatieEenheidLocatieFormChoicePanel<Verbintenis>(
				"organisatieEenheidLocatie", getChangeRecordingModel(), required,
				new PropertyModel<Opleiding>(getDefaultModel(), "opleiding"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onUpdateOrganisatieEenheid(AjaxRequestTarget target,
						OrganisatieEenheid newSelection)
				{
					super.onUpdateOrganisatieEenheid(target, newSelection);
					target.addComponent(teamLabel);
				}

				@Override
				public void onUpdateLocatie(AjaxRequestTarget target, Locatie newSelection)
				{
					target.addComponent(teamLabel);
				}
			};
		veldenLinks.add(organisatieEenheidLocatieChoice);

		teamLabel = new Label("team", new LoadableDetachableModel<String>()
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected String load()
			{
				OrganisatieEenheid orgeenh =
					organisatieEenheidLocatieChoice.getOrganisatieEenheidCombo()
						.getOrganisatieEenheid();
				Locatie loc = organisatieEenheidLocatieChoice.getLocatieCombo().getLocatie();
				if (getOpleiding() != null)
				{
					Team team = getOpleiding().selecteerTeam(orgeenh, loc);
					if (team != null)
						return team.toString();
				}
				return "";
			}
		});

		teamLabel.setOutputMarkupId(true);
		veldenLinks.add(teamLabel);
	}

	private void addToelichtingVeld()
	{
		veldenLinks.add(new TextArea<String>("toelichting"));
	}

	private Bekostigingsperiode createNewBekostigingsperiode()
	{
		Verbintenis verbintenis = getVerbintenis();

		Bekostigingsperiode ret = new Bekostigingsperiode();
		ret.setVerbintenis(verbintenis);

		ret.setBegindatum(verbintenis.getBegindatum());
		ret.setEinddatum(getEinddatumVerbintenis());

		ret.setBekostigd(true);

		return ret;
	}

	private void sorteerBekostigingsperiodesOpBegindatum(List<Bekostigingsperiode> periodes)
	{
		Collections.sort(periodes, new Comparator<Bekostigingsperiode>()
		{
			@Override
			public int compare(Bekostigingsperiode periode1, Bekostigingsperiode periode2)
			{
				return periode1.getBegindatum().compareTo(periode2.getBegindatum());
			}
		});
	}

	private Date getEinddatumVerbintenis()
	{
		return getVerbintenis().getEinddatum();
	}

	private boolean verbintenisIsMuteerbaar()
	{
		if (getVerbintenis() != null && getVerbintenis().getStatus() != null)
			return getVerbintenis().getStatus().isMuteerbaar();
		return true;
	}

	private boolean verbintenisIsBVE()
	{
		return getVerbintenis() != null && getVerbintenis().isBVEVerbintenis();
	}

	private boolean verbintenisIsBRONBVE()
	{
		return !verbintenisIsMuteerbaar() && !verbintenisIsAfgesloten() && verbintenisIsBVE();
	}

	private boolean verbintenisIsAfgesloten()
	{
		if (getVerbintenis() != null && getVerbintenis().getStatus() != null)
			return getVerbintenis().getStatus().isAfgesloten();
		return false;
	}

	private boolean verbintenisIsBronCommuniceerbaar()
	{
		return getVerbintenis() != null && getVerbintenis().isBronCommuniceerbaar();
	}

	@Override
	public Verbintenis getVerbintenis()
	{
		return (Verbintenis) getDefaultModelObject();
	}

	private Bekostigd getBekostigd()
	{
		return getVerbintenis().getBekostigd();
	}

	private List<Bekostigingsperiode> getBekostigingsperiodes()
	{
		return getVerbintenis().getBekostigingsperiodes();
	}

	private Opleiding getOpleiding()
	{
		return getVerbintenis().getOpleiding();
	}

	private Deelnemer getDeelnemer()
	{
		return getVerbintenis().getDeelnemer();
	}

	private String getExterneCodelLabel()
	{
		return getVerbintenis().getExterneCodeLabel();
	}

	private Verbintenis getOudeVerbintenis()
	{
		return oudeVerbintenisModel.getObject();
	}

	/**
	 * De eerste plaatsing bij een nieuwe verbintenis
	 */
	private class PlaatsingModel extends AbstractReadOnlyModel<Plaatsing>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Plaatsing getObject()
		{
			Verbintenis verbintenis = getVerbintenis();
			if (verbintenis != null && nieuw && verbintenis.getPlaatsingen().size() == 1)
				return verbintenis.getPlaatsingen().get(0);

			return null;
		}
	}

	/**
	 * Webmarkupcontainer die zichtbaar is als tegelijk de eerste plaatsing bewerkt wordt
	 */
	private class PlaatsingRow extends WebMarkupContainer
	{
		private static final long serialVersionUID = 1L;

		boolean praktijkonderwijs;

		public PlaatsingRow(String id)
		{
			this(id, false);
		}

		public PlaatsingRow(String id, boolean praktijkonderwijs)
		{
			super(id);
			this.praktijkonderwijs = praktijkonderwijs;
		}

		@Override
		public boolean isVisible()
		{
			boolean visible = plaatsingModel.getObject() != null;
			if (visible && praktijkonderwijs)
			{
				Opleiding huidigeOpleiding = getOpleiding();
				visible =
					huidigeOpleiding != null
						&& huidigeOpleiding.getVerbintenisgebied().getTaxonomie().isVO()
						&& "0090".equals(huidigeOpleiding.getVerbintenisgebied().getExterneCode());
			}

			return visible;
		}
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		oudeVerbintenisModel.detach();
		plaatsingModel.detach();
		relevanteVooropleiding.detach();
	}

	private void updateGeplandeEinddatum(Verbintenis verbintenis)
	{
		Date geplandeEinddatum = verbintenis.berekenGeplandeEinddatum();
		if (geplandeEinddatum != null)
		{
			verbintenis.setGeplandeEinddatum(geplandeEinddatum);
			initialGeplandeDatum = true;
		}
	}

	private void addwaarschuwingVooropleidingenImage()
	{
		waarschuwingVooropleidingen =
			new ContextImage("waarschuwingVooropleidingen", Model.of("assets/img/icons/error.png"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					int getoondAantal = getVooropleidingen().size();
					int totaalAantal = 0;
					if (getDeelnemer().getVooropleidingen() != null)
						totaalAantal += getDeelnemer().getVooropleidingen().size();
					if (getDeelnemer().getVerbintenissen() != null)
						totaalAantal += getDeelnemer().getVerbintenissen().size() - 1;
					// min 1 omdat deze verbintenis sowieso niet getoond moet worden
					return getoondAantal < totaalAantal;
				}
			};
		waarschuwingVooropleidingen
			.add(new SimpleAttributeModifier(
				"title",
				"Niet alle vooropleidingen worden getoond omdat data hiervan voor of na de begindatum van de verbintenis liggen."));
		waarschuwingVooropleidingen
			.add(new SimpleAttributeModifier(
				"alt",
				"Niet alle vooropleidingen worden getoond omdat data hiervan voor of na de begindatum van de verbintenis liggen."));
		waarschuwingVooropleidingen.setOutputMarkupPlaceholderTag(true);
		veldenLinks.add(waarschuwingVooropleidingen);
	}

	private boolean isInitialGeplandeDatum()
	{
		return initialGeplandeDatum;
	}

	private boolean isInburgering()
	{
		return getOpleiding() != null && getOpleiding().isInburgering();
	}

	private boolean isStaatsExamen()
	{
		return getOpleiding() != null
			&& (getOpleiding().getVerbintenisgebied().getTaxonomiecode().startsWith("5.SE"));
	}

	private final class IsInburgeringModel extends AbstractReadOnlyModel<Boolean>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean getObject()
		{
			return isInburgering();
		}
	}

	private final class IsInburgeringContractModel extends AbstractReadOnlyModel<Boolean>
	{
		private static final long serialVersionUID = 1L;

		private boolean contract;

		public IsInburgeringContractModel(boolean contract)
		{
			this.contract = contract;
		}

		@Override
		public Boolean getObject()
		{
			return getVerbintenis().isInburgeringVerbintenis()
				&& (getVerbintenis().isInburgeringContractVerbintenis() == contract);
		}
	}

}