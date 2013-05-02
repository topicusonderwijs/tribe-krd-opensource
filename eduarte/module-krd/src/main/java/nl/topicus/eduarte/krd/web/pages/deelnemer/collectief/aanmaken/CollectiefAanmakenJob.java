package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.io.StringPrintWriter;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.settings.MutatieBlokkedatumSetting;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.CollectieveStatusovergangJobDataMap;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.VerbintenisCollectiefBlokkadedatumValidator;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.VerbintenisCollectiefValidator;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumValidatorMode;

import org.hibernate.FlushMode;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/*
 * @author idserda
 */
@JobInfo(name = CollectiefAanmakenJob.JOB_NAME)
@JobRunClass(CollectiefAanmakenJobRun.class)
public class CollectiefAanmakenJob extends EduArteJob
{
	public static final String JOB_NAME = "Collectief aanmaken";

	private CollectiefAanmakenJobRun run;

	private List<String> validationErrors = new ArrayList<String>();

	private List<Long> succesvol = new ArrayList<Long>();

	private List<Long> mislukt = new ArrayList<Long>();

	private Plaatsing nieuwePlaatsing;

	private Verbintenis nieuweVerbintenis;

	private Class< ? extends Entiteit> soort;

	private Boolean onderwijsproductAfnamesAanmaken;

	private Date verbintenisBlokkadedatum;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException,
			InterruptedException
	{
		setFlushMode(FlushMode.COMMIT);
		try
		{
			run = new CollectiefAanmakenJobRun();
			run.setGestartDoor(getMedewerker());
			run.setRunStart(TimeUtil.getInstance().currentDateTime());

			extractData(context);

			run.setSamenvatting(getRunSamenvatting());
			run.save();

			int totaal = 0;

			if (Plaatsing.class.equals(soort))
			{
				List<Verbintenis> selectie = getValue(context, "selectie");
				collectiefAanmakenPlaatsing(selectie);
				totaal = selectie.size();
			}
			else if (Verbintenis.class.equals(soort))
			{
				List<Deelnemer> selectie = getValue(context, "selectie");
				collectiefAanmakenVerbintenis(selectie);
				totaal = selectie.size();
			}

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting(run.getSamenvatting() + "Totaal: " + totaal + ", succesvol:  "
				+ succesvol.size() + ", mislukt: " + mislukt.size());
			run.update();
			run.commit();
			flushAndClearHibernate();
		}
		catch (InterruptedException e)
		{
			log.error("Job was interrupted: " + e.getMessage(), e);
			throw e;
		}
		catch (Exception e)
		{
			failJob(e);
			throw new JobExecutionException(e);
		}
	}

	private String getRunSamenvatting()
	{
		String ret = "Collectief aanmaken van nieuwe " + soort.getSimpleName() + ". ";

		if (Verbintenis.class.equals(soort))
		{
			if (nieuweVerbintenis.getOpleiding() != null)
				ret = ret + "Opleiding: " + nieuweVerbintenis.getOpleiding().toString() + ". ";
			ret = ret + "Status: " + nieuweVerbintenis.getStatus() + ". ";
		}
		return ret;
	}

	private void collectiefAanmakenVerbintenis(List<Deelnemer> selectie)
			throws InterruptedException
	{
		int count = 0;
		for (Deelnemer deelnemer : selectie)
		{
			if (aanmakenVerbintenisMogelijk(deelnemer))
			{
				maakVerbintenisAan(deelnemer);
				succesvol.add(deelnemer.getId());

				createJobRunDetail("Verbintenis aangemaakt voor deelnemer " + deelnemer.toString());
			}
			else
			{
				mislukt.add(deelnemer.getId());
			}
			setProgress(count, selectie.size());
			count++;

			flushHibernate();
		}
	}

	private void maakVerbintenisAan(Deelnemer deelnemer)
	{
		Verbintenis verbintenis = dupliceerVeldenNieuweVerbintenis(deelnemer);
		verbintenis.saveOrUpdate();

		deelnemer.getVerbintenissen().add(verbintenis);
		deelnemer.saveOrUpdate();

		maakNieuwePlaatsingAan(verbintenis);

		if (onderwijsproductAfnamesAanmaken)
			maakProductAfnamesAan(verbintenis);
	}

	private void maakNieuwePlaatsingAan(Verbintenis verbintenis)
	{
		Plaatsing plaatsing = verbintenis.nieuwePlaatsing();
		verbintenis.saveOrUpdate();

		dupliceerVeldenNaarNieuwePlaatsing(plaatsing);

		plaatsing.save();
	}

	private void dupliceerVeldenNaarNieuwePlaatsing(Plaatsing plaatsing)
	{
		plaatsing.setLeerjaar(getPlaatsingUitNieuweVerbintenis().getLeerjaar());
		plaatsing.setJarenPraktijkonderwijs(getPlaatsingUitNieuweVerbintenis()
			.getJarenPraktijkonderwijs());
		plaatsing.setGroep(getPlaatsingUitNieuweVerbintenis().getGroep());
	}

	private void maakProductAfnamesAan(Verbintenis verbintenis)
	{
		verbintenis.maakDefaultProductregelKeuzes();
	}

	private boolean aanmakenVerbintenisMogelijk(Deelnemer deelnemer)
	{
		Verbintenis verbintenis = dupliceerVeldenNieuweVerbintenis(deelnemer);
		Plaatsing plaatsing = verbintenis.nieuwePlaatsing();
		dupliceerVeldenNaarNieuwePlaatsing(plaatsing);

		VerbintenisCollectiefValidator validator =
			new VerbintenisCollectiefValidator(verbintenis, verbintenis.getStatus());

		addErrors(validator.validate());

		if (getBlokkadedatum() != null)
		{
			VerbintenisCollectiefBlokkadedatumValidator blokkadedatumValidator =
				new VerbintenisCollectiefBlokkadedatumValidator(verbintenis, getBlokkadedatum(),
					BlokkadedatumValidatorMode.Aanmaken);
			addErrors(blokkadedatumValidator.validate());
		}

		if (!validationErrors.isEmpty())
		{
			createJobRunDetailMetErrors();
			return false;
		}
		else
		{
			return true;
		}
	}

	private Verbintenis dupliceerVeldenNieuweVerbintenis(Deelnemer deelnemer)
	{
		// Op deze manier doen omdat Verbintenis geen setter heeft voor Deelnemer..
		Verbintenis verbintenis = new Verbintenis(deelnemer);
		verbintenis.setRelevanteVooropleiding(deelnemer.getHoogsteRelevanteVooropleiding());
		verbintenis.setOvereenkomstnummer(DataAccessRegistry.getHelper(
			NummerGeneratorDataAccessHelper.class).newOvereenkomstnummer());

		verbintenis.setStatus(nieuweVerbintenis.getStatus());
		verbintenis.setOpleiding(nieuweVerbintenis.getOpleiding());

		verbintenis.setIntensiteit(nieuweVerbintenis.getIntensiteit());
		verbintenis.setContacturenPerWeek(nieuweVerbintenis.getContacturenPerWeek());

		verbintenis.setOrganisatieEenheid(nieuweVerbintenis.getOrganisatieEenheid());
		verbintenis.setLocatie(nieuweVerbintenis.getLocatie());

		verbintenis.setBegindatum(nieuweVerbintenis.getBegindatum());
		verbintenis.setGeplandeEinddatum(nieuweVerbintenis.getGeplandeEinddatum());

		verbintenis.setCohort(nieuweVerbintenis.getCohort());
		verbintenis.setContracten(nieuweVerbintenis.getContracten());

		verbintenis.setIndicatieGehandicapt(nieuweVerbintenis.getIndicatieGehandicapt());

		// Velden voor WI taxonomie
		verbintenis.setDatumAkkoord(nieuweVerbintenis.getDatumAkkoord());
		verbintenis.setDatumEersteActiviteit(nieuweVerbintenis.getDatumEersteActiviteit());
		verbintenis.setRedenInburgering(nieuweVerbintenis.getRedenInburgering());
		verbintenis.setProfielInburgering(nieuweVerbintenis.getProfielInburgering());
		verbintenis.setLeerprofiel(nieuweVerbintenis.getLeerprofiel());
		verbintenis.setDeelcursus(nieuweVerbintenis.getDeelcursus());
		verbintenis.setSoortPraktijkexamen(nieuweVerbintenis.getSoortPraktijkexamen());
		verbintenis.setBeginNiveauSchriftelijkeVaardigheden(nieuweVerbintenis
			.getBeginNiveauSchriftelijkeVaardigheden());
		verbintenis.setEindNiveauSchriftelijkeVaardigheden(nieuweVerbintenis
			.getEindNiveauSchriftelijkeVaardigheden());

		return verbintenis;
	}

	private void collectiefAanmakenPlaatsing(List<Verbintenis> selectie)
			throws InterruptedException
	{
		int count = 0;
		for (Verbintenis verbintenis : selectie)
		{
			if (aanmakenPlaatsingMogelijk(verbintenis))
			{
				maakPlaatsingAan(verbintenis);
				succesvol.add(verbintenis.getId());
				createJobRunDetail(createDeelnemerOmschrijving(verbintenis)
					+ " Aanmaken plaatsing geslaagd voor verbintenis "
					+ verbintenis.getOmschrijving());
			}
			else
			{
				mislukt.add(verbintenis.getId());
			}
			setProgress(count, selectie.size());
			count++;

			flushHibernate();
		}
	}

	private void maakPlaatsingAan(Verbintenis verbintenis)
	{
		HibernateObjectCopyManager manager = new HibernateObjectCopyManager(Plaatsing.class);
		Plaatsing plaatsingCopy = manager.copyObject(nieuwePlaatsing);

		plaatsingCopy.setLwoo(verbintenis.getDeelnemer().getLWOOLaatstePlaatsing());

		voegPlaatsingToe(verbintenis, plaatsingCopy);
	}

	private void voegPlaatsingToe(Verbintenis verbintenis, Plaatsing plaatsing)
	{
		NieuwePlaatsingUtil util = new NieuwePlaatsingUtil(verbintenis);
		util.addNieuwePlaatsing(plaatsing);
	}

	private boolean aanmakenPlaatsingMogelijk(Verbintenis verbintenis)
	{
		nieuwePlaatsing.setVerbintenis(verbintenis);
		nieuwePlaatsing.setDeelnemer(verbintenis.getDeelnemer());

		checkVerplichteVeldenPlaatsing(nieuwePlaatsing);

		nieuwePlaatsing.setVerbintenis(null);
		nieuwePlaatsing.setDeelnemer(null);

		if (!validationErrors.isEmpty())
		{
			createJobRunDetailMetErrors();
			return false;
		}
		else
		{
			return true;
		}
	}

	private void checkVerplichteVeldenPlaatsing(Plaatsing plaatsing)
	{
		addErrors(new CollectiefAanmakenValidator(plaatsing).validatePlaatsing());
	}

	private Plaatsing getPlaatsingUitNieuweVerbintenis()
	{
		return nieuweVerbintenis.getPlaatsingen().get(0);
	}

	private Date getBlokkadedatum()
	{
		if (verbintenisBlokkadedatum == null)
		{
			MutatieBlokkedatumSetting setting =
				DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
					MutatieBlokkedatumSetting.class);

			if (setting != null && setting.getValue() != null)
				verbintenisBlokkadedatum = setting.getValue().getBlokkadedatumVerbintenis();
		}
		return verbintenisBlokkadedatum;
	}

	private void extractData(JobExecutionContext context)
	{
		CollectiefAanmakenModel aanmakenModel = getValue(context, "collectiefAanmakenModel");

		nieuwePlaatsing = aanmakenModel.getNieuwePlaatsing();
		nieuweVerbintenis = aanmakenModel.getNieuweVerbintenis();
		soort = aanmakenModel.getSoort();
		onderwijsproductAfnamesAanmaken = aanmakenModel.getOnderwijsproductAfnamesAanmaken();
	}

	private void createJobRunDetail(String error)
	{
		CollectiefAanmakenJobRunDetail detail = new CollectiefAanmakenJobRunDetail(run);
		detail.setUitkomst(error);
		detail.save();
	}

	@SuppressWarnings("unused")
	private CollectieveStatusovergangJobDataMap<VerbintenisStatus> getDataMap()
	{
		return null;
	}

	private void createJobRunDetailMetErrors()
	{
		for (String error : validationErrors)
		{
			createJobRunDetail(error);
		}
		validationErrors.clear();
	}

	private void addErrors(List<String> newValidationErrors)
	{
		validationErrors.addAll(newValidationErrors);
	}

	private String createDeelnemerOmschrijving(Verbintenis verbintenis)
	{
		Deelnemer deelnemer = verbintenis.getDeelnemer();

		String msg =
			"Deelnemer: " + deelnemer.getPersoon().getAchternaam() + ", "
				+ deelnemer.getPersoon().getVoornamen() + " (" + deelnemer.getDeelnemernummer()
				+ "). ";
		return msg;
	}

	private void setFlushMode(FlushMode flushmode)
	{
		SessionDataAccessHelper sessionDAH =
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class);
		sessionDAH.getHibernateSessionProvider().getSession().setFlushMode(flushmode);
	}

	void failJob(Exception e)
	{
		log.error("Collectief aanmaken mislukt");

		run.setSamenvatting("Collectief aanmaken mislukt, klik voor meer informatie");
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		setStatus("Collectief aanmaken mislukt");

		StringPrintWriter spw = new StringPrintWriter();
		e.printStackTrace(spw);
		run.error("Collectief aanmaken is mislukt door een onverwachte fout:\n" + spw.getString());

		run.saveOrUpdate();
		run.commit();
	}

}
