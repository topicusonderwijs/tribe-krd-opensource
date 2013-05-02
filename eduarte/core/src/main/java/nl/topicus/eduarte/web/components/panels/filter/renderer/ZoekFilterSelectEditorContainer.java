package nl.topicus.eduarte.web.components.panels.filter.renderer;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;

/**
 * De editor container voor een {@code <select>} veld.
 * 
 * @author papegaaij
 */
public class ZoekFilterSelectEditorContainer<X, Y, Z> extends
		AbstractZoekFilterFieldContainer<X, Y, Z>
{
	private static final long serialVersionUID = 1L;

	public ZoekFilterSelectEditorContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id, properties);
		replace(new WebMarkupContainer("label").setVisible(false));
	}

	/**
	 * Stel de dropdownchoice in als nullvalid=true wanneer hij niet required is. anders
	 * oogt de dropdownchoice niet required maar je kan je keuze niet ongedaan maken.
	 * 
	 * @see nl.topicus.cobra.web.components.form.AbstractFieldContainer#postProcessFormField(org.apache.wicket.Component,
	 *      nl.topicus.cobra.web.components.form.AutoFieldSet)
	 */
	@Override
	public void postProcessFormField(Component field, AutoFieldSet<X> fieldSet)
	{
		if (field instanceof AbstractSingleSelectChoice< ? >)
		{
			AbstractSingleSelectChoice< ? > choice = (AbstractSingleSelectChoice< ? >) field;
			if (!choice.isRequired())
				choice.setNullValid(true);
		}
		super.postProcessFormField(field, fieldSet);
	}
}
