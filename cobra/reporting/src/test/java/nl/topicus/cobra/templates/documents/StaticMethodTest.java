package nl.topicus.cobra.templates.documents;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.reflection.MethodNotFoundException;
import nl.topicus.cobra.reflection.ReflectionUtil;

import org.junit.Test;

public class StaticMethodTest
{
	@Test
	public void TestStaticMethod()
	{
		try
		{
			List<Class< ? >> classes =
				getClasses("target/classes/", "nl/topicus/cobra/templates", DocumentTemplate.class);
			assertTrue("Geen DocumentTemplate subclasses gevonden", classes.size() > 0);

			for (Class< ? > clazz : classes)
				assertTrue(
					"Class "
						+ clazz.getName()
						+ " heeft geen static method: public static RtfDocument createDocument(InputStream inStream) throws TemplateException",
					ReflectionUtil.findMethod(true, clazz, "createDocument", InputStream.class) != null);

		}
		catch (ClassNotFoundException e)
		{
			fail(e.getMessage());
		}
		catch (MethodNotFoundException e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * list Classes inside a given package
	 * 
	 * @author Jon Peck http://jonpeck.com (adapted from
	 *         http://www.javaworld.com/javaworld/javatips/jw-javatip113.html)
	 * @param dirname
	 *            String name of a Package, EG "java.lang"
	 * @return Class[] classes inside the root of the given package
	 * @throws ClassNotFoundException
	 *             if the Package is invalid
	 */
	public static List<Class< ? >> getClasses(String prefixDir, String dirname,
			Class< ? > targetClass) throws ClassNotFoundException
	{
		String packageName = dirname.replace("/", ".");
		List<Class< ? >> tempClasses = new ArrayList<Class< ? >>();

		// Get a File object for the package
		File directory = null;
		try
		{
			directory = new File(prefixDir + dirname);
		}
		catch (NullPointerException x)
		{
			throw new ClassNotFoundException(packageName + " does not appear to be a valid package");
		}
		if (directory.exists())
		{
			// Get the list of the files contained in the package
			File[] files = directory.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].getName().startsWith("."))
					continue;

				if (files[i].isDirectory())
					tempClasses.addAll(getClasses(prefixDir, dirname + "/" + files[i].getName(),
						targetClass));
				else if (files[i].getName().endsWith(".class"))
				{
					String fileName = files[i].getName();
					Class< ? > loadedClass =
						Class.forName(packageName + '.'
							+ fileName.substring(0, fileName.length() - 6));
					if (loadedClass.isInterface()
						|| Modifier.isAbstract(loadedClass.getModifiers())
						|| Modifier.isPrivate(loadedClass.getModifiers())
						|| loadedClass.isAnonymousClass() || loadedClass.isMemberClass()
						|| loadedClass.isLocalClass() || loadedClass.isEnum())
						continue;
					if (targetClass.isAssignableFrom(loadedClass))
						tempClasses.add(loadedClass);
				}
			}
		}
		else
		{
			throw new ClassNotFoundException(packageName + " does not appear to be a valid package");
		}

		return tempClasses;
	}
}
