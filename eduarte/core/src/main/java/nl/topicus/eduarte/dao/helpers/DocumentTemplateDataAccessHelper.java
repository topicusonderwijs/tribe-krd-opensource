/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.OnderwijsDocumentTemplate;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;

/**
 * @author hop
 */
public interface DocumentTemplateDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<DocumentTemplate, DocumentTemplateZoekFilter>
{

	/**
	 * @param taxonomie
	 * @return een DocumentTemplate welke de opgegeven Taxonomie gelinkt heeft. Dit is
	 *         eigenlijk altijd een {@link OnderwijsDocumentTemplate}
	 */
	DocumentTemplate get(Taxonomie taxonomie);

}
