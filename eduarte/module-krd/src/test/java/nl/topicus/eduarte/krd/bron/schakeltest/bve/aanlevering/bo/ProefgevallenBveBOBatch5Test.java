package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo;

import static nl.topicus.cobra.types.personalia.Geslacht.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Leerweg.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.VerantwoordelijkeAanleverbestand;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;

import org.junit.Test;

public class ProefgevallenBveBOBatch5Test extends ProefgevallenBveBO
{
	@Test
	public void proefgeval47() throws Exception
	{
		createGeval47();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1027"));
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

	private void createGeval47() throws Exception
	{
		getDeelnemer1027();
		verbintenis.setStatus(VerbintenisStatus.Definitief);
		addChange(verbintenis, "status", null, verbintenis.getStatus());
	}

	@Test
	public void proefgeval48() throws Exception
	{
		createGeval48();

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1029"));
		assertThat(melding.getSofinummer(), is("210000375"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910529")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		PeriodegegevensInschrijvingRecord bekostiging = melding.getMeldingen().get(0);
		assertThat(bekostiging.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(bekostiging.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(bekostiging.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(bekostiging.getSoortMutatie(), is(Aanpassing));
	}

	private void createGeval48() throws Exception
	{
		getDeelnemer1029();
		verbintenis.setBekostigd(Bekostigd.Ja);
		addChange(verbintenis, "bekostigd", Bekostigd.Nee, Bekostigd.Ja);
	}

	@Test
	public void proefgeval49() throws Exception
	{
		createGeval49();

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1030"));
		assertThat(melding.getSofinummer(), is("210000387"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910530")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getSoortMutatie(), is(Verwijdering));
	}

	private void createGeval49() throws Exception
	{
		getDeelnemer1030();
		verbintenis.setStatus(VerbintenisStatus.Afgewezen);
		addChange(verbintenis, "status", VerbintenisStatus.Definitief, verbintenis.getStatus());
	}

	@Test
	public void bveBO1Batch5() throws Exception
	{
		createGeval47();
		createGeval48();
		createGeval49();

		batchBVE = maakBronBOBatch();
		batchBVE.setBatchNummer(5);
		batchBVE.getAanleverpunt().setNummer(1);
		batchBVE.setVerantwoordelijkeAanlevering(VerantwoordelijkeAanleverbestand.Accountant);

		writeBronBatch(batchBVE);

		assertThat(batchBVE.getAantalMeldingen(), is(3));
		assertThat(getRecordTypesFromBatch(), is(equalTo(Arrays.asList(305, 320, 321, 305, 321,
			305, 320))));
		assertThat(batchBVE.getAantalRecords(), is(8));
	}
}
