package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.participatie.enums.ExterneAgendaConnection;

@Entity
public class GoogleCalendarKoppeling extends ExterneAgendaKoppeling
{
	private static final long serialVersionUID = 1L;

	@Column(length = 100, nullable = true)
	@Basic(optional = false)
	@AutoForm(label = "Programmanaam", htmlClasses = "unit_max")
	private String applicationName;

	@Column(length = 200, nullable = true)
	@Basic(optional = false)
	@AutoForm(label = "Metafeed URL base", htmlClasses = "unit_max")
	private String metafeedURLBase;

	@Column(length = 50, nullable = true)
	@Basic(optional = false)
	@AutoForm(label = "Event feed URL suffix", htmlClasses = "unit_max")
	private String eventFeedURLSuffix;

	public GoogleCalendarKoppeling()
	{
	}

	/**
	 * @return Returns the applicationName.
	 */
	public String getApplicationName()
	{
		return applicationName;
	}

	/**
	 * @param applicationName
	 *            The applicationName to set.
	 */
	public void setApplicationName(String applicationName)
	{
		this.applicationName = applicationName;
	}

	/**
	 * @return Returns the metafeedURLBase.
	 */
	public String getMetafeedURLBase()
	{
		return metafeedURLBase;
	}

	/**
	 * @param metafeedURLBase
	 *            The metafeedURLBase to set.
	 */
	public void setMetafeedURLBase(String metafeedURLBase)
	{
		this.metafeedURLBase = metafeedURLBase;
	}

	/**
	 * @return Returns the eventFeedURLSuffix.
	 */
	public String getEventFeedURLSuffix()
	{
		return eventFeedURLSuffix;
	}

	/**
	 * @param eventFeedURLSuffix
	 *            The eventFeedURLSuffix to set.
	 */
	public void setEventFeedURLSuffix(String eventFeedURLSuffix)
	{
		this.eventFeedURLSuffix = eventFeedURLSuffix;
	}

	@Override
	public ExterneAgendaConnection connect(ExterneAgenda agenda) throws ExterneAgendaException
	{
		return new GoogleCalendarConnection(this, agenda);
	}

	@Override
	public void selectDefaults()
	{
		super.selectDefaults();
		metafeedURLBase = "http://www.google.com/calendar/feeds/";
		eventFeedURLSuffix = "/private/full";
		applicationName = "topicus-eduarte-" + EduArteApp.get().getVersion();
	}

	@Override
	public String getBeschrijving()
	{
		return "Google Calendar";
	}
}
