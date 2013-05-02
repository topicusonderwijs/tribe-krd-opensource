package nl.topicus.eduarte.web.pages.deelnemer.personalia;

import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.table.AbstractTableFieldContainer;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.Model;

/**
 * @author hop
 */
public class LabelWarningDisplayContainer<X, Y, Z> extends AbstractTableFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	public LabelWarningDisplayContainer(String id, FieldProperties<X, Y, Z> properties,
			String warning)
	{
		super(id, properties);
		ContextImage image =
			new ContextImage("image", new Model<String>("assets/img/icons/warning.gif"));
		image.add(new SimpleAttributeModifier("title", warning));
		image.add(new SimpleAttributeModifier("alt", warning));
		add(image);
	}
}
