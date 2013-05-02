package nl.topicus.eduarte.krd.bron.events;

import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.bron.BronBpvWijzigingToegestaanCheck;
import nl.topicus.eduarte.krd.bron.BronEntiteitChanges;
import nl.topicus.eduarte.krd.bron.BronEvent;
import nl.topicus.eduarte.krd.bron.BronBpvWijzigingToegestaanCheck.WijzigingToegestaanResultaat;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord;

public class WijzigingBpvgegevensInschrijvingBO implements BronEvent<BpvGegevensRecord>
{
	private final BronAanleverMelding melding;

	private final BPVInschrijving bpv;

	private final BronEntiteitChanges<Verbintenis> changes;

	public WijzigingBpvgegevensInschrijvingBO(BronAanleverMelding aanleverMelding,
			BronEntiteitChanges<Verbintenis> changes, BPVInschrijving inschrijving)
	{
		this.melding = aanleverMelding;
		this.changes = changes;
		this.bpv = inschrijving;
	}

	@Override
	public boolean isToepasselijk()
	{
		if (!bpv.isOpnemenInBron())
		{
			return false;
		}

		// Is uberhaupt alleen van toepassing als het om een BO verbintenis gaat.
		// Daarnaast alleen van toepassing als er een handmatige BRON melding verstuurd
		// moet worden, of als de bijbehorende verbintenis naar BRON moet (en
		// communiceerbaar is).
		boolean isBoVerbintenis = bpv.getVerbintenis().isBOVerbintenis();
		if (!isBoVerbintenis)
			return false;

		boolean isHandmatig = bpv.isHandmatigVersturenNaarBron();
		if (isHandmatig)
			return true;

		boolean verbintenisIsBronCommuniceerbaar = bpv.getVerbintenis().isBronCommuniceerbaar();
		boolean bpvMoetNaarBron = changes.moetNaarBron(bpv);
		return bpvMoetNaarBron && verbintenisIsBronCommuniceerbaar;
	}

	@Override
	public BpvGegevensRecord createMelding() throws BronException
	{
		if (!isToepasselijk())
		{
			return null;
		}

		BronBveAanleverRecord record = (BronBveAanleverRecord) findOrNew();

		melding.setBronOnderwijssoort(BronOnderwijssoort.BEROEPSONDERWIJS);

		if (record.getSoortMutatie() == null)
		{
			record.setSoortMutatie(changes.getBveSoortMutatie(bpv));
		}
		else if (record.getSoortMutatie() == Verwijdering
			&& changes.getBveSoortMutatie(bpv) != Verwijdering)
		{
			record.setSoortMutatie(Aanpassing);
		}
		else
		{
			verwijderMeldingAlsToevoegingOngedaanGemaaktWordt(record);
		}

		record.vulBoBpvgegevensInschrijving();

		WijzigingToegestaanResultaat wijzigingIsToegestaan = controleerOfWijzigingToegestaanIs();

		if (melding.getIngangsDatum() == null)
		{
			melding.setIngangsDatum(TimeUtil.vandaag());
		}

		if (!melding.isBekostigingsRelevant())
		{
			melding.setBekostigingsRelevant(wijzigingIsToegestaan.isAccountantsmutatie());
		}
		return record;
	}

	private WijzigingToegestaanResultaat controleerOfWijzigingToegestaanIs() throws BronException
	{
		Date oudeBegindatum = getOude("begindatum", bpv.getBegindatum());
		Date oudeAfsluitdatum = getOude("afsluitdatum", bpv.getAfsluitdatum());
		BPVStatus oudeBpvStatus = getOude("status", bpv.getStatus());
		BPVBedrijfsgegeven oudeBedrijfsgegeven =
			getOude("bedrijfsgegeven", bpv.getBedrijfsgegeven());
		Long oudeBedrijfsgegevenId =
			oudeBedrijfsgegeven != null ? oudeBedrijfsgegeven.getId() : null;

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(oudeBegindatum, oudeAfsluitdatum,
				oudeBedrijfsgegevenId, oudeBpvStatus, bpv, bpv.getVerbintenis());
		WijzigingToegestaanResultaat resultaat = check.getResultaat();
		if (!resultaat.isToegestaan())
		{
			throw new BronException(resultaat.getReden());
		}
		return resultaat;
	}

	private <T> T getOude(String kenmerk, T bestaandeWaarde)
	{
		if (changes.heeftWijziging(bpv, kenmerk))
			return changes.<T> getPreviousValue(bpv, kenmerk);
		else
			return bestaandeWaarde;
	}

	private BpvGegevensRecord findOrNew()
	{
		BpvGegevensRecord record = melding.getBpvRecord(bpv.getVolgnummer());
		if (record != null)
			return record;
		return BronBveAanleverRecord.newBoBpvgegevensInschrijving(melding, bpv);
	}

	private void verwijderMeldingAlsToevoegingOngedaanGemaaktWordt(BronBveAanleverRecord record)
	{
		boolean oorspronkelijkeMutatieIsToevoeging = Toevoeging == record.getSoortMutatie();
		boolean nieuweMutatieIsVerwijdering = Verwijdering == changes.getBveSoortMutatie(bpv);
		if (oorspronkelijkeMutatieIsToevoeging && nieuweMutatieIsVerwijdering)
		{
			melding.getMeldingen().remove(record);
			record.setMelding(null);
			record.delete();
		}
	}
}
