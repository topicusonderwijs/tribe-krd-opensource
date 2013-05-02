package nl.topicus.eduarte.web.components.panels.signalen;

import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.AbstractRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.eduarte.entities.signalering.Signaal;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;

public class OngelezenRowFactoryDecorator extends AbstractRowFactoryDecorator<Signaal>
{
	public OngelezenRowFactoryDecorator(CustomDataPanelRowFactory<Signaal> inner)
	{
		super(inner);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public WebMarkupContainer createRow(String id, CustomDataPanel<Signaal> panel,
			Item<Signaal> item)
	{
		return applyActiefCssClass(super.createRow(id, panel, item), item);
	}

	protected WebMarkupContainer applyActiefCssClass(WebMarkupContainer rowWMC, Item<Signaal> item)
	{
		if ((item.getModelObject()).getDatumGelezen() == null)
			rowWMC.add(new AppendingAttributeModifier("class", "bold", " "));
		return rowWMC;
	}
}
