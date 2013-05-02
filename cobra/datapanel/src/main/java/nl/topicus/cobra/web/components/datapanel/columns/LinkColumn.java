/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.link.SecureLink;
import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author loite
 */
public abstract class LinkColumn<T> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	private final String tekst;

	private final String tooltip;

	private final String cssClass;

	private final boolean askConfirmation;

	public LinkColumn(String id, String header, String tekst, String tooltip, String cssClass)
	{
		this(id, header, tekst, tooltip, cssClass, false);
	}

	/**
	 * @param askConfirmation
	 *            Geeft aan of er dmv een javascript popupje om bevestiging gevraag moet
	 *            worden. De tekst van deze popup is aan te passen door de methode
	 */
	public LinkColumn(String id, String header, String tekst, String tooltip, String cssClass,
			boolean askConfirmation)
	{
		super(id, header);
		this.tekst = tekst;
		this.tooltip = tooltip;
		this.cssClass = cssClass;
		this.askConfirmation = askConfirmation;
	}

	/**
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator#populateItem(org.apache.wicket.markup.repeater.Item,
	 *      java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		cell.add(new LinkPanel(componentId, rowModel));
	}

	/**
	 * Methode die aangeroepen wordt bij het klikken op de link.
	 * 
	 * @param rowModel
	 */
	protected abstract void onClick(IModel<T> rowModel);

	/**
	 * Tekst die in een confirmation-dialog getoond word als er om bevestiging gevraagd
	 * moet worden. Override om je eigen tekst te tonen.
	 */
	protected String getBevestigingsTekst()
	{
		return "Weet u het zeker?";
	}

	/**
	 * Geeft de CSS class van deze link. Standaard voor alle rows hetzelfde. Override om
	 * een custom class mee te geven indien deze afhankelijk is van je row model.
	 */
	@SuppressWarnings("unused")
	protected String getCssClass(IModel<T> rowModel)
	{
		return cssClass;
	}

	private final class LinkPanel extends TypedPanel<T>
	{
		private static final long serialVersionUID = 1L;

		public LinkPanel(String id, IModel<T> rowModel)
		{
			super(id, rowModel);
			SecureLink<T> link = new SecureLink<T>("link", rowModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick()
				{
					LinkColumn.this.onClick(getModel());
				}

				@Override
				protected CharSequence getOnClickScript(CharSequence url)
				{
					if (askConfirmation)
					{
						StringBuffer buffer = new StringBuffer(80);
						buffer.append("return confirm('" + getBevestigingsTekst() + "');");
						return buffer;
					}
					return super.getOnClickScript(url);
				}

				@Override
				public boolean isEnabled()
				{
					return super.isEnabled() && LinkColumn.this.isContentsEnabled(getModel());
				}

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && LinkColumn.this.isContentsVisible(getModel());
				}
			};
			link.add(ComponentFactory.getDataLabel("label", getLabelTekst(rowModel)));
			link.add(new AttributeModifier("title", true, new Model<String>(tooltip)));
			link.add(new AttributeModifier("class", true,
				new Model<String>(getCssClass(getModel()))));
			setSecurityCheck(link, getModel());
			add(link);
		}
	}

	@SuppressWarnings("unused")
	protected void setSecurityCheck(SecureLink<T> link, IModel<T> rowModel)
	{
	}

	@SuppressWarnings("unused")
	protected String getLabelTekst(IModel<T> rowModel)
	{
		return tekst;
	}

}
