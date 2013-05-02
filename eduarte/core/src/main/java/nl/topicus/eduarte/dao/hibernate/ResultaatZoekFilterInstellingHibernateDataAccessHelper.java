package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.ResultaatZoekFilterInstellingDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.resultaatstructuur.GroepResultaatZoekFilterInstelling;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatZoekFilterInstelling;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.hibernate.Criteria;

public class ResultaatZoekFilterInstellingHibernateDataAccessHelper extends
		HibernateDataAccessHelper<ResultaatZoekFilterInstelling> implements
		ResultaatZoekFilterInstellingDataAccessHelper
{

	public ResultaatZoekFilterInstellingHibernateDataAccessHelper(
			HibernateSessionProvider provider, QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public void saveZoekFilter(ToetsZoekFilter filter, Medewerker medewerker, Groep groep,
			boolean vulGekoppeldAanVerbintenis)
	{
		ResultaatstructuurZoekFilter structuurFilter = filter.getResultaatstructuurFilter();
		ResultaatZoekFilterInstelling instelling = getInstelling(medewerker);
		if (instelling == null)
		{
			instelling = new ResultaatZoekFilterInstelling();
			instelling.setMedewerker(medewerker);
		}

		instelling.setCodePath(filter.getCodePath());
		instelling.setType(structuurFilter.getType());
		instelling.setCategorie(structuurFilter.getCategorie());
		if (vulGekoppeldAanVerbintenis)
			instelling
				.setGekoppeldAanVerbintenis(structuurFilter.isAlleenGekoppeldAanVerbintenis());
		instelling.saveOrUpdate();

		if (groep != null)
		{
			GroepResultaatZoekFilterInstelling groepInstelling =
				getGroepInstelling(instelling, groep);
			if (groepInstelling == null)
			{
				groepInstelling = new GroepResultaatZoekFilterInstelling();
				groepInstelling.setFilterInstelling(instelling);
				groepInstelling.setGroep(groep);
			}
			groepInstelling.setCohort(structuurFilter.getCohort());
			groepInstelling.setOnderwijsproduct(structuurFilter.getOnderwijsproduct());
			groepInstelling.saveOrUpdate();
		}
	}

	@Override
	public void vulZoekFilter(ToetsZoekFilter filter, Medewerker medewerker, Groep groep,
			boolean vulGekoppeldAanVerbintenis)
	{
		ResultaatstructuurZoekFilter structuurFilter = filter.getResultaatstructuurFilter();
		ResultaatZoekFilterInstelling instelling = getInstelling(medewerker);
		if (instelling == null)
			return;

		filter.setCodePath(instelling.getCodePath());
		structuurFilter.setType(instelling.getType());
		structuurFilter.setCategorie(instelling.getCategorie());
		if (vulGekoppeldAanVerbintenis)
			structuurFilter
				.setAlleenGekoppeldAanVerbintenis(instelling.isGekoppeldAanVerbintenis());

		if (groep != null)
		{
			GroepResultaatZoekFilterInstelling groepInstelling =
				getGroepInstelling(instelling, groep);
			if (groepInstelling == null)
				return;

			structuurFilter.setCohort(groepInstelling.getCohort());
			if (groepInstelling.getOnderwijsproduct() != null)
				structuurFilter.setOnderwijsproduct(groepInstelling.getOnderwijsproduct());
		}
	}

	private ResultaatZoekFilterInstelling getInstelling(Medewerker medewerker)
	{
		Criteria criteria = createCriteria(ResultaatZoekFilterInstelling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("medewerker", medewerker);
		return cachedTypedUnique(criteria);
	}

	private GroepResultaatZoekFilterInstelling getGroepInstelling(
			ResultaatZoekFilterInstelling instelling, Groep groep)
	{
		Criteria criteria = createCriteria(GroepResultaatZoekFilterInstelling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("filterInstelling", instelling);
		builder.addEquals("groep", groep);
		return cachedUnique(criteria);
	}
}
