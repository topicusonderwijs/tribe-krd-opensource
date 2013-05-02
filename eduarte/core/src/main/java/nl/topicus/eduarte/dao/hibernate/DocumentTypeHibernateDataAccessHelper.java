package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.DocumentTypeDataAccessHelper;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.zoekfilters.DocumentTypeZoekFilter;

import org.hibernate.Criteria;

public class DocumentTypeHibernateDataAccessHelper
		extends
		CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper<DocumentType, DocumentTypeZoekFilter>
		implements DocumentTypeDataAccessHelper
{

	public DocumentTypeHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(DocumentTypeZoekFilter filter)
	{
		Criteria criteria = super.createCriteria(filter);
		criteria.createAlias("categorie", "categorie");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("categorie", filter.getCategorie());

		return criteria;
	}
}
