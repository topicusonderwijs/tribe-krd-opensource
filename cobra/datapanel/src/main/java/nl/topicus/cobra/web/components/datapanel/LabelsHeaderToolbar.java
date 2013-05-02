package nl.topicus.cobra.web.components.datapanel;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.columns.ExportHeaderColumn;
import nl.topicus.cobra.web.components.datapanel.columns.HideableColumn;
import nl.topicus.cobra.web.components.datapanel.columns.ISpanningColumn;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.sort.AjaxFallbackOrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

public class LabelsHeaderToolbar<T> extends CustomDataPanelToolbar<T>
{
	private static final long serialVersionUID = 1L;

	public LabelsHeaderToolbar(final CustomDataPanel<T> datapanel,
			final ConcatModel exportHeaderModel)
	{
		super(datapanel.getModel());

		ListView<CustomColumn<T>> headers = new ListView<CustomColumn<T>>("headers", getModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<CustomColumn<T>> item)
			{
				if (item.getIndex() == 0)
				{
					exportHeaderModel.clear();
					if (datapanel.getCSVTitle() != null)
						exportHeaderModel.append(datapanel.getCSVTitle() + "\r\n");
				}
				CustomColumn<T> column = (item.getModelObject());
				MarkupContainer wrapper;
				if (datapanel.isSortable() && column.isSortable())

					wrapper =
						new AjaxFallbackOrderByBorder("wrapper", column.getSortProperty(),
							(ISortStateLocator) datapanel.getDataProvider())
						{
							private static final long serialVersionUID = 1L;

							@Override
							protected void onSortChanged()
							{
								datapanel.setCurrentPage(0);
							}

							@Override
							protected void onAjaxClick(AjaxRequestTarget target)
							{
								target.addComponent(datapanel);
							}
						};
				else
					wrapper = new WebMarkupContainer("wrapper");
				if (column instanceof IStyledColumn< ? >)
				{
					IStyledColumn< ? > styledColumn = ((IStyledColumn< ? >) column);
					if (StringUtil.isNotEmpty(styledColumn.getCssClass()))
						wrapper.add(new AppendingAttributeModifier("class", styledColumn
							.getCssClass()));

				}
				Component header = column.getHeader(wrapper, "colHead");
				boolean visible = true;
				if (column instanceof HideableColumn< ? >)
				{
					HideableColumn< ? > hideCol = (HideableColumn< ? >) column;
					visible = hideCol.isColumnVisible() && hideCol.isColumnVisibleInExport();
				}
				if (visible)
				{
					if (column instanceof ExportHeaderColumn< ? >)
						exportHeaderModel
							.append(((ExportHeaderColumn<T>) column).getExportHeader());
					else
						exportHeaderModel.append(header.getDefaultModelObjectAsString());
					exportHeaderModel.append(";");
				}
				wrapper.add(header);
				int colspan = 1;
				if (column instanceof ISpanningColumn< ? >)
				{
					colspan = ((ISpanningColumn<T>) column).getSpan();
				}
				if (colspan > 1)
				{
					wrapper.add(new AttributeModifier("colspan", true, new Model<Integer>(Integer
						.valueOf(colspan))));
				}
				item.add(wrapper);
				item.setRenderBodyOnly(true);
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
						CustomColumn<T> column = getModelObject();
						if (column instanceof HideableColumn< ? >)
							return super.isVisible()
								&& ((HideableColumn<T>) column).isColumnVisible();
						return super.isVisible();
					}
				};
			}
		};
		headers.setVersioned(false);
		add(headers);
	}
}
