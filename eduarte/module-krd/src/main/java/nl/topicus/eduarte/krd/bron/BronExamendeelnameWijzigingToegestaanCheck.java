package nl.topicus.eduarte.krd.bron;

import static nl.topicus.eduarte.krd.bron.BronExamendeelnameWijzigingToegestaanCheck.WijzigingToegestaanResultaat.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.Examenstatus;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.krd.entities.bron.BronStatus;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

/**
 * Als Soort mutatie = ´V´: de in BRON geregistreerde Datum behaald van het betreffende
 * BO-diploma mag niet liggen in het kalenderjaar van een recente** 1 oktober teldatum
 * waarvoor in BRON een BO-mutatiestop is geregistreerd, tenzij één of meer van de
 * volgende situaties van toepassing is: - het 300-record geeft aan dat de accountant
 * verantwoordelijk is - de Indicatie bekostiging diploma voor het betreffende BO-diploma
 * heeft volgens BRON de waarde ´Nee´<br>
 * 
 * Als Soort mutatie = ´T´: de Datum behaald in de melding mag niet liggen in het
 * kalenderjaar van een recente** 1 oktober teldatum waarvoor in BRON een BO-mutatiestop
 * is geregistreerd, tenzij één of meer van de volgende situaties van toepassing is: - het
 * 300-record geeft aan dat de accountant verantwoordelijk is - de Indicatie bekostiging
 * diploma in de melding heeft de waarde ´Nee´<br>
 * 
 * Als Soort mutatie = ´A´ en voor de 1 oktober teldatum van het kalenderjaar van de in
 * BRON geregistreerde Datum behaald is in BRON een BO-mutatiestop geregistreerd: het
 * kalenderjaar van de Datum behaald in de melding mag niet afwijken van het kalenderjaar
 * van de in BRON geregistreerde Datum behaald tenzij één of meer van de volgende
 * situaties van toepassing is: - het 300-record geeft aan dat de accountant
 * verantwoordelijk is - de in BRON geregistreerde Indicatie bekostiging diploma heeft de
 * waarde ´Nee´<br>
 * 
 * Als Soort mutatie = ´A´ en voor de 1 oktober teldatum van het kalenderjaar van de Datum
 * behaald in de melding is in BRON een BO-mutatiestop geregistreerd: het kalenderjaar van
 * de in BRON geregistreerde Datum behaald mag niet afwijken van het kalenderjaar van de
 * Datum behaald in de melding tenzij één of meer van de volgende situaties van toepassing
 * is: - het 300-record geeft aan dat de accountant verantwoordelijk is - de in BRON
 * geregistreerde Indicatie bekostiging diploma heeft de waarde ´Nee´<br>
 * 
 * Als Soort mutatie = ´A´ en voor de 1 oktober teldatum van het kalenderjaar van de in
 * BRON geregistreerde Datum behaald is in BRON een BO-mutatiestop geregistreerd: de
 * Indicatie bekostiging diploma in de melding mag niet afwijken van de in BRON
 * geregistreerde Indicatie bekostiging diploma, tenzij de volgende situatie van
 * toepassing is: - het 300-record geeft aan dat de accountant verantwoordelijk is<br>
 * 
 * (**) recent => alle 1 oktober teldatums sinds het kalenderjaar van Datum inschrijving
 * in BRON (gerekend vanaf 1 oktober 2004) tot en met het kalenderjaar van de
 * systeemdatum. Ook na de introductie van de 1 februari teldatum op 1/8/2008 gaat het
 * hier alleen om de 1/10 teldatums.
 */
public class BronExamendeelnameWijzigingToegestaanCheck implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String WIJZIGEN_NA_MUTATIEBEPERKING = "WIJZIGEN_NA_MUTATIEBEPERKING";

	public enum WijzigingToegestaanResultaat
	{
		MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop(false,
				false),
		SchooljaarIsAfgeslotenVoorMutaties(false, false),
		GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie(true, false),
		ExamendeelnameIsNietBekostigd(true, false),
		Geen_BO_VerbintenisOfExamendeelnameIsNogNietCommuniceerbaarMetBron(true, false),
		AlleMutatiesZijnNogToegestaan(true, false),
		GeenBelangrijkeKenmerkenAangepast(true, false),
		MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking(true, true),
		MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop(true, true),
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

	private final WijzigingToegestaanResultaat resultaat;

	private transient Verbintenis verbintenis;

	private transient Date oudeDatumBehaald;

	private transient Examenstatus oudeExamenstatus;

	private transient boolean oudeBekostigd;

	private transient Examendeelname deelname;

	public BronExamendeelnameWijzigingToegestaanCheck(Date oudeDatumBehaald,
			Examenstatus oudeExamenstatus, boolean oudeBekostigd, Examendeelname deelname)
	{
		this.verbintenis = deelname.getVerbintenis();
		this.oudeDatumBehaald = oudeDatumBehaald;
		this.oudeExamenstatus = oudeExamenstatus;
		this.oudeBekostigd = oudeBekostigd;
		this.deelname = deelname;

		resultaat = controleerWijzigingToegestaan();
		detach();
	}

	private void detach()
	{
		this.verbintenis = null;
		this.deelname = null;
		this.oudeExamenstatus = null;
		this.oudeDatumBehaald = null;
	}

	private WijzigingToegestaanResultaat controleerWijzigingToegestaan()
	{
		// Examendeelnames zijn alleen voor BO bekostigingsrelevant.
		if (!verbintenis.isBOVerbintenis())
		{
			return Geen_BO_VerbintenisOfExamendeelnameIsNogNietCommuniceerbaarMetBron;
		}

		// in geval van niet bekostigde deelname, is alles geoorloofd.
		if (!oudeBekostigd && !deelname.isBekostigd())
		{
			return ExamendeelnameIsNietBekostigd;
		}

		if (isToevoeging())
		{
			return controleerToevoeging();
		}
		if (isVerwijdering())
		{
			return controleerVerwijdering();
		}
		return controleerAanpassing();
	}

	private boolean isToevoeging()
	{
		return deelname.getDatumUitslag() != null
			&& Examenstatus.isNieuwVoorBronBo(oudeExamenstatus, deelname.getExamenstatus());
	}

	private WijzigingToegestaanResultaat controleerToevoeging()
	{
		TimeUtil timeUtil = TimeUtil.getInstance();

		Date datumBehaald = deelname.getDatumUitslag();
		int kalenderJaar = timeUtil.getYear(datumBehaald);
		Date peildatum = timeUtil.asDate(kalenderJaar, Calendar.OCTOBER, 1);

		return controleerBronStatusOpDatum(peildatum);
	}

	protected boolean getSecurityCheckWijzigingNaMutatiebeperking()
	{
		boolean toegestaan =
			new DataSecurityCheck(WIJZIGEN_NA_MUTATIEBEPERKING).isActionAuthorized(Enable.class);
		return (toegestaan);
	}

	private boolean isVerwijdering()
	{
		return Examenstatus.moetUitBronVerwijderdWordenBo(oudeExamenstatus, deelname
			.getExamenstatus());
	}

	private WijzigingToegestaanResultaat controleerVerwijdering()
	{
		TimeUtil timeUtil = TimeUtil.getInstance();

		int kalenderJaar = timeUtil.getYear(oudeDatumBehaald);
		Date peildatum = timeUtil.asDate(kalenderJaar, Calendar.OCTOBER, 1);

		return controleerBronStatusOpDatum(peildatum);
	}

	private WijzigingToegestaanResultaat controleerAanpassing()
	{
		TimeUtil timeUtil = TimeUtil.getInstance();

		WijzigingToegestaanResultaat wijzigingToegestaan = GeenBelangrijkeKenmerkenAangepast;

		int oudeKalenderJaar = timeUtil.getYear(oudeDatumBehaald);
		int nieuweKalenderJaar = timeUtil.getYear(deelname.getDatumUitslag());

		if (oudeKalenderJaar != nieuweKalenderJaar)
		{
			WijzigingToegestaanResultaat oudeDatumMag =
				controleerBronStatusOpDatum(timeUtil.asDate(oudeKalenderJaar, Calendar.OCTOBER, 1));
			WijzigingToegestaanResultaat nieuweDatumMag =
				controleerBronStatusOpDatum(timeUtil
					.asDate(nieuweKalenderJaar, Calendar.OCTOBER, 1));

			if (!oudeDatumMag.isToegestaan())
				return oudeDatumMag;
			if (!nieuweDatumMag.isToegestaan())
				return nieuweDatumMag;
			if (oudeDatumMag.isAccountantsmutatie())
				wijzigingToegestaan = oudeDatumMag;
			else
				wijzigingToegestaan = nieuweDatumMag;
		}
		if (wijzigingToegestaan == null || wijzigingToegestaan.isToegestaan()
			&& oudeBekostigd != deelname.isBekostigd())
		{
			WijzigingToegestaanResultaat wijzigingBekostigingMag =
				controleerBronStatusOpDatum(timeUtil.asDate(oudeKalenderJaar, Calendar.OCTOBER, 1));
			if (!wijzigingBekostigingMag.isToegestaan()
				|| wijzigingBekostigingMag.isAccountantsmutatie())
				wijzigingToegestaan = wijzigingBekostigingMag;
		}
		return wijzigingToegestaan;
	}

	private WijzigingToegestaanResultaat controleerBronStatusOpDatum(Date peildatum)
	{
		Locatie locatie = verbintenis.getLocatie();
		BronOnderwijssoort bronOnderwijssoort = verbintenis.getBronOnderwijssoort();

		BronStatus bronStatus = BronStatus.getBronStatus(peildatum, locatie, bronOnderwijssoort);

		if (bronStatus == null)
		{
			return GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie;
		}
		if (bronStatus.isAlleMutatiesToegestaan())
		{
			return AlleMutatiesZijnNogToegestaan;
		}
		if (bronStatus == BronStatus.MutatiebeperkingIngesteld
			|| bronStatus == BronStatus.MutatiestopIngesteld)
		{
			if (getSecurityCheckWijzigingNaMutatiebeperking())
				if (bronStatus == BronStatus.MutatiebeperkingIngesteld)
					return MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking;
				else
					return MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop;
			else
				return MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop;
		}
		return SchooljaarIsAfgeslotenVoorMutaties;
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
}
