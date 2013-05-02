package nl.topicus.eduarte.web.components.panels.afspraak;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.dao.participatie.helpers.DeelnemerMedewerkerGroepDataAccesHelper;
import nl.topicus.eduarte.entities.participatie.DeelnemerMedewerkerGroep;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

public class ConvertToEntityModelWrapper implements IModel<DeelnemerMedewerkerGroep>,
		IWrapModel<DeelnemerMedewerkerGroep>
{
	private static final long serialVersionUID = 1L;

	private IModel<IdObject> inner;

	public ConvertToEntityModelWrapper(IModel<IdObject> inner)
	{
		this.inner = inner;
	}

	@Override
	public DeelnemerMedewerkerGroep getObject()
	{
		DeelnemerMedewerkerGroepDataAccesHelper helper =
			DataAccessRegistry.getHelper(DeelnemerMedewerkerGroepDataAccesHelper.class);
		if (inner.getObject() == null)
			return null;

		IdObject apObject = inner.getObject();
		return helper.get(apObject);
	}

	@Override
	public void setObject(DeelnemerMedewerkerGroep object)
	{
		if (object == null)
			inner.setObject(null);
		else
		{
			DeelnemerMedewerkerGroep dmg = object;
			inner.setObject(dmg.getEntiteit());
		}
	}

	@Override
	public void detach()
	{
		inner.detach();
	}

	@Override
	public IModel<IdObject> getWrappedModel()
	{
		return inner;
	}
}