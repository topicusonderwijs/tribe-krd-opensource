package nl.topicus.eduarte.zoekfilters;

import java.util.List;

import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.model.IModel;

public class ResultaatZoekFilter extends AbstractZoekFilter<Resultaat>
{
	private static final long serialVersionUID = 1L;

	private ToetsZoekFilter toetsZoekFilter;

	private IModel<List<Deelnemer>> deelnemers;

	private IModel<List<Toets>> toetsen;

	private Boolean actueel;

	public ResultaatZoekFilter(ToetsZoekFilter toetsZoekFilter, List<Deelnemer> deelnemers)
	{
		setToetsZoekFilter(toetsZoekFilter);
		setDeelnemers(deelnemers);
	}

	public ResultaatZoekFilter(List<Toets> toetsen)
	{
		setToetsen(toetsen);
	}

	public void setToetsZoekFilter(ToetsZoekFilter toetsZoekFilter)
	{
		this.toetsZoekFilter = toetsZoekFilter;
	}

	public ToetsZoekFilter getToetsZoekFilter()
	{
		return toetsZoekFilter;
	}

	public List<Deelnemer> getDeelnemers()
	{
		return getModelObject(deelnemers);
	}

	public void setDeelnemers(List<Deelnemer> deelnemers)
	{
		this.deelnemers = makeModelFor(deelnemers);
	}

	public List<Toets> getToetsen()
	{
		return getModelObject(toetsen);
	}

	public void setToetsen(List<Toets> toetsen)
	{
		this.toetsen = makeModelFor(toetsen);
	}

	public boolean isBeperkt()
	{
		return (toetsZoekFilter != null && toetsZoekFilter.isBeperktInResultaatstructuur())
			|| (getDeelnemers() != null && !getDeelnemers().isEmpty())
			|| (getToetsen() != null && !getToetsen().isEmpty());
	}

	public Boolean getActueel()
	{
		return actueel;
	}

	public void setActueel(Boolean actueel)
	{
		this.actueel = actueel;
	}
}
