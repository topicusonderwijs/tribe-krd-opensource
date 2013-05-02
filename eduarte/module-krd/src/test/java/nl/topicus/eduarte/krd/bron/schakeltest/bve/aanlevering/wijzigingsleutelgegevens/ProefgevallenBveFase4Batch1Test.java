package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.wijzigingsleutelgegevens;

import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd.*;
import static nl.topicus.eduarte.entities.taxonomie.MBOLeerweg.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.ExamengegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.HoogsteVooropleiding;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Leerweg;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.VerantwoordelijkeAanleverbestand;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;

import org.junit.Ignore;
import org.junit.Test;

public class ProefgevallenBveFase4Batch1Test extends ProefgevallenBveFase4
{
	@Test
	public void proefgeval01() throws Exception
	{
		createGeval01();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1001"));
		assertThat(melding.getSofinummer(), is("210000004"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910501")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record1.getGevolgdeOpleiding(), is("11014"));
		assertThat(record1.getLeerweg(), is(Leerweg.Beroepsopleidend));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Voltijd));
		assertThat(record1.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
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
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));
	}

	private void createGeval01() throws Exception
	{
		getDeelnemer1001();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval02() throws Exception
	{
		createGeval02();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1001"));
		assertThat(melding.getSofinummer(), is("210000004"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910501")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record1.getGevolgdeOpleiding(), is("11014"));
		assertThat(record1.getLeerweg(), is(Leerweg.Beroepsopleidend));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Voltijd));
		assertThat(record1.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
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
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));
	}

	private void createGeval02() throws Exception
	{
		getDeelnemer1001();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval03() throws Exception
	{
		createGeval03(true);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1001"));
		assertThat(melding.getSofinummer(), is("210000004"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910501")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(3));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("2"));
		assertThat(record1.getGevolgdeOpleiding(), is("10670"));
		assertThat(record1.getLeerweg(), is((Leerweg) null));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Examendeelnemer));
		assertThat(record1.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20081231)));
		assertThat(record1.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(record1.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(record1.getIndicatieRisicodeelnemer(), is((Boolean) null));
		assertThat(record1.getIndicatieGehandicapt(), is(false));
		assertThat(record1.getLaatsteVooropleiding(), is((String) null));
		assertThat(record1.getLocatie(), is((Integer) null));

		PeriodegegevensInschrijvingRecord record2 = melding.getMeldingen().get(1);
		assertThat(record2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(record2.getSoortMutatie(), is(Toevoeging));
		assertThat(record2.getInschrijvingsvolgnummer(), is("2"));
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(false));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));

		ExamengegevensRecord record3 = melding.getMeldingen().get(2);
		assertThat(record3.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		assertThat(record3.getSoortMutatie(), is(Toevoeging));
		assertThat(record3.getInschrijvingsvolgnummer(), is("2"));
		assertThat(record3.getBehaaldeDeelKwalificatie(), is("10670"));
		assertThat(record3.getDatumBehaald(), is(asDate(20080930)));
		assertThat(record3.getIndicatieBekostigingDiploma(), is(true));
	}

	private void createGeval03(boolean addChanges) throws Exception
	{
		getDeelnemer1001();

		builder.addVerbintenisMBO(20080801, 10670, null, Nee);
		verbintenis = builder.getVerbintenis();
		verbintenis.setIntensiteit(Intensiteit.Examendeelnemer);
		verbintenis.setGeplandeEinddatum(asDate(20081231));

		addExamendeelname(verbintenis, 20080930);
		if (addChanges)
		{
			Examendeelname examen = verbintenis.getExamendeelnames().get(0);
			addChange(examen, "examenstatus", null, examen.getExamenstatus());
			addChange(verbintenis, "status", VerbintenisStatus.Voorlopig,
				VerbintenisStatus.Definitief);
		}
	}

	@Test
	public void proefgeval04() throws Exception
	{
		createGeval04(true);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1001"));
		assertThat(melding.getSofinummer(), is("210000004"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910501")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(3));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("3"));
		assertThat(record1.getGevolgdeOpleiding(), is("10508"));
		assertThat(record1.getLeerweg(), is(Leerweg.Beroepsopleidend));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Deeltijd));
		assertThat(record1.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(record1.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(record1.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(record1.getIndicatieRisicodeelnemer(), is((Boolean) null));
		assertThat(record1.getIndicatieGehandicapt(), is(false));
		assertThat(record1.getLaatsteVooropleiding(), is((String) null));
		assertThat(record1.getLocatie(), is((Integer) null));

		PeriodegegevensInschrijvingRecord record2 = melding.getMeldingen().get(1);
		assertThat(record2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(record2.getSoortMutatie(), is(Toevoeging));
		assertThat(record2.getInschrijvingsvolgnummer(), is("3"));
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));

		ExamengegevensRecord record3 = melding.getMeldingen().get(2);
		assertThat(record3.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		assertThat(record3.getSoortMutatie(), is(Toevoeging));
		assertThat(record3.getInschrijvingsvolgnummer(), is("3"));
		assertThat(record3.getBehaaldeDeelKwalificatie(), is("10508"));
		assertThat(record3.getDatumBehaald(), is(asDate(20081231)));
		assertThat(record3.getIndicatieBekostigingDiploma(), is(true));
	}

	private void createGeval04(boolean addChanges) throws Exception
	{
		createGeval03(false);

		builder.addVerbintenisMBO(20080801, 10508, BOL, Ja);
		verbintenis = builder.getVerbintenis();
		verbintenis.setLocatie(locatie);
		verbintenis.setGeplandeEinddatum(asDate(20100101));
		verbintenis.setIntensiteit(Intensiteit.Deeltijd);

		addExamendeelname(verbintenis, 20081231);
		if (addChanges)
		{
			Examendeelname examen = verbintenis.getExamendeelnames().get(0);
			addChange(examen, "examenstatus", null, examen.getExamenstatus());
			addChange(verbintenis, "status", VerbintenisStatus.Voorlopig,
				VerbintenisStatus.Definitief);
		}
	}

	@Test
	public void proefgeval05() throws Exception
	{
		createGeval05(true);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1001"));
		assertThat(melding.getSofinummer(), is("210000004"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910501")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("4"));
		assertThat(record1.getGevolgdeOpleiding(), is("11048"));
		assertThat(record1.getLeerweg(), is(Leerweg.Beroepsbegeleidend));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Voltijd));
		assertThat(record1.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(record1.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(record1.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(record1.getIndicatieRisicodeelnemer(), is((Boolean) null));
		assertThat(record1.getIndicatieGehandicapt(), is(false));
		assertThat(record1.getLaatsteVooropleiding(), is((String) null));
		assertThat(record1.getLocatie(), is((Integer) null));

		PeriodegegevensInschrijvingRecord record2 = melding.getMeldingen().get(1);
		assertThat(record2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(record2.getSoortMutatie(), is(Toevoeging));
		assertThat(record2.getInschrijvingsvolgnummer(), is("4"));
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));
	}

	private void createGeval05(boolean addChanges) throws Exception
	{
		createGeval04(false);
		builder.addVerbintenisMBO(20080801, 11048, BBL, Ja);
		verbintenis = builder.getVerbintenis();
		verbintenis.setLocatie(locatie);
		verbintenis.setGeplandeEinddatum(asDate(20100101));

		if (addChanges)
		{
			addChange(verbintenis, "status", VerbintenisStatus.Voorlopig,
				VerbintenisStatus.Definitief);
		}
	}

	@Test
	public void proefgeval06() throws Exception
	{
		createGeval06();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1001"));
		assertThat(melding.getSofinummer(), is("210000004"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910501")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("5"));
		assertThat(record1.getGevolgdeOpleiding(), is("11001"));
		assertThat(record1.getLeerweg(), is(Leerweg.Beroepsbegeleidend));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Deeltijd));
		assertThat(record1.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(record1.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(record1.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(record1.getIndicatieRisicodeelnemer(), is((Boolean) null));
		assertThat(record1.getIndicatieGehandicapt(), is(false));
		assertThat(record1.getLaatsteVooropleiding(), is((String) null));
		assertThat(record1.getLocatie(), is((Integer) null));

		PeriodegegevensInschrijvingRecord record2 = melding.getMeldingen().get(1);
		assertThat(record2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(record2.getSoortMutatie(), is(Toevoeging));
		assertThat(record2.getInschrijvingsvolgnummer(), is("5"));
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));
	}

	private void createGeval06() throws Exception
	{
		createGeval05(false);
		builder.addVerbintenisMBO(20080801, 11001, BBL, Ja);
		verbintenis = builder.getVerbintenis();
		verbintenis.setLocatie(locatie);
		verbintenis.setGeplandeEinddatum(asDate(20100101));
		verbintenis.setIntensiteit(Intensiteit.Deeltijd);
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval07() throws Exception
	{
		createGeval07();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1009"));
		assertThat(melding.getSofinummer(), is("210000107"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910509")));
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
		assertThat(record1.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(record1.getWerkelijkeDatumUitschrijving(), is(asDate(20080930)));
		assertThat(record1.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(record1.getIndicatieRisicodeelnemer(), is((Boolean) null));
		assertThat(record1.getIndicatieGehandicapt(), is(false));
		assertThat(record1.getLaatsteVooropleiding(), is((String) null));
		assertThat(record1.getLocatie(), is((Integer) null));

		PeriodegegevensInschrijvingRecord record2 = melding.getMeldingen().get(1);
		assertThat(record2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(record2.getSoortMutatie(), is(Toevoeging));
		assertThat(record2.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));
	}

	private void createGeval07() throws Exception
	{
		getDeelnemer1009();
		verbintenis.getOpleiding().getVerbintenisgebied().setExterneCode("11048");
		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.BBL);
		verbintenis.setGeplandeEinddatum(asDate(20100101));
		verbintenis.setEinddatum(asDate(20080930));
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	@Ignore("Oplossen als deelkwalificaties goed werken")
	public void proefgeval08() throws Exception
	{
		// FIXME deelkwalificaties moeten nog geimplementeerd worden of een andere
		// oplossing gevonden worden voor deze testgevallen.
		createGeval08();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1010"));
		assertThat(melding.getSofinummer(), is("210000119"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910510")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(getRecordTypes(melding), is(Arrays.asList(305, 320, 321, 323, 323, 323)));
		assertThat(melding.getMeldingen().size(), is(5));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record1.getGevolgdeOpleiding(), is("11048"));
		assertThat(record1.getLeerweg(), is(Leerweg.Beroepsbegeleidend));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Voltijd));
		assertThat(record1.getDatumInschrijving(), is(asDate(20081101)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
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
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20081101)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));

		ExamengegevensRecord record3 = melding.getMeldingen().get(2);
		assertThat(record3.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		assertThat(record3.getSoortMutatie(), is(Toevoeging));
		assertThat(record3.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record3.getBehaaldeDeelKwalificatie(), is("11048"));
		assertThat(record3.getDatumBehaald(), is(asDate(20090201)));
		assertThat(record3.getIndicatieBekostigingDiploma(), is(true));

		ExamengegevensRecord record4 = melding.getMeldingen().get(3);
		assertThat(record4.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		assertThat(record4.getSoortMutatie(), is(Toevoeging));
		assertThat(record4.getInschrijvingsvolgnummer(), is("2"));
		// VOOR SCHAKELTEST DIENT DIT waarde 1 te hebben, deelkwalificaties worden niet
		// direct ondersteund door EduArte
		// assertThat(record4.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record4.getBehaaldeDeelKwalificatie(), is("10395"));
		assertThat(record4.getDatumBehaald(), is(asDate(20081101)));
		assertThat(record4.getIndicatieBekostigingDiploma(), is(true));

		ExamengegevensRecord record5 = melding.getMeldingen().get(4);
		assertThat(record5.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		assertThat(record5.getSoortMutatie(), is(Toevoeging));
		assertThat(record5.getInschrijvingsvolgnummer(), is("3"));
		// VOOR SCHAKELTEST DIENT DIT waarde 1 te hebben, deelkwalificaties worden niet
		// direct ondersteund door EduArte
		// assertThat(record5.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record5.getBehaaldeDeelKwalificatie(), is("57010"));
		assertThat(record5.getDatumBehaald(), is(asDate(20081101)));
		assertThat(record5.getIndicatieBekostigingDiploma(), is(false));

	}

	private void createGeval08() throws Exception
	{
		getDeelnemer1010();

		verbintenis.setGeplandeEinddatum(asDate(20100101));

		Examendeelname deelname1_ = addExamendeelname(verbintenis, 20090201);

		builder.addVerbintenisMBO(20080801, 10395, BOL, Nee);
		Verbintenis deelkwalificatie2 = builder.getVerbintenis();
		Examendeelname deelname2 = addExamendeelname(deelkwalificatie2, 20081101);

		builder.addVerbintenisMBO(20080801, 57010, BOL, Nee);
		Verbintenis deelkwalificatie3 = builder.getVerbintenis();

		Examendeelname deelname3 = addExamendeelname(deelkwalificatie3, 20081101);
		deelname3.setBekostigd(false);

		addChange(deelname1_, "examenstatus", null, deelname1_.getExamenstatus());
		addChange(deelname2, "examenstatus", null, deelname2.getExamenstatus());
		addChange(deelname3, "examenstatus", null, deelname2.getExamenstatus());

		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	@Ignore("Oplossen als deelkwalificaties goed werken")
	public void proefgeval09() throws Exception
	{
		// FIXME deelkwalificaties moeten nog geimplementeerd worden of een andere
		// oplossing gevonden worden voor deze testgevallen.
		createGeval09();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1011"));
		assertThat(melding.getSofinummer(), is("210000120"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910511")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(getRecordTypes(melding), is(Arrays.asList(305, 320, 321, 323, 323)));
		assertThat(melding.getMeldingen().size(), is(4));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record1.getGevolgdeOpleiding(), is("11048"));
		assertThat(record1.getLeerweg(), is(Leerweg.Beroepsbegeleidend));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Voltijd));
		assertThat(record1.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
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
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));

		ExamengegevensRecord record3 = melding.getMeldingen().get(2);
		assertThat(record3.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		assertThat(record3.getSoortMutatie(), is(Toevoeging));
		assertThat(record3.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record3.getBehaaldeDeelKwalificatie(), is("11048"));
		assertThat(record3.getDatumBehaald(), is(asDate(20081101)));
		assertThat(record3.getIndicatieBekostigingDiploma(), is(true));

		ExamengegevensRecord record4 = melding.getMeldingen().get(3);
		assertThat(record4.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		assertThat(record4.getSoortMutatie(), is(Toevoeging));
		assertThat(record4.getInschrijvingsvolgnummer(), is("2"));
		// VOOR SCHAKELTEST DIENT DIT waarde 1 te hebben, deelkwalificaties worden niet
		// direct ondersteund door EduArte
		// assertThat(record4.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record4.getBehaaldeDeelKwalificatie(), is("10670"));
		assertThat(record4.getDatumBehaald(), is(asDate(20081101)));
		assertThat(record4.getIndicatieBekostigingDiploma(), is(true));
	}

	private void createGeval09() throws Exception
	{
		getDeelnemer1011();

		verbintenis.setGeplandeEinddatum(asDate(20100101));

		Examendeelname deelname1_ = addExamendeelname(verbintenis, 20081101);

		builder.addVerbintenisMBO(20081101, 10670, BOL, Nee);

		Verbintenis deelkwalificatie2 = builder.getVerbintenis();
		Examendeelname deelname2 = addExamendeelname(deelkwalificatie2, 20081101);

		addChange(deelname1_, "examenstatus", null, deelname1_.getExamenstatus());
		addChange(deelname2, "examenstatus", null, deelname2.getExamenstatus());

		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval10() throws Exception
	{
		createGeval10();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1012"));
		assertThat(melding.getSofinummer(), is("210000132"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910512")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(record1.getSoortMutatie(), is(Toevoeging));
		assertThat(record1.getInschrijvingsvolgnummer(), is("1"));
		assertThat(record1.getGevolgdeOpleiding(), is("90034"));
		assertThat(record1.getLeerweg(), is(Leerweg.Beroepsbegeleidend));
		assertThat(record1.getIntensiteit(), is(Intensiteit.Voltijd));
		assertThat(record1.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(record1.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
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
		assertThat(record2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(record2.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(record2.getIndicatieLesgeld(), is((String) null));
	}

	private void createGeval10() throws Exception
	{
		getDeelnemer1012();

		verbintenis.getOpleiding().getVerbintenisgebied().setExterneCode("90034");
		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.BBL);
		verbintenis.setGeplandeEinddatum(asDate(20100101));

		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	@Ignore("Oplossen als deelkwalificaties goed werken")
	public void bveBO1Batch1() throws Exception
	{
		createGeval01();
		createGeval02();
		createGeval03(true);
		createGeval04(true);
		createGeval05(true);
		createGeval06();
		createGeval07();
		createGeval08();
		createGeval09();
		createGeval10();

		batchBVE = maakBronBOBatch();
		batchBVE.setBatchNummer(1);
		batchBVE.getAanleverpunt().setNummer(1);

		assertThat(batchBVE.getBrinNummer(), is("01AA"));
		assertThat(batchBVE.getAanleverPuntNummer(), is(1));
		assertThat(batchBVE.getSectordeel(), is(Sectordeel.Beroepsonderwijs));

		assertThat(batchBVE.getInternOrganisatieNummer(), is((String) null));
		assertThat(batchBVE.getBatchNummer(), is(1));
		assertThat(batchBVE.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(batchBVE.getVersieProgrammaVanEisen(), is("1.1"));

		assertThat(batchBVE.getAantalMeldingen(), is(10));
		assertThat(batchBVE.getAantalRecords(), is(38));
		assertThat(batchBVE.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanleverbestand.Instelling));
		assertThat(batchBVE.getLaatsteAanlevering(), is(false));

		writeBronBatch(batchBVE);
	}
}