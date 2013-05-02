package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingInrichtingExporteren;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractOpleidingSelectiePage;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

/**
 * @author papegaaij
 */
@PageInfo(title = "Opleidingsinrichting exporteren", menu = "Onderwijs > Opleiding > Inrichting exporteren")
@InPrincipal(OpleidingInrichtingExporteren.class)
public class OpleidingInrichtingExporterenSelectiePage extends AbstractOpleidingSelectiePage
{
	private static final long serialVersionUID = 1L;

	public OpleidingInrichtingExporterenSelectiePage(SecurePage returnPage)
	{
		super(returnPage, OpleidingZoekFilter.createDefaultFilter(),
			new HibernateSelection<Opleiding>(Opleiding.class),
			new OpleidingInrichtingExporterenTarget());
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
}
