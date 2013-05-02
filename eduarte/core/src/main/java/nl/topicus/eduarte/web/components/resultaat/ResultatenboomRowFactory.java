package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;

public class ResultatenboomRowFactory extends CustomDataPanelRowFactory<Toets>
{
	private static final long serialVersionUID = 1L;

	private ToetsZoekFilter toetsFilter;

	public ResultatenboomRowFactory(ToetsZoekFilter toetsFilter)
	{
		this.toetsFilter = toetsFilter;
	}

	@Override
	public WebMarkupContainer createRow(String id, CustomDataPanel<Toets> panel,
			final Item<Toets> item)
	{
		return new WebMarkupContainer(id)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && isRowVisible(item.getModelObject());
			}
		};
	}

	protected boolean isRowVisible(Toets toets)
	{
		return toetsFilter.getToetsCodeFilter() == null
			|| toetsFilter.getToetsCodeFilter().getToetsCodesAsSet().contains(toets.getCode());
	}

	@Override
	public void detach()
	{
		super.detach();
		toetsFilter.detach();
	}
}
