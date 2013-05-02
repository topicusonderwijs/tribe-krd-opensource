package nl.topicus.eduarte.krd.bron.events;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.bron.BronEvent;
import nl.topicus.eduarte.krd.bron.BronVerbintenisChanges;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.InschrijvingsgegevensRecord;

public class WijzigingInschrijvingsgegevensED implements BronEvent<InschrijvingsgegevensRecord>
{
	private final BronAanleverMelding aanleverMelding;

	private final Verbintenis verbintenis;

	private final BronVerbintenisChanges changes;

	public WijzigingInschrijvingsgegevensED(BronAanleverMelding aanleverMelding,
			BronVerbintenisChanges changes)
	{
		this.aanleverMelding = aanleverMelding;
		this.changes = changes;
		this.verbintenis = changes.getEntiteit();
	}

	@Override
	public boolean isToepasselijk()
	{
		if (!verbintenis.isOpleidingBronCommuniceerbaar())
			return false;
		return verbintenis.isEducatieVerbintenis()
			&& (verbintenis.isHandmatigVersturenNaarBron() || (changes.moetNaarBron(verbintenis) && !changes
				.heeftAlleenVakWijzigingenED(verbintenis)));
	}

	@Override
	public InschrijvingsgegevensRecord createMelding()
	{
		if (!isToepasselijk())
			return null;

		aanleverMelding.setBronOnderwijssoort(BronOnderwijssoort.EDUCATIE);

		BronBveAanleverRecord record = (BronBveAanleverRecord) findOrNew();
		record.vulEdInschrijvingsgegevensRecord();

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
		return BronBveAanleverRecord.newEdInschrijvingsgegevensRecord(aanleverMelding, verbintenis);
	}
}