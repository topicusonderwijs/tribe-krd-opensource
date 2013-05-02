package nl.topicus.cobra.hibernate.id;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Joost Limburg
 */
public class ConfigurableIdFactory extends AbstractConfigurableId
{
	// Logging zodat we kunnen terugkijken als er iets mis gaat
	private static final Logger log = LoggerFactory.getLogger(ConfigurableIdFactory.class);

	/**
	 * Geeft een hibernate sessie.
	 */
	private Session session;

	/**
	 * Constructor. Bepaalt of de factory in Oracle of SQLServer modus moet staan. Wordt
	 * bepaalt door de parameter hibernate.connection.type op te halen uit de properties.
	 * 
	 * @param session
	 *            De hibernate sessie.
	 */
	public ConfigurableIdFactory(Session session)
	{
		super();
		this.session = session;
	}

	/**
	 * Bereidt de database voor om generators op te draaien.
	 */
	public void prepareDatabase() throws Exception
	{
		// Bereidt de SQLServer database voor:
		if (getDatabaseType() == DatabaseType.SQLServer)
		{
			// createSQLInstance
		}
		else if (getDatabaseType() == DatabaseType.Oracle)
		{
			// Niet benodigd.
		}

		// Creer de HIBERNATE_SEQUENCE:
		createNewGenerator("HIBERNATE_SEQUENCE", new Long(1), new Long(999999999999999999L),
			new Long(1));
	}

	/**
	 * Creert een nieuwe sequence in de database.
	 * 
	 * @param sequenceName
	 *            De naam van de sequence.
	 * @param startValue
	 *            De startwaarde van de sequence.
	 * @param maxValue
	 *            De maximale waarde van de sequence.
	 * @param increment
	 *            Het aantal waarmee de sequence verhoogt wordt per aanvraag.
	 * @throws Exception
	 *             Kan een exceptie gooien als er een fout optreedt bij het aanmaken van
	 *             de sequence.
	 */
	public void createNewGenerator(String sequenceName, Long startValue, Long maxValue,
			Long increment) throws Exception
	{
		// Test of de generator al bestaat.
		if (getGeneratorList().contains(sequenceName.toUpperCase()))
		{
			log.warn("De aan te maken sequence bestaat al: " + sequenceName);
		}
		else
		{
			if (getDatabaseType() == DatabaseType.SQLServer)
			{
				// createSQLGenerator
			}
			else if (getDatabaseType() == DatabaseType.Oracle)
			{
				createOracleGenerator(sequenceName, maxValue, startValue, increment);
			}
		}
	}

	/**
	 * Creert de generator voor Oracle
	 */
	private void createOracleGenerator(String name, Long max, Long start, Long increment)
			throws Exception
	{
		SQLQuery create =
			session.createSQLQuery("CREATE SEQUENCE " + name + " minvalue 1 maxvalue " + max
				+ " start with " + start + " increment by " + increment + " nocache");
		create.executeUpdate();
	}

	/**
	 * Geeft een lijst met idGenerators die beschikbaar zijn voor de user.
	 * 
	 * @return lijst met idGenerators.
	 */
	@SuppressWarnings("unchecked")
	public List<String> getGeneratorList() throws Exception
	{
		List<String> list = new ArrayList<String>();

		SQLQuery query = session.createSQLQuery("SELECT sequence_name FROM user_sequences ");
		list.addAll(query.list());

		return list;
	}
}
