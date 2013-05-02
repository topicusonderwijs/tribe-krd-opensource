/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.models;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.providers.LocatieProvider;
import nl.topicus.eduarte.util.OrganisatieEenheidLocatieUtil;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

/**
 * Een model om de lijst met vestigingen op te leveren waar de gebruiker recht op heeft,
 * zonder vast te zitten aan een component. Dit betekend wel dat alle models op dezelfde
 * pagina dezelfde rechten hebben. Indien er een andere
 * {@link nl.topicus.eduarte.entities.organisatie.Instelling} of {@link Medewerker} dan de
 * ingelogde nodig is moet de overeenkomstige methode uit dit model overschreven worden.
 * In de policyfile dient voor dit model een {@link DataPermission} opgenomen te worden
 * met als naam {pageClass}&lt;model.list.OrganisatieEenheid&gt; waarbij {pageClass} de
 * classnaam van de {@link Page} is.
 * 
 * @author marrink
 */
public class OrganisatieEenhedenPageModel extends LoadableDetachableModel<List<OrganisatieEenheid>>
{
	private static final long serialVersionUID = 1L;

	private LocatieProvider locatieProvider;

	private IModel< ? > filterEntiteitModel;

	private IOrganisatieEenhedenLocatiesModel organisatieEenheidLocatiesModel;

	public OrganisatieEenhedenPageModel(ISecurityCheck check, LocatieProvider locatieProvider)
	{
		this(check, locatieProvider, null);
	}

	public OrganisatieEenhedenPageModel(ISecurityCheck check, LocatieProvider locatieProvider,
			IModel< ? > filterEntiteitModel)
	{
		this.organisatieEenheidLocatiesModel = new OrganisatieEenhedenLocatiesModel(check);
		this.locatieProvider = locatieProvider;
		this.filterEntiteitModel = filterEntiteitModel;
	}

	@Override
	protected List<OrganisatieEenheid> load()
	{
		List<OrganisatieEenheidLocatieKoppeling> orgEhdLocs =
			OrganisatieEenheidLocatieUtil.flatten(organisatieEenheidLocatiesModel.getObject());

		List<IOrganisatieEenheidLocatieKoppelEntiteit< ? >> filter =
			OrganisatieEenheidLocatieUtil.getFilterKoppelingen(filterEntiteitModel);
		if (filter != null)
			orgEhdLocs =
				OrganisatieEenheidLocatieUtil.filterOrganisatieEenheidLocatie(orgEhdLocs, filter);

		List<OrganisatieEenheid> ret =
			OrganisatieEenheidLocatieUtil.getActieveOrganisatieEenheden(orgEhdLocs,
				locatieProvider == null ? null : locatieProvider.getLocatie());

		Collections.sort(ret);
		return ret;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filterEntiteitModel);
		ComponentUtil.detachQuietly(organisatieEenheidLocatiesModel);
	}
}