/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.participatie.enums;

import java.util.Date;

/**
 * @author vandekamp eigen interface van blokobject voor participatie
 */
public interface IParticipatieBlokObject
{
	/**
	 * @param datum
	 *            De datum waarop gecontroleerd moet worden
	 * @return true als dit object actief is op de gegeven datum.
	 */
	public boolean isActiefOpDatum(Date datum);

	/**
	 * @return De begintijd van dit blokobject.
	 */
	public Date getBeginDatumTijd();

	/**
	 * @return De eindtijd van dit blokobject.
	 */
	public Date getEindDatumTijd();

	/**
	 * @return het beginlesuur van dit blokobject
	 */
	public Integer getBeginLesuur();

	/**
	 * @return het eindlesuur van dit blokobject
	 */
	public Integer getEindLesuur();

	/**
	 * @return De css-class die gebruikt moet worden voor dit blokobject.
	 */
	public String getCssClass();

	/**
	 * @return De title (tool-tip) die getoond moet worden voor dit blokobject.
	 */
	public String getTitle();

}
