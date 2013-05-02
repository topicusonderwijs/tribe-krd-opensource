package nl.topicus.eduarte.entities.inschrijving;

import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;
import nl.topicus.cobra.util.TimeUtil;

import org.junit.Test;

public class VerbintenisTest
{
	@Test
	public void statusIntakeTussenIntakeEnDefinitief()
	{
		assertThat(Intake.tussen(Intake, Definitief), is(true));
	}

	@Test
	public void statusIntakeBuitenIntakeEnDefinitief()
	{
		assertThat(Intake.tussen(Voorlopig, Definitief), is(false));
	}

	@Test
	public void statusVoorlopigTussenIntakeEnDefinitief()
	{
		assertThat(Voorlopig.tussen(Intake, Definitief), is(true));
	}

	@Test
	public void statusDefinitiefBuitenIntakeEnVoorlopig()
	{
		assertThat(Definitief.tussen(Intake, Voorlopig), is(false));
	}

	@Test
	public void geplandeDuurInStudiemaanden()
	{
		@SuppressWarnings("deprecation")
		Verbintenis verbintenis = new Verbintenis();

		verbintenis.setBegindatum(TimeUtil.getInstance().asDate(2010, 7, 1));
		verbintenis.setGeplandeEinddatum(TimeUtil.getInstance().asDate(2011, 6, 31));
		assertThat(verbintenis.getGeplandeDuurInStudiemaanden(), is(10));

		verbintenis.setBegindatum(TimeUtil.getInstance().asDate(2010, 4, 1));
		verbintenis.setGeplandeEinddatum(TimeUtil.getInstance().asDate(2010, 6, 31));
		assertThat(verbintenis.getGeplandeDuurInStudiemaanden(), is(2));

		verbintenis.setBegindatum(TimeUtil.getInstance().asDate(2010, 4, 1));
		verbintenis.setGeplandeEinddatum(TimeUtil.getInstance().asDate(2010, 7, 31));
		assertThat(verbintenis.getGeplandeDuurInStudiemaanden(), is(2));

		verbintenis.setBegindatum(TimeUtil.getInstance().asDate(2010, 4, 1));
		verbintenis.setGeplandeEinddatum(TimeUtil.getInstance().asDate(2010, 8, 31));
		assertThat(verbintenis.getGeplandeDuurInStudiemaanden(), is(3));

		verbintenis.setBegindatum(TimeUtil.getInstance().asDate(2010, 11, 1));
		verbintenis.setGeplandeEinddatum(TimeUtil.getInstance().asDate(2011, 0, 31));
		assertThat(verbintenis.getGeplandeDuurInStudiemaanden(), is(2));

		verbintenis.setBegindatum(TimeUtil.getInstance().asDate(2010, 7, 1));
		verbintenis.setGeplandeEinddatum(TimeUtil.getInstance().asDate(2012, 6, 31));
		assertThat(verbintenis.getGeplandeDuurInStudiemaanden(), is(20));

		verbintenis.setBegindatum(TimeUtil.getInstance().asDate(2010, 7, 1));
		verbintenis.setGeplandeEinddatum(TimeUtil.getInstance().asDate(2012, 9, 31));
		assertThat(verbintenis.getGeplandeDuurInStudiemaanden(), is(22));
	}
}
