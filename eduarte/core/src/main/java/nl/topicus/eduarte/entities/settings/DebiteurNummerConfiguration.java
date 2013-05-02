package nl.topicus.eduarte.entities.settings;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nl.topicus.cobra.web.components.form.AutoForm;

@Embeddable
public class DebiteurNummerConfiguration implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true, name = "deelnemernummerIsDebiteurnr")
	@AutoForm(label = "Deelnemernummer als debiteurnummer gebruiken", required = true)
	private boolean deelnemernummerIsDebiteurnummer;

	@Column(nullable = true, name = "gezamenlijkeRange")
	@AutoForm(label = "Gezamenlijke range voor personen en organisaties", required = true)
	private boolean gezamenlijkeRangePersonenOrganisaties;

	public DebiteurNummerConfiguration()
	{
	}

	public boolean isDeelnemernummerIsDebiteurnummer()
	{
		return deelnemernummerIsDebiteurnummer;
	}

	public void setDeelnemernummerIsDebiteurnummer(boolean deelnemernummerIsDebiteurnummer)
	{
		this.deelnemernummerIsDebiteurnummer = deelnemernummerIsDebiteurnummer;
	}

	public boolean isGezamenlijkeRangePersonenOrganisaties()
	{
		return gezamenlijkeRangePersonenOrganisaties;
	}

	public void setGezamenlijkeRangePersonenOrganisaties(
			boolean gezamenlijkeRangePersonenOrganisaties)
	{
		this.gezamenlijkeRangePersonenOrganisaties = gezamenlijkeRangePersonenOrganisaties;
	}
}
