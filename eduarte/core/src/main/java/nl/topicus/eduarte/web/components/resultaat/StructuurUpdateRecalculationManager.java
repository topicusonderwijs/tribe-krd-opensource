package nl.topicus.eduarte.web.components.resultaat;

import static nl.topicus.eduarte.web.components.resultaat.ResultatenModel.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.model.IModel;

public class StructuurUpdateRecalculationManager extends JobRecalculationManager
{
	private static final long serialVersionUID = 1L;

	public StructuurUpdateRecalculationManager()
	{
	}

	public void recalculate(List<Toets> toetsen)
	{
		for (Toets curToets : toetsen)
			addRecalcuation(curToets);

		recalculate();
	}

	public void addRecalcuation(Toets toets)
	{
		List<Deelnemer> deelnemers =
			DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).getDeelnemersMetResultaten(
				toets);
		for (Deelnemer curDeelnemer : deelnemers)
		{
			addRecalcuation(toets, curDeelnemer);
		}
	}

	@Override
	protected void setMedewerkerOpSamengesteld(Resultaat berekend, Resultaat vorigResultaat)
	{
		if (vorigResultaat != null)
			berekend.setIngevoerdDoor(vorigResultaat.getIngevoerdDoor());
	}

	@Override
	protected void recalculateCijfers(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		if (recalcKey.getToets().hasScoreschaal() || recalcKey.getToets().getStudiepunten() != null)
		{
			List<Resultaat> resultatenVoorToets = getPogingen(recalcKey, resultaten, false);
			for (Resultaat curResultaat : resultatenVoorToets)
			{
				Resultaat newResultaat = curResultaat.copy();
				newResultaat.setActueel(true);
				newResultaat.setOverschrijft(curResultaat);
				newResultaat.appendBerekening("Dit resultaat is voor een herberekening gekopiëerd");
				newResultaat.save();

				curResultaat.setActueel(false);
				curResultaat.setInSamengesteld(false);
				curResultaat.setGeldend(false);
				curResultaat.update();

				getFromMap(recalcKey, resultaten).get(
					POGING_START_IDX + curResultaat.getHerkansingsnummer()).add(0, newResultaat);
			}
			super.recalculateCijfers(recalcKey, resultaten);
		}
	}

	@Override
	protected Resultaat determineGeldendResultaat(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten,
			Map<Resultaat, Collection<Resultaat>> markeerInSamengesteld)
	{
		Resultaat newGeldend =
			super.determineGeldendResultaat(recalcKey, resultaten, markeerInSamengesteld);
		if (recalcKey.getToets().isSamengesteldMetHerkansing())
		{
			// Als samengesteldMetHerkansing aan is gezet, kan het zijn dat er een oud
			// geldend resultaat achterblijft, deze moet overschreven worden.
			Resultaat oldGeldend = getGeldendResultaat(recalcKey, resultaten, true, false, 0);
			if (oldGeldend != null && oldGeldend.getHerkansingsnummer() == -1)
			{
				oldGeldend.setActueel(false);
				oldGeldend.setGeldend(false);
				if (newGeldend == null)
				{
					if (oldGeldend.isNullResultaat())
					{
						newGeldend = oldGeldend;
					}
					else
					{
						newGeldend = oldGeldend.copy();
						newGeldend.setCijferOfWaarde(null);
					}
					newGeldend.setHerkansingsnummer(0);
				}

				newGeldend.setGeldend(true);
				newGeldend.setActueel(true);
				if (newGeldend != oldGeldend)
					newGeldend.setOverschrijft(oldGeldend);
				oldGeldend.update();
				newGeldend.saveOrUpdate();

				if (!markeerInSamengesteld.containsKey(newGeldend))
				{
					// zorg dat het gekopieerde of hergebruikte resultaat in de map van
					// gebruikte resultaten staat
					boolean found = false;
					for (Map.Entry<Resultaat, Collection<Resultaat>> curEntry : markeerInSamengesteld
						.entrySet())
					{
						if (curEntry.getKey() != null
							&& curEntry.getKey().getHerkansingsnummer() == 0)
						{
							markeerInSamengesteld.put(newGeldend, curEntry.getValue());
							found = true;
							break;
						}
					}
					if (!found)
						markeerInSamengesteld.put(newGeldend, markeerInSamengesteld.get(null));
				}
			}
		}
		return newGeldend;
	}
}
