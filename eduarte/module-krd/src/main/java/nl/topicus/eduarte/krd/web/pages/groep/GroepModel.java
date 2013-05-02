package nl.topicus.eduarte.krd.web.pages.groep;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.signalering.EventDispatcher;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.GroepDocent;
import nl.topicus.eduarte.entities.groep.GroepMentor;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.vrijevelden.GroepVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.entities.NieuweMentorVoorDeelnemerEvent;
import nl.topicus.eduarte.krd.event.source.NieuweMentorVoorDeelnemerEventSource;
import nl.topicus.eduarte.web.models.secure.AbstractEntiteitEditPageModel;

import org.apache.wicket.model.PropertyModel;

/**
 * Model om een nieuwe Groep aan te maken
 * 
 * @author hoeve
 */
public class GroepModel extends AbstractEntiteitEditPageModel<Groep>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creert een model op basis van informatie voor een nieuwe Groep. Dit model stelt
	 * zich in op RenderMode.EDIT.
	 */
	public GroepModel(Date registratieDatum)
	{
		super(registratieDatum);

		entiteitModel =
			ModelFactory.getCompoundChangeRecordingModel(createDefaultT(), getEntiteitManager());
	}

	/*
	 * Creert een model op basis van een bestaande Groep.
	 */
	public GroepModel(Groep groep)
	{
		super(groep.getBegindatum());

		entiteitModel = ModelFactory.getCompoundChangeRecordingModel(groep, getEntiteitManager());

	}

	@Override
	protected void setEntiteitManager()
	{
		entiteitManager =
			new DefaultModelManager(GroepDocent.class, GroepMentor.class, Plaatsing.class,
				Groepsdeelname.class, VrijVeldOptieKeuze.class, GroepVrijVeld.class, Groep.class);
	}

	@Override
	protected Groep createDefaultT()
	{
		Groep groep = new Groep();
		groep.setBegindatum(getRegistratieDatum());

		return groep;
	}

	/**
	 * @return een lijst van type {@link GroepDocent} !
	 */
	public PropertyModel<List<GroepDocent>> getGroepDocentenListModel()
	{
		return new PropertyModel<List<GroepDocent>>(getEntiteitModel(), "groepDocenten");
	}

	/**
	 * @return een lijst van type {@link GroepMentor} !
	 */
	public PropertyModel<List<GroepMentor>> getGroepMentorenListModel()
	{
		return new PropertyModel<List<GroepMentor>>(getEntiteitModel(), "groepMentoren");
	}

	/**
	 * @return een lijst van type {@link Groepsdeelname} !
	 */
	public PropertyModel<List<Groepsdeelname>> getDeelnamesListModel()
	{
		return new PropertyModel<List<Groepsdeelname>>(getEntiteitModel(), "deelnemers");
	}

	public Groep getGroep()
	{
		return getEntiteitModel().getObject();
	}

	public void addGroepMentor(GroepMentor modelObject)
	{
		Groep groep = getGroep();
		for (GroepMentor currentMentor : groep.getGroepMentoren())
			if (currentMentor.getMedewerker().equals(modelObject.getMedewerker())
				&& currentMentor.isActief(modelObject.getBegindatum()))
				return;

		// nieuwe mentor.
		modelObject.setGroep(groep);

		groep.getGroepMentoren().add(modelObject);
	}

	public void addGroepDocent(GroepDocent modelObject)
	{
		Groep groep = getGroep();
		for (GroepDocent currentDocent : groep.getGroepDocenten())
			if (currentDocent.getMedewerker().equals(modelObject.getMedewerker())
				&& currentDocent.isActief(modelObject.getBegindatum()))
				return;

		// nieuwe docent.
		modelObject.setGroep(groep);

		groep.getGroepDocenten().add(modelObject);
	}

	public void addGroepsdeelname(Groepsdeelname modelObject)
	{
		Groep groep = getGroep();
		for (Groepsdeelname currentDeelname : groep.getDeelnemers())
			if (currentDeelname.getDeelnemer().equals(modelObject.getDeelnemer())
				&& currentDeelname.isActief(modelObject.getBegindatum()))
				return;

		// nieuwe deelname.
		modelObject.setGroep(groep);

		groep.addDeelnemer(modelObject);
	}

	public void deleteGroepsdeelname(Groepsdeelname modelObject)
	{
		Groep groep = getGroep();

		if (groep.getDeelnemers().contains(modelObject))
		{
			modelObject.setGroep(null);
			groep.getDeelnemers().remove(modelObject);
		}
	}

	public void deleteGroepMentor(GroepMentor modelObject)
	{
		Groep groep = getGroep();
		if (groep.getGroepMentoren().contains(modelObject))
		{
			modelObject.setGroep(null);
			groep.getGroepMentoren().remove(modelObject);
		}
	}

	public void deleteGroepDocent(GroepDocent modelObject)
	{
		Groep groep = getGroep();

		if (groep.getGroepDocenten().contains(modelObject))
		{
			modelObject.setGroep(null);
			groep.getGroepDocenten().remove(modelObject);
		}
	}

	/**
	 * Controleert het model en sub objecten voordat alles wordt opgeslagen.
	 */
	@Override
	public void save()
	{
		NieuweMentorVoorDeelnemerEventSource eventsource =
			new NieuweMentorVoorDeelnemerEventSource(getGroep());
		getEntiteitModel().saveObject(eventsource);

		if (eventsource.deelnemerToegevoegd())
			new EventDispatcher<NieuweMentorVoorDeelnemerEvent>(eventsource)
				.dispatchEvents(EduArteContext.get().getGebruiker());

		getEntiteitModel().getObject().commit();
	}
}
