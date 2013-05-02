package nl.topicus.eduarte.krd.web.pages.shared;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensCollectief;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie.ExamendeelnameSelectiePanel;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectiePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

/**
 * Selectie pagina voor Examendeelnames.
 * 
 * @author hoeve
 */
@PageInfo(title = "Examendeelname(s) selecteren", menu = "Deelnemer > Examen > Diploma's afdrukken > [rapportage]")
@InPrincipal(DeelnemerExamensCollectief.class)
public class ExamendeelnameSelectiePage extends
		AbstractSelectiePage<Examendeelname, Examendeelname, ExamendeelnameZoekFilter> implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	public static final ExamendeelnameZoekFilter getDefaultFilter(ExamenWorkflow examenWorkflow)
	{
		ExamendeelnameZoekFilter filter = new ExamendeelnameZoekFilter();
		filter.setExamenworkflow(examenWorkflow);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		filter.addOrderByProperty("deelnemer.deelnemernummer");
		filter.addOrderByProperty("persoon.roepnaam");
		filter.addOrderByProperty("persoon.achternaam");

		return filter;
	}

	public ExamendeelnameSelectiePage(SecurePage returnPage,
			DatabaseSelection<Examendeelname, Examendeelname> selection,
			SelectieTarget<Examendeelname, Examendeelname> target, ExamenWorkflow examenWorkflow)
	{
		super(returnPage, getDefaultFilter(examenWorkflow), selection, target);
	}

	public ExamendeelnameSelectiePage(SecurePage returnPage,
			DatabaseSelection<Examendeelname, Examendeelname> selection,
			SelectieTarget<Examendeelname, Examendeelname> target, ExamendeelnameZoekFilter filter)
	{
		super(returnPage, filter, selection, target);
	}

	@Override
	protected AbstractSelectiePanel<Examendeelname, Examendeelname, ExamendeelnameZoekFilter> createSelectiePanel(
			String id, ExamendeelnameZoekFilter filter,
			Selection<Examendeelname, Examendeelname> selection)
	{
		return new ExamendeelnameSelectiePanel(id, filter,
			(DatabaseSelection<Examendeelname, Examendeelname>) selection);
	}

	@Override
	public int getMaxResults()
	{
		return 2500;
	}
}
