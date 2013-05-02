package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.eduarte.dao.helpers.DeelnemerBijlageDataAccessHelper;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;
import nl.topicus.eduarte.zoekfilters.DeelnemerBijlageZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class DeelnemerBijlageHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<DeelnemerBijlage, DeelnemerBijlageZoekFilter> implements
		DeelnemerBijlageDataAccessHelper
{
	public DeelnemerBijlageHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(DeelnemerBijlageZoekFilter filter)
	{
		Criteria criteria = createCriteria(DeelnemerBijlage.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addNotEquals("deelnemer", filter.getNietDeelnemer());

		builder.createAlias("bijlage", "bijlage");
		builder.addEquals("bijlage.documentnummer", filter.getDocumentnummer());
		if (filter.getEinddatumNa() != null)
		{
			criteria.add(Restrictions.or(Restrictions.isNull("bijlage.geldigTot"), Restrictions.ge(
				"bijlage.geldigTot", filter.getEinddatumNa())));
		}
		// builder.addGreaterOrEquals("bijlage.geldigTot", filter.getEinddatumNa());
		builder.addLessThan("bijlage.geldigTot", filter.getEinddatumVoor());

		if (filter.getDocumentCategorie() != null)
		{
			builder.createAlias("bijlage.documentType", "documentType");
			builder.addEquals("documentType.categorie", filter.getDocumentCategorie());
		}
		return criteria;
	}
}
