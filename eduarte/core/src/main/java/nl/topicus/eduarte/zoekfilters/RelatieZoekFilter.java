/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import java.util.Collection;
import java.util.Date;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.landelijk.Provincie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.personen.RelatieSoort;
import nl.topicus.eduarte.entities.vrijevelden.PersoonVrijVeld;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekFilter.TypeAdres;

import org.apache.wicket.model.IModel;

/**
 * @author idserda
 */
public class RelatieZoekFilter extends AbstractVrijVeldableZoekFilter<PersoonVrijVeld, Relatie>
{
	private static final long serialVersionUID = 1L;

	private Integer deelnemernummer;

	private Integer deelnemernummerVanaf;

	private Integer deelnemernummerTotEnMet;

	private String roepnaam;

	private String achternaam;

	private String volledigeNaam;

	private Long bsn;

	private Long onderwijsnummer;

	private Geslacht geslacht;

	private Date geboortedatumVanaf;

	private Date geboortedatumTotEnMet;

	private IModel<Gemeente> geboorteGemeente;

	private IModel<Land> geboorteLand;

	private IModel<Land> geboorteLandOngelijkAan;

	private IModel<Nationaliteit> nationaliteit1;

	private IModel<Nationaliteit> nationaliteit2;

	private IModel<Nationaliteit> nationaliteit1OngelijkAan;

	private IModel<Nationaliteit> nationaliteit2OngelijkAan;

	private IModel<Groep> groep;

	private IModel<Gemeente> gemeente;

	private IModel<Gemeente> gemeenteOngelijkAan;

	private TypeAdres typeAdres;

	private Boolean zelfdeAdresAlsDeelnemer;

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

	private Collection<Long> idNotIn;

	private IModel<Persoon> deelnemerOngelijkAan;

	private IModel<RelatieSoort> relatieSoort;

	private IModel<ExterneOrganisatie> externeOrganisatie;

	private Long debiteurennummer;

	/**
	 * Zoekt op class Persoon ipv Relatie.
	 */
	private boolean zoekPersonen;

	@AutoForm(label = "Toon ongekoppelde personen")
	private boolean zoekOngekoppeldePersonen;

	private Boolean geboortelandOngelijkAanNL;

	private Boolean nationaliteitOngelijkAanNL;

	public RelatieZoekFilter()
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
		return getModelObject(geboorteLand);
	}

	public void setGeboorteland(Land geboorteLand)
	{
		this.geboorteLand = makeModelFor(geboorteLand);
	}

	public Land getGeboortelandOngelijkAan()
	{
		return getModelObject(geboorteLandOngelijkAan);
	}

	public void setGeboortelandOngelijkAan(Land geboorteLandOngelijkAan)
	{
		this.geboorteLandOngelijkAan = makeModelFor(geboorteLandOngelijkAan);
	}

	public Nationaliteit getNationaliteit1()
	{
		return getModelObject(nationaliteit1);
	}

	public void setNationaliteit1(Nationaliteit nationaliteit1)
	{
		this.nationaliteit1 = makeModelFor(nationaliteit1);
	}

	public Nationaliteit getNationaliteit2()
	{
		return getModelObject(nationaliteit2);
	}

	public void setNationaliteit2(Nationaliteit nationaliteit2)
	{
		this.nationaliteit2 = makeModelFor(nationaliteit2);
	}

	public Nationaliteit getNationaliteit1OngelijkAan()
	{
		return getModelObject(nationaliteit1OngelijkAan);
	}

	public void setNationaliteit1OngelijkAan(Nationaliteit nationaliteit1OngelijkAan)
	{
		this.nationaliteit1OngelijkAan = makeModelFor(nationaliteit1OngelijkAan);
	}

	public Nationaliteit getNationaliteit2OngelijkAan()
	{
		return getModelObject(nationaliteit2OngelijkAan);
	}

	public void setNationaliteit2OngelijkAan(Nationaliteit nationaliteit2OngelijkAan)
	{
		this.nationaliteit2OngelijkAan = makeModelFor(nationaliteit2OngelijkAan);
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

	/**
	 * @return true als er adrescriteria zijn ingesteld.
	 */
	public boolean heeftAdresCriteria()
	{
		return getStraat() != null || getProvincie() != null || getProvincieOngelijkAan() != null
			|| getLand() != null || getLandOngelijkAan() != null || getPlaats() != null
			|| getPlaatsOngelijkAan() != null || getPostcode() != null
			|| getPostcodeVanaf() != null || getPostcodeTotEnMet() != null
			|| getHuisnummer() != null || getGemeente() != null || getGemeenteOngelijkAan() != null;
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

	public Collection<Long> getIdNotIn()
	{
		return idNotIn;
	}

	public void setIdNotIn(Collection<Long> idNotIn)
	{
		this.idNotIn = idNotIn;
	}

	public void setDeelnemerOngelijkAan(Persoon deelnemerOngelijkAan)
	{
		this.deelnemerOngelijkAan = makeModelFor(deelnemerOngelijkAan);
	}

	public Persoon getDeelnemerOngelijkAan()
	{
		return getModelObject(deelnemerOngelijkAan);
	}

	public void setRelatieSoort(RelatieSoort relatieSoort)
	{
		this.relatieSoort = makeModelFor(relatieSoort);
	}

	public RelatieSoort getRelatieSoort()
	{
		return getModelObject(relatieSoort);
	}

	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = makeModelFor(externeOrganisatie);
	}

	public ExterneOrganisatie getExterneOrganisatie()
	{
		return getModelObject(externeOrganisatie);
	}

	public Long getDebiteurennummer()
	{
		return debiteurennummer;
	}

	public void setDebiteurennummer(Long debiteurennummer)
	{
		this.debiteurennummer = debiteurennummer;
	}

	public boolean isZoekPersonen()
	{
		return zoekPersonen;
	}

	public void setZoekPersonen(boolean zoekPersonen)
	{
		this.zoekPersonen = zoekPersonen;
	}

	public void setZoekOngekoppeldePersonen(boolean zoekOngekoppeldePersonen)
	{
		this.zoekOngekoppeldePersonen = zoekOngekoppeldePersonen;
	}

	public boolean isZoekOngekoppeldePersonen()
	{
		return zoekOngekoppeldePersonen;
	}

	public String getHuisnummerToevoeging()
	{
		return huisnummerToevoeging;
	}

	public void setHuisnummerToevoeging(String huisnummerToevoeging)
	{
		this.huisnummerToevoeging = huisnummerToevoeging;
	}

	public TypeAdres getTypeAdres()
	{
		return typeAdres;
	}

	public void setTypeAdres(TypeAdres typeAdres)
	{
		this.typeAdres = typeAdres;
	}

	public Boolean getZelfdeAdresAlsDeelnemer()
	{
		return zelfdeAdresAlsDeelnemer;
	}

	public void setZelfdeAdresAlsDeelnemer(Boolean zelfdeAdresAlsDeelnemer)
	{
		this.zelfdeAdresAlsDeelnemer = zelfdeAdresAlsDeelnemer;
	}

	public void setGeboortelandOngelijkAanNL(Boolean geboortelandOngelijkAanNL)
	{
		this.geboortelandOngelijkAanNL = geboortelandOngelijkAanNL;
	}

	public Boolean getGeboortelandOngelijkAanNL()
	{
		return geboortelandOngelijkAanNL;
	}

	public void setNationaliteitOngelijkAanNL(Boolean nationaliteitOngelijkAanNL)
	{
		this.nationaliteitOngelijkAanNL = nationaliteitOngelijkAanNL;
	}

	public Boolean getNationaliteitOngelijkAanNL()
	{
		return nationaliteitOngelijkAanNL;
	}
}
