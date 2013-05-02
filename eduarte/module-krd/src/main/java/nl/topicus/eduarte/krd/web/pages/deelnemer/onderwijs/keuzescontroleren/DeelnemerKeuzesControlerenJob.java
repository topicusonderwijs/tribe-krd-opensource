package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.keuzescontroleren;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.io.StringPrintWriter;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.hibernate.FlushMode;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/*
 * @author idserda
 */
@JobInfo(name = DeelnemerKeuzesControlerenJob.JOB_NAME)
@JobRunClass(DeelnemerKeuzesControlerenJobRun.class)
public class DeelnemerKeuzesControlerenJob extends EduArteJob
{
	public static final String JOB_NAME = "Deelnemer keuzes controleren";

	private DeelnemerKeuzesControlerenJobRun run;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException
	{
		setFlushMode(FlushMode.COMMIT);
		try
		{
			run = new DeelnemerKeuzesControlerenJobRun();
			run.setGestartDoor(getMedewerker());
			run.setRunStart(TimeUtil.getInstance().currentDateTime());

			controleerKeuzes();

			run.setSamenvatting(getRunSamenvatting());
			run.save();

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting(run.getSamenvatting());
			run.update();
			run.commit();
			flushAndClearHibernate();
		}
		catch (Exception e)
		{
			failJob(e);
			throw new JobExecutionException(e);
		}
	}

	private void controleerKeuzes()
	{
		VerbintenisDataAccessHelper verbintenisHelper =
			DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class);
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();

		for (Verbintenis verbintenis : verbintenisHelper.list(filter))
		{
			List<OnderwijsproductAfnameContext> afnameContexten = getKeuzeObjecten(verbintenis);
			for (@SuppressWarnings("unused")
			OnderwijsproductAfnameContext afnameContext : afnameContexten)
			{

			}
		}
	}

	private List<OnderwijsproductAfnameContext> getKeuzeObjecten(Verbintenis verbintenis)
	{
		ProductregelDataAccessHelper helper =
			DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class);
		ProductregelZoekFilter filter =
			new ProductregelZoekFilter(verbintenis.getOpleiding(), verbintenis.getCohort());
		filter.setTypeProductregel(TypeProductregel.Productregel);
		filter.addOrderByProperty("volgnummer");
		filter.addOrderByProperty("soortProductregel");
		List<Productregel> productregels = helper.list(filter);
		Map<Productregel, OnderwijsproductAfnameContext> keuzes =
			DataAccessRegistry.getHelper(OnderwijsproductAfnameContextDataAccessHelper.class).list(
				verbintenis);
		List<OnderwijsproductAfnameContext> res =
			new ArrayList<OnderwijsproductAfnameContext>(productregels.size());
		for (Productregel productregel : productregels)
		{
			OnderwijsproductAfnameContext context = keuzes.get(productregel);
			if (context != null)
			{
				res.add(context);
			}
		}
		return res;
	}

	private String getRunSamenvatting()
	{
		String ret = "Controleren van deelnemer keuzes";

		return ret;
	}

	@SuppressWarnings("unused")
	private DeelnemerKeuzesControlerenDataMap getDataMap()
	{
		return null;
	}

	private void setFlushMode(FlushMode flushmode)
	{
		SessionDataAccessHelper sessionDAH =
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class);
		sessionDAH.getHibernateSessionProvider().getSession().setFlushMode(flushmode);
	}

	void failJob(Exception e)
	{
		log.error("Keuzes controleren mislukt");
		run.setSamenvatting("Keuzes controleren mislukt, klik voor meer informatie");
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		setStatus("Keuzes controleren mislukt");
		StringPrintWriter spw = new StringPrintWriter();
		e.printStackTrace(spw);
		run.error("Keuzes controleren is mislukt door een onverwachte fout:\n" + spw.getString());
		run.saveOrUpdate();
		run.commit();
	}

}