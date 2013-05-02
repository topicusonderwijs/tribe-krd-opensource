/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.ComponentFactory;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

/**
 * @author vandekamp
 */
public abstract class AjaxLinkColumn<T> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	private final String tekst;

	private final String tooltip;

	private String cssClass;

	public AjaxLinkColumn(String id, String header, String tekst, String tooltip, String cssClass)
	{
		super(id, header);
		this.tekst = tekst;
		this.tooltip = tooltip;
		this.cssClass = cssClass;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		cell.add(new LinkPanel(componentId, rowModel));
	}

	protected abstract void onClick(IModel<T> rowModel, AjaxRequestTarget target);

	@SuppressWarnings("unused")
	protected String getCssClass(IModel<T> rowModel)
	{
		return cssClass;
	}

	protected void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}

	private final class LinkPanel extends Panel
	{
		private static final long serialVersionUID = 1L;

		public LinkPanel(String id, final IModel<T> rowModel)
		{
			super(id);
			setRenderBodyOnly(true);
			AjaxLink<T> link = new AjaxLink<T>("link", rowModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target)
				{
					target.addComponent(this);
					AjaxLinkColumn.this.onClick(getModel(), target);
				}

				@Override
				public boolean isEnabled()
				{
					return super.isEnabled() && AjaxLinkColumn.this.isContentsEnabled(getModel());
				}

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && AjaxLinkColumn.this.isContentsVisible(getModel());
				}
			};
			link.add(ComponentFactory.getDataLabel("label", tekst));
			link.add(new AttributeModifier("title", true, new Model<String>(tooltip)));
			link.add(new AttributeModifier("class", true, new LoadableDetachableModel<String>()
			{

				private static final long serialVersionUID = 1L;

				@Override
				protected String load()
				{
					return getCssClass(rowModel);
				}
			}));
			add(link);
			link.setOutputMarkupId(true);
		}

	}
}
