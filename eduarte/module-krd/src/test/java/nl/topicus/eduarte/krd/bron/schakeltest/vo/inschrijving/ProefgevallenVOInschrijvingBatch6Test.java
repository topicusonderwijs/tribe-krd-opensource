package nl.topicus.eduarte.krd.bron.schakeltest.vo.inschrijving;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Test;

/**
 * Fase 8 Reguliere iteraties tijdens mutatiestop bij aanleverpunt 0.
 */
public class ProefgevallenVOInschrijvingBatch6Test extends ProefgevallenVOInschrijving
{
	/**
	 * Mutatie die geen betrekking heeft op de peildatum (postcode wijzigen).
	 */
	@Test
	public void proefgeval22() throws Exception
	{
		createGeval22();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(melding.getSofiNummer(), is(800000481));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000001"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19890101)));
		assertThat(melding.getGeslacht(), is(Geslacht.Onbekend));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getPostcodeVolgensInstellingGewijzigd(), is("9711LB"));
		assertThat(melding.getDatumIngangAdresWijziging(), is(asDate(20081002)));
	}

	private void createGeval22() throws Exception
	{
		getDeelnemer1001();
		Adres adres = verbintenis.getPersoon().getFysiekAdres().getAdres();
		String oudePostcode = adres.getPostcode();
		adres.setPostcode("9711LB");
		verbintenis.getPersoon().getFysiekAdres().setBegindatum(asDate(20081002));
		addChange(adres, "postcode", oudePostcode, adres.getPostcode());
	}

	/**
	 * Mutatie die geen betrekking heeft op de peildatum (uitschrijving).
	 */
	@Test
	public void proefgeval23() throws Exception
	{
		createGeval23();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Uitschrijving));
		assertThat(melding.getOnderwijsNummer(), is(830000003));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000003"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19940303)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getPostcode(), is("9711LB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20081212)));
	}

	private void createGeval23() throws Exception
	{
		getDeelnemer1003();
		verbintenis.getDeelnemer().setOnderwijsnummer(830000003L);
		VerbintenisStatus oudeStatus = verbintenis.getStatus();
		verbintenis.setEinddatum(asDate(20081212));
		verbintenis.setStatus(VerbintenisStatus.Beeindigd);
		addChange(verbintenis, "einddatum", null, verbintenis.getEinddatum());
		addChange(verbintenis, "status", oudeStatus, verbintenis.getStatus());
	}

	@Test
	public void VOBatch6Test() throws Exception
	{
		createGeval22();
		createGeval23();

		inschrijvingBatchVO = maakBronInschrijvingVOBatch();
		inschrijvingBatchVO.setBatchNummer(5);
		inschrijvingBatchVO.berekenControleTotalen();

		assertThat(inschrijvingBatchVO.getBRINNummer(), is("01OE"));
		assertThat(inschrijvingBatchVO.getAanleverPuntNummer(), is(0));
		assertThat(inschrijvingBatchVO.getBatchNummer(), is(5));
		assertThat(inschrijvingBatchVO.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(inschrijvingBatchVO.getSoortMelding(), is(SoortMelding.Inschrijving));
		assertThat(inschrijvingBatchVO.getAantalRecords(), is(2));
		assertThat(inschrijvingBatchVO.getAantalAanpassingen(), is(1));
		assertThat(inschrijvingBatchVO.getAantalUitschrijvingen(), is(1));
		assertThat(inschrijvingBatchVO.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanlevering.Instelling));
		assertThat(inschrijvingBatchVO.getLaatsteAanlevering(), is(false));

		writeBronBatch(inschrijvingBatchVO);

	}
}
