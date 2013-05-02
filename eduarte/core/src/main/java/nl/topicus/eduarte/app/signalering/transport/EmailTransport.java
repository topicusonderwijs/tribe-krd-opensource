package nl.topicus.eduarte.app.signalering.transport;

import nl.topicus.cobra.mail.SimpleEmailBuilder;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.mail.EduArteMailDao;
import nl.topicus.eduarte.app.signalering.EventReceiver;
import nl.topicus.eduarte.app.signalering.EventTransport;
import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.providers.EmailProvider;

import org.jfree.util.Log;

public class EmailTransport implements EventTransport
{
	private EduArteMailDao mailDao;

	public EmailTransport()
	{
		mailDao = new EduArteMailDao();
	}

	@Override
	public String getName()
	{
		return "E-mail";
	}

	@Override
	public boolean sendEvent(Event event, EventReceiver ontvanger)
	{
		if (ontvanger instanceof EmailProvider)
		{
			EmailProvider emailOntvanger = (EmailProvider) ontvanger;
			if (StringUtil.isEmail(emailOntvanger.getEmailAdres()))
			{
				String appNaam = EduArteApp.get().getLoginTitle();
				SimpleEmailBuilder builder = new SimpleEmailBuilder();
				builder.setSubject(event.getOnderwerp());
				builder
					.setContent("Geachte "
						+ emailOntvanger.getNaam()
						+ ",\n\n"
						+ event.getOmschrijving()
						+ "\n\nWij hopen u hiermee voldoende geïnformeerd te hebben.\n\nMet vriendelijke groet,\n"
						+ appNaam + "\n\nP.S. Dit bericht is automatisch gegenereerd."
						+ " U kunt dit niet beantwoorden.");
				builder.addRecipient(emailOntvanger.getNaam(), emailOntvanger.getEmailAdres());
				builder.setFrom(appNaam, "noreply@topicus.nl");
				try
				{
					mailDao.send(builder.buildMessage());
				}
				catch (Exception e)
				{
					// noop
					Log.error(e);
				}
			}
		}
		return false;
	}

}
