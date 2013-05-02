package nl.topicus.eduarte.krd.web.pages.deelnemer.examens;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.dao.helpers.ExamenWorkflowDataAccessHelper;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.examen.ExamenWorkflowTaxonomie;
import nl.topicus.eduarte.entities.rapportage.OnderwijsDocumentTemplate.ExamenDocumentTemplateType;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensCollectief;
import nl.topicus.eduarte.krd.web.components.panels.examen.ExamenWorkflowPanel;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.correctiestaat.CorrectiestaatGenererenPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.procesverbaal.ProcesverbaalGenererenPage;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.link.PageLinkPanel;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

/**
 * Pagina waarop acties uitgevoerd kunnen worden mbt examens
 * 
 * @author vandekamp
 * @author hoeve
 */
@PageInfo(title = "Examens Actie overzicht", menu = "Deelnemer -> Examens -> Actie overzicht")
@InPrincipal(DeelnemerExamensCollectief.class)
public class DeelnemerKwalificatiePage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	public DeelnemerKwalificatiePage()
	{
		this(null);
	}

	public DeelnemerKwalificatiePage(String info)
	{
		super(CoreMainMenuItem.Deelnemer);
		if (info != null && !info.isEmpty())
			info(info);
		List<ExamenWorkflow> alleExamenWorkflows = getExamenWorkflows();
		int splitListOn = alleExamenWorkflows.size() / 2;
		add(createExamenWorkflows("examenWorkflowsLeft", alleExamenWorkflows, 0, splitListOn));
		add(createExamenWorkflows("examenWorkflowsRight", alleExamenWorkflows, splitListOn,
			alleExamenWorkflows.size()));

		createComponents();
	}

	private RepeatingView createExamenWorkflows(String id,
			List<ExamenWorkflow> alleExamenWorkflows, int startList, int endList)
	{
		List<ExamenWorkflow> examenWorkflows =
			new ArrayList<ExamenWorkflow>(alleExamenWorkflows.subList(startList, endList));
		RepeatingView repeatingView = new RepeatingView(id);
		for (ExamenWorkflow examenWorkflow : examenWorkflows)
		{
			repeatingView.add(new ExamenWorkflowPanel(repeatingView.newChildId(), examenWorkflow)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List< ? extends Panel> getExtraPanels(
						@SuppressWarnings("hiding") String id)
				{
					List<Panel> panels = new ArrayList<Panel>();

					for (ExamenWorkflowTaxonomie workflowTaxonomie : getExamenWorkflow()
						.getExamenWorflowTaxonomieen())
					{
						panels.add(new PageLinkPanel(id, new TaxonomiePageLink(ModelFactory
							.getModel(workflowTaxonomie.getTaxonomie()),
							ExamenDocumentTemplateType.Diploma), workflowTaxonomie.getTaxonomie()
							.getAfkorting()
							+ " diploma's afdrukken"));
						panels.add(new PageLinkPanel(id, new TaxonomiePageLink(ModelFactory
							.getModel(workflowTaxonomie.getTaxonomie()),
							ExamenDocumentTemplateType.Cijferlijst), workflowTaxonomie
							.getTaxonomie().getAfkorting()
							+ " cijferlijsten afdrukken"));
						panels.add(new PageLinkPanel(id, new TaxonomiePageLink(ModelFactory
							.getModel(workflowTaxonomie.getTaxonomie()),
							ExamenDocumentTemplateType.Certificaat), workflowTaxonomie
							.getTaxonomie().getAfkorting()
							+ " certificaten afdrukken"));
						if (workflowTaxonomie.getTaxonomie().isVO())
						{
							panels.add(new PageLinkPanel(id, getProcesverbaalPageLink(),
								"VO Procesverbaal genereren"));
							panels.add(new PageLinkPanel(id, getCorrectiestaatPageLink(),
								"VO Correctiestaat genereren"));
						}
					}

					return panels;
				}

			});
		}
		return repeatingView;
	}

	protected IPageLink getProcesverbaalPageLink()
	{
		return new IPageLink()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return ProcesverbaalGenererenPage.class;
			}

			@Override
			public Page getPage()
			{
				return new ProcesverbaalGenererenPage();
			}
		};
	}

	protected IPageLink getCorrectiestaatPageLink()
	{
		return new IPageLink()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return CorrectiestaatGenererenPage.class;
			}

			@Override
			public Page getPage()
			{
				return new CorrectiestaatGenererenPage();
			}
		};
	}

	private List<ExamenWorkflow> getExamenWorkflows()
	{
		ExamenWorkflowDataAccessHelper helper =
			DataAccessRegistry.getHelper(ExamenWorkflowDataAccessHelper.class);
		List<ExamenWorkflow> examenWorkflows = helper.list();
		return examenWorkflows;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.ActieOverzicht);
	}

	private class TaxonomiePageLink implements IPageLink, IDetachable
	{
		private static final long serialVersionUID = 1L;

		private final IModel<Taxonomie> taxonomieModel;

		private final ExamenDocumentTemplateType examenDocumentType;

		public TaxonomiePageLink(IModel<Taxonomie> taxonomieModel,
				ExamenDocumentTemplateType examenDocumentType)
		{
			this.taxonomieModel = taxonomieModel;
			this.examenDocumentType = examenDocumentType;
		}

		@Override
		public Page getPage()
		{
			return new ExamendeelnameRapportagesPage(getTaxonomieModel(), examenDocumentType,
				DeelnemerKwalificatiePage.this);
		}

		@Override
		public Class< ? extends Page> getPageIdentity()
		{
			return ExamendeelnameRapportagesPage.class;
		}

		public IModel<Taxonomie> getTaxonomieModel()
		{
			return taxonomieModel;
		}

		@Override
		public void detach()
		{
			ComponentUtil.detachQuietly(taxonomieModel);
		}

	}
}
