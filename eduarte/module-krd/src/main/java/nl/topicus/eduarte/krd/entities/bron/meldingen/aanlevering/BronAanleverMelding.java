package nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering;

import static nl.topicus.onderwijs.duo.bron.BRONConstants.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.BronMeldingOnderdeel;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.bron.BronBveAanleverRecordComparator;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzameling;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronBveTerugkoppelMelding;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.PersoonsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.ExamengegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.NT2Vaardigheden;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.ResultaatgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.VakgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.NT2Vaardigheid;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingWerkstuk;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ExamenUitslag;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;

import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "BRON_BVE_AANLEVERMELDINGEN")
public class BronAanleverMelding extends InstellingEntiteit
		implements
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.MeldingEnSleutelgegevensRecord,
		IBronMelding, IBronExamenMelding
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_BronAanlever_deelnemer")
	@AutoForm(include = false)
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verbintenis", nullable = false)
	@Index(name = "idx_BronAanlever_verbintenis")
	private Verbintenis verbintenis;

	@Column(nullable = true)
	private Integer meldingnummer;

	@Column
	private Date ingangsDatum;

	/*
	 * Dit staat ook in de batch maar als de melding in de wachtrij staat is er geen batch
	 * aangekoppeld
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BronOnderwijssoort bronOnderwijssoort;

	@Column(length = 15, nullable = true)
	@AutoForm(label = "Deelnemernummer")
	private String leerlingnummer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch", nullable = true)
	@Index(name = "idx_BronAanleverMelding_batch")
	@AutoForm(include = false)
	private BronBatchBVE batch;

	@Column(length = 9, nullable = true)
	@AutoForm(label = "BSN")
	private String sofinummer;

	@Column(length = 9, nullable = true)
	private String onderwijsnummer;

	@Column(nullable = true)
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	private Datum geboortedatum;

	@Enumerated(EnumType.STRING)
	private Geslacht geslacht;

	@Column(length = 6, nullable = true, name = "postcode")
	private String postcodeVolgensInstelling;

	@Column(length = 4, nullable = true)
	private String land;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	@Lob
	private String reden;

	@OneToMany(mappedBy = "melding", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@AutoForm(include = false)
	@OrderBy("recordNummer asc")
	private List<BronBveAanleverRecord> meldingen = new ArrayList<BronBveAanleverRecord>();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "terugkoppelmelding")
	@Index(name = "idx_BRON_BVE_AAN_terugkoppelm")
	private BronBveTerugkoppelMelding terugkoppelmelding;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BronMeldingStatus bronMeldingStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "examenverzameling")
	@Index(name = "idx_BRON_BVE_AAN_examenverz")
	private BronExamenverzameling examenverzameling;

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
	 * Vlag voor het bijhouden of het object verwijderd is uit de database.
	 */
	@Transient
	@AutoForm(include = false)
	private boolean verwijderd;

	/**
	 * Geeft aan of deze mutatie bekostigingsRelevante info bevat. Dit wordt gebruikt om
	 * te bepalen of het om accountantsmutatie gaat. Afhankelijk van de ingangsdatum van
	 * deze melding, en de bronschooljaarstatus.
	 */
	@Column(nullable = false)
	private boolean bekostigingsRelevant = false;

	public BronAanleverMelding()
	{
	}

	public BronAanleverMelding(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	@Override
	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	@Override
	public Integer getMeldingnummer()
	{
		return meldingnummer;
	}

	public void setMeldingnummer(Integer meldingnummer)
	{
		this.meldingnummer = meldingnummer;
	}

	@Override
	public BronBatchBVE getBatch()
	{
		return batch;
	}

	public void setBatch(BronBatchBVE batch)
	{
		this.batch = batch;
	}

	@Override
	public Datum getGeboortedatum()
	{
		return geboortedatum;
	}

	@Override
	public Geslacht getGeslacht()
	{
		return geslacht;
	}

	@Override
	public String getLand()
	{
		return land;
	}

	@Override
	public String getLeerlingnummer()
	{
		return leerlingnummer;
	}

	@Override
	public String getOnderwijsnummer()
	{
		return onderwijsnummer;
	}

	@Override
	public String getPostcodeVolgensInstelling()
	{
		return postcodeVolgensInstelling;
	}

	@Override
	public String getSofinummer()
	{
		return sofinummer;
	}

	@Override
	public List<BronBveAanleverRecord> getMeldingen()
	{
		return meldingen;
	}

	/**
	 * DEZE METHODE IS GEEN ONDERDEEL VAN DE API. GEBRUIK HEM NIET! Alleen toegevoegd ivm
	 * EnhancedHibernateModel.
	 * 
	 * @param meldingen
	 */
	public void setMeldingen(List<BronBveAanleverRecord> meldingen)
	{
		this.meldingen = meldingen;
	}

	public void setSofinummer(String sofinummer)
	{
		this.sofinummer = sofinummer;
	}

	public void setOnderwijsnummer(String onderwijsnummer)
	{
		this.onderwijsnummer = onderwijsnummer;
	}

	public void setLeerlingnummer(String leerlingnummer)
	{
		this.leerlingnummer = leerlingnummer;
	}

	public void setLand(String land)
	{
		this.land = land;
	}

	public void setPostcodeVolgensInstelling(String postcode)
	{
		String postcodeZonderSpaties = (postcode != null) ? postcode.replaceAll(" ", "") : postcode;
		this.postcodeVolgensInstelling = postcodeZonderSpaties;
	}

	public void setGeslacht(Geslacht geslacht)
	{
		this.geslacht = geslacht;
	}

	public void setGeboortedatum(Datum geboortedatum)
	{
		this.geboortedatum = geboortedatum;
	}

	@Override
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

	public BronBveTerugkoppelMelding getTerugkoppelmelding()
	{
		return terugkoppelmelding;
	}

	public void setTerugkoppelmelding(BronBveTerugkoppelMelding terugkoppelmelding)
	{
		this.terugkoppelmelding = terugkoppelmelding;
	}

	public void setBronOnderwijssoort(BronOnderwijssoort bronOnderwijssoort)
	{
		Asserts.assertNotNull("bronOnderwijssoort", bronOnderwijssoort);
		this.bronOnderwijssoort = bronOnderwijssoort;
	}

	public BronOnderwijssoort getBronOnderwijssoort()
	{
		return bronOnderwijssoort;
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
	public List<BronMeldingOnderdeel> getBronMeldingOnderdelen()
	{
		List<BronMeldingOnderdeel> onderdelen = new ArrayList<BronMeldingOnderdeel>();
		for (BronBveAanleverRecord record : getMeldingen())
		{
			onderdelen.add(record.getBronMeldingOnderdeel());
		}
		return onderdelen;
	}

	@Override
	public BronMeldingStatus getBronMeldingStatus()
	{
		return bronMeldingStatus;
	}

	public void setBronMeldingStatus(BronMeldingStatus bronMeldingStatus)
	{
		this.bronMeldingStatus = bronMeldingStatus;
	}

	@Override
	public String getOnderwijssoort()
	{
		switch (getBronOnderwijssoort())
		{
			case BEROEPSONDERWIJS:
				return "Beroepsonderwijs";
			case EDUCATIE:
				return "Educatie";
			case VAVO:
				return "VAVO";
			case VOORTGEZETONDERWIJS:
				return "Voortgezet Onderwijs";
			default:
				return "Niet bekend";
		}
	}

	@Override
	public List<SoortMutatie> getSoortMutaties()
	{
		List<SoortMutatie> mutaties = new ArrayList<SoortMutatie>();
		for (BronBveAanleverRecord record : getMeldingen())
		{
			mutaties.add(record.getMutatieSoort());
		}
		return mutaties;
	}

	@Override
	public String getBronMeldingOnderdelenOmsch()
	{
		StringBuilder builder = new StringBuilder();
		String komma = "";
		for (BronMeldingOnderdeel onderdeel : getBronMeldingOnderdelen())
		{
			builder.append(komma);
			builder.append(onderdeel.toString());
			komma = ",";
		}
		return builder.toString();
	}

	@Override
	public String getSoortMutatiesOmsch()
	{
		StringBuilder builder = new StringBuilder();
		String komma = "";
		for (SoortMutatie mutatie : getSoortMutaties())
		{
			if (mutatie != null)
			{
				builder.append(komma);
				builder.append(mutatie.getIdentifier());
				komma = ", ";
			}
		}
		return builder.toString();
	}

	public void setExamenverzameling(BronExamenverzameling examenverzameling)
	{
		this.examenverzameling = examenverzameling;
	}

	public BronExamenverzameling getExamenverzameling()
	{
		return examenverzameling;
	}

	private BronBveAanleverRecord getExamenRecord()
	{
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (record.getRecordType() == BRONConstants.BVE_AANLEVERING_VAVO_EXAMENGEGEVENS)
				return record;

		}
		return null;
	}

	@Override
	public int getAantalVakken()
	{
		int aantalVakken = 0;
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (record.getRecordType() == BRONConstants.BVE_AANLEVERING_VAVO_VAKGEGEVENS)
				aantalVakken++;

		}
		return aantalVakken;
	}

	@Override
	public Date getDatumUitslagExamen()
	{
		BronBveAanleverRecord examenRecord = getExamenRecord();
		if (examenRecord != null)
			return examenRecord.getDatumUitslagExamen();
		return null;
	}

	@Override
	public String getExamenCode()
	{
		BronBveAanleverRecord examenRecord = getExamenRecord();
		if (examenRecord != null)
			return examenRecord.getExamen();
		return null;
	}

	@Override
	public ExamenUitslag getUitslagExamen()
	{
		BronBveAanleverRecord examenRecord = getExamenRecord();
		if (examenRecord != null && examenRecord.getUitslagExamen() != null)
			return examenRecord.getUitslagExamen().getVoWaarde();
		return null;
	}

	@Override
	public int getExamenJaar()
	{
		BronBveAanleverRecord examenRecord = getExamenRecord();
		if (examenRecord != null)
			return examenRecord.getExamenjaar();
		return 0;
	}

	@Override
	public BeoordelingWerkstuk getBeoordelingWerkstuk()
	{
		BronBveAanleverRecord examenRecord = getExamenRecord();
		if (examenRecord != null)
			return examenRecord.getBeoordelingWerkstuk();
		return null;
	}

	@Override
	public Integer getCijferWerkstuk()
	{
		BronBveAanleverRecord examenRecord = getExamenRecord();
		if (examenRecord != null && examenRecord.getCijferWerkstuk() != null)
			return Integer.parseInt(examenRecord.getCijferWerkstuk());
		return null;
	}

	@Override
	public String getTitelOfThemaWerkstuk()
	{
		BronBveAanleverRecord examenRecord = getExamenRecord();
		if (examenRecord != null)
			return examenRecord.getTitelThemaWerkstuk();
		return null;
	}

	@Override
	public void saveOrUpdate()
	{
		if (valid())
		{
			super.saveOrUpdate();
		}
	}

	/**
	 * Sanity checks voordat het object opgeslagen wordt in de database. Gooit excepties
	 * als er een invalide toestand geconstateerd is.
	 */
	private boolean valid()
	{
		if (getGeboortedatum() == null)
			throw new RuntimeException(new BronException("Geboortedatum is verplicht"));
		if (getGeslacht() == null)
			throw new RuntimeException(new BronException("Geslacht is verplicht"));
		return true;
	}

	/**
	 * Bepaalt of de melding een reden heeft om naar BRON gestuurd te worden: er moet
	 * minstens &eacute;&eacute;n (of meer) record(s) aanwezig zijn, en als er slechts
	 * &eacute;&eacute;n record bij deze melding geregistreerd is, mag dit niet enkel een
	 * persoonsgegevensrecord zijn (310)&mdash;na een 310 moet een 320, 325 of 330 volgen.
	 */
	public boolean isBronCommuniceerbaar()
	{
		return heeftRecords() && !heeftEnkelPersoonsgegevensRecord() && !isVerwijderd();
	}

	private boolean heeftRecords()
	{
		return !getMeldingen().isEmpty();
	}

	private boolean heeftEnkelPersoonsgegevensRecord()
	{
		return getMeldingen().size() == 1
			&& getMeldingen().get(0).getRecordType() == BRONConstants.BVE_AANLEVERING_PERSOONSGEGEVENS;

	}

	@Override
	public Date getIngangsDatum()
	{
		return ingangsDatum;
	}

	@Override
	public List<IVakMelding> getVakMeldingen()
	{
		List<IVakMelding> vakRecords = new ArrayList<IVakMelding>();
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (record.getRecordType() == BRONConstants.BVE_AANLEVERING_VAVO_VAKGEGEVENS)
				vakRecords.add(record);
		}
		return vakRecords;
	}

	/**
	 * Zet de ingangsdatum van de aanlevermelding,
	 */
	public void setIngangsDatum(Date ingangsDatum)
	{
		if (this.ingangsDatum == null)
		{
			this.ingangsDatum = ingangsDatum;
		}
		else if (ingangsDatum != null && this.ingangsDatum.after(ingangsDatum))
		{
			this.ingangsDatum = ingangsDatum;
		}
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

	public <T> T getRecord(Class<T> class1)
	{
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (class1.isAssignableFrom(record.getActualRecordType()))
			{
				return class1.cast(record);
			}
		}
		return null;
	}

	public PeriodegegevensInschrijvingRecord getPeriodeRecord(Date ingangsdatum)
	{
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (PeriodegegevensInschrijvingRecord.class.isAssignableFrom(record
				.getActualRecordType())
				&& ingangsdatum.equals(record.getDatumIngangPeriodegegevensInschrijving()))
			{
				return PeriodegegevensInschrijvingRecord.class.cast(record);
			}
		}
		return null;
	}

	public BpvGegevensRecord getBpvRecord(int volgnummer)
	{
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (BpvGegevensRecord.class.isAssignableFrom(record.getActualRecordType())
				&& volgnummer == (record.getBpvVolgnummer()))
			{
				return BpvGegevensRecord.class.cast(record);
			}
		}
		return null;
	}

	public ExamengegevensRecord getExamengegevensRecord(String externeCode)
	{
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (ExamengegevensRecord.class.isAssignableFrom(record.getActualRecordType())
				&& externeCode.equals(record.getBehaaldeDeelKwalificatie()))
			{
				return ExamengegevensRecord.class.cast(record);
			}
		}
		return null;
	}

	public ResultaatgegevensRecord getResultaatgegevensRecordED(String externeCode)
	{
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (ResultaatgegevensRecord.class.isAssignableFrom(record.getActualRecordType())
				&& externeCode.equals(record.getVoltooideOpleiding()))
			{
				return ResultaatgegevensRecord.class.cast(record);
			}
		}
		return null;
	}

	public NT2Vaardigheden getNt2VaardigheidRecord(int volgnummer, NT2Vaardigheid vaardigheid)
	{
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (NT2Vaardigheden.class.isAssignableFrom(record.getActualRecordType())
				&& volgnummer == record.getVakvolgnummer()
				&& vaardigheid == record.getNT2Vaardigheid())
			{
				return NT2Vaardigheden.class.cast(record);
			}
		}
		return null;
	}

	public VakgegevensRecord getVakgegevensRecordED(int volgnummer)
	{
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (VakgegevensRecord.class.isAssignableFrom(record.getActualRecordType())
				&& volgnummer == record.getVakvolgnummer())
			{
				return VakgegevensRecord.class.cast(record);
			}
		}
		return null;
	}

	@Override
	public boolean isVerwijderd()
	{
		return verwijderd;
	}

	@Override
	public void setVerwijderd(boolean verwijderd)
	{
		this.verwijderd = verwijderd;
	}

	public void setBekostigingsRelevant(boolean bekostigingsRelevant)
	{
		this.bekostigingsRelevant = bekostigingsRelevant;
	}

	@Override
	public boolean isBekostigingsRelevant()
	{
		return bekostigingsRelevant;
	}

	@Override
	public boolean bevatAlleenToevoegingen()
	{
		for (SoortMutatie soortMutatie : getSoortMutaties())
		{
			if (SoortMutatie.Aanpassing.equals(soortMutatie)
				|| SoortMutatie.Uitschrijving.equals(soortMutatie)
				|| SoortMutatie.Verwijdering.equals(soortMutatie))
				return false;
		}
		return true;
	}

	@Override
	public boolean bevatSofiOfOnderwijsNummer()
	{
		return getSofinummer() != null || getOnderwijsnummer() != null;
	}

	/**
	 * Voegt de melding met zijn records opnieuw toe aan de wachtrij voor versturing naar
	 * BRON. Onkoppelt de melding van terugkoppelingen en batches.
	 */
	public BronAanleverMelding voegOpnieuwToeAanWachtrij()
	{
		if (getExamenverzameling() == null)
		{
			BronAanleverMelding meldingInWachtrij = findMeldingInWachtrij();
			if (meldingInWachtrij == null
				|| bevatTypeMelding(BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS))
			{
				// er staat niets in de wachtrij te wachten, dus kan de foutieve melding
				// gewoon kopieren.
				return kopieerMeldingEnRecords();
			}
			else
			{
				// er staat wel iets in de wachtrij, dus we moeten de afgekeurde melding
				// consolideren met de gegevens in de wachtrij.
				return consolideerMeldingMet(meldingInWachtrij);
			}
		}
		else
		{
			// Examendeelnames voor VAVO komen niet direct in de wachtrij, maar moeten via
			// een volgende examenbatch opnieuw gegenereerd worden. Daarom de
			// examendeelname aanmerken als meenemenInVolgendeBronBatch

			for (BronBveAanleverRecord record : getMeldingen())
			{
				int type = record.getRecordType();
				if (type == BVE_AANLEVERING_VAVO_EXAMENGEGEVENS
					|| type == BVE_AANLEVERING_VAVO_VAKGEGEVENS)
				{
					String code = record.getExamen();
					Integer examenjaar = record.getExamenjaar();
					List<Examendeelname> examendeelnames =
						record.getVerbintenis().getExamendeelnames();
					for (Examendeelname examen : examendeelnames)
					{
						if (examen.getVerbintenis().getExterneCode().equals(code)
							&& examen.getExamenjaar() == examenjaar)
						{
							examen.setGewijzigd(true);
							examen.saveOrUpdate();
						}
					}
				}
			}
		}
		return null;
	}

	private BronAanleverMelding findMeldingInWachtrij()
	{
		BronMeldingZoekFilter filter = new BronMeldingZoekFilter();
		filter.setVerbintenis(verbintenis);
		filter.setDeelnemer(verbintenis.getDeelnemer());
		filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		filter.setBronMeldingOnderdeelNot(BronMeldingOnderdeel.Sleutel);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));

		BronDataAccessHelper wachtrij = DataAccessRegistry.getHelper(BronDataAccessHelper.class);
		List<BronAanleverMelding> meldingenInWachtrij = wachtrij.getBronBveMeldingen(filter);

		if (meldingenInWachtrij.isEmpty())
		{
			return null;
		}
		return meldingenInWachtrij.get(0);
	}

	private BronAanleverMelding kopieerMeldingEnRecords()
	{
		HibernateObjectCopyManager copymanager =
			new HibernateObjectCopyManager(BronAanleverMelding.class, BronBveAanleverRecord.class);

		BronAanleverMelding copy = copymanager.copyObject(this);
		copy.setId(null);
		copy.setBatch(null);
		copy.setBronMeldingStatus(BronMeldingStatus.WACHTRIJ);
		copy.setTerugkoppelmelding(null);
		copy.setExamenverzameling(null);
		copy.vul();
		copy.addReden("Afkeuring door BRON van melding " + getMeldingnummer() + " in batch "
			+ getBatchnummer());
		int index = 0;
		while (index < copy.getMeldingen().size())
		{
			BronBveAanleverRecord record = copy.getMeldingen().get(index);
			record.setId(null);
			record.setTerugkoppelrecord(null);
			record.ververs(false);
			index++;
		}
		copy.consolideerRecords();
		// tijdens consolideren kan een opsla aktie gebeuren
		copy.saveOrUpdate();
		return copy;
	}

	/**
	 * Onderstaande tabel geeft aan welke toestanden van meldingen in de wachtrij mogelijk
	 * zijn na een afkeuring door BRON en op welke manier er dan geconsolideerd moet
	 * worden. Een T is een toevoeging, een A is een aanpassing een V is een verwijdering,
	 * een - betekent 'geen melding'.
	 * 
	 * <pre>
	 *    afkeuring TAV TTAAVV TTTAAAVVV TTTTTTTTT
	 *    naar bron --- AVAVTA --------- TTTAAAVVV
	 *     wachtrij --- ------ TAVATVATV TAVTAVTAV
	 *              ==============================
	 * consolidatie TAV T-A--- TT-ATVAAV TT-TT-TTV
	 * </pre>
	 */
	private BronAanleverMelding consolideerMeldingMet(BronAanleverMelding meldingInWachtrij)
	{
		HibernateObjectCopyManager copymanager =
			new HibernateObjectCopyManager(BronAanleverMelding.class, BronBveAanleverRecord.class);

		List<BronBveAanleverRecord> afgekeurdeRecords = new ArrayList<BronBveAanleverRecord>();
		for (BronBveAanleverRecord record : getMeldingen())
		{
			BronBveAanleverRecord copy = copymanager.copyObject(record);
			copy.setTerugkoppelrecord(null);
			afgekeurdeRecords.add(copy);
		}

		// ververs de gegevens van het 305 record
		meldingInWachtrij.vul();

		meldingInWachtrij.addReden("Afkeuring door BRON van melding " + getMeldingnummer()
			+ " in batch " + getBatchnummer());

		for (BronBveAanleverRecord afgekeurdRecord : afgekeurdeRecords)
		{
			BronBveAanleverRecord recordInWachtrij =
				meldingInWachtrij.getRecord(afgekeurdRecord.getIdentifier());
			if (recordInWachtrij == null)
			{
				// voeg kopie toe aan wachtrij
				afgekeurdRecord.setMelding(meldingInWachtrij);
				meldingInWachtrij.getMeldingen().add(afgekeurdRecord);
			}
			else
			{
				recordInWachtrij.ververs(true);

				switch (afgekeurdRecord.getSoortMutatie())
				{
					case Toevoeging:
						bepaalMutatieSoortNaAfkeuringToevoeging(meldingInWachtrij, recordInWachtrij);
						break;
					case Aanpassing:
						bepaalMutatieSoortNaAfkeuringAanpassing(meldingInWachtrij, recordInWachtrij);
						break;
					case Verwijdering:
						bepaalMutatieSoortNaAfkeuringVerwijdering(meldingInWachtrij,
							recordInWachtrij);
						break;
				}
			}
		}
		// nu we de melding hebben aangepast moeten de records opnieuw geordend worden en
		// eventueel de melding verwijderd indien deze overbodig geworden is.
		meldingInWachtrij.consolideerRecords();

		return meldingInWachtrij;
	}

	private String getBatchnummer()
	{
		return getBatch() == null ? "n/a" : getBatch().getBatchNummerAsString();
	}

	private void bepaalMutatieSoortNaAfkeuringToevoeging(BronAanleverMelding meldingInWachtrij,
			BronBveAanleverRecord recordInWachtrij)
	{
		switch (recordInWachtrij.getSoortMutatie())
		{
			case Toevoeging:
				// hoeft niets veranderd te worden
				recordInWachtrij.saveOrUpdate();
				break;
			case Aanpassing:
				// Als het record in de wachtrij een aanpassing is,
				// terwijl de toevoeging afgekeurd is, dan is het record
				// nog steeds niet bekend binnen BRON, en moet de mutatie
				// een toevoeging worden.
				recordInWachtrij.setSoortMutatie(Toevoeging);
				recordInWachtrij.saveOrUpdate();
				break;
			case Verwijdering:
				// als het record uit bron verwijderd moet worden terwijl
				// de toevoeging afgekeurd is, stuur dan het record
				// helemaal niet meer naar BRON.
				meldingInWachtrij.getMeldingen().remove(recordInWachtrij);
				recordInWachtrij.setMelding(null);
				recordInWachtrij.delete();
				break;
		}
	}

	@SuppressWarnings("unused")
	private void bepaalMutatieSoortNaAfkeuringAanpassing(BronAanleverMelding meldingInWachtrij,
			BronBveAanleverRecord recordInWachtrij)
	{
		switch (recordInWachtrij.getSoortMutatie())
		{
			case Toevoeging:
				// hoeft niets veranderd te worden
				recordInWachtrij.saveOrUpdate();
				break;
			case Aanpassing:
				// hoeft niets veranderd te worden
				recordInWachtrij.saveOrUpdate();
				break;
			case Verwijdering:
				// hoeft niets veranderd te worden
				recordInWachtrij.saveOrUpdate();
				break;
		}
	}

	@SuppressWarnings("unused")
	private void bepaalMutatieSoortNaAfkeuringVerwijdering(BronAanleverMelding meldingInWachtrij,
			BronBveAanleverRecord recordInWachtrij)
	{
		switch (recordInWachtrij.getSoortMutatie())
		{
			case Toevoeging:
				// Als het record in de wachtrij een toevoeging is,
				// terwijl de verwijdering afgekeurd is, dan is het record
				// nog steeds bekend binnen BRON, en moet de mutatie een
				// aanpassing worden.
				recordInWachtrij.setSoortMutatie(Aanpassing);
				recordInWachtrij.saveOrUpdate();
				break;
			case Aanpassing:
				// hoeft niets veranderd te worden
				recordInWachtrij.saveOrUpdate();
				break;
			case Verwijdering:
				// hoeft niets veranderd te worden
				recordInWachtrij.saveOrUpdate();
				break;
		}
	}

	/**
	 * Haalt het record op op basis van de identifier (zoals gespecificeerd door BRON)
	 */
	private BronBveAanleverRecord getRecord(String identifier)
	{
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (record.getIdentifier().equals(identifier))
				return record;
		}
		return null;
	}

	/**
	 * Loopt de records van deze melding langs om te zien of ze nog relevant zijn en
	 * wijzigt of verwijdert records en eventueel de melding zelf. Een 310
	 * (persoonsgegevens) record is namelijk niet nodig wanneer de deelnemer een
	 * sofinummer of onderwijsnummer heeft. De melding zelf is niet meer relevant als er
	 * geen records meer zijn, of slechts enkel een 310 record.
	 */
	public void consolideerRecords()
	{
		// de melding alleen opslaan als deze ook daadwerkelijk records bevat
		// als de melding geen onderwijssoort heeft, dan zijn er alleen
		// personalia-gegevens opgenomen en hoeft de melding niet naar BRON
		if (!isBronCommuniceerbaar() && isSaved() && !isVerwijderd())
		{
			delete();
		}
		else if (isBronCommuniceerbaar() && getVerbintenis() != null)
		{
			// verwijder records indien de melding een verwijderrecord bevat
			if (verwijdertInschrijving())
			{
				BronBveAanleverRecord inschrijvingsRecord = null;
				for (BronBveAanleverRecord record : getMeldingen())
				{
					if (record.isInschrijvingsrecord())
					{
						inschrijvingsRecord = record;
					}
					else
					{
						record.delete();
					}
				}
				getMeldingen().retainAll(Arrays.asList(inschrijvingsRecord));
			}
			PersoonsgegevensRecord persoonsgegevens = getRecord(PersoonsgegevensRecord.class);
			if (persoonsgegevens == null && getSofinummer() == null && getOnderwijsnummer() == null
				&& voegtInschrijvingToe())
			{
				BronBveAanleverRecord record =
					(BronBveAanleverRecord) BronBveAanleverRecord.newPersoonsgegevensRecord(this);
				record.vulPersoonsgegevensRecord();

				// controleer de melding nog een keer om te zien of deze echt nog wel
				// naar bron moet.
				consolideerRecords();
			}
			else if (persoonsgegevens != null
				&& (getOnderwijsnummer() != null || getSofinummer() != null))
			{
				BronBveAanleverRecord record = (BronBveAanleverRecord) persoonsgegevens;
				getMeldingen().remove(record);
				record.setMelding(null);
				record.delete();

				// controleer de melding nog een keer om te zien of deze echt nog wel
				// naar bron moet.
				consolideerRecords();
			}
			else
			{
				// Sorteer de records van de melding in de juiste volgorde, zodat ze
				// ook correct aan bron geleverd worden.
				Collections.sort(getMeldingen(), new BronBveAanleverRecordComparator());
				saveOrUpdate();
			}
		}
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

	public void vul()
	{
		BronEduArteModel model = new BronEduArteModel();

		setLeerlingnummer(model.getLeerlingnummer(deelnemer));
		setSofinummer(model.getSofinummer(deelnemer));
		if (StringUtil.isEmpty(getSofinummer()))
		{
			setOnderwijsnummer(model.getOnderwijsnummer(deelnemer));
		}
		setGeboortedatum(model.getGeboortedatum(deelnemer));
		setGeslacht(model.getGeslacht(deelnemer));

		String postcode = model.getPostcode(deelnemer);
		if (model.woontInNederland(deelnemer))
		{
			if (StringUtil.isNotEmpty(postcode))
			{
				setPostcodeVolgensInstelling(postcode);
			}
			else
			{
				// postcode onbekend, maar wel woonachtig in NL
				setLand(model.getLand(deelnemer));
			}
		}
		else
		{
			setLand(model.getLand(deelnemer));
		}
	}

	public <T> T getExamengegevensRecord(Class<T> class1, Examendeelname deelname)
	{
		List<BronBveAanleverRecord> records = getMeldingen();
		for (BronBveAanleverRecord record : records)
		{
			if (record.getActualRecordType().isAssignableFrom(class1)
				&& deelname.equals(record.getExamenDeelname()))
			{
				return class1.cast(record);
			}
		}
		return null;
	}

	public nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.VakgegevensRecord getVakgegevensRecord(
			Class<nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.VakgegevensRecord> class1,
			OnderwijsproductAfnameContext context)
	{
		List<BronBveAanleverRecord> records = getMeldingen();
		for (BronBveAanleverRecord record : records)
		{
			if (record.getActualRecordType().isAssignableFrom(class1)
				&& context.equals(record.getAfnameContext()))
			{
				return class1.cast(record);
			}
		}
		return null;
	}

	public boolean bevatTypeMelding(int type)
	{
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (record.getRecordType() == type)
				return true;
		}
		return false;
	}

	public boolean bevatPersonaliaMelding()
	{
		return bevatTypeMelding(BVE_AANLEVERING_SLEUTELGEGEVENS)
			|| bevatTypeMelding(BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS)
			|| bevatTypeMelding(BVE_AANLEVERING_PERSOONSGEGEVENS);
	}

	public boolean bevatInschrijvingMelding()
	{
		return bevatTypeMelding(BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS)
			|| bevatTypeMelding(BVE_AANLEVERING_BO_PERIODEGEGEVENS)
			|| bevatTypeMelding(BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS)
			|| bevatTypeMelding(BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS);
	}

	public boolean bevatBPVMelding()
	{
		return bevatTypeMelding(BVE_AANLEVERING_BO_BPVGEGEVENS);
	}

	public Integer getBPVVolgnummer()
	{
		if (bevatBPVMelding())
		{
			for (BronBveAanleverRecord record : getMeldingen())
			{
				if (record.getRecordType() == BVE_AANLEVERING_BO_BPVGEGEVENS)
					return record.getBpvVolgnummer();
			}
		}

		return null;
	}

	public boolean verwijdertInschrijving()
	{
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (record.isInschrijvingsrecord())
			{
				return record.getSoortMutatie() == Verwijdering;
			}
		}
		return false;
	}

	public boolean voegtInschrijvingToe()
	{
		for (BronBveAanleverRecord record : getMeldingen())
		{
			if (record.isInschrijvingsrecord())
			{
				return record.getSoortMutatie() == Toevoeging;
			}
		}
		return false;
	}

	/**
	 * Bepaalt of de melding in aanmerking komt voor verdichting van de records. Zo is het
	 * bijvoorbeeld niet wenselijk om verwijderingen van verbintenissen die in de wachtrij
	 * staan te verdichten met andere mutaties. Een verwijdering gevolgd door een
	 * toevoeging dient dus een verwijdering gevolgd door een toevoeging te blijven.
	 */
	public boolean isVerdichtbaar()
	{
		return !verwijdertInschrijving();
	}
}
