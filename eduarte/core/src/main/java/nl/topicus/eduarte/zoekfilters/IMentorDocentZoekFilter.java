package nl.topicus.eduarte.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.entities.personen.Medewerker;

public interface IMentorDocentZoekFilter<T> extends DetachableZoekFilter<T>
{
	public Medewerker getMentor();

	public void setMentor(Medewerker mentor);

	public Medewerker getDocent();

	public void setDocent(Medewerker docent);

	public Medewerker getMentorOfDocent();

	public void setMentorOfDocent(Medewerker mentorOfDocent);

	public Date getPeildatum();

	public Date getPeilEindDatum();
}
