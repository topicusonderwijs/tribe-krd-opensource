package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.PersoonDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonType;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.zoekfilters.PersoonZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class PersoonHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Persoon, PersoonZoekFilter<Persoon>> implements
		PersoonDataAccessHelper
{
	public PersoonHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public Persoon get(Long id)
	{
		return get(Persoon.class, id);
	}

	@Override
	public List<Persoon> getByBSN(Long bsn)
	{
		Criteria criteria = createCriteria(Persoon.class);
		criteria.add(Restrictions.eq("bsn", bsn));
		List<Persoon> res = cachedTypedList(criteria);

		return res;
	}

	@Override
	public Persoon getPersoonByIdInOudPakket(Long id)
	{
		Criteria criteria = createCriteria(Persoon.class);
		criteria.add(Restrictions.eq("idInOudPakket", id));
		criteria.add(Restrictions.eq("organisatie", EduArteContext.get().getInstelling()));
		return cachedUnique(criteria);
	}

	@Override
	protected Criteria createCriteria(PersoonZoekFilter<Persoon> persoonFilter)
	{
		Criteria criteria = createCriteria(Persoon.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		persoonFilter.addQuickSearchCriteria(builder, "berekendeZoeknaam", "voornamen");

		builder.addILikeCheckWildcard("roepnaam", persoonFilter.getRoepnaam(), MatchMode.ANYWHERE);
		builder.addILikeCheckWildcard("voornamen", persoonFilter.getVoornaam(), MatchMode.ANYWHERE);
		builder.addILikeCheckWildcard("voorvoegsel", persoonFilter.getVoorvoegsel(),
			MatchMode.EXACT);
		builder.addILikeCheckWildcard("achternaam", persoonFilter.getAchternaam(),
			MatchMode.ANYWHERE);
		builder.addEquals("bsn", persoonFilter.getBsn());
		builder.addEquals("geslacht", persoonFilter.getGeslacht());
		builder.addEquals("geboortedatum", persoonFilter.getGeboortedatum());
		if (persoonFilter.getHasDebiteurennummer() != null)
		{
			builder.addIsNull("debiteurennummer", !persoonFilter.getHasDebiteurennummer());
		}

		if (persoonFilter.getPersoonType() != null)
		{
			if (persoonFilter.getPersoonType().equals(PersoonType.Medewerker))
				builder.getCriteria()
					.add(Subqueries.propertyIn("id", createPersoonAlsMedewerker()));
			else if (persoonFilter.getPersoonType().equals(PersoonType.Deelnemer))
				builder.getCriteria().add(Subqueries.propertyIn("id", createPersoonAlsDeelnemer()));
			else if (persoonFilter.getPersoonType().equals(PersoonType.Verzorger))
				builder.getCriteria().add(Subqueries.propertyIn("id", createPersoonAlsVerzorger()));
		}

		return criteria;
	}

	private DetachedCriteria createPersoonAlsMedewerker()
	{
		DetachedCriteria medewerkerDc = createDetachedCriteria(Medewerker.class);
		medewerkerDc.setProjection(Projections.property("persoon"));
		DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(medewerkerDc);
		dcBuilder.addNullOrLessOrEquals("einddatum", TimeUtil.getInstance().currentDate());
		return medewerkerDc;
	}

	private DetachedCriteria createPersoonAlsDeelnemer()
	{
		DetachedCriteria deelnemerCriteria = createDetachedCriteria(Deelnemer.class);
		deelnemerCriteria.setProjection(Projections.property("persoon"));
		DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(deelnemerCriteria);
		dcBuilder.addNullOrLessOrEquals("einddatum", TimeUtil.getInstance().currentDate());
		return deelnemerCriteria;
	}

	private DetachedCriteria createPersoonAlsVerzorger()
	{
		DetachedCriteria relatieCriteria = createDetachedCriteria(Relatie.class);
		relatieCriteria.setProjection(Projections.property("persoon"));
		DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(relatieCriteria);
		dcBuilder.addNullOrLessOrEquals("einddatum", TimeUtil.getInstance().currentDate());
		return relatieCriteria;
	}

	@Override
	public List<Persoon> getKinderen(Long verzorgerId)
	{
		Criteria criteria = createCriteria(Relatie.class);
		criteria.createAlias("deelnemer", "deelnemer");
		criteria.createAlias("relatie", "relatie");

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("relatie.id", verzorgerId);

		criteria.setProjection(Projections.property("deelnemer"));

		return cachedTypedList(criteria);
	}
}
