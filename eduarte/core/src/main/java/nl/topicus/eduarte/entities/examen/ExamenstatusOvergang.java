package nl.topicus.eduarte.entities.examen;

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
 * Statusovergang voor een examendeelname van een status naar een andere. De beginstatus
 * kan null zijn als het om de beginstatus gaat.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ExamenstatusOvergang extends InstellingEntiteit implements
		Comparable<ExamenstatusOvergang>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "examendeelname")
	@Index(name = "idx_exstatusover_deelname")
	private Examendeelname examendeelname;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "vanStatus")
	@Index(name = "idx_exstatusover_van")
	private Examenstatus vanStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "naarStatus")
	@Index(name = "idx_exstatusover_naar")
	private Examenstatus naarStatus;

	/**
	 * Datum/tijd dat de statusovergang uitgevoerd is.
	 */
	@Column(nullable = false)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date datumTijd;

	/**
	 * Opmerkingen bij de statusovergang, bijvoorbeeld waarom de statusovergang mislukt
	 * is.
	 */
	@Column(nullable = true, length = 4000)
	private String opmerkingen;

	public ExamenstatusOvergang()
	{
	}

	/**
	 * @return Returns the examendeelname.
	 */
	public Examendeelname getExamendeelname()
	{
		return examendeelname;
	}

	/**
	 * @param examendeelname
	 *            The examendeelname to set.
	 */
	public void setExamendeelname(Examendeelname examendeelname)
	{
		this.examendeelname = examendeelname;
	}

	/**
	 * @return Returns the vanStatus.
	 */
	public Examenstatus getVanStatus()
	{
		return vanStatus;
	}

	/**
	 * @param vanStatus
	 *            The vanStatus to set.
	 */
	public void setVanStatus(Examenstatus vanStatus)
	{
		this.vanStatus = vanStatus;
	}

	/**
	 * @return Returns the naarStatus.
	 */
	public Examenstatus getNaarStatus()
	{
		return naarStatus;
	}

	/**
	 * @param naarStatus
	 *            The naarStatus to set.
	 */
	public void setNaarStatus(Examenstatus naarStatus)
	{
		this.naarStatus = naarStatus;
	}

	/**
	 * @return Returns the datumTijd.
	 */
	public Date getDatumTijd()
	{
		return datumTijd;
	}

	/**
	 * @param datumTijd
	 *            The datumTijd to set.
	 */
	public void setDatumTijd(Date datumTijd)
	{
		this.datumTijd = datumTijd;
	}

	/**
	 * @return Returns the opmerkingen.
	 */
	public String getOpmerkingen()
	{
		return opmerkingen;
	}

	/**
	 * @param opmerkingen
	 *            The opmerkingen to set.
	 */
	public void setOpmerkingen(String opmerkingen)
	{
		this.opmerkingen = opmerkingen;
	}

	@Override
	public int compareTo(ExamenstatusOvergang o)
	{
		return getDatumTijd().compareTo(o.getDatumTijd());
	}

}
