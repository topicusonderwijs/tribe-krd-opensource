package nl.topicus.eduarte.krd.bron.jobs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.validation.ElfProef;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.krd.dao.helpers.BronFotoRecordDataAccessHelper;
import nl.topicus.eduarte.krd.entities.BronFotobestandInlezenJobRun;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestandVerschil;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestandVerschil.FotoVerschil;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.*;
import nl.topicus.eduarte.krd.zoekfilters.BronFotoRecordZoekFilter;
import nl.topicus.onderwijs.duo.bron.bve.foto.pve_9_9.BronFotoRecordType;
import nl.topicus.onderwijs.duo.bron.bve.foto.pve_9_9.BronFotoType;

public class BronFotoVergelijker<T extends BronFotoRecord>
{
	private int aantalVerschillen = 0;

	private BronEduArteModel model = new BronEduArteModel();

	public int vergelijkFotoMetDatabase(BronFotobestandInlezenJob job, BronFotobestand fotobestand,
			BronFotobestandInlezenJobRun jobrun, Class<T> inschrijvingRecordClass)
			throws InterruptedException
	{
		BronFotoRecordDataAccessHelper helper =
			DataAccessRegistry.getHelper(BronFotoRecordDataAccessHelper.class);

		List<Long> verbintenisIds =
			DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class)
				.getBRONFotoVerbintenissen(fotobestand.getType(), fotobestand.getPeildatum());
		Set<Long> verbintenisInEAMaarNietInFoto = new HashSet<Long>(verbintenisIds);
		BronFotoRecordZoekFilter recordFilter =
			new BronFotoRecordZoekFilter(inschrijvingRecordClass, fotobestand);
		List<Long> fotorecordIds = helper.listIds(recordFilter);

		int counter = fotorecordIds.size();
		for (Long recordId : fotorecordIds)
		{
			IBronFotoInschrijvingRecord record =
				(IBronFotoInschrijvingRecord) helper.get(inschrijvingRecordClass, recordId);
			// Zoek naar het bijbehorende verbintenisrecord in de database.
			Deelnemer deelnemer = getDeelnemer(record, fotobestand, jobrun);
			if (deelnemer != null)
			{
				// Haal de betreffende verbintenis op.
				Verbintenis verbintenis = getVerbintenis(record, deelnemer, fotobestand);
				if (verbintenis != null)
				{
					// Verbintenis gevonden.
					verbintenisInEAMaarNietInFoto.remove(verbintenis.getId());
					// Vergelijk verbintenis met fotorecord.
					vergelijkVerbintenisMetFotoRecord(record, verbintenis);
				}
			}
			counter++;
			job.setProgress(counter, fotorecordIds.size() * 2);
			if (counter % 100 == 0)
			{
				job.flushAndClearHibernate();
			}
		}
		return aantalVerschillen;
	}

	private void vergelijkVerbintenisMetFotoRecord(IBronFotoInschrijvingRecord record,
			Verbintenis verbintenis)
	{
		TimeUtil timeutil = TimeUtil.getInstance();
		if (!timeutil.datesEqual(record.getDatumInschrijving(), verbintenis.getBegindatum()))
		{
			createVerschil(record, FotoVerschil.BegindataKomenNietOvereen, verbintenis);
		}
		if (record.getGeplandeUitschrijfdatum() != null
			&& !timeutil.datesEqual(record.getGeplandeUitschrijfdatum(), verbintenis
				.getGeplandeEinddatum()))
		{
			createVerschil(record, FotoVerschil.VerwachteEinddataKomenNietOvereen, verbintenis);
		}
		if (!timeutil.datesEqual(record.getWerkelijkeUitschrijfdatum(), verbintenis.getEinddatum()))
		{
			createVerschil(record, FotoVerschil.WerkelijkeEinddataKomenNietOvereen, verbintenis);
		}
		String opleidingBron = StringUtil.verwijderVoorloopnullen(record.getCodeOpleiding());
		String opleidingEduarte = StringUtil.verwijderVoorloopnullen(verbintenis.getExterneCode());
		if (!StringUtil.equalOrBothEmpty(opleidingBron, opleidingEduarte))
		{
			createVerschil(record, FotoVerschil.OpleidingenKomenNietOvereen, verbintenis);
		}
		boolean bekostigdInFotoOpTeldatum =
			record.isIndicatieBekostigingInschrijvingOpTeldatumInRecord();
		boolean bekostigdInDBOpTeldatum = verbintenis.isBekostigdOpDatum(record.getTeldatum());
		if (bekostigdInDBOpTeldatum != bekostigdInFotoOpTeldatum)
		{
			createVerschil(record, FotoVerschil.BekostigingOpTeldatumKomenNietOvereen, verbintenis);
		}
		Date feb1 = timeutil.getNextOccurrenceOfDate(record.getTeldatum(), 1, 1);
		boolean bekostigdInFotoOp1Feb = record.isIndicatieBekostigingInschrijvingOp1Februari();
		boolean bekostigdInDBOp1Feb = verbintenis.isBekostigdOpDatum(feb1);
		if (bekostigdInFotoOp1Feb != bekostigdInDBOp1Feb)
		{
			createVerschil(record, FotoVerschil.BekostigingOp1FebKomenNietOvereen, verbintenis);
		}
		if (record.getIndicatieGehandicapt() != null)
		{
			if (record.getIndicatieGehandicapt().booleanValue() != verbintenis
				.getIndicatieGehandicapt())
			{
				createVerschil(record, FotoVerschil.IndicatieGehandicaptKomenNietOvereen,
					verbintenis);
			}
		}
		if (record.getHoogsteVooropleiding() != null
			&& record.getHoogsteVooropleiding() != model.getHoogsteVooropleiding(verbintenis))
		{
			createVerschil(record, FotoVerschil.HoogsteVooropleidingKomenNietOvereen, verbintenis);
		}
		if (record.getLeerweg() != null && record.getLeerweg() != model.getLeerweg(verbintenis))
		{
			createVerschil(record, FotoVerschil.LeerwegKomenNietOvereen, verbintenis);
		}
		if (record.getIntensiteit() != null
			&& record.getIntensiteit() != model.getIntensiteit(verbintenis))
		{
			createVerschil(record, FotoVerschil.IntensiteitKomenNietOvereen, verbintenis);
		}
		if (record.getRedenUitstroom() != null
			&& !StringUtil.equalOrBothEmpty(record.getRedenUitstroom(), model
				.getRedenUitstroom(verbintenis)))
		{
			createVerschil(record, FotoVerschil.RedenUitstroomKomenNietOvereen, verbintenis);
		}
		// Haal het onderwijsvragendenrecord op
		BronFotobestand bestand = record.getBestand();
		Long pgn = record.getPgn();
		if (pgn != null)
		{
			BronFotoRecordDataAccessHelper helper =
				DataAccessRegistry.getHelper(BronFotoRecordDataAccessHelper.class);
			IBronFotoOnderwijsontvangendeRecord ooRecord =
				helper.getOnderwijsontvangendeRecord(bestand, pgn, record
					.getOnderwijsontvangendeRecordClass());
			if (ooRecord == null)
			{
				createVerschil(record, FotoVerschil.OnderwijsontvangendeRecordNietGevondenInFoto,
					verbintenis);
			}
			else
			{
				vergelijkOnderwijsontvangende(record, ooRecord, verbintenis);
			}
		}
		// Vergelijk BPV's en diploma's.
		if (bestand.getType() == BronFotoType.BO
			&& record.getRecordtype() == BronFotoRecordType.ISG)
		{
			vergelijkBPVs((BronFotoBOInschrijvingRecord) record, verbintenis);
			vergelijkDiplomas((BronFotoBOInschrijvingRecord) record, verbintenis);
		}

		// Vergelijk plaatsingen
		if (bestand.getType() == BronFotoType.VO
			&& record.getRecordtype() == BronFotoRecordType.ISG)
		{
			for (BronFotoVOOpleidingRecord opleidingRecord : ((BronFotoVOInschrijvingRecord) record)
				.getOpleidingRecords())
			{
				Plaatsing plaatsing =
					verbintenis.getPlaatsingOpDatum(opleidingRecord.getTeldatum());
				if (plaatsing != null)
				{
					if (!opleidingRecord.getLeerjaar().equals(plaatsing.getLeerjaar()))
						createVerschil(record, FotoVerschil.LeerjaarKomtNietOvereen, verbintenis);

					Integer recordJaren = opleidingRecord.getAantalJarenPraktijkonderwijs();
					Integer plaatsingJaren = plaatsing.getJarenPraktijkonderwijs();

					if (recordJaren != null && plaatsingJaren != null
						&& !recordJaren.equals(plaatsingJaren))
						createVerschil(record, FotoVerschil.PraktijkjaarKomtNietOvereen,
							verbintenis);
				}
			}
		}
	}

	private void vergelijkOnderwijsontvangende(IBronFotoInschrijvingRecord inschrijvingRecord,
			IBronFotoOnderwijsontvangendeRecord record, Verbintenis verbintenis)
	{
		if (record.getGeslacht() != model.getGeslacht(verbintenis.getDeelnemer()))
		{
			createOOVerschil(inschrijvingRecord, record, FotoVerschil.Geslacht, verbintenis);
		}
		if (record.getGeboortedatum() != null)
		{
			if (TimeUtil.getInstance().datesEqual(record.getGeboortedatum(),
				verbintenis.getDeelnemer().getPersoon().getGeboortedatum()))
			{
				createOOVerschil(inschrijvingRecord, record, FotoVerschil.Geboortedatum,
					verbintenis);
			}
		}
		if (record.getGeboorteJaarEnMaand() != null)
		{
			if (verbintenis.getDeelnemer().getPersoon().getGeboortedatum() != null)
			{
				int jaar =
					TimeUtil.getInstance().getYear(
						verbintenis.getDeelnemer().getPersoon().getGeboortedatum());
				int maand =
					TimeUtil.getInstance().getMonth(
						verbintenis.getDeelnemer().getPersoon().getGeboortedatum()) + 1;
				String geboorteJaarEnMaandInDb =
					String.valueOf(jaar)
						+ StringUtil.voegVoorloopnullenToe(String.valueOf(maand), 2);
				if (!record.getGeboorteJaarEnMaand().equals(geboorteJaarEnMaandInDb))
				{
					createOOVerschil(inschrijvingRecord, record, FotoVerschil.GeboorteJaarEnMaand,
						verbintenis);
				}
			}
		}
		Adres woonadres = model.getWoonadres(verbintenis.getDeelnemer());
		String postcodeCijfers = null;
		if (woonadres != null)
		{
			if (woonadres.getLand().getCode().equals("5010"))
				postcodeCijfers = "0010";
			else if (woonadres.getLand().getCode().equals("9089"))
				postcodeCijfers = "0020";
			else if (woonadres.getLand().equals(Land.getNederland()))
				postcodeCijfers = woonadres.getPostcode().substring(0, 4);
			else
				postcodeCijfers = "0030";
		}
		else
		{
			postcodeCijfers = "0040";
		}
		if (!StringUtil.equalOrBothNull(record.getPostcodecijfers(), postcodeCijfers))
		{
			createOOVerschil(inschrijvingRecord, record, FotoVerschil.Postcodecijfers, verbintenis);
		}
		if (!TimeUtil.getInstance().datesEqual(record.getOverlijdensdatum(),
			verbintenis.getDeelnemer().getPersoon().getDatumOverlijden()))
		{
			createOOVerschil(inschrijvingRecord, record, FotoVerschil.Overlijdensdatum, verbintenis);
		}

		// public Date getDatumVestigingInNederland();
		//
		// public Date getDatumVertrekUitNederland();
		//
		// public String getCodeGeboorteland();
		//
		// public String getCodeGeboortelandOuder1();
		//
		// public Geslacht getGeslachtOuder1();
		//
		// public String getCodeGeboortelandOuder2();
		//
		// public Geslacht getGeslachtOuder2();
		//
		// public String getCodeLandWaarnaarVertrokken();
		//
		// public String getCodeVerblijfstitel();
		//
		// public String getCodeNationaliteit1();
		//
		// public String getCodeNationaliteit2();
		//
		// public Integer getLeeftijdOpMeetdatum1();
		//
		// public Date getLeeftijdmeetdatum1();
		//
		// public Integer getLeeftijdOpMeetdatum2();
		//
		// public Date getLeeftijdmeetdatum2();
		//
		// public Integer getLeeftijdOpMeetdatum3();
		//
		// public Date getLeeftijdmeetdatum3();
		//
		// public Integer getLeeftijdOpMeetdatum4();
		//
		// public Date getLeeftijdmeetdatum4();
		//
		// public Integer getLeeftijdOpMeetdatum5();
		//
		// public Date getLeeftijdmeetdatum5();
	}

	private void vergelijkBPVs(BronFotoBOInschrijvingRecord inschrijvingRecord,
			Verbintenis verbintenis)
	{
		List<BPVInschrijving> bpvInschrijvingen = new ArrayList<BPVInschrijving>();
		for (BPVInschrijving bpvInschrijving : verbintenis.getBpvInschrijvingen())
		{
			if (bpvInschrijving.getStatus().isBronCommuniceerbaar())
				bpvInschrijvingen.add(bpvInschrijving);
		}
		for (BronFotoBOBPVRecord bpvRecord : inschrijvingRecord.getBpvRecords())
		{
			BPVInschrijving bpv =
				getAndRemoveBPVInschrijving(bpvRecord.getBpvVolgnummer().intValue(),
					bpvInschrijvingen);
			if (bpv == null)
			{
				createBPVVerschil(inschrijvingRecord, bpvRecord, FotoVerschil.BpvNietGevonden,
					verbintenis, bpv);
			}
			else
			{
				vergelijkBPV(bpvRecord, bpv);
			}
		}
		// Wat over is in de oorspronkelijke lijst werd niet in de foto gevonden.
		for (BPVInschrijving bpv : bpvInschrijvingen)
		{

			createBPVVerschil(inschrijvingRecord, null, FotoVerschil.BpvNietGevondenInBron,
				verbintenis, bpv);
		}
	}

	private void vergelijkBPV(BronFotoBOBPVRecord bpvRecord, BPVInschrijving bpvInschrijving)
	{
		if (!TimeUtil.getInstance().datesEqual(bpvRecord.getAfsluitdatumBPV(),
			bpvInschrijving.getAfsluitdatum()))
		{
			createBPVVerschil(bpvRecord.getInschrijvingRecord(), bpvRecord,
				FotoVerschil.BpvAfsluitdatum, bpvInschrijving.getVerbintenis(), bpvInschrijving);
		}
		if (!TimeUtil.getInstance().datesEqual(bpvRecord.getBegindatumBPV(),
			bpvInschrijving.getBegindatum()))
		{
			createBPVVerschil(bpvRecord.getInschrijvingRecord(), bpvRecord,
				FotoVerschil.BpvBegindatum, bpvInschrijving.getVerbintenis(), bpvInschrijving);
		}
		if (!TimeUtil.getInstance().datesEqual(bpvRecord.getGeplandeEinddatumBPV(),
			bpvInschrijving.getVerwachteEinddatum()))
		{
			createBPVVerschil(bpvRecord.getInschrijvingRecord(), bpvRecord,
				FotoVerschil.BpvGeplandeEinddatum, bpvInschrijving.getVerbintenis(),
				bpvInschrijving);
		}
		if (!TimeUtil.getInstance().datesEqual(bpvRecord.getWerkelijkeEinddatumBPV(),
			bpvInschrijving.getEinddatum()))
		{
			createBPVVerschil(bpvRecord.getInschrijvingRecord(), bpvRecord,
				FotoVerschil.BpvEinddatum, bpvInschrijving.getVerbintenis(), bpvInschrijving);
		}
		if (!equal(bpvRecord.getOmvangBPV(), bpvInschrijving.getTotaleOmvang()))
		{
			createBPVVerschil(bpvRecord.getInschrijvingRecord(), bpvRecord, FotoVerschil.BpvOmvang,
				bpvInschrijving.getVerbintenis(), bpvInschrijving);
		}
		if (!StringUtil.equalOrBothEmpty(bpvRecord.getCodeLeerbedrijf(), model
			.getLeerbedrijf(bpvInschrijving)))
		{
			createBPVVerschil(bpvRecord.getInschrijvingRecord(), bpvRecord,
				FotoVerschil.BpvCodeLeerbedrijf, bpvInschrijving.getVerbintenis(), bpvInschrijving);
		}
	}

	private BPVInschrijving getAndRemoveBPVInschrijving(int volgnummer,
			List<BPVInschrijving> bpvInschrijvingen)
	{
		for (BPVInschrijving bpv : bpvInschrijvingen)
		{
			if (bpv.getVolgnummer() == volgnummer)
			{
				bpvInschrijvingen.remove(bpv);
				return bpv;
			}
		}
		return null;
	}

	private void vergelijkDiplomas(BronFotoBOInschrijvingRecord inschrijvingRecord,
			Verbintenis verbintenis)
	{
		List<Examendeelname> examendeelnames =
			new ArrayList<Examendeelname>(verbintenis.getExamendeelnames());
		for (BronFotoBODiplomaKwalificatieRecord diplomaRecord : inschrijvingRecord
			.getExamenRecords())
		{
			if (diplomaRecord.isDeelkwalificatieDiploma())
			{
				// Controleer op deelkwalificatie in EduArte.
			}
			else
			{
				Examendeelname deelname = getAndRemoveExamendeelname(examendeelnames);
				if (deelname == null)
				{
					createDiplomaVerschil(inschrijvingRecord, diplomaRecord,
						FotoVerschil.DiplomaNietGevonden, verbintenis, deelname);
				}
				else
				{
					vergelijkDiploma(diplomaRecord, deelname);
				}
			}
		}
		// Wat over is in de oorspronkelijke lijst werd niet in de foto gevonden.
		for (Examendeelname deelname : examendeelnames)
		{
			if (deelname.getExamenstatus().isGeslaagd()
				&& deelname.getDatumUitslag().before(inschrijvingRecord.getTeldatum()))
			{
				createDiplomaVerschil(inschrijvingRecord, null,
					FotoVerschil.DiplomaNietGevondenInBron, verbintenis, deelname);
			}
		}
	}

	private void vergelijkDiploma(BronFotoBODiplomaKwalificatieRecord diplomaRecord,
			Examendeelname deelname)
	{
		if (!TimeUtil.getInstance().datesEqual(diplomaRecord.getDatumKwalificatieBehaald(),
			deelname.getDatumUitslag()))
		{
			createDiplomaVerschil(diplomaRecord.getInschrijvingRecord(), diplomaRecord,
				FotoVerschil.DiplomaDatumBehaald, deelname.getVerbintenis(), deelname);
		}
		if (!StringUtil.equalOrBothEmpty(diplomaRecord.getCodeDeelkwalificatieBehaald(), deelname
			.getVerbintenis().getExterneCode()))
		{
			createDiplomaVerschil(diplomaRecord.getInschrijvingRecord(), diplomaRecord,
				FotoVerschil.DiplomaKwalificatieBehaald, deelname.getVerbintenis(), deelname);
		}
		if (diplomaRecord.getIndicatieBekostigingDiploma().booleanValue() != deelname.isBekostigd())
		{
			createDiplomaVerschil(diplomaRecord.getInschrijvingRecord(), diplomaRecord,
				FotoVerschil.DiplomaBekostiging, deelname.getVerbintenis(), deelname);
		}
	}

	private Examendeelname getAndRemoveExamendeelname(List<Examendeelname> examendeelnames)
	{
		for (Examendeelname deelname : examendeelnames)
		{
			if (deelname.getExamenstatus().isGeslaagd())
			{
				examendeelnames.remove(deelname);
				return deelname;
			}
		}
		return null;
	}

	private Verbintenis getVerbintenis(IBronFotoInschrijvingRecord record, Deelnemer deelnemer,
			BronFotobestand fotobestand)
	{
		for (Verbintenis v : deelnemer.getVerbintenissen())
		{
			if (v.getBronStatus() != null && v.getBronStatus().isBekendInBron())
			{
				if (record.getBestand().getType() == getFotoType(v))
				{
					if (record.getBestand().getType() == BronFotoType.VO)
					{
						// VO herkennen obv begindatum.
						Date begindatum = v.getBegindatum();
						if (record.getDatumInschrijving() != null
							&& record.getDatumInschrijving().equals(begindatum))
						{
							return v;
						}
					}
					else
					{
						if (StringUtil.isNotEmpty(v.getVolgnummerInOudPakket()))
						{
							String vlgnr = record.getInschrijvingsvolgnummer();

							if (vlgnr != null && vlgnr.equals(v.getVolgnummerInOudPakket()))
								return v;
						}
						else
						{
							Integer vlgnr = record.getVolgnummerNumeriek();
							if (vlgnr != null && vlgnr.intValue() == v.getVolgnummer())
							{
								return v;
							}
						}
					}
				}
			}
		}
		// Verbintenis niet gevonden.
		BronFotobestandVerschil verschil =
			new BronFotobestandVerschil(fotobestand, FotoVerschil.VerbintenisNietGevonden);
		verschil.setFotoRecord((BronFotoRecord) record);
		verschil.setDeelnemer(deelnemer);
		verschil.save();
		aantalVerschillen++;

		return null;
	}

	private Deelnemer getDeelnemer(IBronFotoInschrijvingRecord record, BronFotobestand fotobestand,
			BronFotobestandInlezenJobRun jobrun)
	{
		ElfProef elfproef = new ElfProef();
		DeelnemerDataAccessHelper deelnemerHelper =
			DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class);
		List<Deelnemer> deelnemers = Collections.emptyList();

		boolean geldigPgn = false;
		boolean pgnIsOnderwijsnummer = false;
		if (record.getPgn() != null && elfproef.isGeldigOnderwijsNummer(record.getPgn()))
		{
			geldigPgn = true;
			pgnIsOnderwijsnummer = true;
			deelnemers = deelnemerHelper.getByOnderwijsnummer(record.getPgn());
		}
		else if (record.getPgn() != null && elfproef.isGeldigSofiNummer(record.getPgn()))
		{
			geldigPgn = true;
			deelnemers = deelnemerHelper.getByBSN(record.getPgn());
		}
		else
		{
			JobRunDetail detail = new JobRunDetail(jobrun);
			detail.setUitkomst("Ongeldig pgn in fotorecord " + record.getId());
			detail.save();
		}
		if (geldigPgn && deelnemers.isEmpty())
		{
			FotoVerschil v =
				pgnIsOnderwijsnummer ? FotoVerschil.DeelnemerOnderwijsnummerNietGevonden
					: FotoVerschil.DeelnemerBSNNietGevonden;
			BronFotobestandVerschil verschil = new BronFotobestandVerschil(fotobestand, v);
			verschil.setFotoRecord((BronFotoRecord) record);
			verschil.save();
			aantalVerschillen++;
		}
		if (geldigPgn && deelnemers.size() > 1)
		{
			JobRunDetail detail = new JobRunDetail(jobrun);
			detail.setUitkomst(String.format("Meer dan één deelnemer gevonden bij %s %d",
				pgnIsOnderwijsnummer ? "onderwijsnummer" : "bsn", record.getPgn()));
			detail.save();
		}
		Deelnemer deelnemer = null;
		if (!deelnemers.isEmpty())
		{
			deelnemer = deelnemers.get(0);
		}
		return deelnemer;
	}

	private BronFotoType getFotoType(Verbintenis verbintenis)
	{
		if (verbintenis.isBOVerbintenis())
			return BronFotoType.BO;
		if (verbintenis.isVAVOVerbintenis() || verbintenis.isEducatieVerbintenis())
			return BronFotoType.ED_VAVO;
		if (verbintenis.isVOVerbintenis())
			return BronFotoType.VO;
		return null;
	}

	private void createVerschil(IBronFotoRecord record, FotoVerschil fv, Verbintenis verbintenis)
	{
		BronFotobestandVerschil verschil = new BronFotobestandVerschil(record, fv, verbintenis);
		verschil.save();
		aantalVerschillen++;
	}

	private void createBPVVerschil(IBronFotoRecord record, BronFotoBOBPVRecord bpvRecord,
			FotoVerschil fv, Verbintenis verbintenis, BPVInschrijving bpvInschrijving)
	{
		BronFotobestandVerschil verschil = new BronFotobestandVerschil(record, fv, verbintenis);
		verschil.setBpvInschrijving(bpvInschrijving);
		verschil.setBpvRecord(bpvRecord);
		verschil.save();
		aantalVerschillen++;
	}

	private void createDiplomaVerschil(IBronFotoRecord record,
			BronFotoBODiplomaKwalificatieRecord examenRecord, FotoVerschil fv,
			Verbintenis verbintenis, Examendeelname examendeelname)
	{
		BronFotobestandVerschil verschil = new BronFotobestandVerschil(record, fv, verbintenis);
		verschil.setExamendeelname(examendeelname);
		verschil.setExamenRecord(examenRecord);
		verschil.save();
		aantalVerschillen++;
	}

	private void createOOVerschil(IBronFotoRecord record,
			IBronFotoOnderwijsontvangendeRecord ooRecord, FotoVerschil fv, Verbintenis verbintenis)
	{
		BronFotobestandVerschil verschil = new BronFotobestandVerschil(record, fv, verbintenis);
		verschil.setOnderwijsontvangendeRecord(ooRecord);
		verschil.save();
		aantalVerschillen++;
	}

	private boolean equal(Integer a, Integer b)
	{
		if (a == null && b == null)
			return true;
		if (a != null && b != null)
		{
			return a.equals(b);
		}
		if (a == null && b != null && b.intValue() == 0)
		{
			return true;
		}
		if (b == null && a != null && a.intValue() == 0)
		{
			return true;
		}
		return false;
	}

}
