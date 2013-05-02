package nl.topicus.eduarte.krd.entities.bron;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.PersoonsgegevensRecord;

import org.junit.Before;
import org.junit.Test;

public class BronBveAanleverRecordTest
{
	private BronAanleverMelding melding;

	private PersoonsgegevensRecord record;

	@Before
	public void setup()
	{
		Deelnemer deelnemer = new Deelnemer();
		deelnemer.setPersoon(new Persoon());
		melding = new BronAanleverMelding(deelnemer);
		record = BronBveAanleverRecord.newPersoonsgegevensRecord(melding);
	}

	@Test
	public void setPlaatsnaam()
	{
		record.setPlaatsnaam("12345678901234567890123456");
		assertThat(record.getPlaatsnaam().length(), is(equalTo(24)));
		assertThat(record.getPlaatsnaam(), is(equalTo("123456789012345678901234")));
	}

	@Test
	public void setStraatnaam()
	{
		record.setStraatnaam("12345678901234567890123456");
		assertThat(record.getStraatnaam().length(), is(equalTo(24)));
		assertThat(record.getStraatnaam(), is(equalTo("123456789012345678901234")));
	}

	@Test
	public void setLocatieOmschrijving()
	{
		record.setLocatieOmschrijving("1234567890123456789012345678901234567890");
		assertThat(record.getLocatieOmschrijving().length(), is(equalTo(35)));
		assertThat(record.getLocatieOmschrijving(),
			is(equalTo("12345678901234567890123456789012345")));
	}

	@Test
	public void setAlleVoornamen()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 22; i++)
		{
			sb.append("1234567890");
		}
		record.setAlleVoornamen(sb.toString());
		assertThat(record.getAlleVoornamen().length(), is(equalTo(200)));
		assertThat(record.getAlleVoornamen(), is(equalTo(sb.toString().substring(0, 200))));
	}

	@Test
	public void setAchternaam()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 22; i++)
		{
			sb.append("1234567890");
		}
		record.setAchternaam(sb.toString());
		assertThat(record.getAchternaam().length(), is(equalTo(200)));
		assertThat(record.getAchternaam(), is(equalTo(sb.toString().substring(0, 200))));
	}

	@Test
	public void setVoorvoegsel()
	{
		record.setVoorvoegsel("12345678901234567890123456");
		assertThat(record.getVoorvoegsel().length(), is(equalTo(10)));
		assertThat(record.getVoorvoegsel(), is(equalTo("1234567890")));
	}

	@Test
	public void setHuisnummerToevoeging()
	{
		record.setHuisnummerToevoeging("12345678901234567890123456");
		assertThat(record.getHuisnummerToevoeging().length(), is(equalTo(5)));
		assertThat(record.getHuisnummerToevoeging(), is(equalTo("12345")));
	}

	@Test
	public void setAdresregelBuitenland1()
	{
		record.setAdresregelBuitenland1("1234567890123456789012345678901234567890");
		assertThat(record.getAdresregelBuitenland1().length(), is(equalTo(35)));
		assertThat(record.getAdresregelBuitenland1(),
			is(equalTo("12345678901234567890123456789012345")));
	}

	@Test
	public void setAdresregelBuitenland2()
	{
		record.setAdresregelBuitenland2("1234567890123456789012345678901234567890");
		assertThat(record.getAdresregelBuitenland2().length(), is(equalTo(35)));
		assertThat(record.getAdresregelBuitenland2(),
			is(equalTo("12345678901234567890123456789012345")));
	}

	@Test
	public void setAdresregelBuitenland3()
	{
		record.setAdresregelBuitenland3("1234567890123456789012345678901234567890");
		assertThat(record.getAdresregelBuitenland3().length(), is(equalTo(35)));
		assertThat(record.getAdresregelBuitenland3(),
			is(equalTo("12345678901234567890123456789012345")));
	}
}
