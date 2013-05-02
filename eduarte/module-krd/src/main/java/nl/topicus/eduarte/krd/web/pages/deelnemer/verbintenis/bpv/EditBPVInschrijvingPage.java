package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentValidatingBehavior;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.form.modifier.PostProcessModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.dao.helpers.BrinDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.PraktijkbiedendeOrganisatie;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.vrijevelden.BPVInschrijvingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerBPVWrite;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumBPVValidatingFormComponent;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumValidatorMode;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.buttons.BPVInschrijvingVerwijderenButton;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.buttons.BPVStatusTerugzettenButton;
import nl.topicus.eduarte.krd.web.validators.BPVBegindatumVerwachteEindatumValidator;
import nl.topicus.eduarte.krd.web.validators.BronBPVMutatieToegestaanValidator;
import nl.topicus.eduarte.krd.web.validators.BronBPVValidator;
import nl.topicus.eduarte.providers.BPVInschrijvingProvider;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.factory.BPVModuleComponentFactory;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.quicksearch.bpvbedrijfsgegeven.BPVBedrijfsgegevenSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.externeorganisatie.ExterneOrganisatieSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.externeorganisatie.contactpersoon.ExterneOrganisatieContactPersoonSearchEditor;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.bpv.DeelnemerBPVPage;
import nl.topicus.eduarte.zoekfilters.BPVBedrijfsgegevenZoekFilter;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieContactPersoonZoekFilter;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter.SoortRedenUitschrijvingTonen;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;

/*
 * @author vandekamp
 */
@PageInfo(title = "BPV bewerken", menu = {"Deelnemer > [deelnemer] > BPV > Nieuwe BPV",
	"Groep > [groep] > [deelnemer] > BPV > Nieuwe BPV"})
@InPrincipal(DeelnemerBPVWrite.class)
@RequiredSecurityCheck(NietOverledenSecurityCheck.class)
public class EditBPVInschrijvingPage extends AbstractDeelnemerPage implements
		BPVInschrijvingProvider, IModuleEditPage<BPVInschrijving>
{
	private class BerekenOmvangModifier extends AjaxFormComponentUpdatingBehavior
	{
		private static final long serialVersionUID = 1L;

		private BerekenOmvangModifier()
		{
			super("onblur");
		}

		@Override
		protected void onUpdate(AjaxRequestTarget target)
		{
			if (getBPV().getDagenPerWeek() != null && getBPV().getUrenPerWeek() == null)
			{
				getBPV().setUrenPerWeek(BigDecimal.valueOf(getBPV().getDagenPerWeek() * 8));
				target.addComponent(veldenRechts.findFieldComponent("urenPerWeek"));
			}

			if (getBPV().getVerwachteEinddatum() != null && getBPV().getBegindatum() != null)
			{
				Integer vorigeOmvang = getBPV().getTotaleOmvang();
				berekenOmvang();
				if (vorigeOmvang != getBPV().getTotaleOmvang())
					target.addComponent(veldenRechts.findFieldComponent("totaleOmvang"));
			}
		}
	}

	private AutoFieldSet<BPVInschrijving> veldenLinks;

	private AutoFieldSet<BPVInschrijving> veldenRechts;

	private ExterneOrganisatieZoekFilter bpvZoekFilter;

	private ExterneOrganisatieZoekFilter contractpartnerZoekfilter;

	private IModel<BPVInschrijving> oudeBPVModel;

	private AutoFieldSet<BPVInschrijving> oudeBPVFieldSet;

	private DatumField begindatumVeld;

	private DatumField verwachteEinddatumVeld;

	private boolean statusTeruggezet = false;

	private Form<Void> form;

	private SecurePage returnToPage;

	public static final String EXTRA_STATUSOVERGANGEN = "EXTRA_STATUSOVERGANGEN";

	private IModel<Boolean> isNogNietDefinitiefModel = new AbstractReadOnlyModel<Boolean>()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean getObject()
		{
			return getBPV().getStatus() != null
				&& !getBPV().getStatus().equals(BPVStatus.Definitief)
				// volgens mant is nr 52172 mag de overeenkomst bij status afgedrukt ook
				// niet meer gewijzigd worden
				&& !getBPV().getStatus().equals(BPVStatus.OvereenkomstAfgedrukt);
		}
	};

	public EditBPVInschrijvingPage(BPVInschrijving bpvInschrijving, SecurePage returnToPage)
	{
		this(bpvInschrijving, null, returnToPage);
	}

	public EditBPVInschrijvingPage(BPVInschrijving bpvInschrijving, BPVInschrijving oudeBPV,
			SecurePage returnToPage)
	{
		super(DeelnemerMenuItem.BPV, bpvInschrijving.getDeelnemer(), bpvInschrijving
			.getVerbintenis());
		this.returnToPage = returnToPage;
		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(bpvInschrijving,
			new DefaultModelManager(VrijVeldOptieKeuze.class, BPVInschrijvingVrijVeld.class,
				BPVInschrijving.class)));

		form = new Form<Void>("bpvForm");
		if (oudeBPV != null)
			oudeBPVModel = ModelFactory.getCompoundChangeRecordingModel(oudeBPV, getManager());
		else
			oudeBPVModel = new Model<BPVInschrijving>(null);

		initZoekFilters();

		addAutoFielsSetOudeBPV();

		addAutoFielsSetLinks();
		addAutoFieldSetRechts();
		voegVrijVeldVeldenToeAanForm();

		voegBlokkadedatumValidatorToe();

		add(form);
		createComponents();
	}

	@SuppressWarnings("unchecked")
	private IChangeRecordingModel<BPVInschrijving> getChangeRecordingModel()
	{
		return (IChangeRecordingModel<BPVInschrijving>) getDefaultModel();
	}

	private ModelManager getManager()
	{
		return getChangeRecordingModel().getManager();
	}

	private void initZoekFilters()
	{
		bpvZoekFilter = new ExterneOrganisatieZoekFilter();
		bpvZoekFilter.addOrderByProperty("naam");
		bpvZoekFilter.setBpvBedrijf(true);
		contractpartnerZoekfilter = new ExterneOrganisatieZoekFilter();
		PraktijkbiedendeOrganisatie praktijkbiedendeOrganisatie =
			getChangeRecordingModel().getObject().getPraktijkbiedendeOrganisatie();
		if (praktijkbiedendeOrganisatie != null
			&& praktijkbiedendeOrganisatie.equals(PraktijkbiedendeOrganisatie.BPVBEDRIJF))
			contractpartnerZoekfilter.setBpvBedrijf(true);
	}

	private void addAutoFielsSetOudeBPV()
	{
		WebMarkupContainer oudeBPV = new WebMarkupContainer("oudeBPV");
		oudeBPVFieldSet = new AutoFieldSet<BPVInschrijving>("veldenOudeBPV", oudeBPVModel);
		oudeBPVFieldSet.setRenderMode(RenderMode.EDIT);
		oudeBPVFieldSet.setSortAccordingToPropertyNames(true);
		oudeBPVFieldSet.setPropertyNames("einddatum", "redenUitschrijving");
		oudeBPVFieldSet.addFieldModifier(new RequiredModifier(true, "einddatum"));
		oudeBPVFieldSet.addFieldModifier(new ConstructorArgModifier("redenUitschrijving",
			SoortRedenUitschrijvingTonen.BPV));
		oudeBPV.setVisible(getOudeBPV() != null);
		oudeBPV.setRenderBodyOnly(true);
		oudeBPV.add(oudeBPVFieldSet);
		form.add(oudeBPV);
	}

	private BPVInschrijving getOudeBPV()
	{
		return oudeBPVModel.getObject();
	}

	private void addAutoFielsSetLinks()
	{
		veldenLinks =
			new AutoFieldSet<BPVInschrijving>("veldenLinksBoven", getChangeRecordingModel());
		veldenLinks.setRenderMode(RenderMode.EDIT);
		veldenLinks.setSortAccordingToPropertyNames(true);
		veldenLinks.setPropertyNames("status", "redenUitschrijving", "opnemenInBron",
			"verbintenis.contextInfoOmschrijving", "bpvBedrijf", "bedrijfsgegeven",
			"kenniscentrum", "codeLeerbedrijf", "contactPersoonBPVBedrijf",
			"praktijkopleiderBPVBedrijf", "naamPraktijkopleiderBPVBedrijf", "contractpartner",
			"contactPersoonContractpartner", "praktijkbiedendeOrganisatie", "praktijkbegeleider",
			"opmerkingen", "neemtBetalingsplichtOver");

		Serializable[] vervolgStatussen = getBPV().getStatus().getVervolgNormaal();
		// als de bijbehorende verbintenis (nog) niet BRON communiceerbaar is, dan kan er
		// geen melding worden aangemaakt. De status kan dan niet worden aangepast.
		if (!getBPV().getVerbintenis().getStatus().isBronCommuniceerbaar())
			vervolgStatussen = new Serializable[] {getBPV().getStatus()};

		veldenLinks.addFieldModifier(new ConstructorArgModifier("status", vervolgStatussen));
		veldenLinks.addFieldModifier(new EnableModifier(new Model<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return !getBPV().isSaved() || getBPV().getStatus() == BPVStatus.Voorlopig;
			}
		}, "opnemenInBron"));
		veldenLinks.addFieldModifier(new EduArteAjaxRefreshModifier("opnemenInBron", veldenLinks));

		veldenLinks.addFieldModifier(new VisibilityModifier(new Model<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getBPV().getStatus() == BPVStatus.Beëindigd;
			}
		}, "redenUitschrijving"));

		veldenLinks.addFieldModifier(new VisibilityModifier(new PropertyModel<Boolean>(
			getDefaultModel(), "opnemenInBron"), "bedrijfsgegeven", "kenniscentrum",
			"codeLeerbedrijf"));
		veldenLinks.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return !getBPV().isOpnemenInBron();
			}
		}, "bpvBedrijf"));

		veldenLinks
			.addFieldModifier(new EnableModifier(isNogNietDefinitiefModel, "contractpartner"));

		veldenLinks.addFieldModifier(new ConstructorArgModifier("redenUitschrijving",
			SoortRedenUitschrijvingTonen.BPV));
		veldenLinks.addFieldModifier(new EnableModifier(isNogNietDefinitiefModel, "bpvBedrijf",
			"bedrijfsgegeven", "praktijkbiedendeOrganisatie"));
		veldenLinks.addModifier("praktijkbiedendeOrganisatie",
			getPraktijkbiedendeUpdatingBehavior());
		veldenLinks.addModifier("status", getStatusUpdatingBehavior());
		veldenLinks.addFieldModifier(new ConstructorArgModifier("bedrijfsgegeven",
			getKenniscentrumFilter()));

		veldenLinks.addFieldModifier(new PostProcessModifier("bedrijfsgegeven")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
					FieldProperties<T, ? , ? > fieldProperties)
			{
				BPVBedrijfsgegevenSearchEditor editor = (BPVBedrijfsgegevenSearchEditor) field;
				editor.addListener(getBPVBedrijfChangeListener());
			}
		});

		veldenLinks.addFieldModifier(new PostProcessModifier("bpvBedrijf")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
					FieldProperties<T, ? , ? > fieldProperties)
			{
				ExterneOrganisatieSearchEditor editor = (ExterneOrganisatieSearchEditor) field;
				editor.addListener(getBPVBedrijfChangeListener());
			}
		});
		veldenLinks.addFieldModifier(new LabelModifier("bpvBedrijf", "BPV-bedrijf"));
		ExterneOrganisatieContactPersoonZoekFilter contactPersZF =
			new ExterneOrganisatieContactPersoonZoekFilter(new PropertyModel<ExterneOrganisatie>(
				getDefaultModel(), "bpvBedrijf"));
		contactPersZF.setContactpersoonBPV(true);
		veldenLinks.addFieldModifier(new ConstructorArgModifier("contactPersoonBPVBedrijf",
			contactPersZF));
		ExterneOrganisatieContactPersoonZoekFilter praktijkOplZF =
			new ExterneOrganisatieContactPersoonZoekFilter(new PropertyModel<ExterneOrganisatie>(
				getDefaultModel(), "bpvBedrijf"));
		praktijkOplZF.setPraktijkopleiderBPV(true);
		veldenLinks.addFieldModifier(new ConstructorArgModifier("praktijkopleiderBPVBedrijf",
			praktijkOplZF));
		veldenLinks.addFieldModifier(new ConstructorArgModifier("contractpartner",
			contractpartnerZoekfilter));
		veldenLinks.addFieldModifier(new ConstructorArgModifier("contactPersoonContractpartner",
			new PropertyModel<ExterneOrganisatie>(getDefaultModel(), "contractpartner")));
		veldenLinks.addFieldModifier(new ConstructorArgModifier("bpvBedrijf", bpvZoekFilter));

		// Alleen BPV's voor BO verbintenissen zijn op te nemen in BRON
		veldenLinks.addFieldModifier(new VisibilityModifier(new Model<Boolean>(getBPV()
			.getVerbintenis().isBOVerbintenis()), "opnemenInBron"));
		veldenLinks.setOutputMarkupId(true);
		form.add(veldenLinks);

	}

	private BPVBedrijfsgegevenZoekFilter getKenniscentrumFilter()
	{
		BPVBedrijfsgegevenZoekFilter filter = new BPVBedrijfsgegevenZoekFilter();
		filter.addOrderByProperty("externeOrganisatie.naam");
		BrinDataAccessHelper helper = DataAccessRegistry.getHelper(BrinDataAccessHelper.class);
		Opleiding opleiding = getBPV().getVerbintenis().getOpleiding();
		if (opleiding != null)
		{
			String brinKenniscentrum = opleiding.getVerbintenisgebied().getBrinKenniscentrum();
			if (brinKenniscentrum != null)
				filter.setKenniscentrum(helper.get(brinKenniscentrum));
		}
		return filter;
	}

	private AjaxFormComponentUpdatingBehavior getStatusUpdatingBehavior()
	{
		return new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				BPVInschrijving bpv = getBPV();
				if (bpv.getStatus().equals(BPVStatus.Volledig) && bpv.getAfsluitdatum() == null)
				{
					bpv.setAfsluitdatum(TimeUtil.getInstance().currentDate());
				}

				updateRequiredComponents(target);
			}
		};
	}

	private void updateRequiredComponents(AjaxRequestTarget target)
	{
		BPVInschrijving bpv = getChangeRecordingModel().getObject();
		boolean required = bpv.getStatus().isBronCommuniceerbaar();

		List<FormComponent< ? >> componentsToUpdate = new ArrayList<FormComponent< ? >>();
		componentsToUpdate.add((FormComponent< ? >) veldenRechts
			.findFieldComponent("verwachteEinddatum"));
		componentsToUpdate
			.add((FormComponent< ? >) veldenRechts.findFieldComponent("totaleOmvang"));
		componentsToUpdate
			.add((FormComponent< ? >) veldenRechts.findFieldComponent("afsluitdatum"));
		for (FormComponent< ? > fc : componentsToUpdate)
		{
			fc.setRequired(required);
			if (target != null)
				target.addComponent(fc);
		}
	}

	private AjaxFormChoiceComponentUpdatingBehavior getPraktijkbiedendeUpdatingBehavior()
	{
		return new AjaxFormChoiceComponentUpdatingBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				BPVInschrijving bpv = getChangeRecordingModel().getObject();
				if (bpv.getPraktijkbiedendeOrganisatie().equals(
					PraktijkbiedendeOrganisatie.BPVBEDRIJF))
				{
					contractpartnerZoekfilter.setBpvBedrijf(true);
					if (bpv.getContractpartner() != null
						&& !bpv.getContractpartner().isBpvBedrijf())
						bpv.setContractpartner(null);
				}
				else
					contractpartnerZoekfilter.setBpvBedrijf(null);
				target.addComponent(veldenLinks.findFieldComponent("contractpartner"));
			}
		};
	}

	private void addAutoFieldSetRechts()
	{
		veldenRechts = new AutoFieldSet<BPVInschrijving>("veldenRechts", getChangeRecordingModel());
		veldenRechts.setRenderMode(RenderMode.EDIT);
		veldenRechts.setSortAccordingToPropertyNames(true);
		veldenRechts.setPropertyNames("verbintenis.geldigVanTotBeschrijving", "begindatum",
			"verwachteEinddatum", "toelichtingBeeindiging", "urenPerWeek", "dagenPerWeek",
			"totaleOmvang", "afsluitdatum", "werkdagen", "locatiePOK");
		veldenRechts.addFieldModifier(new LabelModifier("verbintenis.geldigVanTotBeschrijving",
			"Geldigheid verbintenis"));

		veldenRechts.addModifier("dagenPerWeek",
			new AjaxFormComponentValidatingBehavior("onchange"));
		veldenRechts.addModifier("dagenPerWeek", new RangeValidator<Integer>(0, 7));
		veldenRechts
			.addModifier("urenPerWeek", new AjaxFormComponentValidatingBehavior("onchange"));
		veldenRechts.addModifier("urenPerWeek", new RangeValidator<BigDecimal>(new BigDecimal(0),
			new BigDecimal(168)));

		veldenRechts.addModifier("begindatum", new BerekenOmvangModifier());
		veldenRechts.addModifier("verwachteEinddatum", new BerekenOmvangModifier());
		veldenRechts.addModifier("urenPerWeek", new BerekenOmvangModifier());
		veldenRechts.addModifier("dagenPerWeek", new BerekenOmvangModifier());
		veldenRechts.addModifier("totaleOmvang", new BerekenOmvangModifier());

		veldenRechts.addFieldModifier(new EnableModifier(isNogNietDefinitiefModel, "begindatum",
			"verwachteEinddatum", "afsluitdatum"));

		form.add(veldenRechts);
	}

	private void voegVrijVeldVeldenToeAanForm()
	{
		VrijVeldEntiteitEditPanel<BPVInschrijving> VVEEPanel =
			new VrijVeldEntiteitEditPanel<BPVInschrijving>("vrijVelden", getChangeRecordingModel());
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.BPV_INSCHRIJVING);
		VVEEPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		form.add(VVEEPanel);
	}

	private void voegBlokkadedatumValidatorToe()
	{
		BlokkadedatumValidatorMode mode =
			getBPV().isSaved() ? BlokkadedatumValidatorMode.Bewerken
				: BlokkadedatumValidatorMode.Aanmaken;

		form.add(new BlokkadedatumBPVValidatingFormComponent("blokkadedatumValidator",
			getBPVModel(), getBPV().getBegindatum(), mode));
	}

	private void berekenOmvang()
	{
		Date verwachteEindDatum = getBPV().getVerwachteEinddatum();
		Date beginDatum = getBPV().getBegindatum();
		int dagen = TimeUtil.getInstance().getDifferenceInDays(verwachteEindDatum, beginDatum);
		// +3 ivm afronding
		int weken = (dagen + 3) / 7;

		int totaleOmvang = 0;
		if (getBPV().getUrenPerWeek() != null)
		{
			BigDecimal urenPerWeek = getBPV().getUrenPerWeek();
			double urenPerWeekDouble = urenPerWeek == null ? 0 : urenPerWeek.doubleValue();
			// 40 weken actief per jaar, +26 ivm afronding
			totaleOmvang = (int) (weken * 40 * urenPerWeekDouble + 26) / 52;
			getBPV().setTotaleOmvang(totaleOmvang);
		}

		if (getBPV().getTotaleOmvang() != null && getBPV().getTotaleOmvang() > 5120)
		{
			getBPV().setTotaleOmvang(5120);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		updateRequiredComponents(null);
		ExterneOrganisatieSearchEditor contractpartner =
			(ExterneOrganisatieSearchEditor) veldenLinks.findFieldComponent("contractpartner");
		contractpartner.addListener(new ISelectListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				target
					.addComponent(veldenLinks.findFieldComponent("contactPersoonContractpartner"));
			}
		});

		begindatumVeld = (DatumField) veldenRechts.findFieldComponent("begindatum");
		verwachteEinddatumVeld = (DatumField) veldenRechts.findFieldComponent("verwachteEinddatum");

		form.add(new BegindatumVoorEinddatumValidator(begindatumVeld, verwachteEinddatumVeld));
		form.add(new BPVBegindatumVerwachteEindatumValidator(this, begindatumVeld,
			verwachteEinddatumVeld));

		EnumCombobox<BPVStatus> status =
			(EnumCombobox<BPVStatus>) veldenLinks.findFieldComponent("status");
		status.add(new BronBPVValidator<BPVStatus>(this));

		DatumField afsluitDatumField = (DatumField) veldenRechts.findFieldComponent("afsluitdatum");
		BPVInschrijving bpv = getBPV();
		Long oudeBedrijfsgegevenId = null;
		if (bpv.getBedrijfsgegeven() != null)
			oudeBedrijfsgegevenId = bpv.getBedrijfsgegeven().getId();
		form.add(new BronBPVMutatieToegestaanValidator(afsluitDatumField, bpv.getBegindatum(), bpv
			.getAfsluitdatum(), oudeBedrijfsgegevenId, bpv.getStatus(), getBPVModel()));

	}

	private ISelectListener getBPVBedrijfChangeListener()
	{
		return new ISelectListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				ExterneOrganisatieContactPersoonSearchEditor editorContactPers =
					(ExterneOrganisatieContactPersoonSearchEditor) veldenLinks
						.findFieldComponent("contactPersoonBPVBedrijf");
				editorContactPers.updateQuickSearchField(target);
				ExterneOrganisatieContactPersoonSearchEditor editorPraktijkOpl =
					(ExterneOrganisatieContactPersoonSearchEditor) veldenLinks
						.findFieldComponent("praktijkopleiderBPVBedrijf");
				editorPraktijkOpl.updateQuickSearchField(target);
				target.addComponent(veldenLinks.findFieldComponent("bpvBedrijf"));
				target.addComponent(veldenLinks.findFieldComponent("bedrijfsgegeven"));
				target.addComponent(veldenLinks.findFieldComponent("codeLeerbedrijf"));
				target.addComponent(veldenLinks.findFieldComponent("kenniscentrum"));
			}
		};
	}

	@Override
	public void onDetach()
	{
		ComponentUtil.detachQuietly(contractpartnerZoekfilter);
		ComponentUtil.detachQuietly(bpvZoekFilter);
		super.onDetach();
	}

	public BPVInschrijving getBPV()
	{
		return (BPVInschrijving) getDefaultModelObject();
	}

	@SuppressWarnings("unchecked")
	public IModel<BPVInschrijving> getBPVModel()
	{
		return (IModel<BPVInschrijving>) getDefaultModel();
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
				// Deze validatie moet hier maar, er worden namelijk 2 verschillende
				// velden
				// gebruikt voor het selectern van een BPVbedrijf
				BPVInschrijving inschrijving = getBPV();
				ExterneOrganisatie bedrijf = inschrijving.getBpvBedrijf();
				if (!bedrijf.isActief(inschrijving.getBegindatum()))
				{
					error("Het BPV-bedrijf is niet actief op de begindatum van de BPV");
					return;
				}
				getChangeRecordingModel().saveObject();
				inschrijving.getVerbintenis().getBpvInschrijvingen().add(inschrijving);
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(new DeelnemerBPVPage(getChangeRecordingModel().getObject()
					.getVerbintenis()));
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnToPage));
		voegStatusTerugzettenKnopToe(panel);
		voegVerwijderenKnopToe(panel);
		List<BPVModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(BPVModuleComponentFactory.class);
		for (BPVModuleComponentFactory factory : factories)
		{
			factory.createControleerAccreditatieButton(panel, this);
		}
	}

	private void voegStatusTerugzettenKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new BPVStatusTerugzettenButton(panel, this)
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onClick()
			{
				statusTeruggezet = true;
				getBPV().setStatus(BPVStatus.Voorlopig);
				getBPV().setEinddatum(null);
				EnumCombobox<BPVStatus> statusField =
					(EnumCombobox<BPVStatus>) veldenLinks.findFieldComponent("status");
				statusField.setChoices(Arrays.asList(BPVStatus.Voorlopig.getVervolgNormaal()));
			}
		});
	}

	private void voegVerwijderenKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new BPVInschrijvingVerwijderenButton(panel, this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				Verbintenis verbintenis = getBPV().getVerbintenis();
				verbintenis.getBpvInschrijvingen().remove(getBPV());
				getChangeRecordingModel().deleteObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(new DeelnemerBPVPage(verbintenis));
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && !statusTeruggezet && !getBPV().isInGebruik();
			}

		});
	}
}