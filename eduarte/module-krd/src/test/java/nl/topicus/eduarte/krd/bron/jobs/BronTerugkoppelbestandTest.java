package nl.topicus.eduarte.krd.bron.jobs;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.eduarte.krd.bron.jobs.BronTerugkoppelbestandProcessor.BronParseResult;
import nl.topicus.eduarte.krd.bron.parser.BronEntiteitRecordingFactory;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronBveTerugkoppelbestand;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronVoTerugkoppelbestand;
import nl.topicus.eduarte.tester.EduArteTestCase;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.VoorlooprecordTerugkoppeling;
import nl.topicus.onderwijs.duo.bron.terugkoppeling.TerugkoppelingParser;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOTerugkoppelingDecentraal;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOTerugkoppelingExamenDecentraal;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOTerugkoppelingInschrijvingDecentraal;

import org.junit.Test;

public class BronTerugkoppelbestandTest extends EduArteTestCase
{
	@Test
	public void berekenControleGetallenBve() throws Exception
	{
		byte[] file =
			ResourceUtil.readClassPathFileAsBytes(BronTerugkoppelbestandTest.class, "04EM824T.B00");

		BronParseResult<VoorlooprecordTerugkoppeling> result =
			parseTerugkoppelbestand(file, VoorlooprecordTerugkoppeling.class);

		BronBveTerugkoppelbestand terugkoppeling = (BronBveTerugkoppelbestand) result.getResult();
		terugkoppeling.berekenControleTotalen();

		assertThat(terugkoppeling.getAantalMeldingen(), is(1070));
		assertThat(terugkoppeling.getAantalSignalen(), is(370));

		assertThat(terugkoppeling.getAantalGoedgekeurdeMeldingen(), is(1040));
		assertThat(terugkoppeling.getAantalAfgekeurdeMeldingen(), is(30));
		assertThat(terugkoppeling.getAantalAfkeurSignalen(), is(30));
		assertThat(terugkoppeling.getBatchesInBestand(), is("266, 268"));
		assertThat(terugkoppeling.getAantalGeleverdeRecords(), is(4164));

		terugkoppeling.berekenControleTotalen();

		assertThat(terugkoppeling.getAantalMeldingen(), is(1070));
		assertThat(terugkoppeling.getAantalSignalen(), is(370));

		assertThat(terugkoppeling.getAantalGoedgekeurdeMeldingen(), is(1040));
		assertThat(terugkoppeling.getAantalAfgekeurdeMeldingen(), is(30));
		assertThat(terugkoppeling.getAantalAfkeurSignalen(), is(30));
		assertThat(terugkoppeling.getBatchesInBestand(), is("266, 268"));
		assertThat(terugkoppeling.getAantalGeleverdeRecords(), is(4164));
	}

	@Test
	public void berekenControleGetallenVo() throws Exception
	{
		byte[] file =
			ResourceUtil.readClassPathFileAsBytes(BronTerugkoppelbestandTest.class, "01OE003T.V00");
		Class< ? extends VOTerugkoppelingDecentraal> soort = getVoorlooprecordSoort(file);
		BronParseResult< ? extends VOTerugkoppelingDecentraal> result =
			parseTerugkoppelbestand(file, soort);

		BronVoTerugkoppelbestand bestand = (BronVoTerugkoppelbestand) result.getResult();

		bestand.berekenControleGetallen();
		assertThat(bestand.getAantalMeldingen(), is(2));
		assertThat(bestand.getAantalSignalen(), is(4));
		assertThat(bestand.getAantalAfkeurSignalen(), is(4));
		assertThat(bestand.getAantalAfgekeurdeMeldingen(), is(2));
		assertThat(bestand.getAantalGoedgekeurdeMeldingen(), is(0));
		assertThat(bestand.getBatchesInBestand(), is("003"));

	}

	public Class< ? extends VOTerugkoppelingDecentraal> getVoorlooprecordSoort(byte[] contents)
	{
		String recordtype = new String(contents, 13, Math.min(15, contents.length));
		if ("Examenresultaat".equals(recordtype.trim()))
		{
			return VOTerugkoppelingExamenDecentraal.class;
		}
		else
		{
			return VOTerugkoppelingInschrijvingDecentraal.class;
		}

	}

	/**
	 * Parses the contents based on the rootRecordType.
	 * 
	 * @throws BronException
	 * 
	 * @see TerugkoppelingParser
	 */
	protected <T> BronParseResult<T> parseTerugkoppelbestand(byte[] contents,
			Class<T> rootRecordType) throws BronException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(contents);
		TerugkoppelingParser parser = new TerugkoppelingParser();
		parser.setCheckEmpty(false);
		parser.setCheckRequired(false);
		parser.setRecordingFactory(new BronEntiteitRecordingFactory());

		T voorlooprecord = parser.parse(bais, rootRecordType);
		return new BronParseResult<T>(voorlooprecord);
	}
}
