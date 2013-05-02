package nl.topicus.eduarte.krd.bron.schakeltest.vo.inschrijving;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
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
public class ProefgevallenVOInschrijvingBatch7Test extends ProefgevallenVOInschrijving
{
	/**
	 * Mutatie die wel betrekking heeft op de peildatum (Cumi-code wijzigen).
	 */
	@Test
	public void proefgeval24() throws Exception
	{
		createGeval24();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(melding.getSofiNummer(), is(800000122));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000002"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19940202)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getPostcode(), is("9711LK"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080707)));
		assertThat(melding.getILTCode(), is(2481));
		assertThat(melding.getLeerjaar(), is(3));
		assertThat(melding.getCumiCategorie(), is(CumiCategorie.Categorie4a));
		assertThat(melding.getCumiRatio(), is(CumiRatio.d2));
	}

	private void createGeval24() throws Exception
	{
		getDeelnemer1002();

		Plaatsing leerjaar3 = verbintenis.getPlaatsingOpPeildatum();
		leerjaar3.setLwoo(true);
		leerjaar3.setEinddatum(asDate(20080806));

		Plaatsing leerjaar4 = getPlaatsing(4, verbintenis);
		leerjaar4.setBegindatum(asDate(20080807));
		leerjaar4.setLwoo(true);
		verbintenis.getPlaatsingen().add(0, leerjaar4);

		Persoon persoon = verbintenis.getPersoon();
		CumiRatio oudeRatio = persoon.getCumiRatio();
		persoon.setDatumInNederland(TimeUtil.getInstance().addYears(
			TimeUtil.getInstance().currentDate(), -1));
		addChange(persoon, "cumiRatio", oudeRatio, persoon.getCumiRatio());

	}

	@Test
	public void proefgeval25() throws Exception
	{
		createGeval25();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(melding.getSofiNummer(), is(800000407));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000007"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19940707)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getPostcode(), is("9711LB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080930)));
		assertThat(melding.getILTCode(), is(3081));
		assertThat(melding.getLeerjaar(), is(4));
	}

	private void createGeval25() throws Exception
	{
		getDeelnemer1007();
		// bij proefgeval 14 was de opleiding al veranderd.
		verbintenis.setOpleiding(newOpleiding("3081", "Opleiding3081"));

		plaatsing.setLeerjaar(4);
		plaatsing.setBegindatum(asDate(20080930));
		// bij proefgeval 14 was het leerjaar al veranderd van 4 -> 3
		addChange(plaatsing, "leerjaar", 3, plaatsing.getLeerjaar());

	}

	@Test
	public void VOBatch7Test() throws Exception
	{
		createGeval24();
		createGeval25();

		inschrijvingBatchVO = maakBronInschrijvingVOBatch();
		inschrijvingBatchVO.setBatchNummer(6);
		inschrijvingBatchVO.setSoortMelding(SoortMelding.AccountantsMutatie);
		inschrijvingBatchVO
			.setVerantwoordelijkeAanlevering(VerantwoordelijkeAanlevering.Accountant);
		inschrijvingBatchVO.berekenControleTotalen();

		assertThat(inschrijvingBatchVO.getBRINNummer(), is("01OE"));
		assertThat(inschrijvingBatchVO.getAanleverPuntNummer(), is(0));
		assertThat(inschrijvingBatchVO.getBatchNummer(), is(6));
		assertThat(inschrijvingBatchVO.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(inschrijvingBatchVO.getSoortMelding(), is(SoortMelding.AccountantsMutatie));
		assertThat(inschrijvingBatchVO.getAantalRecords(), is(2));
		assertThat(inschrijvingBatchVO.getAantalAanpassingen(), is(2));
		assertThat(inschrijvingBatchVO.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanlevering.Accountant));
		assertThat(inschrijvingBatchVO.getLaatsteAanlevering(), is(false));

		writeBronBatch(inschrijvingBatchVO);

	}
}
