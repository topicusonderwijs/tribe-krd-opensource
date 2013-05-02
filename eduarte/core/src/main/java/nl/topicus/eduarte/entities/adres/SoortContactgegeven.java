/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.adres;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.SoortContactgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
@IsViewWhenOnNoise
public class SoortContactgegeven extends CodeNaamActiefInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Index(name = "idx_SsoortContact_TypeContact")
	private TypeContactgegeven typeContactgegeven;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@Index(name = "idx_SsoortContact_Std")
	private StandaardContactgegeven standaardContactgegeven;

	public SoortContactgegeven()
	{
	}

	/**
	 * @return Returns the typeContactgegeven.
	 */
	public TypeContactgegeven getTypeContactgegeven()
	{
		return typeContactgegeven;
	}

	/**
	 * @param typeContactgegeven
	 *            The typeContactgegeven to set.
	 */
	public void setTypeContactgegeven(TypeContactgegeven typeContactgegeven)
	{
		this.typeContactgegeven = typeContactgegeven;
	}

	/**
	 * @param code
	 * @return Soort contactgegeven
	 */
	public static SoortContactgegeven getSoortContactgegeven(String code)
	{
		return getContactgegevenHelper().get(code);
	}

	public StandaardContactgegeven getStandaardContactgegeven()
	{
		return standaardContactgegeven;
	}

	public void setStandaardContactgegeven(StandaardContactgegeven standaardContactgegeven)
	{
		this.standaardContactgegeven = standaardContactgegeven;
	}

	public static SoortContactgegevenDataAccessHelper getContactgegevenHelper()
	{
		return DataAccessRegistry.getHelper(SoortContactgegevenDataAccessHelper.class);
	}

	public static SoortContactgegeven getSoortContactgegeven(TypeContactgegeven typegegeven)
	{
		return getContactgegevenHelper().get(typegegeven);
	}
}
