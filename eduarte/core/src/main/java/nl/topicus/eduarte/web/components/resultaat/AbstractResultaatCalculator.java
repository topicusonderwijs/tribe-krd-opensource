package nl.topicus.eduarte.web.components.resultaat;

import static nl.topicus.eduarte.web.components.resultaat.ResultatenModel.*;

import java.util.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat.Resultaatsoort;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal.Schaaltype;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.BerekendResultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.Herkansingsscore;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.jgrapht.traverse.TopologicalOrderIterator;

public abstract class AbstractResultaatCalculator implements IDetachable
{
	private static final long serialVersionUID = 1L;

	private List<ResultaatKey> recalculationKeys;

	private Map<IModel<Deelnemer>, List<IModel<Toets>>> recalculationBase;

	public AbstractResultaatCalculator()
	{
		recalculationBase = new HashMap<IModel<Deelnemer>, List<IModel<Toets>>>();
	}

	public void addRecalcuation(Toets toets, Deelnemer deelnemer)
	{
		IModel<Deelnemer> deelnemerModel = ModelFactory.getModel(deelnemer);
		IModel<Toets> toetsModel = ModelFactory.getModel(toets);
		List<IModel<Toets>> toetsen = recalculationBase.get(deelnemerModel);
		if (toetsen == null)
		{
			toetsen = new ArrayList<IModel<Toets>>();
			recalculationBase.put(deelnemerModel, toetsen);
		}
		toetsen.add(toetsModel);
	}

	public void recalculateResultaten(Map<ResultaatKey, List<List<Resultaat>>> resultaten)
	{
		Map<ResultaatKey, IModel<List<List<Resultaat>>>> calcResultaten =
			new HashMap<ResultaatKey, IModel<List<List<Resultaat>>>>();
		for (Map.Entry<ResultaatKey, List<List<Resultaat>>> curEntry : resultaten.entrySet())
		{
			calcResultaten.put(curEntry.getKey(), new ListModel<List<Resultaat>>(
				new ArrayList<List<Resultaat>>(curEntry.getValue())));
		}
		recalculate(calcResultaten);
	}

	public int getRecalculationSize()
	{
		return recalculationKeys == null ? 0 : recalculationKeys.size();
	}

	public List<ResultaatKey> getRecalculationKeys()
	{
		return recalculationKeys;
	}

	public void recalculate(Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		recalculationKeys = collectAllToetsen();
		for (ResultaatKey curRecalcKey : recalculationKeys)
		{
			recalculate(curRecalcKey, resultaten);
		}
	}

	protected abstract void recalculate(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten);

	private List<ResultaatKey> collectAllToetsen()
	{
		List<ResultaatKey> ret = new ArrayList<ResultaatKey>();
		for (Map.Entry<IModel<Deelnemer>, List<IModel<Toets>>> curEntry : recalculationBase
			.entrySet())
		{
			ToetsDirectedGraph recalcGraph =
				ToetsDirectedGraph.createRecalcGraph(getToetsen(curEntry.getValue()));
			TopologicalOrderIterator<ToetsVertex, ToetsEdge> iterator =
				new TopologicalOrderIterator<ToetsVertex, ToetsEdge>(recalcGraph);
			while (iterator.hasNext())
			{
				Toets curToets = iterator.next().getToets();
				ret.add(new ResultaatKey(ModelFactory.getModel(curToets), curEntry.getKey()));
			}
		}
		return ret;
	}

	private List<Toets> getToetsen(List<IModel<Toets>> toetsModels)
	{
		List<Toets> ret = new ArrayList<Toets>();
		for (IModel<Toets> curToetsModel : toetsModels)
			ret.add(curToetsModel.getObject());
		return ret;
	}

	protected List<List<Resultaat>> getFromMap(ResultaatKey key,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		if (!resultaten.containsKey(key))
		{
			resultaten.put(key, new ListModel<List<Resultaat>>(fetchResultaten(key)));
		}
		return resultaten.get(key).getObject();
	}

	protected List<List<Resultaat>> fetchResultaten(ResultaatKey key)
	{
		List<Resultaat> retResultaten =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).getActueleResultaten(
				key.getToets(), key.getDeelnemer());
		List<List<Resultaat>> resultatenVoorToets =
			convertToResultatenPerPoging(key, retResultaten);
		ResultatenModel.sortResultatenVoorToets(resultatenVoorToets);
		return resultatenVoorToets;
	}

	protected Map<Toets, Resultaat> getGeldendOfAlternatiefResultaten(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten, int pogingNr)
	{
		Map<Toets, Resultaat> geldendOfAlt = new LinkedHashMap<Toets, Resultaat>();
		for (Toets curDeeltoets : recalcKey.getToets().getChildren())
		{
			Resultaat geldendOfAltResultaat =
				getGeldendOfAlternatiefResultaat(new ResultaatKey(new Model<Toets>(curDeeltoets),
					recalcKey.getDeelnemerModel()), resultaten, pogingNr);
			geldendOfAlt.put(curDeeltoets, geldendOfAltResultaat);
		}
		return geldendOfAlt;
	}

	protected Map<Toets, Resultaat> getAlternatiefResultaten(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		Map<Toets, Resultaat> alternatief = new LinkedHashMap<Toets, Resultaat>();
		for (Toets curDeeltoets : recalcKey.getToets().getChildren())
		{
			Resultaat alternatiefResultaat =
				getAlternatiefResultaat(new ResultaatKey(new Model<Toets>(curDeeltoets), recalcKey
					.getDeelnemerModel()), resultaten);
			alternatief.put(curDeeltoets, alternatiefResultaat);
		}
		return alternatief;
	}

	protected Resultaat getGeldendOfAlternatiefResultaat(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten, int pogingNr)
	{
		Resultaat geldend = getGeldendResultaat(recalcKey, resultaten, false, true, pogingNr);
		if (!recalcKey.getToets().isAlternatiefCombinerenMetHoofd())
			return geldend;
		Resultaat alternatief = getAlternatiefResultaat(recalcKey, resultaten);
		return getHoogsteResultaat(geldend, alternatief);
	}

	protected Resultaat getGeldendResultaat(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten, boolean includeNull,
			boolean gebruikEerderePogingen, int poging)
	{
		int pogingNr = Math.min(recalcKey.getToets().getAantalPogingen(), poging);

		if (recalcKey.getToets().isVariant()
			&& (recalcKey.getToets().getVariantVoorPoging() + OFFSET) != pogingNr)
			return null;

		List<List<Resultaat>> retList = getFromMap(recalcKey, resultaten);
		if (retList != null && !retList.get(OFFSET + pogingNr).isEmpty())
		{
			Resultaat ret = retList.get(OFFSET + pogingNr).get(0);
			if (ret != null && (includeNull || !ret.isNullOfZonderCijferOfWaarde()))
				return ret;
		}
		if (retList != null && gebruikEerderePogingen)
		{
			List<Resultaat> actueleResultaten = getActueleResultaten(retList, pogingNr);
			Resultaat ret =
				findGeldendResultaat(actueleResultaten, recalcKey.getToets()
					.getScoreBijHerkansing());
			if (ret != null && (includeNull || !ret.isNullOfZonderCijferOfWaarde()))
				return ret;
		}
		return null;
	}

	private List<Resultaat> getActueleResultaten(List<List<Resultaat>> resultaten, int maxPoging)
	{
		List<Resultaat> ret = new ArrayList<Resultaat>();
		for (int curPoging = 1; curPoging <= maxPoging; curPoging++)
		{
			List<Resultaat> curResultaten = resultaten.get(curPoging + OFFSET);
			if (!curResultaten.isEmpty() && !curResultaten.get(0).isNullResultaat())
				ret.add(curResultaten.get(0));
		}
		return ret;
	}

	protected Resultaat getAlternatiefResultaat(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		List<List<Resultaat>> retList = getFromMap(recalcKey, resultaten);
		if (retList != null && !retList.get(ALTERNATIEF_IDX).isEmpty())
		{
			Resultaat ret = retList.get(ALTERNATIEF_IDX).get(0);
			if (ret != null && !ret.isNullOfZonderCijferOfWaarde())
				return ret;
		}
		return null;
	}

	protected List<Integer> determinePogingNrs(Toets pogingBasis)
	{
		if (pogingBasis == null)
			return Collections.emptyList();
		if (pogingBasis.isSamengesteld() && !pogingBasis.isSamengesteldMetHerkansing()
			&& !pogingBasis.isSamengesteldMetVarianten())
			return Arrays.asList(0);
		List<Integer> ret = new ArrayList<Integer>();
		for (int nr = 1; nr <= pogingBasis.getAantalPogingen(); nr++)
		{
			ret.add(nr);
		}
		return ret;
	}

	protected Toets getPogingBasis(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		Toets toets = recalcKey.getToets();
		if (toets.isSamengesteld())
		{
			if (toets.isSamengesteldMetHerkansing())
			{
				Toets ret = null;
				for (Toets curChild : toets.getChildren())
				{
					if (curChild.getAantalPogingen() > 1
						&& getGeldendResultaat(
							new ResultaatKey(curChild, recalcKey.getDeelnemer()), resultaten,
							false, false, RESULTAAT_NR) != null)
					{
						// zie #51943
						// if (ret != null)
						// return null;
						if (ret != null && ret.getAantalPogingen() != curChild.getAantalPogingen())
							return null;
						ret = curChild;
					}
				}
				if (ret == null)
				{
					// als er geen toets met herkansingen gevonden is waarvoor een geldend
					// resultaat is, ga opnieuw zoeken en pak de eerste toets met
					// herkansingen
					for (Toets curChild : toets.getChildren())
					{
						if (curChild.getAantalPogingen() > 1)
						{
							ret = curChild;
							break;
						}
					}
				}
				return ret;
			}
			else
				return toets;
		}
		return toets;
	}

	protected boolean isAHogerDanB(Resultaat resA, Resultaat resB)
	{
		if (resA == null)
			return false;
		if (resB == null)
			return true;
		return resA.isHogerDan(resB);
	}

	protected Resultaat getHoogsteResultaat(Resultaat resB, Resultaat resA)
	{
		return isAHogerDanB(resA, resB) ? resA : resB;
	}

	protected List<Resultaat> getPogingen(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten, boolean includeNull)
	{
		List<List<Resultaat>> retList = getFromMap(recalcKey, resultaten);
		retList =
			new LinkedList<List<Resultaat>>(retList.subList(POGING_START_IDX, retList.size()));
		Iterator<List<Resultaat>> it = retList.iterator();
		List<Resultaat> ret = new ArrayList<Resultaat>();
		while (it.hasNext())
		{
			List<Resultaat> curResultaatList = it.next();
			if (!curResultaatList.isEmpty()
				&& (includeNull || !curResultaatList.get(0).isNullResultaat()))
				ret.add(curResultaatList.get(0));
		}
		return ret;
	}

	protected boolean isCompleet(Resultaat resultaat, Map<Toets, Resultaat> deeltoetsen)
	{
		boolean compleet = true;
		int behaaldeStudiepunten = 0;
		int ingevuldeResultaten = 0;
		int nietBehaaldeResultaten = 0;
		for (Map.Entry<Toets, Resultaat> curEntry : deeltoetsen.entrySet())
		{
			Toets curToets = curEntry.getKey();
			Resultaat curResultaat = curEntry.getValue();
			if (curResultaat != null)
			{
				behaaldeStudiepunten += curResultaat.getStudiepunten();
				if (curResultaat.isIngevuld())
				{
					ingevuldeResultaten++;
					if (!curResultaat.isCompenseerbaar())
					{
						compleet = false;
						recordBerekening(resultaat, "Het resultaat voor " + curToets.getCode()
							+ " is niet hoog genoeg om gecompenseerd te worden");
					}
				}
				if (curToets.isVerplicht())
				{
					if (!curResultaat.isIngevuld())
					{
						compleet = false;
						recordBerekening(resultaat, "Er is geen resultaat voor de verplichte "
							+ "deeltoets '" + curToets.getCode() + "'");
					}
				}
				if (!curResultaat.isBehaald())
					nietBehaaldeResultaten++;
			}
			else if (curToets.isVerplicht())
			{
				compleet = false;
				recordBerekening(resultaat, "Er is geen resultaat voor de verplichte "
					+ "deeltoets '" + curToets.getCode() + "'");
			}
		}

		Toets toets = resultaat.getToets();
		if (toets.getMaxAantalIngevuld() != null
			&& toets.getMaxAantalIngevuld() < ingevuldeResultaten)
		{
			compleet = false;
			recordBerekening(resultaat, "Het aantal ingevulde deeltoetsen (" + ingevuldeResultaten
				+ ") is hoger dan het maximum (" + toets.getMaxAantalIngevuld() + ")");
		}
		if (toets.getMinAantalIngevuld() != null
			&& toets.getMinAantalIngevuld() > ingevuldeResultaten)
		{
			compleet = false;
			recordBerekening(resultaat, "Het aantal ingevulde deeltoetsen (" + ingevuldeResultaten
				+ ") is lager dan het minimum (" + toets.getMinAantalIngevuld() + ")");
		}
		if (toets.getMaxAantalNietBehaald() != null
			&& toets.getMaxAantalNietBehaald() < nietBehaaldeResultaten)
		{
			compleet = false;
			recordBerekening(resultaat, "Het aantal niet behaalde deeltoetsen ("
				+ nietBehaaldeResultaten + ") is hoger dan het maximum ("
				+ toets.getMaxAantalNietBehaald() + ")");
		}
		if (toets.getMinStudiepuntenVoorBehaald() != null
			&& toets.getMinStudiepuntenVoorBehaald() > behaaldeStudiepunten)
		{
			compleet = false;
			recordBerekening(resultaat, "Het aantal behaalde studiepunten (" + behaaldeStudiepunten
				+ ") is lager dan het minimum (" + toets.getMinStudiepuntenVoorBehaald() + ")");
		}
		return compleet;
	}

	protected BerekendResultaat calculateSamengesteldResultaat(Resultaat samengesteldResultaat,
			Map<Toets, Resultaat> deeltoetsen)
	{
		Toets samengesteldeToets = samengesteldResultaat.getToets();
		String resultatenStr = resultatenToString(deeltoetsen.values());
		switch (samengesteldeToets.getRekenregel())
		{
			case Gemiddelde:
				recordBerekening(samengesteldResultaat, "Het cijfer is het gemiddelde van: "
					+ resultatenStr);
				break;
			case Prioriteit:
				recordBerekening(samengesteldResultaat, "Het cijfer is gekozen uit: "
					+ resultatenStr);
				break;
			case Formule:
				recordBerekening(samengesteldResultaat,
					"Het cijfer is volgens een formule berekend uit: " + resultatenStr);
				break;
		}
		return samengesteldeToets.getRekenregel().calculateSamengesteldResultaat(deeltoetsen,
			samengesteldeToets);
	}

	private String resultatenToString(Collection<Resultaat> resultaten)
	{
		List<Resultaat> nietNullResultaten = new ArrayList<Resultaat>();
		for (Resultaat curResultaat : resultaten)
			if (curResultaat != null && !curResultaat.isNullResultaat())
				nietNullResultaten.add(curResultaat);

		StringBuilder ret = new StringBuilder();
		for (int count = 0; count < nietNullResultaten.size(); count++)
		{
			Resultaat curResultaat = nietNullResultaten.get(count);
			if (count > 0)
			{
				if (count == nietNullResultaten.size() - 1)
					ret.append(" en ");
				else
					ret.append(", ");
			}
			ret.append(curResultaat.getFormattedDisplayCijfer());
			if (Schaaltype.Tekstueel.equals(curResultaat.getToets().getSchaal().getSchaaltype()))
			{
				ret.append(" (");
				ret.append(curResultaat.getFormattedNominaleWaarde());
				ret.append(")");
			}
			if (!curResultaat.getWegingVoorBerekening().equals(1))
			{
				ret.append(" (x");
				ret.append(curResultaat.getWegingVoorBerekening());
				ret.append(")");
			}
		}
		return ret.toString();
	}

	protected Resultaat findGeldendResultaat(List<Resultaat> resultaten,
			Herkansingsscore herkansingsscore)
	{
		Resultaat geldend = null;
		for (Resultaat curResultaat : resultaten)
		{
			if (curResultaat.isActueel())
			{
				if (curResultaat.getSoort().equals(Resultaatsoort.Alternatief))
					continue;

				if (geldend == null || herkansingsscore.isGeldend(geldend, curResultaat))
					geldend = curResultaat;
			}
		}
		return geldend;
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(recalculationBase);
		ComponentUtil.detachQuietly(recalculationKeys);
	}

	protected Resultaat createSamengesteldResultaat(Toets toets, Deelnemer deelnemer, int pogingNr,
			Map<Toets, Resultaat> deeltoetsen)
	{
		Resultaat berekend = new Resultaat();
		berekend.setDeelnemer(deelnemer);
		berekend.setActueel(true);
		berekend.setToets(toets);

		boolean compleet = isCompleet(berekend, deeltoetsen);
		berekend.setSoort(compleet ? Resultaatsoort.Berekend : Resultaatsoort.Tijdelijk);
		if (!toets.isSamengesteldMetHerkansing()
			|| heeftResultaatVoorPoging(deeltoetsen.values(), pogingNr))
		{
			BerekendResultaat berekendResultaat =
				calculateSamengesteldResultaat(berekend, deeltoetsen);
			berekend.setOnafgerondCijfer(berekendResultaat == null ? null : berekendResultaat
				.getOnafgerondCijfer());
			berekend.setCijferOfWaarde(berekendResultaat == null ? null : berekendResultaat
				.getResultaat());
		}

		if (!berekend.isBehaald() && !berekend.isNullResultaat() && toets.getSchaal().isJaNee())
		{
			berekend.setSoort(Resultaatsoort.Berekend);
			berekend.setWaarde(toets.getSchaal().findNee());
		}

		Integer wegingTotaal = 0;
		int studiepunten = 0;
		Date datumBehaald = null;
		for (Resultaat curDeelResultaat : deeltoetsen.values())
		{
			if (curDeelResultaat != null && curDeelResultaat.isBehaald())
				studiepunten += curDeelResultaat.getStudiepunten();
			if (curDeelResultaat != null)
				datumBehaald = lastOf(datumBehaald, curDeelResultaat.getDatumBehaald());
			if (curDeelResultaat != null && curDeelResultaat.isIngevuld())
			{
				if (curDeelResultaat.getToets().isSamengesteld()
					&& !curDeelResultaat.getToets().isSamengesteldMetVarianten())
				{
					wegingTotaal += curDeelResultaat.getWeging();
				}
				else
				{
					wegingTotaal += curDeelResultaat.getToets().getWeging();
				}
			}
		}
		berekend.setDatumBehaald(datumBehaald);
		recordBerekening(berekend, "Voor de onderliggende toetsen zijn " + studiepunten
			+ " studiepunten gehaald");
		berekend.setStudiepunten(studiepunten);
		berekend.setWeging(toets.isAutomatischeWeging() ? wegingTotaal : toets.getWeging());
		return berekend;
	}

	private Date lastOf(Date datumBehaald1, Date datumBehaald2)
	{
		if (datumBehaald1 == null)
			return datumBehaald2;
		if (datumBehaald2 == null)
			return datumBehaald1;
		return datumBehaald1.after(datumBehaald2) ? datumBehaald1 : datumBehaald2;
	}

	private boolean heeftResultaatVoorPoging(Collection<Resultaat> resultaten, int pogingNr)
	{
		for (Resultaat curResultaat : resultaten)
		{
			if (curResultaat != null && curResultaat.getToets().getAantalPogingen() > 1
				&& !curResultaat.isNullResultaat()
				&& (curResultaat.getHerkansingsnummer() + OFFSET) == pogingNr)
				return true;
		}
		return false;
	}

	protected List<List<Resultaat>> convertToResultatenPerPoging(ResultaatKey key,
			List<Resultaat> value)
	{
		int pogingen = key.getToets().getAantalPogingen();
		List<List<Resultaat>> resultatenVoorToets = new ArrayList<List<Resultaat>>();
		for (int count = 0; count < pogingen + POGING_START_IDX; count++)
		{
			resultatenVoorToets.add(new ArrayList<Resultaat>());
		}
		for (Resultaat curResultaat : value)
			ResultatenModel.insertResultaat(curResultaat, resultatenVoorToets);
		return resultatenVoorToets;
	}

	protected abstract void recordBerekening(Resultaat resultaat, String berekening);
}
