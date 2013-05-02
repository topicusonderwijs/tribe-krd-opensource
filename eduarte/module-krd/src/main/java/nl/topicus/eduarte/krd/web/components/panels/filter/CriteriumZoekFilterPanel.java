package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.CriteriumZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * Filterpanel voor criteria.
 * 
 * @author loite
 */
public class CriteriumZoekFilterPanel extends AutoZoekFilterPanel<CriteriumZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public CriteriumZoekFilterPanel(String id, CriteriumZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("cohort"));
	}
}
