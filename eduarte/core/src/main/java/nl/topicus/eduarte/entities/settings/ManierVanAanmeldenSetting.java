package nl.topicus.eduarte.entities.settings;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Manieren van aanmelden voor digitaal aanmelden, afhankelijk van de setting kan een
 * groep opleiding gekozen worden, of er kan niet aangemeld worden bij die
 * organisatie-eenheid
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ManierVanAanmeldenSetting extends
		OrganisatieEenheidSetting<ManierVanAanmelden>
{
	private static final long serialVersionUID = 1L;

	@Column(name = "ManierVanAanmelden", nullable = true)
	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	private ManierVanAanmelden value;

	public ManierVanAanmeldenSetting()
	{
	}

	public ManierVanAanmeldenSetting(OrganisatieEenheid eenheid)
	{
		super(eenheid);
		setValue(ManierVanAanmelden.AanmeldenViaOpleiding);
	}

	@Override
	public ManierVanAanmelden getValue()
	{
		return value;
	}

	@Override
	public void setValue(ManierVanAanmelden value)
	{
		this.value = value;

	}

	@Override
	public final String getOmschrijving()
	{
		return "Manier van aanmelden";
	}

}
