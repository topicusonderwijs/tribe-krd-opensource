package nl.topicus.cobra.dataproviders;

import org.apache.wicket.markup.repeater.data.IDataProvider;

public interface IndexedDataProvider<T> extends IDataProvider<T>
{
	public int getIndex(Object obj);
}
