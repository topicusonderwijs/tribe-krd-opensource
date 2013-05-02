package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Beoordeling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.BeoordelingsType;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieNiveauVerzameling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Groepsbeoordeling;

import org.apache.wicket.model.IModel;

/**
 * @author vandenbrink
 */
public class BeoordelingZoekFilter extends NiveauVerzamelingZoekFilter<Beoordeling>
{

	private static final long serialVersionUID = 1L;

	private IModel<Medewerker> medewerker;

	private IModel<Beoordeling> opgenomenIn;

	private IModel<Groep> groep;

	private IModel<Groepsbeoordeling> groepsBeoordeling;

	private BeoordelingsType type;

	private Boolean opgenomenInIsNull;

	private Class< ? extends CompetentieNiveauVerzameling> subClass;

	public BeoordelingZoekFilter(Class< ? extends CompetentieNiveauVerzameling> subClass)
	{
		this.subClass = subClass;
		addOrderByProperty("id");
	}

	public Class< ? extends CompetentieNiveauVerzameling> getSubClass()
	{
		return subClass;
	}

	public void setSubClass(Class< ? extends CompetentieNiveauVerzameling> subClass)
	{
		this.subClass = subClass;
	}

	public Medewerker getMedewerker()
	{
		return getModelObject(medewerker);
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = makeModelFor(medewerker);
	}

	public Beoordeling getOpgenomenIn()
	{
		return getModelObject(opgenomenIn);
	}

	public void setOpgenomenIn(Beoordeling opgenomenIn)
	{
		this.opgenomenIn = makeModelFor(opgenomenIn);
	}

	public BeoordelingsType getType()
	{
		return type;
	}

	public void setType(BeoordelingsType type)
	{
		this.type = type;
	}

	public Boolean getOpgenomenInIsNull()
	{
		return opgenomenInIsNull;
	}

	public void setOpgenomenInIsNull(Boolean opgenomenInIsNull)
	{
		this.opgenomenInIsNull = opgenomenInIsNull;
	}

	public Groep getGroep()
	{
		return getModelObject(groep);
	}

	public void setGroep(Groep groep)
	{
		this.groep = makeModelFor(groep);
	}

	public Groepsbeoordeling getGroepsbeoordeling()
	{
		return getModelObject(groepsBeoordeling);
	}

	public void setGroepsbeoordeling(Groepsbeoordeling groepsBeoordeling)
	{
		this.groepsBeoordeling = makeModelFor(groepsBeoordeling);
	}
}
