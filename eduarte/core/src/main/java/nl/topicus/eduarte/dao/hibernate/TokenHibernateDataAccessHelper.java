package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.eduarte.dao.helpers.vasco.TokenDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.vasco.Token;
import nl.topicus.eduarte.entities.security.authentication.vasco.TokenStatus;
import nl.topicus.eduarte.zoekfilters.vasco.TokenZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;

public class TokenHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Token, TokenZoekFilter> implements TokenDataAccessHelper
{
	public TokenHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(TokenZoekFilter filter)
	{
		Criteria criteria = createCriteria(Token.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("gebruiker", "gebruiker", CriteriaSpecification.LEFT_JOIN);
		builder.addEquals("gearchiveerd", false);
		builder.addEquals("gebruiker", filter.getAccount());
		builder.addEquals("serienummer", filter.getSerienummer());
		builder.addEquals("status", filter.getStatus());
		return criteria;
	}

	@Override
	public boolean zijnTokensUitgegeven()
	{
		TokenZoekFilter filter = new TokenZoekFilter();
		filter.setStatus(TokenStatus.Uitgegeven);
		int aantal = listCount(filter);
		return aantal > 0;
	}
}
