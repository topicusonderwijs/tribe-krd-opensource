package nl.topicus.eduarte.web.components.quicksearch.voorvoegsel;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.VoorvoegselDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Voorvoegsel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

/**
 * Converts string to voorvoegsel, inner == string, wrappedmodel == voorvoegsel
 * 
 * @author papegaaij
 */
public class WrappedModel implements IModel<Voorvoegsel>, IWrapModel<Voorvoegsel>
{
	private static final long serialVersionUID = 1L;

	private IModel<String> inner;

	public WrappedModel(IModel<String> inner)
	{
		this.inner = inner;
	}

	@Override
	public Voorvoegsel getObject()
	{
		return DataAccessRegistry.getHelper(VoorvoegselDataAccessHelper.class).get(
			inner.getObject());
	}

	@Override
	public void setObject(Voorvoegsel object)
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
