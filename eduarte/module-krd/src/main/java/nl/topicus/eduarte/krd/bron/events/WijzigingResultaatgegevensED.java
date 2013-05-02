package nl.topicus.eduarte.krd.bron.events;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.bron.BronEntiteitChanges;
import nl.topicus.eduarte.krd.bron.BronEvent;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.ResultaatgegevensRecord;

public class WijzigingResultaatgegevensED implements BronEvent<ResultaatgegevensRecord>
{
	private Examendeelname deelname;

	private BronAanleverMelding melding;

	public BronEntiteitChanges<Verbintenis> changes;

	public WijzigingResultaatgegevensED(BronAanleverMelding aanlevermelding,
			BronEntiteitChanges<Verbintenis> changes, Examendeelname deelname)
	{
		this.melding = aanlevermelding;
		this.changes = changes;
		this.deelname = deelname;
	}

	@Override
	public boolean isToepasselijk()
	{
		if (!deelname.getVerbintenis().isOpleidingBronCommuniceerbaar())
			return false;
		return deelname.isEducatie();
	}

	@Override
	public ResultaatgegevensRecord createMelding()
	{
		if (!isToepasselijk())
			return null;

		melding.setBronOnderwijssoort(BronOnderwijssoort.EDUCATIE);
		if (melding.getIngangsDatum() == null)
		{
			melding.setIngangsDatum(TimeUtil.max(TimeUtil.vandaag(), deelname.getVerbintenis()
				.getBegindatum()));
		}

		ResultaatgegevensRecord record = findOrNew();

		if (record.getSoortMutatie() == null)
		{
			record.setSoortMutatie(changes.getBveSoortMutatie(deelname));
		}
		record.setVoltooideOpleiding(deelname.getVerbintenis().getExterneCode());
		record.setDatumVoltooid(deelname.getDatumUitslag());

		return record;
	}

	private ResultaatgegevensRecord findOrNew()
	{
		Verbintenis verbintenis = deelname.getVerbintenis();

		ResultaatgegevensRecord record =
			melding.getResultaatgegevensRecordED(verbintenis.getExterneCode());
		if (record != null)
			return record;
		return BronBveAanleverRecord.newEdResultaatgegevensRecord(melding, verbintenis, deelname);
	}
}
