package nl.topicus.cobra.security.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.security.IPrincipalSource;
import nl.topicus.cobra.security.IPrincipalSourceResolver;

import org.apache.wicket.Component;
import org.apache.wicket.security.actions.WaspActionFactory;
import org.apache.wicket.security.hive.Hive;
import org.apache.wicket.security.hive.SimpleCachingHive;
import org.apache.wicket.security.hive.authorization.Principal;
import org.apache.wicket.security.hive.config.HiveFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHiveFactory<T extends Principal> implements HiveFactory
{
	private static final Logger LOG = LoggerFactory.getLogger(AnnotationHiveFactory.class);

	private List<IPrincipalSourceResolver<T>> resolvers =
		new ArrayList<IPrincipalSourceResolver<T>>();

	private WaspActionFactory actionFactory;

	private List<T> allPrincipals = new ArrayList<T>();

	public AnnotationHiveFactory(WaspActionFactory actionFactory,
			List<IPrincipalSourceResolver<T>> resolvers)
	{
		this.actionFactory = actionFactory;
		this.resolvers = resolvers;
	}

	@Override
	public Hive createHive()
	{
		long start = System.currentTimeMillis();
		LOG.info("Creating hive");
		SimpleCachingHive ret = new SimpleCachingHive();
		Map<Class< ? extends IPrincipalSource<T>>, List<Class< ? extends Component>>> components =
			new HashMap<Class< ? extends IPrincipalSource<T>>, List<Class< ? extends Component>>>();
		for (IPrincipalSourceResolver<T> curResolver : resolvers)
		{
			LOG.info("Scanning components in " + curResolver);
			for (Class< ? extends Component> curComponent : curResolver.getAnnotatedComponents())
			{
				for (Class< ? extends IPrincipalSource<T>> curPrincipalSource : curResolver
					.getPrincipalSources(curComponent))
				{
					List<Class< ? extends Component>> curList = components.get(curPrincipalSource);
					if (curList == null)
					{
						curList = new ArrayList<Class< ? extends Component>>();
						components.put(curPrincipalSource, curList);
					}
					curList.add(curComponent);
				}
			}
		}
		for (IPrincipalSourceResolver<T> curResolver : resolvers)
		{
			for (IPrincipalSource<T> curPrincipalSource : curResolver.getPrinicipalSources())
			{
				List<Class< ? extends Component>> componentClasses =
					components.get(curPrincipalSource.getClass());
				if (componentClasses == null)
					componentClasses = Collections.emptyList();
				allPrincipals.addAll(curPrincipalSource.addPrincipalsToHive(ret, actionFactory,
					componentClasses));
			}
		}
		LOG.info("Done creating hive (created " + allPrincipals.size() + " principals in "
			+ (System.currentTimeMillis() - start) + " ms)");
		return ret;
	}

	public List<T> getAllPrincipals()
	{
		return allPrincipals;
	}
}
