package nl.topicus.eduarte.web.components.panels.filter.vasco;

import java.util.Arrays;

import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.vasco.TokenZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class TokenZoekFilterPanel extends AutoZoekFilterPanel<TokenZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public TokenZoekFilterPanel(String id, TokenZoekFilter zoekfilter, IPageable pageable)
	{
		super(id, zoekfilter, pageable);
		setPropertyNames(Arrays.asList("serienummer", "status", "account"));
	}
}
