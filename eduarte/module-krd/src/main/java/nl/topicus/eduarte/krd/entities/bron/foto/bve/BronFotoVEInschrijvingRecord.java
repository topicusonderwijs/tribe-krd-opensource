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
 * 
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotoVEInschrijvingRecord extends BronFotoVERecord implements
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
	private Date geplandeDatumUitschrijving;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date werkelijkeDatumUitschrijving;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private HoogsteVooropleiding hoogsteVooropleiding;

	@Column(nullable = true)
	private Integer contacturenPerWeek;

	@Column(nullable = true)
	private Boolean indicatieNieuwkomer;

	@Column(nullable = true, length = 4)
	private String behaaldeOpleiding;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumOpleidingBehaald;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inschrijvingRecord", targetEntity = BronFotoVERecord.class)
	@Where(clause = "DTYPE='BronFotoVEVakBasiseducatieRecord'")
	private List<BronFotoVEVakBasiseducatieRecord> vakRecords =
		new ArrayList<BronFotoVEVakBasiseducatieRecord>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inschrijvingRecord", targetEntity = BronFotoVERecord.class)
	@Where(clause = "DTYPE='BronFotoVEVAVOExamenRecord'")
	private List<BronFotoVEVAVOExamenRecord> examenRecords =
		new ArrayList<BronFotoVEVAVOExamenRecord>();

	public BronFotoVEInschrijvingRecord()
	{
	}

	public BronFotoVEInschrijvingRecord(String[] velden)
	{
		super(velden);
		codeOpleiding = velden[12];
		datumInschrijving = TimeUtil.getInstance().isoStringAsDate(velden[13]);
		geplandeDatumUitschrijving = TimeUtil.getInstance().isoStringAsDate(velden[14]);
		werkelijkeDatumUitschrijving = TimeUtil.getInstance().isoStringAsDate(velden[15]);
		hoogsteVooropleiding = HoogsteVooropleiding.BachelorWO.parse(velden[16]);
		contacturenPerWeek = StringUtil.isEmpty(velden[17]) ? null : Integer.valueOf(velden[17]);
		indicatieNieuwkomer = parseBronBoolean(velden[18]);
		behaaldeOpleiding = velden[19];
		datumOpleidingBehaald = TimeUtil.getInstance().isoStringAsDate(velden[20]);
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

	public Date getGeplandeDatumUitschrijving()
	{
		return geplandeDatumUitschrijving;
	}

	public void setGeplandeDatumUitschrijving(Date geplandeDatumUitschrijving)
	{
		this.geplandeDatumUitschrijving = geplandeDatumUitschrijving;
	}

	public Date getWerkelijkeDatumUitschrijving()
	{
		return werkelijkeDatumUitschrijving;
	}

	public void setWerkelijkeDatumUitschrijving(Date werkelijkeDatumUitschrijving)
	{
		this.werkelijkeDatumUitschrijving = werkelijkeDatumUitschrijving;
	}

	public HoogsteVooropleiding getHoogsteVooropleiding()
	{
		return hoogsteVooropleiding;
	}

	public void setHoogsteVooropleiding(HoogsteVooropleiding hoogsteVooropleiding)
	{
		this.hoogsteVooropleiding = hoogsteVooropleiding;
	}

	public Integer getContacturenPerWeek()
	{
		return contacturenPerWeek;
	}

	public void setContacturenPerWeek(Integer contacturenPerWeek)
	{
		this.contacturenPerWeek = contacturenPerWeek;
	}

	public Boolean getIndicatieNieuwkomer()
	{
		return indicatieNieuwkomer;
	}

	public void setIndicatieNieuwkomer(Boolean indicatieNieuwkomer)
	{
		this.indicatieNieuwkomer = indicatieNieuwkomer;
	}

	public String getBehaaldeOpleiding()
	{
		return behaaldeOpleiding;
	}

	public void setBehaaldeOpleiding(String behaaldeOpleiding)
	{
		this.behaaldeOpleiding = behaaldeOpleiding;
	}

	public Date getDatumOpleidingBehaald()
	{
		return datumOpleidingBehaald;
	}

	public void setDatumOpleidingBehaald(Date datumOpleidingBehaald)
	{
		this.datumOpleidingBehaald = datumOpleidingBehaald;
	}

	public List<BronFotoVEVakBasiseducatieRecord> getVakRecords()
	{
		return vakRecords;
	}

	public void setVakRecords(List<BronFotoVEVakBasiseducatieRecord> vakRecords)
	{
		this.vakRecords = vakRecords;
	}

	public List<BronFotoVEVAVOExamenRecord> getExamenRecords()
	{
		return examenRecords;
	}

	public void setExamenRecords(List<BronFotoVEVAVOExamenRecord> examenRecords)
	{
		this.examenRecords = examenRecords;
	}

	@Override
	public Date getGeplandeUitschrijfdatum()
	{
		return getGeplandeDatumUitschrijving();
	}

	@Override
	public String getIndicatieBekostigingInschrijvingOp1FebruariOmschrijving()
	{
		// niet interessant voor VAVO en ED
		return null;
	}

	@Override
	public String getIndicatieBekostigingInschrijvingOpTeldatumInRecordOmschrijving()
	{
		// niet interessant voor VAVO en ED
		return null;
	}

	@Override
	public Boolean getIndicatieGehandicapt()
	{
		// niet interessant voor VAVO en ED
		return null;
	}

	@Override
	public String getIndicatieGehandicaptOmschrijving()
	{
		// niet interessant voor VAVO en ED
		return null;
	}

	@Override
	public Intensiteit getIntensiteit()
	{
		// niet interessant voor VAVO en ED
		return null;
	}

	@Override
	public Leerweg getLeerweg()
	{
		// niet interessant voor VAVO en ED
		return null;
	}

	@Override
	public Class< ? extends IBronFotoOnderwijsontvangendeRecord> getOnderwijsontvangendeRecordClass()
	{
		// Voorlopig BO onderwijsontvangende teruggeven. Lijkt er niet op dat dit
		// verschilt tussen ED/VAVO en BO.
		return BronFotoVEOnderwijsontvangendeRecord.class;
	}

	@Override
	public String getRedenUitstroom()
	{
		// niet interessant voor VAVO en ED
		return null;
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

	@Override
	public Date getWerkelijkeUitschrijfdatum()
	{
		return getWerkelijkeDatumUitschrijving();
	}

	@Override
	public boolean isIndicatieBekostigingInschrijvingOp1Februari()
	{
		// niet interessant voor VAVO en ED
		return false;
	}

	@Override
	public boolean isIndicatieBekostigingInschrijvingOpTeldatumInRecord()
	{
		// niet interessant voor VAVO en ED
		return false;
	}
}
