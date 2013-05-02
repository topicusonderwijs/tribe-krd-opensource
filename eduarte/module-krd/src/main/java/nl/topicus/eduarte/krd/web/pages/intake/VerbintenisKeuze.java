package nl.topicus.eduarte.krd.web.pages.intake;

import java.io.Serializable;

import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;

public class VerbintenisKeuze implements Serializable
{
	private static final long serialVersionUID = 1L;

	private boolean intakeVerbintenis;

	private Serializable id;

	public VerbintenisKeuze()
	{
		intakeVerbintenis = false;
		id = null;
	}

	public VerbintenisKeuze(Verbintenis intake)
	{
		intakeVerbintenis = true;
		id = intake.getCurrentId();
	}

	public Verbintenis getVerbintenis(IntakeWizardModel model)
	{
		Deelnemer deelnemer = model.getDeelnemer();
		Serializable searchId = intakeVerbintenis ? id : model.getStap4idVanNieuweVerbintenis();
		for (Verbintenis curVerbintenis : deelnemer.getVerbintenissen())
			if (curVerbintenis.getCurrentId().equals(searchId))
				return curVerbintenis;
		return null;
	}

	public String toString(IntakeWizardModel model)
	{
		if (intakeVerbintenis)
		{
			String date = "";
			for (Intakegesprek ig : getVerbintenis(model).getIntakegesprekken())
			{
				if (ig.getDatumTijd() != null)
					date = " (" + getVerbintenis(model).getIntakegesprekkenDatumTijd() + ")";
			}
			return "Intakegesprek " + getVerbintenis(model).getIntakegesprekkenOrganisatieEenheid()
				+ date;
		}
		else
		{
			return "Ja";
		}
	}

	public String getIdValue()
	{
		return Boolean.toString(intakeVerbintenis) + ":" + id;
	}

	public boolean isIntakeVerbintenis()
	{
		return intakeVerbintenis;
	}

	@Override
	public int hashCode()
	{
		if (!intakeVerbintenis)
			return 0;
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof VerbintenisKeuze)
		{
			VerbintenisKeuze other = (VerbintenisKeuze) obj;
			if (intakeVerbintenis != other.intakeVerbintenis)
				return false;
			return !intakeVerbintenis || other.id.equals(id);
		}
		return false;
	}
}
