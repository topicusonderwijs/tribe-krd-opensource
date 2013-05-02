package nl.topicus.eduarte.krd.bron;

import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd.*;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.entities.BronMeldingOnderdeel;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.krd.bron.events.*;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.zoekfilters.AlwaysAuthorizedContext;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.PersoonsgegevensRecord;

public class BronControllerBve
{
	private final BronEduArteModel model = new BronEduArteModel();

	void verwerkVerbintenisWijzigingen(BronVerbintenisChanges changes) throws BronException
	{
		BronAanleverMelding melding =
			findOrCreateAanleverMelding(changes.getEntiteit(), changes.toString());

		verwerkPersonaliaGegevens(melding, changes);
		verwerkVerbintenisWijzigingen(melding, changes);
		verwerkBekostigingsperiodeWijzigingen(melding, changes);
		verwerkBPVInschrijvingWijzigingenBO(melding, changes);
		verwerkExamendeelnameWijzigingenBO(melding, changes);

		verwerkExamendeelnameWijzigingenED(melding, changes);
		verwerkResultaatWijzigingenED(melding, changes);
		verwerkVakgegevensWijzigingenED(melding, changes);
		melding.consolideerRecords();
		BronUtils.updateStatussenNaAanmaken(melding);
	}

	private BronAanleverMelding findOrCreateAanleverMelding(Verbintenis verbintenis, String reden)
	{
		BronMeldingZoekFilter filter = new BronMeldingZoekFilter();
		filter.setVerbintenis(verbintenis);
		filter.setDeelnemer(verbintenis.getDeelnemer());
		filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		filter.setBronMeldingOnderdeelNot(BronMeldingOnderdeel.Sleutel);

		filter.setAuthorizationContext(new AlwaysAuthorizedContext());

		return findOrCreateAanleverMelding(filter, reden);
	}

	private BronAanleverMelding findOrCreateAanleverMelding(BronMeldingZoekFilter filter,
			String reden)
	{
		List<BronAanleverMelding> meldingen = findAanleverMelding(filter);
		for (BronAanleverMelding melding : meldingen)
		{
			if (melding.isVerdichtbaar())
				return melding;
		}
		Deelnemer deelnemer = filter.getDeelnemer();
		Verbintenis verbintenis = filter.getVerbintenis();

		BronAanleverMelding melding = createBveAanleverMelding(deelnemer, verbintenis);
		melding.addReden(reden);
		return melding;
	}

	private List<BronAanleverMelding> findAanleverMelding(BronMeldingZoekFilter filter)
	{
		BronDataAccessHelper wachtrij = DataAccessRegistry.getHelper(BronDataAccessHelper.class);
		List<BronAanleverMelding> meldingen = wachtrij.getBronBveMeldingen(filter);

		return meldingen;
	}

	private void verwerkPersonaliaGegevens(BronAanleverMelding melding,
			BronVerbintenisChanges changes)
	{
		DeelnemerZonderBsn event = new DeelnemerZonderBsn(changes.getDeelnemer(), changes, melding);
		if (event.isToepasselijk())
		{
			event.createMelding();
		}
	}

	private void verwerkVerbintenisWijzigingen(BronAanleverMelding melding,
			BronVerbintenisChanges changes) throws BronException
	{
		Verbintenis verbintenis = changes.getEntiteit();
		boolean hoeftNietNaarBron = !changes.moetNaarBron(verbintenis);
		boolean heeftGeenWijzigingen =
			!changes.heeftWijzigingen(verbintenis)
				&& changes.getGewijzigde(Vooropleiding.class).isEmpty();
		if (hoeftNietNaarBron || heeftGeenWijzigingen)
		{
			return;
		}
		ArrayList<BronEvent< ? >> events = new ArrayList<BronEvent< ? >>();
		if (verbintenis.isBOVerbintenis())
		{
			events.add(new WijzigingInschrijvingsgegevensBO(melding, changes));

			if (verbintenis.getBekostigd() != Gedeeltelijk)
			{
				// indien de verbintenis geheel (niet) bekostigd is, wordt de bekostiging
				// bepaald door het veld "Bekostigd" op de verbintenis. Deze kan altijd
				// wijzigen, dus er moet altijd een WijzigingPeriodegegevensInschrijvingBO
				// afgevuurd worden (deze bepaalt zelf of er al dan niet een 321 record
				// aangemaakt moet worden).
				events.add(new WijzigingPeriodegegevensInschrijvingBO(melding, changes));
			}
			else if (changes.isNieuwVoorBron(verbintenis))
			{
				// indien de bekostigingsperiodes al aangemaakt waren, maar de verbintenis
				// nog niet naar bron moest werden deze niet automatisch meegezonden bij
				// het aanleveren van de verbintenis aan BRON. Dit geldt alleen voor
				// gedeeltelijk bekostigde verbintenissen, die nieuw zijn voor bron.
				for (Bekostigingsperiode periode : verbintenis.getBekostigingsperiodes())
				{
					events
						.add(new WijzigingPeriodegegevensInschrijvingBO(melding, changes, periode));
				}
			}
		}
		else if (verbintenis.isEducatieVerbintenis())
		{
			events.add(new WijzigingInschrijvingsgegevensED(melding, changes));
		}
		else if (verbintenis.isVAVOVerbintenis())
		{
			events.add(new WijzigingInschrijvingsgegevensVAVO(melding, changes));
		}
		else if (verbintenis.isVOVerbintenis())
		{
			// doe niets, wordt op een andere plek afgehandeld
		}
		else
		{
			// doe niets, is een taxonomie die niet naar bron gestuurd hoeft te
			// worden, bijv. Wet Inburgering (WI)
		}
		for (BronEvent< ? > event : events)
		{
			if (event.isToepasselijk())
			{
				event.createMelding();
			}
		}
	}

	private void verwerkBekostigingsperiodeWijzigingen(BronAanleverMelding melding,
			BronEntiteitChanges<Verbintenis> changes) throws BronException
	{
		List<Bekostigingsperiode> gewijzigdePeriodes =
			changes.getGewijzigde(Bekostigingsperiode.class);
		for (Bekostigingsperiode periode : gewijzigdePeriodes)
		{
			Verbintenis verbintenis = periode.getVerbintenis();
			if (!changes.moetNaarBron(verbintenis))
			{
				continue;
			}
			ArrayList<BronEvent< ? >> events = new ArrayList<BronEvent< ? >>();
			if (verbintenis.isBOVerbintenis())
			{
				events.add(new WijzigingPeriodegegevensInschrijvingBO(melding, changes, periode));
			}
			for (BronEvent< ? > event : events)
			{
				if (event.isToepasselijk())
				{
					event.createMelding();
				}
			}
		}
	}

	private void verwerkBPVInschrijvingWijzigingenBO(BronAanleverMelding melding,
			BronEntiteitChanges<Verbintenis> changes) throws BronException
	{
		List<BPVInschrijving> gewijzigdeBPVInschrijvingen =
			changes.getGewijzigde(BPVInschrijving.class);

		// als code leerbedrijf van het BPV bedrijf is aangepast, moeten alle BPV
		// inschrijvingen die aan dat bedrijf gekoppeld zijn ook naar BRON gestuurd
		// worden.
		List<BPVBedrijfsgegeven> gewijzigdeBedrijven =
			changes.getGewijzigde(BPVBedrijfsgegeven.class);
		if (!gewijzigdeBedrijven.isEmpty())
		{
			List<BPVInschrijving> bpvs = changes.getEntiteit().getBpvInschrijvingen();
			for (BPVInschrijving bpv : bpvs)
			{
				if (!gewijzigdeBPVInschrijvingen.contains(bpv)
					&& gewijzigdeBedrijven.contains(bpv.getBedrijfsgegeven()))
				{
					gewijzigdeBPVInschrijvingen.add(bpv);
				}
			}
		}

		for (BPVInschrijving bpvInschrijving : gewijzigdeBPVInschrijvingen)
		{
			BronEvent< ? > event =
				new WijzigingBpvgegevensInschrijvingBO(melding, changes, bpvInschrijving);
			if (event.isToepasselijk())
			{
				event.createMelding();
			}
		}
	}

	private void verwerkExamendeelnameWijzigingenBO(BronAanleverMelding melding,
			BronEntiteitChanges<Verbintenis> changes) throws BronException
	{
		List<Examendeelname> gewijzigdeDeelnames = changes.getGewijzigde(Examendeelname.class);
		for (Examendeelname deelname : gewijzigdeDeelnames)
		{
			BronEvent< ? > event = new WijzigingExamengegevensBO(melding, changes, deelname);
			event.createMelding();
		}
	}

	private void verwerkExamendeelnameWijzigingenED(BronAanleverMelding melding,
			BronEntiteitChanges<Verbintenis> changes) throws BronException
	{
		List<Examendeelname> deelnames = changes.getGewijzigde(Examendeelname.class);
		for (Examendeelname deelname : deelnames)
		{
			BronEvent< ? > event = new WijzigingResultaatgegevensED(melding, changes, deelname);
			event.createMelding();
		}
	}

	private void verwerkResultaatWijzigingenED(BronAanleverMelding melding,
			BronEntiteitChanges<Verbintenis> changes) throws BronException
	{
		List<Resultaat> resultaten = changes.getGewijzigde(Resultaat.class);
		for (Resultaat resultaat : resultaten)
		{
			BronEvent< ? > event =
				new WijzigingNT2VaardighedenResultaatED(melding, changes, resultaat);
			event.createMelding();
		}
	}

	private void verwerkVakgegevensWijzigingenED(BronAanleverMelding melding,
			BronEntiteitChanges<Verbintenis> changes) throws BronException
	{
		List<OnderwijsproductAfnameContext> gewijzigdeOnderwijsproductAfnameContexten =
			getGewijzigdeOnderwijsproductAfnameContexten(changes);

		for (OnderwijsproductAfnameContext context : gewijzigdeOnderwijsproductAfnameContexten)
		{
			BronEvent< ? > event = new WijzigingVakgegevensED(melding, changes, context);
			event.createMelding();
		}

		for (OnderwijsproductAfnameContext context : gewijzigdeOnderwijsproductAfnameContexten)
		{
			BronEvent< ? > event =
				new WijzigingNT2VaardighedenAfnamecontextED(melding, changes, context);
			event.createMelding();
		}
	}

	private List<OnderwijsproductAfnameContext> getGewijzigdeOnderwijsproductAfnameContexten(
			BronEntiteitChanges<Verbintenis> changes)
	{
		List<OnderwijsproductAfnameContext> gewijzigdeOnderwijsprodyctAfnameContexten =
			new ArrayList<OnderwijsproductAfnameContext>();

		List<Verbintenis> gewijzigdeVerbintenissen = changes.getGewijzigde(Verbintenis.class);
		for (Verbintenis verbintenis : gewijzigdeVerbintenissen)
		{
			if (changes.isNieuwVoorBron(verbintenis)
				|| changes.heeftWijziging(verbintenis, "afnameContexten"))
			{
				List<OnderwijsproductAfnameContext> vorigeOnderwijsproductAfnameContext =
					changes.getPreviousValue(verbintenis, "afnameContexten");

				if (vorigeOnderwijsproductAfnameContext == null
					|| (vorigeOnderwijsproductAfnameContext.size() < verbintenis
						.getAfnameContexten().size()))
					vorigeOnderwijsproductAfnameContext = verbintenis.getAfnameContexten();

				for (OnderwijsproductAfnameContext context : vorigeOnderwijsproductAfnameContext)
				{
					if (!gewijzigdeOnderwijsprodyctAfnameContexten.contains(context))
						gewijzigdeOnderwijsprodyctAfnameContexten.add(context);
				}
			}
		}

		if (changes.heeftWijzigingenVoor(OnderwijsproductAfname.class))
		{
			List<OnderwijsproductAfname> gewijzigdeOnderwijsproductAfnames =
				changes.getGewijzigde(OnderwijsproductAfname.class);
			for (OnderwijsproductAfname gewijzigdeOnderwijsproductAfname : gewijzigdeOnderwijsproductAfnames)
			{
				for (OnderwijsproductAfnameContext context : gewijzigdeOnderwijsproductAfname
					.getAfnameContexten())
				{
					if (!gewijzigdeOnderwijsprodyctAfnameContexten.contains(context))
						gewijzigdeOnderwijsprodyctAfnameContexten.add(context);
				}
			}
		}

		if (changes.heeftWijzigingenVoor(OnderwijsproductAfnameContext.class))
		{
			List<OnderwijsproductAfnameContext> gewijzigdeContexten =
				changes.getGewijzigde(OnderwijsproductAfnameContext.class);

			for (OnderwijsproductAfnameContext context : gewijzigdeContexten)
			{
				if (!gewijzigdeOnderwijsprodyctAfnameContexten.contains(context))
					gewijzigdeOnderwijsprodyctAfnameContexten.add(context);
			}
		}

		return gewijzigdeOnderwijsprodyctAfnameContexten;
	}

	void verwerkPersonaliaWijzigingen(BronDeelnemerChanges changes)
	{
		Deelnemer deelnemer = changes.getDeelnemer();

		List<BronAanleverMelding> meldingen = findVerbintenisMeldingen(deelnemer);
		for (BronAanleverMelding melding : meldingen)
		{
			if (!melding.isVerwijderd())
			{
				PersoonsgegevensRecord persoonsgegevens =
					melding.getRecord(PersoonsgegevensRecord.class);
				if (persoonsgegevens != null && persoonsgegevens instanceof BronBveAanleverRecord)
				{
					BronBveAanleverRecord record = (BronBveAanleverRecord) persoonsgegevens;
					record.ververs(false);
					record.saveOrUpdate();
				}
			}
		}

		if (changes.bevatSleutelgegevenWijziging() && model.isInBronScopeEnBVE(deelnemer)
			&& !changes.isDeelnemerNieuwVoorBron()
			&& !changes.getDeelnemer().isNegeerSleutelWijzigingenBron())
		{
			// sleutelgegevens wijzigingen krijgen hun eigen aanlevermelding (moet vanwege
			// het BRON protocol)
			String reden = changes.toString();
			BronAanleverMelding melding = findOrCreateSleutelgegevensMelding(deelnemer, reden);

			if (melding.getVerbintenis() == null)
			{
				// controleer of de verbintenis op de melding gezet is, zoniet, dan is er
				// geen bron-communiceerbare verbintenis gekoppeld aan de deelnemer en
				// hoeft er dus ook geen wijziging verstuurd te worden.
				return;
			}
			WijzigingSleutelgegevens event = new WijzigingSleutelgegevens(melding, changes);
			event.createMelding();
			melding.consolideerRecords();
			verwerkBsnWijzigingInWachtrij(changes);
			verwerkOnderwijsnummerWijzigingInWachtrij(changes);
			verwerkPersonaliaWijzigingenInWachtrij(changes);
			BronUtils.updateStatussenNaAanmaken(melding);
		}
	}

	private BronAanleverMelding findOrCreateSleutelgegevensMelding(Deelnemer deelnemer, String reden)
	{
		BronMeldingZoekFilter filter = new BronMeldingZoekFilter();
		filter.setDeelnemer(deelnemer);
		filter.setVerbintenis(model.getBVEVerbintenis(deelnemer));
		filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		filter.setBronMeldingOnderdeel(BronMeldingOnderdeel.Sleutel);
		filter.setAuthorizationContext(new AlwaysAuthorizedContext());

		BronDataAccessHelper wachtrij = DataAccessRegistry.getHelper(BronDataAccessHelper.class);
		List<IBronMelding> meldingen = wachtrij.getBronMeldingen(filter);

		BronAanleverMelding melding = null;
		if (meldingen.isEmpty())
		{
			Verbintenis verbintenis = filter.getVerbintenis();
			melding = createBveAanleverMelding(deelnemer, verbintenis);
		}
		else
		{
			melding = (BronAanleverMelding) meldingen.get(0);
		}

		melding.addReden(reden);
		return melding;
	}

	private BronAanleverMelding createBveAanleverMelding(Deelnemer deelnemer,
			Verbintenis verbintenis)
	{
		BronAanleverMelding melding = new BronAanleverMelding(deelnemer);
		melding.setVerbintenis(verbintenis);

		melding.vul();

		return melding;
	}

	/**
	 * Verwerkt enkel meldingen zonder BSN indien de deelnemer van zonder-BSN naar BSN
	 * gegaan is. In dit geval is het BSN achterhaald en dient via dit nummer
	 * gecommuniceerd te worden met BRON (en niet meer met een eventueel onderwijsnummer).
	 */
	private void verwerkBsnWijzigingInWachtrij(BronDeelnemerChanges changes)
	{
		boolean bsnGewijzigd = changes.heeftWijziging(changes.getDeelnemer().getPersoon(), "bsn");

		if (!bsnGewijzigd)
			return;

		Deelnemer deelnemer = changes.getDeelnemer();
		List<BronAanleverMelding> verbintenisMeldingenInWachtrij =
			findVerbintenisMeldingen(deelnemer);
		for (BronAanleverMelding melding : verbintenisMeldingenInWachtrij)
		{
			if (melding.getSofinummer() != null || melding.getOnderwijsnummer() == null)
			{
				melding.setSofinummer(model.getSofinummer(deelnemer));
				melding.setOnderwijsnummer(null);
				melding.consolideerRecords();
			}
		}
	}

	private List<BronAanleverMelding> findVerbintenisMeldingen(Deelnemer deelnemer)
	{
		BronMeldingZoekFilter filter = new BronMeldingZoekFilter();
		filter.setDeelnemer(deelnemer);
		filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		filter.setBronMeldingOnderdeelNot(BronMeldingOnderdeel.Sleutel);
		filter.setAuthorizationContext(new AlwaysAuthorizedContext());

		BronDataAccessHelper wachtrij = DataAccessRegistry.getHelper(BronDataAccessHelper.class);
		List<BronAanleverMelding> meldingen = wachtrij.getBronBveMeldingen(filter);
		return meldingen;
	}

	/**
	 * Voert een wijziging in het onderwijsnummer van een deelnemer door in de wachtrij.
	 */
	private void verwerkOnderwijsnummerWijzigingInWachtrij(BronDeelnemerChanges changes)
	{
		boolean onderwijsnummerGewijzigd =
			changes.heeftWijziging(changes.getDeelnemer(), "onderwijsnummer");
		if (!onderwijsnummerGewijzigd)
			return;

		Deelnemer deelnemer = changes.getDeelnemer();
		List<BronAanleverMelding> verbintenisMeldingenInWachtrij =
			findVerbintenisMeldingen(deelnemer);
		for (BronAanleverMelding melding : verbintenisMeldingenInWachtrij)
		{
			// alleen een onderwijsnummer gebruiken wanneer het BSN niet gevuld is.
			if (melding.getSofinummer() == null)
			{
				melding.setOnderwijsnummer(model.getOnderwijsnummer(deelnemer));
				melding.consolideerRecords();
			}
		}
	}

	/**
	 * Verwerkt de personalia wijzigingen in meldingen die in de wachtrij staan voor deze
	 * deelnemer, maar alleen voor nieuwe toevoegingen, aangezien deelnemers die al bekend
	 * zijn bij BRON via een sleutelgegevenswijziging (306 record) aangepast moeten
	 * worden.
	 */
	private void verwerkPersonaliaWijzigingenInWachtrij(BronDeelnemerChanges changes)
	{
		Deelnemer deelnemer = changes.getDeelnemer();
		Persoon persoon = deelnemer.getPersoon();

		boolean personaliaGewijzigd =
			changes.heeftWijziging(persoon, "geboortedatum")
				|| changes.heeftWijziging(persoon, "toepassingGeboortedatum")
				|| changes.heeftWijziging(persoon, "geslacht");

		if (!personaliaGewijzigd)
			return;

		List<BronAanleverMelding> verbintenisMeldingenInWachtrij =
			findVerbintenisMeldingen(deelnemer);
		for (BronAanleverMelding melding : verbintenisMeldingenInWachtrij)
		{
			if (!deelnemer.getBronStatus().isBekendInBron())
			{
				melding.vul();
				melding.consolideerRecords();
			}
		}
	}

	public BronAanleverMelding createExamenMeldingen(Examendeelname deelname)
	{
		BronAanleverMelding melding = findOrCreateExamenMeldingVavo(deelname, "");

		WijzigingExamengegevensVAVO event = new WijzigingExamengegevensVAVO(melding, deelname);
		if (event.createMelding() != null)
		{
			melding.consolideerRecords();
			return melding;
		}
		return null;
	}

	private BronAanleverMelding findOrCreateExamenMeldingVavo(Examendeelname deelname, String reden)
	{
		BronMeldingZoekFilter filter = new BronMeldingZoekFilter();
		filter.setDeelnemer(deelname.getVerbintenis().getDeelnemer());
		filter.setVerbintenis(deelname.getVerbintenis());
		filter.setExamendeelname(deelname);
		filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		filter.setBronMeldingOnderdeel(BronMeldingOnderdeel.Examen);
		filter.setBronOnderwijssoort(BronOnderwijssoort.VAVO);
		filter.setAuthorizationContext(new AlwaysAuthorizedContext());

		return findOrCreateAanleverMelding(filter, reden);
	}
}
