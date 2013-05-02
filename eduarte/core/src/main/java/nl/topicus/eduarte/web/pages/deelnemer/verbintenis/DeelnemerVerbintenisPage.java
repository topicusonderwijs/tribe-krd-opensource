/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer.verbintenis;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.cobra.web.components.datapanel.CollapsableRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelClickableRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.core.principals.deelnemer.verbintenis.DeelnemerVerbintenissen;
import nl.topicus.eduarte.dao.helpers.DocumentTemplateDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.web.components.datapanel.HighlightVerbintenisRowFactoryDecorator;
import nl.topicus.eduarte.web.components.factory.KRDModuleComponentFactory;
import nl.topicus.eduarte.web.components.link.SingleEntityRapportageGenereerLink;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DocumentTemplateTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.VerbintenisMetPlaatsingenTable;
import nl.topicus.eduarte.web.components.panels.verbintenis.DeelnemerVerbintenisDetailPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;

import org.apache.wicket.Component;
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
 * @author idserda
 */
@PageInfo(title = "Verbintenis", menu = {"Deelnemer > [deelnemer] > Verbintenis",
	"Groep > [groep] > [deelnemer] > Verbintenis"})
@InPrincipal(DeelnemerVerbintenissen.class)
public class DeelnemerVerbintenisPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private ModuleEditPageButton<Plaatsing> plaatsingBewerken;

	private IModel<Boolean> visibilityModel;

	private Dialog overeenkomstLinkWindow;

	protected DeelnemerVerbintenisDetailPanel detailPanel;

	protected CustomDataPanel<IdObject> datapanel;

	public DeelnemerVerbintenisPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerVerbintenisPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerVerbintenisPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public DeelnemerVerbintenisPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(DeelnemerMenuItem.Verbintenis, deelnemer, verbintenis);

		IDataProvider<IdObject> dataprovider =
			new VerbintenisMetPlaatsingenDataProvider(getContextDeelnemerModel());

		detailPanel =
			new DeelnemerVerbintenisDetailPanel("detailPanel", ModelFactory.getModel(verbintenis));
		detailPanel.setOutputMarkupId(true);
		updateVerbintenis(verbintenis);

		CollapsableRowFactoryDecorator<IdObject> rowFactory =
			new CollapsableRowFactoryDecorator<IdObject>(
				new HighlightVerbintenisRowFactoryDecorator<IdObject>(
					new CustomDataPanelAjaxClickableRowFactory<IdObject>(detailPanel
						.getPlaatsingOfVerbintenisModel())
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void onClick(AjaxRequestTarget target, Item<IdObject> item)
						{
							Object obj = selectedObject.getObject();

							if (obj instanceof Plaatsing)
							{
								plaatsingBewerken.setVisible(true);
							}
							else if (obj instanceof Verbintenis)
							{
								plaatsingBewerken.setVisible(false);
							}

							target.addComponent(datapanel);
							target.addComponent(recreateBottomRow());
							target.addComponent(detailPanel);
							target.addComponent(overeenkomstLinkWindow);
						}

						@Override
						protected boolean isSelected(CustomDataPanel<IdObject> panel,
								Item<IdObject> item, IModel<IdObject> itemModel)
						{
							return selectedObject != null
								&& JavaUtil.equalsOrBothNull(selectedObject.getObject(), itemModel
									.getObject());
						}
					}));

		datapanel =
			new EduArteDataPanel<IdObject>("dataPanel", dataprovider,
				new VerbintenisMetPlaatsingenTable(rowFactory));
		datapanel.setGroeperenButtonVisible(false);
		datapanel.setRowFactory(rowFactory);

		add(datapanel);
		add(detailPanel);

		overeenkomstLinkWindow =
			createOnderwijsovereenkomstAfdrukkenLinkWindow("diplomaLinkWindow");

		createComponents();
	}

	private Dialog createOnderwijsovereenkomstAfdrukkenLinkWindow(String id)
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
					return getOnderwijsovereenkomstTemplates();
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
			protected void onClick(IModel<DocumentTemplate> documentTemplateModel)
			{
				getVerbintenis().setStatus(VerbintenisStatus.Afgedrukt);
				getVerbintenis().update();
				getVerbintenis().commit();

				new SingleEntityRapportageGenereerLink<Verbintenis>("dummy", documentTemplateModel,
					"verbintenis", detailPanel.getModelAsVerbintenis()).onClick();
			}
		});
		onderwijsOvereenkomstLinkWindow.add(linkDataPanel);
		return onderwijsOvereenkomstLinkWindow;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.setOutputMarkupId(true);
		panel.setDefaultModel(detailPanel.getDefaultModel());

		addModuleButtons(panel);
		panel.addButton(createPlaatsingToevoegenButton(panel));
		createVerbintenisBewerkenButton(panel);

		panel.addButton(plaatsingBewerken = createPlaatsingBewerkenButton(panel));

		panel.addButton(new OvereenkomstAfdrukkenButton(panel, "Onderwijsovereenkomst afdrukken",
			CobraKeyAction.GEEN, ButtonAlignment.LEFT, overeenkomstLinkWindow));
	}

	protected void createVerbintenisBewerkenButton(BottomRowPanel panel)
	{
		List<KRDModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(KRDModuleComponentFactory.class);
		visibilityModel = new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				Object obj = detailPanel.getDefaultModelObject();

				if (obj instanceof Verbintenis)
					return Boolean.TRUE;
				else
					return Boolean.FALSE;
			}
		};

		for (KRDModuleComponentFactory factory : factories)
		{
			factory.newVerbintenisBewerkOfVerwijderenKnop(panel, detailPanel
				.getModelAsVerbintenis(), visibilityModel, this);
		}
	}

	private void addModuleButtons(BottomRowPanel panel)
	{
		List<KRDModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(KRDModuleComponentFactory.class);
		for (KRDModuleComponentFactory factory : factories)
		{
			factory.newVerbintenisToevoegenKnop(panel, getContextDeelnemerModel(),
				DeelnemerVerbintenisPage.this);
			visibilityModel = new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return isBeeindigenKnopZichtbaar();
				}
			};

			factory.newVerbintenisBeeindigenKnop(panel, getGeselecteerdeVerbintenisModel(),
				visibilityModel);
			factory.newHerIntakeKnop(panel, getContextDeelnemerModel(),
				DeelnemerVerbintenisPage.this);
			factory.newVerbintenisKopierenKnop(panel, detailPanel.getModelAsVerbintenis(), this,
				visibilityModel);
		}
	}

	private DocumentTemplateZoekFilter getDocumentTemplateZoekFilter()
	{
		DocumentTemplateZoekFilter templateZoekFilter = new DocumentTemplateZoekFilter();
		templateZoekFilter.setAccount(getIngelogdeAccount());
		if (getVerbintenis() != null)
			templateZoekFilter.setTaxonomie(getVerbintenis().getTaxonomie());
		templateZoekFilter.setContext(DocumentTemplateContext.Verbintenis);
		templateZoekFilter.setCategorie(DocumentTemplateCategorie.Onderwijsovereenkomst);
		return templateZoekFilter;
	}

	private List<DocumentTemplate> getOnderwijsovereenkomstTemplates()
	{
		if (getVerbintenis() != null)
		{
			DocumentTemplateDataAccessHelper dah =
				DataAccessRegistry.getHelper(DocumentTemplateDataAccessHelper.class);
			List<DocumentTemplate> templates = dah.list(getDocumentTemplateZoekFilter());
			return templates;
		}
		return Collections.emptyList();
	}

	private void updateVerbintenis(Verbintenis verbintenis)
	{
		IModel<IdObject> model = detailPanel.getPlaatsingOfVerbintenisModel();
		if (verbintenis != null && verbintenis.getStatus() == VerbintenisStatus.Intake)
			model.setObject(null);
		else
			model.setObject(verbintenis);
	}

	private Verbintenis getVerbintenis()
	{
		if (detailPanel.getDefaultModelObject() instanceof Verbintenis)
			return (Verbintenis) detailPanel.getDefaultModelObject();
		else
			return null;
	}

	protected Plaatsing createNieuwePlaatsing(Verbintenis verbintenis)
	{
		return verbintenis.nieuwePlaatsing();
	}

	protected Verbintenis getGeselecteerdeVerbintenis()
	{
		Verbintenis ret = null;
		Object obj = detailPanel.getDefaultModelObject();

		if (obj instanceof VerbintenisProvider)
		{
			ret = ((VerbintenisProvider) obj).getVerbintenis();
		}
		return ret;
	}

	protected Plaatsing getGeselecteerdePlaatsing()
	{
		Object obj = detailPanel.getDefaultModelObject();

		if (obj instanceof Plaatsing)
		{
			return (Plaatsing) obj;
		}
		return null;
	}

	protected IModel<Verbintenis> getGeselecteerdeVerbintenisModel()
	{
		return new LoadableDetachableModel<Verbintenis>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Verbintenis load()
			{
				return getGeselecteerdeVerbintenis();
			}
		};
	}

	@Override
	public void detachModels()
	{
		visibilityModel.detach();
		super.detachModels();
	}

	@Override
	protected void onSelectionChanged(Verbintenis verbintenis)
	{
		updateVerbintenis(verbintenis);
	}

	protected boolean isBeeindigenKnopZichtbaar()
	{
		Object obj = detailPanel.getDefaultModelObject();
		if (!(obj instanceof Verbintenis))
			return false;

		Verbintenis v = (Verbintenis) obj;
		return v.getStatus().equals(VerbintenisStatus.Definitief);
	}

	protected ModuleEditPageButton<Plaatsing> createPlaatsingBewerkenButton(BottomRowPanel panel)
	{
		ModuleEditPageButton<Plaatsing> ret =
			new ModuleEditPageButton<Plaatsing>(panel, "Bewerken", CobraKeyAction.GEEN,
				Plaatsing.class, getSelectedMenuItem(), DeelnemerVerbintenisPage.this, detailPanel
					.getModelAsPlaatsing())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && getGeselecteerdePlaatsing() != null;
				}
			};
		return ret;
	}

	protected ModuleEditPageButton<Plaatsing> createPlaatsingToevoegenButton(BottomRowPanel panel)
	{
		ModuleEditPageButton<Plaatsing> plaatsingToevoegen =
			new ModuleEditPageButton<Plaatsing>(panel, "Nieuwe plaatsing", CobraKeyAction.GEEN,
				Plaatsing.class, getSelectedMenuItem(), DeelnemerVerbintenisPage.this, null)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Plaatsing getEntity()
				{
					return createNieuwePlaatsing(getGeselecteerdeVerbintenis());
				}

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && getGeselecteerdeVerbintenis() != null
						&& !getGeselecteerdeVerbintenis().getStatus().isAfgesloten();
				}

			};
		return plaatsingToevoegen;
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
				SecureComponentHelper.alias(DeelnemerVerbintenisPage.class)
					+ "AfdrukkenButtonTonen"), getContextDeelnemer()));
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
						Verbintenis verbintenis = getVerbintenis();
						if (verbintenis != null
							&& verbintenis.getStatus() == VerbintenisStatus.Afgedrukt)
						{
							this.stop();
						}
						super.onPostProcessTarget(timerTarget);
					}

				};
			Component fieldset =
				DeelnemerVerbintenisPage.this.get("detailPanel:detailsVerbintenis:detailsLinks");
			fieldset.add(autoUpdate);
			target.addComponent(fieldset);
			window.open(target);
		}

		@Override
		public boolean isVisible()
		{
			return getVerbintenis() != null
				&& getVerbintenis().getStatus() == VerbintenisStatus.Volledig
				&& getOnderwijsovereenkomstTemplates().size() > 0;
		}
	}

}
