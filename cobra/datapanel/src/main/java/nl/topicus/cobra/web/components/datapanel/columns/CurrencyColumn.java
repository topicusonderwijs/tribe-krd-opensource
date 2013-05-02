/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Voor het afdrukken van een {@link BigDecimal} als een € x.xxx,xx bedrag. Gebruik
 * {@link CurrencyColumn#setColorize(boolean)} om negatieve bedragen rood te kleuren.
 * 
 * @author hop
 */
public class CurrencyColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	private boolean colorize = false;

	public CurrencyColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	public CurrencyColumn(String id, String header, String sortProperty, String propertyExpression)
	{
		super(id, header, sortProperty, propertyExpression);
	}

	protected BigDecimal getModelObject(IModel<T> rowModel, String propertyExpression)
	{
		return new PropertyModel<BigDecimal>(rowModel, propertyExpression).getObject();
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			final IModel<T> rowModel, int span)
	{
		BigDecimal bigDecimal = getModelObject(rowModel, getPropertyExpression());
		String currency = "";
		if (bigDecimal != null)
		{
			NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("nl", "NL"));
			currency = format.format(bigDecimal).replace(" ", "&nbsp;");
		}

		Component label;
		cell.add(label = new Label(componentId, new Model<String>(currency))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && CurrencyColumn.this.isContentsEnabled(rowModel);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && CurrencyColumn.this.isContentsVisible(rowModel);
			}
		}.setRenderBodyOnly(true).setEscapeModelStrings(false));

		if (bigDecimal != null && bigDecimal.longValue() < 0L && isColorize())
			label.add(new AttributeModifier("style", true, new Model<String>("color: red;")));
	}

	public boolean isColorize()
	{
		return colorize;
	}

	/**
	 * @param colorize
	 *            true -> kleur negatieve cijfers rood.
	 */
	public CurrencyColumn<T> setColorize(boolean colorize)
	{
		this.colorize = colorize;

		return this;
	}
}
