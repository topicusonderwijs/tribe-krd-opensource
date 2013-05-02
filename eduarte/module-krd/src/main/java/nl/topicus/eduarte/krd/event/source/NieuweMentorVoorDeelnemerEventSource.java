package nl.topicus.eduarte.krd.event.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.IModificationCallback;
import nl.topicus.cobra.velocity.VelocityHelper;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.signalering.EventAbonnementType;
import nl.topicus.eduarte.app.signalering.EventReceiver;
import nl.topicus.eduarte.app.signalering.EventSource;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.signalering.settings.EventAbonnementSetting;
import nl.topicus.eduarte.krd.entities.NieuweMentorVoorDeelnemerEvent;

import org.apache.velocity.VelocityContext;

public class NieuweMentorVoorDeelnemerEventSource implements
		EventSource<NieuweMentorVoorDeelnemerEvent>, IModificationCallback
{
	private Groep groep;

	private List<Deelnemer> deelnemers = new ArrayList<Deelnemer>();

	public NieuweMentorVoorDeelnemerEventSource(Groep groep)
	{
		this.groep = groep;
	}

	@Override
	public NieuweMentorVoorDeelnemerEvent createEvent()
	{
		NieuweMentorVoorDeelnemerEvent ret = new NieuweMentorVoorDeelnemerEvent();
		ret.setGroepId(groep.getId());

		ret.setOnderwerp(createOnderwerp());
		VelocityContext context = new VelocityContext();
		context.put("deelnemers", deelnemers);
		context.put("groep", groep);
		ret.setOmschrijving(VelocityHelper.generateMessage(
			"NieuwDeelnemerVoorMentorMailTemplate.vm", context));
		return ret;
	}

	@Override
	public Map<EventAbonnementType, List< ? extends EventReceiver>> getReceivers()
	{

		if (EduArteApp.get().isModuleActive(EduArteModuleKey.DEELNEMER_BEGELEIDING))
			return DataAccessRegistry.getHelper(GroepDataAccessHelper.class).getEventReceivers(
				groep);
		else
			return new HashMap<EventAbonnementType, List< ? extends EventReceiver>>();
	}

	@Override
	public void delete(IdObject object, Class< ? extends IdObject> clazz)
	{
	}

	@Override
	public void saveOrUpdate(IdObject object, Class< ? extends IdObject> clazz)
	{
		if (groep.isSaved() && !object.isSaved() && clazz.equals(Groepsdeelname.class))
		{
			deelnemers.add(((Groepsdeelname) object).getDeelnemer());
		}

	}

	public boolean deelnemerToegevoegd()
	{
		return deelnemers.size() > 0;
	}

	public String createOnderwerp()
	{
		String onderwerp = "";
		onderwerp += deelnemers.size();
		onderwerp += " nieuwe deelnemer";
		onderwerp += (deelnemers.size() > 1 ? "s" : "");
		onderwerp += " voor groep " + groep.getNaam() + ": ";

		int max = 4;

		for (int i = 0; i < deelnemers.size(); i++)
		{
			if (i > 0)
			{
				if (i == deelnemers.size() - 1)
				{
					onderwerp += " en ";
				}
				else
				{
					if (i == max - 1)
					{
						onderwerp += " en nog " + (deelnemers.size() - i) + " anderen.";
						break;
					}
					else
					{
						onderwerp += ", ";
					}
				}
			}
			onderwerp += deelnemers.get(i).getDeelnemernummer();
		}
		return onderwerp;
	}

	@Override
	public boolean isEventEnabledVoorReceiver(EventReceiver actualReceiver,
			EventAbonnementSetting eventAbonnementSetting)
	{
		return true;
	}
}
