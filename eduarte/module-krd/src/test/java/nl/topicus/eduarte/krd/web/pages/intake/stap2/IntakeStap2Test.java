package nl.topicus.eduarte.krd.web.pages.intake.stap2;

import nl.topicus.eduarte.krd.web.pages.KRDPagesTestCase;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardModel;

import org.junit.Test;

public class IntakeStap2Test extends KRDPagesTestCase
{
	@Test
	public void rendertIntakeRelaties()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		IntakeWizardModel model = new IntakeWizardModel();

		tester.startPage(new IntakeStap2Achtergrond(model));
		tester.assertRenderedPage(IntakeStap2Achtergrond.class);
	}
}
