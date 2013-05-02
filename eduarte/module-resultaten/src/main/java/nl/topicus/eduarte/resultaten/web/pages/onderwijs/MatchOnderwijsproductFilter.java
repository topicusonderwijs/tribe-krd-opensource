package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import nl.topicus.cobra.quartz.CobraJob;
import nl.topicus.cobra.quartz.JobDescriptionFilter;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;

import org.apache.wicket.model.IModel;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

public class MatchOnderwijsproductFilter implements JobDescriptionFilter
{
	private static final long serialVersionUID = 1L;

	private Long onderwijsproductId;

	public MatchOnderwijsproductFilter(Onderwijsproduct product)
	{
		this.onderwijsproductId = product.getId();
	}

	@Override
	public boolean matches(CobraJob job, JobExecutionContext context)
	{
		Resultaatstructuur structuur =
			(Resultaatstructuur) context.getMergedJobDataMap().get("resultaatstructuur");
		return onderwijsproductId.equals(structuur.getOnderwijsproduct().getId());
	}

	@Override
	public boolean matches(Trigger trigger)
	{
		Resultaatstructuur structuur =
			(Resultaatstructuur) getFromJobDataMap(trigger, "resultaatstructuur");
		return onderwijsproductId.equals(structuur.getOnderwijsproduct().getId());
	}

	private Object getFromJobDataMap(Trigger trigger, String key)
	{
		Object ret = trigger.getJobDataMap().get(key);
		return ret instanceof IModel< ? > ? ((IModel< ? >) ret).getObject() : ret;
	}
}
