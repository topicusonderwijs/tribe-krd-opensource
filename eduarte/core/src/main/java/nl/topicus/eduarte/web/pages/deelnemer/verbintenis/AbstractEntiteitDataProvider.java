package nl.topicus.eduarte.web.pages.deelnemer.verbintenis;

import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * Dataprovider waar (model van) een entiteit ingestopt kan worden. Zelf createLijst()
 * implementeren om te bepalen wat er vervolgens weer uitkomt.
 * 
 * @author idserda
 */
public abstract class AbstractEntiteitDataProvider<T> implements IDataProvider<T>
{
	private static final long serialVersionUID = 1L;

	protected List<T> lijst;

	protected IModel< ? > entiteitModel;

	public AbstractEntiteitDataProvider(IModel< ? > entiteitModel)
	{
		this.entiteitModel = entiteitModel;
	}

	@Override
	public Iterator<T> iterator(int first, int count)
	{
		if (lijst == null)
			createLijst();
		int ubound = Math.min(lijst.size(), first + count);
		return lijst.subList(first, ubound).iterator();
	}

	@Override
	public IModel<T> model(T object)
	{
		return ModelFactory.getModel(object);
	}

	@Override
	public int size()
	{
		if (lijst == null)
			createLijst();
		return lijst.size();
	}

	@Override
	public void detach()
	{
		lijst = null;
		ComponentUtil.detachQuietly(entiteitModel);
	}

	abstract protected void createLijst();
}
