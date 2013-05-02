package nl.topicus.cobra.web.components.navigation.paging;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;

public class CustomPagingNavigation extends AjaxPagingNavigation
{
	private static final long serialVersionUID = 1L;

	public CustomPagingNavigation(String id, IPageable pageable, IPagingLabelProvider labelProvider)
	{
		super(id, pageable, labelProvider);
	}

	public int getStart()
	{
		return getStartIndex();
	}
}
