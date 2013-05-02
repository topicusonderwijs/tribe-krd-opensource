package nl.topicus.eduarte.krd.jobs;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.ToegestaneExamenstatusOvergangDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.Examenstatus;
import nl.topicus.eduarte.entities.examen.ExamenstatusOvergang;
import nl.topicus.eduarte.entities.examen.ToegestaneExamenstatusOvergang;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.dao.helpers.ExamendeelnameDataAccessHelper;
import nl.topicus.eduarte.krd.entities.CriteriumbankControleJobRun;
import nl.topicus.eduarte.util.criteriumbank.CriteriumbankControle;

import org.quartz.JobExecutionContext;

@JobInfo(name = CriteriumbankControleJob.JOB_NAME)
@JobRunClass(CriteriumbankControleJobRun.class)
public class CriteriumbankControleJob extends EduArteJob
{
	public static final String JOB_NAME = "Criteriumbank controle";

	private int controleGeslaagd = 0;

	private int controleNietGeslaagd = 0;

	private CriteriumbankControleJobRun jobRun;

	private ToegestaneExamenstatusOvergang toegestaneOvergang;

	private List<Long> examendeelnameIds;

	private List<Long> verbintenisIds;

	@Override
	protected void executeJob(JobExecutionContext context) throws InterruptedException
	{
		setFlushInterval(25);
		extractData(context);
		jobRun = new CriteriumbankControleJobRun();
		jobRun.setGestartDoor(getMedewerker());
		jobRun.setRunStart(TimeUtil.getInstance().currentDateTime());
		jobRun.setSamenvatting("Criteriumbankcontrole voor " + toegestaneOvergang.getActie());
		jobRun.save();

		doeCriteriumBankControles();

		jobRun.setRunEinde(TimeUtil.getInstance().currentDateTime());
		jobRun.setSamenvatting(jobRun.getSamenvatting() + ": " + controleGeslaagd
			+ " deelnemers voldoen aan criteriumbankcontrole, " + controleNietGeslaagd
			+ " deelnemers voldoen niet. ");
		jobRun.update();
		jobRun.commit();

	}

	private void doeCriteriumBankControles() throws InterruptedException
	{
		int count = 0;
		for (Long examendeelnameId : examendeelnameIds)
		{
			Long verbintenisId = verbintenisIds.get(count);
			Examendeelname deelname = getExamendeelname(examendeelnameId, verbintenisId);
			ExamenstatusOvergang overgang = createOvergangEnControle(deelname);
			setStatus("Controle uitvoeren voor "
				+ deelname.getVerbintenis().getDeelnemer().getDeelnemernummer());
			pasDeelnameAan(deelname, overgang);
			overgang.save();
			setProgress(count, examendeelnameIds.size());
			count = flushAndClearHibernateAndIncCount(count);
		}
	}

	private ExamenstatusOvergang createOvergangEnControle(Examendeelname deelname)
	{
		ExamenstatusOvergang overgang = createExamenstatusOvergang(deelname);
		CriteriumbankControle controle = new CriteriumbankControle(deelname.getVerbintenis());
		boolean voldoet = controle.voldoetAanCriteriumbank();
		Examenstatus naarStatus;
		if (voldoet && !controle.isHeeftFouten())
		{
			controleGeslaagd++;
			naarStatus = toegestaneOvergang.getNaarExamenstatus();
		}
		else
		{
			controleNietGeslaagd++;
			naarStatus = toegestaneOvergang.getAfgewezenExamenstatus();
			String opmerkingen = controle.getAlleMeldingenFormatted();
			overgang.setOpmerkingen(opmerkingen);
			if (opmerkingen != null && !opmerkingen.isEmpty())
				createJobRunDetail(jobRun, deelname, opmerkingen);
		}

		overgang.setNaarStatus(naarStatus);
		return overgang;
	}

	private void pasDeelnameAan(Examendeelname deelname, ExamenstatusOvergang overgang)
	{
		deelname.setExamenstatus(overgang.getNaarStatus());
		deelname.pasAfnameContextenAan(overgang.getNaarStatus());
		deelname.setDatumLaatsteStatusovergang(overgang.getDatumTijd());
		deelname.saveOrUpdate();
	}

	private ToegestaneExamenstatusOvergang getToegestaneOvergang(
			Long toegestaneExamenstatusOvergangId)
	{
		ToegestaneExamenstatusOvergangDataAccessHelper helper =
			DataAccessRegistry.getHelper(ToegestaneExamenstatusOvergangDataAccessHelper.class);
		return helper.get(ToegestaneExamenstatusOvergang.class, toegestaneExamenstatusOvergangId);
	}

	private Examendeelname getExamendeelname(Long examendeelnameId, Long verbintenisId)
	{
		if (examendeelnameId == null)
		{
			return createExamendeelname(verbintenisId);
		}
		ExamendeelnameDataAccessHelper helper =
			DataAccessRegistry.getHelper(ExamendeelnameDataAccessHelper.class);
		return helper.get(Examendeelname.class, examendeelnameId);
	}

	private Examendeelname createExamendeelname(Long verbintenisId)
	{
		Verbintenis verbintenis =
			DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class).getVerbintenisById(
				verbintenisId);
		Examendeelname deelname = new Examendeelname(verbintenis);
		if (toegestaneOvergang.getExamenWorkflow().isHeeftTijdvakken())
		{
			deelname.setTijdvak(Integer.valueOf(1));
		}
		verbintenis.getExamendeelnames().add(deelname);
		return deelname;
	}

	private void createJobRunDetail(CriteriumbankControleJobRun run, Examendeelname examendeelname,
			String opmerkingen)
	{
		JobRunDetail detail = new JobRunDetail(run);
		StringBuilder dln = new StringBuilder();
		dln.append(examendeelname.getVerbintenis().getDeelnemer().getContextInfoOmschrijving())
			.append(" - ").append(
				examendeelname.getVerbintenis().getDeelnemer().getDeelnemernummer()).append(" - ")
			.append(examendeelname.getVerbintenis().getContextInfoOmschrijving());
		dln.append(": ").append(opmerkingen);
		detail.setUitkomst(dln.toString());
		detail.save();
		run.getDetails().add(detail);
	}

	private ExamenstatusOvergang createExamenstatusOvergang(Examendeelname examendeelname)
	{
		ExamenstatusOvergang overgang = new ExamenstatusOvergang();
		overgang.setDatumTijd(TimeUtil.getInstance().currentDateTime());
		overgang.setExamendeelname(examendeelname);
		overgang.setVanStatus(examendeelname.getExamenstatus());
		examendeelname.getStatusovergangen().add(overgang);
		return overgang;
	}

	private void extractData(JobExecutionContext context)
	{
		Long toegExamenstOvergId = getValue(context, "toegestaneExamenstatusOvergangId");
		toegestaneOvergang = getToegestaneOvergang(toegExamenstOvergId);
		examendeelnameIds = getValue(context, "examendeelnameIds");
		verbintenisIds = getValue(context, "verbintenisIds");
	}
}
