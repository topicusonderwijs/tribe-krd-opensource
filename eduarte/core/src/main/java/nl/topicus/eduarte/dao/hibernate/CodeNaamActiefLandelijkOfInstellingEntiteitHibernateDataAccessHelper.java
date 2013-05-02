package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.codenaamactief.ICodeNaamActiefEntiteit;
import nl.topicus.eduarte.entities.onderwijsproduct.*;
import nl.topicus.eduarte.entities.opleiding.Team;
import nl.topicus.eduarte.entities.organisatie.SoortExterneOrganisatie;
import nl.topicus.eduarte.zoekfilters.ICodeNaamActiefZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper<T extends ICodeNaamActiefEntiteit, ZF extends ICodeNaamActiefZoekFilter<T>>
		extends AbstractZoekFilterDataAccessHelper<T, ZF> implements
		CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<T, ZF>
{
	public CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper(
			HibernateSessionProvider provider, QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ZF filter)
	{
		Criteria criteria = createCriteria(filter.getEntityClass());
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("actief", filter.getActief());

		return criteria;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R extends T> R get(String code, Class<R> entityClass)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(entityClass);
		criteria.add(Restrictions.eq("code", code));

		return (R) cachedTypedUnique(criteria);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R extends T> List<R> list(Class<R> entityClass, String... orderBy)
	{
		Criteria criteria = createCriteria(entityClass);
		criteria.add(Restrictions.eq("actief", Boolean.TRUE));
		if (orderBy.length == 0)
		{
			criteria.addOrder(Order.asc("naam"));
		}
		else
		{
			for (String order : orderBy)
			{
				criteria.addOrder(Order.asc(order));
			}
		}
		return (List<R>) cachedTypedList(criteria);
	}

	@Override
	public boolean isLeerstijlInGebruik(Leerstijl leerstijl)
	{
		return leerstijl.isInGebruik();
	}

	@Override
	public boolean isSoortPraktijklokaalInGebruik(SoortPraktijklokaal soortPraktijklokaal)
	{
		return soortPraktijklokaal.isInGebruik();
	}

	@Override
	public boolean isTypeLocatieInGebruik(TypeLocatie typeLocatie)
	{
		return typeLocatie.isInGebruik();
	}

	@Override
	public boolean isTypeToetsInGebruik(TypeToets typeToets)
	{
		return typeToets.isInGebruik();
	}

	@Override
	public boolean isSoortOnderwijsproductInGebruik(SoortOnderwijsproduct soortOnderwijsproduct)
	{
		return soortOnderwijsproduct.isInGebruik();
	}

	@Override
	public boolean isSoortExterneOrganisatieInGebruik(
			SoortExterneOrganisatie soortExterneOrganisatie)
	{
		return soortExterneOrganisatie.isInGebruik();
	}

	@Override
	public boolean isGebruiksmiddelInGebruik(Gebruiksmiddel gebruiksmiddel)
	{
		return gebruiksmiddel.isInGebruik();
	}

	@Override
	public boolean isVerbruiksmiddelInGebruik(Verbruiksmiddel verbruiksmiddel)
	{
		return verbruiksmiddel.isInGebruik();
	}

	@Override
	public boolean isAggregatieniveauInGebruik(Aggregatieniveau aggregatieniveau)
	{
		return aggregatieniveau.isInGebruik();
	}

	@Override
	public Aggregatieniveau getAggregatieniveau(int niveau)
	{
		Criteria criteria = createCriteria(Aggregatieniveau.class);
		criteria.add(Restrictions.eq("niveau", Integer.valueOf(niveau)));
		return (Aggregatieniveau) cachedTypedUnique(criteria);
	}

	@Override
	public boolean isTeamInGebruik(Team team)
	{
		return team.isInGebruik();
	}

	@Override
	public boolean isOnderwijsproductNiveauInGebruik(OnderwijsproductNiveauaanduiding niveau)
	{
		return niveau.isInGebruik();
	}

}
