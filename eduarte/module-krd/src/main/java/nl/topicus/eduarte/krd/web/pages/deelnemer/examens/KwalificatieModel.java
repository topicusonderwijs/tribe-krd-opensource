package nl.topicus.eduarte.krd.web.pages.deelnemer.examens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteScheduler;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.Examenstatus;
import nl.topicus.eduarte.entities.examen.ExamenstatusOvergang;
import nl.topicus.eduarte.entities.examen.ToegestaneBeginstatus;
import nl.topicus.eduarte.entities.examen.ToegestaneExamenstatusOvergang;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.dao.helpers.ExamendeelnameDataAccessHelper;
import nl.topicus.eduarte.krd.jobs.CriteriumbankControleJob;
import nl.topicus.eduarte.krd.jobs.CriteriumbankControleJobDataMap;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.Examennummering.BijBestaandeNummers;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KwalificatieModel implements IModel<KwalificatieModel>
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(KwalificatieModel.class);

	private IModel<ToegestaneExamenstatusOvergang> toegestaneExamenstatusOvergang;

	private IModel<Examenstatus> geselecteerdeStatus;

	private Integer tijdvak;

	private Date datumUitslag;

	private Examennummering examennummering;

	private Class< ? extends Page> responsePageClass;

	private String infoString;

	public KwalificatieModel()
	{
	}

	@Override
	public KwalificatieModel getObject()
	{
		return this;
	}

	@Override
	public void setObject(KwalificatieModel object)
	{
		throw new UnsupportedOperationException();

	}

	@Override
	public void detach()
	{
		ComponentUtil.detachFields(this);
		ComponentUtil.detachQuietly(toegestaneExamenstatusOvergang);
		ComponentUtil.detachQuietly(geselecteerdeStatus);
	}

	public void setToegestaneExamenstatusOvergang(
			ToegestaneExamenstatusOvergang toegestaneExamenstatusOvergang)
	{
		this.toegestaneExamenstatusOvergang = ModelFactory.getModel(toegestaneExamenstatusOvergang);
	}

	public ToegestaneExamenstatusOvergang getToegestaneExamenstatusOvergang()
	{
		return toegestaneExamenstatusOvergang.getObject();
	}

	public Integer getTijdvak()
	{
		return tijdvak;
	}

	public void setTijdvak(Integer tijdvak)
	{
		this.tijdvak = tijdvak;
	}

	public void setExamennummering(Examennummering examennummering)
	{
		this.examennummering = examennummering;
	}

	public Examennummering getExamennummering()
	{
		return examennummering;
	}

	public void voltooi(Collection<Verbintenis> selectie)
	{
		if (getToegestaneExamenstatusOvergang().getNaarExamenstatus() != null
			&& getToegestaneExamenstatusOvergang().getNaarExamenstatus().isCriteriumbankControle())
		{
			List<Long> examendeelnameIds = new ArrayList<Long>();
			List<Long> verbintenisIds = new ArrayList<Long>();
			for (Verbintenis verbintenis : selectie)
			{
				Examendeelname deelname = getExamendeelname(verbintenis);
				if (deelname.isSaved())
					examendeelnameIds.add(deelname.getId());
				else
				{
					examendeelnameIds.add(null);
					deelname.evict();
				}
				verbintenisIds.add(verbintenis.getId());
			}
			CriteriumbankControleJobDataMap datamap =
				new CriteriumbankControleJobDataMap(examendeelnameIds, verbintenisIds,
					getToegestaneExamenstatusOvergang().getId());
			startCriteriumbankControleJob(datamap);
			setResponsePageClass(CriteriumbankControleOverzichtPage.class);
		}
		else
		{
			for (Verbintenis verbintenis : selectie)
			{
				// haalt de examendeelname op, of maakt een nieuwe aan
				Examendeelname deelname = getExamendeelname(verbintenis);
				Examenstatus naarStatus;
				if (getToegestaneExamenstatusOvergang().isExamennummersToekennen())
					maakExamennummerAan(deelname);
				if (getToegestaneExamenstatusOvergang().isTijdvakAangeven())
					deelname.setTijdvak(getTijdvak());
				if (getToegestaneExamenstatusOvergang().isBepaaltDatumUitslag())
				{
					deelname.setDatumUitslag(getDatumUitslag());
					deelname.setExamenjaar(TimeUtil.getInstance().getYear(
						deelname.getDatumUitslag()));
				}

				if (getGeselecteerdeStatus() != null)
					naarStatus = getGeselecteerdeStatus();
				else
					naarStatus = getToegestaneExamenstatusOvergang().getNaarExamenstatus();
				if (naarStatus != null)
				{
					ExamenstatusOvergang statusOvergang = createExamenstatusOvergang(deelname);
					statusOvergang.setNaarStatus(naarStatus);
					statusOvergang.setDatumTijd(TimeUtil.getInstance().currentDateTime());
					deelname.setExamenstatus(naarStatus);
					deelname.pasAfnameContextenAan(naarStatus);
					deelname.setDatumLaatsteStatusovergang(statusOvergang.getDatumTijd());
					deelname.saveOrUpdate();
					statusOvergang.save();
				}
				else
				{
					deelname.saveOrUpdate();
				}
			}
			setResponsePageClass(DeelnemerKwalificatiePage.class);
			setInfoString(getToegestaneExamenstatusOvergang().getActie() + " uitgevoerd voor "
				+ selectie.size() + " deelnemers");
		}
	}

	protected ExamenstatusOvergang createExamenstatusOvergang(Examendeelname deelname)
	{
		ExamenstatusOvergang examenstatusOvergang = new ExamenstatusOvergang();
		examenstatusOvergang.setExamendeelname(deelname);
		examenstatusOvergang.setVanStatus(deelname.getExamenstatus());
		deelname.getStatusovergangen().add(examenstatusOvergang);
		return examenstatusOvergang;
	}

	protected void maakExamennummerAan(Examendeelname deelname)
	{
		if (deelname.getExamennummer() == null
			|| examennummering.getBijBestaandeNummers().equals(BijBestaandeNummers.Overschrijven))
		{
			deelname.setExamennummer(examennummering.getBeginnummer());
			deelname.setExamennummerPrefix(examennummering.getPrefix());
			examennummering.setBeginnummer(examennummering.getBeginnummer() + 1);
		}
	}

	protected Examendeelname getExamendeelname(Verbintenis verbintenis)
	{
		ExamendeelnameZoekFilter filter = new ExamendeelnameZoekFilter();
		List<Examenstatus> examenstatussen = new ArrayList<Examenstatus>();
		for (ToegestaneBeginstatus beginstatus : getToegestaneExamenstatusOvergang()
			.getToegestaneBeginstatussen())
			examenstatussen.add(beginstatus.getExamenstatus());
		filter.setExamenstatusList(examenstatussen);
		filter.setVerbintenis(verbintenis);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		ExamendeelnameDataAccessHelper helper =
			DataAccessRegistry.getHelper(ExamendeelnameDataAccessHelper.class);
		if (helper.list(filter).size() > 0)
			return helper.list(filter).get(0);
		return createExamendeelname(verbintenis);
	}

	private Examendeelname createExamendeelname(Verbintenis verbintenis)
	{
		Examendeelname deelname = new Examendeelname(verbintenis);
		if (getToegestaneExamenstatusOvergang().getExamenWorkflow().isHeeftTijdvakken())
		{
			deelname.setTijdvak(Integer.valueOf(1));
		}
		return deelname;
	}

	protected void startCriteriumbankControleJob(CriteriumbankControleJobDataMap datamap)
	{
		try
		{
			EduArteScheduler scheduler = EduArteApp.get().getEduarteScheduler();
			scheduler.triggerJob(CriteriumbankControleJob.class, datamap);
		}
		catch (SchedulerException e)
		{
			log.error(e.toString(), e);
		}
	}

	public void setResponsePageClass(Class< ? extends Page> responsePageClass)
	{
		this.responsePageClass = responsePageClass;
	}

	public Class< ? extends Page> getResponsePageClass()
	{
		return responsePageClass;
	}

	public Examenstatus getGeselecteerdeStatus()
	{
		if (geselecteerdeStatus == null)
			return null;
		return geselecteerdeStatus.getObject();
	}

	public void setGeselecteerdeStatus(Examenstatus geselecteerdeStatus)
	{
		this.geselecteerdeStatus = ModelFactory.getModel(geselecteerdeStatus);
	}

	public void setDatumUitslag(Date datumUitslag)
	{
		this.datumUitslag = datumUitslag;
	}

	public Date getDatumUitslag()
	{
		return datumUitslag;
	}

	public void setInfoString(String infoString)
	{
		this.infoString = infoString;
	}

	public String getInfoString()
	{
		return infoString;
	}
}