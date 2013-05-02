package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo;

import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd.*;
import static nl.topicus.eduarte.entities.taxonomie.MBOLeerweg.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.ExamengegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie;

import org.junit.Ignore;
import org.junit.Test;

public class ProefgevallenBveBOBatch3Test extends ProefgevallenBveBO
{
	@Test
	public void proefgeval28() throws Exception
	{
		createGeval28();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1009"));
		assertThat(melding.getMeldingen().size(), is(1));

		BronBveAanleverRecord actual = melding.getMeldingen().get(0);
		assertThat(actual.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(actual.getSoortMutatie(), is(Aanpassing));
	}

	private void createGeval28() throws Exception
	{
		getDeelnemer1009();
		verbintenis.setEinddatum(asDate(20090930));
		addChange(verbintenis, "einddatum", null, asDate(20090930));
	}

	@Test
	public void proefgeval29() throws Exception
	{
		createGeval29();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1010"));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord record = melding.getMeldingen().get(0);
		assertThat(record.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record.getSoortMutatie(), is(Aanpassing));
		assertThat(record.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record.getGeplandeDatumUitschrijving(), is(asDate(20110601)));
	}

	private void createGeval29() throws Exception
	{
		getDeelnemer1010();
		verbintenis.setGeplandeEinddatum(asDate(20110601));
		addChange(verbintenis, "geplandeEinddatum", null, asDate(20110601));
	}

	@Test
	public void proefgeval30() throws Exception
	{
		createGeval30();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1011"));
		assertThat(melding.getMeldingen().size(), is(3));

		InschrijvingsgegevensRecord record320 = melding.getMeldingen().get(0);
		assertThat(record320.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record320.getSoortMutatie(), is(Aanpassing));
		assertThat(record320.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record320.getDatumInschrijving(), is(asDate(20080901)));

		PeriodegegevensInschrijvingRecord record321 = melding.getMeldingen().get(1);

		assertThat(record321.getSoortMutatie(), is(SoortMutatie.Verwijdering));
		assertThat(record321.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record321.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));

		record321 = melding.getMeldingen().get(2);
		assertThat(record321.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(record321.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record321.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080901)));
	}

	private void createGeval30() throws Exception
	{
		getDeelnemer1011();
		Date oud = verbintenis.getBegindatum();
		verbintenis.setBegindatum(asDate(20080901));

		// TODO geval heeft extra veld in een keer: risicodeelnemer, niet opgenomen
		addChange(verbintenis, "begindatum", oud, verbintenis.getBegindatum());
	}

	@Test
	public void proefgeval31() throws Exception
	{
		createGeval31();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1012"));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord record = melding.getMeldingen().get(0);
		assertThat(record.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record.getSoortMutatie(), is(Verwijdering));
		assertThat(record.getInschrijvingsvolgnummer(), is("1"));

		// onderstaande asserts zouden volgens het testgeval moeten gelden, echter veel
		// van deze velden zijn verplicht, en mogen dus niet leeg gelaten worden.

		// assertNull(record.getDatumInschrijving());
		// assertNull(record.getGeplandeDatumUitschrijving());
		// assertNull(record.getGevolgdeOpleiding());
		// assertNull(record.getHoogsteVooropleiding());
		// assertNull(record.getIndicatieGehandicapt());
		// assertNull(record.getIndicatieRisicodeelnemer());
		// assertNull(record.getIntensiteit());
		// assertNull(record.getLaatsteVooropleiding());
		// assertNull(record.getLeerweg());
		// assertNull(record.getLocatie());
		// assertNull(record.getRedenUitstroom());
	}

	private void createGeval31() throws Exception
	{
		getDeelnemer1012();
		addChange(verbintenis, "status", verbintenis.getStatus(), VerbintenisStatus.Voorlopig);
		verbintenis.setStatus(VerbintenisStatus.Voorlopig);
	}

	@Test
	public void proefgeval32() throws Exception
	{
		createGeval32();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1013"));
		List<BronBveAanleverRecord> records = melding.getMeldingen();
		assertThat(records.size(), is(2));

		PeriodegegevensInschrijvingRecord periode1 = records.get(0);
		assertThat(periode1.getRecordType(), is(321));
		assertThat(periode1.getSoortMutatie(), is(Aanpassing));
		assertThat(periode1.getInschrijvingsvolgnummer(), is("1"));
		assertThat(periode1.getDatumIngangPeriodegegevensInschrijving(), is(verbintenis
			.getBegindatum()));
		assertThat(periode1.getIndicatieBekostigingInschrijving(), is(false));

		PeriodegegevensInschrijvingRecord periode2 = records.get(1);
		assertThat(periode2.getRecordType(), is(321));
		assertThat(periode2.getSoortMutatie(), is(Toevoeging));
		assertThat(periode2.getInschrijvingsvolgnummer(), is("1"));
		assertThat(periode2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20091101)));
		assertThat(periode2.getIndicatieBekostigingInschrijving(), is(false));
	}

	private void createGeval32() throws Exception
	{
		getDeelnemer1013();

		verbintenis.setBekostigd(Bekostigd.Gedeeltelijk);

		List<Bekostigingsperiode> periodes = verbintenis.getBekostigingsperiodes();
		Bekostigingsperiode periode1 = new Bekostigingsperiode();
		periode1.setVerbintenis(verbintenis);
		periode1.setBegindatum(verbintenis.getBegindatum());
		periode1.setBekostigd(false);
		periode1.setVerbintenis(verbintenis);
		periode1.setEinddatum(asDate(20091030));

		Bekostigingsperiode periode2 = new Bekostigingsperiode();
		periode2.setVerbintenis(verbintenis);
		periode2.setBegindatum(asDate(20091101));
		periode2.setBekostigd(false);

		periodes.add(periode1);
		periodes.add(periode2);

		addChange(verbintenis, "bekostigd", Nee, Gedeeltelijk);
		addChange(periode1, "begindatum", null, verbintenis.getBegindatum());
		addChange(periode1, "bekostigd", null, false);
		addChange(periode1, "einddatum", null, asDate(20091030));

		addChange(periode2, "begindatum", null, asDate(20091101));
		addChange(periode2, "bekostigd", null, false);
	}

	@Test
	public void proefgeval33() throws Exception
	{
		createGeval33();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1014"));
		assertThat(melding.getMeldingen().size(), is(1));

		PeriodegegevensInschrijvingRecord record = melding.getMeldingen().get(0);
		assertThat(record.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(record.getSoortMutatie(), is(Aanpassing));
		assertThat(record.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(record.getIndicatieBekostigingInschrijving(), is(false));
	}

	private void createGeval33() throws Exception
	{
		getDeelnemer1014();

		Bekostigd oudeWaarde = verbintenis.getBekostigd();
		Bekostigd nieuweWaarde = Bekostigd.Nee;
		verbintenis.setBekostigd(nieuweWaarde);
		addChange(verbintenis, "bekostigd", oudeWaarde, nieuweWaarde);
	}

	@Test
	public void proefgeval34() throws Exception
	{
		createGeval34();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1015"));
		assertThat(melding.getMeldingen().size(), is(1));

		ExamengegevensRecord examenmelding = melding.getMeldingen().get(0);
		assertThat(examenmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		assertThat(examenmelding.getBehaaldeDeelKwalificatie(), is("10342"));
		assertThat(examenmelding.getDatumBehaald(), is(asDate(20090211)));
		assertThat(examenmelding.getIndicatieBekostigingDiploma(), is(false));
		assertThat(examenmelding.getSoortMutatie(), is(Toevoeging));
	}

	private void createGeval34() throws Exception
	{
		getDeelnemer1015();

		Examendeelname examendeelname = addExamendeelname(verbintenis, 20090211);
		examendeelname.setBekostigd(false);
		addChange(examendeelname, "examenstatus", null, examendeelname.getExamenstatus());
	}

	@Test
	public void proefgeval35() throws Exception
	{
		createGeval35();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1016"));
		assertThat(melding.getMeldingen().size(), is(1));

		ExamengegevensRecord examenmelding = melding.getMeldingen().get(0);
		assertThat(examenmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));

		// FIXME dit is KUT omdat we geen deelkwalificaties hebben in EduArte op het
		// moment van de afname van de integratietest. Hierdoor moeten we een verbintenis
		// mocken en deze hetzelfde volgnummer geven als de 'echte' verbintenis.
		// deelkwalificatie.setVolgnummer(1);

		assertThat(examenmelding.getInschrijvingsvolgnummer(), is("2"));
		// assertThat(examenmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(examenmelding.getBehaaldeDeelKwalificatie(), is("57016"));
		assertThat(examenmelding.getDatumBehaald(), is(asDate(20090213)));
		assertThat(examenmelding.getIndicatieBekostigingDiploma(), is(false));
		assertThat(examenmelding.getSoortMutatie(), is(Toevoeging));
	}

	private void createGeval35() throws Exception
	{
		getDeelnemer1016();

		builder.addVerbintenisMBO(20080801, 57016, BOL, Nee);

		Verbintenis deelkwalificatie = builder.getVerbintenis();

		Examendeelname examendeelname = addExamendeelname(deelkwalificatie, 20090213);
		examendeelname.setBekostigd(false);
		addChange(examendeelname, "examenstatus", null, examendeelname.getExamenstatus());
	}

	@Test
	@Ignore("In afwachting van een goede oplossing voor deelkwalificaties")
	public void proefgeval36() throws Exception
	{
		createGeval36();
		controller.save();

		// FIXME
		// probleem is dat we een aparte verbintenis maken voor de tweede kwalificatie,
		// wat na de verdichting resulteert in een aparte BRON melding (het is namelijk
		// een andere verbintenis)

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1017"));
		assertThat(melding.getSofinummer(), is("210000193"));

		assertThat(getRecordTypes(melding), is(Arrays.asList(305, 323, 323)));
		assertThat(melding.getMeldingen().size(), is(2));

		ExamengegevensRecord examen1 = melding.getMeldingen().get(0);
		assertThat(examen1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		// FIXME deelkwalificaties moeten nog geimplementeerd worden of een andere
		// oplossing gevonden worden voor deze testgevallen.
		// deelkwalificatie1.setVolgnummer(1);
		assertThat(examen1.getInschrijvingsvolgnummer(), is("2"));
		assertThat(examen1.getSoortMutatie(), is(Toevoeging));
		assertThat(examen1.getIndicatieBekostigingDiploma(), is(false));
		assertThat(examen1.getBehaaldeDeelKwalificatie(), is("11051"));
		assertThat(examen1.getDatumBehaald(), is(asDate(20090214)));

		ExamengegevensRecord examen2 = melding.getMeldingen().get(1);
		assertThat(examen2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		// FIXME deelkwalificaties moeten nog geimplementeerd worden of een andere
		// oplossing gevonden worden voor deze testgevallen.
		// deelkwalificatie2.setVolgnummer(1);
		assertThat(examen2.getInschrijvingsvolgnummer(), is("3"));
		assertThat(examen2.getSoortMutatie(), is(Toevoeging));
		assertThat(examen2.getIndicatieBekostigingDiploma(), is(false));
		assertThat(examen2.getBehaaldeDeelKwalificatie(), is("11052"));
		assertThat(examen2.getDatumBehaald(), is(asDate(20090215)));
	}

	private void createGeval36() throws Exception
	{
		getDeelnemer1017();

		builder.addVerbintenisMBO(20080801, 11051, BOL, Nee);

		Verbintenis deelkwalificatie1 = builder.getVerbintenis();

		Examendeelname deelname1_ = addExamendeelname(deelkwalificatie1, 20090214);
		deelname1_.setBekostigd(false);

		builder.addVerbintenisMBO(20080801, 11052, BOL, Nee);

		Verbintenis deelkwalificatie2 = builder.getVerbintenis();
		Examendeelname deelname2 = addExamendeelname(deelkwalificatie2, 20090215);
		deelname2.setBekostigd(false);

		addChange(deelname1_, "examenstatus", null, deelname1_.getExamenstatus());
		addChange(deelname2, "examenstatus", null, deelname2.getExamenstatus());
	}

	@Test
	public void proefgeval37() throws Exception
	{
		createGeval37();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1018"));
		assertThat(melding.getSofinummer(), is("210000211"));

		assertThat(melding.getMeldingen().size(), is(1));

		ExamengegevensRecord examen1 = melding.getMeldingen().get(0);
		assertThat(examen1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		assertThat(examen1.getSoortMutatie(), is(Aanpassing));
		assertThat(examen1.getIndicatieBekostigingDiploma(), is(true));
		assertThat(examen1.getBehaaldeDeelKwalificatie(), is("12003"));
		assertThat(examen1.getDatumBehaald(), is(asDate(20090411)));
	}

	private void createGeval37() throws Exception
	{
		getDeelnemer1018();

		Examendeelname examendeelname = verbintenis.getExamendeelnames().get(0);
		Date origineel = examendeelname.getDatumUitslag();
		examendeelname.setDatumUitslag(asDate(20090411));

		addChange(examendeelname, "datumUitslag", origineel, examendeelname.getDatumUitslag());
	}

	@Test
	public void proefgeval38() throws Exception
	{
		createGeval38();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1019"));
		assertThat(melding.getSofinummer(), is("210000223"));

		assertThat(melding.getMeldingen().size(), is(1));

		ExamengegevensRecord examen1 = melding.getMeldingen().get(0);
		assertThat(examen1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));

		assertThat(examen1.getSoortMutatie(), is(Aanpassing));
		assertThat(examen1.getIndicatieBekostigingDiploma(), is(false));
		assertThat(examen1.getBehaaldeDeelKwalificatie(), is("12003"));
		assertThat(examen1.getDatumBehaald(), is(asDate(20090211)));

	}

	private void createGeval38() throws Exception
	{
		getDeelnemer1019();

		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);

		boolean bekostigd = deelname.isBekostigd();
		deelname.setBekostigd(false);
		addChange(deelname, "bekostigd", bekostigd, deelname.isBekostigd());
	}

	@Test
	public void proefgeval39() throws Exception
	{
		createGeval39();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1020"));
		assertThat(melding.getSofinummer(), is("210000235"));

		assertThat(melding.getMeldingen().size(), is(2));

		BpvGegevensRecord bpv1 = melding.getMeldingen().get(0);
		assertThat(bpv1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_BPVGEGEVENS));
		assertThat(bpv1.getSoortMutatie(), is(Toevoeging));
		assertThat(bpv1.getBpvVolgnummer(), is(1));
		assertThat(bpv1.getAfsluitdatumBpv(), is(asDate(20081010)));
		assertThat(bpv1.getDatumBeginBpv(), is(asDate(20081011)));
		assertThat(bpv1.getGeplandeDatumEindeBpv(), is(asDate(20100801)));
		assertThat(bpv1.getLeerbedrijf(), is("02PR9725JJ0001601"));
		assertThat(bpv1.getOmvangBpv(), is(5120));

		BpvGegevensRecord bpv2 = melding.getMeldingen().get(1);
		assertThat(bpv2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_BPVGEGEVENS));
		assertThat(bpv2.getSoortMutatie(), is(Toevoeging));
		assertThat(bpv2.getBpvVolgnummer(), is(2));
		assertThat(bpv2.getAfsluitdatumBpv(), is(asDate(20081010)));
		assertThat(bpv2.getDatumBeginBpv(), is(asDate(20081011)));
		assertThat(bpv2.getGeplandeDatumEindeBpv(), is(asDate(20100801)));
		assertThat(bpv2.getLeerbedrijf(), is("00SE9722TB0000201"));
		assertThat(bpv2.getOmvangBpv(), is(1000));
	}

	private void createGeval39() throws Exception
	{
		getDeelnemer1020();

		BPVInschrijving bpv1 =
			addBpvInschrijving(20081010, 20081011, 20100801, "02PR9725JJ0001601", 5120);
		BPVInschrijving bpv2 =
			addBpvInschrijving(20081010, 20081011, 20100801, "00SE9722TB0000201", 1000);

		addChange(bpv1, "status", null, BPVStatus.Definitief);
		addChange(bpv2, "status", null, BPVStatus.Definitief);
	}

	@Test
	public void proefgeval40() throws Exception
	{
		createGeval40();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1021"));
		assertThat(melding.getSofinummer(), is("210000259"));

		assertThat(melding.getMeldingen().size(), is(1));

		BpvGegevensRecord bpv1 = melding.getMeldingen().get(0);
		assertThat(bpv1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_BPVGEGEVENS));
		assertThat(bpv1.getSoortMutatie(), is(Toevoeging));
		assertThat(bpv1.getBpvVolgnummer(), is(2));
		assertThat(bpv1.getAfsluitdatumBpv(), is(asDate(20081201)));
		assertThat(bpv1.getDatumBeginBpv(), is(asDate(20081201)));
		assertThat(bpv1.getGeplandeDatumEindeBpv(), is(asDate(20090401)));
		assertNull(bpv1.getLeerbedrijf());
		assertThat(bpv1.getBrinCodeKbb(), is("00SE"));
		assertThat(bpv1.getNaamLeerbedrijf(), is("Miss Sim"));
		assertThat(bpv1.getPostcodeLeerbedrijf(), is("1062HH"));
		assertThat(bpv1.getHuisnummerLeerbedrijf(), is(13));
		assertThat(bpv1.getOmvangBpv(), is(1000));
		assertThat(bpv1.getCreboCodeBpv(), is("12003"));

		// NIET WEG: onderstaande crebo code is de waarde zoals deze gespecificeerd is in
		// de BRON schakeltest. Wij vermoeden dat de opgegeven CREBO code verkeerd is,
		// aangezien de deelnemer 1021 opleiding 12003
		// "Assistent medewerker voedsel en groen (bloemen- en tuincentrumbranche)" volgt,
		// en de BPV inschrijving volgens de schakeltest code 10013 "Medewerker Inkoop"
		// zou moeten zijn. Pas weghalen als bevestigd is dat wij het goed hebben.
		// assertThat(bpv1.getCreboCodeBpv(), is("10013"));
	}

	private void createGeval40() throws Exception
	{
		getDeelnemer1021();

		ExterneOrganisatie bedrijf = new ExterneOrganisatie();
		bedrijf.setNaam("Miss Sim");
		ExterneOrganisatieAdres adres = bedrijf.newAdres();
		adres.setFysiekadres(true);
		adres.setBegindatum(asDate(20000101));
		bedrijf.getAdressen().add(adres);
		bedrijf.getFysiekAdres().getAdres().setPostcode("1062HH");
		bedrijf.getFysiekAdres().getAdres().setHuisnummer("13");
		bedrijf.setBpvBedrijf(true);

		BPVInschrijving bpv2 = addBpvInschrijving(20081201, 20081201, 20090401, null, 1000);
		bpv2.getBedrijfsgegeven().setKenniscentrum(new Brin("00SE"));
		bpv2.setBpvBedrijf(bedrijf);
		bpv2.getBedrijfsgegeven().setExterneOrganisatie(bedrijf);
		bpv2.setStatus(BPVStatus.Definitief);

		addChange(bpv2, "status", null, BPVStatus.Definitief);
	}

	@Test
	public void proefgeval41() throws Exception
	{
		createGeval41();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1022"));
		assertThat(melding.getSofinummer(), is("210000260"));

		assertThat(melding.getMeldingen().size(), is(1));

		BpvGegevensRecord bpv1 = melding.getMeldingen().get(0);
		assertThat(bpv1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_BPVGEGEVENS));
		assertThat(bpv1.getSoortMutatie(), is(Aanpassing));
		assertThat(bpv1.getBpvVolgnummer(), is(1));
		assertThat(bpv1.getAfsluitdatumBpv(), is(asDate(20080811)));
		assertThat(bpv1.getDatumBeginBpv(), is(asDate(20080901)));
		assertThat(bpv1.getGeplandeDatumEindeBpv(), is(asDate(20081210)));
		assertThat(bpv1.getWerkelijkeDatumEindeBpv(), is(asDate(20081210)));
		assertThat(bpv1.getLeerbedrijf(), is("02PR9725JJ0001601"));
		assertNull(bpv1.getOmvangBpv());
	}

	private void createGeval41() throws Exception
	{
		getDeelnemer1022();

		BPVInschrijving bpv = verbintenis.getBpvInschrijvingen().get(0);
		bpv.setEinddatum(asDate(20081210));

		Date vorigeVerwachteEinddatum = bpv.getVerwachteEinddatum();
		bpv.setVerwachteEinddatum(asDate(20081210));
		bpv.setTotaleOmvang(null);

		addChange(bpv, "einddatum", null, bpv.getEinddatum());
		addChange(bpv, "verwachteEinddatum", vorigeVerwachteEinddatum, bpv.getVerwachteEinddatum());
		addChange(bpv, "totaleOmvang", 1000, null);
	}

	@Test
	public void proefgeval42() throws Exception
	{
		createGeval42();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1023"));
		assertThat(melding.getSofinummer(), is("210000272"));

		assertThat(melding.getMeldingen().size(), is(1));

		BpvGegevensRecord bpv1 = melding.getMeldingen().get(0);
		assertThat(bpv1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_BPVGEGEVENS));
		assertThat(bpv1.getSoortMutatie(), is(Verwijdering));
		assertThat(bpv1.getBpvVolgnummer(), is(1));
		// assertNull(bpv1.getAfsluitdatumBpv());
		// assertNull(bpv1.getDatumBeginBpv());
		// assertNull(bpv1.getGeplandeDatumEindeBpv());
		// assertNull(bpv1.getLeerbedrijf());
		// assertNull(bpv1.getBrinCodeKbb());
		// assertNull(bpv1.getNaamLeerbedrijf());
		// assertNull(bpv1.getPostcodeLeerbedrijf());
		// assertNull(bpv1.getHuisnummerLeerbedrijf());
		// assertNull(bpv1.getOmvangBpv());
		// assertNull(bpv1.getCreboCodeBpv());
	}

	private void createGeval42() throws Exception
	{
		getDeelnemer1023();

		bpvInschrijving.setStatus(BPVStatus.Voorlopig);
		addChange(bpvInschrijving, "status", BPVStatus.Definitief, bpvInschrijving.getStatus());
	}

	@Test
	@Ignore("In afwachting van een goede oplossing voor proefgeval36()")
	public void bveBO1Batch3() throws Exception
	{
		createGeval28();
		createGeval29();
		createGeval30();
		createGeval31();
		createGeval32();
		createGeval33();
		createGeval34();
		createGeval35();
		createGeval36();
		createGeval37();
		createGeval38();
		createGeval39();
		createGeval40();
		createGeval41();
		createGeval42();

		batchBVE = maakBronBOBatch();
		batchBVE.setBatchNummer(3);
		batchBVE.getAanleverpunt().setNummer(1);
		batchBVE.setLaatsteAanlevering(true);

		writeBronBatch(batchBVE);

		assertThat(batchBVE.getAantalMeldingen(), is(15));
		assertThat(batchBVE.getAantalRecords(), is(33));
	}
}
