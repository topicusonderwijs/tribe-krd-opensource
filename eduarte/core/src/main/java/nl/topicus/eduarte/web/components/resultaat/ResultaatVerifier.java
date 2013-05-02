package nl.topicus.eduarte.web.components.resultaat;

import static nl.topicus.eduarte.web.components.resultaat.ResultatenModel.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.eduarte.dao.hibernate.IllegalResultaatException;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal.Schaaltype;

import org.apache.wicket.model.IModel;

public class ResultaatVerifier extends AbstractResultaatCalculator
{
	private static final long serialVersionUID = 1L;

	public ResultaatVerifier()
	{
	}

	public void verify(Map<ResultaatKey, List<Resultaat>> resultatenPerDeelnemerToets)
	{
		Map<ResultaatKey, List<List<Resultaat>>> resultaten =
			new HashMap<ResultaatKey, List<List<Resultaat>>>();
		for (Map.Entry<ResultaatKey, List<Resultaat>> curEntry : resultatenPerDeelnemerToets
			.entrySet())
		{
			ResultaatKey curKey = curEntry.getKey();
			addRecalcuation(curKey.getToets(), curKey.getDeelnemer());
			resultaten.put(curKey, convertToResultatenPerPoging(curKey, curEntry.getValue()));
		}
		ResultatenModel.sortResultaten(resultaten);
		recalculateResultaten(resultaten);
	}

	@Override
	protected void recalculate(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		Map<Resultaat, Collection<Resultaat>> markeerInSamengesteld = Collections.emptyMap();
		if (recalcKey.getToets().isSamengesteld())
			markeerInSamengesteld = verifySamengesteld(recalcKey, resultaten);
		else
			verifyCijfers(recalcKey, resultaten);
		Resultaat geldend = verifyGeldend(recalcKey, resultaten);
		verifyInSamengesteld(recalcKey, resultaten, markeerInSamengesteld.get(geldend));
	}

	private void verifyInSamengesteld(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten,
			Collection<Resultaat> gebruikteResultaten)
	{
		Deelnemer deelnemer = recalcKey.getDeelnemer();
		for (Toets curChild : recalcKey.getToets().getChildren())
		{
			ResultaatKey childKey = new ResultaatKey(curChild, deelnemer);
			for (Resultaat curResultaat : getPogingen(childKey, resultaten, true))
			{
				verifyInSamengesteld(curResultaat, gebruikteResultaten);
			}
			Resultaat alt = getAlternatiefResultaat(childKey, resultaten);
			if (alt != null)
			{
				verifyInSamengesteld(alt, gebruikteResultaten);
			}
		}
	}

	private void verifyInSamengesteld(Resultaat curResultaat,
			Collection<Resultaat> gebruikteResultaten)
	{
		if (curResultaat.isInSamengesteld()
			&& (gebruikteResultaten == null || !gebruikteResultaten.contains(curResultaat)))
		{
			throw new IllegalResultaatException(curResultaat,
				"Het resultaat is gemarkeerd als in-samengesteld, maar wordt niet gebruikt.");
		}
		else if (!curResultaat.isInSamengesteld() && gebruikteResultaten != null
			&& gebruikteResultaten.contains(curResultaat))
		{
			throw new IllegalResultaatException(curResultaat,
				"Het resultaat is niet gemarkeerd als in-samengesteld, maar wordt wel gebruikt.");
		}
	}

	private Map<Resultaat, Collection<Resultaat>> verifySamengesteld(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		Map<Resultaat, Collection<Resultaat>> gebruikteResultaten =
			new HashMap<Resultaat, Collection<Resultaat>>();

		Toets pogingBasis = getPogingBasis(recalcKey, resultaten);
		for (int curPogingNr : determinePogingNrs(pogingBasis))
		{
			Toets toets = recalcKey.getToets();
			Deelnemer deelnemer = recalcKey.getDeelnemer();
			Map<Toets, Resultaat> geldendOfAltSet =
				getGeldendOfAlternatiefResultaten(recalcKey, resultaten, curPogingNr);
			Map<Toets, Resultaat> alternatiefSet = getAlternatiefResultaten(recalcKey, resultaten);
			Resultaat geldend =
				getGeldendResultaat(recalcKey, resultaten, true, false, curPogingNr);
			// er valt niets te controleren als het resultaat overschreven is
			if (geldend != null && geldend.isGefixeerd())
				continue;

			Resultaat geldendOfAlt =
				createSamengesteldResultaat(toets, deelnemer, curPogingNr, geldendOfAltSet);
			Resultaat alternatief =
				createSamengesteldResultaat(toets, deelnemer, curPogingNr, alternatiefSet);
			Resultaat berekend = getHoogsteResultaat(geldendOfAlt, alternatief);

			if (geldend == null)
			{
				if (berekend != null && !berekend.isNullResultaat())
				{
					throw new IllegalResultaatException(berekend,
						"Er is geen geldend resultaat, terwijl er wel een resultaat berekend is.");
				}
			}
			else
			{
				compareResultaten(geldend, berekend);
				if (geldendOfAlt == berekend)
					gebruikteResultaten.put(geldend, geldendOfAltSet.values());
				else
					gebruikteResultaten.put(geldend, alternatiefSet.values());
			}
		}
		return gebruikteResultaten;
	}

	private void compareResultaten(Resultaat geldend, Resultaat berekend)
	{
		if (!JavaUtil.bigDecimalEqualsOrBothNull(berekend.getCijfer(), geldend.getCijfer()))
			throw new IllegalResultaatException(geldend,
				"Het cijfer voor de samengestelde toets klopt niet: " + geldend.getCijfer()
					+ " != " + berekend.getCijfer());
		if (!geldend.isNullResultaat() && !berekend.isNullResultaat())
		{
			if (!JavaUtil.equalsOrBothNull(berekend.getSoort(), geldend.getSoort()))
				throw new IllegalResultaatException(geldend,
					"De soort voor de samengestelde toets klopt niet: " + geldend.getSoort()
						+ " != " + berekend.getSoort());
		}
		if (!JavaUtil.equalsOrBothNull(berekend.getScore(), geldend.getScore()))
			throw new IllegalResultaatException(geldend,
				"De score voor de samengestelde toets klopt niet: " + geldend.getScore() + " != "
					+ berekend.getScore());
		if (!JavaUtil.equalsOrBothNull(berekend.getWaarde(), geldend.getWaarde()))
			throw new IllegalResultaatException(geldend,
				"De waarde voor de samengestelde toets klopt niet: " + geldend.getWaarde() + " != "
					+ berekend.getWaarde());
		if (berekend.getStudiepunten() != geldend.getStudiepunten())
			throw new IllegalResultaatException(geldend,
				"Het aantal behaalde studiepunten voor de samengestelde toets klopt niet: "
					+ geldend.getStudiepunten() + " != " + berekend.getStudiepunten());
		if (berekend.getWeging() != null && !berekend.getWeging().equals(geldend.getWeging()))
			throw new IllegalResultaatException(geldend,
				"De berekende weging voor de samengestelde toets klopt niet: "
					+ geldend.getWeging() + " != " + berekend.getWeging());
	}

	private void verifyCijfers(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		Toets toets = recalcKey.getToets();
		for (List<Resultaat> resultatenLijst : getFromMap(recalcKey, resultaten))
		{
			for (Resultaat curResultaat : resultatenLijst)
			{
				if (curResultaat.getScore() != null && !curResultaat.isGefixeerd()
					&& curResultaat.isActueel())
				{
					if (toets.getSchaal().getSchaaltype().equals(Schaaltype.Cijfer))
					{
						BigDecimal juisteCijfer =
							toets.berekenCijfer(curResultaat.getScore(), curResultaat
								.getHerkansingsnummer() + 1);
						if (!JavaUtil.bigDecimalEqualsOrBothNull(curResultaat.getCijfer(),
							juisteCijfer))
							throw new IllegalResultaatException(curResultaat,
								"Het cijfer is onjuist: " + curResultaat.getCijfer() + " != "
									+ juisteCijfer);
					}
					else
					{
						Schaalwaarde juisteWaarde = toets.berekenWaarde(curResultaat.getScore());
						if (!JavaUtil.equalsOrBothNull(juisteWaarde, curResultaat.getWaarde()))
							throw new IllegalResultaatException(curResultaat,
								"Het cijfer is onjuist: " + curResultaat.getWaarde() + " != "
									+ juisteWaarde);
					}
				}
				if (curResultaat.isActueel())
				{
					Integer toetsPunten = curResultaat.getToets().getStudiepunten();
					int verwachteStudiepunten =
						curResultaat.isBehaald() && toetsPunten != null ? toetsPunten : 0;
					if (verwachteStudiepunten != curResultaat.getStudiepunten())
						throw new IllegalResultaatException(curResultaat,
							"Het aantal behaalde studiepunten klopt niet: "
								+ curResultaat.getStudiepunten() + " != " + verwachteStudiepunten);
				}
			}
		}
	}

	private Resultaat verifyGeldend(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		Resultaat geldend;
		if (recalcKey.getToets().isSamengesteld()
			&& !recalcKey.getToets().isSamengesteldMetHerkansing()
			&& !recalcKey.getToets().isSamengesteldMetVarianten())
			geldend = getGeldendResultaat(recalcKey, resultaten, true, false, RESULTAAT_NR);
		else
			geldend =
				findGeldendResultaat(getPogingen(recalcKey, resultaten, true), recalcKey.getToets()
					.getScoreBijHerkansing());

		if (geldend != null)
		{
			if (!geldend.isGeldend())
				throw new IllegalResultaatException(geldend,
					"Het resultaat dat geldend had moeten zijn, is dat niet");
			// if (geldend.isZonderCijferOfWaarde())
			// throw new IllegalResultaatException(geldend,
			// "Het resultaat dat geldend is, heeft geen cijfer of waarde (alleen een score)");
		}
		for (List<Resultaat> resultatenLijst : getFromMap(recalcKey, resultaten))
		{
			for (Resultaat curResultaat : resultatenLijst)
			{
				if (!curResultaat.equals(geldend) && curResultaat.isGeldend())
				{
					throw new IllegalResultaatException(curResultaat,
						"Een onjuist resultaat is geldend");
				}
			}
		}
		return geldend;
	}

	@Override
	protected void recordBerekening(Resultaat resultaat, String berekening)
	{
		// do nothing
	}
}
