package nl.topicus.eduarte.web.components.quicksearch.plaats;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.dao.helpers.PlaatsDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Plaats;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

/**
 * Converts string to voorvoegsel, inner == string, wrappedmodel == plaats
 * 
 * @author papegaaij
 */
public class WrappedModel implements IModel<Plaats>, IWrapModel<Plaats>
{
	private static final long serialVersionUID = 1L;

	private IModel<String> inner;

	private IModel<Land> landModel;

	private boolean isNederland()
	{
		if (landModel == null)
			return true;

		Land land = landModel.getObject();
		return land != null && land.isNederland();
	}

	public WrappedModel(IModel<String> inner, IModel<Land> landModel)
	{
		this.inner = inner;
		this.landModel = landModel;
	}

	public WrappedModel(IModel<String> inner)
	{
		this(inner, null);
	}

	@Override
	public Plaats getObject()
	{
		String naam = inner.getObject();
		if (StringUtil.isEmpty(naam))
			return null;

		if (!isNederland())
			return new Plaats(naam);

		return DataAccessRegistry.getHelper(PlaatsDataAccessHelper.class).get(naam);
	}

	@Override
	public void setObject(Plaats object)
	{
		inner.setObject(object == null ? null : object.toString());
	}

	@Override
	public void detach()
	{
		inner.detach();
	}

	@Override
	public IModel<String> getWrappedModel()
	{
		return inner;
	}
}
