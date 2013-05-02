package nl.topicus.eduarte.entities.security.authentication.vasco;

import static nl.topicus.eduarte.entities.security.authentication.vasco.TokenStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.eduarte.tester.EduArteTestCase;

import org.junit.Before;
import org.junit.Test;

public class TokenTest extends EduArteTestCase
{
	private Token token;

	@Before
	public void setup()
	{
		token = new Token("123456789GODEFAULT3", "GODEFAULT3", "123456789abcdefg");
	}

	@Test
	public void applicatienaamWordtVerwijderdVanSerienummer()
	{
		assertThat(token.getSerienummer(), is("123456789"));
	}

	@Test
	public void serienummerZonderapplicatienaamBlijftSerienummer()
	{
		assertThat(token.getSerienummer(), is("123456789"));
	}

	@Test
	public void standaardStatusIsBeschikbaar()
	{
		assertThat(token.getStatus(), is(Beschikbaar));
	}

	@Test
	public void deblokkerenUitgegevenGeblokkeerdToken()
	{
		token.geefUit(tester.getAccount());
		token.blokkeer();
		token.deblokkeer();
		assertThat(token.getStatus(), is(Uitgegeven));
		assertThat(token.getGebruiker(), is(tester.getAccount()));
	}

	@Test
	public void deblokkerenBeschikbaarGeblokkeerdToken()
	{
		token.blokkeer();
		token.deblokkeer();
		assertThat(token.getStatus(), is(Beschikbaar));
		assertThat(token.getGebruiker(), is(nullValue()));
	}

	@Test
	public void blokkerenZetStatus()
	{
		token.blokkeer();
		assertThat(token.getStatus(), is(Geblokkeerd));
		assertThat(token.getGebruiker(), is(nullValue()));
	}

	@Test
	public void blokkerenUitgegevenTokenZetStatus()
	{
		token.geefUit(tester.getAccount());
		token.blokkeer();
		assertThat(token.getStatus(), is(Geblokkeerd));
		assertThat(token.getGebruiker(), is(tester.getAccount()));
	}

	@Test
	public void innemenGeblokkeerdTokenLaatTokenGeblokkeerd()
	{
		token.geefUit(tester.getAccount());
		token.blokkeer();
		token.neemIn();
		assertThat(token.getStatus(), is(Geblokkeerd));
		assertThat(token.getGebruiker(), is(nullValue()));
	}

	@Test
	public void uitgevenAanAccountZetStatus()
	{
		token.geefUit(tester.getAccount());

		assertThat(token.getStatus(), is(Uitgegeven));
		assertThat(token.getGebruiker(), is(tester.getAccount()));
	}

	@Test
	public void innemenVanAccountZetStatus()
	{
		token.geefUit(tester.getAccount());

		assertThat(token.getGebruiker(), is(tester.getAccount()));
		assertThat(token.getStatus(), is(Uitgegeven));

		token.neemIn();
		assertThat(token.getGebruiker(), is(nullValue()));
		assertThat(token.getStatus(), is(Beschikbaar));
	}
}
