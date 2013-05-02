package nl.topicus.eduarte.entities.settings;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.web.components.text.APIKeyField;

@Embeddable
public class APIKeyConfiguration implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(name = "apikey", nullable = true, length = 32)
	@AutoForm(label = "API sleutel", editorClass = APIKeyField.class, description = "Pas op. Het wijzigen van de API sleutel heeft als gevolg dat iedereen, die gebruik maakt van de webservices, de nieuwe sleutel dient te ontvangen. Zonder de nieuwe sleutel kan men geen gebruik meer maken van de webservices.")
	private String apiKey;

	public APIKeyConfiguration()
	{
	}

	public String getAPIKey()
	{
		return apiKey;
	}

	public void setAPIKey(String apiKey)
	{
		this.apiKey = apiKey;
	}
}
