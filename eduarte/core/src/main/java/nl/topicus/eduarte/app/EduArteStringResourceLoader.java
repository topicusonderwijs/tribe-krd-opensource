package nl.topicus.eduarte.app;

import java.util.Locale;

import nl.topicus.cobra.web.components.ResourcePostfixProvider;

import org.apache.wicket.Component;
import org.apache.wicket.resource.loader.ComponentStringResourceLoader;

public class EduArteStringResourceLoader extends ComponentStringResourceLoader
{

	public EduArteStringResourceLoader()
	{
	}

	@Override
	public String loadStringResource(Component component, String key)
	{
		String ret = super.loadStringResource(component, key);
		if (ret == null && component != null)
		{
			return loadStringResourceForComponentClass(component, key);
		}
		return ret;
	}

	private String loadStringResourceForComponentClass(Component component, String key)
	{
		Locale locale = component.getLocale();
		String style = component.getStyle();
		Component curParent = component;
		while (curParent != null)
		{
			Class< ? > checkClass = component.getClass();
			while (checkClass != null)
			{
				String ret =
					loadStringResource(curParent.getClass(),
						checkClass.getSimpleName() + "." + key, locale, style);
				if (ret == null && component instanceof ResourcePostfixProvider)
					ret =
						loadStringResource(curParent.getClass(), checkClass.getSimpleName() + "-"
							+ ((ResourcePostfixProvider) component).getResourcePostfix() + "."
							+ key, locale, style);
				if (ret != null)
					return ret;
				checkClass = checkClass.getSuperclass();
			}
			curParent = curParent.getParent();
		}
		return null;
	}
}
