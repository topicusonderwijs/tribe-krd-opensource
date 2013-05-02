package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.HoogsteVooropleiding;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Leerweg;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

/**
 * Inschrijvingrecord in een BO fotobestand
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotoBOInschrijvingRecord extends BronFotoBORecord implements
		IBronFotoInschrijvingRecord
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true, length = 5)
	private String codeOpleiding;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumInschrijving;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date geplandeUitschrijfdatum;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date werkelijkeUitschrijfdatum;

	@Column(nullable = true, length = 4)
	private String locatie;

	@Column(nullable = true, length = 5)
	private String indicatieBekostiging;

	@Column(nullable = true)
	private Boolean indicatieGehandicapt;

	@Column(nullable = true)
	private Boolean indicatieLesgeld;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private HoogsteVooropleiding hoogsteVooropleiding;

	@Column(nullable = true, length = 5)
	private String opleidingAfgelopenJaar;

	@Column(nullable = true)
	private Boolean indicatieRisicoDeelnemer;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private Leerweg leerweg;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private Intensiteit intensiteit;

	@Column(nullable = true, length = 2)
	private String redenUitstroom;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inschrijvingRecord", targetEntity = BronFotoBORecord.class)
	@Where(clause = "DTYPE='BronFotoBODiplomaKwalificatieRecord'")
	private List<BronFotoBODiplomaKwalificatieRecord> examenRecords =
		new ArrayList<BronFotoBODiplomaKwalificatieRecord>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inschrijvingRecord", targetEntity = BronFotoBORecord.class)
	@Where(clause = "DTYPE='BronFotoBOBPVRecord'")
	private List<BronFotoBOBPVRecord> bpvRecords = new ArrayList<BronFotoBOBPVRecord>();

	public BronFotoBOInschrijvingRecord()
	{
	}

	public BronFotoBOInschrijvingRecord(String[] velden)
	{
		super(velden);
		codeOpleiding = velden[9];
		datumInschrijving = TimeUtil.getInstance().isoStringAsDate(velden[10]);
		geplandeUitschrijfdatum = TimeUtil.getInstance().isoStringAsDate(velden[11]);
		werkelijkeUitschrijfdatum = TimeUtil.getInstance().isoStringAsDate(velden[12]);
		locatie = velden[13];
		indicatieBekostiging = velden[14];
		indicatieGehandicapt = parseBronBoolean(velden[15]);
		indicatieLesgeld = parseBronBoolean(velden[16]);
		hoogsteVooropleiding = HoogsteVooropleiding.BachelorWO.parse(velden[17]);
		opleidingAfgelopenJaar = velden[18];
		indicatieRisicoDeelnemer = parseBronBoolean(velden[19]);
		leerweg = Leerweg.parseBronFotoRepresentation(velden[20]);
		intensiteit = Intensiteit.Deeltijd.parse(velden[21]);
		if (velden.length >= 23)
		{
			redenUitstroom = velden[22];
		}
	}

	public boolean isIndicatieBekostigingInschrijvingOpTeldatumInRecord()
	{
		if (getIndicatieBekostiging() == null || getIndicatieBekostiging().length() < 5)
			return false;
		return getIndicatieBekostiging().substring(0, 1).equals("J");
	}

	public String getIndicatieBekostigingInschrijvingOpTeldatumInRecordOmschrijving()
	{
		return isIndicatieBekostigingInschrijvingOpTeldatumInRecord() ? "Ja" : "Nee";
	}

	public boolean isIndicatieBekostigingInschrijvingOp1Februari()
	{
		if (getIndicatieBekostiging() == null || getIndicatieBekostiging().length() < 5)
			return false;
		return getIndicatieBekostiging().substring(1, 2).equals("J");
	}

	public String getIndicatieBekostigingInschrijvingOp1FebruariOmschrijving()
	{
		return isIndicatieBekostigingInschrijvingOp1Februari() ? "Ja" : "Nee";
	}

	public String getCodeOpleiding()
	{
		return codeOpleiding;
	}

	public void setCodeOpleiding(String codeOpleiding)
	{
		this.codeOpleiding = codeOpleiding;
	}

	public Date getDatumInschrijving()
	{
		return datumInschrijving;
	}

	public void setDatumInschrijving(Date datumInschrijving)
	{
		this.datumInschrijving = datumInschrijving;
	}

	public Date getGeplandeUitschrijfdatum()
	{
		return geplandeUitschrijfdatum;
	}

	public void setGeplandeUitschrijfdatum(Date geplandeUitschrijfdatum)
	{
		this.geplandeUitschrijfdatum = geplandeUitschrijfdatum;
	}

	public Date getWerkelijkeUitschrijfdatum()
	{
		return werkelijkeUitschrijfdatum;
	}

	public void setWerkelijkeUitschrijfdatum(Date werkelijkeUitschrijfdatum)
	{
		this.werkelijkeUitschrijfdatum = werkelijkeUitschrijfdatum;
	}

	public String getLocatie()
	{
		return locatie;
	}

	public void setLocatie(String locatie)
	{
		this.locatie = locatie;
	}

	public String getIndicatieBekostiging()
	{
		return indicatieBekostiging;
	}

	public void setIndicatieBekostiging(String indicatieBekostiging)
	{
		this.indicatieBekostiging = indicatieBekostiging;
	}

	public Boolean getIndicatieGehandicapt()
	{
		return indicatieGehandicapt;
	}

	public void setIndicatieGehandicapt(Boolean indicatieGehandicapt)
	{
		this.indicatieGehandicapt = indicatieGehandicapt;
	}

	public String getIndicatieGehandicaptOmschrijving()
	{
		return getIndicatieGehandicapt().booleanValue() ? "Ja" : "Nee";
	}

	public Boolean getIndicatieLesgeld()
	{
		return indicatieLesgeld;
	}

	public void setIndicatieLesgeld(Boolean indicatieLesgeld)
	{
		this.indicatieLesgeld = indicatieLesgeld;
	}

	public HoogsteVooropleiding getHoogsteVooropleiding()
	{
		return hoogsteVooropleiding;
	}

	public void setHoogsteVooropleiding(HoogsteVooropleiding hoogsteVooropleiding)
	{
		this.hoogsteVooropleiding = hoogsteVooropleiding;
	}

	public String getOpleidingAfgelopenJaar()
	{
		return opleidingAfgelopenJaar;
	}

	public void setOpleidingAfgelopenJaar(String opleidingAfgelopenJaar)
	{
		this.opleidingAfgelopenJaar = opleidingAfgelopenJaar;
	}

	public Boolean getIndicatieRisicoDeelnemer()
	{
		return indicatieRisicoDeelnemer;
	}

	public void setIndicatieRisicoDeelnemer(Boolean indicatieRisicoDeelnemer)
	{
		this.indicatieRisicoDeelnemer = indicatieRisicoDeelnemer;
	}

	public Leerweg getLeerweg()
	{
		return leerweg;
	}

	public void setLeerweg(Leerweg leerweg)
	{
		this.leerweg = leerweg;
	}

	public Intensiteit getIntensiteit()
	{
		return intensiteit;
	}

	public void setIntensiteit(Intensiteit intensiteit)
	{
		this.intensiteit = intensiteit;
	}

	public String getRedenUitstroom()
	{
		return redenUitstroom;
	}

	public void setRedenUitstroom(String redenUitstroom)
	{
		this.redenUitstroom = redenUitstroom;
	}

	@Override
	public Integer getVolgnummerNumeriek()
	{
		String vlgnr = getInschrijvingsvolgnummer();
		if (StringUtil.isNotEmpty(vlgnr))
		{
			return StringUtil.getFirstNumberSequence(vlgnr);
		}
		return null;
	}

	public List<BronFotoBODiplomaKwalificatieRecord> getExamenRecords()
	{
		return examenRecords;
	}

	public void setExamenRecords(List<BronFotoBODiplomaKwalificatieRecord> examenRecords)
	{
		this.examenRecords = examenRecords;
	}

	public List<BronFotoBOBPVRecord> getBpvRecords()
	{
		return bpvRecords;
	}

	public void setBpvRecords(List<BronFotoBOBPVRecord> bpvRecords)
	{
		this.bpvRecords = bpvRecords;
	}

	@Override
	public Class< ? extends IBronFotoOnderwijsontvangendeRecord> getOnderwijsontvangendeRecordClass()
	{
		return BronFotoBOOnderwijsontvangendeRecord.class;
	}

}
