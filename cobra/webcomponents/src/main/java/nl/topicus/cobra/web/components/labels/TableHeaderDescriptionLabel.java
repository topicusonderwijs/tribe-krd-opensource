package nl.topicus.cobra.web.components.labels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Adds a description to the label,usually used in a form to give more information about
 * the input value
 * 
 * @author vanharen
 */
public class TableHeaderDescriptionLabel extends Panel
{
	private static final long serialVersionUID = 1L;

	public TableHeaderDescriptionLabel(String id, String label, String description)
	{
		this(id, new Model<String>(label), label, description);
	}

	public TableHeaderDescriptionLabel(String id, IModel< ? > model, String label,
			String description)
	{
		super(id, model);
		Label colHead = new Label(id, label);
		WebMarkupContainer descriptionContainer = new WebMarkupContainer("description");
		descriptionContainer.add(new AttributeModifier("title", true,
			new Model<String>(description)));

		add(colHead);
		add(descriptionContainer);
	}
}
