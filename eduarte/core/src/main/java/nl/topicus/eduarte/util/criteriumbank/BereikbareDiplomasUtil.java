package nl.topicus.eduarte.util.criteriumbank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat.Resultaatsoort;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameZoekFilter;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

/**
 * Util class voor het controleren of een deelnemer kan voldoen aan de criteria voor een
 * andere opleiding dan de eigen opleiding. De util zorgt ervoor dat de onderwijsproducten
 * die een deelnemer afneemt ingezet worden voor een andere opleiding en voert vervolgens
 * een criteriumbankcontrole uit.
 * 
 * 
 * 
 * @author loite
 * 
 */
public class BereikbareDiplomasUtil implements IDetachable
{
	private static final long serialVersionUID = 1L;

	private static final int MAX_AANTAL_INVULLINGEN = 100;

	private final IModel<Deelnemer> deelnemer;

	private final IModel<List<Opleiding>> opleidingen;

	private final IModel<Cohort> cohort;

	private IModel<List<Opleiding>> geslaagdVoorOpleidingen;

	private boolean uitgevoerd;

	private final Map<Long, List<String>> meldingen = new HashMap<Long, List<String>>();

	/**
	 * Constructor voor een BeschikbareDiplomasUtil waarmee gecontroleerd kan worden voor
	 * welke van de gegeven opleidingen de deelnemer in aanmerking zou kunnen komen voor
	 * een diploma.
	 * 
	 * @param deelnemer
	 * @param opleiding
	 * @param cohort
	 */
	public BereikbareDiplomasUtil(Deelnemer deelnemer, Opleiding opleiding, Cohort cohort)
	{
		this(deelnemer, Arrays.asList(opleiding), cohort);
	}

	/**
	 * Constructor voor een BeschikbareDiplomasUtil waarmee gecontroleerd kan worden voor
	 * welke van de gegeven opleidingen de deelnemer in aanmerking zou kunnen komen voor
	 * een diploma.
	 * 
	 * @param deelnemer
	 * @param opleidingen
	 * @param cohort
	 */
	public BereikbareDiplomasUtil(Deelnemer deelnemer, List<Opleiding> opleidingen, Cohort cohort)
	{
		this.deelnemer = ModelFactory.getModel(deelnemer);
		this.opleidingen = ModelFactory.getModel(opleidingen);
		this.cohort = ModelFactory.getModel(cohort);
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer.getObject();
	}

	public List<Opleiding> getOpleidingen()
	{
		return opleidingen.getObject();
	}

	public Cohort getCohort()
	{
		return cohort.getObject();
	}

	public List<Opleiding> getGeslaagdVoorOpleidingen()
	{
		if (!uitgevoerd)
			throw new IllegalStateException(
				"De methode berekenOpleidingenWaarvoorDeDeelnemerIsGeslaagd moet eerst uitgevoerd worden voordat deze methode aangeroepen kan worden.");
		return geslaagdVoorOpleidingen.getObject();
	}

	public boolean isGeslaagdVoorOpleiding(Opleiding opleiding)
	{
		return getGeslaagdVoorOpleidingen().contains(opleiding);
	}

	public void berekenOpleidingenWaarvoorDeDeelnemerIsGeslaagd()
	{
		List<Opleiding> res = new ArrayList<Opleiding>(getOpleidingen().size());
		for (Opleiding opleiding : getOpleidingen())
		{
			// Controleer of deze opleiding uberhaupt criteria heeft.
			if (!opleiding.getLandelijkeEnLokaleCriteria(getCohort()).isEmpty())
			{
				// Bepaal de mogelijke manieren waarop de productregels gevuld kunnen
				// worden.
				List<Map<Productregel, OnderwijsproductAfnameContext>> mogelijkeInvullingen =
					getMogelijkeKeuzeInvullingen(opleiding);
				if (mogelijkeInvullingen.isEmpty())
				{
					putMelding(
						opleiding,
						"Productregels konden niet ingevuld worden. Alleen afgenomen onderwijsproducten met een definitief eindresultaat kan gebruikt worden voor een productregel.");
				}
				else
				{
					// Ga per invulling na of voldaan is aan de criteriumbank.
					for (Map<Productregel, OnderwijsproductAfnameContext> keuzes : mogelijkeInvullingen)
					{
						CriteriumbankControle controle =
							new CriteriumbankControle(getDeelnemer(), opleiding, getCohort(),
								keuzes);
						if (controle.voldoetAanCriteriumbank())
						{
							res.add(opleiding);
							break;
						}
						else
						{
							for (String melding : controle.getAlgemeneMeldingen())
							{
								putMelding(opleiding, melding);
							}
						}
					}
				}
			}
		}
		geslaagdVoorOpleidingen = ModelFactory.getModel(res);
		uitgevoerd = true;
	}

	private void putMelding(Opleiding opleiding, String melding)
	{
		List<String> opleidingMeldingen = meldingen.get(opleiding.getId());
		if (opleidingMeldingen == null)
		{
			opleidingMeldingen = new ArrayList<String>();
			meldingen.put(opleiding.getId(), opleidingMeldingen);
		}
		opleidingMeldingen.add(melding);
	}

	/**
	 * 
	 * @param opleiding
	 * @return De meldingen die geregistreerd zijn voor de gegeven opleiding na uitvoeren
	 *         van de controle.
	 */
	public List<String> getMeldingen(Opleiding opleiding)
	{
		return meldingen.get(opleiding.getId());
	}

	public List<Map<Productregel, OnderwijsproductAfnameContext>> getMogelijkeKeuzeInvullingen(
			Opleiding opleiding)
	{
		List<Productregel> productregels =
			opleiding.getLandelijkeEnLokaleProductregelsBehalveAfgeleideProductregels(getCohort());
		List<List<Integer>> mogelijkeInvullingen =
			new ArrayList<List<Integer>>(productregels.size());
		List<OnderwijsproductAfname> afnames = getAfnamesMetDefinitiefEindresultaat();
		for (Productregel productregel : productregels)
		{
			Set<Onderwijsproduct> toegestaneOnderwijsproducten =
				productregel.getOnderwijsproducten(opleiding);
			List<Integer> invullingen = new ArrayList<Integer>(toegestaneOnderwijsproducten.size());
			int index = 0;
			for (OnderwijsproductAfname afname : afnames)
			{
				if (toegestaneOnderwijsproducten.contains(afname.getOnderwijsproduct()))
				{
					invullingen.add(Integer.valueOf(index));
				}
				index++;
			}
			if (!productregel.isVerplicht())
			{
				// Geen keuze is ook toegestaan.
				invullingen.add(Integer.valueOf(-1));
			}
			mogelijkeInvullingen.add(invullingen);
		}
		// Maak alle mogelijke permutaties.
		List<List<Integer>> alleMogelijkeInvullingen = getPermutations(mogelijkeInvullingen);
		List<Map<Productregel, OnderwijsproductAfnameContext>> res =
			new ArrayList<Map<Productregel, OnderwijsproductAfnameContext>>(
				alleMogelijkeInvullingen.size());
		for (List<Integer> invulling : alleMogelijkeInvullingen)
		{
			Map<Productregel, OnderwijsproductAfnameContext> map =
				new HashMap<Productregel, OnderwijsproductAfnameContext>();
			int index = 0;
			for (Integer i : invulling)
			{
				if (i.equals(Integer.valueOf(-1)))
				{
					map.put(productregels.get(index), null);
				}
				else
				{
					map.put(productregels.get(index), createContext(afnames.get(i.intValue()),
						productregels.get(index)));
				}
				index++;
			}
			res.add(map);
		}
		return res;
	}

	private OnderwijsproductAfnameContext createContext(OnderwijsproductAfname afname,
			Productregel productregel)
	{
		OnderwijsproductAfnameContext context = new OnderwijsproductAfnameContext();
		context.setOnderwijsproductAfname(afname);
		context.setProductregel(productregel);

		return context;
	}

	private List<OnderwijsproductAfname> getAfnamesMetDefinitiefEindresultaat()
	{
		OnderwijsproductAfnameZoekFilter filter =
			new OnderwijsproductAfnameZoekFilter(getDeelnemer());
		List<OnderwijsproductAfname> afnames =
			DataAccessRegistry.getHelper(OnderwijsproductAfnameDataAccessHelper.class).list(filter);
		// Verwijder onderwijsproductafnames zonder een definitief eindresultaat.
		Map<OnderwijsproductAfname, Resultaat> eindresultaten =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).getEindresultaten(
				getDeelnemer(), afnames);
		int index = 0;
		while (index < afnames.size())
		{
			OnderwijsproductAfname afname = afnames.get(index);
			Resultaat res = eindresultaten.get(afname);
			if (res == null || res.getSoort() == Resultaatsoort.Tijdelijk)
			{
				afnames.remove(index);
			}
			else
			{
				index++;
			}
		}
		return afnames;
	}

	private List<List<Integer>> getPermutations(List<List<Integer>> input)
	{
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		if (!input.isEmpty())
		{
			addPermutations(input, res, 0, new ArrayList<Integer>());
		}

		return res;
	}

	private void addPermutations(List<List<Integer>> input, List<List<Integer>> res,
			int currentDepth, List<Integer> currentList)
	{
		if (currentDepth == input.size() - 1)
		{
			if (res.size() < MAX_AANTAL_INVULLINGEN)
			{
				// laatste lijst
				for (Integer i : input.get(currentDepth))
				{
					if (i.equals(Integer.valueOf(-1)) || !currentList.contains(i))
					{
						List<Integer> newList = new ArrayList<Integer>(currentDepth + 1);
						newList.addAll(currentList);
						newList.add(i);
						res.add(newList);
					}
				}
			}
			else
			{
				System.out.println("Too many permutations");
			}
		}
		else
		{
			for (Integer i : input.get(currentDepth))
			{
				if (i.equals(Integer.valueOf(-1)) || !currentList.contains(i))
				{
					List<Integer> newList = new ArrayList<Integer>(currentDepth + 1);
					newList.addAll(currentList);
					newList.add(i);
					if (res.size() < MAX_AANTAL_INVULLINGEN)
					{
						addPermutations(input, res, currentDepth + 1, newList);
					}
				}
			}
		}
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(deelnemer);
		ComponentUtil.detachQuietly(cohort);
		ComponentUtil.detachQuietly(opleidingen);
		ComponentUtil.detachQuietly(geslaagdVoorOpleidingen);
	}

}
