package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.eduarte.entities.dbs.trajecten.GesprekSamenvattingTemplate;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

public class GesprekSamenvattingTemplateZoekFilter extends
		AbstractZoekFilter<GesprekSamenvattingTemplate>
{
	private static final long serialVersionUID = 1L;

	public GesprekSamenvattingTemplateZoekFilter()
	{
		addOrderByProperty("naam");
	}
}
