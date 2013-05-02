package nl.topicus.eduarte.web.pages.login;

import nl.topicus.eduarte.tester.EduArteTestCase;
import nl.topicus.eduarte.web.pages.home.HomePage;

import org.junit.Test;

public class LoginPageTest extends EduArteTestCase
{
	@Test
	public void toonLoginPage()
	{
		// default gedrag is dat er aangemeld is (om niet bij elke test aan te melden),
		// dus eerst afmelden
		tester.logoff();
		tester.setupRequestAndResponse();

		tester.startPage(LoginPage.class);
		tester.assertRenderedPage(LoginPage.class);
	}

	@Test
	public void toonLoginPageAlsNietIngelogd()
	{
		// default gedrag is dat er aangemeld is (om niet bij elke test aan te melden),
		// dus eerst afmelden
		tester.logoff();
		tester.setupRequestAndResponse();

		tester.startPage(HomePage.class);
		tester.assertRenderedPage(LoginPage.class);
	}
}
