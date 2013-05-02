package nl.topicus.eduarte.onderwijscatalogus.jobs;

import java.math.BigDecimal;

public class OnderwijsproductImportRegel
{
	private String code;

	private String titel;

	private String omschrijving;

	private String soortOnderwijsproduct;

	private String zoektermen;

	private int status;

	private String taxonomieCodes;

	private String organisatieEenheden;

	private String locaties;

	private String paklijst;

	private String voorwaarden;

	private BigDecimal kostprijs;

	private String kalender;

	private String uitvoeringsfrequentie;

	private BigDecimal omvang;

	private BigDecimal belasting;

	private int aggregatieniveau;

	private Boolean startonderwijsproduct;

	private String leerstijl;

	private String toegankelijkheid;

	private int minimumAantalDeelnemers;

	private int maximumAantalDeelnemers;

	private String juridischEigenaar;

	private String gebruiksrecht;

	private String soortPraktijklokaal;

	private String typeToets;

	private String personeelCompetenties;

	private String personeelKennisGebiedNiveau;

	private String personeelWettelijkeVereisten;

	private String personeelBevoegdheid;

	private String typeLocatie;

	private String gebruiksmiddelen;

	private String verbruiksmiddelen;

	public OnderwijsproductImportRegel(String code, String titel, String omschrijving,
			String soortOnderwijsproduct, Integer status, Integer aggregatieniveau)
	{
		this.code = code;
		this.titel = titel;
		this.omschrijving = omschrijving;
		this.soortOnderwijsproduct = soortOnderwijsproduct;
		this.status = status;
		this.aggregatieniveau = aggregatieniveau;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getTitel()
	{
		return titel;
	}

	public void setTitel(String titel)
	{
		this.titel = titel;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getSoortOnderwijsproduct()
	{
		return soortOnderwijsproduct;
	}

	public void setSoortOnderwijsproduct(String soortOnderwijsproduct)
	{
		this.soortOnderwijsproduct = soortOnderwijsproduct;
	}

	public String getZoektermen()
	{
		return zoektermen;
	}

	public void setZoektermen(String zoektermen)
	{
		this.zoektermen = zoektermen;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getTaxonomieCodes()
	{
		return taxonomieCodes;
	}

	public void setTaxonomieCodes(String taxonomieCodes)
	{
		this.taxonomieCodes = taxonomieCodes;
	}

	public String getOrganisatieEenheden()
	{
		return organisatieEenheden;
	}

	public void setOrganisatieEenheden(String organisatieEenheden)
	{
		this.organisatieEenheden = organisatieEenheden;
	}

	public void setLocaties(String locaties)
	{
		this.locaties = locaties;
	}

	public String getLocaties()
	{
		return locaties;
	}

	public String getPaklijst()
	{
		return paklijst;
	}

	public void setPaklijst(String paklijst)
	{
		this.paklijst = paklijst;
	}

	public String getVoorwaarden()
	{
		return voorwaarden;
	}

	public void setVoorwaarden(String voorwaarden)
	{
		this.voorwaarden = voorwaarden;
	}

	public String getKalender()
	{
		return kalender;
	}

	public void setKalender(String kalender)
	{
		this.kalender = kalender;
	}

	public String getUitvoeringsfrequentie()
	{
		return uitvoeringsfrequentie;
	}

	public void setUitvoeringsfrequentie(String uitvoeringsfrequentie)
	{
		this.uitvoeringsfrequentie = uitvoeringsfrequentie;
	}

	public int getAggregatieniveau()
	{
		return aggregatieniveau;
	}

	public void setAggregatieniveau(int aggregatieniveau)
	{
		this.aggregatieniveau = aggregatieniveau;
	}

	public Boolean getStartonderwijsproduct()
	{
		return startonderwijsproduct;
	}

	public void setStartonderwijsproduct(Boolean startonderwijsproduct)
	{
		this.startonderwijsproduct = startonderwijsproduct;
	}

	public String getLeerstijl()
	{
		return leerstijl;
	}

	public void setLeerstijl(String leerstijl)
	{
		this.leerstijl = leerstijl;
	}

	public String getToegankelijkheid()
	{
		return toegankelijkheid;
	}

	public void setToegankelijkheid(String toegankelijkheid)
	{
		this.toegankelijkheid = toegankelijkheid;
	}

	public int getMinimumAantalDeelnemers()
	{
		return minimumAantalDeelnemers;
	}

	public void setMinimumAantalDeelnemers(int minimumAantalDeelnemers)
	{
		this.minimumAantalDeelnemers = minimumAantalDeelnemers;
	}

	public int getMaximumAantalDeelnemers()
	{
		return maximumAantalDeelnemers;
	}

	public void setMaximumAantalDeelnemers(int maximumAantalDeelnemers)
	{
		this.maximumAantalDeelnemers = maximumAantalDeelnemers;
	}

	public String getJuridischEigenaar()
	{
		return juridischEigenaar;
	}

	public void setJuridischEigenaar(String juridischEigenaar)
	{
		this.juridischEigenaar = juridischEigenaar;
	}

	public String getGebruiksrecht()
	{
		return gebruiksrecht;
	}

	public void setGebruiksrecht(String gebruiksrecht)
	{
		this.gebruiksrecht = gebruiksrecht;
	}

	public String getSoortPraktijklokaal()
	{
		return soortPraktijklokaal;
	}

	public void setSoortPraktijklokaal(String soortPraktijklokaal)
	{
		this.soortPraktijklokaal = soortPraktijklokaal;
	}

	public String getTypeToets()
	{
		return typeToets;
	}

	public void setTypeToets(String typeToets)
	{
		this.typeToets = typeToets;
	}

	public String getPersoneelCompetenties()
	{
		return personeelCompetenties;
	}

	public void setPersoneelCompetenties(String personeelCompetenties)
	{
		this.personeelCompetenties = personeelCompetenties;
	}

	public String getPersoneelKennisGebiedNiveau()
	{
		return personeelKennisGebiedNiveau;
	}

	public void setPersoneelKennisGebiedNiveau(String personeelKennisGebiedNiveau)
	{
		this.personeelKennisGebiedNiveau = personeelKennisGebiedNiveau;
	}

	public String getPersoneelWettelijkeVereisten()
	{
		return personeelWettelijkeVereisten;
	}

	public void setPersoneelWettelijkeVereisten(String personeelWettelijkeVereisten)
	{
		this.personeelWettelijkeVereisten = personeelWettelijkeVereisten;
	}

	public String getPersoneelBevoegdheid()
	{
		return personeelBevoegdheid;
	}

	public void setPersoneelBevoegdheid(String personeelBevoegdheid)
	{
		this.personeelBevoegdheid = personeelBevoegdheid;
	}

	public String getTypeLocatie()
	{
		return typeLocatie;
	}

	public void setTypeLocatie(String typeLocatie)
	{
		this.typeLocatie = typeLocatie;
	}

	public String getGebruiksmiddelen()
	{
		return gebruiksmiddelen;
	}

	public void setGebruiksmiddelen(String gebruiksmiddelen)
	{
		this.gebruiksmiddelen = gebruiksmiddelen;
	}

	public String getVerbruiksmiddelen()
	{
		return verbruiksmiddelen;
	}

	public void setVerbruiksmiddelen(String verbruiksmiddelen)
	{
		this.verbruiksmiddelen = verbruiksmiddelen;
	}

	public BigDecimal getKostprijs()
	{
		return kostprijs;
	}

	public void setKostprijs(BigDecimal kostprijs)
	{
		this.kostprijs = kostprijs;
	}

	public BigDecimal getOmvang()
	{
		return omvang;
	}

	public void setOmvang(BigDecimal omvang)
	{
		this.omvang = omvang;
	}

	public BigDecimal getBelasting()
	{
		return belasting;
	}

	public void setBelasting(BigDecimal belasting)
	{
		this.belasting = belasting;
	}
}