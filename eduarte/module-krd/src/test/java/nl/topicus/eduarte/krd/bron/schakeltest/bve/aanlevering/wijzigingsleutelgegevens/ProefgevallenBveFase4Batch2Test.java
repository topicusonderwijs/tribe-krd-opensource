package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.wijzigingsleutelgegevens;

import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.HoogsteVooropleiding;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Leerweg;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.VerantwoordelijkeAanleverbestand;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;

import org.junit.Test;

public class ProefgevallenBveFase4Batch2Test extends ProefgevallenBveFase4
{
	@Test
	public void proefgeval11() throws Exception
	{
		createGeval11();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("4001"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is("100320254"));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910701")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record1.getGevolgdeOpleiding(), is("11048"));
		assertThat(record1.getLeerweg(), is(Leerweg.Beroepsbegeleidend));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Voltijd));
		assertThat(record1.getDatumInschrijving(), is(asDate(20090331)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100401)));
		assertThat(record1.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(record1.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(record1.getIndicatieRisicodeelnemer(), is((Boolean) null));
		assertThat(record1.getIndicatieGehandicapt(), is(false));
		assertThat(record1.getLaatsteVooropleiding(), is((String) null));
		assertThat(record1.getLocatie(), is((Integer) null));

		PeriodegegevensInschrijvingRecord record2 = melding.getMeldingen().get(1);
		assertThat(record2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(record2.getSoortMutatie(), is(Toevoeging));
		assertThat(record2.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20090331)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));
	}

	private void createGeval11() throws Exception
	{
		getDeelnemer4001();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval12() throws Exception
	{
		createGeval12();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("4002"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is("100144750"));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910702")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record1.getGevolgdeOpleiding(), is("11048"));
		assertThat(record1.getLeerweg(), is(Leerweg.Beroepsbegeleidend));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Voltijd));
		assertThat(record1.getDatumInschrijving(), is(asDate(20090331)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100401)));
		assertThat(record1.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(record1.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(record1.getIndicatieRisicodeelnemer(), is((Boolean) null));
		assertThat(record1.getIndicatieGehandicapt(), is(false));
		assertThat(record1.getLaatsteVooropleiding(), is((String) null));
		assertThat(record1.getLocatie(), is((Integer) null));

		PeriodegegevensInschrijvingRecord record2 = melding.getMeldingen().get(1);
		assertThat(record2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(record2.getSoortMutatie(), is(Toevoeging));
		assertThat(record2.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20090331)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));
	}

	private void createGeval12() throws Exception
	{
		getDeelnemer4002();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval13() throws Exception
	{
		createGeval13();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("4003"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is("100069880"));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910703")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is((String) null));
		assertThat(melding.getLand(), is("6030"));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record1.getGevolgdeOpleiding(), is("11048"));
		assertThat(record1.getLeerweg(), is(Leerweg.Beroepsbegeleidend));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Voltijd));
		assertThat(record1.getDatumInschrijving(), is(asDate(20090331)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100401)));
		assertThat(record1.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(record1.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(record1.getIndicatieRisicodeelnemer(), is((Boolean) null));
		assertThat(record1.getIndicatieGehandicapt(), is(false));
		assertThat(record1.getLaatsteVooropleiding(), is((String) null));
		assertThat(record1.getLocatie(), is((Integer) null));

		PeriodegegevensInschrijvingRecord record2 = melding.getMeldingen().get(1);
		assertThat(record2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(record2.getSoortMutatie(), is(Toevoeging));
		assertThat(record2.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20090331)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));
	}

	private void createGeval13() throws Exception
	{
		getDeelnemer4003();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval15() throws Exception
	{
		createGeval15();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("4004"));
		assertThat(melding.getSofinummer(), is("210000041"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910504")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record1.getGevolgdeOpleiding(), is("11048"));
		assertThat(record1.getLeerweg(), is(Leerweg.Beroepsbegeleidend));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Voltijd));
		assertThat(record1.getDatumInschrijving(), is(asDate(20090331)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100401)));
		assertThat(record1.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(record1.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(record1.getIndicatieRisicodeelnemer(), is((Boolean) null));
		assertThat(record1.getIndicatieGehandicapt(), is(false));
		assertThat(record1.getLaatsteVooropleiding(), is((String) null));
		assertThat(record1.getLocatie(), is((Integer) null));

		PeriodegegevensInschrijvingRecord record2 = melding.getMeldingen().get(1);
		assertThat(record2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(record2.getSoortMutatie(), is(Toevoeging));
		assertThat(record2.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20090331)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));
	}

	private void createGeval15() throws Exception
	{
		getDeelnemer4004();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval16() throws Exception
	{
		createGeval16();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("4005"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is("100509521"));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910705")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is((String) null));
		assertThat(melding.getLand(), is("6030"));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record1.getGevolgdeOpleiding(), is("11048"));
		assertThat(record1.getLeerweg(), is(Leerweg.Beroepsbegeleidend));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Voltijd));
		assertThat(record1.getDatumInschrijving(), is(asDate(20090331)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100401)));
		assertThat(record1.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(record1.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(record1.getIndicatieRisicodeelnemer(), is((Boolean) null));
		assertThat(record1.getIndicatieGehandicapt(), is(false));
		assertThat(record1.getLaatsteVooropleiding(), is((String) null));
		assertThat(record1.getLocatie(), is((Integer) null));

		PeriodegegevensInschrijvingRecord record2 = melding.getMeldingen().get(1);
		assertThat(record2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(record2.getSoortMutatie(), is(Toevoeging));
		assertThat(record2.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20090331)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));
	}

	private void createGeval16() throws Exception
	{
		getDeelnemer4005();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void bveBO1Batch2() throws Exception
	{
		createGeval11();
		createGeval12();
		createGeval13();
		createGeval15();
		createGeval16();

		batchBVE = maakBronBOBatch();
		batchBVE.setBatchNummer(2);
		batchBVE.getAanleverpunt().setNummer(1);

		assertThat(batchBVE.getBrinNummer(), is("01AA"));
		assertThat(batchBVE.getAanleverPuntNummer(), is(1));
		assertThat(batchBVE.getSectordeel(), is(Sectordeel.Beroepsonderwijs));

		assertThat(batchBVE.getInternOrganisatieNummer(), is((String) null));
		assertThat(batchBVE.getBatchNummer(), is(2));
		assertThat(batchBVE.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(batchBVE.getVersieProgrammaVanEisen(), is("1.1"));

		assertThat(batchBVE.getAantalMeldingen(), is(5));
		assertThat(batchBVE.getAantalRecords(), is(16));
		assertThat(batchBVE.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanleverbestand.Instelling));
		assertThat(batchBVE.getLaatsteAanlevering(), is(false));

		writeBronBatch(batchBVE);
	}
}