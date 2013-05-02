package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.HoogsteVooropleiding;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Leerweg;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiCategorie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiRatio;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotoVOInschrijvingRecord extends BronFotoVORecord implements
		IBronFotoInschrijvingRecord
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@Enumerated(value = EnumType.STRING)
	private CumiCategorie cumiCategorie;

	@Column(nullable = true)
	@Enumerated(value = EnumType.STRING)
	private CumiRatio cumiRatio;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumInschrijving;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumUitschrijving;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inschrijvingRecord")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BronFotoVOOpleidingRecord> opleidingRecords =
		new ArrayList<BronFotoVOOpleidingRecord>();

	public BronFotoVOInschrijvingRecord()
	{
	}

	public BronFotoVOInschrijvingRecord(String[] velden)
	{
		super(velden);
		cumiCategorie = CumiCategorie.Categorie3a.parse(velden[13]);
		cumiRatio = CumiRatio.a1.parse(velden[14]);
		datumInschrijving = TimeUtil.getInstance().isoStringAsDate(velden[16]);
		datumUitschrijving = TimeUtil.getInstance().isoStringAsDate(velden[17]);
	}

	public Date getDatumInschrijving()
	{
		return datumInschrijving;
	}

	public void setDatumInschrijving(Date datumInschrijving)
	{
		this.datumInschrijving = datumInschrijving;
	}

	public Date getDatumUitschrijving()
	{
		return datumUitschrijving;
	}

	public void setDatumUitschrijving(Date datumUitschrijving)
	{
		this.datumUitschrijving = datumUitschrijving;
	}

	public CumiCategorie getCumiCategorie()
	{
		return cumiCategorie;
	}

	public void setCumiCategorie(CumiCategorie cumiCategorie)
	{
		this.cumiCategorie = cumiCategorie;
	}

	public CumiRatio getCumiRatio()
	{
		return cumiRatio;
	}

	public void setCumiRatio(CumiRatio cumiRatio)
	{
		this.cumiRatio = cumiRatio;
	}

	@Override
	public String getCodeOpleiding()
	{
		BronFotoVOOpleidingRecord opleidingRecord = getOpleidingRecord();
		return opleidingRecord == null ? null : opleidingRecord.getElementcode();
	}

	@Override
	public Date getGeplandeUitschrijfdatum()
	{
		return null;
	}

	@Override
	public HoogsteVooropleiding getHoogsteVooropleiding()
	{
		return null;
	}

	@Override
	public String getIndicatieBekostigingInschrijvingOp1FebruariOmschrijving()
	{
		return "J";
	}

	@Override
	public String getIndicatieBekostigingInschrijvingOpTeldatumInRecordOmschrijving()
	{
		return "J";
	}

	@Override
	public Boolean getIndicatieGehandicapt()
	{
		return null;
	}

	@Override
	public String getIndicatieGehandicaptOmschrijving()
	{
		return null;
	}

	@Override
	public Intensiteit getIntensiteit()
	{
		return null;
	}

	@Override
	public Leerweg getLeerweg()
	{
		return null;
	}

	@Override
	public Class< ? extends IBronFotoOnderwijsontvangendeRecord> getOnderwijsontvangendeRecordClass()
	{
		return BronFotoVOOnderwijsontvangendeRecord.class;
	}

	@Override
	public String getRedenUitstroom()
	{
		return null;
	}

	@Override
	public Integer getVolgnummerNumeriek()
	{
		return null;
	}

	@Override
	public Date getWerkelijkeUitschrijfdatum()
	{
		return datumUitschrijving;
	}

	@Override
	public boolean isIndicatieBekostigingInschrijvingOp1Februari()
	{
		return true;
	}

	@Override
	public boolean isIndicatieBekostigingInschrijvingOpTeldatumInRecord()
	{
		return true;
	}

	public BronFotoVOOpleidingRecord getOpleidingRecord()
	{
		List<BronFotoVOOpleidingRecord> list = getOpleidingRecords();
		if (!list.isEmpty())
		{
			return list.get(0);
		}
		return null;
	}

	public List<BronFotoVOOpleidingRecord> getOpleidingRecords()
	{
		return opleidingRecords;
	}

	public void setOpleidingRecords(List<BronFotoVOOpleidingRecord> opleidingRecords)
	{
		this.opleidingRecords = opleidingRecords;
	}

}
