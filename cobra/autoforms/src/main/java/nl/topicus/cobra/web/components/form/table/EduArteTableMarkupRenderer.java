package nl.topicus.cobra.web.components.form.table;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldMarkupRenderer;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * {@code EduArteTableMarkupRenderer} is een {@link FieldMarkupRenderer} die de form
 * velden onder elkaar in een table plaatst. Dit is de standaard renderer binnen EduArte.
 * 
 * @author papegaaij
 */
public class EduArteTableMarkupRenderer extends TableMarkupRenderer
{
	@SuppressWarnings("hiding")
	public static final String NAME = "EduArteTableMarkupRenderer";

	@Override
	public <T> Panel createFieldSetMarkup(String id, AutoFieldSet<T> fieldSet)
	{
		return new EduArteTableFieldSetMarkupPanel(id, fieldSet);
	}
}
