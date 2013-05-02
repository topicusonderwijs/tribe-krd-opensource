package nl.topicus.eduarte.web.components.choice.multiple;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.LocatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;

import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.security.swarm.models.SwarmModel;

public class LocatieMultipleChoice extends ListMultipleChoice<Locatie>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Locatie>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Locatie> load()
		{
			return DataAccessRegistry.getHelper(LocatieDataAccessHelper.class).list(
				EduArteContext.get().getPeildatum());
		}
	}

	public LocatieMultipleChoice(String id)
	{
		this(id, null);
	}

	public LocatieMultipleChoice(String id, IModel<Collection<Locatie>> model)
	{
		super(id, model, new ListModel(), new ToStringRenderer());
	}

	@Override
	public List< ? extends Locatie> getChoices()
	{
		initSecureChoicesModel();
		return super.getChoices();
	}

	private void initSecureChoicesModel()
	{
		try
		{
			Field choicesField =
				AbstractSingleSelectChoice.class.getSuperclass().getDeclaredField("choices");
			choicesField.setAccessible(true);
			Object choicesModel = choicesField.get(this);
			if (choicesModel instanceof SwarmModel< ? >)
				((SwarmModel< ? >) choicesModel).getSecurityId(this);
		}
		catch (SecurityException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

}
