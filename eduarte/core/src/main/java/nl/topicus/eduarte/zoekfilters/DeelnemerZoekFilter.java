/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.BronEntiteitStatus;
import nl.topicus.eduarte.entities.adres.TypeContactgegeven;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.entities.inschrijving.Schooladvies;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.kenmerk.Kenmerk;
import nl.topicus.eduarte.entities.kenmerk.KenmerkCategorie;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.landelijk.Provincie;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.vrijevelden.PersoonVrijVeld;

import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public class DeelnemerZoekFilter extends AbstractVrijVeldableZoekFilter<PersoonVrijVeld, Deelnemer>
		implements IMentorDocentZoekFilter<Deelnemer>,
		IVerantwoordelijkeUitvoerendeZoekFilter<Deelnemer>
{
	private static final long serialVersionUID = 1L;

	public static enum TypeAdres
	{
		Woonadres,
		Postadres,
		Beide;
	}

	private Integer deelnemernummer;

	private Integer deelnemernummerVanaf;

	private Integer deelnemernummerTotEnMet;

	private String roepnaam;

	private Boolean roepnaamIsLeeg;

	private String voornamen;

	private Boolean voornamenIsLeeg;

	private String voorletters;

	private Boolean voorlettersIsLeeg;

	private String achternaam;

	private String aanspreeknaam;

	private String voorvoegsel;

	private String officieleVoorvoegsel;

	private String volledigeNaam;

	private String officieelofaanspreek;

	private Boolean officieleNaamWijktAf;

	private Long bsn;

	private Boolean bsnIsLeeg;

	private Long onderwijsnummer;

	private Boolean onderwijsnummerIsLeeg;

	private Long debiteurennummer;

	private Geslacht geslacht;

	private Date geboortedatumVanaf;

	private Date geboortedatumTotEnMet;

	private Date geboortedatum;

	private Boolean geboortedatumIsLeeg;

	private Date registratieDatumVanaf;

	private Date registratieDatumTotEnMet;

	private Date registratieDatum;

	private Boolean registratieDatumIsLeeg;

	private Date datumInNL;

	private Date datumInNLVanaf;

	private Date datumInNLTotEnMet;

	private Boolean datumInNLIsLeeg;

	private IModel<Gemeente> geboorteGemeente;

	private IModel<Land> geboorteland;

	private Boolean geboortelandOngelijkAanNL;

	private Boolean geboortelandIsLeeg;

	private IModel<List<Land>> geboortelandList;

	private IModel<Nationaliteit> nationaliteit;

	private IModel<List<Nationaliteit>> nationaliteitList;

	private Boolean nationaliteitOngelijkAanNL;

	private Boolean nationaliteitIsLeeg;

	private Boolean dubbeleNationaliteit;

	private Boolean overleden;

	private IModel<Groepstype> groepstype;

	private IModel<Groep> groep;

	private Boolean groepsdeelnameBeeindigd;

	private IModel<List<Groep>> groepList;

	private IModel<Gemeente> gemeente;

	private IModel<Gemeente> gemeenteOngelijkAan;

	private TypeAdres typeAdres;

	private Boolean geheimAdres;

	private String huisnummer;

	private String huisnummerToevoeging;

	private IModel<Land> land;

	private IModel<Land> landOngelijkAan;

	private String plaats;

	private String plaatsOngelijkAan;

	private String postcode;

	private String postcodeVanaf;

	private String postcodeTotEnMet;

	private IModel<Provincie> provincie;

	private IModel<Provincie> provincieOngelijkAan;

	private String straat;

	private String snelZoekenString;

	private boolean exactSnelZoeken;

	private Boolean lgf;

	private Boolean allochtoon;

	@AutoForm(label = "Inburgeraar")
	private Boolean nieuwkomer;

	private Boolean meerderjarig;

	private Boolean vertegenwoordiger;

	private Boolean betalingsplichtige;

	private boolean mentorOfDocentRequired;

	private IModel<Medewerker> docent;

	private IModel<Medewerker> mentor;

	private IModel<Medewerker> mentorOfDocent;

	private IModel<Medewerker> verantwoordelijke;

	private IModel<Medewerker> uitvoerende;

	private IModel<Medewerker> verantwoordelijkeOfUitvoerende;

	private BronEntiteitStatus bronStatus;

	private Date peilEindDatum;

	private Boolean heeftMeerdereVerbintenissen;

	private IModel<List<Long>> deelnemerIdsIn;

	private IModel<KenmerkCategorie> kenmerkCategorie;

	private IModel<Kenmerk> kenmerk;

	private IModel<Brin> brinVooropleiding;

	private IModel<ExterneOrganisatie> vooropleidingExterneOrganisatie;

	private IModel<List<Brin>> brinVooropleidingList;

	private String naamOnderwijsinstellingVooropleiding;

	private SoortOnderwijs categorieVooropleiding;

	private IModel<SoortVooropleiding> soortVooropleiding;

	private Date vooropleidingBegindatumVanaf;

	private Date vooropleidingBegindatumTotEnMet;

	private Date vooropleidingEinddatumVanaf;

	private Date vooropleidingEinddatumTotEnMet;

	private Integer vooropleidingAantalJarenOnderwijsVanaf;

	private Integer vooropleidingAantalJarenOnderwijsTotEnMet;

	private Boolean vooropleidingAantalJarenOnderwijsIsLeeg;

	private Boolean vooropleidingDiplomaBehaald;

	private IModel<Schooladvies> vooropleidingSchooladvies;

	private Integer vooropleidingCitoscore;

	private String contactgegeven;

	private TypeContactgegeven typeContactgegeven;

	private IModel<List<DocumentCategorie>> documentCategorieList;

	private IModel<List<DocumentType>> documentTypeList;

	private Boolean uitsluitenVanFacturatie;

	private Boolean heeftAccount;

	private Date datumLaatsteKenmerkWijziging;

	private IModel<List<Kenmerk>> kenmerkenLijst;

	private Long ocwnummer;

	private Integer studielinknummer;

	private String eersteVoorletter;

	private Date startkwalificatieplichtigOpDatum;

	public DeelnemerZoekFilter()
	{
		super(PersoonVrijVeld.class);
	}

	public String getRoepnaam()
	{
		return roepnaam;
	}

	public void setRoepnaam(String roepnaam)
	{
		this.roepnaam = roepnaam;
	}

	public String getAchternaam()
	{
		return achternaam;
	}

	public void setAchternaam(String achternaam)
	{
		this.achternaam = achternaam;
	}

	public String getAanspreeknaam()
	{
		return aanspreeknaam;
	}

	public void setAanspreeknaam(String aanspreeknaam)
	{
		this.aanspreeknaam = aanspreeknaam;
	}

	public String getOfficieelofaanspreek()
	{
		return officieelofaanspreek;
	}

	public void setOfficieelofaanspreek(String officieelofaanspreek)
	{
		this.officieelofaanspreek = officieelofaanspreek;
	}

	public Long getOnderwijsnummer()
	{
		return onderwijsnummer;
	}

	public void setOnderwijsnummer(Long onderwijsnummer)
	{
		this.onderwijsnummer = onderwijsnummer;
	}

	public Geslacht getGeslacht()
	{
		return geslacht;
	}

	public void setGeslacht(Geslacht geslacht)
	{
		this.geslacht = geslacht;
	}

	public Date getGeboortedatumVanaf()
	{
		return geboortedatumVanaf;
	}

	public void setGeboortedatumVanaf(Date geboortedatumVanaf)
	{
		this.geboortedatumVanaf = geboortedatumVanaf;
	}

	public Date getGeboortedatumTotEnMet()
	{
		return geboortedatumTotEnMet;
	}

	public void setGeboortedatumTotEnMet(Date geboortedatumTotEnMet)
	{
		this.geboortedatumTotEnMet = geboortedatumTotEnMet;
	}

	public Gemeente getGeboorteGemeente()
	{
		return getModelObject(geboorteGemeente);
	}

	public void setGeboorteGemeente(Gemeente geboorteGemeente)
	{
		this.geboorteGemeente = makeModelFor(geboorteGemeente);
	}

	public Land getGeboorteland()
	{
		return getModelObject(geboorteland);
	}

	public void setGeboorteland(Land geboorteland)
	{
		this.geboorteland = makeModelFor(geboorteland);
	}

	public Long getDebiteurennummer()
	{
		return debiteurennummer;
	}

	public void setDebiteurennummer(Long debiteurennummer)
	{
		this.debiteurennummer = debiteurennummer;
	}

	public Date getDatumInNL()
	{
		return datumInNL;
	}

	public void setDatumInNL(Date datumInNL)
	{
		this.datumInNL = datumInNL;
	}

	public void setGeboorteGemeente(IModel<Gemeente> geboorteGemeente)
	{
		this.geboorteGemeente = geboorteGemeente;
	}

	public Groep getGroep()
	{
		return getModelObject(groep);
	}

	public void setGroep(Groep groep)
	{
		this.groep = makeModelFor(groep);
	}

	public String getVolledigeNaam()
	{
		return volledigeNaam;
	}

	public void setVolledigeNaam(String volledigeNaam)
	{
		this.volledigeNaam = volledigeNaam;
	}

	public Gemeente getGemeente()
	{
		return getModelObject(gemeente);
	}

	public void setGemeente(Gemeente gemeente)
	{
		this.gemeente = makeModelFor(gemeente);
	}

	public Gemeente getGemeenteOngelijkAan()
	{
		return getModelObject(gemeenteOngelijkAan);
	}

	public void setGemeenteOngelijkAan(Gemeente gemeenteOngelijkAan)
	{
		this.gemeenteOngelijkAan = makeModelFor(gemeenteOngelijkAan);
	}

	public String getHuisnummer()
	{
		return huisnummer;
	}

	public void setHuisnummer(String huisnummer)
	{
		this.huisnummer = huisnummer;
	}

	public Land getLand()
	{
		return getModelObject(land);
	}

	public void setLand(Land land)
	{
		this.land = makeModelFor(land);
	}

	public Land getLandOngelijkAan()
	{
		return getModelObject(landOngelijkAan);
	}

	public void setLandOngelijkAan(Land landOngelijkAan)
	{
		this.landOngelijkAan = makeModelFor(landOngelijkAan);
	}

	public String getPlaats()
	{
		return plaats;
	}

	public void setPlaats(String plaats)
	{
		this.plaats = plaats;
	}

	public String getPlaatsOngelijkAan()
	{
		return plaatsOngelijkAan;
	}

	public void setPlaatsOngelijkAan(String plaatsOngelijkAan)
	{
		this.plaatsOngelijkAan = plaatsOngelijkAan;
	}

	public String getPostcode()
	{
		return postcode;
	}

	public void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}

	public String getPostcodeVanaf()
	{
		return postcodeVanaf;
	}

	public void setPostcodeVanaf(String postcodeVanaf)
	{
		this.postcodeVanaf = postcodeVanaf;
	}

	public String getPostcodeTotEnMet()
	{
		return postcodeTotEnMet;
	}

	public void setPostcodeTotEnMet(String postcodeTotEnMet)
	{
		this.postcodeTotEnMet = postcodeTotEnMet;
	}

	public Provincie getProvincie()
	{
		return getModelObject(provincie);
	}

	public void setProvincie(Provincie provincie)
	{
		this.provincie = makeModelFor(provincie);
	}

	public Provincie getProvincieOngelijkAan()
	{
		return getModelObject(provincieOngelijkAan);
	}

	public void setProvincieOngelijkAan(Provincie provincieOngelijkAan)
	{
		this.provincieOngelijkAan = makeModelFor(provincieOngelijkAan);
	}

	public String getStraat()
	{
		return straat;
	}

	public void setStraat(String straat)
	{
		this.straat = straat;
	}

	public boolean heeftAdresCriteria()
	{
		return getStraat() != null || getProvincie() != null || getProvincieOngelijkAan() != null
			|| getLand() != null || getLandOngelijkAan() != null || getPlaats() != null
			|| getPlaatsOngelijkAan() != null || getPostcode() != null
			|| getPostcodeVanaf() != null || getPostcodeTotEnMet() != null
			|| getHuisnummer() != null || getGemeente() != null || getGemeenteOngelijkAan() != null
			|| getHuisnummerToevoeging() != null || getTypeAdres() != null
			|| getGeheimAdres() != null;
	}

	public Long getBsn()
	{
		return bsn;
	}

	public void setBsn(Long bsn)
	{
		this.bsn = bsn;
	}

	public Integer getDeelnemernummer()
	{
		return deelnemernummer;
	}

	public void setDeelnemernummer(Integer deelnemernummer)
	{
		this.deelnemernummer = deelnemernummer;
	}

	public Integer getDeelnemernummerVanaf()
	{
		return deelnemernummerVanaf;
	}

	public void setDeelnemernummerVanaf(Integer deelnemernummerVanaf)
	{
		this.deelnemernummerVanaf = deelnemernummerVanaf;
	}

	public Integer getDeelnemernummerTotEnMet()
	{
		return deelnemernummerTotEnMet;
	}

	public void setDeelnemernummerTotEnMet(Integer deelnemernummerTotEnMet)
	{
		this.deelnemernummerTotEnMet = deelnemernummerTotEnMet;
	}

	public Groepstype getGroepstype()
	{
		return getModelObject(groepstype);
	}

	public void setGroepstype(Groepstype groepstype)
	{
		this.groepstype = makeModelFor(groepstype);
	}

	public String getSnelZoekenString()
	{
		return snelZoekenString;
	}

	public void setSnelZoekenString(String snelZoekenString)
	{
		this.snelZoekenString = snelZoekenString;
	}

	public boolean isExactSnelZoeken()
	{
		return exactSnelZoeken;
	}

	public void setExactSnelZoeken(boolean exactSnelZoeken)
	{
		this.exactSnelZoeken = exactSnelZoeken;
	}

	public Date getGeboortedatum()
	{
		return geboortedatum;
	}

	public void setGeboortedatum(Date geboortedatum)
	{
		this.geboortedatum = geboortedatum;
	}

	public Boolean getLgf()
	{
		return lgf;
	}

	public void setLgf(Boolean lgf)
	{
		this.lgf = lgf;
	}

	public Boolean getAllochtoon()
	{
		return allochtoon;
	}

	public void setAllochtoon(Boolean allochtoon)
	{
		this.allochtoon = allochtoon;
	}

	public Boolean getNieuwkomer()
	{
		return nieuwkomer;
	}

	public void setNieuwkomer(Boolean nieuwkomer)
	{
		this.nieuwkomer = nieuwkomer;
	}

	public Boolean isMeerderjarig()
	{
		return meerderjarig;
	}

	public void setMeerderjarig(Boolean meerderjarig)
	{
		this.meerderjarig = meerderjarig;
	}

	public Boolean isVertegenwoordiger()
	{
		return vertegenwoordiger;
	}

	public void setVertegenwoordiger(Boolean vertegenwoordiger)
	{
		this.vertegenwoordiger = vertegenwoordiger;
	}

	public Boolean isBetalingsplichtige()
	{
		return betalingsplichtige;
	}

	public void setBetalingsplichtige(Boolean betalingsplichtige)
	{
		this.betalingsplichtige = betalingsplichtige;
	}

	public boolean isMentorOfDocentRequired()
	{
		return mentorOfDocentRequired;
	}

	public void setMentorOfDocentRequired(boolean mentorOfDocentRequired)
	{
		this.mentorOfDocentRequired = mentorOfDocentRequired;
	}

	/**
	 * Stelt de medewerker in die wordt gebruikt als filter voor deelnemers die deze
	 * medewerker als mentor of docent hebben.
	 * 
	 * @param medewerker
	 */
	@Override
	public void setMentorOfDocent(Medewerker medewerker)
	{
		this.mentorOfDocent = makeModelFor(medewerker);
	}

	@Override
	public Medewerker getMentorOfDocent()
	{
		return getModelObject(mentorOfDocent);
	}

	@Override
	public Medewerker getDocent()
	{
		return getModelObject(docent);
	}

	@Override
	public void setDocent(Medewerker docent)
	{
		this.docent = makeModelFor(docent);
	}

	@Override
	public Medewerker getMentor()
	{
		return getModelObject(mentor);
	}

	@Override
	public void setMentor(Medewerker mentor)
	{
		this.mentor = makeModelFor(mentor);
	}

	@Override
	public Medewerker getVerantwoordelijke()
	{
		return getModelObject(verantwoordelijke);
	}

	@Override
	public void setVerantwoordelijke(Medewerker verantwoordelijke)
	{
		this.verantwoordelijke = makeModelFor(verantwoordelijke);
	}

	@Override
	public Medewerker getUitvoerende()
	{
		return getModelObject(uitvoerende);
	}

	@Override
	public void setUitvoerende(Medewerker uitvoerende)
	{
		this.uitvoerende = makeModelFor(uitvoerende);
	}

	@Override
	public Medewerker getVerantwoordelijkeOfUitvoerende()
	{
		return getModelObject(verantwoordelijkeOfUitvoerende);
	}

	@Override
	public void setVerantwoordelijkeOfUitvoerende(Medewerker verantwoordelijkeOfUitvoerende)
	{
		this.verantwoordelijkeOfUitvoerende = makeModelFor(verantwoordelijkeOfUitvoerende);
	}

	public BronEntiteitStatus getBronStatus()
	{
		return bronStatus;
	}

	public void setBronStatus(BronEntiteitStatus bronStatus)
	{
		this.bronStatus = bronStatus;
	}

	public Date getPeilEindDatum()
	{
		if (peilEindDatum == null)
			return getPeildatum();
		return peilEindDatum;
	}

	public void setPeilEindDatum(Date peilEindDatum)
	{
		this.peilEindDatum = peilEindDatum;
	}

	public void setHeeftMeerdereVerbintenissen(Boolean heeftMeerdereVerbintenissen)
	{
		this.heeftMeerdereVerbintenissen = heeftMeerdereVerbintenissen;
	}

	public Boolean getHeeftMeerdereVerbintenissen()
	{
		return heeftMeerdereVerbintenissen;
	}

	public List<Long> getDeelnemerIdsIn()
	{
		return getModelObject(deelnemerIdsIn);
	}

	public void setDeelnemerIdsIn(List<Long> deelnemerIdsIn)
	{
		this.deelnemerIdsIn = makeModelFor(deelnemerIdsIn);
	}

	public void setDeelnemerIdsInModel(IModel<List<Long>> deelnemerIdsIn)
	{
		this.deelnemerIdsIn = deelnemerIdsIn;
	}

	public KenmerkCategorie getKenmerkCategorie()
	{
		return getModelObject(kenmerkCategorie);
	}

	public void setKenmerkCategorie(KenmerkCategorie kenmerkCategorie)
	{
		this.kenmerkCategorie = makeModelFor(kenmerkCategorie);
	}

	public Kenmerk getKenmerk()
	{
		return getModelObject(kenmerk);
	}

	public void setKenmerk(Kenmerk kenmerk)
	{
		this.kenmerk = makeModelFor(kenmerk);
	}

	public Boolean getRoepnaamIsLeeg()
	{
		return roepnaamIsLeeg;
	}

	public void setRoepnaamIsLeeg(Boolean roepnaamIsLeeg)
	{
		this.roepnaamIsLeeg = roepnaamIsLeeg;
	}

	public String getVoornamen()
	{
		return voornamen;
	}

	public void setVoornamen(String voornamen)
	{
		this.voornamen = voornamen;
	}

	public Boolean getVoornamenIsLeeg()
	{
		return voornamenIsLeeg;
	}

	public void setVoornamenIsLeeg(Boolean voornamenIsLeeg)
	{
		this.voornamenIsLeeg = voornamenIsLeeg;
	}

	public String getVoorletters()
	{
		return voorletters;
	}

	public void setVoorletters(String voorletters)
	{
		this.voorletters = voorletters;
	}

	public Boolean getVoorlettersIsLeeg()
	{
		return voorlettersIsLeeg;
	}

	public void setVoorlettersIsLeeg(Boolean voorlettersIsLeeg)
	{
		this.voorlettersIsLeeg = voorlettersIsLeeg;
	}

	public String getVoorvoegsel()
	{
		return voorvoegsel;
	}

	public void setVoorvoegsel(String voorvoegsel)
	{
		this.voorvoegsel = voorvoegsel;
	}

	public String getOfficieleVoorvoegsel()
	{
		return officieleVoorvoegsel;
	}

	public void setOfficieleVoorvoegsel(String officieleVoorvoegsel)
	{
		this.officieleVoorvoegsel = officieleVoorvoegsel;
	}

	public Boolean getOfficieleNaamWijktAf()
	{
		return officieleNaamWijktAf;
	}

	public void setOfficieleNaamWijktAf(Boolean officieleNaamWijktAf)
	{
		this.officieleNaamWijktAf = officieleNaamWijktAf;
	}

	public Boolean getBsnIsLeeg()
	{
		return bsnIsLeeg;
	}

	public void setBsnIsLeeg(Boolean bsnIsLeeg)
	{
		this.bsnIsLeeg = bsnIsLeeg;
	}

	public Boolean getOnderwijsnummerIsLeeg()
	{
		return onderwijsnummerIsLeeg;
	}

	public void setOnderwijsnummerIsLeeg(Boolean onderwijsnummerIsLeeg)
	{
		this.onderwijsnummerIsLeeg = onderwijsnummerIsLeeg;
	}

	public Boolean getGeboortedatumIsLeeg()
	{
		return geboortedatumIsLeeg;
	}

	public void setGeboortedatumIsLeeg(Boolean geboortedatumIsLeeg)
	{
		this.geboortedatumIsLeeg = geboortedatumIsLeeg;
	}

	public Date getDatumInNLVanaf()
	{
		return datumInNLVanaf;
	}

	public void setDatumInNLVanaf(Date datumInNLVanaf)
	{
		this.datumInNLVanaf = datumInNLVanaf;
	}

	public Date getDatumInNLTotEnMet()
	{
		return datumInNLTotEnMet;
	}

	public void setDatumInNLTotEnMet(Date datumInNLTotEnMet)
	{
		this.datumInNLTotEnMet = datumInNLTotEnMet;
	}

	public Boolean getDatumInNLIsLeeg()
	{
		return datumInNLIsLeeg;
	}

	public void setDatumInNLIsLeeg(Boolean datumInNLIsLeeg)
	{
		this.datumInNLIsLeeg = datumInNLIsLeeg;
	}

	public Boolean getGeboortelandOngelijkAanNL()
	{
		return geboortelandOngelijkAanNL;
	}

	public void setGeboortelandOngelijkAanNL(Boolean geboortelandOngelijkAanNL)
	{
		this.geboortelandOngelijkAanNL = geboortelandOngelijkAanNL;
	}

	public Boolean getGeboortelandIsLeeg()
	{
		return geboortelandIsLeeg;
	}

	public void setGeboortelandIsLeeg(Boolean geboortelandIsLeeg)
	{
		this.geboortelandIsLeeg = geboortelandIsLeeg;
	}

	public Boolean getNationaliteitOngelijkAanNL()
	{
		return nationaliteitOngelijkAanNL;
	}

	public void setNationaliteitOngelijkAanNL(Boolean nationaliteitOngelijkAanNL)
	{
		this.nationaliteitOngelijkAanNL = nationaliteitOngelijkAanNL;
	}

	public Boolean getNationaliteitIsLeeg()
	{
		return nationaliteitIsLeeg;
	}

	public void setNationaliteitIsLeeg(Boolean nationaliteitIsLeeg)
	{
		this.nationaliteitIsLeeg = nationaliteitIsLeeg;
	}

	public Boolean getDubbeleNationaliteit()
	{
		return dubbeleNationaliteit;
	}

	public void setDubbeleNationaliteit(Boolean dubbeleNationaliteit)
	{
		this.dubbeleNationaliteit = dubbeleNationaliteit;
	}

	public Boolean getOverleden()
	{
		return overleden;
	}

	public void setOverleden(Boolean overleden)
	{
		this.overleden = overleden;
	}

	public Boolean getBetalingsplichtige()
	{
		return betalingsplichtige;
	}

	public Nationaliteit getNationaliteit()
	{
		return getModelObject(nationaliteit);
	}

	public void setNationaliteit(Nationaliteit nationaliteit)
	{
		this.nationaliteit = makeModelFor(nationaliteit);
	}

	public TypeAdres getTypeAdres()
	{
		return typeAdres;
	}

	public void setTypeAdres(TypeAdres typeAdres)
	{
		this.typeAdres = typeAdres;
	}

	public Boolean getGeheimAdres()
	{
		return geheimAdres;
	}

	public void setGeheimAdres(Boolean geheimAdres)
	{
		this.geheimAdres = geheimAdres;
	}

	public String getHuisnummerToevoeging()
	{
		return huisnummerToevoeging;
	}

	public void setHuisnummerToevoeging(String huisnummerToevoeging)
	{
		this.huisnummerToevoeging = huisnummerToevoeging;
	}

	public Brin getBrinVooropleiding()
	{
		return getModelObject(brinVooropleiding);
	}

	public void setBrinVooropleiding(Brin brinVooropleiding)
	{
		this.brinVooropleiding = makeModelFor(brinVooropleiding);
	}

	public ExterneOrganisatie getVooropleidingExterneOrganisatie()
	{
		return getModelObject(vooropleidingExterneOrganisatie);
	}

	public void setVooropleidingExterneOrganisatie(
			ExterneOrganisatie vooropleidingExterneOrganisatie)
	{
		this.vooropleidingExterneOrganisatie = makeModelFor(vooropleidingExterneOrganisatie);
	}

	public String getNaamOnderwijsinstellingVooropleiding()
	{
		return naamOnderwijsinstellingVooropleiding;
	}

	public void setNaamOnderwijsinstellingVooropleiding(String naamOnderwijsinstellingVooropleiding)
	{
		this.naamOnderwijsinstellingVooropleiding = naamOnderwijsinstellingVooropleiding;
	}

	public SoortOnderwijs getCategorieVooropleiding()
	{
		return categorieVooropleiding;
	}

	public void setCategorieVooropleiding(SoortOnderwijs categorieVooropleiding)
	{
		this.categorieVooropleiding = categorieVooropleiding;
	}

	public SoortVooropleiding getSoortVooropleiding()
	{
		return getModelObject(soortVooropleiding);
	}

	public void setSoortVooropleiding(SoortVooropleiding soortVooropleiding)
	{
		this.soortVooropleiding = makeModelFor(soortVooropleiding);
	}

	public Date getVooropleidingBegindatumVanaf()
	{
		return vooropleidingBegindatumVanaf;
	}

	public void setVooropleidingBegindatumVanaf(Date vooropleidingBegindatumVanaf)
	{
		this.vooropleidingBegindatumVanaf = vooropleidingBegindatumVanaf;
	}

	public Date getVooropleidingBegindatumTotEnMet()
	{
		return vooropleidingBegindatumTotEnMet;
	}

	public void setVooropleidingBegindatumTotEnMet(Date vooropleidingBegindatumTotEnMet)
	{
		this.vooropleidingBegindatumTotEnMet = vooropleidingBegindatumTotEnMet;
	}

	public Date getVooropleidingEinddatumVanaf()
	{
		return vooropleidingEinddatumVanaf;
	}

	public void setVooropleidingEinddatumVanaf(Date vooropleidingEinddatumVanaf)
	{
		this.vooropleidingEinddatumVanaf = vooropleidingEinddatumVanaf;
	}

	public Date getVooropleidingEinddatumTotEnMet()
	{
		return vooropleidingEinddatumTotEnMet;
	}

	public void setVooropleidingEinddatumTotEnMet(Date vooropleidingEinddatumTotEnMet)
	{
		this.vooropleidingEinddatumTotEnMet = vooropleidingEinddatumTotEnMet;
	}

	public Integer getVooropleidingAantalJarenOnderwijsVanaf()
	{
		return vooropleidingAantalJarenOnderwijsVanaf;
	}

	public void setVooropleidingAantalJarenOnderwijsVanaf(
			Integer vooropleidingAantalJarenOnderwijsVanaf)
	{
		this.vooropleidingAantalJarenOnderwijsVanaf = vooropleidingAantalJarenOnderwijsVanaf;
	}

	public Integer getVooropleidingAantalJarenOnderwijsTotEnMet()
	{
		return vooropleidingAantalJarenOnderwijsTotEnMet;
	}

	public void setVooropleidingAantalJarenOnderwijsTotEnMet(
			Integer vooropleidingAantalJarenOnderwijsTotEnMet)
	{
		this.vooropleidingAantalJarenOnderwijsTotEnMet = vooropleidingAantalJarenOnderwijsTotEnMet;
	}

	public Boolean getVooropleidingAantalJarenOnderwijsIsLeeg()
	{
		return vooropleidingAantalJarenOnderwijsIsLeeg;
	}

	public void setVooropleidingAantalJarenOnderwijsIsLeeg(
			Boolean vooropleidingAantalJarenOnderwijsIsLeeg)
	{
		this.vooropleidingAantalJarenOnderwijsIsLeeg = vooropleidingAantalJarenOnderwijsIsLeeg;
	}

	public Boolean getVooropleidingDiplomaBehaald()
	{
		return vooropleidingDiplomaBehaald;
	}

	public void setVooropleidingDiplomaBehaald(Boolean vooropleidingDiplomaBehaald)
	{
		this.vooropleidingDiplomaBehaald = vooropleidingDiplomaBehaald;
	}

	public Schooladvies getVooropleidingSchooladvies()
	{
		return getModelObject(vooropleidingSchooladvies);
	}

	public void setVooropleidingSchooladvies(Schooladvies vooropleidingSchooladvies)
	{
		this.vooropleidingSchooladvies = makeModelFor(vooropleidingSchooladvies);
	}

	public Integer getVooropleidingCitoscore()
	{
		return vooropleidingCitoscore;
	}

	public void setVooropleidingCitoscore(Integer vooropleidingCitoscore)
	{
		this.vooropleidingCitoscore = vooropleidingCitoscore;
	}

	public boolean heeftVooropleidingCriteria()
	{
		return getVooropleidingExterneOrganisatie() != null || getBrinVooropleiding() != null
			|| getNaamOnderwijsinstellingVooropleiding() != null
			|| getCategorieVooropleiding() != null || getSoortVooropleiding() != null
			|| getVooropleidingBegindatumTotEnMet() != null
			|| getVooropleidingBegindatumTotEnMet() != null
			|| getVooropleidingEinddatumVanaf() != null
			|| getVooropleidingEinddatumTotEnMet() != null
			|| getVooropleidingAantalJarenOnderwijsVanaf() != null
			|| getVooropleidingAantalJarenOnderwijsTotEnMet() != null
			|| getVooropleidingAantalJarenOnderwijsIsLeeg() != null
			|| getVooropleidingDiplomaBehaald() != null
			|| (getBrinVooropleidingList() != null && !getBrinVooropleidingList().isEmpty())
			|| getVooropleidingSchooladvies() != null || getVooropleidingCitoscore() != null;
	}

	public List<Land> getGeboortelandList()
	{
		return getModelObject(geboortelandList);
	}

	public void setGeboortelandList(List<Land> geboortelandList)
	{
		this.geboortelandList = makeModelFor(geboortelandList);
	}

	public List<Nationaliteit> getNationaliteitList()
	{
		return getModelObject(nationaliteitList);
	}

	public void setNationaliteitList(List<Nationaliteit> nationaliteitList)
	{
		this.nationaliteitList = makeModelFor(nationaliteitList);
	}

	public List<Groep> getGroepList()
	{
		return getModelObject(groepList);
	}

	public void setGroepList(List<Groep> groepList)
	{
		this.groepList = makeModelFor(groepList);
	}

	public List<Brin> getBrinVooropleidingList()
	{
		return getModelObject(brinVooropleidingList);
	}

	public void setBrinVooropleidingList(List<Brin> brinVooropleidingList)
	{
		this.brinVooropleidingList = makeModelFor(brinVooropleidingList);
	}

	public void setContactgegeven(String contactgegeven)
	{
		this.contactgegeven = contactgegeven;
	}

	public String getContactgegeven()
	{
		return contactgegeven;
	}

	public void setTypeContactgegeven(TypeContactgegeven typeContactgegeven)
	{
		this.typeContactgegeven = typeContactgegeven;
	}

	public TypeContactgegeven getTypeContactgegeven()
	{
		return typeContactgegeven;
	}

	public void setDocumentTypeList(List<DocumentType> documentTypeList)
	{
		this.documentTypeList = makeModelFor(documentTypeList);
	}

	public List<DocumentType> getDocumentTypeList()
	{
		return getModelObject(documentTypeList);
	}

	public void setDocumentCategorieList(List<DocumentCategorie> documentCategorieList)
	{
		this.documentCategorieList = makeModelFor(documentCategorieList);
	}

	public List<DocumentCategorie> getDocumentCategorieList()
	{
		return getModelObject(documentCategorieList);
	}

	public boolean heeftBijalgeCriteria()
	{
		return (getDocumentCategorieList() != null && !getDocumentCategorieList().isEmpty())
			|| (getDocumentTypeList() != null && !getDocumentTypeList().isEmpty());
	}

	public Date getRegistratieDatumVanaf()
	{
		return registratieDatumVanaf;
	}

	public void setRegistratieDatumVanaf(Date registratieDatumVanaf)
	{
		this.registratieDatumVanaf = registratieDatumVanaf;
	}

	public Date getRegistratieDatumTotEnMet()
	{
		return registratieDatumTotEnMet;
	}

	public void setRegistratieDatumTotEnMet(Date registratieDatumTotEnMet)
	{
		this.registratieDatumTotEnMet = registratieDatumTotEnMet;
	}

	public Date getRegistratieDatum()
	{
		return registratieDatum;
	}

	public void setRegistratieDatum(Date registratieDatum)
	{
		this.registratieDatum = registratieDatum;
	}

	public Boolean getRegistratieDatumIsLeeg()
	{
		return registratieDatumIsLeeg;
	}

	public void setRegistratieDatumIsLeeg(Boolean registratieDatumIsLeeg)
	{
		this.registratieDatumIsLeeg = registratieDatumIsLeeg;
	}

	public void setUitsluitenVanFacturatie(Boolean uitsluitenVanFacturatie)
	{
		this.uitsluitenVanFacturatie = uitsluitenVanFacturatie;
	}

	public Boolean getUitsluitenVanFacturatie()
	{
		return uitsluitenVanFacturatie;
	}

	public void setHeeftAccount(Boolean heeftAccount)
	{
		this.heeftAccount = heeftAccount;
	}

	public Boolean getHeeftAccount()
	{
		return heeftAccount;
	}

	public Date getDatumLaatsteKenmerkWijziging()
	{
		return datumLaatsteKenmerkWijziging;
	}

	public void setDatumLaatsteKenmerkWijziging(Date datumLaatsteKenmerkWijziging)
	{
		this.datumLaatsteKenmerkWijziging = datumLaatsteKenmerkWijziging;
	}

	public List<Kenmerk> getKenmerkenLijst()
	{
		return getModelObject(kenmerkenLijst);
	}

	public void setKenmerkenList(List<Kenmerk> kenmerkenLijst)
	{
		this.kenmerkenLijst = makeModelFor(kenmerkenLijst);
	}

	public Long getOcwnummer()
	{
		return ocwnummer;
	}

	public void setOcwnummer(Long ocwnummer)
	{
		this.ocwnummer = ocwnummer;
	}

	public Integer getStudielinknummer()
	{
		return studielinknummer;
	}

	public void setStudielinknummer(Integer studielinknummer)
	{
		this.studielinknummer = studielinknummer;
	}

	/**
	 * @param groepsdeelnameBeeindigd
	 *            true indien het filter alleen deelnemers moet retourneren met een
	 *            groepsdeelname die eindigt in de peilperiode; false voor groepsdeelnames
	 *            die niet eindigen in de peilperiode. Wordt gebruikt in combinatie met
	 *            groep of groepList.
	 */
	public void setGroepsdeelnameBeeindigd(Boolean groepsdeelnameBeeindigd)
	{
		this.groepsdeelnameBeeindigd = groepsdeelnameBeeindigd;
	}

	/**
	 * @return true indien het filter alleen deelnemers moet retourneren met een
	 *         groepsdeelname die eindigt in de peilperiode; false voor groepsdeelnames
	 *         die niet eindigen in de peilperiode. Wordt gebruikt in combinatie met groep
	 *         of groepList.
	 */
	public Boolean getGroepsdeelnameBeeindigd()
	{
		return groepsdeelnameBeeindigd;
	}

	public void setEersteVoorletter(String eersteVoorletter)
	{
		this.eersteVoorletter = eersteVoorletter;
	}

	public String getEersteVoorletter()
	{
		return eersteVoorletter;
	}

	public Date getStartkwalificatieplichtigOpDatum()
	{
		return startkwalificatieplichtigOpDatum;
	}

	public void setStartkwalificatieplichtigOpDatum(Date startkwalificatieplichtigOpDatum)
	{
		this.startkwalificatieplichtigOpDatum = startkwalificatieplichtigOpDatum;
	}
}
