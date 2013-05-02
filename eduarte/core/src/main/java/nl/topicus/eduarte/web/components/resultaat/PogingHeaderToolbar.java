package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.CustomColumn;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelToolbar;
import nl.topicus.cobra.web.components.datapanel.columns.HideableColumn;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

public class PogingHeaderToolbar<T> extends CustomDataPanelToolbar<T>
{
	private static final long serialVersionUID = 1L;

	public PogingHeaderToolbar(final CustomDataPanel<T> datapanel)
	{
		super(datapanel.getModel());

		ListView<CustomColumn<T>> headers =
			new ListView<CustomColumn<T>>("headers", datapanel.getModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<CustomColumn<T>> item)
				{
					if (item.getModelObject() instanceof ResultaatColumn< ? >)
					{
						ResultaatColumn< ? > column =
							((ResultaatColumn< ? >) item.getModelObject());
						item.add(new Label("label", getPogingString(column.getPoging())));
						item.add(new AppendingAttributeModifier("class", column.getCssClass()));
					}
					else
						item.add(new WebMarkupContainer("label"));
				}

				private String getPogingString(int pogingNummer)
				{
					switch (pogingNummer)
					{
						case ResultatenModel.ALTERNATIEF_NR:
							return "Alt.";
						case ResultatenModel.RESULTAAT_NR:
							return "Res.";
						default:
							return "Pog. " + pogingNummer;
					}
				}

				@Override
				public boolean getReuseItems()
				{
					return datapanel.getReuseItems();
				}

				@Override
				protected ListItem<CustomColumn<T>> newItem(int index)
				{
					return new ListItem<CustomColumn<T>>(index, getListItemModel(getModel(), index))
					{
						private static final long serialVersionUID = 1L;

						@Override
						public boolean isVisible()
						{
							Object column = getModelObject();
							if (column instanceof HideableColumn< ? >)
								return super.isVisible()
									&& ((HideableColumn< ? >) column).isColumnVisible();
							return super.isVisible();
						}
					};
				}
			};
		headers.setVersioned(false);
		add(headers);
	}
}
