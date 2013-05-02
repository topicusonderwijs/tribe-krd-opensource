package nl.topicus.eduarte.krd.bron.events;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.bron.BronEntiteitChanges;
import nl.topicus.eduarte.krd.bron.BronEvent;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.InschrijvingsgegevensRecord;

public class WijzigingInschrijvingsgegevensVAVO implements BronEvent<InschrijvingsgegevensRecord>
{
	private final BronAanleverMelding aanleverMelding;

	private final BronEntiteitChanges<Verbintenis> changes;

	private final Verbintenis verbintenis;

	public WijzigingInschrijvingsgegevensVAVO(BronAanleverMelding melding,
			BronEntiteitChanges<Verbintenis> changes)
	{
		this.aanleverMelding = melding;
		this.changes = changes;
		this.verbintenis = changes.getEntiteit();
	}

	@Override
	public boolean isToepasselijk()
	{
		if (!verbintenis.isOpleidingBronCommuniceerbaar())
			return false;
		return verbintenis.isVAVOVerbintenis()
			&& (verbintenis.isHandmatigVersturenNaarBron() || changes.moetNaarBron(verbintenis));
	}

	@Override
	public InschrijvingsgegevensRecord createMelding()
	{
		if (!isToepasselijk())
			return null;

		aanleverMelding.setBronOnderwijssoort(BronOnderwijssoort.VAVO);

		BronBveAanleverRecord record = (BronBveAanleverRecord) findOrNew();
		record.vulVavoInschrijvingsgegevensRecord();

		if (record.getSoortMutatie() == null)
		{
			record.setSoortMutatie(changes.getBveSoortMutatie(verbintenis));
		}
		if (aanleverMelding.getIngangsDatum() == null)
		{
			aanleverMelding.setIngangsDatum(TimeUtil.max(TimeUtil.vandaag(), verbintenis
				.getBegindatum()));
		}
		return record;
	}

	private InschrijvingsgegevensRecord findOrNew()
	{
		InschrijvingsgegevensRecord record =
			aanleverMelding.getRecord(InschrijvingsgegevensRecord.class);
		if (record != null)
			return record;
		return BronBveAanleverRecord.newVavoInschrijvingsgegevensRecord(aanleverMelding,
			verbintenis);
	}
}
