package nl.topicus.eduarte.krd.bron.events;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.krd.bron.BronDeelnemerChanges;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.krd.bron.BronEvent;
import nl.topicus.eduarte.krd.bron.BronStateChange;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.WijzigingSleutelgegevensRecord;

public class WijzigingSleutelgegevens implements BronEvent<WijzigingSleutelgegevensRecord>
{
	private final BronEduArteModel model = new BronEduArteModel();

	private final BronAanleverMelding melding;

	private final BronDeelnemerChanges changes;

	public WijzigingSleutelgegevens(BronAanleverMelding aanleverMelding,
			BronDeelnemerChanges changes)
	{
		this.melding = aanleverMelding;
		this.changes = changes;

		Deelnemer deelnemer = changes.getDeelnemer();
		Verbintenis bveVerbintenis = model.getBVEVerbintenis(deelnemer);
		BronOnderwijssoort sector = model.getBronOnderwijssoort(bveVerbintenis);
		aanleverMelding.setBronOnderwijssoort(sector);
	}

	@Override
	public boolean isToepasselijk()
	{
		Deelnemer deelnemer = changes.getDeelnemer();
		Persoon persoon = deelnemer.getPersoon();
		boolean heeftGBARelatie = persoon.getBsn() != null;

		boolean adresWijziging = false;
		boolean verbintenisActiefBijAdresWijziging = false;
		boolean sofinummerAchterhaald = false;
		boolean personaliaWijziging = false;

		for (BronStateChange change : changes)
		{
			if (!change.isSleutelwaardeWijziging())
				continue;
			String property = change.getPropertyName();

			sofinummerAchterhaald =
				sofinummerAchterhaald
					|| ("bsn".equals(property) && deelnemer.getOnderwijsnummer() != null);
			personaliaWijziging =
				personaliaWijziging
					|| (!heeftGBARelatie && "geboortedatum,geslacht,toepassingGeboortedatum"
						.contains(property));
			if ("postcode,land".contains(property))
			{
				Adres gewijzigdAdres = (Adres) change.getEntity();
				for (PersoonAdres persAdres : gewijzigdAdres.getPersoonAdressen())
				{
					adresWijziging = adresWijziging || persAdres.isFysiekadres();
					if (persAdres.isFysiekadres())
					{
						verbintenisActiefBijAdresWijziging =
							verbintenisActiefBijAdresWijziging
								|| model.getBVEVerbintenis(deelnemer).isActiefTijdens(
									persAdres.getBegindatum(), persAdres.getEinddatum());
					}

				}
			}
		}
		return changes.bevatSleutelgegevenWijziging()
			&& (sofinummerAchterhaald || (adresWijziging && verbintenisActiefBijAdresWijziging) || personaliaWijziging);
	}

	@Override
	public WijzigingSleutelgegevensRecord createMelding()
	{
		if (!isToepasselijk())
			return null;
		BronBveAanleverRecord record = findOrNew();

		verwerkPersoonwijzigingen(record);
		verwerkAdreswijzigingen(record);

		if (melding.getIngangsDatum() == null)
		{
			melding.setIngangsDatum(TimeUtil.vandaag());
		}

		if (!record.isSleutelwijzigingGevuld())
		{
			melding.setVerwijderd(true);
			melding.getMeldingen().remove(record);
			if (record.isSaved())
			{
				record.setMelding(null);
				record.delete();
			}
			if (melding.isSaved())
			{
				melding.delete();
			}
		}
		return record;
	}

	private BronBveAanleverRecord findOrNew()
	{
		WijzigingSleutelgegevensRecord record =
			melding.getRecord(WijzigingSleutelgegevensRecord.class);
		if (record == null)
		{
			record = BronBveAanleverRecord.newWijzigingSleutelgegevensRecord(melding);
		}
		return (BronBveAanleverRecord) record;
	}

	private void verwerkPersoonwijzigingen(WijzigingSleutelgegevensRecord record)
	{
		// Sofinummer achterhaald moet, indein gevuld, bestaan bij de GBA in combinatie
		// met het geslacht en de geboortedatum uit het 306-record, aangevuld met de
		// ontbrekende elementen uit het 305 record.
		verwerkBsnWijziging(record);
		verwerkGeboortedatumWijziging(record);
		verwerkGeslachtWijziging(record);
	}

	private void verwerkBsnWijziging(WijzigingSleutelgegevensRecord record)
	{
		Deelnemer deelnemer = changes.getDeelnemer();
		Persoon persoon = deelnemer.getPersoon();
		if (changes.heeftWijziging(persoon, "bsn") && deelnemer.getOnderwijsnummer() != null)
		{
			Long vorigeBsn = changes.getPreviousValue(persoon, "bsn");

			if (vorigeBsn == null)
			{
				record.setSofinummerAchterhaald(model.getSofinummer(deelnemer));

				// als sofi-nummer achterhaald gevuld is, moet in het 305 record het
				// onderwijsnummer gevuld zijn.
				melding.setSofinummer(null);
				melding.setOnderwijsnummer(model.getOnderwijsnummer(deelnemer));
			}
		}
	}

	private void verwerkGeboortedatumWijziging(WijzigingSleutelgegevensRecord record)
	{
		Deelnemer deelnemer = changes.getDeelnemer();
		Persoon persoon = deelnemer.getPersoon();
		if (changes.heeftWijziging(persoon, "geboortedatum")
			|| changes.heeftWijziging(persoon, "toepassingGeboortedatum"))
		{
			record.setGeboortedatumGewijzigd(model.getGeboortedatum(deelnemer));
			melding.setGeboortedatum(model.getVorigeGeboorteDatum(changes, deelnemer.getPersoon()));
		}
	}

	private void verwerkGeslachtWijziging(WijzigingSleutelgegevensRecord record)
	{
		Deelnemer deelnemer = changes.getDeelnemer();
		Persoon persoon = deelnemer.getPersoon();

		if (changes.heeftWijziging(persoon, "geslacht"))
		{
			record.setGeslachtGewijzigd(model.getGeslacht(deelnemer));
			melding.setGeslacht(changes.<Geslacht> getPreviousValue(persoon, "geslacht"));
		}
	}

	private void verwerkAdreswijzigingen(WijzigingSleutelgegevensRecord record)
	{
		List<Adres> gewijzigdeAdressen = changes.getGewijzigde(Adres.class);

		Adres gewijzigdWoonAdres = null;
		Date ingangsdatumWijziging = null;
		for (Adres gewijzigdAdres : gewijzigdeAdressen)
		{
			for (PersoonAdres persoonAdres : gewijzigdAdres.getPersoonAdressen())
			{
				if (persoonAdres.isFysiekadres())
				{
					gewijzigdWoonAdres = gewijzigdAdres;
					ingangsdatumWijziging = persoonAdres.getBegindatum();
					break;
				}
			}
		}

		if (gewijzigdWoonAdres != null
			&& (changes.heeftWijziging(gewijzigdWoonAdres, "land") || changes.heeftWijziging(
				gewijzigdWoonAdres, "postcode")))
		{
			Land nieuweLand = gewijzigdWoonAdres.getLand();
			Land vorigeLand = nieuweLand;
			if (changes.heeftWijziging(gewijzigdWoonAdres, "land"))
				vorigeLand = changes.getPreviousValue(gewijzigdWoonAdres, "land");

			String nieuwePostcode = gewijzigdWoonAdres.getPostcode();
			String vorigePostcode = nieuwePostcode;
			if (changes.heeftWijziging(gewijzigdWoonAdres, "postcode"))
			{
				vorigePostcode = changes.getPreviousValue(gewijzigdWoonAdres, "postcode");
			}
			record.setDatumIngangAdreswijziging(ingangsdatumWijziging);
			if (nieuweLand.isNederland())
			{
				record.setPostcodeVolgensInstellingGewijzigd(nieuwePostcode);
				record.setLandGewijzigd(null);
			}
			else
			{
				record.setLandGewijzigd(nieuweLand.getCode());
				record.setPostcodeVolgensInstellingGewijzigd(null);
			}

			melding.setPostcodeVolgensInstelling(vorigePostcode);
			melding.setLand(vorigeLand == null ? null : vorigeLand.getCode());
			if (Land.CODE_NEDERLAND.equals(melding.getLand())
				&& StringUtil.isEmpty(melding.getPostcodeVolgensInstelling()))
			{
				// onbekend adres binnen NL, komt niet binnen KRD voor, maar wel binnen de
				// schakeltest
				melding.setPostcodeVolgensInstelling(null);
			}
			else if (Land.CODE_NEDERLAND.equals(melding.getLand()))
			{
				melding.setLand(null);
			}
			else
			{
				melding.setPostcodeVolgensInstelling(null);
			}
		}
	}
}
