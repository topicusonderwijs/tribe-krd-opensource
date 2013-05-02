package nl.topicus.eduarte.krd.web.pages.intake.stap2;

import java.util.Date;

import junit.framework.Assert;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.krd.web.pages.KRDPagesTestCase;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardModel;
import nl.topicus.eduarte.tester.matchers.PropertyExpressionMatcher;

import org.apache.wicket.Component;
import org.junit.Before;
import org.junit.Test;

public class IntakeStap2BewerkPersoonRelatiePageTest extends KRDPagesTestCase
{
	private IntakeWizardModel wizard;

	private Relatie relatie;

	@Before
	public void setUp()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		wizard = new IntakeWizardModel();
		relatie = wizard.addNieuwePersoonRelatie();
	}

	@Test
	public void wettelijkeVertegenwoordigerNietTonenAlsDeelnemerMeerderjarig()
	{
		maakDeelnemerMeerderjarig();

		tester.startPage(new IntakeStap2BewerkPersoonRelatiePage(wizard, relatie, true));
		tester.assertRenderedPage(IntakeStap2BewerkPersoonRelatiePage.class);

		Component component =
			tester.first(tester.getLastRenderedPage(), new PropertyExpressionMatcher(
				"wettelijkeVertegenwoordiger"));
		Assert.assertNull(component);
	}

	private void maakDeelnemerMeerderjarig()
	{
		TimeUtil timeutil = TimeUtil.getInstance();
		Date geboortedatum = timeutil.currentDate();
		geboortedatum = timeutil.addYears(geboortedatum, -18);

		wizard.getDeelnemer().getPersoon().setGeboortedatum(geboortedatum);
	}

	@Test
	public void betalingsplichtigeNietTonenAlsDeelnemerMeerderjarig()
	{
		maakDeelnemerMeerderjarig();

		tester.startPage(new IntakeStap2BewerkPersoonRelatiePage(wizard, relatie, true));
		tester.assertRenderedPage(IntakeStap2BewerkPersoonRelatiePage.class);

		Component component =
			tester.first(tester.getLastRenderedPage(), new PropertyExpressionMatcher(
				"betalingsplichtige"));
		Assert.assertNull(component);
	}

	@Test
	public void wettelijkeVertegenwoordigerTonenAlsDeelnemerMinderjarig()
	{
		maakDeelnemerMinderjarig();

		tester.startPage(new IntakeStap2BewerkPersoonRelatiePage(wizard, relatie, true));
		tester.assertRenderedPage(IntakeStap2BewerkPersoonRelatiePage.class);

		Component component =
			tester.first(tester.getLastRenderedPage(), new PropertyExpressionMatcher(
				"wettelijkeVertegenwoordiger"));
		Assert.assertNotNull(component);
	}

	@Test
	public void betalingsplichtigeNietTonenAlsDeelnemerMinderjarig()
	{
		maakDeelnemerMinderjarig();

		tester.startPage(new IntakeStap2BewerkPersoonRelatiePage(wizard, relatie, true));
		tester.assertRenderedPage(IntakeStap2BewerkPersoonRelatiePage.class);

		Component component =
			tester.first(tester.getLastRenderedPage(), new PropertyExpressionMatcher(
				"betalingsplichtige"));
		Assert.assertNotNull(component);
	}

	private void maakDeelnemerMinderjarig()
	{
		TimeUtil timeutil = TimeUtil.getInstance();
		Date geboortedatum = timeutil.currentDate();
		geboortedatum = timeutil.addYears(geboortedatum, -18);
		geboortedatum = timeutil.addDays(geboortedatum, 1);

		wizard.getDeelnemer().getPersoon().setGeboortedatum(geboortedatum);
	}
}
