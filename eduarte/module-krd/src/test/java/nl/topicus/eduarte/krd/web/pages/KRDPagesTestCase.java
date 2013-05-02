package nl.topicus.eduarte.krd.web.pages;

import nl.topicus.cobra.web.components.form.AutoFormRegistry;
import nl.topicus.eduarte.krd.KRDInitializer;
import nl.topicus.eduarte.tester.EduArteTestCase;

import org.junit.Before;

public abstract class KRDPagesTestCase extends EduArteTestCase
{
	@Before
	public void inittestsKRD()
	{
		KRDInitializer.initializeAutoFormRegistry(AutoFormRegistry.getInstance());
	}
}
