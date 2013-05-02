package nl.topicus.eduarte.krd.bron;

import static nl.topicus.onderwijs.duo.bron.BRONConstants.*;

import java.util.Comparator;
import java.util.Date;

import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.NT2Vaardigheden;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.VakgegevensRecord;

public class BronBveAanleverRecordComparator implements Comparator<BronBveAanleverRecord>
{
	@Override
	public int compare(BronBveAanleverRecord o1, BronBveAanleverRecord o2)
	{
		int type1 = o1.getRecordType();
		int type2 = o2.getRecordType();

		if ((type1 == BVE_AANLEVERING_ED_VAKGEGEVENS || type1 == BVE_AANLEVERING_ED_NT2VAARDIGHEDEN)
			&& (type2 == BVE_AANLEVERING_ED_VAKGEGEVENS || type2 == BVE_AANLEVERING_ED_NT2VAARDIGHEDEN))
		{
			if (type1 == BVE_AANLEVERING_ED_VAKGEGEVENS && type2 == BVE_AANLEVERING_ED_VAKGEGEVENS)
			{
				return ((VakgegevensRecord) o1).getVakvolgnummer()
					- ((VakgegevensRecord) o2).getVakvolgnummer();
			}
			if (type1 == BVE_AANLEVERING_ED_VAKGEGEVENS
				&& type2 == BVE_AANLEVERING_ED_NT2VAARDIGHEDEN)
			{
				Integer vak1 = ((VakgegevensRecord) o1).getVakvolgnummer();
				Integer vak2 = ((NT2Vaardigheden) o2).getVakvolgnummer();
				if (vak1 == vak2)
					return -1;
				return vak1 - vak2;
			}
			if (type1 == BVE_AANLEVERING_ED_NT2VAARDIGHEDEN
				&& type2 == BVE_AANLEVERING_ED_VAKGEGEVENS)
			{
				Integer vak1 = ((NT2Vaardigheden) o1).getVakvolgnummer();
				Integer vak2 = ((VakgegevensRecord) o2).getVakvolgnummer();
				if (vak1 == vak2)
					return 1;
				return vak1 - vak2;
			}
			if (type1 == BVE_AANLEVERING_ED_VAKGEGEVENS && type2 == BVE_AANLEVERING_ED_VAKGEGEVENS)
			{
				Integer vak1 = ((NT2Vaardigheden) o1).getVakvolgnummer();
				Integer vak2 = ((NT2Vaardigheden) o2).getVakvolgnummer();

				return vak1 - vak2;
			}
		}
		else if (type1 == BVE_AANLEVERING_BO_PERIODEGEGEVENS
			&& type2 == BVE_AANLEVERING_BO_PERIODEGEGEVENS)
		{
			PeriodegegevensInschrijvingRecord p1 = o1;
			PeriodegegevensInschrijvingRecord p2 = o2;

			Date d1 = p1.getDatumIngangPeriodegegevensInschrijving();
			Date d2 = p2.getDatumIngangPeriodegegevensInschrijving();

			return d1.compareTo(d2);
		}
		return type1 - type2;
	}
}
