package nl.topicus.eduarte.dao.hibernate;

import java.util.Properties;

import nl.topicus.cobra.dao.helpers.IndexNummerGeneratorDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.entities.IdObject;

import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.enhanced.DatabaseStructure;

/**
 * Genereert een ongebruikt nummer voor oa indexes.
 * 
 * @author Joost Limburg
 */
public class IndexNummerGeneratorHibernateDataAccessHelper extends
		EduarteGeneratorHibernateAccessHelper<IdObject> implements
		IndexNummerGeneratorDataAccessHelper
{
	// deze waarde NIET hoger maken ivm long roll-over naar negatieve waarden!
	public static final long MAX_VALUE = 2147483646L;

	// Gebruikte dialect, belangrijk voor het achterhalen van ondersteuning van sequences
	// in de database.
	private Dialect dialect;

	private HibernateSessionProvider provider;

	public IndexNummerGeneratorHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
		this.provider = provider;

		dialect = provider.getDialect();
	}

	@Override
	public int newIndexNummer()
	{
		return getOrCreateNextValue("idx_gen").intValue();
	}

	/**
	 * Probeert een nieuw ongebruikt nummer te vekrijgen, als de sequence niet bestaat
	 * wordt deze aangemaakt in de database en een InstellingSequence entiteit met
	 * standaard waardes
	 * 
	 * @param sequence
	 *            De sequence die opgevraagd wordt
	 * @return Nieuw ongebruikt nummer voor deze sequence
	 */
	private Long getOrCreateNextValue(String sequence)
	{
		Long newValue = null;
		try
		{
			newValue = generateNextValue(sequence);
		}
		catch (Exception ex)
		{
			// Sequence does not exist, create:
			createSequence(sequence);

			try
			{
				newValue = generateNextValue(sequence);
			}
			catch (Exception e)
			{
				log.error("Could not generate next value for sequence: " + sequence);
			}
		}

		return newValue;
	}

	/**
	 * Creer de nieuwe sequence
	 * 
	 * @param sequence
	 *            Type sequence
	 */
	private void createSequence(String sequence)
	{
		DatabaseStructure structure = null;

		String seqNaam = sequence.toUpperCase();
		Properties params = new Properties();
		params.put(SEQUENCE_PARAM, seqNaam);
		structure =
			this.buildDatabaseStructure(params, dialect, false, seqNaam.toUpperCase(), 0, 1);

		String[] createSql = structure.sqlCreateStrings(dialect);

		Transaction start = provider.getSession().beginTransaction();

		for (String sql : createSql)
		{
			SQLQuery query = provider.getSession().createSQLQuery(sql);
			query.executeUpdate();
		}

		start.commit();
	}

	/**
	 * Genereer de volgende waarde van een sequence
	 * 
	 * @param sequence
	 *            De sequence die opgevraagd wordt
	 * @return De nieuwe, ongebruikte, waarde
	 */
	private Long generateNextValue(String sequence) throws Exception
	{
		Long newValue = null;

		Properties params = new Properties();
		params.put(SEQUENCE_PARAM, sequence.toUpperCase());
		this.configure(new org.hibernate.type.LongType(), params, dialect);
		newValue = (Long) this.generate((SessionImplementor) provider.getSession(), new Long(0));

		return newValue;
	}
}
