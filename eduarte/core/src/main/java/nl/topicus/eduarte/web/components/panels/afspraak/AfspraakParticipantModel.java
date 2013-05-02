package nl.topicus.eduarte.web.components.panels.afspraak;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.app.security.checks.GroepSecurityCheck;
import nl.topicus.eduarte.dao.participatie.helpers.PersoonlijkeGroepDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakParticipant;
import nl.topicus.eduarte.entities.participatie.PersoonlijkeGroep;
import nl.topicus.eduarte.entities.participatie.enums.UitnodigingStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

public class AfspraakParticipantModel implements IModel<IdObject>
{
	private static final long serialVersionUID = 1L;

	private AfspraakParticipantEditPanel editPanel;

	private int index;

	public AfspraakParticipantModel(AfspraakParticipantEditPanel editPanel, ListItem< ? > item)
	{
		this.editPanel = editPanel;
		this.index = item.getIndex();
	}

	@Override
	public IdObject getObject()
	{
		List<AfspraakParticipant> participanten = getParticipanten();
		if (participanten.size() <= index)
			return null;
		return participanten.get(index).getParticipantEntiteit();
	}

	@Override
	public void setObject(IdObject object)
	{
		addParticipant(object, index);
	}

	@Override
	public void detach()
	{
	}

	private Afspraak getAfspraak()
	{
		return editPanel.getAfspraak();
	}

	private List<AfspraakParticipant> getParticipanten()
	{
		return editPanel.getParticipanten();
	}

	private void addParticipant(IdObject newParticipantEntiteit, int participantIndex)
	{
		if (getAfspraak().getParticipant(newParticipantEntiteit) == null)
		{
			AfspraakParticipant newAD = new AfspraakParticipant();
			newAD.setAfspraak(getAfspraak());
			newAD.setParticipantEntiteit(newParticipantEntiteit);
			if (isToevoegenAllowed(newAD))
			{
				addSingleParticipant(participantIndex, newAD);
			}
			else if (newParticipantEntiteit instanceof Groep)
			{
				addNoiseGroepParticipanten((Groep) newParticipantEntiteit, participantIndex);
			}
			else if (newParticipantEntiteit instanceof PersoonlijkeGroep)
			{
				addPersoonlijkeGroepParticipanten((PersoonlijkeGroep) newParticipantEntiteit,
					participantIndex);
			}
		}
	}

	private void addSingleParticipant(int participantIndex, AfspraakParticipant newParticipant)
	{
		if (getParticipanten().size() > participantIndex)
			getParticipanten().set(participantIndex, newParticipant);
		else
			getParticipanten().add(newParticipant);

		if (Afspraak.isDirectePlaatsingAllowed(newParticipant)
			|| Afspraak.isDirectePlaatsingVerplicht(newParticipant, editPanel.getEditor()))
			newParticipant.setUitnodigingStatus(UitnodigingStatus.DIRECTE_PLAATSING);
		else
			newParticipant.setUitnodigingStatus(UitnodigingStatus.UITGENODIGD);
		newParticipant.resetUitnodigingVerstuurd(true);
	}

	private void addNoiseGroepParticipanten(Groep groep, int participantIndex)
	{
		if (getParticipanten().size() > participantIndex)
			getParticipanten().remove(participantIndex);

		for (Groepsdeelname curDeelnemer : groep.getDeelnemersOpPeildatum())
		{
			addParticipant(curDeelnemer.getDeelnemer(), getParticipanten().size());
		}
	}

	private void addPersoonlijkeGroepParticipanten(PersoonlijkeGroep persoonlijkeGroep,
			int participantIndex)
	{
		if (getParticipanten().size() > participantIndex)
			getParticipanten().remove(participantIndex);

		List<Deelnemer> deelnemers =
			DataAccessRegistry.getHelper(PersoonlijkeGroepDataAccessHelper.class).getDeelnemers(
				persoonlijkeGroep, getAfspraak().getBeginDatum());
		// persoonlijkeGroep.setBeginDatum(getAfspraak().getBeginDatum());
		for (Deelnemer curDeelnemer : deelnemers)
		{
			addParticipant(curDeelnemer, getParticipanten().size());
		}
	}

	private boolean isToevoegenAllowed(AfspraakParticipant participant)
	{
		if (participant.getGroep() != null)
		{
			return new GroepSecurityCheck(new DataSecurityCheck(Afspraak.DEELNEMER_WRITE),
				participant.getGroep()).isActionAuthorized(Enable.class);
		}
		else if (participant.getPersoonlijkeGroep() != null)
		{
			for (Deelnemer curDeelnemer : DataAccessRegistry.getHelper(
				PersoonlijkeGroepDataAccessHelper.class).getDeelnemers(
				participant.getPersoonlijkeGroep(), getAfspraak().getBeginDatum()))
			{
				if (!new DeelnemerSecurityCheck(new DataSecurityCheck(
					"deelnemer.participatie.agenda.write"), curDeelnemer)
					.isActionAuthorized(Enable.class))
				{
					return false;
				}
			}
		}
		return true;
	}
}
