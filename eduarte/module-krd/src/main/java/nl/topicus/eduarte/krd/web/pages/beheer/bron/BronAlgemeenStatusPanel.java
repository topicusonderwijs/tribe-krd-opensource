package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.security.TargetBasedSecureBookmarkablePageLink;
import nl.topicus.eduarte.entities.BronMeldingOnderdeel;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.krd.dao.helpers.BronAanleverpuntDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronExamenverzamenlingDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.ExamendeelnameDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtWrite;
import nl.topicus.eduarte.krd.principals.deelnemer.bron.DeelnemerBronCfiTerugmeldingInlezen;
import nl.topicus.eduarte.krd.principals.deelnemer.bron.DeelnemerBronFotoInlezen;
import nl.topicus.eduarte.krd.web.components.modalwindow.bron.BronTerugkoppelbestandInlezenModalWindow;
import nl.topicus.eduarte.krd.web.components.modalwindow.bron.SelecteerOnderwijssoortModalWindow;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.cfi.BronCfiTerugmeldingInlezenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.foto.BronFotobestandInlezenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten.BronExamenverzamelingenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten.BronMeldingenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten.BronSignalenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.terugkoppeling.BronTerugkoppelbestandInlezenPage;
import nl.topicus.eduarte.krd.zoekfilters.BronExamenverzamelingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronSignaalZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter.TypeMelding;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

public class BronAlgemeenStatusPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private SelecteerOnderwijssoortModalWindow modalWindowRegulier;

	private SelecteerOnderwijssoortModalWindow modalWindowAccountant;

	private BronTerugkoppelbestandInlezenModalWindow modalWindowTerugkoppelingInlezen;

	private BronMeldingZoekFilter filter;

	private boolean heeftAanleverpunten = true;

	private RepeatingView statussen;

	private IModel<BronSchooljaarStatus> schooljaarStatusModel;

	public BronAlgemeenStatusPanel(String id, IModel<BronSchooljaarStatus> schooljaarStatusModel)
	{
		super(id);
		this.filter = new BronMeldingZoekFilter(schooljaarStatusModel);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		this.schooljaarStatusModel = schooljaarStatusModel;
		statussen = new RepeatingView("statussen");
		add(statussen);
	}

	private void addRows()
	{
		filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		filter.setTypeMelding(TypeMelding.Regulier);
		filter.setGeblokkeerd(Boolean.FALSE);
		addWachrijRow("Reguliere meldingen in de wachtrij", getBatchAanmakenLink());

		filter.setGeblokkeerd(Boolean.TRUE);
		addRow("Geblokkeerde meldingen", getGeblokkeerdeMeldingenTonenLink());

		filter.setGeblokkeerd(Boolean.FALSE);
		filter.setTypeMelding(TypeMelding.Accountant);
		addRow("Accountantsmeldingen in de wachtrij", getAccountantsBatchAanmakenLink());

		addExamendeelnemersRow("Examendeeln. met niet-verzonden result.",
			getExamenverzamelingMakenLink());
		addExamenverzamelingenRow("Examenverzamelingen gereed", getExamenBatchAanmakeLink());

		filter.setTypeMelding(null);
		filter.setMeldingStatus(BronMeldingStatus.IN_BEHANDELING);
		addRow("Meldingen in behandeling bij BRON", getTerugkoppelingInlezenLink());

		addSignalenRow("Ongeaccordeerde signalen", getSignalenTonenLink());

		addOnlyLinkRow(getFotoInlezenLink());
		addOnlyLinkRow(getCFILink());
	}

	private void addWachrijRow(String omschrijving, AbstractLink link)
	{
		BronDataAccessHelper helper = DataAccessRegistry.getHelper(BronDataAccessHelper.class);
		filter.setBronOnderwijssoort(BronOnderwijssoort.BEROEPSONDERWIJS);
		String aantalBO = String.valueOf(helper.getAantalMeldingen(filter));
		filter.setBronOnderwijssoort(BronOnderwijssoort.EDUCATIE);
		String aantalED = String.valueOf(helper.getAantalMeldingen(filter));
		filter.setBronMeldingOnderdeelNot(BronMeldingOnderdeel.Examen);
		filter.setBronOnderwijssoort(BronOnderwijssoort.VAVO);
		String aantalVAVO = String.valueOf(helper.getAantalMeldingen(filter));
		filter.setBronMeldingOnderdeel(BronMeldingOnderdeel.VOInschrijving);
		filter.setBronOnderwijssoort(BronOnderwijssoort.VOORTGEZETONDERWIJS);
		String aantalVO = String.valueOf(helper.getAantalMeldingen(filter));
		filter.setBronMeldingOnderdeelNot(null);
		filter.setBronMeldingOnderdeel(null);
		addRow(omschrijving, aantalBO, aantalED, aantalVAVO, aantalVO, link);
	}

	private void addExamendeelnemersRow(String omschrijving, AbstractLink link)
	{
		ExamendeelnameDataAccessHelper deelnameHelper =
			DataAccessRegistry.getHelper(ExamendeelnameDataAccessHelper.class);
		ExamendeelnameZoekFilter exFilter = new ExamendeelnameZoekFilter();
		exFilter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		exFilter.setSchooljaar(filter.getSchooljaar());
		exFilter.setAlleenGewijzigde(true);
		exFilter.setBronOnderwijssoort(BronOnderwijssoort.BEROEPSONDERWIJS);
		String aantalBO =
			String.valueOf(deelnameHelper.getAantalExamendeelnamesInWachtrij(exFilter));
		exFilter.setBronOnderwijssoort(BronOnderwijssoort.EDUCATIE);
		String aantalED =
			String.valueOf(deelnameHelper.getAantalExamendeelnamesInWachtrij(exFilter));
		exFilter.setBronOnderwijssoort(BronOnderwijssoort.VAVO);
		String aantalVAVO =
			String.valueOf(deelnameHelper.getAantalExamendeelnamesInWachtrij(exFilter));
		exFilter.setBronOnderwijssoort(BronOnderwijssoort.VOORTGEZETONDERWIJS);
		String aantalVO =
			String.valueOf(deelnameHelper.getAantalExamendeelnamesInWachtrij(exFilter));
		addRow(omschrijving, aantalBO, aantalED, aantalVAVO, aantalVO, link);
	}

	private void addExamenverzamelingenRow(String omschrijving, AbstractLink link)
	{
		BronExamenverzamenlingDataAccessHelper verzamelingHelper =
			DataAccessRegistry.getHelper(BronExamenverzamenlingDataAccessHelper.class);
		BronExamenverzamelingZoekFilter examenVerzFilter =
			new BronExamenverzamelingZoekFilter(schooljaarStatusModel);
		examenVerzFilter.setBronOnderwijssoort(BronOnderwijssoort.BEROEPSONDERWIJS);
		String aantalBO = String.valueOf(verzamelingHelper.getAantalExVerzGereed(examenVerzFilter));
		examenVerzFilter.setBronOnderwijssoort(BronOnderwijssoort.EDUCATIE);
		String aantalED = String.valueOf(verzamelingHelper.getAantalExVerzGereed(examenVerzFilter));
		examenVerzFilter.setBronOnderwijssoort(BronOnderwijssoort.VAVO);
		String aantalVAVO =
			String.valueOf(verzamelingHelper.getAantalExVerzGereed(examenVerzFilter));
		examenVerzFilter.setBronOnderwijssoort(BronOnderwijssoort.VOORTGEZETONDERWIJS);
		String aantalVO = String.valueOf(verzamelingHelper.getAantalExVerzGereed(examenVerzFilter));
		addRow(omschrijving, aantalBO, aantalED, aantalVAVO, aantalVO, link);
	}

	private void addSignalenRow(String omschrijving, AbstractLink link)
	{
		BronDataAccessHelper bronHelper = DataAccessRegistry.getHelper(BronDataAccessHelper.class);
		BronAanleverpunt aanleverpunt = filter.getAanleverpunt();
		Schooljaar schooljaar = filter.getSchooljaar();
		filter.setBronOnderwijssoort(BronOnderwijssoort.BEROEPSONDERWIJS);
		String aantalBO =
			String.valueOf(bronHelper.getAantalOngeaccordeerdeSignalen(
				BronOnderwijssoort.BEROEPSONDERWIJS, aanleverpunt, schooljaar));
		String aantalED =
			String.valueOf(bronHelper.getAantalOngeaccordeerdeSignalen(BronOnderwijssoort.EDUCATIE,
				aanleverpunt, schooljaar));
		String aantalVAVO =
			String.valueOf(bronHelper.getAantalOngeaccordeerdeSignalen(BronOnderwijssoort.VAVO,
				aanleverpunt, schooljaar));
		String aantalVO =
			String.valueOf(bronHelper.getAantalOngeaccordeerdeSignalen(
				BronOnderwijssoort.VOORTGEZETONDERWIJS, aanleverpunt, schooljaar));
		addRow(omschrijving, aantalBO, aantalED, aantalVAVO, aantalVO, link);
	}

	private void addOnlyLinkRow(AbstractLink link)
	{
		addRow("", "", "", "", "", link);
	}

	private void addRow(String omschrijving, AbstractLink link)
	{
		BronDataAccessHelper helper = DataAccessRegistry.getHelper(BronDataAccessHelper.class);
		filter.setBronOnderwijssoort(BronOnderwijssoort.BEROEPSONDERWIJS);
		String aantalBO = String.valueOf(helper.getAantalMeldingen(filter));
		filter.setBronOnderwijssoort(BronOnderwijssoort.EDUCATIE);
		String aantalED = String.valueOf(helper.getAantalMeldingen(filter));
		filter.setBronOnderwijssoort(BronOnderwijssoort.VAVO);
		String aantalVAVO = String.valueOf(helper.getAantalMeldingen(filter));
		filter.setBronOnderwijssoort(BronOnderwijssoort.VOORTGEZETONDERWIJS);
		String aantalVO = String.valueOf(helper.getAantalMeldingen(filter));
		addRow(omschrijving, aantalBO, aantalED, aantalVAVO, aantalVO, link);
	}

	private void addRow(String omschrijving, String aantalBO, String aantalED, String aantalVAVO,
			String aantalVO, AbstractLink link)
	{
		WebMarkupContainer row = new WebMarkupContainer(statussen.newChildId());
		row.add(new Label("omschrijving", omschrijving));
		row.add(new Label("aantalBO", aantalBO));
		row.add(new Label("aantalED", aantalED));
		row.add(new Label("aantalVAVO", aantalVAVO));
		row.add(new Label("aantalVO", aantalVO));
		row.add(link);
		statussen.add(row);
	}

	@Override
	protected void onBeforeRender()
	{
		if (!hasBeenRendered())
		{
			addRows();
			addModalWindowRegulier();
			addModalWindowAccountant();
			addModalWindowTerugkoppeling();
		}
		BronAanleverpuntDataAccessHelper helper =
			DataAccessRegistry.getHelper(BronAanleverpuntDataAccessHelper.class);
		heeftAanleverpunten = helper.existsBronAanleverpunten();

		super.onBeforeRender();
	}

	private void addModalWindowRegulier()
	{
		modalWindowRegulier =
			new SelecteerOnderwijssoortModalWindow("modalWindowRegulier",
				new Model<BronOnderwijssoort>(), filter, TypeMelding.Regulier);
		modalWindowRegulier.setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if (modalWindowRegulier.getDefaultModelObject() != null)
				{
					BronOnderwijssoort onderwijssoort =
						(BronOnderwijssoort) modalWindowRegulier.getDefaultModelObject();
					filter.setTypeMelding(TypeMelding.Regulier);
					filter.setBronOnderwijssoort(onderwijssoort);
					BronBatchModel model = new BronBatchModel(filter);
					if (!StringUtil.isEmpty(model.getError()))
					{
						error(onderwijssoort + ": " + model.getError());
						((BronAlgemeenPage) getPage()).refreshFeedback(target);
					}
					else
						setResponsePage(new BronBatchesAanmakenPage(model));
				}
			}
		});
		add(modalWindowRegulier);
	}

	private void addModalWindowAccountant()
	{
		modalWindowAccountant =
			new SelecteerOnderwijssoortModalWindow("modalWindowAccountant",
				new Model<BronOnderwijssoort>(), filter, TypeMelding.Accountant);
		modalWindowAccountant.setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if (modalWindowAccountant.getDefaultModelObject() != null)
				{
					BronOnderwijssoort onderwijssoort =
						(BronOnderwijssoort) modalWindowAccountant.getDefaultModelObject();
					filter.setTypeMelding(TypeMelding.Accountant);
					filter.setBronOnderwijssoort(onderwijssoort);
					BronBatchModel model = new BronBatchModel(filter);
					if (!StringUtil.isEmpty(model.getError()))
					{
						error(onderwijssoort + ": " + model.getError());
						((BronAlgemeenPage) getPage()).refreshFeedback(target);
					}
					else
						setResponsePage(new BronBatchesAanmakenPage(model));
				}
			}
		});
		add(modalWindowAccountant);
	}

	private void addModalWindowTerugkoppeling()
	{
		modalWindowTerugkoppelingInlezen =
			new BronTerugkoppelbestandInlezenModalWindow("modalWindowTerugkoppeling");
		modalWindowTerugkoppelingInlezen.setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
			}
		});
		add(modalWindowTerugkoppelingInlezen);
	}

	protected void maakBatchAan(AjaxRequestTarget target)
	{
		modalWindowRegulier.show(target);
	}

	protected void maakAccountantsbatchAan(AjaxRequestTarget target)
	{
		modalWindowAccountant.show(target);
	}

	protected void maakExamenverzamelingAan()
	{
		setResponsePage(new BronExamenverzamelingMakenPage(new BronExamenverzamelingZoekFilter(
			schooljaarStatusModel)));
	}

	protected void maakExamenbatchAan()
	{
		setResponsePage(new BronExamenverzamelingenPage(new BronExamenverzamelingZoekFilter(
			schooljaarStatusModel)));
	}

	protected void leesTerugkoppelingIn(AjaxRequestTarget target)
	{
		modalWindowTerugkoppelingInlezen.show(target);
	}

	protected void toonGeblokkeerdeMeldingen()
	{
		BronMeldingZoekFilter bronMeldingZoekFilter =
			new BronMeldingZoekFilter(schooljaarStatusModel);
		bronMeldingZoekFilter.setGeblokkeerd(true);
		bronMeldingZoekFilter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);

		setResponsePage(new BronMeldingenPage(bronMeldingZoekFilter));
	}

	protected void toonSignalen()
	{
		BronSignaalZoekFilter fltr = new BronSignaalZoekFilter(schooljaarStatusModel);
		fltr.setGeaccordeerd(Boolean.FALSE);
		setResponsePage(new BronSignalenPage(fltr));
	}

	protected void leesBronFotoIn()
	{
		setResponsePage(BronFotobestandInlezenPage.class);
	}

	protected void leesCfiTerugmeldingsoverzichtIn()
	{
		setResponsePage(BronCfiTerugmeldingInlezenPage.class);
	}

	private AbstractLink getBatchAanmakenLink()
	{
		AjaxLink<Void> link = new AjaxLink<Void>("link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return heeftAanleverpunten;
			}

			@Override
			public boolean isVisible()
			{
				return new DataSecurityCheck(BronOverzichtWrite.BRON_WRITE)
					.isActionAuthorized(Enable.class);
			}

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				maakBatchAan(target);
			}
		};
		link.add(new Label("linkLabel", "Batch aanmaken"));
		return link;
	}

	private AbstractLink getAccountantsBatchAanmakenLink()
	{
		AjaxLink<Void> link = new AjaxLink<Void>("link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return heeftAanleverpunten;
			}

			@Override
			public boolean isVisible()
			{
				return new DataSecurityCheck(BronOverzichtWrite.BRON_WRITE)
					.isActionAuthorized(Enable.class);
			}

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				maakAccountantsbatchAan(target);
			}
		};
		link.add(new Label("linkLabel", "Accountantsbatch aanmaken"));
		return link;
	}

	private AbstractLink getExamenverzamelingMakenLink()
	{
		Link<Void> link = new Link<Void>("link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return heeftAanleverpunten;
			}

			@Override
			public boolean isVisible()
			{
				return new DataSecurityCheck(BronOverzichtWrite.BRON_WRITE)
					.isActionAuthorized(Enable.class);
			}

			@Override
			public void onClick()
			{
				maakExamenverzamelingAan();
			}
		};
		link.add(new Label("linkLabel", "Verzameling maken"));
		return link;
	}

	public AbstractLink getExamenBatchAanmakeLink()
	{
		Link<Void> link = new Link<Void>("link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return heeftAanleverpunten;
			}

			@Override
			public void onClick()
			{
				maakExamenbatchAan();
			}

		};
		link.add(new Label("linkLabel", "Examenbatch aanmaken"));
		return link;
	}

	private AbstractLink getTerugkoppelingInlezenLink()
	{
		TargetBasedSecureBookmarkablePageLink<Void> link =
			new TargetBasedSecureBookmarkablePageLink<Void>("link",
				BronTerugkoppelbestandInlezenPage.class)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled()
				{
					return heeftAanleverpunten;
				}
			};
		link.add(new Label("linkLabel", "Terugkoppeling inlezen"));
		return link;
	}

	private AbstractLink getSignalenTonenLink()
	{
		Link<Void> link = new Link<Void>("link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				toonSignalen();
			}

			@Override
			public boolean isVisible()
			{
				return new DataSecurityCheck(BronOverzichtWrite.BRON_WRITE)
					.isActionAuthorized(Enable.class);
			}
		};
		link.add(new Label("linkLabel", "Tonen"));
		return link;
	}

	private AbstractLink getGeblokkeerdeMeldingenTonenLink()
	{
		Link<Void> link = new Link<Void>("link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				toonGeblokkeerdeMeldingen();
			}
		};
		link.add(new Label("linkLabel", "Tonen"));
		return link;
	}

	private AbstractLink getFotoInlezenLink()
	{
		Link<Void> link = new Link<Void>("link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				leesBronFotoIn();
			}

			@Override
			public boolean isVisible()
			{
				return new DataSecurityCheck(DeelnemerBronFotoInlezen.BRON_FOTO_INLEZEN)
					.isActionAuthorized(Enable.class);
			}
		};
		link.add(new Label("linkLabel", "Foto inlezen"));
		return link;
	}

	private AbstractLink getCFILink()
	{
		Link<Void> link = new Link<Void>("link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				leesCfiTerugmeldingsoverzichtIn();
			}

			@Override
			public boolean isVisible()
			{
				return new DataSecurityCheck(DeelnemerBronCfiTerugmeldingInlezen.BRON_CFI_INLEZEN)
					.isActionAuthorized(Enable.class);
			}
		};
		link.add(new Label("linkLabel", "Cfi-terugmeldingsoverzicht inlezen"));
		return link;
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(filter);
		ComponentUtil.detachQuietly(schooljaarStatusModel);
		super.onDetach();
	}
}
