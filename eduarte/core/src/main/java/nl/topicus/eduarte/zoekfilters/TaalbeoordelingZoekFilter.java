package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.ModerneTaal;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalbeoordeling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Uitstroom;

import org.apache.wicket.model.IModel;

/**
 * @author vandenbrink
 */
public class TaalbeoordelingZoekFilter extends AbstractZoekFilter<Taalbeoordeling>
{
	private static final long serialVersionUID = 1L;

	private IModel<Deelnemer> deelnemer;

	private IModel<Uitstroom> uitstroom;

	private IModel<Medewerker> medewerker;

	private IModel<ModerneTaal> taal;

	public TaalbeoordelingZoekFilter()
	{
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Medewerker getMedewerker()
	{
		return getModelObject(medewerker);
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = makeModelFor(medewerker);
	}

	public Uitstroom getUitstroom()
	{
		return getModelObject(uitstroom);
	}

	public void setUitstroom(Uitstroom uitstroom)
	{
		this.uitstroom = makeModelFor(uitstroom);
	}

	public ModerneTaal getTaal()
	{
		return getModelObject(taal);
	}

	public void setTaal(ModerneTaal taal)
	{
		this.taal = makeModelFor(taal);
	}
}
