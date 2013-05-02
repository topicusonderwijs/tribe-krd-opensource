package nl.topicus.eduarte.web.components.panels.bottomrow;

import nl.topicus.cobra.app.CobraSession;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessException;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.eduarte.entities.Entiteit;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;

public class ProbeerTeVerwijderenButton extends VerwijderButton
{
	private static final long serialVersionUID = 1L;

	private Class< ? extends Page> target;

	private String objectNaamEnAanwVnw;

	public ProbeerTeVerwijderenButton(BottomRowPanel bottomRow,
			IModel< ? extends Entiteit> objectToDelete, String objectNaamEnAanwVnw,
			Class< ? extends Page> target)
	{
		this(bottomRow, objectToDelete, objectNaamEnAanwVnw);
		this.target = target;
	}

	private ProbeerTeVerwijderenButton(BottomRowPanel bottomRow,
			IModel< ? extends Entiteit> objectToDelete, String objectNaamEnAanwVnw)
	{
		super(bottomRow, "Verwijder", "Weet u zeker dat u " + objectNaamEnAanwVnw
			+ " wilt verwijderen?");
		setDefaultModel(objectToDelete);
		this.objectNaamEnAanwVnw = objectNaamEnAanwVnw;
	}

	@Override
	public boolean isVisible()
	{
		return ((Entiteit) getDefaultModelObject()).isSaved();
	}

	@Override
	protected void onClick()
	{
		try
		{
			removeEntity();
			CobraSession.getCobraSession().getNavigationStack().clear();
		}
		catch (DataAccessException e)
		{
			Session.get().error(
				StringUtil.firstCharUppercase(objectNaamEnAanwVnw)
					+ " wordt reeds gebruikt en kan hierdoor niet meer verwijderd worden.");
		}
		setResponsePage(target);
	}

	protected void removeEntity()
	{
		if (getDefaultModel() instanceof IChangeRecordingModel< ? >)
		{
			IChangeRecordingModel< ? > model = (IChangeRecordingModel< ? >) getDefaultModel();
			model.deleteObject();
		}
		else
		{
			Entiteit object = (Entiteit) getDefaultModelObject();
			object.delete();
		}
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
	}
}
