package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.app.signalering.EventTransport;
import nl.topicus.eduarte.dao.helpers.EventAbonnementSettingDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.entities.signalering.settings.EventAbonnementSetting;
import nl.topicus.eduarte.entities.signalering.settings.GlobaalAbonnementSetting;
import nl.topicus.eduarte.entities.signalering.settings.MedewerkerDeelnemerAbonnering;
import nl.topicus.eduarte.entities.signalering.settings.MedewerkerGroepAbonnering;
import nl.topicus.eduarte.entities.signalering.settings.PersoonlijkAbonnementSetting;

import org.hibernate.Criteria;

public class EventAbonnementSettingHibernateDataAccessHelper extends
		HibernateDataAccessHelper<EventAbonnementSetting> implements
		EventAbonnementSettingDataAccessHelper
{

	public EventAbonnementSettingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<GlobaalAbonnementSetting> getDefaultSettings(Class< ? extends Event> eventClass,
			Class< ? extends EventTransport> transportClass)
	{
		Criteria criteria = createCriteria(GlobaalAbonnementSetting.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("eventClassname", eventClass.getName());
		builder.addEquals("transportClassname", transportClass.getName());
		return cachedList(criteria);
	}

	@Override
	public List<PersoonlijkAbonnementSetting> getSettings(Class< ? extends Event> eventClass,
			Class< ? extends EventTransport> transportClass)
	{
		Criteria criteria = createCriteria(PersoonlijkAbonnementSetting.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("eventClassname", eventClass.getName());
		builder.addEquals("transportClassname", transportClass.getName());
		return cachedList(criteria);
	}

	@Override
	public List<MedewerkerDeelnemerAbonnering> getDeelnemerAbonnementen(Medewerker medewerker)
	{
		Criteria criteria = createCriteria(MedewerkerDeelnemerAbonnering.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("medewerker", medewerker);
		return cachedList(criteria);
	}

	@Override
	public List<MedewerkerGroepAbonnering> getGroepAbonnementen(Medewerker medewerker)
	{
		Criteria criteria = createCriteria(MedewerkerGroepAbonnering.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("medewerker", medewerker);
		return cachedList(criteria);
	}
}
