package nl.topicus.eduarte.krd.web.pages.medewerker;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.eduarte.dao.helpers.SoortContactgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.StandaardContactgegeven;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.OrganisatieMedewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.vrijevelden.MedewerkerVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.web.models.secure.AbstractEntiteitEditPageModel;
import nl.topicus.eduarte.zoekfilters.SoortContactgegevenZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Model om een nieuwe medewerker aan te maken
 * 
 * @author hoeve
 */
public class MedewerkerModel extends AbstractEntiteitEditPageModel<Medewerker>
{
	private static final long serialVersionUID = 1L;

	public MedewerkerModel(Medewerker medewerker)
	{
		super(medewerker.getCreatedAt());

		entiteitModel =
			ModelFactory.getCompoundChangeRecordingModel(medewerker, getEntiteitManager());
	}

	public MedewerkerModel(Date registratieDatum)
	{
		super(registratieDatum);
		setEntiteitManager();

		entiteitModel =
			ModelFactory.getCompoundChangeRecordingModel(createDefaultT(), getEntiteitManager());
	}

	@Override
	protected void setEntiteitManager()
	{
		entiteitManager =
			new DefaultModelManager(VrijVeldOptieKeuze.class, MedewerkerVrijVeld.class,
				OrganisatieMedewerker.class, PersoonContactgegeven.class, PersoonAdres.class,
				Adres.class, Medewerker.class, Persoon.class);
	}

	@Override
	protected Medewerker createDefaultT()
	{
		Medewerker medewerker = new Medewerker();
		medewerker.setPersoon(createDefaultPersoon());

		return medewerker;
	}

	private Persoon createDefaultPersoon()
	{
		Persoon persoon = new Persoon();

		voegContactgegevensToe(persoon);

		return persoon;
	}

	private void voegContactgegevensToe(Persoon persoon)
	{
		SoortContactgegevenDataAccessHelper soortHelper =
			DataAccessRegistry.getHelper(SoortContactgegevenDataAccessHelper.class);
		SoortContactgegevenZoekFilter filter = new SoortContactgegevenZoekFilter();
		filter.setStandaardContactgegeven(StandaardContactgegeven.StandaardTonenBijPersoon,
			StandaardContactgegeven.StandaardTonen);
		filter.setActief(true);

		List<SoortContactgegeven> soorten = soortHelper.list(filter);
		for (SoortContactgegeven soort : soorten)
		{
			voegContactgegevenToeAanPersoon(persoon, soort);
		}
	}

	private void voegContactgegevenToeAanPersoon(Persoon persoon, SoortContactgegeven soort)
	{
		PersoonContactgegeven gegeven = new PersoonContactgegeven();
		gegeven.setPersoon(persoon);
		gegeven.setSoortContactgegeven(soort);
		persoon.getContactgegevens().add(gegeven);
	}

	public PropertyModel<List<OrganisatieMedewerker>> getOrganisatieMedewerkerListModel()
	{
		return new PropertyModel<List<OrganisatieMedewerker>>(getEntiteitModel(),
			"organisatieMedewerkers");
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

	public IModel<Persoon> getPersoonModel()
	{
		return new PropertyModel<Persoon>(getEntiteitModel(), "persoon");
	}
}
