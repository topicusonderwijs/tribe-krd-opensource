package nl.topicus.cobra.update;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

/**
 * @author Emond Papegaaij
 * @since 13 juli 2009
 */
public class ComponentScanner
{

	private static final String RESOURCE_PATTERN = "**/*.class";

	public List<Class< ? >> scanForClasses(Class< ? extends Annotation> annotationClass,
			String packageName)
	{
		return scanForClasses(Object.class, annotationClass, packageName);
	}

	/**
	 * @return != null
	 */
	public <C> List<Class< ? extends C>> scanForClasses(Class<C> baseClass,
			Class< ? extends Annotation> annotationClass, String packageName)
	{
		TypeFilter filter =
			annotationClass == null ? null : new AnnotationTypeFilter(annotationClass);
		return scanForClasses(baseClass, packageName, filter);
	}

	/**
	 * Scans and filters classes using the TypeFilter as a filter.
	 */
	public <C> List<Class< ? extends C>> scanForClasses(Class<C> baseClass, String packageName,
			TypeFilter filter)
	{
		List<Class< ? extends C>> ret = new ArrayList<Class< ? extends C>>();
		try
		{
			ResourcePatternResolver resourcePatternResolver =
				new PathMatchingResourcePatternResolver();
			String usePackageName = packageName;
			if (!usePackageName.endsWith("."))
				usePackageName += ".";
			String pattern =
				ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ ClassUtils.convertClassNameToResourcePath(usePackageName) + RESOURCE_PATTERN;
			Resource[] resources = resourcePatternResolver.getResources(pattern);
			MetadataReaderFactory readerFactory =
				new CachingMetadataReaderFactory(resourcePatternResolver);
			for (Resource resource : resources)
			{
				if (resource.isReadable())
				{
					MetadataReader reader = readerFactory.getMetadataReader(resource);
					String className = reader.getClassMetadata().getClassName();
					if (filter == null || filter.match(reader, readerFactory))
					{
						Class< ? > readClass =
							resourcePatternResolver.getClassLoader().loadClass(className);
						if (baseClass.isAssignableFrom(readClass))
							ret.add(readClass.asSubclass(baseClass));
					}
				}
			}
			return ret;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

}