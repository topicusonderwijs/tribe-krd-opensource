package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
public class BronFotoVEVakBasiseducatieRecord extends BronFotoVERecord
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumVakBehaald;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vakRecord")
	private List<BronFotoVEVaardigheidNT2Record> vaardigheidRecords =
		new ArrayList<BronFotoVEVaardigheidNT2Record>();

	public BronFotoVEVakBasiseducatieRecord()
	{
	}

	public BronFotoVEVakBasiseducatieRecord(String[] velden,
			BronFotoVEInschrijvingRecord inschrijvingRecord)
	{
		super(velden);
		setInschrijvingRecord(inschrijvingRecord);
		datumVakBehaald = TimeUtil.getInstance().isoStringAsDate(velden[12]);
	}

	public Date getDatumVakBehaald()
	{
		return datumVakBehaald;
	}

	public void setDatumVakBehaald(Date datumVakBehaald)
	{
		this.datumVakBehaald = datumVakBehaald;
	}

	public List<BronFotoVEVaardigheidNT2Record> getVaardigheidRecords()
	{
		return vaardigheidRecords;
	}

	public void setVaardigheidRecords(List<BronFotoVEVaardigheidNT2Record> vaardigheidRecords)
	{
		this.vaardigheidRecords = vaardigheidRecords;
	}

}
