/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer.intake;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CollapsableRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.DisableSecurityCheckMarker;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.checks.OrganisatieEenheidLocatieSecurityCheck;
import nl.topicus.eduarte.core.principals.deelnemer.verbintenis.DeelnemerIntakes;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.web.components.datapanel.HighlightVerbintenisRowFactoryDecorator;
import nl.topicus.eduarte.web.components.factory.KRDModuleComponentFactory;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.IntakegesprekTable;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.IntakegesprekZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Intake = verbintenis <br/>
 * Intakegesprek = gesprek gaand over een verbintenis
 * 
 * @author hoeve
 */
@PageInfo(title = "Intake", menu = {"Deelnemer > [deelnemer]", "Groep > [groep] > [deelnemer]"})
@InPrincipal(DeelnemerIntakes.class)
public class DeelnemerIntakePage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	protected DeelnemerIntakeDetailPanel detailPanel;

	protected CustomDataPanel<VerbintenisProvider> datapanel;

	private ModuleEditPageButton<Intakegesprek> intakegesprekToevoegen;

	public DeelnemerIntakePage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerIntakePage(Deelnemer deelnemer)
	{
		this(deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerIntakePage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public DeelnemerIntakePage(@SuppressWarnings("unused") Deelnemer deelnemer,
			Verbintenis verbintenis)
	{
		this(verbintenis, verbintenis.getEerstVolgendeIntakegesprek());
	}

	@SuppressWarnings("unchecked")
	public DeelnemerIntakePage(Verbintenis verbintenis, Intakegesprek intakegesprek)
	{
		super(DeelnemerMenuItem.Intake, verbintenis.getDeelnemer(), verbintenis);

		IntakegesprekZoekFilter filter = new IntakegesprekZoekFilter();

		filter.setDeelnemerId(getContextDeelnemer().getId());

		IDataProvider<VerbintenisProvider> dataprovider =
			new ListModelDataProvider<VerbintenisProvider>(
				new LoadableDetachableModel<List<VerbintenisProvider>>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected List<VerbintenisProvider> load()
					{
						List<VerbintenisProvider> lijst = new ArrayList<VerbintenisProvider>();
						for (Verbintenis intake : getContextDeelnemer().getVerbintenissen())
						{
							List<Intakegesprek> gesprekken = intake.getIntakegesprekken();
							if (intake.getStatus() == VerbintenisStatus.Intake
								|| !gesprekken.isEmpty())
							{
								lijst.add(intake);
								for (Intakegesprek gesprek : gesprekken)
								{
									lijst.add(gesprek);
								}
							}
						}
						return lijst;
					}
				});

		IModel model = ModelFactory.getModel(intakegesprek);
		if (model.getObject() == null && dataprovider.size() > 0)
		{
			model.setObject(dataprovider.iterator(0, 1).next());
		}

		detailPanel =
			new DeelnemerIntakeDetailPanel("detailPanel", model, DeelnemerIntakePage.this);
		detailPanel.setOutputMarkupPlaceholderTag(true);

		CollapsableRowFactoryDecorator<VerbintenisProvider> rowFactory =
			new CollapsableRowFactoryDecorator<VerbintenisProvider>(
				new HighlightVerbintenisRowFactoryDecorator<VerbintenisProvider>(
					new CustomDataPanelAjaxClickableRowFactory<VerbintenisProvider>(detailPanel
						.getVerbintenisOfIntakegesprekModel())
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void onClick(AjaxRequestTarget target,
								Item<VerbintenisProvider> item)
						{
							target.addComponent(datapanel);
							target.addComponent(detailPanel);
							target.addComponent(recreateBottomRow());
						}
					}));
		datapanel =
			new EduArteDataPanel<VerbintenisProvider>("dataPanel", dataprovider,
				new IntakegesprekTable<VerbintenisProvider>(rowFactory, false));
		datapanel.setGroeperenButtonVisible(false);
		datapanel.setRowFactory(rowFactory);

		add(datapanel);
		add(detailPanel);

		createComponents();
	}

	private Verbintenis getGeselecteerdeVerbintenis()
	{
		if (detailPanel.getDefaultModelObject() instanceof Verbintenis)
			return (Verbintenis) detailPanel.getDefaultModelObject();
		return null;
	}

	private Intakegesprek getGeselecteerdIntakegesprek()
	{
		if (detailPanel.getDefaultModelObject() instanceof Intakegesprek)
			return (Intakegesprek) detailPanel.getDefaultModelObject();
		return null;
	}

	private Verbintenis getVerbintenis()
	{
		if (getGeselecteerdeVerbintenis() != null)
			return getGeselecteerdeVerbintenis();
		else if (getGeselecteerdIntakegesprek() != null)
			return getGeselecteerdIntakegesprek().getVerbintenis();
		else
			return null;
	}

	protected Intakegesprek createNieuwIntakegesprek()
	{
		Verbintenis intake = getVerbintenis() != null ? getVerbintenis() : getContextVerbintenis();
		Intakegesprek intakegesprek = new Intakegesprek();
		intakegesprek.setVerbintenis(intake);
		intakegesprek.setOrganisatieEenheid(intake.getOrganisatieEenheid());
		intakegesprek.setGewensteOpleiding(intake.getOpleiding());
		intakegesprek.setGewensteLocatie(intake.getLocatie());
		return intakegesprek;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.setDefaultModel(detailPanel.getDefaultModel());
		panel.addButton(createIntakegesprekToevoegenButton(panel));
		createHerIntakeButton(panel);
		if (detailPanel.getDefaultModelObject() != null)
		{
			panel.addButton(createIntakegesprekBewerkenButton(panel));
			panel.addButton(createVerbintenisMakenVanIntakeButton(panel));
		}
	}

	protected ModuleEditPageButton<Intakegesprek> createIntakegesprekBewerkenButton(
			BottomRowPanel panel)
	{
		ModuleEditPageButton<Intakegesprek> ret =
			new ModuleEditPageButton<Intakegesprek>(panel, "Bewerken", CobraKeyAction.GEEN,
				Intakegesprek.class, getSelectedMenuItem(), DeelnemerIntakePage.this, detailPanel
					.getModelAsIntakegesprek())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && getGeselecteerdIntakegesprek() != null;
				}
			};
		return ret;
	}

	protected void createHerIntakeButton(BottomRowPanel panel)
	{
		List<KRDModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(KRDModuleComponentFactory.class);
		for (KRDModuleComponentFactory factory : factories)
		{
			factory.newHerIntakeKnop(panel, getContextDeelnemerModel(), DeelnemerIntakePage.this);
		}
	}

	protected ModuleEditPageButton<Intakegesprek> createIntakegesprekToevoegenButton(
			BottomRowPanel panel)
	{
		intakegesprekToevoegen =
			new ModuleEditPageButton<Intakegesprek>(panel, "Nieuw intakegesprek",
				CobraKeyAction.GEEN, Intakegesprek.class, getSelectedMenuItem(),
				DeelnemerIntakePage.this, null)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Intakegesprek getEntity()
				{
					return createNieuwIntakegesprek();
				}

				@Override
				public boolean isVisible()
				{
					return super.isVisible()
						&& ((getVerbintenis() != null && !getVerbintenis().getStatus()
							.isAfgesloten()) || getContextVerbintenis() != null
							&& !getContextVerbintenis().isBeeindigd());
				}
			};
		DisableSecurityCheckMarker.place(intakegesprekToevoegen,
			OrganisatieEenheidLocatieSecurityCheck.class);
		return intakegesprekToevoegen;
	}

	protected ModuleEditPageButton<Verbintenis> createVerbintenisMakenVanIntakeButton(
			BottomRowPanel panel)
	{
		ModuleEditPageButton<Verbintenis> verbintenisToevoegen =
			new ModuleEditPageButton<Verbintenis>(panel, "Verbintenis maken/afmelden",
				CobraKeyAction.GEEN, Verbintenis.class, DeelnemerMenuItem.Verbintenis, this, null)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Verbintenis getEntity()
				{
					Intakegesprek gesprek = getGeselecteerdIntakegesprek();
					Verbintenis intake = gesprek.getVerbintenis();
					if (gesprek.getGewensteBegindatum() != null)
					{
						intake.setBegindatum(gesprek.getGewensteBegindatum());
						Date eind = intake.berekenGeplandeEinddatum();
						if (eind != null)
							intake.setGeplandeEinddatum(eind);
					}
					if (gesprek.getGewensteEinddatum() != null)
						intake.setGeplandeEinddatum(gesprek.getGewensteEinddatum());
					if (gesprek.getGewensteOpleiding() != null)
						intake.setOpleiding(gesprek.getGewensteOpleiding());
					if (gesprek.getGewensteOpleiding() != null)
						intake.setLocatie(gesprek.getGewensteLocatie());
					intake.setStatus(VerbintenisStatus.Voorlopig);
					return intake;
				}

				@Override
				protected Page createTargetPage(
						Class< ? extends IModuleEditPage<Verbintenis>> pageClass,
						SecurePage returnPage)
				{
					return (Page) ReflectionUtil.invokeConstructor(pageClass, getEntity(),
						returnPage, Boolean.TRUE);
				}

				@Override
				public boolean isVisible()
				{
					if (getContextDeelnemer().getPersoon().isOverleden())
						return false;

					return getGeselecteerdIntakegesprek() != null
						&& VerbintenisStatus.Intake.equals(getGeselecteerdIntakegesprek()
							.getVerbintenis().getStatus());
				}

			};
		return verbintenisToevoegen;
	}

	@Override
	protected void onSelectionChanged(Verbintenis verbintenis)
	{
		if (VerbintenisStatus.Intake.equals(verbintenis.getStatus()))
			detailPanel.setDefaultModelObject(verbintenis);
		else
			intakegesprekToevoegen.setVisible(!verbintenis.isBeeindigd());
	}
}
