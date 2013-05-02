package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

public class LesdagindelingZoekFilter extends AbstractZoekFilter<LesdagIndeling>
{

	private static final long serialVersionUID = 1L;

	private String code;

	private String dag;

	private LesweekIndeling lesweekIndeling;

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return code;
	}

	public void setDag(String dag)
	{
		this.dag = dag;
	}

	public String getDag()
	{
		return dag;
	}

	public void setLesweekIndeling(LesweekIndeling lesweekIndeling)
	{
		this.lesweekIndeling = lesweekIndeling;
	}

	public LesweekIndeling getLesweekIndeling()
	{
		return lesweekIndeling;
	}
}
