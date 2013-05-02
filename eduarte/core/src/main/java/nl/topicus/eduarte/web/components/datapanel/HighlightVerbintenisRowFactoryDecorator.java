package nl.topicus.eduarte.web.components.datapanel;

import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.AbstractRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;

/**
 * 
 * @author idserda
 */
public class HighlightVerbintenisRowFactoryDecorator<T> extends AbstractRowFactoryDecorator<T>
{
	public HighlightVerbintenisRowFactoryDecorator(CustomDataPanelRowFactory<T> inner)
	{
		super(inner);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public WebMarkupContainer createRow(String id, CustomDataPanel<T> panel, Item<T> item)
	{
		return applyActiefCssClass(super.createRow(id, panel, item), item);
	}

	protected WebMarkupContainer applyActiefCssClass(WebMarkupContainer rowWMC, Item<T> item)
	{
		if (item.getModelObject() instanceof Verbintenis)
			rowWMC.add(new AppendingAttributeModifier("class", "bold italic", " "));

		return rowWMC;
	}
}
