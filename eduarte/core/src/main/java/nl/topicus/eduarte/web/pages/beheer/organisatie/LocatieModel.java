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
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.LocatieAdres;
import nl.topicus.eduarte.entities.organisatie.LocatieContactgegeven;
import nl.topicus.eduarte.web.models.secure.AbstractEntiteitEditPageModel;
import nl.topicus.eduarte.zoekfilters.SoortContactgegevenZoekFilter;

import org.apache.wicket.model.PropertyModel;

/**
 * Model om een nieuwe Locatie aan te maken of een bestaande te bewerken.
 * 
 * @author hoeve
 */
public class LocatieModel extends AbstractEntiteitEditPageModel<Locatie>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creert een model op basis van een bestaande Locatie.
	 */
	public LocatieModel(Locatie locatie)
	{
		super(locatie.getBegindatum());

		entiteitModel = ModelFactory.getCompoundChangeRecordingModel(locatie, getEntiteitManager());
		voegContactgegevensToe(locatie);
	}

	@Override
	protected void setEntiteitManager()
	{
		entiteitManager =
			new DefaultModelManager(LocatieAdres.class, Adres.class, SoortContactgegeven.class,
				LocatieContactgegeven.class, Locatie.class);
	}

	@Override
	protected Locatie createDefaultT()
	{
		Locatie locatie = new Locatie();
		locatie.setBegindatum(getRegistratieDatum());

		voegContactgegevensToe(locatie);

		return locatie;
	}

	private void voegContactgegevensToe(Locatie locatie)
	{
		List<SoortContactgegeven> huidigeSoortContactgegevens =
			new ArrayList<SoortContactgegeven>();

		for (LocatieContactgegeven huidigGegeven : locatie.getContactgegevens())
			if (!huidigeSoortContactgegevens.contains(huidigGegeven.getSoortContactgegeven()))
				huidigeSoortContactgegevens.add(huidigGegeven.getSoortContactgegeven());

		SoortContactgegevenDataAccessHelper soortHelper =
			DataAccessRegistry.getHelper(SoortContactgegevenDataAccessHelper.class);
		SoortContactgegevenZoekFilter filter = new SoortContactgegevenZoekFilter();
		filter.setStandaardContactgegeven(StandaardContactgegeven.StandaardTonenBijPersoon,
			StandaardContactgegeven.StandaardTonen);
		filter.setActief(true);

		List<SoortContactgegeven> soorten = soortHelper.list(filter);
		for (SoortContactgegeven soort : soorten)
		{
			if (!huidigeSoortContactgegevens.contains(soort))
				voegContactgegevenToeAanLocatie(locatie, soort);
		}
	}

	private void voegContactgegevenToeAanLocatie(Locatie locatie, SoortContactgegeven soort)
	{
		LocatieContactgegeven gegeven = new LocatieContactgegeven();
		gegeven.setLocatie(locatie);
		gegeven.setSoortContactgegeven(soort);
		locatie.getContactgegevens().add(gegeven);
	}

	public PropertyModel<List<LocatieContactgegeven>> getLocatieContactgegevenListModel()
	{
		return new PropertyModel<List<LocatieContactgegeven>>(getEntiteitModel(), "contactgegevens");
	}

	public Locatie getLocatie()
	{
		return getObject();
	}

	@Override
	public void save()
	{
		getEntiteitModel().saveObject();
		getEntiteitModel().getObject().commit();
	}
}
