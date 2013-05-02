/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.components.ISecureComponent;
import org.apache.wicket.security.components.SecureComponentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper voor wicket componenten. Geen factory.
 * 
 * @author marrink
 */
public final class ComponentUtil
{
	private static final Logger LOG = LoggerFactory.getLogger(ComponentUtil.class);

	private static Method onPopulateMethod;

	/**
	 * 
	 */
	private ComponentUtil()
	{
	}

	/**
	 * Haalt de security check op van een component. Controleert of de component een
	 * {@link ISecureComponent} is voordat de metadata gebruikt wordt. Let op gebruik
	 * nooit deze methode vanuit 1 van de methoden in {@link ISecureComponent} maar
	 * gebruik daar altijd de {@link SecureComponentHelper} voor. Om stackoverflows te
	 * voorkomen.
	 * 
	 * @param component
	 * @return check van de component of null als deze leeg is.
	 */
	public static ISecurityCheck getSecurityCheck(Component component)
	{
		if (component instanceof ISecureComponent)
			return ((ISecureComponent) component).getSecurityCheck();
		return SecureComponentHelper.getSecurityCheck(component);
	}

	/**
	 * Zet een security check op een component. Controleert of de component een
	 * {@link ISecureComponent} is voordat de metadata gebruikt wordt. Let op gebruik
	 * nooit deze methode vanuit 1 van de methoden in {@link ISecureComponent} maar
	 * gebruik daar altijd de {@link SecureComponentHelper} voor. Om stackoverflows te
	 * voorkomen.
	 * 
	 * @param component
	 * @param check
	 * @return de component
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Component> T setSecurityCheck(T component, ISecurityCheck check)
	{
		if (component instanceof ISecureComponent)
		{
			((ISecureComponent) component).setSecurityCheck(check);
			return component;
		}
		return (T) SecureComponentHelper.setSecurityCheck(component, check);
	}

	/**
	 * Triggerd de onPopulate methode van een {@link AbstractRepeater} de basis class voor
	 * zowel {@link DataView} als {@link ListView}, waardoor alle items gepopuleerd
	 * worden. Dit is een smerige hack maar soms noodzakelijk.
	 * 
	 * @param view
	 */
	public static void invokePopulate(AbstractRepeater view)
	{
		try
		{
			if (onPopulateMethod == null)
			{
				onPopulateMethod = AbstractRepeater.class.getDeclaredMethod("onPopulate");
				onPopulateMethod.setAccessible(true);
			}
			onPopulateMethod.invoke(view);
		}
		catch (SecurityException e)
		{
			LOG.debug(e.getMessage(), e);
		}
		catch (NoSuchMethodException e)
		{
			LOG.debug(e.getMessage(), e);
		}
		catch (IllegalArgumentException e)
		{
			LOG.debug(e.getMessage(), e);
		}
		catch (IllegalAccessException e)
		{
			LOG.debug(e.getMessage(), e);
		}
		catch (InvocationTargetException e)
		{
			LOG.debug(e.getMessage(), e);
		}
	}

	/**
	 * Detacht het object zonder een {@link NullPointerException} te gooien als deze
	 * <code>null</code> is.
	 * <p>
	 * Gebruik:
	 * 
	 * <pre>
	 * public void onDetach()
	 * {
	 * 	if (myModel != null)
	 * 		myModel.detach();
	 * }
	 * </pre>
	 * 
	 * wordt vervangen door:
	 * 
	 * <pre>
	 * public void onDetach()
	 * {
	 * 	ResourceUtil.detachQuietly(myModel);
	 * }
	 * </pre>
	 * 
	 * @param detachable
	 */
	@SuppressWarnings("unchecked")
	public static void detachQuietly(Object detachable)
	{
		if (detachable instanceof Component)
		{
			((Component) detachable).detach();
		}
		else if (detachable instanceof IDetachable)
		{
			((IDetachable) detachable).detach();
		}
		else if (detachable instanceof Map)
		{
			for (Map.Entry< ? , ? > entry : ((Map< ? , ? >) detachable).entrySet())
			{
				detachQuietly(entry.getKey());
				detachQuietly(entry.getValue());
			}
		}
		else if (detachable instanceof Iterable)
		{
			Iterator<Object> iter = ((Iterable) detachable).iterator();
			while (iter.hasNext())
			{
				detachQuietly(iter.next());
			}
		}
	}

	/**
	 * Detacht de velden van de <code>detachable</code>.
	 * 
	 * @param detachable
	 */
	@SuppressWarnings("unchecked")
	public static void detachFields(Object detachable)
	{
		if (detachable == null)
			return;

		Class clazz = detachable.getClass();
		while (clazz != null)
		{
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++)
			{
				Field field = fields[i];
				// synthetische velden zoals zoals this$1 niet detachen
				if (!field.isSynthetic()
					&& (IDetachable.class.isAssignableFrom(field.getType()) || Component.class
						.isAssignableFrom(field.getType())))
				{
					// het is een IModel veld dus:
					boolean original = field.isAccessible();
					field.setAccessible(true);
					try
					{
						detachQuietly(field.get(detachable));
					}
					catch (Exception e)
					{
						LOG.error("Kan " + field.getName() + " niet detachen", e);
					}
					finally
					{
						field.setAccessible(original);
					}
				}
			}
			clazz = clazz.getSuperclass();

		}

	}

	/**
	 * Zet de maximum lengte voor een bepaald veld op de component. Hiervoor wordt gebruik
	 * gemaakt van de hibernate annotaties. Indien de lengte niet gespecificeerd is op de
	 * class wordt default 255 gebruikt. Voor de veldnaam wordt het id van de component
	 * genomen.
	 * 
	 * @param component
	 *            component waarvan de maximum lengte ingesteld moet worden
	 * @param objClass
	 *            hibernate entiteit
	 * @return de component
	 */
	public static final <T extends Component> T fixLength(T component, Class< ? > objClass)
	{
		if (component != null)
			ComponentUtil.fixLength(component, objClass, component.getId(), 255);
		return component;
	}

	/**
	 * Zet de maximum lengte voor een bepaald veld op de component. Hiervoor wordt gebruik
	 * gemaakt van de hibernate annotaties. Indien de lengte niet gespecificeerd is op de
	 * class wordt default 255 gebruikt.
	 * 
	 * @param component
	 *            component waarvan de maximum lengte ingesteld moet worden
	 * @param objClass
	 *            hibernate entiteit
	 * @param property
	 *            velnaam op de class
	 * @return de component
	 */
	public static final <T extends Component> T fixLength(T component, Class< ? > objClass,
			String property)
	{
		return ComponentUtil.fixLength(component, objClass, property, 255);
	}

	/**
	 * Zet de maximum lengte voor een bepaald veld op de component. Hiervoor wordt gebruik
	 * gemaakt van de hibernate annotaties. Voor de veldnaam wordt het id van de component
	 * genomen.
	 * 
	 * @param component
	 *            component waarvan de maximum lengte ingesteld moet worden
	 * @param objClass
	 *            hibernate entiteit
	 * @param defaultLength
	 *            de te gebruiken lengte als deze niet gespecificeerd is.
	 * @return de component
	 */
	public static final <T extends Component> T fixLength(T component, Class< ? > objClass,
			int defaultLength)
	{
		if (component != null)
			ComponentUtil.fixLength(component, objClass, component.getId(), defaultLength);
		return component;
	}

	/**
	 * Zet de maximum lengte voor de invoer van de component.
	 * 
	 * @param component
	 *            component waarvan de maximum lengte ingesteld moet worden hibernate
	 *            entiteit
	 * @param length
	 *            de maximum lengte.
	 * @return de component
	 */
	public static final <T extends Component> T fixLength(T component, int length)
	{
		if (component != null)
			ComponentUtil.fixLength(component, null, component.getId(), length);
		return component;
	}

	/**
	 * Zet de maximum lengte voor een bepaald veld op de component. Hiervoor wordt gebruik
	 * gemaakt van de hibernate annotaties.
	 * 
	 * @param component
	 *            component waarvan de maximum lengte ingesteld moet worden
	 * @param objClass
	 *            hibernate entiteit
	 * @param property
	 *            velnaam op de class
	 * @param defaultLength
	 *            de te gebruiken lengte als deze niet gespecificeerd is.
	 * @return de component
	 */
	public static final <T extends Component> T fixLength(T component, Class< ? > objClass,
			String property, int defaultLength)
	{
		if (component != null)
			component.add(new SimpleAttributeModifier("maxlength", ""
				+ ComponentUtil.findLengthInAnnotations(objClass, property, defaultLength)));
		return component;
	}

	public static int findLengthInAnnotations(Class< ? > objClass, String property)
	{
		return ComponentUtil.findLengthInAnnotations(objClass, property, 255);
	}

	private static int findLengthInAnnotations(Class< ? > objClass, String property,
			int defaultLength)
	{
		if (objClass == null || StringUtil.isEmpty(property))
			return defaultLength;

		Field field = null;
		try
		{
			Class< ? > test = objClass;
			while (test != Object.class)
			{
				try
				{
					field = test.getDeclaredField(property);
					break;
				}
				catch (NoSuchFieldException e)
				{
					test = test.getSuperclass();
				}
			}
			if (field != null)
			{
				javax.persistence.Column column =
					field.getAnnotation(javax.persistence.Column.class);
				if (column != null)
					return column.length(); // default value for length = 255
			}
			else
				return defaultLength;
		}
		catch (SecurityException e)
		{
			LOG.warn(e.getMessage(), e);
		}
		return defaultLength;
	}
}
