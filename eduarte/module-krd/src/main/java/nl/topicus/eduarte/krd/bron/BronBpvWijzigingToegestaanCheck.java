package nl.topicus.eduarte.krd.bron;

import static nl.topicus.eduarte.krd.bron.BronBpvWijzigingToegestaanCheck.WijzigingToegestaanResultaat.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.krd.entities.bron.BronStatus;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

public class BronBpvWijzigingToegestaanCheck implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String WIJZIGEN_NA_MUTATIEBEPERKING = "WIJZIGEN_NA_MUTATIEBEPERKING";

	public enum WijzigingToegestaanResultaat
	{
		EinddatumMagNietNaBestaandeEinddatumLiggen(false, false),
		MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop(false,
				false),
		AfsluitdatumLigtVoorPeildatumVorigJaar(false, false),
		SchooljaarIsAfgeslotenVoorMutaties(false, false),
		GeenSchooljaarStatusGevonden(false, false),
		AanvullingBpvBedrijfsgegeven(true, false),
		AfsluitdatumsLiggenVoorPeildatum(true, false),
		AfsluitdatumsLiggenNaPeildatum(true, false),
		AfsluitdatumLigtNaPeildatum(true, false),
		AlleMutatiesZijnNogToegestaan(true, false),
		Geen_BO_VerbintenisOf_BPV_IsNogNietCommuniceerbaarMetBron(true, false),
		GeenBelangrijkeVeldenAangepast(true, false),
		GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie(true, false),
		IsGeen_BO_Of_VO_Verbintenis(true, false),
		LeerwegIsNiet_BBL_of_CBL(true, false),
		MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking(true, false),
		MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop(true, true),
		VerbintenisNogNietBekendBijBron(true, false),
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

		@Override
		public String toString()
		{
			return getReden();
		}

		public boolean isAccountantsmutatie()
		{
			return accountantsmutatie;
		}
	}

	private WijzigingToegestaanResultaat resultaat;

	private Schooljaar huidigeSchooljaar = Schooljaar.huidigSchooljaar();

	public BronBpvWijzigingToegestaanCheck(Date oudeBegindatum, Date oudeAfsluitdatum,
			Long oudeBpvBedrijfId, BPVStatus oudeBpvStatus, BPVInschrijving bpvInschrijving,
			Verbintenis verbintenis)
	{
		this.resultaat =
			zijnBPVWijzigingenToegestaan(oudeBegindatum, oudeAfsluitdatum, oudeBpvBedrijfId,
				oudeBpvStatus, verbintenis, bpvInschrijving);
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

	/**
	 * Indien Soort mutatie = “T” of “V”, en de leerweg van de inschrijving is BBL of
	 * CBL:De mutatie is alleen toegestaan als de Afsluitdatum BPV op of na 1 januari van
	 * het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij het 300-record
	 * aangeeft dat de accountant verantwoordelijk is.<br>
	 * 
	 * Indien Soort mutatie = “A”, en de leerweg van de inschrijving is BBL of CBL: De
	 * datum begin BPV mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij het
	 * 300-record aangeeft dat de accountant verantwoordelijk is<br>
	 * 
	 * Indien Soort mutatie = “A”, en de leerweg van de inschrijving is BBL of CBL: Het
	 * Leerbedrijf mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij een
	 * van de volgende situaties van kracht is: - het 300-record geeft aan dat de
	 * accountant verantwoordelijk is - het Leerbedrijf in Bron is niet gevuld
	 * 
	 * Indien Soort mutatie = “A”, en de leerweg van de inschrijving is BBL of CBL: De
	 * afsluitdatum mag alleen worden aangepast als de Afsluitdatum BPV in Bron en de
	 * Afsluitdatum BPV in de melding beiden voor, of beiden na 1 januari van het
	 * studiejaar waarvoor de mutatiestop van kracht is liggen, tenzij het 300-record
	 * aangeeft dat de accountant verantwoordelijk is
	 */
	private WijzigingToegestaanResultaat zijnBPVWijzigingenToegestaan(Date oudeBegindatum,
			Date oudeAfsluitdatum, Long oudeBpvBedrijfId, BPVStatus oudeStatus,
			Verbintenis verbintenis, BPVInschrijving bpvInschrijving)
	{
		if (!verbintenis.isBOVerbintenis()
			|| (BPVStatus.Voorlopig == bpvInschrijving.getStatus() && !(oudeStatus != null && oudeStatus
				.isBronCommuniceerbaar())))
		{
			return Geen_BO_VerbintenisOf_BPV_IsNogNietCommuniceerbaarMetBron;
		}

		MBOLeerweg leerweg = verbintenis.getOpleiding().getLeerweg();
		if (leerweg != MBOLeerweg.BBL && leerweg != MBOLeerweg.CBL)
		{
			return LeerwegIsNiet_BBL_of_CBL;
		}

		Date eenJan = getEenJanuari();
		Date eenJanVorigJaar = getEenJanuariVorigJaar();

		// Toevoeging of Verwijdering
		if (isVerwijdering(oudeStatus, bpvInschrijving.getStatus())
			|| isToevoeging(oudeStatus, bpvInschrijving.getStatus()))
		{
			return isVolgensBronStatusMutatieToegestaan(verbintenis, bpvInschrijving
				.getAfsluitdatum(), eenJan);
		}

		BronStatus bronStatus = getBronStatus(verbintenis);

		// Aanpassing
		resultaat = GeenBelangrijkeVeldenAangepast;

		// Afsluitdatum aangepast
		if (resultaat.isToegestaan() && oudeAfsluitdatum != null
			&& !oudeAfsluitdatum.equals(bpvInschrijving.getAfsluitdatum()))
		{
			if (resultaat.isToegestaan() && oudeAfsluitdatum.before(eenJanVorigJaar))
			{
				resultaat = AfsluitdatumLigtVoorPeildatumVorigJaar;
			}
			else if (resultaat.isToegestaan()
				&& (oudeAfsluitdatum.equals(eenJan) || oudeAfsluitdatum.after(eenJan))
				&& (bpvInschrijving.getAfsluitdatum().equals(eenJan) || bpvInschrijving
					.getAfsluitdatum().after(eenJan)))
			{
				resultaat = AfsluitdatumsLiggenNaPeildatum;
			}
			else if (resultaat.isToegestaan() && oudeAfsluitdatum.before(eenJan)
				&& bpvInschrijving.getAfsluitdatum().before(eenJan))
			{
				resultaat = AfsluitdatumsLiggenVoorPeildatum;
			}
			else if (resultaat.isToegestaan() && oudeAfsluitdatum.before(eenJan))
				resultaat =
					isVolgensBronStatusMutatieToegestaan(verbintenis, oudeAfsluitdatum, eenJan);
			else if (resultaat.isToegestaan() && bpvInschrijving.getAfsluitdatum().before(eenJan))
				resultaat =
					isVolgensBronStatusMutatieToegestaan(verbintenis, bpvInschrijving
						.getAfsluitdatum(), eenJan);
		}
		// begindatum of bedrijfsgegevens aangepast
		if (resultaat.isToegestaan() && !bpvInschrijving.getBegindatum().equals(oudeBegindatum))
		{
			resultaat =
				isVolgensBronStatusMutatieToegestaan(verbintenis,
					bpvInschrijving.getAfsluitdatum(), eenJan);
		}
		// dit kan in EduArte niet voorkomen aangezien een BPV bedrijf verplicht is
		// als de BPV status op Volledig komt te staan, maar is volgens de BRON
		// specificaties wel mogelijk.
		if (resultaat.isToegestaan() && oudeBpvBedrijfId == null
			&& bpvInschrijving.getBpvBedrijf().getId() != null)
		{
			if (bronStatus != null && bronStatus.isGeenMutatieToegestaan())
				resultaat = SchooljaarIsAfgeslotenVoorMutaties;
			else
				resultaat = AanvullingBpvBedrijfsgegeven;
		}
		else if (resultaat.isToegestaan() && oudeBpvBedrijfId != null
			&& !oudeBpvBedrijfId.equals(bpvInschrijving.getBedrijfsgegeven().getId()))
		{
			resultaat =
				isVolgensBronStatusMutatieToegestaan(verbintenis,
					bpvInschrijving.getAfsluitdatum(), eenJan);
		}
		return resultaat;
	}

	private boolean isToevoeging(BPVStatus oudeStatus, BPVStatus status)
	{
		boolean isNogNietBekendBijBron = oudeStatus == null || !oudeStatus.isBronCommuniceerbaar();
		return isNogNietBekendBijBron && status.isBronCommuniceerbaar();
	}

	private boolean isVerwijdering(BPVStatus oudeStatus, BPVStatus status)
	{
		boolean isBekendBijBron = oudeStatus != null && oudeStatus.isBronCommuniceerbaar();
		return isBekendBijBron && !status.isBronCommuniceerbaar();
	}

	private Date getEenJanuariVorigJaar()
	{
		Date eenJanVorigJaar =
			TimeUtil.getInstance().getFirstDayOfMonth(huidigeSchooljaar.getStartJaar(),
				Calendar.JANUARY);
		return eenJanVorigJaar;
	}

	private Date getEenJanuari()
	{
		Date eenJan =
			TimeUtil.getInstance().getFirstDayOfMonth(huidigeSchooljaar.getEindJaar(),
				Calendar.JANUARY);
		return eenJan;
	}

	private WijzigingToegestaanResultaat isVolgensBronStatusMutatieToegestaan(
			Verbintenis verbintenis, Date datum, Date peildatum)
	{
		if (datum != null && (datum.equals(peildatum) || datum.after(peildatum)))
		{
			return AfsluitdatumLigtNaPeildatum;
		}
		BronStatus bronStatus = getBronStatus(verbintenis);
		if (bronStatus == null)
		{
			return GeenSchooljaarStatusGevonden;
		}
		if (bronStatus.isAlleMutatiesToegestaan())
		{
			return AlleMutatiesZijnNogToegestaan;
		}
		if (bronStatus == BronStatus.MutatiebeperkingIngesteld
			|| bronStatus == BronStatus.MutatiestopIngesteld)
		{
			if (getSecurityCheckWijzigingNaMutatiebeperking())
				return bronStatus == BronStatus.MutatiebeperkingIngesteld
					? MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking
					: MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop;
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

	private BronStatus getBronStatus(Verbintenis verbintenis)
	{
		return BronStatus.getBronStatus(verbintenis);
	}
}
