package nl.topicus.eduarte.entities.settings;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nl.topicus.cobra.web.components.form.AutoForm;

@Embeddable
public class OrganisatieIpAdresConfiguration implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(name = "ipadressen", nullable = true, length = 4000)
	@AutoForm(label = "IP Adressen", description = "Alleen toegang toestaan (voor de hele organisatie) vanaf de hier ingevulde IP adressen. Meerdere adressen worden gescheiden door een komma. Als het adres eindigt op .0 (bijvoorbeeld 192.168.1.0) heeft de hele range 192.168.1.1 t/m 192.168.1.254 toegang.", htmlClasses = "unit_300")
	private String ipAdressen;

	public OrganisatieIpAdresConfiguration()
	{
	}

	public String getIpAdressen()
	{
		return ipAdressen;
	}

	public void setIpAdressen(String ipAdressen)
	{
		this.ipAdressen = ipAdressen;
	}

}
