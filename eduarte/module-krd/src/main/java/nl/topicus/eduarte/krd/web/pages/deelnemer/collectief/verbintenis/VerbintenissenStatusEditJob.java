package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.verbintenis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.io.StringPrintWriter;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vervolgonderwijs;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.inschrijving.Vervolgonderwijs.SoortVervolgonderwijs;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.settings.MutatieBlokkedatumSetting;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.jobs.rapportage.RapportageJobDataMap;
import nl.topicus.eduarte.jobs.rapportage.RapportageJobUtil;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.CollectieveStatusovergangJobDataMap;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.VerbintenisCollectiefBlokkadedatumValidator;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.VerbintenisCollectiefValidator;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumValidatorMode;

import org.hibernate.FlushMode;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

/*
 * @author idserda
 */
@JobInfo(name = VerbintenissenStatusEditJob.JOB_NAME)
@JobRunClass(VerbintenissenStatusEditJobRun.class)
public class VerbintenissenStatusEditJob extends EduArteJob
{
	public static final String JOB_NAME = "Collectief staus van verbintenissen wijzigen";

	private VerbintenissenStatusEditJobRun run;

	private List<String> validationErrors = new ArrayList<String>();

	private List<Long> succesvol = new ArrayList<Long>();

	private List<Long> mislukt = new ArrayList<Long>();

	private VerbintenisStatus beginstatus;

	private VerbintenisStatus eindstatus;

	private Date einddatum;

	private RedenUitschrijving redenUitschrijving;

	private DocumentTemplate documentTemplate;

	private Date verbintenisBlokkadedatum;

	// Toelichting bij beeindigen verbintenis
	private String toelichting;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException,
			InterruptedException
	{
		setFlushMode(FlushMode.COMMIT);
		try
		{
			run = new VerbintenissenStatusEditJobRun();
			run.setGestartDoor(getMedewerker());
			run.setRunStart(TimeUtil.getInstance().currentDateTime());

			extractData(context);

			run.setSamenvatting("Verbintenisstatus wijzigen van " + beginstatus.toString()
				+ " naar " + eindstatus.toString() + ". ");
			run.save();

			List<Verbintenis> verbintenissen = getValue(context, "selectie");

			wijzigVerbintenissenStatus(verbintenissen);

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting(run.getSamenvatting() + " Totaal: " + verbintenissen.size()
				+ ", succesvol:  " + succesvol.size() + ", mislukt: " + mislukt.size());
			run.update();
			run.commit();
			flushAndClearHibernate();

			if (VerbintenisStatus.Afgedrukt.equals(eindstatus))
			{
				if (succesvol.size() > 0)
				{
					List<Verbintenis> afdrukken =
						DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class).list(
							Verbintenis.class, succesvol);
					drukVerbintenisAf(afdrukken);
					createJobRunDetail("Rapportagetaak gestart voor het genereren van onderwijsovereenkomsten.");
				}
			}
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

	private void wijzigVerbintenissenStatus(List<Verbintenis> verbintenissen)
			throws InterruptedException
	{
		int count = 0;
		for (Verbintenis verbintenis : verbintenissen)
		{
			if (statusovergangMogelijk(verbintenis))
			{
				updateVerbintenis(verbintenis);
				succesvol.add(verbintenis.getId());
				createJobRunDetail(createDeelnemerOmschrijving(verbintenis)
					+ " Statusovergang geslaagd voor verbintenis " + verbintenis.getOmschrijving());
			}
			else
			{
				// JobRunDetail met foutmelding wordt in validator aangemaakt.
				mislukt.add(verbintenis.getId());
			}
			setProgress(count, verbintenissen.size());
			count++;

			// Nodig omdat anders aanmaken van BRON meldingen misgaat.
			flushHibernate();
		}
	}

	private void updateVerbintenis(Verbintenis verbintenis)
	{
		verbintenis.setStatus(eindstatus);
		if (eindstatus == VerbintenisStatus.Definitief
			&& verbintenis.getBekostigd() == Bekostigd.Nee && verbintenis.isBOVerbintenis())
			verbintenis.setBekostigd(Bekostigd.Ja);

		if (VerbintenisStatus.Beeindigd.equals(eindstatus))
			beeindigVerbintenis(verbintenis);

		verbintenis.saveOrUpdate();
	}

	private void drukVerbintenisAf(List<Verbintenis> verbintenissen) throws SchedulerException
	{
		RapportageJobDataMap<Verbintenis, Verbintenis> datamap =
			new RapportageJobDataMap<Verbintenis, Verbintenis>();

		datamap.setDahClass(VerbintenisDataAccessHelper.class);
		datamap.setSelection(verbintenissen);
		datamap.setDocumentTemplate(documentTemplate);
		datamap.setEindType(documentTemplate.getType());

		RapportageJobUtil.startRapportageJob(documentTemplate, datamap);

	}

	private void beeindigVerbintenis(Verbintenis verbintenis)
	{
		verbintenis.setRedenUitschrijving(redenUitschrijving);
		verbintenis.setEinddatum(einddatum);
		verbintenis.setToelichting(toelichting);
		setVervolgonderwijsVerbintenis(verbintenis);

		beeindigBPV(verbintenis);
		beeindigPlaatsingen(verbintenis);
	}

	private void setVervolgonderwijsVerbintenis(Verbintenis verbintenis)
	{
		Vervolgonderwijs vervolgonderwijs = new Vervolgonderwijs();
		vervolgonderwijs.setSoortVervolgonderwijs(SoortVervolgonderwijs.Onbekend);
		vervolgonderwijs.saveOrUpdate();

		verbintenis.setVervolgonderwijs(vervolgonderwijs);
	}

	private void beeindigBPV(Verbintenis verbintenis)
	{
		for (BPVInschrijving bpvInschrijving : verbintenis.getBpvInschrijvingen())
		{
			if (BPVStatus.Definitief.equals(bpvInschrijving.getStatus())
				&& bpvInschrijving.getRedenUitschrijving() == null
				&& bpvInschrijving.getEinddatum() == null)
			{
				createJobRunDetail(createDeelnemerOmschrijving(verbintenis) + " BPV beeindigd: "
					+ bpvInschrijving.toString());

				bpvInschrijving.setRedenUitschrijving(redenUitschrijving);
				bpvInschrijving.setStatus(BPVStatus.Beëindigd);
				bpvInschrijving.setEinddatum(einddatum);
				bpvInschrijving.update();
			}
		}
	}

	private void beeindigPlaatsingen(Verbintenis verbintenis)
	{
		for (Plaatsing plaatsing : verbintenis.getPlaatsingen())
		{
			if (!plaatsing.getBegindatum().after(verbintenis.getEinddatum())
				&& plaatsing.getEinddatumNotNull().after(verbintenis.getBegindatum()))
			{
				createJobRunDetail(createDeelnemerOmschrijving(verbintenis)
					+ " plaatsing beeindigd: " + plaatsing.getGeldigVanTotBeschrijving());

				plaatsing.setEinddatum(einddatum);
				plaatsing.update();
			}
		}
	}

	private boolean statusovergangMogelijk(Verbintenis verbintenis)
	{
		validationErrors.clear();

		checkVerplichteVelden(verbintenis);
		checkBlokkadeDatum(verbintenis);
		checkBeeindigenMogelijk(verbintenis);

		if (!validationErrors.isEmpty())
		{
			createJobRunDetailMetErrors();
			return false;
		}
		else
			return true;
	}

	private void checkBlokkadeDatum(Verbintenis verbintenis)
	{
		if (getBlokkadedatum() != null)
		{
			BlokkadedatumValidatorMode mode = null;

			if (eindstatus.equals(VerbintenisStatus.Beeindigd))
				mode = BlokkadedatumValidatorMode.Beeindigen;
			else
				mode = BlokkadedatumValidatorMode.Bewerken;

			verbintenis.setEinddatum(einddatum);
			addValidationErrors(new VerbintenisCollectiefBlokkadedatumValidator(verbintenis,
				getBlokkadedatum(), mode).validate());
			verbintenis.setEinddatum(null);
		}
	}

	private void checkVerplichteVelden(Verbintenis verbintenis)
	{
		addValidationErrors(new VerbintenisCollectiefValidator(verbintenis, beginstatus, eindstatus)
			.validate());
	}

	private void createJobRunDetailMetErrors()
	{
		for (String error : validationErrors)
		{
			createJobRunDetail(error);
		}
	}

	private void createJobRunDetail(String error)
	{
		VerbintenissenStatusEditJobRunDetail detail = new VerbintenissenStatusEditJobRunDetail(run);
		detail.setUitkomst(error);
		detail.save();
	}

	private void checkBeeindigenMogelijk(Verbintenis verbintenis)
	{
		// In principe niet (meer) nodig, aangezien er geen verbintenissen selecteerbaar
		// zijn waarvoor dit geldt.
		if (einddatum != null && einddatum.before(verbintenis.getBegindatum()))
		{
			addValidationError("Fout bij statusovergang verbintenis "
				+ verbintenis.getOmschrijving() + ". Einddatum ligt voor begindatum");
		}
	}

	private void extractData(JobExecutionContext context)
	{
		beginstatus = getValue(context, "beginstatus");
		eindstatus = getValue(context, "eindstatus");

		einddatum = getValue(context, "einddatum");
		redenUitschrijving = getValue(context, "redenUitschrijving");
		toelichting = getValue(context, "toelichting");

		documentTemplate = getValue(context, "documentTemplate");

	}

	@SuppressWarnings("unused")
	private CollectieveStatusovergangJobDataMap<VerbintenisStatus> getDataMap()
	{
		return null;
	}

	private Date getBlokkadedatum()
	{
		if (verbintenisBlokkadedatum == null)
		{
			MutatieBlokkedatumSetting setting =
				DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
					MutatieBlokkedatumSetting.class);

			if (setting != null && setting.getValue() != null)
				verbintenisBlokkadedatum = setting.getValue().getBlokkadedatumVerbintenis();
		}
		return verbintenisBlokkadedatum;
	}

	private void addValidationError(String newValidationError)
	{
		validationErrors.add(newValidationError);
	}

	private void addValidationErrors(List<String> newValidationErrors)
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
		log.error("Verbintenisstatussen wijzigen mislukt");

		run.setSamenvatting("Verbintenisstatussen wijzigen mislukt, klik voor meer informatie");
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		setStatus("Verbintenisstatussen wijzigen mislukt");

		StringPrintWriter spw = new StringPrintWriter();
		e.printStackTrace(spw);
		run.error("Verbintenisstatussen wijzigen is mislukt door een onverwachte fout:\n"
			+ spw.getString());

		run.saveOrUpdate();
		run.commit();
	}

}