package nl.topicus.eduarte.app;

import nl.topicus.cobra.web.components.ResourcePostfixProvider;

import org.apache.wicket.Component;
import org.apache.wicket.Localizer;

public class EduArteLocalizer extends Localizer
{

	public EduArteLocalizer()
	{
	}

	@Override
	protected String getCacheKey(String key, Component component)
	{
		String ret = super.getCacheKey(key, component);
		if (component instanceof ResourcePostfixProvider)
		{
			ret += "-" + ((ResourcePostfixProvider) component).getResourcePostfix();
		}
		return ret;
	}
}
