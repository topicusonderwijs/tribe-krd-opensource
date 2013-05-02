package nl.topicus.eduarte.rapportage.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.inwords.Dutch;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.ResultaatHibernateDataAccessHelper.TypeResultaat;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatVrijstelling;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.rapportage.model.DeelnemerResultatenRapportageModel;
import nl.topicus.eduarte.rapportage.model.VergaderlijstRapportageModel;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

/**
 * Class die specifieke rapportagemethodes voor Verbintenis bevat. Ipv de verbintenisclass
 * vol te gooien met dit soort methodes kunnen deze allemaal hier geplaats worden.
 * Vervolgens moet een delegate method in Verbintenis geplaatst worden.
 * 
 * @author loite
 * 
 */
public class VerbintenisRapportage
{
	private final Verbintenis verbintenis;

	public VerbintenisRapportage(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	/**
	 * @return een lijst met de TaxonomieElement van alle onderwijsproducttaxonomieën van
	 *         alle onderwijsproductafnamecontexten van deze verbintenis. Oftewel: Het
	 *         taxonomie-element van alle onderwijsproducten die bij een productregel bij
	 *         deze verbintenis zijn gekozen.
	 */
	public List<TaxonomieElement> getTaxonomieElementenVanProductregelkeuzes()
	{
		List<TaxonomieElement> res = new ArrayList<TaxonomieElement>();
		for (OnderwijsproductAfnameContext context : verbintenis.getAfnameContexten())
		{
			// Probeer het specifieke taxonomie-element voor deze keuze te vinden.
			TaxonomieElement element = context.getTaxonomieElement();
			if (element == null)
			{
				// Geen specifieke keuze gevonden, voeg eventueel alle mogelijkheden toe.
				Onderwijsproduct product =
					context.getOnderwijsproductAfname().getOnderwijsproduct();
				for (OnderwijsproductTaxonomie productTaxonomie : product
					.getOnderwijsproductTaxonomieList())
				{
					res.add(productTaxonomie.getTaxonomieElement());
				}
			}
			else
			{
				// Voeg het specifieke element toe.
				res.add(element);
			}
		}

		return res;
	}

	/**
	 * 
	 * @return Het aantal gekozen onderwijsproducten (bij een productregel) bij deze
	 *         verbintenis.
	 */
	public String getAantalGekozenOnderwijsproductenInWoorden()
	{
		long count = getTaxonomieElementenVanProductregelkeuzes().size();
		return new Dutch().toWords(count);
	}

	public List<OnderwijsproductAfnameContext> getOnderwijsproductAfnameContextenVoorCijferlijst()
	{
		List<OnderwijsproductAfnameContext> res =
			new ArrayList<OnderwijsproductAfnameContext>(verbintenis.getAfnameContexten().size());
		for (OnderwijsproductAfnameContext context : verbintenis.getAfnameContexten())
		{
			if (context.isToonOpCijferlijst())
			{
				res.add(context);
			}
		}
		return res;
	}

	public List<OnderwijsproductAfname> getOnderwijsproductAfnamesVoorCijferlijst()
	{
		List<OnderwijsproductAfname> res =
			new ArrayList<OnderwijsproductAfname>(verbintenis.getAfnameContexten().size());
		for (OnderwijsproductAfnameContext context : verbintenis.getAfnameContexten())
		{
			if (context.isToonOpCijferlijst())
			{
				res.add(context.getOnderwijsproductAfname());
			}
		}
		return res;
	}

	/**
	 * 
	 * @return Alle eindresultaten die op de cijferlijst moeten komen. Dat zijn alle
	 *         onderwijsproductafnamecontexten die het vinkje 'Cijferlijst' aan hebben
	 *         staan.
	 */
	public Resultaat getEindresultaatCombinatievak()
	{
		// Maak een lijst in de volgorde van de productregels.
		List<Productregel> productregels =
			verbintenis.getOpleiding().getLandelijkeEnLokaleProductregels(verbintenis.getCohort());

		for (Productregel productregel : productregels)
		{
			if ("Combinatievak".equals(productregel.getNaam()))
			{
				return productregel.getEindresultaat(verbintenis);
			}
		}

		return null;
	}

	public List<Resultaat> getEindresultatenCijferlijst()
	{
		List<OnderwijsproductAfname> afnames = getOnderwijsproductAfnamesVoorCijferlijst();
		Map<OnderwijsproductAfname, Resultaat> tempResultaten =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).getEindresultaten(
				verbintenis.getDeelnemer(), afnames);
		// Maak een lijst in de volgorde van de productregels.
		List<Productregel> productregels =
			verbintenis.getOpleiding().getLandelijkeEnLokaleProductregels(verbintenis.getCohort());
		List<Resultaat> res = new ArrayList<Resultaat>(afnames.size());
		List<OnderwijsproductAfnameContext> keuzes =
			getOnderwijsproductAfnameContextenVoorCijferlijst();
		for (Productregel productregel : productregels)
		{
			for (OnderwijsproductAfnameContext keuze : keuzes)
			{
				if (keuze.getProductregel().equals(productregel)
					&& keuze.isToonOpCijferlijst()
					&& !keuze.getOnderwijsproductAfname().getOnderwijsproduct()
						.isHeeftWerkstuktitel())
				{
					if (keuze.getOnderwijsproductAfname().getVrijstellingType().isVrijstelling())
					{
						res.add(new ResultaatVrijstelling(keuze.getOnderwijsproductAfname()));
					}
					else
					{
						Resultaat eind = tempResultaten.get(keuze.getOnderwijsproductAfname());
						if (eind != null && !eind.isTijdelijk())
						{
							res.add(eind);
						}
					}
					break;

				}
			}
		}
		return res;
	}

	/**
	 * 
	 * @return Alle eindresultaten van een bepaald soort (SoortOnderwijsproduct) die op de
	 *         cijferlijst moeten komen. Dat zijn alle onderwijsproductafnamecontexten van
	 *         het juiste soort die het vinkje 'Cijferlijst' aan hebben staan.
	 */
	public List<Resultaat> getEindresultatenCijferlijstOpSoort(String soortOnderwijsproduct)
	{
		List<OnderwijsproductAfname> afnames = getOnderwijsproductAfnamesVoorCijferlijst();
		Map<OnderwijsproductAfname, Resultaat> tempResultaten =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).getEindresultaten(
				verbintenis.getDeelnemer(), afnames);
		// Maak een lijst in de volgorde van de productregels.
		List<Productregel> productregels =
			verbintenis.getOpleiding().getLandelijkeEnLokaleProductregels(verbintenis.getCohort());
		List<Resultaat> res = new ArrayList<Resultaat>(afnames.size());
		List<OnderwijsproductAfnameContext> keuzes =
			getOnderwijsproductAfnameContextenVoorCijferlijst();
		for (Productregel productregel : productregels)
		{
			for (OnderwijsproductAfnameContext keuze : keuzes)
			{
				if (keuze.getProductregel().equals(productregel)
					&& keuze.isToonOpCijferlijst()
					&& !keuze.getOnderwijsproductAfname().getOnderwijsproduct()
						.isHeeftWerkstuktitel()
					&& keuze.getOnderwijsproductAfname().getOnderwijsproduct().getSoortProduct()
						.getNaam().equals(soortOnderwijsproduct))
				{
					if (keuze.getOnderwijsproductAfname().getVrijstellingType().isVrijstelling())
					{
						res.add(new ResultaatVrijstelling(keuze.getOnderwijsproductAfname()));
					}
					else
					{
						Resultaat eind = tempResultaten.get(keuze.getOnderwijsproductAfname());
						if (!eind.isTijdelijk())
						{
							res.add(eind);
						}
					}
					break;
				}
			}
		}
		return res;
	}

	/**
	 * 
	 * @return Alle eindresultaten die op de cijferlijst moeten komen, behalve de
	 *         taalvaardigheden
	 */
	public List<Resultaat> getEindresultatenCijferlijstZonderTalen()
	{
		List<Resultaat> ret = new ArrayList<Resultaat>(getEindresultatenCijferlijst());
		for (Resultaat resultaat : getEindresultatenCijferlijst())
		{
			// resultaat kan ook een ResultaatVrijstelling zijn, deze heeft geen
			// gekoppelde toets.
			if (Resultaat.class.equals(resultaat.getClass()))
			{
				for (Toets toets : resultaat.getToets().getResultaatstructuur().getToetsen())
				{
					if (toets.getSoort().isNT2VaardigheidToets())
					{
						ret.remove(resultaat);
						break;
					}
				}
			}
		}
		return ret;
	}

	/**
	 * 
	 * @return Alle eindresultaten die op de cijferlijst moeten komen, behalve de
	 *         taalvaardigheden
	 */
	public List<Resultaat> getEindresultatenCijferlijstAlleenTalen()
	{
		List<Resultaat> ret = new ArrayList<Resultaat>();
		for (Resultaat resultaat : getEindresultatenCijferlijst())
		{
			// resultaat kan ook een ResultaatVrijstelling zijn, deze heeft geen
			// gekoppelde toets.
			if (Resultaat.class.equals(resultaat.getClass()))
			{
				for (Toets toets : resultaat.getToets().getResultaatstructuur().getToetsen())
				{
					if (toets.getSoort().isNT2VaardigheidToets())
					{
						ret.add(resultaat);
						break;
					}
				}
			}
		}
		return ret;
	}

	/**
	 * 
	 * @return Alle schoolexamenresultaten die op de cijferlijst moeten komen. Dat zijn
	 *         alle onderwijsproductafnamecontexten die het vinkje 'Cijferlijst' aan
	 *         hebben staan.
	 */
	public List<Resultaat> getSchoolexamenResultatenCijferlijst()
	{
		List<OnderwijsproductAfname> afnames = getOnderwijsproductAfnamesVoorCijferlijst();
		Map<Onderwijsproduct, Resultaat> tempResultaten =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class)
				.getDefinitieveSchoolexamenResultaten(verbintenis.getDeelnemer(), afnames,
					TypeResultaat.InSamengesteld);
		// Maak een lijst in de volgorde van de productregels.
		List<Productregel> productregels =
			verbintenis.getOpleiding().getLandelijkeEnLokaleProductregels(verbintenis.getCohort());
		List<Resultaat> res = new ArrayList<Resultaat>(afnames.size());
		List<OnderwijsproductAfnameContext> keuzes =
			getOnderwijsproductAfnameContextenVoorCijferlijst();
		for (Productregel productregel : productregels)
		{
			for (OnderwijsproductAfnameContext keuze : keuzes)
			{
				if (keuze.getProductregel().equals(productregel)
					&& keuze.isToonOpCijferlijst()
					&& !keuze.getOnderwijsproductAfname().getOnderwijsproduct()
						.isHeeftWerkstuktitel())
				{
					res.add(tempResultaten.get(keuze.getOnderwijsproductAfname()
						.getOnderwijsproduct()));
					break;
				}
			}
		}
		return res;
	}

	/**
	 * 
	 * @return Alle eindresultaten die op de cijferlijst moeten komen. Dat zijn alle
	 *         onderwijsproductafnamecontexten die het vinkje 'Cijferlijst' aan hebben
	 *         staan.
	 */
	public List<Resultaat> getCentraalExamenResultatenCijferlijst()
	{
		List<OnderwijsproductAfname> afnames = getOnderwijsproductAfnamesVoorCijferlijst();
		Map<Onderwijsproduct, List<Resultaat>> tempResultaten =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class)
				.getDefinitieveCentraalExamenResultaten(verbintenis.getDeelnemer(), afnames);
		// Maak een lijst in de volgorde van de productregels.
		List<Productregel> productregels =
			verbintenis.getOpleiding().getLandelijkeEnLokaleProductregels(verbintenis.getCohort());
		List<Resultaat> res = new ArrayList<Resultaat>(afnames.size());
		List<OnderwijsproductAfnameContext> keuzes =
			getOnderwijsproductAfnameContextenVoorCijferlijst();
		for (Productregel productregel : productregels)
		{
			for (OnderwijsproductAfnameContext keuze : keuzes)
			{
				if (keuze.getProductregel().equals(productregel)
					&& keuze.isToonOpCijferlijst()
					&& !keuze.getOnderwijsproductAfname().getOnderwijsproduct()
						.isHeeftWerkstuktitel())
				{
					List<Resultaat> ceResultaten =
						tempResultaten.get(keuze.getOnderwijsproductAfname().getOnderwijsproduct());
					if (ceResultaten != null)
					{
						boolean found = false;
						for (Resultaat ce : ceResultaten)
						{
							if (ce.isInSamengesteld())
							{
								res.add(ce);
								found = true;
								break;
							}
						}
						if (!found)
						{
							for (Resultaat ce : ceResultaten)
							{
								if (ce.isGeldend())
								{
									res.add(ce);
									break;
								}
							}
						}
					}
					break;
				}
			}
		}
		return res;
	}

	/**
	 * @return Een lijst met alle SE-, CE-, en eindresultaten voor de cijferlijst waarvoor
	 *         geldt dat het onderwijsproduct op basis van het eindresultaat is behaald
	 */
	public List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> getSE_CE_EindresultatenCijferlijstBehaald()
	{
		List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> resultaten =
			getSE_CE_EindresultatenCijferlijst(false);
		List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> filteredResultaten =
			new ArrayList<OnderwijsproductAfnameContext_SE_CE_Eindresultaat>();
		for (OnderwijsproductAfnameContext_SE_CE_Eindresultaat resultaat : resultaten)
		{
			if (resultaat.getEindresultaat() != null && resultaat.getEindresultaat().isBehaald())
				filteredResultaten.add(resultaat);
		}
		return filteredResultaten;
	}

	/**
	 * @return Een lijst met alle SE-, CE-, en eindresultaten voor de cijferlijst waarvoor
	 *         geldt dat het onderwijsproduct op basis van het eindresultaat is behaald
	 *         per deel(zoals gemeenschappelijk profiel) en zonder de combinatievakken
	 */
	public List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> getSE_CE_EindresultatenCijferlijstPerDeel(
			String soortProductregelDiplomanaam)
	{
		List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> resultaten =
			getSE_CE_EindresultatenCijferlijst(true);
		List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> filteredResultaten =
			new ArrayList<OnderwijsproductAfnameContext_SE_CE_Eindresultaat>();
		for (OnderwijsproductAfnameContext_SE_CE_Eindresultaat resultaat : resultaten)
		{
			boolean isDiplomanaamGelijk =
				resultaat.getKeuze().getProductregel().getSoortProductregel().getDiplomanaam()
					.equalsIgnoreCase(soortProductregelDiplomanaam);
			boolean isCombinatieVak = resultaat.getKeuze().isOnderdeelVanCombinatiecijfer();
			if (isDiplomanaamGelijk && !isCombinatieVak)
			{
				filteredResultaten.add(resultaat);
			}
		}
		return filteredResultaten;
	}

	/**
	 * 
	 * @return Een lijst met alle SE-, CE-, en eindresultaten voor de cijferlijst.
	 */
	public List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> getSE_CE_EindresultatenCijferlijst(
			boolean metSW)
	{
		List<OnderwijsproductAfname> afnames = getOnderwijsproductAfnamesVoorCijferlijst();
		Map<Onderwijsproduct, List<Resultaat>> alleCeResultaten =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class)
				.getDefinitieveCentraalExamenResultaten(verbintenis.getDeelnemer(), afnames);
		Map<Onderwijsproduct, Resultaat> alleSeResultaten =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class)
				.getDefinitieveSchoolexamenResultaten(verbintenis.getDeelnemer(), afnames,
					TypeResultaat.InSamengesteld);
		Map<OnderwijsproductAfname, Resultaat> eindresultaten =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).getEindresultaten(
				verbintenis.getDeelnemer(), afnames);
		// Maak een lijst in de volgorde van de productregels.
		List<Productregel> productregels =
			verbintenis.getOpleiding().getLandelijkeEnLokaleProductregels(verbintenis.getCohort());
		List<OnderwijsproductAfnameContext> keuzes =
			getOnderwijsproductAfnameContextenVoorCijferlijst();
		List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> res =
			new ArrayList<OnderwijsproductAfnameContext_SE_CE_Eindresultaat>(afnames.size());
		for (Productregel productregel : productregels)
		{
			for (OnderwijsproductAfnameContext keuze : keuzes)
			{
				if (keuze.getProductregel().equals(productregel)
					&& keuze.isToonOpCijferlijst()
					&& (!keuze.getOnderwijsproductAfname().getOnderwijsproduct()
						.isHeeftWerkstuktitel() || metSW))
				{
					Resultaat eindresultaat = eindresultaten.get(keuze.getOnderwijsproductAfname());
					if (eindresultaat != null && eindresultaat.isTijdelijk())
						eindresultaat = null;

					Resultaat seResultaat =
						alleSeResultaten.get(keuze.getOnderwijsproductAfname()
							.getOnderwijsproduct());

					List<Resultaat> ceResultaten =
						alleCeResultaten.get(keuze.getOnderwijsproductAfname()
							.getOnderwijsproduct());
					Resultaat ceResultaat = null;
					if (ceResultaten != null)
					{
						boolean found = false;
						for (Resultaat ce : ceResultaten)
						{
							if (ce.isInSamengesteld())
							{
								ceResultaat = ce;
								found = true;
								break;
							}
						}
						if (!found)
						{
							for (Resultaat ce : ceResultaten)
							{
								if (ce.isGeldend())
								{
									ceResultaat = ce;
									break;
								}
							}
						}
					}
					res.add(new OnderwijsproductAfnameContext_SE_CE_Eindresultaat(keuze,
						seResultaat, ceResultaat, eindresultaat));

					break;
				}
			}
		}

		return res;
	}

	/**
	 * @return Een lijst met de onderwijsproducten die voorkomen in het combinatievak.
	 */
	public List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> getSE_CE_EindresultatenCombinatievak()
	{
		List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> alleVakken =
			getSE_CE_EindresultatenCijferlijst(true);
		List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> res =
			new ArrayList<OnderwijsproductAfnameContext_SE_CE_Eindresultaat>();
		List<OnderwijsproductAfnameContext> keuzes = new ArrayList<OnderwijsproductAfnameContext>();
		for (OnderwijsproductAfnameContext_SE_CE_Eindresultaat vak : alleVakken)
		{
			keuzes.add(vak.getKeuze());
		}
		for (OnderwijsproductAfnameContext_SE_CE_Eindresultaat vak : alleVakken)
		{
			if (vak.getKeuze().isOnderdeelVanCombinatiecijfer(keuzes))
			{
				res.add(vak);
			}
		}

		return res;
	}

	/**
	 * @return de titel van het werkstuk dat aan een onderwijsproductafnamecontext van het
	 *         verbintenis gekoppeld is. De methode gaat ervan uit dat het verbintenis
	 *         maar 1 onderwijsproductafnamecontext heeft met een werktstuktitel.
	 */
	public String getWerkstukTitel()
	{
		for (OnderwijsproductAfnameContext context : verbintenis.getAfnameContexten())
		{
			if (context.getOnderwijsproductAfname().getOnderwijsproduct().isHeeftWerkstuktitel())
			{
				return context.getOnderwijsproductAfname().getWerkstuktitel();
			}
		}
		return null;
	}

	public List<OnderwijsproductAfnameContext> getOnderwijsproductAfnameContextenMetBetrekkingOpWerkstuk()
	{
		List<OnderwijsproductAfnameContext> res = new ArrayList<OnderwijsproductAfnameContext>();
		for (OnderwijsproductAfnameContext context : verbintenis.getAfnameContexten())
		{
			if (context.isWerkstukHoortBijProduct())
			{
				res.add(context);
			}
		}
		return res;
	}

	/**
	 * @return een comma-separated string met de landelijke namen van de
	 *         onderwijsproducten die betrekking hebben op het werkstuk binnen deze
	 *         verbintenis.
	 */
	public String getLandelijkeNamenOnderwijsproductenMetBetrekkingOpWerkstuk()
	{
		List<OnderwijsproductAfnameContext> list =
			getOnderwijsproductAfnameContextenMetBetrekkingOpWerkstuk();
		StringBuilder res = new StringBuilder();
		boolean first = true;
		for (OnderwijsproductAfnameContext context : list)
		{
			if (!first)
			{
				res.append(", ");
			}
			first = false;
			res.append(context.getOnderwijsproductAfname().getOnderwijsproduct()
				.getLandelijkeNaam());
		}
		return res.toString();
	}

	public Resultaat getBeoordelingWerkstuk()
	{
		for (OnderwijsproductAfnameContext context : verbintenis.getAfnameContexten())
		{
			if (context.getOnderwijsproductAfname().getOnderwijsproduct().isHeeftWerkstuktitel())
			{
				List<OnderwijsproductAfname> afnames = new ArrayList<OnderwijsproductAfname>(1);
				afnames.add(context.getOnderwijsproductAfname());
				Map<OnderwijsproductAfname, Resultaat> map =
					DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class)
						.getEindresultaten(verbintenis.getDeelnemer(), afnames);
				return map.get(context.getOnderwijsproductAfname());
			}
		}
		return null;
	}

	/**
	 * @return een comma-separated string met de landelijke namen van de
	 *         onderwijsproducten die betrekking hebben op het combinatievak binnen deze
	 *         verbintenis.
	 */
	public String getLandelijkeNamenOnderwijsproductenMetBetrekkingOpCombinatievak()
	{
		List<OnderwijsproductAfnameContext> list =
			getOnderwijsproductAfnameContextenMetBetrekkingOpWerkstuk();
		StringBuilder res = new StringBuilder();
		boolean first = true;
		for (OnderwijsproductAfnameContext context : list)
		{
			if (!first)
			{
				res.append(", ");
			}
			first = false;
			res.append(context.getOnderwijsproductAfname().getOnderwijsproduct()
				.getLandelijkeNaam());
		}
		return res.toString();
	}

	public Resultaat getNiveauNT2Vaardigheid(SoortToets vaardigheid, SoortToets niveau)
	{
		if (!vaardigheid.isNT2VaardigheidToets())
			return null;
		if (!vaardigheid.isUniekBinnenResultaatstructuur())
			return null;
		for (OnderwijsproductAfnameContext context : verbintenis.getAfnameContexten())
		{
			Resultaatstructuur structuur =
				context.getOnderwijsproductAfname().getResultaatstructuur();
			if (structuur != null)
			{
				Toets toets = structuur.getToets(vaardigheid);
				if (toets != null)
				{
					Toets behaaldNiveau = toets.getChild(niveau);
					if (behaaldNiveau != null)
					{
						// Bepaal het resultaat van deze toets voor deze deelnemer.
						return DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class)
							.getGeldendeResultaat(behaaldNiveau, verbintenis.getDeelnemer());
					}
				}
			}
		}
		return null;
	}

	@Exportable
	public DeelnemerResultatenRapportageModel getCijfers()
	{
		return new DeelnemerResultatenRapportageModel(ModelFactory.getModel(verbintenis),
			new OrganisatieEenheidLocatieAuthorizationContext(new AlwaysGrantedSecurityCheck()));
	}

	@Exportable
	public VergaderlijstRapportageModel getCijfersVergaderlijst()
	{
		return new VergaderlijstRapportageModel(ModelFactory.getModel(verbintenis));
	}
}
