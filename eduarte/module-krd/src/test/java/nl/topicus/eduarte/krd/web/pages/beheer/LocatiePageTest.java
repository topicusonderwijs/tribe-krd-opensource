package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.eduarte.krd.web.pages.KRDPagesTestCase;
import nl.topicus.eduarte.web.pages.beheer.organisatie.LocatieEditPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.LocatieZoekenPage;

import org.junit.Test;

public class LocatiePageTest extends KRDPagesTestCase
{
	@Test
	public void toonLocatieZoekenPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(LocatieZoekenPage.class);
		tester.assertRenderedPage(LocatieZoekenPage.class);
	}

	@Test
	public void toonLocatieEditPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(LocatieZoekenPage.class);
		tester.assertRenderedPage(LocatieZoekenPage.class);
		tester.clickLink("layLeft:bottom:rightButtons:0:link");
		tester.assertRenderedPage(LocatieEditPage.class);
	}
}
