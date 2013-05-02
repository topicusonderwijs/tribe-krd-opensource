/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.models;

import java.util.List;

import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;

import org.apache.wicket.model.IModel;

public interface IOrganisatieEenhedenLocatiesModel extends
		IModel<List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >>>
{

	public boolean isInstellingClearance();

	public boolean isOrganisatieEenheidClearance();
}
