package nl.topicus.eduarte.web.pages.beheer.account;

import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.web.components.datapanel.CustomColumn;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelToolbar;
import nl.topicus.eduarte.app.security.PrincipalGroup;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.RechtenColumn;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

public class ActionToolbar extends CustomDataPanelToolbar<PrincipalGroup>
{
	private static final long serialVersionUID = 1L;

	public ActionToolbar(CustomDataPanel<PrincipalGroup> datapanel)
	{
		super(datapanel.getModel());

		ListView<CustomColumn<PrincipalGroup>> headers =
			new ListView<CustomColumn<PrincipalGroup>>("headers", datapanel.getModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<CustomColumn<PrincipalGroup>> item)
				{
					CustomColumn<PrincipalGroup> column = item.getModelObject();
					if (column instanceof RechtenColumn)
					{
						RechtenColumn rechtenColumn = (RechtenColumn) column;
						if (column.isVisible() && rechtenColumn.isWrite())
						{
							Label label =
								new Label("label", rechtenColumn.getAction().getAnnotation(
									Description.class).value());
							label.add(new SimpleAttributeModifier("colspan", "2"));
							item.add(label);
						}
						else
						{
							item.add(new WebMarkupContainer("label").setVisible(false));
						}
					}
					else
					{
						item.add(new Label("label", ""));
					}
				}
			};
		add(headers);
	}
}
