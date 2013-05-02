/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.util.importeren.accounts;

import java.io.Serializable;

/**
 * @author loite
 */
public final class ImportSettings implements Serializable
{
	private static final long serialVersionUID = 1L;

	private IdentificerendeKolom identificerendeKolom;

	private boolean wachtwoordGenereren;

	private boolean emailVersturen;

	/**
	 * Constructor
	 */
	public ImportSettings()
	{
		identificerendeKolom = IdentificerendeKolom.Roostercode;
	}

	/**
	 * @return Returns the identificerendeKolom.
	 */
	public IdentificerendeKolom getIdentificerendeKolom()
	{
		return identificerendeKolom;
	}

	/**
	 * @param identificerendeKolom
	 *            The identificerendeKolom to set.
	 */
	public void setIdentificerendeKolom(IdentificerendeKolom identificerendeKolom)
	{
		this.identificerendeKolom = identificerendeKolom;
	}

	/**
	 * @return Returns the wachtwoordGenereren.
	 */
	public boolean isWachtwoordGenereren()
	{
		return wachtwoordGenereren;
	}

	/**
	 * @param wachtwoordGenereren
	 *            The wachtwoordGenereren to set.
	 */
	public void setWachtwoordGenereren(boolean wachtwoordGenereren)
	{
		this.wachtwoordGenereren = wachtwoordGenereren;
	}

	/**
	 * @return Returns the emailVersturen.
	 */
	public boolean isEmailVersturen()
	{
		return emailVersturen;
	}

	/**
	 * @param emailVersturen
	 *            The emailVersturen to set.
	 */
	public void setEmailVersturen(boolean emailVersturen)
	{
		this.emailVersturen = emailVersturen;
	}
}
