package nl.topicus.cobra.templates;

import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;

public class ExtractPropertyUtil
{
	private final static int MAX_LEVELS = 50;

	private boolean flat = true;

	private boolean showTypes = false;

	private boolean rememberDone = false;

	private final static boolean ONLY_EXPORTABLE = true;

	private Set<Class< ? >> hiddenClasses;

	private static boolean hasToString(Class< ? > clazz)
	{
		try
		{
			Method toString = clazz.getDeclaredMethod("toString", new Class[] {});
			if (Modifier.isPublic(toString.getModifiers()))
				return true;
		}
		catch (NoSuchMethodException e)
		{
			// doe niets
		}
		return false;

	}

	public ExtractPropertyUtil()
	{
	}

	public ExtractPropertyUtil(boolean flat, boolean showTypes, boolean rememberDone)
	{
		this.flat = flat;
		this.showTypes = showTypes;
		this.rememberDone = rememberDone;
	}

	public ExtractPropertyUtil(boolean flat, boolean showTypes, boolean rememberDone,
			Class< ? >... hiddenClass)
	{
		this(flat, showTypes, rememberDone);
		hiddenClasses = new HashSet<Class< ? >>(Arrays.asList(hiddenClass));
	}

	public void extractClass(Class< ? > clazz, String property, Writer writer)
	{
		extractClass(clazz, property, new HashSet<Class< ? >>(), 0, new PrintWriter(writer));
	}

	public Set<Class< ? >> extractClass(Class< ? > clazz, String prefix, Set<Class< ? >> done,
			int level, PrintWriter pw)
	{
		if (isSimpleType(clazz))
		{
			pw.println(prefix);
			return done;
		}
		if (!isSimpleType(clazz) && ONLY_EXPORTABLE
			&& clazz.getAnnotation(Exportable.class) == null)
			return done;

		Set<Class< ? >> newDone = done;
		List<Class< ? >> toDo = new ArrayList<Class< ? >>();
		if (flat)
		{
			newDone = new HashSet<Class< ? >>(done);
		}
		else
		{
			pw.println();
			pw.println(clazz.getSimpleName());
		}
		newDone.add(clazz);

		List<Method> methods = Arrays.asList(clazz.getMethods());
		for (Method method : methods)
		{
			Exportable annotation = method.getAnnotation(Exportable.class);
			if (ONLY_EXPORTABLE && annotation == null)
				continue;
			String name = method.getName();
			// String description = "";
			// if (annotation != null)
			// description = annotation.omschrijving();
			if ((name.startsWith("get") || name.startsWith("is")))
			{
				String propertyName;
				if (name.startsWith("is"))
					propertyName = StringUtil.firstCharLowercase(name.substring(2));
				else
					propertyName = StringUtil.firstCharLowercase(name.substring(3));
				Class< ? >[] parameters = method.getParameterTypes();
				int count = parameters.length;
				if (count != 0)
				{
					StringBuilder builder = new StringBuilder(propertyName);
					builder.append('(');
					for (int i = 0; i < count; i++)
					{
						if (i > 0)
							builder.append(',');
						builder.append(parameters[i].getSimpleName());
					}
					builder.append(')');
					propertyName = builder.toString();
				}
				String fullPropertyName = prefix + "." + propertyName;
				Type generics = method.getGenericReturnType();
				Class< ? > returnType = method.getReturnType();
				boolean toString = hasToString(returnType);
				while (returnType != null)
				{
					// while lusje om te kunnen nesten bij lijstnotaties
					if (isHiddenClass(returnType))
					{
						// do nothing
					}
					else if (returnType.isArray() || Iterable.class.isAssignableFrom(returnType))
					{
						if (generics != null && generics instanceof ParameterizedType)
						{
							Type actual[] = ((ParameterizedType) generics).getActualTypeArguments();
							if (actual.length == 1 && actual[0] instanceof Class< ? >)
							{
								Class< ? > itemType = (Class< ? >) actual[0];
								returnType = itemType;
								fullPropertyName = fullPropertyName + "[*]";
								continue;
							}
							else if (actual.length == 1 && actual[0] instanceof WildcardType)
							{
								Type bounds[] = ((WildcardType) actual[0]).getUpperBounds();
								if (bounds.length > 0 && bounds[0] instanceof Class< ? >)
								{
									returnType = (Class< ? >) bounds[0];
									fullPropertyName = fullPropertyName + "[*]";
									continue;
								}
							}
						}
					}
					else if (isSimpleType(returnType))
					{
						pw.println(fullPropertyName);
					}

					else if (((!rememberDone && !done.contains(returnType)) || (rememberDone && !newDone
						.contains(returnType)))
						&& level < MAX_LEVELS)
					{
						if (toString)
						{
							pw.print(fullPropertyName);
							if (showTypes)
								pw.print(" (" + returnType.getSimpleName() + ")");
							pw.println();
						}
						newDone.add(returnType);
						if (flat)
							extractClass(returnType, fullPropertyName, newDone, level + 1, pw);
						else if (!toDo.contains(returnType))
							toDo.add(returnType);
					}
					// else if (showTypes)
					// {
					// if (flat)
					// {
					// pw.println(fullPropertyName + "... (" + (toString ? "ook " : "")
					// + returnType.getSimpleName() + ")");
					// }
					// else
					// pw.println(fullPropertyName + " (" + (toString ? "ook " : "")
					// + returnType.getSimpleName() + ")");
					// }
					else if (toString)
					{
						pw.println(fullPropertyName);
					}
					else
					{
						if (flat)
						{
							pw.println(fullPropertyName + "...");
						}
					}

					returnType = null; // klaar
				}
			}
		}
		for (Class< ? > nextClass : toDo)
		{
			extractClass(nextClass, ""/* "(" + nextClass.getSimpleName() + ")" */, newDone,
				level + 1, pw);
		}
		return newDone;
	}

	private boolean isSimpleType(Class< ? > returnType)
	{
		return returnType.isPrimitive() || returnType.equals(String.class)
			|| returnType.equals(Boolean.class) || returnType.equals(java.util.Date.class)
			|| Number.class.isAssignableFrom(returnType) || Enum.class.isAssignableFrom(returnType);
	}

	public boolean isFlat()
	{
		return flat;
	}

	public void setFlat(boolean flat)
	{
		this.flat = flat;
	}

	public boolean isShowTypes()
	{
		return showTypes;
	}

	public void setShowTypes(boolean showTypes)
	{
		this.showTypes = showTypes;
	}

	public void setHiddenClasses(Set<Class< ? >> hiddenClasses)
	{
		this.hiddenClasses = hiddenClasses;
	}

	public Set<Class< ? >> getHiddenClasses()
	{
		return hiddenClasses;
	}

	public boolean isHiddenClass(Class< ? > clazz)
	{
		if (hiddenClasses == null)
			return false;

		for (Class< ? > cls : hiddenClasses)
			if (cls.isAssignableFrom(clazz))
				return true;

		return false;
	}

	public boolean isRememberDone()
	{
		return rememberDone;
	}

	public void setRememberDone(boolean rememberDone)
	{
		this.rememberDone = rememberDone;
	}

}
