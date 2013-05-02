package nl.topicus.eduarte.web.components.choice;

import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.app.EduArteApp;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.WaspAction;

/**
 * Combobox met alle security actions.
 * 
 * @author papegaaij
 */
public class ActionClassCombobox extends AbstractAjaxDropDownChoice<Class< ? extends WaspAction>>
{
	private static final long serialVersionUID = 1L;

	public ActionClassCombobox(String id, IModel<Class< ? extends WaspAction>> model)
	{
		super(id, model, EduArteApp.get().getActions(RechtenSoort.INSTELLING),
			new IChoiceRenderer<Class< ? extends WaspAction>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getIdValue(Class< ? extends WaspAction> object, int index)
				{
					return object.toString();
				}

				@Override
				public Object getDisplayValue(Class< ? extends WaspAction> object)
				{
					return object.getAnnotation(Description.class).value();
				}
			});
	}
}
