package nl.topicus.eduarte.entities.settings;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nl.topicus.cobra.web.components.form.AutoForm;

@Embeddable
public class ResultaatControleConfiguration implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@AutoForm(description = "Schakelt de uitgebreide controle van de resultaten in. Deze "
		+ "controle is langzaam, maar kan databasecorruptie voorkomen bij fouten in de software.")
	private boolean actief;

	public ResultaatControleConfiguration()
	{
		actief = true;
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}
}
