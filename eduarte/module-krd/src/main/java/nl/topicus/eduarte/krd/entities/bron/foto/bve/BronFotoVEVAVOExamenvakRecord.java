package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import javax.persistence.*;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.ToepassingResultaatExamenvak;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingSchoolExamen;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.HogerNiveau;

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
public class BronFotoVEVAVOExamenvakRecord extends BronFotoVERecord
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private Boolean diplomavak;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private ToepassingResultaatExamenvak toepassingExamenvak;

	@Column(nullable = true)
	private Boolean indicatieWerkstuk;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private HogerNiveau hogerNiveau;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private BeoordelingSchoolExamen beoordelingSchoolExamen;

	@Column(nullable = true)
	private Integer cijferSchoolexamen;

	@Column(nullable = true)
	private Integer cijferCE1;

	@Column(nullable = true)
	private Integer cijferCE2;

	@Column(nullable = true)
	private Integer cijferCE3;

	@Column(nullable = true)
	private Integer eindcijfer1;

	@Column(nullable = true)
	private Integer eindcijfer2;

	@Column(nullable = true)
	private Integer eindcijfer3;

	@Column(nullable = true)
	private Integer cijferCijferlijst;

	@Column(nullable = true)
	private Boolean verwezenNaarVolgendTijdvak;

	@Column(nullable = true)
	private Boolean certificaatOpVakniveau;

	@Column(nullable = true)
	private Boolean indicatieCombinatiecijfer;

	@Column(nullable = true, length = 4)
	private String vakcodeHogerNiveau;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "examenRecord")
	@Index(name = "idx_BronVEVAVO_examenRecord")
	private BronFotoVEVAVOExamenRecord examenRecord;

	public BronFotoVEVAVOExamenvakRecord()
	{
	}

	public BronFotoVEVAVOExamenvakRecord(String[] velden, BronFotoVEVAVOExamenRecord examenRecord)
	{
		super(velden);
		setExamenRecord(examenRecord);
		diplomavak = parseBronBoolean(velden[12]);
		toepassingExamenvak = ToepassingResultaatExamenvak.Dispensatie.parse(velden[13]);
		indicatieWerkstuk = parseBronBoolean(velden[14]);
		hogerNiveau = HogerNiveau.VWO.parse(velden[15]);
		beoordelingSchoolExamen = BeoordelingSchoolExamen.Goed.parse(velden[16]);
		cijferSchoolexamen = StringUtil.isEmpty(velden[17]) ? null : Integer.valueOf(velden[17]);
		cijferCE1 = StringUtil.isEmpty(velden[18]) ? null : Integer.valueOf(velden[18]);
		cijferCE2 = StringUtil.isEmpty(velden[19]) ? null : Integer.valueOf(velden[19]);
		cijferCE3 = StringUtil.isEmpty(velden[20]) ? null : Integer.valueOf(velden[20]);
		eindcijfer1 = StringUtil.isEmpty(velden[21]) ? null : Integer.valueOf(velden[21]);
		eindcijfer2 = StringUtil.isEmpty(velden[22]) ? null : Integer.valueOf(velden[22]);
		eindcijfer3 = StringUtil.isEmpty(velden[23]) ? null : Integer.valueOf(velden[23]);
		cijferCijferlijst = StringUtil.isEmpty(velden[24]) ? null : Integer.valueOf(velden[24]);
		verwezenNaarVolgendTijdvak = parseBronBoolean(velden[25]);
		certificaatOpVakniveau = parseBronBoolean(velden[26]);
		indicatieCombinatiecijfer = parseBronBoolean(velden[27]);
		vakcodeHogerNiveau = velden[28];
	}

	public Boolean getDiplomavak()
	{
		return diplomavak;
	}

	public void setDiplomavak(Boolean diplomavak)
	{
		this.diplomavak = diplomavak;
	}

	public ToepassingResultaatExamenvak getToepassingExamenvak()
	{
		return toepassingExamenvak;
	}

	public void setToepassingExamenvak(ToepassingResultaatExamenvak toepassingExamenvak)
	{
		this.toepassingExamenvak = toepassingExamenvak;
	}

	public Boolean getIndicatieWerkstuk()
	{
		return indicatieWerkstuk;
	}

	public void setIndicatieWerkstuk(Boolean indicatieWerkstuk)
	{
		this.indicatieWerkstuk = indicatieWerkstuk;
	}

	public BeoordelingSchoolExamen getBeoordelingSchoolExamen()
	{
		return beoordelingSchoolExamen;
	}

	public void setBeoordelingSchoolExamen(BeoordelingSchoolExamen beoordelingSchoolExamen)
	{
		this.beoordelingSchoolExamen = beoordelingSchoolExamen;
	}

	public Integer getCijferCE1()
	{
		return cijferCE1;
	}

	public void setCijferCE1(Integer cijferCE1)
	{
		this.cijferCE1 = cijferCE1;
	}

	public Integer getCijferCE2()
	{
		return cijferCE2;
	}

	public void setCijferCE2(Integer cijferCE2)
	{
		this.cijferCE2 = cijferCE2;
	}

	public Integer getCijferCE3()
	{
		return cijferCE3;
	}

	public void setCijferCE3(Integer cijferCE3)
	{
		this.cijferCE3 = cijferCE3;
	}

	public Integer getEindcijfer1()
	{
		return eindcijfer1;
	}

	public void setEindcijfer1(Integer eindcijfer1)
	{
		this.eindcijfer1 = eindcijfer1;
	}

	public Integer getEindcijfer2()
	{
		return eindcijfer2;
	}

	public void setEindcijfer2(Integer eindcijfer2)
	{
		this.eindcijfer2 = eindcijfer2;
	}

	public Integer getEindcijfer3()
	{
		return eindcijfer3;
	}

	public void setEindcijfer3(Integer eindcijfer3)
	{
		this.eindcijfer3 = eindcijfer3;
	}

	public Integer getCijferCijferlijst()
	{
		return cijferCijferlijst;
	}

	public void setCijferCijferlijst(Integer cijferCijferlijst)
	{
		this.cijferCijferlijst = cijferCijferlijst;
	}

	public Boolean getVerwezenNaarVolgendTijdvak()
	{
		return verwezenNaarVolgendTijdvak;
	}

	public void setVerwezenNaarVolgendTijdvak(Boolean verwezenNaarVolgendTijdvak)
	{
		this.verwezenNaarVolgendTijdvak = verwezenNaarVolgendTijdvak;
	}

	public Boolean getCertificaatOpVakniveau()
	{
		return certificaatOpVakniveau;
	}

	public void setCertificaatOpVakniveau(Boolean certificaatOpVakniveau)
	{
		this.certificaatOpVakniveau = certificaatOpVakniveau;
	}

	public Boolean getIndicatieCombinatiecijfer()
	{
		return indicatieCombinatiecijfer;
	}

	public void setIndicatieCombinatiecijfer(Boolean indicatieCombinatiecijfer)
	{
		this.indicatieCombinatiecijfer = indicatieCombinatiecijfer;
	}

	public String getVakcodeHogerNiveau()
	{
		return vakcodeHogerNiveau;
	}

	public void setVakcodeHogerNiveau(String vakcodeHogerNiveau)
	{
		this.vakcodeHogerNiveau = vakcodeHogerNiveau;
	}

	public HogerNiveau getHogerNiveau()
	{
		return hogerNiveau;
	}

	public void setHogerNiveau(HogerNiveau hogerNiveau)
	{
		this.hogerNiveau = hogerNiveau;
	}

	public Integer getCijferSchoolexamen()
	{
		return cijferSchoolexamen;
	}

	public void setCijferSchoolexamen(Integer cijferSchoolexamen)
	{
		this.cijferSchoolexamen = cijferSchoolexamen;
	}

	public BronFotoVEVAVOExamenRecord getExamenRecord()
	{
		return examenRecord;
	}

	public void setExamenRecord(BronFotoVEVAVOExamenRecord examenRecord)
	{
		this.examenRecord = examenRecord;
	}

}
