package nl.topicus.eduarte.krd.bron.schakeltest.vo.inschrijving;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiCategorie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiRatio;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Test;

public class ProefgevallenVOInschrijvingBatch1Test extends ProefgevallenVOInschrijving
{
	@Test
	public void proefgeval01() throws Exception
	{
		createGeval01();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(800000481));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000001"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19890101)));
		assertThat(melding.getGeslacht(), is(Geslacht.Onbekend));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080730)));
		assertThat(melding.getILTCode(), is(2781));
		assertThat(melding.getLeerjaar(), is(3));
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

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(800000122));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000002"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19940202)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getPostcode(), is("9711LK"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080707)));
		assertThat(melding.getILTCode(), is(2881));
		assertThat(melding.getLeerjaar(), is(3));
		assertThat(melding.getCumiCategorie(), is(CumiCategorie.Categorie4a));
		assertThat(melding.getCumiRatio(), is(CumiRatio.d1));
	}

	private void createGeval02() throws Exception
	{
		getDeelnemer1002();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval03() throws Exception
	{
		createGeval03();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000003"));
		assertThat(melding.getAchternaam(), is("Proefgevaldrievv"));
		assertThat(melding.getAlleVoornamen(), is("Voornaamdrievv"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19940303)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getStraatNaam(), is("Herenstraat"));
		assertThat(melding.getHuisNummer(), is(33));
		assertThat(melding.getPostcode(), is("9711LB"));
		assertThat(melding.getPlaatsnaam(), is("Groningen"));
		assertThat(melding.getIngangsDatum(), is(asDate(20081002)));
		assertThat(melding.getILTCode(), is(2981));
		assertThat(melding.getLeerjaar(), is(3));
	}

	private void createGeval03() throws Exception
	{
		getDeelnemer1003();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval04() throws Exception
	{
		createGeval04();
		controller.save();
		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000006"));
		assertThat(melding.getAchternaam(), is("Achternaamachtenvijftig"));
		assertThat(melding.getAlleVoornamen(), is("Voornaamachtenvijftig"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19910708)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getStraatNaam(), is("Kempkensberg"));
		assertThat(melding.getHuisNummer(), is(2));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getPlaatsnaam(), is("Groningen"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080828)));
		assertThat(melding.getILTCode(), is(3081));
		assertThat(melding.getLeerjaar(), is(3));
	}

	private void createGeval04() throws Exception
	{
		getDeelnemer1006();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval05() throws Exception
	{
		createGeval05();
		controller.save();
		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(800000407));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000007"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19940707)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getPostcode(), is("9711LB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080229)));
		assertThat(melding.getILTCode(), is(2781));
		assertThat(melding.getLeerjaar(), is(4));
	}

	private void createGeval05() throws Exception
	{
		getDeelnemer1007();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	public void proefgeval06() throws Exception
	{
		createGeval06();
		controller.save();
		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(800000419));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000009"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19940909)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getPostcode(), is("9711LB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080707)));
		assertThat(melding.getILTCode(), is(2881));
		assertThat(melding.getLeerjaar(), is(3));

	}

	private void createGeval06() throws Exception
	{
		getDeelnemer1009();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval07() throws Exception
	{
		createGeval07();
		controller.save();
		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getOnderwijsNummer(), is(100509703));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000010"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19910706)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9711LK"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080707)));
		assertThat(melding.getILTCode(), is(3081));
		assertThat(melding.getLeerjaar(), is(4));

	}

	private void createGeval07() throws Exception
	{
		getDeelnemer1010();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void VOBatch1Test() throws Exception
	{
		createGeval01();
		createGeval02();
		createGeval03();
		createGeval04();
		createGeval05();
		createGeval06();
		createGeval07();

		inschrijvingBatchVO = maakBronInschrijvingVOBatch();
		inschrijvingBatchVO.setBatchNummer(1);
		inschrijvingBatchVO.berekenControleTotalen();

		assertThat(inschrijvingBatchVO.getBRINNummer(), is("01OE"));
		assertThat(inschrijvingBatchVO.getAanleverPuntNummer(), is(0));
		assertThat(inschrijvingBatchVO.getBatchNummer(), is(1));
		assertThat(inschrijvingBatchVO.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(inschrijvingBatchVO.getSoortMelding(), is(SoortMelding.Inschrijving));
		assertThat(inschrijvingBatchVO.getAantalRecords(), is(7));
		assertThat(inschrijvingBatchVO.getAantalToevoegingen(), is(7));
		assertThat(inschrijvingBatchVO.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanlevering.Instelling));
		assertThat(inschrijvingBatchVO.getLaatsteAanlevering(), is(false));

		writeBronBatch(inschrijvingBatchVO);

	}
}
