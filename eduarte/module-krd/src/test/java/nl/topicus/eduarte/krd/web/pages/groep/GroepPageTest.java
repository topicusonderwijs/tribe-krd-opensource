package nl.topicus.eduarte.krd.web.pages.groep;

import nl.topicus.eduarte.krd.web.pages.KRDPagesTestCase;
import nl.topicus.eduarte.web.pages.groep.GroepZoekenPage;

import org.junit.Test;

public class GroepPageTest extends KRDPagesTestCase
{
	@Test
	public void toonGroepZoekenPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(GroepZoekenPage.class);
		tester.assertRenderedPage(GroepZoekenPage.class);
	}

	@Test
	public void toonGroepEditPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(GroepZoekenPage.class);
		tester.assertRenderedPage(GroepZoekenPage.class);
		tester.clickLink("layLeft:bottom:rightButtons:0:link");
		tester.assertRenderedPage(GroepEditPage.class);
	}
}
