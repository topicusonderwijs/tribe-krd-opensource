package nl.topicus.eduarte.app.beanpropertyresolvers;

import java.util.Map;

import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.jobs.EduArteJob;

public class EduArteBackgroundPrototypeBeanPropertyResolver extends
		EduArteBackgroundBeanPropertyResolver
{
	public EduArteBackgroundPrototypeBeanPropertyResolver(Object context)
	{
		super(context);
	}

	public EduArteBackgroundPrototypeBeanPropertyResolver(Map<String, Object> context,
			EduArteJob job)
	{
		super(supplementContext(context, job));
	}

	protected static Map<String, Object> supplementContext(Map<String, Object> context,
			EduArteJob job)
	{
		EduArteBackgroundBeanPropertyResolver.supplementContext(context, job);

		context.put("verbintenis", new Verbintenis(new Deelnemer()));
		context.put("groep", new Groep());
		context.put("examendeelname", new Examendeelname());

		return context;
	}
}
