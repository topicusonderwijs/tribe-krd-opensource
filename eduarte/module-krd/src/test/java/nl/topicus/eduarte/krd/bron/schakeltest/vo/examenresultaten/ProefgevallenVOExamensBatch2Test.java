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
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.HogerNiveau;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Ignore;
import org.junit.Test;

public class ProefgevallenVOExamensBatch2Test extends ProefgevallenVOExamens
{
	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void proefgeval12() throws Exception
	{
		createGeval12();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval12(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(830000008));
		assertThat(melding.getLeerlingNummerInstelling(), is("3"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19891219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(150));
		assertThat(melding.getExamenJaar(), is(2009));
		assertThat(melding.getTitelOfThemaWerkstuk(), is("Werkstuk 12"));
		assertThat(melding.getBeoordelingWerkstuk(), is(BeoordelingWerkstuk.Voldoende));

		List<BronVakGegegevensVOMelding> vakMeldingen = melding.getVakgegevens();
		assertThat(vakMeldingen.size(), is(14));
		BronVakGegegevensVOMelding vakMelding = vakMeldingen.get(0);
		assertThat(vakMelding.getExamenVak(), is(11));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(1);
		assertThat(vakMelding.getExamenVak(), is(51));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(63));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(2);
		assertThat(vakMelding.getExamenVak(), is(52));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(75));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(3);
		assertThat(vakMelding.getExamenVak(), is(61));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(90));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(4);
		assertThat(vakMelding.getExamenVak(), is(71));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(66));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(5);
		assertThat(vakMelding.getExamenVak(), is(301));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(67));
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
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(10);
		assertThat(vakMelding.getExamenVak(), is(321));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(100));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(11);
		assertThat(vakMelding.getExamenVak(), is(152));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(99));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(12);
		assertThat(vakMelding.getExamenVak(), is(172));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(52));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));

		vakMelding = vakMeldingen.get(13);
		assertThat(vakMelding.getExamenVak(), is(183));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(true));
		assertThat(vakMelding.getCijferSchoolExamen(), is(61));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
	}

	private void createVakGegegevensGeval12(BronExamenresultaatVOMelding melding)
	{
		melding.setILTCode(150);
		melding.setTitelOfThemaWerkstuk("Werkstuk 12");
		melding.setBeoordelingWerkstuk(BeoordelingWerkstuk.Voldoende);

		List<BronVakGegegevensVOMelding> meldingen = new ArrayList<BronVakGegegevensVOMelding>();
		meldingen.add(createSeGegevens(melding, 11, true, false, null, null, 60, null, false, null,
			null, null, false));
		meldingen.add(createSeGegevens(melding, 51, true, false, null, null, 63, null, false, null,
			null, null, false));
		meldingen.add(createSeGegevens(melding, 52, true, false, null, null, 75, null, false, null,
			null, null, false));
		meldingen.add(createSeGegevens(melding, 61, true, false, null, null, 90, null, false, null,
			null, null, false));
		meldingen.add(createSeGegevens(melding, 71, true, false, null, null, 66, null, false, null,
			null, null, false));
		meldingen.add(createSeGegevens(melding, 301, true, false, null, null, 67, null, false,
			null, null, null, false));
		meldingen.add(createSeGegevens(melding, 351, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, null, null, null, false));
		meldingen.add(createSeGegevens(melding, 361, true, false, null, null, 76, null, false,
			null, null, null, false));
		meldingen.add(createSeGegevens(melding, 401, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, false, null, null, null, false));
		meldingen.add(createSeGegevens(melding, 122, true, false, null, null, 100, null, false,
			null, null, null, false));
		meldingen.add(createSeGegevens(melding, 321, true, false, null, null, 100, null, false,
			null, null, null, false));
		meldingen.add(createSeGegevens(melding, 152, true, false, null, null, 99, null, false,
			null, null, null, false));
		meldingen.add(createSeGegevens(melding, 172, true, false, null, null, 52, null, false,
			null, null, null, false));
		meldingen.add(createSeGegevens(melding, 183, true, true, null, null, 61, null, false, null,
			null, null, false));
		melding.setVakgegevens(meldingen);
	}

	private void createGeval12() throws Exception
	{
		getDeelnemer3();
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());
		maakBronExamenVOBatch();

	}

	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void proefgeval13() throws Exception
	{
		createGeval13();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval13(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(840000005));
		assertThat(melding.getLeerlingNummerInstelling(), is("7"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(372));
		assertThat(melding.getExamenJaar(), is(2009));
		assertThat(melding.getTitelOfThemaWerkstuk(), is("Werkstuk 13"));
		assertThat(melding.getBeoordelingWerkstuk(), is(BeoordelingWerkstuk.Goed));

		List<BronVakGegegevensVOMelding> vakMeldingen = melding.getVakgegevens();
		assertThat(vakMeldingen.size(), is(11));
		BronVakGegegevensVOMelding vakMelding = vakMeldingen.get(0);
		assertThat(vakMelding.getExamenVak(), is(1001));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(50));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

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
		assertThat(vakMelding.getCijferSchoolExamen(), is(62));
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

	private void createVakGegegevensGeval13(BronExamenresultaatVOMelding melding)
	{

		melding.setILTCode(372);
		melding.setTitelOfThemaWerkstuk("Werkstuk 13");
		melding.setBeoordelingWerkstuk(BeoordelingWerkstuk.Goed);

		List<BronVakGegegevensVOMelding> meldingen = new ArrayList<BronVakGegegevensVOMelding>();
		meldingen.add(createSeGegevens(melding, 1001, true, false, null, null, 50, null, false,
			null, false, null, false));
		meldingen.add(createSeGegevens(melding, 1002, true, false, null, null, 65, null, false,
			null, false, null, false));
		meldingen.add(createSeGegevens(melding, 1019, true, true, null, null, 62, null, false,
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

	private void createGeval13() throws Exception
	{
		getDeelnemer7();
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());
	}

	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void proefgeval14() throws Exception
	{
		createGeval14();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval14(melding);

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
		assertThat(vakMelding.getCijferSchoolExamen(), is(50));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(false));
		assertThat(vakMelding.getIndicatieCombinatieCijfer(), is(false));

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
		assertThat(vakMelding.getCijferSchoolExamen(), is(62));
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

	private void createVakGegegevensGeval14(BronExamenresultaatVOMelding melding)
	{
		melding.setILTCode(372);
		melding.setTitelOfThemaWerkstuk("Werkstuk 14");
		melding.setBeoordelingWerkstuk(BeoordelingWerkstuk.Goed);
		List<BronVakGegegevensVOMelding> meldingen = new ArrayList<BronVakGegegevensVOMelding>();
		meldingen.add(createSeGegevens(melding, 1001, true, false, HogerNiveau.VWO, null, 50, null,
			false, null, false, 1001, false));
		meldingen.add(createSeGegevens(melding, 1002, true, false, null, null, 65, null, false,
			null, false, null, false));
		meldingen.add(createSeGegevens(melding, 1019, true, true, null, null, 62, null, false,
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

	private void createGeval14() throws Exception
	{
		getDeelnemer10();
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());
	}

	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void proefgeval15() throws Exception
	{
		createGeval15();
		writeDummyExamenBatchVO();

		BronExamenresultaatVOMelding melding = getBronExamenresultaatVOMelding(0);
		createVakGegegevensGeval15(melding);

		assertThat(melding.getVestigingsVolgnummer(), is(1));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(850000002));
		assertThat(melding.getLeerlingNummerInstelling(), is("11"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getILTCode(), is(2911));
		assertThat(melding.getExamenJaar(), is(2009));

		List<BronVakGegegevensVOMelding> vakMeldingen = melding.getVakgegevens();
		assertThat(vakMeldingen.size(), is(8));
		BronVakGegegevensVOMelding vakMelding = vakMeldingen.get(0);
		assertThat(vakMelding.getExamenVak(), is(11));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(100));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(true));
		assertThat(vakMelding.getCertificaat(), is(false));

		vakMelding = vakMeldingen.get(1);
		assertThat(vakMelding.getExamenVak(), is(71));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(90));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(true));
		assertThat(vakMelding.getCertificaat(), is(false));

		vakMelding = vakMeldingen.get(2);
		assertThat(vakMelding.getExamenVak(), is(322));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(80));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(true));
		assertThat(vakMelding.getCertificaat(), is(false));

		vakMelding = vakMeldingen.get(3);
		assertThat(vakMelding.getExamenVak(), is(353));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getBeoordelingSchoolExamen(), is(BeoordelingSchoolExamen.Goed));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(true));
		assertThat(vakMelding.getCertificaat(), is(false));

		vakMelding = vakMeldingen.get(4);
		assertThat(vakMelding.getExamenVak(), is(416));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getBeoordelingSchoolExamen(), is(BeoordelingSchoolExamen.Goed));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(true));
		assertThat(vakMelding.getCertificaat(), is(false));

		vakMelding = vakMeldingen.get(5);
		assertThat(vakMelding.getExamenVak(), is(601));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(70));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(true));
		assertThat(vakMelding.getCertificaat(), is(false));

		vakMelding = vakMeldingen.get(6);
		assertThat(vakMelding.getExamenVak(), is(153));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(60));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(true));
		assertThat(vakMelding.getCertificaat(), is(false));

		vakMelding = vakMeldingen.get(7);
		assertThat(vakMelding.getExamenVak(), is(173));
		assertThat(vakMelding.isDiplomaVak(), is(true));
		assertThat(vakMelding.getIndicatieWerkstuk(), is(false));
		assertThat(vakMelding.getCijferSchoolExamen(), is(50));
		assertThat(vakMelding.getVerwezenNaarVolgendeTijdvak(), is(true));
		assertThat(vakMelding.getCertificaat(), is(false));

	}

	private void createVakGegegevensGeval15(BronExamenresultaatVOMelding melding)
	{
		melding.setILTCode(2911);
		List<BronVakGegegevensVOMelding> meldingen = new ArrayList<BronVakGegegevensVOMelding>();
		meldingen.add(createSeGegevens(melding, 11, true, false, null, null, 100, null, true,
			false, null, null, false));
		meldingen.add(createSeGegevens(melding, 71, true, false, null, null, 90, null, true, false,
			null, null, false));
		meldingen.add(createSeGegevens(melding, 322, true, false, null, null, 80, null, true,
			false, null, null, false));
		meldingen.add(createSeGegevens(melding, 353, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, true, false, null, null, false));
		meldingen.add(createSeGegevens(melding, 416, true, false, null,
			BeoordelingSchoolExamen.Goed, null, null, true, false, null, null, false));
		meldingen.add(createSeGegevens(melding, 601, true, false, null, null, 70, null, true,
			false, null, null, false));
		meldingen.add(createSeGegevens(melding, 153, true, false, null, null, 60, null, true,
			false, null, null, false));
		meldingen.add(createSeGegevens(melding, 173, true, false, null, null, 50, null, true,
			false, null, null, false));
		melding.setVakgegevens(meldingen);
	}

	private void createGeval15() throws Exception
	{
		getDeelnemer11();
		verbintenis.setLocatie(getLocatie("00DI01"));
		Examendeelname deelname = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis.getExamendeelnames().get(0), "examenstatus", null, deelname
			.getExamenstatus());
	}

	@Test
	@Ignore("Helaas kan deze test niet meer uitgevoerd worden, omdat er geen melding aangemaakt wordt als er geen vakgegevens zijn")
	public void VOBatch2Test() throws Exception
	{
		createGeval12();
		createGeval13();
		createGeval14();
		createGeval15();

		examenBatchVO = maakBronExamenVOBatch();
		createVakGegegevensGeval12(getBronExamenresultaatVOMelding(0));
		createVakGegegevensGeval13(getBronExamenresultaatVOMelding(1));
		createVakGegegevensGeval14(getBronExamenresultaatVOMelding(2));
		createVakGegegevensGeval15(getBronExamenresultaatVOMelding(3));
		// opgehoogd ivm foute aanlevering
		examenBatchVO.setBatchNummer(3);
		examenBatchVO.berekenControleTotalen();

		assertThat(examenBatchVO.getBRINNummer(), is("00DI"));
		assertThat(examenBatchVO.getAanleverPuntNummer(), is(0));
		assertThat(examenBatchVO.getBatchNummer(), is(3));
		assertThat(examenBatchVO.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(examenBatchVO.getSoortMelding(), is(SoortMelding.Examenresultaat));

		assertThat(examenBatchVO.getAantalRecords(), is(4));
		assertThat(examenBatchVO.getAantalToevoegingen(), is(4));

		assertThat(examenBatchVO.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanlevering.Instelling));

		writeBronBatch(examenBatchVO);

	}
}
