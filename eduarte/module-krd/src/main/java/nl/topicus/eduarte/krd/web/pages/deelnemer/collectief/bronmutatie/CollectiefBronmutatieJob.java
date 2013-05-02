package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bronmutatie;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.io.StringPrintWriter;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.bron.BronController;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.VerbintenisCollectiefValidator;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;

import org.hibernate.FlushMode;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author loite
 */
@JobInfo(name = CollectiefBronmutatieJob.JOB_NAME)
@JobRunClass(CollectiefBronmutatieJobRun.class)
public class CollectiefBronmutatieJob extends EduArteJob
{
	public static final String JOB_NAME = "Collectief BRON-mutaties aanmaken";

	private CollectiefBronmutatieJobRun run;

	private Enum< ? > soortMutatie;

	private List<String> validationErrors = new ArrayList<String>();

	private List<Long> succesvol = new ArrayList<Long>();

	private List<Long> mislukt = new ArrayList<Long>();

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException,
			InterruptedException
	{
		setFlushMode(FlushMode.COMMIT);
		try
		{
			run = new CollectiefBronmutatieJobRun();
			run.setGestartDoor(getMedewerker());
			run.setRunStart(TimeUtil.getInstance().currentDateTime());

			extractData(context);

			run.setSamenvatting("Aanmaken BRON-mutaties");
			run.save();

			List<Verbintenis> selectie = getValue(context, "selectie");

			maakMutaties(selectie);

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting(run.getSamenvatting() + "Totaal: " + selectie.size()
				+ ", succesvol:  " + succesvol.size() + ", mislukt: " + mislukt.size());
			run.update();
			run.commit();
			flushAndClearHibernate();
		}
		catch (InterruptedException e)
		{
			log.error("Job was interrupted: " + e.getMessage(), e);
			throw e;
		}
		catch (Exception e)
		{
			failJob(e);
			throw new JobExecutionException(e);
		}
	}

	private void maakMutaties(List<Verbintenis> selectie) throws InterruptedException
	{
		int count = 0;
		for (Verbintenis entiteit : selectie)
		{
			if (wijzigigenMogelijk(entiteit))
			{
				boolean success = false;
				if (entiteit.isVOVerbintenis())
				{
					if (soortMutatie instanceof SoortMutatie)
					{
						Plaatsing plaatsing = entiteit.getPlaatsingOpPeildatum();
						maakNieuweBronMelding(plaatsing);
						success = true;
					}
				}
				else if (entiteit.isBOVerbintenis() || entiteit.isEducatieVerbintenis()
					|| entiteit.isVAVOVerbintenis())
				{
					if (soortMutatie instanceof nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie)
					{
						maakNieuweBronMelding(entiteit);
						success = true;
					}
				}
				if (success)
				{
					succesvol.add(entiteit.getId());
					createJobRunDetail(createDeelnemerOmschrijving(entiteit)
						+ " BRON-melding aanmaken geslaagd voor verbintenis "
						+ entiteit.getOmschrijving());
				}
				else
				{
					mislukt.add(entiteit.getId());
				}
			}
			else
			{
				mislukt.add(entiteit.getId());
			}
			setProgress(count, selectie.size());
			count++;

			flushHibernate();
		}
	}

	private void maakNieuweBronMelding(Verbintenis verbintenis)
	{
		BronController bronController = new BronController();

		if (verbintenis.isEducatieVerbintenis())
		{
			verbintenis.setHandmatigVersturenNaarBron(soortMutatie);
			bronController.controleerOpWijzigingenOpBronVelden(verbintenis,
				new Object[] {soortMutatie}, new Object[] {null},
				new String[] {"handmatigVersturenNaarBronMutatie"});
			bronController.save();
		}
		else if (verbintenis.isBOVerbintenis())
		{
			verbintenis.setHandmatigVersturenNaarBron(soortMutatie);
			bronController.controleerOpWijzigingenOpBronVelden(verbintenis,
				new Object[] {soortMutatie}, new Object[] {null},
				new String[] {"handmatigVersturenNaarBronMutatie"});
			bronController.save();
		}

		BatchDataAccessHelper< ? > helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		helper.batchExecute();
	}

	private void maakNieuweBronMelding(Plaatsing plaatsing) throws BronException
	{
		BronController bronController = new BronController();

		plaatsing.getVerbintenis().setHandmatigVersturenNaarBron(soortMutatie);
		plaatsing.setHandmatigVersturenNaarBron(soortMutatie);

		bronController.controleerOpWijzigingenOpBronVelden(plaatsing, new Object[] {soortMutatie},
			new Object[] {null}, new String[] {"handmatigVersturenNaarBronMutatie"});

		bronController.save();

		BatchDataAccessHelper< ? > helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		helper.batchExecute();
	}

	private void extractData(JobExecutionContext context)
	{
		soortMutatie = getValue(context, "soortMutatie");
	}

	private boolean wijzigigenMogelijk(Verbintenis entiteit)
	{
		checkVerplichteVeldenEnBron(entiteit);

		if (!validationErrors.isEmpty())
		{
			createJobRunDetailMetErrors();
			return false;
		}
		else
		{
			return true;
		}
	}

	private void checkVerplichteVeldenEnBron(Verbintenis entiteit)
	{
		addErrors(new VerbintenisCollectiefValidator(entiteit, entiteit.getStatus()).validate());
	}

	private void createJobRunDetail(String error)
	{
		CollectiefBronmutatieJobRunDetail detail = new CollectiefBronmutatieJobRunDetail(run);
		detail.setUitkomst(error);
		detail.save();
	}

	private void createJobRunDetailMetErrors()
	{
		for (String error : validationErrors)
		{
			createJobRunDetail(error);
		}
		validationErrors.clear();
	}

	private void addErrors(List<String> newValidationErrors)
	{
		validationErrors.addAll(newValidationErrors);
	}

	private String createDeelnemerOmschrijving(Verbintenis verbintenis)
	{
		Deelnemer deelnemer = verbintenis.getDeelnemer();

		String msg =
			"Deelnemer: " + deelnemer.getPersoon().getAchternaam() + ", "
				+ deelnemer.getPersoon().getVoornamen() + " (" + deelnemer.getDeelnemernummer()
				+ "). ";
		return msg;
	}

	private void setFlushMode(FlushMode flushmode)
	{
		SessionDataAccessHelper sessionDAH =
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class);
		sessionDAH.getHibernateSessionProvider().getSession().setFlushMode(flushmode);
	}

	void failJob(Exception e)
	{
		log.error("Aanmaken BRON-mutaties mislukt");

		run.setSamenvatting("Aanmaken BRON-mutaties mislukt, klik voor meer informatie");
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		setStatus("Aanmaken BRON-mutaties mislukt");

		StringPrintWriter spw = new StringPrintWriter();
		e.printStackTrace(spw);
		run.error("Aanmaken BRON-mutaties is mislukt door een onverwachte fout:\n"
			+ spw.getString());

		run.saveOrUpdate();
		run.commit();
	}

}