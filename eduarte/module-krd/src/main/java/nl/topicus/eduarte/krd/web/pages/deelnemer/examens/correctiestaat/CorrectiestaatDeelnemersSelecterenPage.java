package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.correctiestaat;

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
@PageInfo(title = "Deelnemer(s) selecteren", menu = "Deelnemer -> Examen -> Acties-overzicht -> VO Correctiestaat genereren")
@InPrincipal(DeelnemerExamensCollectief.class)
public class CorrectiestaatDeelnemersSelecterenPage extends
		AbstractDeelnemerSelectiePage<Verbintenis> implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private static final VerbintenisZoekFilter getDefaultFilter(Correctiestaat correctiestaat)
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		filter.setAfgenomenOnderwijsproduct(correctiestaat.getOnderwijsproduct());
		filter.setExamendeelnameTijdvakGelijkAan(correctiestaat.getTijdvak());
		filter.addOrderByProperty("deelnemer.deelnemernummer");
		filter.addOrderByProperty("persoon.roepnaam");
		filter.addOrderByProperty("persoon.achternaam");
		return filter;
	}

	public CorrectiestaatDeelnemersSelecterenPage(Correctiestaat correctiestaat)
	{
		this(getDefaultFilter(correctiestaat), correctiestaat);
	}

	private CorrectiestaatDeelnemersSelecterenPage(VerbintenisZoekFilter filter,
			Correctiestaat correctiestaat)
	{
		super(CorrectiestaatGenererenPage.class, new DeelnemerCollectiefPageContext(
			"Correctiestaat deelnemers selecteren", DeelnemerCollectiefMenuItem.ActieOverzicht),
			filter, new HibernateSelection<Verbintenis>(Verbintenis.class),
			new CorrectiestaatSelecterenTarget(correctiestaat));
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
