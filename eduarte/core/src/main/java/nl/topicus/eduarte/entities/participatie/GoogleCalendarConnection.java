package nl.topicus.eduarte.entities.participatie;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakParticpantDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.enums.ExterneAgendaConnection;
import nl.topicus.eduarte.entities.participatie.enums.UitnodigingStatus;

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.When;

public class GoogleCalendarConnection implements ExterneAgendaConnection
{
	private final URL feedUrl;

	private final CalendarService service;

	public GoogleCalendarConnection(GoogleCalendarKoppeling googleCalendarKoppeling,
			ExterneAgenda agenda) throws ExterneAgendaException
	{
		service = new CalendarService(googleCalendarKoppeling.getApplicationName());
		try
		{
			feedUrl =
				new URL(googleCalendarKoppeling.getMetafeedURLBase() + agenda.getGebruikersNaam()
					+ googleCalendarKoppeling.getEventFeedURLSuffix());

			service.setUserCredentials(agenda.getGebruikersNaam(), agenda.getWachtwoord());
		}
		catch (Exception e)
		{
			throw new ExterneAgendaException(e);
		}
	}

	private Date gDateTimeToDate(DateTime dateTime)
	{
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(dateTime.getValue());
		if (dateTime.isDateOnly())
		{
			cal.add(Calendar.MILLISECOND, -cal.getTimeZone().getOffset(cal.getTimeInMillis()));
		}
		return cal.getTime();
	}

	@Override
	public List<Afspraak> fetch(CacheRegion region) throws ExterneAgendaException
	{
		CalendarQuery myQuery = new CalendarQuery(feedUrl);
		myQuery.setMinimumStartTime(new DateTime(region.getRegionStartDate()));
		myQuery.setMaximumStartTime(new DateTime(region.getRegionEndDate()));

		// Send the request and receive the response:
		CalendarEventFeed resultFeed;
		try
		{
			resultFeed = service.query(myQuery, CalendarEventFeed.class);
		}
		catch (Exception e)
		{
			throw new ExterneAgendaException(e);
		}

		List<Afspraak> ret = new ArrayList<Afspraak>();
		for (int i = 0; i < resultFeed.getEntries().size(); i++)
		{
			CalendarEventEntry entry = resultFeed.getEntries().get(i);
			int timeCount = 0;
			for (When curTime : entry.getTimes())
			{
				// skip to prevent duplicates when afspraak overlaps end of month
				if (gDateTimeToDate(curTime.getStartTime()).before(region.getRegionStartDate()))
					continue;

				Afspraak afspraak = new Afspraak();
				afspraak.setCacheRegion(region);
				afspraak.setTitel(entry.getTitle().getPlainText());
				if (StringUtil.isEmpty(afspraak.getTitel()))
					afspraak.setTitel("(Geen onderwerp)");
				afspraak.setOmschrijving(entry.getPlainTextContent());
				afspraak.setOrganisatieEenheid(region.getExterneAgenda().getKoppeling()
					.getOrganisatieEenheid());
				afspraak
					.setAfspraakType(region.getExterneAgenda().getKoppeling().getAfspraakType());
				afspraak.setBeginDatumTijd(gDateTimeToDate(curTime.getStartTime()));
				afspraak.setEindDatumTijd(gDateTimeToDate(curTime.getEndTime()));
				afspraak.setAuteur(region.getExterneAgenda().getEigenaar());
				afspraak.setExternId(entry.getId() + "-" + timeCount);
				afspraak.setExternSysteem(null);
				afspraak.save();

				for (IdObject curObject : DataAccessRegistry.getHelper(
					AfspraakParticpantDataAccessHelper.class).findPossibleParticipanten(
					region.getExterneAgenda().getEigenaar()))
				{
					AfspraakParticipant participant = new AfspraakParticipant();
					participant.setAfspraak(afspraak);
					participant.setParticipantEntiteit(curObject);
					participant.setUitnodigingStatus(UitnodigingStatus.DIRECTE_PLAATSING);
					afspraak.addParticipant(participant);
					participant.save();
				}
				timeCount++;
				ret.add(afspraak);
			}
		}

		return ret;
	}
}
