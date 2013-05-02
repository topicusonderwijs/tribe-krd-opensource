package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.ToepassingBeoordelingWerkstuk;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.UitslagExamen;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingWerkstuk;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotoVEVAVOExamenRecord extends BronFotoVERecord
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumUitslagExamen;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private UitslagExamen uitslagExamen;

	@Column(nullable = true, length = 150)
	private String titelOfThemaWerkstuk;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private BeoordelingWerkstuk beoordelingWerkstuk;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private ToepassingBeoordelingWerkstuk toepassingWerkstuk;

	@Column(nullable = true, length = 3)
	private String cijferWerkstuk;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "examenRecord")
	private List<BronFotoVEVAVOExamenvakRecord> examenvakRecords =
		new ArrayList<BronFotoVEVAVOExamenvakRecord>();

	public BronFotoVEVAVOExamenRecord()
	{
	}

	public BronFotoVEVAVOExamenRecord(String[] velden,
			BronFotoVEInschrijvingRecord inschrijvingRecord)
	{
		super(velden);
		setInschrijvingRecord(inschrijvingRecord);
		datumUitslagExamen = TimeUtil.getInstance().isoStringAsDate(velden[12]);
		uitslagExamen = UitslagExamen.Afgewezen.parse(velden[13]);
		titelOfThemaWerkstuk = velden[14];
		beoordelingWerkstuk = BeoordelingWerkstuk.Goed.parse(velden[15]);
		toepassingWerkstuk = ToepassingBeoordelingWerkstuk.Dispensatie.parse(velden[16]);
		cijferWerkstuk = velden[17];
	}

	public Date getDatumUitslagExamen()
	{
		return datumUitslagExamen;
	}

	public void setDatumUitslagExamen(Date datumUitslagExamen)
	{
		this.datumUitslagExamen = datumUitslagExamen;
	}

	public UitslagExamen getUitslagExamen()
	{
		return uitslagExamen;
	}

	public void setUitslagExamen(UitslagExamen uitslagExamen)
	{
		this.uitslagExamen = uitslagExamen;
	}

	public String getTitelOfThemaWerkstuk()
	{
		return titelOfThemaWerkstuk;
	}

	public void setTitelOfThemaWerkstuk(String titelOfThemaWerkstuk)
	{
		this.titelOfThemaWerkstuk = titelOfThemaWerkstuk;
	}

	public BeoordelingWerkstuk getBeoordelingWerkstuk()
	{
		return beoordelingWerkstuk;
	}

	public void setBeoordelingWerkstuk(BeoordelingWerkstuk beoordelingWerkstuk)
	{
		this.beoordelingWerkstuk = beoordelingWerkstuk;
	}

	public ToepassingBeoordelingWerkstuk getToepassingWerkstuk()
	{
		return toepassingWerkstuk;
	}

	public void setToepassingWerkstuk(ToepassingBeoordelingWerkstuk toepassingWerkstuk)
	{
		this.toepassingWerkstuk = toepassingWerkstuk;
	}

	public String getCijferWerkstuk()
	{
		return cijferWerkstuk;
	}

	public void setCijferWerkstuk(String cijferWerkstuk)
	{
		this.cijferWerkstuk = cijferWerkstuk;
	}

	public List<BronFotoVEVAVOExamenvakRecord> getExamenvakRecords()
	{
		return examenvakRecords;
	}

	public void setExamenvakRecords(List<BronFotoVEVAVOExamenvakRecord> examenvakRecords)
	{
		this.examenvakRecords = examenvakRecords;
	}

}
