/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

/**
 * @author papegaaij
 */
public interface IInstellingEntiteit extends IOrganisatieEntiteit
{
	/**
	 * @return De instelling waarbij de entiteit hoort
	 */
	public Instelling getOrganisatie();
}
