package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.eduarte.krd.web.pages.KRDPagesTestCase;

import org.junit.Test;

public class FunctiePageTest extends KRDPagesTestCase
{
	@Test
	public void toonFunctieZoekenPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(FunctieZoekenPage.class);
		tester.assertRenderedPage(FunctieZoekenPage.class);
	}

	@Test
	public void toonFunctieEditPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(FunctieZoekenPage.class);
		tester.assertRenderedPage(FunctieZoekenPage.class);
		tester.clickLink("layLeft:bottom:rightButtons:0:link");
		tester.assertRenderedPage(FunctieEditPage.class);
	}
}
