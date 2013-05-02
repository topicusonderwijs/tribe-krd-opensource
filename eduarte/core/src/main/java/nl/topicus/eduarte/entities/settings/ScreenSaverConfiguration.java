package nl.topicus.eduarte.entities.settings;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nl.topicus.cobra.web.components.form.AutoForm;

@Embeddable
public class ScreenSaverConfiguration implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private boolean actief;

	@Column(name = "timeout", nullable = true)
	@AutoForm(label = "Timeout (minuten)", required = true)
	private Integer timeout = 0;

	@Column(name = "sessietimeout", nullable = true)
	@AutoForm(label = "Sessie beëindigen", required = true, description = "Het aantal minuten "
		+ "waarna de gebruiker uitgelogd wordt, als de screensaver actief is.")
	private Integer sessieTimeout = 0;

	public ScreenSaverConfiguration()
	{
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public void setTimeout(Integer timeout)
	{
		this.timeout = timeout;
	}

	public Integer getTimeout()
	{
		return timeout;
	}

	public void setSessieTimeout(Integer sessieTimeout)
	{
		this.sessieTimeout = sessieTimeout;
	}

	public Integer getSessieTimeout()
	{
		return sessieTimeout;
	}
}
