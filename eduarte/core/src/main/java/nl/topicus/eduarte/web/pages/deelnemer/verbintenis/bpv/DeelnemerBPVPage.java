/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer.verbintenis.bpv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CollapsableGroupRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelClickableRowFactory;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.core.principals.deelnemer.verbintenis.DeelnemerBPV;
import nl.topicus.eduarte.dao.helpers.DocumentTemplateDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.PraktijkbiedendeOrganisatie;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.factory.KRDModuleComponentFactory;
import nl.topicus.eduarte.web.components.link.SingleEntityRapportageGenereerLink;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVInschrijvingTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DocumentTemplateTable;
import nl.topicus.eduarte.web.components.panels.verbintenis.DeelnemerBPVPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.AbstractEntiteitDataProvider;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.security.components.SecureComponentHelper;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;
import org.apache.wicket.util.time.Duration;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.dialog.Dialog;

/**
 * @author vandekamp
 */
@PageInfo(title = "BPV", menu = {"Deelnemer > [deelnemer] > BPV",
	"Groep > [groep] > [deelnemer] > BPV"})
@InPrincipal(DeelnemerBPV.class)
public class DeelnemerBPVPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private DeelnemerBPVPanel detailPanel;

	private Dialog overeenkomstLinkWindow;

	public DeelnemerBPVPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerBPVPage(BPVInschrijving inschrijving)
	{
		this(inschrijving.getDeelnemer(), inschrijving.getVerbintenis(), inschrijving);
	}

	public DeelnemerBPVPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerBPVPage(Verbintenis inschrijving)
	{
		this(inschrijving.getDeelnemer(), inschrijving);
	}

	public DeelnemerBPVPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		this(deelnemer, verbintenis, null);
	}

	public DeelnemerBPVPage(Deelnemer deelnemer, Verbintenis verbintenis,
			BPVInschrijving inschrijving)
	{
		super(DeelnemerMenuItem.BPV, deelnemer, verbintenis);

		IDataProvider<BPVInschrijving> dataprovider =
			new AbstractEntiteitDataProvider<BPVInschrijving>(getContextVerbintenisModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void createLijst()
				{
					List<BPVInschrijving> bpvs =
						((Verbintenis) entiteitModel.getObject()).getBpvInschrijvingen();
					lijst = new ArrayList<BPVInschrijving>();
					lijst.addAll(bpvs);
				}
			};

		final CustomDataPanel<BPVInschrijving> datapanel =
			new EduArteDataPanel<BPVInschrijving>("dataPanel", dataprovider,
				new BPVInschrijvingTable());

		detailPanel = new DeelnemerBPVPanel("detailPanel", getEersteBPVModel(inschrijving), this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getDefaultModelObject() != null;
			}
		};
		detailPanel.setOutputMarkupPlaceholderTag(true);
		detailPanel.setOutputMarkupId(true);

		datapanel.setRowFactory(new CollapsableGroupRowFactoryDecorator<BPVInschrijving>(
			new CustomDataPanelAjaxClickableRowFactory<BPVInschrijving>(detailPanel.getBPVModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(AjaxRequestTarget target, Item<BPVInschrijving> item)
				{
					detailPanel.setDefaultModelObject(item.getModelObject());
					target.addComponent(datapanel);
					target.addComponent(detailPanel);
					target.addComponent(overeenkomstLinkWindow);
					refreshBottomRow(target);
				}
			}));

		add(datapanel);
		add(detailPanel);
		overeenkomstLinkWindow = createPraktijkovereenkomstAfdrukkenLinkWindow("diplomaLinkWindow");
		createComponents();
	}

	public IModel<BPVInschrijving> getEersteBPVModel(BPVInschrijving inschrijving)
	{
		if (inschrijving != null)
		{
			return ModelFactory.getModel(inschrijving);
		}
		else
		{
			return getEersteBPVModel();
		}
	}

	private IModel<BPVInschrijving> getEersteBPVModel()
	{
		Verbintenis verbintenis = getContextVerbintenis();
		if (verbintenis != null && !verbintenis.getBpvInschrijvingen().isEmpty())
		{
			return ModelFactory.getModel(verbintenis.getBpvInschrijvingen().get(0));
		}
		else
		{
			return ModelFactory.getModel((BPVInschrijving) null);
		}
	}

	@Override
	protected void onSelectionChanged(Verbintenis inschrijving)
	{
		IModel<BPVInschrijving> bpvModel = getEersteBPVModel();
		if (bpvModel != null && bpvModel.getObject() != null)
		{
			detailPanel.setDefaultModelObject(bpvModel.getObject());
			detailPanel.setVisible(true);
		}
		else
		{
			detailPanel.setVisible(false);
		}
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.setOutputMarkupId(true);
		addModuleButtons(panel);
		panel.addButton(createBPVToevoegenButton(panel));
		panel.addButton(createBPVBewerkenButton(panel));
		addBewerkenButtonBijBeeindigd(panel);
		panel.addButton(new OvereenkomstAfdrukkenButton(panel, "Praktijkovereenkomst afdrukken",
			CobraKeyAction.GEEN, ButtonAlignment.LEFT, overeenkomstLinkWindow));
	}

	private BPVInschrijving getBpvInschrijving()
	{
		return (BPVInschrijving) detailPanel.getDefaultModelObject();
	}

	private final class OvereenkomstAfdrukkenButton extends AbstractAjaxLinkButton
	{
		private static final long serialVersionUID = 1L;

		private final Dialog window;

		public OvereenkomstAfdrukkenButton(BottomRowPanel bottomRow, String label,
				ActionKey action, ButtonAlignment alignment, Dialog window)
		{
			super(bottomRow, label, action, alignment);
			this.window = window;
			ComponentUtil.setSecurityCheck(this, new DeelnemerSecurityCheck(new DataSecurityCheck(
				SecureComponentHelper.alias(DeelnemerBPVPage.class) + "AfdrukkenButtonTonen"),
				getContextDeelnemer()));
		}

		@Override
		protected void onClick(AjaxRequestTarget target)
		{
			AjaxSelfUpdatingTimerBehavior autoUpdate =
				new AjaxSelfUpdatingTimerBehavior(Duration.seconds(3))
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onPostProcessTarget(AjaxRequestTarget timerTarget)
					{
						BPVInschrijving verbintenis = getBpvInschrijving();
						if (verbintenis != null
							&& verbintenis.getStatus() == BPVStatus.OvereenkomstAfgedrukt)
						{
							this.stop();
						}
						super.onPostProcessTarget(timerTarget);
					}

				};
			AutoFieldSet<BPVInschrijving> fieldset = detailPanel.getDetailsLinks();
			fieldset.add(autoUpdate);
			target.addComponent(fieldset);
			window.open(target);
		}

		@Override
		public boolean isVisible()
		{
			return super.isVisible() && getBpvInschrijving() != null
				&& getBpvInschrijving().getStatus() == BPVStatus.Volledig
				&& getPraktijkovereenkomstTemplates().size() > 0;
		}
	}

	private Dialog createPraktijkovereenkomstAfdrukkenLinkWindow(String id)
	{

		Dialog onderwijsOvereenkomstLinkWindow = new Dialog(id);
		onderwijsOvereenkomstLinkWindow.setCloseEvent(JsScopeUiEvent
			.quickScope("setTimeout('location.reload(true)',10);"));
		onderwijsOvereenkomstLinkWindow.setWidth(700);
		add(onderwijsOvereenkomstLinkWindow);

		LoadableDetachableModel<List<DocumentTemplate>> model =
			new LoadableDetachableModel<List<DocumentTemplate>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<DocumentTemplate> load()
				{
					return getPraktijkovereenkomstTemplates();
				}
			};
		ListModelDataProvider<DocumentTemplate> listprovider =
			new ListModelDataProvider<DocumentTemplate>(model);

		CustomDataPanel<DocumentTemplate> linkDataPanel =
			new EduArteDataPanel<DocumentTemplate>("datapanel", listprovider,
				new DocumentTemplateTable());
		linkDataPanel.setRowFactory(new CustomDataPanelClickableRowFactory<DocumentTemplate>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(@SuppressWarnings( {"hiding"}) IModel<DocumentTemplate> model)
			{
				getBpvInschrijving().setStatus(BPVStatus.OvereenkomstAfgedrukt);
				getBpvInschrijving().update();
				getBpvInschrijving().commit();

				new SingleEntityRapportageGenereerLink<BPVInschrijving>("dummy", model,
					"bpvInschrijving", detailPanel.getBPVModel()).onClick();
			}
		});
		onderwijsOvereenkomstLinkWindow.add(linkDataPanel);
		return onderwijsOvereenkomstLinkWindow;
	}

	private DocumentTemplateZoekFilter getDocumentTemplateZoekFilter()
	{
		DocumentTemplateZoekFilter templateZoekFilter = new DocumentTemplateZoekFilter();
		templateZoekFilter.setAccount(getIngelogdeAccount());
		if (getBpvInschrijving() != null)
			templateZoekFilter.setTaxonomie(getBpvInschrijving().getVerbintenis().getTaxonomie());
		templateZoekFilter.setContext(DocumentTemplateContext.BPVVerbintenis);
		templateZoekFilter.setCategorie(DocumentTemplateCategorie.BPVOvereenkomst);
		return templateZoekFilter;
	}

	private List<DocumentTemplate> getPraktijkovereenkomstTemplates()
	{
		if (getBpvInschrijving() != null)
		{
			DocumentTemplateDataAccessHelper dah =
				DataAccessRegistry.getHelper(DocumentTemplateDataAccessHelper.class);
			List<DocumentTemplate> templates = dah.list(getDocumentTemplateZoekFilter());
			return templates;
		}
		return Collections.emptyList();
	}

	private ModuleEditPageButton<BPVInschrijving> createBPVBewerkenButton(BottomRowPanel panel)
	{
		return new ModuleEditPageButton<BPVInschrijving>(panel, "Bewerken",
			CobraKeyAction.BEWERKEN, BPVInschrijving.class, getSelectedMenuItem(),
			DeelnemerBPVPage.this, detailPanel.getBPVModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				BPVInschrijving bpvInschrijving =
					(BPVInschrijving) detailPanel.getDefaultModelObject();

				return super.isVisible() && detailPanel.getDefaultModelObject() != null
					&& !bpvInschrijving.getStatus().equals(BPVStatus.Beëindigd);
			}
		};
	}

	private ModuleEditPageButton<BPVInschrijving> createBPVToevoegenButton(BottomRowPanel panel)
	{
		return new ModuleEditPageButton<BPVInschrijving>(panel, "Nieuwe BPV",
			CobraKeyAction.TOEVOEGEN, BPVInschrijving.class, getSelectedMenuItem(),
			DeelnemerBPVPage.this, null)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getContextVerbintenis() != null
					&& getContextVerbintenis().getStatus().isActief();
			}

			@Override
			protected BPVInschrijving getEntity()
			{
				return createNieuweBPV();
			}

		};
	}

	private BPVInschrijving createNieuweBPV()
	{
		BPVInschrijving bpvInschrijving = new BPVInschrijving(getContextVerbintenis());
		bpvInschrijving.setOvereenkomstnummer(DataAccessRegistry.getHelper(
			NummerGeneratorDataAccessHelper.class).newOvereenkomstnummer());
		bpvInschrijving.setStatus(BPVStatus.Voorlopig);
		bpvInschrijving.setPraktijkbiedendeOrganisatie(PraktijkbiedendeOrganisatie.BPVBEDRIJF);
		bpvInschrijving.setOpnemenInBron(getContextVerbintenis().isBOVerbintenis());

		if (getContextVerbintenis().getOpleiding() != null
			&& MBOLeerweg.BBL.equals(getContextVerbintenis().getOpleiding().getLeerweg()))
		{
			bpvInschrijving.setVerwachteEinddatum(getContextVerbintenis().getGeplandeEinddatum());
		}

		return bpvInschrijving;
	}

	private void addBewerkenButtonBijBeeindigd(BottomRowPanel panel)
	{
		List<KRDModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(KRDModuleComponentFactory.class);
		if (detailPanel.getDefaultModelObject() != null)
			for (KRDModuleComponentFactory factory : factories)
			{
				IModel<Boolean> visibilityModelBeeindigd = new AbstractReadOnlyModel<Boolean>()
				{

					private static final long serialVersionUID = 1L;

					@Override
					public Boolean getObject()
					{
						BPVInschrijving bpvInschrijving =
							(BPVInschrijving) detailPanel.getDefaultModelObject();
						return bpvInschrijving.getStatus().equals(BPVStatus.Beëindigd);
					}
				};
				factory.newBPVBeeindigenKnop(panel, detailPanel.getBPVModel(),
					DeelnemerBPVPage.this, visibilityModelBeeindigd, "Bewerken",
					ButtonAlignment.RIGHT);
			}
	}

	private void addModuleButtons(BottomRowPanel panel)
	{
		List<KRDModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(KRDModuleComponentFactory.class);
		if (detailPanel.getDefaultModelObject() != null)
			for (KRDModuleComponentFactory factory : factories)
			{
				final IModel<Boolean> visibilityModelDefinitief =
					new AbstractReadOnlyModel<Boolean>()
					{
						private static final long serialVersionUID = 1L;

						@Override
						public Boolean getObject()
						{
							BPVInschrijving bpvInschrijving =
								(BPVInschrijving) detailPanel.getDefaultModelObject();
							return bpvInschrijving.getStatus().equals(BPVStatus.Definitief);

						}
					};

				factory.newBPVBeeindigenKnop(panel, detailPanel.getBPVModel(),
					DeelnemerBPVPage.this, visibilityModelDefinitief, "BPV beëindigen",
					ButtonAlignment.LEFT);

				factory.newBPVKopierenKnop(panel, detailPanel.getBPVModel(), DeelnemerBPVPage.this,
					visibilityModelDefinitief);
			}
	}
}
