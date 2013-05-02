package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo;

import static nl.topicus.cobra.types.personalia.Geslacht.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd.*;
import static nl.topicus.eduarte.entities.taxonomie.MBOLeerweg.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Leerweg.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.bron.schakeltest.BronBuilder;
import nl.topicus.eduarte.krd.bron.schakeltest.BronTestData;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.ExamengegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;

import org.junit.Ignore;
import org.junit.Test;

public class ProefgevallenBveBOBatch4Test extends ProefgevallenBveBO
{
	@Test
	public void proefgeval43() throws Exception
	{
		createGeval43();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1024"));
		assertThat(melding.getSofinummer(), is("210000296"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910524")));
		assertThat(melding.getGeslacht(), is(Vrouw));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("11052"));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20110101)));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));
		assertThat(inschrijving.getLeerweg(), is(Beroepsopleidend));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));

		PeriodegegevensInschrijvingRecord bekostiging = melding.getMeldingen().get(1);
		assertThat(bekostiging.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(bekostiging.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(bekostiging.getIndicatieBekostigingInschrijving(), is(false));
		assertThat(bekostiging.getSoortMutatie(), is(Toevoeging));
	}

	private void createGeval43() throws Exception
	{
		getDeelnemer1024();
		addChange(verbintenis, "status", null, verbintenis.getStatus());
	}

	@Test
	@Ignore("Deelkwalificaties worden niet goed ondersteund in BRON, dus deze test ff uitgezet")
	public void proefgeval44() throws Exception
	{
		// FIXME deelkwalificaties moeten nog geimplementeerd worden of een andere
		// oplossing gevonden worden voor deze testgevallen.
		createGeval44();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1025"));
		assertThat(melding.getSofinummer(), is("210000302"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910525")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(getRecordTypes(melding), is(equalTo(Arrays.asList(305, 323))));
		assertThat(melding.getMeldingen().size(), is(1));

		ExamengegevensRecord examen = melding.getMeldingen().get(0);
		assertThat(examen.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		// VOOR SCHAKELTEST dient dit 1 te zijn, echter EduArte ondersteunt niet deze
		// manier van deelkwalificaties uitdelen.
		// assertThat(examen.getInschrijvingsvolgnummer(), is("1"));
		assertThat(examen.getInschrijvingsvolgnummer(), is("2"));
		assertThat(examen.getBehaaldeDeelKwalificatie(), is("56176"));
		assertThat(examen.getSoortMutatie(), is(Toevoeging));
		assertThat(examen.getDatumBehaald(), is(asDate(20081111)));
		assertThat(examen.getIndicatieBekostigingDiploma(), is(false));
	}

	private void createGeval44() throws Exception
	{
		getDeelnemer1025();

		builder.addVerbintenisMBO(20080801, 56176, BOL, Nee);

		Verbintenis deelkwalificatie1 = builder.getVerbintenis();

		// FIXME!!!

		Examendeelname deelname1_ = addExamendeelname(deelkwalificatie1, 20081111);
		deelname1_.setBekostigd(false);
		deelname1_.setExamenstatus(getGeslaagd());

		addChange(deelname1_, "examenstatus", null, deelname1_.getExamenstatus());
	}

	@Test
	public void proefgeval45() throws Exception
	{
		createGeval45();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1026"));
		assertThat(melding.getSofinummer(), is("210000326"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910526")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("10630"));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20110101)));
		assertThat(inschrijving.getIntensiteit(), is(Examendeelnemer));

		// Voor de officiele schakeltest moet hier staan:
		// assertThat(inschrijving.getLeerweg(), is(equalTo(Beroepsopleidend)));
		// omdat dit geval een signaal moet triggeren binnen BRON. Onze software kan deze
		// melding officieel niet triggeren, dus hier moet de verwachte waarde
		// gecontroleerd worden.
		assertThat(inschrijving.getLeerweg(), is(nullValue()));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));

		PeriodegegevensInschrijvingRecord bekostiging = melding.getMeldingen().get(1);
		assertThat(bekostiging.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(bekostiging.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(bekostiging.getIndicatieBekostigingInschrijving(), is(false));
		assertThat(bekostiging.getSoortMutatie(), is(Toevoeging));
	}

	private void createGeval45() throws Exception
	{
		BronTestData data = getDeelnemer1026();

		BronBuilder bbuilder = data.getBuilder();
		bbuilder.addVerbintenisMBO(20080801, 10630, BOL, Nee);
		data = bbuilder.build();
		verbintenis = data.getVerbintenis();
		verbintenis.setIntensiteit(Intensiteit.Examendeelnemer);
		verbintenis.setStatus(VerbintenisStatus.Definitief);

		addChange(verbintenis, "status", null, verbintenis.getStatus());
	}

	@Test
	public void proefgeval46() throws Exception
	{
		createGeval46();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1028"));
		assertThat(melding.getSofinummer(), is("210000351"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910528")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("10342"));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20110101)));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));
		assertThat(inschrijving.getLeerweg(), is(Beroepsbegeleidend));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));

		PeriodegegevensInschrijvingRecord bekostiging = melding.getMeldingen().get(1);
		assertThat(bekostiging.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(bekostiging.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(bekostiging.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(bekostiging.getSoortMutatie(), is(Toevoeging));
	}

	private void createGeval46() throws Exception
	{
		getDeelnemer1028();

		verbintenis.setStatus(VerbintenisStatus.Definitief);
		addChange(verbintenis, "status", null, verbintenis.getStatus());
	}

	@Test
	@Ignore("Uitgezet in afwachting van proefgeval44()")
	public void bveBO1Batch4() throws Exception
	{
		createGeval43();
		createGeval44();
		createGeval45();
		createGeval46();

		batchBVE = maakBronBOBatch();
		batchBVE.setBatchNummer(4);
		batchBVE.getAanleverpunt().setNummer(1);

		writeBronBatch(batchBVE);

		assertThat(batchBVE.getAantalMeldingen(), is(4));
		assertThat(batchBVE.getAantalRecords(), is(12));
	}
}
