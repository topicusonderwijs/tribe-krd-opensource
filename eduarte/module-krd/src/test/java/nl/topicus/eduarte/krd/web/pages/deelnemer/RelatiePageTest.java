package nl.topicus.eduarte.krd.web.pages.deelnemer;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.personen.RelatieSoort;
import nl.topicus.eduarte.krd.web.pages.KRDPagesTestCase;
import nl.topicus.eduarte.krd.web.pages.deelnemer.relatie.DeelnemerPersoonAlsRelatieToevoegenPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.relatie.EditRelatiePage;
import nl.topicus.eduarte.web.pages.deelnemer.personalia.DeelnemerRelatiesOverzichtPage;

import org.apache.wicket.util.tester.FormTester;
import org.junit.Ignore;
import org.junit.Test;

public class RelatiePageTest extends KRDPagesTestCase
{
	@Test
	public void toonDeelnemerRelatiesOverzichtPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		Deelnemer deelnemer =
			DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).get(102446L);

		tester.startPage(new DeelnemerRelatiesOverzichtPage(deelnemer));
		tester.assertRenderedPage(DeelnemerRelatiesOverzichtPage.class);
	}

	@Test
	@Ignore
	public void toonPersoonAlsRelatieToevoegenPage()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		Deelnemer deelnemer =
			DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).get(102446L);

		tester.startPage(new DeelnemerRelatiesOverzichtPage(deelnemer));
		tester.assertRenderedPage(DeelnemerRelatiesOverzichtPage.class);
		tester.clickLink("layLeft:bottom:rightButtons:0:link");
		tester.assertRenderedPage(DeelnemerPersoonAlsRelatieToevoegenPage.class);
	}

	@Ignore("werkt niet dankzij bug WICKET-2203")
	@Test
	public void nieuwPersoonAlsRelatieToevoegen()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		Deelnemer deelnemer =
			DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).get(102446L);

		tester.startPage(new DeelnemerRelatiesOverzichtPage(deelnemer));
		tester.assertRenderedPage(DeelnemerRelatiesOverzichtPage.class);
		tester.clickLink("layLeft:bottom:rightButtons:0:link");
		tester.assertRenderedPage(DeelnemerPersoonAlsRelatieToevoegenPage.class);
		tester.clickLink("layLeft:bottom:rightButtons:0:link");
		tester.assertRenderedPage(EditRelatiePage.class);

		EditRelatiePage page = (EditRelatiePage) tester.getLastRenderedPage();

		Relatie relatie = page.getRelatie();
		RelatieSoort soort = new RelatieSoort();
		soort.setNaam("Ouder");
		relatie.setRelatieSoort(soort);
		relatie.getRelatie().setAchternaam("Aajoud");
		relatie.getRelatie().setOfficieleAchternaam("Aajoud");

		List<PersoonAdres> adressen = relatie.getRelatie().getFysiekAdressen();
		for (PersoonAdres adres : adressen)
		{
			adres.getAdres().setPostcode("1234AA");
			adres.getAdres().setHuisnummer("123");
		}
		adressen = relatie.getRelatie().getPostAdressen();
		for (PersoonAdres adres : adressen)
		{
			adres.getAdres().setPostcode("1234AA");
			adres.getAdres().setHuisnummer("123");
		}

		FormTester formTester = tester.newFormTester("personaliaForm");
		formTester.submit();
		tester.clickLink("layLeft:bottom:rightButtons:0:link");

		tester.assertErrorMessages(new String[] {});
		tester.assertRenderedPage(DeelnemerRelatiesOverzichtPage.class);
	}
}
