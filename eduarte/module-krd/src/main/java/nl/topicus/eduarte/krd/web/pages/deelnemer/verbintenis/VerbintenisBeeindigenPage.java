package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxConfirmationButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.VerbintenisContract;
import nl.topicus.eduarte.entities.inschrijving.Vervolgonderwijs;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.inschrijving.Vervolgonderwijs.SoortVervolgonderwijs;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.vrijevelden.VerbintenisVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenisStatusovergang;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.krd.web.components.panels.verbintenis.BPVBeeindigenPanel;
import nl.topicus.eduarte.krd.web.components.panels.verbintenis.VerbintenisBeeindigenPanel;
import nl.topicus.eduarte.krd.web.components.panels.verbintenis.VervolgonderwijsPanel;
import nl.topicus.eduarte.krd.web.validators.BronVerbintenisMutatieToegestaanValidator;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.web.components.factory.FinancieelModuleComponentFactory;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.DeelnemerVerbintenisPage;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.checks.ClassSecurityCheck;

@PageInfo(title = "Verbintenis beëindigen", menu = {"Deelnemer > [deelnemer] > Verbintenis > [verbintenis] > Verbintenis beëindigen"})
@InPrincipal(DeelnemerVerbintenissenWrite.class)
@RequiredSecurityCheck(NietOverledenSecurityCheck.class)
public class VerbintenisBeeindigenPage extends AbstractDeelnemerPage implements IEditPage,
		VerbintenisProvider
{
	private Form<Void> form;

	private List<BPVBeeindigenPanel> bpvPanels = new ArrayList<BPVBeeindigenPanel>();

	private boolean kanOngedaanmaken = false;

	private Boolean restitutie = false;

	private IModel<Opleiding> oudeOpleiding;

	public VerbintenisBeeindigenPage(IModel<Verbintenis> verbintenisModel)
	{
		this(verbintenisModel, false);
	}

	public VerbintenisBeeindigenPage(IModel<Verbintenis> verbintenisModel, boolean kanOngedaanmaken)
	{
		this(((verbintenisModel.getObject())).getDeelnemer(), (verbintenisModel.getObject()),
			kanOngedaanmaken);
	}

	public VerbintenisBeeindigenPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		this(deelnemer, verbintenis, false);
	}

	public VerbintenisBeeindigenPage(Deelnemer deelnemer, Verbintenis verbintenis,
			boolean kanOngedaanmaken)
	{
		super(DeelnemerMenuItem.Verbintenis, deelnemer, verbintenis);

		this.kanOngedaanmaken = kanOngedaanmaken;

		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(verbintenis,
			new DefaultModelManager(BPVInschrijving.class, Vervolgonderwijs.class,
				VrijVeldOptieKeuze.class, VerbintenisVrijVeld.class, VerbintenisContract.class,
				Bekostigingsperiode.class, Verbintenis.class)));

		if ((verbintenis.getStatus() == VerbintenisStatus.Beeindigd)
			|| (verbintenis.getStatus() == VerbintenisStatus.Definitief))
			Asserts.assertTrue("Status van verbintenis", true);
		else
			Asserts.assertTrue("Status van verbintenis", false);

		form = new Form<Void>("form");

		createBPVBeeindigenPanel("BPVBeeindigenPanel");
		createVerbintenisBeeindigenPanel("verbintenisBeeindigenPanel");
		createVervolgonderwijsPanel("vervolgonderwijsPanel");
		createVrijVeldVeldenPanel("vrijVelden");
		createRestitutiePanel("restitutiePanel");
		createBlokkadedatumValidator("blokkadedatumValidator");

		add(form);

		createComponents();
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(oudeOpleiding);
		super.onDetach();
	}

	@Override
	public void onBeforeRender()
	{
		super.onBeforeRender();
		Verbintenis verbintenis = getVerbintenis();
		VerbintenisBeeindigenPanel panel =
			(VerbintenisBeeindigenPanel) form.get("verbintenisBeeindigenPanel");
		AutoFieldSet<Verbintenis> fieldset = panel.getEditAutoFieldSet();
		FormComponent< ? > fc =
			(FormComponent< ? >) fieldset.getFieldProperties("einddatum").getComponent();
		if (!hasBeenRendered())
		{
			oudeOpleiding = ModelFactory.getModel(verbintenis.getOpleiding());
			form.add(new BronVerbintenisMutatieToegestaanValidator(fc, verbintenis.getBegindatum(),
				verbintenis.getEinddatum(), verbintenis.getStatus(), oudeOpleiding, verbintenis
					.getIntensiteit(), verbintenis.getBekostigd(), getVerbintenisModel()));
		}
	}

	private void createRestitutiePanel(String id)
	{
		WebMarkupContainer restitutiePanel = null;
		if (!kanOngedaanmaken)
		{
			List<FinancieelModuleComponentFactory> factories =
				EduArteApp.get().getPanelFactories(FinancieelModuleComponentFactory.class);
			for (FinancieelModuleComponentFactory factory : factories)
			{
				restitutiePanel =
					factory.newRestitutiePanel(id, getContextVerbintenisModel(),
						new PropertyModel<Boolean>(this, "restitutie"));
			}
		}

		if (restitutiePanel == null)
			restitutiePanel = new WebMarkupContainer(id);
		form.add(restitutiePanel);
	}

	private void createVerbintenisBeeindigenPanel(String id)
	{
		Panel verbintenisBeeindigenPanel =
			new VerbintenisBeeindigenPanel(id, getVerbintenisModel(), form, bpvPanels);
		form.add(verbintenisBeeindigenPanel);
	}

	private void createVervolgonderwijsPanel(String id)
	{
		if (getVerbintenis().getVervolgonderwijs() == null)
		{
			Vervolgonderwijs vervolgonderwijs = new Vervolgonderwijs();
			vervolgonderwijs.setSoortVervolgonderwijs(SoortVervolgonderwijs.Onbekend);
			getVerbintenis().setVervolgonderwijs(vervolgonderwijs);
		}

		form.add(new VervolgonderwijsPanel(id, getVerbintenisModel()));
	}

	private void createBPVBeeindigenPanel(String id)
	{
		RepeatingView bpvs = new RepeatingView(id);
		for (BPVInschrijving bpvInschrijving : getActieveBPV())
		{
			bpvInschrijving.setStatus(BPVStatus.Beëindigd);
			BPVBeeindigenPanel bpvPanel =
				new BPVBeeindigenPanel(bpvs.newChildId(), ModelFactory.getCompoundModel(
					bpvInschrijving, getManager()), new PropertyModel<Date>(getDefaultModel(),
					"einddatum"));
			bpvPanel.setOutputMarkupId(true);
			bpvs.add(bpvPanel);
			bpvPanels.add(bpvPanel);
		}
		form.add(bpvs);
	}

	private void createBlokkadedatumValidator(String id)
	{
		form.add(new BlokkadedatumVerbintenisValidatingFormComponent(id, this,
			BlokkadedatumValidatorMode.Beeindigen));
	}

	private ModelManager getManager()
	{
		return getVerbintenisModel().getManager();
	}

	@SuppressWarnings("unchecked")
	public IChangeRecordingModel<Verbintenis> getVerbintenisModel()
	{
		return (IChangeRecordingModel<Verbintenis>) getDefaultModel();
	}

	private void createVrijVeldVeldenPanel(String id)
	{
		VrijVeldEntiteitEditPanel<Verbintenis> VVEEPanel =
			new VrijVeldEntiteitEditPanel<Verbintenis>(id, getVerbintenisModel());
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.UITSCHRIJVING);
		VVEEPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		VVEEPanel.getVrijVeldZoekFilter().setFilterOpTaxonomie(true);
		VVEEPanel.getVrijVeldZoekFilter().setOpleiding(
			new PropertyModel<Opleiding>(getDefaultModel(), "opleiding"));

		form.add(VVEEPanel);
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
				processForm();
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				getVerbintenis().refresh();
				return new DeelnemerVerbintenisPage(getVerbintenis());
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return VerbintenisBeeindigenPage.class;
			}
		}));
		panel.addButton(new BeeindigenOngedaanMakenKnop(panel, "Beëindiging ongedaan maken",
			CobraKeyAction.GEEN, ButtonAlignment.LEFT));

	}

	private void processForm()
	{
		Verbintenis verbintenis = getVerbintenis();
		verbintenis.setStatus(VerbintenisStatus.Beeindigd);
		beeindigBPVInschrijvingen();
		beeindigPlaatsingen();

		Vervolgonderwijs vervolgonderwijs = verbintenis.getVervolgonderwijs();
		vervolgonderwijs.saveOrUpdate();

		verbintenis.saveOrUpdate();
		verbintenis.commit();

		Page responsePage = null;
		if (restitutie)
		{
			List<FinancieelModuleComponentFactory> factories =
				EduArteApp.get().getPanelFactories(FinancieelModuleComponentFactory.class);
			for (FinancieelModuleComponentFactory factory : factories)
			{
				responsePage = factory.newRestitutiePage(getContextVerbintenisModel());
			}
		}
		if (responsePage == null)
			responsePage = new DeelnemerVerbintenisPage(getVerbintenis());

		setResponsePage(responsePage);
	}

	private void beeindigBPVInschrijvingen()
	{
		for (BPVInschrijving inschrijving : getVerbintenis().getBpvInschrijvingen())
		{
			inschrijving.saveOrUpdate();
		}
	}

	private void beeindigPlaatsingen()
	{
		for (Plaatsing plaatsing : getVerbintenis().getPlaatsingen())
		{
			if (!plaatsing.getBegindatum().after(getVerbintenis().getEinddatum())
				&& plaatsing.getEinddatumNotNull().after(getVerbintenis().getBegindatum()))
			{
				plaatsing.setEinddatum(getVerbintenis().getEinddatum());
			}
		}
	}

	private List<BPVInschrijving> getActieveBPV()
	{
		List<BPVInschrijving> inschrijvingen = getVerbintenis().getBpvInschrijvingen();

		List<BPVInschrijving> ret = new ArrayList<BPVInschrijving>();

		for (BPVInschrijving inschrijving : inschrijvingen)
		{
			if (inschrijving.getStatus() != BPVStatus.Afgemeld
				&& inschrijving.getEinddatum() == null)
			{
				ret.add(inschrijving);
			}
		}
		return ret;
	}

	@InPrincipal(DeelnemerVerbintenisStatusovergang.class)
	private final class BeeindigenOngedaanMakenKnop extends AbstractAjaxConfirmationButton
	{
		private static final long serialVersionUID = 1L;

		private BeeindigenOngedaanMakenKnop(BottomRowPanel bottomRow, String label,
				CobraKeyAction action, ButtonAlignment alignment)
		{
			super(bottomRow, label,
				"Weet u zeker dat u de beëindiging van de verbintenis ongedaan wilt maken?",
				action, alignment, null);
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(
				BeeindigenOngedaanMakenKnop.class));
		}

		@Override
		protected void onClick(AjaxRequestTarget target)
		{
			Verbintenis verbintenis = getVerbintenis();
			Date begindatum = verbintenis.getBegindatum();
			Date einddatum = verbintenis.getEinddatum();
			Bekostigd bekostigd = verbintenis.getBekostigd();
			VerbintenisStatus status = verbintenis.getStatus();
			Opleiding opleiding = verbintenis.getOpleiding();
			Intensiteit intensiteit = verbintenis.getIntensiteit();

			if (!verbintenis.getVervolgonderwijs().isSaved())
				verbintenis.setVervolgonderwijs(null);

			if (verbintenis.isBVEVerbintenis()
				&& verbintenis.getRelevanteVooropleidingVooropleiding() == null
				&& verbintenis.getDeelnemer().getHoogsteVooropleiding() == null)
			{
				warn("Deelnemer heeft geen vooropleidingen. Deze is verplicht voor deze verbintenis in communicatie met BRON.");
				refreshFeedback(target);
			}
			else
			{
				// hack om een lege relevante vooropleiding uit de conversie alsnog te
				// setten
				// omdat je geen mogelijkheden hebt om de
				if (verbintenis.isBVEVerbintenis()
					&& verbintenis.getRelevanteVooropleidingVooropleiding() == null)
					verbintenis.setRelevanteVooropleidingVooropleiding(verbintenis.getDeelnemer()
						.getHoogsteVooropleiding());

				verbintenis.setStatus(VerbintenisStatus.Definitief);
				verbintenis.setEinddatum(null);
				verbintenis.setRedenUitschrijving(null);
				BronVerbintenisWijzigingToegestaanCheck check =
					new BronVerbintenisWijzigingToegestaanCheck(begindatum, einddatum, status,
						opleiding, intensiteit, bekostigd, verbintenis);

				if (!BlokkadedatumVerbintenisValidator.isWijzigenToegestaan(verbintenis))
				{
					error("Kan de mutatie niet doorvoeren omdat de begindatum van de verbintenis voor de blokkadedatum ligt.");
					verbintenis.refresh();
					refreshFeedback(target);
					return;
				}

				if (!check.isWijzigingToegestaan())
				{
					error("Kan de mutatie niet doorvoeren, omdat dit betrekking heeft op een bron teldatum waarvoor geen mutaties meer gedaan mogen worden.");
					verbintenis.refresh();
					refreshFeedback(target);
					return;
				}

				verbintenis.saveOrUpdate();
				verbintenis.commit();

				DeelnemerVerbintenisPage page = new DeelnemerVerbintenisPage(verbintenis);
				if (!verbintenis.getBpvInschrijvingen().isEmpty())
					page
						.info("De beëindiging van de verbintenis is ongedaan gemaakt. Controleer of er BPV's zijn waarvan de beëindiging ongedaan moet worden gemaakt.");
				setResponsePage(page);
			}
		}

		@Override
		public boolean isVisible()
		{
			// de beeindiging van een VO verbintenis mag je niet ongedaan maken van BRON;
			// je kunt alleen de uitschrijfdatum aanpassen naar een eerdere datum
			return kanOngedaanmaken && !getVerbintenis().isVOVerbintenis();
		}
	}

	@Override
	public Verbintenis getVerbintenis()
	{
		return (Verbintenis) getDefaultModelObject();
	}
}
