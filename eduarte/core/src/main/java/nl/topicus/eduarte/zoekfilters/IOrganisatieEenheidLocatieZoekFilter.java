package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

public interface IOrganisatieEenheidLocatieZoekFilter<T> extends DetachableZoekFilter<T>,
		OrganisatieEenheidLocatieProvider
{
	public OrganisatieEenheidLocatieAuthorizationContext getAuthorizationContext();

	public void setAuthorizationContext(
			OrganisatieEenheidLocatieAuthorizationContext authorizationContext);
}
