package nl.topicus.eduarte.app.beanpropertyresolvers;

import java.util.Map;

import nl.topicus.cobra.templates.resolvers.BeanPropertyResolver;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.jobs.EduArteJob;

import org.apache.commons.lang.NullArgumentException;

public class EduArteBackgroundBeanPropertyResolver extends BeanPropertyResolver
{
	public EduArteBackgroundBeanPropertyResolver(Object context)
	{
		super(context);
	}

	public EduArteBackgroundBeanPropertyResolver(Map<String, Object> context, EduArteJob job)
	{
		super(supplementContext(context, job));
	}

	protected static Map<String, Object> supplementContext(Map<String, Object> context,
			EduArteJob job)
	{

		if (job == null)
			throw new NullArgumentException("geen job beschikbaar");

		context.put("huidigemedewerker", job.getMedewerker());
		context.put("huidigeorganisatie", job.getOrganisatie());
		context.put("huidigaccount", job.getAccount());

		if (job.getMedewerker() != null)
			context.put("AUTHOR", job.getMedewerker().getPersoon().getVolledigeNaam());
		else if (job.getMedewerker() != null)
			context.put("AUTHOR", job.getAccount().getGebruikersnaam());

		context.put("DATE", TimeUtil.getInstance().currentDateTime());
		context.put("datum", TimeUtil.getInstance().currentDateTime());

		return context;
	}
}
