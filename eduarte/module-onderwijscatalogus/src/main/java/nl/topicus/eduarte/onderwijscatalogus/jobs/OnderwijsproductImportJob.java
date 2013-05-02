package nl.topicus.eduarte.onderwijscatalogus.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.LocatieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SoortOnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.onderwijsproduct.*;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.onderwijscatalogus.entities.OndProdImportJobRunDetail;
import nl.topicus.eduarte.onderwijscatalogus.entities.OnderwijsproductImportJobRun;

import org.quartz.JobExecutionContext;

@JobInfo(name = OnderwijsproductImportJob.JOB_NAME)
@JobRunClass(OnderwijsproductImportJobRun.class)
public class OnderwijsproductImportJob extends EduArteJob
{
	public static final String JOB_NAME = "Onderwijsproducten importeren";

	private OnderwijsproductImportFile file;

	private OnderwijsproductImportJobRun run;

	private List<OnderwijsproductImportRegel> geimporteerdeRegels =
		new ArrayList<OnderwijsproductImportRegel>();

	private int aantalLeerstijlen;

	private int aantalSoortPraktijklokaal;

	private int aantalTypeToets;

	private int aantalTypeLocatie;

	private int aantalGebruiksmiddelen;

	private int aantalVerbruiksmiddelen;

	private int aantalSoortOnderwijsproduct;

	@Override
	protected void executeJob(JobExecutionContext context) throws InterruptedException
	{
		extractData(context);

		run = new OnderwijsproductImportJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting("Importeren is gestart");
		run.save();
		aantalLeerstijlen = importCodeNaamEntiteit(Leerstijl.class, file.getLeerstijlen());
		aantalSoortPraktijklokaal =
			importCodeNaamEntiteit(SoortPraktijklokaal.class, file.getSoortPraktijklokalen());
		aantalTypeToets = importCodeNaamEntiteit(TypeToets.class, file.getTypeToetsen());
		aantalTypeLocatie = importCodeNaamEntiteit(TypeLocatie.class, file.getTypeLocaties());
		aantalGebruiksmiddelen =
			importCodeNaamEntiteit(Gebruiksmiddel.class, file.getGebruiksmiddelen());
		aantalVerbruiksmiddelen =
			importCodeNaamEntiteit(Verbruiksmiddel.class, file.getVerbruiksmiddelen());
		aantalSoortOnderwijsproduct =
			importSoortOnderwijsproducten(file.getSoortenOnderwijsproducten());
		importOnderwijsproducten(file.getOnderwijsproducten());
		importPaklijstEnVoorwaarden();

		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		if (file.getWorkbook() != null)
		{
			OndProdImportJobRunDetail detail = new OndProdImportJobRunDetail(run);
			detail.setUitkomst("Aantal leerstijlen geimporteerd: " + aantalLeerstijlen);
			detail.save();
			detail = new OndProdImportJobRunDetail(run);
			detail.setUitkomst("Aantal soorten praktijklokaal geimporteerd: "
				+ aantalSoortPraktijklokaal);
			detail.save();
			detail = new OndProdImportJobRunDetail(run);
			detail.setUitkomst("Aantal types toets geimporteerd: " + aantalTypeToets);
			detail.save();
			detail = new OndProdImportJobRunDetail(run);
			detail.setUitkomst("Aantal types locatie geimporteerd: " + aantalTypeLocatie);
			detail.save();
			detail = new OndProdImportJobRunDetail(run);
			detail.setUitkomst("Aantal gebruiksmiddelen geimporteerd: " + aantalGebruiksmiddelen);
			detail.save();
			detail = new OndProdImportJobRunDetail(run);
			detail.setUitkomst("Aantal verbruiksmiddelen geimporteerd: " + aantalVerbruiksmiddelen);
			detail.save();
			detail = new OndProdImportJobRunDetail(run);
			detail.setUitkomst("Aantal soorten onderwijsproduct geimporteerd: "
				+ aantalSoortOnderwijsproduct);
			detail.save();

			run.setSamenvatting("Er zijn " + geimporteerdeRegels.size()
				+ " onderwijsproducten succesvol geimporteerd uit bestand "
				+ file.getBestandsnaam());
		}
		else
		{
			run.setSamenvatting("Het bestand voldoet niet aan het verwachte formaat");
		}
		run.update();
		run.commit();
	}

	private void importPaklijstEnVoorwaarden()
	{
		for (OnderwijsproductImportRegel oir : geimporteerdeRegels)
		{
			OnderwijsproductDataAccessHelper helper =
				DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class);
			Onderwijsproduct ondProd = helper.get(oir.getCode());
			createOndProdOnderdelen(ondProd, oir.getPaklijst());
			createOndProdVoorwaarden(ondProd, oir.getVoorwaarden());
		}
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
	}

	private void createOndProdVoorwaarden(Onderwijsproduct ondProd, String voorwaarden)
	{
		List<String> codes = getCodes(voorwaarden);
		for (String code : codes)
		{
			Onderwijsproduct ondProdVoorwaarde =
				DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class).get(code);
			if (ondProdVoorwaarde != null)
			{
				OnderwijsproductVoorwaarde voorwaarde =
					new OnderwijsproductVoorwaarde(ondProdVoorwaarde, ondProd);
				voorwaarde.save();
			}
		}
	}

	private void createOndProdOnderdelen(Onderwijsproduct ondProd, String paklijst)
	{
		List<String> codes = getCodes(paklijst);
		for (String code : codes)
		{
			Onderwijsproduct ondProdOnderdeel =
				DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class).get(code);
			if (ondProdOnderdeel != null)
			{
				OnderwijsproductSamenstelling samenstelling =
					new OnderwijsproductSamenstelling(ondProd, ondProdOnderdeel);
				samenstelling.save();
			}
		}
	}

	private void importOnderwijsproducten(List<OnderwijsproductImportRegel> onderwijsproducten)
			throws InterruptedException
	{
		List<String> bestaandeCodes =
			DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class).getCodes();
		int count = 0;
		for (OnderwijsproductImportRegel oir : onderwijsproducten)
		{
			String error = "";
			if (bestaandeCodes.contains(oir.getCode()))
				error = "Code bestaat al";
			else if (oir.getCode().length() > 20)
				error = "Lengte van de code is te groot";
			else if (oir.getTitel().length() > 100)
				error = "Lengte van de titel is te groot";
			else if (oir.getOmschrijving().length() > 2000)
				error = "Lengte van de omschrijving is te groot";
			else if (getOnderwijsproductStatus(oir.getStatus()) == null)
				error = "Geen geldige status opgegeven";
			else if (getAggregatieniveau(oir.getAggregatieniveau()) == null)
				error = "Geen geldig aggregatieniveau opgegeven";
			else if (getSoortOnderwijsproduct(oir.getSoortOnderwijsproduct()) == null)
				error = "Soort onderwijsproduct kan niet worden gevonden";
			if (StringUtil.isNotEmpty(error))
			{
				OndProdImportJobRunDetail detail = new OndProdImportJobRunDetail(run);
				detail.setUitkomst("Aanmaken van onderwijsproduct met code: '" + oir.getCode()
					+ "' is mislukt. Reden: " + error);
				detail.save();
			}
			else
			{
				Onderwijsproduct ondProd = new Onderwijsproduct();
				ondProd.setCode(oir.getCode());
				ondProd.setTitel(oir.getTitel());
				ondProd.setOmschrijving(oir.getOmschrijving());
				ondProd.setSoortProduct(getSoortOnderwijsproduct(oir.getSoortOnderwijsproduct()));
				ondProd.setStatus(getOnderwijsproductStatus(oir.getStatus()));
				ondProd.setAggregatieniveau(getAggregatieniveau(oir.getAggregatieniveau()));
				ondProd.setZoektermenAlsString(oir.getZoektermen());
				ondProd.setKostprijs(oir.getKostprijs());
				if (StringUtil.isNotEmpty(oir.getKalender()) && oir.getKalender().length() <= 2000)
					ondProd.setKalender(oir.getKalender());
				if (StringUtil.isNotEmpty(oir.getUitvoeringsfrequentie())
					&& oir.getUitvoeringsfrequentie().length() <= 2000)
					ondProd.setUitvoeringsfrequentie(oir.getUitvoeringsfrequentie());
				ondProd.setOmvang(oir.getOmvang());
				ondProd.setBelasting(oir.getBelasting());
				ondProd.setStartonderwijsproduct(oir.getStartonderwijsproduct() == null ? false
					: oir.getStartonderwijsproduct());
				ondProd.setLeerstijl(getCodeNaamEntiteit(Leerstijl.class, oir.getLeerstijl()));
				ondProd.setToegankelijkheid(oir.getToegankelijkheid());
				ondProd.setMinimumAantalDeelnemers(oir.getMinimumAantalDeelnemers());
				ondProd.setMaximumAantalDeelnemers(oir.getMaximumAantalDeelnemers());
				if (StringUtil.isNotEmpty(oir.getJuridischEigenaar())
					&& oir.getJuridischEigenaar().length() <= 2000)
					ondProd.setJuridischEigenaar(oir.getJuridischEigenaar());
				if (StringUtil.isNotEmpty(oir.getGebruiksrecht())
					&& oir.getGebruiksrecht().length() <= 2000)
					ondProd.setGebruiksrecht(oir.getGebruiksrecht());
				ondProd.setSoortPraktijklokaal(getCodeNaamEntiteit(SoortPraktijklokaal.class, oir
					.getSoortPraktijklokaal()));
				ondProd.setTypeToets(getCodeNaamEntiteit(TypeToets.class, oir.getTypeToets()));
				if (StringUtil.isNotEmpty(oir.getPersoneelCompetenties())
					&& oir.getPersoneelCompetenties().length() <= 2000)
					ondProd.setPersoneelCompetenties(oir.getPersoneelCompetenties());
				if (StringUtil.isNotEmpty(oir.getPersoneelKennisGebiedNiveau())
					&& oir.getPersoneelKennisGebiedNiveau().length() <= 2000)
					ondProd.setPersoneelKennisgebiedEnNiveau(oir.getPersoneelKennisGebiedNiveau());
				if (StringUtil.isNotEmpty(oir.getPersoneelWettelijkeVereisten())
					&& oir.getPersoneelWettelijkeVereisten().length() <= 2000)
					ondProd.setPersoneelWettelijkeVereisten(oir.getPersoneelWettelijkeVereisten());
				if (StringUtil.isNotEmpty(oir.getPersoneelBevoegdheid())
					&& oir.getPersoneelBevoegdheid().length() <= 2000)
					ondProd.setPersoneelBevoegdheid(oir.getPersoneelBevoegdheid());
				ondProd
					.setTypeLocatie(getCodeNaamEntiteit(TypeLocatie.class, oir.getTypeLocatie()));
				ondProd.save();
				createOndProdTaxonomie(ondProd, oir.getTaxonomieCodes());
				createOndProdAanbod(ondProd, oir.getOrganisatieEenheden(), oir.getLocaties());
				createOndProdGebruiksmiddelen(ondProd, oir.getGebruiksmiddelen());
				createOndProdVerbruiksmiddelen(ondProd, oir.getVerbruiksmiddelen());
				ondProd.save();
				bestaandeCodes.add(oir.getCode());
				geimporteerdeRegels.add(oir);
				setProgress(count, onderwijsproducten.size());
				count = flushAndClearHibernateAndIncCount(count);
			}
		}
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
	}

	private void createOndProdAanbod(Onderwijsproduct ondProd, String organisatieEenheden,
			String locaties)
	{
		List<String> codeOrganisaties = getCodes(organisatieEenheden);
		List<String> codeLocaties = getCodes(locaties);
		int index = 0;
		for (String code : codeOrganisaties)
		{
			if (codeLocaties.isEmpty()
				|| (codeLocaties.size() > index && !StringUtil.isEmpty(codeLocaties.get(index))))
			{
				OrganisatieEenheidDataAccessHelper organisatieHelper =
					DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class);
				OrganisatieEenheid organisatieEenheid = organisatieHelper.get(code);
				if (codeLocaties.isEmpty())
				{
					if (organisatieEenheid != null)
					{
						OnderwijsproductAanbod aanbod =
							new OnderwijsproductAanbod(organisatieEenheid, ondProd);
						aanbod.save();
					}
				}
				else
				{
					LocatieDataAccessHelper locatieHelper =
						DataAccessRegistry.getHelper(LocatieDataAccessHelper.class);
					Locatie locatie = locatieHelper.get(codeLocaties.get(index));
					if (organisatieEenheid != null && locatie != null)
					{
						OnderwijsproductAanbod aanbod =
							new OnderwijsproductAanbod(organisatieEenheid, ondProd);
						aanbod.setLocatie(locatie);
						aanbod.save();
					}
				}
			}
			index++;
		}

	}

	private void createOndProdVerbruiksmiddelen(Onderwijsproduct ondProd, String verbruiksmiddelen)
	{
		List<String> codes = getCodes(verbruiksmiddelen);
		for (String code : codes)
		{
			Verbruiksmiddel verbruiksmiddel = getCodeNaamEntiteit(Verbruiksmiddel.class, code);
			if (verbruiksmiddel != null)
			{
				OnderwijsproductVerbruiksmiddel ondProdVerbr =
					new OnderwijsproductVerbruiksmiddel();
				ondProdVerbr.setOnderwijsproduct(ondProd);
				ondProdVerbr.setVerbruiksmiddel(verbruiksmiddel);
				ondProdVerbr.save();
			}
		}
	}

	private void createOndProdGebruiksmiddelen(Onderwijsproduct ondProd, String gebruiksmiddelen)
	{
		List<String> codes = getCodes(gebruiksmiddelen);
		for (String code : codes)
		{
			Gebruiksmiddel gebruiksmiddel = getCodeNaamEntiteit(Gebruiksmiddel.class, code);
			if (gebruiksmiddel != null)
			{
				OnderwijsproductGebruiksmiddel ondProdGebr = new OnderwijsproductGebruiksmiddel();
				ondProdGebr.setOnderwijsproduct(ondProd);
				ondProdGebr.setGebruiksmiddel(gebruiksmiddel);
				ondProdGebr.save();
			}
		}
	}

	private void createOndProdTaxonomie(Onderwijsproduct ondProd, String taxonomieCodes)
	{
		List<String> codes = getCodes(taxonomieCodes);
		TaxonomieElementDataAccessHelper helper =
			DataAccessRegistry.getHelper(TaxonomieElementDataAccessHelper.class);
		for (String code : codes)
		{
			TaxonomieElement element = helper.get(code);
			if (element != null)
			{
				OnderwijsproductTaxonomie ondProdTax = new OnderwijsproductTaxonomie();
				ondProdTax.setOnderwijsproduct(ondProd);
				ondProdTax.setTaxonomieElement(element);
				ondProdTax.save();
			}
		}

	}

	private List<String> getCodes(String codesString)
	{
		if (StringUtil.isEmpty(codesString))
			return new ArrayList<String>();
		List<String> codes = new ArrayList<String>();
		String[] splitted = codesString.split(",");
		for (String string : splitted)
		{
			if (StringUtil.isNotEmpty(string) && !codes.contains(string))
				codes.add(string.trim());
		}
		return codes;
	}

	private SoortOnderwijsproduct getSoortOnderwijsproduct(String code)
	{
		SoortOnderwijsproductDataAccessHelper helper =
			DataAccessRegistry.getHelper(SoortOnderwijsproductDataAccessHelper.class);
		return helper.get(code);
	}

	@SuppressWarnings("unchecked")
	private <T extends CodeNaamActiefInstellingEntiteit> T getCodeNaamEntiteit(Class<T> clazz,
			String code)
	{
		if (StringUtil.isEmpty(code))
			return null;
		CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<T, ? > helper =
			DataAccessRegistry
				.getHelper(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);
		T entiteit = helper.get(code, clazz);
		if (entiteit == null)
		{
			OndProdImportJobRunDetail detail = new OndProdImportJobRunDetail(run);
			detail.setUitkomst("Kan " + clazz.getSimpleName() + " met code: '" + code
				+ "' niet vinden.");
			detail.save();
		}
		return entiteit;

	}

	private Aggregatieniveau getAggregatieniveau(int aggregatieniveau)
	{
		return DataAccessRegistry.getHelper(
			CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).getAggregatieniveau(
			aggregatieniveau);
	}

	private OnderwijsproductStatus getOnderwijsproductStatus(int status)
	{
		switch (status)
		{
			case 1:
				return OnderwijsproductStatus.Aangevraagd;
			case 2:
				return OnderwijsproductStatus.InOntwikkeling;
			case 3:
				return OnderwijsproductStatus.Beschikbaar;
			case 4:
				return OnderwijsproductStatus.Vervallen;
			case 5:
				return OnderwijsproductStatus.NietBeschikbaar;
			default:
				return null;
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends CodeNaamActiefInstellingEntiteit> int importCodeNaamEntiteit(Class<T> clazz,
			Map<String, String> map)
	{
		int aantal = 0;
		List<T> bestaandeEntiteiten =
			DataAccessRegistry.getHelper(
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).list(clazz);
		List<String> bestaandeCodes = new ArrayList<String>();
		for (T entiteit : bestaandeEntiteiten)
		{
			bestaandeCodes.add(entiteit.getCode());
		}
		for (String key : map.keySet())
		{
			if (bestaandeCodes.contains(key))
			{
				OndProdImportJobRunDetail detail = new OndProdImportJobRunDetail(run);
				detail.setUitkomst(clazz.getSimpleName() + " met code " + key
					+ " kon niet geimporteerd worden, omdat deze code al bestaat");
				detail.save();
			}
			else if (key.length() > 10)
			{
				OndProdImportJobRunDetail detail = new OndProdImportJobRunDetail(run);
				detail
					.setUitkomst(clazz.getSimpleName()
						+ " met code "
						+ key
						+ " kon niet geimporteerd worden, omdat de lengte van de code groter is dan 10 karakters");
				detail.save();

			}
			else if (map.get(key).length() > 30)
			{
				OndProdImportJobRunDetail detail = new OndProdImportJobRunDetail(run);
				detail
					.setUitkomst(clazz.getSimpleName()
						+ " met code "
						+ key
						+ " kon niet geimporteerd worden, omdat de lengte van de naam groter is dan 30 karakters");
				detail.save();
			}
			else
			{
				T entiteit = ReflectionUtil.invokeConstructor(clazz);
				entiteit.setCode(key);
				entiteit.setNaam(map.get(key));
				entiteit.setActief(true);
				entiteit.save();
				aantal++;
			}

		}
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).flush();
		return aantal;
	}

	@SuppressWarnings("unchecked")
	private int importSoortOnderwijsproducten(
			List<SoortOnderwijsproductImportRegel> soortenOnderwijsproducten)
	{
		int aantal = 0;
		List<SoortOnderwijsproduct> bestaandeSoorten =
			DataAccessRegistry.getHelper(
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).list(
				SoortOnderwijsproduct.class);
		List<String> bestaandeCodes = new ArrayList<String>();
		for (SoortOnderwijsproduct soortOnderwijsproduct : bestaandeSoorten)
		{
			bestaandeCodes.add(soortOnderwijsproduct.getCode());
		}
		for (SoortOnderwijsproductImportRegel soir : soortenOnderwijsproducten)
		{
			if (bestaandeCodes.contains(soir.getCode()))
			{
				OndProdImportJobRunDetail detail = new OndProdImportJobRunDetail(run);
				detail.setUitkomst("Soort onderwijsproduct met code " + soir.getCode()
					+ " kon niet geimporteerd worden, omdat deze code al bestaat");
				detail.save();
			}
			else if (soir.getCode().length() > 10)
			{
				OndProdImportJobRunDetail detail = new OndProdImportJobRunDetail(run);
				detail
					.setUitkomst("Soort onderwijsproduct met code "
						+ soir.getCode()
						+ " kon niet geimporteerd worden, omdat de lengte van de code groter is dan 10 karakters");
				detail.save();
			}
			else if (soir.getNaam().length() > 30)
			{
				OndProdImportJobRunDetail detail = new OndProdImportJobRunDetail(run);
				detail
					.setUitkomst("Soort onderwijsproduct met code "
						+ soir.getCode()
						+ " kon niet geimporteerd worden, omdat de lengte van de naam groter is dan 30 karakters");
				detail.save();
			}
			else
			{
				SoortOnderwijsproduct soortOnderwijsproduct = new SoortOnderwijsproduct();
				soortOnderwijsproduct.setCode(soir.getCode());
				bestaandeCodes.add(soir.getCode());
				soortOnderwijsproduct.setNaam(soir.getNaam());
				soortOnderwijsproduct.setSummatief(soir.isSummatief());
				soortOnderwijsproduct.setActief(true);
				soortOnderwijsproduct.save();
				aantal++;
			}

		}
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).flush();
		return aantal;
	}

	private void extractData(JobExecutionContext context)
	{
		file = getValue(context, "bestand");
	}
}
