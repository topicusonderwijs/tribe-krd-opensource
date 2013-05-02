package nl.topicus.eduarte.dao.hibernate;

import java.math.BigDecimal;
import java.util.Properties;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.InstellingSequence;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.settings.DebiteurNummerConfiguration;
import nl.topicus.eduarte.entities.settings.DebiteurNummerSetting;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.enhanced.DatabaseStructure;

/**
 * Een nummer-generator die gekoppeld is aan een InstellingsSequence. Vanuit de applicatie
 * kan dynamisch nieuwe generators aangemaakt worden.
 * 
 * @author Joost Limburg
 */
public class NummerGeneratorHibernateDataAccessHelper extends
		EduarteGeneratorHibernateAccessHelper<InstellingEntiteit> implements
		NummerGeneratorDataAccessHelper
{
	// deze waarde NIET hoger maken ivm long roll-over naar negatieve waarden!
	public static final long MAX_VALUE = 999999999999999999L;

	// Gebruikte dialect, belangrijk voor het achterhalen van ondersteuning van sequences
	// in de database.
	private Dialect dialect;

	private HibernateSessionProvider provider;

	public NummerGeneratorHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
		this.provider = provider;

		dialect = provider.getDialect();
	}

	public enum Sequences
	{
		/**
		 * Opgebouwd uit: DEBITEURNUMMER, naam van de sequence. debiteurennummer,
		 * columnnaam. Persoon.class, entiteit
		 */
		DEBITEURNUMMER("debiteurennummer", Persoon.class),
		ORGDEBITEURNUMMER("debiteurennummer", ExterneOrganisatie.class),
		DEELNEMERNUMMER("deelnemernummer", Deelnemer.class),
		VERZUIMMELDINGSNUMMER("meldingsnummer", IbgVerzuimmelding.class),
		OVEREENKOMSTNUMMER("overeenkomstnummer", Verbintenis.class, BPVInschrijving.class),
		TAXONOMIECODE("id", TaxonomieElement.class),
		// factuur is onderdeel van financieel en hier niet bereikbaar
		FACTUURNUMMER("factuurnummer", "factuur"),
		FACTUUREXPORTNUMMER("nummer", "factuurexport");

		private String targetColumnName;

		private String targetTableName;

		private Class< ? >[] persistentClass;

		private Sequences(String targetColumnName, Class< ? >... persistentClass)
		{
			this.targetColumnName = targetColumnName;
			this.persistentClass = persistentClass;
		}

		private Sequences(String targetColumnName, String targetTableName)
		{
			this.targetColumnName = targetColumnName;
			this.targetTableName = targetTableName;
		}

		public String getTargetColumnName()
		{
			return targetColumnName;
		}

		public Class< ? >[] getPersistentClass()
		{
			return persistentClass;
		}

		public String getTargetTableName()
		{
			return targetTableName;
		}
	}

	/**
	 * Verwijder de sequence van de database en entiteit
	 * 
	 * @param sequence
	 */
	private void dropSequence(InstellingSequence sequence)
	{
		DatabaseStructure structure = null;
		Instelling instelling = EduArteContext.get().getInstelling();
		if (instelling != null)
		{
			String seqNaam = sequence.getNaam() + "_" + instelling.getId();
			Properties params = new Properties();
			params.put(SEQUENCE_PARAM, seqNaam);
			structure =
				this.buildDatabaseStructure(params, dialect, false, seqNaam.toUpperCase(), sequence
					.getStartWaarde().intValue(), 1);

			String[] createSql = structure.sqlDropStrings(dialect);

			Transaction start = provider.getSession().beginTransaction();

			for (String sql : createSql)
			{
				SQLQuery query = provider.getSession().createSQLQuery(sql);
				query.executeUpdate();
			}

			start.commit();
		}
	}

	/**
	 * Creer de nieuwe sequence voor de instelling
	 * 
	 * @param sequence
	 *            Type sequence
	 */
	private void createSequence(InstellingSequence sequence)
	{
		DatabaseStructure structure = null;
		Instelling instelling = EduArteContext.get().getInstelling();
		if (instelling != null)
		{
			String seqNaam = sequence.getNaam() + "_" + instelling.getId();
			Properties params = new Properties();
			params.put(SEQUENCE_PARAM, seqNaam);
			structure =
				this.buildDatabaseStructure(params, dialect, false, seqNaam.toUpperCase(), Math
					.max(sequence.getStartWaarde().intValue(), 1), 1);

			String[] createSql = structure.sqlCreateStrings(dialect);

			Transaction start = provider.getSession().beginTransaction();

			for (String sql : createSql)
			{
				SQLQuery query = provider.getSession().createSQLQuery(sql);
				query.executeUpdate();
			}

			start.commit();
		}
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
	private Long getOrCreateNextValue(Sequences sequence)
	{
		Long newValue = null;
		try
		{
			newValue = generateNextValue(sequence);
		}
		catch (Exception ex)
		{
			// Sequence does not exist, create:
			getCurrentInstellingSequence(sequence);

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
	 * Genereer de volgende waarde van een (instellings)sequence
	 * 
	 * @param sequence
	 *            De sequence die opgevraagd wordt
	 * @return De nieuwe, ongebruikte, waarde
	 */
	private Long generateNextValue(Sequences sequence) throws Exception
	{
		Long newValue = null;
		Instelling instelling = EduArteContext.get().getInstelling();

		if (instelling != null)
		{
			Properties params = new Properties();
			params.put(SEQUENCE_PARAM, (sequence + "_" + instelling.getId()).toUpperCase());
			this.configure(new org.hibernate.type.LongType(), params, dialect);
			newValue =
				(Long) this.generate((SessionImplementor) provider.getSession(), new Long(0));
		}

		return newValue;
	}

	/**
	 * Verkrijg de InstellingSequence instellingen van een instelling aan de hand van de
	 * sequence.
	 * 
	 * @param sequence
	 *            De sequence die gekoppeld aan de InstellingSequence is
	 * @return De instellingSequence die gekoppeld aan de database sequence is
	 */
	public InstellingSequence getCurrentInstellingSequence(Sequences sequence)
	{
		Asserts.assertNotEmpty("naam", sequence.getTargetColumnName());
		Criteria criteria = createCriteria(InstellingSequence.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("naam", sequence.toString().toLowerCase());
		InstellingSequence iseq = (InstellingSequence) cachedUnique(criteria);

		if (iseq == null)
		{
			log.warn("Ontbrekende nummergenerator: " + sequence
				+ ", nieuw aangemaakt met default values.");
			iseq = new InstellingSequence();
			iseq.setNaam(sequence.toString().toLowerCase());

			Long startValue = getMaximumBinnenInstelling(sequence);

			if (startValue == null || startValue < 1)
			{
				startValue = new Long(1);
			}

			iseq.setStartWaarde(startValue);
			iseq.setMaximum(MAX_VALUE);
			iseq.save();

			try
			{
				createSequence(iseq);
				iseq.commit();
			}
			catch (Exception e)
			{

				log.warn("Sequence moet opnieuw aangemaakt worden: " + iseq.getNaam() + " voor "
					+ iseq.getOrganisatie().getNaam());
				try
				{
					dropSequence(iseq);
					createSequence(iseq);
					iseq.commit();
				}
				catch (Exception ex)
				{
					log.error("Kon sequence niet opnieuw aanmaken: " + iseq.getNaam() + " voor "
						+ iseq.getOrganisatie().getNaam());
					iseq.rollback();
				}
			}
		}
		else
		{
			// Check dat de sequence er daadwerkelijk is.
			if (dialect.supportsSequences())
			{
				SQLQuery query =
					createSQLQuery("select last_number from user_sequences where sequence_name='"
						+ (iseq.getNaam() + "_" + iseq.getOrganisatie().getId()).toUpperCase()
						+ "'");
				BigDecimal currWaarde = (BigDecimal) query.uniqueResult();
				if (currWaarde == null)
				{
					createSequence(iseq);
				}
			}

		}

		return iseq;
	}

	@Override
	public long newDebiteurnummer()
	{
		return getOrCreateNextValue(Sequences.DEBITEURNUMMER);
	}

	/**
	 * Kijkt, aan de hand van de DebiteurNummerSetting, of een organisatiedebiteur gebruik
	 * moet maken van de sequence van debiteuren
	 * 
	 * @param sequence
	 *            De originele sequence waarnaar gekeken wordt
	 * @return Aangepaste sequence als OrganisatieDebiteuren gebruik moeten maken van
	 *         Debiteurnummers
	 */
	private Sequences switchDebitOfOrgdebit(Sequences sequence)
	{
		Sequences retSeq = sequence;

		if (sequence.equals(Sequences.ORGDEBITEURNUMMER))
		{
			SettingsDataAccessHelper settingsHelper =
				DataAccessRegistry.getHelper(SettingsDataAccessHelper.class);
			DebiteurNummerSetting debiteurNummerSetting =
				settingsHelper.getSetting(DebiteurNummerSetting.class);

			retSeq = Sequences.DEBITEURNUMMER;

			if (debiteurNummerSetting != null)
			{
				DebiteurNummerConfiguration config = debiteurNummerSetting.getValue();
				if (config != null)
				{
					if (!config.isGezamenlijkeRangePersonenOrganisaties())
					{
						retSeq = Sequences.ORGDEBITEURNUMMER;
					}
				}
			}
		}
		return retSeq;
	}

	@Override
	public long newOrganisatieDebiteurnummer()
	{
		return getOrCreateNextValue(switchDebitOfOrgdebit(Sequences.ORGDEBITEURNUMMER));
	}

	@Override
	public int newDeelnemernummer()
	{
		return getOrCreateNextValue(Sequences.DEELNEMERNUMMER).intValue();
	}

	@Override
	public long newVerzuimmeldingsnummer()
	{
		return getOrCreateNextValue(Sequences.VERZUIMMELDINGSNUMMER);
	}

	@Override
	public long newOvereenkomstnummer()
	{
		return getOrCreateNextValue(Sequences.OVEREENKOMSTNUMMER);
	}

	@Override
	public long newFactuurnummer()
	{
		return getOrCreateNextValue(Sequences.FACTUURNUMMER);
	}

	@Override
	public String newTaxonomiecode(String type)
	{
		return type + getOrCreateNextValue(Sequences.TAXONOMIECODE);
	}

	/**
	 * Update de sequence in de database. Dit past de huidige sequence aan, en de entiteit
	 * die aan de instelling gekoppeld is.
	 */
	public void updateInstellingSequence(InstellingSequence sequence)
	{
		log.info("Updating sequence " + sequence.getNaam() + " for "
			+ sequence.getOrganisatie().getNaam());
		sequence.saveOrUpdate();
		sequence.commit();

		try
		{
			log.info("Dropping sequence " + sequence.getNaam() + " for "
				+ sequence.getOrganisatie().getNaam());
			dropSequence(sequence);
		}
		catch (Exception ex)
		{
			log.warn("Exception occured while trying to delete sequence " + sequence.getNaam()
				+ " for " + sequence.getOrganisatie().getNaam());
		}

		try
		{
			log.info("Creating sequence " + sequence.getNaam() + " for "
				+ sequence.getOrganisatie().getNaam());
			createSequence(sequence);
		}
		catch (Exception ex)
		{
			log.error("Exception occured while trying to create sequence " + sequence.getNaam()
				+ " for " + sequence.getOrganisatie().getNaam());
		}

		log.info("Commiting sequence " + sequence.getNaam() + " for "
			+ sequence.getOrganisatie().getNaam());
		sequence.commit();
	}

	/**
	 * Verkrijg de huidige waarde van een sequence
	 * 
	 * @param sequence
	 *            De sequence waarvan de huidige waarde moet worden opgehaald, instelling
	 *            specifiek
	 * @return de huidige waarde van de sequence
	 */
	public long getCurrentValue(Sequences sequence)
	{
		Long currWaarde = new Long(0);
		Instelling instelling = EduArteContext.get().getInstelling();
		Sequences calcSequence = switchDebitOfOrgdebit(sequence);

		try
		{
			// Voor database die sequences ondersteunt (Oracle):
			if (dialect.supportsSequences() && instelling != null)
			{
				SQLQuery query =
					createSQLQuery("select last_number from user_sequences where sequence_name='"
						+ calcSequence + "_" + instelling.getId() + "'");
				currWaarde = ((BigDecimal) query.uniqueResult()).longValue();
			}
			// Voor databases die alleen TABLE sequences ondersteunt (SQLServer):
			else if (instelling != null)
			{
				SQLQuery query =
					createSQLQuery("select next_val from " + calcSequence + "_"
						+ instelling.getId());
				currWaarde = ((BigDecimal) query.uniqueResult()).longValue();
			}
		}
		catch (Exception ex)
		{
			// Sequence does not exist, create:
			getCurrentInstellingSequence(sequence);

			try
			{
				currWaarde = getOrCreateNextValue(sequence);
			}
			catch (Exception e)
			{
				log.error("Could not generate next value for sequence: " + sequence);
			}
		}

		return currWaarde;
	}

	/**
	 * Haalt de huidige maximumwaarde op van een sequence binnen een instelling
	 * 
	 * @param sequences
	 *            De sequence waarvan de maximumwaarde bepaalt moet worden
	 * @return De maximale huidige waarde binnen de sequence, of 0 als deze nog geen
	 *         waarden heeft
	 */
	private long getMaximumBinnenInstelling(Sequences sequences)
	{
		long maxVal = 0L;
		Instelling instelling = EduArteContext.get().getInstelling();

		if (sequences.getTargetTableName() != null && instelling != null)
		{
			SQLQuery huidigQuery =
				createSQLQuery("select max( " + sequences.getTargetColumnName() + ") from "
					+ sequences.getTargetTableName() + " where organisatie=" + instelling.getId());
			Object huidig = huidigQuery.uniqueResult();
			maxVal = (huidig != null ? ((Number) huidig).longValue() : 1);
		}
		else if (instelling != null)
		{
			for (Class< ? > clazz : sequences.getPersistentClass())
			{
				Number currentval = null;
				Criteria huidig = createCriteria(clazz);
				huidig.setProjection(Projections.max(sequences.getTargetColumnName()));
				currentval = uncachedUnique(huidig);

				if (currentval == null)
				{
					currentval = 1;
				}
				else
				{
					maxVal = currentval.longValue();
				}
			}
		}
		return maxVal;
	}
}

