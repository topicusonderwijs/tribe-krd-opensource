package nl.topicus.cobra.web.components.datapanel.columns;

import java.math.BigDecimal;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.wiquery.MoneyInputField;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * CustomPropertyColumn die de waarde niet in een label toont, maar in een MoneyInputField
 * zodat de gebruiker deze waarde kan aanpassen. Opslaan dient nog door de pagina geregeld
 * te worden.
 * 
 * @author hoeve
 */
public class CustomPropertyCurrencyFieldColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	private boolean required = false;

	public CustomPropertyCurrencyFieldColumn(String id, String header, String sortProperty,
			String propertyExpression)
	{
		super(id, header, sortProperty, propertyExpression);
	}

	public CustomPropertyCurrencyFieldColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		CurrencyPanel panel = new CurrencyPanel(componentId);
		panel.add(new CustomMoneyInputField("currencyField", rowModel, createLabelModel(rowModel))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled()
					&& CustomPropertyCurrencyFieldColumn.this.isContentsEnabled(rowModel);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible()
					&& CustomPropertyCurrencyFieldColumn.this.isContentsVisible(rowModel);
			}

		}.setRequired(required));
		cell.add(panel);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IModel<BigDecimal> createLabelModel(IModel<T> rowModel)
	{
		return (IModel<BigDecimal>) super.createLabelModel(rowModel);
	}

	public boolean isRequired()
	{
		return required;
	}

	public CustomPropertyCurrencyFieldColumn<T> setRequired(boolean required)
	{
		this.required = required;
		return this;
	}

	private class CustomMoneyInputField extends MoneyInputField
	{
		private static final long serialVersionUID = 1L;

		protected IModel<T> rowModel;

		public CustomMoneyInputField(String id, IModel<T> rowModel, IModel<BigDecimal> model)
		{
			super(id, model);
			this.rowModel = rowModel;
		}

		@Override
		public void detachModels()
		{
			super.detachModels();

			ComponentUtil.detachQuietly(rowModel);
		}
	}

	private class CurrencyPanel extends Panel
	{
		private static final long serialVersionUID = 1L;

		private CurrencyPanel(String id)
		{
			super(id);
			setRenderBodyOnly(true);
		}
	}
}
