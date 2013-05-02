package nl.topicus.eduarte.resultaten.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.TransientIdObject;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.quartz.JobSegment;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurCategorieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.resultaatstructuur.*;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.jobs.OverschrijfActie;
import nl.topicus.eduarte.resultaten.entities.ResultaatstructurenImporterenJobRun;
import nl.topicus.eduarte.resultaten.entities.ResultaatstructurenImporterenJobRunDetail;
import nl.topicus.eduarte.resultaten.web.components.security.ResultaatstructuurSecurityCheck;
import nl.topicus.eduarte.xml.JAXBContextFactory;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.ResultaatstructuurExport;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XResultaatstructuur;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XResultaatstructuurCategorie;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XResultaatstructuurDeelnemer;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XScoreschaalwaarde;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XToets;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XToetsverwijzing;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;
import org.quartz.JobExecutionContext;

@JobInfo(name = ResultaatstructurenImporterenJob.JOB_NAME)
@JobRunClass(ResultaatstructurenImporterenJobRun.class)
public class ResultaatstructurenImporterenJob extends EduArteJob
{
	public static final String JOB_GROUP = "KRD";

	public static final String JOB_NAME = "Resultaatstructuren importeren";

	private class ImporteerActie
	{
		private XResultaatstructuur structuur;

		private String structuurImporterenError;

		private String verwijzingenImporterenError;

		public ImporteerActie(XResultaatstructuur structuur)
		{
			this.structuur = structuur;
		}

		public XResultaatstructuur getStructuur()
		{
			return structuur;
		}

		public Onderwijsproduct getProduct()
		{
			return structuur.getOnderwijsproduct();
		}

		public Cohort getCohort()
		{
			return structuur.getCohort();
		}

		public String getCode()
		{
			return structuur.getCode();
		}

		public Type getType()
		{
			return structuur.getType();
		}

		public boolean isStructuurImporterenMogelijk()
		{
			return structuurImporterenError == null;
		}

		public String getStructuurImporterenError()
		{
			return structuurImporterenError;
		}

		public void setStructuurImporterenError(String structuurImporterenError)
		{
			this.structuurImporterenError = structuurImporterenError;
			setVerwijzingenImporterenError("Toetsverwijzingen niet geïmporteerd door een fout bij de structuur: "
				+ structuurImporterenError);
		}

		public boolean isVerwijzingenImporterenMogelijk()
		{
			return verwijzingenImporterenError == null;
		}

		public String getVerwijzingenImporterenError()
		{
			return verwijzingenImporterenError;
		}

		public void setVerwijzingenImporterenError(String verwijzingenImporterenError)
		{
			this.verwijzingenImporterenError = verwijzingenImporterenError;
		}

		@Override
		public String toString()
		{
			return structuur.getCode() + "/" + structuur.getOnderwijsproduct().getCode() + "("
				+ structuur.getCohort() + ")";
		}
	}

	private enum ImporteerOnderdeel implements JobSegment
	{
		COLLECT(20),
		IMPORT_STRUCTUUR(50),
		IMPORT_VERWIJZINGEN(30);

		private int pct;

		ImporteerOnderdeel(int pct)
		{
			this.pct = pct;
		}

		@Override
		public int getPercent()
		{
			return pct;
		}
	}

	private ResultaatstructurenImporterenJobRun jobToRollBack;

	private int importStructuurCount = 0;

	private int skipStructuurCount = 0;

	private int overwriteStructuurCount = 0;

	private int importVerwijzingCount = 0;

	private int skipVerwijzingCount = 0;

	private int overwriteVerwijzingCount = 0;

	private ResultaatstructuurImporteerSettings settings;

	@Override
	protected void executeJob(JobExecutionContext context) throws InterruptedException
	{
		extractData(context);
		if (jobToRollBack != null)
		{
			jobRunRollBack();
		}
		else
		{
			ResultaatstructurenImporterenJobRun run = new ResultaatstructurenImporterenJobRun();
			run.setGestartDoor(getMedewerker());
			run.setRunStart(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting("Resultaatstructuren importeren");
			run.save();

			List<ImporteerActie> acties = collectActies(run);
			Map<XToets, Long> toetsen = new HashMap<XToets, Long>();
			if (settings.isStructurenImporteren())
				importeerStructuren(run, acties, toetsen);
			flushHibernate();
			if (settings.isVerwijzingenImporteren())
				importeerVerwijzingen(run, acties, toetsen);

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting(run.getSamenvatting() + ": Structuren: " + importStructuurCount
				+ " geïmporteerd, " + overwriteStructuurCount + " overschreven, "
				+ skipStructuurCount + " overgeslagen. Verwijzingen: " + importVerwijzingCount
				+ " geïmporteerd, " + overwriteVerwijzingCount + " overschreven, "
				+ skipVerwijzingCount + " overgeslagen.");
			run.update();
			run.commit();
		}
	}

	private List<ImporteerActie> collectActies(final ResultaatstructurenImporterenJobRun run)
			throws InterruptedException
	{
		List<ImporteerActie> ret = new ArrayList<ImporteerActie>();
		try
		{
			Unmarshaller unmarshaller = JAXBContextFactory.createUnmarshaller();
			unmarshaller.setEventHandler(new ValidationEventHandler()
			{
				@Override
				public boolean handleEvent(ValidationEvent event)
				{
					run.error(format(event.getLocator()) + event.getMessage());
					return event.getSeverity() != ValidationEvent.FATAL_ERROR;
				}

				private String format(ValidationEventLocator locator)
				{
					if (locator == null)
						return "";
					return "Regel " + locator.getLineNumber() + " (kol "
						+ locator.getColumnNumber() + ") ";
				}
			});
			ResultaatstructuurExport export =
				(ResultaatstructuurExport) unmarshaller.unmarshal(settings.getBestand());
			int count = 0;
			for (XResultaatstructuur curStructuur : export.getResultaatstructuren())
			{
				setStatus("Verzamelen van informatie over " + curStructuur.getCode());
				if (matchesSettings(curStructuur))
				{
					if (isValid(curStructuur))
						ret.add(prepareForCopy(run, curStructuur));
					else
						run.error(curStructuur.getType() + "/" + curStructuur.getCode() + " ("
							+ curStructuur.getCohort() + " " + curStructuur.getOnderwijsproduct()
							+ ") overgeslagen vanwege fouten in het importbestand");
				}
				setProgress(count, export.getResultaatstructuren().size(),
					ImporteerOnderdeel.COLLECT);
				count = flushAndClearHibernateAndIncCount(count);
			}
		}
		catch (JAXBException e)
		{
			String msg = e.getMessage();
			if (msg == null && e.getCause() != null)
				msg = e.getCause().getMessage();
			run.error(msg);
		}
		return ret;
	}

	private boolean isValid(XResultaatstructuur structuur)
	{
		if (structuur.getOnderwijsproduct() == null)
			return false;
		if (structuur.getType().equals(Type.FORMATIEF) && structuur.getAuteur() == null)
			return false;
		if (structuur.getCohort() == null)
			return false;
		for (XResultaatstructuurDeelnemer curDeelnemer : structuur.getDeelnemers())
			if (!isValid(curDeelnemer))
				return false;
		for (Medewerker curMedewerker : structuur.getMedewerkers())
			if (curMedewerker == null)
				return false;
		return isValid(structuur.getEindresultaat());
	}

	private boolean isValid(XResultaatstructuurDeelnemer deelnemer)
	{
		return deelnemer.getGroep() != null || deelnemer.getDeelnemer() != null;
	}

	private boolean isValid(XToets toets)
	{
		if (toets.getSchaal() == null)
			return false;
		for (XScoreschaalwaarde curScoreschaalwaarde : toets.getScoreschaalwaarden())
			if (!isValid(curScoreschaalwaarde))
				return false;
		for (XToets curDeeltoets : toets.getDeeltoetsen())
			if (!isValid(curDeeltoets))
				return false;
		return true;
	}

	private boolean isValid(XScoreschaalwaarde scoreschaalwaarde)
	{
		return scoreschaalwaarde.getWaarde() != null;
	}

	private boolean matchesSettings(XResultaatstructuur structuur)
	{
		if (settings.getType() != null && structuur.getType() != settings.getType())
			return false;
		if (!StringUtil.isEmpty(settings.getCode())
			&& !structuur.getCode().equals(settings.getCode()))
			return false;
		if (settings.getOnderwijsproduct() != null
			&& !structuur.getOnderwijsproduct().equals(settings.getOnderwijsproduct()))
			return false;
		return true;
	}

	private void importeerStructuren(ResultaatstructurenImporterenJobRun run,
			List<ImporteerActie> acties, Map<XToets, Long> toetsen) throws InterruptedException
	{
		int count = 0;
		for (ImporteerActie curActie : acties)
		{
			setStatus("Importeren van structuur van " + curActie);
			if (curActie.isStructuurImporterenMogelijk())
				importeerResultaatstructuur(run, curActie, toetsen);
			else
			{
				run.error(curActie.getStructuurImporterenError());
				skipStructuurCount++;
			}
			setProgress(count, acties.size(), ImporteerOnderdeel.IMPORT_STRUCTUUR);
			count = flushAndClearHibernateAndIncCount(count);
		}
	}

	private void importeerVerwijzingen(ResultaatstructurenImporterenJobRun run,
			List<ImporteerActie> acties, Map<XToets, Long> toetsen) throws InterruptedException
	{
		int count = 0;
		for (ImporteerActie curActie : acties)
		{
			setStatus("Importeren van verwijzingen van " + curActie);
			if (curActie.isVerwijzingenImporterenMogelijk())
				importeerVerwijzingen(run, curActie, toetsen);
			else
			{
				run.error(curActie.getVerwijzingenImporterenError());
				skipVerwijzingCount++;
			}
			setProgress(count, acties.size(), ImporteerOnderdeel.IMPORT_VERWIJZINGEN);
			count = flushAndClearHibernateAndIncCount(count);
		}
	}

	private ImporteerActie prepareForCopy(ResultaatstructurenImporterenJobRun run,
			XResultaatstructuur structuur)
	{
		ImporteerActie ret = new ImporteerActie(structuur);
		if (ret.getProduct().getEinddatum() != null
			&& !ret.getProduct().getEinddatum().after(ret.getCohort().getBegindatum()))
		{
			ret.setStructuurImporterenError(ret.getProduct().getCode() + " ("
				+ ret.getProduct().getOmschrijving() + ") is niet geldig in cohort "
				+ ret.getCohort().getNaam());
			return ret;
		}

		Resultaatstructuur doelStructuur =
			findResultaatstructuur(ret.getProduct(), ret.getCohort(), ret.getType(), ret.getCode());
		if (doelStructuur == null)
		{
			if (!settings.isStructurenImporteren())
				ret.setVerwijzingenImporterenError("Er bestaat geen resultaatstructuur voor "
					+ ret.getProduct().getCode() + " (" + ret.getProduct().getOmschrijving() + ")");
			return ret;
		}

		if (settings.isStructurenImporteren()
			&& settings.getActieBijBestaandeStructuur().equals(OverschrijfActie.Overslaan))
		{
			ret.setStructuurImporterenError("Resultaatstructuur voor " + ret.getProduct().getCode()
				+ " (" + ret.getProduct().getOmschrijving()
				+ ") niet geïmporteerd vanwege een bestaande resultaatstructuur.");
			return ret;
		}

		if (!settings.isStructurenImporteren() && settings.isVerwijzingenImporteren()
			&& settings.getActieBijBestaandeVerwijzingen().equals(OverschrijfActie.Overslaan)
			&& heeftVerwijzingen(doelStructuur))
		{
			ret.setVerwijzingenImporterenError("Toetsverwijzingen voor de structuur voor "
				+ ret.getProduct().getCode() + " (" + ret.getProduct().getOmschrijving()
				+ ") niet geïmporteerd vanwege bestaande toetsverwijzingen.");
		}

		if (DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class).heeftResultaten(
			doelStructuur))
		{
			ret.setStructuurImporterenError("Resultaatstructuur voor " + ret.getProduct().getCode()
				+ " (" + ret.getProduct().getOmschrijving()
				+ ") niet verwijderd vanwege bestaande resultaten.");
			return ret;
		}

		if (settings.isStructurenImporteren())
		{
			setStatus("Verwijderen van bestaande structuur van "
				+ ret.getProduct().getOmschrijving());
			ModelFactory.getCompoundChangeRecordingModel(doelStructuur,
				new DefaultModelManager(getResultaatstructuurClasses())).deleteObject(
				new VerwijderToetsVerwijzingCallback());

			run.info("Bestaande resultaatstructuur verwijderd voor " + ret.getProduct().getCode()
				+ " (" + ret.getProduct().getOmschrijving() + ")");
			overwriteStructuurCount++;
		}
		else if (settings.isVerwijzingenImporteren() && ret.isVerwijzingenImporterenMogelijk()
			&& heeftVerwijzingen(doelStructuur))
		{
			for (Toets curToets : doelStructuur.getToetsen())
			{
				for (ToetsVerwijzing curVerwijzing : curToets.getUitgaandeVerwijzingen())
					curVerwijzing.delete();
				curToets.getUitgaandeVerwijzingen().clear();
			}
			run.info("Bestaande toetsverwijzingen verwijderd voor de structuur voor "
				+ ret.getProduct().getCode() + " (" + ret.getProduct().getOmschrijving() + ")");
			overwriteVerwijzingCount++;
		}
		flushHibernate();
		return ret;
	}

	private boolean heeftVerwijzingen(Resultaatstructuur structuur)
	{
		for (Toets curToets : structuur.getToetsen())
		{
			if (!curToets.getUitgaandeVerwijzingen().isEmpty())
				return true;
		}
		return false;
	}

	private Resultaatstructuur findResultaatstructuur(Onderwijsproduct product, Cohort cohort,
			Type type, String code)
	{
		return DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class)
			.getResultaatstructuur(product, cohort, type, code);
	}

	private void importeerResultaatstructuur(ResultaatstructurenImporterenJobRun run,
			ImporteerActie actie, Map<XToets, Long> toetsen)
	{
		Map<XToets, Toets> tmpToetsen = new HashMap<XToets, Toets>();
		Resultaatstructuur structuur = createStructuur(actie.getStructuur(), tmpToetsen);
		if (isAuthorized(structuur))
		{
			ModelFactory.getCompoundChangeRecordingModel(structuur,
				new DefaultModelManager(getResultaatstructuurClasses())).saveObject();
			recordToetsen(toetsen, tmpToetsen);

			ResultaatstructurenImporterenJobRunDetail detail =
				new ResultaatstructurenImporterenJobRunDetail(run);
			detail.setUitkomst("Resultaatstructuur " + structuur.getOnderwijsproduct().getCode()
				+ "/" + structuur.getCode() + "(" + structuur.getType() + ") geïmporteerd");
			detail.setResultaatstructuurId(structuur.getId());
			importStructuurCount++;
			detail.save();
		}
		else
		{
			actie.setStructuurImporterenError("Onvoldoende rechten om " + actie + " te importeren");
			run.error(actie.getStructuurImporterenError());
			skipStructuurCount++;
		}
	}

	private void recordToetsen(Map<XToets, Long> toetsen, Map<XToets, Toets> tmpToetsen)
	{
		for (Map.Entry<XToets, Toets> curEntry : tmpToetsen.entrySet())
			toetsen.put(curEntry.getKey(), curEntry.getValue().getId());
	}

	private Resultaatstructuur createStructuur(XResultaatstructuur xmlStructuur,
			Map<XToets, Toets> toetsen)
	{
		Resultaatstructuur ret = new Resultaatstructuur();
		ret.setActief(xmlStructuur.isActief());
		ret.setAuteur(xmlStructuur.getAuteur());
		ret.setCategorie(getOrCreateCategorie(xmlStructuur.getCategorie()));
		ret.setCode(xmlStructuur.getCode());
		ret.setCohort(xmlStructuur.getCohort());
		ret.setEindresultaat(createToets(ret, xmlStructuur.getEindresultaat(), toetsen));
		ret.setNaam(xmlStructuur.getNaam());
		ret.setOnderwijsproduct(xmlStructuur.getOnderwijsproduct());
		ret.setSpecifiek(xmlStructuur.isSpecifiek());
		ret.setStatus(xmlStructuur.getStatus());
		ret.setType(xmlStructuur.getType());

		for (XResultaatstructuurDeelnemer curDeelnemer : xmlStructuur.getDeelnemers())
		{
			ResultaatstructuurDeelnemer deelnemer = new ResultaatstructuurDeelnemer();
			deelnemer.setDeelnemer(curDeelnemer.getDeelnemer());
			deelnemer.setGroep(curDeelnemer.getGroep());
			deelnemer.setResultaatstructuur(ret);
			ret.getDeelnemers().add(deelnemer);
		}
		for (Medewerker curMedewerker : xmlStructuur.getMedewerkers())
		{
			ResultaatstructuurMedewerker medewerker = new ResultaatstructuurMedewerker();
			medewerker.setMedewerker(curMedewerker);
			medewerker.setResultaatstructuur(ret);
			ret.getMedewerkers().add(medewerker);
		}
		return ret;
	}

	private Toets createToets(Resultaatstructuur structuur, XToets toets, Map<XToets, Toets> toetsen)
	{
		Toets ret = new Toets();
		ret.setResultaatstructuur(structuur);
		ret.setAantalHerkansingen(toets.getAantalHerkansingen());
		ret.setAlternatiefCombinerenMetHoofd(toets.isAlternatiefCombinerenMetHoofd());
		ret.setAlternatiefResultaatMogelijk(toets.isAlternatiefResultaatMogelijk());
		if (toets.isAutomatischeWeging() != null)
			ret.setAutomatischeWeging(toets.isAutomatischeWeging());
		ret.setCode(toets.getCode());
		ret.setCompenseerbaarVanaf(toets.getCompenseerbaarVanaf());
		ret.setFormule(toets.getFormule());
		if (toets.isHandmatigInleveren() != null)
			ret.setHandmatigInleveren(toets.isHandmatigInleveren());
		ret.setMaxAantalIngevuld(toets.getMaxAantalIngevuld());
		ret.setMaxAantalNietBehaald(toets.getMaxAantalNietBehaald());
		ret.setMinAantalIngevuld(toets.getMinAantalIngevuld());
		ret.setMinStudiepuntenVoorBehaald(toets.getMinStudiepuntenVoorBehaald());
		ret.setNaam(toets.getNaam());
		ret.setOverschrijfbaar(toets.isOverschrijfbaar());
		ret.setReferentieCode(toets.getReferentieCode());
		ret.setReferentieVersie(toets.getReferentieVersie());
		ret.setRekenregel(toets.getRekenregel());
		ret.setSamengesteld(toets.isSamengesteld());
		ret.setSamengesteldMetHerkansing(toets.isSamengesteldMetHerkansing());
		ret.setSamengesteldMetVarianten(toets.isSamengesteldMetVarianten());
		ret.setSchaal(toets.getSchaal());
		ret.setScoreBijHerkansing(toets.getScoreBijHerkansing());
		ret.setScoreschaal(toets.getScoreschaal());
		ret.setScoreschaalLengteTijdvak1(toets.getScoreschaalLengteTijdvak1());
		ret.setScoreschaalLengteTijdvak2(toets.getScoreschaalLengteTijdvak2());
		ret.setScoreschaalLengteTijdvak3(toets.getScoreschaalLengteTijdvak3());
		ret.setScoreschaalNormeringTijdvak1(toets.getScoreschaalNormeringTijdvak1());
		ret.setScoreschaalNormeringTijdvak2(toets.getScoreschaalNormeringTijdvak2());
		ret.setScoreschaalNormeringTijdvak3(toets.getScoreschaalNormeringTijdvak3());
		ret.setSoort(toets.getSoort());
		ret.setStudiepunten(toets.getStudiepunten());
		ret.setVariantVoorPoging(toets.getVariantVoorPoging());
		ret.setVerplicht(toets.isVerplicht());
		ret.setVerwijsbaar(toets.isVerwijsbaar());
		ret.setVolgnummer(toets.getVolgnummer());
		ret.setWeging(toets.getWeging());

		for (XScoreschaalwaarde curScoreschaalwaarde : toets.getScoreschaalwaarden())
		{
			Scoreschaalwaarde scoreschaalwaarde = createScoreschaalwaarde(curScoreschaalwaarde);
			scoreschaalwaarde.setToets(ret);
			ret.getScoreschaalwaarden().add(scoreschaalwaarde);
		}

		for (XToets curDeeltoets : toets.getDeeltoetsen())
		{
			Toets deeltoets = createToets(structuur, curDeeltoets, toetsen);
			deeltoets.setParent(ret);
			ret.getChildren().add(deeltoets);
		}

		toetsen.put(toets, ret);
		return ret;
	}

	private Scoreschaalwaarde createScoreschaalwaarde(XScoreschaalwaarde scoreschaalwaarde)
	{
		Scoreschaalwaarde ret = new Scoreschaalwaarde();
		ret.setAdvies(scoreschaalwaarde.getAdvies());
		ret.setTotScore(scoreschaalwaarde.getTotScore());
		ret.setVanafScore(scoreschaalwaarde.getVanafScore());
		ret.setWaarde(scoreschaalwaarde.getWaarde());
		return ret;
	}

	private ResultaatstructuurCategorie getOrCreateCategorie(XResultaatstructuurCategorie categorie)
	{
		if (categorie == null)
			return null;

		ResultaatstructuurCategorie ret =
			DataAccessRegistry.getHelper(ResultaatstructuurCategorieDataAccessHelper.class).get(
				categorie.getNaam());
		if (ret == null)
		{
			ret = new ResultaatstructuurCategorie();
			ret.setNaam(categorie.getNaam());
			ret.setActief(categorie.isActief());
		}
		return ret;
	}

	private void importeerVerwijzingen(ResultaatstructurenImporterenJobRun run,
			ImporteerActie actie, Map<XToets, Long> toetsen)
	{
		Resultaatstructuur structuur =
			findResultaatstructuur(actie.getProduct(), actie.getCohort(), actie.getType(), actie
				.getCode());
		if (!isAuthorized(structuur))
		{
			run.error("Onvoldoende rechten om verwijzingen van resultaatstructuur ("
				+ structuur.getType() + "/" + structuur.getCode() + ") te importeren");
			skipVerwijzingCount++;
			return;
		}

		for (XToets curXmlToets : getAllToetsen(actie.getStructuur().getEindresultaat(),
			new ArrayList<XToets>()))
		{
			for (XToetsverwijzing curVerwijzing : curXmlToets.getVerwijzingen())
			{
				ToetsDataAccessHelper toetsHelper =
					DataAccessRegistry.getHelper(ToetsDataAccessHelper.class);
				Toets toets = toetsHelper.get(Toets.class, toetsen.get(curXmlToets));
				ToetsVerwijzing nieuweVerwijzing = new ToetsVerwijzing();
				nieuweVerwijzing.setLezenUit(toets);
				if (curVerwijzing.getToetsref() != null)
					nieuweVerwijzing.setSchrijvenIn(curVerwijzing.getToetsref());
				else
				{
					Long id = toetsen.get(curVerwijzing.getToets());
					if (id != null)
						nieuweVerwijzing.setSchrijvenIn(toetsHelper.get(Toets.class, id));
				}

				if (nieuweVerwijzing.getSchrijvenIn() != null)
				{
					nieuweVerwijzing.save();
					ResultaatstructurenImporterenJobRunDetail detail =
						new ResultaatstructurenImporterenJobRunDetail(run);
					detail.setUitkomst("Toets verwijzing " + nieuweVerwijzing
						+ " geïmporteerd van " + actie);
					detail.setToetsVerwijzingId(nieuweVerwijzing.getId());
					importVerwijzingCount++;
					detail.save();
				}
				else
				{
					run.error("Het doel van de verwijzing voor " + toets
						+ " kon niet gevonden worden");
					skipVerwijzingCount++;
				}
			}
		}
	}

	private List<XToets> getAllToetsen(XToets basis, List<XToets> ret)
	{
		ret.add(basis);
		for (XToets curDeeltoets : basis.getDeeltoetsen())
			getAllToetsen(curDeeltoets, ret);
		return ret;
	}

	private boolean isAuthorized(final Resultaatstructuur structuur)
	{
		ResultaatstructuurSecurityCheck check =
			new ResultaatstructuurSecurityCheck(new DataSecurityCheck(structuur.getType()
				.getSecurityId()), structuur)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean isEntitySet()
				{
					return true;
				}

				@Override
				protected Resultaatstructuur getResultaatstructuur()
				{
					return structuur;
				}
			};
		check.setSecurityIdPostfix(Resultaatstructuur.IMPORTEREN);
		check.setEditTarget(true);
		boolean authorized = check.isActionAuthorized(Enable.class);
		return authorized;
	}

	@SuppressWarnings("unchecked")
	private Class< ? extends TransientIdObject>[] getResultaatstructuurClasses()
	{
		return new Class[] {DeelnemerResultaatVersie.class, Scoreschaalwaarde.class,
			ToetsVerwijzing.class, PersoonlijkeToetscode.class, Toets.class,
			ResultaatstructuurDeelnemer.class, ResultaatstructuurMedewerker.class,
			Resultaatstructuur.class};
	}

	@SuppressWarnings("unchecked")
	private void jobRunRollBack()
	{
		for (JobRunDetail curDetail : jobToRollBack.getDetails())
		{
			if (curDetail instanceof ResultaatstructurenImporterenJobRunDetail)
			{
				ResultaatstructurenImporterenJobRunDetail curCopyDetail =
					(ResultaatstructurenImporterenJobRunDetail) curDetail;
				if (curCopyDetail.getToetsVerwijzingId() != null)
				{
					BatchDataAccessHelper<ToetsVerwijzing> helper =
						DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
					ToetsVerwijzing deleteVerwijzing =
						helper.get(ToetsVerwijzing.class, curCopyDetail.getToetsVerwijzingId());
					if (deleteVerwijzing != null
						&& !DataAccessRegistry
							.getHelper(ResultaatstructuurDataAccessHelper.class)
							.heeftResultaten(deleteVerwijzing.getLezenUit().getResultaatstructuur()))
					{
						setStatus("Verwijderen van toetsverwijzing " + deleteVerwijzing);
						deleteVerwijzing.delete();
					}
				}
			}
		}
		for (JobRunDetail curDetail : jobToRollBack.getDetails())
		{
			if (curDetail instanceof ResultaatstructurenImporterenJobRunDetail)
			{
				ResultaatstructurenImporterenJobRunDetail curCopyDetail =
					(ResultaatstructurenImporterenJobRunDetail) curDetail;
				if (curCopyDetail.getResultaatstructuurId() != null)
				{
					Resultaatstructuur deleteStructuur =
						DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class).get(
							curCopyDetail.getResultaatstructuurId());
					if (deleteStructuur != null
						&& !DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class)
							.heeftResultaten(deleteStructuur))
					{
						Onderwijsproduct product = deleteStructuur.getOnderwijsproduct();
						setStatus("Verwijderen van bestaande structuur van "
							+ product.getOmschrijving());
						ModelFactory.getCompoundChangeRecordingModel(deleteStructuur,
							new DefaultModelManager(getResultaatstructuurClasses())).deleteObject();
					}
				}
			}
		}
		jobToRollBack.setTeruggedraaid(true);
		jobToRollBack.commit();
	}

	private void extractData(JobExecutionContext context)
	{
		if (hasValue(context, CobraScheduler.ROLL_BACK_KEY))
		{
			this.jobToRollBack = getValue(context, CobraScheduler.ROLL_BACK_KEY);
		}
		else
		{
			settings = getValue(context, "settings");
		}
	}
}
