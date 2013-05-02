package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.wiquery.Progressbar;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ProgressbarColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	public ProgressbarColumn(String id, String header, String property)
	{
		super(id, header, property);
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		IModel<Object> mod = new PropertyModel<Object>(rowModel, getPropertyExpression());
		cell.add(new ProgressbarPanel(componentId, mod, rowModel));
	}

	private final class ProgressbarPanel extends TypedPanel<Object>
	{
		private static final long serialVersionUID = 1L;

		public ProgressbarPanel(String componentId, IModel<Object> propertyModel,
				final IModel<T> rowModel)
		{
			super(componentId, propertyModel);
			setRenderBodyOnly(true);
			add(new Progressbar("progress", propertyModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled()
				{
					return super.isEnabled() && ProgressbarColumn.this.isContentsEnabled(rowModel);
				}

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && ProgressbarColumn.this.isContentsVisible(rowModel);
				}
			});
		}
	}
}
