package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.eduarte.krd.web.pages.KRDPagesTestCase;
import nl.topicus.eduarte.web.pages.beheer.organisatie.OrganisatieEenheidEditPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.OrganisatieEenheidZoekenPage;

import org.junit.Test;

public class OrganisatieEenheidPageTest extends KRDPagesTestCase
{
	@Test
	public void toonOrganisatieEenheidZoekenPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(OrganisatieEenheidZoekenPage.class);
		tester.assertRenderedPage(OrganisatieEenheidZoekenPage.class);
	}

	@Test
	public void toonOrganisatieEenheidEditPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(OrganisatieEenheidZoekenPage.class);
		tester.assertRenderedPage(OrganisatieEenheidZoekenPage.class);
		tester.clickLink("layLeft:bottom:rightButtons:0:link");
		tester.assertRenderedPage(OrganisatieEenheidEditPage.class);
	}
}
