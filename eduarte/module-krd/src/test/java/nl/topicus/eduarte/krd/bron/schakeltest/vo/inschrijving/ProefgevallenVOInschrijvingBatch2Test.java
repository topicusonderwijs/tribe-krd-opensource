package nl.topicus.eduarte.krd.bron.schakeltest.vo.inschrijving;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Test;

public class ProefgevallenVOInschrijvingBatch2Test extends ProefgevallenVOInschrijving
{
	@Test
	public void proefgeval08() throws Exception
	{
		createGeval08();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(22));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getOnderwijsNummer(), is(100511515));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000004"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19910707)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getCodeLandAdres(), is("6039"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080531)));
		assertThat(melding.getILTCode(), is(2981));
		assertThat(melding.getLeerjaar(), is(4));
	}

	private void createGeval08() throws Exception
	{
		getDeelnemer1004();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval09() throws Exception
	{
		createGeval09();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(melding.getVestigingsVolgnummer(), is(22));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000005"));
		assertThat(melding.getAchternaam(), is("Achternaameenenzestig"));
		assertThat(melding.getAlleVoornamen(), is("Voornaameenenzestig"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19910711)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getStraatNaam(), is("Kempkensberg"));
		assertThat(melding.getHuisNummer(), is(2));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getPlaatsnaam(), is("Groningen"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080601)));
		assertThat(melding.getILTCode(), is(2781));
		assertThat(melding.getLeerjaar(), is(3));
	}

	private void createGeval09() throws Exception
	{
		getDeelnemer1005();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void VOBatch2Test() throws Exception
	{
		createGeval08();
		createGeval09();

		inschrijvingBatchVO = maakBronInschrijvingVOBatch();

		BronAanleverpunt aanleverpunt = new BronAanleverpunt();
		aanleverpunt.setLaatsteBatchNrVO(-1);
		aanleverpunt.setLaatsteBatchNrBO(99);
		aanleverpunt.setLaatsteBatchNrED(199);
		aanleverpunt.setLaatsteBatchNrVAVO(299);
		aanleverpunt.setNummer(22);

		inschrijvingBatchVO.setAanleverpunt(aanleverpunt);
		inschrijvingBatchVO.setBatchNummer(1);
		inschrijvingBatchVO.berekenControleTotalen();

		assertThat(inschrijvingBatchVO.getBRINNummer(), is("01OE"));
		assertThat(inschrijvingBatchVO.getAanleverPuntNummer(), is(22));
		assertThat(inschrijvingBatchVO.getBatchNummer(), is(1));
		assertThat(inschrijvingBatchVO.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(inschrijvingBatchVO.getSoortMelding(), is(SoortMelding.Inschrijving));
		assertThat(inschrijvingBatchVO.getAantalRecords(), is(2));
		assertThat(inschrijvingBatchVO.getAantalToevoegingen(), is(2));
		assertThat(inschrijvingBatchVO.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanlevering.Instelling));
		assertThat(inschrijvingBatchVO.getLaatsteAanlevering(), is(false));

		writeBronBatch(inschrijvingBatchVO);

	}
}
