package nl.topicus.eduarte.krd.entities;

import javax.persistence.Entity;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.signalering.EventAbonnementType;
import nl.topicus.eduarte.app.signalering.EventDescription;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.signalering.events.IObjectGekoppeldEvent;
import nl.topicus.eduarte.krd.event.handler.NieuweMentorVoorDeelnemerEventHandler;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@EventDescription(name = "Nieuwe groepsdeelnames", handler = NieuweMentorVoorDeelnemerEventHandler.class, abonnementTypes = {EventAbonnementType.Mentor})
@Module(EduArteModuleKey.DEELNEMER_BEGELEIDING)
public class NieuweMentorVoorDeelnemerEvent extends AbstractGroepEvent implements
		IObjectGekoppeldEvent<Groep>
{
	private static final long serialVersionUID = 1L;

	public NieuweMentorVoorDeelnemerEvent()
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public Groep getObject()
	{
		BatchDataAccessHelper<Groep> helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		return helper.get(Groep.class, getGroepId());
	}

	@Override
	public String berekenHash()
	{
		// Events van afspraken zijn altijd uniek
		return null;
	}

	@Override
	public boolean discardWhenNoReceivers()
	{
		// en omdat ze uniek zijn, heeft opslaan ook geen nut als niemand het event
		// ontvangt
		return true;
	}
}
