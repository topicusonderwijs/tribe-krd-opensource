package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import javax.persistence.*;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.NT2Vaardigheid;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Base record voor fotorecords van volwasseneducatie.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotoVERecord extends BronFotoRecord
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true, length = 5)
	private String codeVavoExamen;

	@Column(nullable = true)
	private Integer examenjaar;

	@Column(nullable = true, length = 4)
	private String vakcode;

	@Column(nullable = true)
	private Integer vakvolgnummer;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private NT2Vaardigheid nt2Vaardigheid;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "veInschrijvingRecord")
	@Index(name = "idx_BronFotoReco_examenRecord")
	private BronFotoVEInschrijvingRecord inschrijvingRecord;

	public BronFotoVERecord()
	{
	}

	public BronFotoVERecord(String[] velden)
	{
		super(velden);
		codeVavoExamen = velden[6];
		examenjaar = StringUtil.isEmpty(velden[7]) ? null : Integer.valueOf(velden[7]);
		vakcode = velden[8];
		vakvolgnummer = StringUtil.isEmpty(velden[9]) ? null : Integer.valueOf(velden[9]);
		nt2Vaardigheid = NT2Vaardigheid.parseBronFoto(velden[10]);
	}

	public String getCodeVavoExamen()
	{
		return codeVavoExamen;
	}

	public void setCodeVavoExamen(String codeVavoExamen)
	{
		this.codeVavoExamen = codeVavoExamen;
	}

	public Integer getExamenjaar()
	{
		return examenjaar;
	}

	public void setExamenjaar(Integer examenjaar)
	{
		this.examenjaar = examenjaar;
	}

	public String getVakcode()
	{
		return vakcode;
	}

	public void setVakcode(String vakcode)
	{
		this.vakcode = vakcode;
	}

	public Integer getVakvolgnummer()
	{
		return vakvolgnummer;
	}

	public void setVakvolgnummer(Integer vakvolgnummer)
	{
		this.vakvolgnummer = vakvolgnummer;
	}

	public NT2Vaardigheid getNt2Vaardigheid()
	{
		return nt2Vaardigheid;
	}

	public void setNt2Vaardigheid(NT2Vaardigheid nt2Vaardigheid)
	{
		this.nt2Vaardigheid = nt2Vaardigheid;
	}

	public BronFotoVEInschrijvingRecord getInschrijvingRecord()
	{
		return inschrijvingRecord;
	}

	public void setInschrijvingRecord(BronFotoVEInschrijvingRecord inschrijvingRecord)
	{
		this.inschrijvingRecord = inschrijvingRecord;
	}
}
