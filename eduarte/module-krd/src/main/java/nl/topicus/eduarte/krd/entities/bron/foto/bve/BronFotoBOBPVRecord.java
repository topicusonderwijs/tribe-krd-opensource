package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.util.StringUtil;
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
public class BronFotoBOBPVRecord extends BronFotoBORecord
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date afsluitdatumBPV;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date begindatumBPV;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date geplandeEinddatumBPV;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date werkelijkeEinddatumBPV;

	@Column(nullable = true)
	private Integer omvangBPV;

	@Column(nullable = true, length = 40)
	private String codeLeerbedrijf;

	public BronFotoBOBPVRecord()
	{
	}

	public BronFotoBOBPVRecord(String[] velden, BronFotoBOInschrijvingRecord inschrijvingRecord)
	{
		super(velden);
		setInschrijvingRecord(inschrijvingRecord);
		afsluitdatumBPV = TimeUtil.getInstance().isoStringAsDate(velden[9]);
		begindatumBPV = TimeUtil.getInstance().isoStringAsDate(velden[10]);
		geplandeEinddatumBPV = TimeUtil.getInstance().isoStringAsDate(velden[11]);
		werkelijkeEinddatumBPV = TimeUtil.getInstance().isoStringAsDate(velden[12]);
		omvangBPV = StringUtil.isEmpty(velden[13]) ? null : Integer.valueOf(velden[13]);
		codeLeerbedrijf = velden[14];
	}

	public Date getAfsluitdatumBPV()
	{
		return afsluitdatumBPV;
	}

	public void setAfsluitdatumBPV(Date afsluitdatumBPV)
	{
		this.afsluitdatumBPV = afsluitdatumBPV;
	}

	public Date getBegindatumBPV()
	{
		return begindatumBPV;
	}

	public void setBegindatumBPV(Date begindatumBPV)
	{
		this.begindatumBPV = begindatumBPV;
	}

	public Date getGeplandeEinddatumBPV()
	{
		return geplandeEinddatumBPV;
	}

	public void setGeplandeEinddatumBPV(Date geplandeEinddatumBPV)
	{
		this.geplandeEinddatumBPV = geplandeEinddatumBPV;
	}

	public Date getWerkelijkeEinddatumBPV()
	{
		return werkelijkeEinddatumBPV;
	}

	public void setWerkelijkeEinddatumBPV(Date werkelijkeEinddatumBPV)
	{
		this.werkelijkeEinddatumBPV = werkelijkeEinddatumBPV;
	}

	public Integer getOmvangBPV()
	{
		return omvangBPV;
	}

	public void setOmvangBPV(Integer omvangBPV)
	{
		this.omvangBPV = omvangBPV;
	}

	public String getCodeLeerbedrijf()
	{
		return codeLeerbedrijf;
	}

	public void setCodeLeerbedrijf(String codeLeerbedrijf)
	{
		this.codeLeerbedrijf = codeLeerbedrijf;
	}
}
