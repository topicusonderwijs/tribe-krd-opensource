package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.BookmarkDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.sidebar.Bookmark;
import nl.topicus.eduarte.entities.sidebar.Bookmark.SoortBookmark;
import nl.topicus.eduarte.zoekfilters.BookmarkZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

public class BookmarkHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Bookmark, BookmarkZoekFilter> implements
		BookmarkDataAccessHelper
{
	public BookmarkHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<Bookmark> list(Account account, String pageClass, SoortBookmark soort)
	{
		Asserts.assertNotNull("account", account);
		Asserts.assertNotEmpty("pageClass", pageClass);
		Criteria criteria = createCriteria(Bookmark.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("account", account);
		builder.addEquals("soort", soort);
		criteria.add(Restrictions.or(Restrictions.eq("pageClass", pageClass),
			Restrictions.eq("pagePrivate", Boolean.FALSE)));
		return cachedTypedList(criteria);
	}

	@Override
	protected Criteria createCriteria(BookmarkZoekFilter filter)
	{
		Criteria criteria = createCriteria(Bookmark.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		criteria.createAlias("bookmarkFolder", "bookmarkFolder", CriteriaSpecification.LEFT_JOIN);
		if (filter.getAccount() != null)
		{
			builder.createAlias("account", "account");
			builder.addEquals("account.id", filter.getAccount().getId());
		}
		builder.addEquals("soort", filter.getSoort());
		return criteria;
	}
}
