package nl.topicus.eduarte.dao.hibernate;

import java.io.Serializable;
import java.util.Properties;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.enhanced.DatabaseStructure;
import org.hibernate.id.enhanced.Optimizer;
import org.hibernate.id.enhanced.OptimizerFactory;
import org.hibernate.id.enhanced.SequenceStructure;
import org.hibernate.id.enhanced.TableStructure;
import org.hibernate.mapping.Table;
import org.hibernate.type.Type;
import org.hibernate.util.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Overgenomen vanuit de hibernate SequenceStyleGenerator, extends alleen van de
 * HibernateDataAccessHelper zodat we het niet via annotations hoeven aan te roepen.
 * 
 * @author Joost Limburg
 */
public class EduarteGeneratorHibernateAccessHelper<T> extends HibernateDataAccessHelper<T>
		implements PersistentIdentifierGenerator, Configurable
{
	public EduarteGeneratorHibernateAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	protected static final Logger log =
		LoggerFactory.getLogger(EduarteGeneratorHibernateAccessHelper.class);

	// general purpose parameters ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static final String SEQUENCE_PARAM = "sequence_name";

	public static final String DEF_SEQUENCE_NAME = "hibernate_sequence";

	public static final String INITIAL_PARAM = "initial_value";

	public static final int DEFAULT_INITIAL_VALUE = 1;

	public static final String INCREMENT_PARAM = "increment_size";

	public static final int DEFAULT_INCREMENT_SIZE = 1;

	public static final String OPT_PARAM = "optimizer";

	public static final String FORCE_TBL_PARAM = "force_table_use";

	// table-specific parameters ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static final String VALUE_COLUMN_PARAM = "value_column";

	public static final String DEF_VALUE_COLUMN = "next_val";

	// state ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private DatabaseStructure databaseStructure;

	private Optimizer optimizer;

	private Type identifierType;

	/**
	 * Getter for property 'databaseStructure'.
	 * 
	 * @return Value for property 'databaseStructure'.
	 */
	public DatabaseStructure getDatabaseStructure()
	{
		return databaseStructure;
	}

	/**
	 * Getter for property 'optimizer'.
	 * 
	 * @return Value for property 'optimizer'.
	 */
	public Optimizer getOptimizer()
	{
		return optimizer;
	}

	/**
	 * Getter for property 'identifierType'.
	 * 
	 * @return Value for property 'identifierType'.
	 */
	public Type getIdentifierType()
	{
		return identifierType;
	}

	// Configurable implementation ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void configure(Type type, Properties params, Dialect dialect) throws MappingException
	{
		this.identifierType = type;
		boolean forceTableUse = PropertiesHelper.getBoolean(FORCE_TBL_PARAM, params, false);

		final String sequenceName = determineSequenceName(params);

		final int initialValue = determineInitialValue(params);
		int incrementSize = determineIncrementSize(params);

		final String optimizationStrategy = determineOptimizationStrategy(params, incrementSize);
		incrementSize = determineAdjustedIncrementSize(optimizationStrategy, incrementSize);

		if (dialect.supportsSequences() && !forceTableUse)
		{
			if (OptimizerFactory.POOL.equals(optimizationStrategy)
				&& !dialect.supportsPooledSequences())
			{
				forceTableUse = true;
				log
					.info("Forcing table use for sequence-style generator due to pooled optimizer selection where db does not support pooled sequences");
			}
		}

		this.databaseStructure =
			buildDatabaseStructure(params, dialect, forceTableUse, sequenceName, initialValue,
				incrementSize);

		this.optimizer =
			OptimizerFactory.buildOptimizer(optimizationStrategy,
				identifierType.getReturnedClass(), incrementSize, -1);
		this.databaseStructure.prepare(optimizer);
	}

	/**
	 * Determine the name of the sequence (or table if this resolves to a physical table)
	 * to use.
	 * <p/>
	 * Called during {@link #configure configuration}.
	 * 
	 * @param params
	 *            The params supplied in the generator config (plus some standard useful
	 *            extras).
	 * @return The sequence name
	 */
	protected String determineSequenceName(Properties params)
	{
		String sequenceName = PropertiesHelper.getString(SEQUENCE_PARAM, params, DEF_SEQUENCE_NAME);
		if (sequenceName.indexOf('.') < 0)
		{
			String schemaName = params.getProperty(SCHEMA);
			String catalogName = params.getProperty(CATALOG);
			sequenceName = Table.qualify(catalogName, schemaName, sequenceName);
		}
		return sequenceName;
	}

	/**
	 * Determine the name of the column used to store the generator value in the db.
	 * <p/>
	 * Called during {@link #configure configuration} <b>when resolving to a physical
	 * table</b>.
	 * 
	 * @param params
	 *            The params supplied in the generator config (plus some standard useful
	 *            extras).
	 * @return The value column name
	 */
	protected String determineValueColumnName(Properties params)
	{
		return PropertiesHelper.getString(VALUE_COLUMN_PARAM, params, DEF_VALUE_COLUMN);
	}

	/**
	 * Determine the initial sequence value to use. This value is used when initializing
	 * the {@link #getDatabaseStructure() database structure} (i.e. sequence/table).
	 * <p/>
	 * Called during {@link #configure configuration}.
	 * 
	 * @param params
	 *            The params supplied in the generator config (plus some standard useful
	 *            extras).
	 * @return The initial value
	 */
	protected int determineInitialValue(Properties params)
	{
		return PropertiesHelper.getInt(INITIAL_PARAM, params, DEFAULT_INITIAL_VALUE);
	}

	/**
	 * Determine the increment size to be applied. The exact implications of this value
	 * depends on the {@link #getOptimizer() optimizer} being used.
	 * <p/>
	 * Called during {@link #configure configuration}.
	 * 
	 * @param params
	 *            The params supplied in the generator config (plus some standard useful
	 *            extras).
	 * @return The increment size
	 */
	protected int determineIncrementSize(Properties params)
	{
		return PropertiesHelper.getInt(INCREMENT_PARAM, params, DEFAULT_INCREMENT_SIZE);
	}

	/**
	 * Determine the optimizer to use.
	 * <p/>
	 * Called during {@link #configure configuration}.
	 * 
	 * @param params
	 *            The params supplied in the generator config (plus some standard useful
	 *            extras).
	 * @param incrementSize
	 *            The {@link #determineIncrementSize determined increment size}
	 * @return The optimizer strategy (name)
	 */
	protected String determineOptimizationStrategy(Properties params, int incrementSize)
	{
		String defOptStrategy = incrementSize <= 1 ? OptimizerFactory.NONE : OptimizerFactory.POOL;
		return PropertiesHelper.getString(OPT_PARAM, params, defOptStrategy);
	}

	/**
	 * In certain cases we need to adjust the increment size based on the selected
	 * optimizer. This is the hook to achieve that.
	 * 
	 * @param optimizationStrategy
	 *            The optimizer strategy (name)
	 * @param incrementSize
	 *            The {@link #determineIncrementSize determined increment size}
	 * @return The adjusted increment size.
	 */
	protected int determineAdjustedIncrementSize(String optimizationStrategy, int incrementSize)
	{
		if (OptimizerFactory.NONE.equals(optimizationStrategy) && incrementSize > 1)
		{
			log.warn("config specified explicit optimizer of [" + OptimizerFactory.NONE
				+ "], but [" + INCREMENT_PARAM + "=" + incrementSize
				+ "; honoring optimizer setting");
			incrementSize = 1;
		}
		return incrementSize;
	}

	/**
	 * Build the database structure.
	 * 
	 * @param params
	 *            The params supplied in the generator config (plus some standard useful
	 *            extras).
	 * @param dialect
	 *            The dialect being used.
	 * @param forceTableUse
	 *            Should a table be used even if the dialect supports sequences?
	 * @param sequenceName
	 *            The name to use for the sequence or table.
	 * @param initialValue
	 *            The initial value.
	 * @param incrementSize
	 *            the increment size to use (after any adjustments).
	 * @return The db structure representation
	 */
	protected DatabaseStructure buildDatabaseStructure(Properties params, Dialect dialect,
			boolean forceTableUse, String sequenceName, int initialValue, int incrementSize)
	{
		boolean useSequence = dialect.supportsSequences() && !forceTableUse;
		if (useSequence)
		{
			return new SequenceStructure(dialect, sequenceName, initialValue, incrementSize,
				Long.class);
		}
		else
		{
			String valueColumnName = determineValueColumnName(params);
			return new TableStructure(dialect, sequenceName, valueColumnName, initialValue,
				incrementSize, Long.class);
		}
	}

	// IdentifierGenerator implementation ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException
	{
		return optimizer.generate(databaseStructure.buildCallback(session));
	}

	// PersistentIdentifierGenerator implementation ~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Object generatorKey()
	{
		return databaseStructure.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] sqlCreateStrings(Dialect dialect) throws HibernateException
	{
		return databaseStructure.sqlCreateStrings(dialect);
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] sqlDropStrings(Dialect dialect) throws HibernateException
	{
		return databaseStructure.sqlDropStrings(dialect);
	}
}
