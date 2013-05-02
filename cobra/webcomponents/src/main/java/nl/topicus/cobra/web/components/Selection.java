package nl.topicus.cobra.web.components;

import org.apache.wicket.model.IDetachable;

public interface Selection<R, S> extends IDetachable
{
	public boolean isSelected(S object);

	public void add(S object);

	public void remove(S object);

	public void clear();

	public int size();
}
