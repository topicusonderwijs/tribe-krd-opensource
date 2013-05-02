package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.eduarte.krd.web.pages.KRDPagesTestCase;
import nl.topicus.eduarte.web.pages.beheer.organisatie.SoortOrganisatieEenheidEditPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.SoortOrganisatieEenheidZoekenPage;

import org.junit.Test;

public class SoortOrganisatieEenheidPageTest extends KRDPagesTestCase
{
	@Test
	public void toonSoortOrganisatieEenheidZoekenPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(SoortOrganisatieEenheidZoekenPage.class);
		tester.assertRenderedPage(SoortOrganisatieEenheidZoekenPage.class);
	}

	@Test
	public void toonSoortOrganisatieEenheidEditPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(SoortOrganisatieEenheidZoekenPage.class);
		tester.assertRenderedPage(SoortOrganisatieEenheidZoekenPage.class);
		tester.clickLink("layLeft:bottom:rightButtons:0:link");
		tester.assertRenderedPage(SoortOrganisatieEenheidEditPage.class);
	}
}
