package nl.topicus.eduarte.providers;

import nl.topicus.eduarte.app.signalering.EventReceiver;

public interface EmailProvider extends EventReceiver
{
	public String getEmailAdres();

	public String getNaam();
}
