package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import java.util.Date;

import javax.persistence.*;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.onderwijs.duo.bron.bve.foto.pve_9_9.BronFotoRecordType;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.FotoOnderwijssector;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import com.google.gdata.util.common.base.StringUtil;

/**
 * Abstract base class voor alle BVE-bron fotorecords.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorColumn(length = 60)
public abstract class BronFotoRecord extends InstellingEntiteit implements IBronFotoRecord
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "bestand")
	@Index(name = "idx_BronFotoRecord_bestand")
	private BronFotobestand bestand;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private BronFotoRecordType recordtype;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private FotoOnderwijssector onderwijssector;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = false)
	private Date teldatum;

	@Column(length = 1, nullable = false)
	private String status;

	@Column(nullable = true)
	private Long pgn;

	@Column(nullable = true, length = 5)
	private String inschrijvingsvolgnummer;

	@Column(nullable = true, length = 4)
	private String brinnummer;

	public BronFotoRecord()
	{
	}

	public BronFotoRecord(String[] velden)
	{
		if (velden.length < 9)
			throw new IllegalArgumentException("Niet genoeg velden");
		recordtype = BronFotoRecordType.valueOf(velden[0]);
		onderwijssector = FotoOnderwijssector.valueOf(velden[1]);
		teldatum = TimeUtil.getInstance().isoStringAsDate(velden[2]);
		status = velden[3];
		pgn = StringUtil.isEmpty(velden[4]) ? null : Long.valueOf(velden[4]);
		inschrijvingsvolgnummer = velden[5];
		if (onderwijssector == FotoOnderwijssector.BO)
			brinnummer = velden[8];
		else if (onderwijssector == FotoOnderwijssector.VE)
			brinnummer = velden[11];
		else if (onderwijssector == FotoOnderwijssector.VO)
			brinnummer = velden[11];
	}

	public BronFotobestand getBestand()
	{
		return bestand;
	}

	public void setBestand(BronFotobestand bestand)
	{
		this.bestand = bestand;
	}

	public BronFotoRecordType getRecordtype()
	{
		return recordtype;
	}

	public void setRecordtype(BronFotoRecordType recordtype)
	{
		this.recordtype = recordtype;
	}

	public Date getTeldatum()
	{
		return teldatum;
	}

	public void setTeldatum(Date teldatum)
	{
		this.teldatum = teldatum;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Long getPgn()
	{
		return pgn;
	}

	public void setPgn(Long pgn)
	{
		this.pgn = pgn;
	}

	public String getInschrijvingsvolgnummer()
	{
		return inschrijvingsvolgnummer;
	}

	public void setInschrijvingsvolgnummer(String inschrijvingsvolgnummer)
	{
		this.inschrijvingsvolgnummer = inschrijvingsvolgnummer;
	}

	public String getBrinnummer()
	{
		return brinnummer;
	}

	public void setBrinnummer(String brinnummer)
	{
		this.brinnummer = brinnummer;
	}

	public FotoOnderwijssector getOnderwijssector()
	{
		return onderwijssector;
	}

	public void setOnderwijssector(FotoOnderwijssector onderwijssector)
	{
		this.onderwijssector = onderwijssector;
	}

	protected static Boolean parseBronBoolean(String tekst)
	{
		if (nl.topicus.cobra.util.StringUtil.isEmpty(tekst))
			return null;
		return tekst.equalsIgnoreCase("J");
	}

}
