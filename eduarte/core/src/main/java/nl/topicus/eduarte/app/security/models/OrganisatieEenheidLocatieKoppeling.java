package nl.topicus.eduarte.app.security.models;

import java.io.Serializable;
import java.util.Date;

import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

public class OrganisatieEenheidLocatieKoppeling implements
		IOrganisatieEenheidLocatieKoppelEntiteit<OrganisatieEenheidLocatieKoppeling>
{
	private static final long serialVersionUID = 1L;

	private OrganisatieEenheid organisatieEenheid;

	private Locatie locatie;

	public OrganisatieEenheidLocatieKoppeling(OrganisatieEenheidLocatieProvider provider)
	{
		this.organisatieEenheid = provider.getOrganisatieEenheid();
		this.locatie = provider.getLocatie();
	}

	public OrganisatieEenheidLocatieKoppeling(OrganisatieEenheid organisatieEenheid, Locatie locatie)
	{
		this.organisatieEenheid = organisatieEenheid;
		this.locatie = locatie;
	}

	@Override
	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	@Override
	public Locatie getLocatie()
	{
		return locatie;
	}

	@Override
	public boolean isActief(Date peildatum)
	{
		return true;
	}

	@Override
	public IOrganisatieEenheidLocatieKoppelbaarEntiteit<OrganisatieEenheidLocatieKoppeling> getEntiteit()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLocatie(Locatie locatie)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Serializable getIdAsSerializable()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Long getVersion()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSaved()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setVersion(Long version)
	{
		throw new UnsupportedOperationException();
	}
}