/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.eduarte.app.security.models.IOrganisatieEenhedenLocatiesModel;
import nl.topicus.eduarte.app.security.models.OrganisatieEenheidLocatieKoppeling;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndelingOrganisatieEenheidLocatie;
import nl.topicus.eduarte.participatie.zoekfilters.LesweekindelingZoekFilter;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Criteria;

public class LesweekIndelingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<LesweekIndeling, LesweekindelingZoekFilter> implements
		LesweekIndelingDataAccessHelper
{
	private static class OrganisatieEenheidLocatiesModel extends
			LoadableDetachableModel<List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >>>
			implements IOrganisatieEenhedenLocatiesModel
	{
		private static final long serialVersionUID = 1L;

		private IModel<IdObject> participantModel;

		public OrganisatieEenheidLocatiesModel(IdObject participant)
		{
			participantModel = ModelFactory.getModel(participant);
		}

		@Override
		protected List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >> load()
		{
			IdObject participant = participantModel.getObject();
			if (participant instanceof IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? >)
				return ((IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? >) participant)
					.getOrganisatieEenheidLocatieKoppelingen();
			OrganisatieEenheidLocatieProvider orgEhdLoc =
				(OrganisatieEenheidLocatieProvider) participant;
			return Arrays.asList(new OrganisatieEenheidLocatieKoppeling(orgEhdLoc));
		}

		@Override
		public boolean isInstellingClearance()
		{
			return false;
		}

		@Override
		public boolean isOrganisatieEenheidClearance()
		{
			return true;
		}
	}

	public LesweekIndelingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public LesweekIndeling getlesweekIndeling(LesweekindelingZoekFilter filter)
	{
		Criteria criteria = createCriteria(LesweekIndelingOrganisatieEenheidLocatie.class);
		if (!filter.addOrganisatieEenheidLocatieCriteria(criteria))
			return null;

		List<LesweekIndelingOrganisatieEenheidLocatie> tmpList = cachedList(criteria);
		Collections.sort(tmpList);

		if (tmpList == null || tmpList.isEmpty())
			return null;

		return tmpList.get(0).getLesweekIndeling();
	}

	@Override
	protected Criteria createCriteria(LesweekindelingZoekFilter filter)
	{
		Criteria criteria = createCriteria(LesweekIndeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (!filter.addOrganisatieEenheidLocatieDetachedCriteria(this, criteria,
			LesweekIndelingOrganisatieEenheidLocatie.class, "lesweekIndeling"))
			return null;

		builder.addEquals("code", filter.getCode());
		builder.addEquals("naam", filter.getNaam());
		builder.addEquals("omschrijving", filter.getOmschrijving());

		if (filter.isActief())
			builder.addEquals("actief", filter.isActief());

		return criteria;
	}

	@Override
	public List<LesweekIndeling> list()
	{
		return list(LesweekIndeling.class, new String[] {});
	}

	@Override
	public List<LesweekIndeling> getLesweekIndelingen(IdObject context)
	{
		LesweekindelingZoekFilter filter = new LesweekindelingZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new OrganisatieEenheidLocatiesModel(context)));
		return list(filter);
	}
}
