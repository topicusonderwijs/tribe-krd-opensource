package nl.topicus.eduarte.krd.bron.schakeltest.vo.inschrijving;

import static java.util.Arrays.*;
import static nl.topicus.eduarte.tester.hibernate.DatabaseAction.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Test;

/**
 * Iteraties voor mutatiestop bij aanleverpunt 0.
 */
@SuppressWarnings("hiding")
public class ProefgevallenVOInschrijvingBatch4Test extends ProefgevallenVOInschrijving
{
	/**
	 * Aanpassen van persoonsgegevens (geboortedatum) van een persoon met een
	 * onderwijsnummer, waardoor het sofi-nummer wordt gevonden.
	 */
	@Test
	public void proefgeval17() throws Exception
	{
		createGeval17();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();
		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));

		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(melding.getOnderwijsNummer(), is(100511771));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000006"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19910708)));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcode(), is("9722TB"));
		assertThat(melding.getGeboorteDatumGewijzigd(), is(Datum.valueOf(19900606)));
	}

	private void createGeval17() throws Exception
	{
		getDeelnemer1006();

		// aanpassingen die gedaan zijn bij proefgeval 12
		verbintenis.getDeelnemer().setOnderwijsnummer(100511771L);
		verbintenis.setBegindatum(asDate(20080930));
		verbintenis.setOpleiding(newOpleiding("2981", "Ander opleiding"));

		// aanpassen van de geboortedatum
		Persoon persoon = verbintenis.getPersoon();
		Date vorigeGeboortedatum = persoon.getGeboortedatum();
		persoon.setGeboortedatum(asDate(19900606));
		addChange(persoon, "geboortedatum", vorigeGeboortedatum, persoon.getGeboortedatum());
	}

	/**
	 * Een nieuwe inschrijving behorende bij de uitschrijving bij proefgeval 15 met een
	 * ingangsdatum die één dag ligt na de uitschrijvingdatum van de eerste inschrijving.
	 */
	@Test
	public void proefgeval18() throws Exception
	{
		createGeval18();
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));

		assertThat(melding.getVestigingsVolgnummer(), is(21));
		assertThat(melding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(melding.getSofiNummer(), is(800000419));
		assertThat(melding.getLeerlingNummerInstelling(), is("1000000009"));
		assertThat(melding.getGeboorteDatum(), is(Datum.valueOf(19940909)));
		assertThat(melding.getGeslacht(), is(Geslacht.Vrouw));
		assertThat(melding.getPostcode(), is("9711LB"));
		assertThat(melding.getIngangsDatum(), is(asDate(20080930)));
		assertThat(melding.getILTCode(), is(2781));
		assertThat(melding.getLeerjaar(), is(3));
	}

	private void createGeval18() throws Exception
	{
		getDeelnemer1009();
		verbintenis.setBegindatum(asDate(20080930));
		plaatsing.setBegindatum(verbintenis.getBegindatum());
		verbintenis.setOpleiding(newOpleiding("2781", "Opleiding2781"));
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void VOBatch4Test() throws Exception
	{
		createGeval17();
		createGeval18();

		inschrijvingBatchVO = maakBronInschrijvingVOBatch();
		inschrijvingBatchVO.setBatchNummer(3);
		inschrijvingBatchVO.berekenControleTotalen();

		assertThat(inschrijvingBatchVO.getBRINNummer(), is("01OE"));
		assertThat(inschrijvingBatchVO.getAanleverPuntNummer(), is(0));
		assertThat(inschrijvingBatchVO.getBatchNummer(), is(3));
		assertThat(inschrijvingBatchVO.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(inschrijvingBatchVO.getSoortMelding(), is(SoortMelding.Inschrijving));
		assertThat(inschrijvingBatchVO.getAantalRecords(), is(2));
		assertThat(inschrijvingBatchVO.getAantalToevoegingen(), is(1));
		assertThat(inschrijvingBatchVO.getAantalAanpassingen(), is(1));
		assertThat(inschrijvingBatchVO.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanlevering.Instelling));
		assertThat(inschrijvingBatchVO.getLaatsteAanlevering(), is(false));

		writeBronBatch(inschrijvingBatchVO);

	}
}
