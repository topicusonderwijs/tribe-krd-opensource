package nl.topicus.eduarte.krd.bron.schakeltest.vo.inschrijving;

import static java.util.Arrays.*;
import static nl.topicus.eduarte.tester.hibernate.DatabaseAction.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiCategorie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiRatio;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Test;

@SuppressWarnings("hiding")
public class ProefgevallenVOInschrijvingBatch3Test extends ProefgevallenVOInschrijving
{
	@Test
	public void proefgeval10() throws Exception
	{
		createGeval10();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));

		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(melding.getSofiNummer(), is(800000481));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000001"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19890101)));
		assertThat(melding.getGeslacht(), is(Geslacht.Onbekend));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080730)));
		assertThat(melding.getILTCode(), is(2781));
		assertThat(melding.getLeerjaar(), is(4));
	}

	private void createGeval10() throws Exception
	{
		getDeelnemer1001();
		plaatsing = verbintenis.getPlaatsingOpPeildatum();
		plaatsing.setBegindatum(asDate(20080730));
		verbintenis.setBegindatum(plaatsing.getBegindatum());
		int vorigLeerjaar = plaatsing.getLeerjaar();
		plaatsing.setLeerjaar(4);
		addChange(plaatsing, "leerjaar", vorigLeerjaar, plaatsing.getLeerjaar());
	}

	@Test
	public void proefgeval11() throws Exception
	{
		createGeval11();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding1 = getEersteBronInschrijvingsgegevensVOMelding();
		BronInschrijvingsgegevensVOMelding melding2 = getTweedeBronInschrijvingsgegevensVOMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding1), insert(melding2))));

		assertThat(melding1.getVestigingsVolgnummer(), is(21));
		assertThat(melding1.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(melding1.getSofiNummer(), is(800000122));
		assertThat(melding1.getLeerlingNummerInstelling(), is("1000000002"));
		assertThat(melding1.getGeboorteDatum(), is(Datum.valueOf(19940202)));
		assertThat(melding1.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding1.getPostcode(), is("9711LK"));
		assertThat(melding1.getIngangsDatum(), is(asDate(20080707)));
		assertThat(melding1.getILTCode(), is(2481));
		assertThat(melding1.getLeerjaar(), is(3));
		assertThat(melding1.getCumiCategorie(), is(CumiCategorie.Categorie4a));
		assertThat(melding1.getCumiRatio(), is(CumiRatio.d1));

		assertThat(melding2.getVestigingsVolgnummer(), is(21));
		assertThat(melding2.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(melding2.getSofiNummer(), is(800000122));
		assertThat(melding2.getLeerlingNummerInstelling(), is("1000000002"));
		assertThat(melding2.getGeboorteDatum(), is(Datum.valueOf(19940202)));
		assertThat(melding2.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding2.getPostcode(), is("9711LK"));
		assertThat(melding2.getIngangsDatum(), is(asDate(20080807)));
		assertThat(melding2.getILTCode(), is(2481));
		assertThat(melding2.getLeerjaar(), is(4));
		assertThat(melding2.getCumiCategorie(), is(CumiCategorie.Categorie4a));
		assertThat(melding2.getCumiRatio(), is(CumiRatio.d1));
	}

	private void createGeval11() throws Exception
	{
		getDeelnemer1002();

		Plaatsing leerjaar3 = verbintenis.getPlaatsingOpPeildatum();
		leerjaar3.setEinddatum(asDate(20080806));
		leerjaar3.setLwoo(true);

		Plaatsing leerjaar4 = getPlaatsing(4, verbintenis);
		leerjaar4.setBegindatum(asDate(20080807));
		leerjaar4.setLwoo(true);

		verbintenis.getPlaatsingen().add(0, leerjaar4);

		addChange(leerjaar3, "lwoo", false, true);
		addChange(leerjaar3, "einddatum", asDate(20090731), asDate(20080806));
		addChange(leerjaar4, "begindatum", null, asDate(20080807));
		addChange(leerjaar4, "leerjaar", null, 4);
		addChange(leerjaar4, "lwoo", false, true);
	}

	@Test
	public void proefgeval12() throws Exception
	{
		createGeval12();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding1 = getEersteBronInschrijvingsgegevensVOMelding();
		BronInschrijvingsgegevensVOMelding melding2 = getTweedeBronInschrijvingsgegevensVOMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding1), insert(melding2))));

		assertThat(melding1.getVestigingsVolgnummer(), is(21));
		assertThat(melding1.getSoortMutatie(), is(SoortMutatie.Verwijdering));
		assertThat(melding1.getOnderwijsNummer(), is(100511771));
		assertThat(melding1.getLeerlingNummerInstelling(), is("1000000006"));
		assertThat(melding1.getGeboorteDatum(), is(Datum.valueOf(19910708)));
		assertThat(melding1.getGeslacht(), is(Geslacht.Man));
		assertThat(melding1.getPostcode(), is("9722TB"));
		assertThat(melding1.getIngangsDatum(), is(asDate(20080828)));

		assertThat(melding2.getVestigingsVolgnummer(), is(21));
		assertThat(melding2.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding2.getOnderwijsNummer(), is(100511771));
		assertThat(melding2.getLeerlingNummerInstelling(), is("1000000006"));
		assertThat(melding2.getGeboorteDatum(), is(Datum.valueOf(19910708)));
		assertThat(melding2.getGeslacht(), is(Geslacht.Man));
		assertThat(melding2.getPostcode(), is("9722TB"));
		assertThat(melding2.getIngangsDatum(), is(asDate(20080930)));
		assertThat(melding2.getILTCode(), is(2981));
		assertThat(melding2.getLeerjaar(), is(3));
	}

	private void createGeval12() throws Exception
	{
		getDeelnemer1006();
		Long vorigeOnderwijsNummer = deelnemer.getOnderwijsnummer();
		verbintenis.getDeelnemer().setOnderwijsnummer(100511771L);
		addChange(deelnemer, "onderwijsnummer", vorigeOnderwijsNummer, deelnemer
			.getOnderwijsnummer());
		Date vorigeBeginDatum = verbintenis.getBegindatum();
		verbintenis.setBegindatum(asDate(20080930));
		addChange(verbintenis, "begindatum", vorigeBeginDatum, verbintenis.getBegindatum());
		Opleiding vorigeOpleiding = verbintenis.getOpleiding();
		verbintenis.setOpleiding(newOpleiding("2981", "Ander opleiding"));
		addChange(verbintenis, "opleiding", vorigeOpleiding, verbintenis.getOpleiding());
	}

	@Test
	public void proefgeval13() throws Exception
	{
		createGeval13();
		controller.save();
		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));

		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Verwijdering));
		assertThat(melding.getSofiNummer(), is(800000407));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000007"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19940707)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getPostcode(), is("9711LB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080229)));
	}

	private void createGeval13() throws Exception
	{
		getDeelnemer1007();
		addChange(verbintenis, "status", VerbintenisStatus.Definitief, VerbintenisStatus.Afgewezen);
	}

	@Test
	public void proefgeval14() throws Exception
	{
		createGeval14();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(800000407));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000007"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19940707)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getPostcode(), is("9711LB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080808)));
		assertThat(melding.getILTCode(), is(3081));
		assertThat(melding.getLeerjaar(), is(3));
	}

	private void createGeval14() throws Exception
	{
		getDeelnemer1007();
		verbintenis.setBegindatum(asDate(20080808));
		plaatsing.setBegindatum(verbintenis.getBegindatum());
		verbintenis.setOpleiding(newOpleiding("3081", "Opleiding3081"));
		verbintenis.getPlaatsingOpPeildatum().setLeerjaar(3);
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval15() throws Exception
	{
		createGeval15();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));

		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Uitschrijving));
		assertThat(melding.getSofiNummer(), is(800000419));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000009"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19940909)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getPostcode(), is("9711LB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080929)));
	}

	private void createGeval15() throws Exception
	{
		getDeelnemer1009();
		verbintenis.setEinddatum(asDate(20080929));
		addChange(verbintenis, "einddatum", null, verbintenis.getEinddatum());
		addChange(verbintenis, "status", VerbintenisStatus.Definitief, VerbintenisStatus.Beeindigd);
	}

	/**
	 * Aanpassen van persoonsgegevens (geboortedatum) van een persoon met een
	 * onderwijsnummer, waardoor (nog steeds) geen sofi-nummer wordt gevonden (dus zelfde
	 * onderwijsnummer) op peildatum.
	 */
	@Test
	public void proefgeval16() throws Exception
	{
		createGeval16();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));

		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(melding.getOnderwijsNummer(), is(100509703));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000010"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19910706)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9711LK"));
		assertThat(melding.getGeboorteDatumGewijzigd(), is(Datum.valueOf(19900303)));
	}

	private void createGeval16() throws Exception
	{
		getDeelnemer1010();
		Persoon persoon = verbintenis.getPersoon();
		Date vorigeGeboortedatum = persoon.getGeboortedatum();
		persoon.setGeboortedatum(asDate(19900303));
		addChange(persoon, "geboortedatum", vorigeGeboortedatum, persoon.getGeboortedatum());
	}

	@Test
	public void VOBatch3Test() throws Exception
	{
		createGeval10();
		createGeval11();
		createGeval12();
		createGeval13();
		createGeval14();
		createGeval15();
		createGeval16();

		inschrijvingBatchVO = maakBronInschrijvingVOBatch();
		inschrijvingBatchVO.setBatchNummer(2);
		inschrijvingBatchVO.setLaatsteAanlevering(true);
		inschrijvingBatchVO.berekenControleTotalen();

		assertThat(inschrijvingBatchVO.getBRINNummer(), is("01OE"));
		assertThat(inschrijvingBatchVO.getAanleverPuntNummer(), is(0));
		assertThat(inschrijvingBatchVO.getBatchNummer(), is(2));
		assertThat(inschrijvingBatchVO.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(inschrijvingBatchVO.getSoortMelding(), is(SoortMelding.Inschrijving));
		assertThat(inschrijvingBatchVO.getAantalToevoegingen(), is(2));
		assertThat(inschrijvingBatchVO.getAantalAanpassingen(), is(4));
		assertThat(inschrijvingBatchVO.getAantalVerwijderingen(), is(2));
		assertThat(inschrijvingBatchVO.getAantalUitschrijvingen(), is(1));
		assertThat(inschrijvingBatchVO.getAantalRecords(), is(9));
		assertThat(inschrijvingBatchVO.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanlevering.Instelling));
		assertThat(inschrijvingBatchVO.getLaatsteAanlevering(), is(true));

		writeBronBatch(inschrijvingBatchVO);

	}
}
