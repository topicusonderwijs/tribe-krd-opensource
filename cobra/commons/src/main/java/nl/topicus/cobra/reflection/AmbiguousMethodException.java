package nl.topicus.cobra.reflection;

import java.util.Arrays;
import java.util.List;

public class AmbiguousMethodException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public <T> AmbiguousMethodException(List<Callable<T>> applicable, Class< ? >... args)
	{
		super("Method invocation is ambiguous for " + Arrays.asList(args)
			+ ". Applicable methods: " + applicable);
	}

}
