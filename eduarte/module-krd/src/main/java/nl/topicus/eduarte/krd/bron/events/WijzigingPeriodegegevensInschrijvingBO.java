package nl.topicus.eduarte.krd.bron.events;

import static nl.topicus.cobra.util.TimeUtil.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd.*;

import java.util.Date;

import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
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
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie;

public class WijzigingPeriodegegevensInschrijvingBO implements
		BronEvent<PeriodegegevensInschrijvingRecord>
{
	private final BronAanleverMelding aanlevermelding;

	private final Bekostigingsperiode periode;

	private final BronEntiteitChanges<Verbintenis> changes;

	private final Verbintenis verbintenis;

	public WijzigingPeriodegegevensInschrijvingBO(BronAanleverMelding aanlevermelding,
			BronEntiteitChanges<Verbintenis> changes)
	{
		this.aanlevermelding = aanlevermelding;
		this.changes = changes;
		this.verbintenis = changes.getEntiteit();
		this.periode = null;
	}

	public WijzigingPeriodegegevensInschrijvingBO(BronAanleverMelding melding,
			BronEntiteitChanges<Verbintenis> changes, Bekostigingsperiode periode)
	{
		this.aanlevermelding = melding;
		this.changes = changes;
		this.verbintenis = changes.getEntiteit();
		this.periode = periode;
	}

	@Override
	public boolean isToepasselijk()
	{
		if (!verbintenis.isOpleidingBronCommuniceerbaar())
			return false;
		// Als er naast bekostigswijzigingen in de Verbintenis zelf ook wijzigingen aan
		// individuele Bekostigingsperiodes zijn, dan voor de wijzigingen in de
		// Verbintenis zelf geen melding maken.
		boolean heeftOokBekostigingsperiodeWijzigingen =
			(periode == null && changes.heeftWijzigingenVoor(Bekostigingsperiode.class));

		boolean verbintenisIsNieuw = verbintenisIsNieuwVoorBron();
		boolean handmatig = (periode != null) && periode.isHandmatigVersturenNaarBron();
		boolean periodeIsNieuw = periodeIsNieuwVoorBron();
		boolean nieuweBekostiginggegevens =
			verbintenisIsNieuw || handmatig || periodeIsNieuw || changes.isGewijzigd(periode)
				|| changes.heeftBekostigingsWijzigingen(verbintenis);
		return verbintenis.isBOVerbintenis() && verbintenis.isBronCommuniceerbaar()
			&& nieuweBekostiginggegevens && !heeftOokBekostigingsperiodeWijzigingen;
	}

	private boolean periodeIsNieuwVoorBron()
	{
		boolean periodeIsNieuw =
			(changes.heeftBekostigingsWijzigingen(verbintenis) && periode == null)
				|| (periode != null && changes.isNieuwVoorBron(periode));
		return periodeIsNieuw;
	}

	private boolean verbintenisIsNieuwVoorBron()
	{
		boolean verbintenisIsNieuw =
			changes.getBveSoortMutatie(verbintenis) == SoortMutatie.Toevoeging;
		return verbintenisIsNieuw;
	}

	@Override
	public PeriodegegevensInschrijvingRecord createMelding() throws BronException
	{
		if (!isToepasselijk())
			return null;

		aanlevermelding.setBronOnderwijssoort(BronOnderwijssoort.BEROEPSONDERWIJS);

		if (verbintenis.getBekostigd() != Gedeeltelijk)
			verwerkGeheelBekostigdeVerbintenisWijzigingen();
		else
			verwerkGedeeltelijkBekostigdeVerbintenisWijzigingen();

		WijzigingToegestaanResultaat wijzigingToegestaan = controleerOfWijzigingToegestaanIs();

		if (!aanlevermelding.isBekostigingsRelevant())
		{
			aanlevermelding.setBekostigingsRelevant(wijzigingToegestaan.isAccountantsmutatie());
		}
		return null;
	}

	private void verwerkGeheelBekostigdeVerbintenisWijzigingen()
	{
		if (changes.heeftWijziging(verbintenis, "begindatum"))
		{
			verwerkWijzigingVerbintenisBegindatum();
		}
		else
		{
			verwerkWijzigingVerbintenisBekostiging();
		}
	}

	private void verwerkWijzigingVerbintenisBegindatum()
	{
		Date oudeIngangsdatum = changes.getPreviousValue(verbintenis, "begindatum");
		Date nieuweIngangsdatum = verbintenis.getBegindatum();

		verwerkWijzigingIngangsdatum(oudeIngangsdatum, nieuweIngangsdatum);
	}

	private void verwerkWijzigingIngangsdatum(Date oudeIngangsdatum, Date nieuweIngangsdatum)
	{
		verwijderPeriodegegeven(oudeIngangsdatum);
		voegtoePeriodegegeven(nieuweIngangsdatum);
	}

	private void verwijderPeriodegegeven(Date ingangsdatum)
	{
		// ingangsdatum is null wanneer de verbintenis en/of periode nieuw is, er valt dan
		// niets te verwijderen.
		if (ingangsdatum == null || verbintenisIsNieuwVoorBron())
			return;

		BronBveAanleverRecord oudeRecord = findOrCreate(ingangsdatum);
		oudeRecord.vulBoPeriodegegevens();
		oudeRecord.setDatumIngangPeriodegegevensInschrijving(ingangsdatum);

		if (oudeRecord.getSoortMutatie() != SoortMutatie.Toevoeging)
		{
			oudeRecord.setSoortMutatie(SoortMutatie.Verwijdering);
		}
		else
		{
			if (oudeRecord.isSaved())
				oudeRecord.delete();
			aanlevermelding.getMeldingen().remove(oudeRecord);
		}
	}

	private void voegtoePeriodegegeven(Date ingangsdatum)
	{
		// ingangsdatum is null wanneer de verbintenis en/of periode verwijderd is, er
		// valt dan niets toe te voegen
		if (ingangsdatum == null)
			return;

		BronBveAanleverRecord nieuweRecord = findOrCreate(ingangsdatum);
		nieuweRecord.vulBoPeriodegegevens();
		nieuweRecord.setDatumIngangPeriodegegevensInschrijving(ingangsdatum);
		if (nieuweRecord.getSoortMutatie() == SoortMutatie.Verwijdering)
		{
			nieuweRecord.setSoortMutatie(SoortMutatie.Aanpassing);
		}
		else if (nieuweRecord.getSoortMutatie() != SoortMutatie.Aanpassing)
		{
			nieuweRecord.setSoortMutatie(SoortMutatie.Toevoeging);
		}
		// repareer voor het geval Verbintenis gaat van Bekostigd.Ja/Nee -> Gedeeltelijk
		if (ingangsdatum.equals(verbintenis.getBegindatum())
			&& !changes.heeftWijziging(verbintenis, "begindatum"))
			nieuweRecord.setSoortMutatie(SoortMutatie.Aanpassing);
	}

	private void verwerkWijzigingVerbintenisBekostiging()
	{
		BronBveAanleverRecord nieuweRecord = findOrCreate(verbintenis.getBegindatum());
		nieuweRecord.vulBoPeriodegegevens();
		nieuweRecord.setDatumIngangPeriodegegevensInschrijving(verbintenis.getBegindatum());
		if (nieuweRecord.getSoortMutatie() == null)
		{
			nieuweRecord.setSoortMutatie(verbintenisIsNieuwVoorBron() ? SoortMutatie.Toevoeging
				: SoortMutatie.Aanpassing);
		}
	}

	private void verwerkGedeeltelijkBekostigdeVerbintenisWijzigingen()
	{
		if (periode != null)
		{
			if (changes.heeftWijziging(verbintenis, "bekostigd")
				&& periode.getBegindatum().equals(verbintenis.getBegindatum())
				&& !changes.heeftWijziging(verbintenis, "begindatum")
				&& !changes.heeftWijziging(periode, "bekostigd"))
				return;

			if (changes.heeftWijziging(periode, "begindatum"))
			{
				Date oudeBegindatum = changes.getPreviousValue(periode, "begindatum");

				Date nieuweBegindatum = changes.getCurrentValue(periode, "begindatum");
				verwerkWijzigingIngangsdatum(oudeBegindatum, nieuweBegindatum);
				return;
			}
		}

		BronBveAanleverRecord record = (BronBveAanleverRecord) findOrCreate();

		record.vulBoPeriodegegevens();

		if (aanlevermelding.getIngangsDatum() == null)
		{
			aanlevermelding.setIngangsDatum(max(vandaag(), verbintenis.getBegindatum()));
		}

		// Alleen de mutatiesoort aanpassen als deze nog niet gezet was.
		if (record.getSoortMutatie() == null)
		{
			if (periode != null && periode.isHandmatigVersturenNaarBron())
			{
				record.setSoortMutatie(periode.getHandmatigeBronBveSoortMutatie());
			}
			else if (!verbintenisIsNieuwVoorBron() && periode == null)
			{
				record.setSoortMutatie(SoortMutatie.Aanpassing);
			}
			else if (verbintenisIsNieuwVoorBron())
			{
				record.setSoortMutatie(SoortMutatie.Toevoeging);
			}
			else if (periodeIsNieuwVoorBron())
			{
				record.setSoortMutatie(SoortMutatie.Toevoeging);
			}
			else
			{
				record.setSoortMutatie(SoortMutatie.Aanpassing);
			}
		}
	}

	private WijzigingToegestaanResultaat controleerOfWijzigingToegestaanIs() throws BronException
	{
		Date oudeBegindatum = getOude("begindatum", verbintenis.getBegindatum());
		Date oudeEinddatum = getOude("einddatum", verbintenis.getEinddatum());
		Bekostigd oudeBekostigd = getOude("bekostigd", verbintenis.getBekostigd());
		Intensiteit oudeIntensiteit = getOude("intensiteit", verbintenis.getIntensiteit());
		VerbintenisStatus oudeStatus = getOude("status", verbintenis.getStatus());
		Opleiding oudeOpleiding = getOude("opleiding", verbintenis.getOpleiding());

		BronVerbintenisWijzigingToegestaanCheck check = null;

		if (periode != null)
		{
			Bekostigingsperiode oudePeriode = new Bekostigingsperiode();
			oudePeriode.setBegindatum(getOudePeriode("begindatum", periode.getBegindatum()));
			oudePeriode.setEinddatum(getOudePeriode("einddatum", periode.getEinddatum()));
			Boolean oudePeriodeBekostigd = getOudePeriode("bekostigd", periode.isBekostigd());
			oudePeriode.setBekostigd(oudePeriodeBekostigd != null && oudePeriodeBekostigd);

			check =
				new BronVerbintenisWijzigingToegestaanCheck(oudeBekostigd, oudeIntensiteit,
					oudePeriode, periode);
		}
		else
		{
			check =
				new BronVerbintenisWijzigingToegestaanCheck(oudeBegindatum, oudeEinddatum,
					oudeStatus, oudeOpleiding, oudeIntensiteit, oudeBekostigd, verbintenis);
		}

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

	private <T> T getOudePeriode(String kenmerk, T bestaandeWaarde)
	{
		if (changes.heeftWijziging(periode, kenmerk))
			return changes.<T> getPreviousValue(periode, kenmerk);
		else
			return bestaandeWaarde;
	}

	private PeriodegegevensInschrijvingRecord findOrCreate()
	{
		Date ingangsdatum = getIngangsdatum();
		return findOrCreate(ingangsdatum);
	}

	private BronBveAanleverRecord findOrCreate(Date ingangsdatum)
	{
		BronBveAanleverRecord record =
			(BronBveAanleverRecord) aanlevermelding.getPeriodeRecord(ingangsdatum);
		if (record != null)
			return record;
		return (BronBveAanleverRecord) BronBveAanleverRecord.newBoPeriodegegevens(aanlevermelding,
			verbintenis, periode);
	}

	private Date getIngangsdatum()
	{
		return periode == null ? verbintenis.getBegindatum() : periode.getBegindatum();
	}
}