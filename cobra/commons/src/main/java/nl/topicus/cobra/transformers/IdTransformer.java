package nl.topicus.cobra.transformers;

public class IdTransformer implements Transformer<Object>
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object transform(Object value)
	{
		return value;
	}
}
