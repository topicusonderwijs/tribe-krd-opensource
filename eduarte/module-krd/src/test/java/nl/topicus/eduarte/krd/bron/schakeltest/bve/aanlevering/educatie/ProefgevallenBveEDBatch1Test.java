package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.educatie;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.VakgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.HoogsteVooropleiding;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.VerantwoordelijkeAanleverbestand;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;

import org.junit.Test;

public class ProefgevallenBveEDBatch1Test extends ProefgevallenBveED
{
	@Test
	public void proefgeval01() throws Exception
	{
		createGeval01();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3001"));
		assertThat(melding.getSofinummer(), is("210000004"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910501")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		// onderstaande inschrijvingsmelding is voor alle records in deze batch hetzelfde,
		// vandaar alleen hier getest
		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		// ook onderstaande vakgegevensmelding is voor alle records in deze batch
		// hetzelfde, vandaar alleen hier getest
		VakgegevensRecord vakgegevens = melding.getMeldingen().get(1);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0310"));

		assertThat(melding.getMeldingen().size(), is(2));
	}

	private void createGeval01() throws Exception
	{
		getDeelnemer3001();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);

	}

	@Test
	public void proefgeval02() throws Exception
	{
		createGeval02();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3009"));
		assertThat(melding.getSofinummer(), is("210000107"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910509")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
	}

	private void createGeval02() throws Exception
	{
		getDeelnemer3009();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval03() throws Exception
	{
		createGeval03();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3010"));
		assertThat(melding.getSofinummer(), is("210000119"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910510")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
	}

	private void createGeval03() throws Exception
	{
		getDeelnemer3010();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval04() throws Exception
	{
		createGeval04();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3011"));
		assertThat(melding.getSofinummer(), is("210000120"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910511")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
	}

	private void createGeval04() throws Exception
	{
		getDeelnemer3011();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval05() throws Exception
	{
		createGeval05();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3012"));
		assertThat(melding.getSofinummer(), is("210000132"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910512")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
	}

	private void createGeval05() throws Exception
	{
		getDeelnemer3012();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval06() throws Exception
	{
		createGeval06();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3013"));
		assertThat(melding.getSofinummer(), is("210000144"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910513")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
	}

	private void createGeval06() throws Exception
	{
		getDeelnemer3013();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval07() throws Exception
	{
		createGeval07();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3014"));
		assertThat(melding.getSofinummer(), is("210000156"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910514")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
	}

	private void createGeval07() throws Exception
	{
		getDeelnemer3014();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval08() throws Exception
	{
		createGeval08();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3017"));
		assertThat(melding.getSofinummer(), is("210000193"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910517")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
	}

	private void createGeval08() throws Exception
	{
		getDeelnemer3017();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval09() throws Exception
	{
		createGeval09();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3018"));
		assertThat(melding.getSofinummer(), is("210000211"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910518")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
	}

	private void createGeval09() throws Exception
	{
		getDeelnemer3018();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval10() throws Exception
	{
		createGeval10();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3020"));
		assertThat(melding.getSofinummer(), is("210000235"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910520")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
	}

	private void createGeval10() throws Exception
	{
		getDeelnemer3020();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval11() throws Exception
	{
		createGeval11();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3021"));
		assertThat(melding.getSofinummer(), is("210000259"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910521")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));
	}

	private void createGeval11() throws Exception
	{
		getDeelnemer3021();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval12() throws Exception
	{
		createGeval12();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3025"));
		assertThat(melding.getSofinummer(), is("210000302"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910525")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(1);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0310"));

		vakgegevens = melding.getMeldingen().get(2);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(2));
		assertThat(vakgegevens.getVak(), is("0011"));

		assertThat(melding.getMeldingen().size(), is(3));
	}

	private void createGeval12() throws Exception
	{
		getDeelnemer3025();
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

		batchBVE = maakBronEDBatch();

		batchBVE.setBatchNummer(1);
		batchBVE.getAanleverpunt().setNummer(1);
		batchBVE.getOrganisatie().getBrincode().setCode("04CY");

		assertThat(batchBVE.getBrinNummer(), is("04CY"));
		assertThat(batchBVE.getAanleverPuntNummer(), is(1));
		assertThat(batchBVE.getSectordeel(), is(Sectordeel.Basiseducatie));
		assertThat(batchBVE.getInternOrganisatieNummer(), is((String) null));
		assertThat(batchBVE.getBatchNummer(), is(1));
		assertThat(batchBVE.getBestandSoort(), is(BestandSoort.AANLEVERING));
		// assertThat(batchBVE.getVersieProgrammaVanEisen(), is("1.1"));
		assertThat(batchBVE.getAantalMeldingen(), is(12));
		assertThat(batchBVE.getAantalRecords(), is(38));
		assertThat(batchBVE.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanleverbestand.Instelling));
		assertThat(batchBVE.getLaatsteAanlevering(), is(false));

		writeBronBatch(batchBVE);
	}
}
