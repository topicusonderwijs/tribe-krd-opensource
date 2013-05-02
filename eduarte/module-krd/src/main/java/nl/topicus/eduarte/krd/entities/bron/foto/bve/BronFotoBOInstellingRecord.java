package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Instellingsrecord in een BO fotobestand
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotoBOInstellingRecord extends BronFotoBORecord
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date aanmaakdatum;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumVerklaringOntvangen;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumAssuranceRapportOntvangen;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumRappelVerstuurd;

	@Column(nullable = true)
	private Long controletotaalAccountant;

	public BronFotoBOInstellingRecord()
	{
	}

	public BronFotoBOInstellingRecord(String[] velden)
	{
		super(velden);
		aanmaakdatum = TimeUtil.getInstance().isoStringAsDate(velden[9]);
		datumVerklaringOntvangen = TimeUtil.getInstance().isoStringAsDate(velden[10]);
		datumAssuranceRapportOntvangen = TimeUtil.getInstance().isoStringAsDate(velden[11]);
		datumRappelVerstuurd = TimeUtil.getInstance().isoStringAsDate(velden[12]);
		controletotaalAccountant = StringUtil.isEmpty(velden[13]) ? null : Long.valueOf(velden[13]);
	}

	public Date getAanmaakdatum()
	{
		return aanmaakdatum;
	}

	public void setAanmaakdatum(Date aanmaakdatum)
	{
		this.aanmaakdatum = aanmaakdatum;
	}

	public Date getDatumVerklaringOntvangen()
	{
		return datumVerklaringOntvangen;
	}

	public void setDatumVerklaringOntvangen(Date datumVerklaringOntvangen)
	{
		this.datumVerklaringOntvangen = datumVerklaringOntvangen;
	}

	public Date getDatumAssuranceRapportOntvangen()
	{
		return datumAssuranceRapportOntvangen;
	}

	public void setDatumAssuranceRapportOntvangen(Date datumAssuranceRapportOntvangen)
	{
		this.datumAssuranceRapportOntvangen = datumAssuranceRapportOntvangen;
	}

	public Date getDatumRappelVerstuurd()
	{
		return datumRappelVerstuurd;
	}

	public void setDatumRappelVerstuurd(Date datumRappelVerstuurd)
	{
		this.datumRappelVerstuurd = datumRappelVerstuurd;
	}

	public Long getControletotaalAccountant()
	{
		return controletotaalAccountant;
	}

	public void setControletotaalAccountant(Long controletotaalAccountant)
	{
		this.controletotaalAccountant = controletotaalAccountant;
	}

}
