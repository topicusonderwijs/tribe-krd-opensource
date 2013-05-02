package nl.topicus.eduarte.dao.helpers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.app.signalering.EventAbonnementType;
import nl.topicus.eduarte.app.signalering.EventReceiver;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.GroepDocent;
import nl.topicus.eduarte.entities.groep.GroepMentor;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.IMentorDocentZoekFilter;

import org.hibernate.criterion.Criterion;

/**
 * @author hoeve
 */
public interface GroepDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Groep, GroepZoekFilter>
{
	/**
	 * Zoek groep met id.
	 * 
	 * @param id
	 * @return groep of null als deze niet bestaat
	 */
	public Groep get(Long id);

	/**
	 * @param code
	 * @return De Groep
	 */
	public Groep getByGroepcode(String code);

	public List<Groep> getGroepen(List<String> roostercodes, Date peildatum);

	public List<GroepDocent> getGroepenMetDocent(Medewerker docent, Date peildatum);

	public List<GroepMentor> getGroepenMetMentor(Medewerker mentor, Date peildatum);

	List<Medewerker> getMentorenVanGroep(Groep groep, Date peildatum);

	Map<EventAbonnementType, List< ? extends EventReceiver>> getEventReceivers(Groep groep);

	public List<Criterion> createMentorDocent(IMentorDocentZoekFilter< ? > filter,
			String deelnemerAlias);
}
