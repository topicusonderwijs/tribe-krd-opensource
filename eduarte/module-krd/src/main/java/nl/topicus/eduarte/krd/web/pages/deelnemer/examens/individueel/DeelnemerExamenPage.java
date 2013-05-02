package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.individueel;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelClickableRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.dao.helpers.DocumentTemplateDataAccessHelper;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.ExamenstatusOvergang;
import nl.topicus.eduarte.entities.examen.ToegestaneExamenstatusOvergang;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.entities.rapportage.OnderwijsDocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.OnderwijsDocumentTemplate.ExamenDocumentTemplateType;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.krd.dao.helpers.ExamendeelnameDataAccessHelper;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensInzien;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensWijzigen;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.ExamendeelnameTable;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.link.SingleEntityRapportageGenereerLink;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DocumentTemplateTable;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;
import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.dialog.Dialog;

/**
 * Pagina met de examendeelnames van een deelnemer.
 * 
 * @author loite
 * 
 */
@PageInfo(title = "Examens", menu = {"Deelnemer > [deelnemer] > Examens",
	"Groep > [groep] > [deelnemer] > Examens"})
@InPrincipal(DeelnemerExamensInzien.class)
public class DeelnemerExamenPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private Dialog diplomaLinkWindow;

	private Dialog certificaatLinkWindow;

	private Dialog cijferlijstLinkWindow;

	private final class DeelnemerExamenstatusOvergangModalWindow extends
			ExamenstatusOvergangModalWindow
	{
		private static final long serialVersionUID = 1L;

		public DeelnemerExamenstatusOvergangModalWindow(String id, ModelManager modelManager)
		{
			super(id, modelManager);
			setWindowClosedCallback(new WindowClosedCallback()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClose(AjaxRequestTarget target)
				{
					Examendeelname deelname = detailPanel.getExamendeelname();
					if (deelname == null)
					{
						deelname = getContextVerbintenis().getLaatsteExamendeelname();
					}
					if (deelname != null)
					{
						deelname.refresh();
						detailPanel.setExamendeelname(deelname);
						target.addComponent(datapanel);
						target.addComponent(detailPanel);
						refreshBottomRow(target);
					}
				}

			});
		}
	}

	private final ExamendeelnamePanel detailPanel;

	private final EduArteDataPanel<Examendeelname> datapanel;

	private final DeelnemerExamenstatusOvergangModalWindow modalWindow;

	public DeelnemerExamenPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer());
	}

	public DeelnemerExamenPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerExamenPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public DeelnemerExamenPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		this(deelnemer, verbintenis, verbintenis == null ? null : verbintenis
			.getLaatsteExamendeelname());
	}

	public DeelnemerExamenPage(Deelnemer deelnemer, Verbintenis verbintenis,
			Examendeelname examendeelname)
	{
		super(DeelnemerMenuItem.Examens, deelnemer, verbintenis);
		ExamendeelnameZoekFilter filter = new ExamendeelnameZoekFilter(deelnemer);
		filter.setVerbintenisModel(getContextVerbintenisModel());
		filter.addOrderByProperty("datumLaatsteStatusovergang");
		filter.setAscending(false);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		GeneralFilteredSortableDataProvider<Examendeelname, ExamendeelnameZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter, ExamendeelnameDataAccessHelper.class);

		DefaultModelManager modelManager =
			new DefaultModelManager(ExamenstatusOvergang.class, Examendeelname.class);
		detailPanel = new ExamendeelnamePanel("detailPanel", examendeelname, false, null);
		detailPanel.setOutputMarkupId(true);
		add(detailPanel);

		datapanel =
			new EduArteDataPanel<Examendeelname>("datapanel", provider, new ExamendeelnameTable());
		datapanel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<Examendeelname>(
			detailPanel.getExamendeelnameModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<Examendeelname> item)
			{
				target.addComponent(datapanel);
				target.addComponent(detailPanel);
				target.addComponent(diplomaLinkWindow);
				refreshBottomRow(target);
			}

		});
		add(datapanel);

		diplomaLinkWindow =
			addExamenDocumentLinkWindow("diplomaLinkWindow", ExamenDocumentTemplateType.Diploma);
		certificaatLinkWindow =
			addExamenDocumentLinkWindow("certificaatLinkWindow",
				ExamenDocumentTemplateType.Certificaat);
		cijferlijstLinkWindow =
			addExamenDocumentLinkWindow("cijferlijstLinkWindow",
				ExamenDocumentTemplateType.Cijferlijst);

		modalWindow = new DeelnemerExamenstatusOvergangModalWindow("modalWindow", modelManager);
		add(modalWindow);

		createComponents();
	}

	private Dialog addExamenDocumentLinkWindow(String id,
			ExamenDocumentTemplateType examenDocumentType)
	{

		Dialog examenDocumentLinkWindow = new Dialog(id);
		examenDocumentLinkWindow.setCloseEvent(JsScopeUiEvent
			.quickScope("setTimeout('location.reload(true)',10);"));
		examenDocumentLinkWindow.setWidth(800);
		add(examenDocumentLinkWindow);

		GeneralFilteredSortableDataProvider<DocumentTemplate, DocumentTemplateZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(
				getDocumentTemplateZoekFilter(examenDocumentType),
				DocumentTemplateDataAccessHelper.class);

		CustomDataPanel<DocumentTemplate> linkDataPanel =
			new EduArteDataPanel<DocumentTemplate>("datapanel", provider,
				new DocumentTemplateTable());
		linkDataPanel.setRowFactory(new CustomDataPanelClickableRowFactory<DocumentTemplate>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(IModel<DocumentTemplate> model)
			{
				new SingleEntityRapportageGenereerLink<Examendeelname>("link", model,
					DocumentTemplateContext.Examendeelname.getModelName(), detailPanel
						.getExamendeelnameModel()).onClick();
			}
		});
		examenDocumentLinkWindow.add(linkDataPanel);
		return examenDocumentLinkWindow;
	}

	private DocumentTemplateZoekFilter getDocumentTemplateZoekFilter(
			ExamenDocumentTemplateType examenDocumentType)
	{
		DocumentTemplateZoekFilter zoekFilter =
			new DocumentTemplateZoekFilter(getIngelogdeAccount(), OnderwijsDocumentTemplate.class);
		zoekFilter.setCategorie(DocumentTemplateCategorie.Examens);
		zoekFilter.setContext(DocumentTemplateContext.Examendeelname);
		zoekFilter.setExamenDocumentType(examenDocumentType);
		zoekFilter.setTaxonomieModel(new PropertyModel<Taxonomie>(getContextVerbintenisModel(),
			"opleiding.verbintenisgebied.taxonomie"));

		return zoekFilter;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.setOutputMarkupId(true);
		List<ToegestaneExamenstatusOvergang> overgangen;
		if (getContextVerbintenis() == null || getContextVerbintenis().getOpleiding() == null)
		{
			overgangen = Collections.emptyList();
		}
		else
		{
			Verbintenis ver = getContextVerbintenis();
			Opleiding opl = ver.getOpleiding();
			Verbintenisgebied vergeb = opl.getVerbintenisgebied();
			Taxonomie tax = vergeb.getTaxonomie();
			ExamenWorkflow flow = tax.getExamenWorkflow();
			if (flow != null)
			{
				overgangen = flow.getToegestaneExamenstatusOvergangen();
			}
			else
			{
				overgangen = Collections.emptyList();
			}
		}
		for (ToegestaneExamenstatusOvergang toeg : overgangen)
		{
			ExamenStatusovergangButton button =
				new ExamenStatusovergangButton(panel, toeg.getActieIndividueelKort(),
					CobraKeyAction.GEEN, ButtonAlignment.RIGHT);

			button.setDefaultModel(ModelFactory.getModel(toeg));
			panel.addButton(button);
		}
		panel.addButton(new BewerkenButton<Void>(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				if (detailPanel.getExamendeelname() == null)
					return DeelnemerExamenPage.this;
				return new DeelnemerExamenEditPage(detailPanel.getExamendeelname());
			}

			@Override
			public Class< ? extends DeelnemerExamenEditPage> getPageIdentity()
			{
				return DeelnemerExamenEditPage.class;
			}

		}));
		panel.addButton(new AbstractAjaxLinkButton(panel, "Naar BRON", CobraKeyAction.GEEN,
			ButtonAlignment.RIGHT)
		{

			private static final long serialVersionUID = 1L;

			@Override
			public String getLabel()
			{
				if (detailPanel.getExamendeelname() == null)
					return "";
				if (detailPanel.getExamendeelname().isMeenemenInVolgendeBronBatch())
					return "Niet naar BRON";
				else
					return "Naar BRON";
			}

			@Override
			public boolean isVisible()
			{
				Examendeelname deelname = detailPanel.getExamendeelname();
				if (deelname != null
					&& (deelname.getVerbintenis().isVOVerbintenis() || deelname.getVerbintenis()
						.isVAVOVerbintenis()))
					return true;
				return false;
			}

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				Examendeelname deelname = detailPanel.getExamendeelname();
				if (deelname != null)
				{
					deelname.setMeenemenInVolgendeBronBatch(!deelname
						.isMeenemenInVolgendeBronBatch());
					deelname.saveOrUpdate();
					deelname.commit();
					refreshBottomRow(target);
					target.addComponent(detailPanel);
				}
			}
		});
		panel.addButton(new ExamenDocumentButton(panel, "Diploma afdrukken", CobraKeyAction.GEEN,
			ButtonAlignment.LEFT, ExamenDocumentTemplateType.Diploma, diplomaLinkWindow));
		panel.addButton(new ExamenDocumentButton(panel, "Certificaat afdrukken",
			CobraKeyAction.GEEN, ButtonAlignment.LEFT, ExamenDocumentTemplateType.Certificaat,
			certificaatLinkWindow));
		panel.addButton(new ExamenDocumentButton(panel, "Cijferlijst afdrukken",
			CobraKeyAction.GEEN, ButtonAlignment.LEFT, ExamenDocumentTemplateType.Cijferlijst,
			cijferlijstLinkWindow));
	}

	private final class ExamenDocumentButton extends AbstractAjaxLinkButton
	{
		private static final long serialVersionUID = 1L;

		private final ExamenDocumentTemplateType examenDocumentType;

		private final Dialog window;

		public ExamenDocumentButton(BottomRowPanel bottomRow, String label, ActionKey action,
				ButtonAlignment alignment, ExamenDocumentTemplateType examenDocumentType,
				Dialog window)
		{
			super(bottomRow, label, action, alignment);
			this.examenDocumentType = examenDocumentType;
			this.window = window;
		}

		@Override
		protected void onClick(AjaxRequestTarget target)
		{
			window.open(target);
		}

		@Override
		public boolean isVisible()
		{
			if (examenDocumentType == ExamenDocumentTemplateType.Diploma)
			{
				return detailPanel.getExamendeelname() != null
					&& detailPanel.getExamendeelname().getExamenstatus().isGeslaagd();
			}
			if (examenDocumentType == ExamenDocumentTemplateType.Certificaat)
			{
				return detailPanel.getExamendeelname() != null
					&& detailPanel.getExamendeelname().getExamenstatus().isCertificaten();
			}
			return detailPanel.getExamendeelname() != null;
		}

		@Override
		protected WebMarkupContainer getLink(String linkId)
		{
			int listCount =
				DataAccessRegistry.getHelper(DocumentTemplateDataAccessHelper.class).listCount(
					getDocumentTemplateZoekFilter(examenDocumentType));
			if (listCount == 1)
			{
				IModel<DocumentTemplate> templateModel =
					ModelFactory.getModel(DataAccessRegistry.getHelper(
						DocumentTemplateDataAccessHelper.class).list(
						getDocumentTemplateZoekFilter(examenDocumentType)).get(0));
				return new SingleEntityRapportageGenereerLink<Examendeelname>(linkId,
					templateModel, DocumentTemplateContext.Examendeelname.getModelName(),
					detailPanel.getExamendeelnameModel());
			}
			return super.getLink(linkId);
		}
	}

	@Override
	protected void onSelectionChanged(Verbintenis verbintenis)
	{
		if (verbintenis != null)
		{
			setResponsePage(new DeelnemerExamenPage(verbintenis));
		}
	}

	@InPrincipal(DeelnemerExamensWijzigen.class)
	private class ExamenStatusovergangButton extends AbstractAjaxLinkButton
	{
		private static final long serialVersionUID = 1L;

		public ExamenStatusovergangButton(BottomRowPanel bottomRow, String label, ActionKey action,
				ButtonAlignment alignment)
		{
			super(bottomRow, label, action, alignment);
			ComponentUtil.setSecurityCheck(this, new DeelnemerSecurityCheck(new ClassSecurityCheck(
				ExamenStatusovergangButton.class), getContextDeelnemer()));
		}

		@Override
		protected void onClick(AjaxRequestTarget target)
		{
			ToegestaneExamenstatusOvergang overgang =
				(ToegestaneExamenstatusOvergang) getDefaultModelObject();
			modalWindow.setDefaultModel(ModelFactory.getModel(overgang));
			// Maak een nieuwe examendeelname aan indien dat nodig is.
			Examendeelname examendeelname;
			if (detailPanel.getExamendeelname() == null && overgang.getNaarExamenstatus() != null
				&& overgang.getNaarExamenstatus().isBeginstatus()
				&& !(overgang.isTijdvakAangeven() || overgang.isExamennummersToekennen()))
			{
				examendeelname = new Examendeelname(getContextVerbintenis());
				if (overgang.getExamenWorkflow().isHeeftTijdvakken())
				{
					examendeelname.setTijdvak(1);
				}
			}
			else
			{
				examendeelname = detailPanel.getExamendeelname();
			}
			modalWindow.show(target, examendeelname);
		}

		@Override
		protected AttributeModifier getTitleModifier()
		{
			return new AttributeModifier("title", true, new AbstractReadOnlyModel<String>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject()
				{
					ToegestaneExamenstatusOvergang overgang =
						(ToegestaneExamenstatusOvergang) getDefaultModelObject();
					return overgang.getActieIndividueel();
				}
			});
		}

		@Override
		public boolean isVisible()
		{
			if (getContextDeelnemer().getPersoon().isOverleden())
				return false;

			if (!getContextVerbintenis().getStatus().isBronCommuniceerbaar())
				return false;
			if (getContextVerbintenis().getCohort() == null)
				return false;

			ToegestaneExamenstatusOvergang overgang =
				(ToegestaneExamenstatusOvergang) getDefaultModelObject();
			if (overgang.getNaarExamenstatus() != null
				&& overgang.getNaarExamenstatus().isBeginstatus() && overgang.getVolgnummer() == 1)
			{
				// Dit is een beginstatus. Deze mag getoond worden als de
				// deelnemer geen examendeelname heeft.
				Verbintenis verbintenis = getContextVerbintenis();
				return verbintenis.getExamendeelnames().size() == 0;
				// return !verbintenis.heeftActieveExamendeelname()
				// && !verbintenis.isDiplomaBehaald();
			}
			return detailPanel.getExamendeelname() != null
				&& overgang.isToegestaanVanafExamenstatus(detailPanel.getExamendeelname()
					.getExamenstatus());
		}

	}

}
