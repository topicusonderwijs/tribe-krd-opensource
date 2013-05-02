package nl.topicus.cobra.reflection;

public class MethodNotFoundException extends Exception
{
	private static final long serialVersionUID = 1L;

	public MethodNotFoundException(Class< ? > clazz, String name, Class< ? >... argTypes)
	{
		super(constructMessage(clazz, name, argTypes));
	}

	private static String constructMessage(Class< ? > clazz, String name, Class< ? >... argTypes)
	{
		StringBuilder sb = new StringBuilder(clazz.getName());
		sb.append('.');
		sb.append(name);
		sb.append('(');
		boolean first = true;
		for (Class< ? > curArg : argTypes)
		{
			if (!first)
				sb.append(", ");
			first = false;
			if (curArg == null)
				sb.append("<anytype>");
			else
				sb.append(curArg.getSimpleName());
		}
		sb.append(')');
		return sb.toString();
	}
}
