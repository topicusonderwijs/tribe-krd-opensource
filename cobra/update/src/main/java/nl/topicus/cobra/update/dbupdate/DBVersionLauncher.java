package nl.topicus.cobra.update.dbupdate;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nl.topicus.cobra.update.ComponentScanner;
import nl.topicus.cobra.update.dbupdate.action.Action;
import nl.topicus.cobra.update.dbupdate.dbaccess.DBVersionHandle;
import nl.topicus.cobra.update.dbupdate.dbaccess.DBVersionMarkDao;
import nl.topicus.cobra.util.ResourceUtil;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Onderdeel van het nieuwe DBVersion tool
 * 
 * @author Chris Gunnink
 * @since 14 juli 2009
 */
public class DBVersionLauncher implements DBVersionCallback
{
	public enum Database
	{
		ONBEKEND("n/a", "n/a"),
		MYSQL("jdbc:mysql:", "com.mysql.jdbc.Driver"),
		ORACLE("jdbc:oracle:thin:@", "oracle.jdbc.driver.OracleDriver"),
		POSTGRESQL("jdbc:postgresql:", "org.postgresql.Driver"),
		SQLSERVER("jdbc:sqlserver:", "com.microsoft.sqlserver.jdbc.SQLServerDriver");

		private final String prefix;

		private final String driver;

		private Database(String prefix, String driver)
		{
			this.prefix = prefix;
			this.driver = driver;
		}

		public String getDriver()
		{
			return driver;
		}

		public String getPrefix()
		{
			return prefix;
		}

		public static Database from(String connectionUrl)
		{
			for (Database database : values())
			{
				if (database != ONBEKEND && connectionUrl.contains(database.name().toLowerCase()))
					return database;
			}
			return ONBEKEND;
		}
	}

	private class UpdateAction implements Action
	{
		@Override
		public void performAction(Option option)
		{
			String scanBasePackage =
				(getScanBasePackage() != null) ? getScanBasePackage() : DEFAULT_SCAN_BASE_PACKAGE;

			// FIXME misschien is "100.0" verwarrend als hoogste versie, zie
			// printHelp(); maar na succesvol updaten wordt de 'echte' versie
			// getoond
			String version = option.getValue("100.0");
			getManager().updateTo(version, scanBasePackage);
		}
	}

	public static final String DEFAULT_SCAN_BASE_PACKAGE = "nl.topicus";

	private DBVersionHandle dbHandle;

	private DBVersionMarkDao versionDAO;

	private Option help;

	private Option info;

	private Option update;

	private Options options = new Options();

	private Map<Option, Action> extraOptions = new LinkedHashMap<Option, Action>();

	public DBVersionLauncher()
	{
		options.addOption(help = new Option("h", "help", false, "toont dit overzicht."));
		options
			.addOption(info = new Option("i", "info", false, "toont de versie van de database."));

		update =
			new Option("u", "update", true, "werkt de database bij naar de gewenste versie, "
				+ "indien geen versie is opgegeven wordt de database bijgewerkt naar de "
				+ "laatste versie.");
		update.setOptionalArg(true);
		update.setArgName("versie");
		options.addOption(update);

		addOption(update, new UpdateAction());
	}

	/**
	 * Registers the given optional action, which will be executed when the given
	 * commandline option is specified.
	 */
	public void addOption(Option option, Action actionToPeform)
	{
		extraOptions.put(option, actionToPeform);
		options.addOption(option);
	}

	/**
	 * App specific code, such as starting Spring/Hibernate
	 * 
	 * You have to call setDbHandle() and setVersionDAO() to populate the corresponding
	 * fields
	 */
	protected void init()
	{
	}

	/**
	 * Return a app specific package to scan Task classes
	 * 
	 * @return String or null to use default DEFAULT_SCAN_BASE_PACKAGE
	 */
	protected String getScanBasePackage()
	{
		return null;
	}

	/**
	 * App specific code, such as convert scripts, adding/removing instances
	 * 
	 * @param taskInstances
	 *            != null
	 */
	@Override
	public void afterTaskList(List<Object> taskInstances)
	{
	}

	@Override
	public List<Object> getCustomTasks()
	{
		return null;
	}

	protected void printHelp()
	{
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(getClass().getName(), options);
	}

	protected final void setDbHandle(DBVersionHandle dbHandle)
	{
		this.dbHandle = dbHandle;
	}

	protected final void setVersionDAO(DBVersionMarkDao versionDAO)
	{
		this.versionDAO = versionDAO;
	}

	protected DBVersionHandle getDbHandle()
	{
		return dbHandle;
	}

	protected DBVersionMarkDao getVersionDAO()
	{
		return versionDAO;
	}

	protected DBVersionManager getManager()
	{
		if (dbHandle == null)
		{
			throw new IllegalStateException(
				"this.dbHandle cannot be null; use setDbHandle() to populate this field");
		}
		if (versionDAO == null)
		{
			throw new IllegalStateException(
				"this.versionDAO cannot be null; use setVersionDAO() to populate this field");
		}
		return new DBVersionManager(dbHandle, versionDAO, new ComponentScanner(), this);
	}

	protected final void launch(String[] args)
	{
		init();

		CommandLineParser parser = new GnuParser();
		try
		{
			CommandLine line = parser.parse(options, args);
			DBVersionManager manager = getManager();

			if (line.hasOption(help.getOpt()))
			{
				printHelp();
				return;
			}
			else if (line.hasOption(info.getOpt()))
			{
				System.out.println("Huidige versie: " + manager.getCurrentVersion());
				return;
			}

			for (Map.Entry<Option, Action> curOption : extraOptions.entrySet())
			{
				if (line.hasOption(curOption.getKey().getOpt()))
				{
					Option lineOption = null;
					for (Option curLineOption : line.getOptions())
					{
						if (curLineOption.equals(curOption.getKey()))
						{
							lineOption = curLineOption;
							break;
						}
					}
					curOption.getValue().performAction(lineOption);
				}
			}
		}
		catch (ParseException exp)
		{
			System.err.println("Ongeldige optie(s) opgegeven: " + exp.getMessage());
		}
	}

	public static void checkDatabaseConnectionBeforeC3p0KillsAKitten()
	{
		URL hibernateUrl = DBVersionLauncher.class.getResource("/hibernate.properties");
		InputStream is = DBVersionLauncher.class.getResourceAsStream("/hibernate.properties");
		Connection connection = null;

		Properties hibernate = new Properties();
		Properties system = System.getProperties();

		String username = "n/a";
		String password = "n/a";
		String databaseUrl = "n/a";
		String defaultschema = "n/a";
		String driver = "n/a";
		String dialect = "n/a";
		Database database = Database.ONBEKEND;

		try
		{
			System.out.print("Testen van de database configuratie");
			System.out.flush();

			System.out.print(".");
			System.out.flush();

			if (is != null)
			{
				hibernate.load(is);
			}

			username = hibernate.getProperty("hibernate.connection.username", username);
			password = hibernate.getProperty("hibernate.connection.password", password);
			databaseUrl = hibernate.getProperty("hibernate.connection.url", databaseUrl);
			defaultschema = hibernate.getProperty("hibernate.default_schema", defaultschema);
			driver = hibernate.getProperty("hibernate.connection.driver_class", driver);
			dialect = hibernate.getProperty("hibernate.dialect", dialect);

			username = system.getProperty("hibernate.connection.username", username);
			password = system.getProperty("hibernate.connection.password", password);
			databaseUrl = system.getProperty("hibernate.connection.url", databaseUrl);
			defaultschema = system.getProperty("hibernate.default_schema", defaultschema);
			driver = system.getProperty("hibernate.connection.driver_class", driver);
			dialect = system.getProperty("hibernate.dialect", dialect);

			if ("n/a".equals(username))
			{
				throw new IllegalStateException("Geen hibernate.connection.username opgegeven");
			}
			if ("n/a".equals(password))
			{
				throw new IllegalStateException("Geen hibernate.connection.password opgegeven");
			}
			if ("n/a".equals(databaseUrl))
			{
				throw new IllegalStateException("Geen hibernate.connection.url opgegeven");
			}

			if (!databaseUrl.startsWith("jdbc"))
			{
				throw new IllegalStateException(
					"Kan database niet bepalen aan de hand van de connection URL verwacht: 'jdbc:<database>:...'");
			}

			database = Database.from(databaseUrl);
			if (database == Database.ONBEKEND)
			{
				throw new IllegalStateException("Onbekende database opgegeven");
			}

			if (!"n/a".equals(dialect))
			{
				throw new IllegalStateException(
					"Het database dialect wordt automatisch door Hibernate bepaald en dient niet geconfigureerd te worden.");
			}
			if ("n/a".equals(driver))
			{
				System.setProperty("hibernate.connection.driver_class", database.getDriver());
			}
			else
			{
				if ("n/a".equals(driver))
				{
					throw new IllegalStateException(
						"Geen hibernate.connection.driver_class geconfigureerd");
				}
				if (!database.getDriver().equals(driver))
				{
					throw new IllegalStateException("hibernate.connection.driver_class '" + driver
						+ "' is niet correct voor de gedetecteerde database " + database);
				}
			}
			System.out.println(". [succes]");

			System.out.print("Testen van de database connectie");
			System.out.flush();

			Class.forName(database.getDriver());

			System.out.print(".");
			System.out.flush();

			connection = DriverManager.getConnection(databaseUrl, username, password);

			System.out.print(".");
			System.out.flush();

			connection.isValid(5);
			System.out.println(". [succes]");
			System.out.println();
		}
		catch (Exception e)
		{
			System.out.println(". [faal]");
			System.out.println();
			System.out.println("Database gaf foutmelding: " + e.getMessage());
			System.out.println();
			System.out.println("Configuratiebestand:      " + hibernateUrl);
			System.out.println();
			printProperty(hibernate, "hibernate.connection.username");
			printProperty(hibernate, "hibernate.connection.password");
			printProperty(hibernate, "hibernate.connection.url");
			printProperty(hibernate, "hibernate.connection.driver_class");
			printProperty(hibernate, "hibernate.default_schema");
			printProperty(hibernate, "hibernate.dialect");
			System.out.println();
			System.out.println("Commandline instellingen:");
			System.out.println();
			printProperty(system, "hibernate.connection.username");
			printProperty(system, "hibernate.connection.password");
			printProperty(system, "hibernate.connection.url");
			printProperty(system, "hibernate.connection.driver_class");
			printProperty(system, "hibernate.default_schema");
			printProperty(system, "hibernate.dialect");
			System.out.println();
			System.out.println("Gedetecteerde configuratie:");
			System.out.println();
			System.out.println("    Database:                 " + database.name().toLowerCase());
			System.out.println("    Database driver_class:    " + database.getDriver());

			ResourceUtil.closeQuietly(connection);
			ResourceUtil.closeQuietly(is);
			System.exit(1);
		}
		finally
		{
			ResourceUtil.closeQuietly(connection);
			ResourceUtil.closeQuietly(is);
		}
	}

	private static void printProperty(Properties properties, String key)
	{
		System.out.printf("    %-40s %s\n", key + ":", properties.getProperty(key, "n/a"));
	}
}
