package nl.topicus.eduarte.participatie.web.components.choice.combobox;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.participatie.helpers.ExterneAgendaKoppelingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.ExterneAgendaKoppeling;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class KoppelingCombobox extends AbstractAjaxDropDownChoice<ExterneAgendaKoppeling>
{
	private static final long serialVersionUID = 1L;

	private static class ChoicesModel extends LoadableDetachableModel<List<ExterneAgendaKoppeling>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<ExterneAgendaKoppeling> load()
		{
			return DataAccessRegistry.getHelper(ExterneAgendaKoppelingDataAccessHelper.class).list(
				EduArteContext.get().getOrganisatieEenhedenOrInstelling());
		}
	}

	public KoppelingCombobox(String id, IModel<ExterneAgendaKoppeling> model)
	{
		super(id, model, new ChoicesModel());
	}
}
