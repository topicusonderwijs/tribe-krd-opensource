package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo;

import static nl.topicus.cobra.types.personalia.Geslacht.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;

import org.junit.Test;

public class ProefgevallenBveBOBatch1Test extends ProefgevallenBveBO
{
	@Test
	public void proefgeval01() throws Exception
	{
		createGeval01();
		controller.save();

		assertThat(getAantalInTransactie(BronAanleverMelding.class), is(1));
		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is(equalTo("1001")));
		assertThat(melding.getSofinummer(), is(equalTo("210000004")));
		assertThat(melding.getOnderwijsnummer(), is(nullValue()));
		assertThat(melding.getGeboortedatum(), is(equalTo(Datum.valueOf(19910501))));
		assertThat(melding.getGeslacht(), is(equalTo(Man)));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is(nullValue()));

		assertThat(melding.getMeldingen().size(), is(2));

		BronBveAanleverRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));

		BronBveAanleverRecord record2 = melding.getMeldingen().get(1);
		assertThat(record2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(record2.getSoortMutatie(), is(Toevoeging));
	}

	private void createGeval01() throws Exception
	{
		getDeelnemer1001();
		addChange(deelnemer.getPersoon(), "bsn", null, deelnemer.getPersoon().getBsn());
		addChange(deelnemer.getPersoon(), "geboortedatum", null, deelnemer.getPersoon()
			.getGeboortedatum());
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval02() throws Exception
	{
		createGeval02();

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is(equalTo("1009")));
		assertThat(melding.getSofinummer(), is(equalTo("210000107")));
		assertThat(melding.getOnderwijsnummer(), is(nullValue()));
		assertThat(melding.getGeboortedatum(), is(equalTo(Datum.valueOf(19910509))));
		assertThat(melding.getGeslacht(), is(equalTo(Man)));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is(nullValue()));
	}

	private void createGeval02() throws Exception
	{
		getDeelnemer1009();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval03() throws Exception
	{
		createGeval03();

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is(equalTo("1010")));
		assertThat(melding.getSofinummer(), is(equalTo("210000119")));
		assertThat(melding.getOnderwijsnummer(), is(nullValue()));
		assertThat(melding.getGeboortedatum(), is(equalTo(Datum.valueOf(19910510))));
		assertThat(melding.getGeslacht(), is(equalTo(Man)));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is(nullValue()));
	}

	private void createGeval03() throws Exception
	{
		getDeelnemer1010();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval04() throws Exception
	{
		createGeval04();

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is(equalTo("1011")));
		assertThat(melding.getSofinummer(), is(equalTo("210000120")));
		assertThat(melding.getOnderwijsnummer(), is(nullValue()));
		assertThat(melding.getGeboortedatum(), is(equalTo(Datum.valueOf(19910511))));
		assertThat(melding.getGeslacht(), is(equalTo(Man)));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is(nullValue()));
	}

	private void createGeval04() throws Exception
	{
		getDeelnemer1011();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval05() throws Exception
	{
		createGeval05();

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is(equalTo("1012")));
		assertThat(melding.getSofinummer(), is(equalTo("210000132")));
		assertThat(melding.getOnderwijsnummer(), is(nullValue()));
		assertThat(melding.getGeboortedatum(), is(equalTo(Datum.valueOf(19910512))));
		assertThat(melding.getGeslacht(), is(equalTo(Man)));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is(nullValue()));
	}

	private void createGeval05() throws Exception
	{
		getDeelnemer1012();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	private void createGeval06() throws Exception
	{
		getDeelnemer1013();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	private void createGeval07() throws Exception
	{
		getDeelnemer1014();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	private void createGeval08() throws Exception
	{
		getDeelnemer1015();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	private void createGeval09() throws Exception
	{
		getDeelnemer1020();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	private void createGeval10() throws Exception
	{
		getDeelnemer1025();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	private void createGeval11() throws Exception
	{
		getDeelnemer1026();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	private void createGeval12() throws Exception
	{
		getDeelnemer1029();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	private void createGeval13() throws Exception
	{
		getDeelnemer1030();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void bveBO1Batch1() throws Exception
	{
		createGeval01();
		createGeval02();
		createGeval03();
		createGeval04();
		createGeval05();
		createGeval06();
		createGeval07();
		createGeval08();
		createGeval09();
		createGeval10();
		createGeval11();
		createGeval12();
		createGeval13();

		batchBVE = maakBronBOBatch();
		batchBVE.setBatchNummer(1);
		batchBVE.getAanleverpunt().setNummer(1);

		assertThat(batchBVE.getAantalMeldingen(), is(13));
		assertThat(batchBVE.getAantalRecords(), is(40));

		writeBronBatch(batchBVE);
	}
}
