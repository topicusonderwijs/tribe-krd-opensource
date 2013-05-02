package nl.topicus.eduarte.krd.bron.events;

import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.krd.bron.BronEntiteitChanges;
import nl.topicus.eduarte.krd.bron.BronEvent;
import nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck;
import nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck.WijzigingToegestaanResultaat;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

public class WijzigingInschrijvingsgegevensBO implements BronEvent<InschrijvingsgegevensRecord>
{
	private final BronAanleverMelding melding;

	private final Verbintenis verbintenis;

	private final BronEntiteitChanges<Verbintenis> changes;

	public WijzigingInschrijvingsgegevensBO(BronAanleverMelding aanleverMelding,
			BronEntiteitChanges<Verbintenis> changes)
	{
		this.melding = aanleverMelding;
		this.changes = changes;
		this.verbintenis = changes.getEntiteit();
	}

	@Override
	public boolean isToepasselijk()
	{
		if (!verbintenis.isOpleidingBronCommuniceerbaar())
			return false;
		return verbintenis.isBOVerbintenis()
			&& (verbintenis.isHandmatigVersturenNaarBron() || (changes.moetNaarBron(verbintenis) && !changes
				.heeftAlleenBekostigingsWijzigingen(verbintenis)));
	}

	@Override
	public InschrijvingsgegevensRecord createMelding() throws BronException
	{
		if (!isToepasselijk())
			return null;

		melding.setBronOnderwijssoort(BronOnderwijssoort.BEROEPSONDERWIJS);

		BronBveAanleverRecord record = findOrCreate();

		record.vulBoInschrijfgegevens();

		if (melding.getIngangsDatum() == null)
		{
			melding.setIngangsDatum(TimeUtil.max(TimeUtil.vandaag(), verbintenis.getBegindatum()));
		}

		WijzigingToegestaanResultaat wijzigingToegestaan = controleerOfWijzigingToegestaanIs();

		// enkel de soort mutatie zetten als deze nog niet gezet is.
		if (record.getSoortMutatie() == null)
		{
			record.setSoortMutatie(changes.getBveSoortMutatie(verbintenis));
		}
		else if (record.getSoortMutatie() == Aanpassing
			&& changes.getBveSoortMutatie(verbintenis) == Verwijdering)
		{
			record.setSoortMutatie(Verwijdering);
		}
		else if (record.getSoortMutatie() == Verwijdering
			&& changes.getBveSoortMutatie(verbintenis) != Verwijdering)
		{
			throw new BronException(
				"Verbintenis wordt verwijderd uit BRON, maar er wordt nog een mutatie op doorgevoerd: "
					+ melding.getReden());
		}
		else
		{
			verwijderMeldingAlsToevoegingOngedaanGemaaktWordt(record);
		}

		melding.setBekostigingsRelevant(melding.isBekostigingsRelevant()
			|| wijzigingToegestaan.isAccountantsmutatie());
		return record;
	}

	private BronBveAanleverRecord findOrCreate()
	{
		BronBveAanleverRecord record =
			(BronBveAanleverRecord) melding.getRecord(InschrijvingsgegevensRecord.class);
		if (record != null)
			return record;
		return (BronBveAanleverRecord) BronBveAanleverRecord.newBoInschrijfgegevens(melding,
			verbintenis);
	}

	private WijzigingToegestaanResultaat controleerOfWijzigingToegestaanIs() throws BronException
	{
		Date oudeBegindatum = getOude("begindatum", verbintenis.getBegindatum());
		Date oudeEinddatum = getOude("einddatum", verbintenis.getEinddatum());
		Bekostigd oudeBekostigd = getOude("bekostigd", verbintenis.getBekostigd());
		Intensiteit oudeIntensiteit = getOude("intensiteit", verbintenis.getIntensiteit());
		VerbintenisStatus oudeStatus = getOude("status", verbintenis.getStatus());
		Opleiding oudeOpleiding = getOude("opleiding", verbintenis.getOpleiding());

		BronVerbintenisWijzigingToegestaanCheck check =
			new BronVerbintenisWijzigingToegestaanCheck(oudeBegindatum, oudeEinddatum, oudeStatus,
				oudeOpleiding, oudeIntensiteit, oudeBekostigd, verbintenis);

		WijzigingToegestaanResultaat resultaat = check.getResultaat();
		if (!resultaat.isToegestaan())
			throw new BronException(resultaat.getReden());
		return resultaat;
	}

	private <T> T getOude(String kenmerk, T bestaandeWaarde)
	{
		if (changes.heeftWijziging(verbintenis, kenmerk))
			return changes.<T> getPreviousValue(verbintenis, kenmerk);
		else
			return bestaandeWaarde;
	}

	private void verwijderMeldingAlsToevoegingOngedaanGemaaktWordt(
			InschrijvingsgegevensRecord record)
	{
		boolean oorspronkelijkeMutatieIsToevoeging = Toevoeging == record.getSoortMutatie();
		boolean nieuweMutatieIsVerwijdering =
			Verwijdering == changes.getBveSoortMutatie(verbintenis);
		if (oorspronkelijkeMutatieIsToevoeging && nieuweMutatieIsVerwijdering)
		{
			melding.delete();
			melding.setVerwijderd(true);
		}
	}
}
