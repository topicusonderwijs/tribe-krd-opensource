package nl.topicus.eduarte.web.pages.deelnemer.verbintenis;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.DeelnemerSelectiePanel;
import nl.topicus.eduarte.web.components.panels.filter.DeelnemerSelectieZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractDeelnemerSelectiePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;

public abstract class AbstractVerbintenisSelectiePage extends
		AbstractDeelnemerSelectiePage<Verbintenis> implements IEditPage
{
	private static final long serialVersionUID = 1L;

	public AbstractVerbintenisSelectiePage(SecurePage returnPage, VerbintenisZoekFilter filter,
			AbstractSelectieTarget<Verbintenis, Verbintenis> target)
	{
		super(returnPage, filter, new HibernateSelection<Verbintenis>(Verbintenis.class), target);
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	protected AbstractSelectiePanel<Verbintenis, Verbintenis, VerbintenisZoekFilter> createSelectiePanel(
			String id, VerbintenisZoekFilter filter, Selection<Verbintenis, Verbintenis> selection)
	{
		return new DeelnemerSelectiePanel<Verbintenis>(id, filter,
			(DatabaseSelection<Verbintenis, Verbintenis>) selection)
		{
			private static final long serialVersionUID = 1L;

			@Override
			@SuppressWarnings("hiding")
			protected Panel createZoekFilterPanel(String id, VerbintenisZoekFilter filter,
					CustomDataPanel<Verbintenis> customDataPanel)
			{
				return new DeelnemerSelectieZoekFilterPanel(id, filter, customDataPanel);
			}
		};
	}

}