package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bpv;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.io.StringPrintWriter;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.BPVInschrijvingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.settings.MutatieBlokkedatumSetting;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.jobs.rapportage.RapportageJobDataMap;
import nl.topicus.eduarte.jobs.rapportage.RapportageJobUtil;
import nl.topicus.eduarte.krd.bron.BronBpvWijzigingToegestaanCheck;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumValidatorMode;
import nl.topicus.eduarte.krd.web.validators.BronBPVValidator;
import nl.topicus.eduarte.providers.BPVInschrijvingProvider;

import org.apache.wicket.validation.IErrorMessageSource;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.Validatable;
import org.hibernate.FlushMode;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

/*
 * @author idserda
 */
@JobInfo(name = BPVInschrijvingCollectiefEditJob.JOB_NAME)
@JobRunClass(BPVInschrijvingCollectiefEditJobRun.class)
public class BPVInschrijvingCollectiefEditJob extends EduArteJob
{
	public static final String JOB_NAME = "Collectief status van BPV inschrijvingen wijzigen";

	private BPVInschrijvingCollectiefEditJobRun run;

	private List<String> validationErrors = new ArrayList<String>();

	private List<Long> succesvol = new ArrayList<Long>();

	private List<Long> mislukt = new ArrayList<Long>();

	private BPVStatus beginstatus;

	private BPVStatus eindstatus;

	private Date einddatum;

	private RedenUitschrijving redenUitschrijving;

	private DocumentTemplate documentTemplate;

	private Boolean statusovergangBPVInschrijvingToegestaan;

	// Toelichting bij beeindigen
	private String toelichting;

	private Date bpvInschrijvingBlokkadedatum;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException,
			InterruptedException
	{
		setFlushMode(FlushMode.COMMIT);
		try
		{
			run = new BPVInschrijvingCollectiefEditJobRun();
			run.setGestartDoor(getMedewerker());
			run.setRunStart(TimeUtil.getInstance().currentDateTime());

			extractData(context);

			run.setSamenvatting("BPV inschrijving status wijzigen van " + beginstatus.toString()
				+ " naar " + eindstatus.toString() + ". ");
			run.save();

			List<BPVInschrijving> inschrijvingen = getValue(context, "selectie");

			wijzigStatus(inschrijvingen);

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting(run.getSamenvatting() + " Totaal: " + inschrijvingen.size()
				+ ", succesvol:  " + succesvol.size() + ", mislukt: " + mislukt.size());
			run.update();
			run.commit();
			flushAndClearHibernate();

			if (BPVStatus.OvereenkomstAfgedrukt.equals(eindstatus))
			{
				if (succesvol.size() > 0)
				{
					List<BPVInschrijving> afdrukken =
						DataAccessRegistry.getHelper(BPVInschrijvingDataAccessHelper.class).list(
							BPVInschrijving.class, succesvol);
					drukOvereenkomstAf(afdrukken);
					createJobRunDetail("Rapportagetaak gestart voor het genereren van praktijkovereenkomsten.");
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

	private void wijzigStatus(List<BPVInschrijving> inschrijvingen) throws InterruptedException
	{
		int count = 0;
		for (BPVInschrijving inschrijving : inschrijvingen)
		{
			if (statusovergangMogelijk(inschrijving))
			{
				updateBPVInschrijving(inschrijving);
				succesvol.add(inschrijving.getId());
				createJobRunDetail(createDeelnemerOmschrijving(inschrijving)
					+ " Statusovergang geslaagd voor " + inschrijving.toString());
			}
			else
			{
				mislukt.add(inschrijving.getId());
			}
			setProgress(count, inschrijvingen.size());
			count++;

			flushHibernate();
		}
	}

	private void updateBPVInschrijving(BPVInschrijving inschrijving)
	{
		inschrijving.setStatus(eindstatus);

		if (BPVStatus.Beëindigd.equals(eindstatus))
			beeindigBPV(inschrijving);

		inschrijving.saveOrUpdate();
	}

	private void drukOvereenkomstAf(List<BPVInschrijving> inschrijvingen) throws SchedulerException
	{
		RapportageJobDataMap<BPVInschrijving, BPVInschrijving> datamap =
			new RapportageJobDataMap<BPVInschrijving, BPVInschrijving>();

		datamap.setDahClass(BPVInschrijvingDataAccessHelper.class);
		datamap.setSelection(inschrijvingen);
		datamap.setDocumentTemplate(documentTemplate);
		datamap.setEindType(documentTemplate.getType());

		RapportageJobUtil.startRapportageJob(documentTemplate, datamap);
	}

	private void beeindigBPV(BPVInschrijving inschrijving)
	{
		inschrijving.setRedenUitschrijving(redenUitschrijving);
		inschrijving.setEinddatum(einddatum);
		inschrijving.setToelichtingBeeindiging(toelichting);
	}

	private boolean statusovergangMogelijk(BPVInschrijving inschrijving)
	{
		validationErrors.clear();

		checkVerplichteVelden(inschrijving);
		checkToegestaanBron(inschrijving);
		checkBPVBlokkadedatumValidator(inschrijving);
		checkBeeindigenMogelijk(inschrijving);

		if (!validationErrors.isEmpty())
		{
			createJobRunDetailMetErrors();
			return false;
		}
		else
			return true;
	}

	private void checkToegestaanBron(BPVInschrijving inschrijving)
	{
		inschrijving.setStatus(eindstatus);

		checkVerbintenisCommuniceerbaar(inschrijving);
		checkMutatieToegestaanBron(inschrijving);
		checkBPVBronValidator(inschrijving);

		inschrijving.setStatus(beginstatus);
	}

	private void checkVerbintenisCommuniceerbaar(BPVInschrijving inschrijving)
	{
		if (!inschrijving.getVerbintenis().isBronCommuniceerbaar())
			addValidationError(createDeelnemerOmschrijving(inschrijving)
				+ "Fout bij statusovergang BPV inschrijving " + inschrijving.toString()
				+ ". Verbintenis is niet BRON communiceerbaar.");
	}

	private void checkBPVBronValidator(final BPVInschrijving inschrijving)
	{
		BronBPVValidator<Void> validator = new BronBPVValidator<Void>(new BPVInschrijvingProvider()
		{
			@Override
			public BPVInschrijving getBPV()
			{
				return null;
			}

		});

		validator.onValidate(new Validatable<Void>()
		{
			@Override
			public void error(IValidationError error)
			{
				addValidationError(error.getErrorMessage(new IErrorMessageSource()
				{
					@Override
					public String getMessage(String key)
					{
						// TODO: Hier via locale de 'key' als foutmelding gebruiken.

						String msg = createDeelnemerOmschrijving(inschrijving);

						msg =
							msg.concat("Fout bij statusovergang BPV inschrijving "
								+ inschrijving.toString() + " (" + key + ")");

						return msg;
					}

					@Override
					public String substitute(String string, Map<String, Object> vars)
							throws IllegalStateException
					{
						return string;
					}
				}));

			}
		}, inschrijving);
	}

	private void checkBPVBlokkadedatumValidator(BPVInschrijving inschrijving)
	{
		if (getBlokkadedatum() != null)
		{
			BlokkadedatumValidatorMode mode = null;

			if (eindstatus.equals(BPVStatus.Beëindigd))
				mode = BlokkadedatumValidatorMode.Beeindigen;
			else
				mode = BlokkadedatumValidatorMode.Bewerken;

			inschrijving.setEinddatum(einddatum);
			addValidationErrors(new BPVInschrijvingCollectiefBlokkadedatumValidator(inschrijving,
				getBlokkadedatum(), mode).validate());
			inschrijving.setEinddatum(null);
		}
	}

	private void checkMutatieToegestaanBron(BPVInschrijving inschrijving)
	{
		Long bedrijfsgegevenId = null;
		BPVBedrijfsgegeven bedrijfsgegeven = inschrijving.getBedrijfsgegeven();
		if (bedrijfsgegeven != null)
			bedrijfsgegevenId = bedrijfsgegeven.getId();

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(inschrijving.getBegindatum(), inschrijving
				.getAfsluitdatum(), bedrijfsgegevenId, beginstatus, inschrijving, inschrijving
				.getVerbintenis())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean getSecurityCheckWijzigingNaMutatiebeperking()
				{
					return statusovergangBPVInschrijvingToegestaan;
				}
			};
		if (!check.isWijzigingToegestaan())
			addValidationError("Overgang niet toegestaan voor BRON.");
	}

	private void checkVerplichteVelden(BPVInschrijving inschrijving)
	{
		addValidationErrors(new BPVInschrijvingCollectiefValidator(inschrijving, eindstatus)
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
		BPVInschrijvingCollectiefEditJobRunDetail detail =
			new BPVInschrijvingCollectiefEditJobRunDetail(run);
		detail.setUitkomst(error);
		detail.save();
	}

	private void checkBeeindigenMogelijk(BPVInschrijving inschrijving)
	{
		// In principe niet (meer) nodig, aangezien er geen inschrijvingen selecteerbaar
		// zijn waarvoor dit geldt.
		if (einddatum != null && einddatum.before(inschrijving.getBegindatum()))
		{
			addValidationError("Fout bij statusovergang BPV " + inschrijving.toString()
				+ ". Einddatum ligt voor begindatum");
		}
	}

	private Date getBlokkadedatum()
	{
		if (bpvInschrijvingBlokkadedatum == null)
		{
			MutatieBlokkedatumSetting setting =
				DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
					MutatieBlokkedatumSetting.class);

			if (setting != null && setting.getValue() != null)
				bpvInschrijvingBlokkadedatum = setting.getValue().getBlokkadedatumBPV();
		}
		return bpvInschrijvingBlokkadedatum;
	}

	private void extractData(JobExecutionContext context)
	{
		beginstatus = getValue(context, "beginstatus");
		eindstatus = getValue(context, "eindstatus");

		einddatum = getValue(context, "einddatum");
		redenUitschrijving = getValue(context, "redenUitschrijving");
		toelichting = getValue(context, "toelichting");

		documentTemplate = getValue(context, "documentTemplate");

		statusovergangBPVInschrijvingToegestaan =
			getValue(context, "statusovergangBPVInschrijvingToegestaan");
	}

	private void addValidationError(String newValidationError)
	{
		validationErrors.add(newValidationError);
	}

	private void addValidationErrors(List<String> newValidationErrors)
	{
		validationErrors.addAll(newValidationErrors);
	}

	private String createDeelnemerOmschrijving(BPVInschrijving inschrijving)
	{
		Deelnemer deelnemer = inschrijving.getDeelnemer();

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
		log.error("BPV inschrijving status wijzigen mislukt");

		run.setSamenvatting("BPV inschrijving status wijzigen mislukt, klik voor meer informatie");
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		setStatus("BPV inschrijving status wijzigen mislukt");

		StringPrintWriter spw = new StringPrintWriter();
		e.printStackTrace(spw);
		run.error("BPV inschrijving status wijzigen is mislukt door een onverwachte fout:\n"
			+ spw.getString());

		run.saveOrUpdate();
		run.commit();
	}

}