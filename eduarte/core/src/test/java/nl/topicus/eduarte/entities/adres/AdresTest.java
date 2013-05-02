package nl.topicus.eduarte.entities.adres;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class AdresTest
{
	@Test
	public void testParseHuisnummer()
	{
		assertThat(Adres.parseHuisnummerToevoeging("100"), is(new String[] {"100", ""}));
		assertThat(Adres.parseHuisnummerToevoeging("100A"), is(new String[] {"100", "A"}));
		assertThat(Adres.parseHuisnummerToevoeging("100 A"), is(new String[] {"100", "A"}));
		assertThat(Adres.parseHuisnummerToevoeging("100 -200"), is(new String[] {"100", "-200"}));
		assertThat(Adres.parseHuisnummerToevoeging("1-3"), is(new String[] {"1", "-3"}));
	}
}