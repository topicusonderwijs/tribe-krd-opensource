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
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.HogerNiveau;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Ignore;
import org.junit.Test;

public class ProefgevallenVOExamensBatch3Test extends ProefgevallenVOExamens
{
	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void proefgeval16() throws Exception
	{
		createGeval16();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval16(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(860000011));
		assertThat(melding.getLeerlingNummerInstelling(), is("4"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19891219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(150));
		assertThat(melding.getExamenJaar(), is(2009));
		assertThat(melding.getDatumUitslagExamen(), is(asDate(20090514)));
		assertThat(melding.getUitslagExamen(), is(ExamenUitslag.Geslaagd));
		assertThat(melding.getTitelOfThemaWerkstuk(), is("Werkstuk 16"));
		assertThat(melding.getBeoordelingWerkstuk(), is(BeoordelingWerkstuk.Goed));
		assertThat(melding.getCijferWerkstuk(), is(75));

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
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(75));
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
		assertThat(vakMelding.getCijferSchoolExamen(), is(70));
		assertThat(vakMelding.getCijferCijferlijst(), is(7));
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
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(10);
		assertThat(vakMelding.getExamenVak(), is(321));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(90));
		assertThat(vakMelding.getCijferCijferlijst(), is(9));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(11);
		assertThat(vakMelding.getExamenVak(), is(152));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(70));
		assertThat(vakMelding.getCijferCE1(), is(70));
		assertThat(vakMelding.getEersteEindcijfer(), is(7));
		assertThat(vakMelding.getCijferCijferlijst(), is(7));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(12);
		assertThat(vakMelding.getExamenVak(), is(172));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCE1(), is(60));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(13);
		assertThat(vakMelding.getExamenVak(), is(183));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(true));
		assertThat(vakMelding.getCijferSchoolExamen(), is(50));
		assertThat(vakMelding.getCijferCE1(), is(50));
		assertThat(vakMelding.getEersteEindcijfer(), is(5));
		assertThat(vakMelding.getCijferCijferlijst(), is(5));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
	}

	private void createVakGegegevensGeval16(BronExamenresultaatVOMelding melding)
	{
		melding.setILTCode(150);
		melding.setDatumUitslagExamen(asDate(20090514));
		melding.setUitslagExamen(ExamenUitslag.Geslaagd);
		melding.setTitelOfThemaWerkstuk("Werkstuk 16");
		melding.setBeoordelingWerkstuk(BeoordelingWerkstuk.Goed);
		melding.setCijferWerkstuk(75);

		List<BronVakGegegevensVOMelding> meldingen = new ArrayList<BronVakGegegevensVOMelding>();
		meldingen.add(createSeGegevens(melding, 11, true, false, null, null, 90, 80, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 51, false, false, null, null, 75, null, false,
			null, null, null, true));
		meldingen.add(createSeGegevens(melding, 52, true, false, null, null, 60, 40, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 61, true, false, null, null, 90, null, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 71, true, false, null, null, 70, 70, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 301, true, false, null, null, 60, null, false,
			null, null, null, true));
		meldingen.add(createSeGegevens(melding, 351, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, null, null, null, false));
		meldingen.add(createSeGegevens(melding, 361, true, false, null, null, 70, null, false,
			null, null, null, true));
		meldingen.add(createSeGegevens(melding, 401, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, null, null, null, false));
		meldingen.add(createSeGegevens(melding, 122, true, false, null, null, 60, null, false,
			null, null, null, true));
		meldingen.add(createSeGegevens(melding, 321, true, false, null, null, 90, null, false,
			null, null, null, true));
		meldingen.add(createSeGegevens(melding, 152, true, false, null, null, 70, 70, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 172, true, false, null, null, 60, 60, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 183, true, true, null, null, 50, 50, false, null,
			null, null, true));
		melding.setVakgegevens(meldingen);
	}

	private void createGeval16() throws Exception
	{
		getDeelnemer4();
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());

	}

	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void proefgeval17() throws Exception
	{
		createGeval17();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval17(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(810000003));
		assertThat(melding.getLeerlingNummerInstelling(), is("8"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(372));
		assertThat(melding.getExamenJaar(), is(2009));
		assertThat(melding.getDatumUitslagExamen(), is(asDate(20090514)));
		assertThat(melding.getUitslagExamen(), is(ExamenUitslag.Afgewezen));
		assertThat(melding.getTitelOfThemaWerkstuk(), is("Werkstuk 17"));
		assertThat(melding.getBeoordelingWerkstuk(), is(BeoordelingWerkstuk.Onafgerond));

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
		assertThat(vakMelding.getCijferSchoolExamen(), is(45));
		assertThat(vakMelding.getCijferCE1(), is(55));
		assertThat(vakMelding.getEersteEindcijfer(), is(5));
		assertThat(vakMelding.getCijferCijferlijst(), is(5));
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
		assertThat(vakMelding.getCijferSchoolExamen(), is(53));
		assertThat(vakMelding.getCijferCijferlijst(), is(5));
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
		assertThat(vakMelding.getExamenVak(), is(1024));
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

	private void createVakGegegevensGeval17(BronExamenresultaatVOMelding melding)
	{
		melding.setILTCode(372);
		melding.setDatumUitslagExamen(asDate(20090514));
		melding.setUitslagExamen(ExamenUitslag.Afgewezen);
		melding.setTitelOfThemaWerkstuk("Werkstuk 17");
		melding.setBeoordelingWerkstuk(BeoordelingWerkstuk.Onafgerond);

		List<BronVakGegegevensVOMelding> meldingen = new ArrayList<BronVakGegegevensVOMelding>();
		meldingen.add(createSeGegevens(melding, 1001, true, false, null, null, 50, 60, false, null,
			false, null, true));
		meldingen.add(createSeGegevens(melding, 1002, true, false, null, null, 45, 55, false, null,
			false, null, true));
		meldingen.add(createSeGegevens(melding, 1019, true, true, null, null, 62, null, false,
			null, true, null, true));
		meldingen.add(createSeGegevens(melding, 353, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, null, false, null, false));
		meldingen.add(createSeGegevens(melding, 301, true, false, null, null, 53, null, false,
			null, true, null, true));
		meldingen.add(createSeGegevens(melding, 1020, true, false, null,
			BeoordelingSchoolExamen.Voldoende, null, null, false, null, false, null, false));
		meldingen.add(createSeGegevens(melding, 1005, true, false, null, null, 70, 90, false, null,
			false, null, true));
		meldingen.add(createSeGegevens(melding, 1024, true, false, null, null, 50, 50, false, null,
			false, null, true));
		meldingen.add(createSeGegevens(melding, 1022, true, false, null, null, 65, 60, false, null,
			false, null, true));
		meldingen.add(createSeGegevens(melding, 1021, true, false, null, null, 45, 51, false, null,
			false, null, true));
		meldingen.add(createSeGegevens(melding, 1025, true, false, null, null, 75, 71, false, null,
			false, null, true));
		melding.setVakgegevens(meldingen);
	}

	private void createGeval17() throws Exception
	{
		getDeelnemer8();
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());
	}

	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void proefgeval18() throws Exception
	{
		createGeval18();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval18(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(1));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(330000007));
		assertThat(melding.getLeerlingNummerInstelling(), is("2"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(2551));
		assertThat(melding.getExamenJaar(), is(2009));
		assertThat(melding.getDatumUitslagExamen(), is(asDate(20090514)));
		assertThat(melding.getUitslagExamen(), is(ExamenUitslag.Geslaagd));
		assertThat(melding.getTitelOfThemaWerkstuk(), is("Werkstuk 18"));
		assertThat(melding.getBeoordelingWerkstuk(), is(BeoordelingWerkstuk.Goed));

		List<BronVakGegegevensVOMelding> vakMeldingen = melding.getVakgegevens();
		assertThat(vakMeldingen.size(), is(8));
		BronVakGegegevensVOMelding vakMelding = vakMeldingen.get(0);
		assertThat(vakMelding.getExamenVak(), is(11));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getHogerNiveau(), is(HogerNiveau.VMBOTheoretischeLeerweg));
		assertThat(vakMelding.getCijferSchoolExamen(), is(100));
		assertThat(vakMelding.getCijferCE1(), is(50));
		assertThat(vakMelding.getEersteEindcijfer(), is(8));
		assertThat(vakMelding.getCijferCijferlijst(), is(8));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getCertificaat(), is(false));
		assertThat(vakMelding.getVakCodeHogerNiveau(), is(11));

		vakMelding = vakMeldingen.get(1);
		assertThat(vakMelding.getExamenVak(), is(71));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(90));
		assertThat(vakMelding.getCijferCE1(), is(60));
		assertThat(vakMelding.getEersteEindcijfer(), is(8));
		assertThat(vakMelding.getCijferCijferlijst(), is(8));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getCertificaat(), is(false));

		vakMelding = vakMeldingen.get(2);
		assertThat(vakMelding.getExamenVak(), is(322));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(80));
		assertThat(vakMelding.getCijferCijferlijst(), is(8));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getCertificaat(), is(false));

		vakMelding = vakMeldingen.get(3);
		assertThat(vakMelding.getExamenVak(), is(353));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getBeoordelingSchoolExamen(), is(BeoordelingSchoolExamen.Goed));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getCertificaat(), is(false));

		vakMelding = vakMeldingen.get(4);
		assertThat(vakMelding.getExamenVak(), is(416));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getBeoordelingSchoolExamen(), is(BeoordelingSchoolExamen.Goed));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getCertificaat(), is(false));

		vakMelding = vakMeldingen.get(5);
		assertThat(vakMelding.getExamenVak(), is(801));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(70));
		assertThat(vakMelding.getCijferCE1(), is(80));
		// Hier is cijfer niet precies het gemiddelde, dus wordt het voor het gemaak even
		// ingevuld zoals op de testshteet staat
		vakMelding.setEersteEindcijfer(7);
		vakMelding.setCijferCijferlijst(7);
		assertThat(vakMelding.getEersteEindcijfer(), is(7));
		assertThat(vakMelding.getCijferCijferlijst(), is(7));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getCertificaat(), is(false));

		vakMelding = vakMeldingen.get(6);
		assertThat(vakMelding.getExamenVak(), is(153));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCE1(), is(90));
		// volgens mij ook een foutje in de testsheet daar staat dat onderstaande een 7
		// moet zijn
		vakMelding.setEersteEindcijfer(7);
		vakMelding.setCijferCijferlijst(7);
		assertThat(vakMelding.getEersteEindcijfer(), is(7));
		assertThat(vakMelding.getCijferCijferlijst(), is(7));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getCertificaat(), is(false));

		vakMelding = vakMeldingen.get(7);
		assertThat(vakMelding.getExamenVak(), is(233));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(50));
		assertThat(vakMelding.getCijferCE1(), is(90));
		// volgens mij ook een foutje in de testsheet daar staat dat onderstaande een 6
		// moet zijn
		vakMelding.setEersteEindcijfer(6);
		vakMelding.setCijferCijferlijst(6);
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getCertificaat(), is(false));
	}

	private void createVakGegegevensGeval18(BronExamenresultaatVOMelding melding)
	{
		melding.setILTCode(2551);
		melding.setDatumUitslagExamen(asDate(20090514));
		melding.setUitslagExamen(ExamenUitslag.Geslaagd);
		melding.setTitelOfThemaWerkstuk("Werkstuk 18");
		melding.setBeoordelingWerkstuk(BeoordelingWerkstuk.Goed);

		List<BronVakGegegevensVOMelding> meldingen = new ArrayList<BronVakGegegevensVOMelding>();
		meldingen.add(createSeGegevens(melding, 11, true, false,
			HogerNiveau.VMBOTheoretischeLeerweg, null, 100, 50, false, false, false, 11, true));
		meldingen.add(createSeGegevens(melding, 71, true, false, null, null, 90, 60, false, false,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 322, true, false, null, null, 80, null, false,
			false, null, null, true));
		meldingen.add(createSeGegevens(melding, 353, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, false, null, null, false));
		meldingen.add(createSeGegevens(melding, 416, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, false, null, null, false));
		meldingen.add(createSeGegevens(melding, 801, true, false, null, null, 70, 80, false, false,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 153, true, false, null, null, 60, 90, false, false,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 233, true, false, null, null, 50, 90, false, false,
			null, null, true));

		melding.setVakgegevens(meldingen);
	}

	private void createGeval18() throws Exception
	{
		getDeelnemer2();
		verbintenis.setLocatie(getLocatie("00DI01"));
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());
	}

	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void VOBatch2Test() throws Exception
	{
		createGeval16();
		createGeval17();
		createGeval18();

		examenBatchVO = maakBronExamenVOBatch();
		createVakGegegevensGeval16(getBronExamenresultaatVOMelding(0));
		createVakGegegevensGeval17(getBronExamenresultaatVOMelding(1));
		examenBatchVO.setBatchNummer(4);
		examenBatchVO.berekenControleTotalen();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(2);
		createVakGegegevensGeval18(melding);
		List<BronVakGegegevensVOMelding> vakGegevens = melding.getVakgegevens();
		BronVakGegegevensVOMelding vakMelding = vakGegevens.get(5);
		vakMelding.setEersteEindcijfer(7);
		vakMelding.setCijferCijferlijst(7);
		vakMelding = vakGegevens.get(6);
		vakMelding.setEersteEindcijfer(7);
		vakMelding.setCijferCijferlijst(7);
		vakMelding = vakGegevens.get(7);
		vakMelding.setEersteEindcijfer(6);
		vakMelding.setCijferCijferlijst(6);

		assertThat(examenBatchVO.getBRINNummer(), is("00DI"));
		assertThat(examenBatchVO.getAanleverPuntNummer(), is(0));
		assertThat(examenBatchVO.getBatchNummer(), is(4));
		assertThat(examenBatchVO.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(examenBatchVO.getSoortMelding(), is(SoortMelding.Examenresultaat));

		assertThat(examenBatchVO.getAantalRecords(), is(3));
		assertThat(examenBatchVO.getAantalToevoegingen(), is(3));

		assertThat(examenBatchVO.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanlevering.Instelling));

		writeBronBatch(examenBatchVO);

	}
}
