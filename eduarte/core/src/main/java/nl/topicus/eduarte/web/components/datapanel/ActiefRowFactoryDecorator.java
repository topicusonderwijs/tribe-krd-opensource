package nl.topicus.eduarte.web.components.datapanel;

import java.util.Date;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.AbstractRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.eduarte.entities.begineinddatum.IBeginEinddatumEntiteit;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * Custom class om inactieve entiteiten (tov systeem datum) een andere kleur te geven dan
 * de actieve.
 * 
 * @author hoeve
 */
public class ActiefRowFactoryDecorator<T> extends AbstractRowFactoryDecorator<T>
{
	public ActiefRowFactoryDecorator(CustomDataPanelRowFactory<T> inner)
	{
		super(inner);
	}

	private static final long serialVersionUID = 1L;

	private Date vandaag = TimeUtil.getInstance().currentDate();

	@Override
	public WebMarkupContainer createRow(String id, CustomDataPanel<T> panel, Item<T> item)
	{
		return applyActiefCssClass(super.createRow(id, panel, item), item);
	}

	/**
	 * Past een css class toe op de row wanneer de ModelObject inactief is (indien van
	 * toepassing).
	 */
	protected WebMarkupContainer applyActiefCssClass(WebMarkupContainer rowWMC, final Item<T> item)
	{
		rowWMC.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				if ((item.getModelObject() instanceof IBeginEinddatumEntiteit && !((IBeginEinddatumEntiteit) item
					.getModelObject()).isActief(vandaag))
					|| (item.getModelObject() instanceof IActiefEntiteit && !((IActiefEntiteit) item
						.getModelObject()).isActief()))
					return "inactive";
				return null;
			}
		}, " "));
		return rowWMC;
	}
}
