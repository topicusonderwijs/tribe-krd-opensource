package nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.AbstractBronBatchVO;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.AbstractBronVoTerugkoppelMelding;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;

import org.apache.wicket.util.string.Strings;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "BRON_VO_AANLEVERMELDINGEN")
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
public abstract class AbstractBronVOMelding extends InstellingEntiteit implements IBronMelding
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	@Column(nullable = false, insertable = false, updatable = false)
	@RestrictedAccess(hasGetter = false, hasSetter = false)
	private Integer DTYPE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_BRON_VO_AANL_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verbintenis", nullable = false)
	@Index(name = "idx_BRON_VO_AANL_verbintenis")
	private Verbintenis verbintenis;

	@Column(nullable = false)
	private Integer vestigingsVolgnummer;

	@Column(nullable = false)
	private Integer meldingNummer;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SoortMutatie soortMutatie;

	// of sofi- of onderwijsnummer moet ingevuld zijn, daarom nullable = true
	@Column(nullable = true)
	private Integer sofiNummer;

	// of sofi- of onderwijsnummer moet ingevuld zijn, daarom nullable = true
	@Column(nullable = true)
	private Integer onderwijsNummer;

	@Column(nullable = false, length = 10)
	private String leerlingNummerInstelling;

	@Column(nullable = false)
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	private Datum geboorteDatum;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Geslacht geslacht;

	@ManyToOne
	@JoinColumn(nullable = true, name = "batch")
	@Index(name = "idx_BRON_VO_AANL_batch")
	private AbstractBronBatchVO< ? > batch;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BronMeldingStatus bronMeldingStatus;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	@Lob
	private String reden;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "terugkoppelmelding")
	@Index(name = "idx_BRON_VO_AANL_terugkoppelm")
	private AbstractBronVoTerugkoppelMelding terugkoppelmelding;

	/*
	 * Geblokkeerde meldingen worden niet standaard meegenomen bij het aanmaken van een
	 * batch.
	 */
	@Column(nullable = false)
	private boolean geblokkeerd;

	@Lob
	@Column(nullable = true)
	private String redenGeblokkeerd;

	/**
	 * Geeft aan of deze mutatie bekostigingsRelevante info bevat. Dit wordt gebruikt om
	 * te bepalen of het om accountantsmutatie gaat. Afhankelijk van de ingangsdatum van
	 * deze melding, en de bronschooljaarstatus. voor examengegevens is dat altijd false
	 */
	@Column(nullable = false)
	private boolean bekostigingsRelevant;

	protected AbstractBronVOMelding()
	{
		// alleen voor Hibernate!!!
	}

	protected AbstractBronVOMelding(AbstractBronBatchVO< ? > batch)
	{
		this.batch = batch;
		// standaard bij het aanmaken de status op wachtrij zetten
		setBronMeldingStatus(BronMeldingStatus.WACHTRIJ);
	}

	public void vulSleutelgegevens()
	{
		BronEduArteModel model = new BronEduArteModel();
		setVestigingsVolgnummer(model.getVestigingsVolgNummer(getVerbintenis()));
		setGeboorteDatum(model.getGeboortedatum(getDeelnemer()));
		setGeslacht(model.getGeslacht(getDeelnemer()));
		String sofiNr = model.getSofinummer(getDeelnemer());
		String onderwijsnummer = model.getOnderwijsnummer(getDeelnemer());
		if (!Strings.isEmpty(sofiNr))
		{
			setSofiNummer(Integer.parseInt(sofiNr));
		}
		else if (!Strings.isEmpty(onderwijsnummer))
		{
			setOnderwijsNummer(Integer.parseInt(onderwijsnummer));
		}
		setLeerlingNummerInstelling(model.getLeerlingnummer(getDeelnemer()));
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Integer getVestigingsVolgnummer()
	{
		return vestigingsVolgnummer;
	}

	public void setVestigingsVolgnummer(Integer vestigingsVolgnummer)
	{
		this.vestigingsVolgnummer = vestigingsVolgnummer;
	}

	public Integer getMeldingNummer()
	{
		return meldingNummer;
	}

	/**
	 * Nodig om aan interface te voldoen.
	 */
	@Override
	public Integer getMeldingnummer()
	{
		return getMeldingNummer();
	}

	public void setMeldingNummer(Integer meldingNummer)
	{
		this.meldingNummer = meldingNummer;
	}

	public SoortMutatie getSoortMutatie()
	{
		return soortMutatie;
	}

	public void setSoortMutatie(SoortMutatie soortMutatie)
	{
		this.soortMutatie = soortMutatie;
	}

	public Integer getSofiNummer()
	{
		return sofiNummer;
	}

	public void setSofiNummer(Integer sofiNummer)
	{
		this.sofiNummer = sofiNummer;
	}

	public Integer getOnderwijsNummer()
	{
		return onderwijsNummer;
	}

	public void setOnderwijsNummer(Integer onderwijsNummer)
	{
		this.onderwijsNummer = onderwijsNummer;
	}

	public String getLeerlingNummerInstelling()
	{
		return leerlingNummerInstelling;
	}

	public void setLeerlingNummerInstelling(String leerlingNummerInstelling)
	{
		this.leerlingNummerInstelling = leerlingNummerInstelling;
	}

	public Datum getGeboorteDatum()
	{
		return geboorteDatum;
	}

	public void setGeboorteDatum(Datum geboorteDatum)
	{
		this.geboorteDatum = geboorteDatum;
	}

	public Geslacht getGeslacht()
	{
		return geslacht;
	}

	public void setGeslacht(Geslacht geslacht)
	{
		this.geslacht = geslacht;
	}

	public AbstractBronBatchVO< ? > getBatch()
	{
		return batch;
	}

	public void setBatch(AbstractBronBatchVO< ? > batch)
	{
		this.batch = batch;
	}

	@Override
	public List<SoortMutatie> getSoortMutaties()
	{
		List<SoortMutatie> mutaties = new ArrayList<SoortMutatie>();
		mutaties.add(getSoortMutatie());
		return mutaties;
	}

	public void setBronMeldingStatus(BronMeldingStatus bronMeldingStatus)
	{
		this.bronMeldingStatus = bronMeldingStatus;
	}

	public BronMeldingStatus getBronMeldingStatus()
	{
		return bronMeldingStatus;
	}

	public final String getOnderwijssoort()
	{
		return "Voortgezet onderwijs";
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	@Override
	public String getBronMeldingOnderdelenOmsch()
	{
		return getBronMeldingOnderdelen().get(0).toString();
	}

	@Override
	public String getSoortMutatiesOmsch()
	{
		return getSoortMutatie().toString();
	}

	public String getReden()
	{
		return reden;
	}

	public void addReden(String wijziging)
	{
		if (StringUtil.isEmpty(wijziging))
			return;
		if (StringUtil.isEmpty(reden))
		{
			reden = wijziging.substring(0, Math.min(4000, wijziging.length()));
		}
		else
		{
			int length = reden.length() + 1 + wijziging.length();
			if (length <= 4000)
			{
				reden = reden + "\n" + wijziging;
			}
		}
	}

	public void setTerugkoppelmelding(AbstractBronVoTerugkoppelMelding terugkoppelmelding)
	{
		this.terugkoppelmelding = terugkoppelmelding;
	}

	public AbstractBronVoTerugkoppelMelding getTerugkoppelmelding()
	{
		if (terugkoppelmelding != null)
		{
			AbstractBronVoTerugkoppelMelding meld =
				(AbstractBronVoTerugkoppelMelding) terugkoppelmelding.doUnproxy();
			return meld;
		}
		return null;
	}

	@Override
	public void setGeblokkeerd(boolean geblokkeerd)
	{
		this.geblokkeerd = geblokkeerd;
	}

	@Override
	public boolean isGeblokkeerd()
	{
		return geblokkeerd;
	}

	@Override
	public String getRedenGeblokkeerd()
	{
		return redenGeblokkeerd;
	}

	@Override
	public void setRedenGeblokkeerd(String redenGeblokkeerd)
	{
		this.redenGeblokkeerd = redenGeblokkeerd;
	}

	public void setBekostigingsRelevant(boolean bekostigingsRelevant)
	{
		this.bekostigingsRelevant = bekostigingsRelevant;
	}

	public boolean isBekostigingsRelevant()
	{
		return bekostigingsRelevant;
	}

	@Override
	public boolean bevatAlleenToevoegingen()
	{
		return SoortMutatie.Toevoeging.equals(getSoortMutatie());
	}

	public boolean bevatSofiOfOnderwijsNummer()
	{
		return getSofiNummer() != null || getOnderwijsNummer() != null;
	}

	@Override
	public Integer getBatchNummer()
	{
		if (getBatch() != null)
			return getBatch().getBatchNummer();
		return null;
	}

	@Override
	public String getBestandsnaam()
	{
		if (getBatch() != null)
			return getBatch().getBestandsnaam();
		return null;
	}

	@Override
	public Integer getTerugkoppelbestandNummer()
	{
		if (getTerugkoppelmelding() == null)
			return null;
		return getTerugkoppelmelding().getBatchNummer();
	}

	public abstract void voegOpnieuwToeAanWachtrij();
}
