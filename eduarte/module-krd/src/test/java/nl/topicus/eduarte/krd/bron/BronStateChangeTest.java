package nl.topicus.eduarte.krd.bron;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;

import org.junit.Test;

public class BronStateChangeTest
{
	@Test
	public void nieuweWaarde()
	{
		BronStateChange change = new BronStateChange(new Deelnemer(), "naam", null, "Alexander");
		assertThat(change.toString(), is("Deelnemer.naam:  -> Alexander"));
	}

	@Test
	public void datumWaarde()
	{
		Persoon persoon = new Persoon();
		persoon.setId(12345L);
		BronStateChange change =
			new BronStateChange(persoon, "geboorteDatum", TimeUtil.getInstance().parseDateString(
				"2009-01-01"), null);
		assertThat(change.toString(), is("Persoon.geboorteDatum: 2009-01-01 -> "));
	}
}
