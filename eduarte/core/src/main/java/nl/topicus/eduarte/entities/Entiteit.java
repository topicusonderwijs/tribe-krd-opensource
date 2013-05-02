/*

 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.InGebruikCheckDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.UnproxyingHelper;
import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.entities.TransientIdObject;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.util.StringUtil.StringConverter;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.security.authentication.Account;

import org.apache.wicket.util.lang.PropertyResolver;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;
import org.hibernate.proxy.HibernateProxy;

@MappedSuperclass()
public abstract class Entiteit implements TransientIdObject
{
	private static final long serialVersionUID = 1L;

	public static final String STR_CREATED_AT = "CREATED_AT";

	public static final String STR_CREATED_BY = "CREATED_BY";

	public static final String STR_LAST_MODIFIED_AT = "LAST_MODIFIED_AT";

	public static final String STR_LAST_MODIFIED_BY = "LAST_MODIFIED_BY";

	public static final String STR_INDEX_GENERATED_NAME = "GENERATED_NAME";

	public static final class PropertyStringConverter<T extends Entiteit> implements
			StringConverter<T>
	{
		private final String separator;

		private final String expression;

		public PropertyStringConverter(String expression)
		{
			this(", ", expression);
		}

		public PropertyStringConverter(String separator, String expression)
		{
			this.separator = separator;
			this.expression = expression;
		}

		public String toString(T object, int listIndex)
		{
			Object value = PropertyResolver.getValue(expression, object);
			return value == null ? "" : String.valueOf(value);
		}

		public String getSeparator(int listIndex)
		{
			return separator;
		}
	}

	/** unieke identifier voor de entiteit. */
	@Id()
	@GeneratedValue(generator = "hibernate_generator")
	/** generator die het doet op meerdere database omgevingen **/
	@GenericGenerator(name = "hibernate_generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
		@Parameter(name = "sequence_name", value = "HIBERNATE_SEQUENCE"),
		@Parameter(name = "increment_size", value = "1"),
		@Parameter(name = "optimizer", value = "none")})
	@AccessType("property")
	private Long id;

	/**
	 * Boolean die aangeeft of dit object gearchiveerd is. Standaard vragen operationele
	 * queries alleen de niet-gearchiveerde gegevens op. Gegegevens moeten handmatig
	 * gearchiveerd worden door de gebruiker. Defaultwaarde is toegevoegd voor de
	 * conversie.
	 */
	@Column(nullable = false, columnDefinition = "number(1,0) default 0")
	@Index(name = STR_INDEX_GENERATED_NAME + "_arch")
	@AutoForm(include = false)
	private boolean gearchiveerd;

	/**
	 * Creatie datum van deze entiteit, wordt gezet door de
	 * {@link EntiteitAuditInterceptor}.
	 */
	@Column(updatable = false, name = STR_CREATED_AT)
	@FieldPersistance(value = FieldPersistenceMode.SKIP)
	@RestrictedAccess(hasSetter = false)
	@AutoForm(include = false)
	private Date createdAt;

	/**
	 * Creator account van deze entiteit, wordt automatisch gezet door de
	 * {@link EntiteitAuditInterceptor}.
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(updatable = false, name = STR_CREATED_BY)
	@FieldPersistance(value = FieldPersistenceMode.SKIP)
	@RestrictedAccess(hasSetter = false)
	@AutoForm(include = false)
	private Account createdBy;

	/**
	 * Laatste modificatie datum van deze entiteit, wordt automatisch gezet door de
	 * {@link EntiteitAuditInterceptor}.
	 */
	@Column(updatable = true, name = STR_LAST_MODIFIED_AT)
	@FieldPersistance(value = FieldPersistenceMode.SKIP)
	@RestrictedAccess(hasSetter = false)
	@AutoForm(include = false)
	private Date lastModifiedAt;

	/**
	 * Account die deze entiteit als laatste gewijzigd heeft. Wordt automatisch gezet door
	 * de {@link EntiteitAuditInterceptor}.
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(updatable = true, name = STR_LAST_MODIFIED_BY)
	@FieldPersistance(value = FieldPersistenceMode.SKIP)
	@RestrictedAccess(hasSetter = false)
	@AutoForm(include = false)
	private Account lastModifiedBy;

	@Transient
	private Serializable tempId;

	/**
	 * versie nummer voor de entiteit, hiermee wordt bepaald of een object al door een
	 * ander gewijzigd is.
	 */
	@Version()
	private Long version;

	/**
	 * Constructor.
	 */
	public Entiteit()
	{
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @see nl.topicus.cobra.entities.IdObject#getIdAsSerializable()
	 */
	public Serializable getIdAsSerializable()
	{
		return getId();
	}

	/**
	 * @see nl.topicus.cobra.entities.IdObject#isSaved()
	 */
	public boolean isSaved()
	{
		// Opgeslagen als een GELDIG id aan toegekend is.
		// Het kan voorkomen dat er nep-id's toegekend worden
		// in de applicatie om speciale gevallen te kunnen herkennen.
		return (getId() != null && getId().longValue() > 0);
	}

	/**
	 * Zet de identifier (wordt door Hibernate automatisch gedaan).
	 * 
	 * @param id
	 *            The id to set.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Geeft de versie van de entiteit, wordt gebruikt voor optimistic locking.
	 * 
	 * @return de versie van de entiteit.
	 */
	public Long getVersion()
	{
		return version;
	}

	/**
	 * Zet de versie van de entiteit, wordt gebruikt voor optimistic locking.
	 * 
	 * @param version
	 *            het nieuwe versie nummer
	 */
	public void setVersion(Long version)
	{
		this.version = version;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + ": " + String.valueOf(id);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj instanceof Entiteit)
		{
			Entiteit other = (Entiteit) obj;
			if (getTemporaryId() != null)
				return getTemporaryId().equals(other.getTemporaryId());
			if (getId() == null && other.getId() == null)
				return false;
			else if (getId() != null)
				return getId().equals(other.getId());
			// else fallthrough
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		if (getTemporaryId() != null)
			return getTemporaryId().hashCode();
		if (id != null)
			return id.hashCode();
		return super.hashCode();
	}

	/**
	 * Voegt de entiteit toe aan de database. Er wordt geen commit uitgevoerd na de actie.
	 * 
	 * @return Het id van de entiteit
	 */
	@SuppressWarnings("unchecked")
	public Serializable save()
	{
		return getBatchDataAccessHelper().save(this);
	}

	/**
	 * Wijzigt de entiteit in de database. Er wordt geen commit uitgevoerd.
	 */
	@SuppressWarnings("unchecked")
	public void update()
	{
		getBatchDataAccessHelper().update(this);
	}

	/**
	 * Wijzig of voegt de entiteit toe aan de database. Er wordt geen commit uitgevoerd.
	 */
	@SuppressWarnings("unchecked")
	public void saveOrUpdate()
	{
		getBatchDataAccessHelper().saveOrUpdate(this);
	}

	/**
	 * Verwijderd de entiteit uit de database. Er wordt geen commit uitgevoerd.
	 */
	@SuppressWarnings("unchecked")
	public void delete()
	{
		getBatchDataAccessHelper().delete(this);
	}

	/**
	 * Easy accessmethod to a <code>BatchDataAccessHelper</code>. Override in subclass to
	 * meet your desired subclass.
	 * 
	 * @return a helper
	 */
	@SuppressWarnings("unchecked")
	protected BatchDataAccessHelper getBatchDataAccessHelper()
	{
		return DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
	}

	/**
	 * Voert een commit uit op de database.
	 */
	public void commit()
	{
		getBatchDataAccessHelper().batchExecute();
	}

	/**
	 * Voert een rollback uit op de database.
	 */
	public void rollback()
	{
		getBatchDataAccessHelper().batchRollback();
	}

	/**
	 * Verwijdert de entiteit uit de huidige Hibernate sessie.
	 */
	@SuppressWarnings("unchecked")
	public void evict()
	{
		((HibernateDataAccessHelper) getBatchDataAccessHelper()).evict(this);
	}

	/**
	 * Forceert een update in de database van deze entiteit. <strong>LETOP</strong> Dit
	 * voert dus ook eventuele openstaande wijzigingen voor deze entiteit in de database
	 * door!
	 */
	public void touch()
	{
		this.lastModifiedAt = new Date();
		saveOrUpdate();
	}

	/**
	 * @see nl.topicus.cobra.entities.TransientIdObject#getTemporaryId()
	 */
	@Override
	public Serializable getTemporaryId()
	{
		return tempId;
	}

	/**
	 * @see nl.topicus.cobra.entities.TransientIdObject#setTemporaryId(java.io.Serializable)
	 */
	@Override
	public void setTemporaryId(Serializable id)
	{
		this.tempId = id;
	}

	/**
	 * Geeft het huidig gebruikte id. Als het object opgeslagen is, dan is dit het echte
	 * id, anders het temp id.
	 */
	public Serializable getCurrentId()
	{
		if (isSaved())
			return getId();
		return getTemporaryId();
	}

	/**
	 * unproxied een proxy. Let op het huidige object wordt nooit aangepast maar je krijgt
	 * het unproxied object terug.
	 * 
	 * @return this indien geen proxy anders een ander object
	 */
	@SuppressWarnings("unchecked")
	public final <T extends Entiteit> T doUnproxy()
	{
		UnproxyingHelper helper =
			(UnproxyingHelper) DataAccessRegistry.getInstance().getAccessHelperImplementing(
				UnproxyingHelper.class);
		return (T) helper.unproxy(this);
	}

	@SuppressWarnings("unchecked")
	public final <T extends Entiteit> T reget(Class<T> clazz)
	{
		DataAccessHelper<T> helper = DataAccessRegistry.getHelper(DataAccessHelper.class);
		Serializable getId = id;
		if (this instanceof HibernateProxy)
		{
			getId = ((HibernateProxy) this).getHibernateLazyInitializer().getIdentifier();
		}
		return helper.get(clazz, getId);
	}

	/**
	 * Doet een refresh van deze entiteit
	 */
	@SuppressWarnings("unchecked")
	public void refresh()
	{
		((HibernateDataAccessHelper) getBatchDataAccessHelper()).refresh(this);
	}

	/**
	 * Doet een flush van de Hibernate session (en dus niet alleen van deze entiteit!)
	 */
	public void flush()
	{
		getBatchDataAccessHelper().flush();
	}

	/**
	 * @return Returns the createdAt.
	 */
	public Date getCreatedAt()
	{
		return createdAt;
	}

	/**
	 * @return Returns the createdBy.
	 */
	public Account getCreatedBy()
	{
		return createdBy;
	}

	/**
	 * @return Returns the lastModifiedAt.
	 */
	public Date getLastModifiedAt()
	{
		return lastModifiedAt;
	}

	public String getLastModifiedAtFormatted()
	{
		return TimeUtil.getInstance().formatDateTime(getLastModifiedAt());
	}

	/**
	 * @return Returns the lastModifiedBy.
	 */
	public Account getLastModifiedBy()
	{
		return lastModifiedBy;
	}

	protected void setLastModifiedAt(Date lastModifiedAt)
	{
		this.lastModifiedAt = lastModifiedAt;
	}


	public abstract boolean isLandelijk();

	/**
	 * @return Ja als dit een landelijk object is en anders Nee.
	 */
	public String getLandelijkOmschrijving()
	{
		return isLandelijk() ? "Ja" : "Nee";
	}

	public boolean isGearchiveerd()
	{
		return gearchiveerd;
	}

	public void setGearchiveerd(boolean gearchiveerd)
	{
		this.gearchiveerd = gearchiveerd;
	}

	public boolean isInGebruik()
	{
		return DataAccessRegistry.getHelper(InGebruikCheckDataAccessHelper.class).isInGebruik(this);
	}
}
