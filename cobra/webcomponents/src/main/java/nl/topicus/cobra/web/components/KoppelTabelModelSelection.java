package nl.topicus.cobra.web.components;

import java.util.Collection;

import nl.topicus.cobra.util.JavaUtil;

import org.apache.wicket.model.IModel;

/**
 * Selectie voor het leggen van many-to-many relaties
 * 
 * @author papegaaij
 */
public abstract class KoppelTabelModelSelection<R, S> extends ModelSelection<R, S>
{
	private static final long serialVersionUID = 1L;

	public KoppelTabelModelSelection(IModel< ? extends Collection<R>> model)
	{
		super(model);
	}

	protected abstract R newR(S object);

	protected abstract S convertRtoS(R object);

	@Override
	public void add(S object)
	{
		getSelected().add(newR(object));
	}

	@Override
	protected R convertStoR(S object)
	{
		for (R curSelected : getSelected())
		{
			if (JavaUtil.equalsOrBothNull(convertRtoS(curSelected), object))
				return curSelected;
		}
		return null;
	}
}
