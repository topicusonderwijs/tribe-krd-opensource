package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
public class BronFotoVEInstellingRecord extends BronFotoVERecord
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date aanmaakdatum;

	public BronFotoVEInstellingRecord()
	{
	}

	public BronFotoVEInstellingRecord(String[] velden)
	{
		super(velden);
		aanmaakdatum = TimeUtil.getInstance().isoStringAsDate(velden[12]);
	}

	public Date getAanmaakdatum()
	{
		return aanmaakdatum;
	}

	public void setAanmaakdatum(Date aanmaakdatum)
	{
		this.aanmaakdatum = aanmaakdatum;
	}

}
