package nl.topicus.cobra.hibernate.id;

import org.hibernate.cfg.Configuration;

public class AbstractConfigurableId
{
	// We ondersteunen de volgende soorten database.
	public enum DatabaseType
	{
		SQLServer,
		Oracle;
	}

	// Het soort database, standaard Oracle.
	private DatabaseType databaseType = DatabaseType.Oracle;

	/**
	 * 
	 * Constructor. Bepaalt in welke modus de class moet staan. Wordt bepaalt door de
	 * parameter hibernate.connection.type op te halen uit de properties.
	 */
	public AbstractConfigurableId()
	{
		String sType = new Configuration().getProperty("hibernate.connection.type");

		if (sType != null && sType.equals("SQLServer"))
		{
			setDatabaseType(DatabaseType.SQLServer);
		}
		else
		{
			setDatabaseType(DatabaseType.Oracle);
		}
	}

	/**
	 * Zet de database type.
	 * 
	 * @param databaseType
	 *            De type database.
	 */
	public void setDatabaseType(DatabaseType databaseType)
	{
		this.databaseType = databaseType;
	}

	/**
	 * Get het database type.
	 * 
	 * @return De database type.
	 */
	public DatabaseType getDatabaseType()
	{
		return databaseType;
	}
}
