package nl.topicus.eduarte.krd.bron;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.BronEntiteitStatus;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.IBronStatusEntiteit;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.entities.bron.BronTypeWijziging;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding.VoMeldingSoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;

public class BronUtils
{
	public static void updateStatussenNaVerwijderen(IBronMelding bronMelding)
	{
		for (IBronStatusEntiteit entiteit : getEntiteiten(bronMelding))
		{
			entiteit.setBronStatus(entiteit.getBronStatus().getNietInBehandelingStatus());
			((Entiteit) entiteit).saveOrUpdate();
		}
	}

	public static void updateStatussenNaAanmaken(IBronMelding bronMelding)
	{
		for (IBronStatusEntiteit entiteit : getEntiteiten(bronMelding))
		{
			BronEntiteitStatus huidigeStatus = entiteit.getBronStatus();
			if (huidigeStatus == null)
			{
				huidigeStatus = BronEntiteitStatus.Geen;
			}
			if (bronMelding.isVerwijderd())
				entiteit.setBronStatus(huidigeStatus.getNietInBehandelingStatus());
			else
				entiteit.setBronStatus(huidigeStatus.getWachtrijStatus());

		}
	}

	public static void updateStatussenNaBatchAanmaken(List<IBronMelding> bronMeldingen)
	{
		for (IBronMelding bronMelding : bronMeldingen)
		{
			for (IBronStatusEntiteit entiteit : getEntiteiten(bronMelding))
			{
				entiteit.setBronStatus(entiteit.getBronStatus().getInBehandelingStatus());
				((Entiteit) entiteit).saveOrUpdate();
			}
		}
	}

	public static void updateStatussenNaTerugkoppeling(IBronMelding bronMelding, boolean goedgekeurd)
	{
		for (IBronStatusEntiteit entiteit : getEntiteiten(bronMelding))
		{
			entiteit.setBronStatus(entiteit.getBronStatus().getVervolgStatus(goedgekeurd));
			entiteit.setBronDatum(TimeUtil.getInstance().currentDateTime());
			((Entiteit) entiteit).saveOrUpdate();
		}
	}

	private static List<IBronStatusEntiteit> getEntiteiten(IBronMelding bronMelding)
	{
		List<IBronStatusEntiteit> ret = new ArrayList<IBronStatusEntiteit>();
		Deelnemer deelnemer = bronMelding.getDeelnemer();
		if (bronMelding instanceof BronAanleverMelding)
		{
			BronAanleverMelding melding = (BronAanleverMelding) bronMelding;
			for (BronBveAanleverRecord record : melding.getMeldingen())
			{
				BronTypeWijziging typeWijziging =
					BronTypeWijziging.getTypeWijziging(record.getRecordType());
				if (typeWijziging != null)
				{
					switch (typeWijziging)
					{
						case Deelnemer:
							ret.add(deelnemer);
							break;
						case Verbintenis:
							if (!deelnemer.getBronStatus().isGemeldAanBron())
								ret.add(deelnemer);
							ret.add(melding.getVerbintenis());
							break;
						case BPV:
							BPVInschrijving bpvInschrijving = record.getBpvInschrijving();
							if (bpvInschrijving != null)
								ret.add(bpvInschrijving);
							break;
						case Examen:
							OnderwijsproductAfnameContext oac = record.getAfnameContext();
							if (oac != null)
								ret.add(oac);
							Examendeelname examen = record.getExamenDeelname();
							if (examen != null)
								ret.add(examen);
							break;
					}
				}
			}
		}
		if (bronMelding instanceof BronInschrijvingsgegevensVOMelding)
		{
			BronInschrijvingsgegevensVOMelding melding =
				(BronInschrijvingsgegevensVOMelding) bronMelding;
			if (melding.getSoortMutatie() == SoortMutatie.Toevoeging)
			{
				ret.add(melding.getVerbintenis());
				ret.add(deelnemer);
			}
			else if (VoMeldingSoort.I == melding.getSoort())
				ret.add(melding.getVerbintenis());
			else if (VoMeldingSoort.W == melding.getSoort())
				ret.add(deelnemer);
		}
		if (bronMelding instanceof BronExamenresultaatVOMelding)
		{
			BronExamenresultaatVOMelding melding = (BronExamenresultaatVOMelding) bronMelding;
			Examendeelname deelname = melding.getExamendeelname();
			if (deelname != null)
				ret.add(deelname);
		}
		return ret;
	}
}
