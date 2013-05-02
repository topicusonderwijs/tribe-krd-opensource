package nl.topicus.eduarte.entities.organisatie;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BrinTest
{
	@Test
	public void testBrin()
	{
		assertThat(Brin.testBrincode("00AA"), is(true));
		assertThat(Brin.testBrincode("00AA1"), is(true));
		assertThat(Brin.testBrincode("00AA01"), is(true));
		assertThat(Brin.testBrincode("0011"), is(true));
		assertThat(Brin.testBrincode("00110"), is(true));
		assertThat(Brin.testBrincode("001101"), is(true));

		assertThat(Brin.testBrincode("0"), is(false));
		assertThat(Brin.testBrincode("00"), is(false));
		assertThat(Brin.testBrincode("000"), is(false));
		assertThat(Brin.testBrincode("AA00"), is(false));
		assertThat(Brin.testBrincode("AA00A"), is(false));
		assertThat(Brin.testBrincode("0000AA"), is(false));
		assertThat(Brin.testBrincode("00AAAA"), is(false));

	}

	@Test
	public void vestigingVolgnummer()
	{
		assertThat(new Brin("00AA1").getVestigingsVolgnummer(), is(1));
		assertThat(new Brin("00AA01").getVestigingsVolgnummer(), is(1));
		assertThat(new Brin("00AA09").getVestigingsVolgnummer(), is(9));
		assertNull(new Brin("00AA").getVestigingsVolgnummer());
	}
}
