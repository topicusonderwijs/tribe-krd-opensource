package nl.topicus.eduarte.krd.bron.schakeltest.bve.terugkoppeling;

import static nl.topicus.cobra.types.personalia.Geslacht.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.eduarte.krd.bron.parser.BronEntiteitRecordingFactory;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.Batchgegevens;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.MeldingEnSleutelgegevens;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.TerugkoppelRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.VoorlooprecordTerugkoppeling;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.terugkoppeling.TerugkoppelingParser;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.StatusMelding;

import org.junit.Test;

public class TerugkoppelingenBveBatchTest
{
	private VoorlooprecordTerugkoppeling voorlooprecord;

	private List< ? extends MeldingEnSleutelgegevens> meldingen;

	private Batchgegevens batchgegevens;

	/**
	 * Deze test controleert het terugkoppelbestand dat we ontvangen hebben naar
	 * aanleiding van een fout aangeleverde batch. De meldingen zijn dan ook allemaal
	 * afgekeurd.
	 */
	@Test
	public void terugkoppelingBOBatch0() throws BronException, IOException
	{
		voorlooprecord = parseTerugkoppelbestand("01AA000T.B01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();

		checkVoorlooprecord("01AA", 1, 1, Beroepsonderwijs, 1, 1);
		checkBatchgegevens(1, 13, 0, "20090612", null, 13, 0);

		// postcode *en* landcode aanleveren mag niet...
		check(1, 1, 'A', 1001, "210000004", null, "19910501", Man, "9722TB", "6030", 420, 421, 499);
		check(1, 2, 'A', 1009, "210000107", null, "19910509", Man, "9722TB", "6030", 420, 421, 499);
		check(1, 3, 'A', 1010, "210000119", null, "19910510", Man, "9722TB", "6030", 420, 421, 499);
		check(1, 4, 'A', 1011, "210000120", null, "19910511", Man, "9722TB", "6030", 420, 421, 499);
		check(1, 5, 'A', 1012, "210000132", null, "19910512", Man, "9722TB", "6030", 420, 421, 499);
		check(1, 6, 'A', 1013, "210000144", null, "19910513", Man, "9722TB", "6030", 420, 421, 499);
		check(1, 7, 'A', 1014, "210000156", null, "19910514", Man, "9722TB", "6030", 420, 421, 499);
		check(1, 8, 'A', 1015, "210000168", null, "19910515", Man, "9722TB", "6030", 420, 421, 499);
		check(1, 9, 'A', 1020, "210000235", null, "19910520", Man, "9722TB", "6030", 420, 421, 499);
		check(1, 10, 'A', 1025, "210000302", null, "19910525", Man, "9722TB", "6030", 420, 421, 499);
		check(1, 11, 'A', 1026, "210000326", null, "19910526", Man, "9722TB", "6030", 420, 421, 499);
		check(1, 12, 'A', 1029, "210000375", null, "19910529", Man, "9722TB", "6030", 420, 421, 499);
		check(1, 13, 'A', 1030, "210000387", null, "19910530", Man, "9722TB", "6030", 420, 421, 499);
	}

	@Test
	public void terugkoppelingBOBatch1() throws BronException, IOException
	{
		voorlooprecord = parseTerugkoppelbestand("01AA001T.B01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();

		checkVoorlooprecord("01AA", 1, 1, Beroepsonderwijs, 1, 1);
		checkBatchgegevens(1, 0, 13, "20090615", null, 13, 0);

		check(1, 1, 'G', 1001, "210000004", null, "19910501", Man, "9722TB", null, 410, 411, 420,
			421);
		check(1, 2, 'G', 1009, "210000107", null, "19910509", Man, "9722TB", null, 410, 411, 420,
			421);
		check(1, 3, 'G', 1010, "210000119", null, "19910510", Man, "9722TB", null, 410, 411, 420,
			421);
		check(1, 4, 'G', 1011, "210000120", null, "19910511", Man, "9722TB", null, 410, 411, 420,
			421);
		check(1, 5, 'G', 1012, "210000132", null, "19910512", Man, "9722TB", null, 410, 411, 420,
			421);
		check(1, 6, 'G', 1013, "210000144", null, "19910513", Man, "9722TB", null, 410, 411, 420,
			421);
		check(1, 7, 'G', 1014, "210000156", null, "19910514", Man, "9722TB", null, 410, 411, 420,
			421);
		check(1, 8, 'G', 1015, "210000168", null, "19910515", Man, "9722TB", null, 410, 411, 420,
			421);
		check(1, 9, 'G', 1020, "210000235", null, "19910520", Man, "9722TB", null, 410, 411, 420,
			421);
		check(1, 10, 'G', 1025, "210000302", null, "19910525", Man, "9722TB", null, 410, 411, 420,
			421);
		check(1, 11, 'G', 1026, "210000326", null, "19910526", Man, "9722TB", null, 410, 411, 420,
			421);
		check(1, 12, 'G', 1029, "210000375", null, "19910529", Man, "9722TB", null, 410, 411, 420,
			421);
		check(1, 13, 'G', 1030, "210000387", null, "19910530", Man, "9722TB", null, 410, 411, 420,
			421);
	}

	@Test
	public void terugkoppelingBOBatch2() throws BronException, IOException
	{
		voorlooprecord = parseTerugkoppelbestand("01AA002T.B01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();

		checkVoorlooprecord("01AA", 1, 2, Beroepsonderwijs, 1, 1);
		checkBatchgegevens(2, 2, 12, "20090615", null, 14, 0);

		check(2, 1, 'G', 1002, "210000697", null, "19910716", Man, "9722TB", null, 410, 411, 420,
			421, 421);
		check(2, 2, 'G', 1003, "210000028", null, "19910503", Man, "9722TB", null, 410, 411, 420,
			421);
		check(2, 3, 'G', 1004, null, "100320254", "19910701", Man, null, "6030", 420, 421);
		check(2, 4, 'G', 1005, "210000703", null, "19910717", Man, "9722TB", null, 410, 411, 420,
			421, 499);
		check(2, 5, 'G', 1006, null, "100144750", "19910702", Man, null, "6030", 420, 421, 499);
		check(2, 6, 'A', 1007, "80000122", null, "19900202", Vrouw, "9711LK", null, 420, 421, 499);
		check(2, 7, 'A', 1008, "210000727", null, "19910719", Man, "9722TB", null, 420, 421, 499);
		check(2, 8, 'G', 1016, "210000181", null, "19910516", Man, "9722TB", null, 410, 411, 420,
			421, 423);
		check(2, 9, 'G', 1017, "210000193", null, "19910517", Man, "9722TB", null, 410, 411, 420,
			421, 423);
		check(2, 10, 'G', 1018, "210000211", null, "19910518", Man, "9722TB", null, 410, 411, 420,
			421, 423);
		check(2, 11, 'G', 1019, "210000223", null, "19910519", Man, "9722TB", null, 410, 411, 420,
			421, 423);
		check(2, 12, 'G', 1021, "210000259", null, "19910521", Man, "9722TB", null, 410, 411, 420,
			421, 422, 499);
		check(2, 13, 'G', 1022, "210000260", null, "19910522", Man, "9722TB", null, 410, 411, 420,
			421, 422, 499);
		check(2, 14, 'G', 1023, "210000272", null, "19910523", Man, "9722TB", null, 410, 411, 420,
			421, 422, 499);
	}

	@Test
	public void terugkoppelingBOBatch3() throws BronException, IOException
	{
		voorlooprecord = parseTerugkoppelbestand("01AA003T.B01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();

		checkVoorlooprecord("01AA", 1, 3, Beroepsonderwijs, 1, 1);
		checkBatchgegevens(3, 0, 2, "20090618", null, 2, 0);

		check(3, 1, 'G', 1007, "800000122", null, "19900202", Vrouw, "9711LK", null, 410, 411, 420,
			421);
		check(3, 2, 'G', 1008, "210000727", null, "19910719", Man, "9722TB", null, 410, 411, 420,
			421);
	}

	@Test
	public void terugkoppelingBOBatch4() throws Exception
	{
		voorlooprecord = parseTerugkoppelbestand("01AA004T.B01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();

		checkVoorlooprecord("01AA", 1, 4, Beroepsonderwijs, 1, 1);
		checkBatchgegevens(4, 3, 12, "20090618", null, 15, 0);

		check(4, 1, 'G', 1009, "210000107", null, "19910509", Man, "9722TB", null, 410, 411, 420);
		check(4, 2, 'G', 1010, "210000119", null, "19910510", Man, "9722TB", null, 410, 411, 420);
		check(4, 6, 'A', 1014, "210000156", null, "19910514", Man, "9722TB", null, 421, 499);
		check(4, 8, 'A', 1016, "210000181", null, "19910516", Man, "9722TB", null, 423, 499);
		check(4, 9, 'A', 1017, "210000193", null, "19910517", Man, "9722TB", null, 423, 423, 499);
		check(4, 12, 'G', 1020, "210000235", null, "19910520", Man, "9722TB", null, 410, 411, 422,
			422, 499, 499);
	}

	@Test
	public void terugkoppelingBOBatch5() throws Exception
	{
		voorlooprecord = parseTerugkoppelbestand("01AA005T.B01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();

		checkVoorlooprecord("01AA", 1, 5, Beroepsonderwijs, 1, 1);
		checkBatchgegevens(5, 0, 3, "20090618", null, 3, 0);

	}

	@Test
	public void terugkoppelingBOBatch6() throws Exception
	{
		voorlooprecord = parseTerugkoppelbestand("01AA006T.B01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();

		checkVoorlooprecord("01AA", 1, 6, Beroepsonderwijs, 1, 1);
		checkBatchgegevens(6, 3, 1, "20090618", null, 4, 0);
	}

	@Test
	public void terugkoppelingBOBatch7() throws Exception
	{
		voorlooprecord = parseTerugkoppelbestand("01AA007T.B01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();

		checkVoorlooprecord("01AA", 1, 7, Beroepsonderwijs, 1, 1);
		checkBatchgegevens(7, 1, 2, "20090618", null, 3, 0);
	}

	@Test
	public void terugkoppelingEducatieBatch1() throws Exception
	{
		voorlooprecord = parseTerugkoppelbestand("04CY001T.E01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();
	}

	@Test
	public void terugkoppelingEducatieBatch2() throws Exception
	{
		voorlooprecord = parseTerugkoppelbestand("04CY002T.E01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();
	}

	@Test
	public void terugkoppelingEducatieBatch3() throws Exception
	{
		voorlooprecord = parseTerugkoppelbestand("04CY003T.E01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();
	}

	@Test
	public void terugkoppelingVavoBatch1() throws Exception
	{
		voorlooprecord = parseTerugkoppelbestand("01AA001T.A01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();
	}

	@Test
	public void terugkoppelingVavoBatch2() throws Exception
	{
		voorlooprecord = parseTerugkoppelbestand("01AA002T.A01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();
	}

	@Test
	public void terugkoppelingVavoBatch3() throws Exception
	{
		voorlooprecord = parseTerugkoppelbestand("01AA003T.A01");
		batchgegevens = voorlooprecord.getBatchgegevens().get(0);
		meldingen = batchgegevens.getMeldingen();
	}

	private void checkVoorlooprecord(String brin, int aanleverpunt, int batchnr, Sectordeel sector,
			int aantalTeruggekoppeldeBestanden, int aantalBatchgegevens)
	{
		assertThat(voorlooprecord.getBrinNummer(), is(brin));
		assertThat(voorlooprecord.getAanleverpuntNummer(), is(aanleverpunt));
		assertThat(voorlooprecord.getBestandSoort(), is(BestandSoort.TERUGKOPPELING));
		assertThat(voorlooprecord.getBRONBatchNummer(), is(batchnr));
		assertThat(voorlooprecord.getSectordeel(), is(sector));
		assertThat(voorlooprecord.getAantalTeruggekoppeldeAanleverbestanden(),
			is(aantalTeruggekoppeldeBestanden));
		assertThat(voorlooprecord.getBatchgegevens().size(), is(aantalBatchgegevens));
	}

	private void checkBatchgegevens(int batchnr, int aantalFout, int aantalGoed, String datum,
			String organisatieNr, int aantalMeldingen, int aantalInBehandeling)
	{
		assertThat("Batchnr", batchgegevens.getBatchnummerAanleverbestand(), is(equalTo(batchnr)));
		assertThat("Aantal fouten", batchgegevens.getCumulatiefAantalFouteMeldingen(),
			is(equalTo(aantalFout)));
		assertThat("Aantal goeden", batchgegevens.getCumulatiefAantalGoedeMeldingen(),
			is(equalTo(aantalGoed)));
		assertThat("Datum", batchgegevens.getDatumOntvangstAanleverbestand(), is(Datum.valueOf(
			datum).asDate()));
		assertThat("Organisatie nr", batchgegevens.getInternOrganisatienummer(), is(organisatieNr));
		assertThat("Aantal meldingen", batchgegevens.getMeldingenInAanleverbestand(),
			is(equalTo(aantalMeldingen)));
		assertThat("Aantal in behandeling", batchgegevens
			.getResterendAantalMeldingenInBehandeling(), is(equalTo(aantalInBehandeling)));
		assertThat("Aantal ingelezen meldingen", batchgegevens.getMeldingen().size(),
			is(equalTo(aantalMeldingen)));
	}

	private void check(int batchNr, int meldingNr, char status, int leerlingNr, String sofinummer,
			String onderwijsnummer, String geboortedatum, Geslacht geslacht, String postcode,
			String land, int... verwachtteRecords)
	{
		MeldingEnSleutelgegevens melding = meldingen.get(meldingNr - 1);
		assertThat(melding.getBatchnummerAanleverbestand(), is(batchNr));
		assertThat(melding.getMeldingnummer(), is(meldingNr));
		assertThat(melding.getLeerlingnummer(), is(String.valueOf(leerlingNr)));
		assertThat(melding.getSofinummer(), is(equalTo(sofinummer)));
		assertThat(melding.getOnderwijsnummer(), is(equalTo(onderwijsnummer)));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf(geboortedatum)));
		assertThat(melding.getGeslacht(), is(geslacht));
		assertThat(melding.getPostcodeVolgensInstelling(), is(postcode));
		assertThat(melding.getLand(), is(land));
		assertThat(melding.getRecords().size(), is(verwachtteRecords.length));
		assertThat(melding.getStatusMelding(), is(equalTo(StatusMelding.AFGEKEURD
			.parse("" + status))));

		for (int i = 0; i < verwachtteRecords.length; i++)
		{
			TerugkoppelRecord record = melding.getRecords().get(i);

			assertThat("Record type " + i, record.getRecordType(),
				is(equalTo(verwachtteRecords[i])));
		}
	}

	private VoorlooprecordTerugkoppeling parseTerugkoppelbestand(String filename)
			throws IOException, BronException
	{
		byte[] bytes = ResourceUtil.readClassPathFileAsBytes(getClass(), filename);

		TerugkoppelingParser parser = new TerugkoppelingParser();
		parser.setRecordingFactory(new BronEntiteitRecordingFactory());
		VoorlooprecordTerugkoppeling record =
			parser.parse(new ByteArrayInputStream(bytes), VoorlooprecordTerugkoppeling.class);
		return record;
	}
}
