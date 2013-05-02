package nl.topicus.eduarte.krd.bron;

import java.util.List;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;

public class BronVerbintenisChanges extends BronEntiteitChanges<Verbintenis>
{
	private final Verbintenis verbintenis;

	public BronVerbintenisChanges(Verbintenis verbintenis, List<BronStateChange> changes)
	{
		super(verbintenis, changes);
		this.verbintenis = verbintenis;
	}

	public Deelnemer getDeelnemer()
	{
		return verbintenis.getDeelnemer();
	}
}
