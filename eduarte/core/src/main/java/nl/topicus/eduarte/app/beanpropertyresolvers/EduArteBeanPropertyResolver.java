package nl.topicus.eduarte.app.beanpropertyresolvers;

import java.util.Map;

import nl.topicus.cobra.templates.resolvers.BeanPropertyResolver;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;

public class EduArteBeanPropertyResolver extends BeanPropertyResolver
{
	public EduArteBeanPropertyResolver(Object context)
	{
		super(context);
	}

	public EduArteBeanPropertyResolver(Map<String, Object> context)
	{
		super(supplementContext(context));
	}

	protected static Map<String, Object> supplementContext(Map<String, Object> context)
	{
		context.put("huidigemedewerker", EduArteContext.get().getMedewerker().doUnproxy());
		context.put("huidigeorganisatie", EduArteContext.get().getOrganisatie().doUnproxy());
		context.put("huidigaccount", EduArteContext.get().getAccount().doUnproxy());

		if (EduArteContext.get().getMedewerker() != null)
			context.put("AUTHOR", EduArteContext.get().getMedewerker().getPersoon()
				.getVolledigeNaam());
		else if (EduArteContext.get().getAccount() != null)
			context.put("AUTHOR", EduArteContext.get().getAccount().getGebruikersnaam());

		context.put("DATE", TimeUtil.getInstance().currentDateTime());
		context.put("datum", TimeUtil.getInstance().currentDateTime());

		return context;
	}
}
