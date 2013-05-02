package nl.topicus.eduarte.participatie.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakParticipant;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.UitnodigingStatus;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.entities.participatie.olc.OlcWaarneming;

public class OlcUtil
{
	public enum OlcVerwerkingsCode
	{
		Verwerkt,
		IsAlVerwerkt,
		GeenEindtijd,
		DeelnemerHeeftAlAanwezigWaarneming;
	}

	private List<OlcWaarneming> aantemakenWaarnemingen = new ArrayList<OlcWaarneming>();

	public OlcVerwerkingsCode CompleteWaarnemingAanmaken(OlcWaarneming olcWaarneming)
	{
		if (olcWaarneming.isVerwerkt())
			return OlcVerwerkingsCode.IsAlVerwerkt;
		if (olcWaarneming.getEindTijd() == null)
			return OlcVerwerkingsCode.GeenEindtijd;

		OlcWaarneming originalWaarneming = olcWaarneming;

		AfspraakDataAccessHelper helper =
			DataAccessRegistry.getHelper(AfspraakDataAccessHelper.class);

		Date beginDatumTijd =
			TimeUtil.getInstance().mergeDateAndTime(olcWaarneming.getDatum(),
				olcWaarneming.getBeginTijd());

		Date eindDatumTijd =
			TimeUtil.getInstance().mergeDateAndTime(olcWaarneming.getDatum(),
				olcWaarneming.getEindTijd());

		List<Afspraak> list =
			helper.getOverlappendeAfspraken(olcWaarneming.getDeelnemer(), beginDatumTijd,
				eindDatumTijd);

		begin(olcWaarneming, list, beginDatumTijd, eindDatumTijd);
		genereerWaarnemingen();

		originalWaarneming.setVerwerkt(true);
		originalWaarneming.saveOrUpdate();
		originalWaarneming.commit();

		return OlcVerwerkingsCode.Verwerkt;
	}

	private void createWaarneming(OlcWaarneming olcWaarneming)
	{
		Date beginDatumTijd;
		Date eindDatumTijd;
		beginDatumTijd =
			TimeUtil.getInstance().mergeDateAndTime(olcWaarneming.getDatum(),
				olcWaarneming.getBeginTijd());

		eindDatumTijd =
			TimeUtil.getInstance().mergeDateAndTime(olcWaarneming.getDatum(),
				olcWaarneming.getEindTijd());

		AfspraakParticipant participant = new AfspraakParticipant();
		participant.setDeelnemer(olcWaarneming.getDeelnemer());
		participant.setUitnodigingStatus(UitnodigingStatus.DIRECTE_PLAATSING);

		Afspraak OLCAfspraak = new Afspraak();
		OLCAfspraak.addParticipant(participant);
		OLCAfspraak.setBeginDatumTijd(beginDatumTijd);
		OLCAfspraak.setEindDatumTijd(eindDatumTijd);
		OLCAfspraak.setAfspraakType(olcWaarneming.getOlcLocatie().getAfspraakType());
		OLCAfspraak.setAfspraakLocatie(olcWaarneming.getOlcLocatie().toString());
		OLCAfspraak.setTitel(olcWaarneming.getOlcLocatie().getAfspraakType().getNaam());
		OLCAfspraak.setOmschrijving(olcWaarneming.getOlcLocatie().getAfspraakType()
			.getOmschrijving());
		// OLCAfspraak.setOrganisatie(olcWaarneming.getOrganisatie());
		OLCAfspraak.setPresentieRegistratieVerwerkt(true);
		OLCAfspraak.setPresentieRegistratieVerplicht(true);
		OLCAfspraak.setOrganisatieEenheid(olcWaarneming.getOlcLocatie().getOrganisatieEenheid());

		int duurInMinuten =
			TimeUtil.getInstance().getDifferenceInMinutes(olcWaarneming.getBeginTijd(),
				olcWaarneming.getEindTijd());

		if (duurInMinuten > 0)
			OLCAfspraak.setMinutenIIVO((duurInMinuten * olcWaarneming.getOlcLocatie()
				.getAfspraakType().getPercentageIIVO()) / 100);

		if (olcWaarneming.getMedewerker() != null)
			OLCAfspraak.setAuteur(olcWaarneming.getMedewerker().getPersoon());

		OLCAfspraak.saveOrUpdate();

		Waarneming afspraakWaarneming = new Waarneming();
		afspraakWaarneming.setAfspraak(OLCAfspraak);
		afspraakWaarneming.setBeginDatumTijd(beginDatumTijd);
		afspraakWaarneming.setEindDatumTijd(eindDatumTijd);
		afspraakWaarneming.setAfgehandeld(true);
		// afspraakWaarneming.setOrganisatie(olcWaarneming.getOrganisatie());
		afspraakWaarneming.setWaarnemingSoort(WaarnemingSoort.Aanwezig);
		afspraakWaarneming.setDeelnemer(olcWaarneming.getDeelnemer());
		afspraakWaarneming.saveOrUpdate();

		// olcWaarneming.setVerwerkt(true);
		// olcWaarneming.batchSaveOrUpdate();

		participant.setAfspraak(OLCAfspraak);
		participant.saveOrUpdate();

		participant.commit();
	}

	private void begin(OlcWaarneming olcWaarneming, List<Afspraak> list, Date olcBegin, Date olcEind)
	{

		Date tempBegin = olcBegin;
		Date tempEind = olcEind;

		if (!list.isEmpty())
		{
			Afspraak afspraak = list.get(0);
			if (afspraak.getEindDatumTijd().before(tempBegin))
			{
				list.remove(0);
				begin(olcWaarneming, list, tempBegin, tempEind);
			}
			else if (afspraak.isPresentieRegistratieVerwerkt())
			{
				Waarneming afspraakWaarneming =
					afspraak.getWaarneming(olcWaarneming.getDeelnemer());

				if (afspraakWaarneming != null
					&& afspraakWaarneming.getWaarnemingSoort().equals(WaarnemingSoort.Aanwezig))
				{

					if (!afspraak.getBeginDatumTijd().after(tempBegin)
						&& !afspraak.getEindDatumTijd().before(tempEind))
					{
						// OLC valt binnen afspraak dus hoeft er geen waarneming worden
						// aangemaakt
						return;
					}

					if (!afspraak.getBeginDatumTijd().after(tempBegin))
					{
						// Afspraak beginTijd ligt voor olc tijd
						// * Olc beginTijd is Afspraak BeginTijd

						tempBegin = afspraak.getEindDatumTijd();

						list.remove(0);
						begin(olcWaarneming, list, tempBegin, tempEind);
					}
					else if (!afspraak.getBeginDatumTijd().after(tempEind))
					{
						// Afspraak beginTijd ligt voor olc eindTijd
						// * Olc eindTijd is Afspraak BeginTijd

						OlcWaarneming copieWaarneming = copieWaarnemingMaken(olcWaarneming);
						copieWaarneming.setBeginTijd(tempBegin);
						copieWaarneming.setEindTijd(afspraak.getBeginDatumTijd());
						aantemakenWaarnemingen.add(copieWaarneming);

						tempBegin = afspraak.getEindDatumTijd();

						list.remove(0);
						begin(olcWaarneming, list, tempBegin, tempEind);
					}
					else
					{
						OlcWaarneming copieWaarneming = copieWaarnemingMaken(olcWaarneming);
						copieWaarneming.setBeginTijd(tempBegin);
						copieWaarneming.setEindTijd(tempEind);
						aantemakenWaarnemingen.add(copieWaarneming);

						list.remove(0);
						begin(olcWaarneming, list, tempBegin, tempEind);
					}
				}
				else
				{
					list.remove(0);
					begin(olcWaarneming, list, tempBegin, tempEind);
				}
			}
			else
			{
				list.remove(0);
				begin(olcWaarneming, list, tempBegin, tempEind);
			}
		}
		else
		{
			if (tempBegin.before(tempEind))
			{
				OlcWaarneming copieWaarneming = copieWaarnemingMaken(olcWaarneming);
				copieWaarneming.setBeginTijd(tempBegin);
				copieWaarneming.setEindTijd(tempEind);
				aantemakenWaarnemingen.add(copieWaarneming);
			}
		}
	}

	private OlcWaarneming copieWaarnemingMaken(OlcWaarneming olcWaarneming)
	{
		OlcWaarneming copieWaarneming = new OlcWaarneming();
		copieWaarneming.setVerwerkt(olcWaarneming.isVerwerkt());
		copieWaarneming.setOlcLocatie(olcWaarneming.getOlcLocatie());
		copieWaarneming.setBeginTijd(TimeUtil.getInstance().getTimeWithoutSeconds(
			olcWaarneming.getBeginTijd()));
		copieWaarneming.setEindTijd(TimeUtil.getInstance().getTimeWithoutSeconds(
			olcWaarneming.getEindTijd()));
		copieWaarneming.setDeelnemer(olcWaarneming.getDeelnemer());
		copieWaarneming.setMedewerker(olcWaarneming.getMedewerker());
		copieWaarneming.setDatum(olcWaarneming.getDatum());
		copieWaarneming.setAfspraakType(olcWaarneming.getAfspraakType());

		return copieWaarneming;
	}

	private void genereerWaarnemingen()
	{
		for (OlcWaarneming waarneming : aantemakenWaarnemingen)
		{
			if (waarneming.getBeginTijd().after(waarneming.getEindTijd()))
				throw new IllegalStateException("begintijd: " + waarneming.getBeginTijd()
					+ " ligt na eindtijd: " + waarneming.getEindTijd());
			createWaarneming(waarneming);
		}
	}
}
