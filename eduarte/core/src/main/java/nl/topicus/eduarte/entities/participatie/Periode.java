package nl.topicus.eduarte.entities.participatie;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een periode deze hoort bij een periodeIndeling
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Periode extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private int volgnummer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "periodeIndeling", nullable = false)
	@Index(name = "idx_periode_periodeIndel")
	@IgnoreInGebruik
	private PeriodeIndeling periodeIndeling;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date datumBegin;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date datumEind;

	public Periode()
	{
	}

	public int getVolgnummer()
	{
		return volgnummer;
	}

	public void setVolgnummer(int volgnummer)
	{
		this.volgnummer = volgnummer;
	}

	public PeriodeIndeling getPeriodeIndeling()
	{
		return periodeIndeling;
	}

	public void setPeriodeIndeling(PeriodeIndeling periodeIndeling)
	{
		this.periodeIndeling = periodeIndeling;
	}

	public Date getDatumBegin()
	{
		return datumBegin;
	}

	public void setDatumBegin(Date datumBegin)
	{
		this.datumBegin = datumBegin;
	}

	public Date getDatumEind()
	{
		return datumEind;
	}

	public void setDatumEind(Date datumEind)
	{
		this.datumEind = datumEind;
	}
}
