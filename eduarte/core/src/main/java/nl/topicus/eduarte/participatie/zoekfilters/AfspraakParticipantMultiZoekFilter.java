package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.participatie.AfspraakParticipant;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * Filter dat gebruikt wordt voor de auto complete text field voor afspraakparticipanten.
 * 
 * @author papegaaij
 */
public class AfspraakParticipantMultiZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<AfspraakParticipant>
{
	private static final long serialVersionUID = 1L;

	private int maxResults = 10;

	private String input;

	private Date datum;

	private IModel<Medewerker> medewerkerEigenaar;

	private IModel<Deelnemer> deelnemerEigenaar;

	public AfspraakParticipantMultiZoekFilter()
	{
	}

	public String getInput()
	{
		return input;
	}

	public void setInput(String input)
	{
		this.input = input;
	}

	public int getMaxResults()
	{
		return maxResults;
	}

	public void setMaxResults(int maxResults)
	{
		this.maxResults = maxResults;
	}

	public void setDatum(Date datum)
	{
		this.datum = datum;
	}

	public Date getDatum()
	{
		return datum;
	}

	public void setEigenaar(IdObject eigenaar)
	{
		if (eigenaar instanceof Deelnemer)
		{
			setDeelnemerEigenaar((Deelnemer) eigenaar);
		}
		else
		{
			setMedewerkerEigenaar((Medewerker) eigenaar);
		}
	}

	public Medewerker getMedewerkerEigenaar()
	{
		return getModelObject(medewerkerEigenaar);
	}

	public void setMedewerkerEigenaar(Medewerker medewerkerEigenaar)
	{
		this.medewerkerEigenaar = makeModelFor(medewerkerEigenaar);
	}

	public Deelnemer getDeelnemerEigenaar()
	{
		return getModelObject(deelnemerEigenaar);
	}

	public void setDeelnemerEigenaar(Deelnemer deelnemerEigenaar)
	{
		this.deelnemerEigenaar = makeModelFor(deelnemerEigenaar);
	}
}
