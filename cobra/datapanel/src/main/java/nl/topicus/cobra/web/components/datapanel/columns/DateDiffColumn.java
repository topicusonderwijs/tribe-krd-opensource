/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import java.util.Date;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Kolom die het verschil tussen 2 datum velden toont in de gewenste tijdsaanduiding (col1
 * -col2). Deze kolom kan sorteerbaar zijn indien zelf de expressie hiervoor wordt
 * geleverd. In oracle kan dat bv op de volgende wijze: order by date_col1 - date_col2.
 * Het kan verstandig zijn om dan een index op deze functie te zetten.
 * 
 * @author marrink
 */
public class DateDiffColumn<T> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	private final Time time;

	private final String expression1;

	private final String expression2;

	public DateDiffColumn(String id, String header, String sortProperty, String expression1,
			String expression2, Time time)
	{
		super(id, header, sortProperty);
		this.expression1 = expression1;
		this.expression2 = expression2;
		this.time = time;
	}

	public DateDiffColumn(String id, String header, String expression1, String expression2,
			Time time)
	{
		super(id, header);
		this.expression1 = expression1;
		this.expression2 = expression2;
		this.time = time;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			final IModel<T> rowModel, int span)
	{
		cell.add(new Label(componentId, new DiffModel<T>(expression1, expression2, rowModel, time))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && DateDiffColumn.this.isContentsEnabled(rowModel);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && DateDiffColumn.this.isContentsVisible(rowModel);
			}
		}.setRenderBodyOnly(true));
	}

	private static final class DiffModel<T> extends LoadableDetachableModel<String>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Date> field1;

		private IModel<Date> field2;

		private Time time;

		public DiffModel(String expression1, String expression2, IModel<T> source, Time time)
		{
			super();
			this.time = time;
			field1 = new PropertyModel<Date>(source, expression1);
			field2 = new PropertyModel<Date>(source, expression2);
			getObject();
		}

		@Override
		protected String load()
		{
			Date date1 = field1.getObject();
			Date date2 = field2.getObject();
			if (date1 == null)
				date1 = new Date();
			if (date2 == null)
				date2 = new Date();
			return time.convertFromMilliseconds(date1.getTime() - date2.getTime()) + " "
				+ time.getLabel();
		}

		@Override
		protected void onDetach()
		{
			field1.detach();
			field2.detach();
		}
	}

	/**
	 * Tijdsaanduiding. Om snel te kunnen converteren tussen milliseconden en een andere
	 * eenheid.
	 * 
	 * @author marrink
	 */
	public static enum Time
	{
		/**
		 * Uren
		 */
		Hours("uur", 1000 * 60 * 60),
		/**
		 * Minuten.
		 */
		Minutes("min.", 1000 * 60),
		/**
		 * Seconden.
		 */
		Seconds("sec.", 1000),
		/**
		 * Miliseconden.
		 */
		Milliseconds("msec.", 1);
		private String label;

		private int factor;

		private Time(String label, int factor)
		{
			this.label = label;
			this.factor = factor;
		}

		public long convertFromMilliseconds(long milis)
		{
			return milis / factor;
		}

		public String getLabel()
		{
			return label;
		}
	}
}
