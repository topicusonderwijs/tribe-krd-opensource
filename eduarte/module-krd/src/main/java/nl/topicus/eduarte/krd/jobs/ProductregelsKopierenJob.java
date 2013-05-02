package nl.topicus.eduarte.krd.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.TransientIdObject;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.ToegestaanOnderwijsproduct;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.entities.ProductregelsKopierenJobRun;
import nl.topicus.eduarte.krd.entities.ProductregelsKopierenJobRunDetail;
import nl.topicus.eduarte.web.pages.shared.KopieerSettings;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;
import org.quartz.JobExecutionContext;

@JobInfo(name = ProductregelsKopierenJob.JOB_NAME)
@JobRunClass(ProductregelsKopierenJobRun.class)
public class ProductregelsKopierenJob extends EduArteJob
{
	public static final String JOB_NAME = "Productregels kopiëren";

	private ProductregelsKopierenJobRun jobToRollBack;

	private IModel<List<Productregel>> productregels;

	private Cohort broncohort;

	private Cohort doelcohort;

	private int copyCount = 0;

	private int skipCount = 0;

	private boolean kopieerOnderwijsproducten;

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
			ProductregelsKopierenJobRun run = new ProductregelsKopierenJobRun();
			run.setGestartDoor(getMedewerker());
			run.setRunStart(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting("Productregels kopiëren naar cohort " + doelcohort);
			run.save();

			Set<Opleiding> opleidingen = getValue(context, "opleidingen");
			List<Long> opleidingIds = new ArrayList<Long>();
			for (Opleiding curOpleiding : opleidingen)
				opleidingIds.add(curOpleiding.getId());

			kopieerProductregels(run, opleidingIds);

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting(run.getSamenvatting() + ": " + copyCount + " gekopiëerd, "
				+ skipCount + " overgeslagen.");
			run.update();
			run.commit();
		}
	}

	private void kopieerProductregels(ProductregelsKopierenJobRun run, List<Long> opleidingIds)
			throws InterruptedException
	{
		int count = 0;
		for (Long curOpleidingId : opleidingIds)
		{
			Opleiding curOpleiding =
				DataAccessRegistry.getHelper(OpleidingDataAccessHelper.class).get(Opleiding.class,
					curOpleidingId);
			List<Productregel> productregelsToCopy;
			if (productregels == null)
			{
				productregelsToCopy = getProductregels(curOpleiding, broncohort);
			}
			else
			{
				productregelsToCopy = productregels.getObject();
				productregels.detach();
			}

			if (productregelsToCopy != null && !productregelsToCopy.isEmpty())
			{
				setStatus("Kopiëren van productregels van " + curOpleiding.getNaam());
				kopieerProductregels(run, productregelsToCopy, curOpleiding);
			}
			setProgress(count, opleidingIds.size());
			count = flushAndClearHibernateAndIncCount(count);
		}
	}

	private List<Productregel> getProductregels(Opleiding opleiding, Cohort cohort)
	{
		return opleiding.getLokaleProductregels(cohort);
	}

	private void kopieerProductregels(ProductregelsKopierenJobRun run,
			List<Productregel> productregelsToCopy, Opleiding doelOpleiding)
	{
		HibernateObjectCopyManager copyManager =
			new HibernateObjectCopyManager(getProductregelClasses());
		for (Productregel bronProductregel : productregelsToCopy)
		{
			// Controleer of deze productregel al bestaat bij de doelopleiding.
			if (doelOpleiding.getProductregel(bronProductregel.getAfkorting(), doelcohort) == null)
			{
				Productregel copy = copyManager.copyObject(bronProductregel);
				copy.setCohort(doelcohort);
				copy.setOpleiding(doelOpleiding);
				copy.setVerbintenisgebied(doelOpleiding.getVerbintenisgebied());
				ModelFactory.getCompoundChangeRecordingModel(copy,
					new DefaultModelManager(getProductregelClasses())).saveObject();

				ProductregelsKopierenJobRunDetail detail =
					new ProductregelsKopierenJobRunDetail(run);
				detail.setUitkomst("Productregel gekopiëerd: " + copy.getAfkorting() + " - "
					+ copy.getNaam());
				detail.setProductregelId(copy.getId());
				copyCount++;
				detail.save();
			}
			else
			{
				ProductregelsKopierenJobRunDetail detail =
					new ProductregelsKopierenJobRunDetail(run);
				detail.setUitkomst("Productregel " + bronProductregel.getAfkorting() + " - "
					+ bronProductregel.getNaam()
					+ " overgeslagen omdat een productregel met deze afkorting al bestaat");
				skipCount++;
				detail.save();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Class< ? extends TransientIdObject>[] getProductregelClasses()
	{
		if (kopieerOnderwijsproducten)
			return new Class[] {ToegestaanOnderwijsproduct.class, Productregel.class};
		return new Class[] {Productregel.class};
	}

	private void jobRunRollBack()
	{
		int cnt = 0;
		for (JobRunDetail curDetail : jobToRollBack.getDetails())
		{
			if (curDetail instanceof ProductregelsKopierenJobRunDetail)
			{
				ProductregelsKopierenJobRunDetail curCopyDetail =
					(ProductregelsKopierenJobRunDetail) curDetail;
				if (curCopyDetail.getProductregelId() != null)
				{
					Productregel deleteProductregel =
						DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class).get(
							Productregel.class, curCopyDetail.getProductregelId());
					if (deleteProductregel != null)
					{

						VerbintenisZoekFilter vzf = new VerbintenisZoekFilter();
						vzf.setProductregel(deleteProductregel);
						vzf.setPeildatum(TimeUtil.getInstance().getMinDate());
						vzf
							.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
								new AlwaysGrantedSecurityCheck()));
						if (DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class)
							.getDeelnemerCount(vzf) == 0)
						{
							DefaultModelManager manager =
								new DefaultModelManager(ToegestaanOnderwijsproduct.class);
							for (ToegestaanOnderwijsproduct to : deleteProductregel
								.getToegestaneOnderwijsproducten())
							{
								ModelFactory.getCompoundChangeRecordingModel(to, manager)
									.deleteObject();
							}
							setStatus("Verwijderen van bestaande productregel "
								+ deleteProductregel.getAfkorting() + " - "
								+ deleteProductregel.getNaam());
							ModelFactory.getCompoundChangeRecordingModel(deleteProductregel,
								new DefaultModelManager(getProductregelClasses())).deleteObject();
							cnt++;
						}
						else
						{
							// er zijn deelnemers gekoppeld aan deze productregel, dus hij
							// kan niet verwijderd worden
							curDetail.setUitkomst(curDetail.getUitkomst()
								+ ". Niet teruggedraaid, deelnemers gekoppeld.");
						}
					}
				}
			}
		}
		jobToRollBack.setSamenvatting(jobToRollBack.getSamenvatting() + " " + cnt
			+ " teruggedraaid.");
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
			KopieerSettings settings = getValue(context, "kopieerSettings");
			broncohort = settings.getBronCohort();
			doelcohort = settings.getDoelCohort();
			kopieerOnderwijsproducten = settings.isKopieerOnderwijsproducten();
			List<Productregel> regels = getValue(context, "productregels");
			if (regels != null)
				productregels = ModelFactory.getListModel(regels);
		}
	}
}
