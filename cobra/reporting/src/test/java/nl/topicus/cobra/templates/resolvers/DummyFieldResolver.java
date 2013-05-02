package nl.topicus.cobra.templates.resolvers;

import nl.topicus.cobra.templates.FieldInfo;

public final class DummyFieldResolver implements FieldResolver
{
	@Override
	public FieldInfo getInfo(String name)
	{
		return null;
	}

	@Override
	public Object next(String name)
	{
		return null;
	}

	@Override
	public Object resolve(String name)
	{
		return null;
	}

}
