package nl.topicus.eduarte.krd.bron.schakeltest.vo.examenresultaten;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronVakGegegevensVOMelding;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingSchoolExamen;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingWerkstuk;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ExamenUitslag;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ToepassingResultaat;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Ignore;
import org.junit.Test;

public class ProefgevallenVOExamensBatch5Test extends ProefgevallenVOExamens
{
	@Test
	@Ignore
	public void proefgeval23() throws Exception
	{
		createGeval23();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval23(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(1));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Verwijdering));
		assertThat(melding.getSofiNummer(), is(320000011));
		assertThat(melding.getLeerlingNummerInstelling(), is("1"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(2551));
		assertThat(melding.getExamenJaar(), is(2009));
	}

	private void createVakGegegevensGeval23(BronExamenresultaatVOMelding melding)
	{
		melding.setILTCode(2551);
	}

	private void createGeval23() throws Exception
	{
		getDeelnemer1();
		verbintenis.setLocatie(getLocatie("00DI01"));
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", deelname
			.getExamenstatus(), null);

	}

	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void proefgeval24() throws Exception
	{
		createGeval24();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval24(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(340000004));
		assertThat(melding.getLeerlingNummerInstelling(), is("5"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19891219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(150));
		assertThat(melding.getExamenJaar(), is(2009));
		assertThat(melding.getDatumUitslagExamen(), is(asDate(20090430)));
		assertThat(melding.getUitslagExamen(), is(ExamenUitslag.Geslaagd));
		assertThat(melding.getTitelOfThemaWerkstuk(), is("Werkstuk 24"));
		assertThat(melding.getBeoordelingWerkstuk(), is(BeoordelingWerkstuk.Goed));
		assertThat(melding.getToepassingBeoordelingWerkstuk(),
			is(ToepassingResultaat.GeexamineerdInJaarVanMelding));

		List<BronVakGegegevensVOMelding> vakMeldingen = melding.getVakgegevens();
		assertThat(vakMeldingen.size(), is(14));
		BronVakGegegevensVOMelding vakMelding = vakMeldingen.get(0);
		assertThat(vakMelding.getExamenVak(), is(11));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(90));
		assertThat(vakMelding.getCijferCE1(), is(80));
		assertThat(vakMelding.getEersteEindcijfer(), is(9));
		assertThat(vakMelding.getCijferCijferlijst(), is(9));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(1);
		assertThat(vakMelding.getExamenVak(), is(51));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(75));
		assertThat(vakMelding.getCijferCE1(), is(80));
		assertThat(vakMelding.getEersteEindcijfer(), is(8));
		assertThat(vakMelding.getCijferCijferlijst(), is(8));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(2);
		assertThat(vakMelding.getExamenVak(), is(52));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCE1(), is(40));
		assertThat(vakMelding.getEersteEindcijfer(), is(5));
		assertThat(vakMelding.getCijferCijferlijst(), is(5));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(3);
		assertThat(vakMelding.getExamenVak(), is(61));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(90));
		assertThat(vakMelding.getCijferCE1(), is(90));
		assertThat(vakMelding.getEersteEindcijfer(), is(9));
		assertThat(vakMelding.getCijferCijferlijst(), is(9));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(4);
		assertThat(vakMelding.getExamenVak(), is(71));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(70));
		assertThat(vakMelding.getCijferCE1(), is(70));
		assertThat(vakMelding.getEersteEindcijfer(), is(7));
		assertThat(vakMelding.getCijferCijferlijst(), is(7));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(5);
		assertThat(vakMelding.getExamenVak(), is(301));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCE1(), is(60));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(6);
		assertThat(vakMelding.getExamenVak(), is(351));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getBeoordelingSchoolExamen(), is(BeoordelingSchoolExamen.Goed));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(7);
		assertThat(vakMelding.getExamenVak(), is(361));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCE1(), is(60));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(8);
		assertThat(vakMelding.getExamenVak(), is(401));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getBeoordelingSchoolExamen(), is(BeoordelingSchoolExamen.Goed));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(9);
		assertThat(vakMelding.getExamenVak(), is(122));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(90));
		assertThat(vakMelding.getCijferCE1(), is(70));
		assertThat(vakMelding.getEersteEindcijfer(), is(8));
		assertThat(vakMelding.getCijferCijferlijst(), is(8));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(10);
		assertThat(vakMelding.getExamenVak(), is(321));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(70));
		assertThat(vakMelding.getCijferCE1(), is(80));
		assertThat(vakMelding.getEersteEindcijfer(), is(8));
		assertThat(vakMelding.getCijferCijferlijst(), is(8));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(11);
		assertThat(vakMelding.getExamenVak(), is(152));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCE1(), is(70));
		assertThat(vakMelding.getEersteEindcijfer(), is(7));
		assertThat(vakMelding.getCijferCijferlijst(), is(7));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(12);
		assertThat(vakMelding.getExamenVak(), is(172));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(40));
		assertThat(vakMelding.getCijferCE1(), is(60));
		assertThat(vakMelding.getEersteEindcijfer(), is(5));
		assertThat(vakMelding.getCijferCijferlijst(), is(5));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(13);
		assertThat(vakMelding.getExamenVak(), is(183));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(true));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCE1(), is(50));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
	}

	private void createVakGegegevensGeval24(BronExamenresultaatVOMelding melding)
	{
		melding.setILTCode(150);
		melding.setDatumUitslagExamen(asDate(20090430));
		melding.setUitslagExamen(ExamenUitslag.Geslaagd);
		melding.setTitelOfThemaWerkstuk("Werkstuk 24");
		melding.setBeoordelingWerkstuk(BeoordelingWerkstuk.Goed);
		melding.setToepassingBeoordelingWerkstuk(ToepassingResultaat.GeexamineerdInJaarVanMelding);

		List<BronVakGegegevensVOMelding> meldingen = new ArrayList<BronVakGegegevensVOMelding>();
		meldingen.add(createSeGegevens(melding, 11, false, false, null, null, 90, 80, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 51, false, false, null, null, 75, 80, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 52, false, false, null, null, 60, 40, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 61, false, false, null, null, 90, 90, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 71, false, false, null, null, 70, 70, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 301, false, false, null, null, 60, 60, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 351, false, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, null, null, null, false));
		meldingen.add(createSeGegevens(melding, 361, false, false, null, null, 60, 60, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 401, false, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, null, null, null, false));
		meldingen.add(createSeGegevens(melding, 122, false, false, null, null, 90, 70, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 321, false, false, null, null, 70, 80, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 152, false, false, null, null, 60, 70, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 172, false, false, null, null, 40, 60, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 183, false, true, null, null, 60, 50, false, null,
			null, null, true));
		melding.setVakgegevens(meldingen);
	}

	private void createGeval24() throws Exception
	{
		getDeelnemer5();
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());
	}

	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void proefgeval25() throws Exception
	{
		createGeval25();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval25(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(310000002));
		assertThat(melding.getLeerlingNummerInstelling(), is("6"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19891231)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(150));
		assertThat(melding.getExamenJaar(), is(2009));
		assertThat(melding.getDatumUitslagExamen(), is(asDate(20090430)));
		assertThat(melding.getUitslagExamen(), is(ExamenUitslag.Geslaagd));
		assertThat(melding.getTitelOfThemaWerkstuk(), is("Werkstuk 25"));
		assertThat(melding.getBeoordelingWerkstuk(), is(BeoordelingWerkstuk.Goed));
		assertThat(melding.getToepassingBeoordelingWerkstuk(),
			is(ToepassingResultaat.GeexamineerdInJaarVanMelding));

		List<BronVakGegegevensVOMelding> vakMeldingen = melding.getVakgegevens();
		assertThat(vakMeldingen.size(), is(14));
		BronVakGegegevensVOMelding vakMelding = vakMeldingen.get(0);
		assertThat(vakMelding.getExamenVak(), is(11));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(90));
		assertThat(vakMelding.getCijferCE1(), is(80));
		assertThat(vakMelding.getEersteEindcijfer(), is(9));
		assertThat(vakMelding.getCijferCijferlijst(), is(9));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(1);
		assertThat(vakMelding.getExamenVak(), is(51));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(75));
		assertThat(vakMelding.getCijferCE1(), is(80));
		assertThat(vakMelding.getEersteEindcijfer(), is(8));
		assertThat(vakMelding.getCijferCijferlijst(), is(8));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(2);
		assertThat(vakMelding.getExamenVak(), is(52));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCE1(), is(40));
		assertThat(vakMelding.getEersteEindcijfer(), is(5));
		assertThat(vakMelding.getCijferCijferlijst(), is(5));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(3);
		assertThat(vakMelding.getExamenVak(), is(61));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(90));
		assertThat(vakMelding.getCijferCE1(), is(90));
		assertThat(vakMelding.getEersteEindcijfer(), is(9));
		assertThat(vakMelding.getCijferCijferlijst(), is(9));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(4);
		assertThat(vakMelding.getExamenVak(), is(71));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(70));
		assertThat(vakMelding.getCijferCE1(), is(70));
		assertThat(vakMelding.getEersteEindcijfer(), is(7));
		assertThat(vakMelding.getCijferCijferlijst(), is(7));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(5);
		assertThat(vakMelding.getExamenVak(), is(301));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCE1(), is(60));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(6);
		assertThat(vakMelding.getExamenVak(), is(351));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getBeoordelingSchoolExamen(), is(BeoordelingSchoolExamen.Goed));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(7);
		assertThat(vakMelding.getExamenVak(), is(361));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCE1(), is(60));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(8);
		assertThat(vakMelding.getExamenVak(), is(401));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getBeoordelingSchoolExamen(), is(BeoordelingSchoolExamen.Goed));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(9);
		assertThat(vakMelding.getExamenVak(), is(122));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(90));
		assertThat(vakMelding.getCijferCE1(), is(70));
		assertThat(vakMelding.getEersteEindcijfer(), is(8));
		assertThat(vakMelding.getCijferCijferlijst(), is(8));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(10);
		assertThat(vakMelding.getExamenVak(), is(321));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(70));
		assertThat(vakMelding.getCijferCE1(), is(80));
		assertThat(vakMelding.getEersteEindcijfer(), is(8));
		assertThat(vakMelding.getCijferCijferlijst(), is(8));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(11);
		assertThat(vakMelding.getExamenVak(), is(152));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCE1(), is(70));
		assertThat(vakMelding.getEersteEindcijfer(), is(7));
		assertThat(vakMelding.getCijferCijferlijst(), is(7));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(12);
		assertThat(vakMelding.getExamenVak(), is(172));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(40));
		assertThat(vakMelding.getCijferCE1(), is(60));
		assertThat(vakMelding.getEersteEindcijfer(), is(5));
		assertThat(vakMelding.getCijferCijferlijst(), is(5));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(13);
		assertThat(vakMelding.getExamenVak(), is(183));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(true));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCE1(), is(50));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
	}

	private void createVakGegegevensGeval25(BronExamenresultaatVOMelding melding)
	{
		melding.setILTCode(150);
		melding.setDatumUitslagExamen(asDate(20090430));
		melding.setUitslagExamen(ExamenUitslag.Geslaagd);
		melding.setTitelOfThemaWerkstuk("Werkstuk 25");
		melding.setBeoordelingWerkstuk(BeoordelingWerkstuk.Goed);
		melding.setToepassingBeoordelingWerkstuk(ToepassingResultaat.GeexamineerdInJaarVanMelding);

		List<BronVakGegegevensVOMelding> meldingen = new ArrayList<BronVakGegegevensVOMelding>();
		meldingen.add(createSeGegevens(melding, 11, true, false, null, null, 90, 80, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 51, true, false, null, null, 75, 80, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 52, true, false, null, null, 60, 40, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 61, true, false, null, null, 90, 90, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 71, true, false, null, null, 70, 70, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 301, true, false, null, null, 60, 60, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 351, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, null, null, null, false));
		meldingen.add(createSeGegevens(melding, 361, true, false, null, null, 60, 60, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 401, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, null, null, null, false));
		meldingen.add(createSeGegevens(melding, 122, true, false, null, null, 90, 70, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 321, true, false, null, null, 70, 80, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 152, true, false, null, null, 60, 70, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 172, true, false, null, null, 40, 60, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 183, true, true, null, null, 60, 50, false, null,
			null, null, true));
		melding.setVakgegevens(meldingen);
	}

	private void createGeval25() throws Exception
	{
		getDeelnemer6();
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		deelnemer.getPersoon().setGeboortedatum(asDate(19891231));
		deelnemer.getPersoon().setGeslacht(Geslacht.Vrouw);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());
	}

	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void proefgeval26() throws Exception
	{
		createGeval26();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval26(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(820000012));
		assertThat(melding.getLeerlingNummerInstelling(), is("9"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(373));
		assertThat(melding.getExamenJaar(), is(2009));
		assertThat(melding.getDatumUitslagExamen(), is(asDate(20090428)));
		assertThat(melding.getUitslagExamen(), is(ExamenUitslag.Geslaagd));
		assertThat(melding.getTitelOfThemaWerkstuk(), is("Werkstuk 26"));
		assertThat(melding.getBeoordelingWerkstuk(), is(BeoordelingWerkstuk.Goed));

		List<BronVakGegegevensVOMelding> vakMeldingen = melding.getVakgegevens();
		assertThat(vakMeldingen.size(), is(11));
		BronVakGegegevensVOMelding vakMelding = vakMeldingen.get(0);
		assertThat(vakMelding.getExamenVak(), is(1001));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(50));
		assertThat(vakMelding.getCijferCE1(), is(60));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

		vakMelding = vakMeldingen.get(1);
		assertThat(vakMelding.getExamenVak(), is(1002));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(55));
		assertThat(vakMelding.getCijferCE1(), is(55));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

		vakMelding = vakMeldingen.get(2);
		assertThat(vakMelding.getExamenVak(), is(1019));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(true));
		assertThat(vakMelding.getCijferSchoolExamen(), is(62));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(true));

		vakMelding = vakMeldingen.get(3);
		assertThat(vakMelding.getExamenVak(), is(353));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getBeoordelingSchoolExamen(), is(BeoordelingSchoolExamen.Goed));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

		vakMelding = vakMeldingen.get(4);
		assertThat(vakMelding.getExamenVak(), is(301));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(58));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(true));

		vakMelding = vakMeldingen.get(5);
		assertThat(vakMelding.getExamenVak(), is(1020));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getBeoordelingSchoolExamen(), is(BeoordelingSchoolExamen.Voldoende));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

		vakMelding = vakMeldingen.get(6);
		assertThat(vakMelding.getExamenVak(), is(1005));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(70));
		assertThat(vakMelding.getCijferCE1(), is(90));
		assertThat(vakMelding.getEersteEindcijfer(), is(8));
		assertThat(vakMelding.getCijferCijferlijst(), is(8));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

		vakMelding = vakMeldingen.get(7);
		assertThat(vakMelding.getExamenVak(), is(1017));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(50));
		assertThat(vakMelding.getCijferCE1(), is(50));
		assertThat(vakMelding.getEersteEindcijfer(), is(5));
		assertThat(vakMelding.getCijferCijferlijst(), is(5));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

		vakMelding = vakMeldingen.get(8);
		assertThat(vakMelding.getExamenVak(), is(1022));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(65));
		assertThat(vakMelding.getCijferCE1(), is(60));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

		vakMelding = vakMeldingen.get(9);
		assertThat(vakMelding.getExamenVak(), is(1021));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(45));
		assertThat(vakMelding.getCijferCE1(), is(51));
		assertThat(vakMelding.getEersteEindcijfer(), is(5));
		assertThat(vakMelding.getCijferCijferlijst(), is(5));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

		vakMelding = vakMeldingen.get(10);
		assertThat(vakMelding.getExamenVak(), is(1025));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(75));
		assertThat(vakMelding.getCijferCE1(), is(71));
		assertThat(vakMelding.getEersteEindcijfer(), is(7));
		assertThat(vakMelding.getCijferCijferlijst(), is(7));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

	}

	private void createVakGegegevensGeval26(BronExamenresultaatVOMelding melding)
	{
		melding.setILTCode(373);
		melding.setDatumUitslagExamen(asDate(20090428));
		melding.setUitslagExamen(ExamenUitslag.Geslaagd);
		melding.setTitelOfThemaWerkstuk("Werkstuk 26");
		melding.setBeoordelingWerkstuk(BeoordelingWerkstuk.Goed);

		List<BronVakGegegevensVOMelding> meldingen = new ArrayList<BronVakGegegevensVOMelding>();
		meldingen.add(createSeGegevens(melding, 1001, true, false, null, null, 50, 60, false, null,
			false, null, true));
		meldingen.add(createSeGegevens(melding, 1002, true, false, null, null, 55, 55, false, null,
			false, null, true));
		meldingen.add(createSeGegevens(melding, 1019, true, true, null, null, 62, null, false,
			null, true, null, true));
		meldingen.add(createSeGegevens(melding, 353, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, null, false, null, false));
		meldingen.add(createSeGegevens(melding, 301, true, false, null, null, 58, null, false,
			null, true, null, true));
		meldingen.add(createSeGegevens(melding, 1020, true, false, null,
			BeoordelingSchoolExamen.Voldoende, null, null, false, null, false, null, false));
		meldingen.add(createSeGegevens(melding, 1005, true, false, null, null, 70, 90, false, null,
			false, null, true));
		meldingen.add(createSeGegevens(melding, 1017, true, false, null, null, 50, 50, false, null,
			false, null, true));
		meldingen.add(createSeGegevens(melding, 1022, true, false, null, null, 65, 60, false, null,
			false, null, true));
		meldingen.add(createSeGegevens(melding, 1021, true, false, null, null, 45, 51, false, null,
			false, null, true));
		meldingen.add(createSeGegevens(melding, 1025, true, false, null, null, 75, 71, false, null,
			false, null, true));
		melding.setVakgegevens(meldingen);
	}

	private void createGeval26() throws Exception
	{
		getDeelnemer9();
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());
	}

	@Test
	@Ignore
	public void VOBatch2Test() throws Exception
	{
		createGeval23();
		createGeval24();
		createGeval25();
		createGeval26();

		examenBatchVO = maakBronExamenVOBatch();
		createVakGegegevensGeval23(getBronExamenresultaatVOMelding(0));
		createVakGegegevensGeval24(getBronExamenresultaatVOMelding(1));
		createVakGegegevensGeval25(getBronExamenresultaatVOMelding(2));
		createVakGegegevensGeval26(getBronExamenresultaatVOMelding(3));
		examenBatchVO.setBatchNummer(6);

		assertThat(examenBatchVO.getBRINNummer(), is("00DI"));
		assertThat(examenBatchVO.getAanleverPuntNummer(), is(0));
		assertThat(examenBatchVO.getBatchNummer(), is(6));
		assertThat(examenBatchVO.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(examenBatchVO.getSoortMelding(), is(SoortMelding.Examenresultaat));

		assertThat(examenBatchVO.getAantalRecords(), is(4));
		assertThat(examenBatchVO.getAantalToevoegingen(), is(3));
		assertThat(examenBatchVO.getAantalVerwijderingen(), is(1));

		assertThat(examenBatchVO.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanlevering.Instelling));

		writeBronBatch(examenBatchVO);

	}
}
