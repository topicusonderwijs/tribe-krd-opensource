package nl.topicus.eduarte.resultaten;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OrganisatieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.IllegalResultaatException;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = "file:src/test/java/nl/topicus/eduarte/resultaten/inithibernate.xml")
@Ignore
public class ResultatenVerifyTest extends AbstractJUnit4SpringContextTests
{
	private static Logger log = LoggerFactory.getLogger(ResultatenVerifyTest.class);

	@Autowired
	private ResultaatDataAccessHelper resultaatDataAccessHelper;

	@Autowired
	private OrganisatieDataAccessHelper organisatieDataAccessHelper;

	@Autowired
	private DeelnemerDataAccessHelper deelnemerDataAccessHelper;

	@Autowired
	private SessionDataAccessHelper sessionDataAccessHelper;

	private List<String> errors = new ArrayList<String>();

	@Before
	public void clearErrorList()
	{
		errors.clear();
	}

	protected void assertNoErrors()
	{
		if (!errors.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			for (String curError : errors)
			{
				sb.append(curError).append('\n');
			}
			fail(sb.toString());
		}
	}

	protected void addError(String error)
	{
		log.warn(error);
		errors.add(error);
	}

	@Test
	@Ignore
	public void testEntities()
	{
		try
		{
			EduArteContext.get().setOrganisatie(
				organisatieDataAccessHelper.getOrganisatie("ROC Aventus"));
			EduArteContext.get().setPeildatumModel(
				new Model<Date>(TimeUtil.getInstance().currentDate()));

			int count = 0;
			int clearinterval = 500;
			log.info("getDeelnemerIdsMetResultaten()");
			List<Long> deelnemerids = deelnemerDataAccessHelper.getDeelnemerIdsMetResultaten();
			log.info("scanning " + deelnemerids.size() + " deelnemers");

			for (Long curId : deelnemerids)
			{
				try
				{
					float loc = ((float) count / deelnemerids.size());

					if (count % clearinterval == 0)
					{
						resultaatDataAccessHelper.flush();
						sessionDataAccessHelper.clearSession();
					}
					if (count % 100 == 0)
						log.info((loc * 100) + "%");

					List<Deelnemer> deelnemers =
						Arrays.asList(deelnemerDataAccessHelper.get(Deelnemer.class, curId));
					ResultaatZoekFilter resultaatZoekFilter =
						new ResultaatZoekFilter(new ToetsZoekFilter(), deelnemers);
					resultaatDataAccessHelper.verifyResultaten(resultaatZoekFilter);
				}
				catch (IllegalResultaatException e)
				{
					addError(e.getMessage());
				}
				count++;
			}
		}
		finally
		{
			EduArteContext.get();
			EduArteContext.clearContext();
		}
		assertNoErrors();
	}
}
