/*
 * $Id: IExportableView.java,v 1.2 2008-03-14 09:41:29 harmsen Exp $
 * $Revision: 1.2 $
 * $Date: 2008-03-14 09:41:29 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.dataview;

import nl.topicus.cobra.fileresources.CsvFileResourceStream;

/**
 * Interface voor views, zoals dataviews en listviews, die de gegevens naar een
 * csv-bestand kunnen exporteren.
 * 
 * @author loite
 */
public interface IExportableView
{
	public CsvFileResourceStream generateCsvFileFromView(String bestandsnaam, String header);

	public CsvFileResourceStream generateCsvFileFromView(String bestandsnaam);

	/**
	 * @return true wanneer de dataview kolommen die herhaald worden op meerdere regels
	 *         mag overslaan (dus bijvoorbeeld naam van een deelnemer bij het tonen van
	 *         verbintenissen).
	 */
	public boolean allowSkipColumns();

}
