package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.participatie.helpers.BudgetDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.participatie.Budget;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.BudgetZoekFilter;

import org.hibernate.Criteria;

public class BudgetHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Budget, BudgetZoekFilter> implements
		BudgetDataAccessHelper
{
	public BudgetHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BudgetZoekFilter filter)
	{
		Criteria criteria = createCriteria(Budget.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("onderwijsproduct", "onderwijsproduct");
		builder.addEquals("onderwijsproduct", filter.getOnderwijsproduct());
		builder.addEquals("verbintenis", filter.getVerbintenis());
		builder.addEquals("aantalUur", filter.getAantalUur());
		return criteria;
	}

	@Override
	public int getBudget(Onderwijsproduct onderwijsproduct, Deelnemer deelnemer)
	{
		Asserts.assertNotNull("onderwijsproduct", onderwijsproduct);
		Asserts.assertNotNull("deelnemer", deelnemer);
		Criteria criteria = createCriteria(Budget.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("onderwijsproduct", onderwijsproduct);
		builder.addIn("verbintenis", deelnemer.getVerbintenissen());
		List<Budget> budgetList = cachedList(criteria);
		int aantalUur = 0;
		for (Budget budget : budgetList)
		{
			aantalUur += budget.getAantalUur();
		}
		return aantalUur;
	}

	@Override
	public int getBudgettenVanDeelnemer(Deelnemer deelnemer)
	{
		Asserts.assertNotNull("deelnemer", deelnemer);
		Criteria criteria = createCriteria(Budget.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addIn("verbintenis", deelnemer.getVerbintenissen());
		List<Budget> budgetList = cachedList(criteria);
		int aantalUur = 0;
		for (Budget budget : budgetList)
		{
			aantalUur += budget.getAantalUur();
		}
		return aantalUur;
	}
}
