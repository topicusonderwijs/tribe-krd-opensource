package nl.topicus.eduarte.krd.bron;

import java.util.List;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;

public class BronDeelnemerChanges extends BronEntiteitChanges<Deelnemer>
{
	private final Deelnemer deelnemer;

	public BronDeelnemerChanges(Deelnemer deelnemer, List<BronStateChange> changes)
	{
		super(deelnemer, changes);
		this.deelnemer = deelnemer;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public boolean isDeelnemerNieuwVoorBron()
	{
		for (Verbintenis verbintenis : deelnemer.getVerbintenissen())
		{
			if (!isNieuwVoorBron(verbintenis))
				return false;
		}
		return true;
	}
}
