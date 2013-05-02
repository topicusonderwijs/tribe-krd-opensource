package nl.topicus.eduarte.app.signalering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.EventAbonnementSettingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.EventDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.entities.signalering.settings.GlobaalAbonnementSetting;
import nl.topicus.eduarte.entities.signalering.settings.PersoonlijkAbonnementSetting;
import nl.topicus.eduarte.providers.PersoonProvider;

public class EventDispatcher<T extends Event>
{
	private EventSource<T> eventSource;

	public EventDispatcher(EventSource<T> eventSource)
	{
		this.eventSource = eventSource;
	}

	@SuppressWarnings("unchecked")
	public void dispatchEvents(Persoon sender)
	{
		EventAbonnementSettingDataAccessHelper settingHelper =
			DataAccessRegistry.getHelper(EventAbonnementSettingDataAccessHelper.class);
		EventDataAccessHelper<T> eventHelper =
			DataAccessRegistry.getHelper(EventDataAccessHelper.class);

		T event = eventSource.createEvent();
		String hash = event.berekenHash();
		if (hash != null)
		{
			event.setHash(hash);
			if (eventHelper.isEerderOpgetreden(event))
				return;
		}
		event.save();
		if (hash == null)
			event.setHash(event.getId().toString());

		boolean eventVerstuurd = false;

		for (EventTransport curTransport : EduArteApp.get().getTransports())
		{
			List<GlobaalAbonnementSetting> standaardSettings =
				settingHelper.getDefaultSettings(event.getClass(), curTransport.getClass());
			List<PersoonlijkAbonnementSetting> persoonlijkeSettings =
				settingHelper.getSettings(event.getClass(), curTransport.getClass());
			Set<EventReceiver> receivers = new HashSet<EventReceiver>();
			for (Map.Entry<EventAbonnementType, List< ? extends EventReceiver>> possibleReceivers : eventSource
				.getReceivers().entrySet())
			{
				Map<Persoon, PersoonlijkAbonnementSetting> persoonlijkeSettingsVoorType =
					getSettingsPerPersoon(possibleReceivers.getKey(), persoonlijkeSettings);
				GlobaalAbonnementSetting standaardSetting =
					getStandaardSetting(possibleReceivers.getKey(), standaardSettings);
				EventAbonnementInstelling standaardInstelling =
					standaardSetting == null ? EventAbonnementInstelling.Aan : standaardSetting
						.getWaarde();
				for (EventReceiver curReceiver : possibleReceivers.getValue())
				{
					EventReceiver actualReceiver;
					if (curReceiver instanceof PersoonProvider)
						actualReceiver = ((PersoonProvider) curReceiver).getPersoon();
					else
						actualReceiver = curReceiver;

					PersoonlijkAbonnementSetting persoonlijkeSetting =
						persoonlijkeSettingsVoorType.get(actualReceiver);
					if (!actualReceiver.equals(sender)
						&& isEventEnabledVoorReceiver(standaardInstelling, persoonlijkeSetting)
						&& isEventAllowedVoorReceiver(event, actualReceiver)
						&& eventSource.isEventEnabledVoorReceiver(actualReceiver,
							persoonlijkeSetting == null ? standaardSetting : persoonlijkeSetting))
						receivers.add(actualReceiver);
				}
			}
			for (EventReceiver curReceiver : receivers)
			{
				eventVerstuurd |= curTransport.sendEvent(event, curReceiver);
			}
		}
		if (!eventVerstuurd && event.discardWhenNoReceivers())
			event.delete();
	}

	private Map<Persoon, PersoonlijkAbonnementSetting> getSettingsPerPersoon(
			EventAbonnementType type, List<PersoonlijkAbonnementSetting> settings)
	{
		Map<Persoon, PersoonlijkAbonnementSetting> ret =
			new HashMap<Persoon, PersoonlijkAbonnementSetting>();
		for (PersoonlijkAbonnementSetting curSetting : settings)
			if (curSetting.getType().equals(type))
				ret.put(curSetting.getPersoon(), curSetting);
		return ret;
	}

	private GlobaalAbonnementSetting getStandaardSetting(EventAbonnementType type,
			List<GlobaalAbonnementSetting> settings)
	{
		for (GlobaalAbonnementSetting curSetting : settings)
			if (curSetting.getType().equals(type))
				return curSetting;
		return null;
	}

	private boolean isEventEnabledVoorReceiver(EventAbonnementInstelling standaardSetting,
			PersoonlijkAbonnementSetting persoonlijkeSetting)
	{
		switch (standaardSetting)
		{
			case Verplicht:
				return true;
			case Aan:
				return persoonlijkeSetting == null ? true : EventAbonnementInstelling.Aan
					.equals(persoonlijkeSetting.getWaarde());
			case Uit:
				return persoonlijkeSetting == null ? false : EventAbonnementInstelling.Aan
					.equals(persoonlijkeSetting.getWaarde());
		}
		throw new IllegalArgumentException("Ongeldige setting: " + standaardSetting);
	}

	private boolean isEventAllowedVoorReceiver(Event event, EventReceiver receiver)
	{
		if (receiver instanceof Persoon)
		{
			Account account =
				DataAccessRegistry.getHelper(AccountDataAccessHelper.class).get((Persoon) receiver);
			if (account != null)
				return event.isEventAllowed(account);
		}
		return true;
	}
}
