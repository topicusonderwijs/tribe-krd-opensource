package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.BookmarkFolderDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.sidebar.BookmarkFolder;
import nl.topicus.eduarte.zoekfilters.BookmarkFolderZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

/**
 * @author niesink
 */
public class BookmarkFolderHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<BookmarkFolder, BookmarkFolderZoekFilter> implements
		BookmarkFolderDataAccessHelper
{

	/**
	 * Constructor
	 * 
	 * @param provider
	 */
	public BookmarkFolderHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<BookmarkFolder> list(Account account)
	{
		Asserts.assertNotNull("account", account);
		Criteria criteria = createCriteria(BookmarkFolder.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("account", account);
		return cachedTypedList(criteria);
	}

	@Override
	public BookmarkFolder getVolgorde(Integer volgorde)
	{
		Criteria criteria = createCriteria(BookmarkFolder.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("volgorde", volgorde);
		return cachedTypedUnique(criteria);
	}

	@Override
	protected Criteria createCriteria(BookmarkFolderZoekFilter filter)
	{
		Criteria criteria = createCriteria(BookmarkFolder.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("volgorde", filter.getVolgorde());
		if (filter.getAccount() != null)
		{
			builder.createAlias("account", "account");
			builder.addEquals("account.id", filter.getAccount().getId());
		}
		criteria.addOrder(Order.desc("volgorde"));
		return criteria;
	}
}
