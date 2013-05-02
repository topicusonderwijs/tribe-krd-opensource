package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.util.StringUtil;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * 
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotoVOOpleidingRecord extends BronFotoVORecord
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true, length = 8)
	private String elementcode;

	@Column(nullable = true)
	private Integer leerjaar;

	@Column(nullable = true)
	private Integer aantalJarenPraktijkonderwijs;

	@Column(nullable = true, length = 10)
	private String vestigingsBrinnummer;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "voInschrijvingRecord")
	@Index(name = "idx_BronFotoReco_voInschr")
	private BronFotoVOInschrijvingRecord inschrijvingRecord;

	public BronFotoVOOpleidingRecord()
	{
	}

	public BronFotoVOOpleidingRecord(String[] velden,
			BronFotoVOInschrijvingRecord inschrijvingRecord)
	{
		super(velden);
		setInschrijvingRecord(inschrijvingRecord);
		elementcode = velden[7];
		leerjaar = StringUtil.isEmpty(velden[13]) ? null : Integer.valueOf(velden[13]);
		aantalJarenPraktijkonderwijs =
			StringUtil.isEmpty(velden[14]) ? null : Integer.valueOf(velden[14]);
		vestigingsBrinnummer = velden[15];
	}

	public String getElementcode()
	{
		return elementcode;
	}

	public void setElementcode(String elementcode)
	{
		this.elementcode = elementcode;
	}

	public Integer getLeerjaar()
	{
		return leerjaar;
	}

	public void setLeerjaar(Integer leerjaar)
	{
		this.leerjaar = leerjaar;
	}

	public Integer getAantalJarenPraktijkonderwijs()
	{
		return aantalJarenPraktijkonderwijs;
	}

	public void setAantalJarenPraktijkonderwijs(Integer aantalJarenPraktijkonderwijs)
	{
		this.aantalJarenPraktijkonderwijs = aantalJarenPraktijkonderwijs;
	}

	public String getVestigingsBrinnummer()
	{
		return vestigingsBrinnummer;
	}

	public void setVestigingsBrinnummer(String vestigingsBrinnummer)
	{
		this.vestigingsBrinnummer = vestigingsBrinnummer;
	}

	public BronFotoVOInschrijvingRecord getInschrijvingRecord()
	{
		return inschrijvingRecord;
	}

	public void setInschrijvingRecord(BronFotoVOInschrijvingRecord inschrijvingRecord)
	{
		this.inschrijvingRecord = inschrijvingRecord;
	}

}
