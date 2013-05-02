package nl.topicus.eduarte.entities.settings;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import nl.topicus.cobra.web.components.form.AutoFormEmbedded;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author jutten
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class APIKeySetting extends OrganisatieSetting<APIKeyConfiguration>
{
	private static final long serialVersionUID = 1L;

	@Embedded
	@AutoFormEmbedded
	private APIKeyConfiguration value;

	public APIKeySetting()
	{
		value = new APIKeyConfiguration();
	}

	@Override
	public String getOmschrijving()
	{
		return "API sleutel webservice authenticatie";
	}

	@Override
	public APIKeyConfiguration getValue()
	{
		return value;
	}

	@Override
	public void setValue(APIKeyConfiguration value)
	{
		this.value = value;
	}
}
