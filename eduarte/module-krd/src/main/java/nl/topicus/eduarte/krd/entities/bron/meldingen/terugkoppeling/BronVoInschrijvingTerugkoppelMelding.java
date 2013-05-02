package nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.HuisnummerAanduiding;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOTerugkoppelingReguliereMeldingDecentraal;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiCategorie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiRatio;

import org.hibernate.annotations.Type;

@Entity
@DiscriminatorValue(value = "210")
public class BronVoInschrijvingTerugkoppelMelding extends AbstractBronVoTerugkoppelMelding
		implements VOTerugkoppelingReguliereMeldingDecentraal
{
	private static final long serialVersionUID = 1L;

	@Column(length = 200)
	private String achternaam;

	@Column(length = 10)
	private String voorvoegsel;

	@Column(length = 200)
	private String alleVoornamen;

	@Column(length = 24)
	private String straatNaam;

	@Column(length = 5)
	private Integer huisNummer;

	@Column(length = 5)
	private String huisnummerToevoeging;

	@Column(length = 35)
	private String locatieOmschrijving;

	@Column
	@Enumerated(EnumType.STRING)
	private HuisnummerAanduiding huisnummerAanduiding;

	@Column(length = 24)
	private String plaatsnaam;

	@Column(length = 6)
	private String postcodeVolgensSchool;

	@Column(length = 4)
	private String landcodeVolgensSchool;

	@Column(length = 8, name = "ingangPostcLandcVolgSchool")
	private String datumIngangPostcodeLandcodeVolgensSchool;

	@Column(length = 35)
	private String adresregelBuitenland1;

	@Column(length = 35)
	private String adresregelBuitenland2;

	@Column(length = 35)
	private String adresregelBuitenland3;

	@Column(length = 4)
	private String codeLandAdres;

	@Column
	@Temporal(TemporalType.DATE)
	private Date datumOverlijden;

	@Column(name = "datumIngangAdres")
	@Temporal(TemporalType.DATE)
	private Date datumIngangAdresWijziging;

	@Column(length = 4)
	private String codeGeboorteland;

	@Column(length = 4)
	private String codeGeboortelandOuder1;

	@Column(length = 4)
	private String codeGeboortelandOuder2;

	@Column
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	private Datum datumVestigingInNederland;

	@Column
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	private Datum datumVertrekUitNederland;

	@Column(length = 4)
	private String codeLandWaarnaarVertrokken;

	@Column(length = 4)
	private String codeNationaliteit1;

	@Column
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	private Datum datumIngangNationaliteit1;

	@Column(length = 4)
	private String codeNationaliteit2;

	@Column
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	private Datum datumIngangNationaliteit2;

	@Column(length = 4)
	private String codeVerblijfstitel;

	@Column
	@Temporal(TemporalType.DATE)
	private Date datumIngangVerblijfstitel;

	@Column
	@Temporal(TemporalType.DATE)
	private Date datumEindeVerblijfstitel;

	@Column
	@Temporal(TemporalType.DATE)
	private Date ingangsDatum;

	@Column
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	private Datum eindDatum;

	@Column(length = 1)
	private Integer jarenPraktijkOnderwijs;

	@Column(length = 1)
	private Integer leerjaar;

	@Column
	@Enumerated(EnumType.STRING)
	private CumiCategorie cumiCategorie;

	@Column
	@Enumerated(EnumType.STRING)
	private CumiRatio cumiRatio;

	public String getAchternaam()
	{
		return achternaam;
	}

	public void setAchternaam(String achternaam)
	{
		this.achternaam = achternaam;
	}

	public String getVoorvoegsel()
	{
		return voorvoegsel;
	}

	public void setVoorvoegsel(String voorvoegsel)
	{
		this.voorvoegsel = voorvoegsel;
	}

	public String getAlleVoornamen()
	{
		return alleVoornamen;
	}

	public void setAlleVoornamen(String alleVoornamen)
	{
		this.alleVoornamen = alleVoornamen;
	}

	public String getStraatNaam()
	{
		return straatNaam;
	}

	public void setStraatNaam(String straatNaam)
	{
		this.straatNaam = straatNaam;
	}

	public Integer getHuisNummer()
	{
		return huisNummer;
	}

	public void setHuisNummer(Integer huisNummer)
	{
		this.huisNummer = huisNummer;
	}

	public String getHuisnummerToevoeging()
	{
		return huisnummerToevoeging;
	}

	public void setHuisnummerToevoeging(String huisnummerToevoeging)
	{
		this.huisnummerToevoeging = huisnummerToevoeging;
	}

	public String getLocatieOmschrijving()
	{
		return locatieOmschrijving;
	}

	public void setLocatieOmschrijving(String locatieOmschrijving)
	{
		this.locatieOmschrijving = locatieOmschrijving;
	}

	public HuisnummerAanduiding getHuisnummerAanduiding()
	{
		return huisnummerAanduiding;
	}

	public void setHuisnummerAanduiding(HuisnummerAanduiding huisnummerAanduiding)
	{
		this.huisnummerAanduiding = huisnummerAanduiding;
	}

	public String getPlaatsnaam()
	{
		return plaatsnaam;
	}

	public void setPlaatsnaam(String plaatsnaam)
	{
		this.plaatsnaam = plaatsnaam;
	}

	public String getCodeLandAdres()
	{
		return codeLandAdres;
	}

	public void setCodeLandAdres(String codeLandAdres)
	{
		this.codeLandAdres = codeLandAdres;
	}

	public Date getDatumOverlijden()
	{
		return datumOverlijden;
	}

	public void setDatumOverlijden(Date datumOverlijden)
	{
		this.datumOverlijden = datumOverlijden;
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

	public String getCodeGeboortelandOuder2()
	{
		return codeGeboortelandOuder2;
	}

	public void setCodeGeboortelandOuder2(String codeGeboortelandOuder2)
	{
		this.codeGeboortelandOuder2 = codeGeboortelandOuder2;
	}

	public Datum getDatumVestigingInNederland()
	{
		return datumVestigingInNederland;
	}

	public void setDatumVestigingInNederland(Datum datumVestigingInNederland)
	{
		this.datumVestigingInNederland = datumVestigingInNederland;
	}

	public Datum getDatumVertrekUitNederland()
	{
		return datumVertrekUitNederland;
	}

	public void setDatumVertrekUitNederland(Datum datumVertrekUitNederland)
	{
		this.datumVertrekUitNederland = datumVertrekUitNederland;
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

	public Date getDatumIngangVerblijfstitel()
	{
		return datumIngangVerblijfstitel;
	}

	public void setDatumIngangVerblijfstitel(Date datumIngangVerblijfstitel)
	{
		this.datumIngangVerblijfstitel = datumIngangVerblijfstitel;
	}

	public Date getDatumEindeVerblijfstitel()
	{
		return datumEindeVerblijfstitel;
	}

	public void setDatumEindeVerblijfstitel(Date datumEindeVerblijfstitel)
	{
		this.datumEindeVerblijfstitel = datumEindeVerblijfstitel;
	}

	public Date getIngangsDatum()
	{
		return ingangsDatum;
	}

	public void setIngangsDatum(Date ingangsDatum)
	{
		this.ingangsDatum = ingangsDatum;
	}

	public Datum getEindDatum()
	{
		return eindDatum;
	}

	public void setEindDatum(Datum eindDatum)
	{
		this.eindDatum = eindDatum;
	}

	public Integer getJarenPraktijkOnderwijs()
	{
		return jarenPraktijkOnderwijs;
	}

	public void setJarenPraktijkOnderwijs(Integer jarenPraktijkOnderwijs)
	{
		this.jarenPraktijkOnderwijs = jarenPraktijkOnderwijs;
	}

	public Integer getLeerjaar()
	{
		return leerjaar;
	}

	public void setLeerjaar(Integer leerjaar)
	{
		this.leerjaar = leerjaar;
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

	public String getCodeNationaliteit1()
	{
		return codeNationaliteit1;
	}

	public void setCodeNationaliteit1(String codeNationaliteit1)
	{
		this.codeNationaliteit1 = codeNationaliteit1;
	}

	public Datum getDatumIngangNationaliteit1()
	{
		return datumIngangNationaliteit1;
	}

	public void setDatumIngangNationaliteit1(Datum datumIngangNationaliteit1)
	{
		this.datumIngangNationaliteit1 = datumIngangNationaliteit1;
	}

	public String getCodeNationaliteit2()
	{
		return codeNationaliteit2;
	}

	public void setCodeNationaliteit2(String codeNationaliteit2)
	{
		this.codeNationaliteit2 = codeNationaliteit2;
	}

	public Datum getDatumIngangNationaliteit2()
	{
		return datumIngangNationaliteit2;
	}

	public void setDatumIngangNationaliteit2(Datum datumIngangNationaliteit2)
	{
		this.datumIngangNationaliteit2 = datumIngangNationaliteit2;
	}

	public String getPostcodeVolgensSchool()
	{
		return postcodeVolgensSchool;
	}

	public void setPostcodeVolgensSchool(String postcodeVolgensSchool)
	{
		this.postcodeVolgensSchool = postcodeVolgensSchool;
	}

	public String getLandcodeVolgensSchool()
	{
		return landcodeVolgensSchool;
	}

	public void setLandcodeVolgensSchool(String landcodeVolgensSchool)
	{
		this.landcodeVolgensSchool = landcodeVolgensSchool;
	}

	public String getDatumIngangPostcodeLandcodeVolgensSchool()
	{
		return datumIngangPostcodeLandcodeVolgensSchool;
	}

	public void setDatumIngangPostcodeLandcodeVolgensSchool(
			String datumIngangPostcodeLandcodeVolgensSchool)
	{
		this.datumIngangPostcodeLandcodeVolgensSchool = datumIngangPostcodeLandcodeVolgensSchool;
	}

	public String getAdresregelBuitenland1()
	{
		return adresregelBuitenland1;
	}

	public void setAdresregelBuitenland1(String adresregelBuitenland1)
	{
		this.adresregelBuitenland1 = adresregelBuitenland1;
	}

	public String getAdresregelBuitenland2()
	{
		return adresregelBuitenland2;
	}

	public void setAdresregelBuitenland2(String adresregelBuitenland2)
	{
		this.adresregelBuitenland2 = adresregelBuitenland2;
	}

	public String getAdresregelBuitenland3()
	{
		return adresregelBuitenland3;
	}

	public void setAdresregelBuitenland3(String adresregelBuitenland3)
	{
		this.adresregelBuitenland3 = adresregelBuitenland3;
	}

	public void setDatumIngangAdresWijziging(Date datumIngangAdresWijziging)
	{
		this.datumIngangAdresWijziging = datumIngangAdresWijziging;
	}

	public Date getDatumIngangAdresWijziging()
	{
		return datumIngangAdresWijziging;
	}

}
