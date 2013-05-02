package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;

import org.apache.wicket.model.IModel;

public class SchaalwaardeZoekFilter extends AbstractZoekFilter<Schaalwaarde>
{
	private static final long serialVersionUID = 1L;

	private IModel<Schaal> schaal;

	public SchaalwaardeZoekFilter(Schaal schaal)
	{
		setSchaal(schaal);
	}

	public void setSchaal(Schaal schaal)
	{
		this.schaal = makeModelFor(schaal);
	}

	public Schaal getSchaal()
	{
		return getModelObject(schaal);
	}
}
