package nl.topicus.eduarte.entities.ibgverzuimloket;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.onderwijs.ibgverzuimloket.model.IIbgVerzuimmelding;
import nl.topicus.onderwijs.ibgverzuimloket.model.IbgEnums;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class IbgVerzuimmelding extends BeginEinddatumInstellingEntiteit implements
		IIbgVerzuimmelding<IbgVerzuimdag>
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private boolean actieOndernemen;

	@Column(length = 60, nullable = true)
	@AutoForm(label = "Functie melder")
	private String functieMelder;

	@Column(nullable = true, length = 5)
	@AutoForm(label = "Netnummer")
	private String netnummermelder;

	@Column(length = 100, nullable = true)
	private String toelichting;

	@Lob
	@Column(nullable = true)
	@AutoForm(label = "Toelichting actie gewenst", htmlClasses = "unit_max")
	private String toelichtingActieGewenst;

	@Lob
	@Column(nullable = true)
	@AutoForm(label = "Toelichting ondernomen actie", htmlClasses = "unit_max")
	private String toelichtingOndernomenactie;

	@Column(nullable = true)
	private boolean verzuimdagGespecificeerd;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private IbgEnums.Verzuimsoort verzuimsoort;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date melddatumtijd;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verbintenis", nullable = false)
	@Index(name = "idx_verzuimmelding_verbintenis")
	private Verbintenis verbintenis;

	@Column(length = 10, nullable = true)
	@AutoForm(label = "Abonneenummer")
	private String abonneenummerMelder;

	@Column(length = 100, nullable = false)
	@AutoForm(label = "E-mailadres")
	private String emailadresMelder;

	@Column(length = 200, nullable = false)
	@AutoForm(label = "Vermoedelijke reden")
	private String vermoedelijkeReden;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verzuimmelding")
	private List<IbgVerzuimdag> verzuimdagen = new ArrayList<IbgVerzuimdag>();

	@Column(nullable = true)
	private boolean alleLessenGemist;

	@Column(length = 100, nullable = false)
	@AutoForm(label = "Naam melder")
	private String aanduidingContactpersoon;

	@Column(nullable = true)
	@AutoForm(label = "Begindatum selectieperiode")
	private Date begindatumSelectie;

	@Column(nullable = false)
	private BigInteger meldingsnummer;

	@Column(nullable = true)
	private Date laatsteMutatieDatum;

	@Column(nullable = true)
	private Date laatsteMutatieTijd;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private IbgEnums.StatusMeldingRelatiefVerzuim status;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@AutoForm(label = "CC e-mail ontvanger")
	private IbgEnums.CCEmailontvanger ccEmailontvanger;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_verzuimmelding_locatie")
	private Locatie locatie;

	@Column(nullable = false)
	private boolean verzonden;

	public IbgEnums.CCEmailontvanger getCcEmailontvanger()
	{
		return ccEmailontvanger;
	}

	public boolean isVerzonden()
	{
		return verzonden;
	}

	public void setVerzonden(boolean verzonden)
	{
		this.verzonden = verzonden;
	}

	// gebruik createIbgVerzuimmelding
	protected IbgVerzuimmelding()
	{

	}

	@Override
	public boolean isActieOndernemen()
	{
		return actieOndernemen;
	}

	@Override
	public Date getBeginverzuim()
	{
		return getBegindatum();
	}

	@Override
	public Date getEindverzuim()
	{
		return getEinddatum();
	}

	public String getfunctieMelder()
	{
		return functieMelder;
	}

	@Override
	public String getNetnummermelder()
	{
		return netnummermelder;
	}

	@Override
	public String getToelichting()
	{

		return toelichting;
	}

	@Override
	public String getToelichtingActieGewenst()
	{
		return toelichtingActieGewenst;
	}

	@Override
	public String getToelichtingOndernomenactie()
	{
		return toelichtingOndernomenactie;
	}

	@Override
	public IbgEnums.Verzuimsoort getVerzuimsoort()
	{
		return verzuimsoort;
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	@Override
	public void setActieOndernemen(boolean value)
	{
		actieOndernemen = value;

	}

	@Override
	public void setBeginverzuim(Date value)
	{
		setBegindatum(value);
	}

	@Override
	public void setEindverzuim(Date value)
	{
		setEinddatum(value);
	}

	public void setFunctieMelder(String value)
	{
		functieMelder = value;
	}

	@Override
	public void setNetnummermelder(String value)
	{
		netnummermelder = value;
	}

	@Override
	public void setToelichtingOndernomenactie(String value)
	{
		toelichtingOndernomenactie = value;
	}

	@Override
	public void setToelichtingActieGewenst(String value)
	{
		toelichtingActieGewenst = value;
	}

	@Override
	public void setVerzuimsoort(IbgEnums.Verzuimsoort value)
	{
		verzuimsoort = value;
	}

	public void setVerbintenis(Verbintenis value)
	{
		verbintenis = value;
	}

	@Override
	public Date getMelddatumtijd()
	{

		return melddatumtijd;
	}

	@Override
	public void setMelddatumtijd(Date value)
	{
		melddatumtijd = value;
	}

	public void setToelichting(String value)
	{
		toelichting = value;
	}

	@Override
	public List<IbgVerzuimdag> getVerzuimdagen()
	{
		return verzuimdagen;
	}

	public void setVerzuimdagen(List<IbgVerzuimdag> values)
	{
		verzuimdagen = values;
	}

	@Override
	public String getAbonneenummerMelder()
	{
		return abonneenummerMelder;
	}

	@Override
	public String getEmailadresMelder()
	{
		return emailadresMelder;
	}

	@Override
	public String getFunctieMelder()
	{

		return functieMelder;
	}

	@Override
	public String getVermoedelijkeReden()
	{
		return vermoedelijkeReden;
	}

	@Override
	public BigInteger getMeldingsnummer()
	{
		return meldingsnummer;
	}

	@Override
	public void setAbonneenummerMelder(String value)
	{
		abonneenummerMelder = value;

	}

	@Override
	public void setEmailadresMelder(String value)
	{
		emailadresMelder = value;

	}

	@Override
	public void setVermoedelijkeReden(String value)
	{
		vermoedelijkeReden = value;

	}

	@Override
	public boolean isAlleLessenGemist()
	{
		return alleLessenGemist;
	}

	@Override
	public void setAlleLessenGemist(boolean value)
	{
		alleLessenGemist = value;

	}

	@Override
	public String getAanduidingContactpersoon()
	{
		return aanduidingContactpersoon;
	}

	@Override
	public void setAanduidingContactpersoon(String value)
	{
		aanduidingContactpersoon = value;
	}

	@Override
	public void setMeldingsnummer(BigInteger value)
	{
		meldingsnummer = value;
	}

	@Override
	public Date getBegindatumSelectie()
	{
		return begindatumSelectie;
	}

	@Override
	public void setBegindatumSelectie(Date value)
	{
		begindatumSelectie = value;

	}

	@Override
	public Date getLaatsteMutatieDatum()
	{
		return laatsteMutatieDatum;
	}

	@Override
	public void setLaatsteMutatieDatum(Date value)
	{
		laatsteMutatieDatum = value;
	}

	@Override
	public IbgEnums.StatusMeldingRelatiefVerzuim getStatus()
	{
		return status;
	}

	@Override
	public void setStatus(IbgEnums.StatusMeldingRelatiefVerzuim value)
	{
		status = value;
	}

	public static IbgVerzuimmelding createIbgVerzuimmelding(Verbintenis inschrijving)
	{
		IbgVerzuimmelding melding = new IbgVerzuimmelding();
		melding.setMelddatumtijd(TimeUtil.getInstance().currentDateTime());
		melding.setVerbintenis(inschrijving);

		return melding;
	}

	@Override
	public void setVerzuimdagGespecificeerd(boolean verzuimdagGespecificeerd)
	{
		this.verzuimdagGespecificeerd = verzuimdagGespecificeerd;
	}

	@Override
	public boolean isVerzuimdagGespecificeerd()
	{
		return verzuimdagGespecificeerd;
	}

	@Override
	public void setCcEmailontvanger(IbgEnums.CCEmailontvanger emailontvanger)
	{
		ccEmailontvanger = emailontvanger;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	public Locatie getLocatie()
	{
		return locatie;
	}

	@Override
	public void setLaatsteMutatieTijd(Date laatsteMutatieTijd)
	{
		this.laatsteMutatieTijd = laatsteMutatieTijd;
	}

	@Override
	public Date getLaatsteMutatieTijd()
	{
		return laatsteMutatieTijd;
	}
}
