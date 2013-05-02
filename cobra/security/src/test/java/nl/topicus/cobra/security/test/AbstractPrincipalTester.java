package nl.topicus.cobra.security.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Everybody;
import nl.topicus.cobra.security.IPrincipalSource;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.NoReadPrincipalSource;
import nl.topicus.cobra.security.Write;
import nl.topicus.cobra.security.impl.AbstractPrincipalImpl;
import nl.topicus.cobra.security.impl.AnnotationHiveFactory;
import nl.topicus.cobra.security.impl.SecurityUtil;

import org.apache.wicket.security.hive.Hive;
import org.apache.wicket.security.hive.authorization.Principal;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractPrincipalTester<T extends AbstractPrincipalImpl<T>>
{
	private static AnnotationHiveFactory< ? > hiveFactory;

	private static Hive hive;

	private List<T> principals;

	private Set<Class< ? extends IPrincipalSource<T>>> principalSources =
		new HashSet<Class< ? extends IPrincipalSource<T>>>();

	private List<String> errors = new ArrayList<String>();

	public AbstractPrincipalTester()
	{
	}

	@Before
	@SuppressWarnings("unchecked")
	public void initPrincipals()
	{
		if (hiveFactory == null)
		{
			hiveFactory = getHiveFactory();
			hive = hiveFactory.createHive();
		}
		principals = (List<T>) hiveFactory.getAllPrincipals();
		for (T curPrincipal : principals)
		{
			principalSources.add(curPrincipal.getSourceClass());
		}
	}

	abstract protected AnnotationHiveFactory<T> getHiveFactory();

	@Before
	public void clearErrorList()
	{
		errors.clear();
	}

	protected void assertNoErrors()
	{
		if (!errors.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			for (String curError : errors)
			{
				sb.append(curError).append('\n');
			}
			fail(sb.toString());
		}
	}

	protected void addError(String error)
	{
		errors.add(error);
	}

	protected List<T> getPrincipals()
	{
		return principals;
	}

	protected Set<Class< ? extends IPrincipalSource<T>>> getPrincipalSources()
	{
		return principalSources;
	}

	protected Hive getHive()
	{
		return hive;
	}

	@Test
	public void testDescriptions()
	{
		for (Class< ? extends IPrincipalSource<T>> curSourceClass : principalSources)
		{
			if (!curSourceClass.isAnnotationPresent(Description.class))
			{
				addError(curSourceClass.getName() + " heeft geen @Description annotatie");
			}
		}
		assertNoErrors();
	}

	@Test
	public void testActions()
	{
		for (Class< ? extends IPrincipalSource<T>> curSourceClass : principalSources)
		{
			if (!curSourceClass.isAnnotationPresent(Actions.class))
			{
				addError(curSourceClass.getName() + " heeft geen @Actions annotatie");
			}
		}
		assertNoErrors();
	}

	@Test
	public void testWriteLoops()
	{
		for (Class< ? extends IPrincipalSource<T>> curSourceClass : principalSources)
		{
			if (curSourceClass.isAnnotationPresent(Write.class))
			{
				Class< ? extends IPrincipalSource< ? extends Principal>> read =
					curSourceClass.getAnnotation(Write.class).read();
				if (read.isAnnotationPresent(Write.class))
					addError(curSourceClass.getName() + " heeft " + read.getName()
						+ " als lees principal welke ook de @Write annotatie heeft");
			}
		}
		assertNoErrors();
	}

	@Test
	public void testRead()
	{
		List<Class< ? extends IPrincipalSource< ? extends Principal>>> readList =
			new ArrayList<Class< ? extends IPrincipalSource< ? extends Principal>>>();

		for (Class< ? extends IPrincipalSource<T>> curSourceClass : principalSources)
		{
			if (curSourceClass.isAnnotationPresent(Write.class))
			{
				Class< ? extends IPrincipalSource< ? extends Principal>> read =
					curSourceClass.getAnnotation(Write.class).read();

				if (!read.equals(NoReadPrincipalSource.class))
				{
					if (readList.contains(read))
					{
						addError(curSourceClass.getName()
							+ " heeft "
							+ read.getName()
							+ " als lees principal welke ook in een andere principal als lees recht gebruikt wordt");
					}
					else
						readList.add(read);
				}
			}
		}
		assertNoErrors();
	}

	@Test
	public void testImpliesLoops()
	{
		Set<Class< ? extends IPrincipalSource<T>>> todoSet =
			new LinkedHashSet<Class< ? extends IPrincipalSource<T>>>(principalSources);
		Map<Class< ? extends IPrincipalSource<T>>, Set<Class< ? extends IPrincipalSource<T>>>> closure =
			new HashMap<Class< ? extends IPrincipalSource<T>>, Set<Class< ? extends IPrincipalSource<T>>>>();

		while (!todoSet.isEmpty())
		{
			Class< ? extends IPrincipalSource<T>> first = todoSet.iterator().next();
			todoSet.remove(first);

			Set<Class< ? extends IPrincipalSource<T>>> curSet = closure.get(first);
			if (curSet == null)
			{
				curSet = new HashSet<Class< ? extends IPrincipalSource<T>>>();
				closure.put(first, curSet);
			}
			if (curSet.addAll(SecurityUtil.getImplies(first)))
			{
				todoSet.add(first);
			}
		}

		for (Class< ? extends IPrincipalSource<T>> curSourceClass : principalSources)
		{
			Set<Class< ? extends IPrincipalSource<T>>> curImplies = closure.get(curSourceClass);
			for (Class< ? extends IPrincipalSource<T>> curImplied : curImplies)
			{
				if (closure.get(curImplied).contains(curSourceClass))
				{
					addError("Implies loop tussen " + curSourceClass.getName() + " en "
						+ curImplied.getName());
				}
			}
		}
		assertNoErrors();
	}

	private boolean isOrImplies(Class< ? extends IPrincipalSource< ? >> base,
			Class< ? extends IPrincipalSource< ? >> check)
	{
		if (check.isAnnotationPresent(Everybody.class) || check.equals(base))
			return true;
		if (base.isAnnotationPresent(Implies.class))
		{
			for (Class< ? extends IPrincipalSource< ? >> newBase : base
				.getAnnotation(Implies.class).value())
			{
				if (isOrImplies(newBase, check))
					return true;
			}
		}
		return false;
	}
}
