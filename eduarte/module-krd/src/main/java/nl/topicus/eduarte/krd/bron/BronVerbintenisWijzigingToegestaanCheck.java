package nl.topicus.eduarte.krd.bron;

import static nl.topicus.cobra.util.TimeUtil.*;
import static nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck.WijzigingToegestaanResultaat.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.krd.entities.bron.BronStatus;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

public class BronVerbintenisWijzigingToegestaanCheck implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String WIJZIGEN_NA_MUTATIEBEPERKING = "WIJZIGEN_NA_MUTATIEBEPERKING";

	public enum WijzigingToegestaanResultaat
	{
		EinddatumMagNietNaBestaandeEinddatumLiggen(false, false),
		MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop(false,
				false),
		SchooljaarIsAfgeslotenVoorMutaties(false, false),
		GeenSchooljaarStatusGevonden(false, false),
		IsGeen_BO_Of_VO_Verbintenis(true, false),
		VerbintenisNogNietBekendBijBron(true, false),
		AlleMutatiesZijnNogToegestaan(true, false),
		GeenBelangrijkeVeldenAangepast(true, false),
		NietBekostigdOpRecentePeildatum(true, false),
		GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie(true, false),
		MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking(true, false),
		MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop(true, true),
		MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop(true, true),
		//
		;

		private final boolean accountantsmutatie;

		private final boolean toegestaan;

		private WijzigingToegestaanResultaat(boolean toegestaan, boolean accountantsmutatie)
		{
			this.toegestaan = toegestaan;
			this.accountantsmutatie = accountantsmutatie;
		}

		public String getReden()
		{
			return StringUtil.convertCamelCase(name());
		}

		public boolean isToegestaan()
		{
			return toegestaan;
		}

		public boolean isAccountantsmutatie()
		{
			return accountantsmutatie;
		}

		@Override
		public String toString()
		{
			return getReden();
		}
	}

	private WijzigingToegestaanResultaat resultaat;

	private Schooljaar huidigeSchooljaar = Schooljaar.huidigSchooljaar();

	/**
	 * Lege constructor om wat methodes uit deze class te gebruiken
	 */
	public BronVerbintenisWijzigingToegestaanCheck()
	{
	}

	public BronVerbintenisWijzigingToegestaanCheck(Date begindatum, Verbintenis verbintenis)
	{
		this.resultaat = zijnPlaatsingWijzigingenToegestaan(begindatum, verbintenis);
	}

	public BronVerbintenisWijzigingToegestaanCheck(Bekostigd oudeBekostigd,
			Intensiteit oudeIntensiteit, Bekostigingsperiode oudePeriode,
			Bekostigingsperiode periode)
	{
		this.resultaat =
			isPeriodegegevenWijzigingToegestaan(oudeBekostigd, oudeIntensiteit, oudePeriode,
				periode);
	}

	public BronVerbintenisWijzigingToegestaanCheck(Date oudeBegindatum, Date oudeEinddatum,
			VerbintenisStatus oudeStatus, Opleiding oudeOpleiding, Intensiteit oudeIntensiteit,
			Bekostigd oudeBekostigd, Verbintenis verbintenis)
	{
		this.resultaat =
			zijnVerbintenisWijzigingenToegestaan(oudeBegindatum, oudeEinddatum, oudeStatus,
				oudeOpleiding, oudeIntensiteit, oudeBekostigd, verbintenis);
	}

	public WijzigingToegestaanResultaat getResultaat()
	{
		return resultaat;
	}

	public String getReden()
	{
		return resultaat.getReden();
	}

	public boolean isWijzigingToegestaan()
	{
		return resultaat.isToegestaan();
	}

	private WijzigingToegestaanResultaat zijnPlaatsingWijzigingenToegestaan(Date ingangsDatum,
			Verbintenis verbintenis)
	{
		if (verbintenis.isVOVerbintenis())
			return isPlaatsingsWijzigingVoorVOToegestaan(ingangsDatum, verbintenis);

		return IsGeen_BO_Of_VO_Verbintenis;
	}

	private WijzigingToegestaanResultaat zijnVerbintenisWijzigingenToegestaan(Date oudeBegindatum,
			Date oudeEinddatum, VerbintenisStatus oudeStatus, Opleiding oudeOpleiding,
			Intensiteit oudeIntensiteit, Bekostigd oudeBekostigd, Verbintenis verbintenis)
	{
		if (verbintenis.isBOVerbintenis())
			return isVerbintenisWijzigingVoorBOToegestaan(oudeBegindatum, oudeEinddatum,
				oudeStatus, oudeOpleiding, oudeIntensiteit, oudeBekostigd, verbintenis);
		if (verbintenis.isVOVerbintenis())
			return isVerbintenisWijzigingVoorVOToegestaan(oudeBegindatum, oudeEinddatum,
				oudeStatus, verbintenis);
		return IsGeen_BO_Of_VO_Verbintenis;
	}

	private WijzigingToegestaanResultaat isPlaatsingsWijzigingVoorVOToegestaan(Date ingangsDatum,
			Verbintenis verbintenis)
	{
		Date peildatumVO = getPeildatumVo();
		return isVolgensBronStatusMutatieToegestaan(verbintenis, ingangsDatum, peildatumVO);
	}

	private WijzigingToegestaanResultaat isPeriodegegevenWijzigingToegestaan(
			Bekostigd oudeBekostigd, Intensiteit oudeIntensiteit, Bekostigingsperiode oudePeriode,
			Bekostigingsperiode periode)
	{
		Verbintenis verbintenis = periode.getVerbintenis();

		WijzigingToegestaanResultaat wijziging =
			bepaalInitieleResultaat(oudePeriode.getBegindatum(), oudeBekostigd, oudeIntensiteit,
				periode.getVerbintenis());

		Date minBegindatum = min(oudePeriode.getBegindatum(), periode.getBegindatum());
		Date maxEinddatum =
			min(huidigeSchooljaar.getEinddatum(), max(oudePeriode.getEinddatumNotNull(), periode
				.getEinddatumNotNull()));

		List<Date> peildata = Schooljaar.allePeildataTussen(minBegindatum, maxEinddatum);
		Iterator<Date> peildatumIterator = peildata.iterator();

		while (wijziging.isToegestaan() && peildatumIterator.hasNext())
		{
			Date datum = peildatumIterator.next();

			// controle op of de verbintenis bekostigd hoeft hier niet omdat de
			// bekostiging *wijzigt*. Dit mag alleen als de status op de teldatum het niet
			// verbiedt.
			BronStatus bronStatus =
				BronStatus.getBronStatus(datum, verbintenis.getLocatie(), verbintenis
					.getBronOnderwijssoort());
			WijzigingToegestaanResultaat mutatieToegestaanOpPeildatum =
				isVolgensBronStatusMutatieToegestaan(bronStatus);
			if (!mutatieToegestaanOpPeildatum.isToegestaan()
				|| mutatieToegestaanOpPeildatum.compareTo(wijziging) > 0)
				wijziging = mutatieToegestaanOpPeildatum;

			if (mutatieToegestaanOpPeildatum == GeenSchooljaarStatusGevonden
				&& Schooljaar.valueOf(datum).na(Schooljaar.huidigSchooljaar()))
			{
				wijziging = WijzigingToegestaanResultaat.AlleMutatiesZijnNogToegestaan;
			}

		}

		return wijziging;
	}

	/**
	 * Als Soort mutatie = ´T´: de combinatie van Datum inschrijving en Werkelijke datum
	 * uitschrijving in de melding mag niet zodanig zijn dat de toe te voegen inschrijving
	 * actief is op één of meer recente* teldatums waarvoor in BRON een BO-mutatiestop is
	 * geregistreerd, tenzij één of meer van de volgende situaties van toepassing is: -
	 * het 300-record geeft aan dat de accountant verantwoordelijk is - iedere indicatie
	 * bekostiging inschrijving in de melding met invloed op recente* teldatums, waarvoor
	 * in BRON een BOmutatiestop is geregistreerd, heeft de waarde ´Nee´<br>
	 * 
	 * Als Soort mutatie = ´V´: de combinatie van Datum inschrijving en Werkelijke datum
	 * uitschrijving in BRON mag niet zodanig zijn dat de te verwijderen inschrijving
	 * actief is op één of meer recente* teldatums waarvoor in BRON een BO-mutatiestop is
	 * geregistreerd, tenzij één of meer van de volgende situaties van toepassing is: -
	 * het 300-record geeft aan dat de accountant verantwoordelijk is - iedere indicatie
	 * bekostiging inschrijving met invloed op recente* teldatums, waarvoor in BRON een
	 * BO-mutatiestop is geregistreerd, heeft volgens BRON de waarde ´Nee´<br>
	 * 
	 * Als Soort mutatie = ´V´: voor de betreffende inschrijving mogen in BRON geen
	 * diploma’s geregistreerd zijn met een Datum behaald in het kalenderjaar van een
	 * recente** teldatum waarvoor in BRON een BO-mutatiestop is geregistreerd, tenzij één
	 * of meer van de volgende situaties van toepassing is: - het 300-record geeft aan dat
	 * de accountant verantwoordelijk is - de indicatie bekostiging diploma voor ieder
	 * diploma dat voor de inschrijving behaald is in een kalenderjaar van een recente**
	 * teldatum waarvoor in BRON een BO-mutatiestop is geregistreerd, heeft volgens BRON
	 * de waarde ´Nee´<br>
	 * 
	 * Als Soort mutatie = ´A´ en als de inschrijving volgens BRON actief is op één of
	 * meer recente* teldatums waarvoor in BRON een BO-mutatiestop is geregistreerd: de
	 * waarde van Gevolgde opleiding mag in de melding niet afwijken van de in BRON
	 * geregistreerde waarde, tenzij één of meer van de volgende situaties van toepassing
	 * is: - het 300-record geeft aan dat de accountant verantwoordelijk is - iedere
	 * indicatie bekostiging inschrijving met invloed op de recente* teldatums, waarvoor
	 * in BRON een BO-mutatiestop is geregistreerd, heeft volgens BRON de waarde ´Nee´<br>
	 * 
	 * Als Soort mutatie = ´A´ en als de inschrijving volgens BRON actief is op één of
	 * meer recente* teldatums waarvoor in BRON een BO-mutatiestop is geregistreerd: de
	 * waarde van Leerweg mag in de melding niet afwijken van de in BRON geregistreerde
	 * waarde, tenzij één of meer van de volgende situaties van toepassing is: - het
	 * 300-record geeft aan dat de accountant verantwoordelijk is - iedere indicatie
	 * bekostiging inschrijving met invloed op de recente* teldatums, waarvoor in BRON een
	 * BO-mutatiestop is geregistreerd, heeft volgens BRON de waarde ´Nee´<br>
	 * 
	 * Als Soort mutatie = ´A´ en als de inschrijving volgens BRON actief is op één of
	 * meer recente* teldatums waarvoor in BRON een BO-mutatiestop is geregistreerd: de
	 * waarde van Intensiteit mag in de melding niet afwijken van de in BRON
	 * geregistreerde waarde, tenzij één of meer van de volgende situaties van toepassing
	 * is: - het 300-record geeft aan dat de accountant verantwoordelijk is - iedere
	 * indicatie bekostiging inschrijving met invloed op de recente* teldatums, waarvoor
	 * in BRON een BO-mutatiestop is geregistreerd, heeft volgens BRON de waarde ´Nee´<br>
	 * 
	 * Als Soort mutatie = ´A´: de combinatie van Datum inschrijving en Werkelijke datum
	 * uitschrijving in de melding mag er niet voor zorgen dat de inschrijving in BRON
	 * niet langer actief is op één of meer recente* teldatums waarvoor in BRON een
	 * BO-mutatiestop is geregistreerd en waarop de inschrijving volgens BRON actief is óf
	 * dat de inschrijving juist actief wordt op één of meer recente* teldatums waarvoor
	 * in BRON een BO-mutatiestop is geregistreerd en waarop de inschrijving volgens BRON
	 * nog niet actief is, tenzij één van de volgende situaties van toepassing is: - het
	 * 300-record geeft aan dat de accountant verantwoordelijk is - iedere indicatie
	 * bekostiging inschrijving met invloed op de recente* teldatum(s), waarvoor in BRON
	 * een BO-mutatiestop is geregistreerd en waarop de inschrijving door de melding
	 * (in)actief wordt, heeft volgens BRON de waarde ´Nee´<br>
	 * 
	 * @param oudeEinddatum
	 */
	private WijzigingToegestaanResultaat isVerbintenisWijzigingVoorBOToegestaan(
			Date oudeBegindatum, Date oudeEinddatum, VerbintenisStatus oudeStatus,
			Opleiding oudeOpleiding, Intensiteit oudeIntensiteit, Bekostigd oudeBekostigd,
			Verbintenis verbintenis)
	{
		if (isToevoeging(oudeStatus, verbintenis.getStatus()))
		{
			return controleerToevoeging(verbintenis.getBegindatum(), verbintenis.getBekostigd(),
				verbintenis.getIntensiteit(), verbintenis);
		}
		else if (isVerwijdering(oudeStatus, verbintenis.getStatus()))
		{
			return controleerVerwijdering(oudeBegindatum, oudeBekostigd, oudeIntensiteit,
				verbintenis);
		}

		Date oudsteBegindatum = TimeUtil.min(oudeBegindatum, verbintenis.getBegindatum());

		WijzigingToegestaanResultaat wijziging =
			bepaalInitieleResultaat(oudsteBegindatum, oudeBekostigd, oudeIntensiteit, verbintenis);

		// officieel volgens de KRD normen mogen opleiding en intensiteit niet wijzigen in
		// de UI, maar om daar niet overal van uit te gaan toch hier nog even controleren
		// of het volgens de regels correct gebeurt.
		boolean opleidingGewijzigd = isGewijzigd(oudeOpleiding, verbintenis.getOpleiding());
		boolean intensiteitGewijzigd = isGewijzigd(oudeIntensiteit, verbintenis.getIntensiteit());
		boolean bekostigingGewijzigd = isGewijzigd(oudeBekostigd, verbintenis.getBekostigd());

		if (wijziging.isToegestaan()
			&& (opleidingGewijzigd || intensiteitGewijzigd || bekostigingGewijzigd))
		{
			WijzigingToegestaanResultaat kenmerkWijziging =
				controleerVerbintenisBekostigingRecenteTeldatumsOverPeriode(oudsteBegindatum,
					huidigeSchooljaar.getEinddatum(), oudeBekostigd, oudeIntensiteit, verbintenis);
			if (!kenmerkWijziging.isToegestaan() || kenmerkWijziging.compareTo(wijziging) > 0)
				wijziging = kenmerkWijziging;
		}

		boolean begindatumGewijzigd = isGewijzigd(oudeBegindatum, verbintenis.getBegindatum());
		if (wijziging.isToegestaan() && begindatumGewijzigd)
		{
			Date jongsteBegindatum = max(oudeBegindatum, verbintenis.getBegindatum());

			Schooljaar schooljaar = Schooljaar.valueOf(jongsteBegindatum);
			if (tussen(schooljaar.getEenOktober(), oudsteBegindatum, jongsteBegindatum)
				|| tussen(schooljaar.getEenFebruari(), oudsteBegindatum, jongsteBegindatum))
			{
				WijzigingToegestaanResultaat datumWijziging =
					controleerVerbintenisBekostigingRecenteTeldatumsOverPeriode(oudsteBegindatum,
						huidigeSchooljaar.getEinddatum(), oudeBekostigd, oudeIntensiteit,
						verbintenis);

				if (!datumWijziging.isToegestaan() || datumWijziging.compareTo(wijziging) > 0)
					wijziging = datumWijziging;
			}
		}

		boolean einddatumGewijzigd = isGewijzigd(oudeEinddatum, verbintenis.getEinddatum());
		if (wijziging.isToegestaan() && einddatumGewijzigd)
		{
			Date datum1 = oudeEinddatum;
			Date datum2 = verbintenis.getEinddatum();

			if (datum1 == null)
				datum1 = huidigeSchooljaar.getEinddatum();

			if (datum2 == null)
				datum2 = huidigeSchooljaar.getEinddatum();

			Date jongsteEinddatum = max(datum1, datum2);
			Date oudsteEinddatum = min(datum1, datum2);

			WijzigingToegestaanResultaat datumWijziging =
				controleerVerbintenisBekostigingRecenteTeldatumsOverPeriode(oudsteEinddatum,
					jongsteEinddatum, oudeBekostigd, oudeIntensiteit, verbintenis);
			if (!datumWijziging.isToegestaan() || datumWijziging.compareTo(wijziging) > 0)
				wijziging = datumWijziging;
		}
		return wijziging;
	}

	private WijzigingToegestaanResultaat bepaalInitieleResultaat(Date oudeBegindatum,
			Bekostigd oudeBekostigd, Intensiteit oudeIntensiteit, Verbintenis verbintenis)
	{
		Date oudsteBegindatum = min(oudeBegindatum, verbintenis.getBegindatum());
		BronStatus bronStatus =
			BronStatus.getBronStatus(oudsteBegindatum, verbintenis.getLocatie(), verbintenis
				.getBronOnderwijssoort());

		if (bronStatus == BronStatus.GegevensWordenIngevoerd)
			return AlleMutatiesZijnNogToegestaan;

		if (verbintenis.wasBekostigdOpDatum(oudeBekostigd, oudeIntensiteit, oudeBegindatum == null
			? oudsteBegindatum : oudeBegindatum))
			return GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie;
		return NietBekostigdOpRecentePeildatum;
	}

	private <T> boolean isGewijzigd(T oudeWaarde, T nieuweWaarde)
	{
		return !JavaUtil.equalsOrBothNull(oudeWaarde, nieuweWaarde);
	}

	/**
	 * Controleert of de toevoeging van de verbintenis toegestaan is volgens het PvE van
	 * BRON. Hierbij wordt het volgende gecontroleert:
	 * <ul>
	 * <li>De combinatie van Datum inschrijving en Werkelijke datum uitschrijving in de
	 * melding mag niet zodanig zijn dat de toe te voegen inschrijving actief is op één of
	 * meer recente(*) teldatums waarvoor in BRON een BO-mutatiestop is geregistreerd,
	 * tenzij één of meer van de volgende situaties van toepassing is:
	 * <ul>
	 * <li>de accountant is verantwoordelijk</li>
	 * <li>iedere indicatie bekostigd inschrijving in de melding met invloed op recente(*)
	 * teldatums, waarvoor in BRON en BO-mutatiestop is geregistreerd, heeft de waarde
	 * 'Nee'</li>
	 * </ul>
	 * </li>
	 * <li>De combinatie van Datum inschrijving en Werkelijke datum uitschrijving in de
	 * medling mag niet zodanig zijn dat de toe te voegen inschrijving actief is op één of
	 * meer recente(*) teldatums waarvoor in BRON een goedkeurend BO-assurance-rapport is
	 * geregistreerd, tenzij de volgende situatie van toepassing is:
	 * <ul>
	 * <li>iedere indicatie bekostigd inschrijving in de melding met invloed op recente(*)
	 * teldatums, waarvoor in BRON en BO-mutatiestop is geregistreerd, heeft de waarde
	 * 'Nee'</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * (*) Recent: alle teldatums sinds Datum inschrijving (gerekend vanaf 1 oktober
	 * <em>2004</em>), eventueel tot en met Werkelijke datum uitschrijving. Door de
	 * introductie van de tweede teldatum gaat het hier vanaf 1-8-2008 niet alleen om de 1
	 * oktober teldatum, maar ook om de 1 februari teldatum.
	 */
	private WijzigingToegestaanResultaat controleerToevoeging(Date oudsteBegindatum,
			Bekostigd oudeBekostigd, Intensiteit oudeIntensiteit, Verbintenis verbintenis)
	{
		return controleerVerbintenisBekostigingRecenteTeldatumsOverPeriode(oudsteBegindatum,
			huidigeSchooljaar.getEinddatum(), oudeBekostigd, oudeIntensiteit, verbintenis);
	}

	/**
	 * Controleert of de verwijdering van een verbintenis toegestaan is volgens het PvE.
	 * <h3>Tijdens een mutatiestop</h3>
	 * <p>
	 * Als soort mutatie = 'V': voor de betreffende inschrijving mogen in BRON geen
	 * diploma's geregistreerd zijn met een Datum behaald in het kalenderjaar van een
	 * recente teldatum waarvoor in BRON een BO-mutatiestop is geregistreerd, tenzij één
	 * of meer van de volgende situaties van toepassing is:
	 * <ul>
	 * <li>de account is verantwoordelijk</li>
	 * <li>de indicatie bekostiging diploma voor ieder diploma dat voor de inschrijving
	 * behaald is in een kalenderjaar van een recente teldaum waarvoor in BRON een
	 * BO-mutatiestop is geregistreerd heeft volgens BRON de waarde 'Nee'</li>
	 * </ul>
	 * <h3>Na afgifte assurancerapport</h3>
	 * <p>
	 * Als Soor tmutatie = 'V': voor de betreffende inschrijving mogen in BRON geen
	 * diploma's geregistreerd zijn met een Datum behaald in het kalenderjaar van een
	 * recente teldatum waarvoor in BRON een goedkeurend BO-assurance-rapport is
	 * geregistreerd, tenzij de volgende situatie van toepassing is:
	 * <ul>
	 * <li>de indicatie bekostiging diploma voor ieder diploma dat voor de inschrijving
	 * behaald is in een kalenderjaar van een recente teldatum waarvoor in BRON een
	 * goedkeurend BO-assurance-rapport is gergistreerd, heeft volgens BRON de waarde
	 * 'Nee'.</li>
	 * </ul>
	 */
	private WijzigingToegestaanResultaat controleerVerwijdering(Date oudsteBegindatum,
			Bekostigd oudeBekostigd, Intensiteit oudeIntensiteit, Verbintenis verbintenis)
	{
		WijzigingToegestaanResultaat wijziging =
			controleerVerbintenisBekostigingRecenteTeldatumsOverPeriode(oudsteBegindatum,
				huidigeSchooljaar.getEinddatum(), oudeBekostigd, oudeIntensiteit, verbintenis);

		Iterator<Examendeelname> deelnames = verbintenis.getExamendeelnames().iterator();
		while (wijziging.isToegestaan() && deelnames.hasNext())
		{
			Examendeelname deelname = deelnames.next();
			if (deelname.isBekostigd() && deelname.getDatumUitslag() != null)
			{
				BronStatus bronStatus =
					BronStatus.getBronStatus(deelname.getDatumUitslag(), verbintenis.getLocatie(),
						verbintenis.getBronOnderwijssoort());

				WijzigingToegestaanResultaat mutatieToegestaanOpPeildatum =
					isVolgensBronStatusMutatieToegestaan(bronStatus);
				if (!mutatieToegestaanOpPeildatum.isToegestaan()
					|| mutatieToegestaanOpPeildatum.compareTo(wijziging) > 0)
					wijziging = mutatieToegestaanOpPeildatum;
			}
		}
		return wijziging;
	}

	/**
	 * Loopt de bekostiging op elke teldatum vanaf de ingangsdatum van de verbintenis door
	 * om te controleren of de mutatie al dan niet toegestaan is (en of deze gecontroleerd
	 * moet worden door de accountant).
	 */
	private WijzigingToegestaanResultaat controleerVerbintenisBekostigingRecenteTeldatumsOverPeriode(
			Date begindatumPeriode, Date einddatumPeriode, Bekostigd oudeBekostigd,
			Intensiteit oudeIntensiteit, Verbintenis verbintenis)
	{
		WijzigingToegestaanResultaat wijziging =
			bepaalInitieleResultaat(begindatumPeriode, oudeBekostigd, oudeIntensiteit, verbintenis);

		List<Date> peildata = Schooljaar.allePeildataTussen(begindatumPeriode, einddatumPeriode);
		Iterator<Date> peildatumIterator = peildata.iterator();

		while (wijziging.isToegestaan() && peildatumIterator.hasNext())
		{
			Date datum = peildatumIterator.next();

			// Het is ook niet toegestaan als de bekostiging veranderd wordt voor een
			// peildatum waarvoor geen mutaties meer gedaan mogen worden, daarom deze
			// vergelijking
			if (verbintenis.wasBekostigdOpDatum(oudeBekostigd, oudeIntensiteit, datum)
				|| verbintenis.wasBekostigdOpDatum(oudeBekostigd, oudeIntensiteit, datum) != verbintenis
					.isBekostigdOpDatum(datum))
			{
				BronStatus bronStatus =
					BronStatus.getBronStatus(datum, verbintenis.getLocatie(), verbintenis
						.getBronOnderwijssoort());
				WijzigingToegestaanResultaat mutatieToegestaanOpPeildatum =
					isVolgensBronStatusMutatieToegestaan(bronStatus);

				if (!mutatieToegestaanOpPeildatum.isToegestaan()
					|| mutatieToegestaanOpPeildatum.isAccountantsmutatie()
					|| mutatieToegestaanOpPeildatum.compareTo(wijziging) > 0)
					wijziging = mutatieToegestaanOpPeildatum;

				if (mutatieToegestaanOpPeildatum == GeenSchooljaarStatusGevonden
					&& Schooljaar.valueOf(datum).na(Schooljaar.huidigSchooljaar()))
				{
					wijziging = WijzigingToegestaanResultaat.AlleMutatiesZijnNogToegestaan;
				}

			}
		}

		return wijziging;
	}

	/**
	 * Ingangsdatum, eindatum, ILT-code,Jaren praktijkonderwijs, Leerjaar
	 */
	private WijzigingToegestaanResultaat isVerbintenisWijzigingVoorVOToegestaan(
			Date oudeBegindatum, Date oudeEinddatum, VerbintenisStatus oudeStatus,
			Verbintenis verbintenis)
	{
		Date peildatumVO = getPeildatumVo();
		Date peildatumVOVorigJaar = getPeildatumVoVorigJaar();
		// Toevoeging of verwijdering
		if (isToevoeging(oudeStatus, verbintenis.getStatus())
			|| isVerwijdering(oudeStatus, verbintenis.getStatus()))
		{
			return isVolgensBronStatusMutatieToegestaan(verbintenis, verbintenis.getBegindatum(),
				peildatumVO);
		}

		// Uitschrijving
		else if (oudeEinddatum == null && verbintenis.getEinddatum() != null
			&& oudeStatus.isBronCommuniceerbaar())
		{
			return isVolgensBronStatusMutatieToegestaan(verbintenis, verbintenis.getEinddatum(),
				peildatumVO);
		}

		// Aanpassing
		else
		{
			// begindatum aangepast
			if (oudeBegindatum != null && !oudeBegindatum.equals(verbintenis.getBegindatum()))
			{
				if (oudeBegindatum.before(peildatumVO))
					return isVolgensBronStatusMutatieToegestaan(verbintenis, oudeBegindatum,
						peildatumVO);
				if (verbintenis.getBegindatum().before(peildatumVO))
					return isVolgensBronStatusMutatieToegestaan(verbintenis, verbintenis
						.getBegindatum(), peildatumVOVorigJaar);
			}
			// einddatum aangepast (controle volgens mantis #58356)
			if (oudeEinddatum != null && !oudeEinddatum.equals(verbintenis.getEinddatum()))
			{
				if (!verbintenis.getBronStatus().isGemeldAanBron())
					return VerbintenisNogNietBekendBijBron;

				if (oudeEinddatum.after(verbintenis.getEinddatum()))
					if (getSecurityCheckWijzigingNaMutatiebeperking())
						return MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop;
					else
						return MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop;
				else
					return EinddatumMagNietNaBestaandeEinddatumLiggen;
			}
			return GeenBelangrijkeVeldenAangepast;
		}
	}

	private boolean isToevoeging(VerbintenisStatus oudeStatus, VerbintenisStatus nieuweStatus)
	{
		boolean isNogNietBekendBijBron = oudeStatus == null || !oudeStatus.isBronCommuniceerbaar();
		return isNogNietBekendBijBron && nieuweStatus.isBronCommuniceerbaar();
	}

	private boolean isVerwijdering(VerbintenisStatus oudeStatus, VerbintenisStatus nieuweStatus)
	{
		boolean isBekendBijBron = oudeStatus != null && oudeStatus.isBronCommuniceerbaar();
		return isBekendBijBron && !nieuweStatus.isBronCommuniceerbaar();
	}

	private Date getPeildatumVoVorigJaar()
	{
		Date peildatumVOVorigJaar =
			TimeUtil.getInstance().getFirstDayOfMonth(huidigeSchooljaar.getStartJaar() - 1,
				Calendar.OCTOBER);
		return peildatumVOVorigJaar;
	}

	public Date getPeildatumVo()
	{
		Date peildatumVO =
			TimeUtil.getInstance().getFirstDayOfMonth(huidigeSchooljaar.getStartJaar(),
				Calendar.OCTOBER);
		return peildatumVO;
	}

	public Date getPeildatumBO()
	{
		Date peildatumBO =
			TimeUtil.getInstance().getFirstDayOfMonth(huidigeSchooljaar.getEindJaar(),
				Calendar.FEBRUARY);
		return peildatumBO;
	}

	public WijzigingToegestaanResultaat isVolgensBronStatusMutatieToegestaan(
			Verbintenis verbintenis, Date datum, Date peildatum)
	{
		if (na(datum, peildatum))
		{
			return NietBekostigdOpRecentePeildatum;
		}

		BronStatus bronStatus = BronStatus.getBronStatus(verbintenis);

		WijzigingToegestaanResultaat mutatieToegestaan =
			isVolgensBronStatusMutatieToegestaan(bronStatus);

		if (mutatieToegestaan == GeenSchooljaarStatusGevonden
			&& Schooljaar.valueOf(datum).na(Schooljaar.valueOf(peildatum)))
		{
			// Er is geen status gevonden en het betreffende schooljaar ligt in de
			// toekomst, dus: alle mutaties zijn nog toegestaan
			return AlleMutatiesZijnNogToegestaan;
		}
		else
		{
			return mutatieToegestaan;
		}

	}

	private WijzigingToegestaanResultaat isVolgensBronStatusMutatieToegestaan(BronStatus bronStatus)
	{
		if (bronStatus == null)
		{
			return GeenSchooljaarStatusGevonden;
		}
		if (bronStatus.isAlleMutatiesToegestaan())
		{
			return AlleMutatiesZijnNogToegestaan;
		}
		if (bronStatus == BronStatus.MutatiebeperkingIngesteld)
		{
			if (getSecurityCheckWijzigingNaMutatiebeperking())
				return MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking;
			else
				return MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop;
		}
		if (bronStatus == BronStatus.MutatiestopIngesteld)
		{
			if (getSecurityCheckWijzigingNaMutatiebeperking())
				return MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop;
			else
				return MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop;
		}
		return SchooljaarIsAfgeslotenVoorMutaties;
	}

	protected boolean getSecurityCheckWijzigingNaMutatiebeperking()
	{
		boolean toegestaan =
			new DataSecurityCheck(WIJZIGEN_NA_MUTATIEBEPERKING).isActionAuthorized(Enable.class);
		return (toegestaan);
	}
}