package nl.topicus.eduarte.entities.landelijk;

import static nl.topicus.eduarte.entities.landelijk.Schooljaar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SchooljaarTest
{
	private IModel<Date> peildatumModel;

	@Test
	public void omschrijving2008_2009()
	{
		assertThat(Schooljaar.valueOf(2008).getOmschrijving(), is("2008/2009"));
	}

	@Test
	public void afkorting2008_2009()
	{
		assertThat(Schooljaar.valueOf(2008).getAfkorting(), is("08/09"));
	}

	@Test
	public void startJaar2008_2009is2008()
	{
		assertThat(Schooljaar.valueOf(2008).getStartJaar(), is(2008));
	}

	@Test
	public void eindJaar2008_2009is2009()
	{
		assertThat(Schooljaar.valueOf(2008).getEindJaar(), is(2009));
	}

	@Test
	public void valueOfDate2008IsSJ_2008_2009()
	{
		Date date = TimeUtil.getInstance().asDate(2008, 11, 20);
		assertThat(Schooljaar.valueOf(date), is(Schooljaar.valueOf(2008)));
	}

	@Test
	public void valueOfHuidigSchooljaarIsSchooljaarVanVandaag()
	{
		Date vandaag = TimeUtil.getInstance().currentDate();
		assertThat(Schooljaar.huidigSchooljaar(), is(Schooljaar.valueOf(vandaag)));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setBegindatumFaalt()
	{
		Schooljaar.huidigSchooljaar().setBegindatum(new Date());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setBegindatumNullFaalt()
	{
		Schooljaar.huidigSchooljaar().setBegindatum(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setEinddatumFaalt()
	{
		Schooljaar.huidigSchooljaar().setEinddatum(new Date());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void setEinddatumNullFaalt()
	{
		Schooljaar.huidigSchooljaar().setEinddatum(null);
	}

	@Test
	public void isAfgelopen()
	{
		assertTrue(Schooljaar.valueOf(1990).isAfgelopen());
	}

	@Test
	public void huidigSchooljaarIsNietAfgelopen()
	{
		assertFalse(Schooljaar.huidigSchooljaar().isAfgelopen());
	}

	@Test
	public void begindatum2008_2009_is_20080801()
	{
		Date begin = TimeUtil.getInstance().asDate(2008, Calendar.AUGUST, 1);
		assertThat(Schooljaar.valueOf(2008).getBegindatum(), is(begin));
	}

	@Test
	public void einddatum2008_2009_is_20090731()
	{
		Date eind = TimeUtil.getInstance().asDate(2009, Calendar.JULY, 31);
		assertThat(Schooljaar.valueOf(2008).getEinddatum(), is(eind));
	}

	@Test
	public void getVolgendSchooljaar2008_2009_is_2009_2010()
	{
		assertThat(Schooljaar.valueOf(2008).getVolgendSchooljaar(), is(Schooljaar.valueOf(2009)));
	}

	@Test
	public void getVorigSchooljaar2008_2009_is_2007_2008()
	{
		assertThat(Schooljaar.valueOf(2008).getVorigSchooljaar(), is(Schooljaar.valueOf(2007)));
	}

	@Before
	public void savePeildatumModel()
	{
		peildatumModel = EduArteContext.get().getPeildatumModel();
	}

	@Test
	public void valueOfPeildatumSchooljaar()
	{
		Date date = TimeUtil.getInstance().parseDateString("2005-11-20");
		EduArteContext.get().setPeildatumModel(new Model<Date>(date));
		assertThat(Schooljaar.schooljaarOpPeildatum(), is(Schooljaar.valueOf(2005)));
	}

	@After
	public void restorePeildatumModel()
	{
		EduArteContext.get().setPeildatumModel(peildatumModel);
	}

	@Test
	public void parse2008_2009()
	{
		assertThat(Schooljaar.parse("2008/2009"), is(Schooljaar.valueOf(2008)));
		assertThat(Schooljaar.parse("2008-2009"), is(Schooljaar.valueOf(2008)));
	}

	@Test
	public void parse08_09()
	{
		assertThat(Schooljaar.parse("08/09"), is(Schooljaar.valueOf(2008)));
		assertThat(Schooljaar.parse("08-09"), is(Schooljaar.valueOf(2008)));
	}

	@Test
	public void parse90_91()
	{
		assertThat(Schooljaar.parse("90/91"), is(Schooljaar.valueOf(1990)));
	}

	@Test
	public void parseAAAA_2009fails()
	{
		assertThat(Schooljaar.parse("AAAA/2009"), is(nullValue()));
	}

	@Test
	public void parse2008_AAAAfails()
	{
		assertThat(Schooljaar.parse("2008/AAAA"), is(nullValue()));
	}

	@Test
	public void parse2008A2009fails()
	{
		assertThat(Schooljaar.parse("2008A2009"), is(nullValue()));
	}

	@Test
	public void eenFebruari()
	{
		assertThat(Schooljaar.valueOf(2009).getEenFebruari(), is(TimeUtil.getInstance().asDate(
			2010, Calendar.FEBRUARY, 1)));
		assertThat(Schooljaar.valueOf(2010).getEenFebruari(), is(TimeUtil.getInstance().asDate(
			2011, Calendar.FEBRUARY, 1)));
	}

	@Test
	public void voorNa()
	{
		assertTrue(Schooljaar.valueOf(2008).voor(Schooljaar.valueOf(2009)));
		assertTrue(Schooljaar.valueOf(2008).gelijkOfVoor(Schooljaar.valueOf(2009)));
		assertFalse(Schooljaar.valueOf(2008).na(Schooljaar.valueOf(2009)));
		assertFalse(Schooljaar.valueOf(2008).gelijkOfNa(Schooljaar.valueOf(2009)));

		assertTrue(Schooljaar.valueOf(2009).na(Schooljaar.valueOf(2008)));
		assertTrue(Schooljaar.valueOf(2009).gelijkOfNa(Schooljaar.valueOf(2008)));
		assertFalse(Schooljaar.valueOf(2009).voor(Schooljaar.valueOf(2008)));
		assertFalse(Schooljaar.valueOf(2009).gelijkOfVoor(Schooljaar.valueOf(2008)));

		assertFalse(Schooljaar.valueOf(2008).voor(Schooljaar.valueOf(2008)));
		assertTrue(Schooljaar.valueOf(2008).gelijkOfVoor(Schooljaar.valueOf(2008)));
		assertFalse(Schooljaar.valueOf(2008).na(Schooljaar.valueOf(2008)));
		assertTrue(Schooljaar.valueOf(2008).gelijkOfNa(Schooljaar.valueOf(2008)));
	}

	@Test
	public void allePeildata()
	{
		Schooljaar schooljaar = huidigSchooljaar();
		assertThat(schooljaar.getBoPeildata(), is(Arrays.asList(schooljaar.getEenOktober(),
			schooljaar.getEenFebruari())));
	}

	@Test
	public void peildataNa1Augustus()
	{
		Schooljaar schooljaar = Schooljaar.huidigSchooljaar();
		assertThat(schooljaar.getBoPeildataOpOfNa(schooljaar.getBegindatum()), is(Arrays.asList(
			schooljaar.getEenOktober(), schooljaar.getEenFebruari())));
	}

	@Test
	public void peildataOpOfNa1Oktober()
	{
		Schooljaar schooljaar = Schooljaar.huidigSchooljaar();
		assertThat(schooljaar.getBoPeildataOpOfNa(schooljaar.getEenOktober()), is(Arrays.asList(
			schooljaar.getEenOktober(), schooljaar.getEenFebruari())));
	}

	@Test
	public void peildataNa1Oktober()
	{
		Schooljaar schooljaar = Schooljaar.huidigSchooljaar();
		TimeUtil util = TimeUtil.getInstance();
		Date tweeOktober = util.addDays(schooljaar.getEenOktober(), 1);

		assertThat(schooljaar.getBoPeildataOpOfNa(tweeOktober), is(Arrays.asList(schooljaar
			.getEenFebruari())));
	}

	@Test
	public void peildataNa1Januari()
	{
		Schooljaar schooljaar = Schooljaar.huidigSchooljaar();

		assertThat(schooljaar.getBoPeildataOpOfNa(schooljaar.getEenJanuari()), is(Arrays
			.asList(schooljaar.getEenFebruari())));
	}

	@Test
	public void peildataOpOfNa1Februari()
	{
		Schooljaar schooljaar = Schooljaar.huidigSchooljaar();

		assertThat(schooljaar.getBoPeildataOpOfNa(schooljaar.getEenFebruari()), is(Arrays
			.asList(schooljaar.getEenFebruari())));
	}

	@Test
	public void peildataNa1Februari()
	{
		Schooljaar schooljaar = Schooljaar.huidigSchooljaar();
		TimeUtil util = TimeUtil.getInstance();
		Date tweeFebruari = util.addDays(schooljaar.getEenFebruari(), 1);

		assertThat(schooljaar.getBoPeildataOpOfNa(tweeFebruari), is(Collections.<Date> emptyList()));
	}
}
