package nl.topicus.eduarte.zoekfilters;

import java.util.List;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.IJkpunt;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.MeeteenheidKoppelType;

import org.apache.wicket.model.IModel;

/**
 * @author vandenbrink
 */
public class IJkpuntZoekFilter extends NiveauVerzamelingZoekFilter<IJkpunt>
{

	public IJkpuntZoekFilter()
	{
	}

	private static final long serialVersionUID = 1L;

	private IModel<Opleiding> opleiding;

	private MeeteenheidKoppelType type;

	private boolean deelnemerNullOrEquals = false;

	private boolean filterOnDeelnemer = true;

	private IModel<List<Verbintenis>> verbintenissen;

	public boolean isDeelnemerNullOrEquals()
	{
		return deelnemerNullOrEquals;
	}

	public void setDeelnemerNullOrEquals(boolean deelnemerNullOrEquals)
	{
		this.deelnemerNullOrEquals = deelnemerNullOrEquals;
	}

	public MeeteenheidKoppelType getType()
	{
		return type;
	}

	public void setType(MeeteenheidKoppelType type)
	{
		this.type = type;
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public List<Verbintenis> getVerbintenissen()
	{
		return getModelObject(verbintenissen);
	}

	public void setVerbintenissen(List<Verbintenis> verbintenissen)
	{
		this.verbintenissen = makeModelFor(verbintenissen);
	}

	public void setFilterOnDeelnemer(boolean filterOnDeelnemer)
	{
		this.filterOnDeelnemer = filterOnDeelnemer;
	}

	public boolean isFilterOnDeelnemer()
	{
		return filterOnDeelnemer;
	}
}
