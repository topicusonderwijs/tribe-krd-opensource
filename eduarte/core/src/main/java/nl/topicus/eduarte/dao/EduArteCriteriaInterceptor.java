package nl.topicus.eduarte.dao;

import java.util.Arrays;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.organisatie.IInstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEntiteit;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Organisatie;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;

public class EduArteCriteriaInterceptor implements QueryInterceptor
{
	@Override
	public Criteria intercept(Criteria criteria, Class< ? > entityClass)
	{
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (IInstellingEntiteit.class.isAssignableFrom(entityClass))
		{
			Instelling curInstelling = EduArteContext.get().getInstelling();
			if (curInstelling == null)
				throw new IllegalStateException(
					"Cannot create criteria: EduArteContext.get().getInstelling() returns null");
			builder.addEquals("organisatie", curInstelling);
		}
		else if (IOrganisatieEntiteit.class.isAssignableFrom(entityClass))
		{
			Organisatie curOrganisatie = EduArteContext.get().getOrganisatie();
			if (curOrganisatie == null)
				throw new IllegalStateException(
					"Cannot create criteria: EduArteContext.get().getOrganisatie() returns null");
			builder.addEquals("organisatie", curOrganisatie);
		}
		else if (LandelijkOfInstellingEntiteit.class.isAssignableFrom(entityClass))
		{
			Instelling curInstelling = EduArteContext.get().getInstelling();
			if (curInstelling == null)
				builder.addIsNull("organisatie", true);
			else
				builder.addNullOrEquals("organisatie", curInstelling);
		}
		return criteria;
	}

	@Override
	public DetachedCriteria intercept(DetachedCriteria criteria, Class< ? > entityClass)
	{
		DetachedCriteriaBuilder builder = new DetachedCriteriaBuilder(criteria);
		if (IInstellingEntiteit.class.isAssignableFrom(entityClass))
		{
			Instelling curInstelling = EduArteContext.get().getInstelling();
			if (curInstelling == null)
				throw new IllegalStateException(
					"Cannot create criteria: EduArteContext.get().getInstelling() returns null");
			builder.addEquals("organisatie", curInstelling);
		}
		else if (IOrganisatieEntiteit.class.isAssignableFrom(entityClass))
		{
			Organisatie curOrganisatie = EduArteContext.get().getOrganisatie();
			if (curOrganisatie == null)
				throw new IllegalStateException(
					"Cannot create criteria: EduArteContext.get().getOrganisatie() returns null");
			builder.addEquals("organisatie", curOrganisatie);
		}
		else if (LandelijkOfInstellingEntiteit.class.isAssignableFrom(entityClass))
		{
			Instelling curInstelling = EduArteContext.get().getInstelling();
			if (curInstelling == null)
				builder.addIsNull("organisatie", true);
			else
				builder.addNullOrEquals("organisatie", curInstelling);
		}
		return criteria;
	}

	@Override
	public String intercept(String hqlQuery, Class< ? > entityClass, String... aliases)
	{
		StringBuilder ret = new StringBuilder(hqlQuery);
		if (IInstellingEntiteit.class.isAssignableFrom(entityClass))
		{
			Instelling curInstelling = EduArteContext.get().getInstelling();
			if (curInstelling == null)
				throw new IllegalStateException(
					"Cannot create criteria: EduArteContext.get().getInstelling() returns null");
			addClause(ret, "alias.organisatie = :interceptedorganisatie", aliases);
		}
		else if (IOrganisatieEntiteit.class.isAssignableFrom(entityClass))
		{
			Organisatie curOrganisatie = EduArteContext.get().getOrganisatie();
			if (curOrganisatie == null)
				throw new IllegalStateException(
					"Cannot create criteria: EduArteContext.get().getOrganisatie() returns null");
			addClause(ret, "alias.organisatie = :interceptedorganisatie", aliases);
		}
		else if (LandelijkOfInstellingEntiteit.class.isAssignableFrom(entityClass))
		{
			Instelling curInstelling = EduArteContext.get().getInstelling();
			if (curInstelling == null)
				addClause(ret, "alias.organisatie is null", aliases);
			else
				addClause(ret,
					"(alias.organisatie = :interceptedorganisatie or alias.organisatie is null)",
					aliases);
		}
		return ret.toString();
	}

	private void addClause(StringBuilder query, String clause, String... aliases)
	{
		if (aliases.length == 0)
			addClause(query, clause.replaceAll("alias\\.", ""));
		else
		{
			for (String curAlias : aliases)
			{
				addClause(query, clause.replaceAll("alias", curAlias));
			}
		}
	}

	private void addClause(StringBuilder query, String clause)
	{
		if (query.indexOf(" where ") > -1)
			query.append(" and ");
		else
			query.append(" where ");
		query.append(clause);
	}

	@Override
	public void postProcess(Query query, Class< ? > entityClass)
	{
		if (Arrays.asList(query.getNamedParameters()).contains("interceptedorganisatie"))
			query.setParameter("interceptedorganisatie", EduArteContext.get().getOrganisatie());
	}
}
