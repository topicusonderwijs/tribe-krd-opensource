package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.entities.examen.Examenstatus;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class ExamenstatusMultipleChoice extends ListMultipleChoice<Examenstatus>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Examenstatus>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<Examenstatus> load()
		{
			return DataAccessRegistry.getHelper(BatchDataAccessHelper.class).list(
				Examenstatus.class, "naam");
		}
	}

	public ExamenstatusMultipleChoice(String id)
	{
		this(id, null);
	}

	public ExamenstatusMultipleChoice(String id, IModel<Collection<Examenstatus>> model)
	{
		super(id, model, new ListModel(), new IChoiceRenderer<Examenstatus>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Examenstatus status)
			{
				return status.getNaam() + " (" + status.getExamenWorkflow().getNaam() + ")";
			}

			@Override
			public String getIdValue(Examenstatus status, int index)
			{
				return status.getId().toString();
			}

		});
	}

}
