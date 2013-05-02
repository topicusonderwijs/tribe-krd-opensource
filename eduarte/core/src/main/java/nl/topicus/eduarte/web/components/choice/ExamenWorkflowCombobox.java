package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.ExamenWorkflowDataAccessHelper;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox met mogelijke examen workflows.
 * 
 * @author papegaaij
 */
public class ExamenWorkflowCombobox extends AbstractAjaxDropDownChoice<ExamenWorkflow>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<ExamenWorkflow>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<ExamenWorkflow> load()
		{
			return DataAccessRegistry.getHelper(ExamenWorkflowDataAccessHelper.class).list();
		}
	}

	public ExamenWorkflowCombobox(String id)
	{
		this(id, null);
	}

	public ExamenWorkflowCombobox(String id, IModel<ExamenWorkflow> model)
	{
		this(id, model, new ListModel());
	}

	public ExamenWorkflowCombobox(String id, IModel<ExamenWorkflow> model,
			IModel< ? extends List< ? extends ExamenWorkflow>> choices)
	{
		super(id, model, choices, new ToStringRenderer());
	}
}
