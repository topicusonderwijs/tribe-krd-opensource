package nl.topicus.eduarte.entities.organisatie;

import java.util.List;

public interface IOrganisatieEenheidLocatieKoppelbaarEntiteit<T extends IOrganisatieEenheidLocatieKoppelEntiteit<T>>
{
	public List<T> getOrganisatieEenheidLocatieKoppelingen();
}
