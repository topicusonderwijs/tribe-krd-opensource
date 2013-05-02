package nl.topicus.eduarte.entities.begineinddatum;

import static nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.personen.PersoonAdres;

import org.junit.Before;
import org.junit.Test;

public class BeginEinddatumUtilTest
{
	private Date vandaag;

	private Date eergisteren;

	private Date gisteren;

	private Date morgen;

	private TimeUtil timeutil;

	private PersoonAdres adres;

	@Before
	public void setup()
	{
		timeutil = TimeUtil.getInstance();
		vandaag = timeutil.currentDate();
		eergisteren = timeutil.addDays(vandaag, -2);
		gisteren = timeutil.addDays(vandaag, -1);
		morgen = timeutil.addDays(vandaag, 1);
		adres = new PersoonAdresTest();
	}

	@Test
	public void filterLegeLijst()
	{
		List<PersoonAdres> lijst = new ArrayList<PersoonAdres>();
		Assert.assertEquals(Collections.emptyList(), getElementenOpPeildatum(lijst, vandaag));
	}

	@Test
	public void filterNaarLeegResultaat()
	{
		List<PersoonAdres> lijst = new ArrayList<PersoonAdres>();
		adres.setBegindatum(morgen);
		lijst.add(adres);

		Assert.assertEquals(Collections.emptyList(), getElementenOpPeildatum(lijst, vandaag));
		Assert.assertEquals(Collections.emptyList(), getElementenOpPeildatum(lijst, gisteren));
	}

	@Test
	public void filterNaarGevuldResultaat()
	{
		adres.setBegindatum(gisteren);
		List<PersoonAdres> lijst = Arrays.asList(adres);

		Assert.assertEquals(Arrays.asList(adres), getElementenOpPeildatum(lijst, vandaag));
		Assert.assertEquals(Arrays.asList(adres), getElementenOpPeildatum(lijst, morgen));
	}

	@Test
	public void verlopenInterval()
	{
		adres.setBegindatum(gisteren);
		adres.setEinddatum(vandaag);

		List<PersoonAdres> lijst = Arrays.asList(adres);

		Assert.assertEquals(Collections.emptyList(), getElementenOpPeildatum(lijst, eergisteren));
		Assert.assertEquals(Arrays.asList(adres), getElementenOpPeildatum(lijst, vandaag));
		Assert.assertEquals(Collections.emptyList(), getElementenOpPeildatum(lijst, morgen));
	}

	private class PersoonAdresTest extends PersoonAdres
	{
		private static final long serialVersionUID = 1L;

		public PersoonAdresTest()
		{

		}
	}
}
