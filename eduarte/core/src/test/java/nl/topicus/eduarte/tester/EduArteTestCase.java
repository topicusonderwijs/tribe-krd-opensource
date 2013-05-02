package nl.topicus.eduarte.tester;

import java.util.Date;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoFormRegistry;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.CoreModuleGenericInitializer;

import org.apache.wicket.model.Model;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@ContextConfiguration(locations = {"classpath:/nl/topicus/eduarte/tester/spring-eduartetester.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class})
public abstract class EduArteTestCase
{
	@Autowired
	protected EduArteTester tester;

	@Before
	public void setupStuff()
	{
		// if (tester == null)
		// {
		// tester = new EduArteTester();
		// // tester.addModule("coreModule", new CoreModule());
		//
		// }
		CoreModuleGenericInitializer.initializeAutoFormRegistry(AutoFormRegistry.getInstance());
		EduArteContext.get().setPeildatumModel(
			new Model<Date>(TimeUtil.getInstance().currentDate()));
		EduArteContext.get().setOrganisatie(tester.getInstelling());
		tester.voerTestUitMetMedewerker();
	}

	@After
	public void cleanup()
	{
		EduArteContext.clearContext();
		tester.getWicketSession().cleanupFeedbackMessages();

		BatchDataAccessHelper< ? > helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		helper.batchRollback();
	}
}
