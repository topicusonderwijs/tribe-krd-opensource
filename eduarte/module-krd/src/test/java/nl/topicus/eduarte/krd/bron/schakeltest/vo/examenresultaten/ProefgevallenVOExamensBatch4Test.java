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
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ToepassingResultaat;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Ignore;
import org.junit.Test;

public class ProefgevallenVOExamensBatch4Test extends ProefgevallenVOExamens
{
	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void proefgeval19() throws Exception
	{
		createGeval19();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval19(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(830000008));
		assertThat(melding.getLeerlingNummerInstelling(), is("3"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19891219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(150));
		assertThat(melding.getExamenJaar(), is(2009));
		assertThat(melding.getDatumUitslagExamen(), is(asDate(20090430)));
		assertThat(melding.getUitslagExamen(), is(ExamenUitslag.Geslaagd));
		assertThat(melding.getTitelOfThemaWerkstuk(), is("Werkstuk 19"));
		assertThat(melding.getBeoordelingWerkstuk(), is(BeoordelingWerkstuk.Voldoende));
		assertThat(melding.getToepassingBeoordelingWerkstuk(),
			is(ToepassingResultaat.GeexamineerdInJaarVanMelding));

		List<BronVakGegegevensVOMelding> vakMeldingen = melding.getVakgegevens();
		assertThat(vakMeldingen.size(), is(14));
		BronVakGegegevensVOMelding vakMelding = vakMeldingen.get(0);
		assertThat(vakMelding.getExamenVak(), is(11));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getCijferCE1(), is(61));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(1);
		assertThat(vakMelding.getExamenVak(), is(51));
		assertThat(vakMelding.isDiplomaVak(), is(false));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(63));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(2);
		assertThat(vakMelding.getExamenVak(), is(52));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(75));
		assertThat(vakMelding.getCijferCE1(), is(76));
		assertThat(vakMelding.getEersteEindcijfer(), is(8));
		assertThat(vakMelding.getCijferCijferlijst(), is(8));
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
		assertThat(vakMelding.getCijferSchoolExamen(), is(66));
		assertThat(vakMelding.getCijferCE1(), is(83));
		assertThat(vakMelding.getEersteEindcijfer(), is(7));
		assertThat(vakMelding.getCijferCijferlijst(), is(7));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(5);
		assertThat(vakMelding.getExamenVak(), is(301));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(67));
		assertThat(vakMelding.getCijferCijferlijst(), is(7));
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
		assertThat(vakMelding.getCijferSchoolExamen(), is(76));
		assertThat(vakMelding.getCijferCijferlijst(), is(8));
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
		assertThat(vakMelding.getCijferSchoolExamen(), is(100));
		assertThat(vakMelding.getCijferCijferlijst(), is(10));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(10);
		assertThat(vakMelding.getExamenVak(), is(321));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(100));
		assertThat(vakMelding.getCijferCijferlijst(), is(10));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(11);
		assertThat(vakMelding.getExamenVak(), is(152));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(99));
		assertThat(vakMelding.getCijferCE1(), is(100));
		assertThat(vakMelding.getEersteEindcijfer(), is(10));
		assertThat(vakMelding.getCijferCijferlijst(), is(10));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(12);
		assertThat(vakMelding.getExamenVak(), is(172));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(52));
		assertThat(vakMelding.getCijferCE1(), is(68));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(13);
		assertThat(vakMelding.getExamenVak(), is(183));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(true));
		assertThat(vakMelding.getCijferSchoolExamen(), is(61));
		assertThat(vakMelding.getCijferCE1(), is(62));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
	}

	private void createVakGegegevensGeval19(BronExamenresultaatVOMelding melding)
	{

		melding.setILTCode(150);
		melding.setDatumUitslagExamen(asDate(20090430));
		melding.setUitslagExamen(ExamenUitslag.Geslaagd);
		melding.setTitelOfThemaWerkstuk("Werkstuk 19");
		melding.setBeoordelingWerkstuk(BeoordelingWerkstuk.Voldoende);
		melding.setToepassingBeoordelingWerkstuk(ToepassingResultaat.GeexamineerdInJaarVanMelding);

		List<BronVakGegegevensVOMelding> meldingen = new ArrayList<BronVakGegegevensVOMelding>();
		meldingen.add(createSeGegevens(melding, 11, true, false, null, null, 60, 61, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 51, false, false, null, null, 63, null, false,
			null, null, null, true));
		meldingen.add(createSeGegevens(melding, 52, true, false, null, null, 75, 76, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 61, true, false, null, null, 90, null, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 71, true, false, null, null, 66, 83, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 301, true, false, null, null, 67, null, false,
			null, null, null, true));
		meldingen.add(createSeGegevens(melding, 351, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, null, null, null, false));
		meldingen.add(createSeGegevens(melding, 361, true, false, null, null, 76, null, false,
			null, null, null, true));
		meldingen.add(createSeGegevens(melding, 401, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, null, null, null, false));
		meldingen.add(createSeGegevens(melding, 122, true, false, null, null, 100, null, false,
			null, null, null, true));
		meldingen.add(createSeGegevens(melding, 321, true, false, null, null, 100, null, false,
			null, null, null, true));
		meldingen.add(createSeGegevens(melding, 152, true, false, null, null, 99, 100, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 172, true, false, null, null, 52, 68, false, null,
			null, null, true));
		meldingen.add(createSeGegevens(melding, 183, true, true, null, null, 61, 62, false, null,
			null, null, true));
		melding.setVakgegevens(meldingen);
	}

	private void createGeval19() throws Exception
	{
		getDeelnemer3();
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());

	}

	@Test
	@Ignore
	public void proefgeval20() throws Exception
	{
		createGeval20();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval20(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Verwijdering));
		assertThat(melding.getSofiNummer(), is(840000005));
		assertThat(melding.getLeerlingNummerInstelling(), is("7"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(372));
		assertThat(melding.getExamenJaar(), is(2009));
	}

	private void createVakGegegevensGeval20(BronExamenresultaatVOMelding melding)
	{
		melding.setILTCode(372);
	}

	private void createGeval20() throws Exception
	{
		getDeelnemer7();
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", deelname
			.getExamenstatus(), null);
	}

	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void proefgeval21() throws Exception
	{
		createGeval21();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval21(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(870000007));
		assertThat(melding.getLeerlingNummerInstelling(), is("10"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(372));
		assertThat(melding.getExamenJaar(), is(2009));
		assertThat(melding.getTitelOfThemaWerkstuk(), is("Werkstuk 14"));
		assertThat(melding.getBeoordelingWerkstuk(), is(BeoordelingWerkstuk.Goed));

		List<BronVakGegegevensVOMelding> vakMeldingen = melding.getVakgegevens();
		assertThat(vakMeldingen.size(), is(11));
		BronVakGegegevensVOMelding vakMelding = vakMeldingen.get(0);
		assertThat(vakMelding.getExamenVak(), is(1001));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getHogerNiveau(), is(HogerNiveau.VWO));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));
		assertThat(vakMelding.getVakCodeHogerNiveau(), is(1001));

		vakMelding = vakMeldingen.get(1);
		assertThat(vakMelding.getExamenVak(), is(1002));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(65));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

		vakMelding = vakMeldingen.get(2);
		assertThat(vakMelding.getExamenVak(), is(1019));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(true));
		assertThat(vakMelding.getCijferSchoolExamen(), is(67));
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
		assertThat(vakMelding.getCijferSchoolExamen(), is(70));
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
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

		vakMelding = vakMeldingen.get(7);
		assertThat(vakMelding.getExamenVak(), is(1024));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(50));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

		vakMelding = vakMeldingen.get(8);
		assertThat(vakMelding.getExamenVak(), is(1022));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(65));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

		vakMelding = vakMeldingen.get(9);
		assertThat(vakMelding.getExamenVak(), is(1021));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(80));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

		vakMelding = vakMeldingen.get(10);
		assertThat(vakMelding.getExamenVak(), is(1025));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(75));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));
	}

	private void createVakGegegevensGeval21(BronExamenresultaatVOMelding melding)
	{
		melding.setILTCode(372);
		melding.setTitelOfThemaWerkstuk("Werkstuk 14");
		melding.setBeoordelingWerkstuk(BeoordelingWerkstuk.Goed);

		List<BronVakGegegevensVOMelding> meldingen = new ArrayList<BronVakGegegevensVOMelding>();
		meldingen.add(createSeGegevens(melding, 1001, true, false, HogerNiveau.VWO, null, 60, null,
			false, null, false, 1001, false));
		meldingen.add(createSeGegevens(melding, 1002, true, false, null, null, 65, null, false,
			null, false, null, false));
		meldingen.add(createSeGegevens(melding, 1019, true, true, null, null, 67, null, false,
			null, true, null, false));
		meldingen.add(createSeGegevens(melding, 353, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, null, false, null, false));
		meldingen.add(createSeGegevens(melding, 301, true, false, null, null, 70, null, false,
			null, true, null, false));
		meldingen.add(createSeGegevens(melding, 1020, true, false, null,
			BeoordelingSchoolExamen.Voldoende, null, null, false, null, false, null, false));
		meldingen.add(createSeGegevens(melding, 1005, true, false, null, null, 70, null, false,
			null, false, null, false));
		meldingen.add(createSeGegevens(melding, 1024, true, false, null, null, 50, null, false,
			null, false, null, false));
		meldingen.add(createSeGegevens(melding, 1022, true, false, null, null, 65, null, false,
			null, false, null, false));
		meldingen.add(createSeGegevens(melding, 1021, true, false, null, null, 80, null, false,
			null, false, null, false));
		meldingen.add(createSeGegevens(melding, 1025, true, false, null, null, 75, null, false,
			null, false, null, false));
		melding.setVakgegevens(meldingen);
	}

	private void createGeval21() throws Exception
	{
		getDeelnemer10();
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());
	}

	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void proefgeval22() throws Exception
	{
		createGeval22();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval22(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(1));
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
		assertThat(vakMelding.getCijferCE2(), is(50));
		assertThat(vakMelding.getEersteEindcijfer(), is(5));
		assertThat(vakMelding.getTweedeEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(3);
		assertThat(vakMelding.getExamenVak(), is(61));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(80));
		assertThat(vakMelding.getCijferCijferlijst(), is(8));
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
		assertThat(vakMelding.getCijferCE1(), is(60));
		assertThat(vakMelding.getEersteEindcijfer(), is(6));
		assertThat(vakMelding.getCijferCijferlijst(), is(6));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

	}

	private void createVakGegegevensGeval22(BronExamenresultaatVOMelding melding)
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
		BronVakGegegevensVOMelding vakMelding =
			createSeGegevens(melding, 52, true, false, null, null, 60, 40, false, null, null, null,
				true);
		vakMelding.setCijferCE2(50);
		vakMelding.setTweedeEindcijfer(6);
		vakMelding.setCijferCijferlijst(6);
		meldingen.add(vakMelding);
		meldingen.add(createSeGegevens(melding, 61, true, false, null, null, 80, null, false, null,
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
		meldingen.add(createSeGegevens(melding, 183, true, true, null, null, 50, 60, false, null,
			null, null, true));
		melding.setVakgegevens(meldingen);
	}

	private void createGeval22() throws Exception
	{
		getDeelnemer4();
		verbintenis.setLocatie(getLocatie("00DI01"));
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());
	}

	@Test
	@Ignore
	public void VOBatch2Test() throws Exception
	{
		createGeval19();
		createGeval20();
		createGeval21();
		createGeval22();

		examenBatchVO = maakBronExamenVOBatch();
		createVakGegegevensGeval19(getBronExamenresultaatVOMelding(0));
		createVakGegegevensGeval20(getBronExamenresultaatVOMelding(1));
		createVakGegegevensGeval21(getBronExamenresultaatVOMelding(2));
		createVakGegegevensGeval22(getBronExamenresultaatVOMelding(3));
		examenBatchVO.setBatchNummer(5);

		assertThat(examenBatchVO.getBRINNummer(), is("00DI"));
		assertThat(examenBatchVO.getAanleverPuntNummer(), is(0));
		assertThat(examenBatchVO.getBatchNummer(), is(5));
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
