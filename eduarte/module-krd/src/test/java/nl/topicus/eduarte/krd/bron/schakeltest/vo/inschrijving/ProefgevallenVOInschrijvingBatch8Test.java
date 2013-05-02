package nl.topicus.eduarte.krd.bron.schakeltest.vo.inschrijving;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Test;

/**
 * Fase 11 Iteraties na accountantsverklaring bij aanleverpunt 22.
 */
public class ProefgevallenVOInschrijvingBatch8Test extends ProefgevallenVOInschrijving
{
	/**
	 * Mutatie die geen betrekking heeft op de peildatum Buitenlands adres in een
	 * Nederlands adres wijzigen).
	 */
	@Test
	public void proefgeval26() throws Exception
	{
		createGeval26();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(22));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(melding.getOnderwijsNummer(), is(100511515));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000004"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19910707)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertNull(melding.getPostcode());
		assertThat(melding.getCodeLandAdres(), is("6039"));
		assertThat(melding.getPostcodeVolgensInstellingGewijzigd(), is("9711LK"));
		assertThat(melding.getDatumIngangAdresWijziging(), is(asDate(20081212)));

	}

	private void createGeval26() throws Exception
	{
		getDeelnemer1004();
		Adres adres = verbintenis.getPersoon().getFysiekAdres().getAdres();
		String oudePostcode = adres.getPostcode();
		Land oudeLand = adres.getLand();
		adres.setLand(Land.getNederland());
		adres.setPostcode("9711LK");
		verbintenis.getPersoon().getFysiekAdres().setBegindatum(asDate(20081212));
		addChange(adres, "land", oudeLand, adres.getLand());
		addChange(adres, "postcode", oudePostcode, adres.getPostcode());
	}

	/**
	 * Mutatie die geen betrekking heeft op de peildatum (einddatum toevoegen).
	 */
	@Test
	public void proefgeval27() throws Exception
	{
		createGeval27();
		controller.save();

		assertThat(getAantalInTransactie(BronInschrijvingsgegevensVOMelding.class), is(equalTo(1)));
		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(22));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Uitschrijving));
		assertThat(melding.getSofiNummer(), is(210000636));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000005"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19910711)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20090730)));
		// assertThat(melding.getILTCode(), is(2881));
		// assertThat(melding.getLeerjaar(), is(4));
	}

	private void createGeval27() throws Exception
	{
		getDeelnemer1005();
		// Dit was allemaal al aangepast bij proefgeval 19
		verbintenis.getPersoon().setBsn(210000636L);
		verbintenis.setOpleiding(newOpleiding("2881", "Opleiding2881"));
		verbintenis.getPlaatsingOpPeildatum().setLeerjaar(4);

		verbintenis.setEinddatum(asDate(20090730));
		addChange(verbintenis, "einddatum", null, verbintenis.getEinddatum());
		addChange(verbintenis, "status", VerbintenisStatus.Definitief, VerbintenisStatus.Beeindigd);
	}

	@Test
	public void VOBatch7Test() throws Exception
	{
		createGeval26();
		createGeval27();

		inschrijvingBatchVO = maakBronInschrijvingVOBatch();

		BronAanleverpunt aanleverpunt = new BronAanleverpunt();
		aanleverpunt.setLaatsteBatchNrVO(-1);
		aanleverpunt.setLaatsteBatchNrBO(99);
		aanleverpunt.setLaatsteBatchNrED(199);
		aanleverpunt.setLaatsteBatchNrVAVO(299);
		aanleverpunt.setNummer(22);
		inschrijvingBatchVO.setAanleverpunt(aanleverpunt);
		inschrijvingBatchVO.setBatchNummer(2);
		inschrijvingBatchVO.berekenControleTotalen();

		assertThat(inschrijvingBatchVO.getBRINNummer(), is("01OE"));
		assertThat(inschrijvingBatchVO.getAanleverPuntNummer(), is(22));
		assertThat(inschrijvingBatchVO.getBatchNummer(), is(2));
		assertThat(inschrijvingBatchVO.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(inschrijvingBatchVO.getSoortMelding(), is(SoortMelding.Inschrijving));
		assertThat(inschrijvingBatchVO.getAantalToevoegingen(), is(0));
		assertThat(inschrijvingBatchVO.getAantalAanpassingen(), is(1));
		assertThat(inschrijvingBatchVO.getAantalUitschrijvingen(), is(1));
		assertThat(inschrijvingBatchVO.getAantalVerwijderingen(), is(0));
		assertThat(inschrijvingBatchVO.getAantalRecords(), is(2));
		assertThat(inschrijvingBatchVO.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanlevering.Instelling));
		assertThat(inschrijvingBatchVO.getLaatsteAanlevering(), is(false));

		writeBronBatch(inschrijvingBatchVO);

	}
}
