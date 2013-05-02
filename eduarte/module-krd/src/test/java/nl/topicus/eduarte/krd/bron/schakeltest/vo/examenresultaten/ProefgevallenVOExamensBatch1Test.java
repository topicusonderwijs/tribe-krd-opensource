package nl.topicus.eduarte.krd.bron.schakeltest.vo.examenresultaten;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Test;

public class ProefgevallenVOExamensBatch1Test extends ProefgevallenVOExamens
{
	@Test
	public void proefgeval01() throws Exception
	{
		createGeval01();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(320000011));
		assertThat(melding.getLeerlingNummerInstelling(), is("1"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080801)));
		assertThat(melding.getILTCode(), is(2551));
		assertThat(melding.getLeerjaar(), is(4));
	}

	private void createGeval01() throws Exception
	{
		getDeelnemer1();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval02() throws Exception
	{
		createGeval02();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(330000007));
		assertThat(melding.getLeerlingNummerInstelling(), is("2"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080801)));
		assertThat(melding.getILTCode(), is(2551));
		assertThat(melding.getLeerjaar(), is(4));
	}

	private void createGeval02() throws Exception
	{
		getDeelnemer2();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval03() throws Exception
	{
		createGeval03();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(830000008));
		assertThat(melding.getLeerlingNummerInstelling(), is("3"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19891219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20081001)));
		assertThat(melding.getILTCode(), is(150));
		assertThat(melding.getLeerjaar(), is(6));
	}

	private void createGeval03() throws Exception
	{
		getDeelnemer3();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval04() throws Exception
	{
		createGeval04();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(860000011));
		assertThat(melding.getLeerlingNummerInstelling(), is("4"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19891219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080930)));
		assertThat(melding.getILTCode(), is(150));
		assertThat(melding.getLeerjaar(), is(6));
	}

	private void createGeval04() throws Exception
	{
		getDeelnemer4();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval05() throws Exception
	{
		createGeval05();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(340000004));
		assertThat(melding.getLeerlingNummerInstelling(), is("5"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19891219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080901)));
		assertThat(melding.getILTCode(), is(150));
		assertThat(melding.getLeerjaar(), is(6));
	}

	private void createGeval05() throws Exception
	{
		getDeelnemer5();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval06() throws Exception
	{
		createGeval06();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(310000002));
		assertThat(melding.getLeerlingNummerInstelling(), is("6"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19891219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080901)));
		assertThat(melding.getILTCode(), is(150));
		assertThat(melding.getLeerjaar(), is(6));
	}

	private void createGeval06() throws Exception
	{
		getDeelnemer6();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval07() throws Exception
	{
		createGeval07();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(840000005));
		assertThat(melding.getLeerlingNummerInstelling(), is("7"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080801)));
		assertThat(melding.getILTCode(), is(372));
		assertThat(melding.getLeerjaar(), is(5));
	}

	private void createGeval07() throws Exception
	{
		getDeelnemer7();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval08() throws Exception
	{
		createGeval08();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(810000003));
		assertThat(melding.getLeerlingNummerInstelling(), is("8"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080801)));
		assertThat(melding.getILTCode(), is(372));
		assertThat(melding.getLeerjaar(), is(5));
	}

	private void createGeval08() throws Exception
	{
		getDeelnemer8();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval09() throws Exception
	{
		createGeval09();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(820000012));
		assertThat(melding.getLeerlingNummerInstelling(), is("9"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080801)));
		assertThat(melding.getILTCode(), is(372));
		assertThat(melding.getLeerjaar(), is(5));
	}

	private void createGeval09() throws Exception
	{
		getDeelnemer9();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval10() throws Exception
	{
		createGeval10();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(870000007));
		assertThat(melding.getLeerlingNummerInstelling(), is("10"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080801)));
		assertThat(melding.getILTCode(), is(372));
		assertThat(melding.getLeerjaar(), is(5));
	}

	private void createGeval10() throws Exception
	{
		getDeelnemer10();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval11() throws Exception
	{
		createGeval11();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(0));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(850000002));
		assertThat(melding.getLeerlingNummerInstelling(), is("11"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19911219)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080801)));
		assertThat(melding.getILTCode(), is(2911));
		assertThat(melding.getLeerjaar(), is(4));
	}

	private void createGeval11() throws Exception
	{
		getDeelnemer11();
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
		createGeval08();
		createGeval09();
		createGeval10();
		createGeval11();

		inschrijvingBatchVO = maakBronInschrijvingVOBatch();
		inschrijvingBatchVO.setBatchNummer(1);
		inschrijvingBatchVO.setBRINNummer("00DI");
		inschrijvingBatchVO.setLaatsteAanlevering(true);
		inschrijvingBatchVO.berekenControleTotalen();

		assertThat(inschrijvingBatchVO.getBRINNummer(), is("00DI"));
		assertThat(inschrijvingBatchVO.getAanleverPuntNummer(), is(0));
		assertThat(inschrijvingBatchVO.getBatchNummer(), is(1));
		assertThat(inschrijvingBatchVO.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(inschrijvingBatchVO.getSoortMelding(), is(SoortMelding.Inschrijving));
		assertThat(inschrijvingBatchVO.getAantalRecords(), is(11));
		assertThat(inschrijvingBatchVO.getAantalToevoegingen(), is(11));
		assertThat(inschrijvingBatchVO.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanlevering.Instelling));
		assertThat(inschrijvingBatchVO.getLaatsteAanlevering(), is(true));

		writeBronBatch(inschrijvingBatchVO);

	}
}
