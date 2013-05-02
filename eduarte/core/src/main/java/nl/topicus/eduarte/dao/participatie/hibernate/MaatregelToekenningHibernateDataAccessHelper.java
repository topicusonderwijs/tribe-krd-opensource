package nl.topicus.eduarte.dao.participatie.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.participatie.helpers.MaatregelToekenningDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.MaatregelToekenning;
import nl.topicus.eduarte.participatie.zoekfilters.MaatregelToekenningZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

public class MaatregelToekenningHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<MaatregelToekenning, MaatregelToekenningZoekFilter>
		implements MaatregelToekenningDataAccessHelper
{
	public MaatregelToekenningHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(MaatregelToekenningZoekFilter filter)
	{
		Criteria criteria = createCriteria(MaatregelToekenning.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("deelnemer", "deelnemer");
		builder.createAlias("maatregel", "maatregel");
		builder.createAlias("deelnemer.persoon", "persoon");

		DetachedCriteria dcInschrijving = createDetachedCriteria(Verbintenis.class);
		DetachedCriteriaBuilder dBuilder = new DetachedCriteriaBuilder(dcInschrijving);
		dcInschrijving.setProjection(Projections.property("deelnemer"));

		dBuilder.addLessOrEquals("begindatum", filter.getEindDatum());
		dBuilder.addGreaterOrEquals("einddatumNotNull", filter.getBeginDatum());

		if (!filter.addOrganisatieEenheidLocatieCriteria(dcInschrijving))
			return null;

		builder.propertyIn("deelnemer", dcInschrijving);

		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("maatregel", filter.getMaatregel());
		builder.addEquals("nagekomen", filter.getNagekomen());
		builder.addEquals("veroorzaaktDoor", filter.getVeroorzaaktDoor());
		builder.addEquals("maatregelDatum", filter.getMaatregelDatum());
		builder.addGreaterOrEquals("maatregelDatum", filter.getBeginDatum());
		builder.addLessOrEquals("maatregelDatum", filter.getEindDatum());

		return criteria;
	}
}
