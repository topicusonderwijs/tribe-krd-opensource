package nl.topicus.eduarte.web.components.choice.multiple;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.app.security.models.OrganisatieEenhedenPageModel;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.ComponentSecurityCheck;
import org.apache.wicket.security.swarm.models.SwarmModel;

public class OrganisatieEenheidMultipleChoice extends ListMultipleChoice<OrganisatieEenheid>
{
	private static final long serialVersionUID = 1L;

	public OrganisatieEenheidMultipleChoice(String id)
	{
		this(id, null);
	}

	public OrganisatieEenheidMultipleChoice(String id, IModel<Collection<OrganisatieEenheid>> model)
	{
		super(id, model, (IModel<List<OrganisatieEenheid>>) null, new ToStringRenderer());
		setChoices(new OrganisatieEenhedenPageModel(new ComponentSecurityCheck(this), null));
	}

	@Override
	public List< ? extends OrganisatieEenheid> getChoices()
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
