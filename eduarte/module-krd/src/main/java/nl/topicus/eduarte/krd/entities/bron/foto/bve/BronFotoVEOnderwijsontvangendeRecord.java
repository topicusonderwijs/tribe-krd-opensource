package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import nl.topicus.cobra.types.personalia.Geslacht;
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
public class BronFotoVEOnderwijsontvangendeRecord extends BronFotoVERecord implements
		IBronFotoOnderwijsontvangendeRecord
{
	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private Geslacht geslacht;

	private Date geboortedatum;

	private String postcodecijfers;

	private Date overlijdensdatum;

	private Date datumVestigingInNederland;

	private Date datumVertrekUitNederland;

	private String codeGeboorteland;

	private String codeGeboortelandOuder1;

	@Enumerated(EnumType.STRING)
	private Geslacht geslachtOuder1;

	private String codeGeboortelandOuder2;

	@Enumerated(EnumType.STRING)
	private Geslacht geslachtOuder2;

	private String codeLandWaarnaarVertrokken;

	private String codeVerblijfstitel;

	private String codeNationaliteit1;

	private String codeNationaliteit2;

	private Integer leeftijdOpMeetdatum1;

	private Date leeftijdmeetdatum1;

	private Integer leeftijdOpMeetdatum2;

	private Date leeftijdmeetdatum2;

	private Integer leeftijdOpMeetdatum3;

	private Date leeftijdmeetdatum3;

	private Integer leeftijdOpMeetdatum4;

	private Date leeftijdmeetdatum4;

	private Integer leeftijdOpMeetdatum5;

	private Date leeftijdmeetdatum5;

	public BronFotoVEOnderwijsontvangendeRecord()
	{
	}

	public BronFotoVEOnderwijsontvangendeRecord(String[] velden)
	{
		super(velden);
		geslacht = Geslacht.parse(velden[12]);
		geboortedatum = TimeUtil.getInstance().isoStringAsDate(velden[13]);
		postcodecijfers = velden[14];
		overlijdensdatum = TimeUtil.getInstance().isoStringAsDate(velden[15]);
		datumVestigingInNederland = TimeUtil.getInstance().isoStringAsDate(velden[16]);
		datumVertrekUitNederland = TimeUtil.getInstance().isoStringAsDate(velden[17]);
		codeGeboorteland = velden[18];
		codeGeboortelandOuder1 = velden[19];
		geslachtOuder1 = Geslacht.parse(velden[20]);
		codeGeboortelandOuder2 = velden[21];
		geslachtOuder2 = Geslacht.parse(velden[22]);
		codeLandWaarnaarVertrokken = velden[23];
		codeVerblijfstitel = velden[24];
		codeNationaliteit1 = velden[25];
		codeNationaliteit2 = velden[26];
		leeftijdOpMeetdatum1 = StringUtil.isEmpty(velden[27]) ? null : Integer.valueOf(velden[27]);
		leeftijdmeetdatum1 = TimeUtil.getInstance().isoStringAsDate(velden[28]);
		leeftijdOpMeetdatum2 = StringUtil.isEmpty(velden[29]) ? null : Integer.valueOf(velden[29]);
		leeftijdmeetdatum2 = TimeUtil.getInstance().isoStringAsDate(velden[30]);
		leeftijdOpMeetdatum3 = StringUtil.isEmpty(velden[31]) ? null : Integer.valueOf(velden[31]);
		leeftijdmeetdatum3 = TimeUtil.getInstance().isoStringAsDate(velden[32]);
		leeftijdOpMeetdatum4 = StringUtil.isEmpty(velden[33]) ? null : Integer.valueOf(velden[33]);
		leeftijdmeetdatum4 = TimeUtil.getInstance().isoStringAsDate(velden[34]);
		leeftijdOpMeetdatum5 = StringUtil.isEmpty(velden[35]) ? null : Integer.valueOf(velden[35]);
		leeftijdmeetdatum5 = TimeUtil.getInstance().isoStringAsDate(velden[36]);
	}

	public Geslacht getGeslacht()
	{
		return geslacht;
	}

	public void setGeslacht(Geslacht geslacht)
	{
		this.geslacht = geslacht;
	}

	public Date getGeboortedatum()
	{
		return geboortedatum;
	}

	public void setGeboortedatum(Date geboortedatum)
	{
		this.geboortedatum = geboortedatum;
	}

	public String getPostcodecijfers()
	{
		return postcodecijfers;
	}

	public void setPostcodecijfers(String postcodecijfers)
	{
		this.postcodecijfers = postcodecijfers;
	}

	public Date getOverlijdensdatum()
	{
		return overlijdensdatum;
	}

	public void setOverlijdensdatum(Date overlijdensdatum)
	{
		this.overlijdensdatum = overlijdensdatum;
	}

	public Date getDatumVestigingInNederland()
	{
		return datumVestigingInNederland;
	}

	public void setDatumVestigingInNederland(Date datumVestigingInNederland)
	{
		this.datumVestigingInNederland = datumVestigingInNederland;
	}

	public Date getDatumVertrekUitNederland()
	{
		return datumVertrekUitNederland;
	}

	public void setDatumVertrekUitNederland(Date datumVertrekUitNederland)
	{
		this.datumVertrekUitNederland = datumVertrekUitNederland;
	}

	public String getCodeGeboorteland()
	{
		return codeGeboorteland;
	}

	public void setCodeGeboorteland(String codeGeboorteland)
	{
		this.codeGeboorteland = codeGeboorteland;
	}

	public String getCodeGeboortelandOuder1()
	{
		return codeGeboortelandOuder1;
	}

	public void setCodeGeboortelandOuder1(String codeGeboortelandOuder1)
	{
		this.codeGeboortelandOuder1 = codeGeboortelandOuder1;
	}

	public Geslacht getGeslachtOuder1()
	{
		return geslachtOuder1;
	}

	public void setGeslachtOuder1(Geslacht geslachtOuder1)
	{
		this.geslachtOuder1 = geslachtOuder1;
	}

	public String getCodeGeboortelandOuder2()
	{
		return codeGeboortelandOuder2;
	}

	public void setCodeGeboortelandOuder2(String codeGeboortelandOuder2)
	{
		this.codeGeboortelandOuder2 = codeGeboortelandOuder2;
	}

	public Geslacht getGeslachtOuder2()
	{
		return geslachtOuder2;
	}

	public void setGeslachtOuder2(Geslacht geslachtOuder2)
	{
		this.geslachtOuder2 = geslachtOuder2;
	}

	public String getCodeLandWaarnaarVertrokken()
	{
		return codeLandWaarnaarVertrokken;
	}

	public void setCodeLandWaarnaarVertrokken(String codeLandWaarnaarVertrokken)
	{
		this.codeLandWaarnaarVertrokken = codeLandWaarnaarVertrokken;
	}

	public String getCodeVerblijfstitel()
	{
		return codeVerblijfstitel;
	}

	public void setCodeVerblijfstitel(String codeVerblijfstitel)
	{
		this.codeVerblijfstitel = codeVerblijfstitel;
	}

	public String getCodeNationaliteit1()
	{
		return codeNationaliteit1;
	}

	public void setCodeNationaliteit1(String codeNationaliteit1)
	{
		this.codeNationaliteit1 = codeNationaliteit1;
	}

	public String getCodeNationaliteit2()
	{
		return codeNationaliteit2;
	}

	public void setCodeNationaliteit2(String codeNationaliteit2)
	{
		this.codeNationaliteit2 = codeNationaliteit2;
	}

	public Integer getLeeftijdOpMeetdatum1()
	{
		return leeftijdOpMeetdatum1;
	}

	public void setLeeftijdOpMeetdatum1(Integer leeftijdOpMeetdatum1)
	{
		this.leeftijdOpMeetdatum1 = leeftijdOpMeetdatum1;
	}

	public Date getLeeftijdmeetdatum1()
	{
		return leeftijdmeetdatum1;
	}

	public void setLeeftijdmeetdatum1(Date leeftijdmeetdatum1)
	{
		this.leeftijdmeetdatum1 = leeftijdmeetdatum1;
	}

	public Integer getLeeftijdOpMeetdatum2()
	{
		return leeftijdOpMeetdatum2;
	}

	public void setLeeftijdOpMeetdatum2(Integer leeftijdOpMeetdatum2)
	{
		this.leeftijdOpMeetdatum2 = leeftijdOpMeetdatum2;
	}

	public Date getLeeftijdmeetdatum2()
	{
		return leeftijdmeetdatum2;
	}

	public void setLeeftijdmeetdatum2(Date leeftijdmeetdatum2)
	{
		this.leeftijdmeetdatum2 = leeftijdmeetdatum2;
	}

	public Integer getLeeftijdOpMeetdatum3()
	{
		return leeftijdOpMeetdatum3;
	}

	public void setLeeftijdOpMeetdatum3(Integer leeftijdOpMeetdatum3)
	{
		this.leeftijdOpMeetdatum3 = leeftijdOpMeetdatum3;
	}

	public Date getLeeftijdmeetdatum3()
	{
		return leeftijdmeetdatum3;
	}

	public void setLeeftijdmeetdatum3(Date leeftijdmeetdatum3)
	{
		this.leeftijdmeetdatum3 = leeftijdmeetdatum3;
	}

	public Integer getLeeftijdOpMeetdatum4()
	{
		return leeftijdOpMeetdatum4;
	}

	public void setLeeftijdOpMeetdatum4(Integer leeftijdOpMeetdatum4)
	{
		this.leeftijdOpMeetdatum4 = leeftijdOpMeetdatum4;
	}

	public Date getLeeftijdmeetdatum4()
	{
		return leeftijdmeetdatum4;
	}

	public void setLeeftijdmeetdatum4(Date leeftijdmeetdatum4)
	{
		this.leeftijdmeetdatum4 = leeftijdmeetdatum4;
	}

	public Integer getLeeftijdOpMeetdatum5()
	{
		return leeftijdOpMeetdatum5;
	}

	public void setLeeftijdOpMeetdatum5(Integer leeftijdOpMeetdatum5)
	{
		this.leeftijdOpMeetdatum5 = leeftijdOpMeetdatum5;
	}

	public Date getLeeftijdmeetdatum5()
	{
		return leeftijdmeetdatum5;
	}

	public void setLeeftijdmeetdatum5(Date leeftijdmeetdatum5)
	{
		this.leeftijdmeetdatum5 = leeftijdmeetdatum5;
	}

	@Override
	public String getGeboorteJaarEnMaand()
	{
		return null;
	}
}
