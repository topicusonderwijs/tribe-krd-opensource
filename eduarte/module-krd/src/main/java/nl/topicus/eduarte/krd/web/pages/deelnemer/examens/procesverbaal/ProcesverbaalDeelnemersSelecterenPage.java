package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.procesverbaal;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensCollectief;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.ExamennummerColumn;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.DeelnemerCohortSelectiePanel;
import nl.topicus.eduarte.web.pages.shared.AbstractDeelnemerSelectiePage;
import nl.topicus.eduarte.web.pages.shared.DeelnemerCollectiefPageContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

/**
 * Selectie pagina voor Deelnemers.
 * 
 * @author vandekamp
 */
@PageInfo(title = "Deelnemer(s) selecteren", menu = "Deelnemer -> Examen -> Acties-overzicht -> VO Procesverbaal genereren")
@InPrincipal(DeelnemerExamensCollectief.class)
public class ProcesverbaalDeelnemersSelecterenPage extends
		AbstractDeelnemerSelectiePage<Verbintenis> implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private static final VerbintenisZoekFilter getDefaultFilter(Procesverbaal procesverbaal)
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		filter.setAfgenomenOnderwijsproduct(procesverbaal.getOnderwijsproduct());
		filter.setOrganisatieEenheid(procesverbaal.getOrganisatieEenheid());
		filter.setOpleiding(procesverbaal.getOpleiding());
		filter.addOrderByProperty("deelnemer.deelnemernummer");
		filter.addOrderByProperty("persoon.roepnaam");
		filter.addOrderByProperty("persoon.achternaam");
		return filter;
	}

	public ProcesverbaalDeelnemersSelecterenPage(Procesverbaal procesverbaal)
	{
		this(getDefaultFilter(procesverbaal), procesverbaal);
	}

	private ProcesverbaalDeelnemersSelecterenPage(VerbintenisZoekFilter filter,
			Procesverbaal procesverbaal)
	{
		super(ProcesverbaalGenererenPage.class, new DeelnemerCollectiefPageContext(
			"Procesverbaal deelnemers selecteren", DeelnemerCollectiefMenuItem.ActieOverzicht),
			filter, new HibernateSelection<Verbintenis>(Verbintenis.class),
			new ProcesverbaalSelecterenTarget(procesverbaal));
	}

	@Override
	protected AbstractSelectiePanel<Verbintenis, Verbintenis, VerbintenisZoekFilter> createSelectiePanel(
			String id, VerbintenisZoekFilter filter, Selection<Verbintenis, Verbintenis> selection)
	{
		DeelnemerCohortSelectiePanel<Verbintenis> ret =
			new DeelnemerCohortSelectiePanel<Verbintenis>(id, filter,
				(DatabaseSelection<Verbintenis, Verbintenis>) selection, false);
		ret.getContentDescription().addColumn(
			new ExamennummerColumn("Examennummer", "Examennummer"));
		return ret;
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}
}
