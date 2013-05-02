package nl.topicus.eduarte.krd.bron.schakeltest.bve.terugkoppeling;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.eduarte.krd.bron.parser.BronEntiteitRecordingFactory;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.terugkoppeling.TerugkoppelingParser;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOTerugkoppelingExamenDecentraal;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOTerugkoppelingResultaat;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOVakgegevensRecord;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOVoorloopRecordBatch;

import org.junit.Test;

public class TerugkoppelingenVoBatchTest
{
	private VOTerugkoppelingExamenDecentraal terugkoppelbestand;

	@Test
	public void batch01OE003T() throws IOException, BronException
	{
		terugkoppelbestand = parseTerugkoppelbestand("01OE003T.V00");
		assertNotNull(terugkoppelbestand);
		assertThat(terugkoppelbestand.getAantalGeleverdeRecords(), is(equalTo(11)));

		VOVoorloopRecordBatch batch = terugkoppelbestand.getBatches().get(0);
		assertThat(batch.getAantalMeldingenFout(), is(equalTo(2)));
		assertThat(batch.getAantalMeldingenGoed(), is(equalTo(0)));
		assertThat(batch.getAantalMeldingenNogInBehandeling(), is(equalTo(0)));

		List< ? extends Object> terugkoppelingen = batch.getTerugkoppelingen();
		assertThat(terugkoppelingen.size(), is(equalTo(2)));

		VOTerugkoppelingResultaat resultaat = (VOTerugkoppelingResultaat) terugkoppelingen.get(0);
		assertThat(resultaat.getSignalen().size(), is(equalTo(1)));

		resultaat = (VOTerugkoppelingResultaat) terugkoppelingen.get(1);
		assertThat(resultaat.getVakMeldingen().size(), is(equalTo(3)));

		VOVakgegevensRecord vakgegevensRecord = resultaat.getVakMeldingen().get(0);
		assertThat(vakgegevensRecord.getSignalen().size(), is(equalTo(1)));
	}

	@Test
	public void batch01OE004T() throws IOException, BronException
	{
		terugkoppelbestand = parseTerugkoppelbestand("01OE004T.V00");
		Assert.assertNotNull(terugkoppelbestand);
	}

	private VOTerugkoppelingExamenDecentraal parseTerugkoppelbestand(String filename)
			throws IOException, BronException
	{
		byte[] bytes = ResourceUtil.readClassPathFileAsBytes(getClass(), filename);

		TerugkoppelingParser parser = new TerugkoppelingParser();
		parser.setCheckEmpty(false);
		parser.setCheckRequired(false);
		parser.setRecordingFactory(new BronEntiteitRecordingFactory());
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		VOTerugkoppelingExamenDecentraal record =
			parser.parse(bais, VOTerugkoppelingExamenDecentraal.class);
		return record;
	}

}
