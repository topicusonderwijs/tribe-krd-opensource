package nl.topicus.eduarte.krd.web.pages.beheer.organisatie.extern;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.SoortContactgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.StandaardContactgegeven;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieContactgegeven;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieOpmerking;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatiePraktijkbegeleider;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.vrijevelden.ExterneOrganisatieVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.web.models.secure.AbstractEntiteitEditPageModel;
import nl.topicus.eduarte.zoekfilters.SoortContactgegevenZoekFilter;

import org.apache.wicket.model.PropertyModel;

/**
 * Model om een nieuwe externeOrganisatie aan te maken
 * 
 * @author vandekamp
 */
public class ExterneOrganisatieModel extends AbstractEntiteitEditPageModel<ExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creert een model op basis van een bestaande externeOrganisatie.
	 */
	public ExterneOrganisatieModel(ExterneOrganisatie externeOrganisatie)
	{
		super(TimeUtil.getInstance().currentDate());

		entiteitModel =
			ModelFactory.getCompoundChangeRecordingModel(externeOrganisatie, getEntiteitManager());
		voegContactgegevensToe(externeOrganisatie);
	}

	@Override
	protected void setEntiteitManager()
	{
		entiteitManager =
			new DefaultModelManager(ExterneOrganisatieAdres.class, Adres.class,
				ExterneOrganisatieContactPersoon.class, ExterneOrganisatieContactgegeven.class,
				BPVBedrijfsgegeven.class, VrijVeldOptieKeuze.class,
				ExterneOrganisatieVrijVeld.class, ExterneOrganisatieOpmerking.class,
				BPVCriteriaExterneOrganisatie.class, ExterneOrganisatiePraktijkbegeleider.class,
				ExterneOrganisatie.class, Brin.class);
	}

	@Override
	protected ExterneOrganisatie createDefaultT()
	{
		ExterneOrganisatie externeOrganisatie = new ExterneOrganisatie();
		voegContactgegevensToe(externeOrganisatie);
		return externeOrganisatie;
	}

	private void voegContactgegevensToe(ExterneOrganisatie externeOrganisatie)
	{
		List<SoortContactgegeven> huidigeSoortContactgegevens =
			new ArrayList<SoortContactgegeven>();

		for (ExterneOrganisatieContactgegeven huidigGegeven : externeOrganisatie
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
				voegContactgegevenToeAanExterneOrganisatie(externeOrganisatie, soort);
		}

	}

	private void voegContactgegevenToeAanExterneOrganisatie(ExterneOrganisatie externeOrganisatie,
			SoortContactgegeven soort)
	{
		ExterneOrganisatieContactgegeven gegeven = new ExterneOrganisatieContactgegeven();
		gegeven.setExterneOrganisatie(externeOrganisatie);
		gegeven.setSoortContactgegeven(soort);
		externeOrganisatie.getContactgegevens().add(gegeven);
	}

	public PropertyModel<List<ExterneOrganisatieContactPersoon>> getContPersListModel()
	{
		return new PropertyModel<List<ExterneOrganisatieContactPersoon>>(getEntiteitModel(),
			"contactPersonen");
	}

	public ExterneOrganisatie getExterneOrganisatie()
	{
		return getEntiteitModel().getObject();
	}

	public ExterneOrganisatieContactPersoon createNieuwContactPersoon()
	{
		ExterneOrganisatieContactPersoon nieuwContactPersoon =
			new ExterneOrganisatieContactPersoon(getExterneOrganisatie());
		getObject().addContactPersoon(nieuwContactPersoon);
		return nieuwContactPersoon;
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

	public PropertyModel<List<BPVBedrijfsgegeven>> getBPVBedrGegListModel()
	{
		return new PropertyModel<List<BPVBedrijfsgegeven>>(getEntiteitModel(),
			"bpvBedrijfsgegevens");
	}

	public BPVBedrijfsgegeven createNieuwBPVBedrGeg()
	{
		BPVBedrijfsgegeven nieuwBPV = new BPVBedrijfsgegeven(getExterneOrganisatie());
		return nieuwBPV;
	}

	public PropertyModel<List<ExterneOrganisatieOpmerking>> getBPVOpmerkingListModel()
	{
		return new PropertyModel<List<ExterneOrganisatieOpmerking>>(getEntiteitModel(),
			"opmerkingen");
	}

	public PropertyModel<List<BPVCriteriaExterneOrganisatie>> getBPVCriteriaListModel()
	{
		return new PropertyModel<List<BPVCriteriaExterneOrganisatie>>(getEntiteitModel(),
			"bpvCriteria");
	}

	public PropertyModel<List<ExterneOrganisatiePraktijkbegeleider>> getExterneOrganisatiePraktijkbegeleiderListModel()
	{
		return new PropertyModel<List<ExterneOrganisatiePraktijkbegeleider>>(getEntiteitModel(),
			"praktijkbegeleiders");

	}
}
