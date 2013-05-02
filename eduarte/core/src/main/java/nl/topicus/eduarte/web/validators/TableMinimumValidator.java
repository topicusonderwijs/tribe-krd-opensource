package nl.topicus.eduarte.web.validators;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

/**
 * 
 * 
 * @author vanharen
 */
public class TableMinimumValidator extends AbstractFormValidator
{

	private static final long serialVersionUID = 1L;

	private CustomDataPanel< ? > panel;

	private int minimum;

	public TableMinimumValidator(CustomDataPanel< ? > panel, int minimum)
	{
		this.panel = panel;
		this.minimum = minimum;
	}

	@Override
	public void validate(Form< ? > form)
	{
		int rowCount = panel.getRowCount();
		if (rowCount < minimum)
		{
			form.error("Er dient minimaal 1 organisatie aanbod te zijn");
		}

	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		return null;
	}
}
