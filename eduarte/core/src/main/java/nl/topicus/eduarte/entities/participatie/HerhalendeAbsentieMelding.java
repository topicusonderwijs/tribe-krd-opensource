package nl.topicus.eduarte.entities.participatie;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Een herhalende absentieMelding geeft aan dat een absentieMelding zichzelf om de X weken
 * herhaalt. Alle instanties van de herhalende absentieMelding verwijzen naar dit object.
 * Wanneer een instantie aangepast wordt, kan de gebruiker ervoor kiezen om alleen de
 * instantie aan te passen, of alle (nog niet uitgevoerde) instanties aan te passen.
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class HerhalendeAbsentieMelding extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = false)
	private Date beginDatum;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = false)
	private Date eindDatum;

	/**
	 * Het aantal weken tussen elke absentieMelding.
	 */
	@Column(nullable = false)
	private int weekCyclus;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "herhalendeAbsentieMelding")
	private List<AbsentieMelding> absentieMeldingen;

	public HerhalendeAbsentieMelding()
	{
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

	public int getWeekCyclus()
	{
		return weekCyclus;
	}

	public void setWeekCyclus(int weekCyclus)
	{
		this.weekCyclus = weekCyclus;
	}

	public List<AbsentieMelding> getAbsentieMeldingen()
	{
		return absentieMeldingen;
	}

	public void setAbsentieMeldingen(List<AbsentieMelding> absentieMeldingen)
	{
		this.absentieMeldingen = absentieMeldingen;
	}

}
