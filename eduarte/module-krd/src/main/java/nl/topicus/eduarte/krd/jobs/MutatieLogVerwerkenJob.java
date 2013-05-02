package nl.topicus.eduarte.krd.jobs;

import static java.util.Arrays.*;
import static nl.topicus.cobra.util.StringUtil.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.io.StringPrintWriter;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.ExceptionUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteConfig;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.dao.helpers.MutatieLogVerwerkersLogDataAccessHelper;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkenJobRun;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerker;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerker.UpdateSoort;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkersLog;
import nl.topicus.eduarte.krd.zoekfilters.MutatieLogVerwerkersLogZoekFilter;

import org.apache.wicket.util.time.Duration;
import org.hibernate.Hibernate;
import org.hibernate.LockOptions;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Session.LockRequest;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.SQLServerDialect;
import org.quartz.JobExecutionContext;

@JobInfo(name = MutatieLogVerwerkenJob.JOB_NAME)
@JobRunClass(MutatieLogVerwerkenJobRun.class)
public class MutatieLogVerwerkenJob extends EduArteJob
{
	private static final int BATCHSIZE = 100;

	public static final String JOB_NAME = "Mutatielogverwerken";

	private static final ConcurrentHashMap<Long, AtomicBoolean> barriers =
		new ConcurrentHashMap<Long, AtomicBoolean>();

	private HibernateSessionProvider sessionProvider = null;

	private boolean errorOpgetreden = false;

	private JobRun jobrun = null;

	@Override
	protected void executeJob(JobExecutionContext context) throws InterruptedException
	{
		Organisatie organisatie = getOrganisatie();
		if (organisatie != null)
		{
			verwerkMutatieLogAlsGeenAndereVerwerkerVoorOrganisatieDraait(organisatie);
		}
		else
		{
			log.error("Er is een mutatielogverwerker actief zonder een gekoppelde organisatie");
		}
	}

	private void verwerkMutatieLogAlsGeenAndereVerwerkerVoorOrganisatieDraait(
			Organisatie organisatie) throws InterruptedException
	{
		// er mag slechts één job per organisatie draaien. Er mogen wel jobs voor
		// verschillende organisaties naast elkaar draaien. Onderstaande barrier voorkomt
		// dat er voor één specifieke organisatie meerere jobs naast elkaar draaien.

		AtomicBoolean mutatielogVerwerkerDraaitVoorOrganisatie =
			barriers.putIfAbsent(organisatie.getId(), new AtomicBoolean(false));

		// omdat putIfAbsent null teruggeeft wanneer de waarde nog niet bekend was, nog
		// een keer opvragen om zo geen NPEs te krijgen

		if (mutatielogVerwerkerDraaitVoorOrganisatie == null)
			mutatielogVerwerkerDraaitVoorOrganisatie = barriers.get(organisatie.getId());

		// probeer de barriere op te trekken, lukt dit niet, dan is er al een andere job
		// aan het lopen voor deze organisatie

		if (!mutatielogVerwerkerDraaitVoorOrganisatie.compareAndSet(false, true))
		{
			log.info("Er draait al een mutatielog verwerker voor {}", organisatie.getNaam());
			return;
		}

		try
		{
			verwerkMutatieLogsVoorOrganisatie(organisatie);
		}
		finally
		{
			mutatielogVerwerkerDraaitVoorOrganisatie.set(false);
		}
	}

	private void verwerkMutatieLogsVoorOrganisatie(Organisatie organisatie)
			throws InterruptedException
	{
		jobrun = new MutatieLogVerwerkenJobRun();
		try
		{
			log.info("Verwerkt mutatielog van {}", organisatie.getNaam());

			verwerkMutatieLog(organisatie);
		}
		catch (InterruptedException e)
		{
			organisatie.rollback();

			String samenvatting = "Verwerken van mutatielog is onderbroken: " + e.getMessage();
			error(samenvatting, e);
			throw e;
		}
		catch (Exception e)
		{
			String samenvatting = "Verwerken van mutatielog is gelukt";
			samenvatting =
				"Verwerken van mutatielog is foutgegaan ivm " + ExceptionUtil.getDescription(e);
			error(samenvatting, e);
			organisatie.rollback();
		}
		finally
		{
			maakSamenvattingVanJobRun();
		}
	}

	private void verwerkMutatieLog(Organisatie organisatie) throws InterruptedException
	{
		List<MutatieLogVerwerkersLog> verwerkersLogs = getActieveVerwerkers(organisatie);
		for (int i = 0; i < verwerkersLogs.size(); i++)
		{
			setProgress(i, verwerkersLogs.size());
			MutatieLogVerwerkersLog verwerkersLog = verwerkersLogs.get(i);

			// log opnieuw ophalen zodat we later geen lazyinitshitexceptions krijgen
			verwerkersLog =
				(MutatieLogVerwerkersLog) getSession().get(MutatieLogVerwerkersLog.class,
					verwerkersLog.getId());

			Class< ? > verwerkerClass = Hibernate.getClass(verwerkersLog.getVerwerker());
			String verwerkerClassName = convertCamelCase(verwerkerClass.getSimpleName());
			String tabellen = verwerkersLog.getTabellen().toLowerCase();

			// verwerker unproxy-en zodat we later geen lazyinitshitexceptions krijgen
			verwerkersLog.getVerwerker().doUnproxy();

			if (!isRemoteServiceConfiguratieIngevuldVoorDezeOmgeving(verwerkersLog))
				continue;

			if (!isModuleVanVerwerkerAfgenomen(verwerkersLog))
				continue;

			Timestamp vorigeVerwerking = verwerkersLog.getLaatstVerwerktOp();
			verwerkMutatieLog(verwerkersLog);

			info(String
				.format(
					"%s heeft alle wijzigingen verwerkt tussen %s en %s met als laatst verwerkte id %d voor tabellen %s",
					verwerkerClassName, vorigeVerwerking, verwerkersLog.getLaatstVerwerktOp(),
					verwerkersLog.getLaatstVerwerktId(), tabellen));

			verwerkersLog.commit();
			getSession().close();
		}
	}

	private List<MutatieLogVerwerkersLog> getActieveVerwerkers(Organisatie organisatie)
	{
		MutatieLogVerwerkersLogDataAccessHelper helper =
			DataAccessRegistry.getHelper(MutatieLogVerwerkersLogDataAccessHelper.class);
		MutatieLogVerwerkersLogZoekFilter filter = new MutatieLogVerwerkersLogZoekFilter();

		filter.setOrderByList(Arrays.asList("organisatie", "id"));
		filter.setOrganisatie(organisatie);
		filter.setActief(true);

		return helper.list(filter);
	}

	private boolean isRemoteServiceConfiguratieIngevuldVoorDezeOmgeving(
			MutatieLogVerwerkersLog verwerkersLog)
	{
		Class< ? > verwerkerClass = Hibernate.getClass(verwerkersLog.getVerwerker());
		String verwerkerClassName = convertCamelCase(verwerkerClass.getSimpleName());

		boolean isGeconfigureerd = true;
		EduArteConfig configuration = EduArteApp.get().getConfiguration();
		if (configuration == EduArteConfig.Productie && !verwerkersLog.isProductieGeconfigureerd())
		{
			error(verwerkerClassName
				+ " kan gewijzigde entiteiten niet verzenden omdat de productie instellingen niet ingevuld zijn.");
			isGeconfigureerd = false;
		}
		else if (configuration != EduArteConfig.Productie && !verwerkersLog.isTestGeconfigureerd())
		{
			error(verwerkerClassName
				+ " kan gewijzigde entiteiten niet verzenden omdat de test instellingen niet ingevuld zijn.");
			isGeconfigureerd = false;
		}
		return isGeconfigureerd;
	}

	private boolean isModuleVanVerwerkerAfgenomen(MutatieLogVerwerkersLog verwerkersLog)
	{
		MutatieLogVerwerker verwerker = verwerkersLog.getVerwerker();

		@SuppressWarnings("unchecked")
		Class<MutatieLogVerwerker> clz = Hibernate.getClass(verwerker);
		Module module = clz.getAnnotation(Module.class);
		EduArteApp eduarte = EduArteApp.get();
		boolean moduleAfgenomen =
			eduarte.isModuleActive(verwerkersLog.getOrganisatie(), module.value());
		if (!moduleAfgenomen)
		{
			List<EduArteModuleKey> modules = asList(module.value());
			if (modules.size() == 1)
			{
				error("De module " + modules.get(0) + " voor "
					+ convertCamelCase(clz.getSimpleName())
					+ " is niet afgenomen, geen berichten verzonden");
			}
			else
			{
				error("De modules " + StringUtil.toString(modules, ", ", "<geen>") + " voor "
					+ convertCamelCase(clz.getSimpleName())
					+ " zijn niet afgenomen, geen berichten verzonden");
			}
		}
		return moduleAfgenomen;
	}

	private Session getSession()
	{
		SessionDataAccessHelper helper =
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class);
		HibernateSessionProvider provider = helper.getHibernateSessionProvider();
		return provider.getSession();
	}

	public void error(String message)
	{
		errorOpgetreden = true;
		log.warn(message);
		setStatus(message);
		jobrun.error(message);
	}

	public void warn(String message)
	{
		log.info(message);
		setStatus(message);
		jobrun.info(message);
	}

	public void error(String message, Throwable t)
	{
		errorOpgetreden = true;
		// voorkom NPEs in de foutafhandelingcode
		if (t == null)
		{
			error(message);
			return;
		}
		log.error(message, t);
		setStatus(message);
		jobrun.error(message);

		StringPrintWriter spw = new StringPrintWriter();
		ExceptionUtil.printStackTrace(spw, t);
		jobrun.error(spw.getString());
	}

	public void info(String message)
	{
		log.info(message);
		setStatus(message);
		jobrun.info(message);
	}

	private void verwerkMutatieLog(MutatieLogVerwerkersLog verwerkersLog)
	{
		PluginCallbackHandler handler = new PluginCallbackHandler(verwerkersLog);

		Organisatie organisatie = verwerkersLog.getOrganisatie();
		MutatieLogVerwerker verwerker = verwerkersLog.getVerwerker();

		long maxRowId = bepaalHoogsteMutatielogId();
		List<GewijzigdRecord> records =
			getMutatieRecords(organisatie, verwerker, maxRowId, BATCHSIZE);

		while (!records.isEmpty())
		{
			for (GewijzigdRecord record : records)
			{
				handler.setGewijzigdRecord(record);

				try
				{
					EduArteContext.get().runInContext(handler, organisatie);
					updateVerwerkingstatus(verwerkersLog, record.getRowId());
				}
				catch (RuntimeException e)
				{
					log.error(
						"Exceptie opgetreden tijdens het verwerken van de mutatielog voor verwerker "
							+ Hibernate.getClass(verwerker).getSimpleName(), e);
				}
			}
			// ruim de hibernate sessie volledig op omdat je anders uit geheugen loopt.
			// Heeft nare gevolgen voor proxies en dergelijke, maar die zijn wel op te
			// lossen door de entiteiten opnieuw uit de database te halen of opnieuw te
			// attachen aan de sessie.
			getSession().close();
			records = getMutatieRecords(organisatie, verwerker, maxRowId, BATCHSIZE);
		}

		updateVerwerkingstatus(verwerkersLog, maxRowId);
		getSession().close();
	}

	private List<GewijzigdRecord> getMutatieRecords(Organisatie organisatie,
			MutatieLogVerwerker verwerker, long maxMutatielogId, int batchsize)
	{
		SqlQuery mutatieLogQuery =
			createMutatieLogQuery(organisatie, verwerker, maxMutatielogId, batchsize);
		return getMutatieRecords(mutatieLogQuery);
	}

	private long bepaalHoogsteMutatielogId()
	{
		SQLQuery query = getSession().createSQLQuery("select max(id) from mutatielog");
		return ((Number) query.uniqueResult()).longValue();
	}

	private SqlQuery createMutatieLogQuery(Organisatie organisatie, MutatieLogVerwerker verwerker,
			long maxMutatielogId, int batchsize)
	{
		StringBuilder SQL = new StringBuilder();
		String sql = "";

		SQL.append("SELECT ML.ID         AS ID, \n");
		SQL.append("  ML.MODIFIED_ACTION AS MODIFIED_ACTION, \n");
		SQL.append("  ML.MODIFIED_TABLE  AS MODIFIED_TABLE, \n");
		SQL.append("  ML.MODIFIED_ID     AS MODIFIED_ID \n");
		SQL.append("FROM MUTATIELOG ML, \n");
		SQL.append("  MUTATIELOGVERWERKERSLOG MLVL \n");
		SQL.append("WHERE ML.ORGANISATIE = ? \n");
		SQL.append("AND MLVL.ORGANISATIE = ML.ORGANISATIE \n");
		SQL.append("AND MLVL.VERWERKER   = ? \n");
		SQL.append("AND ML.ID            > MLVL.LAATST_VERWERKT_ID \n");
		SQL.append("AND ML.ID            <= ? \n");
		SQL.append("AND MODIFIED_TABLE  IN \n");
		SQL.append("  (SELECT TABLE_NAME \n");
		SQL.append("  FROM MUTATIELOGVERWERKERSTABELLEN \n");
		SQL.append("  WHERE VERWERKER = MLVL.VERWERKER \n");
		SQL.append("  ) \n");
		SQL.append("ORDER BY ID");

		// SQLSERVER SPECIFIEKE AANPASSING:
		if (getDialect() instanceof SQLServerDialect)
		{
			sql = SQL.toString().replace("SELECT ML.ID", "SELECT TOP " + batchsize + " ML.ID");
		}
		// Overige databases (ORACLE):
		else
		{
			sql = "SELECT * FROM (" + SQL.toString() + ") where rownum <= " + BATCHSIZE;
		}

		LinkedHashSet<Object> parameters = new LinkedHashSet<Object>();
		parameters.add(organisatie.getId());
		parameters.add(verwerker.getId());
		parameters.add(maxMutatielogId);

		SqlQuery query = new SqlQuery(sql, parameters);
		return query;
	}

	protected HibernateSessionProvider getSessionProvider()
	{
		if (sessionProvider == null)
			sessionProvider =
				DataAccessRegistry.getHelper(SessionDataAccessHelper.class)
					.getHibernateSessionProvider();
		return sessionProvider;
	}

	protected Dialect getDialect()
	{
		return getSessionProvider().getDialect();
	}

	private List<GewijzigdRecord> getMutatieRecords(final SqlQuery query)
	{
		SQLQuery sqlQuery = getSession().createSQLQuery(query.getQuery());

		int parameterIndex = 0;
		for (Object parameter : query.getParameters())
		{
			if (parameter instanceof Long)
			{
				sqlQuery.setLong(parameterIndex, (Long) parameter);
			}
			else if (parameter instanceof String)
			{
				sqlQuery.setString(parameterIndex, (String) parameter);
			}
			else
			{
				sqlQuery.setParameter(parameterIndex, parameter);
			}
			parameterIndex++;
		}

		sqlQuery.setTimeout((int) Duration.minutes(15).seconds());

		List<GewijzigdRecord> resultaat = new ArrayList<GewijzigdRecord>();
		@SuppressWarnings("unchecked")
		List<Object[]> records = sqlQuery.list();
		for (Object[] columns : records)
		{
			GewijzigdRecord record =
				new GewijzigdRecord(this, ((Number) columns[0]).longValue(),
					UpdateSoort.from(((Character) columns[1]).toString()), (String) columns[2],
					((Number) columns[3]).longValue());
			resultaat.add(record);
		}

		return resultaat;
	}

	private void updateVerwerkingstatus(MutatieLogVerwerkersLog verwerkersLog, long maxId)
	{
		LockRequest lockRequest = getSession().buildLockRequest(LockOptions.NONE);
		lockRequest.lock(verwerkersLog);

		verwerkersLog.setLaatstVerwerktId(maxId);
		verwerkersLog.setLaatstVerwerktOp(new Timestamp(new Date().getTime()));

		verwerkersLog.saveOrUpdate();
		verwerkersLog.commit();
	}

	private void maakSamenvattingVanJobRun()
	{
		if (jobrun == null)
		{
			jobrun = new MutatieLogVerwerkenJobRun();
		}
		String samenvatting = "";
		if (errorOpgetreden)
		{
			samenvatting =
				"Er zijn fouten opgetreden tijdens de verwerking van de job, klik voor meer informatie";
		}
		else
		{
			samenvatting =
				"De verwerking van de mutatielog is succesvol verlopen (klik voor meer informatie)";
		}

		jobrun.setGestartDoor(getMedewerker());
		jobrun.setRunStart(getDatumTijdOpgestart());
		jobrun.setSamenvatting(samenvatting);
		jobrun.setRunEinde(new Date());
		for (JobRunDetail detail : jobrun.getDetails())
		{
			detail.saveOrUpdate();
		}
		jobrun.saveOrUpdate();
		jobrun.commit();
	}

	/**
	 * Bridge tussen het ophalen van de gewijzigde gegevens en het verwerken van die
	 * gegevens door plugins. Deze bridge zorgt ervoor dat de organisatie op de context
	 * gezet wordt zodat de plugins gewoon gebruik kunnen maken van normale EduArte
	 * entiteiten en andere handige zaken zoals DataAccessHelpers.
	 */
	private static class PluginCallbackHandler implements Runnable
	{
		private final MutatieLogVerwerkersLog plugin;

		private GewijzigdRecord record;

		public PluginCallbackHandler(MutatieLogVerwerkersLog plugin)
		{
			this.plugin = plugin;
		}

		public void setGewijzigdRecord(GewijzigdRecord record)
		{
			this.record = record;
		}

		@Override
		public void run()
		{
			MutatieLogVerwerker verwerker = plugin.getVerwerker();
			verwerker.verwerk(record, plugin);
		}
	}

	public static class SqlQuery
	{
		private String query;

		private LinkedHashSet<Object> parameters;

		public SqlQuery(String query, LinkedHashSet<Object> parameters)
		{
			this.query = query;
			this.parameters = parameters;
		}

		public String getQuery()
		{
			return query;
		}

		public LinkedHashSet<Object> getParameters()
		{
			return parameters;
		}
	}
}
