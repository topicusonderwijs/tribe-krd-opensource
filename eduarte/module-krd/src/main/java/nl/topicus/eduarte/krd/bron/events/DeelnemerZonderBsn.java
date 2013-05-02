package nl.topicus.eduarte.krd.bron.events;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.bron.BronEntiteitChanges;
import nl.topicus.eduarte.krd.bron.BronEvent;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.PersoonsgegevensRecord;

public class DeelnemerZonderBsn implements BronEvent<PersoonsgegevensRecord>
{
	private final Deelnemer deelnemer;

	private final BronAanleverMelding melding;

	private final BronEntiteitChanges<Verbintenis> changes;

	public DeelnemerZonderBsn(Deelnemer deelnemer, BronEntiteitChanges<Verbintenis> changes,
			BronAanleverMelding aanleverMelding)
	{
		this.deelnemer = deelnemer;
		this.melding = aanleverMelding;
		this.changes = changes;
	}

	@Override
	public boolean isToepasselijk()
	{
		if (!changes.getEntiteit().isOpleidingBronCommuniceerbaar())
			return false;
		boolean heeftGeenPersoonsgebondennummer = !deelnemer.heeftPersoonsgebondennummer();
		boolean verbintenisIsNieuwVoorBron = changes.isNieuwVoorBron(changes.getEntiteit());
		return heeftGeenPersoonsgebondennummer && verbintenisIsNieuwVoorBron;
	}

	@Override
	public PersoonsgegevensRecord createMelding()
	{
		if (!isToepasselijk())
		{
			return null;
		}
		BronBveAanleverRecord record = (BronBveAanleverRecord) findOrCreate();
		record.vulPersoonsgegevensRecord();

		if (melding.getIngangsDatum() == null)
		{
			melding.setIngangsDatum(TimeUtil.vandaag());
		}
		return record;
	}

	private PersoonsgegevensRecord findOrCreate()
	{
		PersoonsgegevensRecord record = melding.getRecord(PersoonsgegevensRecord.class);
		if (record != null)
			return record;
		return BronBveAanleverRecord.newPersoonsgegevensRecord(melding);
	}
}
