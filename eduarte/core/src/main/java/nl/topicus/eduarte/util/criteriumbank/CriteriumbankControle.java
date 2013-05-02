package nl.topicus.eduarte.util.criteriumbank;

import java.util.*;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.app.functies.AantalGroterDan;
import nl.topicus.eduarte.app.functies.AantalKleinerDan;
import nl.topicus.eduarte.app.functies.Als;
import nl.topicus.eduarte.app.functies.FunctieUtil;
import nl.topicus.eduarte.app.functies.Gemiddeld;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat.Resultaatsoort;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal.Schaaltype;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;

/**
 * Service voor het controleren of een deelnemer voldoet aan alle eisen die bij een
 * opleiding gesteld zijn, oftewel of de deelnemer geslaagd is voor z'n opleiding.
 * 
 * @author loite
 */
public class CriteriumbankControle
{
	/**
	 * Een criteriumbank kan maximaal 1 keer uitgevoerd worden.
	 */
	private boolean uitgevoerd;

	private final Opleiding opleiding;

	private final Deelnemer deelnemer;

	private final Cohort cohort;

	private final List<Productregel> productregels;

	private final Map<Productregel, OnderwijsproductAfnameContext> keuzes;

	private final Map<String, String> eindresultaten;

	private final Map<String, String> eindresultatenStrings;

	private final Map<String, Resultaat> eindresultaatObjecten;

	private final Map<String, Boolean> behaald;

	private final Map<String, Integer> volgnummers;

	/**
	 * Zijn er fouten geconstateerd tijdens de controle, dus bijvoorbeeld fouten in de
	 * formules.
	 */
	private boolean heeftFouten = false;

	/**
	 * Bevat algemene meldingen na het uitvoeren van een criteriumbankcontrole.
	 */
	private final List<String> algemeneMeldingen = new ArrayList<String>();

	private final Map<Productregel, String> productregelMeldingen =
		new HashMap<Productregel, String>();

	private final Map<Criterium, String> criteriumMeldingen = new HashMap<Criterium, String>();

	/**
	 * Criteria die een foutmelding veroorzaken vanwege een syntax error of niet bestaande
	 * formule.
	 */
	private final Set<Criterium> ongeldigeCriteria = new HashSet<Criterium>();

	public CriteriumbankControle(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis.getOpleiding(), verbintenis.getCohort(),
			DataAccessRegistry.getHelper(OnderwijsproductAfnameContextDataAccessHelper.class).list(
				verbintenis));
	}

	public CriteriumbankControle(Deelnemer deelnemer, Opleiding opleiding, Cohort cohort,
			Map<Productregel, OnderwijsproductAfnameContext> keuzes)
	{
		Asserts.assertNotNull("deelnemer", deelnemer);
		Asserts.assertNotNull("opleiding", opleiding);
		Asserts.assertNotNull("cohort", cohort);
		Asserts.assertNotNull("keuzes", keuzes);

		this.cohort = cohort;
		this.opleiding = opleiding;
		this.deelnemer = deelnemer;
		this.keuzes = keuzes;
		productregels = opleiding.getLandelijkeEnLokaleProductregels(cohort);
		eindresultaten = new HashMap<String, String>(productregels.size());
		eindresultatenStrings = new HashMap<String, String>(productregels.size());
		eindresultaatObjecten = new HashMap<String, Resultaat>(productregels.size());
		behaald = new HashMap<String, Boolean>(productregels.size());
		volgnummers = new HashMap<String, Integer>(productregels.size());
		initEindresultaten();
	}

	/**
	 * Constructor voor een criteriumbankcontrole voor een bepaalde opleiding. Hiermee kan
	 * de werking van de formules die bij een opleiding zijn gedefinieerd gecontroleerd
	 * worden.
	 * 
	 * @param opleiding
	 * @param cohort
	 */
	CriteriumbankControle(Opleiding opleiding, Cohort cohort)
	{
		Asserts.assertNotNull("opleiding", opleiding);
		Asserts.assertNotNull("cohort", cohort);
		this.cohort = cohort;
		this.opleiding = opleiding;
		this.deelnemer = null;
		productregels = opleiding.getLandelijkeEnLokaleProductregels(cohort);
		keuzes = new HashMap<Productregel, OnderwijsproductAfnameContext>(productregels.size());
		eindresultaten = new HashMap<String, String>(productregels.size());
		eindresultatenStrings = new HashMap<String, String>(productregels.size());
		eindresultaatObjecten = new HashMap<String, Resultaat>(productregels.size());
		behaald = new HashMap<String, Boolean>(productregels.size());
		volgnummers = new HashMap<String, Integer>(productregels.size());
		initRandomKeuzes();
		initRandomEindresultaten();
	}

	private void initRandomKeuzes()
	{
		Random rnd = new Random();
		for (Productregel productregel : productregels)
		{
			Set<Onderwijsproduct> mogelijk = productregel.getOnderwijsproducten(opleiding);
			if (mogelijk.size() > 0)
			{
				List<Onderwijsproduct> mogelijkLijst = new ArrayList<Onderwijsproduct>(mogelijk);
				int index = rnd.nextInt(mogelijk.size());
				OnderwijsproductAfname afname = new OnderwijsproductAfname();
				afname.setCohort(cohort);
				afname.setOnderwijsproduct(mogelijkLijst.get(index));
				OnderwijsproductAfnameContext keuze = new OnderwijsproductAfnameContext();
				keuze.setOnderwijsproductAfname(afname);
				keuze.setProductregel(productregel);
				keuzes.put(productregel, keuze);
			}
			OnderwijsproductAfnameContext keuze = keuzes.get(productregel);
			if (keuze == null)
			{
				eindresultaten.put(productregel.getAfkorting(), FunctieUtil.GEEN_CIJFER
					.toPlainString());
				eindresultatenStrings.put(productregel.getAfkorting(), "''");
				behaald.put(productregel.getAfkorting(), Boolean.FALSE);
				volgnummers.put(productregel.getAfkorting(), Integer.valueOf(-1));
			}
			else
			{
				int res = rnd.nextInt(10) + 1;
				eindresultaten.put(productregel.getAfkorting(), String.valueOf(res));
				eindresultatenStrings.put(productregel.getAfkorting(), "'" + res + "'");
				behaald.put(productregel.getAfkorting(), Boolean.valueOf(res >= 6));
				volgnummers.put(productregel.getAfkorting(), Integer.valueOf(res));
			}
		}
	}

	private void initEindresultaten()
	{
		List<OnderwijsproductAfname> afnames = new ArrayList<OnderwijsproductAfname>(keuzes.size());
		for (OnderwijsproductAfnameContext keuze : keuzes.values())
		{
			if (keuze != null)
			{
				afnames.add(keuze.getOnderwijsproductAfname());
			}
		}
		Map<OnderwijsproductAfname, Resultaat> tempResultaten =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).getEindresultaten(
				deelnemer, afnames);
		List<Criterium> criteria = opleiding.getLandelijkeEnLokaleCriteria(cohort);
		boolean gebruiktToetscodes = gebruiktToetscodes(criteria);
		for (Productregel productregel : productregels)
		{
			OnderwijsproductAfnameContext keuze = keuzes.get(productregel);
			Resultaat resultaat = null;
			if (keuze != null)
			{
				resultaat = tempResultaten.get(keuze.getOnderwijsproductAfname());
			}
			else if (productregel.getTypeProductregel() == TypeProductregel.AfgeleideProductregel)
			{
				resultaat = productregel.getEindresultaat(deelnemer, opleiding, keuzes);
			}
			boolean tekstueel = resultaat == null ? false : resultaat.isTekstueel();
			eindresultaatObjecten.put(productregel.getAfkorting(), resultaat);
			String cijferWaarde =
				resultaat == null || resultaat.getSoort() == Resultaatsoort.Tijdelijk
					? FunctieUtil.GEEN_CIJFER.toPlainString() : StringUtil
						.valueOrAlternativeIfNull(resultaat.getCijfer(), FunctieUtil.GEEN_CIJFER
							.toPlainString());
			String stringWaarde =
				resultaat == null || resultaat.getSoort() == Resultaatsoort.Tijdelijk
					? FunctieUtil.GEEN_CIJFER.toPlainString() : StringUtil
						.valueOrAlternativeIfNull(resultaat.getDisplayWaarde(true),
							FunctieUtil.GEEN_CIJFER.toPlainString());
			stringWaarde = "'" + stringWaarde + "'";

			if (tekstueel)
			{
				volgnummers.put(productregel.getAfkorting(), resultaat == null
					|| resultaat.getSoort() == Resultaatsoort.Tijdelijk ? Integer.valueOf(-1)
					: Integer.valueOf(resultaat.getVolgnummer()));
			}
			else
			{
				volgnummers.put(productregel.getAfkorting(), Integer.valueOf(-1));
			}
			eindresultaten.put(productregel.getAfkorting(), cijferWaarde);
			eindresultatenStrings.put(productregel.getAfkorting(), stringWaarde);

			Boolean productregelBehaald;
			boolean isEvc = false;
			if (keuze != null
				&& (keuze.getOnderwijsproductAfname().getVrijstellingType().isVrijstelling()))
			{
				productregelBehaald = Boolean.TRUE;
				isEvc = true;
			}
			else
			{
				productregelBehaald =
					resultaat == null ? Boolean.FALSE : Boolean.valueOf(resultaat.isBehaald());
			}
			behaald.put(productregel.getAfkorting(), productregelBehaald);

			if (gebruiktToetscodes && keuze != null
				&& keuze.getProductregel().getTypeProductregel() == TypeProductregel.Productregel)
			{
				Set<String> toetscodes = getToetscodes(productregel);
				Map<String, Resultaat> toetsresultaten = getToetsResultaten(keuze);
				for (String toets : toetscodes)
				{
					resultaat = toetsresultaten.get(toets);
					tekstueel = resultaat == null ? false : resultaat.isTekstueel();
					cijferWaarde =
						resultaat == null || resultaat.getSoort() == Resultaatsoort.Tijdelijk
							? FunctieUtil.GEEN_CIJFER.toPlainString() : StringUtil
								.valueOrAlternativeIfNull(resultaat.getCijfer(),
									FunctieUtil.GEEN_CIJFER.toPlainString());
					stringWaarde =
						resultaat == null || resultaat.getSoort() == Resultaatsoort.Tijdelijk
							? FunctieUtil.GEEN_CIJFER.toPlainString() : StringUtil
								.valueOrAlternativeIfNull(resultaat.getDisplayWaarde(true),
									FunctieUtil.GEEN_CIJFER.toPlainString());
					stringWaarde = "'" + stringWaarde + "'";
					eindresultaatObjecten.put(productregel.getAfkorting() + "." + toets, resultaat);
					eindresultaten.put(productregel.getAfkorting() + "." + toets, cijferWaarde);
					eindresultatenStrings.put(productregel.getAfkorting() + "." + toets,
						stringWaarde);
					if (tekstueel)
					{
						volgnummers.put(productregel.getAfkorting() + "." + toets,
							resultaat == null || resultaat.getSoort() == Resultaatsoort.Tijdelijk
								? Integer.valueOf(-1) : Integer.valueOf(resultaat.getVolgnummer()));
					}
					else
					{
						volgnummers.put(productregel.getAfkorting() + "." + toets, Integer
							.valueOf(-1));
					}
					if (isEvc)
					{
						behaald.put(productregel.getAfkorting() + "." + toets, Boolean.TRUE);
					}
					else
					{
						behaald.put(productregel.getAfkorting() + "." + toets, resultaat == null
							? Boolean.FALSE : Boolean.valueOf(resultaat.isBehaald()));
					}
				}
			}
		}
	}

	private boolean gebruiktToetscodes(List<Criterium> criteria)
	{
		for (Productregel productregel : productregels)
		{
			for (Criterium criterium : criteria)
			{
				if (criterium.getFormule() != null
					&& criterium.getFormule().contains("#{" + productregel.getAfkorting() + "."))
				{
					return true;
				}
			}
		}
		return false;
	}

	private void initRandomEindresultaten()
	{
		Random rnd = new Random();
		for (Productregel productregel : productregels)
		{
			OnderwijsproductAfnameContext keuze = keuzes.get(productregel);
			Set<String> toetscodes = getToetscodes(productregel);
			if (keuze == null)
			{
				eindresultaten.put(productregel.getAfkorting(), FunctieUtil.GEEN_CIJFER
					.toPlainString());
				eindresultatenStrings.put(productregel.getAfkorting(), "''");
				behaald.put(productregel.getAfkorting(), Boolean.FALSE);
				volgnummers.put(productregel.getAfkorting(), Integer.valueOf(-1));
				for (String toets : toetscodes)
				{
					String code = productregel.getAfkorting() + "." + toets;
					eindresultaten.put(code, FunctieUtil.GEEN_CIJFER.toPlainString());
					eindresultatenStrings.put(code, "''");
					behaald.put(code, Boolean.FALSE);
					volgnummers.put(code, Integer.valueOf(-1));
				}
			}
			else
			{
				int res = rnd.nextInt(10) + 1;
				eindresultaten.put(productregel.getAfkorting(), String.valueOf(res));
				eindresultatenStrings.put(productregel.getAfkorting(), "'" + res + "'");
				behaald.put(productregel.getAfkorting(), Boolean.valueOf(res >= 6));
				volgnummers.put(productregel.getAfkorting(), Integer.valueOf(res));
				for (String toets : toetscodes)
				{
					String code = productregel.getAfkorting() + "." + toets;
					eindresultaten.put(code, String.valueOf(res));
					eindresultatenStrings.put(code, "'" + res + "'");
					behaald.put(code, Boolean.valueOf(res >= 6));
					volgnummers.put(code, Integer.valueOf(res));
				}
			}
		}
	}

	private Set<String> getToetscodes(Productregel productregel)
	{
		return productregel.getToetscodesVoorCriteriumbank(opleiding);
	}

	private Map<String, Resultaat> getToetsResultaten(OnderwijsproductAfnameContext context)
	{
		List<Toets> toetsen = getToetsen(context);
		Map<String, Resultaat> resultaten = new HashMap<String, Resultaat>();
		ResultaatZoekFilter filter = new ResultaatZoekFilter(toetsen);
		filter.setDeelnemers(Arrays.asList(context.getOnderwijsproductAfname().getDeelnemer()));
		List<Resultaat> list =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).list(filter);
		for (Resultaat res : list)
		{
			if (res.isGeldend())
			{
				resultaten.put(res.getToets().getCode(), res);
			}
		}
		return resultaten;
	}

	private List<Toets> getToetsen(OnderwijsproductAfnameContext context)
	{
		ResultaatstructuurDataAccessHelper helper =
			DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class);
		Resultaatstructuur structuur =
			helper.getSummatieveResultaatstructuur(context.getOnderwijsproductAfname()
				.getOnderwijsproduct(), context.getOnderwijsproductAfname().getCohort());
		if (structuur == null)
			return Collections.emptyList();
		return new ArrayList<Toets>(structuur.getEindresultaat().getChildren());
	}

	/**
	 * @return true indien de gegeven verbintenis voldoet aan alle eisen die gesteld zijn
	 *         voor de opleiding waaraan de verbintenis is gekoppeld. De lijst met
	 *         meldingen wordt gevuld met de eventuele meldingen voor de gegeven
	 *         verbintenis.
	 * @throws IllegalStateException
	 *             Als de mehtode al een keer aangeroepen is voor deze
	 *             criteriumbankcontrole.
	 */
	public boolean voldoetAanCriteriumbank()
	{
		if (uitgevoerd)
		{
			throw new IllegalStateException(
				"Deze criteriumbankcontrole is al uitgevoerd. Een criteriumbankcontrole kan maximaal een keer uitgevoerd worden.");
		}
		// Controleer dat de verbintenis alle benodigde gegevens bevat.
		if (opleiding == null)
		{
			algemeneMeldingen.add("Verbintenis heeft geen opleiding");
			return false;
		}
		if (cohort == null)
		{
			algemeneMeldingen.add("Verbintenis heeft geen cohort");
			return false;
		}

		if (!voldoetAanProductregels())
			return false;
		if (!voldoetAanVerplichteResultaten())
			return false;
		if (!voldoetAanCriteria())
			return false;
		return true;
	}

	/**
	 * @return true indien de gegeven verbintenis voldoet aan alle eisen die gesteld
	 *         worden door de productregels die bij de opleiding en het verbintenisgebied
	 *         gedefinieerd zijn.
	 */
	private boolean voldoetAanProductregels()
	{
		boolean succes = true;
		for (Productregel productregel : productregels)
		{
			OnderwijsproductAfnameContext keuze = keuzes.get(productregel);
			if (keuze == null
				&& productregel.getTypeProductregel() == TypeProductregel.Productregel
				&& productregel.isVerplicht())
			{
				algemeneMeldingen.add("Deelnemer heeft geen keuze gemaakt voor productregel "
					+ productregel.getNaam());
				productregelMeldingen.put(productregel, "Geen keuze gemaakt");
				succes = false;
			}
			else if (keuze != null)
			{
				// controleer dat de keuze daadwerkelijk geldig is.
				Set<Onderwijsproduct> producten =
					productregel.getOnderwijsproductenMetCache(opleiding);
				if (!producten.contains(keuze.getOnderwijsproductAfname().getOnderwijsproduct()))
				{
					algemeneMeldingen.add("Het onderwijsproduct "
						+ keuze.getOnderwijsproductAfname().getOnderwijsproduct()
							.getContextInfoOmschrijving() + " is niet geldig voor de productregel "
						+ productregel.getNaam());
					productregelMeldingen.put(productregel, "Ongeldige keuze gemaakt");
					succes = false;
				}
				// Controleer dat een eventuele titel van een werkstuk is ingevuld.
				if (keuze.getOnderwijsproductAfname().getOnderwijsproduct().isHeeftWerkstuktitel())
				{
					if (StringUtil.isEmpty(keuze.getOnderwijsproductAfname().getWerkstuktitel()))
					{
						algemeneMeldingen.add("Er is geen titel ingevoerd voor "
							+ keuze.getOnderwijsproductAfname().getOnderwijsproduct()
								.getContextInfoOmschrijving());
						productregelMeldingen.put(productregel, "Geen titel ingevoerd");
						succes = false;
					}
				}
			}
		}
		return succes;
	}

	/**
	 * 
	 * @return true indien de gegeven verbintenis voldoet aan alle eisen mbt verplichte
	 *         resultaten. Dat wil zeggen dat alle verplichte productregels een defintief
	 *         resultaat moet hebben.
	 */
	private boolean voldoetAanVerplichteResultaten()
	{
		boolean succes = true;
		for (Productregel productregel : productregels)
		{
			if (productregel.isVerplicht())
			{
				OnderwijsproductAfnameContext keuze = keuzes.get(productregel);
				if (keuze != null
					&& !keuze.getOnderwijsproductAfname().getVrijstellingType().isVrijstelling()
					&& keuze.getOnderwijsproductAfname().getOnderwijsproduct().getSoortProduct()
						.isSummatief())
				{
					Resultaat resultaat = eindresultaatObjecten.get(productregel.getAfkorting());
					if (resultaat == null || resultaat.getCijfer() == null
						|| resultaat.getSoort() == Resultaatsoort.Tijdelijk)
					{
						algemeneMeldingen
							.add("Deelnemer heeft geen definitief resultaat voor productregel "
								+ productregel.getNaam());
						productregelMeldingen.put(productregel, "Geen definitief resultaat");
						succes = false;
					}
				}
			}
		}
		return succes;
	}

	/**
	 * @return true indien de gegeven verbintenis voldoet aan alle eisen die gesteld
	 *         worden in criteria bij de opleiding en het verbintenisgebied.
	 */
	boolean voldoetAanCriteria()
	{
		boolean succes = true;
		try
		{
			// Maak een evaluator.
			Evaluator eval = prepareEval();
			List<Criterium> criteria = opleiding.getLandelijkeEnLokaleCriteria(cohort);
			if (criteria.isEmpty())
			{
				// Geen criteria gedefinieerd, dus ook geen controle mogelijk.
				algemeneMeldingen.add("Er zijn geen criteria gedefinieerd voor de opleiding");
				return false;
			}
			for (Criterium criterium : criteria)
			{
				if (!voldoetAanCriterium(criterium, eval))
					succes = false;
			}
			return succes;
		}
		catch (Exception e)
		{
			algemeneMeldingen.add("Er is een fout opgetreden bij het controleren van de criteria: "
				+ e.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * Methode om een bepaald criterium te testen.
	 * 
	 * @param criterium
	 * @return true indien het criterium geen fouten bevat en de deelnemer aan het
	 *         criterium voldoet. Methode is alleen bedoeld voor testdoeleinden.
	 */
	boolean voldoetAanCriterium(Criterium criterium)
	{
		try
		{
			// Maak een evaluator.
			Evaluator eval = prepareEval();
			return voldoetAanCriterium(criterium, eval);
		}
		catch (Exception e)
		{
			algemeneMeldingen.add("Er is een fout opgetreden bij het controleren van de criteria: "
				+ e.getLocalizedMessage());
			return false;
		}
	}

	private boolean voldoetAanCriterium(Criterium criterium, Evaluator eval)
	{
		boolean succes = true;
		String res = null;
		try
		{
			eval.parse(criterium.getFormule());
			res = eval.evaluate();
			Double doubleRes = Double.valueOf(res);
			if (doubleRes.doubleValue() == 0.0)
			{
				algemeneMeldingen.add(criterium.getMelding());
				criteriumMeldingen.put(criterium, criterium.getMelding());
				succes = false;
			}
		}
		catch (EvaluationException e)
		{
			algemeneMeldingen
				.add("Het berekenen van de formule van het criterium '" + criterium.getNaam()
					+ "' is niet gelukt. Foutmelding: " + e.getLocalizedMessage());
			criteriumMeldingen.put(criterium, "Berekenen van formule mislukt. Foutmelding: "
				+ e.getLocalizedMessage());
			ongeldigeCriteria.add(criterium);
			succes = false;
			heeftFouten = true;
		}
		catch (NumberFormatException e)
		{
			// Resultaat van formule is geen getal.
			algemeneMeldingen.add("De uitkomst van het criterium '" + criterium.getNaam()
				+ "' was geen getal. Uitkomst: " + res);
			criteriumMeldingen.put(criterium,
				"De uitkomst van de formule is geen getal. Uitkomst: " + res);
			ongeldigeCriteria.add(criterium);
			succes = false;
			heeftFouten = true;
		}
		return succes;
	}

	/**
	 * Bereidt een nieuwe evaluator voor de gegeven verbintenis. Dit houdt o.a. in het
	 * toevoegen van alle mogelijke variabelen en functies aan de evaluator.
	 * 
	 * @return Een evaluator die gebruikt kan worden voor de gegeven verbintenis.
	 */
	private Evaluator prepareEval()
	{
		Evaluator eval = new Evaluator();
		// Voeg alle mogelijke variabelen toe aan de evaluator.
		// Voeg de eindresultaten van de productregels toe.
		for (String afkorting : eindresultaten.keySet())
		{
			boolean geldig = true;
			try
			{
				eval.isValidName(afkorting);
			}
			catch (IllegalArgumentException e)
			{
				geldig = false;
			}
			if (geldig)
			{
				eval.putVariable(afkorting, eindresultaten.get(afkorting));
				Resultaat res = eindresultaatObjecten.get(afkorting);
				if (res == null
					|| (res.getToets() != null && res.getToets().getSchaal().getSchaaltype() == Schaaltype.Tekstueel))
				{
					eval
						.putVariable(afkorting + "_cijfer", FunctieUtil.GEEN_CIJFER.toPlainString());
				}
				else
				{
					eval.putVariable(afkorting + "_cijfer", eindresultaten.get(afkorting));
				}
				eval.putVariable(afkorting + "_tekst", eindresultatenStrings.get(afkorting));
				eval.putVariable(afkorting + "_behaald", behaald.get(afkorting).booleanValue()
					? "1" : "0");
				eval.putVariable(afkorting + "_volgnummer", volgnummers.get(afkorting).toString());
				eval.putVariable(afkorting + "_studiepunten", (res == null ? "0" : String
					.valueOf(res.getStudiepunten())));
			}
		}

		for (Productregel productregel : productregels)
		{
			// Voeg gegevens van het geselecteerde onderwijsproduct toe.
			OnderwijsproductAfnameContext keuze = keuzes.get(productregel);
			if (keuze != null)
			{
				eval.putVariable(productregel.getAfkorting() + "_code", "'"
					+ keuze.getOnderwijsproductAfname().getOnderwijsproduct().getCode() + "'");
				eval.putVariable(productregel.getAfkorting() + "_externeCode", "'"
					+ keuze.getOnderwijsproductAfname().getOnderwijsproduct().getExterneCode()
					+ "'");
				eval.putVariable(productregel.getAfkorting() + "_taxonomiecode", "'"
					+ keuze.getOnderwijsproductAfname().getOnderwijsproduct().getTaxonomiecode()
					+ "'");
				eval.putVariable(productregel.getAfkorting() + "_credits", StringUtil
					.valueOrAlternativeIfNull(keuze.getOnderwijsproductAfname().getCredits(), "0"));
			}
			else
			{
				eval.putVariable(productregel.getAfkorting() + "_code", "''");
				eval.putVariable(productregel.getAfkorting() + "_externeCode", "''");
				eval.putVariable(productregel.getAfkorting() + "_taxonomiecode", "''");
			}
			eval.putVariable(productregel.getAfkorting() + "_ingevuld", keuze == null ? "0" : "1");
			// Voeg minimale waarde van de productregel toe.
			eval.putVariable(productregel.getAfkorting() + "_min", StringUtil
				.valueOrEmptyIfNull(productregel.getMinimaleWaarde()));
			eval.putVariable(productregel.getAfkorting() + "_minTekst", "'"
				+ StringUtil.valueOrEmptyIfNull(productregel.getMinimaleWaardeTekst()) + "'");
		}
		eval.putFunction(AantalKleinerDan.INSTANCE);
		eval.putFunction(AantalGroterDan.INSTANCE);
		eval.putFunction(Gemiddeld.INSTANCE);
		eval.putFunction(Als.INSTANCE);

		return eval;
	}

	public boolean isHeeftFouten()
	{
		return heeftFouten;
	}

	/**
	 * @return Returns the algemeneMeldingen.
	 */
	public List<String> getAlgemeneMeldingen()
	{
		return algemeneMeldingen;
	}

	/**
	 * @return Returns the productregelMeldingen.
	 */
	public Map<Productregel, String> getProductregelMeldingen()
	{
		return productregelMeldingen;
	}

	/**
	 * @return Returns the criteriumMeldingen.
	 */
	public Map<Criterium, String> getCriteriumMeldingen()
	{
		return criteriumMeldingen;
	}

	/**
	 * 
	 * @return Alle meldingen die gegenereerd zijn door de criteriumbankcontrole,
	 *         gescheiden door linefeeds.
	 */
	public String getAlleMeldingenFormatted()
	{
		StringBuilder res = new StringBuilder();
		for (String melding : getAlgemeneMeldingen())
		{
			res.append(melding).append("\n");
		}

		return res.toString();
	}

	public Set<Criterium> getOngeldigeCriteria()
	{
		return ongeldigeCriteria;
	}

}
