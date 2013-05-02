package nl.topicus.eduarte.krd.web.pages.intake.stap4;

import nl.topicus.eduarte.krd.web.pages.KRDPagesTestCase;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardModel;

import org.junit.Test;

public class IntakeStap4Test extends KRDPagesTestCase
{

	@Test
	public void rendertIntakeRelaties()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		IntakeWizardModel model = new IntakeWizardModel();

		tester.startPage(new IntakeStap4Verbintenis(model));
		tester.assertRenderedPage(IntakeStap4Verbintenis.class);
	}
}
