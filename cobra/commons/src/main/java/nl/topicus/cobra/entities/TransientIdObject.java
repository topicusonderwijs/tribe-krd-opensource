/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.entities;

import java.io.Serializable;

/**
 * Interface om transient en persistent objecten uniek te kunnen identificeren binnen
 * mappen of andere collecties. Hiervoor wordt een zogeheten tijdelijk id gezet. Het is
 * niet de bedoeling dat dit id gepersisteerd wordt. Zolang dit id gezet is dienen deze
 * entiteiten dit id te gebruiken in equals en hashcode berekeningen. Ook als in de
 * tussentijd het object gepersisteerd wordt. Zodra dit tijdelijke id weer null is kan de
 * normale equals en hashcode weer in werking treden. Het systeem zal er voor zorgen dat
 * zo snel mogelijk nadat een tijdelijk id niet meer nodig is dit verwijderd wordt.
 * 
 * @author marrink
 */
public interface TransientIdObject extends IdObject
{
	public Serializable getTemporaryId();

	public void setTemporaryId(Serializable id);
}
