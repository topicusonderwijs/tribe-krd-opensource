/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.entities;

import java.io.Serializable;

/**
 * @author loite
 */
public interface IdObject extends Serializable
{
	/**
	 * @return Het id van dit object.
	 */
	public Serializable getIdAsSerializable();

	/**
	 * @return true als dit object opgeslagen is in de database, en anders false.
	 */
	public boolean isSaved();

	/**
	 * @return The version of this entity.
	 */
	public Long getVersion();

	/**
	 * @param version
	 */
	public void setVersion(Long version);

}
