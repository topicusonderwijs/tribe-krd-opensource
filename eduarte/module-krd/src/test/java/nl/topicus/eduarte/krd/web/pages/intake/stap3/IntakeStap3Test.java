package nl.topicus.eduarte.krd.web.pages.intake.stap3;

import nl.topicus.eduarte.krd.web.pages.KRDPagesTestCase;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardModel;

import org.junit.Test;

public class IntakeStap3Test extends KRDPagesTestCase
{
	@Test
	public void rendertIntakeRelaties()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		IntakeWizardModel model = new IntakeWizardModel();

		tester.startPage(new IntakeStap3Intakegesprekken(model));
		tester.assertRenderedPage(IntakeStap3Intakegesprekken.class);
	}
}
