package nl.topicus.eduarte.krd.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.krd.dao.helpers.BronFotobestandVerschilDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestandVerschil;
import nl.topicus.eduarte.krd.zoekfilters.BronFotobestandVerschilZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;

public class BronFotobestandVerschilHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<BronFotobestandVerschil, BronFotobestandVerschilZoekFilter>
		implements BronFotobestandVerschilDataAccessHelper
{

	public BronFotobestandVerschilHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BronFotobestandVerschilZoekFilter filter)
	{
		Criteria criteria = createCriteria(BronFotobestandVerschil.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("fotoRecord", "fotoRecord", JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("deelnemer", "deelnemer", JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("deelnemer.persoon", "persoon", JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("verbintenis", "verbintenis", JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("verbintenis.opleiding", "opleiding", JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("opleiding.verbintenisgebied", "verbintenisgebied",
			JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("verbintenis.organisatieEenheid", "organisatieEenheid",
			JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("verbintenis.locatie", "locatie", JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("examendeelname", "examendeelname", JoinFragment.LEFT_OUTER_JOIN);

		if (!filter.addOrganisatieEenheidLocatieCriteria("verbintenis.", criteria))
			return null;
		builder.addLessOrEquals("verbintenis.begindatum", filter.getPeildatum());
		builder.addGreaterOrEquals("verbintenis.einddatumNotNull", filter.getPeildatum());
		builder.addEquals("fotoRecord.teldatum", filter.getTeldatum());
		builder.addEquals("bestand", filter.getFotobestand());
		builder.addEquals("verschil", filter.getVerschil());
		builder.addILikeCheckWildcard("persoon.achternaam", filter.getAchternaam(),
			MatchMode.ANYWHERE);
		if (filter.getPgn() != null)
		{
			builder.addOrs(Restrictions.eq("persoon.bsn", filter.getPgn()),
				Restrictions.eq("deelnemer.onderwijsnummer", filter.getPgn()));
		}

		return criteria;
	}

	@Override
	public List<BronFotobestandVerschil> getVerschillenList(BronFotobestand bronFotobestand)
	{
		Asserts.assertNotEmpty("bronfotobestand", bronFotobestand);
		Criteria criteria = createCriteria(BronFotobestandVerschil.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("bestand", bronFotobestand);
		return cachedList(criteria);
	}

}
