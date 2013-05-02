package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class PlanningTemplate extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private int aantalEenhedenNaAanvang;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TijdEenheid tijdEenheid;

	@Column(nullable = true)
	private Integer aantalEenhedenTussenHerhaling;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private TijdEenheid eenheidTussenHerhaling;

	@Column(nullable = true)
	private Integer stoptNaAantalEenheden;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private TijdEenheid stoptNaEenheid;

	@Column(nullable = false)
	private int stoptNaAantalKeer = 100;

	public PlanningTemplate()
	{
	}

	public int getAantalEenhedenNaAanvang()
	{
		return aantalEenhedenNaAanvang;
	}

	public void setAantalEenhedenNaAanvang(int aantalEenhedenNaAanvang)
	{
		this.aantalEenhedenNaAanvang = aantalEenhedenNaAanvang;
	}

	public TijdEenheid getTijdEenheid()
	{
		return tijdEenheid;
	}

	public void setTijdEenheid(TijdEenheid tijdEenheid)
	{
		this.tijdEenheid = tijdEenheid;
	}

	@Override
	public String toString()
	{
		if (getAantalEenhedenTussenHerhaling() == null)
			return "Eenmalig:" + getAantalEenhedenNaAanvang() + " " + getTijdEenheid();

		return "Periodiek: elke " + getAantalEenhedenTussenHerhaling() + " "
			+ getEenheidTussenHerhaling();
	}

	public Integer getAantalEenhedenTussenHerhaling()
	{
		return aantalEenhedenTussenHerhaling;
	}

	public void setAantalEenhedenTussenHerhaling(Integer aantalEenhedenTussenHerhaling)
	{
		this.aantalEenhedenTussenHerhaling = aantalEenhedenTussenHerhaling;
	}

	public TijdEenheid getEenheidTussenHerhaling()
	{
		return eenheidTussenHerhaling;
	}

	public void setEenheidTussenHerhaling(TijdEenheid eenheidTussenHerhaling)
	{
		this.eenheidTussenHerhaling = eenheidTussenHerhaling;
	}

	public Integer getStoptNaAantalEenheden()
	{
		return stoptNaAantalEenheden;
	}

	public void setStoptNaAantalEenheden(Integer stoptNaAantalEenheden)
	{
		this.stoptNaAantalEenheden = stoptNaAantalEenheden;
	}

	public TijdEenheid getStoptNaEenheid()
	{
		return stoptNaEenheid;
	}

	public void setStoptNaEenheid(TijdEenheid stoptNaEenheid)
	{
		this.stoptNaEenheid = stoptNaEenheid;
	}

	public int getStoptNaAantalKeer()
	{
		return stoptNaAantalKeer;
	}

	public void setStoptNaAantalKeer(int stoptNaAantalKeer)
	{
		this.stoptNaAantalKeer = stoptNaAantalKeer;
	}
}
