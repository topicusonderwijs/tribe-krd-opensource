/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.DocumentTemplateDataAccessHelper;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class DocumentTemplateHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<DocumentTemplate, DocumentTemplateZoekFilter> implements
		DocumentTemplateDataAccessHelper
{
	public DocumentTemplateHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(DocumentTemplateZoekFilter filter)
	{
		Criteria criteria =
			createCriteria(filter.getTemplateClass() == null ? DocumentTemplate.class : filter
				.getTemplateClass());
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("valid", filter.getValide());
		builder.addEquals("actief", filter.getActief());
		builder.addEquals("context", filter.getContext());
		builder.addEquals("categorie", filter.getCategorie());
		builder.addEquals("taxonomie", filter.getTaxonomie());
		builder.addIn("categorie", filter.getCategorieen());
		builder.addIn("context", filter.getContexten());
		builder.addEquals("examenDocumentType", filter.getExamenDocumentType());
		if (filter.getAccount() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(DocumentTemplateRecht.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addIn("rol", filter.getAccount().getRollenAsRol());
			dc.setProjection(Projections.property("documentTemplate"));
			builder.addOrs(Restrictions.eq("beperkAutorisatie", Boolean.FALSE),
				Subqueries.propertyIn("id", dc));
		}

		builder.addILikeFixedMatchMode("bestandsnaam", filter.getBestandsnaam(), MatchMode.START);
		builder
			.addILikeFixedMatchMode("omschrijving", filter.getOmschrijving(), MatchMode.ANYWHERE);

		return criteria;
	}

	@Override
	public DocumentTemplate get(Taxonomie taxonomie)
	{
		Criteria criteria = createCriteria(DocumentTemplate.class);
		criteria.add(Restrictions.eq("taxonomie", taxonomie));
		return cachedUnique(criteria);
	}
}
