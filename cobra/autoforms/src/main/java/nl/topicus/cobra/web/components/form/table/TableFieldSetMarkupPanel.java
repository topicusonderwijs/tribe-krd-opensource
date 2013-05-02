package nl.topicus.cobra.web.components.form.table;

import java.util.List;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.HtmlClassModel;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * {@code TableFieldSetMarkupPanel} bevat de standaard markup voor
 * {@link TableMarkupRenderer}, bestaande uit het label en de table.
 * 
 * @author papegaaij
 */
public class TableFieldSetMarkupPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Maakt een TableFieldSetMarkupPanel.
	 * 
	 * @param id
	 *            De id.
	 * @param fieldSet
	 *            De fieldset.
	 */
	public TableFieldSetMarkupPanel(String id, AutoFieldSet< ? > fieldSet)
	{
		super(id);
		WebMarkupContainer table = new WebMarkupContainer("table")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isTransparentResolver()
			{
				return true;
			}
		};
		add(table);
		table.add(new AttributeAppender("class", new HtmlClassModel(
			new PropertyModel<List<String>>(fieldSet, "htmlClasses")), " "));
	}
}
