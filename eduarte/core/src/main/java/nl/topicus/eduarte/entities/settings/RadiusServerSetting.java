package nl.topicus.eduarte.entities.settings;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import nl.topicus.cobra.web.components.form.AutoFormEmbedded;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Houdt per organisatie bij of er gebruik gemaakt wordt van een RadiusServer, en wat de
 * instellingen zijn
 * 
 * @author vanderkamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class RadiusServerSetting extends OrganisatieSetting<RadiusServerConfiguration>
{
	private static final long serialVersionUID = 1L;

	@Embedded
	@AutoFormEmbedded
	private RadiusServerConfiguration value;

	public RadiusServerSetting()
	{
		value = new RadiusServerConfiguration();
	}

	/**
	 * @see nl.topicus.eduarte.entities.settings.OrganisatieSetting#getOmschrijving()
	 */
	@Override
	public String getOmschrijving()
	{
		return "Radius server";
	}

	@Override
	public RadiusServerConfiguration getValue()
	{
		return value;
	}

	@Override
	public void setValue(RadiusServerConfiguration value)
	{
		this.value = value;
	}
}
