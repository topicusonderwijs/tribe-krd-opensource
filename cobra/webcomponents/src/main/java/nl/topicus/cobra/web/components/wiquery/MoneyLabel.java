package nl.topicus.cobra.web.components.wiquery;

import java.math.BigDecimal;

import nl.topicus.cobra.converters.BigDecimalConverter;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.wiquery.resources.ResourceRefUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;

public class MoneyLabel extends Label implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private Options options = new Options();

	private IConverter converter;

	private int fractionDigits = 2;

	public MoneyLabel(String id)
	{
		this(id, null);
	}

	public MoneyLabel(String id, IModel<BigDecimal> model)
	{
		this(id, model, new BigDecimalConverter(2, true));
	}

	public MoneyLabel(String id, IModel<BigDecimal> model, IConverter converter)
	{
		this(id, model, converter, 2);
	}

	public MoneyLabel(String id, IModel<BigDecimal> model, IConverter converter, int fractionDigits)
	{
		super(id, model);

		Asserts.assertNotNull("converter", converter);
		Asserts.assertNotNull("fractionDigits", fractionDigits);

		add(new AppendingAttributeModifier("class", "moneyinputfield"));
		this.converter = converter;
		this.fractionDigits = fractionDigits;
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		ResourceRefUtil.addMaskMoney(resourceManager);
	}

	@Override
	@SuppressWarnings("unchecked")
	public IConverter getConverter(Class type)
	{
		return converter;
	}

	public JsStatement statement()
	{
		options.putLiteral("decimal", ",");
		options.putLiteral("thousands", ".");
		options.putLiteral("allowZero", "true");
		options.put("precision", fractionDigits);
		return new JsQuery(this).$().chain("maskMoney", options.getJavaScriptOptions());
	}
}
