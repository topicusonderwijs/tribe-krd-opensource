package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv;

import java.math.BigDecimal;
import java.util.Date;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractConfirmationLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerBPVInschrijvingStatusovergang;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerBPVWrite;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumBPVValidatingFormComponent;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumBPVValidator;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumValidatorMode;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.bpv.DeelnemerBPVPage;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter.SoortRedenUitschrijvingTonen;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.ClassSecurityCheck;

/*
 * @author vandekamp
 */
@PageInfo(title = "BPV beeindigen", menu = {"Deelnemer > [deelnemer] > BPV > Nieuwe BPV",
	"Groep > [groep] > [deelnemer] > BPV > Nieuwe BPV"})
@InPrincipal(DeelnemerBPVWrite.class)
public class BPVBeeindigenPage extends AbstractDeelnemerPage implements
		IModuleEditPage<BPVInschrijving>
{
	private Form<Void> form;

	private AbstractDeelnemerPage returnPage;

	public BPVBeeindigenPage(IModel<BPVInschrijving> bpvInschrijvingModel,
			AbstractDeelnemerPage returnPage)
	{
		super(DeelnemerMenuItem.BPV, returnPage.getContextDeelnemer(), returnPage
			.getContextVerbintenis());
		this.returnPage = returnPage;
		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(bpvInschrijvingModel
			.getObject(), new DefaultModelManager(BPVInschrijving.class)));

		form = new Form<Void>("bpvForm");

		AutoFieldSet<BPVInschrijving> links =
			new AutoFieldSet<BPVInschrijving>("links", getBPVInschrijvingModel());
		links.setPropertyNames("status", "bronStatus", "verbintenis.contextInfoOmschrijving",
			"bpvBedrijf", "contactPersoonBPVBedrijf", "bedrijfsgegeven", "codeLeerbedrijf",
			"bedrijfsgegeven.codeLeerbedrijf", "contractpartner", "contactPersoonContractpartner",
			"praktijkbiedendeOrganisatie", "praktijkopleiderBPVBedrijf", "praktijkbegeleider",
			"opmerkingen");
		links.setSortAccordingToPropertyNames(true);
		links.setRenderMode(RenderMode.DISPLAY);
		form.add(links);

		AutoFieldSet<BPVInschrijving> rechtsBoven =
			new AutoFieldSet<BPVInschrijving>("rechtsBoven", getBPVInschrijvingModel());
		rechtsBoven.setPropertyNames("begindatum", "verwachteEinddatum");
		rechtsBoven.setSortAccordingToPropertyNames(true);
		rechtsBoven.setRenderMode(RenderMode.DISPLAY);
		form.add(rechtsBoven);

		final AutoFieldSet<BPVInschrijving> rechtsMidden =
			new AutoFieldSet<BPVInschrijving>("rechtsMidden", getBPVInschrijvingModel());
		rechtsMidden.setPropertyNames("einddatum", "redenUitschrijving", "toelichtingBeeindiging",
			"gerealiseerdeOmvang");
		rechtsMidden.addFieldModifier(new ConstructorArgModifier("redenUitschrijving",
			SoortRedenUitschrijvingTonen.BPV));
		rechtsMidden.addFieldModifier(new RequiredModifier(true, "einddatum"));
		rechtsMidden.addFieldModifier(new RequiredModifier(true, "redenUitschrijving"));
		rechtsMidden.addModifier("einddatum", new AjaxFormComponentUpdatingBehavior("onblur")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				berekenOmvang();
				target.addComponent(rechtsMidden.findFieldComponent("gerealiseerdeOmvang"));
			}
		});

		rechtsMidden.setSortAccordingToPropertyNames(true);
		rechtsMidden.setRenderMode(RenderMode.EDIT);
		form.add(rechtsMidden);

		form.add(new BlokkadedatumBPVValidatingFormComponent("blokkadedatumValidator",
			getBPVInschrijvingModel(), BlokkadedatumValidatorMode.Beeindigen));

		AutoFieldSet<BPVInschrijving> rechtsOnder =
			new AutoFieldSet<BPVInschrijving>("rechtsOnder", getBPVInschrijvingModel());
		rechtsOnder.setPropertyNames("totaleOmvang", "urenPerWeek", "dagenPerWeek");
		rechtsOnder.setSortAccordingToPropertyNames(true);
		rechtsOnder.setRenderMode(RenderMode.DISPLAY);
		form.add(rechtsOnder);

		add(form);
		createComponents();
	}

	private void berekenOmvang()
	{
		BPVInschrijving bpv = (BPVInschrijving) getDefaultModelObject();
		Date einddatum = bpv.getEinddatum();
		Date beginDatum = bpv.getBegindatum();
		int dagen = TimeUtil.getInstance().getDifferenceInDays(einddatum, beginDatum);
		// +3 ivm afronding
		int weken = (dagen + 3) / 7;

		int gerealiseerdeOmvang = 0;
		BigDecimal urenPerWeek = bpv.getUrenPerWeek();
		Integer dagenPerWeek = bpv.getDagenPerWeek();
		if (urenPerWeek != null || dagenPerWeek != null)
		{
			if (urenPerWeek == null)
				urenPerWeek = BigDecimal.valueOf(dagenPerWeek * 8);

			double urenPerWeekDouble = urenPerWeek == null ? 0 : urenPerWeek.doubleValue();
			// 40 weken actief per jaar, +26 ivm afronding
			gerealiseerdeOmvang = (int) (weken * 40 * urenPerWeekDouble + 26) / 52;
			bpv.setGerealiseerdeOmvang(gerealiseerdeOmvang);
			if (bpv.getGerealiseerdeOmvang() != null && bpv.getGerealiseerdeOmvang() > 5120)
			{
				bpv.setGerealiseerdeOmvang(5120);
			}
		}
		else
			bpv.setGerealiseerdeOmvang(bpv.getTotaleOmvang());

	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		AutoFieldSet< ? > rechtsMidden = (AutoFieldSet< ? >) form.get("rechtsMidden");
		DatumField einddatumVeld = (DatumField) rechtsMidden.findFieldComponent("einddatum");
		form.add(new DatumGroterOfGelijkDatumValidator("Einddatum", einddatumVeld,
			((BPVInschrijving) getDefaultModelObject()).getBegindatum()));
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
				BPVInschrijving bpvInschrijving =
					(BPVInschrijving) BPVBeeindigenPage.this.getDefaultModelObject();
				bpvInschrijving.setStatus(BPVStatus.Beëindigd);
				((IChangeRecordingModel< ? >) BPVBeeindigenPage.this.getDefaultModel())
					.saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(returnPage);
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnPage));

		panel.addButton(new BeeindigenBPVOngedaanMakenKnop(panel, "Beëindiging ongedaan maken",
			CobraKeyAction.GEEN, ButtonAlignment.LEFT));
	}

	private BPVInschrijving getBPVInschrijving()
	{
		return (BPVInschrijving) getDefaultModelObject();
	}

	@SuppressWarnings("unchecked")
	public IModel<BPVInschrijving> getBPVInschrijvingModel()
	{
		return (IModel<BPVInschrijving>) getDefaultModel();
	}

	@InPrincipal(DeelnemerBPVInschrijvingStatusovergang.class)
	private final class BeeindigenBPVOngedaanMakenKnop extends AbstractConfirmationLinkButton
	{
		private static final long serialVersionUID = 1L;

		private BeeindigenBPVOngedaanMakenKnop(BottomRowPanel bottomRow, String label,
				ActionKey action, ButtonAlignment alignment)
		{
			super(bottomRow, label, action, alignment,
				"Weet u zeker dat u de beëindiging van de BPV ongedaan wilt maken?");
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(
				BeeindigenBPVOngedaanMakenKnop.class));
		}

		@Override
		protected void onClick()
		{
			BPVInschrijving inschrijving = getBPVInschrijving();
			inschrijving.setStatus(BPVStatus.Definitief);
			inschrijving.setGerealiseerdeOmvang(null);
			inschrijving.setEinddatum(null);
			inschrijving.setRedenUitschrijving(null);
			inschrijving.setToelichtingBeeindiging(null);

			inschrijving.saveOrUpdate();
			inschrijving.commit();

			setResponsePage(new DeelnemerBPVPage(inschrijving));
		}

		@Override
		public boolean isVisible()
		{
			return getBPVInschrijving().getStatus().equals(BPVStatus.Beëindigd)
				&& BlokkadedatumBPVValidator.isWijzigenToegestaan(getBPVInschrijving());
		}
	}
}