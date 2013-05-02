/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.navigation.paging;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;

/**
 * @author hop
 */
public class CustomPagingNavigator extends AjaxPagingNavigator
{
	private static final long serialVersionUID = 1L;

	private final Component title;

	public CustomPagingNavigator(String id, IPageable pageable, Component title)
	{
		super(id, pageable);
		this.title = title;
	}

	public CustomPagingNavigator(String id, IPageable pageable, IPagingLabelProvider labelProvider,
			Component title)
	{
		super(id, pageable, labelProvider);
		this.title = title;
	}

	@Override
	protected PagingNavigation newNavigation(IPageable pageable, IPagingLabelProvider labelProvider)
	{
		return new CustomPagingNavigation("navigation", pageable, labelProvider)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && CustomPagingNavigator.this.isEnabled()
					&& CustomPagingNavigator.this.isEnableAllowed();
			}
		};
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		CustomPagingNavigation pagingNavigation = (CustomPagingNavigation) getPagingNavigation();

		WebMarkupContainer dotsFirst = (WebMarkupContainer) get("dotsFirst");
		if (dotsFirst == null)
		{
			dotsFirst = new WebMarkupContainer("dotsFirst");
			add(dotsFirst);
		}

		Link< ? > firstLink = (Link< ? >) get("first");
		if (firstLink != null)
		{
			boolean visible = pagingNavigation.getStart() > 0;
			firstLink.setVisible(visible);
			dotsFirst.setVisible(visible);
		}

		WebMarkupContainer dotsLast = (WebMarkupContainer) get("dotsLast");
		if (dotsLast == null)
		{
			dotsLast = new WebMarkupContainer("dotsLast");
			add(dotsLast);
		}

		Link< ? > lastLink = (Link< ? >) get("last");
		if (lastLink != null)
		{
			boolean visible =
				pagingNavigation.getStart() + pagingNavigation.getViewSize() < getPageable()
					.getPageCount();
			lastLink.setVisible(visible);
			dotsLast.setVisible(visible);
		}
	}

	@Override
	protected Link< ? > newPagingNavigationLink(String id, IPageable pageable, int pageNumber)
	{
		Link< ? > ret = super.newPagingNavigationLink(id, pageable, pageNumber);
		if (id.equals("last"))
		{
			ret.add(new Label("pageNumber", Integer.toString(pageable.getPageCount())));
		}
		return ret;
	}

	/**
	 * @see org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator#onAjaxEvent(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onAjaxEvent(AjaxRequestTarget target)
	{
		super.onAjaxEvent(target);
		if (title != null)
		{
			target.addComponent(title);
		}
		onNavigate();
	}

	protected void onNavigate()
	{

	}
}
