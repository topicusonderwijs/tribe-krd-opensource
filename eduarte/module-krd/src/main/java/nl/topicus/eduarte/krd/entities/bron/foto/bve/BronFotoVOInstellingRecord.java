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
 * 
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotoVOInstellingRecord extends BronFotoVERecord
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date aanmaakdatum;

	@Column(nullable = true)
	private Long controletotaalAccountant;

	public BronFotoVOInstellingRecord()
	{
	}

	public BronFotoVOInstellingRecord(String[] velden)
	{
		super(velden);
		aanmaakdatum = TimeUtil.getInstance().isoStringAsDate(velden[13]);
		setControletotaalAccountant(StringUtil.isEmpty(velden[16]) ? null : Long
			.valueOf(velden[16]));
	}

	public Date getAanmaakdatum()
	{
		return aanmaakdatum;
	}

	public void setAanmaakdatum(Date aanmaakdatum)
	{
		this.aanmaakdatum = aanmaakdatum;
	}

	public void setControletotaalAccountant(Long controletotaalAccountant)
	{
		this.controletotaalAccountant = controletotaalAccountant;
	}

	public Long getControletotaalAccountant()
	{
		return controletotaalAccountant;
	}

}
