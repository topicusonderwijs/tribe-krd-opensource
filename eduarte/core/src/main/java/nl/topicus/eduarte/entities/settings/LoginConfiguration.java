package nl.topicus.eduarte.entities.settings;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nl.topicus.cobra.web.components.form.AutoForm;

@Embeddable
public class LoginConfiguration implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(name = "loginPogingActief", nullable = true)
	private boolean actief;

	@Column(name = "pogingen", nullable = true)
	@AutoForm(label = "Aantal pogingen", required = true)
	private Integer pogingen;

	public LoginConfiguration()
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

	public void setPogingen(Integer pogingen)
	{
		this.pogingen = pogingen;
	}

	public Integer getPogingen()
	{
		return pogingen;
	}
}
