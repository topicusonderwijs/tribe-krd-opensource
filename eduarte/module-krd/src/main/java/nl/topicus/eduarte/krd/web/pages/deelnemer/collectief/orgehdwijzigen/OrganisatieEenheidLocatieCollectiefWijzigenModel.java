package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.orgehdwijzigen;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

import org.apache.wicket.model.IModel;

public class OrganisatieEenheidLocatieCollectiefWijzigenModel implements
		IModel<OrganisatieEenheidLocatieProvider>, OrganisatieEenheidLocatieProvider
{
	private static final long serialVersionUID = 1L;

	private IModel<OrganisatieEenheid> organisatieEenheid;

	private IModel<Locatie> locatie;

	public OrganisatieEenheidLocatieCollectiefWijzigenModel()
	{
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		if (organisatieEenheid == null)
			organisatieEenheid = ModelFactory.getModel(null);
		return organisatieEenheid.getObject();
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = ModelFactory.getModel(organisatieEenheid);
	}

	public Locatie getLocatie()
	{
		if (locatie == null)
			locatie = ModelFactory.getModel(null);
		return locatie.getObject();
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = ModelFactory.getModel(locatie);

	}

	@Override
	public OrganisatieEenheidLocatieProvider getObject()
	{
		return this;
	}

	@Override
	public void setObject(OrganisatieEenheidLocatieProvider object)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(organisatieEenheid);
		ComponentUtil.detachQuietly(locatie);
	}

}