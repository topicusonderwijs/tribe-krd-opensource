package nl.topicus.eduarte.web.components.resultaat;

import static nl.topicus.eduarte.web.components.resultaat.ResultatenModel.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.resultaatstructuur.DeelnemerToetsBevriezing;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsVerwijzing;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat.Resultaatsoort;

import org.apache.wicket.model.IModel;

public class RecalculationManager extends AbstractResultaatCalculator
{
	private static final long serialVersionUID = 1L;

	private IModel<Medewerker> medewerkerModel;

	protected ResultatenModel resultatenModel;

	public RecalculationManager()
	{
	}

	public RecalculationManager(IModel<Medewerker> medewerkerModel, ResultatenModel resultatenModel)
	{
		this.medewerkerModel = medewerkerModel;
		this.resultatenModel = resultatenModel;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerkerModel = ModelFactory.getModel(medewerker);
	}

	@Override
	protected void recalculate(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		Map<Resultaat, Collection<Resultaat>> markeerInSamengesteld = Collections.emptyMap();
		if (recalcKey.getToets().isSamengesteld())
			markeerInSamengesteld = recalculateSamengesteld(recalcKey, resultaten);
		else
			recalculateCijfers(recalcKey, resultaten);

		Resultaat geldend = determineGeldendResultaat(recalcKey, resultaten, markeerInSamengesteld);
		if (geldend != null && markeerInSamengesteld.containsKey(geldend))
		{
			markeerGebruiktInSamengesteld(markeerInSamengesteld.get(geldend));
		}
		if (geldend != null)
			pushResultaatOverVerwijzingen(resultaten, geldend);
		getFromMap(recalcKey, resultaten).get(RESULTAAT_IDX).add(0, geldend);
	}

	@Override
	protected List<List<Resultaat>> fetchResultaten(ResultaatKey key)
	{
		List<List<Resultaat>> resultatenVoorToets = super.fetchResultaten(key);
		if (resultatenModel != null)
		{
			resultatenModel.insertResultaten(key, resultatenVoorToets);
		}
		return resultatenVoorToets;
	}

	protected Map<Resultaat, Collection<Resultaat>> recalculateSamengesteld(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		clearGebruiktInSamengesteld(recalcKey, resultaten);

		Map<Resultaat, Collection<Resultaat>> gebruikteResultaten =
			new HashMap<Resultaat, Collection<Resultaat>>();
		Toets pogingBasis = getPogingBasis(recalcKey, resultaten);
		for (int curPogingNr : determinePogingNrs(pogingBasis))
		{
			Resultaat thisGeldendResultaat =
				getGeldendResultaat(recalcKey, resultaten, true, false, curPogingNr);
			if (thisGeldendResultaat != null && thisGeldendResultaat.isGefixeerd()
				&& !thisGeldendResultaat.isNullOfZonderCijferOfWaarde())
			{
				if (!thisGeldendResultaat.isGeldend())
				{
					thisGeldendResultaat.setGeldend(true);
					thisGeldendResultaat.update();
				}
				continue;
			}

			Toets toets = recalcKey.getToets();
			Deelnemer deelnemer = recalcKey.getDeelnemer();

			Map<Toets, Resultaat> geldendOfAltSet =
				getGeldendOfAlternatiefResultaten(recalcKey, resultaten, curPogingNr);
			Map<Toets, Resultaat> alternatiefSet = getAlternatiefResultaten(recalcKey, resultaten);

			Resultaat geldendOfAlt =
				createSamengesteldResultaat(toets, deelnemer, curPogingNr, geldendOfAltSet);
			Resultaat alternatief =
				createSamengesteldResultaat(toets, deelnemer, curPogingNr, alternatiefSet);

			Resultaat berekend = getHoogsteResultaat(geldendOfAlt, alternatief);
			recordBerekening(berekend, "Er is gebruik gemaakt van de "
				+ (berekend == geldendOfAlt ? "geldende" : "alternatieve")
				+ " resultaten van de onderliggende toetsen");

			if (berekend.isNullResultaat()
				&& (thisGeldendResultaat == null || thisGeldendResultaat.isNullResultaat()))
			{
				if (thisGeldendResultaat != null)
				{
					gebruikteResultaten.put(thisGeldendResultaat, berekend == geldendOfAlt
						? geldendOfAltSet.values() : alternatiefSet.values());
				}
				if (curPogingNr == 1)
				{
					// deze null entry dient om op terug te vallen in speciale gevallen
					gebruikteResultaten.put(null, geldendOfAltSet.values());
				}
				continue;
			}

			berekend.setHerkansingsnummer(curPogingNr - 1);

			setMedewerkerOpSamengesteld(berekend, thisGeldendResultaat);
			if (thisGeldendResultaat != null)
			{
				berekend.setOverschrijft(thisGeldendResultaat);
				thisGeldendResultaat.setGeldend(false);
				thisGeldendResultaat.setActueel(false);
				thisGeldendResultaat.setInSamengesteld(false);
				thisGeldendResultaat.update();
			}

			berekend.save();
			getFromMap(recalcKey, resultaten).get(OFFSET + curPogingNr).add(0, berekend);
			if (geldendOfAlt.isSaved())
				gebruikteResultaten.put(geldendOfAlt, geldendOfAltSet.values());
			if (alternatief.isSaved())
				gebruikteResultaten.put(alternatief, alternatiefSet.values());
		}
		return gebruikteResultaten;
	}

	protected void clearGebruiktInSamengesteld(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		Deelnemer deelnemer = recalcKey.getDeelnemer();
		for (Toets curChild : recalcKey.getToets().getChildren())
		{
			ResultaatKey childKey = new ResultaatKey(curChild, deelnemer);
			for (Resultaat curResultaat : getPogingen(childKey, resultaten, true))
			{
				curResultaat.setInSamengesteld(false);
				curResultaat.update();
			}
			Resultaat alt = getAlternatiefResultaat(childKey, resultaten);
			if (alt != null)
			{
				alt.setInSamengesteld(false);
				alt.update();
			}
		}
	}

	protected void markeerGebruiktInSamengesteld(Collection<Resultaat> gebruikteResultaten)
	{
		if (gebruikteResultaten == null)
			return;

		for (Resultaat curResultaat : gebruikteResultaten)
		{
			if (curResultaat != null && !curResultaat.isNullOfZonderCijferOfWaarde())
			{
				curResultaat.setInSamengesteld(true);
				curResultaat.update();
			}
		}
	}

	@SuppressWarnings("unused")
	protected void setMedewerkerOpSamengesteld(Resultaat berekend, Resultaat vorigResultaat)
	{
		berekend.setIngevoerdDoor(medewerkerModel.getObject());
	}

	protected void recalculateCijfers(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		List<Resultaat> resultatenVoorToets = getPogingen(recalcKey, resultaten, false);
		for (Resultaat curResultaat : resultatenVoorToets)
		{
			boolean update = false;
			if (!curResultaat.isGefixeerd() && curResultaat.getScore() != null)
			{
				curResultaat.setCijferOfWaardeUitScore(curResultaat.getScore());
				update = true;
			}
			if (curResultaat.getToets().getStudiepunten() != null && curResultaat.isBehaald())
			{
				int toetsPunten = curResultaat.getToets().getStudiepunten();
				if (toetsPunten != curResultaat.getStudiepunten())
				{
					curResultaat.setStudiepunten(toetsPunten);
					recordBerekening(curResultaat, "Voor deze toets zijn " + toetsPunten
						+ " studiepunten gehaald");
					update = true;
				}
			}
			if (update)
				curResultaat.saveOrUpdate();
		}
	}

	@SuppressWarnings("unused")
	protected Resultaat determineGeldendResultaat(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten,
			Map<Resultaat, Collection<Resultaat>> markeerInSamengesteld)
	{
		if (recalcKey.getToets().isSamengesteld()
			&& !recalcKey.getToets().isSamengesteldMetHerkansing()
			&& !recalcKey.getToets().isSamengesteldMetVarianten())
		{
			Resultaat geldend =
				getGeldendResultaat(recalcKey, resultaten, true, false, RESULTAAT_NR);
			if (geldend != null)
			{
				geldend.setGeldend(true);
				geldend.saveOrUpdate();
			}
			return geldend;
		}

		Toets pogingBasis = getPogingBasis(recalcKey, resultaten);
		if (pogingBasis == null)
			return null;

		List<Resultaat> pogingen = getPogingen(recalcKey, resultaten, true);
		Resultaat geldend =
			findGeldendResultaat(pogingen, recalcKey.getToets().getScoreBijHerkansing());
		if (geldend != null)
			recordBerekening(geldend,
				"Dit resultaat is gekozen tot geldend resultaat omdat dit het "
					+ recalcKey.getToets().getScoreBijHerkansing().toString().toLowerCase()
					+ " resultaat is");
		for (Resultaat curResultaat : pogingen)
		{
			if (curResultaat.isActueel())
			{
				curResultaat.setGeldend(curResultaat.equals(geldend));
				curResultaat.update();
			}
		}
		return geldend;
	}

	private void pushResultaatOverVerwijzingen(
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten, Resultaat geldend)
	{
		Toets source = geldend.getToets();
		Deelnemer deelnemer = geldend.getDeelnemer();
		if (source.getUitgaandeVerwijzingen().isEmpty())
			return;

		if (source.isHandmatigInleveren())
		{
			DeelnemerToetsBevriezing bevriezing =
				DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).getBevriezing(source,
					deelnemer);
			if (bevriezing == null || !bevriezing.isIngeleverd())
			{
				recordBerekening(geldend, "Dit resultaat is nog niet ingeleverd");
				return;
			}
		}

		for (ToetsVerwijzing curVerwijzing : source.getUitgaandeVerwijzingen())
		{
			Toets target = curVerwijzing.getSchrijvenIn();
			int pogingNummer =
				geldend.getHerkansingsnummer() < target.getAantalPogingen() ? geldend
					.getHerkansingsnummer() : 0;
			ResultaatKey targetKey = new ResultaatKey(target, deelnemer);
			Resultaat verwezenGeldend =
				getGeldendResultaat(targetKey, resultaten, true, false, pogingNummer + OFFSET);
			if (!target.getResultaatstructuur().isBeschikbaar()
				|| (verwezenGeldend != null && verwezenGeldend.isOverschreven()))
				continue;

			if (resultatenModel != null
				&& resultatenModel.isBevroren(target, deelnemer, pogingNummer + OFFSET))
				continue;

			Resultaat pushResultaat = geldend.copy();
			pushResultaat.setToets(target);
			pushResultaat.setSoort(Resultaatsoort.Verwezen);
			pushResultaat.setOverschrijft(verwezenGeldend);
			pushResultaat.setScore(null);
			pushResultaat.setCijferOfWaarde(geldend.getCijfer() == null ? null : target.getSchaal()
				.calculateCijferOfWaarde(geldend.getCijfer(), 1).getResultaat());
			pushResultaat.setHerkansingsnummer(pogingNummer);
			pushResultaat.setBerekening("Dit resultaat is via een toetsverwijzing overgenomen uit "
				+ curVerwijzing.getLezenUit());
			pushResultaat.save();

			getFromMap(targetKey, resultaten).get(RESULTAAT_IDX).add(0, pushResultaat);
			if (target.getAantalPogingen() > 0)
				getFromMap(targetKey, resultaten).get(pogingNummer + POGING_START_IDX).add(0,
					pushResultaat);

			if (verwezenGeldend != null)
			{
				verwezenGeldend.setGeldend(false);
				verwezenGeldend.setActueel(false);
				verwezenGeldend.update();
			}
		}
	}

	@Override
	protected void recordBerekening(Resultaat resultaat, String berekening)
	{
		resultaat.appendBerekening(berekening);
	}

	@Override
	public void detach()
	{
		super.detach();
		ComponentUtil.detachQuietly(medewerkerModel);
	}
}
