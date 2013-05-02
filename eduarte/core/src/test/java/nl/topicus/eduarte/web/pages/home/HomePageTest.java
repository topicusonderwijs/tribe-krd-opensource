package nl.topicus.eduarte.web.pages.home;

import nl.topicus.eduarte.tester.EduArteTestCase;
import nl.topicus.eduarte.web.pages.login.LoginPage;

import org.junit.Test;

public class HomePageTest extends EduArteTestCase
{
	@Test
	public void toonHomePage()
	{
		tester.setupRequestAndResponse();

		tester.startPage(HomePage.class);
		tester.assertRenderedPage(HomePage.class);
	}

	@Test
	public void logOff()
	{
		tester.setupRequestAndResponse();

		tester.startPage(HomePage.class);
		tester.assertRenderedPage(HomePage.class);
		tester.clickLink("layHeader:mainmenu:logoff");
		tester.assertRenderedPage(LoginPage.class);
	}

	@Test
	public void loggedOffUserKanHomePageNietBereiken()
	{
		tester.logoff();

		tester.startPage(HomePage.class);
		tester.assertRenderedPage(LoginPage.class);
	}
}
