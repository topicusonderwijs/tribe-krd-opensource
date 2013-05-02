package nl.topicus.eduarte.krd.bron.events.vo;

import static nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.*;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.krd.bron.BronDeelnemerChanges;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.krd.bron.BronStateChange;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding.VoMeldingSoort;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;

public class WijzigingSleutelgegevensVO extends AbstractVoEvent
{
	private final BronEduArteModel model = new BronEduArteModel();

	private final BronDeelnemerChanges changes;

	private final Deelnemer deelnemer;

	public WijzigingSleutelgegevensVO(BronDeelnemerChanges changes,
			List<BronInschrijvingsgegevensVOMelding> wachtrij)
	{
		super(wachtrij);
		this.changes = changes;
		this.deelnemer = changes.getDeelnemer();
	}

	@Override
	public boolean isToepasselijk()
	{
		Persoon persoon = deelnemer.getPersoon();

		if (!heeftActieveBronCommuniceerbareVoVerbintenis())
			return false;

		boolean heeftGBARelatie = persoon.getBsn() != null;

		boolean adresWijziging = false;
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
				}
			}
		}

		return changes.bevatSleutelgegevenWijziging()
			&& (sofinummerAchterhaald || adresWijziging || personaliaWijziging);
	}

	private boolean heeftActieveBronCommuniceerbareVoVerbintenis()
	{
		for (Verbintenis verbintenis : deelnemer.getVOVerbintenissen())
		{
			if (verbintenis.isBronCommuniceerbaar() && verbintenis.isActief())
				return true;
		}
		return false;
	}

	@Override
	public BronInschrijvingsgegevensVOMelding createMelding()
	{
		if (!isToepasselijk())
			return null;

		BronInschrijvingsgegevensVOMelding melding = findOrNew(VoMeldingSoort.W, null);
		melding.setSoortMutatie(Aanpassing);
		Persoon persoon = deelnemer.getPersoon();

		if (changes.heeftWijzigingen(persoon))
		{
			if (deelnemer.getOnderwijsnummer() != null)
			{
				if (changes.heeftWijziging(persoon, "geboortedatum")
					|| changes.heeftWijziging(persoon, "toepassingGeboortedatum"))
				{
					Datum geboortedatum = model.getGeboortedatum(deelnemer);
					melding.setGeboorteDatumGewijzigd(geboortedatum);
					melding.setGeboorteDatum(model.getVorigeGeboorteDatum(changes, persoon));
				}
				if (changes.heeftWijziging(persoon, "bsn"))
				{
					melding.setSofinummerAchterhaald(model.getSofinummer(deelnemer));
					melding.setSofiNummer(null);
					melding.setOnderwijsNummer(deelnemer.getOnderwijsnummer().intValue());
				}
			}
			if (changes.heeftWijziging(persoon, "geslacht"))
			{
				melding.setGeslachtGewijzigd(model.getGeslacht(deelnemer));
				Geslacht origineel = changes.getPreviousValue(persoon, "geslacht");
				melding.setGeslacht(origineel);
			}
		}

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
			if (changes.heeftWijziging(gewijzigdWoonAdres, "postcode"))
			{
				if (Land.CODE_NEDERLAND.equals(model.getLand(deelnemer)))
					melding.setPostcodeVolgensInstellingGewijzigd(model
						.getPostcode(gewijzigdWoonAdres));

				Land vorigeland = changes.getPreviousValue(gewijzigdWoonAdres, "land");
				if (vorigeland == null || vorigeland.isNederland())
				{
					String origineel = changes.getPreviousValue(gewijzigdWoonAdres, "postcode");
					if (origineel == null)
						origineel = persoon.getFysiekAdres().getAdres().getPostcode();
					melding.setPostcode(origineel);
				}
				else
					melding.setPostcode(null);
			}

			if (changes.heeftWijziging(gewijzigdWoonAdres, "land"))
			{
				Land vorigeland = changes.getPreviousValue(gewijzigdWoonAdres, "land");
				if (!Land.CODE_NEDERLAND.equals(model.getLand(deelnemer)))
					melding.setLandCodeVolgensInstellingGewijzigd(model.getLand(deelnemer));
				if (vorigeland != null && !vorigeland.isNederland())
					melding.setCodeLandAdres(vorigeland.getCode());
				else
					melding.setCodeLandAdres(null);
			}
			else if (!Land.CODE_NEDERLAND.equals(model.getLand(deelnemer)))
				melding.setCodeLandAdres(model.getLand(deelnemer));
			melding.setDatumIngangAdresWijziging(ingangsdatumWijziging);
		}

		melding.setBekostigingsRelevant(false);
		melding.setIngangsDatum(null);
		melding.addReden(changes.toString());
		if (melding.isSleutelwijzigingGevuld())
		{
			melding.saveOrUpdate();
		}
		else
		{
			melding.setVerwijderd(true);
			if (melding.isSaved())
				melding.delete();
		}
		return melding;
	}

	@Override
	protected Verbintenis getVerbintenis()
	{
		return deelnemer.getVOVerbintenis(TimeUtil.getInstance().currentDate());
	}
}
