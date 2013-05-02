package nl.topicus.eduarte.entities.participatie;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een vakantie die binnen een basisrooster gedefinieerd is.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Vakantie extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 60, nullable = false)
	private String naam;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = false)
	private Date beginDatum;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = false)
	private Date eindDatum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "basisrooster", nullable = false)
	@Index(name = "idx_Vakantie_basisrooster")
	private Basisrooster basisrooster;

	/**
	 * Default constructor voor Hibernate.
	 */
	public Vakantie()
	{
	}

	public Vakantie(String naam, Date beginDatum, Date eindDatum, Basisrooster basisrooster)
	{
		setBasisrooster(basisrooster);
		setNaam(naam);
		setBeginDatum(beginDatum);
		setEindDatum(eindDatum);
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public Date getBeginDatum()
	{
		return beginDatum;
	}

	public void setBeginDatum(Date beginDatum)
	{
		this.beginDatum = beginDatum;
	}

	public Date getEindDatum()
	{
		return eindDatum;
	}

	public void setEindDatum(Date eindDatum)
	{
		this.eindDatum = eindDatum;
	}

	public Basisrooster getBasisrooster()
	{
		return basisrooster;
	}

	public void setBasisrooster(Basisrooster basisrooster)
	{
		this.basisrooster = basisrooster;
	}
}
