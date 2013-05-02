package nl.topicus.cobra.transformers;

import java.io.Serializable;

public interface Transformer<T> extends Serializable
{
	public T transform(T value);
}
