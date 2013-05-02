package nl.topicus.eduarte.krd.bron.schakeltest.vo.inschrijving;

import static java.util.Arrays.*;
import static nl.topicus.eduarte.tester.hibernate.DatabaseAction.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.krd.bron.schakeltest.BronTestData;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Test;

@SuppressWarnings("hiding")
public class ProefgevallenVOInschrijvingBatch5Test extends ProefgevallenVOInschrijving
{
	/**
	 * Dubbele inschrijving binnen instelling over meerdere aanleverpunten met dien
	 * verstande dat het verschillende ILT-code’s betreft.
	 */
	@Test
	public void proefgeval19() throws Exception
	{
		createGeval19();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding1 = getEersteBronInschrijvingsgegevensVOMelding();
		BronInschrijvingsgegevensVOMelding melding2 = getTweedeBronInschrijvingsgegevensVOMelding();
		assertThat(tester.getTransactionLog(), is(asList(insert(melding1), insert(melding2),
			update(melding2))));

		assertThat(melding1.getVerbintenis(), is(verbintenis));
		assertThat(melding2.getVestigingsVolgnummer(), is(21));
		assertThat(melding2.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding2.getSofiNummer(), is(210000636));
		assertThat(melding2.getLeerlingNummerInstelling(), is("1000000005"));
		assertThat(melding2.getGeboorteDatum(), is(Datum.valueOf(19910711)));
		assertThat(melding2.getGeslacht(), is(Geslacht.Man));
		assertThat(melding2.getPostcode(), is("9722TB"));
		assertThat(melding2.getIngangsDatum(), is(asDate(20080601)));
		assertThat(melding2.getILTCode(), is(2881));
		assertThat(melding2.getLeerjaar(), is(4));
	}

	private void createGeval19() throws Exception
	{
		getDeelnemer1005();

		verbintenis.setEinddatum(asDate(20080530));
		verbintenis.setBeeindigd(true);
		verbintenis.setStatus(VerbintenisStatus.Beeindigd);

		builder.addVerbintenisVO(20080601, 2881);
		BronTestData data = builder.build();

		Verbintenis tweede = data.getVerbintenis();

		Plaatsing plaatsing2 = getPlaatsing(4, tweede);
		tweede.setPlaatsingen(Arrays.asList(plaatsing2));

		tweede.setLocatie(getLocatie("01CE21"));

		Persoon persoon = tweede.getPersoon();
		persoon.setBsn(210000636L);
		addChange(verbintenis, "status", VerbintenisStatus.Definitief, VerbintenisStatus.Beeindigd);
		addChange(verbintenis, "einddatum", null, verbintenis.getEinddatum());
		addChange(persoon, "bsn", null, persoon.getBsn());
		addChange(tweede, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval20() throws Exception
	{
		createGeval20();
		controller.save();

		assertThat(getAantalInTransactie(BronInschrijvingsgegevensVOMelding.class), is(equalTo(1)));
		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(800000328));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000008"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19940818)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getPostcode(), is("9711LK"));
		assertThat(melding.getIngangsDatum(), is(asDate(20081212)));
		assertThat(melding.getILTCode(), is(2981));
		assertThat(melding.getLeerjaar(), is(4));
	}

	private void createGeval20() throws Exception
	{
		getDeelnemer1008();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval21() throws Exception
	{
		createGeval21();
		controller.save();

		assertThat(getAantalInTransactie(BronInschrijvingsgegevensVOMelding.class), is(equalTo(1)));
		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(800000511));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000011"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19941111)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getPostcode(), is("9711LK"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080831)));
		assertThat(melding.getILTCode(), is(36));
		assertThat(melding.getLeerjaar(), is(4));
	}

	private void createGeval21() throws Exception
	{
		getDeelnemer1011();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void VOBatch5Test() throws Exception
	{
		createGeval19();
		createGeval20();
		createGeval21();

		inschrijvingBatchVO = maakBronInschrijvingVOBatch();
		inschrijvingBatchVO.setBatchNummer(4);
		inschrijvingBatchVO.berekenControleTotalen();

		assertThat(inschrijvingBatchVO.getBRINNummer(), is("01OE"));
		assertThat(inschrijvingBatchVO.getAanleverPuntNummer(), is(0));
		assertThat(inschrijvingBatchVO.getBatchNummer(), is(4));
		assertThat(inschrijvingBatchVO.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(inschrijvingBatchVO.getSoortMelding(), is(SoortMelding.Inschrijving));
		assertThat(inschrijvingBatchVO.getAantalToevoegingen(), is(3));
		assertThat(inschrijvingBatchVO.getAantalAanpassingen(), is(0));
		assertThat(inschrijvingBatchVO.getAantalUitschrijvingen(), is(1));
		assertThat(inschrijvingBatchVO.getAantalVerwijderingen(), is(0));
		assertThat(inschrijvingBatchVO.getAantalRecords(), is(4));
		assertThat(inschrijvingBatchVO.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanlevering.Instelling));
		assertThat(inschrijvingBatchVO.getLaatsteAanlevering(), is(false));

		writeBronBatch(inschrijvingBatchVO);

	}
}
