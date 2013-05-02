package nl.topicus.eduarte.resultaten.web.pages.deelnemer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.resultaten.principals.deelnemer.DeelnemerCollectiefResultatenmatrix;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.DeelnemerSelectiePanel;
import nl.topicus.eduarte.web.components.panels.filter.DeelnemerCohortSelectieZoekFilterPanel;
import nl.topicus.eduarte.web.pages.PageContext;
import nl.topicus.eduarte.web.pages.shared.AbstractDeelnemerSelectiePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.Panel;

@PageInfo(title = "Resultaten invoeren", menu = "Deelnemer > Resultaten")
@InPrincipal(DeelnemerCollectiefResultatenmatrix.class)
public class ResultatenDeelnemerSelectiePage extends AbstractDeelnemerSelectiePage<Deelnemer>
{
	private static final long serialVersionUID = 1L;

	public ResultatenDeelnemerSelectiePage(Class< ? extends Page> returnPage, PageContext context,
			VerbintenisZoekFilter filter, DatabaseSelection<Deelnemer, Verbintenis> selection,
			SelectieTarget<Deelnemer, Verbintenis> target)
	{
		super(returnPage, context, filter, selection, target);
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

	@Override
	public int getMaxResults()
	{
		return 250;
	}

	@Override
	protected AbstractSelectiePanel<Deelnemer, Verbintenis, VerbintenisZoekFilter> createSelectiePanel(
			String id, VerbintenisZoekFilter filter, Selection<Deelnemer, Verbintenis> selection)
	{
		return new DeelnemerSelectiePanel<Deelnemer>(id, filter,
			(DatabaseSelection<Deelnemer, Verbintenis>) selection)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Panel createZoekFilterPanel(String id1, VerbintenisZoekFilter filter1,
					CustomDataPanel<Verbintenis> customDataPanel)
			{
				return new DeelnemerCohortSelectieZoekFilterPanel(id1, filter1, customDataPanel,
					false);
			}
		};
	}
}
