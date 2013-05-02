package nl.topicus.eduarte.entities.settings;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import nl.topicus.cobra.web.components.form.AutoFormEmbedded;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Houdt per organisatie bij of er gebruik gemaakt wordt van de lokale Vasco token server,
 * en waar je die kan vinden.
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class VascoTokensSetting extends OrganisatieSetting<VascoTokensConfiguration>
{
	private static final long serialVersionUID = 1L;

	@Embedded
	@AutoFormEmbedded
	private VascoTokensConfiguration value;

	public VascoTokensSetting()
	{
		value = new VascoTokensConfiguration();
	}

	/**
	 * @see nl.topicus.eduarte.entities.settings.OrganisatieSetting#getOmschrijving()
	 */
	@Override
	public String getOmschrijving()
	{
		return "Vasco tokens";
	}

	@Override
	public VascoTokensConfiguration getValue()
	{
		return value;
	}

	@Override
	public void setValue(VascoTokensConfiguration value)
	{
		this.value = value;
	}
}
