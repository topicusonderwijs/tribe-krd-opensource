package nl.topicus.eduarte.onderwijscatalogus.jobs;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.ToegestaanOnderwijsproduct;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.onderwijscatalogus.entities.ToegestaanOndProdImportJobRun;
import nl.topicus.eduarte.util.OrganisatieEenheidLocatieUtil;

import org.quartz.JobExecutionContext;

@JobInfo(name = ToegestaanOndProdImportJob.JOB_NAME)
@JobRunClass(ToegestaanOndProdImportJobRun.class)
public class ToegestaanOndProdImportJob extends EduArteJob
{
	public static final String JOB_NAME = "Toegestane onderwijsproducten importeren";

	private ToegestaanOndProdImportFile file;

	private ToegestaanOndProdImportJobRun run;

	private List<ToegestaanOndProdImportRegel> geimporteerdeRegels =
		new ArrayList<ToegestaanOndProdImportRegel>();

	@Override
	protected void executeJob(JobExecutionContext context) throws InterruptedException
	{
		extractData(context);

		run = new ToegestaanOndProdImportJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting("Importeren is gestart");
		run.save();
		importToegestaneOnderwijsproducten(file.getToegestaneOnderwijsproducten());
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		if (file.getWorkbook() != null)
		{
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

	private void importToegestaneOnderwijsproducten(
			List<ToegestaanOndProdImportRegel> toegestaneOnderwijsproducten)
			throws InterruptedException
	{
		OnderwijsproductDataAccessHelper ondProdHelper =
			DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class);
		ProductregelDataAccessHelper prHelper =
			DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class);
		OpleidingDataAccessHelper opleidingHelper =
			DataAccessRegistry.getHelper(OpleidingDataAccessHelper.class);
		int count = 0;
		for (ToegestaanOndProdImportRegel oir : toegestaneOnderwijsproducten)
		{

			Cohort cohort = null;
			Productregel productregel = null;
			Onderwijsproduct onderwijsproduct = null;
			List<Opleiding> opleidingen = null;
			String error = "";

			if (geimporteerdeRegels.contains(oir))
				error = "Dubbele regel";
			else if (StringUtil.isEmpty(oir.getOpleidingscode()))
				error = "Opleidingscode is niet gevuld";
			else if (StringUtil.isEmpty(oir.getCohort()))
				error = "Cohort is niet gevuld";
			else if ((cohort = Cohort.asCohort(oir.getCohort())) == null)
				error = "Cohort voldoet niet aan formaat 2010-2011";
			else if (StringUtil.isEmpty(oir.getProductregelAfkorting()))
				error = "Productregel afkorting is niet gevuld";
			else if (StringUtil.isEmpty(oir.getOnderwijsproductCode()))
				error = "Onderwijsproductcode is niet gevuld";
			else if ((onderwijsproduct = ondProdHelper.get(oir.getOnderwijsproductCode())) == null)
				error =
					"Onderwijsproduct met code: " + oir.getOnderwijsproductCode()
						+ " is niet gevonden";
			else if ((opleidingen = opleidingHelper.getOpleidingen(oir.getOpleidingscode())).size() == 0)
				error = "Opleiding met code: " + oir.getOpleidingscode() + " is niet gevonden";
			else if ((opleidingen = opleidingHelper.getOpleidingen(oir.getOpleidingscode())).size() > 1)
				error = "Meerdere opleiding met code: " + oir.getOpleidingscode();
			else if ((productregel =
				prHelper
					.getProductRegel(oir.getProductregelAfkorting(), opleidingen.get(0), cohort)) == null)
				error =
					"Productregel met afkorting " + oir.getProductregelAfkorting()
						+ " is niet gevonden";
			else if (productregel.getToegestaneOnderwijsproductenAlsProducten().contains(
				onderwijsproduct))
				error =
					"Koppeling tussen " + oir.getProductregelAfkorting() + " en "
						+ oir.getOnderwijsproductCode() + " bestaat al";
			else
			{
				if (!OrganisatieEenheidLocatieUtil.gelijkeOrganisatieEenheidLocatie(
					onderwijsproduct, opleidingen.get(0))
					&& !OrganisatieEenheidLocatieUtil.gelijkeOrganisatieEenheidLocatie(opleidingen
						.get(0), onderwijsproduct))
					error =
						"Het onderwijsproductaanbod van  " + onderwijsproduct.getTitel()
							+ " komt niet overeen met het opleidingaanbod";
			}
			if (StringUtil.isNotEmpty(error))
			{
				JobRunDetail detail = new JobRunDetail(run);
				detail.setUitkomst("Aanmaken van toegestaanonderwijsproduct op regel: '"
					+ (count + 1) + "' is mislukt. Reden: " + error);
				detail.save();
				count++;
			}
			else if (productregel != null && onderwijsproduct != null)
			{
				ToegestaanOnderwijsproduct toegOndProd = new ToegestaanOnderwijsproduct();
				toegOndProd.setProductregel(productregel);
				toegOndProd.setOnderwijsproduct(onderwijsproduct);
				toegOndProd.save();
				productregel.getToegestaneOnderwijsproducten().add(toegOndProd);
				geimporteerdeRegels.add(oir);
				setProgress(count, toegestaneOnderwijsproducten.size());
				count = flushAndClearHibernateAndIncCount(count);
			}
		}
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
	}

	private void extractData(JobExecutionContext context)
	{
		file = getValue(context, "bestand");
	}
}
