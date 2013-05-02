package nl.topicus.eduarte.app.beanpropertyresolvers;

import java.util.Map;

import nl.topicus.cobra.templates.resolvers.BeanPropertyResolver;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;

public class EduArteWebServiceBeanPropertyResolver extends BeanPropertyResolver
{
	public EduArteWebServiceBeanPropertyResolver(Object context)
	{
		super(context);
	}

	public EduArteWebServiceBeanPropertyResolver(Map<String, Object> context)
	{
		super(supplementContext(context));
	}

	protected static Map<String, Object> supplementContext(Map<String, Object> context)
	{
		context.put("huidigeorganisatie", EduArteContext.get().getOrganisatie().doUnproxy());
		context.put("DATE", TimeUtil.getInstance().currentDateTime());
		context.put("datum", TimeUtil.getInstance().currentDateTime());
		return context;
	}
}
