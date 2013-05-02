package nl.topicus.eduarte.resultaten.jobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.TransientIdObject;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.quartz.JobSegment;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.CohortDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.resultaatstructuur.*;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.jobs.OverschrijfActie;
import nl.topicus.eduarte.resultaten.entities.ResultaatstructurenKopierenJobRun;
import nl.topicus.eduarte.resultaten.entities.ResultaatstructurenKopierenJobRunDetail;
import nl.topicus.eduarte.resultaten.web.components.security.ResultaatstructuurSecurityCheck;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;
import org.quartz.JobExecutionContext;

@JobInfo(name = ResultaatstructurenKopierenJob.JOB_NAME)
@JobRunClass(ResultaatstructurenKopierenJobRun.class)
public class ResultaatstructurenKopierenJob extends EduArteJob
{
	public static final String JOB_GROUP = "KRD";

	public static final String JOB_NAME = "Resultaatstructuren kopiëren";

	private class KopieerActie
	{
		private Type type;

		private String code;

		private Long bronCohortId;

		private Long bronProductId;

		private Long doelCohortId;

		private Long doelProductId;

		private String structuurKopierenError;

		private String verwijzingenKopierenError;

		public KopieerActie(Resultaatstructuur bronStructuur, Cohort doelCohort,
				Onderwijsproduct doelProduct)
		{
			type = bronStructuur.getType();
			code = bronStructuur.getCode();
			bronCohortId = bronStructuur.getCohort().getId();
			bronProductId = bronStructuur.getOnderwijsproduct().getId();
			doelCohortId = doelCohort.getId();
			doelProductId = doelProduct.getId();
		}

		public Type getType()
		{
			return type;
		}

		public String getCode()
		{
			return code;
		}

		public Cohort getBronCohort()
		{
			return DataAccessRegistry.getHelper(CohortDataAccessHelper.class).get(Cohort.class,
				bronCohortId);
		}

		public Onderwijsproduct getBronProduct()
		{
			return DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class).get(
				Onderwijsproduct.class, bronProductId);
		}

		public Cohort getDoelCohort()
		{
			return DataAccessRegistry.getHelper(CohortDataAccessHelper.class).get(Cohort.class,
				doelCohortId);
		}

		public Onderwijsproduct getDoelProduct()
		{
			return DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class).get(
				Onderwijsproduct.class, doelProductId);
		}

		public boolean isCohortVariabel()
		{
			return !getBronCohort().equals(getDoelCohort());
		}

		public boolean isProductVariabel()
		{
			return !getBronProduct().equals(getDoelProduct());
		}

		public boolean isStructuurKopierenMogelijk()
		{
			return structuurKopierenError == null;
		}

		public String getStructuurKopierenError()
		{
			return structuurKopierenError;
		}

		public void setStructuurKopierenError(String structuurKopierenError)
		{
			this.structuurKopierenError = structuurKopierenError;
			setVerwijzingenKopierenError("Toetsverwijzingen niet gekopiëerd door een fout bij de structuur: "
				+ structuurKopierenError);
		}

		public boolean isVerwijzingenKopierenMogelijk()
		{
			return verwijzingenKopierenError == null;
		}

		public String getVerwijzingenKopierenError()
		{
			return verwijzingenKopierenError;
		}

		public void setVerwijzingenKopierenError(String verwijzingenKopierenError)
		{
			this.verwijzingenKopierenError = verwijzingenKopierenError;
		}

		@Override
		public String toString()
		{
			return getCode() + "/" + getBronProduct().getCode() + "(" + getBronCohort() + ") naar "
				+ getDoelProduct().getCode() + "(" + getDoelCohort() + ")";
		}
	}

	private enum KopieerOnderdeel implements JobSegment
	{
		COLLECT(20),
		COPY_STRUCTUUR(50),
		COPY_VERWIJZINGEN(30);

		private int pct;

		KopieerOnderdeel(int pct)
		{
			this.pct = pct;
		}

		@Override
		public int getPercent()
		{
			return pct;
		}
	}

	private ResultaatstructurenKopierenJobRun jobToRollBack;

	private int copyStructuurCount = 0;

	private int skipStructuurCount = 0;

	private int overwriteStructuurCount = 0;

	private int copyVerwijzingCount = 0;

	private int skipVerwijzingCount = 0;

	private int overwriteVerwijzingCount = 0;

	private ResultaatstructuurKopieerSettings settings;

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
			ResultaatstructurenKopierenJobRun run = new ResultaatstructurenKopierenJobRun();
			run.setGestartDoor(getMedewerker());
			run.setRunStart(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting("Resultaatstructuren kopiëren");
			run.save();

			List<KopieerActie> acties = collectActies(run, context);
			if (settings.isStructurenKopieren())
				kopieerStructuren(run, acties);
			flushHibernate();
			if (settings.isVerwijzingenKopieren())
				kopieerVerwijzingen(run, acties);

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting(run.getSamenvatting() + ": Structuren: " + copyStructuurCount
				+ " gekopiëerd, " + overwriteStructuurCount + " overschreven, "
				+ skipStructuurCount + " overgeslagen. Verwijzingen: " + copyVerwijzingCount
				+ " gekopiëerd, " + overwriteVerwijzingCount + " overschreven, "
				+ skipVerwijzingCount + " overgeslagen.");
			run.update();
			run.commit();
		}
	}

	private List<KopieerActie> collectActies(ResultaatstructurenKopierenJobRun run,
			JobExecutionContext context) throws InterruptedException
	{
		int count = 0;
		List<KopieerActie> ret = new ArrayList<KopieerActie>();
		Cohort bronCohort = settings.getBronCohort();
		Cohort doelCohort = settings.getDoelCohort();
		Resultaatstructuur singleBronStructuur = getValue(context, "bronStructuur");
		Set<Onderwijsproduct> productenList = getValue(context, "onderwijsproducten");
		for (Onderwijsproduct curProduct : productenList)
		{
			setStatus("Verzamelen van informatie over " + curProduct.getOmschrijving());
			List<Resultaatstructuur> bronStructuren =
				singleBronStructuur == null ? findBronStructuren(bronCohort, curProduct, settings
					.getType(), settings.getCode()) : Arrays.asList(singleBronStructuur);
			for (Resultaatstructuur curBronStructuur : bronStructuren)
			{
				ret.add(prepareForCopy(run, curBronStructuur, doelCohort, curProduct));
			}
			setProgress(count, productenList.size(), KopieerOnderdeel.COLLECT);
			count = flushAndClearHibernateAndIncCount(count);
		}
		return ret;
	}

	private List<Resultaatstructuur> findBronStructuren(Cohort bronCohort,
			Onderwijsproduct bronProduct, Type type, String code)
	{
		return DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class)
			.getResultaatstructuren(bronProduct, bronCohort, type, code);
	}

	private void kopieerStructuren(ResultaatstructurenKopierenJobRun run, List<KopieerActie> acties)
			throws InterruptedException
	{
		int count = 0;
		for (KopieerActie curActie : acties)
		{
			setStatus("Kopiëren van structuur van " + curActie);
			if (curActie.isStructuurKopierenMogelijk())
				kopieerResultaatstructuur(run, curActie);
			else
			{
				run.error(curActie.getStructuurKopierenError());
				skipStructuurCount++;
			}
			setProgress(count, acties.size(), KopieerOnderdeel.COPY_STRUCTUUR);
			count = flushAndClearHibernateAndIncCount(count);
		}
	}

	private void kopieerVerwijzingen(ResultaatstructurenKopierenJobRun run,
			List<KopieerActie> acties) throws InterruptedException
	{
		int count = 0;
		for (KopieerActie curActie : acties)
		{
			setStatus("Kopiëren van verwijzingen van " + curActie);
			if (curActie.isVerwijzingenKopierenMogelijk())
				kopieerVerwijzingen(run, curActie);
			else
			{
				run.error(curActie.getVerwijzingenKopierenError());
				skipVerwijzingCount++;
			}
			setProgress(count, acties.size(), KopieerOnderdeel.COPY_VERWIJZINGEN);
			count = flushAndClearHibernateAndIncCount(count);
		}
	}

	private KopieerActie prepareForCopy(ResultaatstructurenKopierenJobRun run,
			Resultaatstructuur bronStructuur, Cohort doelCohort, Onderwijsproduct doelProduct)
	{
		KopieerActie ret = new KopieerActie(bronStructuur, doelCohort, doelProduct);
		if (doelProduct.getEinddatum() != null
			&& !doelProduct.getEinddatum().after(doelCohort.getBegindatum()))
		{
			ret.setStructuurKopierenError(doelProduct.getCode() + " ("
				+ doelProduct.getOmschrijving() + ") is niet geldig in cohort "
				+ doelCohort.getNaam());
			return ret;
		}

		Resultaatstructuur doelStructuur =
			findResultaatstructuur(doelProduct, doelCohort, bronStructuur.getType(), bronStructuur
				.getCode());
		if (doelStructuur == null)
		{
			if (!settings.isStructurenKopieren())
				ret.setVerwijzingenKopierenError("Er bestaat geen resultaatstructuur voor "
					+ doelProduct.getCode() + " (" + doelProduct.getOmschrijving() + ")");
			return ret;
		}

		if (doelStructuur.equals(bronStructuur))
		{
			ret.setStructuurKopierenError("Resultaatstructuur voor " + doelProduct.getCode() + " ("
				+ doelProduct.getOmschrijving() + ") kan zichzelf niet overschrijven.");
			return ret;
		}

		if (settings.isStructurenKopieren()
			&& settings.getActieBijBestaandeStructuur().equals(OverschrijfActie.Overslaan))
		{
			ret.setStructuurKopierenError("Resultaatstructuur voor " + doelProduct.getCode() + " ("
				+ doelProduct.getOmschrijving()
				+ ") niet gekopiëerd vanwege een bestaande resultaatstructuur.");
			return ret;
		}

		if (!settings.isStructurenKopieren() && settings.isVerwijzingenKopieren()
			&& settings.getActieBijBestaandeVerwijzingen().equals(OverschrijfActie.Overslaan)
			&& heeftVerwijzingen(doelStructuur))
		{
			ret.setVerwijzingenKopierenError("Toetsverwijzingen voor de structuur voor "
				+ doelProduct.getCode() + " (" + doelProduct.getOmschrijving()
				+ ") niet gekopiëerd vanwege bestaande toetsverwijzingen.");
		}

		if (DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class).heeftResultaten(
			doelStructuur))
		{
			ret
				.setStructuurKopierenError("Resultaatstructuur voor " + doelProduct.getCode()
					+ " (" + doelProduct.getOmschrijving()
					+ ") niet verwijderd vanwege bestaande resultaten.");
			return ret;
		}

		if (settings.isStructurenKopieren())
		{
			setStatus("Verwijderen van bestaande structuur van " + doelProduct.getOmschrijving());
			ModelFactory.getCompoundChangeRecordingModel(doelStructuur,
				new DefaultModelManager(getResultaatstructuurClassesMetVerwijzing())).deleteObject(
				new VerwijderToetsVerwijzingCallback());

			run.info("Bestaande resultaatstructuur verwijderd voor " + doelProduct.getCode() + " ("
				+ doelProduct.getOmschrijving() + ")");
			overwriteStructuurCount++;
		}
		else if (settings.isVerwijzingenKopieren() && ret.isVerwijzingenKopierenMogelijk()
			&& heeftVerwijzingen(doelStructuur))
		{
			for (Toets curToets : doelStructuur.getToetsen())
			{
				for (ToetsVerwijzing curVerwijzing : curToets.getUitgaandeVerwijzingen())
					curVerwijzing.delete();
				curToets.getUitgaandeVerwijzingen().clear();
			}
			run.info("Bestaande toetsverwijzingen verwijderd voor de structuur voor "
				+ doelProduct.getCode() + " (" + doelProduct.getOmschrijving() + ")");
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

	private void kopieerResultaatstructuur(ResultaatstructurenKopierenJobRun run, KopieerActie actie)
	{
		Resultaatstructuur resultaatstructuur =
			findResultaatstructuur(actie.getBronProduct(), actie.getBronCohort(), actie.getType(),
				actie.getCode());
		HibernateObjectCopyManager copyManager =
			new HibernateObjectCopyManager(getResultaatstructuurClasses());
		Resultaatstructuur copy = copyManager.copyObject(resultaatstructuur);
		copy.setCohort(actie.getDoelCohort());
		copy.setOnderwijsproduct(actie.getDoelProduct());

		if (isAuthorized(copy))
		{
			ModelFactory.getCompoundChangeRecordingModel(copy,
				new DefaultModelManager(getResultaatstructuurClasses())).saveObject();

			ResultaatstructurenKopierenJobRunDetail detail =
				new ResultaatstructurenKopierenJobRunDetail(run);
			detail.setUitkomst("Resultaatstructuur (" + copy.getType() + "/" + copy.getCode()
				+ ") gekopiëerd van " + actie);
			detail.setResultaatstructuurId(copy.getId());
			copyStructuurCount++;
			detail.save();
		}
		else
		{
			actie.setStructuurKopierenError("Onvoldoende rechten om resultaatstructuur ("
				+ copy.getType() + "/" + copy.getCode() + ") van " + actie + " to kopiëren");
			run.error(actie.getStructuurKopierenError());
			skipStructuurCount++;
		}
	}

	private void kopieerVerwijzingen(ResultaatstructurenKopierenJobRun run, KopieerActie actie)
	{
		Resultaatstructuur bronStructuur =
			findResultaatstructuur(actie.getBronProduct(), actie.getBronCohort(), actie.getType(),
				actie.getCode());
		Resultaatstructuur verwijzingBronStructuur =
			findResultaatstructuur(actie.getDoelProduct(), actie.getDoelCohort(), actie.getType(),
				actie.getCode());
		if (!isAuthorized(verwijzingBronStructuur))
		{
			run.error("Onvoldoende rechten om verwijzingen van resultaatstructuur ("
				+ verwijzingBronStructuur.getType() + "/" + verwijzingBronStructuur.getCode()
				+ ") van " + actie + " to kopiëren");
			skipVerwijzingCount++;
			return;
		}

		for (Toets curToets : bronStructuur.getToetsen())
		{
			for (ToetsVerwijzing curVerwijzing : curToets.getUitgaandeVerwijzingen())
			{
				try
				{
					Resultaatstructuur verwijzingDoelStructuur =
						findStructuurVoorVerwijzing(curVerwijzing, actie);
					Toets verwijzingBronToets =
						findToetsVoorVerwijzing(curVerwijzing.getLezenUit(),
							verwijzingBronStructuur);
					Toets verwijzingDoelToets =
						findToetsVoorVerwijzing(curVerwijzing.getSchrijvenIn(),
							verwijzingDoelStructuur);

					ToetsVerwijzing nieuweVerwijzing = new ToetsVerwijzing();
					nieuweVerwijzing.setLezenUit(verwijzingBronToets);
					nieuweVerwijzing.setSchrijvenIn(verwijzingDoelToets);
					nieuweVerwijzing.save();

					ResultaatstructurenKopierenJobRunDetail detail =
						new ResultaatstructurenKopierenJobRunDetail(run);
					detail.setUitkomst("Toets verwijzing " + nieuweVerwijzing + " gekopiëerd van "
						+ actie);
					detail.setToetsVerwijzingId(nieuweVerwijzing.getId());
					copyVerwijzingCount++;
					detail.save();
				}
				catch (IllegalArgumentException e)
				{
					run.error(e.getMessage());
					skipVerwijzingCount++;
				}
			}
		}
	}

	private boolean isAuthorized(final Resultaatstructuur copy)
	{
		ResultaatstructuurSecurityCheck check =
			new ResultaatstructuurSecurityCheck(new DataSecurityCheck(copy.getType()
				.getSecurityId()), copy)
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
					return copy;
				}
			};
		check.setSecurityIdPostfix(Resultaatstructuur.KOPIEREN);
		check.setEditTarget(true);
		boolean authorized = check.isActionAuthorized(Enable.class);
		return authorized;
	}

	private Resultaatstructuur findStructuurVoorVerwijzing(ToetsVerwijzing verwijzing,
			KopieerActie actie)
	{
		Resultaatstructuur origDoelStructuur = verwijzing.getSchrijvenIn().getResultaatstructuur();
		Cohort verwijzingDoelCohort;
		if (actie.isCohortVariabel())
		{
			if (!verwijzing.getLezenUit().getResultaatstructuur().getCohort().equals(
				origDoelStructuur.getCohort()))
			{
				throw new IllegalArgumentException("Kan alleen toetsverwijzingen kopiëren "
					+ "die binnen een cohort blijven bij kopiëren over verschillende cohorten: "
					+ verwijzing);
			}
			verwijzingDoelCohort = actie.getDoelCohort();
		}
		else
			verwijzingDoelCohort = origDoelStructuur.getCohort();

		Onderwijsproduct verwijzingDoelProduct;
		if (actie.isProductVariabel())
		{
			if (!verwijzing.getLezenUit().getResultaatstructuur().getOnderwijsproduct().equals(
				origDoelStructuur.getOnderwijsproduct()))
			{
				throw new IllegalArgumentException("Kan alleen toetsverwijzingen kopiëren die "
					+ "binnen een onderwijsproduct blijven bij kopiëren over verschillende "
					+ "onderwijsproduct: " + verwijzing);
			}
			verwijzingDoelProduct = actie.getDoelProduct();
		}
		else
			verwijzingDoelProduct = origDoelStructuur.getOnderwijsproduct();

		Resultaatstructuur ret =
			findResultaatstructuur(verwijzingDoelProduct, verwijzingDoelCohort, origDoelStructuur
				.getType(), origDoelStructuur.getCode());
		if (ret == null)
		{
			throw new IllegalArgumentException(
				"De resultaatstructuur voor het doel van de verwijzing '(" + verwijzingDoelCohort
					+ ") " + verwijzingDoelProduct.getCode() + "' bestaat niet: " + verwijzing);
		}
		return ret;
	}

	private Toets findToetsVoorVerwijzing(Toets toets, Resultaatstructuur structuur)
	{
		List<String> codes = new ArrayList<String>();
		Toets checkToets = toets;
		while (!checkToets.isEindresultaat())
		{
			codes.add(0, checkToets.getCode());
			checkToets = checkToets.getParent();
		}

		String codePath = structuur.getEindresultaat().getCode() + "/";
		Toets searchToets = structuur.getEindresultaat();
		for (String curCode : codes)
		{
			codePath = codePath + "/" + curCode;
			searchToets = searchToets.getChild(curCode);
			if (searchToets == null)
			{
				throw new IllegalArgumentException("Er is geen toets op het pad '" + codePath
					+ "' binnen " + structuur);
			}
		}
		return searchToets;
	}

	@SuppressWarnings("unchecked")
	private Class< ? extends TransientIdObject>[] getResultaatstructuurClasses()
	{
		return new Class[] {DeelnemerResultaatVersie.class, Scoreschaalwaarde.class,
			PersoonlijkeToetscode.class, Toets.class, ResultaatstructuurDeelnemer.class,
			ResultaatstructuurMedewerker.class, Resultaatstructuur.class};
	}

	@SuppressWarnings("unchecked")
	private Class< ? extends TransientIdObject>[] getResultaatstructuurClassesMetVerwijzing()
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
			if (curDetail instanceof ResultaatstructurenKopierenJobRunDetail)
			{
				ResultaatstructurenKopierenJobRunDetail curCopyDetail =
					(ResultaatstructurenKopierenJobRunDetail) curDetail;
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
			if (curDetail instanceof ResultaatstructurenKopierenJobRunDetail)
			{
				ResultaatstructurenKopierenJobRunDetail curCopyDetail =
					(ResultaatstructurenKopierenJobRunDetail) curDetail;
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
			settings = getValue(context, "kopieerSettings");
		}
	}
}
