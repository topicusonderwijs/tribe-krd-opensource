package nl.topicus.eduarte.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.entities.personen.Medewerker;

public interface IVerantwoordelijkeUitvoerendeZoekFilter<T> extends DetachableZoekFilter<T>
{
	public Medewerker getVerantwoordelijke();

	public void setVerantwoordelijke(Medewerker verantwoordelijke);

	public Medewerker getUitvoerende();

	public void setUitvoerende(Medewerker uitvoerende);

	public Medewerker getVerantwoordelijkeOfUitvoerende();

	public void setVerantwoordelijkeOfUitvoerende(Medewerker verantwoordelijkeOfUitvoerende);

	public Date getPeildatum();

	public Date getPeilEindDatum();
}
