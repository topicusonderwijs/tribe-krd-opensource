package nl.topicus.eduarte.web.pages.beheer.organisatie;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.eduarte.dao.helpers.SoortContactgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.StandaardContactgegeven;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidAdres;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidContactgegeven;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidLocatie;
import nl.topicus.eduarte.entities.personen.OrganisatieEenheidContactPersoon;
import nl.topicus.eduarte.web.models.secure.AbstractEntiteitEditPageModel;
import nl.topicus.eduarte.zoekfilters.SoortContactgegevenZoekFilter;

import org.apache.wicket.model.PropertyModel;

/**
 * Model om een nieuwe OrganisatieEenheid aan te maken
 * 
 * @author hoeve
 */
public class OrganisatieEenheidModel extends AbstractEntiteitEditPageModel<OrganisatieEenheid>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creert een model op basis van een bestaande OrganisatieEenheid.
	 */
	public OrganisatieEenheidModel(OrganisatieEenheid organisatieEenheid)
	{
		super(organisatieEenheid.getBegindatum());

		entiteitModel =
			ModelFactory.getCompoundChangeRecordingModel(organisatieEenheid, getEntiteitManager());
		voegContactgegevensToe(organisatieEenheid);

	}

	@Override
	protected void setEntiteitManager()
	{
		entiteitManager =
			new DefaultModelManager(OrganisatieEenheidAdres.class, Adres.class,
				OrganisatieEenheidContactPersoon.class, OrganisatieEenheidLocatie.class,
				OrganisatieEenheidContactgegeven.class, OrganisatieEenheid.class);
	}

	@Override
	protected OrganisatieEenheid createDefaultT()
	{
		OrganisatieEenheid organisatieEenheid = new OrganisatieEenheid();
		organisatieEenheid.setBegindatum(getRegistratieDatum());

		voegContactgegevensToe(organisatieEenheid);

		return organisatieEenheid;
	}

	private void voegContactgegevensToe(OrganisatieEenheid organisatieEenheid)
	{
		List<SoortContactgegeven> huidigeSoortContactgegevens =
			new ArrayList<SoortContactgegeven>();

		for (OrganisatieEenheidContactgegeven huidigGegeven : organisatieEenheid
			.getContactgegevens())
			if (!huidigeSoortContactgegevens.contains(huidigGegeven.getSoortContactgegeven()))
				huidigeSoortContactgegevens.add(huidigGegeven.getSoortContactgegeven());

		SoortContactgegevenDataAccessHelper soortHelper =
			DataAccessRegistry.getHelper(SoortContactgegevenDataAccessHelper.class);
		SoortContactgegevenZoekFilter filter = new SoortContactgegevenZoekFilter();
		filter.setStandaardContactgegeven(StandaardContactgegeven.StandaardTonenBijOrganisatie,
			StandaardContactgegeven.StandaardTonen);
		filter.setActief(true);

		List<SoortContactgegeven> soorten = soortHelper.list(filter);
		for (SoortContactgegeven soort : soorten)
		{
			if (!huidigeSoortContactgegevens.contains(soort))
				voegContactgegevenToeAanOrganisatieEenheid(organisatieEenheid, soort);
		}

	}

	private void voegContactgegevenToeAanOrganisatieEenheid(OrganisatieEenheid organisatieEenheid,
			SoortContactgegeven soort)
	{
		OrganisatieEenheidContactgegeven gegeven = new OrganisatieEenheidContactgegeven();
		gegeven.setOrganisatieEenheid(organisatieEenheid);
		gegeven.setSoortContactgegeven(soort);
		organisatieEenheid.getContactgegevens().add(gegeven);
	}

	public PropertyModel<List<OrganisatieEenheidLocatie>> getOrganisatieEenheidLocatieListModel()
	{
		return new PropertyModel<List<OrganisatieEenheidLocatie>>(getEntiteitModel(), "locaties");
	}

	public PropertyModel<List<OrganisatieEenheidContactgegeven>> getOrganisatieEenheidContactgegevenListModel()
	{
		return new PropertyModel<List<OrganisatieEenheidContactgegeven>>(getEntiteitModel(),
			"contactgegevens");
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return getEntiteitModel().getObject();
	}

	/**
	 * Controleert het model en sub objecten voordat alles wordt opgeslagen.
	 */
	@Override
	public void save()
	{
		getEntiteitModel().saveObject();
		getEntiteitModel().getObject().commit();
	}

	public void addOrganisatieEenheidLocatie(OrganisatieEenheidLocatie modelObject)
	{
		OrganisatieEenheid organisatieEenheid = getOrganisatieEenheid();
		modelObject.setOrganisatieEenheid(organisatieEenheid);
		if (modelObject.getBegindatum() == null)
			modelObject.setBegindatum(getRegistratieDatum());

		organisatieEenheid.getLocaties().add(modelObject);
	}

	public void removeOrganisatieEenheidLocatie(OrganisatieEenheidLocatie object)
	{
		OrganisatieEenheid eenheid = getOrganisatieEenheid();
		eenheid.getLocaties().remove(object);
	}
}
