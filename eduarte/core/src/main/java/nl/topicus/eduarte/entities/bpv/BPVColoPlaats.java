package nl.topicus.eduarte.entities.bpv;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BPVColoPlaats extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String codeLeerbedrijf;

	@Column(nullable = true)
	private Long leerplaatsId;

	@Column(nullable = true)
	private String leerplaatsSoort;

	@Column(nullable = true)
	private String leerbedrijfNaam;

	@Column(nullable = true)
	private String vacatureLeerplaatsOmschrijving;

	@Column(nullable = true)
	private String leerweg;

	@Column(nullable = true)
	private Integer aantalGeregistreerdeLeerlingen;

	@Column(nullable = true)
	private Integer leerplaatsAantal;

	@Column(nullable = true)
	private String straat;

	@Column(nullable = true)
	private String postcode;

	@Column(nullable = true)
	private String plaats;

	@Column(nullable = true)
	private String land;

	public BPVColoPlaats()
	{

	}

	public String getCodeLeerbedrijf()
	{
		return codeLeerbedrijf;
	}

	public void setCodeLeerbedrijf(String codeLeerbedrijf)
	{
		this.codeLeerbedrijf = codeLeerbedrijf;
	}

	public Long getLeerplaatsId()
	{
		return leerplaatsId;
	}

	public void setLeerplaatsId(Long leerplaatsId)
	{
		this.leerplaatsId = leerplaatsId;
	}

	public String getLeerplaatsSoort()
	{
		return leerplaatsSoort;
	}

	public void setLeerplaatsSoort(String leerplaatsSoort)
	{
		this.leerplaatsSoort = leerplaatsSoort;
	}

	public String getLeerbedrijfNaam()
	{
		return leerbedrijfNaam;
	}

	public void setLeerbedrijfNaam(String leerbedrijfNaam)
	{
		this.leerbedrijfNaam = leerbedrijfNaam;
	}

	public String getVacatureLeerplaatsOmschrijving()
	{
		return vacatureLeerplaatsOmschrijving;
	}

	public void setVacatureLeerplaatsOmschrijving(String vacatureLeerplaatsOmschrijving)
	{
		this.vacatureLeerplaatsOmschrijving = vacatureLeerplaatsOmschrijving;
	}

	public String getLeerweg()
	{
		return leerweg;
	}

	public void setLeerweg(String leerweg)
	{
		this.leerweg = leerweg;
	}

	public Integer getAantalGeregistreerdeLeerlingen()
	{
		return aantalGeregistreerdeLeerlingen;
	}

	public void setAantalGeregistreerdeLeerlingen(Integer aantalGeregistreerdeLeerlingen)
	{
		this.aantalGeregistreerdeLeerlingen = aantalGeregistreerdeLeerlingen;
	}

	public Integer getLeerplaatsAantal()
	{
		return leerplaatsAantal;
	}

	public void setLeerplaatsAantal(Integer leerplaatsAantal)
	{
		this.leerplaatsAantal = leerplaatsAantal;
	}

	public String getStraat()
	{
		return straat;
	}

	public void setStraat(String straat)
	{
		this.straat = straat;
	}

	public String getPostcode()
	{
		return postcode;
	}

	public void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}

	public String getPlaats()
	{
		return plaats;
	}

	public void setPlaats(String plaats)
	{
		this.plaats = plaats;
	}

	public String getLand()
	{
		return land;
	}

	public void setLand(String land)
	{
		this.land = land;
	}

	/**
	 * Afhankelijke van of de omschrijving van de leerplaats gevuld is wordt de postcode
	 * en plaats als bovenste label teruggegeven
	 */
	public String getDragDropLabelBoven()
	{
		StringBuilder builder = new StringBuilder();
		if (getLeerbedrijfNaam().length() > 25
			&& !StringUtil.isEmpty(getVacatureLeerplaatsOmschrijving()))
			builder.append(getLeerbedrijfNaam().substring(0, 25));
		else
			builder.append(getLeerbedrijfNaam());
		if (StringUtil.isEmpty(getVacatureLeerplaatsOmschrijving()))
			return builder.toString();
		if (StringUtil.isNotEmpty(getPostcode()) && StringUtil.isNotEmpty(getPlaats()))
		{
			builder.append("(");
			builder.append(getPostcode());
			builder.append(", ");
			builder.append(getPlaats().toUpperCase());
			builder.append(")");
		}
		return builder.toString();
	}

	public String getDragDropLabelOnder()
	{
		if (!StringUtil.isEmpty(getVacatureLeerplaatsOmschrijving()))
			return getVacatureLeerplaatsOmschrijving();
		StringBuilder builder = new StringBuilder();
		if (StringUtil.isNotEmpty(getPostcode()) && StringUtil.isNotEmpty(getPlaats()))
		{
			builder.append(getPostcode());
			builder.append(", ");
			builder.append(getPlaats().toUpperCase());
		}
		return builder.toString();
	}
}