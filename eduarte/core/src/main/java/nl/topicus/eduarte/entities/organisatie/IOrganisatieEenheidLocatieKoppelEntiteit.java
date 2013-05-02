package nl.topicus.eduarte.entities.organisatie;

import java.util.Date;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

/**
 * Interface voor alle entiteiten die een koppeltabel vormen tussen een andere entiteit en
 * organisatie-eenheden en locaties.
 * 
 * @author loite
 */
public interface IOrganisatieEenheidLocatieKoppelEntiteit<T extends IOrganisatieEenheidLocatieKoppelEntiteit<T>>
		extends IdObject, OrganisatieEenheidLocatieProvider
{
	public OrganisatieEenheid getOrganisatieEenheid();

	public Locatie getLocatie();

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid);

	public void setLocatie(Locatie locatie);

	public IOrganisatieEenheidLocatieKoppelbaarEntiteit<T> getEntiteit();

	public boolean isActief(Date peildatum);
}
