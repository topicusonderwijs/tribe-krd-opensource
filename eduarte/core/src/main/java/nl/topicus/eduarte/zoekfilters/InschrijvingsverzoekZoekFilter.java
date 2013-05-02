package nl.topicus.eduarte.zoekfilters;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.adres.TypeContactgegeven;
import nl.topicus.eduarte.entities.hogeronderwijs.EersteJaars;
import nl.topicus.eduarte.entities.hogeronderwijs.Inschrijvingsverzoek;
import nl.topicus.eduarte.entities.hogeronderwijs.InschrijvingsverzoekStatus;
import nl.topicus.eduarte.entities.hogeronderwijs.LotingStatus;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.landelijk.Provincie;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekFilter;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekFilter.TypeAdres;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class InschrijvingsverzoekZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<Inschrijvingsverzoek>
{
	private static final long serialVersionUID = 1L;

	private InschrijvingsverzoekStatus status;

	private VerbintenisZoekFilter verbintenisZoekFilter;

	private int studiejaar;

	private String instroommomentCode;

	private IModel<Deelnemer> deelnemer;

	private DeelnemerZoekFilter deelnemerZoekFilter;

	private IModel<LotingStatus> lotingStatus;

	private IModel<Date> lotingStatusDatum;

	private IModel<EersteJaars> eersteJaars;

	public InschrijvingsverzoekZoekFilter()
	{
		setDeelnemerZoekFilter(new DeelnemerZoekFilter());
		setVerbintenisZoekFilter(new VerbintenisZoekFilter());
	}

	public Opleiding getOpleiding()
	{
		return verbintenisZoekFilter.getOpleiding();
	}

	public void setOpleiding(Opleiding opleiding)
	{
		verbintenisZoekFilter.setOpleiding(opleiding);
	}

	public InschrijvingsverzoekStatus getInschrijvingsverzoekStatus()
	{
		return status;
	}

	public void setInschrijvingsverzoekStatus(InschrijvingsverzoekStatus status)
	{
		this.status = status;
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenisZoekFilter.getVerbintenis();
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		verbintenisZoekFilter.setVerbintenis(verbintenis);
	}

	public int getStudiejaar()
	{
		return studiejaar;
	}

	public void setStudiejaar(int studiejaar)
	{
		this.studiejaar = studiejaar;
	}

	public String getInstroommomentCode()
	{
		return instroommomentCode;
	}

	public void setInstroommomentCode(String instroommomentCode)
	{
		this.instroommomentCode = instroommomentCode;
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Integer getDeelnemernummerVanaf()
	{
		return deelnemerZoekFilter.getDeelnemernummerVanaf();
	}

	public void setDeelnemernummerVanaf(Integer deelnemernummerVanaf)
	{
		deelnemerZoekFilter.setDeelnemernummerVanaf(deelnemernummerVanaf);
	}

	public String getAchternaam()
	{
		return deelnemerZoekFilter.getAchternaam();
	}

	public void setAchternaam(String achternaam)
	{
		deelnemerZoekFilter.setAchternaam(achternaam);
	}

	public String getAanspreeknaam()
	{
		return deelnemerZoekFilter.getAanspreeknaam();
	}

	public void setAanspreeknaam(String aanspreeknaam)
	{
		deelnemerZoekFilter.setAanspreeknaam(aanspreeknaam);
	}

	public String getOfficieelofaanspreek()
	{
		return deelnemerZoekFilter.getOfficieelofaanspreek();
	}

	public void setOfficieelofaanspreek(String officieelofaanspreek)
	{
		deelnemerZoekFilter.setOfficieelofaanspreek(officieelofaanspreek);
	}

	public Integer getDeelnemernummer()
	{
		return deelnemerZoekFilter.getDeelnemernummer();
	}

	public void setDeelnemernummer(Integer deelnemernummer)
	{
		deelnemerZoekFilter.setDeelnemernummer(deelnemernummer);
	}

	public Integer getDeelnemernummerTotEnMet()
	{
		return deelnemerZoekFilter.getDeelnemernummerTotEnMet();
	}

	public void setDeelnemernummerTotEnMet(Integer deelnemernummerTotEnMet)
	{
		deelnemerZoekFilter.setDeelnemernummerTotEnMet(deelnemernummerTotEnMet);
	}

	public Date getGeboortedatumTotEnMet()
	{
		return deelnemerZoekFilter.getGeboortedatumTotEnMet();
	}

	public void setGeboortedatumTotEnMet(Date geboortedatumTotEnMet)
	{
		deelnemerZoekFilter.setGeboortedatumTotEnMet(geboortedatumTotEnMet);
	}

	public Date getGeboortedatumVanaf()
	{
		return deelnemerZoekFilter.getGeboortedatumVanaf();
	}

	public void setGeboortedatumVanaf(Date geboortedatumVanaf)
	{
		deelnemerZoekFilter.setGeboortedatumVanaf(geboortedatumVanaf);
	}

	public Date getGeboortedatum()
	{
		return deelnemerZoekFilter.getGeboortedatum();
	}

	public void setGeboortedatum(Date geboortedatum)
	{
		deelnemerZoekFilter.setGeboortedatum(geboortedatum);
	}

	public Gemeente getGeboorteGemeente()
	{
		return deelnemerZoekFilter.getGeboorteGemeente();
	}

	public void setGeboorteGemeente(Gemeente gemeente)
	{
		deelnemerZoekFilter.setGeboorteGemeente(gemeente);
	}

	public Land getGeboorteland()
	{
		return deelnemerZoekFilter.getGeboorteland();
	}

	public void setGeboorteland(Land land)
	{
		deelnemerZoekFilter.setGeboorteland(land);
	}

	public Geslacht getGeslacht()
	{
		return deelnemerZoekFilter.getGeslacht();
	}

	public void setGeslacht(Geslacht geslacht)
	{
		deelnemerZoekFilter.setGeslacht(geslacht);
	}

	@AutoForm(editorClass = TextField.class, label = "BSN")
	public Long getBsn()
	{
		return deelnemerZoekFilter.getBsn();
	}

	public void setBsn(Long bsn)
	{
		deelnemerZoekFilter.setBsn(bsn);
	}

	public Boolean getBsnIsLeeg()
	{
		return deelnemerZoekFilter.getBsnIsLeeg();
	}

	public void setBsnIsLeeg(Boolean bsnIsLeeg)
	{
		deelnemerZoekFilter.setBsnIsLeeg(bsnIsLeeg);
	}

	public Boolean getRoepnaamIsLeeg()
	{
		return deelnemerZoekFilter.getRoepnaamIsLeeg();
	}

	public void setRoepnaamIsLeeg(Boolean roepnaamIsLeeg)
	{
		deelnemerZoekFilter.setRoepnaamIsLeeg(roepnaamIsLeeg);
	}

	public String getRoepnaam()
	{
		return deelnemerZoekFilter.getRoepnaam();
	}

	public void setRoepnaam(String roepnaam)
	{
		deelnemerZoekFilter.setRoepnaam(roepnaam);
	}

	public String getVoorletters()
	{
		return deelnemerZoekFilter.getVoorletters();
	}

	public void setVoorletters(String voorletters)
	{
		deelnemerZoekFilter.setVoorletters(voorletters);
	}

	public Boolean getVoorlettersIsLeeg()
	{
		return deelnemerZoekFilter.getVoorlettersIsLeeg();
	}

	public void setVoorlettersIsLeeg(Boolean voorlettersIsLeeg)
	{
		deelnemerZoekFilter.setVoorlettersIsLeeg(voorlettersIsLeeg);
	}

	public String getVoornamen()
	{
		return deelnemerZoekFilter.getVoornamen();
	}

	public void setVoornamen(String voornamen)
	{
		deelnemerZoekFilter.setVoornamen(voornamen);
	}

	public Boolean getVoornamenIsLeeg()
	{
		return deelnemerZoekFilter.getVoornamenIsLeeg();
	}

	public void setVoornamenIsLeeg(Boolean voornamenIsLeeg)
	{
		deelnemerZoekFilter.setVoornamenIsLeeg(voornamenIsLeeg);
	}

	public String getVoorvoegsel()
	{
		return deelnemerZoekFilter.getVoorvoegsel();
	}

	public void setVoorvoegsel(String voorvoegsel)
	{
		deelnemerZoekFilter.setVoorvoegsel(voorvoegsel);
	}

	public String getOfficieleVoorvoegsel()
	{
		return deelnemerZoekFilter.getOfficieleVoorvoegsel();
	}

	public void setOfficieleVoorvoegsel(String officieleVoorvoegsel)
	{
		deelnemerZoekFilter.setOfficieleVoorvoegsel(officieleVoorvoegsel);
	}

	public Date getRegistratieDatumVanaf()
	{
		return deelnemerZoekFilter.getRegistratieDatumVanaf();
	}

	public void setRegistratieDatumVanaf(Date registratieDatumVanaf)
	{
		deelnemerZoekFilter.setRegistratieDatumVanaf(registratieDatumVanaf);
	}

	public Boolean getGeboortedatumIsLeeg()
	{
		return deelnemerZoekFilter.getGeboortedatumIsLeeg();
	}

	public void setGeboortedatumIsLeeg(Boolean geboortedatumIsLeeg)
	{
		deelnemerZoekFilter.setGeboortedatumIsLeeg(geboortedatumIsLeeg);
	}

	public Date getRegistratieDatumTotEnMet()
	{
		return deelnemerZoekFilter.getRegistratieDatumTotEnMet();
	}

	public void setRegistratieDatumTotEnMet(Date registratieDatumTotEnMet)
	{
		deelnemerZoekFilter.setRegistratieDatumTotEnMet(registratieDatumTotEnMet);
	}

	public Date getRegistratieDatum()
	{
		return deelnemerZoekFilter.getRegistratieDatum();
	}

	public void setRegistratieDatum(Date registratieDatum)
	{
		deelnemerZoekFilter.setRegistratieDatum(registratieDatum);
	}

	public Boolean getRegistratieDatumIsLeeg()
	{
		return deelnemerZoekFilter.getRegistratieDatumIsLeeg();
	}

	public void setRegistratieDatumIsLeeg(Boolean registratieDatumIsLeeg)
	{
		deelnemerZoekFilter.setRegistratieDatumIsLeeg(registratieDatumIsLeeg);
	}

	public Boolean getDatumInNLIsLeeg()
	{
		return deelnemerZoekFilter.getDatumInNLIsLeeg();
	}

	public void setDatumInNLIsLeeg(Boolean datumInNLIsLeeg)
	{
		deelnemerZoekFilter.setDatumInNLIsLeeg(datumInNLIsLeeg);
	}

	public Date getDatumInNLTotEnMet()
	{
		return deelnemerZoekFilter.getDatumInNLTotEnMet();
	}

	public void setDatumInNLTotEnMet(Date datumInNLTotEnMet)
	{
		deelnemerZoekFilter.setDatumInNLTotEnMet(datumInNLTotEnMet);
	}

	public Date getDatumInNLVanaf()
	{
		return deelnemerZoekFilter.getDatumInNLVanaf();
	}

	public void setDatumInNLVanaf(Date datumInNLVanaf)
	{
		deelnemerZoekFilter.setDatumInNLVanaf(datumInNLVanaf);
	}

	public Boolean getGeboortelandIsLeeg()
	{
		return deelnemerZoekFilter.getGeboortelandIsLeeg();
	}

	public void setGeboortelandIsLeeg(Boolean geboortelandIsLeeg)
	{
		deelnemerZoekFilter.setGeboortelandIsLeeg(geboortelandIsLeeg);
	}

	public Boolean getGeboortelandOngelijkAanNL()
	{
		return deelnemerZoekFilter.getGeboortelandOngelijkAanNL();
	}

	public void setGeboortelandOngelijkAanNL(Boolean geboortelandOngelijkAanNL)
	{
		deelnemerZoekFilter.setGeboortelandOngelijkAanNL(geboortelandOngelijkAanNL);
	}

	public List<Land> getGeboortelandList()
	{
		return deelnemerZoekFilter.getGeboortelandList();
	}

	public void setGeboortelandList(List<Land> geboortelandList)
	{
		deelnemerZoekFilter.setGeboortelandList(geboortelandList);
	}

	public List<Nationaliteit> getNationaliteitList()
	{
		return deelnemerZoekFilter.getNationaliteitList();
	}

	public void setNationaliteitList(List<Nationaliteit> nationaliteitList)
	{
		deelnemerZoekFilter.setNationaliteitList(nationaliteitList);
	}

	public Nationaliteit getNationaliteit()
	{
		return deelnemerZoekFilter.getNationaliteit();
	}

	public void setNationaliteit(Nationaliteit nationaliteit)
	{
		deelnemerZoekFilter.setNationaliteit(nationaliteit);
	}

	public Boolean getNationaliteitIsLeeg()
	{
		return deelnemerZoekFilter.getNationaliteitIsLeeg();
	}

	public void setNationaliteitIsLeeg(Boolean nationaliteitIsLeeg)
	{
		deelnemerZoekFilter.setNationaliteitIsLeeg(nationaliteitIsLeeg);
	}

	public Boolean getNationaliteitOngelijkAanNL()
	{
		return deelnemerZoekFilter.getNationaliteitOngelijkAanNL();
	}

	public void setNationaliteitOngelijkAanNL(Boolean nationaliteitOngelijkAanNL)
	{
		deelnemerZoekFilter.setNationaliteitOngelijkAanNL(nationaliteitOngelijkAanNL);
	}

	public Boolean getDubbeleNationaliteit()
	{
		return deelnemerZoekFilter.getDubbeleNationaliteit();
	}

	public void setDubbeleNationaliteit(Boolean dubbeleNationaliteit)
	{
		deelnemerZoekFilter.setDubbeleNationaliteit(dubbeleNationaliteit);
	}

	public Date getDatumInNL()
	{
		return deelnemerZoekFilter.getDatumInNL();
	}

	public void setDatumInNL(Date datumInNL)
	{
		deelnemerZoekFilter.setDatumInNL(datumInNL);
	}

	public Boolean getAllochtoon()
	{
		return deelnemerZoekFilter.getAllochtoon();
	}

	public void setAllochtoon(Boolean allochtoon)
	{
		deelnemerZoekFilter.setAllochtoon(allochtoon);
	}

	public Boolean getLgf()
	{
		return deelnemerZoekFilter.getLgf();
	}

	public void setLgf(Boolean lgf)
	{
		deelnemerZoekFilter.setLgf(lgf);
	}

	public Boolean getNieuwkomer()
	{
		return deelnemerZoekFilter.getNieuwkomer();
	}

	public void setNieuwkomer(Boolean nieuwkomer)
	{
		deelnemerZoekFilter.setNieuwkomer(nieuwkomer);
	}

	public Boolean isMeerderjarig()
	{
		return deelnemerZoekFilter.isMeerderjarig();
	}

	public void setMeerderjarig(Boolean meerderjarig)
	{
		deelnemerZoekFilter.setMeerderjarig(meerderjarig);
	}

	public Boolean isVertegenwoordiger()
	{
		return deelnemerZoekFilter.isVertegenwoordiger();
	}

	public void setVertegenwoordiger(Boolean vertegenwoordiger)
	{
		deelnemerZoekFilter.setVertegenwoordiger(vertegenwoordiger);
	}

	public Boolean isBetalingsplichtige()
	{
		return deelnemerZoekFilter.isBetalingsplichtige();
	}

	public void setBetalingsplichtige(Boolean betalingsplichtige)
	{
		deelnemerZoekFilter.setBetalingsplichtige(betalingsplichtige);
	}

	public Boolean getOverleden()
	{
		return deelnemerZoekFilter.getOverleden();
	}

	public void setOverleden(Boolean overleden)
	{
		deelnemerZoekFilter.setOverleden(overleden);
	}

	public TypeContactgegeven getTypeContactgegeven()
	{
		return deelnemerZoekFilter.getTypeContactgegeven();
	}

	public void setTypeContactgegeven(TypeContactgegeven typeContactgegeven)
	{
		deelnemerZoekFilter.setTypeContactgegeven(typeContactgegeven);
	}

	public String getContactgegeven()
	{
		return deelnemerZoekFilter.getContactgegeven();
	}

	public void setContactgegeven(String contactgegeven)
	{
		deelnemerZoekFilter.setContactgegeven(contactgegeven);
	}

	public Boolean getGeheimAdres()
	{
		return deelnemerZoekFilter.getGeheimAdres();
	}

	public void setGeheimAdres(Boolean geheimAdres)
	{
		deelnemerZoekFilter.setGeheimAdres(geheimAdres);
	}

	public String getHuisnummerToevoeging()
	{
		return deelnemerZoekFilter.getHuisnummerToevoeging();
	}

	public void setHuisnummerToevoeging(String huisnummerToevoeging)
	{
		deelnemerZoekFilter.setHuisnummerToevoeging(huisnummerToevoeging);
	}

	public TypeAdres getTypeAdres()
	{
		return deelnemerZoekFilter.getTypeAdres();
	}

	public void setTypeAdres(TypeAdres typeAdres)
	{
		deelnemerZoekFilter.setTypeAdres(typeAdres);
	}

	public String getPostcode()
	{
		return deelnemerZoekFilter.getPostcode();
	}

	public void setPostcode(String postcode)
	{
		deelnemerZoekFilter.setPostcode(postcode);
	}

	public String getPostcodeTotEnMet()
	{
		return deelnemerZoekFilter.getPostcodeTotEnMet();
	}

	public void setPostcodeTotEnMet(String postcodeTotEnMet)
	{
		deelnemerZoekFilter.setPostcodeTotEnMet(postcodeTotEnMet);
	}

	public String getPostcodeVanaf()
	{
		return deelnemerZoekFilter.getPostcodeVanaf();
	}

	public void setPostcodeVanaf(String postcodeVanaf)
	{
		deelnemerZoekFilter.setPostcodeVanaf(postcodeVanaf);
	}

	public Provincie getProvincie()
	{
		return deelnemerZoekFilter.getProvincie();
	}

	public void setProvincie(Provincie provincie)
	{
		deelnemerZoekFilter.setProvincie(provincie);
	}

	public Provincie getProvincieOngelijkAan()
	{
		return deelnemerZoekFilter.getProvincieOngelijkAan();
	}

	public void setProvincieOngelijkAan(Provincie provincieOngelijkAan)
	{
		deelnemerZoekFilter.setProvincieOngelijkAan(provincieOngelijkAan);
	}

	public String getStraat()
	{
		return deelnemerZoekFilter.getStraat();
	}

	public void setStraat(String straat)
	{
		deelnemerZoekFilter.setStraat(straat);
	}

	public String getHuisnummer()
	{
		return deelnemerZoekFilter.getHuisnummer();
	}

	public void setHuisnummer(String huisnummer)
	{
		deelnemerZoekFilter.setHuisnummer(huisnummer);
	}

	public Gemeente getGemeente()
	{
		return deelnemerZoekFilter.getGemeente();
	}

	public void setGemeente(Gemeente gemeente)
	{
		deelnemerZoekFilter.setGemeente(gemeente);
	}

	public Gemeente getGemeenteOngelijkAan()
	{
		return deelnemerZoekFilter.getGemeenteOngelijkAan();
	}

	public void setGemeenteOngelijkAan(Gemeente gemeenteOngelijkAan)
	{
		deelnemerZoekFilter.setGemeenteOngelijkAan(gemeenteOngelijkAan);
	}

	public Land getLand()
	{
		return deelnemerZoekFilter.getLand();
	}

	public void setLand(Land land)
	{
		deelnemerZoekFilter.setLand(land);
	}

	public Land getLandOngelijkAan()
	{
		return deelnemerZoekFilter.getLandOngelijkAan();
	}

	public void setLandOngelijkAan(Land landOngelijkAan)
	{
		deelnemerZoekFilter.setLandOngelijkAan(landOngelijkAan);
	}

	public String getPlaats()
	{
		return deelnemerZoekFilter.getPlaats();
	}

	public void setPlaats(String plaats)
	{
		deelnemerZoekFilter.setPlaats(plaats);
	}

	public String getPlaatsOngelijkAan()
	{
		return deelnemerZoekFilter.getPlaatsOngelijkAan();
	}

	public void setPlaatsOngelijkAan(String plaatsOngelijkAan)
	{
		deelnemerZoekFilter.setPlaatsOngelijkAan(plaatsOngelijkAan);
	}

	public LotingStatus getLotingStatus()
	{
		return getModelObject(lotingStatus);
	}

	public void setLotingStatus(LotingStatus lotingStatus)
	{
		this.lotingStatus = makeModelFor(lotingStatus);
	}

	public Date getLotingStatusDatum()
	{
		return getModelObject(lotingStatusDatum);
	}

	public void setLotingStatusDatum(Date lotingStatusDatum)
	{
		this.lotingStatusDatum = makeModelFor(lotingStatusDatum);
	}

	public EersteJaars getEersteJaars()
	{
		return getModelObject(eersteJaars);
	}

	public void setEersteJaars(EersteJaars eersteJaars)
	{
		this.eersteJaars = makeModelFor(eersteJaars);
	}

	public DeelnemerZoekFilter getDeelnemerZoekFilter()
	{
		return this.deelnemerZoekFilter;
	}

	public void setDeelnemerZoekFilter(DeelnemerZoekFilter deelnemerFilter)
	{
		this.deelnemerZoekFilter = deelnemerFilter;
	}

	private void setVerbintenisZoekFilter(VerbintenisZoekFilter verbintenisZoekFilter)
	{
		this.verbintenisZoekFilter = verbintenisZoekFilter;
	}

	public VerbintenisZoekFilter getVerbintenisZoekFilter()
	{
		return verbintenisZoekFilter;
	}

}
