package nl.topicus.eduarte.krd.bron.events;

import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.Examenstatus;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.bron.BronEntiteitChanges;
import nl.topicus.eduarte.krd.bron.BronEvent;
import nl.topicus.eduarte.krd.bron.BronExamendeelnameWijzigingToegestaanCheck;
import nl.topicus.eduarte.krd.bron.BronExamendeelnameWijzigingToegestaanCheck.WijzigingToegestaanResultaat;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.ExamengegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie;

public class WijzigingExamengegevensBO implements BronEvent<ExamengegevensRecord>
{
	private Examendeelname deelname;

	private BronAanleverMelding melding;

	private Verbintenis verbintenis;

	private BronEntiteitChanges<Verbintenis> changes;

	public WijzigingExamengegevensBO(BronAanleverMelding aanlevermelding,
			BronEntiteitChanges<Verbintenis> changes, Examendeelname deelname)
	{
		this.melding = aanlevermelding;
		this.changes = changes;
		this.deelname = deelname;
		this.verbintenis = deelname.getVerbintenis();
	}

	@Override
	public boolean isToepasselijk()
	{
		if (!deelname.getVerbintenis().isOpleidingBronCommuniceerbaar())
			return false;
		if (deelname.isBO() && (deelname.getExamenstatus().isGeslaagd() || isCorrectie()))
			return true;
		if (deelname.getVerbintenis().isLNV() && deelname.getExamenstatus().isCertificaten())
			return true;
		return false;
	}

	private boolean isCorrectie()
	{
		if (changes.heeftWijziging(deelname, "examenstatus"))
		{
			Examenstatus vorigeStatus = changes.getPreviousValue(deelname, "examenstatus");
			if (vorigeStatus != null)
				return vorigeStatus.isGeslaagd();
		}
		return false;
	}

	@Override
	public ExamengegevensRecord createMelding() throws BronException
	{
		if (!isToepasselijk())
			return null;

		boolean totaalExamenSturen = (deelname.getExamenstatus().isGeslaagd() || isCorrectie());
		boolean alleenDeelkwalificatiesSturen = false;
		if (!totaalExamenSturen)
		{
			if (deelname.getVerbintenis().isLNV() && deelname.getExamenstatus().isCertificaten())
			{
				alleenDeelkwalificatiesSturen =
					deelname.getVerbintenis().heeftBehaaldeDeelkwalificaties();
			}
		}
		if (!totaalExamenSturen && !alleenDeelkwalificatiesSturen)
		{
			return null;
		}

		melding.setBronOnderwijssoort(BronOnderwijssoort.BEROEPSONDERWIJS);
		if (melding.getIngangsDatum() == null)
		{
			melding.setIngangsDatum(TimeUtil.vandaag());
		}

		BronBveAanleverRecord record;
		if (alleenDeelkwalificatiesSturen)
		{
			BronBveAanleverRecord.vulBehaaldeDeelkwalificaties(melding, deelname);
			return null;
		}
		else
		{
			record = (BronBveAanleverRecord) findOrNew(verbintenis.getExterneCode());
			SoortMutatie mutatie = changes.getBveSoortMutatie(deelname);
			if (record.getSoortMutatie() == null)
			{
				record.setSoortMutatie(mutatie);
			}
			else
			{
				switch (record.getSoortMutatie())
				{
					case Toevoeging:
						switch (mutatie)
						{
							case Toevoeging:
								break;
							case Aanpassing:
								break;
							case Verwijdering:
								record.setMelding(null);
								melding.getMeldingen().remove(record);
								if (record.isSaved())
								{
									record.delete();
								}
								break;
						}
						break;
					case Verwijdering:
						switch (mutatie)
						{
							case Toevoeging:
								record.setSoortMutatie(Aanpassing);
								break;
							case Aanpassing:
								break;
							case Verwijdering:
								break;
						}
						break;
					case Aanpassing:
						switch (mutatie)
						{
							case Toevoeging:
								record.setSoortMutatie(Toevoeging);
								break;
							case Aanpassing:
								break;
							case Verwijdering:
								record.setSoortMutatie(Verwijdering);
								break;
						}
						break;
				}
			}
			record.vulBoExamengegevens(true);

			WijzigingToegestaanResultaat wijzigingIsToegestaan = controleerOfMutatieToegestaanIs();

			if (!melding.isBekostigingsRelevant())
			{
				boolean hadPermissieNodig =
					wijzigingIsToegestaan == WijzigingToegestaanResultaat.MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop;
				melding.setBekostigingsRelevant(hadPermissieNodig);
			}
			return record;
		}
	}

	private WijzigingToegestaanResultaat controleerOfMutatieToegestaanIs() throws BronException
	{
		Date oudeDatumUitslag = getOude("datumUitslag", deelname.getDatumUitslag());
		Examenstatus oudeExamenstatus = getOude("examenstatus", deelname.getExamenstatus());
		Boolean oudeBekostigd = getOude("bekostigd", deelname.isBekostigd());

		BronExamendeelnameWijzigingToegestaanCheck check =
			new BronExamendeelnameWijzigingToegestaanCheck(oudeDatumUitslag, oudeExamenstatus,
				oudeBekostigd, deelname);
		WijzigingToegestaanResultaat resultaat = check.getResultaat();
		if (!resultaat.isToegestaan())
		{
			throw new BronException(resultaat.getReden());
		}
		return resultaat;
	}

	private <T> T getOude(String kenmerk, T bestaandeWaarde)
	{
		if (changes.heeftWijziging(deelname, kenmerk))
			return changes.<T> getPreviousValue(deelname, kenmerk);
		else
			return bestaandeWaarde;
	}

	private ExamengegevensRecord findOrNew(String behaaldeKwalificatie)
	{
		ExamengegevensRecord record = melding.getExamengegevensRecord(behaaldeKwalificatie);
		if (record != null)
			return record;
		return BronBveAanleverRecord.newBoExamengegevens(melding, verbintenis, deelname);
	}
}
