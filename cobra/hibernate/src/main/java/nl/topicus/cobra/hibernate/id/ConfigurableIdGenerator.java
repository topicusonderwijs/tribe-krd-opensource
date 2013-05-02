package nl.topicus.cobra.hibernate.id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.persistence.SequenceGenerator;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SequenceGenerator(name = "ConfigurableIdGenerator")
/**
 * Custom nummer generator die op meerdere database soorten werkt en instelbaar is.
 * Op het moment van schrijven worden 2 soorten ondersteunt: Oracle, SQLServer.
 * 
 * Middels de hibernate.properties parameter: hibernate.connection.type, 
 * wordt gekozen welke database gebruikt wordt, zodat het niet driver afhankelijk is.
 * 
 * @author Joost Limburg
 */
public class ConfigurableIdGenerator extends AbstractConfigurableId implements IdentifierGenerator,
		Configurable
{
	// Log
	private static final Logger log = LoggerFactory.getLogger(ConfigurableIdGenerator.class);

	// De naam van de IdGenerator, standaard HIBERNATE_SEQUENCE.
	private String sequenceName = "HIBERNATE_SEQUENCE";

	/**
	 * Constructor, bepaalt welk soort database gebruikt wordt. Standaard wordt Oracle
	 * gebruikt.
	 */
	public ConfigurableIdGenerator()
	{
		super();
	}

	/**
	 * Configureert deze IdGenerator. De volgende parameters zijn beschikbaar: -
	 * sequenceName, de value hiervan wordt gebruikt als de naam van de IdGenerator.
	 */
	public void configure(Type arg0, Properties props, Dialect arg2) throws MappingException
	{
		setSequenceName(props.getProperty("sequenceName"));
	}

	/**
	 * Genereer het volgende nummer, wordt aangeroepen via Annotations.
	 * 
	 * @param session
	 *            De hibernate sessie.
	 * @param object
	 *            Het object (niet van belang).
	 * @return een nieuw Id dat gegarandeert vrij is.
	 */
	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException
	{
		return this.generate((Session) session);
	}

	/**
	 * Genereer het volgende nummer, kan handmatig aangeroepen worden.
	 * 
	 * @param session
	 *            De hibernate sessie.
	 * @return een nieuw Id dat gegarandeert vrij is.
	 */
	@SuppressWarnings("deprecation")
	public Long generate(Session session)
	{
		Long id = null;
		try
		{
			// SQL Server nummer genereren
			if (getDatabaseType() == DatabaseType.SQLServer)
			{
				id = generateMSSQL(session.connection());
			}
			// Oracle nummer genereren
			else
			{
				id = generateOracle(session);
			}
		}
		catch (Exception ex)
		{
			log.warn("Could not generate next Id for sequence[" + sequenceName + "], databaseType["
				+ getDatabaseType() + "].");
		}
		return id;
	}

	/**
	 * Verkrijg de huidige waarde van deze nummergenerator.
	 * 
	 * @param session
	 *            De hibernate sessie.
	 * @return het laatst gegenereerde Id.
	 */
	public long getValue(Session session)
	{
		Long id = null;
		try
		{
			String statQuery = "";

			// SQL Server huidig nummer verkrijgen
			if (getDatabaseType() == DatabaseType.SQLServer)
			{
				statQuery = "select isnull(max(id),0) from \"$SSMA_seq_" + getSequenceName() + "\"";
			}
			// Oracle huidig nummer verkrijgen
			else
			{
				statQuery =
					"select last_number from user_sequences where sequence_name='"
						+ getSequenceName() + "'";
			}

			id = getIdByQuery(session, statQuery);
		}
		catch (Exception ex)
		{
			log.warn("Could not get latest Id for sequence[" + sequenceName + "], databaseType["
				+ getDatabaseType() + "].");
		}
		return id;

	}

	/**
	 * Verkrijg de maximale(eind) waarde van deze generator.
	 * 
	 * @param session
	 *            De hibernate sessie.
	 * @return de maximale id die de generator kan genereren.
	 */
	public long getMaxValue(Session session)
	{
		Long id = null;
		try
		{
			String statQuery = "";

			// SQL Server max Id size
			if (getDatabaseType() == DatabaseType.SQLServer)
			{
				statQuery = "select 999999999999999999 from dual";
			}
			// Oracle max Id size
			else
			{
				statQuery =
					"select max_value from user_sequences where sequence_name='"
						+ getSequenceName() + "'";
			}

			id = getIdByQuery(session, statQuery);
		}
		catch (Exception ex)
		{

		}
		return id;
	}

	/**
	 * Oracle specifieke manier van Id's genereren
	 */
	private Long generateOracle(Session session)
	{
		SQLQuery query =
			session.createSQLQuery("select " + getSequenceName() + ".nextval from dual");
		long nieuweWaarde = ((BigDecimal) query.uniqueResult()).longValue();

		return nieuweWaarde;
	}

	/**
	 * SQL Server specifieke manier van Id's genereren
	 */
	private Long generateMSSQL(Connection connection)
	{
		Long id = null;
		try
		{
			CallableStatement stat =
				connection.prepareCall("{call [dbo].[$SSMA_sp_get_nextval_" + getSequenceName()
					+ "](?)}");
			stat.registerOutParameter(1, java.sql.Types.BIGINT);
			stat.execute();
			id = stat.getLong(1);
			stat.close();
		}
		catch (SQLException e)
		{
			throw new HibernateException("Unable to get new identifier value", e);
		}
		return id;
	}

	/**
	 * Execute een query direct op de database, waarvan een Long als returntype wordt
	 * verwacht.
	 */
	private Long getIdByQuery(Session session, String query)
	{
		SQLQuery exQ = session.createSQLQuery(query);
		Object waarde = exQ.uniqueResult();
		return (waarde != null ? ((BigDecimal) exQ.uniqueResult()).longValue() : -1);
	}

	/**
	 * Naam van de generator.
	 */
	public void setSequenceName(String sequenceName)
	{
		this.sequenceName = sequenceName;
	}

	/**
	 * Naam van de generator.
	 */
	public String getSequenceName()
	{
		return sequenceName;
	}
}
