/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.settings;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;

import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Een instelling van een organisatie (instelling). De standaard setting geld per
 * organisatie maar subclasses kunnen dat verder uitsplitsen naar bv organisatieeenheid,
 * medewerker, etc. Vergeet niet een default waarde te specificeren in
 * {@link SettingsDataAccessHelper}
 * 
 * @author marrink
 * @param <T>
 *            het type van de return waarde
 */
@Entity()
@DiscriminatorColumn(length = 255)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public abstract class OrganisatieSetting<T> extends OrganisatieEntiteit
{
	private static final long serialVersionUID = 1L;

	public OrganisatieSetting()
	{
	}

	/**
	 * Een statische omschrijving die getoond kan worden als label.
	 * 
	 * @return Returns the omschrijving.
	 */
	public abstract String getOmschrijving();

	/**
	 * De waarde van de setting.
	 * 
	 * @return de waarde
	 */
	public abstract T getValue();

	/**
	 * Set de waarde.
	 * 
	 * @param value
	 */
	public abstract void setValue(T value);

}
