/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.link.CssAjaxLink;
import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.components.ISecureComponent;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeContext;

/**
 * Column die mbv een {@link CssAjaxLink} een knop (of ander klikbaar iets) toont dat mbv
 * de rechten zowel gedisabled als actief getoond kan worden.
 * 
 * @author hoeve
 */
public abstract class AjaxButtonColumn<T> extends AbstractCustomColumn<T> implements
		AjaxColumnClickRepeater<T>
{
	private static final long serialVersionUID = 1L;

	public AjaxButtonColumn(String id, String header)
	{
		super(id, header);
	}

	protected abstract String getCssEnabled();

	protected abstract String getCssDisabled();

	protected String getConfirmationText()
	{
		return null;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		ButtonPanel buttonPanel =
			new ButtonPanel(componentId, this, cell, rowModel, getCssEnabled(), getCssDisabled());
		cell.add(buttonPanel);
		cell.setOutputMarkupId(true);

		JsQuery jsq = new JsQuery(cell);
		jsq.$().chain(new Event(MouseEvent.CLICK)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public JsScope callback()
			{
				return new JsScope("event")
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void execute(JsScopeContext scopeContext)
					{
						scopeContext.append("event.stopPropagation()");
					}
				};
			}
		});
		jsq.contribute(cell);
	}

	private final class ButtonPanel extends TypedPanel<T> implements ISecureComponent
	{

		private static final long serialVersionUID = 1L;

		private CssAjaxLink<T> cssLink;

		public ButtonPanel(String id, final AjaxColumnClickRepeater<T> repeater,
				final WebMarkupContainer item, IModel<T> rowModel, String cssEnabled,
				String cssDisabled)
		{
			super(id, rowModel);
			setRenderBodyOnly(true);
			cssLink = new CssAjaxLink<T>("button", rowModel, cssEnabled, cssDisabled)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target)
				{
					repeater.onClick(item, getModel(), target);
				}

				@Override
				public boolean isEnabled()
				{
					return super.isEnabled() && AjaxButtonColumn.this.isContentsEnabled(getModel());
				}

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && AjaxButtonColumn.this.isContentsVisible(getModel());
				}

				@Override
				protected IAjaxCallDecorator getAjaxCallDecorator()
				{
					if (getConfirmationText() != null)
					{
						return new AjaxCallDecorator()
						{
							private static final long serialVersionUID = 1L;

							@Override
							public CharSequence decorateScript(CharSequence script)
							{
								AppendingStringBuffer sb = new AppendingStringBuffer();
								sb.append("var answer = confirm('");
								sb.append(getConfirmationText());
								sb.append("'); if(!answer) return false;");
								sb.append(script);
								return sb;
							}
						};
					}
					return super.getAjaxCallDecorator();
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
