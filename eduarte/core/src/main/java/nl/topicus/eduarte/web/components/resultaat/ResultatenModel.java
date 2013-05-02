package nl.topicus.eduarte.web.components.resultaat;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.DeelnemerToetsBevriezing;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat.Resultaatsoort;
import nl.topicus.eduarte.entities.settings.ResultaatControleSetting;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

public class ResultatenModel extends LoadableDetachableModel<ResultatenModel.ResultatenInformation>
{
	private static final long serialVersionUID = 1L;

	/** Het nummer dat gebruikt wordt voor het alternatieve resultaat */
	public static final int ALTERNATIEF_NR = -1;

	/** Het nummer dat gebruikt wordt voor het geldende resultaat */
	public static final int RESULTAAT_NR = 0;

	/** De index in de lijst voor het alternatieve resultaat */
	public static final int ALTERNATIEF_IDX = 0;

	/** De index in de lijst voor het geldende resultaat */
	public static final int RESULTAAT_IDX = 1;

	/** De index in de lijst van de eerste poging */
	public static final int POGING_START_IDX = 2;

	/** Het verschil tussen de nummers en de index */
	public static final int OFFSET = ALTERNATIEF_IDX - ALTERNATIEF_NR;

	public static class ResultatenInformation
	{
		private Map<ResultaatKey, List<List<Resultaat>>> resultatenPerToetsDeelnemer =
			new HashMap<ResultaatKey, List<List<Resultaat>>>();

		private Map<ResultaatKey, BitSet> bevriezingenPerToetsDeelnemer =
			new HashMap<ResultaatKey, BitSet>();

		private int huidigMaximumAantalPogingen = 0;

		private int absoluutMaximumAantalPogingen = 0;

		public ResultatenInformation(List<Toets> toetsen, List<Deelnemer> deelnemers,
				ResultaatZoekFilter zoekFilter)
		{
			initBevriezingen(toetsen, deelnemers);
			for (Toets curToets : toetsen)
			{
				for (Deelnemer curDeelnemer : deelnemers)
				{
					mergeBevriezing(curToets, curDeelnemer);
					initToets(curToets, curDeelnemer);
				}
			}
			if (zoekFilter != null)
			{
				List<Resultaat> resultaten =
					DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).list(zoekFilter);
				for (Resultaat curResultaat : resultaten)
				{
					try
					{
						addResultaat(curResultaat);
					}
					catch (IllegalArgumentException e)
					{
						throw new IllegalArgumentException(e.getMessage() + " deelnemer: "
							+ deelnemers.contains(curResultaat.getDeelnemer()) + " toets: "
							+ toetsen.contains(curResultaat.getToets()));
					}
				}
				sortResultaten();

				huidigMaximumAantalPogingen =
					DataAccessRegistry.getHelper(ToetsDataAccessHelper.class)
						.getMaximumAantalPogingen(zoekFilter.getToetsZoekFilter());

				List<Resultaatstructuur> structuren =
					DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class)
						.getStructuren(
							zoekFilter.getToetsZoekFilter().getResultaatstructuurFilter()
								.getDeelnemers());
				for (Resultaatstructuur curStructuur : structuren)
				{
					for (Toets curToets : curStructuur.getToetsen())
						absoluutMaximumAantalPogingen =
							Math.max(absoluutMaximumAantalPogingen, curToets.getAantalPogingen());
				}
			}
		}

		private void initBevriezingen(List<Toets> toetsen, List<Deelnemer> deelnemers)
		{
			for (DeelnemerToetsBevriezing curBevriezing : DataAccessRegistry.getHelper(
				ToetsDataAccessHelper.class).getBevriezingen(toetsen, deelnemers))
			{
				bevriezingenPerToetsDeelnemer.put(new ResultaatKey(curBevriezing.getToets(),
					curBevriezing.getDeelnemer()), curBevriezing.getBevorenPogingenAsSet());
			}
		}

		private void mergeBevriezing(Toets toets, Deelnemer deelnemer)
		{
			ResultaatKey key = new ResultaatKey(toets, deelnemer);
			BitSet bevroren = bevriezingenPerToetsDeelnemer.get(key);
			if (bevroren == null)
			{
				bevroren = new BitSet();
				bevriezingenPerToetsDeelnemer.put(key, bevroren);
				if (!toets.isEindresultaat())
					mergeBevriezing(toets.getParent(), deelnemer);
			}
			bevroren.or(toets.getBevorenPogingenAsSet());
		}

		private void initToets(Toets toets, Deelnemer deelnemer)
		{
			ResultaatKey key = new ResultaatKey(toets, deelnemer);
			int pogingen = toets.getAantalPogingen();
			List<List<Resultaat>> resultaten =
				new ArrayList<List<Resultaat>>(pogingen + POGING_START_IDX);
			for (int count = 0; count < pogingen + POGING_START_IDX; count++)
			{
				resultaten.add(new ArrayList<Resultaat>());
			}
			resultatenPerToetsDeelnemer.put(key, resultaten);
		}

		private void addResultaat(Resultaat resultaat)
		{
			ResultaatKey key =
				new ResultaatKey(new Model<Toets>(resultaat.getToets()), new Model<Deelnemer>(
					resultaat.getDeelnemer()));
			List<List<Resultaat>> resultatenVoorToets = resultatenPerToetsDeelnemer.get(key);
			if (resultatenVoorToets == null)
			{
				throw new IllegalArgumentException(resultaat + " voor " + key
					+ " valt niet binnen de verwachte set");
			}
			insertResultaat(resultaat, resultatenVoorToets);
		}

		private void sortResultaten()
		{
			ResultatenModel.sortResultaten(resultatenPerToetsDeelnemer);
		}

		public int getHuidigMaximumAantalPogingen()
		{
			return huidigMaximumAantalPogingen;
		}

		public int getAbsoluutMaximumAantalPogingen()
		{
			return absoluutMaximumAantalPogingen;
		}

		public void insertResultaten(ResultaatKey key, List<List<Resultaat>> resultaten)
		{
			resultatenPerToetsDeelnemer.put(key, resultaten);

			Toets toets = key.getToets();
			while (toets != null)
			{
				DeelnemerToetsBevriezing bevriezing =
					DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).getBevriezing(toets,
						key.getDeelnemer());
				BitSet bevriezingSet =
					bevriezing == null ? new BitSet() : bevriezing.getBevorenPogingenAsSet();
				bevriezingSet.or(toets.getBevorenPogingenAsSet());
				bevriezingenPerToetsDeelnemer.put(new ResultaatKey(toets, key.getDeelnemer()),
					bevriezingSet);
				toets = toets.getParent();
			}
		}

		public List<List<Resultaat>> getResultaten(IModel<Toets> toetsModel,
				IModel<Deelnemer> deelnemerModel)
		{
			return resultatenPerToetsDeelnemer.get(new ResultaatKey(toetsModel, deelnemerModel));
		}

		public Map<ResultaatKey, List<List<Resultaat>>> getAllResultaten()
		{
			return resultatenPerToetsDeelnemer;
		}

		public boolean isBevroren(ResultaatKey resultaatKey, int pogingNr)
		{
			Toets toets = resultaatKey.getToets();
			if (!toets.isEindresultaat()
				&& isBevroren(new ResultaatKey(toets.getParent(), resultaatKey.getDeelnemer()),
					RESULTAAT_NR))
				return true;

			BitSet bevroren = bevriezingenPerToetsDeelnemer.get(resultaatKey);
			if (pogingNr > RESULTAAT_NR)
			{
				return bevroren.get(RESULTAAT_IDX) || bevroren.get(pogingNr + OFFSET);
			}
			return bevroren.get(pogingNr + OFFSET);
		}
	}

	private ResultaatZoekFilter zoekFilter;

	private IModel< ? extends List<Toets>> toetsenModel;

	private IModel< ? extends List<Deelnemer>> deelnemersModel;

	/**
	 * Indien het ResultaatZoekFilter null is, zal het model geen resultaat informatie
	 * bevatten, enkel toets informatie (bevroren etc.)
	 */
	public ResultatenModel(ResultaatZoekFilter zoekFilter,
			IModel< ? extends List<Toets>> toetsenModel,
			IModel< ? extends List<Deelnemer>> deelnemersModel)
	{
		this.zoekFilter = zoekFilter;
		this.toetsenModel = toetsenModel;
		this.deelnemersModel = deelnemersModel;
	}

	public static void insertResultaat(Resultaat resultaat,
			List<List<Resultaat>> resultatenVoorToets)
	{
		if (resultaat.getSoort().equals(Resultaatsoort.Alternatief))
		{
			resultatenVoorToets.get(ALTERNATIEF_IDX).add(resultaat);
		}
		else if (!resultaat.getToets().isSamengesteld()
			|| resultaat.getToets().isSamengesteldMetHerkansing()
			|| resultaat.getToets().isSamengesteldMetVarianten())
		{
			resultatenVoorToets.get(resultaat.getHerkansingsnummer() + POGING_START_IDX).add(
				resultaat);
			if (resultaat.isGeldend())
				resultatenVoorToets.get(RESULTAAT_IDX).add(resultaat);
		}
		else
			resultatenVoorToets.get(RESULTAAT_IDX).add(resultaat);
	}

	public static void sortResultaten(
			Map<ResultaatKey, List<List<Resultaat>>> resultatenPerToetsDeelnemer)
	{
		for (List<List<Resultaat>> resultatenVoorToets : resultatenPerToetsDeelnemer.values())
		{
			sortResultatenVoorToets(resultatenVoorToets);
		}
	}

	public static void sortResultatenVoorToets(List<List<Resultaat>> resultatenVoorToets)
	{
		for (List<Resultaat> curList : resultatenVoorToets)
		{
			Collections.sort(curList, new Comparator<Resultaat>()
			{
				@Override
				public int compare(Resultaat o1, Resultaat o2)
				{
					if (o1.equals(o2))
						return 0;
					if (o1.overschrijft(o2))
						return -1;
					if (o2.overschrijft(o1))
						return 1;
					return o2.getCreatedAt().compareTo(o1.getCreatedAt());
				}
			});
		}
	}

	public int getHuidigMaximumAantalPogingen()
	{
		return getObject().getHuidigMaximumAantalPogingen();
	}

	public int getAbsoluutMaximumAantalPogingen()
	{
		return getObject().getAbsoluutMaximumAantalPogingen();
	}

	public boolean isAvailable(IModel<Toets> toetsModel, IModel<Deelnemer> deelnemerModel,
			int pogingNummer)
	{
		Toets toets = toetsModel.getObject();
		if (toets == null
			|| (!toets.isAlternatiefResultaatMogelijk() && pogingNummer == ALTERNATIEF_NR))
			return false;
		if (toets.isVariant() && (toets.getVariantVoorPoging() + OFFSET) != pogingNummer
			&& pogingNummer != RESULTAAT_NR)
			return false;

		List<List<Resultaat>> resultatenVoorToets =
			getObject().getResultaten(toetsModel, deelnemerModel);
		return resultatenVoorToets != null
			&& resultatenVoorToets.size() >= pogingNummer + POGING_START_IDX;
	}

	public boolean isAvailable(Toets toets, Deelnemer deelnemer, int pogingNummer)
	{
		return isAvailable(new Model<Toets>(toets), new Model<Deelnemer>(deelnemer), pogingNummer);
	}

	public Resultaat getResultaat(IModel<Toets> toetsModel, IModel<Deelnemer> deelnemerModel,
			int pogingNummer)
	{
		List<List<Resultaat>> resultatenVoorToets =
			getObject().getResultaten(toetsModel, deelnemerModel);
		if (resultatenVoorToets == null
			|| resultatenVoorToets.size() < pogingNummer + POGING_START_IDX
			|| resultatenVoorToets.get(pogingNummer + OFFSET).isEmpty())
			return null;
		return resultatenVoorToets.get(pogingNummer + OFFSET).get(0);
	}

	public Resultaat getResultaat(Toets toets, Deelnemer deelnemer, int pogingNummer)
	{
		return getResultaat(new Model<Toets>(toets), new Model<Deelnemer>(deelnemer), pogingNummer);
	}

	public void insertResultaten(ResultaatKey key, List<List<Resultaat>> resultaten)
	{
		getObject().insertResultaten(key, resultaten);
	}

	public List<Resultaat> getResultaatHistory(IModel<Toets> toetsModel,
			IModel<Deelnemer> deelnemerModel, int pogingNummer)
	{
		List<List<Resultaat>> resultatenVoorToets =
			getObject().getResultaten(toetsModel, deelnemerModel);
		if (resultatenVoorToets == null
			|| resultatenVoorToets.size() < pogingNummer + POGING_START_IDX
			|| resultatenVoorToets.get(pogingNummer + OFFSET).isEmpty())
			return new ArrayList<Resultaat>();
		return resultatenVoorToets.get(pogingNummer + OFFSET);
	}

	public String getHtmlClassesVoorResultaat(IModel<Toets> toetsModel,
			IModel<Deelnemer> deelnemerModel, int pogingNummer)
	{
		if (!isAvailable(toetsModel, deelnemerModel, pogingNummer))
			return "tblResultaatNA";
		List<Resultaat> resultatenVoorToets =
			getObject().getResultaten(toetsModel, deelnemerModel).get(pogingNummer + OFFSET);

		StringBuilder ret = new StringBuilder();
		if (resultatenVoorToets.isEmpty() || resultatenVoorToets.get(0).isNullResultaat())
			ret.append("tblGeenResultaat");
		else
			ret.append("tblHeeftResultaat");

		Toets toets = toetsModel.getObject();
		Deelnemer deelnemer = deelnemerModel.getObject();
		if (!isAanpasbaar(toets, deelnemer, pogingNummer))
			ret.append(" tblResultaatGeblokkeerd");

		if (pogingNummer == RESULTAAT_NR && isAanpasbaarPogingAanwezig(toets, deelnemer))
			ret.append(" tblAanpasbaarPoging");

		if (resultatenVoorToets.isEmpty())
			return ret.toString();

		Resultaat eersteResultaat = resultatenVoorToets.get(0);
		if (StringUtil.isNotEmpty(eersteResultaat.getNotitie()))
			ret.append(" tblResultaatNote");
		if (eersteResultaat.isOverschreven())
			ret.append(" tblResultaatOverschreven");

		if (eersteResultaat.isTijdelijk() || eersteResultaat.isZonderCijferOfWaarde())
			ret.append(" tblResultaatTijdelijk");
		else if (eersteResultaat.isBehaald())
			ret.append(" tblResultaatBehaald");
		else
			ret.append(" tblResultaatNietBehaald");

		return ret.toString();
	}

	public boolean isBevroren(Toets toets, Deelnemer deelnemer, int pogingNummer)
	{
		return getObject().isBevroren(new ResultaatKey(toets, deelnemer), pogingNummer);
	}

	public boolean isGeblokkeerd(Toets toets, Deelnemer deelnemer, int pogingNummer)
	{
		if (toets == null || !toets.getResultaatstructuur().isBeschikbaar())
			return true;
		if (isBevroren(toets, deelnemer, pogingNummer))
			return true;

		return false;
	}

	public boolean isAanpasbaarPogingAanwezig(Toets toets, Deelnemer deelnemer)
	{
		int maxPoging = toets.getAantalPogingen();
		for (int curPoging = 1; curPoging <= maxPoging; curPoging++)
		{
			if (isAanpasbaar(toets, deelnemer, curPoging))
				return true;
		}
		return false;
	}

	public boolean isAanpasbaar(Toets toets, Deelnemer deelnemer, int pogingNummer)
	{
		if (!isAvailable(toets, deelnemer, pogingNummer))
			return false;

		if (isGeblokkeerd(toets, deelnemer, pogingNummer))
			return false;

		if (toets.isSamengesteld()
			&& (toets.isSamengesteldMetHerkansing() || toets.isSamengesteldMetVarianten()))
			return pogingNummer == ResultatenModel.ALTERNATIEF_NR;

		return pogingNummer != ResultatenModel.RESULTAAT_NR || toets.isOverschrijfbaar();
	}

	public ResultatenInformation getFreshObject()
	{
		detach();
		return getObject();
	}

	@Override
	public ResultatenInformation getObject()
	{
		return super.getObject();
	}

	public boolean isToetsMetAlternatiefAanwezig()
	{
		for (Toets curToets : getToetsen())
		{
			if (curToets.isAlternatiefResultaatMogelijk())
				return true;
		}
		return false;
	}

	@Override
	public void detach()
	{
		super.detach();
		ComponentUtil.detachQuietly(zoekFilter);
		toetsenModel.detach();
		deelnemersModel.detach();
	}

	@Override
	protected ResultatenInformation load()
	{
		return new ResultatenInformation(getToetsen(), getDeelnemers(), zoekFilter);
	}

	public List<Deelnemer> getDeelnemers()
	{
		return deelnemersModel.getObject();
	}

	public List<Toets> getToetsen()
	{
		return toetsenModel.getObject();
	}

	public boolean isEditable()
	{
		return false;
	}

	public void checkResultaten(boolean force)
	{
		if (force
			|| DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
				ResultaatControleSetting.class).getValue().isActief())
		{
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).verifyResultaten(
				zoekFilter);
		}
	}
}
