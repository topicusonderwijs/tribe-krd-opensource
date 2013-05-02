/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.link.CssLink;
import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.components.ISecureComponent;

/**
 * Column die mbv een {@link CssLink} een knop (of ander klikbaar iets) toont dat mbv de
 * rechten zowel gedisabled als actief getoond kan worden.
 * 
 * @author marrink
 */
public abstract class ButtonColumn<T> extends AbstractCustomColumn<T> implements
		ColumnClickRepeater<T>
{
	private static final long serialVersionUID = 1L;

	private String cssEnabled;

	private String cssDisabled;

	public ButtonColumn(String id, String header, String cssEnabled, String cssDisabled)
	{
		super(id, header);
		this.cssEnabled = cssEnabled;
		this.cssDisabled = cssDisabled;
	}

	public ButtonColumn(String id, String header)
	{
		super(id, header);
	}

	protected String getCssEnabled()
	{
		return cssEnabled;
	}

	protected String getCssDisabled()
	{
		return cssDisabled;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		cell.add(createButtonPanel(cell, componentId, rowModel));
	}

	protected Panel createButtonPanel(WebMarkupContainer cell, String componentId,
			IModel<T> rowModel)
	{
		return new ButtonPanel(componentId, this, cell, rowModel, getCssEnabled(), getCssDisabled());
	}

	private final class ButtonPanel extends TypedPanel<T> implements ISecureComponent
	{

		private static final long serialVersionUID = 1L;

		private CssLink<T> cssLink;

		public ButtonPanel(String id, final ColumnClickRepeater<T> repeater,
				final WebMarkupContainer item, IModel<T> rowModel, String cssEnabled,
				String cssDisabled)
		{
			super(id, rowModel);
			setRenderBodyOnly(true);
			cssLink = new CssLink<T>("button", rowModel, cssEnabled, cssDisabled)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick()
				{
					repeater.onClick(item, getModel());
				}

				@Override
				public boolean isEnabled()
				{
					return super.isEnabled() && ButtonColumn.this.isContentsEnabled(getModel());
				}

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && ButtonColumn.this.isContentsVisible(getModel());
				}
			};
			add(cssLink);
		}

		@Override
		public ISecurityCheck getSecurityCheck()
		{
			return cssLink.getSecurityCheck();
		}

		@Override
		public boolean isActionAuthorized(String waspAction)
		{
			return cssLink.isActionAuthorized(waspAction);
		}

		@Override
		public boolean isActionAuthorized(WaspAction action)
		{
			return cssLink.isActionAuthorized(action);
		}

		@Override
		public boolean isAuthenticated()
		{
			return cssLink.isAuthenticated();
		}

		@Override
		public void setSecurityCheck(ISecurityCheck check)
		{
			cssLink.setSecurityCheck(check);
		}
	}
}
