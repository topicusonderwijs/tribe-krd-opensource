package nl.topicus.eduarte.krd.entities.bron;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;

import org.junit.Test;

public class BronBatchBveTest
{
	/**
	 * NullPointerException bij bepalen aantal meldingen nog in behandeling.
	 */
	@Test
	public void mantis52050_geval1()
	{
		BronBatchBVE batch = new BronBatchBVE();
		assertThat(batch.getAantalMeldingenInBehandeling(), is(0));
	}

	/**
	 * NullPointerException bij bepalen aantal meldingen nog in behandeling.
	 */
	@Test
	public void mantis52050_geval2()
	{
		BronBatchBVE batch = new BronBatchBVE();
		batch.setAantalMeldingen(10);
		assertThat(batch.getAantalMeldingenInBehandeling(), is(10));
	}
}
