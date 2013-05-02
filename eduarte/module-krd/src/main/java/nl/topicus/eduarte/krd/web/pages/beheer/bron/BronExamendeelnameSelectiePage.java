package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtWrite;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie.ExamendeelnameSelectiePanel;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectiePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;

/**
 * Selectie pagina voor Examendeelnames.
 * 
 * @author vandekamp
 */
@PageInfo(title = "Examendeelname(s) selecteren", menu = "Beheer > BRON > Verzameling maken > Examendeelnames selecteren")
@InPrincipal(BronOverzichtWrite.class)
public class BronExamendeelnameSelectiePage extends
		AbstractSelectiePage<Examendeelname, Examendeelname, ExamendeelnameZoekFilter> implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	public BronExamendeelnameSelectiePage(SecurePage returnPage,
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
