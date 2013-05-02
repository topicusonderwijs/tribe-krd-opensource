package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.BPVBedrijfsgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;
import nl.topicus.eduarte.zoekfilters.BPVBedrijfsgegevenZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

public class BPVBedrijfsgegevenHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<BPVBedrijfsgegeven, BPVBedrijfsgegevenZoekFilter>
		implements BPVBedrijfsgegevenDataAccessHelper
{
	public BPVBedrijfsgegevenHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BPVBedrijfsgegevenZoekFilter filter)
	{
		Criteria criteria = createCriteria(BPVBedrijfsgegeven.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		criteria.createAlias("externeOrganisatie", "externeOrganisatie");
		filter
			.addQuickSearchCriteria(builder, "externeOrganisatie.code", "externeOrganisatie.naam");
		builder.addLessOrEquals("externeOrganisatie.begindatum", filter.getPeildatum());
		builder.addGreaterOrEquals("externeOrganisatie.einddatumNotNull", filter.getPeildatum());
		builder.addILikeCheckWildcard("externeOrganisatie.naam", filter.getNaam(),
			MatchMode.ANYWHERE);
		builder.addILikeCheckWildcard("externeOrganisatie.verkorteNaam", filter.getVerkorteNaam(),
			MatchMode.ANYWHERE);
		if (filter.getPlaats() != null)
		{
			DetachedCriteria dc =
				createDetachedCriteria(ExterneOrganisatieAdres.class, "externeOrganisatieAdres");
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("externeOrganisatieAdres.adres", "adres");
			dcBuilder.addILikeCheckWildcard("adres.plaats", filter.getPlaats(), MatchMode.START);
			dc.setProjection(Projections.property("externeOrganisatie"));
			builder.propertyIn("externeOrganisatie", dc);
		}
		builder.addIn("externeOrganisatie.soortExterneOrganisatie",
			filter.getSoortExterneOrganisaties());
		builder.addEquals("codeLeerbedrijf", filter.getCodeLeerbedrijf());
		builder.addEquals("relatienummer", filter.getRelatienummer());
		builder.addEquals("kenniscentrum", filter.getKenniscentrum());

		if (filter.getHerkomstCode() != null)
			builder.addEquals("herkomstCode", filter.getHerkomstCode());

		return criteria;
	}
}
