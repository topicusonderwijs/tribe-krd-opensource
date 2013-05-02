package nl.topicus.cobra.dataproviders;

import org.apache.wicket.markup.repeater.data.IDataProvider;

public interface CachingDataProvider<T> extends IDataProvider<T>
{
	public void clearCache();
}
