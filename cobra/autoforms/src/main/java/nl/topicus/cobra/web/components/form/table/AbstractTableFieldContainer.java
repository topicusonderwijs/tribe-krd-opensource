package nl.topicus.cobra.web.components.form.table;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AbstractFieldContainer;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * {@code AbstractTableFieldContainer} bevat de standaard markup voor het renderen van
 * form velden in een table. Voor elk veld wordt een table row gemaakt.
 * 
 * @author papegaaij
 */
public abstract class AbstractTableFieldContainer<X, Y, Z> extends AbstractFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	private boolean renderLabel = true;

	/**
	 * Maakt een nieuw AbstractTableFieldContainer.
	 * 
	 * @param id
	 *            Het id.
	 * @param properties
	 *            De properties.
	 */
	public AbstractTableFieldContainer(String id, final FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);

		WebMarkupContainer labelContainer = new WebMarkupContainer("labelContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return AbstractTableFieldContainer.this.renderLabel;
			}
		};
		add(labelContainer);

		WebMarkupContainer description = new WebMarkupContainer("description")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && StringUtil.isNotEmpty(properties.getDescription());
			}
		};
		description.add(new AttributeAppender("title", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				if (StringUtil.isNotEmpty(properties.getDescription()))
				{
					return properties.getLabel() + " - " + properties.getDescription();
				}
				return null;
			}
		}, " "));
		labelContainer.add(description);

		labelContainer.add(new Label("label", properties.getLabelModel()));
	}

	public AbstractTableFieldContainer<X, Y, Z> setRenderLabel(boolean renderLabel)
	{
		this.renderLabel = renderLabel;
		return this;
	}
}
