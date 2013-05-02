/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Voor het afdrukken van een {@link BigDecimal} als x.xxx,xx %.
 * 
 * @author hoeve
 */
public class PercentageColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	private RoundingMode roundingmode;

	public PercentageColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	public PercentageColumn(String id, String header, String sortProperty, String propertyExpression)
	{
		super(id, header, sortProperty, propertyExpression);
	}

	public RoundingMode getRoundingmode()
	{
		return roundingmode;
	}

	public PercentageColumn<T> setRoundingmode(RoundingMode roundingmode)
	{
		this.roundingmode = roundingmode;

		return this;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			final IModel<T> rowModel, int span)
	{
		IModel<BigDecimal> mod = new PropertyModel<BigDecimal>(rowModel, getPropertyExpression());

		BigDecimal bigDecimal = mod.getObject();
		String percentage = "";
		if (bigDecimal != null)
		{
			NumberFormat format = NumberFormat.getPercentInstance(new Locale("nl", "NL"));
			if (getRoundingmode() != null)
				format.setRoundingMode(getRoundingmode());
			percentage = format.format(bigDecimal);
		}
		cell.add(new Label(componentId, new Model<String>(percentage))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && PercentageColumn.this.isContentsEnabled(rowModel);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && PercentageColumn.this.isContentsVisible(rowModel);
			}
		});
	}
}
