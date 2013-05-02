package nl.topicus.eduarte.krd.jobs;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.quartz.ThreePartSegment;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.CriteriumDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.ToegestaanOnderwijsproduct;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.jobs.OverschrijfActie;
import nl.topicus.eduarte.krd.entities.OpleidingInrichtingImporterenJobRun;
import nl.topicus.eduarte.krd.entities.OpleidingInrichtingImporterenJobRunDetail;
import nl.topicus.eduarte.util.OrganisatieEenheidLocatieUtil;
import nl.topicus.eduarte.xml.JAXBContextFactory;
import nl.topicus.eduarte.xml.onderwijscatalogus.v10.OpleidingInrichtingExport;
import nl.topicus.eduarte.xml.onderwijscatalogus.v10.XCriterium;
import nl.topicus.eduarte.xml.onderwijscatalogus.v10.XOpleidingInrichting;
import nl.topicus.eduarte.xml.onderwijscatalogus.v10.XProductregel;

import org.quartz.JobExecutionContext;

@JobInfo(name = OpleidingInrichtingImporterenJob.JOB_NAME)
@JobRunClass(OpleidingInrichtingImporterenJobRun.class)
public class OpleidingInrichtingImporterenJob extends EduArteJob
{
	public static final String JOB_GROUP = "KRD";

	public static final String JOB_NAME = "Opleidingsinrichting importeren";

	private OpleidingInrichtingImporterenJobRun jobToRollBack;

	private int importCriteriumCount = 0;

	private int skipCriteriumCount = 0;

	private int overwriteCriteriumCount = 0;

	private int importProductregelCount = 0;

	private int skipProductregelCount = 0;

	private int overwriteProductregelCount = 0;

	private OpleidingInrichtingImporteerSettings settings;

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
			OpleidingInrichtingImporterenJobRun run = new OpleidingInrichtingImporterenJobRun();
			run.setGestartDoor(getMedewerker());
			run.setRunStart(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting("Opleidingsinrichting importeren");
			run.save();

			doImport(run);

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting(run.getSamenvatting() + ": Criteria: " + importCriteriumCount
				+ " geïmporteerd, " + overwriteCriteriumCount + " overschreven, "
				+ skipCriteriumCount + " overgeslagen. Productregels: " + importProductregelCount
				+ " geïmporteerd, " + overwriteProductregelCount + " overschreven, "
				+ skipProductregelCount + " overgeslagen.");
			run.update();
			run.commit();
		}
	}

	private void doImport(final OpleidingInrichtingImporterenJobRun run)
			throws InterruptedException
	{
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
			List<XCriterium> criteriaToImport = new ArrayList<XCriterium>();
			List<XProductregel> productregelsToImport = new ArrayList<XProductregel>();
			OpleidingInrichtingExport export =
				(OpleidingInrichtingExport) unmarshaller.unmarshal(settings.getBestand());
			int count = 0;
			for (XOpleidingInrichting curOpleidingInrichting : export.getOpleidingInrichtingen())
			{
				setStatus("Voorbereiden van import voor " + curOpleidingInrichting.getOpleiding());
				if (isValid(curOpleidingInrichting))
					prepareOpleidingInrichting(run, curOpleidingInrichting, criteriaToImport,
						productregelsToImport);
				else
					run.error(curOpleidingInrichting.getOpleiding()
						+ " overgeslagen vanwege fouten in het importbestand");
				setProgress(count, export.getOpleidingInrichtingen().size(),
					ThreePartSegment.FIRST_PART);
				count = flushAndClearHibernateAndIncCount(count);
			}
			flushAndClearHibernate();

			count = 0;
			for (XCriterium curCriterium : criteriaToImport)
			{
				setStatus("Importeren van criterium voor " + curCriterium.getOpleiding());
				importCriterium(run, curCriterium);
				setProgress(count, criteriaToImport.size(), ThreePartSegment.SECOND_PART);
				count = flushAndClearHibernateAndIncCount(count);
			}
			flushAndClearHibernate();

			count = 0;
			for (XProductregel curProductregel : productregelsToImport)
			{
				setStatus("Importeren van productregel voor " + curProductregel.getOpleiding());
				importProductregel(run, curProductregel);
				setProgress(count, criteriaToImport.size(), ThreePartSegment.THIRD_PART);
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
	}

	private boolean isValid(XOpleidingInrichting inrichting)
	{
		if (inrichting.getOpleiding() == null)
			return false;
		for (XCriterium curCriterium : inrichting.getCriteria())
			if (!isValid(curCriterium))
				return false;
		for (XProductregel curProductregel : inrichting.getProductregels())
			if (!isValid(curProductregel))
				return false;
		return true;
	}

	private boolean isValid(XCriterium criterium)
	{
		if (criterium.getCohort() == null)
			return false;
		if (criterium.getOpleiding() == null)
			return false;
		if (criterium.getVerbintenisgebied() == null)
			return false;
		return true;
	}

	private boolean isValid(XProductregel productregel)
	{
		if (productregel.getCohort() == null)
			return false;
		if (productregel.getOpleiding() == null)
			return false;
		if (productregel.getSoortProductregel() == null)
			return false;
		if (productregel.getTypeProductregel() == null)
			return false;
		if (productregel.getVerbintenisgebied() == null)
			return false;
		for (Onderwijsproduct curOnderwijsproduct : productregel.getToegestaneOnderwijsproducten())
			if (curOnderwijsproduct == null)
				return false;
		return true;
	}

	private boolean matchesSettings(XCriterium criterium)
	{
		if (settings.getCohort() != null && !criterium.getCohort().equals(settings.getCohort()))
			return false;
		if (settings.getOpleiding() != null
			&& !criterium.getOpleiding().equals(settings.getOpleiding()))
			return false;
		return true;
	}

	private boolean matchesSettings(XProductregel productregel)
	{
		if (settings.getCohort() != null && !productregel.getCohort().equals(settings.getCohort()))
			return false;
		if (settings.getOpleiding() != null
			&& !productregel.getOpleiding().equals(settings.getOpleiding()))
			return false;
		return true;
	}

	private void prepareOpleidingInrichting(OpleidingInrichtingImporterenJobRun run,
			XOpleidingInrichting opleidingInrichting, List<XCriterium> criteriaToImport,
			List<XProductregel> productregelsToImport)
	{
		if (settings.isCriteriaImporteren())
		{
			for (XCriterium curCriterium : opleidingInrichting.getCriteria())
				if (matchesSettings(curCriterium))
				{
					if (prepareCriterium(run, curCriterium))
						criteriaToImport.add(curCriterium);
				}
		}
		if (settings.isProductregelsImporteren())
		{
			for (XProductregel curProductregel : opleidingInrichting.getProductregels())
				if (matchesSettings(curProductregel))
				{
					if (prepareProductregel(run, curProductregel))
						productregelsToImport.add(curProductregel);
				}
		}
	}

	private boolean prepareCriterium(OpleidingInrichtingImporterenJobRun run, XCriterium criterium)
	{
		CriteriumDataAccessHelper helper =
			DataAccessRegistry.getHelper(CriteriumDataAccessHelper.class);
		Criterium bestaandCriterium =
			helper.getCriterium(criterium.getOpleiding(), criterium.getCohort(), criterium
				.getNaam());
		if (bestaandCriterium != null)
		{
			if (settings.getActieBijBestaandCriterium().equals(OverschrijfActie.Overschrijven))
			{
				run.info("Criterium verwijderd: " + bestaandCriterium);
				overwriteCriteriumCount++;
				bestaandCriterium.delete();
			}
			else
			{
				run.error("Er bestaat al een criterium voor " + bestaandCriterium);
				skipCriteriumCount++;
				return false;
			}
		}
		return true;
	}

	private void importCriterium(OpleidingInrichtingImporterenJobRun run, XCriterium criterium)
	{
		Criterium newCriterium = new Criterium();
		newCriterium.setCohort(criterium.getCohort());
		newCriterium.setFormule(criterium.getFormule());
		newCriterium.setMelding(criterium.getMelding());
		newCriterium.setNaam(criterium.getNaam());
		newCriterium.setOpleiding(criterium.getOpleiding());
		newCriterium.setVerbintenisgebied(criterium.getVerbintenisgebied());
		newCriterium.setVolgnummer(criterium.getVolgnummer());
		newCriterium.save();

		OpleidingInrichtingImporterenJobRunDetail detail =
			new OpleidingInrichtingImporterenJobRunDetail(run);
		detail.setUitkomst("Criterium " + newCriterium + " geïmporteerd");
		detail.setCriteriumId(newCriterium.getId());
		importCriteriumCount++;
		detail.save();
	}

	private boolean prepareProductregel(OpleidingInrichtingImporterenJobRun run,
			XProductregel productregel)
	{
		ProductregelDataAccessHelper helper =
			DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class);
		Productregel bestaandeProductregel =
			helper.getProductRegel(productregel.getAfkorting(), productregel.getOpleiding(),
				productregel.getCohort());
		if (bestaandeProductregel != null)
		{
			if (settings.getActieBijBestaandeProductregel().equals(OverschrijfActie.Overschrijven))
			{
				if (bestaandeProductregel.isInGebruik())
				{
					run.error("Kan de productregel " + bestaandeProductregel + " voor "
						+ productregel.getCohort() + "/" + productregel.getOpleiding().getCode()
						+ " niet verwijderen, deze is in gebruik.");
					skipProductregelCount++;
					return false;
				}
				run.info("Productregel verwijderd: " + bestaandeProductregel);
				overwriteProductregelCount++;
				for (ToegestaanOnderwijsproduct curOnderwijsproduct : bestaandeProductregel
					.getToegestaneOnderwijsproducten())
					curOnderwijsproduct.delete();
				bestaandeProductregel.delete();
			}
			else
			{
				run.error("Er bestaat al een productregel voor " + productregel.getCohort() + "/"
					+ productregel.getOpleiding().getCode() + ": " + bestaandeProductregel);
				skipProductregelCount++;
				return false;
			}
		}
		return true;
	}

	private void importProductregel(OpleidingInrichtingImporterenJobRun run,
			XProductregel productregel)
	{
		Productregel newProductregel = new Productregel();
		newProductregel.setAantalDecimalen(productregel.getAantalDecimalen());
		newProductregel.setAfkorting(productregel.getAfkorting());
		newProductregel.setCohort(productregel.getCohort());
		newProductregel.setMinimaleWaarde(productregel.getMinimaleWaarde());
		newProductregel.setMinimaleWaardeTekst(productregel.getMinimaleWaardeTekst());
		newProductregel.setNaam(productregel.getNaam());
		newProductregel.setOpleiding(productregel.getOpleiding());
		newProductregel.setSoortProductregel(productregel.getSoortProductregel());
		newProductregel.setTypeProductregel(productregel.getTypeProductregel());
		newProductregel.setVerbintenisgebied(productregel.getVerbintenisgebied());
		newProductregel.setVerplicht(productregel.isVerplicht());
		newProductregel.setVolgnummer(productregel.getVolgnummer());
		newProductregel.save();
		int failedProducten = 0;
		for (Onderwijsproduct curOnderwijsproduct : productregel.getToegestaneOnderwijsproducten())
		{
			if (OrganisatieEenheidLocatieUtil.gelijkeOrganisatieEenheidLocatie(curOnderwijsproduct,
				productregel.getOpleiding())
				|| OrganisatieEenheidLocatieUtil.gelijkeOrganisatieEenheidLocatie(productregel
					.getOpleiding(), curOnderwijsproduct))
			{
				ToegestaanOnderwijsproduct newOnderwijsproduct = new ToegestaanOnderwijsproduct();
				newOnderwijsproduct.setProductregel(newProductregel);
				newOnderwijsproduct.setOnderwijsproduct(curOnderwijsproduct);
				newProductregel.getToegestaneOnderwijsproducten().add(newOnderwijsproduct);
				newOnderwijsproduct.save();
			}
			else
			{
				failedProducten++;
			}
		}

		OpleidingInrichtingImporterenJobRunDetail detail =
			new OpleidingInrichtingImporterenJobRunDetail(run);
		detail
			.setUitkomst("Productregel "
				+ newProductregel
				+ " voor "
				+ productregel.getCohort()
				+ "/"
				+ productregel.getOpleiding().getCode()
				+ " geïmporteerd"
				+ (failedProducten == 0
					? ""
					: " "
						+ failedProducten
						+ " onderwijsproduct(en) overgeslagen ivm ongeldige combinatie van onderwijsproductaanbod en opleidingaanbod"));
		detail.setProductregelId(newProductregel.getId());
		importProductregelCount++;
		detail.save();
	}

	@SuppressWarnings("unchecked")
	private void jobRunRollBack()
	{
		for (JobRunDetail curDetail : jobToRollBack.getDetails())
		{
			if (curDetail instanceof OpleidingInrichtingImporterenJobRunDetail)
			{
				OpleidingInrichtingImporterenJobRunDetail curCopyDetail =
					(OpleidingInrichtingImporterenJobRunDetail) curDetail;
				if (curCopyDetail.getCriteriumId() != null)
				{
					BatchDataAccessHelper<Criterium> helper =
						DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
					Criterium deleteCriterium =
						helper.get(Criterium.class, curCopyDetail.getCriteriumId());
					if (deleteCriterium != null)
					{
						setStatus("Verwijderen van criterium " + deleteCriterium);
						deleteCriterium.delete();
					}
				}
			}
		}
		for (JobRunDetail curDetail : jobToRollBack.getDetails())
		{
			if (curDetail instanceof OpleidingInrichtingImporterenJobRunDetail)
			{
				OpleidingInrichtingImporterenJobRunDetail curCopyDetail =
					(OpleidingInrichtingImporterenJobRunDetail) curDetail;
				if (curCopyDetail.getProductregelId() != null)
				{
					Productregel deleteProductregel =
						DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class).get(
							Productregel.class, curCopyDetail.getProductregelId());
					if (deleteProductregel != null)
					{
						setStatus("Verwijderen van productregel " + deleteProductregel);
						for (ToegestaanOnderwijsproduct curOnderwijsproduct : deleteProductregel
							.getToegestaneOnderwijsproducten())
							curOnderwijsproduct.delete();
						deleteProductregel.delete();
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
