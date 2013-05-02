package nl.topicus.eduarte.krd.bron;

import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit.*;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.eduarte.entities.BronMeldingOnderdeel;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.krd.bron.events.WijzigingExamengegevensVO;
import nl.topicus.eduarte.krd.bron.events.vo.WijzigingPersonaliaVO;
import nl.topicus.eduarte.krd.bron.events.vo.WijzigingPlaatsingsgegevensVO;
import nl.topicus.eduarte.krd.bron.events.vo.WijzigingSleutelgegevensVO;
import nl.topicus.eduarte.krd.bron.events.vo.WijzigingVerbintenisgegevensVO;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchVOExamengegevens;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding.VoMeldingSoort;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.zoekfilters.AlwaysAuthorizedContext;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BronControllerVo
{
	private static final Logger log = LoggerFactory.getLogger(BronControllerVo.class);

	private final BronEduArteModel model = new BronEduArteModel();

	/**
	 * Verwerkt de wijzigingen op verbintenis en plaatsingen.
	 */
	void verwerkVerbintenisWijzigingen(BronVerbintenisChanges changes)
	{
		Verbintenis verbintenis = changes.getEntiteit();

		// examendeelnemers hoeven niet naar BRON
		if (verbintenis.getIntensiteit() == Examendeelnemer)
			return;

		List<BronInschrijvingsgegevensVOMelding> wachtrij = getVoMeldingenUitWachtrij(verbintenis);

		if (changes.heeftWijzigingen(verbintenis) || changes.isWijzigingCumiVO(verbintenis))
		{
			WijzigingVerbintenisgegevensVO event =
				new WijzigingVerbintenisgegevensVO(changes, wachtrij, verbintenis);
			event.createMelding();
		}

		List<Plaatsing> gewijzigdePlaatsingen = changes.getGewijzigde(Plaatsing.class);
		for (Plaatsing gewijzigdePlaatsing : gewijzigdePlaatsingen)
		{
			WijzigingPlaatsingsgegevensVO plaatsingWijziging =
				new WijzigingPlaatsingsgegevensVO(changes, wachtrij, gewijzigdePlaatsing);
			plaatsingWijziging.createMelding();
		}
		// pas voor alle meldingen in de wachtrij (ook die net toegevoegd zijn) de BRON
		// status van de verbintenis aan.
		for (BronInschrijvingsgegevensVOMelding melding : wachtrij)
		{
			BronUtils.updateStatussenNaAanmaken(melding);
		}
	}

	void verwerkPersonaliaWijzigingen(BronDeelnemerChanges changes)
	{
		Deelnemer deelnemer = changes.getDeelnemer();
		List<Verbintenis> verbintenissen = deelnemer.getVOVerbintenissen();
		if (verbintenissen.isEmpty())
		{
			return;
		}
		for (Verbintenis verbintenis : verbintenissen)
		{
			if (verbintenis.getIntensiteit() == Examendeelnemer)
				continue;
			if (!changes.moetNaarBron(verbintenis))
				continue;

			List<BronInschrijvingsgegevensVOMelding> wachtrij =
				getVoMeldingenUitWachtrij(verbintenis);

			WijzigingPersonaliaVO event = new WijzigingPersonaliaVO(changes, wachtrij, verbintenis);
			event.createMelding();
		}
		if (changes.bevatSleutelgegevenWijziging())
		{
			List<BronInschrijvingsgegevensVOMelding> wachtrij =
				getVoSleutelMeldingenUitWachtrij(deelnemer);

			WijzigingSleutelgegevensVO event = new WijzigingSleutelgegevensVO(changes, wachtrij);
			BronInschrijvingsgegevensVOMelding melding = event.createMelding();
			if (melding != null)
			{
				BronUtils.updateStatussenNaAanmaken(melding);
			}
			// FIXME: wijzigingen aan PGN doorvoeren op meldingen die al in de wachtrij
			// staan
			// verwerkBsnWijzigingInWachtrij(changes);
			// verwerkOnderwijsnummerWijzigingInWachtrij(changes);
		}
	}

	private List<BronInschrijvingsgegevensVOMelding> getVoMeldingenUitWachtrij(
			Verbintenis verbintenis)
	{
		BronMeldingZoekFilter filter = new BronMeldingZoekFilter();
		filter.setVerbintenis(verbintenis);
		filter.setDeelnemer(verbintenis.getDeelnemer());
		filter.setBronMeldingOnderdeel(BronMeldingOnderdeel.VOInschrijving);
		filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		filter.setBronOnderwijssoort(BronOnderwijssoort.VOORTGEZETONDERWIJS);
		filter.setAuthorizationContext(new AlwaysAuthorizedContext());

		return getVoMeldingen(filter);
	}

	private List<BronInschrijvingsgegevensVOMelding> getVoMeldingenUitWachtrij(Deelnemer deelnemer)
	{
		BronMeldingZoekFilter filter = new BronMeldingZoekFilter();
		filter.setDeelnemer(deelnemer);
		filter.setBronMeldingOnderdeel(BronMeldingOnderdeel.VOInschrijving);
		filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		filter.setBronOnderwijssoort(BronOnderwijssoort.VOORTGEZETONDERWIJS);
		filter.setAuthorizationContext(new AlwaysAuthorizedContext());

		return getVoMeldingen(filter);
	}

	private List<BronInschrijvingsgegevensVOMelding> getVoSleutelMeldingenUitWachtrij(
			Deelnemer deelnemer)
	{
		BronMeldingZoekFilter filter = new BronMeldingZoekFilter();
		filter.setDeelnemer(deelnemer);
		filter.setBronMeldingOnderdeel(BronMeldingOnderdeel.VOInschrijving);
		filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		filter.setBronOnderwijssoort(BronOnderwijssoort.VOORTGEZETONDERWIJS);
		filter.setVoMeldingSoort(VoMeldingSoort.W);
		filter.setAuthorizationContext(new AlwaysAuthorizedContext());

		return getVoMeldingen(filter);
	}

	private List<BronInschrijvingsgegevensVOMelding> getVoMeldingen(BronMeldingZoekFilter filter)
	{
		BronDataAccessHelper wachtrij = DataAccessRegistry.getHelper(BronDataAccessHelper.class);
		List<BronInschrijvingsgegevensVOMelding> result =
			wachtrij.getBronVoMeldingen(filter, BronInschrijvingsgegevensVOMelding.class);

		return result;
	}

	@SuppressWarnings("unused")
	private void verwerkBsnWijzigingInWachtrij(BronDeelnemerChanges changes)
	{
		Deelnemer deelnemer = changes.getDeelnemer();
		Persoon persoon = deelnemer.getPersoon();

		boolean bsnGewijzigd = changes.heeftWijziging(persoon, "bsn");

		if (!bsnGewijzigd)
			return;
		if (persoon.getBsn() == null)
			return;

		int bsn = persoon.getBsn().intValue();

		List<BronInschrijvingsgegevensVOMelding> verbintenisMeldingenInWachtrij =
			getVoMeldingenUitWachtrij(deelnemer);
		for (BronInschrijvingsgegevensVOMelding melding : verbintenisMeldingenInWachtrij)
		{
			// sleutelwijzigingen onveranderd laten
			if (melding.getSoort() == VoMeldingSoort.W)
				continue;

			// bij gelijk sofinummer hoeft er niets te gebeuren
			if (JavaUtil.equalsOrBothNull(melding.getSofiNummer(), bsn))
				continue;

			if (melding.getSofiNummer() != null || melding.getOnderwijsNummer() == null)
			{
				melding.setSofiNummer(Integer.valueOf(model.getSofinummer(deelnemer)));
				melding.setOnderwijsNummer(null);
				melding.saveOrUpdate();
			}
		}
	}

	@SuppressWarnings("unused")
	private void verwerkOnderwijsnummerWijzigingInWachtrij(BronDeelnemerChanges changes)
	{
		Deelnemer deelnemer = changes.getDeelnemer();
		boolean onderwijsnummerGewijzigd = changes.heeftWijziging(deelnemer, "onderwijsnummer");
		if (!onderwijsnummerGewijzigd)
			return;

		if (deelnemer.getOnderwijsnummer() == null)
			return;

		int onderwijsnummer = deelnemer.getOnderwijsnummer().intValue();

		List<BronInschrijvingsgegevensVOMelding> verbintenisMeldingenInWachtrij =
			getVoMeldingenUitWachtrij(deelnemer);

		for (BronInschrijvingsgegevensVOMelding melding : verbintenisMeldingenInWachtrij)
		{
			// sleutelwijzigingen onveranderd laten
			if (melding.getSoort() == VoMeldingSoort.W)
				continue;
			if (JavaUtil.equalsOrBothNull(onderwijsnummer, melding.getOnderwijsNummer()))
				continue;
			if (melding.getSofiNummer() == null && deelnemer.getOnderwijsnummer() != null)
			{
				melding.setOnderwijsNummer(deelnemer.getOnderwijsnummer().intValue());
				melding.saveOrUpdate();
			}
		}
	}

	public BronExamenresultaatVOMelding createExamenMeldingen(Examendeelname deelname)
	{
		BronExamenresultaatVOMelding melding = findOrCreateDeelnemerExamenMeldingVo(deelname);
		WijzigingExamengegevensVO event = new WijzigingExamengegevensVO(melding, deelname);
		return event.createMelding();
	}

	private BronExamenresultaatVOMelding findOrCreateDeelnemerExamenMeldingVo(
			Examendeelname deelname)
	{
		BronExamenresultaatVOMelding melding = findDeelnemerExamenMeldingVo(deelname);

		if (melding != null)
			return melding;

		return createDeelnemerExamenMeldingVo(deelname.getVerbintenis(), null);
	}

	private BronExamenresultaatVOMelding findDeelnemerExamenMeldingVo(Examendeelname deelname)
	{
		Verbintenis verbintenis = deelname.getVerbintenis();
		Deelnemer deelnemer = verbintenis.getDeelnemer();

		BronDataAccessHelper wachtrij = DataAccessRegistry.getHelper(BronDataAccessHelper.class);

		BronMeldingZoekFilter filter = new BronMeldingZoekFilter();
		filter.setDeelnemer(deelnemer);
		filter.setVerbintenis(verbintenis);
		filter.setExamendeelname(deelname);
		filter.setBronMeldingOnderdeel(BronMeldingOnderdeel.VOExamen);
		filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		filter.setBronOnderwijssoort(BronOnderwijssoort.VOORTGEZETONDERWIJS);
		filter.setAuthorizationContext(new AlwaysAuthorizedContext());

		List<BronExamenresultaatVOMelding> result =
			wachtrij.getBronVoMeldingen(filter, BronExamenresultaatVOMelding.class);

		if (result.size() > 1)
		{
			log
				.warn(
					"Meer dan 1 examen records in de wachtrij gevonden voor deelnemer {} en verbintenis {}.",
					deelnemer, verbintenis);
		}
		if (result.isEmpty())
			return null;

		return result.get(0);
	}

	private BronExamenresultaatVOMelding createDeelnemerExamenMeldingVo(Verbintenis verbintenis,
			BronBatchVOExamengegevens batch)
	{
		BronExamenresultaatVOMelding melding = new BronExamenresultaatVOMelding(batch);
		melding.setVerbintenis(verbintenis);
		melding.setDeelnemer(verbintenis.getDeelnemer());
		melding.setVestigingsVolgnummer(model.getVestigingsVolgNummer(verbintenis));
		if (batch == null)
			melding.setMeldingNummer(0);
		else
			melding.setMeldingNummer(batch.getMeldingen().size() + 1);

		String sofiNr = model.getSofinummer(verbintenis.getDeelnemer());
		String onderwijsNummer = model.getOnderwijsnummer(verbintenis.getDeelnemer());
		if (!Strings.isEmpty(sofiNr))
		{
			melding.setSofiNummer(Integer.parseInt(sofiNr));
		}
		else if (!Strings.isEmpty(onderwijsNummer))
		{
			melding.setOnderwijsNummer(Integer.parseInt(onderwijsNummer));
		}
		melding.setLeerlingNummerInstelling(model.getLeerlingnummer(verbintenis.getDeelnemer()));
		melding.setGeboorteDatum(model.getGeboortedatum(verbintenis.getDeelnemer()));
		melding.setGeslacht(model.getGeslacht(verbintenis.getDeelnemer()));
		melding.setPostcode(model.getPostcode(verbintenis.getDeelnemer()));

		return melding;
	}

}
