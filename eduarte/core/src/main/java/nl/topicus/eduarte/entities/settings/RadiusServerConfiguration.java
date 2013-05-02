package nl.topicus.eduarte.entities.settings;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nl.topicus.cobra.web.components.form.AutoForm;

@Embeddable
public class RadiusServerConfiguration implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private boolean actief;

	@Column(name = "host", nullable = true)
	@AutoForm(label = "Serveradres", required = true)
	private String host;

	@Column(name = "poortNummer", nullable = true)
	@AutoForm(label = "Poortnummer", required = true)
	private Integer poort;

	@Column(name = "wachtwoord", nullable = true)
	private String wachtwoord;

	public RadiusServerConfiguration()
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

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public Integer getPoort()
	{
		return poort;
	}

	public void setPoort(Integer poort)
	{
		this.poort = poort;
	}

	public String getWachtwoord()
	{
		return wachtwoord;
	}

	public void setWachtwoord(String wachtwoord)
	{
		this.wachtwoord = wachtwoord;
	}
}
