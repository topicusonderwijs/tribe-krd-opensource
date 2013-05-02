package nl.topicus.cobra.web.components.form.table;

import java.util.List;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.HtmlClassModel;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * {@code EduArteTableFieldSetMarkupPanel} bevat de standaard markup voor
 * {@link EduArteTableMarkupRenderer}, bestaande uit het label en de table.
 * 
 * @author papegaaij
 */
public class EduArteTableFieldSetMarkupPanel extends Panel
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
	public EduArteTableFieldSetMarkupPanel(String id, final AutoFieldSet< ? > fieldSet)
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
		WebMarkupContainer captionContainer = new WebMarkupContainer("captionContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isTransparentResolver()
			{
				return true;
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && !StringUtil.isEmpty(fieldSet.getCaption());
			}
		};
		add(table);
		add(captionContainer);
		table.add(new AttributeAppender("class", new HtmlClassModel(
			new PropertyModel<List<String>>(fieldSet, "htmlClasses")), " "));
	}
}
