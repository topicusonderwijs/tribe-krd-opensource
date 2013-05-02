package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.participatie.enums.IParticipatieBlokObject;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtUtil.Blok;

/**
 * @author vanderkamp
 */
public class BlokkenGenerator
{

	private int startUur;

	private int eindUur;

	private int blokSize;

	public BlokkenGenerator(int startUur, int eindUur, int blokSize)
	{
		this.startUur = startUur;
		this.eindUur = eindUur;
		this.blokSize = blokSize;
	}

	@SuppressWarnings("null")
	public List<Blok> maakBlokken(Date datum, List< ? extends IParticipatieBlokObject> lessen)
	{
		int aantalBlokken = getAantalBlokkenPerDag();
		List<Blok> res = new ArrayList<Blok>(aantalBlokken);
		Date tijd = TimeUtil.getInstance().asTime(0);
		tijd = TimeUtil.getInstance().addHours(tijd, startUur - 1);
		int huidigeBeginIndex = 0;
		Date huidigeBegintijd = null;
		Date laatsteEindtijd = tijd;
		IParticipatieBlokObject huidigBlokObject = null;
		for (int blokIndex = 0; blokIndex < aantalBlokken; blokIndex++)
		{
			if (huidigeBegintijd == null)
			{
				// We zitten buiten een blok.
				if ((huidigBlokObject = heeftLes(lessen, tijd, datum)) != null)
				{
					// Maak een leeg blok aan.
					Blok blok =
						new Blok(blokIndex - huidigeBeginIndex, laatsteEindtijd, tijd, null, null);
					res.add(blok);
					// Begin een blok.
					huidigeBegintijd = tijd;
					huidigeBeginIndex = blokIndex;
				}
			}
			else
			{
				// We zitten nu binnen een blok.
				IParticipatieBlokObject nextBlokObject = heeftLes(lessen, tijd, datum);
				if (nextBlokObject != huidigBlokObject)
				{
					// Eindig het huidige blok.
					Blok blok =
						new Blok(blokIndex - huidigeBeginIndex, huidigeBegintijd, tijd,
							huidigBlokObject.getCssClass(), huidigBlokObject.getTitle());
					res.add(blok);
					huidigeBeginIndex = blokIndex;
					huidigBlokObject = nextBlokObject;
					if (nextBlokObject == null)
					{
						// Doe een reset naar een leeg blok.
						laatsteEindtijd = tijd;
						huidigeBegintijd = null;
					}
					else
					{
						// Doe een reset naar een gevuld blok.
						huidigeBegintijd = tijd;
					}
				}
			}
			tijd = TimeUtil.getInstance().addMinutes(tijd, blokSize);
		}
		// Maak het huidige blok af.
		if (huidigeBegintijd == null)
		{
			// Maak een leeg blok aan.
			Blok blok =
				new Blok(aantalBlokken - huidigeBeginIndex, laatsteEindtijd, tijd, null, null);
			res.add(blok);
		}
		else
		{
			// Eindig het huidige blok.
			if (res.size() == 1 && res.get(0).getAantalBlokken() == 0)
				res.remove(0);
			Blok blok =
				new Blok(aantalBlokken - huidigeBeginIndex, huidigeBegintijd, tijd,
					huidigBlokObject.getCssClass(), huidigBlokObject.getTitle());
			res.add(blok);
		}

		return res;
	}

	private IParticipatieBlokObject heeftLes(List< ? extends IParticipatieBlokObject> lessen,
			Date tijd, Date datum)
	{

		Date tijdDatum = TimeUtil.getInstance().setTimeOnDate(datum, new Time(tijd.getTime()));
		for (IParticipatieBlokObject les : lessen)
		{
			if (les.getEindDatumTijd() != null && les.isActiefOpDatum(tijdDatum))
			{
				return les;
			}
		}
		return null;
	}

	private final int getAantalBlokkenPerDag()
	{
		return ((eindUur - startUur) * 60) / blokSize;
	}
}
