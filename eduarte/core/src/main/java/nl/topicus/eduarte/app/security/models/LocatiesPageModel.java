/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.models;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.providers.OrganisatieEenheidProvider;
import nl.topicus.eduarte.util.OrganisatieEenheidLocatieUtil;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

/**
 * Een model om de lijst met locaties op te leveren waar de gebruiker recht op heeft,
 * zonder vast te zitten aan een component. Dit betekent wel dat alle models op dezelfde
 * pagina dezelfde rechten hebben. Indien er een andere
 * {@link nl.topicus.eduarte.entities.organisatie.Instelling} of {@link Medewerker} dan de
 * ingelogde nodig is moet de overeenkomstige methode uit dit model overschreven worden.
 * In de policyfile dient voor dit model een {@link DataPermission} opgenomen te worden
 * met als naam {pageClass}&lt;model.list.Locatie&gt; waarbij {pageClass} de classnaam van
 * de {@link Page} is.
 * 
 * @author marrink
 */
public class LocatiesPageModel extends LoadableDetachableModel<List<Locatie>>
{
	private static final long serialVersionUID = 1L;

	private OrganisatieEenheidProvider organisatieEenheidProvider;

	private IModel< ? > filterEntiteitModel;

	private IOrganisatieEenhedenLocatiesModel organisatieEenheidLocatiesModel;

	public LocatiesPageModel(ISecurityCheck check,
			OrganisatieEenheidProvider organisatieEenheidProvider)
	{
		this(check, organisatieEenheidProvider, null);
	}

	public LocatiesPageModel(ISecurityCheck check,
			OrganisatieEenheidProvider organisatieEenheidProvider, IModel< ? > filterEntiteitModel)
	{
		this.organisatieEenheidLocatiesModel = new OrganisatieEenhedenLocatiesModel(check);
		this.organisatieEenheidProvider = organisatieEenheidProvider;
		this.filterEntiteitModel = filterEntiteitModel;
	}

	@Override
	protected List<Locatie> load()
	{
		List<OrganisatieEenheidLocatieKoppeling> orgEhdLocs =
			OrganisatieEenheidLocatieUtil.flatten(organisatieEenheidLocatiesModel.getObject());

		List<IOrganisatieEenheidLocatieKoppelEntiteit< ? >> filter =
			OrganisatieEenheidLocatieUtil.getFilterKoppelingen(filterEntiteitModel);
		if (filter != null)
			orgEhdLocs =
				OrganisatieEenheidLocatieUtil.filterOrganisatieEenheidLocatie(orgEhdLocs, filter);

		List<Locatie> ret =
			OrganisatieEenheidLocatieUtil.getActieveLocaties(orgEhdLocs,
				organisatieEenheidProvider == null ? null : organisatieEenheidProvider
					.getOrganisatieEenheid());

		Collections.sort(ret, new Comparator<Locatie>()
		{
			@Override
			public int compare(Locatie o1, Locatie o2)
			{
				return o1.toString().compareTo(o2.toString());
			}
		});
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
