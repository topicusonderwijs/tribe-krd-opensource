package nl.topicus.eduarte.web.components.choice;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.examen.Examenstatus;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox met mogelijke examenstatussen.
 * 
 * @author loite
 */
public class ExamenstatusCombobox extends AbstractAjaxDropDownChoice<Examenstatus>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Examenstatus>>
	{
		private static final long serialVersionUID = 1L;

		private final IModel<ExamenWorkflow> workflowModel;

		public ListModel(IModel<ExamenWorkflow> workflowModel)
		{
			this.workflowModel = workflowModel;
			detach();
		}

		@Override
		protected List<Examenstatus> load()
		{
			if (workflowModel.getObject() == null)
				return Collections.emptyList();
			return workflowModel.getObject().getExamenstatussen();
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			ComponentUtil.detachQuietly(workflowModel);
		}

	}

	public ExamenstatusCombobox(String id, ExamenWorkflow workflow)
	{
		this(id, null, workflow);
	}

	public ExamenstatusCombobox(String id, IModel<ExamenWorkflow> workflowModel)
	{
		super(id, null, new ListModel(workflowModel));
	}

	public ExamenstatusCombobox(String id, IModel<Examenstatus> model, ExamenWorkflow workflow)
	{
		super(id, model, new ListModel(ModelFactory.getModel(workflow)));
	}

	public ExamenstatusCombobox(String id, IModel<Examenstatus> model,
			IModel<ExamenWorkflow> workflowModel)
	{
		super(id, model, new ListModel(workflowModel));
	}

	public ExamenstatusCombobox(String id, IModel<Examenstatus> model, List<Examenstatus> choices)
	{
		super(id, model, ModelFactory.getListModel(choices), new ToStringRenderer());
	}
}
