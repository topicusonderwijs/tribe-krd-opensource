package nl.topicus.eduarte.resultaten.web.pages.shared;

import static nl.topicus.eduarte.web.components.resultaat.AbstractToetsBoomComparator.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.IModelDataProvider;
import nl.topicus.cobra.modelsv2.ExtendedModel;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.resultaat.ResultaatVersionCollection;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Status;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.Herkansingsscore;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.Scoreschaal;
import nl.topicus.eduarte.resultaten.jobs.ResultatenHerberekenJob;
import nl.topicus.eduarte.resultaten.jobs.ResultatenHerberekenJobDataMap;
import nl.topicus.eduarte.resultaten.principals.onderwijs.OnderwijsproductResultaatstructuurWrite;
import nl.topicus.eduarte.resultaten.web.components.datapanel.columns.DeeltoetsOmhoogVerplaatsenButtonColumn;
import nl.topicus.eduarte.resultaten.web.components.datapanel.columns.DeeltoetsOmlaagVerplaatsenButtonColumn;
import nl.topicus.eduarte.resultaten.web.components.datapanel.columns.DeeltoetsToevoegenButtonColumn;
import nl.topicus.eduarte.resultaten.web.components.datapanel.columns.DeeltoetsVerwijderenButtonColumn;
import nl.topicus.eduarte.resultaten.web.components.factory.ToetsWizardButtonFactory;
import nl.topicus.eduarte.resultaten.web.components.security.ResultaatstructuurSecurityCheck;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ToetsTable;
import nl.topicus.eduarte.web.components.resultaat.StructuurToetsComparator;
import nl.topicus.eduarte.web.components.resultaat.ToetsDirectedGraph;
import nl.topicus.eduarte.web.components.resultaat.ToetsEdge;
import nl.topicus.eduarte.web.components.resultaat.ToetsVertex;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.SubpageContext;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jgrapht.alg.CycleDetector;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PageInfo(title = "Resultaatstructuur bewerken", menu = "Onderwijs > Onderwijsproducten > [onderwijsproduct] > Resultaten")
@InPrincipal(OnderwijsproductResultaatstructuurWrite.class)
@RequiredSecurityCheck(ResultaatstructuurSecurityCheck.class)
public class ResultaatstructuurEditPage extends AbstractDynamicContextPage<Resultaatstructuur>
		implements IEditPage
{
	private class ResultaatstructuurVerwijderenButton extends VerwijderButton
	{
		private static final long serialVersionUID = 1L;

		@SpringBean
		private ResultaatstructuurDataAccessHelper helper;

		public ResultaatstructuurVerwijderenButton(BottomRowPanel bottomRow)
		{
			super(bottomRow, "Verwijderen", "Weet u zeker dat u deze resultaatstructuur en alle "
				+ "ingevulde resultaten wilt verwijderen? Dit kan niet ongedaan gemaakt worden.");
			ComponentUtil.setSecurityCheck(this, new ResultaatstructuurSecurityCheck(
				new AlwaysGrantedSecurityCheck(), getResultaatstructuur())
				.setSecurityIdPostfix(Resultaatstructuur.VERWIJDEREN));
			setAction(CobraKeyAction.GEEN);
		}

		@Override
		public boolean isVisible()
		{
			return getResultaatstructuur().isSaved();
		}

		@Override
		protected void onClick()
		{
			helper.verwijderStructuurEnBijbehorendeResultaten(getResultaatstructuurModel());
			helper.batchExecute();
			setResponsePage(returnPage.getReturnPageAfterDelete());
		}
	}

	private class ResultatenVerwijderenButton extends VerwijderButton
	{
		private static final long serialVersionUID = 1L;

		@SpringBean
		private ResultaatstructuurDataAccessHelper helper;

		public ResultatenVerwijderenButton(BottomRowPanel bottomRow)
		{
			super(bottomRow, "Resultaten verwijderen", "Weet u zeker dat u alle ingevulde "
				+ "resultaten voor deze resultaatstructuur wilt verwijderen? Dit kan niet "
				+ "ongedaan gemaakt worden.");
			ComponentUtil.setSecurityCheck(this, new ResultaatstructuurSecurityCheck(
				new AlwaysGrantedSecurityCheck(), getResultaatstructuur())
				.setSecurityIdPostfix(Resultaatstructuur.VERWIJDEREN));
			setAction(CobraKeyAction.GEEN);
		}

		@Override
		public boolean isVisible()
		{
			return getResultaatstructuur().isSaved();
		}

		@Override
		protected void onClick()
		{
			helper.verwijderBijbehorendeResultaten(getResultaatstructuur());
			helper.batchExecute();
		}
	}

	private static final Logger log = LoggerFactory.getLogger(ResultaatstructuurEditPage.class);

	private static final long serialVersionUID = 1L;

	private ResultaatstructuurReturnPage returnPage;

	private Set<IModel<Toets>> toetsenToUpdate = new HashSet<IModel<Toets>>();

	private ResultaatVersionCollection versions;

	private ModelManager modelManager;

	private CustomDataPanel<Toets> toetsenPanel;

	public ResultaatstructuurEditPage(Resultaatstructuur structuur,
			ResultaatstructuurReturnPage returnPage)
	{
		this(ModelFactory.getCompoundChangeRecordingModel(structuur, Resultaatstructuur
			.createModelManager()), returnPage);
	}

	public ResultaatstructuurEditPage(IChangeRecordingModel<Resultaatstructuur> structuurModel,
			ResultaatstructuurReturnPage returnPage)
	{
		super(structuurModel, new SubpageContext(returnPage.getReturnPage()));

		this.returnPage = returnPage;
		modelManager = structuurModel.getManager();

		versions = new ResultaatVersionCollection(getResultaatstructuur());

		add(toetsenPanel = createDataPanel());
		createComponents();
	}

	private CustomDataPanel<Toets> createDataPanel()
	{
		ToetsTable tableContents =
			new ToetsTable(new PropertyModel<Integer>(getContextModel(), "depth"));
		tableContents.addColumn(new BooleanPropertyColumn<Toets>("Heeft resultaten", "Heeft res.",
			null, "heeftResultaten"));
		tableContents.addColumn(new DeeltoetsToevoegenButtonColumn()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<Toets> rowModel)
			{
				Toets parentToets = rowModel.getObject();
				Toets deelToets = new Toets(parentToets);
				deelToets.setWeging(1);
				deelToets.setScoreBijHerkansing(Herkansingsscore.Hoogste);
				deelToets.setScoreschaal(Scoreschaal.Geen);

				setResponsePage(EduArteApp.get().getFirstPanelFactory(
					ToetsWizardButtonFactory.class, EduArteContext.get().getOrganisatie())
					.createDefaultEditPage(
						(ExtendedModel<Toets>) modelManager.getModel(deelToets, null),
						ResultaatstructuurEditPage.this));
			}
		}.setPositioning(Positioning.FIXED_RIGHT));
		tableContents.addColumn(new DeeltoetsVerwijderenButtonColumn()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<Toets> rowModel,
					AjaxRequestTarget target)
			{
				Toets toets = rowModel.getObject();
				getResultaatstructuur().getToetsen().remove(toets);
				toets.getParent().getChildren().remove(toets);
				target.addComponent(toetsenPanel);
				target.addComponent(getBottomRow());
				addToetsToUpdate(toets.getParent());
			}
		}.setPositioning(Positioning.FIXED_RIGHT));
		tableContents.addColumn(new DeeltoetsOmlaagVerplaatsenButtonColumn()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<Toets> rowModel,
					AjaxRequestTarget target)
			{
				Toets toets = rowModel.getObject();
				Toets parent = toets.getParent();
				int index = parent.getChildren().indexOf(toets);
				parent.swapToetsen(index, index + 1);
				target.addComponent(toetsenPanel);
				target.addComponent(getBottomRow());
				addToetsToUpdate(toets.getParent());
			}
		}.setPositioning(Positioning.FIXED_RIGHT));
		tableContents.addColumn(new DeeltoetsOmhoogVerplaatsenButtonColumn()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<Toets> rowModel,
					AjaxRequestTarget target)
			{
				Toets toets = rowModel.getObject();
				Toets parent = toets.getParent();
				int index = parent.getChildren().indexOf(toets);
				parent.swapToetsen(index, index - 1);
				target.addComponent(toetsenPanel);
				target.addComponent(getBottomRow());
				addToetsToUpdate(toets.getParent());
			}
		}.setPositioning(Positioning.FIXED_RIGHT));

		IModelDataProvider<Toets> provider =
			new IModelDataProvider<Toets>(new AbstractReadOnlyModel<List<Toets>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public List<Toets> getObject()
				{
					List<Toets> toetsen =
						new ArrayList<Toets>(getResultaatstructuur().getToetsen());
					Collections.sort(toetsen, new StructuurToetsComparator(ASCENDING));
					return toetsen;
				}
			})
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<Toets> model(Toets object)
				{
					return modelManager.getModel(object, null);
				}
			};
		EduArteDataPanel<Toets> datapanel =
			new EduArteDataPanel<Toets>("datapanel", provider, tableContents);
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Toets>(
			OnderwijsproductToetsEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<Toets> item)
			{
				setResponsePage(EduArteApp.get().getFirstPanelFactory(
					ToetsWizardButtonFactory.class, EduArteContext.get().getOrganisatie())
					.createDefaultEditPage(
						(ExtendedModel<Toets>) modelManager.getModel(item.getModelObject(), null),
						ResultaatstructuurEditPage.this));
			}
		});
		return datapanel;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AbstractLinkButton(panel, "Opslaan en beschikbaar maken",
			CobraKeyAction.GEEN, ButtonAlignment.RIGHT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				doOpslaan(true);
			}

			@Override
			public boolean isVisible()
			{
				return !toetsenToUpdate.isEmpty()
					|| !getResultaatstructuur().getEindresultaat().isSaved();
			}
		});
		panel.addButton(new OpslaanButton(panel, null)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				doOpslaan(false);
			}

			@Override
			protected boolean isFormSubmittingButton()
			{
				return false;
			}

			@Override
			public boolean isVisible()
			{
				return !toetsenToUpdate.isEmpty()
					|| !getResultaatstructuur().getEindresultaat().isSaved();
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnPage.getReturnPage()));
		panel.addButton(new ResultaatstructuurVerwijderenButton(panel));
		panel.addButton(new ResultatenVerwijderenButton(panel));
	}

	private void startUpdateJob(boolean maakBeschikbaar) throws SchedulerException
	{
		log.info("Inside lock: triggering job");
		ResultatenHerberekenJobDataMap datamap =
			new ResultatenHerberekenJobDataMap(getResultaatstructuur(), getToetsenToUpdate(),
				maakBeschikbaar);
		EduArteApp.get().getEduarteScheduler().triggerJob(ResultatenHerberekenJob.class, datamap);
	}

	private List<Toets> getToetsenToUpdate()
	{
		List<Toets> toetsen = new ArrayList<Toets>();
		for (IModel<Toets> curToetsModel : toetsenToUpdate)
		{
			toetsen.add(curToetsModel.getObject());
		}
		return toetsen;
	}

	private Resultaatstructuur getResultaatstructuur()
	{
		return getContextModelObject();
	}

	private IChangeRecordingModel<Resultaatstructuur> getResultaatstructuurModel()
	{
		return (IChangeRecordingModel<Resultaatstructuur>) getContextModel();
	}

	public void addToetsToUpdate(Toets toets)
	{
		Iterator<IModel<Toets>> it = toetsenToUpdate.iterator();
		while (it.hasNext())
		{
			Toets curToets = it.next().getObject();
			if (curToets.equals(toets) || curToets.isAncestorOf(toets))
				return;
			if (toets.isAncestorOf(curToets))
				it.remove();
		}
		toetsenToUpdate.add(ModelFactory.getModel(toets));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(returnPage);
		for (IModel<Toets> curModel : toetsenToUpdate)
			curModel.detach();
	}

	private void saveResultaten(ResultaatVersionCollection newVersions, boolean maakBeschikbaar)
	{
		log.info("Inside lock: verifying versions");
		if (!newVersions.verifyVersions(versions))
		{
			log.info("Inside lock: version mismatch");
			returnPage.getReturnPage().error(
				"Een andere gebruiker heeft wijzigingen doorgevoerd op deze "
					+ "structuur. Voer uw wijzigingen opnieuw in a.u.b.");
			setResponsePage(returnPage.getReturnPage());
		}
		else
		{
			log.info("Inside lock: saving");
			boolean opgeslagenStructuur = getResultaatstructuur().isSaved();
			boolean herberekenen = opgeslagenStructuur && !toetsenToUpdate.isEmpty();
			getResultaatstructuur().touch();
			if (herberekenen)
			{
				getResultaatstructuur().setStatus(Status.IN_HERBEREKENING);
			}
			else if (maakBeschikbaar)
			{
				getResultaatstructuur().setStatus(Status.BESCHIKBAAR);
			}
			getResultaatstructuurModel().saveObject();
			newVersions.incrementAndSave();
			getResultaatstructuur().commit();
			try
			{
				if (herberekenen)
				{
					startUpdateJob(maakBeschikbaar);
				}
				setResponsePage(returnPage.getReturnPage());
			}
			catch (SchedulerException e)
			{
				log.error(e.toString(), e);
				error("Taak kon niet opgestart worden.");
				error(e.getLocalizedMessage());
			}
		}
	}

	private void doOpslaan(final boolean maakBeschikbaar)
	{
		if (!getResultaatstructuur().isEindResultaatCompleet())
		{
			error("Niet alle verplichte velden van het eindresultaat zijn ingevuld.");
			return;
		}

		ToetsDirectedGraph recalcGraph = ToetsDirectedGraph.createRecalcGraph(getToetsenToUpdate());
		CycleDetector<ToetsVertex, ToetsEdge> detector =
			new CycleDetector<ToetsVertex, ToetsEdge>(recalcGraph);
		if (detector.detectCycles())
		{
			Set<ToetsVertex> vertices = detector.findCycles();
			error("Er bestaat een cyclus in de herberekening voor de volgende toetsen: "
				+ StringUtil.maakCommaSeparatedString(vertices));
			return;
		}

		final ResultaatVersionCollection newVersions =
			new ResultaatVersionCollection(getResultaatstructuur());

		try
		{
			log.info("Entering resultaten mutex for " + newVersions.getLockKeys().size() + " keys");
			EduArteApp.get().getResultaatMutex().execute(newVersions, 5000, new Runnable()
			{
				@Override
				public void run()
				{
					saveResultaten(newVersions, maakBeschikbaar);
				}
			});
			log.info("Left resultaten mutex");
		}
		catch (TimeoutException e)
		{
			log.info("Could not aquire lock: timeout");
			error("De resultaten zijn in gebruik door een andere gebruiker of "
				+ "het systeem. Probeer het later nog eens.");
		}
	}
}
