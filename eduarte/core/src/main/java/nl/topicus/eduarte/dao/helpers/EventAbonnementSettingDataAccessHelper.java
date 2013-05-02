package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.eduarte.app.signalering.EventTransport;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.entities.signalering.settings.EventAbonnementSetting;
import nl.topicus.eduarte.entities.signalering.settings.GlobaalAbonnementSetting;
import nl.topicus.eduarte.entities.signalering.settings.MedewerkerDeelnemerAbonnering;
import nl.topicus.eduarte.entities.signalering.settings.MedewerkerGroepAbonnering;
import nl.topicus.eduarte.entities.signalering.settings.PersoonlijkAbonnementSetting;

public interface EventAbonnementSettingDataAccessHelper extends
		DataAccessHelper<EventAbonnementSetting>
{
	public List<GlobaalAbonnementSetting> getDefaultSettings(Class< ? extends Event> eventClass,
			Class< ? extends EventTransport> transportClass);

	public List<PersoonlijkAbonnementSetting> getSettings(Class< ? extends Event> eventClass,
			Class< ? extends EventTransport> transportClass);

	public List<MedewerkerGroepAbonnering> getGroepAbonnementen(Medewerker medewerker);

	public List<MedewerkerDeelnemerAbonnering> getDeelnemerAbonnementen(Medewerker medewerker);
}
