package nl.topicus.eduarte.zoekfilters;

import java.math.BigInteger;
import java.util.Date;

import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.onderwijs.ibgverzuimloket.model.IbgEnums;

import org.apache.wicket.model.IModel;

public class DeelnemerVerzuimloketZoekfilter extends AbstractZoekFilter<IbgVerzuimmelding>
{
	private static final long serialVersionUID = 1L;

	private BigInteger meldingsnummer;

	private IModel<Verbintenis> verbintenis;

	private Date vanafDatum;

	private Date tmDatum;

	private IbgEnums.StatusMeldingRelatiefVerzuim status;

	private Integer leerlingnummer;

	private String naam;

	private IbgEnums.Verzuimsoort verzuimsoort;

	public DeelnemerVerzuimloketZoekfilter()
	{
	}

	public DeelnemerVerzuimloketZoekfilter(Verbintenis inschrijving)
	{
		this.verbintenis = makeModelFor(inschrijving);
	}

	public DeelnemerVerzuimloketZoekfilter(IModel<Verbintenis> inschrijving)
	{
		this.verbintenis = inschrijving;
	}

	public BigInteger getMeldingsnummer()
	{
		return meldingsnummer;
	}

	public void setMeldingsnummer(BigInteger meldingsnummer)
	{
		this.meldingsnummer = meldingsnummer;
	}

	public Verbintenis getVerbintenis()
	{
		return getModelObject(verbintenis);
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = makeModelFor(verbintenis);
	}

	public Date getVanafDatum()
	{
		return vanafDatum;
	}

	public void setVanafDatum(Date vanafDatum)
	{
		this.vanafDatum = vanafDatum;
	}

	public Date getTmDatum()
	{
		return tmDatum;
	}

	public void setTmDatum(Date tmDatum)
	{
		this.tmDatum = tmDatum;
	}

	public IbgEnums.StatusMeldingRelatiefVerzuim getStatus()
	{
		return status;
	}

	public void setStatus(IbgEnums.StatusMeldingRelatiefVerzuim status)
	{
		this.status = status;
	}

	public Integer getLeerlingnummer()
	{
		return leerlingnummer;
	}

	public void setLeerlingnummer(Integer leerlingnummer)
	{
		this.leerlingnummer = leerlingnummer;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public IbgEnums.Verzuimsoort getVerzuimsoort()
	{
		return verzuimsoort;
	}

	public void setVerzuimsoort(IbgEnums.Verzuimsoort verzuimsoort)
	{
		this.verzuimsoort = verzuimsoort;
	}
}
