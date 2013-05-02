/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.deelnemer.examens;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VorigeButton;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.core.principals.deelnemer.ExamendeelnameRapportages;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.entities.rapportage.OnderwijsDocumentTemplate.ExamenDocumentTemplateType;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.krd.dao.helpers.ExamendeelnameDataAccessHelper;
import nl.topicus.eduarte.krd.web.pages.shared.ExamendeelnameSelectiePage;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;
import nl.topicus.eduarte.web.components.link.IRapportageReturnPage;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.sidebar.rapportage.RapportageSideBar;
import nl.topicus.eduarte.web.components.panels.templates.RapportagesPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.time.Duration;

/**
 * Pagina waar we datapanels van {@link DocumentTemplate}s tonen met links om een
 * rapportage te genereren.
 * 
 * @author hoeve
 */
@PageInfo(title = "Examendeelname rapportages", menu = {"Deelnemer > Examen > Diploma's afdrukken",
	"Deelnemer > Examen > Certificaten afdrukken", "Deelnemer > Examen > Cijferlijsten afdrukken"})
@InPrincipal(ExamendeelnameRapportages.class)
public class ExamendeelnameRapportagesPage extends SecurePage implements
		IRapportageReturnPage<Examendeelname, Examendeelname, ExamendeelnameZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private final SecurePage returnPage;

	private final IModel<Taxonomie> taxonomieModel;

	private final ExamenDocumentTemplateType examenDocumentType;

	public ExamendeelnameRapportagesPage(IModel<Taxonomie> taxonomieModel,
			ExamenDocumentTemplateType examenDocumentType, SecurePage returnPage)
	{
		super(CoreMainMenuItem.Deelnemer);
		this.returnPage = returnPage;
		this.taxonomieModel = taxonomieModel;
		this.examenDocumentType = examenDocumentType;
		add(new RapportagesPanel<Examendeelname, Examendeelname, ExamendeelnameZoekFilter>("panel",
			DocumentTemplateContext.Examendeelname, DocumentTemplateCategorie.Examens)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected DocumentTemplateZoekFilter getDefaultZoekFilter(
					DocumentTemplateCategorie categorie)
			{
				DocumentTemplateZoekFilter filter = super.getDefaultZoekFilter(categorie);
				filter.setTaxonomie(getTaxonomie());
				filter.setExamenDocumentType(ExamendeelnameRapportagesPage.this.examenDocumentType);
				return filter;
			}

		});

		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.ActieOverzicht);
	}

	protected Taxonomie getTaxonomie()
	{
		return taxonomieModel.getObject();
	}

	@Override
	public void rapportageInvoked()
	{
		info("De taak is op de achtergrond gestart. (Het kan enkele seconden duren voordat de voortgang in de overzichten zichtbaar is)");

		Component sideBar = this.get(ID_LAYSIDE + ":" + ID_RAPPORTAGESIDEBAR);
		if (sideBar != null && sideBar instanceof RapportageSideBar)
		{
			((RapportageSideBar) sideBar)
				.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));
		}
	}

	@Override
	public Class<ExamendeelnameDataAccessHelper> getDataAccessHelperClass()
	{
		return ExamendeelnameDataAccessHelper.class;
	}

	@Override
	public DetachableZoekFilter<Examendeelname> getDefaultFilter()
	{
		ExamendeelnameZoekFilter filter =
			ExamendeelnameSelectiePage.getDefaultFilter(getTaxonomie().getExamenWorkflow());
		filter.setTaxonomie(getTaxonomie());
		filter.setExamenworkflow(getTaxonomie().getExamenWorkflow());
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		if (filter.getExamenworkflow() != null)
		{
			if (examenDocumentType == ExamenDocumentTemplateType.Diploma)
			{
				filter.setExamenstatus(filter.getExamenworkflow().getGeslaagdStatus());
			}
			else if (examenDocumentType == ExamenDocumentTemplateType.Certificaat)
			{
				filter.setExamenstatus(filter.getExamenworkflow().getCertificatenStatus());
			}
		}
		ComponentUtil.detachQuietly(taxonomieModel);
		return filter;
	}

	@Override
	public void setResponsePage(DatabaseSelection<Examendeelname, Examendeelname> selection,
			SelectieTarget<Examendeelname, Examendeelname> target)
	{
		setResponsePage(new ExamendeelnameSelectiePage(ExamendeelnameRapportagesPage.this,
			selection, target, (ExamendeelnameZoekFilter) getDefaultFilter()));
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new VorigeButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVorige()
			{
				setResponsePage(returnPage);
			}
		});
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(taxonomieModel);
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

	@Override
	public DatabaseSelection<Examendeelname, Examendeelname> createSelection()
	{
		return new HibernateSelection<Examendeelname>(Examendeelname.class);
	}
}
