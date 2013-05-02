package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.eduarte.krd.web.pages.KRDPagesTestCase;

import org.junit.Test;

public class RedenUitDienstPageTest extends KRDPagesTestCase
{
	@Test
	public void toonRedenUitDienstZoekenPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(RedenUitDienstZoekenPage.class);
		tester.assertRenderedPage(RedenUitDienstZoekenPage.class);
	}

	@Test
	public void toonRedenUitDienstEditPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(RedenUitDienstZoekenPage.class);
		tester.assertRenderedPage(RedenUitDienstZoekenPage.class);
		tester.clickLink("layLeft:bottom:rightButtons:0:link");
		tester.assertRenderedPage(RedenUitDienstEditPage.class);
	}
}
