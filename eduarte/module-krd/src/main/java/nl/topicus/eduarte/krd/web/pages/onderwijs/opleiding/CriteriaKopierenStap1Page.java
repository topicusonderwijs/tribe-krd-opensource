package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingCriteriaKopieren;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie.CriteriumSelectiePanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectiePage;
import nl.topicus.eduarte.zoekfilters.CriteriumZoekFilter;

/**
 * @author loite
 */
@PageInfo(title = "Criteria kopiëren", menu = "Onderwijs > Kopiëren criteria")
@InPrincipal(OpleidingCriteriaKopieren.class)
public class CriteriaKopierenStap1Page extends
		AbstractSelectiePage<Criterium, Criterium, CriteriumZoekFilter> implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private final static CriteriumZoekFilter getDefaultFilter(Opleiding bronOpleiding)
	{
		CriteriumZoekFilter ret =
			new CriteriumZoekFilter(bronOpleiding, EduArteSession.get().getSelectedCohortModel());
		ret.setLandelijk(false);
		return ret;
	}

	public CriteriaKopierenStap1Page(Opleiding bronOpleiding, SecurePage returnPage)
	{
		this(returnPage, getDefaultFilter(bronOpleiding));
	}

	private CriteriaKopierenStap1Page(SecurePage returnPage, CriteriumZoekFilter filter)
	{
		super(returnPage, filter, new HibernateSelection<Criterium>(Criterium.class),
			new CriteriaKopierenStap1Target());
	}

	@Override
	protected AbstractSelectiePanel<Criterium, Criterium, CriteriumZoekFilter> createSelectiePanel(
			String id, CriteriumZoekFilter filter, Selection<Criterium, Criterium> selection)
	{
		return new CriteriumSelectiePanel(id, filter,
			(DatabaseSelection<Criterium, Criterium>) selection);
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}
}
