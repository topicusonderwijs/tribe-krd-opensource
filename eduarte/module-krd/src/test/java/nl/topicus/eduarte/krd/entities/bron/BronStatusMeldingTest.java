package nl.topicus.eduarte.krd.entities.bron;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BronStatusMeldingTest
{
	@Test
	public void enumWaardes()
	{
		assertThat(BronMeldingStatus.values(), is(new BronMeldingStatus[] {
			BronMeldingStatus.GOEDGEKEURD, BronMeldingStatus.AFGEKEURD, BronMeldingStatus.WACHTRIJ,
			BronMeldingStatus.IN_BEHANDELING, BronMeldingStatus.VERWIJDERD}));
	}

	@Test
	public void AfgekeurdToChar()
	{
		assertThat(BronMeldingStatus.AFGEKEURD.toChar(), is('A'));
	}

	@Test
	public void GoedgekeurdToChar()
	{
		assertThat(BronMeldingStatus.GOEDGEKEURD.toChar(), is('G'));
	}

	@Test
	public void afgekeurdToString()
	{
		assertThat(BronMeldingStatus.AFGEKEURD.toString(), is("Afgekeurd"));
	}

	@Test
	public void goedgekeurdToString()
	{
		assertThat(BronMeldingStatus.GOEDGEKEURD.toString(), is("Goedgekeurd"));
	}

	@Test
	public void valueOfCharG()
	{
		assertThat(BronMeldingStatus.valueOf('G'), equalTo(BronMeldingStatus.GOEDGEKEURD));
		assertThat(BronMeldingStatus.valueOf('g'), equalTo(BronMeldingStatus.GOEDGEKEURD));
	}

	@Test
	public void valueOfCharA()
	{
		assertThat(BronMeldingStatus.valueOf('A'), equalTo(BronMeldingStatus.AFGEKEURD));
		assertThat(BronMeldingStatus.valueOf('a'), equalTo(BronMeldingStatus.AFGEKEURD));
	}

	@Test(expected = IllegalArgumentException.class)
	public void valueOfCharZFaalt()
	{
		BronMeldingStatus.valueOf('Z');
	}
}
