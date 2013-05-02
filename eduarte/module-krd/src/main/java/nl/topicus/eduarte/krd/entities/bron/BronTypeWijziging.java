package nl.topicus.eduarte.krd.entities.bron;

import java.util.Arrays;
import java.util.List;

import nl.topicus.onderwijs.duo.bron.BRONConstants;

public enum BronTypeWijziging
{
	Deelnemer(BRONConstants.BVE_AANLEVERING_SLEUTELGEGEVENS,
			BRONConstants.BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS,
			BRONConstants.BVE_AANLEVERING_PERSOONSGEGEVENS,
			BRONConstants.VO_AANLEVERING_VO_INSCHRIJFGEGEVENS),
	Verbintenis(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS,
			BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS,
			BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS,
			BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS),
	BPV(BRONConstants.BVE_AANLEVERING_BO_BPVGEGEVENS),
	Examen(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS,
			BRONConstants.BVE_AANLEVERING_ED_RESULTAATGEGEVENS,
			BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS,
			BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN,
			BRONConstants.BVE_AANLEVERING_VAVO_EXAMENGEGEVENS,
			BRONConstants.BVE_AANLEVERING_VAVO_VAKGEGEVENS,
			BRONConstants.VO_AANLEVERING_VO_EXAMENRESULTAAT);

	List<Integer> records;

	private BronTypeWijziging(Integer... recordNummers)
	{
		records = Arrays.asList(recordNummers);
	}

	public List<Integer> getRecords()
	{
		return records;
	}

	public static BronTypeWijziging getTypeWijziging(Integer record)
	{
		for (BronTypeWijziging typeWijziging : BronTypeWijziging.values())
		{
			if (typeWijziging.getRecords().contains(record))
				return typeWijziging;
		}
		return null;
	}
}
